package android.graphics;

import android.common.OplusFeatureCache;
import android.graphics.fonts.FontFamily;
import com.oplus.font.IOplusFontManager;
import com.oplus.util.OplusFontUtils;
import java.util.Map;

/* loaded from: classes.dex */
public class TypefaceExtImpl implements ITypefaceExt {
    public boolean mIsSystemFont = false;
    private Typeface mTypeface;

    public TypefaceExtImpl(Typeface typeface) {
        this.mTypeface = Typeface.DEFAULT;
        this.mTypeface = typeface;
    }

    public Typeface getTypeface() {
        return this.mTypeface;
    }

    public void setSystemTypeface(boolean isSystemFont) {
        this.mIsSystemFont = isSystemFont;
    }

    public boolean isSystemTypeface() {
        return this.mIsSystemFont;
    }

    public void initFontsForserializeFontMap() {
        ((IOplusFontManager) OplusFeatureCache.getOrCreate(IOplusFontManager.DEFAULT, new Object[0])).initFontsForserializeFontMap();
    }

    public void createIndividualTypefae() {
        OplusFontUtils.createIndividualTypefae();
    }

    public boolean createTypefaceForCustom(Map<String, Typeface> outSystemFontMap, Map.Entry<String, FontFamily[]> entry) {
        return OplusFontUtils.createTypefaceForCustom(outSystemFontMap, entry);
    }

    public Typeface replaceSysSans(Typeface typeface, String familyName) {
        return OplusFontUtils.replaceSysSans(typeface, familyName);
    }
}
