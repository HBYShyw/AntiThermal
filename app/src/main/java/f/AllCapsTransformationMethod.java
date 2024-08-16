package f;

import android.content.Context;
import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;
import java.util.Locale;

/* compiled from: AllCapsTransformationMethod.java */
/* renamed from: f.a, reason: use source file name */
/* loaded from: classes.dex */
public class AllCapsTransformationMethod implements TransformationMethod {

    /* renamed from: e, reason: collision with root package name */
    private Locale f11253e;

    public AllCapsTransformationMethod(Context context) {
        this.f11253e = context.getResources().getConfiguration().locale;
    }

    @Override // android.text.method.TransformationMethod
    public CharSequence getTransformation(CharSequence charSequence, View view) {
        if (charSequence != null) {
            return charSequence.toString().toUpperCase(this.f11253e);
        }
        return null;
    }

    @Override // android.text.method.TransformationMethod
    public void onFocusChanged(View view, CharSequence charSequence, boolean z10, int i10, Rect rect) {
    }
}
