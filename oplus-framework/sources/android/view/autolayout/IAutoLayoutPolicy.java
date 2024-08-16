package android.view.autolayout;

/* loaded from: classes.dex */
public interface IAutoLayoutPolicy {
    public static final IAutoLayoutPolicy DEFAULT = new IAutoLayoutPolicy() { // from class: android.view.autolayout.IAutoLayoutPolicy.1
    };

    default IAutoLayoutDrawPolicy getDrawPolicy() {
        return IAutoLayoutDrawPolicy.DEFAULT;
    }

    default IAutoLayoutMeasurePolicy getMeasurePolicy() {
        return IAutoLayoutMeasurePolicy.DEFAULT;
    }

    default IAutoLayoutLayoutPolicy getLayoutPolicy() {
        return IAutoLayoutLayoutPolicy.DEFAULT;
    }
}
