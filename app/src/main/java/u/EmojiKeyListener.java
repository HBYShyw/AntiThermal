package u;

import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import androidx.emoji2.text.EmojiCompat;

/* compiled from: EmojiKeyListener.java */
/* renamed from: u.e, reason: use source file name */
/* loaded from: classes.dex */
final class EmojiKeyListener implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    private final KeyListener f18827a;

    /* renamed from: b, reason: collision with root package name */
    private final a f18828b;

    /* compiled from: EmojiKeyListener.java */
    /* renamed from: u.e$a */
    /* loaded from: classes.dex */
    public static class a {
        public boolean a(Editable editable, int i10, KeyEvent keyEvent) {
            return EmojiCompat.f(editable, i10, keyEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmojiKeyListener(KeyListener keyListener) {
        this(keyListener, new a());
    }

    @Override // android.text.method.KeyListener
    public void clearMetaKeyState(View view, Editable editable, int i10) {
        this.f18827a.clearMetaKeyState(view, editable, i10);
    }

    @Override // android.text.method.KeyListener
    public int getInputType() {
        return this.f18827a.getInputType();
    }

    @Override // android.text.method.KeyListener
    public boolean onKeyDown(View view, Editable editable, int i10, KeyEvent keyEvent) {
        return this.f18828b.a(editable, i10, keyEvent) || this.f18827a.onKeyDown(view, editable, i10, keyEvent);
    }

    @Override // android.text.method.KeyListener
    public boolean onKeyOther(View view, Editable editable, KeyEvent keyEvent) {
        return this.f18827a.onKeyOther(view, editable, keyEvent);
    }

    @Override // android.text.method.KeyListener
    public boolean onKeyUp(View view, Editable editable, int i10, KeyEvent keyEvent) {
        return this.f18827a.onKeyUp(view, editable, i10, keyEvent);
    }

    EmojiKeyListener(KeyListener keyListener, a aVar) {
        this.f18827a = keyListener;
        this.f18828b = aVar;
    }
}
