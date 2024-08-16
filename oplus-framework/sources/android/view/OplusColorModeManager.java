package android.view;

/* loaded from: classes.dex */
public class OplusColorModeManager {
    private int mColorMode = -1;
    private String TAG = "OplusColorModeManager";

    public static OplusColorModeManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void setColorMode(int colorMode) {
        this.mColorMode = colorMode;
    }

    public int getColorMode(int colorMode) {
        if (colorMode == 9 || colorMode == 10) {
            return colorMode;
        }
        int i = this.mColorMode;
        if (i >= 0 && i <= 2) {
            return i;
        }
        return colorMode;
    }

    /* loaded from: classes.dex */
    private static class LazyHolder {
        private static final OplusColorModeManager INSTANCE = new OplusColorModeManager();

        private LazyHolder() {
        }
    }
}
