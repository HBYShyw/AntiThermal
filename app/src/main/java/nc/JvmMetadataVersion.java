package nc;

import java.util.Arrays;
import lc.BinaryVersion;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: JvmMetadataVersion.kt */
/* renamed from: nc.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class JvmMetadataVersion extends BinaryVersion {

    /* renamed from: h, reason: collision with root package name */
    public static final a f15993h = new a(null);

    /* renamed from: i, reason: collision with root package name */
    public static final JvmMetadataVersion f15994i = new JvmMetadataVersion(1, 8, 0);

    /* renamed from: j, reason: collision with root package name */
    public static final JvmMetadataVersion f15995j = new JvmMetadataVersion(new int[0]);

    /* renamed from: g, reason: collision with root package name */
    private final boolean f15996g;

    /* compiled from: JvmMetadataVersion.kt */
    /* renamed from: nc.e$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public JvmMetadataVersion(int[] iArr, boolean z10) {
        super(Arrays.copyOf(iArr, iArr.length));
        k.e(iArr, "versionArray");
        this.f15996g = z10;
    }

    public boolean h() {
        boolean z10;
        if (a() == 1 && b() == 0) {
            return false;
        }
        if (this.f15996g) {
            z10 = f(f15994i);
        } else {
            int a10 = a();
            JvmMetadataVersion jvmMetadataVersion = f15994i;
            z10 = a10 == jvmMetadataVersion.a() && b() <= jvmMetadataVersion.b() + 1;
        }
        return z10;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public JvmMetadataVersion(int... iArr) {
        this(iArr, false);
        k.e(iArr, "numbers");
    }
}
