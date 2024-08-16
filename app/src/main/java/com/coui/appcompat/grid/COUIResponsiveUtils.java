package com.coui.appcompat.grid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import b2.COUILog;
import com.coui.component.responsiveui.ResponsiveUIModel;
import com.coui.component.responsiveui.layoutgrid.MarginType;
import com.coui.component.responsiveui.unit.Dp;
import com.coui.component.responsiveui.window.WindowWidthSizeClass;
import com.support.appcompat.R$dimen;
import java.util.Arrays;

/* compiled from: COUIResponsiveUtils.java */
/* renamed from: com.coui.appcompat.grid.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIResponsiveUtils {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f6156a;

    /* renamed from: b, reason: collision with root package name */
    private static final Rect f6157b;

    /* renamed from: c, reason: collision with root package name */
    private static final Point f6158c;

    static {
        f6156a = COUILog.f4543b || COUILog.d("COUIResponsiveUtils", 3);
        f6157b = new Rect();
        f6158c = new Point();
    }

    public static void a(ResponsiveUIModel responsiveUIModel, int i10, int i11, boolean z10, float[] fArr) {
        int margin = responsiveUIModel.margin();
        int gutter = responsiveUIModel.gutter();
        int columnCount = responsiveUIModel.columnCount();
        int[] columnWidth = responsiveUIModel.columnWidth();
        int i12 = (columnCount - i10) / 2;
        if (z10) {
            margin -= i11;
        }
        float f10 = margin;
        fArr[1] = f10;
        fArr[0] = f10;
        for (int i13 = 0; i13 < i12; i13++) {
            fArr[0] = fArr[0] + columnWidth[i13];
            fArr[1] = fArr[1] + columnWidth[(columnCount - i13) - 1];
        }
        float f11 = i12 * gutter;
        fArr[0] = fArr[0] + f11;
        fArr[1] = fArr[1] + f11;
    }

    public static float b(float f10, int i10, int i11, int i12, Context context) {
        int i13;
        MarginType marginType = i12 == 1 ? MarginType.MARGIN_SMALL : MarginType.MARGIN_LARGE;
        boolean z10 = i11 == 1 || i11 == 2;
        ResponsiveUIModel chooseMargin = new ResponsiveUIModel(context, (int) f10, 0).chooseMargin(marginType);
        int margin = chooseMargin.margin();
        int columnCount = chooseMargin.columnCount();
        if (f6156a) {
            Log.d("COUIResponsiveUtils", "calculateWidth: responsiveUIProxy.columnCount() = " + chooseMargin.columnCount() + " gridNumber = " + i10 + "\nscreenSize = " + f10);
        }
        int min = Math.min(i10, columnCount);
        float calculateGridWidth = chooseMargin.calculateGridWidth(min);
        if (f6156a) {
            Log.d("COUIResponsiveUtils", "calculateWidth = " + calculateGridWidth + " gridNumber = " + min + " getColumnsCount = " + chooseMargin.columnCount() + " width = " + calculateGridWidth + " margin = " + margin + " screenWidth = " + f10 + "\n columnWidth = " + Arrays.toString(chooseMargin.columnWidth()) + "\n typeFlag = " + i11 + "isAddPadding = " + z10);
        }
        if (!z10) {
            i13 = 0;
        } else if (i11 == 1) {
            i13 = context.getResources().getDimensionPixelOffset(R$dimen.grid_list_special_padding);
        } else {
            i13 = context.getResources().getDimensionPixelOffset(R$dimen.grid_card_special_padding);
        }
        return calculateGridWidth + ((z10 ? i13 : 0) * 2);
    }

    public static float c(ResponsiveUIModel responsiveUIModel, int i10, int i11, boolean z10) {
        float width = responsiveUIModel.width((responsiveUIModel.columnCount() - i10) / 2, (i10 + r0) - 1);
        if (f6156a) {
            Log.d("COUIResponsiveUtils", "calculateWidth: width = " + width);
        }
        if (!z10) {
            i11 = 0;
        }
        return width + (i11 * 2);
    }

    public static int d(Context context, int i10) {
        return (int) ((g(context, i10) ? 40 : 24) * context.getResources().getDisplayMetrics().density);
    }

    public static int e(Activity activity) {
        return activity.getWindowManager().getMaximumWindowMetrics().getBounds().width();
    }

    @Deprecated
    public static boolean f(Context context) {
        return false;
    }

    public static boolean g(Context context, int i10) {
        return WindowWidthSizeClass.INSTANCE.fromWidth(context, i10) == WindowWidthSizeClass.Expanded;
    }

    public static boolean h(Context context, int i10) {
        return WindowWidthSizeClass.INSTANCE.fromWidth(context, i10) == WindowWidthSizeClass.Medium;
    }

    public static boolean i(Context context, int i10) {
        return WindowWidthSizeClass.INSTANCE.fromWidth(context, i10) == WindowWidthSizeClass.Compact;
    }

    public static boolean j(int i10) {
        return WindowWidthSizeClass.INSTANCE.fromWidth(new Dp((float) i10)) == WindowWidthSizeClass.Compact;
    }

    public static void k(Context context, View view, int i10, int i11, int i12, int i13, int i14) {
        if (i13 != 0) {
            if (i14 == 0) {
                int size = (View.MeasureSpec.getSize(i10) - ((int) b(View.MeasureSpec.getSize(i10), i13, i11, i12, context))) / 2;
                if (view.getPaddingLeft() == size && view.getPaddingRight() == size) {
                    return;
                }
                view.setPaddingRelative(size, view.getPaddingTop(), size, view.getPaddingBottom());
                return;
            }
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = (int) b(View.MeasureSpec.getSize(i10), i13, i11, i12, context);
            view.setLayoutParams(layoutParams);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:95:0x02eb, code lost:
    
        if (r18.getPaddingRight() != r5[1]) goto L119;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0302  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0313 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x02cc  */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v34 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int l(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, boolean z10, boolean z11) {
        int i18;
        int i19;
        ResponsiveUIModel responsiveUIModel;
        int i20;
        int i21;
        String str;
        boolean z12;
        ?? r12;
        int i22;
        int i23;
        char c10;
        MarginType marginType = i13 == 1 ? MarginType.MARGIN_SMALL : MarginType.MARGIN_LARGE;
        Rect rect = f6157b;
        view.getWindowVisibleDisplayFrame(rect);
        boolean z13 = i12 == 1 || i12 == 2;
        int max = Math.max(0, rect.width());
        int i24 = i17 <= 0 ? max : i17;
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        if (size > 0 && (mode == 1073741824 || mode == Integer.MIN_VALUE)) {
            if (f6156a) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("\npaddingFlag = ");
                sb2.append(i13 == 0 ? "large margin" : "small margin");
                sb2.append("\n isAddPadding = ");
                sb2.append(z13);
                sb2.append("\n typeFlag = ");
                sb2.append(i12);
                sb2.append("\n window width = ");
                sb2.append(max);
                sb2.append("\n screen width = ");
                sb2.append(i24);
                sb2.append("\n parent width = ");
                sb2.append(size);
                sb2.append("\n widthMode = ");
                sb2.append(mode);
                sb2.append("\n widthSize = ");
                sb2.append(size);
                sb2.append("\n gridNumber = ");
                i18 = i11;
                sb2.append(i18);
                Log.d("COUIResponsiveUtils", sb2.toString());
            } else {
                i18 = i11;
            }
            boolean z14 = z10 || z11;
            if (z14 && g(view.getContext(), i24)) {
                responsiveUIModel = new ResponsiveUIModel(view.getContext(), i24, 0).chooseMargin(marginType);
                i19 = 8;
            } else {
                ResponsiveUIModel chooseMargin = new ResponsiveUIModel(view.getContext(), max, 0).chooseMargin(marginType);
                i19 = i18;
                responsiveUIModel = chooseMargin;
            }
            int columnCount = responsiveUIModel.columnCount();
            int d10 = (z14 && i12 == 0) ? d(view.getContext(), size) : responsiveUIModel.margin();
            int min = Math.min(i19, columnCount);
            if (!z13) {
                i20 = 0;
            } else if (i12 == 1) {
                i20 = view.getContext().getResources().getDimensionPixelOffset(R$dimen.grid_list_special_padding);
            } else {
                i20 = view.getContext().getResources().getDimensionPixelOffset(R$dimen.grid_card_special_padding);
            }
            if (f6156a) {
                Log.d("COUIResponsiveUtils", "\nisParentChildHierarchy = " + z10 + "\n isActivityEmbedded = " + z11 + "\n isInPCMode = " + z14 + "\n isLargeScreen = " + g(view.getContext(), i24) + "\n columnCount = " + columnCount + "\n margin = " + d10 + "\n grid number = " + min + "\n special padding = " + i20);
            }
            float c11 = c(responsiveUIModel, min, i20, z13);
            float[] fArr = new float[2];
            a(responsiveUIModel, min, i20, z13, fArr);
            if (f6156a) {
                StringBuilder sb3 = new StringBuilder();
                i21 = i20;
                sb3.append("\nBefore verify, contentWidth = ");
                sb3.append(c11);
                sb3.append("\n padding left = ");
                str = "\n padding left = ";
                sb3.append(fArr[0]);
                sb3.append(" padding right = ");
                sb3.append(fArr[1]);
                Log.d("COUIResponsiveUtils", sb3.toString());
            } else {
                i21 = i20;
                str = "\n padding left = ";
            }
            int i25 = d10 * 2;
            float f10 = size - i25;
            if (c11 > f10 || (z14 && i12 == 0)) {
                if (f6156a) {
                    Log.d("COUIResponsiveUtils", "measureLayoutWithPercent: " + size + " " + i25);
                }
                c11 = f10;
            }
            int i26 = (columnCount - min) / 2;
            if (z14 && i12 == 2) {
                int width = responsiveUIModel.width(i26, (i26 + min) - 1);
                if (g(view.getContext(), i24)) {
                    d10 = i21;
                }
                if (width + (d10 * 2) > size) {
                    fArr[1] = 0.0f;
                    z12 = false;
                    fArr[0] = 0.0f;
                } else {
                    z12 = false;
                    float f11 = (size - r1) / 2.0f;
                    fArr[1] = f11;
                    fArr[0] = f11;
                }
            } else {
                z12 = false;
                if (!z14 || i12 != 0) {
                    if (fArr[0] + fArr[1] + responsiveUIModel.width(i26, (i26 + min) - 1) > size) {
                        if (z13) {
                            d10 -= i21;
                        }
                        float f12 = d10;
                        fArr[1] = f12;
                        r12 = 0;
                        fArr[0] = f12;
                    } else {
                        r12 = 0;
                    }
                    if (f6156a) {
                        Log.d("COUIResponsiveUtils", "\nAfter verify, contentWidth = " + c11 + str + fArr[r12] + " padding right = " + fArr[1] + "\n grid position from " + i26 + " to " + ((i26 + min) - 1));
                    }
                    if (min > 0 || max <= 0 || size > max) {
                        i22 = i14;
                        if (i22 == 0) {
                            if (f6156a) {
                                Log.d("COUIResponsiveUtils", "Exception Padding mode");
                            }
                            if (view.getPaddingLeft() == fArr[0]) {
                                i23 = 1;
                            } else {
                                i23 = 1;
                            }
                            view.setPadding(i15, view.getPaddingTop(), i16, view.getPaddingBottom());
                        }
                        i23 = 1;
                    } else {
                        i22 = i14;
                        if (i22 == 0) {
                            if (f6156a) {
                                Log.d("COUIResponsiveUtils", "Padding mode");
                            }
                            if (view.getPaddingLeft() == fArr[0]) {
                                c10 = 1;
                                if (view.getPaddingRight() == fArr[1]) {
                                    i23 = 1;
                                }
                            } else {
                                c10 = 1;
                            }
                            view.setPadding((int) fArr[0], view.getPaddingTop(), (int) fArr[c10], view.getPaddingBottom());
                        }
                        i23 = 1;
                    }
                    if (i22 == i23) {
                        return i10;
                    }
                    if (f6156a) {
                        Log.d("COUIResponsiveUtils", "Remeasure mode");
                    }
                    return View.MeasureSpec.makeMeasureSpec((int) c11, 1073741824);
                }
                float f13 = d10;
                fArr[1] = f13;
                fArr[0] = f13;
            }
            r12 = z12;
            if (f6156a) {
            }
            if (min > 0) {
            }
            i22 = i14;
            if (i22 == 0) {
            }
            i23 = 1;
            if (i22 == i23) {
            }
        } else {
            if (f6156a) {
                Log.d("COUIResponsiveUtils", "Skip measure because of parent measure unspecific: widthSize = " + size + "widthMode = " + mode);
            }
            return i10;
        }
    }
}
