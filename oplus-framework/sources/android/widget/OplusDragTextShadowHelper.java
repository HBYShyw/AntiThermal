package android.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Slog;
import android.view.View;
import android.view.ViewGroup;
import com.oplus.util.OplusDarkModeUtil;

/* loaded from: classes.dex */
public class OplusDragTextShadowHelper implements IOplusDragTextShadowHelper {
    private static final int MAX_DRAG_TEXT_SHADOW_WIDTH = 216;
    public static final String TAG = "OplusDragTextShadowHelper";
    private static OplusDragTextShadowHelper sInstance;

    public static OplusDragTextShadowHelper getInstance() {
        if (sInstance == null) {
            synchronized (OplusDragTextShadowHelper.class) {
                if (sInstance == null) {
                    sInstance = new OplusDragTextShadowHelper();
                }
            }
        }
        return sInstance;
    }

    @Override // android.widget.IOplusDragTextShadowHelper
    public View.DragShadowBuilder getOplusTextThumbnailBuilder(View textview, String text) {
        TextView shadowViewText;
        if (textview == null) {
            Slog.e(TAG, "getOplusTextThumbnaiBuilder textview is null!");
            return null;
        }
        Context viewContext = textview.getContext();
        ViewGroup shadowView = (ViewGroup) View.inflate(viewContext, 201917475, null);
        if (shadowView == null || (shadowViewText = (TextView) shadowView.findViewById(201457692)) == null) {
            throw new IllegalArgumentException("Unable to inflate text drag thumbnail");
        }
        if (OplusDarkModeUtil.isNightMode(viewContext)) {
            GradientDrawable shadowNight = (GradientDrawable) viewContext.getResources().getDrawable(201850997);
            shadowNight.setColor(Color.parseColor("#404040"));
            shadowViewText.setBackground(shadowNight);
            shadowViewText.setTextColor(Color.parseColor("#FFFFFF"));
        }
        shadowViewText.setText(text);
        float density = viewContext.getResources().getDisplayMetrics().density;
        int x = (int) ((216.0f * density) + 0.5f);
        int width = View.MeasureSpec.makeMeasureSpec(x, Integer.MIN_VALUE);
        int height = View.MeasureSpec.makeMeasureSpec(0, 0);
        shadowView.measure(width, height);
        int shadowViewWidth = shadowView.getMeasuredWidth();
        int shadowViewHeight = shadowView.getMeasuredHeight();
        if (shadowViewWidth == 0 && text != null && text.length() > 0) {
            shadowViewWidth = 1;
            shadowViewHeight = 1;
        }
        shadowView.layout(0, 0, shadowViewWidth, shadowViewHeight);
        shadowView.invalidate();
        return new View.DragShadowBuilder(shadowView);
    }
}
