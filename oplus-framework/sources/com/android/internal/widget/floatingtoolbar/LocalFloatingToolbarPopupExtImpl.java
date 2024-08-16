package com.android.internal.widget.floatingtoolbar;

import android.R;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Outline;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.oplus.grid.OplusGridHelper;

/* loaded from: classes.dex */
public class LocalFloatingToolbarPopupExtImpl implements ILocalFloatingToolbarPopupExt {
    private static final float ELEVATION_ALPHA = 1.0f;
    private static final int ELEVATION_VALUE = 6;
    private static final int MAX_OVER_SCROLL_SIZE = 4;
    private static final int MIN_OVER_SCROLL_SIZE = 3;
    private static final float NUM_0_5 = 0.5f;
    private static final int OVER_FLOW_BTN_MARGIN_START = 10;
    private static final int OVER_FLOW_FIRST_ITEM_PADDING_TOP_DP = 12;
    private static final int OVER_FLOW_NORMAL_ITEM_HEIGHT_DP = 40;
    private static final String TAG = "LocalFloatingToolbarPopupExtImpl";
    private IOverflowPanelViewHelperExt mOverflowPanelViewHelperExt = IOverflowPanelViewHelperExt.DEFAULT;

    public LocalFloatingToolbarPopupExtImpl(Object base) {
    }

    public Integer hookMinOverflowSize() {
        return 3;
    }

    public Integer hookMaxOverflowSize() {
        return 4;
    }

    public int hookFloatingToolbarPopupForLineHeight(Context context, int defaultFloatingToolBarHeightRes) {
        return context.getResources().getDimensionPixelSize(201654403);
    }

    public Drawable hookFloatingToolbarPopupForArrow(Context context, int defaultArrowDrawableId) {
        return context.getResources().getDrawable(201850966, context.getTheme());
    }

    public Drawable hookFloatingToolbarPopupForOverflow(Context context, int defaultOverflowDrawableId) {
        return context.getResources().getDrawable(201850956, context.getTheme());
    }

    public AnimatedVectorDrawable hookFloatingToolbarPopupForToArrow(Context context, int defaultToArrowAnimId) {
        return (AnimatedVectorDrawable) context.getResources().getDrawable(201850957, context.getTheme());
    }

    public AnimatedVectorDrawable hookFloatingToolbarPopupForToOverflow(Context context, int defaultToOverflowAnimId) {
        return (AnimatedVectorDrawable) context.getResources().getDrawable(201850969, context.getTheme());
    }

    public void hookFloatingToolbarPopupAfterOverflowPanelViewHelper(IOverflowPanelViewHelperExt overflowPanelViewHelperExt) {
        this.mOverflowPanelViewHelperExt = overflowPanelViewHelperExt;
    }

    public int hookGetFirstItemPaddingStart(Context context, int paddingStart) {
        int paddingStartExtra = context.getResources().getDimensionPixelSize(201654397);
        return paddingStartExtra + paddingStart;
    }

    public int hookGetLastItemPaddingEnd(Context context, int paddingEnd) {
        int paddingEndExtra = context.getResources().getDimensionPixelSize(201654398);
        return paddingEndExtra + paddingEnd;
    }

    private void setOverflowMenuCount(int count) {
        this.mOverflowPanelViewHelperExt.setOverflowMenuCount(count);
    }

    public void hookLayoutOverflowPanelItems(int overflowMenuCount) {
        setOverflowMenuCount(overflowMenuCount);
    }

    public void hookClearPanels(int overflowMenuCount) {
        setOverflowMenuCount(overflowMenuCount);
    }

    public int hookCalculateOverflowExtension(ViewGroup contentContainerView, int lineHeight) {
        if (contentContainerView != null) {
            return contentContainerView.getPaddingTop() + contentContainerView.getPaddingBottom();
        }
        return (int) (lineHeight * 0.5f);
    }

    public ImageButton hookCreateOverflowButton(Context context, int defaultOverflowButtonRes) {
        return (ImageButton) LayoutInflater.from(context).inflate(201917457, (ViewGroup) null);
    }

    private void setListViewBackground(ListView listview) {
        ColorDrawable color = new ColorDrawable(16777215);
        listview.setSelector(color);
    }

    private void setOverflowScrollBarSize(ListView listview) {
        setListViewBackground(listview);
        Context context = listview.getContext();
        int scrollBarSize = context.getResources().getDimensionPixelSize(201654401);
        listview.setScrollBarSize(scrollBarSize);
    }

    public void hookCreateOverflowPanelAfterOverflowPanel(ListView listview) {
        setOverflowScrollBarSize(listview);
    }

    public void hookCreateOverflowPanelAtAdapterGetView(int position, boolean upward) {
        this.mOverflowPanelViewHelperExt.setConvertViewPosition(position);
        this.mOverflowPanelViewHelperExt.setOverflowDirection(upward);
    }

    public int hookGetMarginVertical(int defaultValue) {
        return 201654465;
    }

    public int hookGetOverflowBtnExtraStartX(View overflowButton) {
        return dp2px(overflowButton.getContext(), 10);
    }

    public int hookCalOverflowTotalItemHeight(Context context, int itemCount, int itemHeight) {
        int lineHeight = dp2px(context, 40);
        int firstItemExtraPaddingTop = dp2px(context, 12);
        return (itemCount * lineHeight) + firstItemExtraPaddingTop;
    }

    public int hookCalOverflowGridMaximumWidth(Context context, int screenWidth) {
        return (int) OplusGridHelper.calculateColumnWidth(context, screenWidth, context.getResources().getInteger(202178606));
    }

    public void hookSetElevation(View view) {
        if (view != null) {
            try {
                final int cornerSize = view.getResources().getDimensionPixelSize(201654396);
                ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() { // from class: com.android.internal.widget.floatingtoolbar.LocalFloatingToolbarPopupExtImpl.1
                    @Override // android.view.ViewOutlineProvider
                    public void getOutline(View view2, Outline outline) {
                        outline.setAlpha(1.0f);
                        outline.setRoundRect(0, 0, view2.getWidth(), view2.getHeight(), cornerSize);
                    }
                };
                view.setOutlineProvider(viewOutlineProvider);
                view.setElevation(view.getResources().getDimensionPixelSize(201654509));
                view.setOutlineSpotShadowColor(view.getResources().getColor(201719905));
            } catch (Exception e) {
                Log.e(TAG, "hook set elevation occur error --- " + e);
            }
        }
    }

    private int dp2px(Context context, int dp) {
        return (int) ((context.getResources().getDisplayMetrics().density * dp) + 0.5f);
    }

    public View hookCreateMenuItemButton(Context context) {
        if (context != null) {
            View menuItemButton = LayoutInflater.from(context).inflate(201917459, (ViewGroup) null);
            return menuItemButton;
        }
        return null;
    }

    public TextView hookUpdateMenuItemButtonForText(View menuItemButton) {
        if (menuItemButton != null) {
            TextView buttonText = (TextView) menuItemButton.findViewById(R.id.immersive_cling_description);
            return buttonText;
        }
        return null;
    }

    public ImageView hookUpdateMenuItemButtonForIcon(View menuItemButton) {
        if (menuItemButton != null) {
            ImageView buttonIcon = (ImageView) menuItemButton.findViewById(R.id.immersive_cling_back_bg_light);
            return buttonIcon;
        }
        return null;
    }

    public ViewGroup hookCreateContentContainer(Context context) {
        if (context != null) {
            ViewGroup contentContainer = (ViewGroup) LayoutInflater.from(context).inflate(201917458, (ViewGroup) null);
            return contentContainer;
        }
        return null;
    }

    public void loadAppIcon(MenuItem menuItem, ImageView imageView, TextView textView) {
        try {
            Context context = imageView.getContext();
            String packageName = menuItem.getIntent().getComponent().getPackageName();
            if (!shouldShowMenuIcon(packageName)) {
                return;
            }
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(packageName, 0);
            Drawable drawable = info.loadIcon(packageManager);
            if (drawable != null) {
                imageView.setVisibility(0);
                imageView.setImageDrawable(drawable);
                int paddingStart = (int) ((context.getResources().getConfiguration().densityDpi * 8) / 160.0f);
                textView.setPaddingRelative(paddingStart, 0, 0, 0);
            }
        } catch (Exception e) {
            Log.d("FloatingToolbar", "loadAppIcon failed - " + e.getMessage());
        }
    }

    public void addRoundCornerAnimation(AnimationSet animationSet, Interpolator interpolator, int duration, final boolean isOpening, final ViewGroup container) {
        if (container == null) {
            return;
        }
        final int overflowCornerRadius = container.getResources().getDimensionPixelSize(201654396);
        Animation animation = new Animation() { // from class: com.android.internal.widget.floatingtoolbar.LocalFloatingToolbarPopupExtImpl.2
            @Override // android.view.animation.Animation
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int cornerRadius;
                if (isOpening) {
                    cornerRadius = overflowCornerRadius - ((int) ((r1 - 48) * interpolatedTime));
                } else {
                    int cornerRadius2 = overflowCornerRadius;
                    cornerRadius = ((int) ((cornerRadius2 - 48) * interpolatedTime)) + 48;
                }
                final int cornerRadius3 = cornerRadius;
                container.setOutlineProvider(new ViewOutlineProvider() { // from class: com.android.internal.widget.floatingtoolbar.LocalFloatingToolbarPopupExtImpl.2.1
                    @Override // android.view.ViewOutlineProvider
                    public void getOutline(View view, Outline outline) {
                        outline.setAlpha(1.0f);
                        outline.setRoundRect(0, 0, container.getWidth(), container.getHeight(), cornerRadius3);
                    }
                });
                container.invalidateOutline();
            }
        };
        animation.setDuration(duration);
        animation.setInterpolator(interpolator);
        animationSet.addAnimation(animation);
    }

    private boolean shouldShowMenuIcon(String packageName) {
        return !"com.heytap.browser".equals(packageName);
    }
}
