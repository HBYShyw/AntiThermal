package android.graphics;

import android.graphics.Path;

/* loaded from: classes.dex */
public class PathExtImpl implements IPathExt {
    private boolean mIsAddArea = false;
    private Path mPath;

    public PathExtImpl(Object base) {
        this.mPath = (Path) base;
    }

    public boolean isAddArea() {
        return this.mIsAddArea;
    }

    private void setIsAddRect(boolean isAddArea) {
        this.mIsAddArea = isAddArea;
    }

    private void setIsAddRect(IPathExt pathExt) {
        if (pathExt != null) {
            this.mIsAddArea = pathExt.isAddArea();
        }
    }

    public void hookPath(IPathExt pathExt) {
        setIsAddRect(pathExt);
    }

    public void hookAddRect() {
        setIsAddRect(true);
    }

    public void hookAddRoundRect(float left, float top, float right, float bottom, float rx, float ry, Path.Direction dir) {
        setIsAddRect(true);
    }

    public void hookAddRoundRect(float left, float top, float right, float bottom, float[] radii, Path.Direction dir) {
        setIsAddRect(true);
    }
}
