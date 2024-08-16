package android.widget;

import android.common.OplusFeatureCache;
import android.content.Context;
import android.graphics.ITypefaceExt;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.oplus.font.IOplusFontManager;

/* loaded from: classes.dex */
public class TextViewExtImpl implements ITextViewExt {
    private IOplusFontManager mFontManager = (IOplusFontManager) OplusFeatureCache.getOrCreate(IOplusFontManager.DEFAULT, new Object[0]);
    private IOplusCustomizeTextViewFeature mOplusCustomizeTextViewFeature = (IOplusCustomizeTextViewFeature) OplusFeatureCache.getOrCreate(IOplusCustomizeTextViewFeature.DEFAULT, new Object[0]);

    public TextViewExtImpl(Object base) {
    }

    public void init(Context context) {
        this.mOplusCustomizeTextViewFeature.init(context);
    }

    public boolean getClipboardStatus() {
        return this.mOplusCustomizeTextViewFeature.getClipboardStatus();
    }

    public int getTypefaceIndex(int originIndex, int oplusIndex) {
        return this.mFontManager.getTypefaceIndex(originIndex, oplusIndex);
    }

    public void replaceFakeBoldToMedium(TextView textView, Typeface typeface, int style) {
        this.mFontManager.replaceFakeBoldToMedium(textView, typeface == null ? ITypefaceExt.DEFAULT : typeface.mTypefaceExt, style);
    }

    public Typeface flipTypeface(Typeface typeface, Paint paint) {
        return this.mFontManager.flipTypeface(typeface == null ? ITypefaceExt.DEFAULT : typeface.mTypefaceExt, paint);
    }
}
