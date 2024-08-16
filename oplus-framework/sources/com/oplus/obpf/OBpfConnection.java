package com.oplus.obpf;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Build;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import com.oplus.obpf.IOplusBpfService;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import libcore.io.IoUtils;
import libcore.io.Memory;

/* loaded from: classes.dex */
public class OBpfConnection {
    private static final short BFP_DATA_BYTES_TYPE = 1;
    private static final short BFP_DATA_KSTACK_TYPE = 3;
    private static final short BFP_DATA_STRING_TYPE = 2;
    private static final short BFP_DATA_USTACK_TYPE = 4;
    private static final int BPF_BIN_DATA_MAGIC_NUM = 1035;
    private static final int BPF_COMMAND_HEADER_LEN = 12;
    private static final int BPF_DATA_HEADER_LEN = 12;
    private static final int BPF_DATA_MAX_BUFFER_SIZE = 4096;
    private static final String BPF_DO_DUMP = "doDump -a";
    private static final String BPF_MANAGER_MAGIC_NUM = "obpf";
    public static final int CUSTOMIZE_THREAD_FD_TYPE = 2;
    public static final int DEFAULT_THREAD_FD_TYPE = 1;
    private static final String DEFAULT_THREAD_NAME = "bpf:default";
    private static final int DELAY_CHECK_TIME = 1000;
    public static final int DUMP_BPF_CONNECT_NAME = 2;
    public static final int DUMP_BPF_MANAGER = 1;
    public static final short MANAGER_DUMP_COMMAND = 2;
    public static final short MANAGER_LOAD_BPF_COMMAND = 1;
    public static final short MANAGER_UNKNOWN_COMMAND = 0;
    public static final short MANAGER_USER_DATA_COMMAND = 3;
    private static final int MAX_COMMAND_LEN = 1024;
    public static final int NO_THREAD_FD_TYPE = 0;
    public static final int OBPF_CONNECT_ERROR = -1;
    public static final int OBPF_CONNECT_TRY_AGAIN = -11;
    private static final int OBPF_MANAGER_DEFAULT_MAX_START_COUNT = 5;
    private static final String OPLUS_BPF_ENABLE_PROP = "sys.oplus.cvt.enable";
    private static final String OPLUS_BPF_MANAGER_SOCKET = "/dev/socket/opluscvtmanager";
    private static final int OPLUS_BPF_MIN_INITIAL_SDK = 33;
    private static final int OPLUS_BPF_MIN_SDK = 34;
    private static final String SERVICE_NAME = "opluscvt";
    private static final String SOCKET_NAME = "opluscvtmanager";
    private static HandlerThread sBpfThread;
    private final String mBpfConnectName;
    private InputStream mBpfInputStream;
    private OutputStream mBpfOutputStream;
    private LocalSocket mBpfSocket;
    private int mBpfSocketFD;
    private final Object mBpfSocketLock;
    private final ByteBuffer mInputBuf;
    private final ByteBufferInputStream mInputData;
    private boolean mIsDefaultQueue;
    private final BpfConnectionListener mListener;
    private Looper mLooper;
    private MessageQueue mMsgQueue;
    private final Object mQuitLock;
    private boolean mQuitting;
    private final Object mReplyBufLock;
    private static final SparseArray<String> DEFAULT_THREAD_FD_MAP = new SparseArray<>();
    private static final SparseArray<String> CUSTOMIZE_THREAD_FD_MAP = new SparseArray<>();
    private static final String TAG = "OCvtConnection";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final Object DEFAULT_THREAD_LOCK = new Object();
    private static volatile IOplusBpfService sBpfService = null;

    /* loaded from: classes.dex */
    public interface BpfConnectionListener {
        void onBpfDataArrived(int i, short s, byte[] bArr);

        void onDisconnect();
    }

    public OBpfConnection(BpfConnectionListener listener, Looper looper, String name) {
        ByteBuffer allocate = ByteBuffer.allocate(4096);
        this.mInputBuf = allocate;
        this.mInputData = new ByteBufferInputStream(new ByteArrayInputStream(allocate.array()));
        this.mBpfSocketLock = new Object();
        this.mReplyBufLock = new Object();
        this.mQuitLock = new Object();
        this.mLooper = null;
        this.mMsgQueue = null;
        this.mIsDefaultQueue = false;
        this.mBpfSocketFD = -1;
        this.mQuitting = false;
        this.mBpfSocket = null;
        this.mBpfOutputStream = null;
        this.mBpfInputStream = null;
        this.mLooper = looper;
        if (looper != null) {
            this.mMsgQueue = looper.getQueue();
        }
        this.mListener = listener;
        this.mBpfConnectName = "bpf:" + name;
        if (this.mMsgQueue == null) {
            this.mIsDefaultQueue = true;
        }
    }

    public static boolean isEnable() {
        if (!isLegalSdkAndBpfEnable()) {
            return false;
        }
        if (!isBpfSocketExist()) {
            Slog.w(TAG, "do not exist");
            return true;
        }
        return true;
    }

    public static boolean isStringType(short dataType) {
        return dataType == 2 || dataType == 3 || dataType == 4;
    }

    public int connect(String message, short command, int commandFlags) {
        if (!isLegalSdkAndBpfEnable()) {
            return -1;
        }
        if (!isBpfSocketExist()) {
            return -11;
        }
        synchronized (this.mBpfSocketLock) {
            if (ignoreConnect()) {
                return -1;
            }
            LocalSocket socket = openSocket();
            if (socket == null) {
                Slog.w(TAG, "connect socket == null");
                return -1;
            }
            try {
                OutputStream ostream = socket.getOutputStream();
                InputStream istream = socket.getInputStream();
                if (!execBpfCommand(ostream, message, command, commandFlags)) {
                    IoUtils.closeQuietly(socket);
                    return -1;
                }
                this.mBpfSocket = socket;
                this.mBpfOutputStream = ostream;
                this.mBpfInputStream = istream;
                this.mBpfSocketFD = socket.getFileDescriptor().getInt$();
                if (!initMsgQueue()) {
                    IoUtils.closeQuietly(socket);
                    this.mBpfSocket = null;
                    return -1;
                }
                this.mMsgQueue.addOnFileDescriptorEventListener(this.mBpfSocket.getFileDescriptor(), 5, new MessageQueue.OnFileDescriptorEventListener() { // from class: com.oplus.obpf.OBpfConnection$$ExternalSyntheticLambda0
                    @Override // android.os.MessageQueue.OnFileDescriptorEventListener
                    public final int onFileDescriptorEvents(FileDescriptor fileDescriptor, int i) {
                        int lambda$connect$0;
                        lambda$connect$0 = OBpfConnection.this.lambda$connect$0(fileDescriptor, i);
                        return lambda$connect$0;
                    }
                });
                this.mBpfSocketLock.notifyAll();
                return this.mBpfSocketFD;
            } catch (IOException ex) {
                IoUtils.closeQuietly(socket);
                Slog.w(TAG, "connect IOException ex = " + ex);
                return -1;
            }
        }
    }

    public void disconnect() {
        synchronized (this.mBpfSocketLock) {
            LocalSocket localSocket = this.mBpfSocket;
            if (localSocket != null) {
                MessageQueue messageQueue = this.mMsgQueue;
                if (messageQueue != null) {
                    messageQueue.removeOnFileDescriptorEventListener(localSocket.getFileDescriptor());
                }
                IoUtils.closeQuietly(this.mBpfSocket);
                this.mBpfSocket = null;
                BpfConnectionListener bpfConnectionListener = this.mListener;
                if (bpfConnectionListener != null) {
                    bpfConnectionListener.onDisconnect();
                }
            }
        }
        synchronized (DEFAULT_THREAD_LOCK) {
            int i = this.mBpfSocketFD;
            if (i >= 0) {
                if (this.mIsDefaultQueue) {
                    DEFAULT_THREAD_FD_MAP.remove(i);
                } else {
                    CUSTOMIZE_THREAD_FD_MAP.remove(i);
                }
                this.mBpfSocketFD = -1;
            }
        }
    }

    public void exit() {
        synchronized (this.mQuitLock) {
            if (this.mQuitting) {
                return;
            }
            this.mQuitting = true;
            disconnect();
            synchronized (DEFAULT_THREAD_LOCK) {
                if (this.mIsDefaultQueue) {
                    quitDefaultThread();
                }
            }
        }
    }

    private void quitDefaultThread() {
        if (sBpfThread != null && DEFAULT_THREAD_FD_MAP.size() <= 0) {
            this.mMsgQueue = null;
            sBpfThread.quitSafely();
            sBpfThread = null;
        }
    }

    public int doDump(int dumpType, int dumpArg) {
        switch (dumpType) {
            case 1:
                return dumpBpfManager();
            case 2:
                return dumpBpfConnectName(dumpArg);
            default:
                return -1;
        }
    }

    public void sendUserData(String message, int commandFlags) {
        OutputStream outputStream;
        synchronized (this.mBpfSocketLock) {
            if (this.mBpfSocket != null && (outputStream = this.mBpfOutputStream) != null) {
                execBpfCommand(outputStream, message, (short) 3, commandFlags);
            }
        }
    }

    private int dumpBpfManager() {
        if (sBpfService == null) {
            IBinder b = ServiceManager.getService(SERVICE_NAME);
            sBpfService = IOplusBpfService.Stub.asInterface(b);
        }
        if (sBpfService == null) {
            Slog.w(TAG, "doDump Service == null");
            return -1;
        }
        try {
            sBpfService.doDump("-a".toCharArray());
            return 0;
        } catch (RemoteException e) {
            Slog.w(TAG, "doDump RemoteException e = " + e);
            return -1;
        }
    }

    private int dumpBpfConnectName(int socketFD) {
        int fdType = 0;
        synchronized (DEFAULT_THREAD_LOCK) {
            int i = 0;
            while (true) {
                SparseArray<String> sparseArray = DEFAULT_THREAD_FD_MAP;
                if (i >= sparseArray.size()) {
                    break;
                }
                String bpfConnectName = sparseArray.valueAt(i);
                int bpfSocketFD = sparseArray.keyAt(i);
                if (bpfSocketFD == socketFD) {
                    fdType = 1;
                }
                Slog.i(TAG, "default thread ConnectName = " + bpfConnectName + ", fd = " + bpfSocketFD);
                i++;
            }
            int i2 = 0;
            while (true) {
                SparseArray<String> sparseArray2 = CUSTOMIZE_THREAD_FD_MAP;
                if (i2 < sparseArray2.size()) {
                    String bpfConnectName2 = sparseArray2.valueAt(i2);
                    int bpfSocketFD2 = sparseArray2.keyAt(i2);
                    if (bpfSocketFD2 == socketFD) {
                        fdType = 2;
                    }
                    Slog.i(TAG, "customize thread ConnectName = " + bpfConnectName2 + ", fd = " + bpfSocketFD2);
                    i2++;
                }
            }
        }
        return fdType;
    }

    private static boolean isBpfSocketExist() {
        File socketFile = new File(OPLUS_BPF_MANAGER_SOCKET);
        return socketFile.exists();
    }

    private static boolean isLegalSdkAndBpfEnable() {
        return Build.VERSION.DEVICE_INITIAL_SDK_INT >= 33 && SystemProperties.getInt(OPLUS_BPF_ENABLE_PROP, 0) > 0;
    }

    private boolean execBpfCommand(OutputStream ostream, String message, short command, int commandFlags) {
        try {
            int len = message.length();
            if (len > 1024) {
                return false;
            }
            ByteBuffer buf = ByteBuffer.allocate(len + 12);
            buf.order(ByteOrder.LITTLE_ENDIAN);
            buf.put(BPF_MANAGER_MAGIC_NUM.getBytes());
            buf.putShort(command);
            buf.putInt(commandFlags);
            buf.putShort((short) len);
            buf.put(message.getBytes());
            ostream.write(buf.array(), 0, buf.position());
            if (DEBUG) {
                Slog.d(TAG, "command position = " + buf.position() + ", buf.array().length = " + buf.array().length + ", len = " + len + ", message = " + message + ", command = " + ((int) command));
                return true;
            }
            return true;
        } catch (IOException ex) {
            Slog.w(TAG, "command IOException message = " + message + ", command = " + ((int) command) + ", ex = " + ex);
            return false;
        }
    }

    private LocalSocket openSocket() {
        try {
            LocalSocket socket = new LocalSocket(3);
            socket.connect(new LocalSocketAddress(SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED));
            return socket;
        } catch (IOException ex) {
            Slog.e(TAG, "Connection failed: " + ex);
            return null;
        }
    }

    private int read(ByteBuffer buf) {
        int read;
        synchronized (this.mBpfSocketLock) {
            try {
                try {
                    read = this.mBpfInputStream.read(buf.array(), 0, buf.array().length);
                } catch (IOException ex) {
                    Slog.w(TAG, "read IOException ex = " + ex);
                    return -1;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return read;
    }

    private boolean initMsgQueue() {
        synchronized (DEFAULT_THREAD_LOCK) {
            if (this.mIsDefaultQueue) {
                if (sBpfThread == null) {
                    HandlerThread handlerThread = new HandlerThread(DEFAULT_THREAD_NAME, 10);
                    sBpfThread = handlerThread;
                    handlerThread.start();
                }
                Looper looper = sBpfThread.getLooper();
                this.mLooper = looper;
                this.mMsgQueue = looper.getQueue();
            }
            if (this.mIsDefaultQueue) {
                DEFAULT_THREAD_FD_MAP.put(this.mBpfSocketFD, this.mBpfConnectName);
            } else {
                CUSTOMIZE_THREAD_FD_MAP.put(this.mBpfSocketFD, this.mBpfConnectName);
            }
        }
        if (this.mMsgQueue == null) {
            Slog.w(TAG, "init fail mMsgQueue = null");
            return false;
        }
        return true;
    }

    private OBpfData handleBpfData(ByteBufferInputStream inputData, int receivedLen) {
        if (receivedLen >= 12) {
            try {
                OBpfData oBpfData = new OBpfData();
                int magicNum = inputData.readInt(ByteOrder.LITTLE_ENDIAN);
                if (magicNum != BPF_BIN_DATA_MAGIC_NUM) {
                    Slog.w(TAG, "handleData mismatch magicNum = " + magicNum);
                    return null;
                }
                int dispatchFlag = inputData.readInt(ByteOrder.LITTLE_ENDIAN);
                short dataType = inputData.readShort(ByteOrder.LITTLE_ENDIAN);
                int msgLen = inputData.readShort(ByteOrder.LITTLE_ENDIAN) & 65535;
                oBpfData.mDispatchFlag = dispatchFlag;
                oBpfData.mDataType = dataType;
                if (msgLen != receivedLen - 12) {
                    Slog.w(TAG, "handleData illegal msgLen = " + msgLen + ", receivedLen = " + receivedLen);
                }
                byte[] bytes = new byte[receivedLen - 12];
                inputData.readFully(bytes);
                oBpfData.mData = bytes;
                return oBpfData;
            } catch (IOException e) {
                Slog.w(TAG, "handleData IOException e = " + e);
            }
        }
        Slog.w(TAG, "handleData fail receivedLen = " + receivedLen);
        return null;
    }

    private void processIncomingData() {
        int len = read(this.mInputBuf);
        if (len > 0) {
            try {
                this.mInputData.reset();
                synchronized (this.mReplyBufLock) {
                    OBpfData oBpfData = handleBpfData(this.mInputData, len);
                    if (oBpfData != null && oBpfData.mDispatchFlag > 0 && oBpfData.mData != null) {
                        this.mListener.onBpfDataArrived(oBpfData.mDispatchFlag, oBpfData.mDataType, oBpfData.mData);
                    }
                }
            } catch (IOException e) {
                Slog.w(TAG, "Failed to parse bpf data buffer. Size = " + len);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: fileDescriptorEventHandler, reason: merged with bridge method [inline-methods] */
    public int lambda$connect$0(FileDescriptor fd, int events) {
        if (this.mListener == null) {
            return 0;
        }
        if ((events & 1) != 0) {
            processIncomingData();
        }
        if ((events & 4) != 0) {
            disconnect();
            return 0;
        }
        return 5;
    }

    private boolean ignoreConnect() {
        boolean quitting;
        if (this.mBpfSocket != null) {
            Slog.w(TAG, "is already connect");
            return true;
        }
        if (this.mListener == null) {
            Slog.w(TAG, "do not have ConnectionListener");
            return true;
        }
        synchronized (this.mQuitLock) {
            quitting = this.mQuitting;
        }
        if (quitting) {
            Slog.w(TAG, "can not use this, now mQuitting = true");
            return true;
        }
        return false;
    }

    protected void finalize() throws Throwable {
        try {
            if (DEBUG) {
                Slog.d(TAG, "finalize ConnectName = " + this.mBpfConnectName);
            }
            exit();
        } finally {
            super.finalize();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class OBpfData {
        private byte[] mData;
        private short mDataType;
        private int mDispatchFlag;

        private OBpfData() {
            this.mDispatchFlag = -1;
            this.mDataType = (short) -1;
            this.mData = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ByteBufferInputStream extends DataInputStream {
        public ByteBufferInputStream(InputStream inputStream) {
            super(inputStream);
        }

        private static long peekLong(byte[] src, ByteOrder order) {
            if (order == ByteOrder.BIG_ENDIAN) {
                long result = (src[0] << 56) + ((src[1] & 255) << 48) + ((src[2] & 255) << 40) + ((src[3] & 255) << 32);
                return result + ((src[4] & 255) << 24) + ((src[5] & 255) << 16) + ((src[6] & 255) << 8) + ((src[7] & 255) << 0);
            }
            long result2 = (src[7] << 56) + ((src[6] & 255) << 48) + ((src[5] & 255) << 40) + ((src[4] & 255) << 32);
            return result2 + ((src[3] & 255) << 24) + ((src[2] & 255) << 16) + ((src[1] & 255) << 8) + ((src[0] & 255) << 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public short readShort(ByteOrder byteOrder) throws IOException {
            byte[] readBuffer = new byte[2];
            readFully(readBuffer);
            return Memory.peekShort(readBuffer, 0, byteOrder);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int readInt(ByteOrder byteOrder) throws IOException {
            byte[] readBuffer = new byte[4];
            readFully(readBuffer);
            return Memory.peekInt(readBuffer, 0, byteOrder);
        }

        private long readLong(ByteOrder byteOrder) throws IOException {
            byte[] readBuffer = new byte[8];
            readFully(readBuffer);
            return peekLong(readBuffer, byteOrder);
        }
    }
}
