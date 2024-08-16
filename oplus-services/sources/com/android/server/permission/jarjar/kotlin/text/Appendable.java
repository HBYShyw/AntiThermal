package com.android.server.permission.jarjar.kotlin.text;

import com.android.server.permission.jarjar.kotlin.jvm.functions.Functions;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Appendable.kt */
/* renamed from: com.android.server.permission.jarjar.kotlin.text.StringsKt__AppendableKt, reason: use source file name */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class Appendable {
    /* JADX WARN: Multi-variable type inference failed */
    public static <T> void appendElement(@NotNull java.lang.Appendable appendable, T t, @Nullable Functions<? super T, ? extends CharSequence> functions) {
        Intrinsics.checkNotNullParameter(appendable, "<this>");
        if (functions != null) {
            appendable.append(functions.invoke(t));
            return;
        }
        if (t == 0 ? true : t instanceof CharSequence) {
            appendable.append((CharSequence) t);
        } else if (t instanceof Character) {
            appendable.append(((Character) t).charValue());
        } else {
            appendable.append(String.valueOf(t));
        }
    }
}
