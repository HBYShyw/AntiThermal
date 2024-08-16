package android.app;

import android.app.servertransaction.TransactionExecutor;
import com.oplus.reflect.RefBoolean;
import com.oplus.reflect.RefClass;

/* loaded from: classes.dex */
public class OplusMirrorTransactionExecutor {
    public static RefBoolean DEBUG_RESOLVER;
    public static Class<?> TYPE = RefClass.load(OplusMirrorTransactionExecutor.class, TransactionExecutor.class);

    public static void setBooleanValue(RefBoolean refBoolean, boolean value) {
        if (refBoolean != null) {
            refBoolean.set((Object) null, value);
        }
    }
}
