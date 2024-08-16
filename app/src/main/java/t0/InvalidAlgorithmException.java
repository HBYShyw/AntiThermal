package t0;

/* compiled from: InvalidAlgorithmException.java */
/* renamed from: t0.b, reason: use source file name */
/* loaded from: classes.dex */
public class InvalidAlgorithmException extends Exception {
    public InvalidAlgorithmException(String str) {
        super("Algorithm(" + str + ") not supported in current scene.");
    }
}
