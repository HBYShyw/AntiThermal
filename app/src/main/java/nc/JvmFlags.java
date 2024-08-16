package nc;

import lc.Flags;

/* compiled from: JvmFlags.kt */
/* renamed from: nc.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class JvmFlags {

    /* renamed from: a, reason: collision with root package name */
    public static final JvmFlags f15985a = new JvmFlags();

    /* renamed from: b, reason: collision with root package name */
    private static final Flags.b f15986b = Flags.d.c();

    /* renamed from: c, reason: collision with root package name */
    private static final Flags.b f15987c;

    /* renamed from: d, reason: collision with root package name */
    private static final Flags.b f15988d;

    static {
        Flags.b c10 = Flags.d.c();
        f15987c = c10;
        f15988d = Flags.d.b(c10);
    }

    private JvmFlags() {
    }

    public final Flags.b a() {
        return f15986b;
    }
}
