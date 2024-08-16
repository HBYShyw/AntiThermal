package com.coui.responsiveui.config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.coui.responsiveui.config.UIConfig;
import com.support.responsive.R$integer;
import java.util.HashMap;
import java.util.LinkedHashMap;

/* loaded from: classes.dex */
public class ResponsiveUIConfig {

    /* renamed from: j, reason: collision with root package name */
    private static ResponsiveUIConfig f8228j = null;

    /* renamed from: k, reason: collision with root package name */
    private static boolean f8229k = false;

    /* renamed from: l, reason: collision with root package name */
    private static HashMap<Integer, ResponsiveUIConfig> f8230l = new LinkedHashMap();

    /* renamed from: g, reason: collision with root package name */
    private int f8237g;

    /* renamed from: h, reason: collision with root package name */
    private Context f8238h;

    /* renamed from: a, reason: collision with root package name */
    private int f8231a = -1;

    /* renamed from: b, reason: collision with root package name */
    private MutableLiveData<UIConfig> f8232b = new MutableLiveData<>();

    /* renamed from: c, reason: collision with root package name */
    private MutableLiveData<UIConfig.Status> f8233c = new MutableLiveData<>();

    /* renamed from: d, reason: collision with root package name */
    private MutableLiveData<Integer> f8234d = new MutableLiveData<>();

    /* renamed from: e, reason: collision with root package name */
    private MutableLiveData<UIScreenSize> f8235e = new MutableLiveData<>();

    /* renamed from: f, reason: collision with root package name */
    private MutableLiveData<Integer> f8236f = new MutableLiveData<>();

    /* renamed from: i, reason: collision with root package name */
    private UIConfig.WindowType f8239i = UIConfig.WindowType.SMALL;

    /* loaded from: classes.dex */
    static class LifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        LifecycleCallbacks() {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPostDestroyed(Activity activity) {
            int hashCode = activity.hashCode();
            if (ResponsiveUIConfig.f8230l.containsKey(Integer.valueOf(hashCode))) {
                ResponsiveUIConfig.f8230l.remove(Integer.valueOf(hashCode));
                Log.v("ResponsiveUIConfig", "newInstance remove the kept instance " + hashCode + ", size " + ResponsiveUIConfig.f8230l.size());
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
        }
    }

    private ResponsiveUIConfig(Context context) {
        g(context);
    }

    private int b(int i10) {
        int integer = this.f8238h.getResources().getInteger(R$integer.inner_responsive_ui_column_4);
        int integer2 = this.f8238h.getResources().getInteger(R$integer.inner_responsive_ui_column_8);
        int integer3 = this.f8238h.getResources().getInteger(R$integer.inner_responsive_ui_column_12);
        int i11 = integer / 2;
        return i10 < integer2 - i11 ? integer : (i10 >= integer2 && i10 >= integer3 - i11) ? integer3 : integer2;
    }

    private void c(Resources resources) {
        this.f8237g = resources.getInteger(R$integer.inner_responsive_ui_column_4);
    }

    private void d(Resources resources) {
        Integer e10 = this.f8236f.e();
        int integer = resources.getInteger(R$integer.responsive_ui_column_count);
        float widthDp = this.f8235e.e().getWidthDp() / f();
        if (widthDp > 1.0f) {
            widthDp = 1.0f;
        }
        int b10 = b((int) (integer * widthDp));
        if (e10 == null || e10.intValue() != b10) {
            this.f8236f.m(Integer.valueOf(b10));
        }
    }

    private UIConfig.Status e(int i10, UIScreenSize uIScreenSize) {
        UIConfig.Status status = UIConfig.Status.UNKNOWN;
        int widthDp = uIScreenSize.getWidthDp();
        int heightDp = uIScreenSize.getHeightDp();
        if (widthDp < 600) {
            this.f8239i = UIConfig.WindowType.SMALL;
        } else if (widthDp < 840) {
            this.f8239i = UIConfig.WindowType.MEDIUM;
        } else {
            this.f8239i = UIConfig.WindowType.LARGE;
        }
        if (i10 == 1) {
            if (widthDp >= 600) {
                return UIConfig.Status.UNFOLD;
            }
            return UIConfig.Status.FOLD;
        }
        if (i10 != 2) {
            Log.d("ResponsiveUIConfig", "undefined orientation Status unknown !!! ");
            return status;
        }
        if (heightDp >= 500) {
            return UIConfig.Status.UNFOLD;
        }
        return UIConfig.Status.FOLD;
    }

    private int f() {
        return this.f8238h.getResources().getConfiguration().screenWidthDp;
    }

    private void g(Context context) {
        this.f8231a = context.hashCode();
        Context applicationContext = context.getApplicationContext();
        this.f8238h = applicationContext;
        c(applicationContext.getResources());
        h(context.getResources().getConfiguration());
        d(context.getResources());
        Log.d("ResponsiveUIConfig", "init uiConfig " + this.f8232b.e() + ", columns count " + this.f8236f.e());
        Log.d("ResponsiveUIConfig", "init addContent [" + getExtendHierarchyParentWidthDp() + ":" + getExtendHierarchyChildWidthDp() + "] - [" + getExtendHierarchyParentColumnsCount() + ":" + getExtendHierarchyChildColumnsCount() + "]");
    }

    public static ResponsiveUIConfig getDefault(Context context) {
        if (f8228j == null) {
            f8228j = new ResponsiveUIConfig(context);
        }
        int hashCode = context.hashCode();
        if (hashCode != f8228j.f8231a) {
            Log.d("ResponsiveUIConfig", "getDefault context hash change from " + f8228j.f8231a + " to " + hashCode);
            f8228j.g(context);
        }
        return f8228j;
    }

    private boolean h(Configuration configuration) {
        int i10 = configuration.orientation;
        UIScreenSize uIScreenSize = new UIScreenSize(configuration.screenWidthDp, configuration.screenHeightDp, configuration.smallestScreenWidthDp);
        UIConfig uIConfig = new UIConfig(e(i10, uIScreenSize), uIScreenSize, i10, this.f8239i);
        UIConfig e10 = this.f8232b.e();
        boolean z10 = false;
        if (uIConfig.equals(e10)) {
            return false;
        }
        if (e10 == null || uIConfig.getStatus() != e10.getStatus()) {
            this.f8233c.m(uIConfig.getStatus());
        }
        if (e10 == null || uIConfig.getOrientation() != e10.getOrientation()) {
            this.f8234d.m(Integer.valueOf(uIConfig.getOrientation()));
            z10 = true;
        }
        if (e10 == null || !uIConfig.getScreenSize().equals(e10.getScreenSize())) {
            int widthDp = uIConfig.getScreenSize().getWidthDp();
            int f10 = f();
            if (Math.abs(widthDp - f10) < 50) {
                this.f8235e.m(uIConfig.getScreenSize());
            } else {
                Log.d("ResponsiveUIConfig", "update ScreenSize few case newWidth " + widthDp + " appWidth " + f10);
                UIScreenSize e11 = this.f8235e.e();
                if (e11 != null) {
                    if (z10) {
                        widthDp = e11.getHeightDp();
                    } else {
                        widthDp = e11.getWidthDp();
                    }
                }
                UIScreenSize uIScreenSize2 = new UIScreenSize(widthDp, uIConfig.getScreenSize().getHeightDp(), uIConfig.getScreenSize().a());
                this.f8235e.m(uIScreenSize2);
                uIConfig.b(e(this.f8234d.e().intValue(), uIScreenSize2));
                uIConfig.c(this.f8239i);
            }
            uIConfig.a(this.f8235e.e());
        }
        this.f8232b.m(uIConfig);
        return true;
    }

    public static ResponsiveUIConfig newInstance(Context context) {
        if (!f8229k && (context.getApplicationContext() instanceof Application)) {
            ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new LifecycleCallbacks());
            f8229k = true;
        }
        int hashCode = context.hashCode();
        if (f8230l.containsKey(Integer.valueOf(hashCode))) {
            Log.v("ResponsiveUIConfig", "newInstance return the kept instance " + hashCode);
            return f8230l.get(Integer.valueOf(hashCode));
        }
        ResponsiveUIConfig responsiveUIConfig = new ResponsiveUIConfig(context);
        f8230l.put(Integer.valueOf(hashCode), responsiveUIConfig);
        Log.v("ResponsiveUIConfig", "newInstance return the new instance " + hashCode + ", size " + f8230l.size());
        return responsiveUIConfig;
    }

    public void flush(Context context) {
        g(context);
    }

    public int getExtendHierarchyChildColumnsCount() {
        return this.f8236f.e().intValue() - getExtendHierarchyParentColumnsCount();
    }

    public int getExtendHierarchyChildWidthDp() {
        return this.f8235e.e().getWidthDp() - getExtendHierarchyParentWidthDp();
    }

    public int getExtendHierarchyParentColumnsCount() {
        return b((int) (this.f8236f.e().intValue() * (getExtendHierarchyParentWidthDp() / this.f8235e.e().getWidthDp())));
    }

    public int getExtendHierarchyParentWidthDp() {
        if (this.f8235e.e().getWidthDp() >= 840) {
            return this.f8238h.getResources().getInteger(R$integer.inner_responsive_ui_extend_hierarchy_parent_width_360);
        }
        if (this.f8235e.e().getWidthDp() >= 600) {
            return this.f8238h.getResources().getInteger(R$integer.inner_responsive_ui_extend_hierarchy_parent_width_300);
        }
        return this.f8235e.e().getWidthDp();
    }

    public UIConfig.WindowType getScreenType() {
        return this.f8232b.e().getWindowType();
    }

    public LiveData<Integer> getUiColumnsCount() {
        return this.f8236f;
    }

    public LiveData<UIConfig> getUiConfig() {
        return this.f8232b;
    }

    public LiveData<Integer> getUiOrientation() {
        return this.f8234d;
    }

    public LiveData<UIScreenSize> getUiScreenSize() {
        return this.f8235e;
    }

    public LiveData<UIConfig.Status> getUiStatus() {
        return this.f8233c;
    }

    public void onActivityConfigChanged(Configuration configuration) {
        if (h(configuration)) {
            d(this.f8238h.getResources());
            Log.d("ResponsiveUIConfig", "onUIConfigChanged uiConfig " + this.f8232b.e() + ", columns count " + this.f8236f.e());
            Log.d("ResponsiveUIConfig", "onUIConfigChanged addContent [" + getExtendHierarchyParentWidthDp() + ":" + getExtendHierarchyChildWidthDp() + "] - [" + getExtendHierarchyParentColumnsCount() + ":" + getExtendHierarchyChildColumnsCount() + "]");
        }
    }

    public int spanCountBaseColumns(int i10, int i11) {
        return (this.f8236f.e().intValue() / i10) * i11;
    }

    public int spanCountBaseWidth(int i10) {
        return spanCountBaseWidth(360, i10);
    }

    public int spanCountBaseColumns(int i10) {
        return spanCountBaseColumns(this.f8237g, i10);
    }

    public int spanCountBaseWidth(int i10, int i11) {
        return (getUiScreenSize().e().getWidthDp() >= 600 || i10 >= 600) ? (int) ((this.f8235e.e().getWidthDp() / i10) * Math.max(i11, 1)) : i11;
    }
}
