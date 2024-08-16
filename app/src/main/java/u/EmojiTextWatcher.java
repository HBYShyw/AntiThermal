package u;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.emoji2.text.EmojiCompat;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/* compiled from: EmojiTextWatcher.java */
/* renamed from: u.g, reason: use source file name */
/* loaded from: classes.dex */
final class EmojiTextWatcher implements TextWatcher {

    /* renamed from: e, reason: collision with root package name */
    private final EditText f18834e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f18835f;

    /* renamed from: g, reason: collision with root package name */
    private EmojiCompat.e f18836g;

    /* renamed from: h, reason: collision with root package name */
    private int f18837h = Integer.MAX_VALUE;

    /* renamed from: i, reason: collision with root package name */
    private int f18838i = 0;

    /* renamed from: j, reason: collision with root package name */
    private boolean f18839j = true;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EmojiTextWatcher.java */
    /* renamed from: u.g$a */
    /* loaded from: classes.dex */
    public static class a extends EmojiCompat.e {

        /* renamed from: a, reason: collision with root package name */
        private final Reference<EditText> f18840a;

        a(EditText editText) {
            this.f18840a = new WeakReference(editText);
        }

        @Override // androidx.emoji2.text.EmojiCompat.e
        public void b() {
            super.b();
            EmojiTextWatcher.b(this.f18840a.get(), 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmojiTextWatcher(EditText editText, boolean z10) {
        this.f18834e = editText;
        this.f18835f = z10;
    }

    private EmojiCompat.e a() {
        if (this.f18836g == null) {
            this.f18836g = new a(this.f18834e);
        }
        return this.f18836g;
    }

    static void b(EditText editText, int i10) {
        if (i10 == 1 && editText != null && editText.isAttachedToWindow()) {
            Editable editableText = editText.getEditableText();
            int selectionStart = Selection.getSelectionStart(editableText);
            int selectionEnd = Selection.getSelectionEnd(editableText);
            EmojiCompat.b().o(editableText);
            EmojiInputFilter.b(editableText, selectionStart, selectionEnd);
        }
    }

    private boolean d() {
        return (this.f18839j && (this.f18835f || EmojiCompat.h())) ? false : true;
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
    }

    public void c(boolean z10) {
        if (this.f18839j != z10) {
            if (this.f18836g != null) {
                EmojiCompat.b().t(this.f18836g);
            }
            this.f18839j = z10;
            if (z10) {
                b(this.f18834e, EmojiCompat.b().d());
            }
        }
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        if (this.f18834e.isInEditMode() || d() || i11 > i12 || !(charSequence instanceof Spannable)) {
            return;
        }
        int d10 = EmojiCompat.b().d();
        if (d10 != 0) {
            if (d10 == 1) {
                EmojiCompat.b().r((Spannable) charSequence, i10, i10 + i12, this.f18837h, this.f18838i);
                return;
            } else if (d10 != 3) {
                return;
            }
        }
        EmojiCompat.b().s(a());
    }
}
