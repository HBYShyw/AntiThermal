package com.android.internal.app;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.oplus.resolver.OplusResolverUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AbstractMultiProfilePagerAdapterExtImpl implements IAbstractMultiProfilePagerAdapterExt {
    private static final String TAG = "MultiProfilePagerAdapterExtImpl";
    private Context mContext = null;
    private Object multiProfilePagerAdapter;

    public AbstractMultiProfilePagerAdapterExtImpl(Object base) {
        this.multiProfilePagerAdapter = base;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public void showActiveEmptyViewState() {
        if (shouldShowEmptyState()) {
            showEmptyViewState();
        }
    }

    private boolean shouldShowEmptyState() {
        Integer currentPage = (Integer) OplusResolverUtils.invokeMethod(this.multiProfilePagerAdapter, "getCurrentPage", new Object[0]);
        if (currentPage == null) {
            return true;
        }
        List<Class> paramCls = new ArrayList<>();
        paramCls.add(Integer.TYPE);
        Object descriptor = OplusResolverUtils.invokeMethod(this.multiProfilePagerAdapter, "getItem", paramCls, currentPage);
        View view = (View) OplusResolverUtils.getFiledValue(descriptor, "rootView");
        return view != null && view.findViewById(R.id.single).getVisibility() == 0;
    }

    private void showEmptyViewState() {
        View rootView;
        Integer currentPage = (Integer) OplusResolverUtils.invokeMethod(this.multiProfilePagerAdapter, "getCurrentPage", new Object[0]);
        if (currentPage == null) {
            return;
        }
        List<Class> paramCls = new ArrayList<>();
        paramCls.add(Integer.TYPE);
        Object descriptor = OplusResolverUtils.invokeMethod(this.multiProfilePagerAdapter, "getItem", paramCls, currentPage);
        ViewGroup parent = (ViewGroup) ((Activity) this.mContext).findViewById(R.id.tabcontent);
        if (parent == null || (rootView = (View) OplusResolverUtils.getFiledValue(descriptor, "rootView")) == null) {
            return;
        }
        Object drawable = ((ImageView) rootView.findViewById(R.id.singleTask)).getDrawable();
        if (parent.getChildCount() == 2) {
            if (parent.getChildAt(1) != rootView) {
                Object drawable2 = ((ImageView) parent.getChildAt(1).findViewById(R.id.singleTask)).getDrawable();
                if (drawable2 != null && (drawable2 instanceof Animatable) && ((Animatable) drawable2).isRunning()) {
                    ((Animatable) drawable2).stop();
                }
                parent.removeViewAt(1);
            } else {
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                    return;
                }
                return;
            }
        } else {
            parent.getChildAt(0).setVisibility(8);
        }
        parent.addView(rootView);
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }
}
