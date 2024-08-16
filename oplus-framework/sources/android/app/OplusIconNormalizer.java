package android.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class OplusIconNormalizer {
    private static final float BOUND_RATIO_MARGIN = 0.05f;
    protected static final float CIRCLE_AREA_BY_RECT = 0.7853982f;
    protected static final boolean DEBUG = false;
    public static final float ICON_VISIBLE_AREA_FACTOR = 0.92f;
    private static final float LINEAR_SCALE_SLOPE = 0.040449437f;
    private static final Object LOCK = new Object();
    protected static final float MAX_CIRCLE_AREA_FACTOR = 0.6597222f;
    protected static final float MAX_SQUARE_AREA_FACTOR = 0.6510417f;
    private static final int MIN_VISIBLE_ALPHA = 40;
    private static final float PIXEL_DIFF_PERCENTAGE_THRESHOLD = 0.005f;
    private static final float SCALE_NOT_INITIALIZED = 0.0f;
    private static final String TAG = "IconNormalizer";
    private static volatile OplusIconNormalizer sIconNormalizer;
    private final Rect mAdaptiveIconBounds;
    private float mAdaptiveIconScale;
    protected final Bitmap mBitmap;
    protected final Rect mBounds;
    protected final Canvas mCanvas;
    protected final float[] mLeftBorder;
    protected final int mMaxSize;
    protected final byte[] mPixels;
    protected final float[] mRightBorder;

    public OplusIconNormalizer(int iconBitmapSize) {
        int max = Math.max(1, iconBitmapSize * 2);
        this.mMaxSize = max;
        Bitmap createBitmap = Bitmap.createBitmap(max, max, Bitmap.Config.ALPHA_8);
        this.mBitmap = createBitmap;
        this.mCanvas = new Canvas(createBitmap);
        this.mPixels = new byte[max * max];
        this.mLeftBorder = new float[max];
        this.mRightBorder = new float[max];
        this.mBounds = new Rect();
        this.mAdaptiveIconBounds = new Rect();
    }

    /* JADX WARN: Code restructure failed: missing block: B:89:0x0046, code lost:
    
        if (r4 <= r22.mMaxSize) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0049, code lost:
    
        r6 = r4;
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00d7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00f1 A[Catch: all -> 0x01a6, TryCatch #0 {, blocks: (B:9:0x000c, B:11:0x0010, B:14:0x0018, B:15:0x001d, B:18:0x0021, B:22:0x002e, B:25:0x005a, B:30:0x008e, B:37:0x00a0, B:40:0x00a9, B:45:0x00bd, B:47:0x00c8, B:56:0x00e1, B:58:0x00f1, B:62:0x0103, B:63:0x00fc, B:66:0x0106, B:69:0x0126, B:71:0x0138, B:72:0x016c, B:74:0x0175, B:75:0x0182, B:77:0x018a, B:79:0x0191, B:84:0x011b, B:86:0x0034, B:88:0x0044, B:94:0x0050, B:99:0x0057, B:100:0x004b), top: B:8:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0138 A[Catch: all -> 0x01a6, TryCatch #0 {, blocks: (B:9:0x000c, B:11:0x0010, B:14:0x0018, B:15:0x001d, B:18:0x0021, B:22:0x002e, B:25:0x005a, B:30:0x008e, B:37:0x00a0, B:40:0x00a9, B:45:0x00bd, B:47:0x00c8, B:56:0x00e1, B:58:0x00f1, B:62:0x0103, B:63:0x00fc, B:66:0x0106, B:69:0x0126, B:71:0x0138, B:72:0x016c, B:74:0x0175, B:75:0x0182, B:77:0x018a, B:79:0x0191, B:84:0x011b, B:86:0x0034, B:88:0x0044, B:94:0x0050, B:99:0x0057, B:100:0x004b), top: B:8:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0175 A[Catch: all -> 0x01a6, TryCatch #0 {, blocks: (B:9:0x000c, B:11:0x0010, B:14:0x0018, B:15:0x001d, B:18:0x0021, B:22:0x002e, B:25:0x005a, B:30:0x008e, B:37:0x00a0, B:40:0x00a9, B:45:0x00bd, B:47:0x00c8, B:56:0x00e1, B:58:0x00f1, B:62:0x0103, B:63:0x00fc, B:66:0x0106, B:69:0x0126, B:71:0x0138, B:72:0x016c, B:74:0x0175, B:75:0x0182, B:77:0x018a, B:79:0x0191, B:84:0x011b, B:86:0x0034, B:88:0x0044, B:94:0x0050, B:99:0x0057, B:100:0x004b), top: B:8:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x011b A[Catch: all -> 0x01a6, TryCatch #0 {, blocks: (B:9:0x000c, B:11:0x0010, B:14:0x0018, B:15:0x001d, B:18:0x0021, B:22:0x002e, B:25:0x005a, B:30:0x008e, B:37:0x00a0, B:40:0x00a9, B:45:0x00bd, B:47:0x00c8, B:56:0x00e1, B:58:0x00f1, B:62:0x0103, B:63:0x00fc, B:66:0x0106, B:69:0x0126, B:71:0x0138, B:72:0x016c, B:74:0x0175, B:75:0x0182, B:77:0x018a, B:79:0x0191, B:84:0x011b, B:86:0x0034, B:88:0x0044, B:94:0x0050, B:99:0x0057, B:100:0x004b), top: B:8:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x019c A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized float getScale(Drawable d, RectF outBounds) {
        int i;
        int topY;
        int bottomY;
        int leftX;
        int rightX;
        int y;
        int y2;
        float hullByRect;
        float scaleRequired;
        float scale;
        if (d == null) {
            return 0.0f;
        }
        if ((d instanceof AdaptiveIconDrawable) && this.mAdaptiveIconScale != 0.0f) {
            if (outBounds != null) {
                outBounds.set(this.mAdaptiveIconBounds);
            }
            return this.mAdaptiveIconScale;
        }
        int width = d.getIntrinsicWidth();
        int height = d.getIntrinsicHeight();
        if (width > 0 && height > 0) {
            int i2 = this.mMaxSize;
            if (width > i2 || height > i2) {
                int max = Math.max(width, height);
                int i3 = this.mMaxSize;
                width = (i3 * width) / max;
                height = (i3 * height) / max;
            }
            this.mBitmap.eraseColor(0);
            d.setBounds(0, 0, width, height);
            d.draw(this.mCanvas);
            ByteBuffer buffer = ByteBuffer.wrap(this.mPixels);
            buffer.rewind();
            this.mBitmap.copyPixelsToBuffer(buffer);
            topY = -1;
            bottomY = -1;
            int i4 = this.mMaxSize;
            leftX = i4 + 1;
            rightX = -1;
            int index = 0;
            int rowSizeDiff = i4 - width;
            y = 0;
            while (y < height) {
                int lastX = -1;
                int firstX = -1;
                int x = 0;
                while (x < width) {
                    ByteBuffer buffer2 = buffer;
                    if ((this.mPixels[index] & 255) > 40) {
                        if (firstX == -1) {
                            firstX = x;
                        }
                        lastX = x;
                    }
                    index++;
                    x++;
                    buffer = buffer2;
                }
                ByteBuffer buffer3 = buffer;
                index += rowSizeDiff;
                this.mLeftBorder[y] = firstX;
                this.mRightBorder[y] = lastX;
                if (firstX != -1) {
                    int bottomY2 = y;
                    if (topY == -1) {
                        topY = y;
                    }
                    leftX = Math.min(leftX, firstX);
                    rightX = Math.max(rightX, lastX);
                    bottomY = bottomY2;
                }
                y++;
                buffer = buffer3;
            }
            if (topY == -1 && rightX != -1) {
                convertToConvexArray(this.mLeftBorder, 1, topY, bottomY);
                convertToConvexArray(this.mRightBorder, -1, topY, bottomY);
                float area = 0.0f;
                for (y2 = 0; y2 < height; y2++) {
                    float f = this.mLeftBorder[y2];
                    if (f > -1.0f) {
                        area += (this.mRightBorder[y2] - f) + 1.0f;
                    }
                }
                int y3 = bottomY + 1;
                float rectArea = (y3 - topY) * ((rightX + 1) - leftX);
                hullByRect = area / rectArea;
                if (hullByRect >= CIRCLE_AREA_BY_RECT) {
                    scaleRequired = MAX_CIRCLE_AREA_FACTOR;
                } else {
                    scaleRequired = ((1.0f - hullByRect) * LINEAR_SCALE_SLOPE) + MAX_SQUARE_AREA_FACTOR;
                }
                this.mBounds.left = leftX;
                this.mBounds.right = rightX;
                this.mBounds.top = topY;
                this.mBounds.bottom = bottomY;
                if (outBounds == null) {
                    float rectArea2 = height;
                    outBounds.set(this.mBounds.left / width, this.mBounds.top / rectArea2, 1.0f - (this.mBounds.right / width), 1.0f - (this.mBounds.bottom / height));
                }
                float areaScale = area / (width * height);
                scale = areaScale <= scaleRequired ? (float) Math.sqrt(scaleRequired / areaScale) : 1.0f;
                if ((d instanceof AdaptiveIconDrawable) && this.mAdaptiveIconScale == 0.0f) {
                    this.mAdaptiveIconScale = scale;
                    this.mAdaptiveIconBounds.set(this.mBounds);
                }
                return scale;
            }
            return 1.0f;
        }
        int i5 = this.mMaxSize;
        width = i5;
        if (height > 0 && height <= this.mMaxSize) {
            i = height;
            height = i;
            this.mBitmap.eraseColor(0);
            d.setBounds(0, 0, width, height);
            d.draw(this.mCanvas);
            ByteBuffer buffer4 = ByteBuffer.wrap(this.mPixels);
            buffer4.rewind();
            this.mBitmap.copyPixelsToBuffer(buffer4);
            topY = -1;
            bottomY = -1;
            int i42 = this.mMaxSize;
            leftX = i42 + 1;
            rightX = -1;
            int index2 = 0;
            int rowSizeDiff2 = i42 - width;
            y = 0;
            while (y < height) {
            }
            if (topY == -1) {
                convertToConvexArray(this.mLeftBorder, 1, topY, bottomY);
                convertToConvexArray(this.mRightBorder, -1, topY, bottomY);
                float area2 = 0.0f;
                while (y2 < height) {
                }
                int y32 = bottomY + 1;
                float rectArea3 = (y32 - topY) * ((rightX + 1) - leftX);
                hullByRect = area2 / rectArea3;
                if (hullByRect >= CIRCLE_AREA_BY_RECT) {
                }
                this.mBounds.left = leftX;
                this.mBounds.right = rightX;
                this.mBounds.top = topY;
                this.mBounds.bottom = bottomY;
                if (outBounds == null) {
                }
                float areaScale2 = area2 / (width * height);
                scale = areaScale2 <= scaleRequired ? (float) Math.sqrt(scaleRequired / areaScale2) : 1.0f;
                if (d instanceof AdaptiveIconDrawable) {
                    this.mAdaptiveIconScale = scale;
                    this.mAdaptiveIconBounds.set(this.mBounds);
                }
                return scale;
            }
            return 1.0f;
        }
        i = this.mMaxSize;
        height = i;
        this.mBitmap.eraseColor(0);
        d.setBounds(0, 0, width, height);
        d.draw(this.mCanvas);
        ByteBuffer buffer42 = ByteBuffer.wrap(this.mPixels);
        buffer42.rewind();
        this.mBitmap.copyPixelsToBuffer(buffer42);
        topY = -1;
        bottomY = -1;
        int i422 = this.mMaxSize;
        leftX = i422 + 1;
        rightX = -1;
        int index22 = 0;
        int rowSizeDiff22 = i422 - width;
        y = 0;
        while (y < height) {
        }
        if (topY == -1) {
        }
        return 1.0f;
    }

    protected static void convertToConvexArray(float[] xCoordinates, int direction, int topY, int bottomY) {
        int start;
        int total = xCoordinates.length;
        float[] angles = new float[total - 1];
        int last = -1;
        float lastAngle = Float.MAX_VALUE;
        for (int i = topY + 1; i <= bottomY; i++) {
            if (xCoordinates[i] > -1.0f) {
                if (lastAngle == Float.MAX_VALUE) {
                    start = topY;
                } else {
                    float currentAngle = (xCoordinates[i] - xCoordinates[last]) / (i - last);
                    int start2 = last;
                    if ((currentAngle - lastAngle) * direction >= 0.0f) {
                        start = start2;
                    } else {
                        start = start2;
                        while (start > topY) {
                            start--;
                            float currentAngle2 = (xCoordinates[i] - xCoordinates[start]) / (i - start);
                            if ((currentAngle2 - angles[start]) * direction >= 0.0f) {
                                break;
                            }
                        }
                    }
                }
                float lastAngle2 = (xCoordinates[i] - xCoordinates[start]) / (i - start);
                for (int j = start; j < i; j++) {
                    angles[j] = lastAngle2;
                    xCoordinates[j] = xCoordinates[start] + ((j - start) * lastAngle2);
                }
                last = i;
                lastAngle = lastAngle2;
            }
        }
    }

    public static int getNormalizedCircleSize(int size) {
        float area = size * size * MAX_CIRCLE_AREA_FACTOR;
        return (int) Math.round(Math.sqrt((4.0f * area) / 3.141592653589793d));
    }
}
