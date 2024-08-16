package android.view.autolayout;

/* loaded from: classes.dex */
public class AutoLayoutSpecialPolicy implements IAutoLayoutPolicy {
    private IAutoLayoutDrawPolicy mDrawPolicy;

    @Override // android.view.autolayout.IAutoLayoutPolicy
    public IAutoLayoutDrawPolicy getDrawPolicy() {
        if (this.mDrawPolicy == null) {
            this.mDrawPolicy = new AutoLayoutSpecialDrawPolicy();
        }
        return this.mDrawPolicy;
    }
}
