package com.coui.appcompat.theme;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.ImageView;
import b2.COUILog;
import b3.COUITintUtil;
import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import com.support.appcompat.R$array;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$id;
import com.support.appcompat.R$style;
import j3.COUIVersionUtil;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import oplus.content.res.OplusExtraConfiguration;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* compiled from: COUIThemeOverlay.java */
/* renamed from: com.coui.appcompat.theme.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIThemeOverlay {

    /* renamed from: c, reason: collision with root package name */
    private static String f7909c;

    /* renamed from: d, reason: collision with root package name */
    private static int f7910d;

    /* renamed from: e, reason: collision with root package name */
    private static boolean f7911e;

    /* renamed from: f, reason: collision with root package name */
    private static boolean f7912f;

    /* renamed from: g, reason: collision with root package name */
    private static boolean f7913g;

    /* renamed from: a, reason: collision with root package name */
    private SparseIntArray f7914a = new SparseIntArray();

    /* renamed from: b, reason: collision with root package name */
    private HashMap<String, WeakReference<Boolean>> f7915b = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIThemeOverlay.java */
    /* renamed from: com.coui.appcompat.theme.a$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private static final COUIThemeOverlay f7916a = new COUIThemeOverlay();
    }

    static {
        f7909c = d() ? "com.oplus.inner.content.res.ConfigurationWrapper" : j3.a.c().b();
        f7911e = p();
        f7913g = r();
        f7912f = q() && COUIVersionUtil.b() > 0;
        f7910d = g();
    }

    private boolean c() {
        try {
            Class.forName("android.content.res.OplusBaseConfiguration");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean d() {
        try {
            Class.forName("com.oplus.inner.content.res.ConfigurationWrapper");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private static int g() {
        int i10 = 0;
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            String str = (String) method.invoke(null, "ro.oplus.theme.version");
            int parseInt = !TextUtils.isEmpty(str) ? Integer.parseInt(str.trim()) : 0;
            if (parseInt != 0) {
                return parseInt;
            }
            try {
                String str2 = (String) method.invoke(null, j3.a.c().f());
                return !TextUtils.isEmpty(str2) ? Integer.parseInt(str2.trim()) : parseInt;
            } catch (Exception e10) {
                e = e10;
                i10 = parseInt;
                COUILog.b("COUIThemeOverlay", "getCompatVersion e: " + e);
                return i10;
            }
        } catch (Exception e11) {
            e = e11;
        }
    }

    private OplusExtraConfiguration h(Configuration configuration) {
        OplusBaseConfiguration oplusBaseConfiguration = (OplusBaseConfiguration) u(OplusBaseConfiguration.class, configuration);
        if (oplusBaseConfiguration == null) {
            return null;
        }
        return oplusBaseConfiguration.mOplusExtraConfiguration;
    }

    public static COUIThemeOverlay i() {
        return a.f7916a;
    }

    private int j(Context context, String str, String str2) {
        if (context.getResources() == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(context.getPackageName())) {
            return 0;
        }
        return context.getResources().getIdentifier(str, str2, context.getPackageName());
    }

    private int k(Context context, int i10, int i11) {
        if (i10 > 0 && context.getResources() != null) {
            Resources resources = context.getResources();
            int i12 = f7910d;
            if (i12 > 12000) {
                TypedArray obtainTypedArray = resources.obtainTypedArray(R$array.coui_theme_arrays_ids);
                r0 = obtainTypedArray.length() >= i10 ? obtainTypedArray.getResourceId(i10 - 1, 0) : 0;
                obtainTypedArray.recycle();
            } else if (i12 == 12000) {
                int j10 = j(context, f7913g ? "coui_theme_arrays_ids_patch_r" : "coui_theme_arrays_ids_patch_o", ThermalWindowConfigInfo.TAG_ARRAY);
                if (f7911e && i11 == 1048576) {
                    j10 = R$array.coui_theme_arrays_ids;
                }
                if (j10 != 0) {
                    TypedArray obtainTypedArray2 = resources.obtainTypedArray(j10);
                    r0 = obtainTypedArray2.length() >= i10 ? obtainTypedArray2.getResourceId(i10 - 1, 0) : 0;
                    obtainTypedArray2.recycle();
                }
            } else {
                int j11 = j(context, f7913g ? "coui_theme_arrays_ids_repatch_r" : "coui_theme_arrays_ids_repatch_o", ThermalWindowConfigInfo.TAG_ARRAY);
                if (j11 != 0) {
                    TypedArray obtainTypedArray3 = resources.obtainTypedArray(j11);
                    r0 = obtainTypedArray3.length() >= i10 ? obtainTypedArray3.getResourceId(i10 - 1, 0) : 0;
                    obtainTypedArray3.recycle();
                }
            }
        }
        return r0;
    }

    private boolean l(Context context) {
        String packageName = context.getPackageName();
        File file = new File("my_company/media/theme/");
        if (!file.exists() || TextUtils.isEmpty(packageName)) {
            return false;
        }
        if (new File(file, packageName).exists()) {
            return true;
        }
        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return false;
        }
        String string = Settings.System.getString(context.getContentResolver(), "custom_theme_path_setting");
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        return new File(string, packageName).exists();
    }

    private boolean m(Context context) {
        String packageName = context.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        OplusExtraConfiguration h10 = h(context.getResources().getConfiguration());
        int i10 = h10 != null ? h10.mUserId : 0;
        String str = "data/theme/";
        if (i10 > 0) {
            str = "data/theme/" + i10;
        }
        return new File(str, packageName).exists();
    }

    private static boolean p() {
        String str = Build.MANUFACTURER;
        return str.equals(String.valueOf(new char[]{'O', 'P', 'P', 'O'})) || str.equals(String.valueOf(new char[]{'O', 'p', 'p', 'o'}));
    }

    private static boolean q() {
        String str = Build.MANUFACTURER;
        return str.equals(String.valueOf(new char[]{'O', 'n', 'e', 'P', 'l', 'u', 's'})) || str.equals(String.valueOf(new char[]{'O', 'N', 'E', 'P', 'L', 'U', 'S'})) || str.equals(String.valueOf(new char[]{'G', 'A', 'L', 'I', 'L', 'E', 'I'})) || str.equals(String.valueOf(new char[]{'g', 'a', 'l', 'i', 'l', 'e', 'i'})) || str.equals(String.valueOf(new char[]{'F', 'A', 'R', 'A', 'D', 'A', 'Y'})) || str.equals(String.valueOf(new char[]{'f', 'a', 'r', 'a', 'd', 'a', 'y'}));
    }

    private static boolean r() {
        String str = Build.MANUFACTURER;
        return str.equals(String.valueOf(new char[]{'R', 'E', 'A', 'L', 'M', 'E'})) || str.equals(String.valueOf(new char[]{'R', 'e', 'a', 'l', 'm', 'e'})) || str.equals(String.valueOf(new char[]{'r', 'e', 'a', 'l', 'm', 'e'}));
    }

    private void s(Context context) {
        int k10;
        int i10;
        if (context == null || o(context)) {
            return;
        }
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R$attr.couiThemeIdentifier});
        int integer = obtainStyledAttributes.getInteger(0, 0);
        obtainStyledAttributes.recycle();
        long f10 = f(context.getResources().getConfiguration());
        int i11 = (int) (65535 & f10);
        int i12 = (int) (16711680 & f10);
        boolean z10 = f7910d < 12000;
        if (f10 != 0) {
            if (i11 == 0 && i12 == 0) {
                return;
            }
            if (i12 == 131072) {
                t(R$id.coui_global_theme, R$style.COUIOverlay_Theme_Single_First);
                return;
            }
            if (i12 != 65536) {
                if (i12 == 262144) {
                    k10 = R$array.coui_theme_arrays_default_patch;
                } else if (i12 == 0 || i12 == 1048576) {
                    k10 = k(context, i11, i12);
                } else {
                    i10 = 0;
                    i11 = -1;
                }
                int i13 = k10;
                i11 = integer - 1;
                i10 = i13;
            } else if (f7912f) {
                i10 = j(context, z10 ? "coui_theme_arrays_single_repatch_p" : "coui_theme_arrays_single_patch_p", ThermalWindowConfigInfo.TAG_ARRAY);
            } else {
                i10 = R$array.coui_theme_arrays_single;
            }
            if (i10 == 0 || i11 == -1) {
                return;
            }
            TypedArray obtainTypedArray = context.getResources().obtainTypedArray(i10);
            if (obtainTypedArray.length() > i11) {
                t(R$id.coui_global_theme, obtainTypedArray.getResourceId(i11, 0));
            }
            obtainTypedArray.recycle();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T u(Class<T> cls, Object obj) {
        if (obj == 0 || !cls.isInstance(obj)) {
            return null;
        }
        return obj;
    }

    public void a(Context context, ImageView imageView, boolean z10) {
        Drawable drawable;
        if (imageView == null || o(context)) {
            return;
        }
        if ((i().n(context) || z10) && (drawable = imageView.getDrawable()) != null) {
            if (drawable instanceof LayerDrawable) {
                COUITintUtil.b(((LayerDrawable) drawable).getDrawable(0), COUIContextUtil.a(context, R$attr.couiColorPrimaryText));
            } else {
                COUITintUtil.b(drawable, COUIContextUtil.a(context, R$attr.couiColorPrimaryText));
            }
            COUIDarkModeUtil.b(imageView, false);
            imageView.setImageDrawable(drawable);
        }
    }

    public void b(Context context) {
        e();
        s(context);
        for (int i10 = 0; i10 < this.f7914a.size(); i10++) {
            context.setTheme(this.f7914a.valueAt(i10));
        }
    }

    public void e() {
        this.f7914a.clear();
    }

    public long f(Configuration configuration) {
        if (!c()) {
            return 0L;
        }
        OplusExtraConfiguration h10 = h(configuration);
        if (h10 != null) {
            return h10.mMaterialColor;
        }
        try {
            Class<?> cls = Class.forName(f7909c);
            if (cls.newInstance() != null) {
                return ((Long) cls.getMethod("getMaterialColor", Configuration.class).invoke(null, configuration)).longValue();
            }
            return 0L;
        } catch (Exception e10) {
            COUILog.b("COUIThemeOverlay", "getCOUITheme e: " + e10);
            return 0L;
        }
    }

    public boolean n(Context context) {
        long f10 = f(context.getResources().getConfiguration());
        return f10 > 0 && (f10 & 2147483647L) != 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x004b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean o(Context context) {
        OplusExtraConfiguration oplusExtraConfiguration;
        long j10;
        boolean z10;
        Configuration configuration = context.getResources().getConfiguration();
        if (configuration == null || !c()) {
            return false;
        }
        try {
            oplusExtraConfiguration = h(context.getResources().getConfiguration());
            try {
            } catch (Exception e10) {
                e = e10;
                Log.d("COUIThemeOverlay", "get extra config failed : " + e.getMessage());
                j10 = 0;
                if (oplusExtraConfiguration == null) {
                }
                if ((1 & j10) == 0) {
                }
                if (z10) {
                }
            }
        } catch (Exception e11) {
            e = e11;
            oplusExtraConfiguration = null;
        }
        if (oplusExtraConfiguration instanceof OplusExtraConfiguration) {
            j10 = oplusExtraConfiguration.mThemeChangedFlags;
            if (oplusExtraConfiguration == null) {
                try {
                    Class<?> cls = Class.forName(f7909c);
                    if (cls.newInstance() != null) {
                        j10 = ((Long) cls.getMethod("getThemeChangedFlags", Configuration.class).invoke(null, configuration)).longValue();
                    }
                } catch (Exception e12) {
                    COUILog.b("COUIThemeOverlay", "isRejectTheme e: " + e12);
                }
            }
            if ((1 & j10) == 0) {
                z10 = false;
            } else if ((j10 & 256) != 0) {
                z10 = l(context);
            } else {
                z10 = m(context);
            }
            return (z10 || (configuration.uiMode & 48) == 32) ? false : true;
        }
        j10 = 0;
        if (oplusExtraConfiguration == null) {
        }
        if ((1 & j10) == 0) {
        }
        if (z10) {
            return false;
        }
    }

    public void t(int i10, int i11) {
        this.f7914a.put(i10, i11);
    }
}
