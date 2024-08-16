package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.appcompat.R$styleable;
import u.EmojiTextViewHelper;

/* compiled from: AppCompatEmojiTextHelper.java */
/* renamed from: androidx.appcompat.widget.i, reason: use source file name */
/* loaded from: classes.dex */
class AppCompatEmojiTextHelper {

    /* renamed from: a, reason: collision with root package name */
    private final TextView f1230a;

    /* renamed from: b, reason: collision with root package name */
    private final EmojiTextViewHelper f1231b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatEmojiTextHelper(TextView textView) {
        this.f1230a = textView;
        this.f1231b = new EmojiTextViewHelper(textView, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputFilter[] a(InputFilter[] inputFilterArr) {
        return this.f1231b.a(inputFilterArr);
    }

    public boolean b() {
        return this.f1231b.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(AttributeSet attributeSet, int i10) {
        TypedArray obtainStyledAttributes = this.f1230a.getContext().obtainStyledAttributes(attributeSet, R$styleable.AppCompatTextView, i10, 0);
        try {
            int i11 = R$styleable.AppCompatTextView_emojiCompatEnabled;
            boolean z10 = obtainStyledAttributes.hasValue(i11) ? obtainStyledAttributes.getBoolean(i11, true) : true;
            obtainStyledAttributes.recycle();
            e(z10);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(boolean z10) {
        this.f1231b.c(z10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(boolean z10) {
        this.f1231b.d(z10);
    }

    public TransformationMethod f(TransformationMethod transformationMethod) {
        return this.f1231b.e(transformationMethod);
    }
}
