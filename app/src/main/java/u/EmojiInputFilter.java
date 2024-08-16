package u;

import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.TextView;
import androidx.emoji2.text.EmojiCompat;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/* compiled from: EmojiInputFilter.java */
/* renamed from: u.d, reason: use source file name */
/* loaded from: classes.dex */
final class EmojiInputFilter implements InputFilter {

    /* renamed from: a, reason: collision with root package name */
    private final TextView f18823a;

    /* renamed from: b, reason: collision with root package name */
    private EmojiCompat.e f18824b;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EmojiInputFilter.java */
    /* renamed from: u.d$a */
    /* loaded from: classes.dex */
    public static class a extends EmojiCompat.e {

        /* renamed from: a, reason: collision with root package name */
        private final Reference<TextView> f18825a;

        /* renamed from: b, reason: collision with root package name */
        private final Reference<EmojiInputFilter> f18826b;

        a(TextView textView, EmojiInputFilter emojiInputFilter) {
            this.f18825a = new WeakReference(textView);
            this.f18826b = new WeakReference(emojiInputFilter);
        }

        private boolean c(TextView textView, InputFilter inputFilter) {
            InputFilter[] filters;
            if (inputFilter == null || textView == null || (filters = textView.getFilters()) == null) {
                return false;
            }
            for (InputFilter inputFilter2 : filters) {
                if (inputFilter2 == inputFilter) {
                    return true;
                }
            }
            return false;
        }

        @Override // androidx.emoji2.text.EmojiCompat.e
        public void b() {
            CharSequence text;
            CharSequence o10;
            super.b();
            TextView textView = this.f18825a.get();
            if (c(textView, this.f18826b.get()) && textView.isAttachedToWindow() && text != (o10 = EmojiCompat.b().o((text = textView.getText())))) {
                int selectionStart = Selection.getSelectionStart(o10);
                int selectionEnd = Selection.getSelectionEnd(o10);
                textView.setText(o10);
                if (o10 instanceof Spannable) {
                    EmojiInputFilter.b((Spannable) o10, selectionStart, selectionEnd);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmojiInputFilter(TextView textView) {
        this.f18823a = textView;
    }

    private EmojiCompat.e a() {
        if (this.f18824b == null) {
            this.f18824b = new a(this.f18823a, this);
        }
        return this.f18824b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(Spannable spannable, int i10, int i11) {
        if (i10 >= 0 && i11 >= 0) {
            Selection.setSelection(spannable, i10, i11);
        } else if (i10 >= 0) {
            Selection.setSelection(spannable, i10);
        } else if (i11 >= 0) {
            Selection.setSelection(spannable, i11);
        }
    }

    @Override // android.text.InputFilter
    public CharSequence filter(CharSequence charSequence, int i10, int i11, Spanned spanned, int i12, int i13) {
        if (this.f18823a.isInEditMode()) {
            return charSequence;
        }
        int d10 = EmojiCompat.b().d();
        if (d10 != 0) {
            boolean z10 = true;
            if (d10 == 1) {
                if (i13 == 0 && i12 == 0 && spanned.length() == 0 && charSequence == this.f18823a.getText()) {
                    z10 = false;
                }
                if (!z10 || charSequence == null) {
                    return charSequence;
                }
                if (i10 != 0 || i11 != charSequence.length()) {
                    charSequence = charSequence.subSequence(i10, i11);
                }
                return EmojiCompat.b().p(charSequence, 0, charSequence.length());
            }
            if (d10 != 3) {
                return charSequence;
            }
        }
        EmojiCompat.b().s(a());
        return charSequence;
    }
}
