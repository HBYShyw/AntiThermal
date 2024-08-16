package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.SystemProperties;
import android.text.Layout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import com.oplus.resolver.OplusResolverUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class OplusTextViewRTLUtilForUG implements IOplusTextViewRTLUtilForUG {
    private static volatile OplusTextViewRTLUtilForUG sInstance = null;
    private Locale mLastUpdateLocale;
    public boolean hasInit = false;
    public boolean mSupportRtl = true;
    public boolean mForceAnyRtl = false;
    public boolean mForceViewStart = false;

    public static OplusTextViewRTLUtilForUG getInstance() {
        if (sInstance == null) {
            synchronized (OplusTextViewRTLUtilForUG.class) {
                if (sInstance == null) {
                    sInstance = new OplusTextViewRTLUtilForUG();
                }
            }
        }
        return sInstance;
    }

    OplusTextViewRTLUtilForUG() {
        this.mLastUpdateLocale = Locale.ENGLISH;
        this.mLastUpdateLocale = Locale.getDefault();
    }

    @Override // android.widget.IOplusTextViewRTLUtilForUG
    public void initRtlParameter(Resources res) {
        String[] locales;
        if (!this.hasInit && res != null && res.getConfiguration() != null && res.getConfiguration().locale != null) {
            String sysLocale = SystemProperties.get("persist.sys.locale", "zh_CN");
            String appLocale = res.getConfiguration().locale.toString();
            if (sysLocale != null && sysLocale.equalsIgnoreCase(OplusResolverUtils.UYGHUR_LAUGUAGE)) {
                this.mForceAnyRtl = true;
                if (appLocale != null && appLocale.equalsIgnoreCase("ug_CN") && (locales = res.getAssets().getNonSystemLocales()) != null) {
                    if (locales.length > 0) {
                        List<String> localeArrays = Arrays.asList(locales);
                        if (localeArrays.contains(OplusResolverUtils.UYGHUR_LAUGUAGE)) {
                            this.mForceViewStart = true;
                        } else {
                            this.mSupportRtl = false;
                        }
                    } else {
                        this.mSupportRtl = false;
                    }
                }
            }
            this.hasInit = true;
        }
    }

    @Override // android.widget.IOplusTextViewRTLUtilForUG
    public boolean getOplusSupportRtl() {
        return this.mSupportRtl;
    }

    @Override // android.widget.IOplusTextViewRTLUtilForUG
    public boolean getDirectionAnyRtl() {
        return this.mForceAnyRtl;
    }

    @Override // android.widget.IOplusTextViewRTLUtilForUG
    public boolean getTextViewStart() {
        return this.mForceViewStart;
    }

    @Override // android.widget.IOplusTextViewRTLUtilForUG
    public Layout.Alignment getLayoutAlignmentForTextView(Layout.Alignment alignment, Context context, TextView textView) {
        boolean forceViewStart = getTextViewStart();
        switch (textView.getTextAlignment()) {
            case 1:
                switch (textView.getGravity() & 8388615) {
                    case 1:
                        Layout.Alignment alignment2 = Layout.Alignment.ALIGN_CENTER;
                        return alignment2;
                    case 3:
                        Layout.Alignment alignment3 = Layout.Alignment.ALIGN_LEFT;
                        return alignment3;
                    case 5:
                        Layout.Alignment alignment4 = Layout.Alignment.ALIGN_RIGHT;
                        return alignment4;
                    case 8388611:
                        Layout.Alignment alignment5 = forceViewStart ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_NORMAL;
                        return alignment5;
                    case 8388613:
                        Layout.Alignment alignment6 = forceViewStart ? Layout.Alignment.ALIGN_LEFT : Layout.Alignment.ALIGN_OPPOSITE;
                        return alignment6;
                    default:
                        Layout.Alignment alignment7 = forceViewStart ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_NORMAL;
                        return alignment7;
                }
            case 2:
                Layout.Alignment alignment8 = Layout.Alignment.ALIGN_NORMAL;
                return alignment8;
            case 3:
                Layout.Alignment alignment9 = Layout.Alignment.ALIGN_OPPOSITE;
                return alignment9;
            case 4:
                Layout.Alignment alignment10 = Layout.Alignment.ALIGN_CENTER;
                return alignment10;
            case 5:
                Layout.Alignment alignment11 = textView.getLayoutDirection() == 1 ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_LEFT;
                return alignment11;
            case 6:
                Layout.Alignment alignment12 = textView.getLayoutDirection() == 1 ? Layout.Alignment.ALIGN_LEFT : Layout.Alignment.ALIGN_RIGHT;
                return alignment12;
            default:
                Layout.Alignment alignment13 = forceViewStart ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_NORMAL;
                return alignment13;
        }
    }

    @Override // android.widget.IOplusTextViewRTLUtilForUG
    public TextDirectionHeuristic getTextDirectionHeuristicForTextView(boolean defaultIsRtl) {
        return getDirectionAnyRtl() ? TextDirectionHeuristics.ANYRTL_LTR : defaultIsRtl ? TextDirectionHeuristics.FIRSTSTRONG_RTL : TextDirectionHeuristics.FIRSTSTRONG_LTR;
    }

    @Override // android.widget.IOplusTextViewRTLUtilForUG
    public boolean hasRtlSupportForView(Context context) {
        return getOplusSupportRtl() && context.getApplicationInfo().hasRtlSupport();
    }

    @Override // android.widget.IOplusTextViewRTLUtilForUG
    public void updateRtlParameterForUG(Resources res, Configuration newConfig) {
        if (res != null && res.getAssets() != null) {
            updateRtlParameterForUG(res.getAssets().getNonSystemLocales(), newConfig);
        }
    }

    @Override // android.widget.IOplusTextViewRTLUtilForUG
    public void updateRtlParameterForUG(String[] availableLocales, Configuration newConfig) {
        if (this.hasInit && availableLocales != null && newConfig != null && newConfig.locale != null && !newConfig.locale.equals(this.mLastUpdateLocale)) {
            this.mForceAnyRtl = false;
            this.mForceViewStart = false;
            this.mSupportRtl = true;
            String newLocale = newConfig.locale.toLanguageTag();
            if (newLocale != null && newLocale.equalsIgnoreCase(OplusResolverUtils.UYGHUR_LAUGUAGE)) {
                this.mForceAnyRtl = true;
                if (availableLocales != null) {
                    if (availableLocales.length > 0) {
                        List<String> localeArrays = Arrays.asList(availableLocales);
                        if (localeArrays.contains(OplusResolverUtils.UYGHUR_LAUGUAGE)) {
                            this.mForceViewStart = true;
                        } else {
                            this.mSupportRtl = false;
                        }
                    } else {
                        this.mSupportRtl = false;
                    }
                }
            }
            this.mLastUpdateLocale = newConfig.locale;
            this.hasInit = true;
        }
    }
}
