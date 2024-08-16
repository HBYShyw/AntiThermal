package androidx.fragment.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.R$styleable;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FragmentLayoutInflaterFactory.java */
/* renamed from: androidx.fragment.app.i, reason: use source file name */
/* loaded from: classes.dex */
public class FragmentLayoutInflaterFactory implements LayoutInflater.Factory2 {

    /* renamed from: e, reason: collision with root package name */
    final FragmentManager f2905e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentLayoutInflaterFactory.java */
    /* renamed from: androidx.fragment.app.i$a */
    /* loaded from: classes.dex */
    public class a implements View.OnAttachStateChangeListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ FragmentStateManager f2906e;

        a(FragmentStateManager fragmentStateManager) {
            this.f2906e = fragmentStateManager;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            Fragment k10 = this.f2906e.k();
            this.f2906e.m();
            SpecialEffectsController.n((ViewGroup) k10.mView.getParent(), FragmentLayoutInflaterFactory.this.f2905e).j();
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentLayoutInflaterFactory(FragmentManager fragmentManager) {
        this.f2905e = fragmentManager;
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    @Override // android.view.LayoutInflater.Factory2
    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        FragmentStateManager w10;
        if (FragmentContainerView.class.getName().equals(str)) {
            return new FragmentContainerView(context, attributeSet, this.f2905e);
        }
        if (!"fragment".equals(str)) {
            return null;
        }
        String attributeValue = attributeSet.getAttributeValue(null, "class");
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Fragment);
        if (attributeValue == null) {
            attributeValue = obtainStyledAttributes.getString(R$styleable.Fragment_android_name);
        }
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.Fragment_android_id, -1);
        String string = obtainStyledAttributes.getString(R$styleable.Fragment_android_tag);
        obtainStyledAttributes.recycle();
        if (attributeValue == null || !FragmentFactory.b(context.getClassLoader(), attributeValue)) {
            return null;
        }
        int id2 = view != null ? view.getId() : 0;
        if (id2 == -1 && resourceId == -1 && string == null) {
            throw new IllegalArgumentException(attributeSet.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + attributeValue);
        }
        Fragment i02 = resourceId != -1 ? this.f2905e.i0(resourceId) : null;
        if (i02 == null && string != null) {
            i02 = this.f2905e.j0(string);
        }
        if (i02 == null && id2 != -1) {
            i02 = this.f2905e.i0(id2);
        }
        if (i02 == null) {
            i02 = this.f2905e.s0().a(context.getClassLoader(), attributeValue);
            i02.mFromLayout = true;
            i02.mFragmentId = resourceId != 0 ? resourceId : id2;
            i02.mContainerId = id2;
            i02.mTag = string;
            i02.mInLayout = true;
            FragmentManager fragmentManager = this.f2905e;
            i02.mFragmentManager = fragmentManager;
            i02.mHost = fragmentManager.v0();
            i02.onInflate(this.f2905e.v0().f(), attributeSet, i02.mSavedFragmentState);
            w10 = this.f2905e.g(i02);
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "Fragment " + i02 + " has been inflated via the <fragment> tag: id=0x" + Integer.toHexString(resourceId));
            }
        } else if (!i02.mInLayout) {
            i02.mInLayout = true;
            FragmentManager fragmentManager2 = this.f2905e;
            i02.mFragmentManager = fragmentManager2;
            i02.mHost = fragmentManager2.v0();
            i02.onInflate(this.f2905e.v0().f(), attributeSet, i02.mSavedFragmentState);
            w10 = this.f2905e.w(i02);
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "Retained Fragment " + i02 + " has been re-attached via the <fragment> tag: id=0x" + Integer.toHexString(resourceId));
            }
        } else {
            throw new IllegalArgumentException(attributeSet.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(resourceId) + ", tag " + string + ", or parent id 0x" + Integer.toHexString(id2) + " with another fragment for " + attributeValue);
        }
        i02.mContainer = (ViewGroup) view;
        w10.m();
        w10.j();
        View view2 = i02.mView;
        if (view2 != null) {
            if (resourceId != 0) {
                view2.setId(resourceId);
            }
            if (i02.mView.getTag() == null) {
                i02.mView.setTag(string);
            }
            i02.mView.addOnAttachStateChangeListener(new a(w10));
            return i02.mView;
        }
        throw new IllegalStateException("Fragment " + attributeValue + " did not create a view.");
    }
}
