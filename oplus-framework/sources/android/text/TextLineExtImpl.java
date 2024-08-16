package android.text;

import android.common.OplusFeatureCache;
import android.graphics.Paint;

/* loaded from: classes.dex */
public class TextLineExtImpl implements ITextLineExt {
    private TextLine mTextLine;

    public TextLineExtImpl(Object base) {
        this.mTextLine = (TextLine) base;
    }

    public boolean hookjustify(float justifyWidth, int spaces, int start, int end, boolean charsValid, char[] chars, CharSequence text, int mstart) {
        float width = Math.abs(this.mTextLine.measure(end, false, (Paint.FontMetricsInt) null));
        float addedWidth = ((ITextJustificationHooks) OplusFeatureCache.getOrCreate(ITextJustificationHooks.DEFAULT, new Object[0])).calculateAddedWidth(justifyWidth, width, spaces, 0, end, charsValid, chars, text, mstart);
        if (addedWidth > 0.0f) {
            ITextLineWrapper wrapper = this.mTextLine.getWrapper();
            wrapper.setAddedWidthForJustify(addedWidth);
            wrapper.setIsJustifying(true);
        }
        return true;
    }
}
