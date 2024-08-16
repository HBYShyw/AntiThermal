package android.view.autolayout;

/* loaded from: classes.dex */
public class AutoLayoutCommonPolicy implements IAutoLayoutPolicy {
    private IAutoLayoutDrawPolicy mDrawPolicy;
    private IAutoLayoutLayoutPolicy mLayoutPolicy;
    private IAutoLayoutMeasurePolicy mMeasurePolicy;

    @Override // android.view.autolayout.IAutoLayoutPolicy
    public IAutoLayoutDrawPolicy getDrawPolicy() {
        if (this.mDrawPolicy == null) {
            this.mDrawPolicy = new AutoLayoutCommonDrawPolicy();
        }
        return this.mDrawPolicy;
    }

    @Override // android.view.autolayout.IAutoLayoutPolicy
    public IAutoLayoutMeasurePolicy getMeasurePolicy() {
        if (this.mMeasurePolicy == null) {
            this.mMeasurePolicy = new AutoLayoutCommonMeasurePolicy();
        }
        return this.mMeasurePolicy;
    }

    @Override // android.view.autolayout.IAutoLayoutPolicy
    public IAutoLayoutLayoutPolicy getLayoutPolicy() {
        if (this.mLayoutPolicy == null) {
            this.mLayoutPolicy = new AutoLayoutCommonLayoutPolicy();
        }
        return this.mLayoutPolicy;
    }
}
