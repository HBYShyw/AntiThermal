package com.coui.appcompat.banner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.coui.appcompat.banner.COUIPageIndicatorKit;
import com.support.control.R$attr;
import com.support.control.R$dimen;
import com.support.control.R$drawable;
import com.support.control.R$id;
import com.support.control.R$layout;
import com.support.control.R$styleable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import kotlin.Metadata;
import ma.h;
import ma.j;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.k;

/* compiled from: COUIPageIndicatorKit.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b.\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\bH\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0016\u0018\u0000 º\u00012\u00020\u0001:\u0005»\u0001¼\u00015B*\b\u0007\u0012\b\u0010´\u0001\u001a\u00030³\u0001\u0012\b\u0010¶\u0001\u001a\u00030µ\u0001\u0012\t\b\u0002\u0010·\u0001\u001a\u00020\u0002¢\u0006\u0006\b¸\u0001\u0010¹\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J \u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u0002H\u0002J\u0018\u0010\r\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u0002H\u0002J\u0010\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u0002H\u0002J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u0002H\u0002J\b\u0010\u0011\u001a\u00020\u0004H\u0002J\b\u0010\u0012\u001a\u00020\u0004H\u0002J\b\u0010\u0013\u001a\u00020\u0004H\u0002J\b\u0010\u0014\u001a\u00020\u0004H\u0002J\u0010\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u0006H\u0002J0\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\u0006H\u0002J\u0018\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u0018H\u0002J\u0010\u0010 \u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0006H\u0002J\u0010\u0010#\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020!H\u0014J\u0006\u0010$\u001a\u00020\u0004J\u0018\u0010'\u001a\u00020\u00042\u0006\u0010%\u001a\u00020\u00022\u0006\u0010&\u001a\u00020\u0002H\u0014J\u000e\u0010(\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u0002J\u000e\u0010)\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010*\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u0002J\u000e\u0010+\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u0002J\u000e\u0010.\u001a\u00020\u00042\u0006\u0010-\u001a\u00020,J\u001e\u00101\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010/\u001a\u00020\u00182\u0006\u00100\u001a\u00020\u0002J\u000e\u00102\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u00104\u001a\u00020\u00042\u0006\u00103\u001a\u00020\u0002R\u0016\u00106\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b5\u0010\u0015R\u0016\u00108\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b7\u0010\u0015R\u0016\u0010:\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b9\u0010\u0015R\u0016\u0010<\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b;\u0010\u0015R\u0016\u0010>\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b=\u0010\u0015R\u0016\u0010@\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b?\u0010\u0015R\u0016\u0010C\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bA\u0010BR\u0016\u0010E\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bD\u0010BR\u0016\u0010G\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bF\u0010\u0015R\u0016\u0010I\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bH\u0010\u0015R\u0016\u0010K\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bJ\u0010\u0015R\u0016\u0010M\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bL\u0010\u0015R\u0016\u0010O\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bN\u0010\u0005R\u0016\u0010P\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0010\u0010\u0005R\u0016\u0010R\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bQ\u0010\u0005R\u0016\u0010S\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\r\u0010\u0005R\u0016\u0010T\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001f\u0010BR\u0016\u0010U\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001d\u0010BR\u0016\u0010V\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b \u0010BR\u0016\u0010X\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bW\u0010BR\u0016\u0010Y\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b4\u0010BR\u0016\u0010Z\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b1\u0010BR\u0014\u0010]\u001a\u00020[8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b2\u0010\\R\u001c\u0010`\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010^8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0013\u0010_R\u0014\u0010c\u001a\u00020a8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000f\u0010bR\u0014\u0010f\u001a\u00020d8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0014\u0010eR\u0016\u0010i\u001a\u0004\u0018\u00010g8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000b\u0010hR\u0014\u0010l\u001a\u00020j8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010kR\u0016\u0010m\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0012\u0010\u0015R\u0018\u0010-\u001a\u0004\u0018\u00010,8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b$\u0010nR\u0014\u0010o\u001a\u00020\u00028\u0002X\u0082D¢\u0006\u0006\n\u0004\b\u0015\u0010\u0015R\u0014\u0010p\u001a\u00020\u00028\u0002X\u0082D¢\u0006\u0006\n\u0004\b\u0011\u0010\u0015R\u0014\u0010q\u001a\u00020\u00028\u0002X\u0082D¢\u0006\u0006\n\u0004\b\u0017\u0010\u0015R\u0014\u0010s\u001a\u00020\u00188\u0002X\u0082D¢\u0006\u0006\n\u0004\br\u0010\u0005R\u0014\u0010u\u001a\u00020\u00188\u0002X\u0082D¢\u0006\u0006\n\u0004\bt\u0010\u0005R\u0014\u0010w\u001a\u00020\u00188\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bv\u0010\u0005R\u0014\u0010y\u001a\u00020\u00188\u0002X\u0082D¢\u0006\u0006\n\u0004\bx\u0010\u0005R\u0014\u0010{\u001a\u00020\u00188\u0002X\u0082D¢\u0006\u0006\n\u0004\bz\u0010\u0005R\u0014\u0010}\u001a\u00020\u00188\u0002X\u0082D¢\u0006\u0006\n\u0004\b|\u0010\u0005R\u0014\u0010\u007f\u001a\u00020\u00188\u0002X\u0082D¢\u0006\u0006\n\u0004\b~\u0010\u0005R\u0016\u0010\u0081\u0001\u001a\u00020\u00188\u0002X\u0082D¢\u0006\u0007\n\u0005\b\u0080\u0001\u0010\u0005R\u0016\u0010\u0083\u0001\u001a\u00020\u00188\u0002X\u0082D¢\u0006\u0007\n\u0005\b\u0082\u0001\u0010\u0005R\u0016\u0010\u0085\u0001\u001a\u00020\u00188\u0002X\u0082\u0004¢\u0006\u0007\n\u0005\b\u0084\u0001\u0010\u0005R\u0016\u0010\u0087\u0001\u001a\u00020\u00188\u0002X\u0082\u0004¢\u0006\u0007\n\u0005\b\u0086\u0001\u0010\u0005R\u0016\u0010\u0089\u0001\u001a\u00020\u00188\u0002X\u0082D¢\u0006\u0007\n\u0005\b\u0088\u0001\u0010\u0005R\u0016\u0010\u008b\u0001\u001a\u00020\u00188\u0002X\u0082\u0004¢\u0006\u0007\n\u0005\b\u008a\u0001\u0010\u0005R\u0016\u0010\u008d\u0001\u001a\u00020\u00188\u0002X\u0082\u0004¢\u0006\u0007\n\u0005\b\u008c\u0001\u0010\u0005R\u0016\u0010\u008f\u0001\u001a\u00020\u00188\u0002X\u0082\u0004¢\u0006\u0007\n\u0005\b\u008e\u0001\u0010\u0005R\u0016\u0010\u0091\u0001\u001a\u00020\u00188\u0002X\u0082\u0004¢\u0006\u0007\n\u0005\b\u0090\u0001\u0010\u0005R\u0016\u0010\u0093\u0001\u001a\u00020\u00188\u0002X\u0082D¢\u0006\u0007\n\u0005\b\u0092\u0001\u0010\u0005R\u0018\u0010\u0095\u0001\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b\u0094\u0001\u0010\u0015R\u0018\u0010\u0097\u0001\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b\u0096\u0001\u0010\u0015R\u0018\u0010\u0099\u0001\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b\u0098\u0001\u0010\u0005R\u0018\u0010\u009b\u0001\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b\u009a\u0001\u0010\u0005R\u0018\u0010\u009d\u0001\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b\u009c\u0001\u0010\u0005R\u0018\u0010\u009f\u0001\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b\u009e\u0001\u0010\u0005R\u0018\u0010¡\u0001\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b \u0001\u0010\u0005R\u0018\u0010£\u0001\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b¢\u0001\u0010\u0005R\u0018\u0010¥\u0001\u001a\u00020\u00188\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b¤\u0001\u0010\u0005R\u0018\u0010§\u0001\u001a\u00020d8\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b¦\u0001\u0010eR\u0018\u0010©\u0001\u001a\u00020d8\u0002@\u0002X\u0082\u000e¢\u0006\u0007\n\u0005\b¨\u0001\u0010eR\u0019\u0010¬\u0001\u001a\u00020\u001c8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bª\u0001\u0010«\u0001R\u0019\u0010®\u0001\u001a\u00020\u001c8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u00ad\u0001\u0010«\u0001R\u001f\u0010²\u0001\u001a\u00020\u00068BX\u0082\u0084\u0002¢\u0006\u000f\n\u0006\b¯\u0001\u0010°\u0001\u001a\u0005\bW\u0010±\u0001¨\u0006½\u0001"}, d2 = {"Lcom/coui/appcompat/banner/COUIPageIndicatorKit;", "Landroid/widget/FrameLayout;", "", "position", "Lma/f0;", "F", "", "stroke", "Landroid/widget/ImageView;", "dot", "color", "E", "Landroid/view/View;", "t", "count", "C", "r", "J", "G", "B", "D", "I", "isPortStickyPath", "K", "", "controlX", "endX", "radius", "Landroid/graphics/Path;", "v", "distance", "u", "w", "Landroid/graphics/Canvas;", "canvas", "dispatchDraw", "H", "widthMeasureSpec", "heightMeasureSpec", "onMeasure", "setDotsCount", "setCurrentPosition", "setTraceDotColor", "setPageIndicatorDotsColor", "Lcom/coui/appcompat/banner/COUIPageIndicatorKit$e;", "onDotClickListener", "setOnDotClickListener", "positionOffset", "positionOffsetPixels", "z", "A", "state", "y", "e", "dotSize", "f", "dotSpacing", "g", "dotColor", "h", "dotStrokeWidth", "i", "dotCornerRadius", "j", "traceDotColor", "k", "Z", "dotIsClickable", "l", "dotIsStrokeStyle", "m", "dotsCount", "n", "currentPosition", "o", "lastPosition", "p", "dotStepDistance", "q", "traceLeft", "traceRight", "s", "finalLeft", "finalRight", "tranceCutTailRight", "isAnimated", "isAnimating", "x", "isAnimatorCanceled", "isPaused", "needSettlePositionTemp", "Landroid/widget/LinearLayout;", "Landroid/widget/LinearLayout;", "indicatorDotsParent", "Ljava/util/ArrayList;", "Ljava/util/ArrayList;", "indicatorDots", "Landroid/graphics/Paint;", "Landroid/graphics/Paint;", "tracePaint", "Landroid/graphics/RectF;", "Landroid/graphics/RectF;", "traceRect", "Landroid/animation/ValueAnimator;", "Landroid/animation/ValueAnimator;", "traceAnimator", "Landroid/os/Handler;", "Landroid/os/Handler;", "mHandler", "layoutWidth", "Lcom/coui/appcompat/banner/COUIPageIndicatorKit$e;", "MSG_START_TRACE_ANIMATION", "MIS_POSITION", "DURATION_TRACE_ANIMATION", "L", "FLOAT_HALF", "M", "FLOAT_ONE", "N", "FLOAT_SQRT_2", "O", "STICKY_DISTANCE_FACTOR", "P", "BEZIER_OFFSET_SLOPE", "Q", "BEZIER_OFFSET_INTERCEPT", "R", "BEZIER_OFFSET_MAX_FACTOR", "S", "BEZIER_OFFSET_MIN_FACTOR", "T", "DISTANCE_TURN_POINT", "U", "BEZIER_OFFSET_X_SLOPE", "V", "BEZIER_OFFSET_X_INTERCEPT", "W", "BEZIER_OFFSET_X_MAX_FACTOR", "a0", "BEZIER_OFFSET_X_MIN_FACTOR", "b0", "BEZIER_OFFSET_X_SLOPE_2", "c0", "BEZIER_OFFSET_X_INTERCEPT_2", "d0", "BEZIER_OFFSET_X_MAX_FACTOR_2", "e0", "BEZIER_OFFSET_X_MIN_FACTOR_2", "f0", "mPortPosition", "g0", "mDepartPosition", "h0", "mPortControlX", "i0", "mPortEndX", "j0", "mDepartControlX", "k0", "mDepartEndX", "l0", "mOffset", "m0", "mOffsetX", "n0", "mOffsetY", "o0", "mPortRect", "p0", "mDepartRect", "q0", "Landroid/graphics/Path;", "mPortStickyPath", "r0", "mDepartStickyPath", "isSelfLayoutRtl$delegate", "Lma/h;", "()Z", "isSelfLayoutRtl", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "t0", "c", "d", "coui-support-nearx_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public class COUIPageIndicatorKit extends FrameLayout {

    /* renamed from: A, reason: from kotlin metadata */
    private final LinearLayout indicatorDotsParent;

    /* renamed from: B, reason: from kotlin metadata */
    private final ArrayList<ImageView> indicatorDots;

    /* renamed from: C, reason: from kotlin metadata */
    private final Paint tracePaint;

    /* renamed from: D, reason: from kotlin metadata */
    private final RectF traceRect;

    /* renamed from: E, reason: from kotlin metadata */
    private final ValueAnimator traceAnimator;

    /* renamed from: F, reason: from kotlin metadata */
    private final Handler mHandler;

    /* renamed from: G, reason: from kotlin metadata */
    private int layoutWidth;

    /* renamed from: H, reason: from kotlin metadata */
    private e onDotClickListener;

    /* renamed from: I, reason: from kotlin metadata */
    private final int MSG_START_TRACE_ANIMATION;

    /* renamed from: J, reason: from kotlin metadata */
    private final int MIS_POSITION;

    /* renamed from: K, reason: from kotlin metadata */
    private final int DURATION_TRACE_ANIMATION;

    /* renamed from: L, reason: from kotlin metadata */
    private final float FLOAT_HALF;

    /* renamed from: M, reason: from kotlin metadata */
    private final float FLOAT_ONE;

    /* renamed from: N, reason: from kotlin metadata */
    private final float FLOAT_SQRT_2;

    /* renamed from: O, reason: from kotlin metadata */
    private final float STICKY_DISTANCE_FACTOR;

    /* renamed from: P, reason: from kotlin metadata */
    private final float BEZIER_OFFSET_SLOPE;

    /* renamed from: Q, reason: from kotlin metadata */
    private final float BEZIER_OFFSET_INTERCEPT;

    /* renamed from: R, reason: from kotlin metadata */
    private final float BEZIER_OFFSET_MAX_FACTOR;

    /* renamed from: S, reason: from kotlin metadata */
    private final float BEZIER_OFFSET_MIN_FACTOR;

    /* renamed from: T, reason: from kotlin metadata */
    private final float DISTANCE_TURN_POINT;

    /* renamed from: U, reason: from kotlin metadata */
    private final float BEZIER_OFFSET_X_SLOPE;

    /* renamed from: V, reason: from kotlin metadata */
    private final float BEZIER_OFFSET_X_INTERCEPT;

    /* renamed from: W, reason: from kotlin metadata */
    private final float BEZIER_OFFSET_X_MAX_FACTOR;

    /* renamed from: a0, reason: collision with root package name and from kotlin metadata */
    private final float BEZIER_OFFSET_X_MIN_FACTOR;

    /* renamed from: b0, reason: collision with root package name and from kotlin metadata */
    private final float BEZIER_OFFSET_X_SLOPE_2;

    /* renamed from: c0, reason: collision with root package name and from kotlin metadata */
    private final float BEZIER_OFFSET_X_INTERCEPT_2;

    /* renamed from: d0, reason: collision with root package name and from kotlin metadata */
    private final float BEZIER_OFFSET_X_MAX_FACTOR_2;

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private int dotSize;

    /* renamed from: e0, reason: collision with root package name and from kotlin metadata */
    private final float BEZIER_OFFSET_X_MIN_FACTOR_2;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private int dotSpacing;

    /* renamed from: f0, reason: collision with root package name and from kotlin metadata */
    private int mPortPosition;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private int dotColor;

    /* renamed from: g0, reason: collision with root package name and from kotlin metadata */
    private int mDepartPosition;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private int dotStrokeWidth;

    /* renamed from: h0, reason: collision with root package name and from kotlin metadata */
    private float mPortControlX;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private int dotCornerRadius;

    /* renamed from: i0, reason: collision with root package name and from kotlin metadata */
    private float mPortEndX;

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    private int traceDotColor;

    /* renamed from: j0, reason: collision with root package name and from kotlin metadata */
    private float mDepartControlX;

    /* renamed from: k, reason: collision with root package name and from kotlin metadata */
    private boolean dotIsClickable;

    /* renamed from: k0, reason: collision with root package name and from kotlin metadata */
    private float mDepartEndX;

    /* renamed from: l, reason: collision with root package name and from kotlin metadata */
    private boolean dotIsStrokeStyle;

    /* renamed from: l0, reason: collision with root package name and from kotlin metadata */
    private float mOffset;

    /* renamed from: m, reason: collision with root package name and from kotlin metadata */
    private int dotsCount;

    /* renamed from: m0, reason: collision with root package name and from kotlin metadata */
    private float mOffsetX;

    /* renamed from: n, reason: collision with root package name and from kotlin metadata */
    private int currentPosition;

    /* renamed from: n0, reason: collision with root package name and from kotlin metadata */
    private float mOffsetY;

    /* renamed from: o, reason: collision with root package name and from kotlin metadata */
    private int lastPosition;

    /* renamed from: o0, reason: collision with root package name and from kotlin metadata */
    private RectF mPortRect;

    /* renamed from: p, reason: collision with root package name and from kotlin metadata */
    private int dotStepDistance;

    /* renamed from: p0, reason: collision with root package name and from kotlin metadata */
    private RectF mDepartRect;

    /* renamed from: q, reason: collision with root package name and from kotlin metadata */
    private float traceLeft;

    /* renamed from: q0, reason: collision with root package name and from kotlin metadata */
    private Path mPortStickyPath;

    /* renamed from: r, reason: collision with root package name and from kotlin metadata */
    private float traceRight;

    /* renamed from: r0, reason: collision with root package name and from kotlin metadata */
    private Path mDepartStickyPath;

    /* renamed from: s, reason: collision with root package name and from kotlin metadata */
    private float finalLeft;

    /* renamed from: s0, reason: collision with root package name */
    private final h f5404s0;

    /* renamed from: t, reason: collision with root package name and from kotlin metadata */
    private float finalRight;

    /* renamed from: u, reason: collision with root package name and from kotlin metadata */
    private boolean tranceCutTailRight;

    /* renamed from: v, reason: collision with root package name and from kotlin metadata */
    private boolean isAnimated;

    /* renamed from: w, reason: collision with root package name and from kotlin metadata */
    private boolean isAnimating;

    /* renamed from: x, reason: collision with root package name and from kotlin metadata */
    private boolean isAnimatorCanceled;

    /* renamed from: y, reason: collision with root package name and from kotlin metadata */
    private boolean isPaused;

    /* renamed from: z, reason: collision with root package name and from kotlin metadata */
    private boolean needSettlePositionTemp;

    /* compiled from: COUIPageIndicatorKit.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0007"}, d2 = {"com/coui/appcompat/banner/COUIPageIndicatorKit$a", "Landroid/animation/AnimatorListenerAdapter;", "Landroid/animation/Animator;", "animation", "Lma/f0;", "onAnimationEnd", "onAnimationStart", "coui-support-nearx_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            k.e(animator, "animation");
            super.onAnimationEnd(animator);
            if (!COUIPageIndicatorKit.this.isAnimatorCanceled) {
                COUIPageIndicatorKit.this.traceRect.right = COUIPageIndicatorKit.this.traceRect.left + COUIPageIndicatorKit.this.dotSize;
                COUIPageIndicatorKit.this.needSettlePositionTemp = false;
                COUIPageIndicatorKit.this.isAnimated = true;
                COUIPageIndicatorKit.this.invalidate();
            }
            COUIPageIndicatorKit.this.isAnimating = false;
            COUIPageIndicatorKit cOUIPageIndicatorKit = COUIPageIndicatorKit.this;
            cOUIPageIndicatorKit.currentPosition = cOUIPageIndicatorKit.lastPosition;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            k.e(animator, "animation");
            super.onAnimationStart(animator);
            COUIPageIndicatorKit.this.isAnimatorCanceled = false;
            COUIPageIndicatorKit cOUIPageIndicatorKit = COUIPageIndicatorKit.this;
            cOUIPageIndicatorKit.traceLeft = cOUIPageIndicatorKit.traceRect.left;
            COUIPageIndicatorKit cOUIPageIndicatorKit2 = COUIPageIndicatorKit.this;
            cOUIPageIndicatorKit2.traceRight = cOUIPageIndicatorKit2.traceRect.right;
        }
    }

    /* compiled from: COUIPageIndicatorKit.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004"}, d2 = {"com/coui/appcompat/banner/COUIPageIndicatorKit$b", "Landroid/view/ViewTreeObserver$OnGlobalLayoutListener;", "Lma/f0;", "onGlobalLayout", "coui-support-nearx_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class b implements ViewTreeObserver.OnGlobalLayoutListener {
        b() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            COUIPageIndicatorKit.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            COUIPageIndicatorKit cOUIPageIndicatorKit = COUIPageIndicatorKit.this;
            cOUIPageIndicatorKit.F(cOUIPageIndicatorKit.currentPosition);
        }
    }

    /* compiled from: COUIPageIndicatorKit.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/banner/COUIPageIndicatorKit$e;", "", "", "position", "Lma/f0;", "a", "coui-support-nearx_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public interface e {
        void a(int i10);
    }

    /* compiled from: COUIPageIndicatorKit.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0006\n\u0002\u0010\u000b\n\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class f extends Lambda implements ya.a<Boolean> {
        f() {
            super(0);
        }

        public final boolean a() {
            return COUIPageIndicatorKit.this.getLayoutDirection() == 1;
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Boolean invoke() {
            return Boolean.valueOf(a());
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIPageIndicatorKit(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        k.e(context, "context");
        k.e(attributeSet, "attrs");
    }

    public /* synthetic */ COUIPageIndicatorKit(Context context, AttributeSet attributeSet, int i10, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, attributeSet, (i11 & 4) != 0 ? R$attr.couiPageIndicatorStyle : i10);
    }

    private final void B() {
        this.isPaused = true;
    }

    private final void C(int i10) {
        if (i10 > 0) {
            int i11 = 0;
            do {
                i11++;
                this.indicatorDotsParent.removeViewAt(r1.getChildCount() - 1);
                ArrayList<ImageView> arrayList = this.indicatorDots;
                k.b(arrayList);
                arrayList.remove(this.indicatorDots.size() - 1);
            } while (i11 < i10);
        }
    }

    private final void D() {
        this.isPaused = false;
    }

    private final void E(boolean z10, ImageView imageView, int i10) {
        Drawable background = imageView.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
        GradientDrawable gradientDrawable = (GradientDrawable) background;
        if (z10) {
            gradientDrawable.setStroke(this.dotStrokeWidth, i10);
        } else {
            gradientDrawable.setColor(i10);
        }
        gradientDrawable.setCornerRadius(this.dotCornerRadius);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void F(int i10) {
        I(this.currentPosition);
        RectF rectF = this.traceRect;
        rectF.left = this.finalLeft;
        rectF.right = this.finalRight;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void G() {
        if (this.traceAnimator == null) {
            return;
        }
        H();
        this.traceAnimator.start();
    }

    private final void I(int i10) {
        if (x()) {
            float f10 = this.layoutWidth - (this.dotSpacing + (i10 * this.dotStepDistance));
            this.finalRight = f10;
            this.finalLeft = f10 - this.dotSize;
        } else {
            int i11 = this.dotSpacing;
            int i12 = this.dotSize;
            float f11 = i11 + i12 + (i10 * this.dotStepDistance);
            this.finalRight = f11;
            this.finalLeft = f11 - i12;
        }
    }

    private final void J() {
        int i10 = this.dotsCount;
        if (i10 < 1) {
            return;
        }
        this.layoutWidth = this.dotStepDistance * i10;
        requestLayout();
    }

    private final void K(int i10, boolean z10) {
        if (z10) {
            RectF rectF = this.mPortRect;
            rectF.top = 0.0f;
            rectF.bottom = this.dotSize;
            if (x()) {
                this.mPortRect.right = this.layoutWidth - (this.dotSpacing + (i10 * this.dotStepDistance));
            } else {
                this.mPortRect.right = this.dotSpacing + this.dotSize + (i10 * this.dotStepDistance);
            }
            RectF rectF2 = this.mPortRect;
            rectF2.left = rectF2.right - this.dotSize;
            return;
        }
        RectF rectF3 = this.mDepartRect;
        rectF3.top = 0.0f;
        rectF3.bottom = this.dotSize;
        if (x()) {
            this.mDepartRect.right = this.layoutWidth - (this.dotSpacing + (i10 * this.dotStepDistance));
        } else {
            this.mDepartRect.right = this.dotSpacing + this.dotSize + (i10 * this.dotStepDistance);
        }
        RectF rectF4 = this.mDepartRect;
        rectF4.left = rectF4.right - this.dotSize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void c(COUIPageIndicatorKit cOUIPageIndicatorKit, ValueAnimator valueAnimator) {
        k.e(cOUIPageIndicatorKit, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        float floatValue = ((Float) animatedValue).floatValue();
        if (cOUIPageIndicatorKit.isAnimatorCanceled) {
            return;
        }
        float f10 = cOUIPageIndicatorKit.traceLeft;
        float f11 = f10 - cOUIPageIndicatorKit.finalLeft;
        float f12 = cOUIPageIndicatorKit.traceRight;
        float f13 = f12 - cOUIPageIndicatorKit.finalRight;
        float f14 = f10 - (f11 * floatValue);
        RectF rectF = cOUIPageIndicatorKit.traceRect;
        float f15 = rectF.right;
        int i10 = cOUIPageIndicatorKit.dotSize;
        if (f14 > f15 - i10) {
            f14 = f15 - i10;
        }
        float f16 = f12 - (f13 * floatValue);
        if (f16 < rectF.left + i10) {
            f16 = f10 + i10;
        }
        if (!cOUIPageIndicatorKit.needSettlePositionTemp) {
            if (cOUIPageIndicatorKit.tranceCutTailRight) {
                rectF.right = f16;
            } else {
                rectF.left = f14;
            }
        } else {
            rectF.left = f14;
            rectF.right = f16;
        }
        if (cOUIPageIndicatorKit.tranceCutTailRight) {
            cOUIPageIndicatorKit.mDepartControlX = rectF.right - (i10 * cOUIPageIndicatorKit.FLOAT_HALF);
        } else {
            cOUIPageIndicatorKit.mDepartControlX = rectF.left + (i10 * cOUIPageIndicatorKit.FLOAT_HALF);
        }
        float f17 = cOUIPageIndicatorKit.mDepartRect.left;
        float f18 = cOUIPageIndicatorKit.FLOAT_HALF;
        float f19 = f17 + (i10 * f18);
        cOUIPageIndicatorKit.mDepartEndX = f19;
        cOUIPageIndicatorKit.mDepartStickyPath = cOUIPageIndicatorKit.v(cOUIPageIndicatorKit.mDepartPosition, cOUIPageIndicatorKit.mDepartControlX, f19, i10 * f18, false);
        cOUIPageIndicatorKit.invalidate();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void r(int i10) {
        if (i10 <= 0) {
            return;
        }
        final int i11 = 0;
        while (true) {
            int i12 = i11 + 1;
            View t7 = t(this.dotIsStrokeStyle, this.dotColor);
            if (this.dotIsClickable) {
                t7.setOnClickListener(new View.OnClickListener() { // from class: n1.d
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        COUIPageIndicatorKit.s(COUIPageIndicatorKit.this, i11, view);
                    }
                });
            }
            ArrayList<ImageView> arrayList = this.indicatorDots;
            k.b(arrayList);
            arrayList.add(t7.findViewById(R$id.page_indicator_dot));
            this.indicatorDotsParent.addView(t7);
            if (i12 >= i10) {
                return;
            } else {
                i11 = i12;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void s(COUIPageIndicatorKit cOUIPageIndicatorKit, int i10, View view) {
        k.e(cOUIPageIndicatorKit, "this$0");
        e eVar = cOUIPageIndicatorKit.onDotClickListener;
        if (eVar == null || cOUIPageIndicatorKit.isAnimating) {
            return;
        }
        cOUIPageIndicatorKit.isAnimated = false;
        cOUIPageIndicatorKit.needSettlePositionTemp = true;
        k.b(eVar);
        eVar.a(i10);
    }

    private final View t(boolean stroke, int color) {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.coui_page_indicator_dot_layout, (ViewGroup) this, false);
        ImageView imageView = (ImageView) inflate.findViewById(R$id.page_indicator_dot);
        imageView.setBackground(getContext().getResources().getDrawable(stroke ? R$drawable.coui_page_indicator_dot_stroke : R$drawable.coui_page_indicator_dot));
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
        int i10 = this.dotSize;
        layoutParams2.height = i10;
        layoutParams2.width = i10;
        imageView.setLayoutParams(layoutParams2);
        int i11 = this.dotSpacing;
        layoutParams2.setMargins(i11, 0, i11, 0);
        k.d(imageView, "dotView");
        E(stroke, imageView, color);
        k.d(inflate, "dot");
        return inflate;
    }

    private final void u(float f10, float f11) {
        this.mOffset = Math.max(Math.min((this.BEZIER_OFFSET_SLOPE * f10) + (this.BEZIER_OFFSET_INTERCEPT * f11), this.BEZIER_OFFSET_MAX_FACTOR * f11), this.BEZIER_OFFSET_MIN_FACTOR * f11);
        float f12 = this.BEZIER_OFFSET_X_MAX_FACTOR;
        this.mOffsetX = f12 * f11;
        this.mOffsetY = 0.0f;
        if (f10 >= this.DISTANCE_TURN_POINT * f11) {
            float max = Math.max(Math.min((this.BEZIER_OFFSET_X_SLOPE * f10) + (this.BEZIER_OFFSET_X_INTERCEPT * f11), f12 * f11), this.BEZIER_OFFSET_X_MIN_FACTOR * f11);
            this.mOffsetX = max;
            float f13 = 2;
            this.mOffsetY = ((f10 - (max * f13)) * f11) / ((this.FLOAT_SQRT_2 * f10) - (f13 * f11));
            return;
        }
        this.mOffsetX = Math.max(Math.min((this.BEZIER_OFFSET_X_SLOPE_2 * f10) + (this.BEZIER_OFFSET_X_INTERCEPT_2 * f11), this.BEZIER_OFFSET_X_MAX_FACTOR_2 * f11), this.BEZIER_OFFSET_X_MIN_FACTOR_2);
        this.mOffsetY = (float) Math.sqrt(Math.pow(f11, 2.0d) - Math.pow(this.mOffsetX, 2.0d));
    }

    private final Path v(int position, float controlX, float endX, float radius, boolean isPortStickyPath) {
        Path path;
        if (isPortStickyPath) {
            path = this.mPortStickyPath;
        } else {
            path = this.mDepartStickyPath;
        }
        path.reset();
        float abs = Math.abs(controlX - endX);
        if (abs < this.STICKY_DISTANCE_FACTOR * radius && position != this.MIS_POSITION) {
            u(abs, radius);
            float f10 = this.FLOAT_HALF;
            float f11 = this.FLOAT_SQRT_2;
            float f12 = f10 * f11 * radius;
            float f13 = f10 * f11 * radius;
            if (controlX > endX) {
                this.mOffsetX = -this.mOffsetX;
                f12 = -f12;
            }
            if (abs >= this.DISTANCE_TURN_POINT * radius) {
                float f14 = controlX + f12;
                float f15 = radius + f13;
                path.moveTo(f14, f15);
                path.lineTo(this.mOffsetX + controlX, this.mOffsetY + radius);
                float f16 = controlX + endX;
                path.quadTo(this.FLOAT_HALF * f16, this.mOffset + radius, endX - this.mOffsetX, this.mOffsetY + radius);
                float f17 = endX - f12;
                path.lineTo(f17, f15);
                float f18 = radius - f13;
                path.lineTo(f17, f18);
                path.lineTo(endX - this.mOffsetX, radius - this.mOffsetY);
                path.quadTo(f16 * this.FLOAT_HALF, radius - this.mOffset, controlX + this.mOffsetX, radius - this.mOffsetY);
                path.lineTo(f14, f18);
                path.lineTo(f14, f15);
            } else {
                path.moveTo(this.mOffsetX + controlX, this.mOffsetY + radius);
                float f19 = controlX + endX;
                path.quadTo(this.FLOAT_HALF * f19, this.mOffset + radius, endX - this.mOffsetX, this.mOffsetY + radius);
                path.lineTo(endX - this.mOffsetX, radius - this.mOffsetY);
                path.quadTo(f19 * this.FLOAT_HALF, radius - this.mOffset, this.mOffsetX + controlX, radius - this.mOffsetY);
                path.lineTo(controlX + this.mOffsetX, radius + this.mOffsetY);
            }
            return path;
        }
        w(isPortStickyPath);
        return path;
    }

    private final void w(boolean z10) {
        if (z10) {
            this.mPortPosition = this.MIS_POSITION;
            this.mPortRect.setEmpty();
            this.mPortStickyPath.reset();
        } else {
            this.mDepartPosition = this.MIS_POSITION;
            this.mDepartRect.setEmpty();
            this.mDepartStickyPath.reset();
        }
    }

    private final boolean x() {
        return ((Boolean) this.f5404s0.getValue()).booleanValue();
    }

    public final void A(int i10) {
        if (this.lastPosition != i10) {
            if (this.isAnimated) {
                this.isAnimated = false;
            }
            this.tranceCutTailRight = !x() ? this.lastPosition <= i10 : this.lastPosition > i10;
            I(i10);
            this.mDepartPosition = i10;
            K(i10, false);
            if (this.lastPosition != i10) {
                if (this.mHandler.hasMessages(this.MSG_START_TRACE_ANIMATION)) {
                    this.mHandler.removeMessages(this.MSG_START_TRACE_ANIMATION);
                }
                H();
                if ((i10 == this.indicatorDotsParent.getChildCount() - 1 && this.lastPosition == 0) || (i10 == 0 && this.lastPosition == this.indicatorDotsParent.getChildCount() - 1)) {
                    ValueAnimator valueAnimator = this.traceAnimator;
                    if (valueAnimator != null) {
                        valueAnimator.setDuration(0L);
                    }
                } else {
                    ValueAnimator valueAnimator2 = this.traceAnimator;
                    if (valueAnimator2 != null) {
                        valueAnimator2.setDuration(240L);
                    }
                }
                this.mHandler.sendEmptyMessageDelayed(this.MSG_START_TRACE_ANIMATION, 100L);
            }
            this.lastPosition = i10;
        }
    }

    public final void H() {
        if (!this.isAnimatorCanceled) {
            this.isAnimatorCanceled = true;
        }
        ValueAnimator valueAnimator = this.traceAnimator;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        this.traceAnimator.end();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        k.e(canvas, "canvas");
        super.dispatchDraw(canvas);
        RectF rectF = this.traceRect;
        int i10 = this.dotCornerRadius;
        canvas.drawRoundRect(rectF, i10, i10, this.tracePaint);
        RectF rectF2 = this.mPortRect;
        int i11 = this.dotCornerRadius;
        canvas.drawRoundRect(rectF2, i11, i11, this.tracePaint);
        canvas.drawPath(this.mPortStickyPath, this.tracePaint);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(this.layoutWidth, this.dotSize);
    }

    public final void setCurrentPosition(int i10) {
        this.currentPosition = i10;
        this.lastPosition = i10;
        F(i10);
    }

    public final void setDotsCount(int i10) {
        int i11 = this.dotsCount;
        if (i11 > 0) {
            C(i11);
        }
        this.dotsCount = i10;
        J();
        r(i10);
    }

    public final void setOnDotClickListener(e eVar) {
        k.e(eVar, "onDotClickListener");
        this.onDotClickListener = eVar;
    }

    public final void setPageIndicatorDotsColor(int i10) {
        this.dotColor = i10;
        ArrayList<ImageView> arrayList = this.indicatorDots;
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        Iterator<ImageView> it = this.indicatorDots.iterator();
        while (it.hasNext()) {
            ImageView next = it.next();
            boolean z10 = this.dotIsStrokeStyle;
            k.d(next, "dot");
            E(z10, next, i10);
        }
    }

    public final void setTraceDotColor(int i10) {
        this.traceDotColor = i10;
        this.tracePaint.setColor(i10);
    }

    public final void y(int i10) {
        if (i10 != 1) {
            if (i10 != 2) {
                return;
            }
            D();
        } else {
            B();
            w(false);
            if (this.isAnimated) {
                this.isAnimated = false;
            }
        }
    }

    public final void z(int i10, float f10, int i11) {
        float f11;
        int i12;
        boolean x10 = x();
        boolean z10 = x10 == (this.currentPosition > i10);
        if (z10) {
            if (i10 == this.indicatorDotsParent.getChildCount() - 1 && this.currentPosition == this.indicatorDotsParent.getChildCount() - 1 && !x10) {
                int i13 = this.dotSpacing;
                int i14 = this.dotSize;
                float f12 = i13 + i14 + (this.dotStepDistance * i10);
                RectF rectF = this.traceRect;
                rectF.right = f12;
                rectF.left = f12 - i14;
            } else {
                if (i10 == this.indicatorDotsParent.getChildCount() - 1 && (i12 = this.currentPosition) == 0) {
                    if (!(f10 == 0.0f) && !x10) {
                        int i15 = this.dotSpacing;
                        int i16 = this.dotSize;
                        float f13 = i15 + i16 + (i12 * this.dotStepDistance);
                        RectF rectF2 = this.traceRect;
                        rectF2.right = f13;
                        rectF2.left = f13 - i16;
                    }
                }
                if (x10) {
                    this.mPortPosition = i10;
                    this.traceRect.right = this.layoutWidth - ((this.dotSpacing + (i10 * r5)) + (this.dotStepDistance * f10));
                } else {
                    this.mPortPosition = i10 + 1;
                    this.traceRect.right = this.dotSpacing + this.dotSize + (i10 * r4) + (this.dotStepDistance * f10);
                }
                if (this.isPaused) {
                    if (!this.isAnimating && this.isAnimated) {
                        RectF rectF3 = this.traceRect;
                        rectF3.left = rectF3.right - this.dotSize;
                    } else {
                        RectF rectF4 = this.traceRect;
                        float f14 = rectF4.right;
                        float f15 = f14 - rectF4.left;
                        int i17 = this.dotSize;
                        if (f15 < i17) {
                            rectF4.left = f14 - i17;
                        }
                    }
                } else if (this.isAnimated) {
                    RectF rectF5 = this.traceRect;
                    rectF5.left = rectF5.right - this.dotSize;
                } else {
                    RectF rectF6 = this.traceRect;
                    float f16 = rectF6.right;
                    float f17 = f16 - rectF6.left;
                    int i18 = this.dotSize;
                    if (f17 < i18) {
                        rectF6.left = f16 - i18;
                    }
                }
            }
        } else if (i10 == this.indicatorDotsParent.getChildCount() - 1 && this.currentPosition == this.indicatorDotsParent.getChildCount() - 1 && x10) {
            float width = getWidth() - (this.dotSpacing + (this.dotStepDistance * i10));
            RectF rectF7 = this.traceRect;
            rectF7.right = width;
            rectF7.left = width - this.dotSize;
        } else {
            if (i10 == this.indicatorDotsParent.getChildCount() - 1 && this.currentPosition == 0) {
                if (!(f10 == 0.0f) && x10) {
                    float width2 = getWidth() - (this.dotSpacing + (this.currentPosition * this.dotStepDistance));
                    RectF rectF8 = this.traceRect;
                    rectF8.right = width2;
                    rectF8.left = width2 - this.dotSize;
                }
            }
            if (x10) {
                this.mPortPosition = i10 + 1;
                this.traceRect.left = ((this.layoutWidth - (this.dotStepDistance * (i10 + f10))) - this.dotSpacing) - this.dotSize;
            } else {
                this.mPortPosition = i10;
                this.traceRect.left = this.dotSpacing + (this.dotStepDistance * (i10 + f10));
            }
            if (this.isPaused) {
                if (!this.isAnimating && this.isAnimated) {
                    RectF rectF9 = this.traceRect;
                    rectF9.right = rectF9.left + this.dotSize;
                } else {
                    RectF rectF10 = this.traceRect;
                    float f18 = rectF10.right;
                    float f19 = rectF10.left;
                    float f20 = f18 - f19;
                    int i19 = this.dotSize;
                    if (f20 < i19) {
                        rectF10.right = f19 + i19;
                    }
                }
            } else if (this.isAnimated) {
                RectF rectF11 = this.traceRect;
                rectF11.right = rectF11.left + this.dotSize;
            } else {
                RectF rectF12 = this.traceRect;
                float f21 = rectF12.right;
                float f22 = rectF12.left;
                float f23 = f21 - f22;
                int i20 = this.dotSize;
                if (f23 < i20) {
                    rectF12.right = f22 + i20;
                }
            }
        }
        if (this.traceRect.right > this.dotSpacing + this.dotSize + ((this.indicatorDotsParent.getChildCount() - 1) * this.dotStepDistance)) {
            this.traceRect.right = this.dotSpacing + this.dotSize + ((this.indicatorDotsParent.getChildCount() - 1) * this.dotStepDistance);
        }
        RectF rectF13 = this.traceRect;
        float f24 = rectF13.left;
        this.traceLeft = f24;
        float f25 = rectF13.right;
        this.traceRight = f25;
        if (z10) {
            f11 = f25 - (this.dotSize * this.FLOAT_HALF);
        } else {
            f11 = (this.dotSize * this.FLOAT_HALF) + f24;
        }
        this.mPortControlX = f11;
        K(this.mPortPosition, true);
        float f26 = this.mPortRect.left;
        int i21 = this.dotSize;
        float f27 = this.FLOAT_HALF;
        float f28 = f26 + (i21 * f27);
        this.mPortEndX = f28;
        this.mPortStickyPath = v(this.mPortPosition, this.mPortControlX, f28, i21 * f27, true);
        if (f10 == 0.0f) {
            this.currentPosition = i10;
            w(true);
        }
        invalidate();
    }

    /* compiled from: COUIPageIndicatorKit.kt */
    @Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u000b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\r\u001a\u00020\f¢\u0006\u0004\b\u000e\u0010\u000fJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016R\u001a\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\b\u0010\t¨\u0006\u0010"}, d2 = {"Lcom/coui/appcompat/banner/COUIPageIndicatorKit$d;", "Landroid/os/Handler;", "Landroid/os/Message;", "msg", "Lma/f0;", "handleMessage", "Ljava/lang/ref/WeakReference;", "Lcom/coui/appcompat/banner/COUIPageIndicatorKit;", "a", "Ljava/lang/ref/WeakReference;", "ref", "obj", "Landroid/os/Looper;", "looper", "<init>", "(Lcom/coui/appcompat/banner/COUIPageIndicatorKit;Landroid/os/Looper;)V", "coui-support-nearx_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    private static final class d extends Handler {

        /* renamed from: a, reason: collision with root package name and from kotlin metadata */
        private final WeakReference<COUIPageIndicatorKit> ref;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public d(COUIPageIndicatorKit cOUIPageIndicatorKit, Looper looper) {
            super(looper);
            k.e(cOUIPageIndicatorKit, "obj");
            k.e(looper, "looper");
            this.ref = new WeakReference<>(cOUIPageIndicatorKit);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            COUIPageIndicatorKit cOUIPageIndicatorKit;
            k.e(message, "msg");
            if (message.what == 17 && (cOUIPageIndicatorKit = this.ref.get()) != null) {
                cOUIPageIndicatorKit.G();
            }
            super.handleMessage(message);
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ d(COUIPageIndicatorKit cOUIPageIndicatorKit, Looper looper, int i10, DefaultConstructorMarker defaultConstructorMarker) {
            this(cOUIPageIndicatorKit, looper);
            if ((i10 & 2) != 0) {
                looper = Looper.getMainLooper();
                k.d(looper, "getMainLooper()");
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIPageIndicatorKit(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        h b10;
        k.e(context, "context");
        k.e(attributeSet, "attrs");
        RectF rectF = new RectF();
        this.traceRect = rectF;
        this.MSG_START_TRACE_ANIMATION = 17;
        this.MIS_POSITION = -1;
        this.DURATION_TRACE_ANIMATION = 300;
        this.FLOAT_HALF = 0.5f;
        this.FLOAT_ONE = 1.0f;
        float sqrt = (float) Math.sqrt(2.0d);
        this.FLOAT_SQRT_2 = sqrt;
        this.STICKY_DISTANCE_FACTOR = 2.95f;
        this.BEZIER_OFFSET_SLOPE = -1.0f;
        this.BEZIER_OFFSET_INTERCEPT = 3.0f;
        this.BEZIER_OFFSET_MAX_FACTOR = 1.0f;
        this.DISTANCE_TURN_POINT = 2.8f;
        this.BEZIER_OFFSET_X_SLOPE = 7.5f - (2.5f * sqrt);
        this.BEZIER_OFFSET_X_INTERCEPT = (7.5f * sqrt) - 21;
        this.BEZIER_OFFSET_X_MAX_FACTOR = 1.5f;
        this.BEZIER_OFFSET_X_MIN_FACTOR = sqrt * 0.5f;
        this.BEZIER_OFFSET_X_SLOPE_2 = 0.625f * sqrt;
        this.BEZIER_OFFSET_X_INTERCEPT_2 = (-1.25f) * sqrt;
        this.BEZIER_OFFSET_X_MAX_FACTOR_2 = sqrt * 0.5f;
        this.mPortRect = new RectF();
        this.mDepartRect = new RectF();
        this.mPortStickyPath = new Path();
        this.mDepartStickyPath = new Path();
        b10 = j.b(new f());
        this.f5404s0 = b10;
        this.indicatorDots = new ArrayList<>();
        this.dotSize = context.getResources().getDimensionPixelSize(R$dimen.coui_page_indicator_dot_size);
        this.dotSpacing = context.getResources().getDimensionPixelSize(R$dimen.coui_page_indicator_dot_spacing);
        this.dotColor = 0;
        this.traceDotColor = 0;
        this.dotIsStrokeStyle = false;
        this.dotCornerRadius = this.dotSize / 2;
        this.dotIsClickable = true;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPageIndicator, i10, 0);
        this.traceDotColor = obtainStyledAttributes.getColor(R$styleable.COUIPageIndicator_traceDotColor, this.traceDotColor);
        this.dotColor = obtainStyledAttributes.getColor(R$styleable.COUIPageIndicator_dotColor, this.dotColor);
        this.dotSize = (int) obtainStyledAttributes.getDimension(R$styleable.COUIPageIndicator_dotSize, this.dotSize);
        this.dotSpacing = (int) obtainStyledAttributes.getDimension(R$styleable.COUIPageIndicator_dotSpacing, this.dotSpacing);
        this.dotCornerRadius = (int) obtainStyledAttributes.getDimension(R$styleable.COUIPageIndicator_dotCornerRadius, this.dotSize / 2);
        this.dotIsClickable = obtainStyledAttributes.getBoolean(R$styleable.COUIPageIndicator_dotClickable, this.dotIsClickable);
        this.dotIsStrokeStyle = obtainStyledAttributes.getBoolean(R$styleable.COUIPageIndicator_dotIsStrokeStyle, this.dotIsStrokeStyle);
        this.dotStrokeWidth = (int) obtainStyledAttributes.getDimension(R$styleable.COUIPageIndicator_dotStrokeWidth, this.dotStrokeWidth);
        obtainStyledAttributes.recycle();
        rectF.top = 0.0f;
        rectF.bottom = this.dotSize;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.traceAnimator = ofFloat;
        k.b(ofFloat);
        ofFloat.setDuration(240L);
        ofFloat.setInterpolator(new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f));
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: n1.c
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUIPageIndicatorKit.c(COUIPageIndicatorKit.this, valueAnimator);
            }
        });
        ofFloat.addListener(new a());
        Paint paint = new Paint(1);
        this.tracePaint = paint;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.traceDotColor);
        this.dotStepDistance = this.dotSize + (this.dotSpacing * 2);
        this.mHandler = new d(this, null, 2, 0 == true ? 1 : 0);
        LinearLayout linearLayout = new LinearLayout(context);
        this.indicatorDotsParent = linearLayout;
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        linearLayout.setOrientation(0);
        addView(linearLayout);
        getViewTreeObserver().addOnGlobalLayoutListener(new b());
    }
}
