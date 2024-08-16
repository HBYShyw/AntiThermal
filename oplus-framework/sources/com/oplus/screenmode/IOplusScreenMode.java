package com.oplus.screenmode;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.screenmode.IOplusScreenModeCallback;

/* loaded from: classes.dex */
public interface IOplusScreenMode extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.screenmode.IOplusScreenMode";

    void addCallback(int i, IOplusScreenModeCallback iOplusScreenModeCallback) throws RemoteException;

    void enterBrightnessChangeToFlicker(boolean z, int i) throws RemoteException;

    void enterDCAndLowBrightnessMode(boolean z) throws RemoteException;

    void enterHighBrightnessMode(boolean z, int i) throws RemoteException;

    void enterPSMode(boolean z) throws RemoteException;

    void enterPSModeOnRate(boolean z, int i) throws RemoteException;

    void enterPSModeOnRateWithToken(boolean z, int i, IBinder iBinder) throws RemoteException;

    int getAppOverrideRefreshRate(String str, int i) throws RemoteException;

    Bundle getAppOverrideRefreshRateList() throws RemoteException;

    String getDisableOverrideViewList(String str) throws RemoteException;

    Bundle getDownScale(String str) throws RemoteException;

    boolean getGameList(Bundle bundle) throws RemoteException;

    boolean isDisplayCompat(String str, int i) throws RemoteException;

    void keepHighRefreshRate(int i) throws RemoteException;

    void overrideWindowRefreshRate(IBinder iBinder, int i) throws RemoteException;

    void remove(int i, IOplusScreenModeCallback iOplusScreenModeCallback) throws RemoteException;

    boolean removeAllCustomizeRefreshRate() throws RemoteException;

    boolean removeCustomizeRefreshRate(String str) throws RemoteException;

    boolean requestGameRefreshRate(String str, int i) throws RemoteException;

    boolean requestMemcRefreshRate(boolean z, int i) throws RemoteException;

    boolean requestNearFlashWithToken(boolean z, int i, IBinder iBinder) throws RemoteException;

    boolean requestRefreshRate(boolean z, int i) throws RemoteException;

    boolean requestRefreshRateWithToken(boolean z, int i, IBinder iBinder) throws RemoteException;

    boolean setAppOverrideRefreshRate(String str, int i, int i2) throws RemoteException;

    void setClientRefreshRate(IBinder iBinder, int i) throws RemoteException;

    int setDownScale(String str, float f, boolean z, boolean z2) throws RemoteException;

    boolean setHighTemperatureStatus(int i, int i2) throws RemoteException;

    void setMemcWorkStatus(boolean z) throws RemoteException;

    boolean supportDisplayCompat(String str, int i) throws RemoteException;

    void updateFpsWhenDcChange(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusScreenMode {
        @Override // com.oplus.screenmode.IOplusScreenMode
        public void addCallback(int index, IOplusScreenModeCallback callback) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void remove(int index, IOplusScreenModeCallback callback) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void setClientRefreshRate(IBinder token, int rate) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean requestRefreshRate(boolean open, int rate) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean supportDisplayCompat(String pkg, int uid) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean setHighTemperatureStatus(int status, int rate) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void enterDCAndLowBrightnessMode(boolean enter) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean isDisplayCompat(String packageName, int uid) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void enterPSMode(boolean enter) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void enterPSModeOnRate(boolean enter, int rate) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean requestGameRefreshRate(String packageName, int rate) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean requestRefreshRateWithToken(boolean open, int rate, IBinder token) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean getGameList(Bundle outBundle) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public String getDisableOverrideViewList(String key) throws RemoteException {
            return null;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void enterPSModeOnRateWithToken(boolean open, int rate, IBinder token) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void overrideWindowRefreshRate(IBinder window, int refreshRateId) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void keepHighRefreshRate(int status) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void updateFpsWhenDcChange(boolean enter) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void setMemcWorkStatus(boolean memc) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean requestMemcRefreshRate(boolean open, int rate) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public int setDownScale(String pkgName, float downscale, boolean enable, boolean originMode) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public Bundle getDownScale(String pkgName) throws RemoteException {
            return null;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void enterHighBrightnessMode(boolean enter, int rate) throws RemoteException {
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean setAppOverrideRefreshRate(String pkg, int mode, int rate) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public int getAppOverrideRefreshRate(String pkg, int mode) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public Bundle getAppOverrideRefreshRateList() throws RemoteException {
            return null;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean removeCustomizeRefreshRate(String pkg) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean removeAllCustomizeRefreshRate() throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public boolean requestNearFlashWithToken(boolean enter, int rate, IBinder token) throws RemoteException {
            return false;
        }

        @Override // com.oplus.screenmode.IOplusScreenMode
        public void enterBrightnessChangeToFlicker(boolean enter, int rate) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusScreenMode {
        static final int TRANSACTION_addCallback = 1;
        static final int TRANSACTION_enterBrightnessChangeToFlicker = 30;
        static final int TRANSACTION_enterDCAndLowBrightnessMode = 7;
        static final int TRANSACTION_enterHighBrightnessMode = 23;
        static final int TRANSACTION_enterPSMode = 9;
        static final int TRANSACTION_enterPSModeOnRate = 10;
        static final int TRANSACTION_enterPSModeOnRateWithToken = 15;
        static final int TRANSACTION_getAppOverrideRefreshRate = 25;
        static final int TRANSACTION_getAppOverrideRefreshRateList = 26;
        static final int TRANSACTION_getDisableOverrideViewList = 14;
        static final int TRANSACTION_getDownScale = 22;
        static final int TRANSACTION_getGameList = 13;
        static final int TRANSACTION_isDisplayCompat = 8;
        static final int TRANSACTION_keepHighRefreshRate = 17;
        static final int TRANSACTION_overrideWindowRefreshRate = 16;
        static final int TRANSACTION_remove = 2;
        static final int TRANSACTION_removeAllCustomizeRefreshRate = 28;
        static final int TRANSACTION_removeCustomizeRefreshRate = 27;
        static final int TRANSACTION_requestGameRefreshRate = 11;
        static final int TRANSACTION_requestMemcRefreshRate = 20;
        static final int TRANSACTION_requestNearFlashWithToken = 29;
        static final int TRANSACTION_requestRefreshRate = 4;
        static final int TRANSACTION_requestRefreshRateWithToken = 12;
        static final int TRANSACTION_setAppOverrideRefreshRate = 24;
        static final int TRANSACTION_setClientRefreshRate = 3;
        static final int TRANSACTION_setDownScale = 21;
        static final int TRANSACTION_setHighTemperatureStatus = 6;
        static final int TRANSACTION_setMemcWorkStatus = 19;
        static final int TRANSACTION_supportDisplayCompat = 5;
        static final int TRANSACTION_updateFpsWhenDcChange = 18;

        public Stub() {
            attachInterface(this, IOplusScreenMode.DESCRIPTOR);
        }

        public static IOplusScreenMode asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusScreenMode.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusScreenMode)) {
                return (IOplusScreenMode) iin;
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
                    return "addCallback";
                case 2:
                    return "remove";
                case 3:
                    return "setClientRefreshRate";
                case 4:
                    return "requestRefreshRate";
                case 5:
                    return "supportDisplayCompat";
                case 6:
                    return "setHighTemperatureStatus";
                case 7:
                    return "enterDCAndLowBrightnessMode";
                case 8:
                    return "isDisplayCompat";
                case 9:
                    return "enterPSMode";
                case 10:
                    return "enterPSModeOnRate";
                case 11:
                    return "requestGameRefreshRate";
                case 12:
                    return "requestRefreshRateWithToken";
                case 13:
                    return "getGameList";
                case 14:
                    return "getDisableOverrideViewList";
                case 15:
                    return "enterPSModeOnRateWithToken";
                case 16:
                    return "overrideWindowRefreshRate";
                case 17:
                    return "keepHighRefreshRate";
                case 18:
                    return "updateFpsWhenDcChange";
                case 19:
                    return "setMemcWorkStatus";
                case 20:
                    return "requestMemcRefreshRate";
                case 21:
                    return "setDownScale";
                case 22:
                    return "getDownScale";
                case 23:
                    return "enterHighBrightnessMode";
                case 24:
                    return "setAppOverrideRefreshRate";
                case 25:
                    return "getAppOverrideRefreshRate";
                case 26:
                    return "getAppOverrideRefreshRateList";
                case 27:
                    return "removeCustomizeRefreshRate";
                case 28:
                    return "removeAllCustomizeRefreshRate";
                case 29:
                    return "requestNearFlashWithToken";
                case 30:
                    return "enterBrightnessChangeToFlicker";
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
                data.enforceInterface(IOplusScreenMode.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusScreenMode.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            IOplusScreenModeCallback _arg1 = IOplusScreenModeCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            addCallback(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            IOplusScreenModeCallback _arg12 = IOplusScreenModeCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            remove(_arg02, _arg12);
                            reply.writeNoException();
                            return true;
                        case 3:
                            IBinder _arg03 = data.readStrongBinder();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            setClientRefreshRate(_arg03, _arg13);
                            return true;
                        case 4:
                            boolean _arg04 = data.readBoolean();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result = requestRefreshRate(_arg04, _arg14);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result2 = supportDisplayCompat(_arg05, _arg15);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg16 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result3 = setHighTemperatureStatus(_arg06, _arg16);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 7:
                            boolean _arg07 = data.readBoolean();
                            data.enforceNoDataAvail();
                            enterDCAndLowBrightnessMode(_arg07);
                            reply.writeNoException();
                            return true;
                        case 8:
                            String _arg08 = data.readString();
                            int _arg17 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result4 = isDisplayCompat(_arg08, _arg17);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 9:
                            boolean _arg09 = data.readBoolean();
                            data.enforceNoDataAvail();
                            enterPSMode(_arg09);
                            reply.writeNoException();
                            return true;
                        case 10:
                            boolean _arg010 = data.readBoolean();
                            int _arg18 = data.readInt();
                            data.enforceNoDataAvail();
                            enterPSModeOnRate(_arg010, _arg18);
                            reply.writeNoException();
                            return true;
                        case 11:
                            String _arg011 = data.readString();
                            int _arg19 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result5 = requestGameRefreshRate(_arg011, _arg19);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 12:
                            boolean _arg012 = data.readBoolean();
                            int _arg110 = data.readInt();
                            IBinder _arg2 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            boolean _result6 = requestRefreshRateWithToken(_arg012, _arg110, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 13:
                            Bundle _arg013 = new Bundle();
                            data.enforceNoDataAvail();
                            boolean _result7 = getGameList(_arg013);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            reply.writeTypedObject(_arg013, 1);
                            return true;
                        case 14:
                            String _arg014 = data.readString();
                            data.enforceNoDataAvail();
                            String _result8 = getDisableOverrideViewList(_arg014);
                            reply.writeNoException();
                            reply.writeString(_result8);
                            return true;
                        case 15:
                            boolean _arg015 = data.readBoolean();
                            int _arg111 = data.readInt();
                            IBinder _arg22 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            enterPSModeOnRateWithToken(_arg015, _arg111, _arg22);
                            reply.writeNoException();
                            return true;
                        case 16:
                            IBinder _arg016 = data.readStrongBinder();
                            int _arg112 = data.readInt();
                            data.enforceNoDataAvail();
                            overrideWindowRefreshRate(_arg016, _arg112);
                            return true;
                        case 17:
                            int _arg017 = data.readInt();
                            data.enforceNoDataAvail();
                            keepHighRefreshRate(_arg017);
                            return true;
                        case 18:
                            boolean _arg018 = data.readBoolean();
                            data.enforceNoDataAvail();
                            updateFpsWhenDcChange(_arg018);
                            reply.writeNoException();
                            return true;
                        case 19:
                            boolean _arg019 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setMemcWorkStatus(_arg019);
                            return true;
                        case 20:
                            boolean _arg020 = data.readBoolean();
                            int _arg113 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result9 = requestMemcRefreshRate(_arg020, _arg113);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 21:
                            String _arg021 = data.readString();
                            float _arg114 = data.readFloat();
                            boolean _arg23 = data.readBoolean();
                            boolean _arg3 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result10 = setDownScale(_arg021, _arg114, _arg23, _arg3);
                            reply.writeNoException();
                            reply.writeInt(_result10);
                            return true;
                        case 22:
                            String _arg022 = data.readString();
                            data.enforceNoDataAvail();
                            Bundle _result11 = getDownScale(_arg022);
                            reply.writeNoException();
                            reply.writeTypedObject(_result11, 1);
                            return true;
                        case 23:
                            boolean _arg023 = data.readBoolean();
                            int _arg115 = data.readInt();
                            data.enforceNoDataAvail();
                            enterHighBrightnessMode(_arg023, _arg115);
                            return true;
                        case 24:
                            String _arg024 = data.readString();
                            int _arg116 = data.readInt();
                            int _arg24 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result12 = setAppOverrideRefreshRate(_arg024, _arg116, _arg24);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 25:
                            String _arg025 = data.readString();
                            int _arg117 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result13 = getAppOverrideRefreshRate(_arg025, _arg117);
                            reply.writeNoException();
                            reply.writeInt(_result13);
                            return true;
                        case 26:
                            Bundle _result14 = getAppOverrideRefreshRateList();
                            reply.writeNoException();
                            reply.writeTypedObject(_result14, 1);
                            return true;
                        case 27:
                            String _arg026 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result15 = removeCustomizeRefreshRate(_arg026);
                            reply.writeNoException();
                            reply.writeBoolean(_result15);
                            return true;
                        case 28:
                            boolean _result16 = removeAllCustomizeRefreshRate();
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        case 29:
                            boolean _arg027 = data.readBoolean();
                            int _arg118 = data.readInt();
                            IBinder _arg25 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            boolean _result17 = requestNearFlashWithToken(_arg027, _arg118, _arg25);
                            reply.writeNoException();
                            reply.writeBoolean(_result17);
                            return true;
                        case 30:
                            boolean _arg028 = data.readBoolean();
                            int _arg119 = data.readInt();
                            data.enforceNoDataAvail();
                            enterBrightnessChangeToFlicker(_arg028, _arg119);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusScreenMode {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusScreenMode.DESCRIPTOR;
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void addCallback(int index, IOplusScreenModeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeInt(index);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void remove(int index, IOplusScreenModeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeInt(index);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void setClientRefreshRate(IBinder token, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(rate);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean requestRefreshRate(boolean open, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(open);
                    _data.writeInt(rate);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean supportDisplayCompat(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean setHighTemperatureStatus(int status, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(rate);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void enterDCAndLowBrightnessMode(boolean enter) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(enter);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean isDisplayCompat(String packageName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void enterPSMode(boolean enter) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(enter);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void enterPSModeOnRate(boolean enter, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(enter);
                    _data.writeInt(rate);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean requestGameRefreshRate(String packageName, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(rate);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean requestRefreshRateWithToken(boolean open, int rate, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(open);
                    _data.writeInt(rate);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean getGameList(Bundle outBundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    if (_reply.readInt() != 0) {
                        outBundle.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public String getDisableOverrideViewList(String key) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeString(key);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void enterPSModeOnRateWithToken(boolean open, int rate, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(open);
                    _data.writeInt(rate);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void overrideWindowRefreshRate(IBinder window, int refreshRateId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeStrongBinder(window);
                    _data.writeInt(refreshRateId);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void keepHighRefreshRate(int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void updateFpsWhenDcChange(boolean enter) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(enter);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void setMemcWorkStatus(boolean memc) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(memc);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean requestMemcRefreshRate(boolean open, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(open);
                    _data.writeInt(rate);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public int setDownScale(String pkgName, float downscale, boolean enable, boolean originMode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeFloat(downscale);
                    _data.writeBoolean(enable);
                    _data.writeBoolean(originMode);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public Bundle getDownScale(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void enterHighBrightnessMode(boolean enter, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(enter);
                    _data.writeInt(rate);
                    this.mRemote.transact(23, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean setAppOverrideRefreshRate(String pkg, int mode, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(mode);
                    _data.writeInt(rate);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public int getAppOverrideRefreshRate(String pkg, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(mode);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public Bundle getAppOverrideRefreshRateList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean removeCustomizeRefreshRate(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean removeAllCustomizeRefreshRate() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public boolean requestNearFlashWithToken(boolean enter, int rate, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(enter);
                    _data.writeInt(rate);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenmode.IOplusScreenMode
            public void enterBrightnessChangeToFlicker(boolean enter, int rate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenMode.DESCRIPTOR);
                    _data.writeBoolean(enter);
                    _data.writeInt(rate);
                    this.mRemote.transact(30, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 29;
        }
    }
}
