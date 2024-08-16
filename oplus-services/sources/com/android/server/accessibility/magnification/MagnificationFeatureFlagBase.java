package com.android.server.accessibility.magnification;

import android.os.Binder;
import android.provider.DeviceConfig;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FunctionalUtils;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class MagnificationFeatureFlagBase {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addOnChangedListener$7() {
    }

    abstract boolean getDefaultValue();

    abstract String getFeatureName();

    abstract String getNamespace();

    private void clearCallingIdentifyAndTryCatch(final Runnable runnable, Runnable runnable2) {
        try {
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    runnable.run();
                }
            });
        } catch (Throwable unused) {
            runnable2.run();
        }
    }

    public boolean isFeatureFlagEnabled() {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(getDefaultValue());
        clearCallingIdentifyAndTryCatch(new Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MagnificationFeatureFlagBase.this.lambda$isFeatureFlagEnabled$1(atomicBoolean);
            }
        }, new Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MagnificationFeatureFlagBase.this.lambda$isFeatureFlagEnabled$2(atomicBoolean);
            }
        });
        return atomicBoolean.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$isFeatureFlagEnabled$1(AtomicBoolean atomicBoolean) {
        atomicBoolean.set(DeviceConfig.getBoolean(getNamespace(), getFeatureName(), getDefaultValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$isFeatureFlagEnabled$2(AtomicBoolean atomicBoolean) {
        atomicBoolean.set(getDefaultValue());
    }

    @VisibleForTesting
    public boolean setFeatureFlagEnabled(final boolean z) {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(getDefaultValue());
        clearCallingIdentifyAndTryCatch(new Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                MagnificationFeatureFlagBase.this.lambda$setFeatureFlagEnabled$3(atomicBoolean, z);
            }
        }, new Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                MagnificationFeatureFlagBase.this.lambda$setFeatureFlagEnabled$4(atomicBoolean);
            }
        });
        return atomicBoolean.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setFeatureFlagEnabled$3(AtomicBoolean atomicBoolean, boolean z) {
        atomicBoolean.set(DeviceConfig.setProperty(getNamespace(), getFeatureName(), Boolean.toString(z), false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setFeatureFlagEnabled$4(AtomicBoolean atomicBoolean) {
        atomicBoolean.set(getDefaultValue());
    }

    public DeviceConfig.OnPropertiesChangedListener addOnChangedListener(final Executor executor, final Runnable runnable) {
        final DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda3
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                MagnificationFeatureFlagBase.this.lambda$addOnChangedListener$5(runnable, properties);
            }
        };
        clearCallingIdentifyAndTryCatch(new Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                MagnificationFeatureFlagBase.this.lambda$addOnChangedListener$6(executor, onPropertiesChangedListener);
            }
        }, new Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                MagnificationFeatureFlagBase.lambda$addOnChangedListener$7();
            }
        });
        return onPropertiesChangedListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addOnChangedListener$5(Runnable runnable, DeviceConfig.Properties properties) {
        if (properties.getKeyset().contains(getFeatureName())) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addOnChangedListener$6(Executor executor, DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
        DeviceConfig.addOnPropertiesChangedListener(getNamespace(), executor, onPropertiesChangedListener);
    }

    public void removeOnChangedListener(DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
        DeviceConfig.removeOnPropertiesChangedListener(onPropertiesChangedListener);
    }
}
