package com.oplus.powermanager.fuelgaue.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.oplus.battery.R;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneFlightData;

@SuppressLint({"RestrictedApi"})
/* loaded from: classes.dex */
public class HighlightPreferenceGroupAdapter extends PreferenceGroupAdapter {
    static final long DELAY_HIGHLIGHT_DURATION_MILLIS = 600;
    private static final int DELAY_TIME = 200;
    private static final boolean ENABLE = true;
    public static final String EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";
    private static final long HIGHLIGHT_DURATION = 1000;
    private static final int HIGHT_LIGHT_COLOR_PREFERENCE_DEFAULT = -1776412;
    private static final int LAST_TIME = 500;
    private static final int START_TIME = 100;
    private static final int STOP_TIME = 250;
    private static final String TAG = "HighlightableAdapter";
    boolean mFadeInAnimated;
    final int mHighlightColor;
    private final String mHighlightKey;
    private int mHighlightPosition;
    private boolean mHighlightRequested;
    private final int mNormalBackgroundRes;

    public HighlightPreferenceGroupAdapter(PreferenceGroup preferenceGroup, String str, boolean z10) {
        super(preferenceGroup);
        this.mHighlightPosition = -1;
        this.mHighlightKey = str;
        this.mHighlightRequested = z10;
        Context context = preferenceGroup.getContext();
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        this.mNormalBackgroundRes = typedValue.resourceId;
        context.getTheme().resolveAttribute(R.attr.couiColorCardPressed, typedValue, true);
        this.mHighlightColor = preferenceGroup.getContext().getColor(typedValue.resourceId);
    }

    private void addHighlightBackground(View view, boolean z10, int i10) {
        view.setTag(R.id.preference_highlighted, Boolean.TRUE);
        if (!this.mFadeInAnimated) {
            this.mFadeInAnimated = true;
            AnimationDrawable animationDrawable = getAnimationDrawable(this.mHighlightColor, view.getBackground());
            view.setBackgroundDrawable(animationDrawable);
            animationDrawable.start();
            Log.d(TAG, "AddHighlight: starting fade in animation");
            requestRemoveHighlightDelayed(view, i10);
            return;
        }
        requestRemoveHighlightDelayed(view, i10);
    }

    public static void adjustInitialExpandedChildCount(PreferenceFragmentCompat preferenceFragmentCompat) {
        PreferenceScreen preferenceScreen;
        Bundle arguments;
        if (preferenceFragmentCompat == null || (preferenceScreen = preferenceFragmentCompat.getPreferenceScreen()) == null || (arguments = preferenceFragmentCompat.getArguments()) == null || TextUtils.isEmpty(arguments.getString(EXTRA_FRAGMENT_ARG_KEY))) {
            return;
        }
        preferenceScreen.p(Integer.MAX_VALUE);
    }

    private static AnimationDrawable getAnimationDrawable(int i10, Drawable drawable) {
        char c10;
        AnimationDrawable animationDrawable = new AnimationDrawable();
        int i11 = (i10 >> 24) & 255;
        Log.d(TAG, "getAnimationDrawable: originalAlpha=" + i11 + " 0xFF=255 0x33=51 bColor=" + i10);
        for (int i12 = 0; i12 < 6; i12++) {
            double d10 = (i11 * (i12 + UserProfileInfo.Constant.NA_LAT_LON)) / 6;
            ColorDrawable colorDrawable = new ColorDrawable(i10);
            colorDrawable.setAlpha((int) d10);
            if (drawable != null) {
                animationDrawable.addFrame(new LayerDrawable(new Drawable[]{drawable, colorDrawable}), 16);
            } else {
                animationDrawable.addFrame(colorDrawable, 16);
            }
        }
        animationDrawable.addFrame(new ColorDrawable(i10), STOP_TIME);
        for (int i13 = 0; i13 < 31; i13++) {
            double d11 = (i11 * ((31 - i13) - UserProfileInfo.Constant.NA_LAT_LON)) / 31;
            ColorDrawable colorDrawable2 = new ColorDrawable(i10);
            colorDrawable2.setAlpha((int) d11);
            if (drawable != null) {
                c10 = 16;
                animationDrawable.addFrame(new LayerDrawable(new Drawable[]{drawable, colorDrawable2}), 16);
            } else {
                c10 = 16;
                animationDrawable.addFrame(colorDrawable2, 16);
                if (i13 == 30) {
                    animationDrawable.addFrame(new ColorDrawable(0), SceneFlightData.INVALID_LATITUDE_LONGITUDE);
                }
            }
        }
        if (drawable != null) {
            animationDrawable.addFrame(drawable, SceneFlightData.INVALID_LATITUDE_LONGITUDE);
        }
        animationDrawable.setOneShot(true);
        return animationDrawable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeHighlightBackground(View view, boolean z10, int i10) {
        Preference item = getItem(i10);
        if (item != null) {
            COUICardListHelper.d(view, COUICardListHelper.b(item));
            Log.d(TAG, "Starting fade out animation: not setBackgroundResource");
        }
    }

    public boolean isHighlightRequested() {
        return this.mHighlightRequested;
    }

    public void requestHighlight(View view, RecyclerView recyclerView, boolean z10) {
        int preferenceAdapterPosition;
        if (this.mHighlightRequested || recyclerView == null || TextUtils.isEmpty(this.mHighlightKey) || (preferenceAdapterPosition = getPreferenceAdapterPosition(this.mHighlightKey)) < 0) {
            return;
        }
        Preference item = getItem(preferenceAdapterPosition);
        if (item != null && (item instanceof PreferenceCategory)) {
            if (z10) {
                recyclerView.scrollToPosition(preferenceAdapterPosition);
                return;
            } else {
                if (preferenceAdapterPosition > 0) {
                    recyclerView.scrollToPosition(preferenceAdapterPosition - 1);
                    return;
                }
                return;
            }
        }
        if (z10) {
            recyclerView.scrollToPosition(preferenceAdapterPosition);
        } else if (preferenceAdapterPosition > 1) {
            recyclerView.scrollToPosition(preferenceAdapterPosition - 2);
        } else {
            recyclerView.scrollToPosition(preferenceAdapterPosition - 1);
        }
        this.mHighlightRequested = true;
        view.postDelayed(new Runnable() { // from class: com.oplus.powermanager.fuelgaue.base.HighlightPreferenceGroupAdapter.1
            @Override // java.lang.Runnable
            public void run() {
                HighlightPreferenceGroupAdapter highlightPreferenceGroupAdapter = HighlightPreferenceGroupAdapter.this;
                highlightPreferenceGroupAdapter.mHighlightPosition = highlightPreferenceGroupAdapter.getPreferenceAdapterPosition(highlightPreferenceGroupAdapter.mHighlightKey);
                HighlightPreferenceGroupAdapter highlightPreferenceGroupAdapter2 = HighlightPreferenceGroupAdapter.this;
                highlightPreferenceGroupAdapter2.notifyItemChanged(highlightPreferenceGroupAdapter2.mHighlightPosition);
            }
        }, DELAY_HIGHLIGHT_DURATION_MILLIS);
    }

    void requestRemoveHighlightDelayed(final View view, final int i10) {
        view.postDelayed(new Runnable() { // from class: com.oplus.powermanager.fuelgaue.base.HighlightPreferenceGroupAdapter.2
            @Override // java.lang.Runnable
            public void run() {
                HighlightPreferenceGroupAdapter.this.mHighlightPosition = -1;
                HighlightPreferenceGroupAdapter.this.removeHighlightBackground(view, true, i10);
            }
        }, HIGHLIGHT_DURATION);
    }

    void updateBackground(PreferenceViewHolder preferenceViewHolder, int i10) {
        View view = preferenceViewHolder.itemView;
        if (i10 == this.mHighlightPosition) {
            addHighlightBackground(view, !this.mFadeInAnimated, i10);
        } else if (Boolean.TRUE.equals(view.getTag(R.id.preference_highlighted))) {
            removeHighlightBackground(view, false, i10);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.preference.PreferenceGroupAdapter, androidx.recyclerview.widget.RecyclerView.h
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder, int i10) {
        super.onBindViewHolder(preferenceViewHolder, i10);
        updateBackground(preferenceViewHolder, i10);
    }
}
