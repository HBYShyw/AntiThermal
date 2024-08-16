package i3;

import android.content.res.Resources;
import kotlin.Metadata;

/* compiled from: COUIDisplayUtil.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006J\u0010\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0007¨\u0006\u0007"}, d2 = {"Li3/a;", "", "", "dpValue", "a", "<init>", "()V", "coui-support-nearx_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: i3.a, reason: use source file name */
/* loaded from: classes.dex */
public final class COUIDisplayUtil {

    /* renamed from: a, reason: collision with root package name */
    public static final COUIDisplayUtil f12488a = new COUIDisplayUtil();

    /* renamed from: b, reason: collision with root package name */
    private static final float f12489b = Resources.getSystem().getDisplayMetrics().density;

    private COUIDisplayUtil() {
    }

    public static final int a(int dpValue) {
        return (int) ((dpValue * f12489b) + 0.5f);
    }
}
