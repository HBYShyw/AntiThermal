package system.ext.registry;

import com.android.server.ISystemServerSocExt;
import com.android.server.IWatchdogSocExt;
import com.android.server.SystemServerSocExtImpl;
import com.android.server.WatchdogSocExtImpl;
import com.android.server.am.ActivityManagerServiceSocExtImpl;
import com.android.server.am.IActivityManagerServiceSocExt;
import com.android.server.am.IOomAdjusterSocExt;
import com.android.server.am.IProcessErrorStateRecordSocExt;
import com.android.server.am.IProcessListSocExt;
import com.android.server.am.IProcessRecordSocExt;
import com.android.server.am.OomAdjusterSocExtImpl;
import com.android.server.am.ProcessErrorStateRecordSocExtImpl;
import com.android.server.am.ProcessListSocExtImpl;
import com.android.server.am.ProcessRecordSocExtImpl;
import com.android.server.audio.AudioServiceSocExtImpl;
import com.android.server.audio.BtHelperSocExtImpl;
import com.android.server.audio.IAudioServiceSocExt;
import com.android.server.audio.IBtHelperSocExt;
import com.android.server.display.IWifiDisplayControllerSocExt;
import com.android.server.display.WifiDisplayControllerSocExtImpl;
import com.android.server.location.gnss.GnssLPSocExtImpl;
import com.android.server.location.gnss.IGnssLocationProviderSocExt;
import com.android.server.pm.HbtUtilSocExtImpl;
import com.android.server.pm.IHbtUtilSocExt;
import com.android.server.pm.IPackageInstallerSessionSocExt;
import com.android.server.pm.IPackageManagerServiceSocExt;
import com.android.server.pm.PackageInstallerSessionSocExtImpl;
import com.android.server.pm.PackageManagerServiceSocExtImpl;
import com.android.server.policy.IPhoneWindowManagerSocExt;
import com.android.server.policy.PhoneWindowManagerSocExtImpl;
import com.android.server.wm.ActivityMetricsLoggerSocExtImpl;
import com.android.server.wm.ActivityRecordSocExtImpl;
import com.android.server.wm.ActivityStarterSocExtImpl;
import com.android.server.wm.ActivityTaskManagerServiceSocExtImpl;
import com.android.server.wm.ActivityTaskSupervisorSocExtImpl;
import com.android.server.wm.AsyncRotationControllerSocExtImpl;
import com.android.server.wm.DisplayPolicySocExtImpl;
import com.android.server.wm.DisplayRotationSocExtImpl;
import com.android.server.wm.IActivityMetricsLoggerSocExt;
import com.android.server.wm.IActivityRecordSocExt;
import com.android.server.wm.IActivityStarterSocExt;
import com.android.server.wm.IActivityTaskManagerServiceSocExt;
import com.android.server.wm.IActivityTaskSupervisorSocExt;
import com.android.server.wm.IAsyncRotationControllerSocExt;
import com.android.server.wm.IDisplayPolicySocExt;
import com.android.server.wm.IDisplayRotationSocExt;
import com.android.server.wm.IRecentTasksSocExt;
import com.android.server.wm.IRootWindowContainerSocExt;
import com.android.server.wm.IScreenRotationAnimationSocExt;
import com.android.server.wm.ISystemGesturesPointerEventListenerSocExt;
import com.android.server.wm.ITaskFragmentSocExt;
import com.android.server.wm.ITaskTapPointerEventListenerSocExt;
import com.android.server.wm.ITransitionSocExt;
import com.android.server.wm.IWindowManagerServiceSocExt;
import com.android.server.wm.RecentTasksSocExtImpl;
import com.android.server.wm.RootWindowContainerSocExtImpl;
import com.android.server.wm.ScreenRotationAnimationSocExtImpl;
import com.android.server.wm.SystemGesturesPointerEventListenerSocExtImpl;
import com.android.server.wm.TaskFragmentSocExtImpl;
import com.android.server.wm.TaskTapPointerEventListenerSocExtImpl;
import com.android.server.wm.TransitionSocExtImpl;
import com.android.server.wm.WindowManagerServiceSocExtImpl;
import system.ext.loader.core.ExtCreator;
import system.ext.loader.core.ExtRegistry;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SocServicesCoreRegistry {
    static {
        ExtRegistry.registerExt(IActivityTaskSupervisorSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda0
            public final Object createExtWith(Object obj) {
                return new ActivityTaskSupervisorSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISystemServerSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda11
            public final Object createExtWith(Object obj) {
                return new SystemServerSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IProcessRecordSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda22
            public final Object createExtWith(Object obj) {
                return new ProcessRecordSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IProcessListSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda24
            public final Object createExtWith(Object obj) {
                return new ProcessListSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOomAdjusterSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda25
            public final Object createExtWith(Object obj) {
                return new OomAdjusterSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IActivityManagerServiceSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda26
            public final Object createExtWith(Object obj) {
                return new ActivityManagerServiceSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISystemGesturesPointerEventListenerSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda27
            public final Object createExtWith(Object obj) {
                return new SystemGesturesPointerEventListenerSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IDisplayPolicySocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda28
            public final Object createExtWith(Object obj) {
                return new DisplayPolicySocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IDisplayRotationSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda29
            public final Object createExtWith(Object obj) {
                return new DisplayRotationSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IScreenRotationAnimationSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda30
            public final Object createExtWith(Object obj) {
                return new ScreenRotationAnimationSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IHbtUtilSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda1
            public final Object createExtWith(Object obj) {
                return HbtUtilSocExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IPackageInstallerSessionSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda2
            public final Object createExtWith(Object obj) {
                return new PackageInstallerSessionSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPackageManagerServiceSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda3
            public final Object createExtWith(Object obj) {
                return new PackageManagerServiceSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IActivityRecordSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda4
            public final Object createExtWith(Object obj) {
                return new ActivityRecordSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IActivityStarterSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda5
            public final Object createExtWith(Object obj) {
                return new ActivityStarterSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IActivityMetricsLoggerSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda6
            public final Object createExtWith(Object obj) {
                return new ActivityMetricsLoggerSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IWindowManagerServiceSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda7
            public final Object createExtWith(Object obj) {
                return new WindowManagerServiceSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPhoneWindowManagerSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda8
            public final Object createExtWith(Object obj) {
                return new PhoneWindowManagerSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ITaskFragmentSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda9
            public final Object createExtWith(Object obj) {
                return new TaskFragmentSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IActivityTaskManagerServiceSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda10
            public final Object createExtWith(Object obj) {
                return new ActivityTaskManagerServiceSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IProcessErrorStateRecordSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda12
            public final Object createExtWith(Object obj) {
                return new ProcessErrorStateRecordSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IWatchdogSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda13
            public final Object createExtWith(Object obj) {
                return new WatchdogSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IGnssLocationProviderSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda14
            public final Object createExtWith(Object obj) {
                return new GnssLPSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IWifiDisplayControllerSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda15
            public final Object createExtWith(Object obj) {
                return new WifiDisplayControllerSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IAudioServiceSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda16
            public final Object createExtWith(Object obj) {
                return new AudioServiceSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBtHelperSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda17
            public final Object createExtWith(Object obj) {
                return new BtHelperSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IRecentTasksSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda18
            public final Object createExtWith(Object obj) {
                return new RecentTasksSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IRootWindowContainerSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda19
            public final Object createExtWith(Object obj) {
                return new RootWindowContainerSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ITaskTapPointerEventListenerSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda20
            public final Object createExtWith(Object obj) {
                return new TaskTapPointerEventListenerSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ITransitionSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda21
            public final Object createExtWith(Object obj) {
                return new TransitionSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IAsyncRotationControllerSocExt.class, new ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda23
            public final Object createExtWith(Object obj) {
                return new AsyncRotationControllerSocExtImpl(obj);
            }
        });
    }
}
