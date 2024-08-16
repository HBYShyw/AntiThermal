package com.oplus.screenshot;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusScreenshotManager extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.screenshot.IOplusScreenshotManager";

    boolean isLongshotDisabled() throws RemoteException;

    boolean isLongshotEnabled() throws RemoteException;

    boolean isLongshotMode() throws RemoteException;

    boolean isScreenshotEdit() throws RemoteException;

    boolean isScreenshotEnabled() throws RemoteException;

    boolean isScreenshotMode() throws RemoteException;

    boolean isScreenshotSupported() throws RemoteException;

    void notifyOverScroll(OplusLongshotEvent oplusLongshotEvent) throws RemoteException;

    void reportLongshotDumpResult(OplusLongshotDump oplusLongshotDump) throws RemoteException;

    void setLongshotEnabled(boolean z) throws RemoteException;

    void setScreenshotEnabled(boolean z) throws RemoteException;

    void stopLongshot() throws RemoteException;

    void takeLongshot(boolean z, boolean z2) throws RemoteException;

    void takeScreenshot(Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusScreenshotManager {
        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public void takeScreenshot(Bundle extras) throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public boolean isScreenshotMode() throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public boolean isScreenshotEdit() throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public void takeLongshot(boolean statusBarVisible, boolean navBarVisible) throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public void stopLongshot() throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public boolean isLongshotMode() throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public boolean isLongshotDisabled() throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public void reportLongshotDumpResult(OplusLongshotDump result) throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public boolean isScreenshotSupported() throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public void setScreenshotEnabled(boolean enabled) throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public boolean isScreenshotEnabled() throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public void setLongshotEnabled(boolean enabled) throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public boolean isLongshotEnabled() throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenshot.IOplusScreenshotManager
        public void notifyOverScroll(OplusLongshotEvent event) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusScreenshotManager {
        static final int TRANSACTION_isLongshotDisabled = 7;
        static final int TRANSACTION_isLongshotEnabled = 13;
        static final int TRANSACTION_isLongshotMode = 6;
        static final int TRANSACTION_isScreenshotEdit = 3;
        static final int TRANSACTION_isScreenshotEnabled = 11;
        static final int TRANSACTION_isScreenshotMode = 2;
        static final int TRANSACTION_isScreenshotSupported = 9;
        static final int TRANSACTION_notifyOverScroll = 14;
        static final int TRANSACTION_reportLongshotDumpResult = 8;
        static final int TRANSACTION_setLongshotEnabled = 12;
        static final int TRANSACTION_setScreenshotEnabled = 10;
        static final int TRANSACTION_stopLongshot = 5;
        static final int TRANSACTION_takeLongshot = 4;
        static final int TRANSACTION_takeScreenshot = 1;

        public Stub() {
            attachInterface(this, IOplusScreenshotManager.DESCRIPTOR);
        }

        public static IOplusScreenshotManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusScreenshotManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusScreenshotManager)) {
                return (IOplusScreenshotManager) iin;
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
                    return "takeScreenshot";
                case 2:
                    return "isScreenshotMode";
                case 3:
                    return "isScreenshotEdit";
                case 4:
                    return "takeLongshot";
                case 5:
                    return "stopLongshot";
                case 6:
                    return "isLongshotMode";
                case 7:
                    return "isLongshotDisabled";
                case 8:
                    return "reportLongshotDumpResult";
                case 9:
                    return "isScreenshotSupported";
                case 10:
                    return "setScreenshotEnabled";
                case 11:
                    return "isScreenshotEnabled";
                case 12:
                    return "setLongshotEnabled";
                case 13:
                    return "isLongshotEnabled";
                case 14:
                    return "notifyOverScroll";
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
                data.enforceInterface(IOplusScreenshotManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusScreenshotManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            Bundle _arg0 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            takeScreenshot(_arg0);
                            return true;
                        case 2:
                            boolean _result = isScreenshotMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 3:
                            boolean _result2 = isScreenshotEdit();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 4:
                            boolean _arg02 = data.readBoolean();
                            boolean _arg1 = data.readBoolean();
                            data.enforceNoDataAvail();
                            takeLongshot(_arg02, _arg1);
                            return true;
                        case 5:
                            stopLongshot();
                            return true;
                        case 6:
                            boolean _result3 = isLongshotMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 7:
                            boolean _result4 = isLongshotDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 8:
                            OplusLongshotDump _arg03 = (OplusLongshotDump) data.readTypedObject(OplusLongshotDump.CREATOR);
                            data.enforceNoDataAvail();
                            reportLongshotDumpResult(_arg03);
                            return true;
                        case 9:
                            boolean _result5 = isScreenshotSupported();
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 10:
                            boolean _arg04 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setScreenshotEnabled(_arg04);
                            return true;
                        case 11:
                            boolean _result6 = isScreenshotEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 12:
                            boolean _arg05 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setLongshotEnabled(_arg05);
                            return true;
                        case 13:
                            boolean _result7 = isLongshotEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 14:
                            OplusLongshotEvent _arg06 = (OplusLongshotEvent) data.readTypedObject(OplusLongshotEvent.CREATOR);
                            data.enforceNoDataAvail();
                            notifyOverScroll(_arg06);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusScreenshotManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusScreenshotManager.DESCRIPTOR;
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public void takeScreenshot(Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    _data.writeTypedObject(extras, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public boolean isScreenshotMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public boolean isScreenshotEdit() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public void takeLongshot(boolean statusBarVisible, boolean navBarVisible) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    _data.writeBoolean(statusBarVisible);
                    _data.writeBoolean(navBarVisible);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public void stopLongshot() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public boolean isLongshotMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public boolean isLongshotDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public void reportLongshotDumpResult(OplusLongshotDump result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    _data.writeTypedObject(result, 0);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public boolean isScreenshotSupported() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public void setScreenshotEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public boolean isScreenshotEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public void setLongshotEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public boolean isLongshotEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshotManager
            public void notifyOverScroll(OplusLongshotEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenshotManager.DESCRIPTOR);
                    _data.writeTypedObject(event, 0);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 13;
        }
    }
}
