package android.text;

import android.content.Context;
import android.widget.TextView;
import com.oplus.util.OplusContextUtil;

/* loaded from: classes.dex */
public class TextJustificationHooksImpl implements ITextJustificationHooks {
    private static final String TAG = "TextJustificationHooksImpl";
    public float mTextViewParaSpacing = 0.0f;
    public boolean mLayoutSpecifiedParaSpacing = false;
    public float mBuilderParaSpacingAdded = 0.0f;

    @Override // android.text.ITextJustificationHooks
    public void setTextViewParaSpacing(Object view, float paraSpacing, Layout layout) {
        if (view == null || !(view instanceof TextView)) {
            return;
        }
        TextView textview = (TextView) view;
        textview.hashCode();
        float oldParaSpacing = this.mTextViewParaSpacing;
        if (paraSpacing != oldParaSpacing) {
            this.mTextViewParaSpacing = paraSpacing;
            if (layout != null) {
                textview.nullLayouts();
                textview.requestLayout();
                textview.invalidate();
            }
        }
    }

    @Override // android.text.ITextJustificationHooks
    public float getTextViewParaSpacing(Object view) {
        return this.mTextViewParaSpacing;
    }

    @Override // android.text.ITextJustificationHooks
    public float getTextViewDefaultLineMulti(Object view, float pxSize, float oriValue) {
        if (view == null || !(view instanceof TextView)) {
            return oriValue;
        }
        TextView textview = (TextView) view;
        Context context = textview.getContext();
        if (!OplusContextUtil.isOplusOSStyle(context)) {
            return oriValue;
        }
        int spSize = px2sp(context, pxSize);
        switch (spSize) {
            case 10:
                return 1.3f;
            case 11:
            case 13:
            case 15:
            case 17:
            case 19:
            case 21:
            case 23:
            default:
                return oriValue;
            case 12:
                return 1.15f;
            case 14:
                return 1.2f;
            case 16:
                return 1.1f;
            case 18:
                return 1.1f;
            case 20:
                return 1.1f;
            case 22:
                return 1.1f;
            case 24:
                return 1.1f;
        }
    }

    private int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) ((pxValue / fontScale) + 0.5f);
    }

    @Override // android.text.ITextJustificationHooks
    public float calculateAddedWidth(float justifyWidth, float width, int spaces, int start, int end, boolean charsValid, char[] chars, CharSequence text, int mstart) {
        int hans = countStretchableHan(0, end, charsValid, chars, text, mstart);
        if (hans != 0) {
            float addedWidth = (justifyWidth - width) / (hans + spaces);
            return addedWidth;
        }
        return 0.0f;
    }

    private int countStretchableHan(int start, int end, boolean charsValid, char[] chars, CharSequence text, int mstart) {
        int count = 0;
        for (int i = start; i < end; i++) {
            char c = charsValid ? chars[i] : text.charAt(i + mstart);
            if (isStretchableHan(c)) {
                count++;
            }
        }
        return count;
    }

    private boolean isStretchableHan(int ch) {
        return ch >= 19968 && ch <= 40869;
    }

    @Override // android.text.ITextJustificationHooks
    public float getLayoutParaSpacingAdded(StaticLayout layout, Object builder, boolean moreChars, CharSequence source, int endPos) {
        layout.hashCode();
        this.mLayoutSpecifiedParaSpacing = false;
        float builderParaSpacingAdded = 0.0f;
        if (moreChars && source.charAt(endPos - 1) == '\n') {
            builderParaSpacingAdded = this.mBuilderParaSpacingAdded;
            if (builderParaSpacingAdded > 0.0f) {
                this.mLayoutSpecifiedParaSpacing = true;
            }
        }
        return builderParaSpacingAdded;
    }

    @Override // android.text.ITextJustificationHooks
    public void setLayoutParaSpacingAdded(Object object, float paraSpacing) {
        this.mBuilderParaSpacingAdded = paraSpacing;
    }

    @Override // android.text.ITextJustificationHooks
    public boolean lineShouldIncludeFontPad(boolean firstLine, StaticLayout layout) {
        return firstLine || this.mLayoutSpecifiedParaSpacing;
    }

    @Override // android.text.ITextJustificationHooks
    public boolean lineNeedMultiply(boolean needMultiply, boolean addLastLineLineSpacing, boolean lastLine, StaticLayout layout) {
        return needMultiply && (addLastLineLineSpacing || !lastLine) && !this.mLayoutSpecifiedParaSpacing;
    }
}
