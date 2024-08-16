package com.android.server.soundtrigger_middleware;

import android.media.soundtrigger_middleware.IInjectGlobalEvent;
import android.media.soundtrigger_middleware.ISoundTriggerInjection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.soundtrigger_middleware.FakeHalFactory;
import com.android.server.soundtrigger_middleware.FakeSoundTriggerHal;
import java.util.concurrent.Executor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class FakeHalFactory implements HalFactory {
    private static final String TAG = "FakeHalFactory";
    private final ISoundTriggerInjection mInjection;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FakeHalFactory(ISoundTriggerInjection iSoundTriggerInjection) {
        this.mInjection = iSoundTriggerInjection;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.soundtrigger_middleware.FakeSoundTriggerHal, android.os.IBinder] */
    @Override // com.android.server.soundtrigger_middleware.HalFactory
    public ISoundTriggerHal create() {
        ?? fakeSoundTriggerHal = new FakeSoundTriggerHal(this.mInjection);
        final IInjectGlobalEvent globalEventInjection = fakeSoundTriggerHal.getGlobalEventInjection();
        return new AnonymousClass1(fakeSoundTriggerHal, new Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeHalFactory$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FakeHalFactory.lambda$create$0(globalEventInjection);
            }
        }, globalEventInjection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$create$0(IInjectGlobalEvent iInjectGlobalEvent) {
        try {
            iInjectGlobalEvent.triggerRestart();
        } catch (RemoteException unused) {
            Slog.wtf(TAG, "Unexpected RemoteException from same process");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.soundtrigger_middleware.FakeHalFactory$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 extends SoundTriggerHw3Compat {
        final /* synthetic */ IInjectGlobalEvent val$session;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(IBinder iBinder, Runnable runnable, IInjectGlobalEvent iInjectGlobalEvent) {
            super(iBinder, runnable);
            this.val$session = iInjectGlobalEvent;
        }

        @Override // com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat, com.android.server.soundtrigger_middleware.ISoundTriggerHal
        public void detach() {
            Executor executor = FakeSoundTriggerHal.ExecutorHolder.INJECTION_EXECUTOR;
            final IInjectGlobalEvent iInjectGlobalEvent = this.val$session;
            executor.execute(new Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeHalFactory$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FakeHalFactory.AnonymousClass1.this.lambda$detach$0(iInjectGlobalEvent);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$detach$0(IInjectGlobalEvent iInjectGlobalEvent) {
            try {
                FakeHalFactory.this.mInjection.onFrameworkDetached(iInjectGlobalEvent);
            } catch (RemoteException unused) {
                Slog.wtf(FakeHalFactory.TAG, "Unexpected RemoteException from same process");
            }
        }

        @Override // com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat, com.android.server.soundtrigger_middleware.ISoundTriggerHal
        public void clientAttached(final IBinder iBinder) {
            Executor executor = FakeSoundTriggerHal.ExecutorHolder.INJECTION_EXECUTOR;
            final IInjectGlobalEvent iInjectGlobalEvent = this.val$session;
            executor.execute(new Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeHalFactory$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    FakeHalFactory.AnonymousClass1.this.lambda$clientAttached$1(iBinder, iInjectGlobalEvent);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$clientAttached$1(IBinder iBinder, IInjectGlobalEvent iInjectGlobalEvent) {
            try {
                FakeHalFactory.this.mInjection.onClientAttached(iBinder, iInjectGlobalEvent);
            } catch (RemoteException unused) {
                Slog.wtf(FakeHalFactory.TAG, "Unexpected RemoteException from same process");
            }
        }

        @Override // com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat, com.android.server.soundtrigger_middleware.ISoundTriggerHal
        public void clientDetached(final IBinder iBinder) {
            FakeSoundTriggerHal.ExecutorHolder.INJECTION_EXECUTOR.execute(new Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeHalFactory$1$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    FakeHalFactory.AnonymousClass1.this.lambda$clientDetached$2(iBinder);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$clientDetached$2(IBinder iBinder) {
            try {
                FakeHalFactory.this.mInjection.onClientDetached(iBinder);
            } catch (RemoteException unused) {
                Slog.wtf(FakeHalFactory.TAG, "Unexpected RemoteException from same process");
            }
        }
    }
}
