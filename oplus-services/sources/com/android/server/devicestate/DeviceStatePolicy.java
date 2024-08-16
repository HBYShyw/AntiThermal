package com.android.server.devicestate;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.android.server.policy.DeviceStatePolicyImpl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class DeviceStatePolicy {
    protected final Context mContext;

    public abstract void configureDeviceForState(int i, Runnable runnable);

    public abstract DeviceStateProvider getDeviceStateProvider();

    protected DeviceStatePolicy(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class DefaultProvider implements Provider {
        DefaultProvider() {
        }

        @Override // com.android.server.devicestate.DeviceStatePolicy.Provider
        public DeviceStatePolicy instantiate(Context context) {
            return new DeviceStatePolicyImpl(context);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Provider {
        DeviceStatePolicy instantiate(Context context);

        static Provider fromResources(Resources resources) {
            String string = resources.getString(R.string.config_usbConfirmActivity);
            if (TextUtils.isEmpty(string)) {
                return new DefaultProvider();
            }
            try {
                return (Provider) Class.forName(string).newInstance();
            } catch (ClassCastException | ReflectiveOperationException e) {
                throw new IllegalStateException("Couldn't instantiate class " + string + " for config_deviceSpecificDeviceStatePolicyProvider: make sure it has a public zero-argument constructor and implements DeviceStatePolicy.Provider", e);
            }
        }
    }
}
