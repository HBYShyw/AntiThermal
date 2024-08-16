package u;

import android.annotation.SuppressLint;
import android.text.Editable;
import androidx.emoji2.text.SpannableBuilder;

/* compiled from: EmojiEditableFactory.java */
/* renamed from: u.b, reason: use source file name */
/* loaded from: classes.dex */
final class EmojiEditableFactory extends Editable.Factory {

    /* renamed from: a, reason: collision with root package name */
    private static final Object f18818a = new Object();

    /* renamed from: b, reason: collision with root package name */
    private static volatile Editable.Factory f18819b;

    /* renamed from: c, reason: collision with root package name */
    private static Class<?> f18820c;

    @SuppressLint({"PrivateApi"})
    private EmojiEditableFactory() {
        try {
            f18820c = Class.forName("android.text.DynamicLayout$ChangeWatcher", false, EmojiEditableFactory.class.getClassLoader());
        } catch (Throwable unused) {
        }
    }

    public static Editable.Factory getInstance() {
        if (f18819b == null) {
            synchronized (f18818a) {
                if (f18819b == null) {
                    f18819b = new EmojiEditableFactory();
                }
            }
        }
        return f18819b;
    }

    @Override // android.text.Editable.Factory
    public Editable newEditable(CharSequence charSequence) {
        Class<?> cls = f18820c;
        if (cls != null) {
            return SpannableBuilder.c(cls, charSequence);
        }
        return super.newEditable(charSequence);
    }
}
