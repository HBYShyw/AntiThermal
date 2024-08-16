package de;

import java.util.Arrays;
import java.util.logging.Logger;
import kotlin.Metadata;
import za.PrimitiveCompanionObjects;
import za.k;

/* compiled from: TaskLogger.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\u001a \u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002\u001a\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\b¨\u0006\u000b"}, d2 = {"Lde/a;", "task", "Lde/d;", "queue", "", "message", "Lma/f0;", "c", "", "ns", "b", "okhttp"}, k = 2, mv = {1, 6, 0})
/* renamed from: de.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class TaskLogger {
    public static final /* synthetic */ void a(a aVar, d dVar, String str) {
        c(aVar, dVar, str);
    }

    public static final String b(long j10) {
        String str;
        if (j10 <= -999500000) {
            str = ((j10 - 500000000) / 1000000000) + " s ";
        } else if (j10 <= -999500) {
            str = ((j10 - 500000) / 1000000) + " ms";
        } else if (j10 <= 0) {
            str = ((j10 - 500) / 1000) + " µs";
        } else if (j10 < 999500) {
            str = ((j10 + 500) / 1000) + " µs";
        } else if (j10 < 999500000) {
            str = ((j10 + 500000) / 1000000) + " ms";
        } else {
            str = ((j10 + 500000000) / 1000000000) + " s ";
        }
        PrimitiveCompanionObjects primitiveCompanionObjects = PrimitiveCompanionObjects.f20358a;
        String format = String.format("%6s", Arrays.copyOf(new Object[]{str}, 1));
        k.d(format, "format(format, *args)");
        return format;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void c(a aVar, d dVar, String str) {
        Logger a10 = TaskRunner.f10943h.a();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(dVar.getF10938b());
        sb2.append(' ');
        PrimitiveCompanionObjects primitiveCompanionObjects = PrimitiveCompanionObjects.f20358a;
        String format = String.format("%-22s", Arrays.copyOf(new Object[]{str}, 1));
        k.d(format, "format(format, *args)");
        sb2.append(format);
        sb2.append(": ");
        sb2.append(aVar.getF10930a());
        a10.fine(sb2.toString());
    }
}
