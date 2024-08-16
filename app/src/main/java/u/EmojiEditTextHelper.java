package u;

import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import androidx.core.util.Preconditions;

/* compiled from: EmojiEditTextHelper.java */
/* renamed from: u.a, reason: use source file name */
/* loaded from: classes.dex */
public final class EmojiEditTextHelper {

    /* renamed from: a, reason: collision with root package name */
    private final b f18813a;

    /* renamed from: b, reason: collision with root package name */
    private int f18814b = Integer.MAX_VALUE;

    /* renamed from: c, reason: collision with root package name */
    private int f18815c = 0;

    /* compiled from: EmojiEditTextHelper.java */
    /* renamed from: u.a$a */
    /* loaded from: classes.dex */
    private static class a extends b {

        /* renamed from: a, reason: collision with root package name */
        private final EditText f18816a;

        /* renamed from: b, reason: collision with root package name */
        private final EmojiTextWatcher f18817b;

        a(EditText editText, boolean z10) {
            this.f18816a = editText;
            EmojiTextWatcher emojiTextWatcher = new EmojiTextWatcher(editText, z10);
            this.f18817b = emojiTextWatcher;
            editText.addTextChangedListener(emojiTextWatcher);
            editText.setEditableFactory(EmojiEditableFactory.getInstance());
        }

        @Override // u.EmojiEditTextHelper.b
        KeyListener a(KeyListener keyListener) {
            if (keyListener instanceof EmojiKeyListener) {
                return keyListener;
            }
            if (keyListener == null) {
                return null;
            }
            return keyListener instanceof NumberKeyListener ? keyListener : new EmojiKeyListener(keyListener);
        }

        @Override // u.EmojiEditTextHelper.b
        InputConnection b(InputConnection inputConnection, EditorInfo editorInfo) {
            return inputConnection instanceof EmojiInputConnection ? inputConnection : new EmojiInputConnection(this.f18816a, inputConnection, editorInfo);
        }

        @Override // u.EmojiEditTextHelper.b
        void c(boolean z10) {
            this.f18817b.c(z10);
        }
    }

    /* compiled from: EmojiEditTextHelper.java */
    /* renamed from: u.a$b */
    /* loaded from: classes.dex */
    static class b {
        b() {
        }

        KeyListener a(KeyListener keyListener) {
            throw null;
        }

        InputConnection b(InputConnection inputConnection, EditorInfo editorInfo) {
            throw null;
        }

        void c(boolean z10) {
            throw null;
        }
    }

    public EmojiEditTextHelper(EditText editText, boolean z10) {
        Preconditions.e(editText, "editText cannot be null");
        this.f18813a = new a(editText, z10);
    }

    public KeyListener a(KeyListener keyListener) {
        return this.f18813a.a(keyListener);
    }

    public InputConnection b(InputConnection inputConnection, EditorInfo editorInfo) {
        if (inputConnection == null) {
            return null;
        }
        return this.f18813a.b(inputConnection, editorInfo);
    }

    public void c(boolean z10) {
        this.f18813a.c(z10);
    }
}
