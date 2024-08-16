package android.view;

/* loaded from: classes.dex */
public class WindowExtImpl implements IWindowExt {
    private Window mBase;
    private OplusColorModeManager mColorModeMgr;

    public WindowExtImpl(Object base) {
        this.mColorModeMgr = null;
        this.mColorModeMgr = OplusColorModeManager.getInstance();
        this.mBase = (Window) base;
    }

    public int getColorMode(int colorMode) {
        int tempmode = this.mColorModeMgr.getColorMode(colorMode);
        return tempmode;
    }

    public void setCloseOnTouchOutForFlexible(boolean flexibleActivitySuitable) {
        if (!this.mBase.getWrapper().isSetCloseOnTouchOutside()) {
            this.mBase.getWrapper().setCloseOnTouchOutside(flexibleActivitySuitable);
        }
    }
}
