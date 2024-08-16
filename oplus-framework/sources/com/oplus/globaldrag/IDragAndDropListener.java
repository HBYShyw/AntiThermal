package com.oplus.globaldrag;

import android.content.ClipData;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.SurfaceControl;

/* loaded from: classes.dex */
public interface IDragAndDropListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.globaldrag.IDragAndDropListener";

    void postCancelDragAndDrop() throws RemoteException;

    void postEndDrag() throws RemoteException;

    void postPerformDrag() throws RemoteException;

    void postReportDropResult() throws RemoteException;

    void prePerformDrag(String str, SurfaceControl surfaceControl, Bundle bundle, ClipData clipData) throws RemoteException;

    void preReportDropResult(String str, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IDragAndDropListener {
        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void prePerformDrag(String name, SurfaceControl surface, Bundle bundle, ClipData data) throws RemoteException {
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void postPerformDrag() throws RemoteException {
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void preReportDropResult(String name, boolean consumed) throws RemoteException {
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void postReportDropResult() throws RemoteException {
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void postCancelDragAndDrop() throws RemoteException {
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void postEndDrag() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IDragAndDropListener {
        static final int TRANSACTION_postCancelDragAndDrop = 5;
        static final int TRANSACTION_postEndDrag = 6;
        static final int TRANSACTION_postPerformDrag = 2;
        static final int TRANSACTION_postReportDropResult = 4;
        static final int TRANSACTION_prePerformDrag = 1;
        static final int TRANSACTION_preReportDropResult = 3;

        public Stub() {
            attachInterface(this, IDragAndDropListener.DESCRIPTOR);
        }

        public static IDragAndDropListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IDragAndDropListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IDragAndDropListener)) {
                return (IDragAndDropListener) iin;
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
                    return "prePerformDrag";
                case 2:
                    return "postPerformDrag";
                case 3:
                    return "preReportDropResult";
                case 4:
                    return "postReportDropResult";
                case 5:
                    return "postCancelDragAndDrop";
                case 6:
                    return "postEndDrag";
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
                data.enforceInterface(IDragAndDropListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IDragAndDropListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            SurfaceControl _arg1 = (SurfaceControl) data.readTypedObject(SurfaceControl.CREATOR);
                            Bundle _arg2 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            ClipData _arg3 = (ClipData) data.readTypedObject(ClipData.CREATOR);
                            data.enforceNoDataAvail();
                            prePerformDrag(_arg0, _arg1, _arg2, _arg3);
                            return true;
                        case 2:
                            postPerformDrag();
                            return true;
                        case 3:
                            String _arg02 = data.readString();
                            boolean _arg12 = data.readBoolean();
                            data.enforceNoDataAvail();
                            preReportDropResult(_arg02, _arg12);
                            return true;
                        case 4:
                            postReportDropResult();
                            return true;
                        case 5:
                            postCancelDragAndDrop();
                            return true;
                        case 6:
                            postEndDrag();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IDragAndDropListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IDragAndDropListener.DESCRIPTOR;
            }

            @Override // com.oplus.globaldrag.IDragAndDropListener
            public void prePerformDrag(String name, SurfaceControl surface, Bundle bundle, ClipData data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IDragAndDropListener.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeTypedObject(surface, 0);
                    _data.writeTypedObject(bundle, 0);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.globaldrag.IDragAndDropListener
            public void postPerformDrag() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IDragAndDropListener.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.globaldrag.IDragAndDropListener
            public void preReportDropResult(String name, boolean consumed) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IDragAndDropListener.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeBoolean(consumed);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.globaldrag.IDragAndDropListener
            public void postReportDropResult() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IDragAndDropListener.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.globaldrag.IDragAndDropListener
            public void postCancelDragAndDrop() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IDragAndDropListener.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.globaldrag.IDragAndDropListener
            public void postEndDrag() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IDragAndDropListener.DESCRIPTOR);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 5;
        }
    }
}
