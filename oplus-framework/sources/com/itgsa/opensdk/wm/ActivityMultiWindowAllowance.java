package com.itgsa.opensdk.wm;

/* loaded from: classes.dex */
public class ActivityMultiWindowAllowance {
    public boolean allowSelfSplitToSplitScreen;
    public boolean allowSwitchToFullScreen;
    public boolean allowSwitchToSplitScreen;

    public ActivityMultiWindowAllowance(boolean allowSelfSplitToSplitScreen, boolean allowSwitchToSplitScreen, boolean allowSwitchToFullScreen) {
        this.allowSelfSplitToSplitScreen = allowSelfSplitToSplitScreen;
        this.allowSwitchToFullScreen = allowSwitchToFullScreen;
        this.allowSwitchToSplitScreen = allowSwitchToSplitScreen;
    }
}
