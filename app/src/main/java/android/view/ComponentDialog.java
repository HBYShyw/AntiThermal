package android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.h;
import androidx.lifecycle.o;
import kotlin.Metadata;
import za.k;

/* compiled from: ComponentDialog.kt */
@Metadata(bv = {}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0016\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u001b\b\u0007\u0012\u0006\u0010\u001c\u001a\u00020\u001b\u0012\b\b\u0003\u0010\u001e\u001a\u00020\u001d¢\u0006\u0004\b\u001f\u0010 J\u0006\u0010\u0005\u001a\u00020\u0004J\u0012\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0015J\b\u0010\n\u001a\u00020\bH\u0015J\b\u0010\u000b\u001a\u00020\bH\u0015J\u0006\u0010\r\u001a\u00020\fJ\b\u0010\u000e\u001a\u00020\bH\u0017R\u0018\u0010\u0012\u001a\u0004\u0018\u00010\u000f8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0017\u001a\u00020\f8\u0002X\u0082\u0004¢\u0006\f\n\u0004\b\u0013\u0010\u0014\u0012\u0004\b\u0015\u0010\u0016R\u0014\u0010\u001a\u001a\u00020\u000f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019¨\u0006!"}, d2 = {"Landroidx/activity/f;", "Landroid/app/Dialog;", "Landroidx/lifecycle/o;", "Landroidx/activity/k;", "Landroidx/lifecycle/h;", "getLifecycle", "Landroid/os/Bundle;", "savedInstanceState", "Lma/f0;", "onCreate", "onStart", "onStop", "Landroidx/activity/OnBackPressedDispatcher;", "getOnBackPressedDispatcher", "onBackPressed", "Landroidx/lifecycle/q;", "e", "Landroidx/lifecycle/q;", "_lifecycleRegistry", "f", "Landroidx/activity/OnBackPressedDispatcher;", "getOnBackPressedDispatcher$annotations", "()V", "onBackPressedDispatcher", "c", "()Landroidx/lifecycle/q;", "lifecycleRegistry", "Landroid/content/Context;", "context", "", "themeResId", "<init>", "(Landroid/content/Context;I)V", "activity_release"}, k = 1, mv = {1, 7, 1})
/* renamed from: androidx.activity.f, reason: use source file name */
/* loaded from: classes.dex */
public class ComponentDialog extends Dialog implements o, OnBackPressedDispatcherOwner {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private LifecycleRegistry _lifecycleRegistry;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private final OnBackPressedDispatcher onBackPressedDispatcher;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ComponentDialog(Context context, int i10) {
        super(context, i10);
        k.e(context, "context");
        this.onBackPressedDispatcher = new OnBackPressedDispatcher(new Runnable() { // from class: androidx.activity.e
            @Override // java.lang.Runnable
            public final void run() {
                ComponentDialog.d(ComponentDialog.this);
            }
        });
    }

    private final LifecycleRegistry c() {
        LifecycleRegistry lifecycleRegistry = this._lifecycleRegistry;
        if (lifecycleRegistry != null) {
            return lifecycleRegistry;
        }
        LifecycleRegistry lifecycleRegistry2 = new LifecycleRegistry(this);
        this._lifecycleRegistry = lifecycleRegistry2;
        return lifecycleRegistry2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(ComponentDialog componentDialog) {
        k.e(componentDialog, "this$0");
        super.onBackPressed();
    }

    @Override // androidx.lifecycle.o
    public final h getLifecycle() {
        return c();
    }

    @Override // android.view.OnBackPressedDispatcherOwner
    public final OnBackPressedDispatcher getOnBackPressedDispatcher() {
        return this.onBackPressedDispatcher;
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        this.onBackPressedDispatcher.f();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 33) {
            this.onBackPressedDispatcher.g(getOnBackInvokedDispatcher());
        }
        c().h(h.b.ON_CREATE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Dialog
    public void onStart() {
        super.onStart();
        c().h(h.b.ON_RESUME);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Dialog
    public void onStop() {
        c().h(h.b.ON_DESTROY);
        this._lifecycleRegistry = null;
        super.onStop();
    }
}
