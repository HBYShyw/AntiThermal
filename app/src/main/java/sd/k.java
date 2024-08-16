package sd;

import fb.PrimitiveRanges;
import fb._Ranges;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

/* compiled from: Regex.kt */
/* loaded from: classes2.dex */
public final class k {
    /* JADX INFO: Access modifiers changed from: private */
    public static final h c(Matcher matcher, CharSequence charSequence) {
        if (matcher.matches()) {
            return new i(matcher, charSequence);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final PrimitiveRanges d(MatchResult matchResult, int i10) {
        PrimitiveRanges k10;
        k10 = _Ranges.k(matchResult.start(i10), matchResult.end(i10));
        return k10;
    }
}
