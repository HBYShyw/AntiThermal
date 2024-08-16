package x9;

import android.app.OplusWhiteListManager;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;
import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import h6.AppFeatureProviderUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: WhiteListUtils.java */
/* renamed from: x9.d, reason: use source file name */
/* loaded from: classes2.dex */
public class WhiteListUtils {

    /* renamed from: d, reason: collision with root package name */
    private static WhiteListUtils f19655d;

    /* renamed from: e, reason: collision with root package name */
    private static ArrayList<String> f19656e = new ArrayList<>();

    /* renamed from: f, reason: collision with root package name */
    private static ArrayList<String> f19657f = new ArrayList<>();

    /* renamed from: g, reason: collision with root package name */
    private static ArrayList<String> f19658g = new ArrayList<>();

    /* renamed from: h, reason: collision with root package name */
    private static List<String> f19659h = null;

    /* renamed from: i, reason: collision with root package name */
    private static ContentObserver f19660i = null;

    /* renamed from: a, reason: collision with root package name */
    private List<String> f19661a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    private List<String> f19662b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private Context f19663c;

    /* compiled from: WhiteListUtils.java */
    /* renamed from: x9.d$a */
    /* loaded from: classes2.dex */
    class a extends ContentObserver {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Context f19664a;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(Handler handler, Context context) {
            super(handler);
            this.f19664a = context;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            if (AppFeatureUtils.d(this.f19664a) || AppFeatureUtils.e(this.f19664a)) {
                Log.d("WhiteListUtils", "updateCustomAutoStartList");
                WhiteListUtils.o(this.f19664a);
            }
            if (AppFeatureUtils.c(this.f19664a)) {
                Log.d("WhiteListUtils", "updateCustomAssociateList");
                WhiteListUtils.n(this.f19664a);
            }
        }
    }

    private WhiteListUtils(Context context) {
        this.f19663c = context;
    }

    public static List<String> a(Context context) {
        ArrayList<String> arrayList;
        synchronized (f19658g) {
            if (f19658g.isEmpty() && context != null) {
                f19658g.addAll(AppFeatureUtils.g(context));
            }
            arrayList = f19658g;
        }
        return arrayList;
    }

    public static List<String> b(Context context) {
        ArrayList<String> arrayList;
        synchronized (f19657f) {
            if (f19657f.isEmpty() && context != null) {
                f19657f.addAll(AppFeatureUtils.i(context));
                f19657f.addAll(AppFeatureUtils.h(context));
            }
            arrayList = f19657f;
        }
        return arrayList;
    }

    public static List<String> c(Context context, boolean z10) {
        return z10 ? b(context) : a(context);
    }

    public static WhiteListUtils d(Context context) {
        if (f19655d == null) {
            f19655d = new WhiteListUtils(context);
        }
        return f19655d;
    }

    private void f() {
        try {
            String[] stringArray = this.f19663c.getResources().getStringArray(this.f19663c.getResources().getIdentifier("push_pkg", ThermalWindowConfigInfo.TAG_ARRAY, "com.oplus.battery"));
            if (stringArray == null) {
                Log.d("WhiteListUtils", "initPushPkgList: failed, null");
                return;
            }
            synchronized (this.f19661a) {
                for (String str : stringArray) {
                    this.f19661a.add(str);
                }
                Log.d("WhiteListUtils", "init push pkg list: " + this.f19661a);
            }
        } catch (Exception e10) {
            e10.printStackTrace();
            Log.e("WhiteListUtils", "initPushPkgList: error");
        }
    }

    private void g() {
        try {
            String[] stringArray = this.f19663c.getResources().getStringArray(this.f19663c.getResources().getIdentifier("self_develop_pre", ThermalWindowConfigInfo.TAG_ARRAY, "com.oplus.battery"));
            if (stringArray == null) {
                Log.d("WhiteListUtils", "initSelfDevelopPre: failed, null");
                return;
            }
            synchronized (this.f19662b) {
                for (String str : stringArray) {
                    this.f19662b.add(str);
                }
                Log.d("WhiteListUtils", "init self develop pre: " + this.f19662b);
            }
        } catch (Exception e10) {
            e10.printStackTrace();
            Log.e("WhiteListUtils", "initSelfDevelopPre: error");
        }
    }

    public static boolean h(Context context, String str) {
        boolean contains;
        synchronized (f19656e) {
            if (f19656e.isEmpty()) {
                f19656e.addAll(AppFeatureUtils.f(context));
            }
            contains = f19656e.contains(str);
        }
        return contains;
    }

    public static boolean i(Context context, String str) {
        if (f19659h == null) {
            f19659h = new OplusWhiteListManager(context).getGlobalWhiteList();
        }
        return f19659h.contains(str);
    }

    public static boolean l(String str) {
        return str.contains("com.android.cts") || "com.oplus.uiengine".equals(str);
    }

    public static void m(Context context, Handler handler) {
        if (context != null && handler != null) {
            if (f19660i == null) {
                f19660i = new a(handler, context);
            }
            Log.d("WhiteListUtils", "registerCustomListObserver");
            AppFeatureProviderUtils.j(context.getContentResolver(), false, f19660i);
            return;
        }
        Log.e("WhiteListUtils", "registerCustomListObserver: invalid arguments");
    }

    public static void n(Context context) {
        synchronized (f19658g) {
            if (context != null) {
                if (!f19658g.isEmpty()) {
                    f19658g.clear();
                }
                f19658g.addAll(AppFeatureUtils.g(context));
            }
        }
    }

    public static void o(Context context) {
        synchronized (f19657f) {
            if (context != null) {
                if (!f19657f.isEmpty()) {
                    f19657f.clear();
                }
                f19657f.addAll(AppFeatureUtils.i(context));
                f19657f.addAll(AppFeatureUtils.h(context));
            }
        }
    }

    public void e() {
        g();
        f();
    }

    public boolean j(String str) {
        synchronized (this.f19661a) {
            return this.f19661a.contains(str);
        }
    }

    public boolean k(String str) {
        synchronized (this.f19662b) {
            Iterator<String> it = this.f19662b.iterator();
            while (it.hasNext()) {
                if (str.startsWith(it.next())) {
                    return true;
                }
            }
            return false;
        }
    }
}
