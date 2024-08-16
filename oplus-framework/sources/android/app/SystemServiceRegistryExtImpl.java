package android.app;

import android.app.ISystemServiceRegistryExt;
import android.app.SystemServiceRegistry;
import android.cmccslice.CmccSliceManager;
import android.hardware.alipay.AlipayManager;
import android.hardware.alipay.IAlipayService;
import android.os.IBinder;
import android.os.Process;
import android.os.ServiceManager;
import android.os.renderacc.IRenderAcceleratingService;
import android.os.renderacc.RenderAcceleratingManager;
import android.os.storage.IStorageHealthInfoService;
import android.os.storage.StorageHealthInfoManager;
import android.payjoy.IPayJoyAccessService;
import android.payjoy.PayJoyAccessManager;
import android.telecom.TelecomManagerExt;
import android.telephony.TelephonyRegistryManagerExt;
import com.oplus.content.OplusContext;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.filter.DynamicFilterManager;
import com.oplus.filter.IDynamicFilterService;
import com.oplus.ims.ImsManagerExt;
import com.oplus.nwpower.OSysNetControlManager;
import com.oplus.os.ILinearmotorVibratorService;
import com.oplus.os.LinearmotorVibrator;
import com.oplus.screenshot.OplusScreenshotManager;
import com.oplus.telephony.SmsManagerExt;
import com.oplus.telephony.SubscriptionManagerExt;
import com.oplus.telephony.TelephonyFeatureManagerExt;
import java.io.FileInputStream;

/* loaded from: classes.dex */
public class SystemServiceRegistryExtImpl implements ISystemServiceRegistryExt {
    public SystemServiceRegistryExtImpl(Object base) {
    }

    /* loaded from: classes.dex */
    public static class StaticExtImpl implements ISystemServiceRegistryExt.IStaticExt {
        private final boolean mIsSystemServer;

        private StaticExtImpl() {
            this.mIsSystemServer = checkAppPackageName().contains("system_server");
        }

        public static StaticExtImpl getInstance(Object obj) {
            return LazyHolder.INSTANCE;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class LazyHolder {
            private static final StaticExtImpl INSTANCE = new StaticExtImpl();

            private LazyHolder() {
            }
        }

        public void registerService() {
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(DynamicFilterManager.SERVICE_NAME, DynamicFilterManager.class, new SystemServiceRegistry.CachedServiceFetcher<DynamicFilterManager>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.1
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public DynamicFilterManager m18createService(ContextImpl ctx) {
                    IBinder binder = ServiceManager.getService(DynamicFilterManager.SERVICE_NAME);
                    IDynamicFilterService service = IDynamicFilterService.Stub.asInterface(binder);
                    return new DynamicFilterManager(ctx.getOuterContext(), service);
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(OplusContext.SCREENSHOT_SERVICE, OplusScreenshotManager.class, new SystemServiceRegistry.CachedServiceFetcher<OplusScreenshotManager>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.2
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public OplusScreenshotManager m26createService(ContextImpl ctx) {
                    return OplusScreenshotManager.getInstance();
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(LinearmotorVibrator.LINEARMOTORVIBRATOR_SERVICE, LinearmotorVibrator.class, new SystemServiceRegistry.CachedServiceFetcher<LinearmotorVibrator>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.3
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public LinearmotorVibrator m27createService(ContextImpl ctx) {
                    if (OplusFeatureConfigManager.getInstacne().hasFeature("oplus.software.vibrator_lmvibrator")) {
                        IBinder b = ServiceManager.getService(LinearmotorVibrator.LINEARMOTORVIBRATOR_SERVICE);
                        ILinearmotorVibratorService service = ILinearmotorVibratorService.Stub.asInterface(b);
                        return new LinearmotorVibrator(ctx.getOuterContext(), service);
                    }
                    return null;
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService("storage_healthinfo", StorageHealthInfoManager.class, new SystemServiceRegistry.CachedServiceFetcher<StorageHealthInfoManager>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.4
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public StorageHealthInfoManager m28createService(ContextImpl ctx) {
                    IBinder b = ServiceManager.getService("storage_healthinfo");
                    IStorageHealthInfoService service = IStorageHealthInfoService.Stub.asInterface(b);
                    return new StorageHealthInfoManager(ctx.getOuterContext(), service);
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(AlipayManager.ALIPAY_SERVICE, AlipayManager.class, new SystemServiceRegistry.CachedServiceFetcher<AlipayManager>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.5
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public AlipayManager m29createService(ContextImpl ctx) {
                    IBinder binder = ServiceManager.getService(AlipayManager.ALIPAY_SERVICE);
                    IAlipayService service = IAlipayService.Stub.asInterface(binder);
                    return new AlipayManager(ctx.getOuterContext(), service);
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(OSysNetControlManager.OSYSNETCONTROL_SERVICE, OSysNetControlManager.class, new SystemServiceRegistry.StaticServiceFetcher<OSysNetControlManager>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.6
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public OSysNetControlManager m30createService() {
                    return OSysNetControlManager.getInstance();
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService("payjoy_access_service", PayJoyAccessManager.class, new SystemServiceRegistry.StaticServiceFetcher<PayJoyAccessManager>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.7
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public PayJoyAccessManager m31createService() {
                    IBinder b = ServiceManager.getService("payjoy_access_service");
                    IPayJoyAccessService payjoyAccessService = IPayJoyAccessService.Stub.asInterface(b);
                    if (payjoyAccessService != null) {
                        return new PayJoyAccessManager(payjoyAccessService);
                    }
                    return null;
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(TelephonyRegistryManagerExt.TELEPHONY_REGISTRY_EXT, TelephonyRegistryManagerExt.class, new SystemServiceRegistry.CachedServiceFetcher<TelephonyRegistryManagerExt>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.8
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public TelephonyRegistryManagerExt m32createService(ContextImpl ctx) {
                    return new TelephonyRegistryManagerExt(ctx);
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(TelecomManagerExt.TELECOM_EXT, TelecomManagerExt.class, new SystemServiceRegistry.CachedServiceFetcher<TelecomManagerExt>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.9
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public TelecomManagerExt m33createService(ContextImpl ctx) {
                    return new TelecomManagerExt(ctx);
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(ImsManagerExt.IIMS_EXT, ImsManagerExt.class, new SystemServiceRegistry.CachedServiceFetcher<ImsManagerExt>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.10
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public ImsManagerExt m19createService(ContextImpl ctx) {
                    return new ImsManagerExt(ctx);
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(SmsManagerExt.ISMS_EXT, SmsManagerExt.class, new SystemServiceRegistry.CachedServiceFetcher<SmsManagerExt>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.11
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public SmsManagerExt m20createService(ContextImpl ctx) {
                    return new SmsManagerExt(ctx);
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(SubscriptionManagerExt.ISUB_EXT, SubscriptionManagerExt.class, new SystemServiceRegistry.CachedServiceFetcher<SubscriptionManagerExt>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.12
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public SubscriptionManagerExt m21createService(ContextImpl ctx) {
                    return new SubscriptionManagerExt(ctx);
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(TelephonyFeatureManagerExt.TELEPHONY_FEATURE_EXT, TelephonyFeatureManagerExt.class, new SystemServiceRegistry.CachedServiceFetcher<TelephonyFeatureManagerExt>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.13
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public TelephonyFeatureManagerExt m22createService(ContextImpl ctx) {
                    return new TelephonyFeatureManagerExt(ctx);
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService("render_accelerating", RenderAcceleratingManager.class, new SystemServiceRegistry.CachedServiceFetcher<RenderAcceleratingManager>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.14
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public RenderAcceleratingManager m23createService(ContextImpl ctx) {
                    IBinder b = ServiceManager.getService("render_accelerating");
                    IRenderAcceleratingService service = IRenderAcceleratingService.Stub.asInterface(b);
                    return new RenderAcceleratingManager(ctx, service);
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService("cmcc5gslice", CmccSliceManager.class, new SystemServiceRegistry.CachedServiceFetcher<CmccSliceManager>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.15
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public CmccSliceManager m24createService(ContextImpl ctx) {
                    return new CmccSliceManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
                }
            });
            SystemServiceRegistry.sSystemServiceRegistryStaticWrapper.registerService(RemoteTaskConstants.CROSS_DEVICE_SERVICE, CrossDeviceManager.class, new SystemServiceRegistry.CachedServiceFetcher<CrossDeviceManager>() { // from class: android.app.SystemServiceRegistryExtImpl.StaticExtImpl.16
                /* renamed from: createService, reason: merged with bridge method [inline-methods] */
                public CrossDeviceManager m25createService(ContextImpl ctx) {
                    return new CrossDeviceManager();
                }
            });
        }

        public int checkAppPackageName(int newState) {
            if (this.mIsSystemServer) {
                return 0;
            }
            return newState;
        }

        private String checkAppPackageName() {
            String callingApp = "";
            FileInputStream fis = null;
            try {
                try {
                    fis = new FileInputStream("/proc/" + Process.myPid() + "/cmdline");
                    byte[] buffer = new byte[50];
                    int count = fis.read(buffer);
                    if (count > 0) {
                        callingApp = new String(buffer, 0, count);
                    }
                    fis.close();
                } catch (Exception e) {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (Throwable th) {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (Exception e2) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
            }
            return callingApp;
        }
    }
}
