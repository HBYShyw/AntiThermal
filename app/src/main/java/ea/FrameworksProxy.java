package ea;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import ea.FrameworksProxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/* compiled from: FrameworksProxy.java */
/* renamed from: ea.g, reason: use source file name */
/* loaded from: classes2.dex */
public class FrameworksProxy {

    /* renamed from: a, reason: collision with root package name */
    private List<b> f11003a = new ArrayList();

    /* compiled from: FrameworksProxy.java */
    /* renamed from: ea.g$a */
    /* loaded from: classes2.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        private static final FrameworksProxy f11004a = new FrameworksProxy();
    }

    /* compiled from: FrameworksProxy.java */
    /* renamed from: ea.g$b */
    /* loaded from: classes2.dex */
    public interface b {
        void onSysStateChanged(Bundle bundle);
    }

    public static FrameworksProxy f() {
        return a.f11004a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer i(Integer num) {
        return num;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean j(Integer num) {
        return Boolean.valueOf(num.intValue() % 2 == 1);
    }

    public void e(int i10, String str, int i11) {
        final Bundle bundle = new Bundle();
        bundle.putInt("stateValue", i10);
        bundle.putString("packageName", str);
        bundle.putInt(TriggerEvent.EXTRA_UID, i11);
        bundle.putInt("open", 0);
        this.f11003a.forEach(new Consumer() { // from class: ea.c
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FrameworksProxy.b) obj).onSysStateChanged(bundle);
            }
        });
    }

    public Map<Integer, Boolean> g() {
        return (Map) j.a().stream().collect(Collectors.toMap(new Function() { // from class: ea.f
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer i10;
                i10 = FrameworksProxy.i((Integer) obj);
                return i10;
            }
        }, new Function() { // from class: ea.e
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean j10;
                j10 = FrameworksProxy.j((Integer) obj);
                return j10;
            }
        }));
    }

    public void l(int i10, String str, int i11) {
        final Bundle bundle = new Bundle();
        bundle.putInt("stateValue", i10);
        bundle.putString("packageName", str);
        bundle.putInt(TriggerEvent.EXTRA_UID, i11);
        bundle.putInt("open", 1);
        this.f11003a.forEach(new Consumer() { // from class: ea.d
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FrameworksProxy.b) obj).onSysStateChanged(bundle);
            }
        });
    }

    public void m(b bVar) {
        if (bVar != null) {
            this.f11003a.add(bVar);
        }
    }
}
