package android.service.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.UserHandle;
import android.service.wallpaper.OplusWallpaperLogUtils;
import android.service.wallpaper.WallpaperService;
import android.util.ArrayMap;
import android.util.Log;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.view.Display;
import android.view.SurfaceControl;
import android.window.ClientWindowFrames;
import com.android.internal.os.HandlerCaller;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;

/* loaded from: classes.dex */
public class WallpaperServiceExtImpl implements IWallpaperServiceExt {
    private static final int FLAG_SWITCH_PHYSICAL_DISPLAY = 4096;
    private static final int MSG_UPDATE_SURFACE = 10000;
    public static final String REASON_CONFIG_CHANGE = "onConfigurationChanged";
    private static final int SECONDARY_DISPLAY_ID = 1;
    private static final String TAG = "WallpaperServiceExtImpl";
    private boolean mIsFromSwitchingUser = false;
    private boolean mIsSupportSeparateWallpaper = false;
    private boolean mIsSwitchingEngineOnDisplay = false;
    private boolean mIsSwitchingPhysicalDisplay = false;
    WallpaperService mWallpaperService;
    OplusWallpaperServiceHelper mWallpaperServiceHelper;

    public WallpaperServiceExtImpl(Object base) {
        this.mWallpaperServiceHelper = null;
        this.mWallpaperService = null;
        this.mWallpaperService = (WallpaperService) base;
        this.mWallpaperServiceHelper = new OplusWallpaperServiceHelper();
        initIsSupportSeparateWallpaper();
        if (this.mIsSupportSeparateWallpaper) {
            initIsSwitchingEngineOnDisplay();
        }
    }

    public void addEngine(WallpaperService.Engine engine) {
        OplusWallpaperServiceHelper oplusWallpaperServiceHelper = this.mWallpaperServiceHelper;
        if (oplusWallpaperServiceHelper != null) {
            oplusWallpaperServiceHelper.addEngine(engine);
        }
    }

    public void registerSetingsContentObserver(Context context) {
        OplusWallpaperServiceHelper oplusWallpaperServiceHelper = this.mWallpaperServiceHelper;
        if (oplusWallpaperServiceHelper != null) {
            oplusWallpaperServiceHelper.registerSetingsContentObserver(context);
        }
    }

    public void removeEngine(WallpaperService.Engine engine) {
        OplusWallpaperServiceHelper oplusWallpaperServiceHelper = this.mWallpaperServiceHelper;
        if (oplusWallpaperServiceHelper != null) {
            oplusWallpaperServiceHelper.removeEngine(engine);
        }
    }

    public void unregisterSettingsContentObserver(Context context) {
        OplusWallpaperServiceHelper oplusWallpaperServiceHelper = this.mWallpaperServiceHelper;
        if (oplusWallpaperServiceHelper != null) {
            oplusWallpaperServiceHelper.unregisterSettingsContentObserver(context);
        }
    }

    public void onBind(Intent intent) {
        if (OplusWallpaperLogUtils.DEBUG) {
            Log.d(TAG, "onBind: intent = " + intent);
        }
        this.mIsFromSwitchingUser = "fromSwitchingUser".equals(intent.getIdentifier());
    }

    public void setWallpaperService(WallpaperService service) {
        this.mWallpaperService = service;
    }

    int getUserId() {
        WallpaperService wallpaperService = this.mWallpaperService;
        if (wallpaperService != null) {
            return wallpaperService.getUserId();
        }
        return UserHandle.myUserId();
    }

    public void adjustMessageQueue(Handler handler, Message msg) {
        if (handler != null) {
            if (handler.hasMessages(msg.what)) {
                handler.sendMessage(msg);
                return;
            }
            if (OplusWallpaperLogUtils.DEBUG) {
                Slog.d(TAG, "adjustMessageQueue send msg=" + msg.what + " to front");
            }
            adjustMessageQueueInternal(handler, msg);
        }
    }

    private void adjustMessageQueueInternal(Handler handler, Message msg) {
        if (handler != null) {
            handler.sendMessageAtFrontOfQueue(msg);
        }
    }

    public void onDispatchAppVisibility(boolean visible, boolean debug) {
        if (debug) {
            Log.i(TAG, "Dispatch app visibility: " + visible);
        }
    }

    public boolean isFromSwitchingUser() {
        return this.mIsFromSwitchingUser;
    }

    public void sendWallpaperCommandMsg(HandlerCaller caller, Message msg, WallpaperService.WallpaperCommand command) {
        if (this.mIsSupportSeparateWallpaper && this.mIsSwitchingEngineOnDisplay && "switchPhysicalDisplay".equalsIgnoreCase(command.action) && !caller.getHandler().hasMessages(msg.what)) {
            if (OplusWallpaperLogUtils.DEBUG) {
                Slog.d(TAG, "sendWallpaperCommandMsg post command=" + command.action + " to front");
            }
            this.mIsSwitchingPhysicalDisplay = true;
            adjustMessageQueueInternal(caller.getHandler(), msg);
            return;
        }
        super.sendWallpaperCommandMsg(caller, msg, command);
    }

    private void initIsSupportSeparateWallpaper() {
        this.mIsSupportSeparateWallpaper = enableSeparateWallpaperForMultiDisplay();
        Slog.i(TAG, "initIsSupportSeparateWallpaper=" + this.mIsSupportSeparateWallpaper);
    }

    private void initIsSwitchingEngineOnDisplay() {
        this.mIsSwitchingEngineOnDisplay = !isFoldRemapDisplayDisabled();
        Slog.i(TAG, "initIsSwitchingEngineOnDisplay=" + this.mIsSwitchingEngineOnDisplay);
    }

    private boolean enableSeparateWallpaperForMultiDisplay() {
        return OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_SEPARATE_WALLPAPER_FOR_MULTI_DISPLAY);
    }

    private boolean isFoldRemapDisplayDisabled() {
        return OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_FOLD_REMAP_DISPLAY_DISABLED);
    }

    public boolean blockReportResizeForWallpaper(ClientWindowFrames currentWinFrames, ClientWindowFrames newWinFrames, MergedConfiguration currentMergedConfiguration, MergedConfiguration newMergedConfiguration, int currentDisplayId, int newDisplayId, boolean reportDraw, boolean forceLayout, SurfaceControl surfaceControl) {
        boolean shouldBlock = false;
        boolean frameChanged = (currentWinFrames.frame.equals(newWinFrames.frame) && currentWinFrames.displayFrame.equals(newWinFrames.displayFrame) && currentWinFrames.parentFrame.equals(newWinFrames.parentFrame)) ? false : true;
        boolean configChanged = !currentMergedConfiguration.equals(newMergedConfiguration);
        boolean displayChanged = currentDisplayId != newDisplayId;
        if (!frameChanged && !configChanged && !displayChanged && !forceLayout && !reportDraw) {
            shouldBlock = true;
        }
        if (shouldBlock) {
            Log.d(TAG, "Block update resized of " + surfaceControl);
        }
        return shouldBlock;
    }

    public void registerLogSwitchObserver(Context context) {
        if (Process.isIsolated()) {
            return;
        }
        OplusWallpaperLogUtils.WallpaperLogSwitchObserver observer = new OplusWallpaperLogUtils.WallpaperLogSwitchObserver();
        OplusWallpaperLogUtils.registerLogSwitchObserver(context, observer);
    }

    public int getSwitchingPhysicalDisplayFlag() {
        return this.mIsSwitchingPhysicalDisplay ? 4096 : 0;
    }

    public void setIsSwitchingPhysicalDisplay(boolean isSwitchingPhysicalDisplay) {
        this.mIsSwitchingPhysicalDisplay = isSwitchingPhysicalDisplay;
    }

    public boolean shouldRedraw(String reason, boolean redraw) {
        if (REASON_CONFIG_CHANGE.equals(reason)) {
            return false;
        }
        return redraw;
    }

    public void onConfigurationChanged(Configuration newConfig, ArrayMap<IBinder, WallpaperService.IWallpaperEngineWrapper> engines) {
        HandlerCaller caller;
        if (newConfig != null && engines != null && !engines.isEmpty() && this.mIsSwitchingPhysicalDisplay && this.mIsSwitchingEngineOnDisplay && this.mIsSupportSeparateWallpaper) {
            if (OplusWallpaperLogUtils.DEBUG) {
                Slog.d(TAG, "onConfigurationChanged newConfig: " + newConfig);
            }
            for (WallpaperService.IWallpaperEngineWrapper engineWrapper : engines.values()) {
                if (engineWrapper.mEngine != null && (caller = engineWrapper.mEngine.mCaller) != null) {
                    Message message = caller.obtainMessageO(10000, REASON_CONFIG_CHANGE);
                    caller.sendMessage(message);
                }
            }
            this.mIsSwitchingPhysicalDisplay = false;
        }
    }

    public boolean isSecondaryDisplay(Display display) {
        return this.mIsSupportSeparateWallpaper && isFoldRemapDisplayDisabled() && display != null && display.getDisplayId() == 1;
    }
}
