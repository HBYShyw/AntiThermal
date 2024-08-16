package android.view;

import android.app.ResourcesManagerExtImpl;
import android.graphics.Rect;
import android.hardware.display.DisplayManagerGlobal;
import android.os.SystemProperties;

/* loaded from: classes.dex */
public class OplusLetterBoxedTouchpadHelper {
    private static final float TRANSFORM_VARIABLE_ONE = 1.0f;
    private static final float TRANSFORM_VARIABLE_TWO = 2.0f;
    private static final float TRANSFORM_VARIABLE_ZERO = 0.0f;
    private float mAdjustMoveXAxisForBlackBorderArea;
    private float mAdjustMoveYAxisForBlackBorderArea;
    private MotionEvent mDownMotionEvent;
    private float mFirstTouchX;
    private float mFirstTouchY;
    private double mFirstTwoFingerPointerLength;
    private float mMoveXAxisForBlackBorderArea;
    private float mMoveYAxisForBlackBorderArea;
    private MotionEvent mPointDownMotionEvent;
    private float mSecondTouchX;
    private float mSecondTouchY;
    private double mSecondTwoFingerPointerLength;
    private ViewRootImpl mViewRootImpl;
    private static final String FUNC_ENABLE_STRING = "persist.sys.lbtp.enable";
    private static boolean sFuncEnable = SystemProperties.getBoolean(FUNC_ENABLE_STRING, true);
    private boolean mIsInOplusCompatModeOrParallelWindow = false;
    private boolean mDisableClick = false;
    private boolean mIsIgnoreAdjustXYAxis = false;
    private boolean mHasAdjustedXYAxisForAppBorderArea = false;

    public OplusLetterBoxedTouchpadHelper(ViewRootImpl viewRootImpl) {
        this.mViewRootImpl = viewRootImpl;
    }

    public void disableClickIfNeededWhenInputEventStart(InputEvent inputEvent) {
        if (!(inputEvent instanceof MotionEvent) || !sFuncEnable || this.mViewRootImpl.mInputEventReceiver == null || this.mViewRootImpl.mContext.getResources().getConfiguration().orientation != 1) {
            return;
        }
        MotionEvent motionEvent = (MotionEvent) inputEvent;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Rect winFrame = this.mViewRootImpl.mWinFrame;
        if (x < 0.0f || x > winFrame.width() || y < 0.0f || y > winFrame.height()) {
            int action = motionEvent.getAction() & 255;
            int pointerCount = motionEvent.getPointerCount();
            if ((action != 0 && !this.mIsInOplusCompatModeOrParallelWindow) || pointerCount > 2) {
                return;
            }
            int touchSlop = ViewConfiguration.get(this.mViewRootImpl.mContext).getScaledTouchSlop();
            if (action == 0) {
                doActionDownWorkIfNeededForBlackBorderArea(motionEvent);
                return;
            }
            if (action == 5) {
                doActionPointerDownWorkIfNeededForBlackBorderArea(motionEvent, touchSlop);
                return;
            }
            if (action == 2) {
                if (!doActionMoveWorkIfNeededForBlackBorderArea(motionEvent, pointerCount, touchSlop)) {
                    return;
                }
            } else if (action == 1) {
                if (!doActionUpWorkIfNeededForBlackBorderArea(motionEvent, touchSlop)) {
                    return;
                }
            } else if (action == 6 && !doActionPointerUpWorkIfNeededForBlackBorderArea(motionEvent)) {
                return;
            }
            adjustXYAxisOfMotifonEventForBlackBorderArea(motionEvent);
            return;
        }
        if (this.mDisableClick && !this.mIsIgnoreAdjustXYAxis) {
            doMotifonEventWorkForAppBorderArea(motionEvent);
        }
    }

    private double calculateFingerDistance(float x1, float y1, float x2, float y2) {
        float lengthX = Math.abs(x1 - x2);
        float lengthY = Math.abs(y1 - y2);
        return Math.sqrt(Math.pow(lengthX, 2.0d) + Math.pow(lengthY, 2.0d));
    }

    private boolean doActionDownWorkIfNeededForBlackBorderArea(MotionEvent motionEvent) {
        if (ResourcesManagerExtImpl.inOplusCompatMode(this.mViewRootImpl.mContext.getResources().getConfiguration()) || this.mViewRootImpl.mContext.getResources().getConfiguration().windowConfiguration.getWindowingMode() == 120) {
            this.mIsInOplusCompatModeOrParallelWindow = true;
        }
        this.mFirstTouchX = motionEvent.getX();
        this.mFirstTouchY = motionEvent.getY();
        this.mDownMotionEvent = MotionEvent.obtain(motionEvent);
        return true;
    }

    private boolean doActionPointerDownWorkIfNeededForBlackBorderArea(MotionEvent motionEvent, int touchSlop) {
        if (motionEvent.getActionIndex() == 1 && this.mDownMotionEvent != null) {
            this.mFirstTouchX = motionEvent.getX();
            this.mFirstTouchY = motionEvent.getY();
            this.mSecondTouchX = motionEvent.getX(1);
            float y = motionEvent.getY(1);
            this.mSecondTouchY = y;
            double calculateFingerDistance = calculateFingerDistance(this.mFirstTouchX, this.mFirstTouchY, this.mSecondTouchX, y);
            this.mFirstTwoFingerPointerLength = calculateFingerDistance;
            if (calculateFingerDistance > touchSlop) {
                this.mPointDownMotionEvent = MotionEvent.obtain(motionEvent);
            }
        }
        return true;
    }

    private boolean doActionMoveWorkIfNeededForBlackBorderArea(MotionEvent motionEvent, int pointerCount, int touchSlop) {
        if (this.mDisableClick && this.mHasAdjustedXYAxisForAppBorderArea && adjustXYAxisOfMotifonEventForAppBorderAreaIfNeeded(motionEvent)) {
            return false;
        }
        if (pointerCount == 2) {
            if (this.mDownMotionEvent == null || this.mPointDownMotionEvent == null) {
                return false;
            }
            float firstFingerX = motionEvent.getX(0);
            float firstFingerY = motionEvent.getY(0);
            float secondFingerX = motionEvent.getX(1);
            float secondFingerY = motionEvent.getY(1);
            double calculateFingerDistance = calculateFingerDistance(firstFingerX, firstFingerY, secondFingerX, secondFingerY);
            this.mSecondTwoFingerPointerLength = calculateFingerDistance;
            if (!this.mDisableClick && Math.abs(calculateFingerDistance - this.mFirstTwoFingerPointerLength) <= touchSlop) {
                return false;
            }
            if (!this.mDisableClick) {
                this.mDisableClick = true;
                this.mIsIgnoreAdjustXYAxis = true;
                adjustXYAxisOfMotifonEventForBlackBorderArea(this.mDownMotionEvent);
                this.mViewRootImpl.mInputEventReceiver.onInputEvent(this.mDownMotionEvent);
                adjustXYAxisOfMotifonEventForBlackBorderArea(this.mPointDownMotionEvent);
                this.mViewRootImpl.mInputEventReceiver.onInputEvent(this.mPointDownMotionEvent);
                this.mIsIgnoreAdjustXYAxis = false;
            }
        } else if (pointerCount == 1) {
            if (this.mDownMotionEvent == null) {
                return false;
            }
            if (this.mPointDownMotionEvent != null) {
                return true;
            }
            double distance = calculateFingerDistance(this.mFirstTouchX, this.mFirstTouchY, motionEvent.getX(), motionEvent.getY());
            boolean z = this.mDisableClick;
            if (!z && distance <= touchSlop) {
                return false;
            }
            if (!z) {
                this.mDisableClick = true;
                this.mIsIgnoreAdjustXYAxis = true;
                adjustXYAxisOfMotifonEventForBlackBorderArea(this.mDownMotionEvent);
                this.mViewRootImpl.mInputEventReceiver.onInputEvent(this.mDownMotionEvent);
                this.mIsIgnoreAdjustXYAxis = false;
            }
        }
        return true;
    }

    private boolean doActionUpWorkIfNeededForBlackBorderArea(MotionEvent motionEvent, int touchSlop) {
        if (this.mDownMotionEvent == null) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        double distance = calculateFingerDistance(this.mFirstTouchX, this.mFirstTouchY, x, y);
        boolean z = this.mDisableClick;
        if (z || distance > touchSlop) {
            return (z && this.mHasAdjustedXYAxisForAppBorderArea && adjustXYAxisOfMotifonEventForAppBorderAreaIfNeeded(motionEvent)) ? false : true;
        }
        return false;
    }

    private boolean doActionPointerUpWorkIfNeededForBlackBorderArea(MotionEvent motionEvent) {
        if (this.mPointDownMotionEvent == null) {
            return false;
        }
        return (this.mDisableClick && this.mHasAdjustedXYAxisForAppBorderArea && adjustXYAxisOfMotifonEventForAppBorderAreaIfNeeded(motionEvent)) ? false : true;
    }

    private void adjustXYAxisOfMotifonEventForBlackBorderArea(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Rect winFrame = new Rect(this.mViewRootImpl.mWinFrame);
        Display display = DisplayManagerGlobal.getInstance().getRealDisplay(this.mViewRootImpl.getDisplayId());
        if (display == null) {
            return;
        }
        Rect displayFrame = new Rect();
        display.getRectSize(displayFrame);
        Rect displayFrame2 = this.mViewRootImpl.getInsetsController().getState().getDisplayFrame();
        if (!displayFrame.equals(displayFrame2)) {
            float scale = displayFrame2.width() / displayFrame.width();
            displayFrame = displayFrame2;
            int width = winFrame.width();
            winFrame.left = (int) (winFrame.left * scale);
            winFrame.right = winFrame.left + width;
        }
        int displayWidth = displayFrame.width();
        int displayHeight = displayFrame.height();
        float windowGlobalScale = this.mViewRootImpl.getWrapper().getExtImpl().getWindowGlobalScale();
        if (windowGlobalScale != 1.0f) {
            displayWidth = (int) ((displayWidth / windowGlobalScale) + 0.5f);
            displayHeight = (int) ((displayHeight / windowGlobalScale) + 0.5f);
        }
        int action = motionEvent.getAction() & 255;
        if (action == 2) {
            this.mMoveXAxisForBlackBorderArea = x;
            this.mMoveYAxisForBlackBorderArea = y;
        }
        if (x < 0.0f) {
            float x2 = x + winFrame.left;
            x = winFrame.right < displayWidth ? x2 * (winFrame.width() / (winFrame.left * TRANSFORM_VARIABLE_TWO)) : x2 * (winFrame.width() / (winFrame.left * 1.0f));
        } else if (x > winFrame.width()) {
            if (winFrame.left <= 0) {
                x = (x - winFrame.right) * (winFrame.width() / ((displayWidth - winFrame.right) * 1.0f));
            } else {
                x = (((x + winFrame.left) - winFrame.right) * (winFrame.width() / ((displayWidth - winFrame.right) * TRANSFORM_VARIABLE_TWO))) + (winFrame.width() / TRANSFORM_VARIABLE_TWO);
            }
        }
        if (y < 0.0f) {
            float y2 = y + winFrame.top;
            y = winFrame.bottom < displayHeight ? y2 * (winFrame.height() / (winFrame.top * TRANSFORM_VARIABLE_TWO)) : y2 * (winFrame.height() / (winFrame.top * 1.0f));
        } else if (y > winFrame.height()) {
            if (winFrame.top <= 0) {
                y = (y - winFrame.bottom) * (winFrame.height() / ((displayHeight - winFrame.bottom) * 1.0f));
            } else {
                y = (((y + winFrame.top) - winFrame.bottom) * (winFrame.height() / ((displayHeight - winFrame.bottom) * TRANSFORM_VARIABLE_TWO))) + (winFrame.height() / TRANSFORM_VARIABLE_TWO);
            }
        }
        motionEvent.setLocation(x, y);
        if (action == 2) {
            this.mAdjustMoveXAxisForBlackBorderArea = x;
            this.mAdjustMoveYAxisForBlackBorderArea = y;
        }
    }

    private void doMotifonEventWorkForAppBorderArea(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        int pointerCount = motionEvent.getPointerCount();
        if (!this.mIsInOplusCompatModeOrParallelWindow) {
            return;
        }
        if (action == 5) {
            doActionPointerDownWorkIfNeededForBlackBorderArea(motionEvent, ViewConfiguration.get(this.mViewRootImpl.mContext).getScaledTouchSlop());
            return;
        }
        if (action == 6) {
            if (this.mPointDownMotionEvent == null) {
                return;
            }
        } else if (action == 2) {
            if (pointerCount == 2) {
                if (this.mDownMotionEvent == null || this.mPointDownMotionEvent == null) {
                    return;
                }
            } else if (pointerCount == 1 && this.mDownMotionEvent == null) {
                return;
            }
        } else if (action == 1 && this.mDownMotionEvent == null) {
            return;
        }
        adjustXYAxisOfMotifonEventForAppBorderArea(motionEvent);
    }

    private boolean adjustXYAxisOfMotifonEventForAppBorderAreaIfNeeded(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Rect winFrame = this.mViewRootImpl.mWinFrame;
        if (x <= 0.0f && this.mMoveXAxisForBlackBorderArea >= winFrame.width()) {
            adjustXYAxisOfMotifonEventForAppBorderArea(motionEvent);
            return true;
        }
        if (x >= winFrame.width() && this.mMoveXAxisForBlackBorderArea <= 0.0f) {
            adjustXYAxisOfMotifonEventForAppBorderArea(motionEvent);
            return true;
        }
        if (y <= 0.0f && this.mMoveYAxisForBlackBorderArea >= winFrame.height()) {
            adjustXYAxisOfMotifonEventForAppBorderArea(motionEvent);
            return true;
        }
        if (y >= winFrame.height() && this.mMoveYAxisForBlackBorderArea <= 0.0f) {
            adjustXYAxisOfMotifonEventForAppBorderArea(motionEvent);
            return true;
        }
        return false;
    }

    private void adjustXYAxisOfMotifonEventForAppBorderArea(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Rect winFrame = new Rect(this.mViewRootImpl.mWinFrame);
        Display display = DisplayManagerGlobal.getInstance().getRealDisplay(this.mViewRootImpl.getDisplayId());
        if (display == null) {
            return;
        }
        Rect displayFrame = new Rect();
        display.getRectSize(displayFrame);
        Rect displayFrame2 = this.mViewRootImpl.getInsetsController().getState().getDisplayFrame();
        if (!displayFrame.equals(displayFrame2)) {
            float scale = displayFrame2.width() / displayFrame.width();
            displayFrame = displayFrame2;
            int width = winFrame.width();
            winFrame.left = (int) (winFrame.left * scale);
            winFrame.right = winFrame.left + width;
        }
        int displayWidth = displayFrame.width();
        int displayHeight = displayFrame.height();
        float windowGlobalScale = this.mViewRootImpl.getWrapper().getExtImpl().getWindowGlobalScale();
        if (windowGlobalScale != 1.0f) {
            displayWidth = (int) ((displayWidth / windowGlobalScale) + 0.5f);
            displayHeight = (int) ((displayHeight / windowGlobalScale) + 0.5f);
        }
        if (winFrame.width() != displayWidth) {
            x = adjustXAxisOfMotifonEventForAppBorderArea(winFrame, x, displayWidth);
        } else {
            y = adjustYAxisOfMotifonEventForAppBorderArea(winFrame, y, displayHeight);
        }
        this.mHasAdjustedXYAxisForAppBorderArea = true;
        motionEvent.setLocation(x, y);
    }

    private float adjustXAxisOfMotifonEventForAppBorderArea(Rect winFrame, float xAxis, int displayWidth) {
        float newXAxis = xAxis;
        if (winFrame.left > 0 && winFrame.right < displayWidth) {
            float f = this.mMoveXAxisForBlackBorderArea;
            if (newXAxis < f) {
                return newXAxis - (winFrame.width() / TRANSFORM_VARIABLE_TWO);
            }
            if (newXAxis > f) {
                return newXAxis + (winFrame.width() / TRANSFORM_VARIABLE_TWO);
            }
            return this.mAdjustMoveXAxisForBlackBorderArea;
        }
        if (winFrame.left == 0) {
            if (newXAxis >= winFrame.width()) {
                newXAxis = winFrame.width() - newXAxis;
            } else {
                newXAxis -= winFrame.width();
            }
        }
        if (winFrame.right == displayWidth) {
            return newXAxis + winFrame.width();
        }
        return newXAxis;
    }

    private float adjustYAxisOfMotifonEventForAppBorderArea(Rect winFrame, float yAxis, int displayHeight) {
        float newYAxis = yAxis;
        if (winFrame.top > 0 && winFrame.bottom < displayHeight) {
            float f = this.mMoveYAxisForBlackBorderArea;
            if (newYAxis < f) {
                return newYAxis - (winFrame.height() / TRANSFORM_VARIABLE_TWO);
            }
            if (newYAxis > f) {
                return newYAxis + (winFrame.height() / TRANSFORM_VARIABLE_TWO);
            }
            return this.mAdjustMoveYAxisForBlackBorderArea;
        }
        if (winFrame.top == 0) {
            if (newYAxis >= winFrame.height()) {
                newYAxis = winFrame.height() - newYAxis;
            } else {
                newYAxis -= winFrame.height();
            }
        }
        if (winFrame.bottom == displayHeight) {
            return newYAxis + winFrame.height();
        }
        return newYAxis;
    }

    public void enableClickIfNeededWhenInputEventFinish(InputEvent inputEvent) {
        if (!(inputEvent instanceof MotionEvent) || !sFuncEnable || this.mViewRootImpl.mInputEventReceiver == null || this.mViewRootImpl.mContext.getResources().getConfiguration().orientation != 1) {
            return;
        }
        MotionEvent motionEvent = (MotionEvent) inputEvent;
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            setInitialState();
        }
    }

    private void setInitialState() {
        this.mIsInOplusCompatModeOrParallelWindow = false;
        this.mDisableClick = false;
        this.mDownMotionEvent = null;
        this.mPointDownMotionEvent = null;
        this.mFirstTouchX = 0.0f;
        this.mFirstTouchY = 0.0f;
        this.mSecondTouchX = 0.0f;
        this.mSecondTouchY = 0.0f;
        this.mFirstTwoFingerPointerLength = 0.0d;
        this.mSecondTwoFingerPointerLength = 0.0d;
        this.mIsIgnoreAdjustXYAxis = false;
        this.mHasAdjustedXYAxisForAppBorderArea = false;
        this.mMoveXAxisForBlackBorderArea = 0.0f;
        this.mMoveYAxisForBlackBorderArea = 0.0f;
        this.mAdjustMoveXAxisForBlackBorderArea = 0.0f;
        this.mAdjustMoveYAxisForBlackBorderArea = 0.0f;
    }

    public boolean isClickDisabled() {
        if (sFuncEnable) {
            return this.mDisableClick;
        }
        return false;
    }
}
