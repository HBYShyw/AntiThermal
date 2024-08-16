package android.widget;

import android.content.Context;
import android.os.customize.OplusCustomizeRestrictionManager;

/* loaded from: classes.dex */
public class OplusCustomizeTextViewFeature implements IOplusCustomizeTextViewFeature {
    private static final String TAG = "OplusCustomizeTextViewFeature";
    private static OplusCustomizeRestrictionManager sOplusCustomizeRestrictionManager;
    private static OplusCustomizeTextViewFeature sOplusCustomizeTextViewFeature = null;

    private OplusCustomizeTextViewFeature() {
    }

    public static synchronized OplusCustomizeTextViewFeature getInstance() {
        OplusCustomizeTextViewFeature oplusCustomizeTextViewFeature;
        synchronized (OplusCustomizeTextViewFeature.class) {
            if (sOplusCustomizeTextViewFeature == null) {
                sOplusCustomizeTextViewFeature = new OplusCustomizeTextViewFeature();
            }
            oplusCustomizeTextViewFeature = sOplusCustomizeTextViewFeature;
        }
        return oplusCustomizeTextViewFeature;
    }

    @Override // android.widget.IOplusCustomizeTextViewFeature
    public void init(Context context) {
        sOplusCustomizeRestrictionManager = OplusCustomizeRestrictionManager.getInstance(context);
    }

    @Override // android.widget.IOplusCustomizeTextViewFeature
    public boolean getClipboardStatus() {
        OplusCustomizeRestrictionManager oplusCustomizeRestrictionManager = sOplusCustomizeRestrictionManager;
        if (oplusCustomizeRestrictionManager != null) {
            return oplusCustomizeRestrictionManager.getClipboardStatus();
        }
        return true;
    }
}
