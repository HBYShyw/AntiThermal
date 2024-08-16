package n1;

import androidx.viewpager2.widget.ViewPager2;
import com.coui.appcompat.banner.COUIBanner;
import com.support.nearx.R$id;
import java.lang.ref.WeakReference;

/* compiled from: COUIBannerOnPageChangeCallback.java */
/* renamed from: n1.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIBannerOnPageChangeCallback extends ViewPager2.i {

    /* renamed from: a, reason: collision with root package name */
    private WeakReference<COUIBanner> f15627a;

    /* renamed from: b, reason: collision with root package name */
    private int f15628b = -1;

    /* renamed from: c, reason: collision with root package name */
    private boolean f15629c;

    public COUIBannerOnPageChangeCallback(COUIBanner cOUIBanner) {
        this.f15627a = new WeakReference<>(cOUIBanner);
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void a(int i10) {
        if (this.f15627a.get() == null) {
            return;
        }
        COUIBanner cOUIBanner = this.f15627a.get();
        if (i10 == 1 || i10 == 2) {
            this.f15629c = true;
        } else if (i10 == 0) {
            this.f15629c = false;
            if (this.f15628b != -1 && cOUIBanner.I()) {
                int i11 = this.f15628b;
                if (i11 == 0) {
                    cOUIBanner.O(cOUIBanner.getRealCount(), false);
                } else if (i11 == cOUIBanner.getItemCount() - 1) {
                    cOUIBanner.O(1, false);
                }
            }
        }
        if (cOUIBanner.getOnPageChangeCallback() != null) {
            cOUIBanner.getOnPageChangeCallback().a(i10);
        }
        if (cOUIBanner.getIndicator() != null) {
            if (((ViewPager2) cOUIBanner.findViewById(R$id.viewpager)).f() && i10 == 1) {
                i10 = 2;
            }
            cOUIBanner.getIndicator().y(i10);
        }
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void b(int i10, float f10, int i11) {
        if (this.f15627a.get() == null) {
            return;
        }
        COUIBanner cOUIBanner = this.f15627a.get();
        int a10 = COUIBannerUtil.a(cOUIBanner.I(), i10, cOUIBanner.getRealCount());
        if (cOUIBanner.getOnPageChangeCallback() != null && a10 == cOUIBanner.getCurrentItem() - 1) {
            cOUIBanner.getOnPageChangeCallback().b(a10, f10, i11);
        }
        cOUIBanner.getIndicator().z(a10, f10, i11);
        if (a10 == 0 && cOUIBanner.getCurrentItem() == 1 && f10 == 0.0f) {
            cOUIBanner.getIndicator().setCurrentPosition(a10);
        } else if (a10 == cOUIBanner.getRealCount() - 1 && f10 == 0.0f) {
            cOUIBanner.getIndicator().setCurrentPosition(a10);
        }
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void c(int i10) {
        if (this.f15627a.get() == null) {
            return;
        }
        COUIBanner cOUIBanner = this.f15627a.get();
        if (this.f15629c) {
            this.f15628b = i10;
            int a10 = COUIBannerUtil.a(cOUIBanner.I(), i10, cOUIBanner.getRealCount());
            if (cOUIBanner.getOnPageChangeCallback() != null) {
                cOUIBanner.getOnPageChangeCallback().c(a10);
            }
            if (cOUIBanner.getIndicator() != null) {
                cOUIBanner.getIndicator().A(a10);
            }
        }
    }
}
