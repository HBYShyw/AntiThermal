package android.os;

import com.oplus.reflect.MethodParams;
import com.oplus.reflect.RefClass;
import com.oplus.reflect.RefStaticMethod;

@Deprecated
/* loaded from: classes.dex */
public class OplusMirrorProcess {
    public static Class<?> TYPE = RefClass.load(OplusMirrorProcess.class, Process.class);

    @MethodParams({int.class})
    public static RefStaticMethod<String> getProcessNameByPid;
}
