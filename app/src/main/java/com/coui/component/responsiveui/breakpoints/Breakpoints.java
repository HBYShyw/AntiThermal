package com.coui.component.responsiveui.breakpoints;

import com.coui.component.responsiveui.unit.Dp;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import kotlin.Metadata;

/* compiled from: Breakpoints.kt */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\b\u001a\u00020\tH\u0016R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lcom/coui/component/responsiveui/breakpoints/Breakpoints;", "", "()V", "BP_EXPANDED_WINDOW_BASE_HEIGHT", "Lcom/coui/component/responsiveui/unit/Dp;", "BP_EXPANDED_WINDOW_BASE_WIDTH", "BP_MEDIUM_WINDOW_BASE_HEIGHT", "BP_MEDIUM_WINDOW_BASE_WIDTH", "toString", "", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
/* loaded from: classes.dex */
public final class Breakpoints {
    public static final Breakpoints INSTANCE = new Breakpoints();
    public static final Dp BP_MEDIUM_WINDOW_BASE_WIDTH = new Dp(DataLinkConstants.THUMBNAIL_HEALTH);
    public static final Dp BP_EXPANDED_WINDOW_BASE_WIDTH = new Dp(840);
    public static final Dp BP_MEDIUM_WINDOW_BASE_HEIGHT = new Dp(480);
    public static final Dp BP_EXPANDED_WINDOW_BASE_HEIGHT = new Dp(900);

    private Breakpoints() {
    }

    public String toString() {
        return "BreakPoints Base-Width (" + BP_MEDIUM_WINDOW_BASE_WIDTH + ", " + BP_EXPANDED_WINDOW_BASE_WIDTH + "), Base-Height (" + BP_MEDIUM_WINDOW_BASE_HEIGHT + ", " + BP_EXPANDED_WINDOW_BASE_HEIGHT + ')';
    }
}
