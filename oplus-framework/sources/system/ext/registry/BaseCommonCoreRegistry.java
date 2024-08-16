package system.ext.registry;

import android.app.ActivityExtImpl;
import android.app.ActivityOptionsExtImpl;
import android.app.AppOpsManagerExtImpl;
import android.app.ApplicationExtImpl;
import android.app.ApplicationPackageManagerExtImpl;
import android.app.ConfigurationControllerExtImpl;
import android.app.ContextImplExtImpl;
import android.app.DialogExtImpl;
import android.app.EmbeddedActivityExtImpl;
import android.app.IActivityClientExt;
import android.app.IActivityExt;
import android.app.IActivityOptionsExt;
import android.app.IActivityThreadExt;
import android.app.IAppOpsManagerExt;
import android.app.IApplicationExt;
import android.app.IApplicationPackageManagerExt;
import android.app.IConfigurationControllerExt;
import android.app.IContextImplExt;
import android.app.IDialogExt;
import android.app.IEmbeddedActivityExt;
import android.app.IInstrumentationExt;
import android.app.ILoadedApkExt;
import android.app.IMessagingStyleExt;
import android.app.INotificationChannelExt;
import android.app.INotificationExt;
import android.app.INotificationManagerExt;
import android.app.IResourcesManagerExt;
import android.app.ISystemServiceRegistryExt;
import android.app.IWallpaperManagerExt;
import android.app.IWindowConfigurationExt;
import android.app.InstrumentationExtImpl;
import android.app.LoadedApkExtImpl;
import android.app.MessagingStyleExtImpl;
import android.app.OplusActivityClientExtImpl;
import android.app.OplusActivityThreadExtImpl;
import android.app.OplusINotificationExtImpl;
import android.app.OplusINotificationManagerExtImpl;
import android.app.OplusNotificationChannelExtImpl;
import android.app.ResourcesManagerExtImpl;
import android.app.SystemServiceRegistryExtImpl;
import android.app.WallpaperManagerExtImpl;
import android.app.WindowConfigurationExtImpl;
import android.app.dialog.AlertControllerExt;
import android.app.dialog.AlertParamsExt;
import android.app.dialog.IAlertControllerExt;
import android.app.dialog.IAlertParamsExt;
import android.app.dialog.IOplusAlertDialogBuilderExt;
import android.app.dialog.OplusAlertDialogBuilderExt;
import android.app.job.IJobInfoExt;
import android.app.job.IJobParametersExt;
import android.app.job.JobInfoExtImpl;
import android.app.job.JobParametersExtImpl;
import android.appwidget.AppWidgetProviderExtImpl;
import android.appwidget.IAppWidgetProviderExt;
import android.bluetooth.BluetoothA2dpSocExtImpl;
import android.bluetooth.BluetoothAdapterExtImpl;
import android.bluetooth.BluetoothDeviceSocExtImpl;
import android.bluetooth.BluetoothGattSocExtImpl;
import android.bluetooth.BluetoothHeadsetSocExtImpl;
import android.bluetooth.BluetoothLeAudioSocExtImpl;
import android.bluetooth.IBluetoothA2dpSocExt;
import android.bluetooth.IBluetoothAdapterExt;
import android.bluetooth.IBluetoothDeviceSocExt;
import android.bluetooth.IBluetoothGattSocExt;
import android.bluetooth.IBluetoothHeadsetSocExt;
import android.bluetooth.IBluetoothLeAudioSocExt;
import android.content.ClipboardManagerExtImpl;
import android.content.ContentProviderExtImpl;
import android.content.ContentResolverExtImpl;
import android.content.IClipboardManagerExt;
import android.content.IContentProviderExt;
import android.content.IContentResolverExt;
import android.content.IIntentExt;
import android.content.IIntentFilterExt;
import android.content.IPendingResultExt;
import android.content.IntentExtImpl;
import android.content.IntentFilterExtImpl;
import android.content.PendingResultExtImpl;
import android.content.pm.ActivityInfoExtImpl;
import android.content.pm.ApplicationInfoExtImpl;
import android.content.pm.IActivityInfoExt;
import android.content.pm.IApplicationInfoExt;
import android.content.pm.ILauncherActivityInfoExt;
import android.content.pm.IPackageInstallerExt;
import android.content.pm.IPackageItemInfoExt;
import android.content.pm.IPackageParserExt;
import android.content.pm.IPackagePartitionsExt;
import android.content.pm.IPackageUserStateExt;
import android.content.pm.IRegisteredServicesCacheExt;
import android.content.pm.IResolveInfoExt;
import android.content.pm.ISessionParamsExt;
import android.content.pm.IUserInfoExt;
import android.content.pm.IVersionedPackageExt;
import android.content.pm.LauncherActivityInfoExtImpl;
import android.content.pm.OplusPackageItemInfoExtImpl;
import android.content.pm.PackageInstallerExtImpl;
import android.content.pm.PackageParserExtImpl;
import android.content.pm.PackagePartitionsExtImpl;
import android.content.pm.PackageUserStateExtImpl;
import android.content.pm.RegisteredServicesCacheExtImpl;
import android.content.pm.ResolveInfoExtImpl;
import android.content.pm.SessionParamsExtImpl;
import android.content.pm.UserInfoExtImpl;
import android.content.pm.VersionedPackageExtImpl;
import android.content.pm.parsing.component.IParsedMainComponentExt;
import android.content.pm.parsing.component.ParsedMainComponentExtImpl;
import android.content.res.ConfigurationExtImpl;
import android.content.res.IConfigurationExt;
import android.content.res.IResourcesExt;
import android.content.res.IResourcesImplExt;
import android.content.res.ResourcesExt;
import android.content.res.ResourcesImplExt;
import android.database.sqlite.ISQLiteConnectionExt;
import android.database.sqlite.SQLiteConnectionExtImpl;
import android.graphics.BaseCanvasExtImpl;
import android.graphics.BitmapExtImpl;
import android.graphics.BitmapFactoryExtImpl;
import android.graphics.IBaseCanvasExt;
import android.graphics.IBitmapExt;
import android.graphics.IBitmapFactoryExt;
import android.graphics.IPaintExt;
import android.graphics.IPathExt;
import android.graphics.IRenderNodeExt;
import android.graphics.IShaderExt;
import android.graphics.PaintExtImpl;
import android.graphics.PathExtImpl;
import android.graphics.RenderNodeExtImpl;
import android.graphics.ShaderExtImpl;
import android.graphics.drawable.AdaptiveIconDrawableExtImpl;
import android.graphics.drawable.IAdaptiveIconDrawableExt;
import android.graphics.drawable.IGradientDrawableExt;
import android.graphics.drawable.IRippleAnimationSessionExt;
import android.graphics.drawable.IStateListDrawableExt;
import android.graphics.drawable.IVectorDrawableExt;
import android.graphics.drawable.OplusGradientDrawableExtImpl;
import android.graphics.drawable.RippleAnimationSessionExtImpl;
import android.graphics.drawable.StateListDrawableExtImpl;
import android.graphics.drawable.VectorDrawableExtImpl;
import android.graphics.fonts.ISystemFontExt;
import android.graphics.fonts.SystemFontExtImpl;
import android.hardware.ISystemSensorManagerExt;
import android.hardware.SystemSensorManagerExtImpl;
import android.hardware.camera2.impl.CameraCaptureSessionImplExtImpl;
import android.hardware.camera2.impl.ICameraCaptureSessionImplExt;
import android.hardware.display.DisplayManagerGlobalExtImpl;
import android.hardware.display.IDisplayManagerGlobalExt;
import android.hardware.fingerprint.FingerprintExtImpl;
import android.hardware.fingerprint.FingerprintManagerExtImpl;
import android.hardware.fingerprint.IFingerprintExt;
import android.hardware.fingerprint.IFingerprintManagerExt;
import android.hardware.fingerprint.util.IOplusFingerprintSupportUtilsExt;
import android.hardware.fingerprint.util.OplusFingerprintSupportUtils;
import android.inputmethodservice.IInputMethodServiceExt;
import android.inputmethodservice.InputMethodServiceExtImpl;
import android.location.ILocationManagerExt;
import android.location.LocationManagerExtImpl;
import android.media.AudioManagerExtImpl;
import android.media.AudioRecordExtImpl;
import android.media.AudioSystemExtImpl;
import android.media.IAudioManagerExt;
import android.media.IAudioRecordExt;
import android.media.IAudioSystemExt;
import android.media.IMediaCodecListExt;
import android.media.IMediaHTTPConnectionExt;
import android.media.IRingtoneManagerExt;
import android.media.MediaCodecListExtImpl;
import android.media.MediaHTTPConnectionExtImpl;
import android.media.RingtoneManagerExtImpl;
import android.media.projection.IMediaProjectionManagerServiceExt;
import android.media.projection.MediaProjectionManagerServiceExtImpl;
import android.mtp.IMtpDatabaseExt;
import android.mtp.MtpDatabaseExtImpl;
import android.os.BatteryStatsExtImpl;
import android.os.BinderProxyExtImpl;
import android.os.EnvironmentExtImpl;
import android.os.IBatteryStatsExt;
import android.os.IBinderProxyExt;
import android.os.IEnvironmentExt;
import android.os.ILooperExt;
import android.os.IMessageQueueExt;
import android.os.IOneTraceExt;
import android.os.IOplusJankMonitorExt;
import android.os.IPowerManagerExt;
import android.os.IStrictModeExt;
import android.os.ISystemVibratorExt;
import android.os.ITheiaManagerExt;
import android.os.IUserManagerExt;
import android.os.IVibratorExt;
import android.os.LooperExtImpl;
import android.os.MessageQueueExtImpl;
import android.os.OneTraceExtImpl;
import android.os.OplusJankMonitorExtImpl;
import android.os.PowerManagerExtImpl;
import android.os.StrictModeExtImpl;
import android.os.SystemVibratorExtImpl;
import android.os.TheiaManagerExtImpl;
import android.os.UserManagerExtImpl;
import android.os.VibratorExtImpl;
import android.os.storage.IStorageManagerExt;
import android.os.storage.IStorageVolumeExt;
import android.os.storage.IVolumeInfoExt;
import android.os.storage.StorageManagerExtImpl;
import android.os.storage.StorageVolumeExtImpl;
import android.os.storage.VolumeInfoExtImpl;
import android.os.vibrator.IVibrationEffectSegmentExt;
import android.os.vibrator.VibrationEffectSegmentExtImpl;
import android.provider.ISettingsExt;
import android.provider.SettingsExtImpl;
import android.server.accessibility.AccessibilityUserStateExtImpl;
import android.server.accessibility.IAccessibilityUserStateExt;
import android.service.wallpaper.EngineExtImpl;
import android.service.wallpaper.IEngineExt;
import android.service.wallpaper.IWallpaperServiceExt;
import android.service.wallpaper.WallpaperServiceExtImpl;
import android.telephony.CarrierConfigManagerExtImpl;
import android.telephony.ICarrierConfigManagerExt;
import android.telephony.IPhoneNumberUtilsExt;
import android.telephony.PhoneNumberUtilsExtImpl;
import android.telephony.data.ApnSettingExtImpl;
import android.telephony.data.IApnSettingExt;
import android.text.IStaticLayoutExt;
import android.text.ITextLineExt;
import android.text.StaticLayoutExtImpl;
import android.text.TextLineExtImpl;
import android.util.IIconDrawableFactoryExt;
import android.util.ILauncherIconsExt;
import android.util.INtpTrustedTimeExt;
import android.util.IconDrawableFactoryExtImpl;
import android.util.LauncherIconsExtImpl;
import android.util.NtpTrustedTimeExtImpl;
import android.view.ChoreographerExtImpl;
import android.view.DisplayExtImpl;
import android.view.DisplayInfoExtImpl;
import android.view.IChoreographerExt;
import android.view.IDisplayExt;
import android.view.IDisplayInfoExt;
import android.view.IInsetsControllerExt;
import android.view.IInsetsSourceConsumerExt;
import android.view.IInsetsStateExt;
import android.view.IRemoteAnimationTargetExt;
import android.view.ISurfaceControlExt;
import android.view.ISurfaceExt;
import android.view.ISurfaceViewExt;
import android.view.IViewExt;
import android.view.IViewGroupExt;
import android.view.IViewRootImplExt;
import android.view.IWindowExt;
import android.view.IWindowLayoutExt;
import android.view.InsetsControllerExtImpl;
import android.view.InsetsSourceConsumerExtImpl;
import android.view.InsetsStateExtImpl;
import android.view.RemoteAnimationTargetExtImpl;
import android.view.SurfaceControlExtImpl;
import android.view.SurfaceExtImpl;
import android.view.SurfaceViewExtImpl;
import android.view.ViewExtImpl;
import android.view.ViewGroupExtImpl;
import android.view.ViewRootImplExtImpl;
import android.view.WindowExtImpl;
import android.view.WindowLayoutExtImpl;
import android.view.accessibility.AccessibilityManagerExtImpl;
import android.view.accessibility.IAccessibilityManagerExt;
import android.view.autofill.AutofillManagerExtImpl;
import android.view.autofill.IAutofillManagerExt;
import android.view.inputmethod.IInputMethodManagerExt;
import android.view.inputmethod.InputMethodManagerExtImpl;
import android.webkit.IWebViewExt;
import android.webkit.WebViewExtImpl;
import android.widget.AbsListViewExtImpl;
import android.widget.EditorExtImpl;
import android.widget.IAbsListviewExt;
import android.widget.IEditorExt;
import android.widget.IImageViewExt;
import android.widget.IOplusOverScrollerExt;
import android.widget.IOplusScrollViewExt;
import android.widget.IPopupWindowExt;
import android.widget.IScrollBarDrawableExt;
import android.widget.ISplineOverScrollerExt;
import android.widget.ITextViewExt;
import android.widget.ImageViewExtImpl;
import android.widget.OplusOverScrollerExtImpl;
import android.widget.OplusScrollViewExtImpl;
import android.widget.PopupWindowExtImpl;
import android.widget.ScrollBarDrawableExtImpl;
import android.widget.SplineOverScrollerExtImpl;
import android.widget.TextViewExtImpl;
import android.window.IStartingWindowInfoExt;
import android.window.ITransitionInfoExt;
import android.window.IWindowContainerTransactionExt;
import android.window.StartingWindowInfoExtImpl;
import android.window.TransitionInfoExtImpl;
import android.window.WindowContainerTransactionExtImpl;
import com.android.ims.IImsCallExt;
import com.android.ims.IImsManagerExt;
import com.android.ims.ImsCallExtImpl;
import com.android.ims.ImsManagerExtImpl;
import com.android.internal.app.AbstractMultiProfilePagerAdapterExtImpl;
import com.android.internal.app.ChooserMultiProfilePagerAdapterExtImpl;
import com.android.internal.app.IAbstractMultiProfilePagerAdapterExt;
import com.android.internal.app.IChooserMultiProfilePagerAdapterExt;
import com.android.internal.app.IResolverActivityExt;
import com.android.internal.app.IResolverHelperExt;
import com.android.internal.app.IResolverListAdapterExt;
import com.android.internal.app.IResolverMultiProfilePagerAdapterExt;
import com.android.internal.app.IUnlaunchableAppActivityExt;
import com.android.internal.app.ResolverActivityExtImpl;
import com.android.internal.app.ResolverHelperExtImpl;
import com.android.internal.app.ResolverListAdapterExtImpl;
import com.android.internal.app.ResolverMultiProfilePagerAdapterExtImpl;
import com.android.internal.app.UnlaunchableAppActivityExtImpl;
import com.android.internal.app.chooser.DisplayResolveInfoExtImpl;
import com.android.internal.app.chooser.IDisplayResolveInfoExt;
import com.android.internal.app.chooser.ISelectableTargetInfoExt;
import com.android.internal.app.chooser.SelectableTargetInfoExtImpl;
import com.android.internal.content.IPackageMonitorExt;
import com.android.internal.content.PackageMonitorExtImpl;
import com.android.internal.graphics.drawable.BackgroundBlurDrawableExtImpl;
import com.android.internal.graphics.drawable.IBackgroundBlurDrawableExt;
import com.android.internal.inputmethod.EditableInputConnectionExtImpl;
import com.android.internal.inputmethod.IEditableInputConnectionExt;
import com.android.internal.os.IPowerProfileExt;
import com.android.internal.os.IProcessCpuTrackerExt;
import com.android.internal.os.IZygoteInitExt;
import com.android.internal.os.PowerProfileExtImpl;
import com.android.internal.os.ProcessCpuTrackerExtImpl;
import com.android.internal.os.ZygoteInitExtImpl;
import com.android.internal.policy.BackdropFrameRendererExtImpl;
import com.android.internal.policy.DecorViewExtImpl;
import com.android.internal.policy.DividerSnapAlgorithmExtImpl;
import com.android.internal.policy.IBackdropFrameRendererExt;
import com.android.internal.policy.IDecorViewExt;
import com.android.internal.policy.IDividerSnapAlgorithmExt;
import com.android.internal.policy.IPhoneFallbackEventHandlerExt;
import com.android.internal.policy.IPhoneWindowExt;
import com.android.internal.policy.ITransitionAnimationExt;
import com.android.internal.policy.PhoneFallbackEventHandlerExtImpl;
import com.android.internal.policy.PhoneWindowExtImpl;
import com.android.internal.policy.TransitionAnimationExtImpl;
import com.android.internal.protolog.BaseProtoLogImplExtImpl;
import com.android.internal.protolog.IBaseProtoLogImplExt;
import com.android.internal.telephony.ISmsApplicationExt;
import com.android.internal.telephony.SmsApplicationExtImpl;
import com.android.internal.telephony.cdma.CdmaSmsMessageExtImpl;
import com.android.internal.telephony.cdma.ICdmaSmsMessageExt;
import com.android.internal.telephony.gsm.GsmSmsMessageExtImpl;
import com.android.internal.telephony.gsm.IGsmSmsMessageExt;
import com.android.internal.util.IScreenshotHelperExt;
import com.android.internal.util.ScreenshotHelperExtImpl;
import com.android.internal.view.IScrollCaptureInternalExt;
import com.android.internal.view.ScrollCaptureInternalExtImpl;
import com.android.internal.widget.ILockPatternUtilsExt;
import com.android.internal.widget.IPointerLocationViewExt;
import com.android.internal.widget.IResolverDrawerLayoutExt;
import com.android.internal.widget.LockPatternUtilsExtImpl;
import com.android.internal.widget.PointerLocationViewExtImpl;
import com.android.internal.widget.ResolverDrawerLayoutExtImp;
import com.android.internal.widget.floatingtoolbar.ILocalFloatingToolbarPopupExt;
import com.android.internal.widget.floatingtoolbar.IOverflowPanelExt;
import com.android.internal.widget.floatingtoolbar.IOverflowPanelViewHelperExt;
import com.android.internal.widget.floatingtoolbar.LocalFloatingToolbarPopupExtImpl;
import com.android.internal.widget.floatingtoolbar.OverflowPanelExtImpl;
import com.android.internal.widget.floatingtoolbar.OverflowPanelViewHelperExtImpl;
import com.oplus.autolayout.IAutoLayoutAdapterExt;
import com.oplus.autolayout.OplusAutoLayoutAdapterExtImpl;
import com.oplus.cust.IOplusCfgFilePolicyExt;
import com.oplus.cust.OplusCfgFilePolicyExtImpl;
import com.oplus.permission.IOplusPermissionCheckInjectorExt;
import com.oplus.permission.OplusPermissionCheckInjectorExtImpl;
import com.oplus.splitscreen.ISplitScreenManagerExt;
import com.oplus.splitscreen.SplitScreenManagerExtImpl;
import com.oplus.uifirst.IOplusUIFirstManagerExt;
import com.oplus.uifirst.OplusUIFirstManagerExtImpl;
import oplus.android.IOplusExtPluginFactoryExt;
import oplus.android.OplusExtPluginFactoryExtImpl;
import oplus.util.IOplusStatisticsExt;
import oplus.util.OplusStatisticsExtImpl;
import system.ext.loader.core.ExtCreator;
import system.ext.loader.core.ExtRegistry;

/* loaded from: classes.dex */
public class BaseCommonCoreRegistry {
    static {
        ExtRegistry.registerExt(IActivityExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda0
            public final Object createExtWith(Object obj) {
                return new ActivityExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IActivityOptionsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda106
            public final Object createExtWith(Object obj) {
                return new ActivityOptionsExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IActivityThreadExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda117
            public final Object createExtWith(Object obj) {
                return new OplusActivityThreadExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IApplicationPackageManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda128
            public final Object createExtWith(Object obj) {
                return new ApplicationPackageManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IWallpaperManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda139
            public final Object createExtWith(Object obj) {
                return WallpaperManagerExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IWindowConfigurationExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda150
            public final Object createExtWith(Object obj) {
                return new WindowConfigurationExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IAppOpsManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda161
            public final Object createExtWith(Object obj) {
                return AppOpsManagerExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IContextImplExt.IStaticExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda172
            public final Object createExtWith(Object obj) {
                return ContextImplExtImpl.StaticExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IConfigurationControllerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda183
            public final Object createExtWith(Object obj) {
                return new ConfigurationControllerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IApplicationExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda194
            public final Object createExtWith(Object obj) {
                return new ApplicationExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IJobParametersExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda11
            public final Object createExtWith(Object obj) {
                return new JobParametersExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IJobInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda22
            public final Object createExtWith(Object obj) {
                return new JobInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IEmbeddedActivityExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda33
            public final Object createExtWith(Object obj) {
                return EmbeddedActivityExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(ILoadedApkExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda44
            public final Object createExtWith(Object obj) {
                return new LoadedApkExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IActivityClientExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda55
            public final Object createExtWith(Object obj) {
                return new OplusActivityClientExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ILoadedApkExt.IStaticExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda66
            public final Object createExtWith(Object obj) {
                return LoadedApkExtImpl.StaticExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(INotificationChannelExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda77
            public final Object createExtWith(Object obj) {
                return new OplusNotificationChannelExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IMessagingStyleExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda88
            public final Object createExtWith(Object obj) {
                return new MessagingStyleExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IResourcesManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda99
            public final Object createExtWith(Object obj) {
                return new ResourcesManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IAlertControllerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda105
            public final Object createExtWith(Object obj) {
                return new AlertControllerExt(obj);
            }
        });
        ExtRegistry.registerExt(IAlertParamsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda107
            public final Object createExtWith(Object obj) {
                return new AlertParamsExt(obj);
            }
        });
        ExtRegistry.registerExt(ILooperExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda108
            public final Object createExtWith(Object obj) {
                return new LooperExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IMessageQueueExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda109
            public final Object createExtWith(Object obj) {
                return new MessageQueueExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IStorageVolumeExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda110
            public final Object createExtWith(Object obj) {
                return new StorageVolumeExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IProcessCpuTrackerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda111
            public final Object createExtWith(Object obj) {
                return new ProcessCpuTrackerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IEditorExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda112
            public final Object createExtWith(Object obj) {
                return new EditorExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IVolumeInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda113
            public final Object createExtWith(Object obj) {
                return new VolumeInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IUserManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda114
            public final Object createExtWith(Object obj) {
                return new UserManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IAbsListviewExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda115
            public final Object createExtWith(Object obj) {
                return new AbsListViewExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IStaticLayoutExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda116
            public final Object createExtWith(Object obj) {
                return new StaticLayoutExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IAdaptiveIconDrawableExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda118
            public final Object createExtWith(Object obj) {
                return new AdaptiveIconDrawableExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IEditableInputConnectionExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda119
            public final Object createExtWith(Object obj) {
                return new EditableInputConnectionExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISystemServiceRegistryExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda120
            public final Object createExtWith(Object obj) {
                return new SystemServiceRegistryExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISystemServiceRegistryExt.IStaticExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda121
            public final Object createExtWith(Object obj) {
                return SystemServiceRegistryExtImpl.StaticExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IInstrumentationExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda122
            public final Object createExtWith(Object obj) {
                return new InstrumentationExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IUnlaunchableAppActivityExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda123
            public final Object createExtWith(Object obj) {
                return new UnlaunchableAppActivityExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IStorageManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda124
            public final Object createExtWith(Object obj) {
                return new StorageManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IFingerprintExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda125
            public final Object createExtWith(Object obj) {
                return new FingerprintExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IFingerprintManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda126
            public final Object createExtWith(Object obj) {
                return new FingerprintManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOplusFingerprintSupportUtilsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda127
            public final Object createExtWith(Object obj) {
                return new OplusFingerprintSupportUtils(obj);
            }
        });
        ExtRegistry.registerExt(IWallpaperServiceExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda129
            public final Object createExtWith(Object obj) {
                return new WallpaperServiceExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(INtpTrustedTimeExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda130
            public final Object createExtWith(Object obj) {
                return new NtpTrustedTimeExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IEngineExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda131
            public final Object createExtWith(Object obj) {
                return new EngineExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBitmapExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda132
            public final Object createExtWith(Object obj) {
                return new BitmapExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPathExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda133
            public final Object createExtWith(Object obj) {
                return new PathExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBitmapFactoryExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda134
            public final Object createExtWith(Object obj) {
                return new BitmapFactoryExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPaintExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda135
            public final Object createExtWith(Object obj) {
                return new PaintExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IGradientDrawableExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda136
            public final Object createExtWith(Object obj) {
                return new OplusGradientDrawableExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBaseCanvasExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda137
            public final Object createExtWith(Object obj) {
                return new BaseCanvasExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISystemFontExt.IStaticExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda138
            public final Object createExtWith(Object obj) {
                return SystemFontExtImpl.StaticExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IStateListDrawableExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda140
            public final Object createExtWith(Object obj) {
                return new StateListDrawableExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IVectorDrawableExt.IStaticExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda141
            public final Object createExtWith(Object obj) {
                return VectorDrawableExtImpl.StaticExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IRenderNodeExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda142
            public final Object createExtWith(Object obj) {
                return new RenderNodeExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBackgroundBlurDrawableExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda143
            public final Object createExtWith(Object obj) {
                return new BackgroundBlurDrawableExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ILockPatternUtilsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda144
            public final Object createExtWith(Object obj) {
                return new LockPatternUtilsExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPointerLocationViewExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda145
            public final Object createExtWith(Object obj) {
                return new PointerLocationViewExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IActivityInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda146
            public final Object createExtWith(Object obj) {
                return new ActivityInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IShaderExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda147
            public final Object createExtWith(Object obj) {
                return new ShaderExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ILocationManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda148
            public final Object createExtWith(Object obj) {
                return new LocationManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IDialogExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda149
            public final Object createExtWith(Object obj) {
                return new DialogExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IContentProviderExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda151
            public final Object createExtWith(Object obj) {
                return new ContentProviderExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IApplicationInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda152
            public final Object createExtWith(Object obj) {
                return new ApplicationInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPackageItemInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda153
            public final Object createExtWith(Object obj) {
                return new OplusPackageItemInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPackageParserExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda154
            public final Object createExtWith(Object obj) {
                return new PackageParserExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPackageUserStateExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda155
            public final Object createExtWith(Object obj) {
                return new PackageUserStateExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IUserInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda156
            public final Object createExtWith(Object obj) {
                return new UserInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IVersionedPackageExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda157
            public final Object createExtWith(Object obj) {
                return new VersionedPackageExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IParsedMainComponentExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda158
            public final Object createExtWith(Object obj) {
                return new ParsedMainComponentExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPackagePartitionsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda159
            public final Object createExtWith(Object obj) {
                return PackagePartitionsExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(ISessionParamsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda160
            public final Object createExtWith(Object obj) {
                return new SessionParamsExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ILauncherActivityInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda162
            public final Object createExtWith(Object obj) {
                return new LauncherActivityInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IAudioRecordExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda163
            public final Object createExtWith(Object obj) {
                return new AudioRecordExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IRegisteredServicesCacheExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda164
            public final Object createExtWith(Object obj) {
                return BaseCommonCoreRegistry.lambda$static$0(obj);
            }
        });
        ExtRegistry.registerExt(IPackageInstallerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda165
            public final Object createExtWith(Object obj) {
                return new PackageInstallerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IResolveInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda166
            public final Object createExtWith(Object obj) {
                return new ResolveInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPackageParserExt.IStaticExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda167
            public final Object createExtWith(Object obj) {
                return PackageParserExtImpl.StaticExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(ICameraCaptureSessionImplExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda168
            public final Object createExtWith(Object obj) {
                return new CameraCaptureSessionImplExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IDisplayManagerGlobalExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda169
            public final Object createExtWith(Object obj) {
                return new DisplayManagerGlobalExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IResourcesExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda170
            public final Object createExtWith(Object obj) {
                return new ResourcesExt(obj);
            }
        });
        ExtRegistry.registerExt(IResourcesImplExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda171
            public final Object createExtWith(Object obj) {
                return new ResourcesImplExt(obj);
            }
        });
        ExtRegistry.registerExt(IConfigurationExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda173
            public final Object createExtWith(Object obj) {
                return new ConfigurationExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPowerProfileExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda174
            public final Object createExtWith(Object obj) {
                return new PowerProfileExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IDecorViewExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda175
            public final Object createExtWith(Object obj) {
                return new DecorViewExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IZygoteInitExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda176
            public final Object createExtWith(Object obj) {
                return new ZygoteInitExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBaseProtoLogImplExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda177
            public final Object createExtWith(Object obj) {
                return new BaseProtoLogImplExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IChooserMultiProfilePagerAdapterExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda178
            public final Object createExtWith(Object obj) {
                return new ChooserMultiProfilePagerAdapterExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IAbstractMultiProfilePagerAdapterExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda179
            public final Object createExtWith(Object obj) {
                return new AbstractMultiProfilePagerAdapterExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IResolverMultiProfilePagerAdapterExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda180
            public final Object createExtWith(Object obj) {
                return new ResolverMultiProfilePagerAdapterExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IDividerSnapAlgorithmExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda181
            public final Object createExtWith(Object obj) {
                return new DividerSnapAlgorithmExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPhoneWindowExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda182
            public final Object createExtWith(Object obj) {
                return new PhoneWindowExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBackdropFrameRendererExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda184
            public final Object createExtWith(Object obj) {
                return new BackdropFrameRendererExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IDisplayResolveInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda185
            public final Object createExtWith(Object obj) {
                return new DisplayResolveInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPhoneFallbackEventHandlerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda186
            public final Object createExtWith(Object obj) {
                return new PhoneFallbackEventHandlerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IScreenshotHelperExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda187
            public final Object createExtWith(Object obj) {
                return new ScreenshotHelperExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IScrollCaptureInternalExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda188
            public final Object createExtWith(Object obj) {
                return new ScrollCaptureInternalExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ITransitionAnimationExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda189
            public final Object createExtWith(Object obj) {
                return new TransitionAnimationExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ITransitionAnimationExt.IStaticExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda190
            public final Object createExtWith(Object obj) {
                return TransitionAnimationExtImpl.StaticExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IMediaHTTPConnectionExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda191
            public final Object createExtWith(Object obj) {
                return new MediaHTTPConnectionExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IMediaCodecListExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda192
            public final Object createExtWith(Object obj) {
                return MediaCodecListExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IAudioSystemExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda193
            public final Object createExtWith(Object obj) {
                return AudioSystemExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IAudioManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda1
            public final Object createExtWith(Object obj) {
                return AudioManagerExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IRingtoneManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda2
            public final Object createExtWith(Object obj) {
                return RingtoneManagerExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IBatteryStatsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda3
            public final Object createExtWith(Object obj) {
                return new BatteryStatsExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBatteryStatsExt.IStaticExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda4
            public final Object createExtWith(Object obj) {
                return BatteryStatsExtImpl.StaticExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IOplusUIFirstManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda5
            public final Object createExtWith(Object obj) {
                return new OplusUIFirstManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IViewRootImplExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda6
            public final Object createExtWith(Object obj) {
                return new ViewRootImplExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IViewExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda7
            public final Object createExtWith(Object obj) {
                return new ViewExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IInsetsStateExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda8
            public final Object createExtWith(Object obj) {
                return new InsetsStateExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IViewGroupExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda9
            public final Object createExtWith(Object obj) {
                return new ViewGroupExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IWindowLayoutExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda10
            public final Object createExtWith(Object obj) {
                return new WindowLayoutExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IWindowExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda12
            public final Object createExtWith(Object obj) {
                return new WindowExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IDisplayInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda13
            public final Object createExtWith(Object obj) {
                return new DisplayInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IDisplayExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda14
            public final Object createExtWith(Object obj) {
                return new DisplayExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IRemoteAnimationTargetExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda15
            public final Object createExtWith(Object obj) {
                return new RemoteAnimationTargetExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ITextViewExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda16
            public final Object createExtWith(Object obj) {
                return new TextViewExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ITextLineExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda17
            public final Object createExtWith(Object obj) {
                return new TextLineExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IChoreographerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda18
            public final Object createExtWith(Object obj) {
                return new ChoreographerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IInsetsControllerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda19
            public final Object createExtWith(Object obj) {
                return new InsetsControllerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IInsetsControllerExt.IStaticExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda20
            public final Object createExtWith(Object obj) {
                return InsetsControllerExtImpl.StaticExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IInsetsSourceConsumerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda21
            public final Object createExtWith(Object obj) {
                return new InsetsSourceConsumerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISurfaceExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda23
            public final Object createExtWith(Object obj) {
                return new SurfaceExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISurfaceControlExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda24
            public final Object createExtWith(Object obj) {
                return new SurfaceControlExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISurfaceViewExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda25
            public final Object createExtWith(Object obj) {
                return new SurfaceViewExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IAccessibilityManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda26
            public final Object createExtWith(Object obj) {
                return new AccessibilityManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IWebViewExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda27
            public final Object createExtWith(Object obj) {
                return new WebViewExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IScrollBarDrawableExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda28
            public final Object createExtWith(Object obj) {
                return new ScrollBarDrawableExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOplusScrollViewExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda29
            public final Object createExtWith(Object obj) {
                return new OplusScrollViewExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOplusOverScrollerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda30
            public final Object createExtWith(Object obj) {
                return new OplusOverScrollerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISplineOverScrollerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda31
            public final Object createExtWith(Object obj) {
                return new SplineOverScrollerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPopupWindowExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda32
            public final Object createExtWith(Object obj) {
                return new PopupWindowExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IImageViewExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda34
            public final Object createExtWith(Object obj) {
                return new ImageViewExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOplusAlertDialogBuilderExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda35
            public final Object createExtWith(Object obj) {
                return new OplusAlertDialogBuilderExt(obj);
            }
        });
        ExtRegistry.registerExt(ILocalFloatingToolbarPopupExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda36
            public final Object createExtWith(Object obj) {
                return new LocalFloatingToolbarPopupExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOverflowPanelExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda37
            public final Object createExtWith(Object obj) {
                return new OverflowPanelExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOverflowPanelViewHelperExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda38
            public final Object createExtWith(Object obj) {
                return new OverflowPanelViewHelperExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IRippleAnimationSessionExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda39
            public final Object createExtWith(Object obj) {
                return new RippleAnimationSessionExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBluetoothAdapterExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda40
            public final Object createExtWith(Object obj) {
                return BluetoothAdapterExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IBluetoothA2dpSocExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda41
            public final Object createExtWith(Object obj) {
                return new BluetoothA2dpSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBluetoothLeAudioSocExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda42
            public final Object createExtWith(Object obj) {
                return new BluetoothLeAudioSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBluetoothDeviceSocExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda43
            public final Object createExtWith(Object obj) {
                return new BluetoothDeviceSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBluetoothGattSocExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda45
            public final Object createExtWith(Object obj) {
                return new BluetoothGattSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IBluetoothHeadsetSocExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda46
            public final Object createExtWith(Object obj) {
                return new BluetoothHeadsetSocExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IStartingWindowInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda47
            public final Object createExtWith(Object obj) {
                return new StartingWindowInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ITransitionInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda48
            public final Object createExtWith(Object obj) {
                return new TransitionInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IWindowContainerTransactionExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda49
            public final Object createExtWith(Object obj) {
                return new WindowContainerTransactionExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IIconDrawableFactoryExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda50
            public final Object createExtWith(Object obj) {
                return new IconDrawableFactoryExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ILauncherIconsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda51
            public final Object createExtWith(Object obj) {
                return new LauncherIconsExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPendingResultExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda52
            public final Object createExtWith(Object obj) {
                return new PendingResultExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IClipboardManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda53
            public final Object createExtWith(Object obj) {
                return new ClipboardManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IContentResolverExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda54
            public final Object createExtWith(Object obj) {
                return new ContentResolverExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IIntentExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda56
            public final Object createExtWith(Object obj) {
                return new IntentExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IIntentFilterExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda57
            public final Object createExtWith(Object obj) {
                return new IntentFilterExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPackageMonitorExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda58
            public final Object createExtWith(Object obj) {
                return new PackageMonitorExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISystemVibratorExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda59
            public final Object createExtWith(Object obj) {
                return new SystemVibratorExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IVibratorExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda60
            public final Object createExtWith(Object obj) {
                return new VibratorExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IVibrationEffectSegmentExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda61
            public final Object createExtWith(Object obj) {
                return new VibrationEffectSegmentExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISystemSensorManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda62
            public final Object createExtWith(Object obj) {
                return new SystemSensorManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IInputMethodManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda63
            public final Object createExtWith(Object obj) {
                return new InputMethodManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IInputMethodServiceExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda64
            public final Object createExtWith(Object obj) {
                return new InputMethodServiceExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOplusPermissionCheckInjectorExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda65
            public final Object createExtWith(Object obj) {
                return OplusPermissionCheckInjectorExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IOplusStatisticsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda67
            public final Object createExtWith(Object obj) {
                return OplusStatisticsExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(ICarrierConfigManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda68
            public final Object createExtWith(Object obj) {
                return new CarrierConfigManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPhoneNumberUtilsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda69
            public final Object createExtWith(Object obj) {
                return new PhoneNumberUtilsExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IImsCallExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda70
            public final Object createExtWith(Object obj) {
                return new ImsCallExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IImsManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda71
            public final Object createExtWith(Object obj) {
                return new ImsManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IApnSettingExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda72
            public final Object createExtWith(Object obj) {
                return new ApnSettingExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISmsApplicationExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda73
            public final Object createExtWith(Object obj) {
                return new SmsApplicationExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ICdmaSmsMessageExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda74
            public final Object createExtWith(Object obj) {
                return new CdmaSmsMessageExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IGsmSmsMessageExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda75
            public final Object createExtWith(Object obj) {
                return new GsmSmsMessageExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ITheiaManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda76
            public final Object createExtWith(Object obj) {
                return TheiaManagerExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IAccessibilityUserStateExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda78
            public final Object createExtWith(Object obj) {
                return new AccessibilityUserStateExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IResolverActivityExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda79
            public final Object createExtWith(Object obj) {
                return new ResolverActivityExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IResolverHelperExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda80
            public final Object createExtWith(Object obj) {
                return new ResolverHelperExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IResolverListAdapterExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda81
            public final Object createExtWith(Object obj) {
                return new ResolverListAdapterExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IResolverDrawerLayoutExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda82
            public final Object createExtWith(Object obj) {
                return new ResolverDrawerLayoutExtImp(obj);
            }
        });
        ExtRegistry.registerExt(ISelectableTargetInfoExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda83
            public final Object createExtWith(Object obj) {
                return new SelectableTargetInfoExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOneTraceExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda84
            public final Object createExtWith(Object obj) {
                IOneTraceExt iOneTraceExt;
                iOneTraceExt = OneTraceExtImpl.INSTANCE;
                return iOneTraceExt;
            }
        });
        ExtRegistry.registerExt(IMtpDatabaseExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda85
            public final Object createExtWith(Object obj) {
                return MtpDatabaseExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IBinderProxyExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda86
            public final Object createExtWith(Object obj) {
                return new BinderProxyExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IEnvironmentExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda87
            public final Object createExtWith(Object obj) {
                return EnvironmentExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IPowerManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda89
            public final Object createExtWith(Object obj) {
                return new PowerManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IPowerManagerExt.IStaticExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda90
            public final Object createExtWith(Object obj) {
                return PowerManagerExtImpl.StaticExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(ISettingsExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda91
            public final Object createExtWith(Object obj) {
                return SettingsExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(ISplitScreenManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda92
            public final Object createExtWith(Object obj) {
                return SplitScreenManagerExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IAutoLayoutAdapterExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda93
            public final Object createExtWith(Object obj) {
                return OplusAutoLayoutAdapterExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IAutofillManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda94
            public final Object createExtWith(Object obj) {
                return new AutofillManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOplusCfgFilePolicyExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda95
            public final Object createExtWith(Object obj) {
                return OplusCfgFilePolicyExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(INotificationExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda96
            public final Object createExtWith(Object obj) {
                return new OplusINotificationExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(INotificationManagerExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda97
            public final Object createExtWith(Object obj) {
                return new OplusINotificationManagerExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IStrictModeExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda98
            public final Object createExtWith(Object obj) {
                return new StrictModeExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(ISQLiteConnectionExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda100
            public final Object createExtWith(Object obj) {
                return new SQLiteConnectionExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOplusExtPluginFactoryExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda101
            public final Object createExtWith(Object obj) {
                return OplusExtPluginFactoryExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IAppWidgetProviderExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda102
            public final Object createExtWith(Object obj) {
                return new AppWidgetProviderExtImpl(obj);
            }
        });
        ExtRegistry.registerExt(IOplusJankMonitorExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda103
            public final Object createExtWith(Object obj) {
                return OplusJankMonitorExtImpl.getInstance(obj);
            }
        });
        ExtRegistry.registerExt(IMediaProjectionManagerServiceExt.class, new ExtCreator() { // from class: system.ext.registry.BaseCommonCoreRegistry$$ExternalSyntheticLambda104
            public final Object createExtWith(Object obj) {
                return MediaProjectionManagerServiceExtImpl.getInstance(obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IRegisteredServicesCacheExt lambda$static$0(Object base) {
        return new RegisteredServicesCacheExtImpl();
    }
}
