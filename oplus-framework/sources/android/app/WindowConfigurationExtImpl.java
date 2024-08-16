package android.app;

/* loaded from: classes.dex */
public class WindowConfigurationExtImpl implements IWindowConfigurationExt {
    public WindowConfigurationExtImpl(Object base) {
    }

    public boolean isWindowingZoomMode(int windowMode) {
        return windowMode == 100;
    }

    public boolean isWindowingComactMode(int windowMode) {
        return windowMode == 120;
    }

    public int getWindowingComactMode() {
        return 120;
    }

    public boolean isWindowingBracketMode(int windowMode) {
        return windowMode == 115;
    }
}
