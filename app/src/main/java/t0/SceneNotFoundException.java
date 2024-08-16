package t0;

/* compiled from: SceneNotFoundException.java */
/* renamed from: t0.d, reason: use source file name */
/* loaded from: classes.dex */
public class SceneNotFoundException extends Exception {
    public SceneNotFoundException(String str) {
        super("Scene(" + str + ") not found.");
    }
}
