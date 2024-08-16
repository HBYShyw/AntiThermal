package u;

import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.SparseArray;
import android.widget.TextView;
import androidx.core.util.Preconditions;
import androidx.emoji2.text.EmojiCompat;

/* compiled from: EmojiTextViewHelper.java */
/* renamed from: u.f, reason: use source file name */
/* loaded from: classes.dex */
public final class EmojiTextViewHelper {

    /* renamed from: a, reason: collision with root package name */
    private final b f18829a;

    /* compiled from: EmojiTextViewHelper.java */
    /* renamed from: u.f$a */
    /* loaded from: classes.dex */
    private static class a extends b {

        /* renamed from: a, reason: collision with root package name */
        private final TextView f18830a;

        /* renamed from: b, reason: collision with root package name */
        private final EmojiInputFilter f18831b;

        /* renamed from: c, reason: collision with root package name */
        private boolean f18832c = true;

        a(TextView textView) {
            this.f18830a = textView;
            this.f18831b = new EmojiInputFilter(textView);
        }

        private InputFilter[] f(InputFilter[] inputFilterArr) {
            int length = inputFilterArr.length;
            for (InputFilter inputFilter : inputFilterArr) {
                if (inputFilter == this.f18831b) {
                    return inputFilterArr;
                }
            }
            InputFilter[] inputFilterArr2 = new InputFilter[inputFilterArr.length + 1];
            System.arraycopy(inputFilterArr, 0, inputFilterArr2, 0, length);
            inputFilterArr2[length] = this.f18831b;
            return inputFilterArr2;
        }

        private SparseArray<InputFilter> g(InputFilter[] inputFilterArr) {
            SparseArray<InputFilter> sparseArray = new SparseArray<>(1);
            for (int i10 = 0; i10 < inputFilterArr.length; i10++) {
                if (inputFilterArr[i10] instanceof EmojiInputFilter) {
                    sparseArray.put(i10, inputFilterArr[i10]);
                }
            }
            return sparseArray;
        }

        private InputFilter[] h(InputFilter[] inputFilterArr) {
            SparseArray<InputFilter> g6 = g(inputFilterArr);
            if (g6.size() == 0) {
                return inputFilterArr;
            }
            int length = inputFilterArr.length;
            InputFilter[] inputFilterArr2 = new InputFilter[inputFilterArr.length - g6.size()];
            int i10 = 0;
            for (int i11 = 0; i11 < length; i11++) {
                if (g6.indexOfKey(i11) < 0) {
                    inputFilterArr2[i10] = inputFilterArr[i11];
                    i10++;
                }
            }
            return inputFilterArr2;
        }

        private TransformationMethod j(TransformationMethod transformationMethod) {
            return transformationMethod instanceof EmojiTransformationMethod ? ((EmojiTransformationMethod) transformationMethod).a() : transformationMethod;
        }

        private void k() {
            this.f18830a.setFilters(a(this.f18830a.getFilters()));
        }

        private TransformationMethod m(TransformationMethod transformationMethod) {
            return ((transformationMethod instanceof EmojiTransformationMethod) || (transformationMethod instanceof PasswordTransformationMethod)) ? transformationMethod : new EmojiTransformationMethod(transformationMethod);
        }

        @Override // u.EmojiTextViewHelper.b
        InputFilter[] a(InputFilter[] inputFilterArr) {
            if (!this.f18832c) {
                return h(inputFilterArr);
            }
            return f(inputFilterArr);
        }

        @Override // u.EmojiTextViewHelper.b
        public boolean b() {
            return this.f18832c;
        }

        @Override // u.EmojiTextViewHelper.b
        void c(boolean z10) {
            if (z10) {
                l();
            }
        }

        @Override // u.EmojiTextViewHelper.b
        void d(boolean z10) {
            this.f18832c = z10;
            l();
            k();
        }

        @Override // u.EmojiTextViewHelper.b
        TransformationMethod e(TransformationMethod transformationMethod) {
            if (this.f18832c) {
                return m(transformationMethod);
            }
            return j(transformationMethod);
        }

        void i(boolean z10) {
            this.f18832c = z10;
        }

        void l() {
            this.f18830a.setTransformationMethod(e(this.f18830a.getTransformationMethod()));
        }
    }

    /* compiled from: EmojiTextViewHelper.java */
    /* renamed from: u.f$b */
    /* loaded from: classes.dex */
    static class b {
        b() {
        }

        InputFilter[] a(InputFilter[] inputFilterArr) {
            throw null;
        }

        public boolean b() {
            throw null;
        }

        void c(boolean z10) {
            throw null;
        }

        void d(boolean z10) {
            throw null;
        }

        TransformationMethod e(TransformationMethod transformationMethod) {
            throw null;
        }
    }

    /* compiled from: EmojiTextViewHelper.java */
    /* renamed from: u.f$c */
    /* loaded from: classes.dex */
    private static class c extends b {

        /* renamed from: a, reason: collision with root package name */
        private final a f18833a;

        c(TextView textView) {
            this.f18833a = new a(textView);
        }

        private boolean f() {
            return !EmojiCompat.h();
        }

        @Override // u.EmojiTextViewHelper.b
        InputFilter[] a(InputFilter[] inputFilterArr) {
            return f() ? inputFilterArr : this.f18833a.a(inputFilterArr);
        }

        @Override // u.EmojiTextViewHelper.b
        public boolean b() {
            return this.f18833a.b();
        }

        @Override // u.EmojiTextViewHelper.b
        void c(boolean z10) {
            if (f()) {
                return;
            }
            this.f18833a.c(z10);
        }

        @Override // u.EmojiTextViewHelper.b
        void d(boolean z10) {
            if (f()) {
                this.f18833a.i(z10);
            } else {
                this.f18833a.d(z10);
            }
        }

        @Override // u.EmojiTextViewHelper.b
        TransformationMethod e(TransformationMethod transformationMethod) {
            return f() ? transformationMethod : this.f18833a.e(transformationMethod);
        }
    }

    public EmojiTextViewHelper(TextView textView, boolean z10) {
        Preconditions.e(textView, "textView cannot be null");
        if (!z10) {
            this.f18829a = new c(textView);
        } else {
            this.f18829a = new a(textView);
        }
    }

    public InputFilter[] a(InputFilter[] inputFilterArr) {
        return this.f18829a.a(inputFilterArr);
    }

    public boolean b() {
        return this.f18829a.b();
    }

    public void c(boolean z10) {
        this.f18829a.c(z10);
    }

    public void d(boolean z10) {
        this.f18829a.d(z10);
    }

    public TransformationMethod e(TransformationMethod transformationMethod) {
        return this.f18829a.e(transformationMethod);
    }
}
