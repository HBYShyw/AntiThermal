package sd;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Appendable.kt */
/* renamed from: sd.m, reason: use source file name */
/* loaded from: classes2.dex */
public class Appendable {
    /* JADX WARN: Multi-variable type inference failed */
    public static <T> void a(java.lang.Appendable appendable, T t7, ya.l<? super T, ? extends CharSequence> lVar) {
        za.k.e(appendable, "<this>");
        if (lVar != null) {
            appendable.append(lVar.invoke(t7));
            return;
        }
        if (t7 == 0 ? true : t7 instanceof CharSequence) {
            appendable.append((CharSequence) t7);
        } else if (t7 instanceof Character) {
            appendable.append(((Character) t7).charValue());
        } else {
            appendable.append(String.valueOf(t7));
        }
    }
}
