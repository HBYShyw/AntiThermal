package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$color;
import androidx.appcompat.R$dimen;
import androidx.appcompat.R$drawable;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.core.graphics.ColorUtils;
import c.AppCompatResources;

/* compiled from: AppCompatDrawableManager.java */
/* renamed from: androidx.appcompat.widget.g, reason: use source file name */
/* loaded from: classes.dex */
public final class AppCompatDrawableManager {

    /* renamed from: b, reason: collision with root package name */
    private static final PorterDuff.Mode f1215b = PorterDuff.Mode.SRC_IN;

    /* renamed from: c, reason: collision with root package name */
    private static AppCompatDrawableManager f1216c;

    /* renamed from: a, reason: collision with root package name */
    private ResourceManagerInternal f1217a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppCompatDrawableManager.java */
    /* renamed from: androidx.appcompat.widget.g$a */
    /* loaded from: classes.dex */
    public class a implements ResourceManagerInternal.c {

        /* renamed from: a, reason: collision with root package name */
        private final int[] f1218a = {R$drawable.abc_textfield_search_default_mtrl_alpha, R$drawable.abc_textfield_default_mtrl_alpha, R$drawable.abc_ab_share_pack_mtrl_alpha};

        /* renamed from: b, reason: collision with root package name */
        private final int[] f1219b = {R$drawable.abc_ic_commit_search_api_mtrl_alpha, R$drawable.abc_seekbar_tick_mark_material, R$drawable.abc_ic_menu_share_mtrl_alpha, R$drawable.abc_ic_menu_copy_mtrl_am_alpha, R$drawable.abc_ic_menu_cut_mtrl_alpha, R$drawable.abc_ic_menu_selectall_mtrl_alpha, R$drawable.abc_ic_menu_paste_mtrl_am_alpha};

        /* renamed from: c, reason: collision with root package name */
        private final int[] f1220c = {R$drawable.abc_textfield_activated_mtrl_alpha, R$drawable.abc_textfield_search_activated_mtrl_alpha, R$drawable.abc_cab_background_top_mtrl_alpha, R$drawable.abc_text_cursor_material, R$drawable.abc_text_select_handle_left_mtrl, R$drawable.abc_text_select_handle_middle_mtrl, R$drawable.abc_text_select_handle_right_mtrl};

        /* renamed from: d, reason: collision with root package name */
        private final int[] f1221d = {R$drawable.abc_popup_background_mtrl_mult, R$drawable.abc_cab_background_internal_bg, R$drawable.abc_menu_hardkey_panel_mtrl_mult};

        /* renamed from: e, reason: collision with root package name */
        private final int[] f1222e = {R$drawable.abc_tab_indicator_material, R$drawable.abc_textfield_search_material};

        /* renamed from: f, reason: collision with root package name */
        private final int[] f1223f = {R$drawable.abc_btn_check_material, R$drawable.abc_btn_radio_material, R$drawable.abc_btn_check_material_anim, R$drawable.abc_btn_radio_material_anim};

        a() {
        }

        private boolean f(int[] iArr, int i10) {
            for (int i11 : iArr) {
                if (i11 == i10) {
                    return true;
                }
            }
            return false;
        }

        private ColorStateList g(Context context) {
            return h(context, 0);
        }

        private ColorStateList h(Context context, int i10) {
            int c10 = ThemeUtils.c(context, R$attr.colorControlHighlight);
            return new ColorStateList(new int[][]{ThemeUtils.f1180b, ThemeUtils.f1183e, ThemeUtils.f1181c, ThemeUtils.f1187i}, new int[]{ThemeUtils.b(context, R$attr.colorButtonNormal), ColorUtils.i(c10, i10), ColorUtils.i(c10, i10), i10});
        }

        private ColorStateList i(Context context) {
            return h(context, ThemeUtils.c(context, R$attr.colorAccent));
        }

        private ColorStateList j(Context context) {
            return h(context, ThemeUtils.c(context, R$attr.colorButtonNormal));
        }

        private ColorStateList k(Context context) {
            int[][] iArr = new int[3];
            int[] iArr2 = new int[3];
            int i10 = R$attr.colorSwitchThumbNormal;
            ColorStateList e10 = ThemeUtils.e(context, i10);
            if (e10 != null && e10.isStateful()) {
                iArr[0] = ThemeUtils.f1180b;
                iArr2[0] = e10.getColorForState(iArr[0], 0);
                iArr[1] = ThemeUtils.f1184f;
                iArr2[1] = ThemeUtils.c(context, R$attr.colorControlActivated);
                iArr[2] = ThemeUtils.f1187i;
                iArr2[2] = e10.getDefaultColor();
            } else {
                iArr[0] = ThemeUtils.f1180b;
                iArr2[0] = ThemeUtils.b(context, i10);
                iArr[1] = ThemeUtils.f1184f;
                iArr2[1] = ThemeUtils.c(context, R$attr.colorControlActivated);
                iArr[2] = ThemeUtils.f1187i;
                iArr2[2] = ThemeUtils.c(context, i10);
            }
            return new ColorStateList(iArr, iArr2);
        }

        private LayerDrawable l(ResourceManagerInternal resourceManagerInternal, Context context, int i10) {
            BitmapDrawable bitmapDrawable;
            BitmapDrawable bitmapDrawable2;
            BitmapDrawable bitmapDrawable3;
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(i10);
            Drawable i11 = resourceManagerInternal.i(context, R$drawable.abc_star_black_48dp);
            Drawable i12 = resourceManagerInternal.i(context, R$drawable.abc_star_half_black_48dp);
            if ((i11 instanceof BitmapDrawable) && i11.getIntrinsicWidth() == dimensionPixelSize && i11.getIntrinsicHeight() == dimensionPixelSize) {
                bitmapDrawable = (BitmapDrawable) i11;
                bitmapDrawable2 = new BitmapDrawable(bitmapDrawable.getBitmap());
            } else {
                Bitmap createBitmap = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                i11.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                i11.draw(canvas);
                bitmapDrawable = new BitmapDrawable(createBitmap);
                bitmapDrawable2 = new BitmapDrawable(createBitmap);
            }
            bitmapDrawable2.setTileModeX(Shader.TileMode.REPEAT);
            if ((i12 instanceof BitmapDrawable) && i12.getIntrinsicWidth() == dimensionPixelSize && i12.getIntrinsicHeight() == dimensionPixelSize) {
                bitmapDrawable3 = (BitmapDrawable) i12;
            } else {
                Bitmap createBitmap2 = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(createBitmap2);
                i12.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                i12.draw(canvas2);
                bitmapDrawable3 = new BitmapDrawable(createBitmap2);
            }
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{bitmapDrawable, bitmapDrawable3, bitmapDrawable2});
            layerDrawable.setId(0, R.id.background);
            layerDrawable.setId(1, R.id.secondaryProgress);
            layerDrawable.setId(2, R.id.progress);
            return layerDrawable;
        }

        private void m(Drawable drawable, int i10, PorterDuff.Mode mode) {
            if (t.a(drawable)) {
                drawable = drawable.mutate();
            }
            if (mode == null) {
                mode = AppCompatDrawableManager.f1215b;
            }
            drawable.setColorFilter(AppCompatDrawableManager.e(i10, mode));
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x0061 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:7:0x0046  */
        @Override // androidx.appcompat.widget.ResourceManagerInternal.c
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean a(Context context, int i10, Drawable drawable) {
            int i11;
            boolean z10;
            PorterDuff.Mode mode = AppCompatDrawableManager.f1215b;
            boolean f10 = f(this.f1218a, i10);
            int i12 = R.attr.colorBackground;
            if (f10) {
                i12 = R$attr.colorControlNormal;
            } else if (f(this.f1220c, i10)) {
                i12 = R$attr.colorControlActivated;
            } else if (f(this.f1221d, i10)) {
                mode = PorterDuff.Mode.MULTIPLY;
            } else {
                if (i10 == R$drawable.abc_list_divider_mtrl_alpha) {
                    i12 = R.attr.colorForeground;
                    i11 = Math.round(40.8f);
                    z10 = true;
                    if (!z10) {
                        return false;
                    }
                    if (t.a(drawable)) {
                        drawable = drawable.mutate();
                    }
                    drawable.setColorFilter(AppCompatDrawableManager.e(ThemeUtils.c(context, i12), mode));
                    if (i11 != -1) {
                        drawable.setAlpha(i11);
                    }
                    return true;
                }
                if (i10 != R$drawable.abc_dialog_material_background) {
                    i11 = -1;
                    z10 = false;
                    i12 = 0;
                    if (!z10) {
                    }
                }
            }
            i11 = -1;
            z10 = true;
            if (!z10) {
            }
        }

        @Override // androidx.appcompat.widget.ResourceManagerInternal.c
        public PorterDuff.Mode b(int i10) {
            if (i10 == R$drawable.abc_switch_thumb_material) {
                return PorterDuff.Mode.MULTIPLY;
            }
            return null;
        }

        @Override // androidx.appcompat.widget.ResourceManagerInternal.c
        public Drawable c(ResourceManagerInternal resourceManagerInternal, Context context, int i10) {
            if (i10 == R$drawable.abc_cab_background_top_material) {
                return new LayerDrawable(new Drawable[]{resourceManagerInternal.i(context, R$drawable.abc_cab_background_internal_bg), resourceManagerInternal.i(context, R$drawable.abc_cab_background_top_mtrl_alpha)});
            }
            if (i10 == R$drawable.abc_ratingbar_material) {
                return l(resourceManagerInternal, context, R$dimen.abc_star_big);
            }
            if (i10 == R$drawable.abc_ratingbar_indicator_material) {
                return l(resourceManagerInternal, context, R$dimen.abc_star_medium);
            }
            if (i10 == R$drawable.abc_ratingbar_small_material) {
                return l(resourceManagerInternal, context, R$dimen.abc_star_small);
            }
            return null;
        }

        @Override // androidx.appcompat.widget.ResourceManagerInternal.c
        public ColorStateList d(Context context, int i10) {
            if (i10 == R$drawable.abc_edit_text_material) {
                return AppCompatResources.a(context, R$color.abc_tint_edittext);
            }
            if (i10 == R$drawable.abc_switch_track_mtrl_alpha) {
                return AppCompatResources.a(context, R$color.abc_tint_switch_track);
            }
            if (i10 == R$drawable.abc_switch_thumb_material) {
                return k(context);
            }
            if (i10 == R$drawable.abc_btn_default_mtrl_shape) {
                return j(context);
            }
            if (i10 == R$drawable.abc_btn_borderless_material) {
                return g(context);
            }
            if (i10 == R$drawable.abc_btn_colored_material) {
                return i(context);
            }
            if (i10 != R$drawable.abc_spinner_mtrl_am_alpha && i10 != R$drawable.abc_spinner_textfield_background_material) {
                if (f(this.f1219b, i10)) {
                    return ThemeUtils.e(context, R$attr.colorControlNormal);
                }
                if (f(this.f1222e, i10)) {
                    return AppCompatResources.a(context, R$color.abc_tint_default);
                }
                if (f(this.f1223f, i10)) {
                    return AppCompatResources.a(context, R$color.abc_tint_btn_checkable);
                }
                if (i10 == R$drawable.abc_seekbar_thumb_material) {
                    return AppCompatResources.a(context, R$color.abc_tint_seek_thumb);
                }
                return null;
            }
            return AppCompatResources.a(context, R$color.abc_tint_spinner);
        }

        @Override // androidx.appcompat.widget.ResourceManagerInternal.c
        public boolean e(Context context, int i10, Drawable drawable) {
            if (i10 == R$drawable.abc_seekbar_track_material) {
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                Drawable findDrawableByLayerId = layerDrawable.findDrawableByLayerId(R.id.background);
                int i11 = R$attr.colorControlNormal;
                m(findDrawableByLayerId, ThemeUtils.c(context, i11), AppCompatDrawableManager.f1215b);
                m(layerDrawable.findDrawableByLayerId(R.id.secondaryProgress), ThemeUtils.c(context, i11), AppCompatDrawableManager.f1215b);
                m(layerDrawable.findDrawableByLayerId(R.id.progress), ThemeUtils.c(context, R$attr.colorControlActivated), AppCompatDrawableManager.f1215b);
                return true;
            }
            if (i10 != R$drawable.abc_ratingbar_material && i10 != R$drawable.abc_ratingbar_indicator_material && i10 != R$drawable.abc_ratingbar_small_material) {
                return false;
            }
            LayerDrawable layerDrawable2 = (LayerDrawable) drawable;
            m(layerDrawable2.findDrawableByLayerId(R.id.background), ThemeUtils.b(context, R$attr.colorControlNormal), AppCompatDrawableManager.f1215b);
            Drawable findDrawableByLayerId2 = layerDrawable2.findDrawableByLayerId(R.id.secondaryProgress);
            int i12 = R$attr.colorControlActivated;
            m(findDrawableByLayerId2, ThemeUtils.c(context, i12), AppCompatDrawableManager.f1215b);
            m(layerDrawable2.findDrawableByLayerId(R.id.progress), ThemeUtils.c(context, i12), AppCompatDrawableManager.f1215b);
            return true;
        }
    }

    public static synchronized AppCompatDrawableManager b() {
        AppCompatDrawableManager appCompatDrawableManager;
        synchronized (AppCompatDrawableManager.class) {
            if (f1216c == null) {
                h();
            }
            appCompatDrawableManager = f1216c;
        }
        return appCompatDrawableManager;
    }

    public static synchronized PorterDuffColorFilter e(int i10, PorterDuff.Mode mode) {
        PorterDuffColorFilter k10;
        synchronized (AppCompatDrawableManager.class) {
            k10 = ResourceManagerInternal.k(i10, mode);
        }
        return k10;
    }

    public static synchronized void h() {
        synchronized (AppCompatDrawableManager.class) {
            if (f1216c == null) {
                AppCompatDrawableManager appCompatDrawableManager = new AppCompatDrawableManager();
                f1216c = appCompatDrawableManager;
                appCompatDrawableManager.f1217a = ResourceManagerInternal.g();
                f1216c.f1217a.t(new a());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void i(Drawable drawable, TintInfo tintInfo, int[] iArr) {
        ResourceManagerInternal.v(drawable, tintInfo, iArr);
    }

    public synchronized Drawable c(Context context, int i10) {
        return this.f1217a.i(context, i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Drawable d(Context context, int i10, boolean z10) {
        return this.f1217a.j(context, i10, z10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ColorStateList f(Context context, int i10) {
        return this.f1217a.l(context, i10);
    }

    public synchronized void g(Context context) {
        this.f1217a.r(context);
    }
}
