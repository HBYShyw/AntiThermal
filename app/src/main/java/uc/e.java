package uc;

import gd.o0;
import java.util.Arrays;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class e extends o<Character> {
    public e(char c10) {
        super(Character.valueOf(c10));
    }

    private final String c(char c10) {
        return c10 == '\b' ? "\\b" : c10 == '\t' ? "\\t" : c10 == '\n' ? "\\n" : c10 == '\f' ? "\\f" : c10 == '\r' ? "\\r" : e(c10) ? String.valueOf(c10) : "?";
    }

    private final boolean e(char c10) {
        byte type = (byte) Character.getType(c10);
        return (type == 0 || type == 13 || type == 14 || type == 15 || type == 16 || type == 18 || type == 19) ? false : true;
    }

    @Override // uc.g
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public o0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        o0 u7 = moduleDescriptor.t().u();
        za.k.d(u7, "module.builtIns.charType");
        return u7;
    }

    @Override // uc.g
    public String toString() {
        String format = String.format("\\u%04X ('%s')", Arrays.copyOf(new Object[]{Integer.valueOf(b().charValue()), c(b().charValue())}, 2));
        za.k.d(format, "format(this, *args)");
        return format;
    }
}
