package w;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.h0;
import kotlin.Metadata;
import za.k;

/* compiled from: InitializerViewModelFactory.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u00002\u00020\u0001B#\u0012\u001a\u0010\f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u000b0\n\"\u0006\u0012\u0002\b\u00030\u000b¢\u0006\u0004\b\r\u0010\u000eJ/\u0010\b\u001a\u00028\u0000\"\b\b\u0000\u0010\u0003*\u00020\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\b\u0010\t¨\u0006\u000f"}, d2 = {"Lw/b;", "Landroidx/lifecycle/h0$b;", "Landroidx/lifecycle/g0;", "T", "Ljava/lang/Class;", "modelClass", "Lw/a;", "extras", "b", "(Ljava/lang/Class;Lw/a;)Landroidx/lifecycle/g0;", "", "Lw/e;", "initializers", "<init>", "([Lw/e;)V", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class b implements h0.b {

    /* renamed from: b, reason: collision with root package name */
    private final e<?>[] f19306b;

    public b(e<?>... eVarArr) {
        k.e(eVarArr, "initializers");
        this.f19306b = eVarArr;
    }

    @Override // androidx.lifecycle.h0.b
    public <T extends ViewModel> T b(Class<T> modelClass, a extras) {
        k.e(modelClass, "modelClass");
        k.e(extras, "extras");
        T t7 = null;
        for (e<?> eVar : this.f19306b) {
            if (k.a(eVar.a(), modelClass)) {
                Object invoke = eVar.b().invoke(extras);
                t7 = invoke instanceof ViewModel ? (T) invoke : null;
            }
        }
        if (t7 != null) {
            return t7;
        }
        throw new IllegalArgumentException("No initializer set for given class " + modelClass.getName());
    }
}
