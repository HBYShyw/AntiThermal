package com.android.server.input;

import android.R;
import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.Slog;
import android.util.TypedValue;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.AccelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.server.input.FocusEventDebugView;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class FocusEventDebugView extends LinearLayout {
    private static final int KEY_FADEOUT_DURATION_MILLIS = 1000;
    private static final int KEY_SEPARATION_MARGIN_DP = 16;
    private static final int KEY_TRANSITION_DURATION_MILLIS = 100;
    private static final int KEY_VIEW_MIN_WIDTH_DP = 32;
    private static final int KEY_VIEW_SIDE_PADDING_DP = 16;
    private static final int KEY_VIEW_TEXT_SIZE_SP = 12;
    private static final int KEY_VIEW_VERTICAL_PADDING_DP = 8;
    private static final int OUTER_PADDING_DP = 16;
    private static final String TAG = FocusEventDebugView.class.getSimpleName();
    private final int mOuterPadding;
    private final PressedKeyContainer mPressedKeyContainer;
    private final Map<Pair<Integer, Integer>, PressedKeyView> mPressedKeys;
    private final PressedKeyContainer mPressedModifierContainer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FocusEventDebugView(Context context) {
        super(context);
        this.mPressedKeys = new HashMap();
        setFocusableInTouchMode(true);
        this.mOuterPadding = (int) TypedValue.applyDimension(1, 16.0f, ((LinearLayout) this).mContext.getResources().getDisplayMetrics());
        setOrientation(0);
        setLayoutDirection(1);
        setGravity(8388691);
        PressedKeyContainer pressedKeyContainer = new PressedKeyContainer(((LinearLayout) this).mContext);
        this.mPressedKeyContainer = pressedKeyContainer;
        pressedKeyContainer.setOrientation(0);
        pressedKeyContainer.setGravity(85);
        pressedKeyContainer.setLayoutDirection(0);
        final HorizontalScrollView horizontalScrollView = new HorizontalScrollView(((LinearLayout) this).mContext);
        horizontalScrollView.addView(pressedKeyContainer);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.server.input.FocusEventDebugView$$ExternalSyntheticLambda1
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                horizontalScrollView.fullScroll(66);
            }
        });
        horizontalScrollView.setHorizontalFadingEdgeEnabled(true);
        addView(horizontalScrollView, new LinearLayout.LayoutParams(0, -2, 1.0f));
        PressedKeyContainer pressedKeyContainer2 = new PressedKeyContainer(((LinearLayout) this).mContext);
        this.mPressedModifierContainer = pressedKeyContainer2;
        pressedKeyContainer2.setOrientation(1);
        pressedKeyContainer2.setGravity(83);
        addView(pressedKeyContainer2, new LinearLayout.LayoutParams(-2, -2));
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        RoundedCorner roundedCorner = windowInsets.getRoundedCorner(3);
        int radius = roundedCorner != null ? roundedCorner.getRadius() : 0;
        RoundedCorner roundedCorner2 = windowInsets.getRoundedCorner(2);
        if (roundedCorner2 != null) {
            radius = Math.max(radius, roundedCorner2.getRadius());
        }
        if (windowInsets.getDisplayCutout() != null) {
            radius = Math.max(radius, windowInsets.getDisplayCutout().getSafeInsetBottom());
        }
        int i = this.mOuterPadding;
        setPadding(i, i, i, radius + i);
        setClipToPadding(false);
        invalidate();
        return super.onApplyWindowInsets(windowInsets);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        handleKeyEvent(keyEvent);
        return super.dispatchKeyEvent(keyEvent);
    }

    public void reportEvent(final InputEvent inputEvent) {
        if (inputEvent instanceof KeyEvent) {
            post(new Runnable() { // from class: com.android.server.input.FocusEventDebugView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FocusEventDebugView.this.lambda$reportEvent$1(inputEvent);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportEvent$1(InputEvent inputEvent) {
        handleKeyEvent(KeyEvent.obtain((KeyEvent) inputEvent));
    }

    private void handleKeyEvent(KeyEvent keyEvent) {
        PressedKeyContainer pressedKeyContainer;
        Pair<Integer, Integer> pair = new Pair<>(Integer.valueOf(keyEvent.getDeviceId()), Integer.valueOf(keyEvent.getScanCode()));
        if (KeyEvent.isModifierKey(keyEvent.getKeyCode())) {
            pressedKeyContainer = this.mPressedModifierContainer;
        } else {
            pressedKeyContainer = this.mPressedKeyContainer;
        }
        PressedKeyView pressedKeyView = this.mPressedKeys.get(pair);
        int action = keyEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                if (pressedKeyView == null) {
                    Slog.w(TAG, "Got key up for " + KeyEvent.keyCodeToString(keyEvent.getKeyCode()) + " that was not tracked as being down.");
                } else {
                    this.mPressedKeys.remove(pair);
                    pressedKeyContainer.handleKeyRelease(pressedKeyView);
                }
            }
        } else if (pressedKeyView != null) {
            if (keyEvent.getRepeatCount() == 0) {
                Slog.w(TAG, "Got key down for " + KeyEvent.keyCodeToString(keyEvent.getKeyCode()) + " that was already tracked as being down.");
            } else {
                pressedKeyContainer.handleKeyRepeat(pressedKeyView);
            }
        } else {
            PressedKeyView pressedKeyView2 = new PressedKeyView(((LinearLayout) this).mContext, getLabel(keyEvent));
            this.mPressedKeys.put(pair, pressedKeyView2);
            pressedKeyContainer.handleKeyPressed(pressedKeyView2);
        }
        keyEvent.recycle();
    }

    private static String getLabel(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 61) {
            return "⇥";
        }
        if (keyCode == 62) {
            return "␣";
        }
        if (keyCode == 66) {
            return "⏎";
        }
        if (keyCode == 67) {
            return "⌫";
        }
        if (keyCode == 111) {
            return "ESC";
        }
        if (keyCode == 112) {
            return "⌦";
        }
        if (keyCode == 160) {
            return "⏎";
        }
        switch (keyCode) {
            case 19:
                return "↑";
            case 20:
                return "↓";
            case 21:
                return "←";
            case 22:
                return "→";
            default:
                switch (keyCode) {
                    case 268:
                        return "↖";
                    case 269:
                        return "↙";
                    case 270:
                        return "↗";
                    case 271:
                        return "↘";
                    default:
                        int unicodeChar = keyEvent.getUnicodeChar();
                        if (unicodeChar != 0) {
                            return new String(Character.toChars(unicodeChar));
                        }
                        String keyCodeToString = KeyEvent.keyCodeToString(keyEvent.getKeyCode());
                        return keyCodeToString.startsWith("KEYCODE_") ? keyCodeToString.substring(8) : keyCodeToString;
                }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PressedKeyView extends TextView {
        private static final ColorFilter sInvertColors = new ColorMatrixColorFilter(new float[]{-1.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});

        PressedKeyView(Context context, String str) {
            super(context);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int applyDimension = (int) TypedValue.applyDimension(1, 16.0f, displayMetrics);
            int applyDimension2 = (int) TypedValue.applyDimension(1, 8.0f, displayMetrics);
            int applyDimension3 = (int) TypedValue.applyDimension(1, 32.0f, displayMetrics);
            int applyDimension4 = (int) TypedValue.applyDimension(2, 12.0f, displayMetrics);
            setText(str);
            setGravity(17);
            setMinimumWidth(applyDimension3);
            setTextSize(applyDimension4);
            setTypeface(Typeface.SANS_SERIF);
            setBackgroundResource(R.drawable.ft_avd_tooverflow);
            setPaddingRelative(applyDimension, applyDimension2, applyDimension, applyDimension2);
            setHighlighted(true);
        }

        void setHighlighted(boolean z) {
            if (z) {
                setTextColor(-16777216);
                getBackground().setColorFilter(sInvertColors);
            } else {
                setTextColor(-1);
                getBackground().clearColorFilter();
            }
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PressedKeyContainer extends LinearLayout {
        private final ViewGroup.MarginLayoutParams mPressedKeyLayoutParams;

        public void handleKeyRepeat(PressedKeyView pressedKeyView) {
        }

        PressedKeyContainer(Context context) {
            super(context);
            int applyDimension = (int) TypedValue.applyDimension(1, 16.0f, context.getResources().getDisplayMetrics());
            LayoutTransition layoutTransition = new LayoutTransition();
            layoutTransition.disableTransitionType(2);
            layoutTransition.disableTransitionType(3);
            layoutTransition.disableTransitionType(1);
            layoutTransition.setDuration(100L);
            setLayoutTransition(layoutTransition);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(-2, -2);
            this.mPressedKeyLayoutParams = marginLayoutParams;
            if (getOrientation() == 1) {
                marginLayoutParams.setMargins(0, applyDimension, 0, 0);
            } else {
                marginLayoutParams.setMargins(applyDimension, 0, 0, 0);
            }
        }

        public void handleKeyPressed(PressedKeyView pressedKeyView) {
            addView(pressedKeyView, getChildCount(), this.mPressedKeyLayoutParams);
            invalidate();
        }

        public void handleKeyRelease(PressedKeyView pressedKeyView) {
            pressedKeyView.setHighlighted(false);
            pressedKeyView.clearAnimation();
            pressedKeyView.animate().alpha(0.0f).setDuration(1000L).setInterpolator(new AccelerateInterpolator()).withEndAction(new Runnable() { // from class: com.android.server.input.FocusEventDebugView$PressedKeyContainer$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FocusEventDebugView.PressedKeyContainer.this.cleanUpPressedKeyViews();
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cleanUpPressedKeyViews() {
            int i = 0;
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                View childAt = getChildAt(i2);
                if (childAt.getAlpha() != 0.0f) {
                    break;
                }
                childAt.setVisibility(8);
                childAt.clearAnimation();
                i++;
            }
            removeViews(0, i);
            invalidate();
        }
    }
}
