package pb;

import za.DefaultConstructorMarker;

/* compiled from: Modality.kt */
/* renamed from: pb.e0, reason: use source file name */
/* loaded from: classes2.dex */
public enum Modality {
    FINAL,
    SEALED,
    OPEN,
    ABSTRACT;


    /* renamed from: e, reason: collision with root package name */
    public static final a f16676e = new a(null);

    /* compiled from: Modality.kt */
    /* renamed from: pb.e0$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Modality a(boolean z10, boolean z11, boolean z12) {
            if (z10) {
                return Modality.SEALED;
            }
            if (z11) {
                return Modality.ABSTRACT;
            }
            if (z12) {
                return Modality.OPEN;
            }
            return Modality.FINAL;
        }
    }
}
