package ea;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import b6.LocalLog;
import com.oplus.athena.interaction.PackageStateInfo;
import com.oplus.deepthinker.IOplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import ea.FrameworksProxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/* compiled from: StateManager.java */
/* renamed from: ea.m, reason: use source file name */
/* loaded from: classes2.dex */
public class StateManager {

    /* renamed from: e, reason: collision with root package name */
    private static volatile StateManager f11026e;

    /* renamed from: f, reason: collision with root package name */
    private static final Map<String, Integer> f11027f = new ArrayMap();

    /* renamed from: a, reason: collision with root package name */
    private Context f11028a;

    /* renamed from: b, reason: collision with root package name */
    private Map<Integer, Boolean> f11029b;

    /* renamed from: c, reason: collision with root package name */
    private RegisterStateStrategy f11030c;

    /* renamed from: d, reason: collision with root package name */
    private IOplusDeepThinkerManager f11031d = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StateManager.java */
    /* renamed from: ea.m$a */
    /* loaded from: classes2.dex */
    public class a implements FrameworksProxy.b {
        a() {
        }

        @Override // ea.FrameworksProxy.b
        public void onSysStateChanged(Bundle bundle) {
            if (bundle == null) {
                LocalLog.a("StateManager", "data null");
                return;
            }
            int i10 = bundle.getInt("stateValue", 0);
            String string = bundle.getString("packageName", "");
            int i11 = bundle.getInt(TriggerEvent.EXTRA_UID, -1);
            int i12 = bundle.getInt("open", -1);
            if (LocalLog.f()) {
                LocalLog.a("StateManager", "stateValue:" + i10 + ", packageName:" + string + ", uid:" + i11 + ", state:" + i12);
            }
            StateManager.this.n(i10, string, i11, i12);
            StateManager.this.f11030c.e(i10, string, i11, i12);
        }
    }

    private StateManager(Context context) {
        this.f11028a = context;
        this.f11030c = new RegisterStateStrategy(context);
        k();
    }

    public static int e(Context context, String str) {
        int i10 = -1;
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        Map<String, Integer> map = f11027f;
        synchronized (map) {
            if (map.containsKey(str)) {
                return map.get(str).intValue();
            }
            try {
                i10 = DeepThinkerProxy.j(context).i().getAppType(str);
            } catch (Exception unused) {
            }
            if (i10 > 0) {
                f11027f.put(str, Integer.valueOf(i10));
            }
            return i10;
        }
    }

    public static StateManager f(Context context) {
        if (f11026e == null) {
            synchronized (StateManager.class) {
                if (f11026e == null) {
                    f11026e = new StateManager(context);
                }
            }
        }
        return f11026e;
    }

    private void k() {
        LocalLog.a("StateManager", "init state manager");
        FrameworksProxy.f().m(new a());
        this.f11029b = FrameworksProxy.f().g();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ PackageStateInfo l(PackageRecord packageRecord) {
        return new PackageStateInfo(packageRecord.e(), packageRecord.f(), packageRecord.d());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer m(j jVar) {
        return Integer.valueOf(jVar.b());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n(int i10, String str, int i11, int i12) {
    }

    public List<PackageStateInfo> g(int i10) {
        return i(i10);
    }

    public List<PackageStateInfo> h(int i10) {
        return i(i10);
    }

    public List<PackageStateInfo> i(int i10) {
        return (List) this.f11030c.b(i10).stream().map(new Function() { // from class: ea.k
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                PackageStateInfo l10;
                l10 = StateManager.l((PackageRecord) obj);
                return l10;
            }
        }).collect(Collectors.toList());
    }

    public List<Integer> j(String str, int i10) {
        if (TextUtils.isEmpty(str) || i10 < 0) {
            return null;
        }
        return new ArrayList((Set) this.f11030c.c(str, i10).stream().map(new Function() { // from class: ea.l
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer m10;
                m10 = StateManager.m((j) obj);
                return m10;
            }
        }).collect(Collectors.toSet()));
    }
}
