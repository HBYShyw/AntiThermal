package cd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gd.g0;
import java.util.List;
import pb.ModuleDescriptor;
import za.Lambda;

/* compiled from: DeserializedArrayValue.kt */
/* renamed from: cd.n, reason: use source file name */
/* loaded from: classes2.dex */
public final class DeserializedArrayValue extends uc.b {

    /* renamed from: c, reason: collision with root package name */
    private final g0 f5273c;

    /* compiled from: DeserializedArrayValue.kt */
    /* renamed from: cd.n$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<ModuleDescriptor, g0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ g0 f5274e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(g0 g0Var) {
            super(1);
            this.f5274e = g0Var;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke(ModuleDescriptor moduleDescriptor) {
            za.k.e(moduleDescriptor, "it");
            return this.f5274e;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeserializedArrayValue(List<? extends uc.g<?>> list, g0 g0Var) {
        super(list, new a(g0Var));
        za.k.e(list, ThermalBaseConfig.Item.ATTR_VALUE);
        za.k.e(g0Var, "type");
        this.f5273c = g0Var;
    }

    public final g0 c() {
        return this.f5273c;
    }
}
