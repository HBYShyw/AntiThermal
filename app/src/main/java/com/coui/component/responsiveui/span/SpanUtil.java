package com.coui.component.responsiveui.span;

import com.coui.component.responsiveui.unit.Dp;
import fb._Ranges;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: SpanUtil.kt */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lcom/coui/component/responsiveui/span/SpanUtil;", "", "()V", "Companion", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
/* loaded from: classes.dex */
public final class SpanUtil {
    public static final int DEFAULT_COLUMNS_PER_SPAN = 4;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* renamed from: a, reason: collision with root package name */
    private static final Dp f8195a = new Dp(360);

    private SpanUtil() {
    }

    /* compiled from: SpanUtil.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\r\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\bJ(\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00042\b\b\u0002\u0010\u0014\u001a\u00020\bJ\u0016\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\bR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lcom/coui/component/responsiveui/span/SpanUtil$Companion;", "", "()V", "DEFAULT_BASE_WIDTH", "Lcom/coui/component/responsiveui/unit/Dp;", "getDEFAULT_BASE_WIDTH", "()Lcom/coui/component/responsiveui/unit/Dp;", "DEFAULT_COLUMNS_PER_SPAN", "", "calculateGapBetweenSpans", "", "windowWidth", "spanCounts", "spanWidth", "margin", "minGap", "calculateSpanCount", "baseWidth", "spanCountPerBaseWidth", "layoutGridWindowWidth", "minSpanCount", "totalColumns", "columnsPerSpan", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ float calculateGapBetweenSpans$default(Companion companion, int i10, int i11, int i12, int i13, int i14, int i15, Object obj) {
            if ((i15 & 16) != 0) {
                i14 = 1;
            }
            return companion.calculateGapBetweenSpans(i10, i11, i12, i13, i14);
        }

        public static /* synthetic */ int calculateSpanCount$default(Companion companion, Dp dp, int i10, Dp dp2, int i11, int i12, Object obj) {
            if ((i12 & 8) != 0) {
                i11 = i10;
            }
            return companion.calculateSpanCount(dp, i10, dp2, i11);
        }

        public final float calculateGapBetweenSpans(int windowWidth, int spanCounts, int spanWidth, int margin, int minGap) {
            float b10;
            if (!(spanCounts > 1)) {
                throw new IllegalArgumentException("spanCounts must be greater than 1");
            }
            if (minGap >= 0) {
                b10 = _Ranges.b(((windowWidth - (margin * 2)) - (spanWidth * spanCounts)) / (spanCounts - 1.0f), minGap);
                return b10;
            }
            throw new IllegalArgumentException("minGap must be equal or greater than 0");
        }

        public final int calculateSpanCount(int totalColumns, int columnsPerSpan) {
            if (!(totalColumns > 0)) {
                throw new IllegalArgumentException("totalColumns must be positive".toString());
            }
            if (!(columnsPerSpan > 0)) {
                throw new IllegalArgumentException("columnsPerSpan must be positive".toString());
            }
            if (columnsPerSpan <= totalColumns) {
                return totalColumns / columnsPerSpan;
            }
            throw new IllegalArgumentException("totalColumns must be equal or greater than columnsPerSpan".toString());
        }

        public final Dp getDEFAULT_BASE_WIDTH() {
            return SpanUtil.f8195a;
        }

        public final int calculateSpanCount(Dp baseWidth, int spanCountPerBaseWidth, Dp layoutGridWindowWidth, int minSpanCount) {
            int c10;
            k.e(baseWidth, "baseWidth");
            k.e(layoutGridWindowWidth, "layoutGridWindowWidth");
            if (spanCountPerBaseWidth >= 1) {
                c10 = _Ranges.c((int) (layoutGridWindowWidth.div(baseWidth).getCom.oplus.thermalcontrol.config.ThermalBaseConfig.Item.ATTR_VALUE java.lang.String() * spanCountPerBaseWidth), minSpanCount);
                return c10;
            }
            throw new IllegalArgumentException("spanCountPerBaseWidth must be equal or greater than 1".toString());
        }
    }
}
