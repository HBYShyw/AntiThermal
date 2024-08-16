package com.android.server.lights;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.light.HwLight;
import android.hardware.light.HwLightState;
import android.hardware.light.ILights;
import android.hardware.lights.ILightsManager;
import android.hardware.lights.Light;
import android.hardware.lights.LightState;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.Preconditions;
import com.android.server.SystemService;
import com.android.server.lights.LightsService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LightsService extends SystemService {
    static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    static final String TAG = "LightsService";
    private Handler mH;
    private final SparseArray<LightImpl> mLightsById;
    private final LightImpl[] mLightsByType;
    private LightsServiceWrapper mLsWrapper;

    @VisibleForTesting
    final LightsManagerBinderService mManagerService;
    private final LightsManager mService;
    private final Supplier<ILights> mVintfLights;

    static native void setLight_native(int i, int i2, int i3, int i4, int i5, int i6);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class LightsManagerBinderService extends ILightsManager.Stub {

        @GuardedBy({"LightsService.this"})
        private final List<Session> mSessions;

        private LightsManagerBinderService() {
            this.mSessions = new ArrayList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public final class Session implements Comparable<Session> {
            final int mPriority;
            final SparseArray<LightState> mRequests = new SparseArray<>();
            final IBinder mToken;

            Session(IBinder iBinder, int i) {
                this.mToken = iBinder;
                this.mPriority = i;
            }

            void setRequest(int i, LightState lightState) {
                if (lightState != null) {
                    this.mRequests.put(i, lightState);
                } else {
                    this.mRequests.remove(i);
                }
            }

            @Override // java.lang.Comparable
            public int compareTo(Session session) {
                return Integer.compare(session.mPriority, this.mPriority);
            }
        }

        public List<Light> getLights() {
            ArrayList arrayList;
            LightsService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_LIGHTS", "getLights requires CONTROL_DEVICE_LIGHTS_PERMISSION");
            synchronized (LightsService.this) {
                arrayList = new ArrayList();
                for (int i = 0; i < LightsService.this.mLightsById.size(); i++) {
                    if (!((LightImpl) LightsService.this.mLightsById.valueAt(i)).isSystemLight()) {
                        HwLight hwLight = ((LightImpl) LightsService.this.mLightsById.valueAt(i)).mHwLight;
                        arrayList.add(new Light(hwLight.id, hwLight.ordinal, hwLight.type));
                    }
                }
            }
            return arrayList;
        }

        public void setLightStates(IBinder iBinder, int[] iArr, LightState[] lightStateArr) {
            LightsService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_LIGHTS", "setLightStates requires CONTROL_DEVICE_LIGHTS permission");
            boolean z = true;
            Preconditions.checkState(iArr.length == lightStateArr.length);
            synchronized (LightsService.this) {
                Session sessionLocked = getSessionLocked((IBinder) Preconditions.checkNotNull(iBinder));
                if (sessionLocked == null) {
                    z = false;
                }
                Preconditions.checkState(z, "not registered");
                checkRequestIsValid(iArr);
                for (int i = 0; i < iArr.length; i++) {
                    sessionLocked.setRequest(iArr[i], lightStateArr[i]);
                }
                invalidateLightStatesLocked();
            }
        }

        public LightState getLightState(int i) {
            LightState lightState;
            LightsService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_LIGHTS", "getLightState(@TestApi) requires CONTROL_DEVICE_LIGHTS permission");
            synchronized (LightsService.this) {
                LightImpl lightImpl = (LightImpl) LightsService.this.mLightsById.get(i);
                if (lightImpl == null || lightImpl.isSystemLight()) {
                    throw new IllegalArgumentException("Invalid light: " + i);
                }
                lightState = new LightState(lightImpl.getColor());
            }
            return lightState;
        }

        public void openSession(final IBinder iBinder, int i) {
            LightsService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_LIGHTS", "openSession requires CONTROL_DEVICE_LIGHTS permission");
            Preconditions.checkNotNull(iBinder);
            synchronized (LightsService.this) {
                Preconditions.checkState(getSessionLocked(iBinder) == null, "already registered");
                try {
                    iBinder.linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.lights.LightsService$LightsManagerBinderService$$ExternalSyntheticLambda0
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            LightsService.LightsManagerBinderService.this.lambda$openSession$0(iBinder);
                        }
                    }, 0);
                    this.mSessions.add(new Session(iBinder, i));
                    Collections.sort(this.mSessions);
                } catch (RemoteException e) {
                    Slog.e(LightsService.TAG, "Couldn't open session, client already died", e);
                    throw new IllegalArgumentException("Client is already dead.");
                }
            }
        }

        public void closeSession(IBinder iBinder) {
            LightsService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_LIGHTS", "closeSession requires CONTROL_DEVICE_LIGHTS permission");
            Preconditions.checkNotNull(iBinder);
            lambda$openSession$0(iBinder);
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(LightsService.this.getContext(), LightsService.TAG, printWriter)) {
                synchronized (LightsService.this) {
                    if (LightsService.this.mVintfLights != null) {
                        printWriter.println("Service: aidl (" + LightsService.this.mVintfLights.get() + ")");
                    } else {
                        printWriter.println("Service: hidl");
                    }
                    printWriter.println("Lights:");
                    for (int i = 0; i < LightsService.this.mLightsById.size(); i++) {
                        LightImpl lightImpl = (LightImpl) LightsService.this.mLightsById.valueAt(i);
                        printWriter.println(String.format("  Light id=%d ordinal=%d color=%08x", Integer.valueOf(lightImpl.mHwLight.id), Integer.valueOf(lightImpl.mHwLight.ordinal), Integer.valueOf(lightImpl.getColor())));
                    }
                    printWriter.println("Session clients:");
                    for (Session session : this.mSessions) {
                        printWriter.println("  Session token=" + session.mToken);
                        for (int i2 = 0; i2 < session.mRequests.size(); i2++) {
                            printWriter.println(String.format("    Request id=%d color=%08x", Integer.valueOf(session.mRequests.keyAt(i2)), Integer.valueOf(session.mRequests.valueAt(i2).getColor())));
                        }
                    }
                    LightsService.this.mLsWrapper.getExtImpl().dumpOplus(printWriter);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: closeSessionInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$openSession$0(IBinder iBinder) {
            synchronized (LightsService.this) {
                Session sessionLocked = getSessionLocked(iBinder);
                if (sessionLocked != null) {
                    this.mSessions.remove(sessionLocked);
                    invalidateLightStatesLocked();
                }
            }
        }

        private void checkRequestIsValid(int[] iArr) {
            for (int i : iArr) {
                LightImpl lightImpl = (LightImpl) LightsService.this.mLightsById.get(i);
                Preconditions.checkState((lightImpl == null || lightImpl.isSystemLight()) ? false : true, "Invalid lightId " + i);
            }
        }

        private void invalidateLightStatesLocked() {
            int i;
            HashMap hashMap = new HashMap();
            int size = this.mSessions.size();
            while (true) {
                size--;
                i = 0;
                if (size < 0) {
                    break;
                }
                SparseArray<LightState> sparseArray = this.mSessions.get(size).mRequests;
                while (i < sparseArray.size()) {
                    hashMap.put(Integer.valueOf(sparseArray.keyAt(i)), sparseArray.valueAt(i));
                    i++;
                }
            }
            while (i < LightsService.this.mLightsById.size()) {
                LightImpl lightImpl = (LightImpl) LightsService.this.mLightsById.valueAt(i);
                if (!lightImpl.isSystemLight()) {
                    LightState lightState = (LightState) hashMap.get(Integer.valueOf(lightImpl.mHwLight.id));
                    if (lightState != null) {
                        lightImpl.setColor(lightState.getColor());
                    } else {
                        lightImpl.turnOff();
                    }
                }
                i++;
            }
        }

        private Session getSessionLocked(IBinder iBinder) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                if (iBinder.equals(this.mSessions.get(i).mToken)) {
                    return this.mSessions.get(i);
                }
            }
            return null;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class LightImpl extends LogicalLight {
        private int mBrightnessMode;
        private int mColor;
        private boolean mFlashing;
        private HwLight mHwLight;
        private boolean mInitialized;
        private int mLastBrightnessMode;
        private int mLastColor;
        private int mMode;
        private int mOffMS;
        private int mOnMS;
        private boolean mUseLowPersistenceForVR;
        private boolean mVrModeEnabled;

        private LightImpl(Context context, HwLight hwLight) {
            this.mHwLight = hwLight;
        }

        @Override // com.android.server.lights.LogicalLight
        public void setBrightness(float f) {
            setBrightness(f, 0);
        }

        @Override // com.android.server.lights.LogicalLight
        public void setBrightness(float f, int i) {
            if (Float.isNaN(f)) {
                Slog.w(LightsService.TAG, "Brightness is not valid: " + f);
                return;
            }
            synchronized (this) {
                if (i == 2) {
                    Slog.w(LightsService.TAG, "setBrightness with LOW_PERSISTENCE unexpected #" + this.mHwLight.id + ": brightness=" + f);
                    return;
                }
                LightsService.this.mLsWrapper.getExtImpl().onSetLight(this.mHwLight.id, (int) f, i);
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void setColor(int i) {
            synchronized (this) {
                LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("setColor");
                setLightLocked(i, 0, 0, 0, 0);
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void setFlashing(int i, int i2, int i3, int i4) {
            synchronized (this) {
                LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("setFlashing");
                setLightLocked(i, i2, i3, i4, 0);
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void pulse() {
            pulse(16777215, 7);
        }

        @Override // com.android.server.lights.LogicalLight
        public void pulse(int i, int i2) {
            synchronized (this) {
                LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("pulse");
                if (this.mColor == 0 && !this.mFlashing) {
                    setLightLocked(i, 2, i2, 1000, 0);
                    this.mColor = 0;
                    LightsService.this.mH.postDelayed(new Runnable() { // from class: com.android.server.lights.LightsService$LightImpl$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            LightsService.LightImpl.this.stopFlashing();
                        }
                    }, i2);
                }
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void turnOff() {
            synchronized (this) {
                LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("turnOff");
                setLightLocked(0, 0, 0, 0, 0);
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void setVrMode(boolean z) {
            synchronized (this) {
                if (this.mVrModeEnabled != z) {
                    this.mVrModeEnabled = z;
                    this.mUseLowPersistenceForVR = LightsService.this.getVrDisplayMode() == 0;
                    if (shouldBeInLowPersistenceMode()) {
                        this.mLastBrightnessMode = this.mBrightnessMode;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stopFlashing() {
            synchronized (this) {
                LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("stopFlashing");
                setLightLocked(this.mColor, 0, 0, 0, 0);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLightLocked(int i, int i2, int i3, int i4, int i5) {
            int i6;
            if (shouldBeInLowPersistenceMode()) {
                i6 = 2;
            } else {
                if (i5 == 2) {
                    i5 = this.mLastBrightnessMode;
                }
                i6 = i5;
            }
            if (this.mInitialized && i == this.mColor && i2 == this.mMode && i3 == this.mOnMS && i4 == this.mOffMS && this.mBrightnessMode == i6 && this.mHwLight.type != 1) {
                return;
            }
            if (LightsService.DEBUG) {
                Slog.v(LightsService.TAG, "setLight #" + this.mHwLight.id + ": color=#" + Integer.toHexString(i) + ": brightnessMode=" + i6);
            }
            this.mInitialized = true;
            this.mLastColor = this.mColor;
            this.mColor = i;
            this.mMode = i2;
            this.mOnMS = i3;
            this.mOffMS = i4;
            this.mBrightnessMode = i6;
            ILightsServiceExt extImpl = LightsService.this.mLsWrapper.getExtImpl();
            HwLight hwLight = this.mHwLight;
            extImpl.setOplusLightUnchecked(hwLight.type, hwLight.id, i, i2, i3, i4, i6);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLightUnchecked(int i, int i2, int i3, int i4, int i5) {
            Trace.traceBegin(131072L, "setLightState(" + this.mHwLight.id + ", 0x" + Integer.toHexString(i) + ")");
            try {
                try {
                    if (LightsService.DEBUG) {
                        Slog.v(LightsService.TAG, "setLightUnchecked #" + this.mHwLight.id + ": color=#" + Integer.toHexString(i) + ": brightnessMode=" + i5);
                    }
                    if (LightsService.this.mVintfLights != null) {
                        HwLightState hwLightState = new HwLightState();
                        hwLightState.color = i;
                        hwLightState.flashMode = (byte) i2;
                        hwLightState.flashOnMs = i3;
                        hwLightState.flashOffMs = i4;
                        hwLightState.brightnessMode = (byte) i5;
                        ((ILights) LightsService.this.mVintfLights.get()).setLightState(this.mHwLight.id, hwLightState);
                    } else {
                        LightsService.setLight_native(this.mHwLight.id, i, i2, i3, i4, i5);
                    }
                } catch (RemoteException | UnsupportedOperationException e) {
                    Slog.e(LightsService.TAG, "Failed issuing setLightState", e);
                }
            } finally {
                Trace.traceEnd(131072L);
            }
        }

        private boolean shouldBeInLowPersistenceMode() {
            return this.mVrModeEnabled && this.mUseLowPersistenceForVR;
        }

        HwLight getHwLight() {
            return this.mHwLight;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isSystemLight() {
            byte b = this.mHwLight.type;
            return b >= 0 && b < 8;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getColor() {
            return this.mColor;
        }
    }

    public LightsService(Context context) {
        this(context, new VintfHalCache(), Looper.myLooper());
    }

    @VisibleForTesting
    LightsService(Context context, Supplier<ILights> supplier, Looper looper) {
        super(context);
        this.mLightsByType = new LightImpl[8];
        this.mLightsById = new SparseArray<>();
        this.mService = new LightsManager() { // from class: com.android.server.lights.LightsService.1
            @Override // com.android.server.lights.LightsManager
            public LogicalLight getLight(int i) {
                if (LightsService.this.mLightsByType == null || i < 0 || i >= LightsService.this.mLightsByType.length) {
                    return null;
                }
                return LightsService.this.mLightsByType[i];
            }
        };
        byte b = 0;
        this.mLsWrapper = new LightsServiceWrapper();
        this.mH = new Handler(looper);
        this.mVintfLights = supplier.get() == null ? null : supplier;
        populateAvailableLights(context);
        this.mManagerService = new LightsManagerBinderService();
        this.mLsWrapper.getExtImpl().init(context, looper);
    }

    private void populateAvailableLights(Context context) {
        if (this.mVintfLights != null) {
            populateAvailableLightsFromAidl(context);
        } else {
            populateAvailableLightsFromHidl(context);
        }
        for (int size = this.mLightsById.size() - 1; size >= 0; size--) {
            int keyAt = this.mLightsById.keyAt(size);
            if (keyAt >= 0) {
                LightImpl[] lightImplArr = this.mLightsByType;
                if (keyAt < lightImplArr.length) {
                    lightImplArr[keyAt] = this.mLightsById.valueAt(size);
                }
            }
        }
    }

    private void populateAvailableLightsFromAidl(Context context) {
        try {
            for (HwLight hwLight : this.mVintfLights.get().getLights()) {
                this.mLightsById.put(hwLight.id, new LightImpl(context, hwLight));
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to get lights from HAL", e);
        }
    }

    private void populateAvailableLightsFromHidl(Context context) {
        for (int i = 0; i < this.mLightsByType.length; i++) {
            HwLight hwLight = new HwLight();
            byte b = (byte) i;
            hwLight.id = b;
            hwLight.ordinal = 1;
            hwLight.type = b;
            this.mLightsById.put(b, new LightImpl(context, hwLight));
        }
    }

    public void onStart() {
        publishLocalService(LightsManager.class, this.mService);
        publishBinderService("lights", this.mManagerService);
        this.mLsWrapper.getExtImpl().setBootAnimationLightInternal(true, 16910351);
    }

    public void onBootPhase(int i) {
        this.mLsWrapper.getExtImpl().onBootComplete(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVrDisplayMode() {
        return Settings.Secure.getIntForUser(getContext().getContentResolver(), "vr_display_mode", 0, ActivityManager.getCurrentUser());
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class VintfHalCache implements Supplier<ILights>, IBinder.DeathRecipient {

        @GuardedBy({"this"})
        private ILights mInstance;

        private VintfHalCache() {
            this.mInstance = null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        public synchronized ILights get() {
            if (this.mInstance == null) {
                IBinder allowBlocking = Binder.allowBlocking(ServiceManager.waitForDeclaredService(ILights.DESCRIPTOR + "/default"));
                if (allowBlocking != null) {
                    this.mInstance = ILights.Stub.asInterface(allowBlocking);
                    try {
                        allowBlocking.linkToDeath(this, 0);
                    } catch (RemoteException unused) {
                        Slog.e(LightsService.TAG, "Unable to register DeathRecipient for " + this.mInstance);
                    }
                }
            }
            return this.mInstance;
        }

        @Override // android.os.IBinder.DeathRecipient
        public synchronized void binderDied() {
            this.mInstance = null;
        }
    }

    public ILightsServiceWrapper getWrapper() {
        return this.mLsWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class LightsServiceWrapper implements ILightsServiceWrapper {
        private ILightsServiceExt mLsExt;

        private LightsServiceWrapper() {
            this.mLsExt = (ILightsServiceExt) ExtLoader.type(ILightsServiceExt.class).base(LightsService.this).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ILightsServiceExt getExtImpl() {
            return this.mLsExt;
        }

        @Override // com.android.server.lights.ILightsServiceWrapper
        public boolean getDebug() {
            return LightsService.DEBUG;
        }

        @Override // com.android.server.lights.ILightsServiceWrapper
        public Object getLightsByType() {
            return LightsService.this.mLightsByType;
        }

        @Override // com.android.server.lights.ILightsServiceWrapper
        public void setLightUnchecked(Object obj, int i, int i2, int i3, int i4, int i5) {
            if (obj instanceof LightImpl) {
                ((LightImpl) obj).setLightUnchecked(i, i2, i3, i4, i5);
            }
        }

        @Override // com.android.server.lights.ILightsServiceWrapper
        public void setLightLocked(Object obj, int i, int i2, int i3, int i4, int i5) {
            if (obj instanceof LightImpl) {
                ((LightImpl) obj).setLightLocked(i, i2, i3, i4, i5);
            }
        }
    }
}
