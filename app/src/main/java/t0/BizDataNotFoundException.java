package t0;

/* compiled from: BizDataNotFoundException.java */
/* renamed from: t0.a, reason: use source file name */
/* loaded from: classes.dex */
public class BizDataNotFoundException extends Exception {
    public BizDataNotFoundException(String str) {
        super("Biz data query failed, " + str);
    }
}
