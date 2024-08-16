package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class VelocityTracker {
    public static final int VELOCITY_TRACKER_STRATEGY_DEFAULT = getVelocityTrackerStrategyDefault();
    public static final int VELOCITY_TRACKER_STRATEGY_IMPULSE = getVelocityTrackerStrategyImpulse();
    public static final int VELOCITY_TRACKER_STRATEGY_LSQ1 = getVelocityTrackerStrategyLsq1();
    public static final int VELOCITY_TRACKER_STRATEGY_LSQ2 = getVelocityTrackerStrategyLsq2();
    public static final int VELOCITY_TRACKER_STRATEGY_LSQ3 = getVelocityTrackerStrategyLsq3();
    public static final int VELOCITY_TRACKER_STRATEGY_WLSQ2_DELTA = getVelocityTrackerStrategyWlsq2Delta();
    public static final int VELOCITY_TRACKER_STRATEGY_WLSQ2_CENTRAL = getVelocityTrackerStrategyWlsq2Central();
    public static final int VELOCITY_TRACKER_STRATEGY_WLSQ2_RECENT = getVelocityTrackerStrategyWlsq2Recent();
    public static final int VELOCITY_TRACKER_STRATEGY_INT1 = getVelocityTrackerStrategyInt1();
    public static final int VELOCITY_TRACKER_STRATEGY_INT2 = getVelocityTrackerStrategyInt2();
    public static final int VELOCITY_TRACKER_STRATEGY_LEGACY = getOplusVelocityTrackerStrategyLegacy();

    public static android.view.VelocityTracker obtain(int strategy) {
        return android.view.VelocityTracker.obtain(strategy);
    }

    private VelocityTracker() {
    }

    private static int getVelocityTrackerStrategyDefault() {
        return -1;
    }

    private static int getVelocityTrackerStrategyImpulse() {
        return 0;
    }

    private static int getVelocityTrackerStrategyLsq1() {
        return 1;
    }

    private static int getVelocityTrackerStrategyLsq2() {
        return 2;
    }

    private static int getVelocityTrackerStrategyLsq3() {
        return 3;
    }

    private static int getVelocityTrackerStrategyWlsq2Delta() {
        return 4;
    }

    private static int getVelocityTrackerStrategyWlsq2Central() {
        return 5;
    }

    private static int getVelocityTrackerStrategyWlsq2Recent() {
        return 6;
    }

    private static int getVelocityTrackerStrategyInt1() {
        return 7;
    }

    private static int getVelocityTrackerStrategyInt2() {
        return 8;
    }

    private static int getOplusVelocityTrackerStrategyLegacy() {
        return 9;
    }
}
