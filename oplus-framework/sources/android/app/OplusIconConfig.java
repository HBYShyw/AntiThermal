package android.app;

import android.graphics.Path;

/* loaded from: classes.dex */
public class OplusIconConfig {
    public static final int UPDATE_DARKMODE_CONFIG = 2;
    public static final int UPDATE_ICON_CONFIG = 1;
    public static final int UPDATE_NONE = 0;
    private boolean artPlusOn;
    private int foregroundSize;
    private int iconShape;
    private int iconSize;
    private boolean isForeign;
    private Path shapePath;
    private int theme;
    private boolean isEmpty = true;
    private int mNeedUpdateConfig = 0;
    private int mUserId = -1;
    private boolean mIsDarkMode = false;
    private boolean mEnableDarkModeIcon = true;

    public int getIconShape() {
        return this.iconShape;
    }

    public void setIconShape(int iconShape) {
        this.iconShape = iconShape;
    }

    public int getTheme() {
        return this.theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getIconSize() {
        return this.iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    public int getForegroundSize() {
        return this.foregroundSize;
    }

    public void setForegroundSize(int foregroundSize) {
        this.foregroundSize = foregroundSize;
    }

    public boolean isArtPlusOn() {
        return this.artPlusOn;
    }

    public void setArtPlusOn(boolean artPlusOn) {
        this.artPlusOn = artPlusOn;
    }

    public Path getShapePath() {
        return this.shapePath;
    }

    public void setShapePath(Path shapePath) {
        this.shapePath = new Path(shapePath);
    }

    public boolean isForeign() {
        return this.isForeign;
    }

    public void setForeign(boolean foreign) {
        this.isForeign = foreign;
    }

    public void setEmpty(boolean empty) {
        this.isEmpty = empty;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public void addUpdateConfig(int update) {
        this.mNeedUpdateConfig |= update;
    }

    public void removeUpdateConfig(int update) {
        this.mNeedUpdateConfig &= ~update;
    }

    public boolean isNeedUpdate() {
        return this.mNeedUpdateConfig != 0;
    }

    public int getNeedUpdateConfig() {
        return this.mNeedUpdateConfig;
    }

    public void setIsDarkMode(boolean isDarkMode) {
        this.mIsDarkMode = isDarkMode;
    }

    public boolean isDarkMode() {
        return this.mIsDarkMode;
    }

    public void setEnableDarkModeIcon(boolean enableDarkModeIcon) {
        this.mEnableDarkModeIcon = enableDarkModeIcon;
    }

    public boolean isEnableDarkModeIcon() {
        return this.mEnableDarkModeIcon;
    }

    public void setUserId(int userId) {
        this.mUserId = userId;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public String toString() {
        return "OplusIconConfig = [ isForeign : " + this.isForeign + ",theme : " + this.theme + ",iconSize : " + this.iconSize + ",iconShape : " + this.iconShape + ",foregroundSize : " + this.foregroundSize + ",artPlusOn : " + this.artPlusOn + ",shapePath ：" + this.shapePath + ", nightMode : " + this.mIsDarkMode + ";enableDarkModeIcon = " + this.mEnableDarkModeIcon + ", userId = " + this.mUserId + " ]";
    }
}
