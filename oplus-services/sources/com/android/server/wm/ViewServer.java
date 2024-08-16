package com.android.server.wm;

import android.util.Slog;
import com.android.server.wm.WindowManagerService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ViewServer implements Runnable {
    private static final String COMMAND_PROTOCOL_VERSION = "PROTOCOL";
    private static final String COMMAND_SERVER_VERSION = "SERVER";
    private static final String COMMAND_WINDOW_MANAGER_AUTOLIST = "AUTOLIST";
    private static final String COMMAND_WINDOW_MANAGER_GET_FOCUS = "GET_FOCUS";
    private static final String COMMAND_WINDOW_MANAGER_LIST = "LIST";
    private static final String LOG_TAG = "WindowManager";
    private static final String VALUE_PROTOCOL_VERSION = "4";
    private static final String VALUE_SERVER_VERSION = "4";
    public static final int VIEW_SERVER_DEFAULT_PORT = 4939;
    private static final int VIEW_SERVER_MAX_CONNECTIONS = 10;
    private final int mPort;
    private ServerSocket mServer;
    private Thread mThread;
    private ExecutorService mThreadPool;
    private final WindowManagerService mWindowManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewServer(WindowManagerService windowManagerService, int i) {
        this.mWindowManager = windowManagerService;
        this.mPort = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean start() throws IOException {
        if (this.mThread != null) {
            return false;
        }
        this.mServer = new ServerSocket(this.mPort, 10, InetAddress.getLocalHost());
        this.mThread = new Thread(this, "Remote View Server [port=" + this.mPort + "]");
        this.mThreadPool = Executors.newFixedThreadPool(10);
        this.mThread.start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean stop() {
        Thread thread = this.mThread;
        if (thread == null) {
            return false;
        }
        thread.interrupt();
        ExecutorService executorService = this.mThreadPool;
        if (executorService != null) {
            try {
                executorService.shutdownNow();
            } catch (SecurityException unused) {
                Slog.w(LOG_TAG, "Could not stop all view server threads");
            }
        }
        this.mThreadPool = null;
        this.mThread = null;
        try {
            this.mServer.close();
            this.mServer = null;
            return true;
        } catch (IOException unused2) {
            Slog.w(LOG_TAG, "Could not close the view server");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRunning() {
        Thread thread = this.mThread;
        return thread != null && thread.isAlive();
    }

    @Override // java.lang.Runnable
    public void run() {
        while (Thread.currentThread() == this.mThread) {
            try {
                Socket accept = this.mServer.accept();
                ExecutorService executorService = this.mThreadPool;
                if (executorService != null) {
                    executorService.submit(new ViewServerWorker(accept));
                } else {
                    try {
                        accept.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                Slog.w(LOG_TAG, "Connection error: ", e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean writeValue(Socket socket, String str) {
        BufferedWriter bufferedWriter;
        boolean z = false;
        BufferedWriter bufferedWriter2 = null;
        try {
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), 8192);
            } catch (Exception unused) {
            } catch (Throwable th) {
                th = th;
            }
            try {
                bufferedWriter.write(str);
                bufferedWriter.write("\n");
                bufferedWriter.flush();
                bufferedWriter.close();
                z = true;
            } catch (Exception unused2) {
                bufferedWriter2 = bufferedWriter;
                if (bufferedWriter2 != null) {
                    bufferedWriter2.close();
                }
                return z;
            } catch (Throwable th2) {
                th = th2;
                bufferedWriter2 = bufferedWriter;
                if (bufferedWriter2 != null) {
                    try {
                        bufferedWriter2.close();
                    } catch (IOException unused3) {
                    }
                }
                throw th;
            }
        } catch (IOException unused4) {
        }
        return z;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    class ViewServerWorker implements Runnable, WindowManagerService.WindowChangeListener {
        private Socket mClient;
        private boolean mNeedWindowListUpdate = false;
        private boolean mNeedFocusedWindowUpdate = false;

        public ViewServerWorker(Socket socket) {
            this.mClient = socket;
        }

        /* JADX WARN: Removed duplicated region for block: B:60:0x00ee A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:67:? A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:68:0x00e2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            BufferedReader bufferedReader;
            IOException e;
            Socket socket;
            String substring;
            boolean viewServerWindowCommand;
            Socket socket2;
            BufferedReader bufferedReader2 = null;
            try {
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(this.mClient.getInputStream()), 1024);
                } catch (IOException e2) {
                    bufferedReader = null;
                    e = e2;
                } catch (Throwable th) {
                    th = th;
                    if (bufferedReader2 != null) {
                    }
                    socket = this.mClient;
                    if (socket == null) {
                    }
                }
                try {
                    try {
                        String readLine = bufferedReader.readLine();
                        int indexOf = readLine.indexOf(32);
                        if (indexOf == -1) {
                            substring = "";
                        } else {
                            String substring2 = readLine.substring(0, indexOf);
                            substring = readLine.substring(indexOf + 1);
                            readLine = substring2;
                        }
                        if (ViewServer.COMMAND_PROTOCOL_VERSION.equalsIgnoreCase(readLine)) {
                            viewServerWindowCommand = ViewServer.writeValue(this.mClient, "4");
                        } else if (ViewServer.COMMAND_SERVER_VERSION.equalsIgnoreCase(readLine)) {
                            viewServerWindowCommand = ViewServer.writeValue(this.mClient, "4");
                        } else if (ViewServer.COMMAND_WINDOW_MANAGER_LIST.equalsIgnoreCase(readLine)) {
                            viewServerWindowCommand = ViewServer.this.mWindowManager.viewServerListWindows(this.mClient);
                        } else if (ViewServer.COMMAND_WINDOW_MANAGER_GET_FOCUS.equalsIgnoreCase(readLine)) {
                            viewServerWindowCommand = ViewServer.this.mWindowManager.viewServerGetFocusedWindow(this.mClient);
                        } else if (ViewServer.COMMAND_WINDOW_MANAGER_AUTOLIST.equalsIgnoreCase(readLine)) {
                            viewServerWindowCommand = windowManagerAutolistLoop();
                        } else {
                            viewServerWindowCommand = ViewServer.this.mWindowManager.viewServerWindowCommand(this.mClient, readLine, substring);
                        }
                        if (!viewServerWindowCommand) {
                            Slog.w(ViewServer.LOG_TAG, "An error occurred with the command: " + readLine);
                        }
                        try {
                            bufferedReader.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                        socket2 = this.mClient;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedReader2 = bufferedReader;
                        if (bufferedReader2 != null) {
                            try {
                                bufferedReader2.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        socket = this.mClient;
                        if (socket == null) {
                            try {
                                socket.close();
                                throw th;
                            } catch (IOException e5) {
                                e5.printStackTrace();
                                throw th;
                            }
                        }
                        throw th;
                    }
                } catch (IOException e6) {
                    e = e6;
                    Slog.w(ViewServer.LOG_TAG, "Connection error: ", e);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e7) {
                            e7.printStackTrace();
                        }
                    }
                    Socket socket3 = this.mClient;
                    if (socket3 != null) {
                        socket3.close();
                    }
                    return;
                }
                if (socket2 != null) {
                    socket2.close();
                }
            } catch (IOException e8) {
                e8.printStackTrace();
            }
        }

        @Override // com.android.server.wm.WindowManagerService.WindowChangeListener
        public void windowsChanged() {
            synchronized (this) {
                this.mNeedWindowListUpdate = true;
                notifyAll();
            }
        }

        @Override // com.android.server.wm.WindowManagerService.WindowChangeListener
        public void focusChanged() {
            synchronized (this) {
                this.mNeedFocusedWindowUpdate = true;
                notifyAll();
            }
        }

        private boolean windowManagerAutolistLoop() {
            boolean z;
            boolean z2;
            boolean z3;
            ViewServer.this.mWindowManager.addWindowChangeListener(this);
            BufferedWriter bufferedWriter = null;
            try {
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(this.mClient.getOutputStream()));
                    while (!Thread.interrupted()) {
                        try {
                            synchronized (this) {
                                while (true) {
                                    z = this.mNeedWindowListUpdate;
                                    if (z || this.mNeedFocusedWindowUpdate) {
                                        break;
                                    }
                                    wait();
                                }
                                z2 = false;
                                if (z) {
                                    this.mNeedWindowListUpdate = false;
                                    z3 = true;
                                } else {
                                    z3 = false;
                                }
                                if (this.mNeedFocusedWindowUpdate) {
                                    this.mNeedFocusedWindowUpdate = false;
                                    z2 = true;
                                }
                            }
                            if (z3) {
                                bufferedWriter2.write("LIST UPDATE\n");
                                bufferedWriter2.flush();
                            }
                            if (z2) {
                                bufferedWriter2.write("ACTION_FOCUS UPDATE\n");
                                bufferedWriter2.flush();
                            }
                        } catch (Exception unused) {
                            bufferedWriter = bufferedWriter2;
                            if (bufferedWriter != null) {
                                bufferedWriter.close();
                            }
                            ViewServer.this.mWindowManager.removeWindowChangeListener(this);
                            return true;
                        } catch (Throwable th) {
                            th = th;
                            bufferedWriter = bufferedWriter2;
                            if (bufferedWriter != null) {
                                try {
                                    bufferedWriter.close();
                                } catch (IOException unused2) {
                                }
                            }
                            ViewServer.this.mWindowManager.removeWindowChangeListener(this);
                            throw th;
                        }
                    }
                    bufferedWriter2.close();
                } catch (Exception unused3) {
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (IOException unused4) {
            }
            ViewServer.this.mWindowManager.removeWindowChangeListener(this);
            return true;
        }
    }
}
