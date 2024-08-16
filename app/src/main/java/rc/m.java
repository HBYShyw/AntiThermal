package rc;

import sd.StringsJVM;
import za.DefaultConstructorMarker;

/* compiled from: DescriptorRenderer.kt */
/* loaded from: classes2.dex */
public enum m {
    PLAIN { // from class: rc.m.b
        @Override // rc.m
        public String b(String str) {
            za.k.e(str, "string");
            return str;
        }
    },
    HTML { // from class: rc.m.a
        @Override // rc.m
        public String b(String str) {
            String z10;
            String z11;
            za.k.e(str, "string");
            z10 = StringsJVM.z(str, "<", "&lt;", false, 4, null);
            z11 = StringsJVM.z(z10, ">", "&gt;", false, 4, null);
            return z11;
        }
    };

    /* synthetic */ m(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract String b(String str);
}
