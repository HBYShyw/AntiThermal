package com.oplus.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.oplus.app.KernelWakeLockInfo;
import com.oplus.app.KernelWakeupInfo;
import com.oplus.app.OplusAlarmInfo;
import com.oplus.app.OplusWakeLockInfo;
import com.oplus.app.SuspendInfo;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusPowerMonitor extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.os.IOplusPowerMonitor";

    void acquireSuspendBlocker(String str) throws RemoteException;

    List<OplusAlarmInfo> getAlarmWakeUpInfo(long j, long j2) throws RemoteException;

    String getRpmMasterStatsFilePath() throws RemoteException;

    String getRpmStatsFilePath() throws RemoteException;

    String[] getSubSystem() throws RemoteException;

    SuspendInfo getSuspendStats() throws RemoteException;

    List<OplusWakeLockInfo> getWakeLockInfo(long j, long j2) throws RemoteException;

    ParcelFileDescriptor getWakeLockInfoFileDescriptor(long j, long j2) throws RemoteException;

    KernelWakeLockInfo[] getWakeLockStats() throws RemoteException;

    KernelWakeupInfo[] getWakeupStats() throws RemoteException;

    String getWakeups() throws RemoteException;

    void recordAlarmWakeupEvent() throws RemoteException;

    void recordAppWakeupEvent(int i, String str) throws RemoteException;

    void recordAppWakeupInfoEvent(OplusAlarmInfo oplusAlarmInfo) throws RemoteException;

    void recordWakeLockAcquireEvent(OplusWakeLockInfo oplusWakeLockInfo) throws RemoteException;

    void recordWakeLockReleaseEvent(OplusWakeLockInfo oplusWakeLockInfo) throws RemoteException;

    void releaseSuspendBlocker(String str) throws RemoteException;

    void resetWakeupEventRecords() throws RemoteException;

    void scheduleRpmUpdate(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusPowerMonitor {
        @Override // com.oplus.os.IOplusPowerMonitor
        public void recordAlarmWakeupEvent() throws RemoteException {
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public void recordAppWakeupEvent(int alarmType, String alarmPackageName) throws RemoteException {
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public void resetWakeupEventRecords() throws RemoteException {
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public void acquireSuspendBlocker(String name) throws RemoteException {
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public void releaseSuspendBlocker(String name) throws RemoteException {
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public String getRpmStatsFilePath() throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public String getRpmMasterStatsFilePath() throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public void scheduleRpmUpdate(boolean calDelta) throws RemoteException {
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public void recordAppWakeupInfoEvent(OplusAlarmInfo alarmInfo) throws RemoteException {
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public List<OplusAlarmInfo> getAlarmWakeUpInfo(long starttime, long endtime) throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public void recordWakeLockAcquireEvent(OplusWakeLockInfo wakeLockInfo) throws RemoteException {
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public void recordWakeLockReleaseEvent(OplusWakeLockInfo wakeLockInfo) throws RemoteException {
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public List<OplusWakeLockInfo> getWakeLockInfo(long starttime, long endtime) throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public KernelWakeLockInfo[] getWakeLockStats() throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public KernelWakeupInfo[] getWakeupStats() throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public SuspendInfo getSuspendStats() throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public String getWakeups() throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public String[] getSubSystem() throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusPowerMonitor
        public ParcelFileDescriptor getWakeLockInfoFileDescriptor(long starttime, long endtime) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusPowerMonitor {
        static final int TRANSACTION_acquireSuspendBlocker = 4;
        static final int TRANSACTION_getAlarmWakeUpInfo = 10;
        static final int TRANSACTION_getRpmMasterStatsFilePath = 7;
        static final int TRANSACTION_getRpmStatsFilePath = 6;
        static final int TRANSACTION_getSubSystem = 18;
        static final int TRANSACTION_getSuspendStats = 16;
        static final int TRANSACTION_getWakeLockInfo = 13;
        static final int TRANSACTION_getWakeLockInfoFileDescriptor = 19;
        static final int TRANSACTION_getWakeLockStats = 14;
        static final int TRANSACTION_getWakeupStats = 15;
        static final int TRANSACTION_getWakeups = 17;
        static final int TRANSACTION_recordAlarmWakeupEvent = 1;
        static final int TRANSACTION_recordAppWakeupEvent = 2;
        static final int TRANSACTION_recordAppWakeupInfoEvent = 9;
        static final int TRANSACTION_recordWakeLockAcquireEvent = 11;
        static final int TRANSACTION_recordWakeLockReleaseEvent = 12;
        static final int TRANSACTION_releaseSuspendBlocker = 5;
        static final int TRANSACTION_resetWakeupEventRecords = 3;
        static final int TRANSACTION_scheduleRpmUpdate = 8;

        public Stub() {
            attachInterface(this, IOplusPowerMonitor.DESCRIPTOR);
        }

        public static IOplusPowerMonitor asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusPowerMonitor.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusPowerMonitor)) {
                return (IOplusPowerMonitor) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "recordAlarmWakeupEvent";
                case 2:
                    return "recordAppWakeupEvent";
                case 3:
                    return "resetWakeupEventRecords";
                case 4:
                    return "acquireSuspendBlocker";
                case 5:
                    return "releaseSuspendBlocker";
                case 6:
                    return "getRpmStatsFilePath";
                case 7:
                    return "getRpmMasterStatsFilePath";
                case 8:
                    return "scheduleRpmUpdate";
                case 9:
                    return "recordAppWakeupInfoEvent";
                case 10:
                    return "getAlarmWakeUpInfo";
                case 11:
                    return "recordWakeLockAcquireEvent";
                case 12:
                    return "recordWakeLockReleaseEvent";
                case 13:
                    return "getWakeLockInfo";
                case 14:
                    return "getWakeLockStats";
                case 15:
                    return "getWakeupStats";
                case 16:
                    return "getSuspendStats";
                case 17:
                    return "getWakeups";
                case 18:
                    return "getSubSystem";
                case 19:
                    return "getWakeLockInfoFileDescriptor";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusPowerMonitor.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusPowerMonitor.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            recordAlarmWakeupEvent();
                            return true;
                        case 2:
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            recordAppWakeupEvent(_arg0, _arg1);
                            return true;
                        case 3:
                            resetWakeupEventRecords();
                            return true;
                        case 4:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            acquireSuspendBlocker(_arg02);
                            return true;
                        case 5:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            releaseSuspendBlocker(_arg03);
                            return true;
                        case 6:
                            String _result = getRpmStatsFilePath();
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 7:
                            String _result2 = getRpmMasterStatsFilePath();
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 8:
                            boolean _arg04 = data.readBoolean();
                            data.enforceNoDataAvail();
                            scheduleRpmUpdate(_arg04);
                            return true;
                        case 9:
                            OplusAlarmInfo _arg05 = (OplusAlarmInfo) data.readTypedObject(OplusAlarmInfo.CREATOR);
                            data.enforceNoDataAvail();
                            recordAppWakeupInfoEvent(_arg05);
                            return true;
                        case 10:
                            long _arg06 = data.readLong();
                            long _arg12 = data.readLong();
                            data.enforceNoDataAvail();
                            List<OplusAlarmInfo> _result3 = getAlarmWakeUpInfo(_arg06, _arg12);
                            reply.writeNoException();
                            reply.writeTypedList(_result3, 1);
                            return true;
                        case 11:
                            OplusWakeLockInfo _arg07 = (OplusWakeLockInfo) data.readTypedObject(OplusWakeLockInfo.CREATOR);
                            data.enforceNoDataAvail();
                            recordWakeLockAcquireEvent(_arg07);
                            return true;
                        case 12:
                            OplusWakeLockInfo _arg08 = (OplusWakeLockInfo) data.readTypedObject(OplusWakeLockInfo.CREATOR);
                            data.enforceNoDataAvail();
                            recordWakeLockReleaseEvent(_arg08);
                            return true;
                        case 13:
                            long _arg09 = data.readLong();
                            long _arg13 = data.readLong();
                            data.enforceNoDataAvail();
                            List<OplusWakeLockInfo> _result4 = getWakeLockInfo(_arg09, _arg13);
                            reply.writeNoException();
                            reply.writeTypedList(_result4, 1);
                            return true;
                        case 14:
                            KernelWakeLockInfo[] _result5 = getWakeLockStats();
                            reply.writeNoException();
                            reply.writeTypedArray(_result5, 1);
                            return true;
                        case 15:
                            KernelWakeupInfo[] _result6 = getWakeupStats();
                            reply.writeNoException();
                            reply.writeTypedArray(_result6, 1);
                            return true;
                        case 16:
                            SuspendInfo _result7 = getSuspendStats();
                            reply.writeNoException();
                            reply.writeTypedObject(_result7, 1);
                            return true;
                        case 17:
                            String _result8 = getWakeups();
                            reply.writeNoException();
                            reply.writeString(_result8);
                            return true;
                        case 18:
                            String[] _result9 = getSubSystem();
                            reply.writeNoException();
                            reply.writeStringArray(_result9);
                            return true;
                        case 19:
                            long _arg010 = data.readLong();
                            long _arg14 = data.readLong();
                            data.enforceNoDataAvail();
                            ParcelFileDescriptor _result10 = getWakeLockInfoFileDescriptor(_arg010, _arg14);
                            reply.writeNoException();
                            reply.writeTypedObject(_result10, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusPowerMonitor {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusPowerMonitor.DESCRIPTOR;
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public void recordAlarmWakeupEvent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public void recordAppWakeupEvent(int alarmType, String alarmPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    _data.writeInt(alarmType);
                    _data.writeString(alarmPackageName);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public void resetWakeupEventRecords() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public void acquireSuspendBlocker(String name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public void releaseSuspendBlocker(String name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public String getRpmStatsFilePath() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public String getRpmMasterStatsFilePath() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public void scheduleRpmUpdate(boolean calDelta) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    _data.writeBoolean(calDelta);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public void recordAppWakeupInfoEvent(OplusAlarmInfo alarmInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    _data.writeTypedObject(alarmInfo, 0);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public List<OplusAlarmInfo> getAlarmWakeUpInfo(long starttime, long endtime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    _data.writeLong(starttime);
                    _data.writeLong(endtime);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    List<OplusAlarmInfo> _result = _reply.createTypedArrayList(OplusAlarmInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public void recordWakeLockAcquireEvent(OplusWakeLockInfo wakeLockInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    _data.writeTypedObject(wakeLockInfo, 0);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public void recordWakeLockReleaseEvent(OplusWakeLockInfo wakeLockInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    _data.writeTypedObject(wakeLockInfo, 0);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public List<OplusWakeLockInfo> getWakeLockInfo(long starttime, long endtime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    _data.writeLong(starttime);
                    _data.writeLong(endtime);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    List<OplusWakeLockInfo> _result = _reply.createTypedArrayList(OplusWakeLockInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public KernelWakeLockInfo[] getWakeLockStats() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    KernelWakeLockInfo[] _result = (KernelWakeLockInfo[]) _reply.createTypedArray(KernelWakeLockInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public KernelWakeupInfo[] getWakeupStats() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    KernelWakeupInfo[] _result = (KernelWakeupInfo[]) _reply.createTypedArray(KernelWakeupInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public SuspendInfo getSuspendStats() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    SuspendInfo _result = (SuspendInfo) _reply.readTypedObject(SuspendInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public String getWakeups() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public String[] getSubSystem() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusPowerMonitor
            public ParcelFileDescriptor getWakeLockInfoFileDescriptor(long starttime, long endtime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerMonitor.DESCRIPTOR);
                    _data.writeLong(starttime);
                    _data.writeLong(endtime);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    ParcelFileDescriptor _result = (ParcelFileDescriptor) _reply.readTypedObject(ParcelFileDescriptor.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 18;
        }
    }
}
