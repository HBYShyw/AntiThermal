package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener;
import java.util.List;

/* loaded from: classes.dex */
public interface IEventHandleService extends IInterface {

    /* loaded from: classes.dex */
    public static class Default implements IEventHandleService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
        public List<Event> getAvailableEvent() {
            return null;
        }

        @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
        public boolean isAvailableEvent(Event event) {
            return false;
        }

        @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
        public void queryEvent(Event event, IEventQueryListener iEventQueryListener) {
        }

        @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
        public void queryEvents(EventConfig eventConfig, IEventQueryListener iEventQueryListener) {
        }

        @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
        public int registerEventCallback(String str, IEventCallback iEventCallback, EventConfig eventConfig) {
            return 0;
        }

        @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
        public void triggerHookEvent(TriggerEvent triggerEvent) {
        }

        @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
        public int unregisterEventCallback(String str) {
            return 0;
        }

        @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
        public int unregisterEventCallbackWithArgs(String str, EventConfig eventConfig) {
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IEventHandleService {
        private static final String DESCRIPTOR = "com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService";
        static final int TRANSACTION_getAvailableEvent = 6;
        static final int TRANSACTION_isAvailableEvent = 7;
        static final int TRANSACTION_queryEvent = 10;
        static final int TRANSACTION_queryEvents = 11;
        static final int TRANSACTION_registerEventCallback = 4;
        static final int TRANSACTION_triggerHookEvent = 1;
        static final int TRANSACTION_unregisterEventCallback = 5;
        static final int TRANSACTION_unregisterEventCallbackWithArgs = 9;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IEventHandleService {
            public static IEventHandleService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
            public List<Event> getAvailableEvent() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAvailableEvent();
                    }
                    obtain2.readException();
                    return obtain2.createTypedArrayList(Event.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
            public boolean isAvailableEvent(Event event) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        obtain.writeInt(1);
                        event.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(7, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAvailableEvent(event);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
            public void queryEvent(Event event, IEventQueryListener iEventQueryListener) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        obtain.writeInt(1);
                        event.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iEventQueryListener != null ? iEventQueryListener.asBinder() : null);
                    if (this.mRemote.transact(10, obtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().queryEvent(event, iEventQueryListener);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
            public void queryEvents(EventConfig eventConfig, IEventQueryListener iEventQueryListener) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (eventConfig != null) {
                        obtain.writeInt(1);
                        eventConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iEventQueryListener != null ? iEventQueryListener.asBinder() : null);
                    if (this.mRemote.transact(11, obtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().queryEvents(eventConfig, iEventQueryListener);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
            public int registerEventCallback(String str, IEventCallback iEventCallback, EventConfig eventConfig) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iEventCallback != null ? iEventCallback.asBinder() : null);
                    if (eventConfig != null) {
                        obtain.writeInt(1);
                        eventConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerEventCallback(str, iEventCallback, eventConfig);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
            public void triggerHookEvent(TriggerEvent triggerEvent) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (triggerEvent != null) {
                        obtain.writeInt(1);
                        triggerEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, obtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().triggerHookEvent(triggerEvent);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
            public int unregisterEventCallback(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unregisterEventCallback(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventHandleService
            public int unregisterEventCallbackWithArgs(String str, EventConfig eventConfig) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (eventConfig != null) {
                        obtain.writeInt(1);
                        eventConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(9, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unregisterEventCallbackWithArgs(str, eventConfig);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEventHandleService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IEventHandleService)) {
                return (IEventHandleService) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public static IEventHandleService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IEventHandleService iEventHandleService) {
            if (Proxy.sDefaultImpl != null || iEventHandleService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iEventHandleService;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i10, Parcel parcel, Parcel parcel2, int i11) {
            if (i10 == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                triggerHookEvent(parcel.readInt() != 0 ? TriggerEvent.CREATOR.createFromParcel(parcel) : null);
                return true;
            }
            if (i10 == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            if (i10 == 4) {
                parcel.enforceInterface(DESCRIPTOR);
                int registerEventCallback = registerEventCallback(parcel.readString(), IEventCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? EventConfig.CREATOR.createFromParcel(parcel) : null);
                parcel2.writeNoException();
                parcel2.writeInt(registerEventCallback);
                return true;
            }
            if (i10 == 5) {
                parcel.enforceInterface(DESCRIPTOR);
                int unregisterEventCallback = unregisterEventCallback(parcel.readString());
                parcel2.writeNoException();
                parcel2.writeInt(unregisterEventCallback);
                return true;
            }
            if (i10 == 6) {
                parcel.enforceInterface(DESCRIPTOR);
                List<Event> availableEvent = getAvailableEvent();
                parcel2.writeNoException();
                parcel2.writeTypedList(availableEvent);
                return true;
            }
            if (i10 != 7) {
                switch (i10) {
                    case 9:
                        parcel.enforceInterface(DESCRIPTOR);
                        int unregisterEventCallbackWithArgs = unregisterEventCallbackWithArgs(parcel.readString(), parcel.readInt() != 0 ? EventConfig.CREATOR.createFromParcel(parcel) : null);
                        parcel2.writeNoException();
                        parcel2.writeInt(unregisterEventCallbackWithArgs);
                        return true;
                    case 10:
                        parcel.enforceInterface(DESCRIPTOR);
                        queryEvent(parcel.readInt() != 0 ? Event.CREATOR.createFromParcel(parcel) : null, IEventQueryListener.Stub.asInterface(parcel.readStrongBinder()));
                        return true;
                    case 11:
                        parcel.enforceInterface(DESCRIPTOR);
                        queryEvents(parcel.readInt() != 0 ? EventConfig.CREATOR.createFromParcel(parcel) : null, IEventQueryListener.Stub.asInterface(parcel.readStrongBinder()));
                        return true;
                    default:
                        return super.onTransact(i10, parcel, parcel2, i11);
                }
            }
            parcel.enforceInterface(DESCRIPTOR);
            boolean isAvailableEvent = isAvailableEvent(parcel.readInt() != 0 ? Event.CREATOR.createFromParcel(parcel) : null);
            parcel2.writeNoException();
            parcel2.writeInt(isAvailableEvent ? 1 : 0);
            return true;
        }
    }

    List<Event> getAvailableEvent();

    boolean isAvailableEvent(Event event);

    void queryEvent(Event event, IEventQueryListener iEventQueryListener);

    void queryEvents(EventConfig eventConfig, IEventQueryListener iEventQueryListener);

    int registerEventCallback(String str, IEventCallback iEventCallback, EventConfig eventConfig);

    void triggerHookEvent(TriggerEvent triggerEvent);

    int unregisterEventCallback(String str);

    int unregisterEventCallbackWithArgs(String str, EventConfig eventConfig);
}
