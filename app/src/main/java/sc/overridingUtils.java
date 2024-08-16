package sc;

import a.ContextAwareHelper;
import java.util.Collection;
import java.util.LinkedList;
import kotlin.collections._Collections;
import ma.Unit;
import pb.CallableDescriptor;
import qd.SmartSet;
import za.Lambda;

/* compiled from: overridingUtils.kt */
/* renamed from: sc.m, reason: use source file name */
/* loaded from: classes2.dex */
public final class overridingUtils {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [H] */
    /* compiled from: overridingUtils.kt */
    /* renamed from: sc.m$a */
    /* loaded from: classes2.dex */
    public static final class a<H> extends Lambda implements ya.l<H, Unit> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ SmartSet<H> f18466e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(SmartSet<H> smartSet) {
            super(1);
            this.f18466e = smartSet;
        }

        public final void a(H h10) {
            SmartSet<H> smartSet = this.f18466e;
            za.k.d(h10, "it");
            smartSet.add(h10);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(Object obj) {
            a(obj);
            return Unit.f15173a;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <H> Collection<H> a(Collection<? extends H> collection, ya.l<? super H, ? extends CallableDescriptor> lVar) {
        Object T;
        Object p02;
        za.k.e(collection, "<this>");
        za.k.e(lVar, "descriptorByHandle");
        if (collection.size() <= 1) {
            return collection;
        }
        LinkedList linkedList = new LinkedList(collection);
        SmartSet a10 = SmartSet.f17432g.a();
        while (!linkedList.isEmpty()) {
            T = _Collections.T(linkedList);
            SmartSet a11 = SmartSet.f17432g.a();
            Collection<ContextAwareHelper> p10 = OverridingUtil.p(T, linkedList, lVar, new a(a11));
            za.k.d(p10, "conflictedHandles = Smar…nflictedHandles.add(it) }");
            if (p10.size() == 1 && a11.isEmpty()) {
                p02 = _Collections.p0(p10);
                za.k.d(p02, "overridableGroup.single()");
                a10.add(p02);
            } else {
                ContextAwareHelper contextAwareHelper = (Object) OverridingUtil.L(p10, lVar);
                za.k.d(contextAwareHelper, "selectMostSpecificMember…roup, descriptorByHandle)");
                CallableDescriptor invoke = lVar.invoke(contextAwareHelper);
                for (ContextAwareHelper contextAwareHelper2 : p10) {
                    za.k.d(contextAwareHelper2, "it");
                    if (!OverridingUtil.B(invoke, lVar.invoke(contextAwareHelper2))) {
                        a11.add(contextAwareHelper2);
                    }
                }
                if (!a11.isEmpty()) {
                    a10.addAll(a11);
                }
                a10.add(contextAwareHelper);
            }
        }
        return a10;
    }
}
