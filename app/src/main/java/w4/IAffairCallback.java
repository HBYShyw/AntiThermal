package w4;

import android.content.Intent;
import android.os.Bundle;

/* compiled from: IAffairCallback.java */
/* renamed from: w4.b, reason: use source file name */
/* loaded from: classes.dex */
public interface IAffairCallback {
    default void execute(int i10, Intent intent) {
    }

    void execute(int i10, Bundle bundle);

    void registerAction();
}
