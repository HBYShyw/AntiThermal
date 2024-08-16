package com.android.server.display.brightness;

import android.R;
import android.content.Context;
import android.hardware.display.DisplayManagerInternal;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.display.brightness.strategy.BoostBrightnessStrategy;
import com.android.server.display.brightness.strategy.DisplayBrightnessStrategy;
import com.android.server.display.brightness.strategy.DozeBrightnessStrategy;
import com.android.server.display.brightness.strategy.FollowerBrightnessStrategy;
import com.android.server.display.brightness.strategy.InvalidBrightnessStrategy;
import com.android.server.display.brightness.strategy.OverrideBrightnessStrategy;
import com.android.server.display.brightness.strategy.ScreenOffBrightnessStrategy;
import com.android.server.display.brightness.strategy.TemporaryBrightnessStrategy;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayBrightnessStrategySelector {
    private static final String TAG = "DisplayBrightnessStrategySelector";
    private final boolean mAllowAutoBrightnessWhileDozingConfig;
    private final BoostBrightnessStrategy mBoostBrightnessStrategy;
    private final int mDisplayId;
    private final DozeBrightnessStrategy mDozeBrightnessStrategy;
    private final FollowerBrightnessStrategy mFollowerBrightnessStrategy;
    private final InvalidBrightnessStrategy mInvalidBrightnessStrategy;
    private String mOldBrightnessStrategyName;
    private final OverrideBrightnessStrategy mOverrideBrightnessStrategy;
    private final ScreenOffBrightnessStrategy mScreenOffBrightnessStrategy;
    private final TemporaryBrightnessStrategy mTemporaryBrightnessStrategy;

    public DisplayBrightnessStrategySelector(Context context, Injector injector, int i) {
        injector = injector == null ? new Injector() : injector;
        this.mDisplayId = i;
        this.mDozeBrightnessStrategy = injector.getDozeBrightnessStrategy();
        this.mScreenOffBrightnessStrategy = injector.getScreenOffBrightnessStrategy();
        this.mOverrideBrightnessStrategy = injector.getOverrideBrightnessStrategy();
        this.mTemporaryBrightnessStrategy = injector.getTemporaryBrightnessStrategy();
        this.mBoostBrightnessStrategy = injector.getBoostBrightnessStrategy();
        this.mFollowerBrightnessStrategy = injector.getFollowerBrightnessStrategy(i);
        InvalidBrightnessStrategy invalidBrightnessStrategy = injector.getInvalidBrightnessStrategy();
        this.mInvalidBrightnessStrategy = invalidBrightnessStrategy;
        this.mAllowAutoBrightnessWhileDozingConfig = context.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromKey);
        this.mOldBrightnessStrategyName = invalidBrightnessStrategy.getName();
    }

    public DisplayBrightnessStrategy selectStrategy(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, int i) {
        DisplayBrightnessStrategy displayBrightnessStrategy = this.mInvalidBrightnessStrategy;
        if (i == 1) {
            displayBrightnessStrategy = this.mScreenOffBrightnessStrategy;
        } else if (shouldUseDozeBrightnessStrategy(displayPowerRequest)) {
            displayBrightnessStrategy = this.mDozeBrightnessStrategy;
        } else if (BrightnessUtils.isValidBrightnessValue(this.mFollowerBrightnessStrategy.getBrightnessToFollow())) {
            displayBrightnessStrategy = this.mFollowerBrightnessStrategy;
        } else if (displayPowerRequest.boostScreenBrightness) {
            displayBrightnessStrategy = this.mBoostBrightnessStrategy;
        } else if (BrightnessUtils.isValidBrightnessValue(displayPowerRequest.screenBrightnessOverride)) {
            displayBrightnessStrategy = this.mOverrideBrightnessStrategy;
        } else if (BrightnessUtils.isValidBrightnessValue(this.mTemporaryBrightnessStrategy.getTemporaryScreenBrightness())) {
            displayBrightnessStrategy = this.mTemporaryBrightnessStrategy;
        }
        if (!this.mOldBrightnessStrategyName.equals(displayBrightnessStrategy.getName())) {
            Slog.i(TAG, "Changing the DisplayBrightnessStrategy from " + this.mOldBrightnessStrategyName + " to" + displayBrightnessStrategy.getName() + " for display " + this.mDisplayId);
            this.mOldBrightnessStrategyName = displayBrightnessStrategy.getName();
        }
        return displayBrightnessStrategy;
    }

    public TemporaryBrightnessStrategy getTemporaryDisplayBrightnessStrategy() {
        return this.mTemporaryBrightnessStrategy;
    }

    public FollowerBrightnessStrategy getFollowerDisplayBrightnessStrategy() {
        return this.mFollowerBrightnessStrategy;
    }

    public boolean isAllowAutoBrightnessWhileDozingConfig() {
        return this.mAllowAutoBrightnessWhileDozingConfig;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println();
        printWriter.println("DisplayBrightnessStrategySelector:");
        printWriter.println("  mDisplayId= " + this.mDisplayId);
        printWriter.println("  mOldBrightnessStrategyName= " + this.mOldBrightnessStrategyName);
        printWriter.println("  mAllowAutoBrightnessWhileDozingConfig= " + this.mAllowAutoBrightnessWhileDozingConfig);
        this.mTemporaryBrightnessStrategy.dump(new IndentingPrintWriter(printWriter, " "));
    }

    private boolean shouldUseDozeBrightnessStrategy(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest) {
        return displayPowerRequest.policy == 1 && !this.mAllowAutoBrightnessWhileDozingConfig;
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class Injector {
        Injector() {
        }

        ScreenOffBrightnessStrategy getScreenOffBrightnessStrategy() {
            return new ScreenOffBrightnessStrategy();
        }

        DozeBrightnessStrategy getDozeBrightnessStrategy() {
            return new DozeBrightnessStrategy();
        }

        OverrideBrightnessStrategy getOverrideBrightnessStrategy() {
            return new OverrideBrightnessStrategy();
        }

        TemporaryBrightnessStrategy getTemporaryBrightnessStrategy() {
            return new TemporaryBrightnessStrategy();
        }

        BoostBrightnessStrategy getBoostBrightnessStrategy() {
            return new BoostBrightnessStrategy();
        }

        FollowerBrightnessStrategy getFollowerBrightnessStrategy(int i) {
            return new FollowerBrightnessStrategy(i);
        }

        InvalidBrightnessStrategy getInvalidBrightnessStrategy() {
            return new InvalidBrightnessStrategy();
        }
    }
}
