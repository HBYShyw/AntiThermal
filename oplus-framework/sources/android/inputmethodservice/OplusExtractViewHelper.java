package android.inputmethodservice;

import android.R;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.EditText;

/* loaded from: classes.dex */
public class OplusExtractViewHelper {
    private static final int BUTTON_ANIMATOR_DOWN_DURATION = 200;
    private static final int BUTTON_ANIMATOR_UP_DURATION = 340;
    private static final float BUTTON_DOWN_SCALE = 0.93f;
    private static final int BUTTON_HEIGHT_28 = 28;
    private static final String BUTTON_SCALE_X = "scaleX";
    private static final String BUTTON_SCALE_Y = "scaleY";
    private static final float BUTTON_UP_SCALE = 1.0f;
    private static final int BUTTON_WIDTH_52 = 52;
    private static final int DEFAULT_THEME_COLOR = -12096257;
    private static final int HIGHLIGHT_ALPHA = 76;
    private static final String KEY_ACCENT_COLOR = "sysui_type_accent_color";
    private static final int LIGHT_BACKGROUND = -657931;
    private static final int LIGHT_BUTTON_OVERLAY = 436207616;
    private static final int LIGHT_CORNER_BACKGROUND = -1;
    private static final int LIGHT_EDIT_HINT_COLOR = 1291845632;
    private static final int LIGHT_EDIT_TEXT_COLOR = -654311424;
    private static final int NIGHT_BACKGROUND = -16777216;
    private static final int NIGHT_BUTTON_OVERLAY = 855638016;
    private static final int NIGHT_CORNER_BACKGROUND = -13750738;
    private static final int NIGHT_EDIT_HINT_COLOR = 1308622847;
    private static final int NIGHT_EDIT_TEXT_COLOR = -637534209;
    private static final int TEXT_SIZE_14 = 14;
    private static final int VALUE_0 = 0;
    private static final int VALUE_12 = 12;
    private static final int VALUE_13 = 13;
    private static final int VALUE_14 = 14;
    private static final int VALUE_16 = 16;
    private static final int VALUE_8 = 8;
    private static final PathInterpolator BUTTON_DOWN_INTERPOLATOR = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
    private static final PathInterpolator BUTTON_UP_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.2f, 1.0f);
    private static int mLastExtractViewHash = 0;

    public static void updateExtractViewStyle(View extractView) {
        if (extractView != null && extractView.hashCode() != mLastExtractViewHash) {
            ViewGroup extractFrame = (ViewGroup) extractView.getParent();
            EditText extractEditText = (EditText) extractView.findViewById(R.id.inputExtractEditText);
            Button extractActionView = (Button) extractView.findViewById(R.id.KEYCODE_1);
            if (extractFrame != null && extractEditText != null && extractActionView != null) {
                mLastExtractViewHash = extractView.hashCode();
                float density = extractView.getResources().getDisplayMetrics().density;
                int dp8 = (int) (8.0f * density);
                int dp12 = (int) (12.0f * density);
                int dp13 = (int) (13.0f * density);
                int dp14 = (int) (density * 14.0f);
                int dp16 = (int) (16.0f * density);
                boolean nightMode = (extractView.getResources().getConfiguration().uiMode & 48) == 32;
                String colorString = Settings.Secure.getString(extractView.getContext().getContentResolver(), KEY_ACCENT_COLOR);
                int themeColor = DEFAULT_THEME_COLOR;
                if (!TextUtils.isEmpty(colorString)) {
                    themeColor = Color.parseColor(colorString);
                }
                GradientDrawable extractBackground = new GradientDrawable();
                extractBackground.setShape(0);
                extractBackground.setCornerRadii(new float[]{dp16, dp16, dp16, dp16, dp16, dp16, dp16, dp16});
                extractBackground.setColor(nightMode ? NIGHT_CORNER_BACKGROUND : -1);
                GradientDrawable actionNormalBackground = new GradientDrawable();
                actionNormalBackground.setShape(0);
                actionNormalBackground.setCornerRadii(new float[]{dp14, dp14, dp14, dp14, dp14, dp14, dp14, dp14});
                actionNormalBackground.setColor(themeColor);
                GradientDrawable actionOverlayBackground = new GradientDrawable();
                actionOverlayBackground.setShape(0);
                actionOverlayBackground.setCornerRadii(new float[]{dp14, dp14, dp14, dp14, dp14, dp14, dp14, dp14});
                actionOverlayBackground.setColor(nightMode ? NIGHT_BUTTON_OVERLAY : LIGHT_BUTTON_OVERLAY);
                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(new int[]{R.attr.state_pressed}, new LayerDrawable(new Drawable[]{actionNormalBackground, actionOverlayBackground}));
                stateListDrawable.addState(new int[]{R.attr.state_enabled}, actionNormalBackground);
                AnimatorSet downAnimatorSet = new AnimatorSet();
                downAnimatorSet.setDuration(200L);
                downAnimatorSet.setInterpolator(BUTTON_DOWN_INTERPOLATOR);
                downAnimatorSet.playTogether(ObjectAnimator.ofFloat(extractActionView, BUTTON_SCALE_X, BUTTON_DOWN_SCALE), ObjectAnimator.ofFloat(extractActionView, BUTTON_SCALE_Y, BUTTON_DOWN_SCALE));
                AnimatorSet upAnimatorSet = new AnimatorSet();
                upAnimatorSet.setDuration(340L);
                downAnimatorSet.setInterpolator(BUTTON_UP_INTERPOLATOR);
                upAnimatorSet.playTogether(ObjectAnimator.ofFloat(extractActionView, BUTTON_SCALE_X, 1.0f), ObjectAnimator.ofFloat(extractActionView, BUTTON_SCALE_Y, 1.0f));
                StateListAnimator stateListAnimator = new StateListAnimator();
                stateListAnimator.addState(new int[]{R.attr.state_pressed}, downAnimatorSet);
                stateListAnimator.addState(new int[]{R.attr.state_enabled}, upAnimatorSet);
                extractEditText.getTextSelectHandleLeft().setTint(themeColor);
                extractEditText.getTextSelectHandleRight().setTint(themeColor);
                extractEditText.getTextSelectHandle().setTint(themeColor);
                extractEditText.getTextCursorDrawable().setTint(themeColor);
                extractEditText.setHighlightColor(Color.argb(76, Color.red(themeColor), Color.green(themeColor), Color.blue(themeColor)));
                extractEditText.setBackground(null);
                extractEditText.setTextSize(14.0f);
                extractEditText.setTextColor(nightMode ? NIGHT_EDIT_TEXT_COLOR : LIGHT_EDIT_TEXT_COLOR);
                extractEditText.setHintTextColor(nightMode ? NIGHT_EDIT_HINT_COLOR : LIGHT_EDIT_HINT_COLOR);
                extractEditText.setPadding(dp16, dp13, 0, dp8);
                extractActionView.setTextColor(-1);
                extractActionView.setMinWidth((int) (density * 52.0f));
                extractActionView.setMinimumWidth((int) (52.0f * density));
                extractActionView.getLayoutParams().height = (int) (28.0f * density);
                extractActionView.setPadding(dp12, 0, dp12, 0);
                extractActionView.setBackground(stateListDrawable);
                extractActionView.setGravity(17);
                extractActionView.setTextSize(14.0f);
                extractActionView.setStateListAnimator(stateListAnimator);
                extractView.setBackground(extractBackground);
                extractView.setPadding(0, 0, dp8, 0);
                ((ViewGroup.MarginLayoutParams) extractView.getLayoutParams()).setMargins(dp16, dp16, dp16, dp16);
                extractFrame.setBackgroundColor(nightMode ? -16777216 : LIGHT_BACKGROUND);
            }
        }
    }
}
