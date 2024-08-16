package com.coui.component.responsiveui;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.ComponentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.o;
import com.coui.component.responsiveui.ResponsiveUIFeature;
import com.coui.component.responsiveui.status.FoldingState;
import com.coui.component.responsiveui.status.FoldingStateUtil;
import com.coui.component.responsiveui.status.WindowFeature;
import com.coui.component.responsiveui.status.WindowFeatureUtil;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ResponsiveUIFeature.kt */
@Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0011\b\u0002\u0012\u0006\u0010\u0014\u001a\u00020\u0013¢\u0006\u0004\b\u0015\u0010\u0016J+\u0010\u0007\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u00022\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u00032\u0006\u0010\u0005\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\u0007\u0010\bJ\u000e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u0003H\u0016R\u001c\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u000b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\r\u0010\u000eR\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\u00038\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0010\u0010\u0011¨\u0006\u0018"}, d2 = {"Lcom/coui/component/responsiveui/ResponsiveUIFeature;", "Lcom/coui/component/responsiveui/IResponsiveUIFeature;", "T", "Landroidx/lifecycle/u;", "liveDataObject", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "c", "(Landroidx/lifecycle/u;Ljava/lang/Object;)V", "Lcom/coui/component/responsiveui/status/WindowFeature;", "getWindowFeatureLiveData", "Ljava/lang/ref/WeakReference;", "Landroid/app/Activity;", "a", "Ljava/lang/ref/WeakReference;", "activityReference", "b", "Landroidx/lifecycle/u;", "windowFeatureLiveData", "Landroidx/activity/ComponentActivity;", "activity", "<init>", "(Landroidx/activity/ComponentActivity;)V", "Companion", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class ResponsiveUIFeature implements IResponsiveUIFeature {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* renamed from: c, reason: collision with root package name */
    private static final ConcurrentHashMap<Integer, IResponsiveUIFeature> f8164c = new ConcurrentHashMap<>();

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private WeakReference<Activity> activityReference;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private MutableLiveData<WindowFeature> windowFeatureLiveData;

    /* compiled from: ResponsiveUIFeature.kt */
    @Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0010\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007J\b\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/coui/component/responsiveui/ResponsiveUIFeature$Companion;", "", "()V", "TAG", "", "featureMap", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lcom/coui/component/responsiveui/IResponsiveUIFeature;", "getFoldingState", "Lcom/coui/component/responsiveui/status/FoldingState;", "context", "Landroid/content/Context;", "getOrCreate", "activity", "Landroidx/activity/ComponentActivity;", "isSupportWindowFeature", "", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final FoldingState getFoldingState(Context context) {
            k.e(context, "context");
            FoldingStateUtil foldingStateUtil = FoldingStateUtil.INSTANCE;
            return FoldingStateUtil.getFoldingState(context);
        }

        public final IResponsiveUIFeature getOrCreate(ComponentActivity activity) {
            k.e(activity, "activity");
            int hashCode = activity.hashCode();
            IResponsiveUIFeature iResponsiveUIFeature = (IResponsiveUIFeature) ResponsiveUIFeature.f8164c.get(Integer.valueOf(hashCode));
            if (iResponsiveUIFeature != null) {
                return iResponsiveUIFeature;
            }
            ResponsiveUIFeature responsiveUIFeature = new ResponsiveUIFeature(activity, null);
            ResponsiveUIFeature.f8164c.put(Integer.valueOf(hashCode), responsiveUIFeature);
            return responsiveUIFeature;
        }

        public final boolean isSupportWindowFeature() {
            WindowFeatureUtil windowFeatureUtil = WindowFeatureUtil.INSTANCE;
            return WindowFeatureUtil.isSupportWindowFeature();
        }
    }

    private ResponsiveUIFeature(ComponentActivity componentActivity) {
        this.activityReference = new WeakReference<>(componentActivity);
        this.windowFeatureLiveData = new MutableLiveData<>();
        WindowFeatureUtil windowFeatureUtil = WindowFeatureUtil.INSTANCE;
        if (WindowFeatureUtil.isSupportWindowFeature()) {
            windowFeatureUtil.trackWindowFeature(componentActivity, new Consumer() { // from class: m3.a
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ResponsiveUIFeature.b(ResponsiveUIFeature.this, (WindowFeature) obj);
                }
            });
        } else {
            Log.d("ResponsiveUIFeature", "[init.isSupportWindowFeature] false");
        }
        componentActivity.getLifecycle().a(new DefaultLifecycleObserver() { // from class: com.coui.component.responsiveui.ResponsiveUIFeature.2
            @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
            public /* bridge */ /* synthetic */ void onCreate(o oVar) {
                super.onCreate(oVar);
            }

            @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
            public void onDestroy(o oVar) {
                k.e(oVar, "owner");
                super.onDestroy(oVar);
                Activity activity = (Activity) ResponsiveUIFeature.this.activityReference.get();
                if (activity == null) {
                    return;
                }
                ResponsiveUIFeature.f8164c.remove(Integer.valueOf(activity.hashCode()));
            }

            @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
            public /* bridge */ /* synthetic */ void onPause(o oVar) {
                super.onPause(oVar);
            }

            @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
            public /* bridge */ /* synthetic */ void onResume(o oVar) {
                super.onResume(oVar);
            }

            @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
            public /* bridge */ /* synthetic */ void onStart(o oVar) {
                super.onStart(oVar);
            }

            @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
            public /* bridge */ /* synthetic */ void onStop(o oVar) {
                super.onStop(oVar);
            }
        });
    }

    public /* synthetic */ ResponsiveUIFeature(ComponentActivity componentActivity, DefaultConstructorMarker defaultConstructorMarker) {
        this(componentActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void b(ResponsiveUIFeature responsiveUIFeature, WindowFeature windowFeature) {
        k.e(responsiveUIFeature, "this$0");
        k.e(windowFeature, "windowFeature");
        responsiveUIFeature.c(responsiveUIFeature.windowFeatureLiveData, windowFeature);
    }

    private final <T> void c(MutableLiveData<T> liveDataObject, T value) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            liveDataObject.m(value);
        } else {
            liveDataObject.k(value);
        }
    }

    public static final FoldingState getFoldingState(Context context) {
        return INSTANCE.getFoldingState(context);
    }

    public static final IResponsiveUIFeature getOrCreate(ComponentActivity componentActivity) {
        return INSTANCE.getOrCreate(componentActivity);
    }

    public static final boolean isSupportWindowFeature() {
        return INSTANCE.isSupportWindowFeature();
    }

    @Override // com.coui.component.responsiveui.IResponsiveUIFeature
    public MutableLiveData<WindowFeature> getWindowFeatureLiveData() {
        return this.windowFeatureLiveData;
    }
}
