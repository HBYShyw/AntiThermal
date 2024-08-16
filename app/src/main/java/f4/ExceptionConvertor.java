package f4;

import com.google.android.play.core.splitinstall.SplitInstallException;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallException;

/* compiled from: ExceptionConvertor.java */
/* renamed from: f4.a, reason: use source file name */
/* loaded from: classes.dex */
public class ExceptionConvertor {
    public static Exception a(Exception exc) {
        return exc instanceof OplusSplitInstallException ? new SplitInstallException(((OplusSplitInstallException) exc).getErrorCode()) : exc;
    }
}
