package com.android.server.wm;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.view.IWindow;
import android.view.InputWindowHandle;
import android.view.MagnificationSpec;
import android.view.WindowInfo;
import android.window.WindowInfosListener;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.DumpUtils;
import com.android.server.wm.AccessibilityWindowsPopulator;
import com.android.server.wm.utils.RegionUtils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class AccessibilityWindowsPopulator extends WindowInfosListener {
    private static final int SURFACE_FLINGER_CALLBACK_WINDOWS_STABLE_TIMES_MS = 35;
    private static final int WINDOWS_CHANGED_NOTIFICATION_MAX_DURATION_TIMES_MS = 450;
    private final AccessibilityController mAccessibilityController;
    private final Handler mHandler;
    private final WindowManagerService mService;
    private static final String TAG = AccessibilityWindowsPopulator.class.getSimpleName();
    private static final float[] sTempFloats = new float[9];

    @GuardedBy({"mLock"})
    private final SparseArray<List<InputWindowHandle>> mInputWindowHandlesOnDisplays = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final SparseArray<Matrix> mMagnificationSpecInverseMatrix = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final SparseArray<WindowInfosListener.DisplayInfo> mDisplayInfos = new SparseArray<>();
    private final SparseArray<MagnificationSpec> mCurrentMagnificationSpec = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final SparseArray<MagnificationSpec> mPreviousMagnificationSpec = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final List<InputWindowHandle> mVisibleWindows = new ArrayList();

    @GuardedBy({"mLock"})
    private boolean mWindowsNotificationEnabled = false;

    @GuardedBy({"mLock"})
    private final Map<IBinder, Matrix> mWindowsTransformMatrixMap = new HashMap();
    private final Object mLock = new Object();
    private final Matrix mTempMatrix1 = new Matrix();
    private final Matrix mTempMatrix2 = new Matrix();
    private final float[] mTempFloat1 = new float[9];
    private final float[] mTempFloat2 = new float[9];
    private final float[] mTempFloat3 = new float[9];

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$1(int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccessibilityWindowsPopulator(WindowManagerService windowManagerService, AccessibilityController accessibilityController) {
        this.mService = windowManagerService;
        this.mAccessibilityController = accessibilityController;
        this.mHandler = new MyHandler(windowManagerService.mH.getLooper());
    }

    public void populateVisibleWindowsOnScreenLocked(int i, List<AccessibilityWindow> list) {
        Matrix matrix = new Matrix();
        Matrix matrix2 = new Matrix();
        synchronized (this.mLock) {
            List<InputWindowHandle> list2 = this.mInputWindowHandlesOnDisplays.get(i);
            if (list2 == null) {
                list.clear();
                return;
            }
            matrix.set(this.mMagnificationSpecInverseMatrix.get(i));
            WindowInfosListener.DisplayInfo displayInfo = this.mDisplayInfos.get(i);
            if (displayInfo != null) {
                matrix2.set(displayInfo.mTransform);
            } else {
                Slog.w(TAG, "The displayInfo of this displayId (" + i + ") called back from the surface fligner is null");
            }
            ShellRoot shellRoot = this.mService.mRoot.getDisplayContent(i).mShellRoots.get(1);
            IBinder accessibilityWindowToken = shellRoot != null ? shellRoot.getAccessibilityWindowToken() : null;
            Iterator<InputWindowHandle> it = list2.iterator();
            while (it.hasNext()) {
                list.add(AccessibilityWindow.initializeData(this.mService, it.next(), matrix, accessibilityWindowToken, matrix2));
            }
        }
    }

    public void onWindowInfosChanged(final InputWindowHandle[] inputWindowHandleArr, final WindowInfosListener.DisplayInfo[] displayInfoArr) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                AccessibilityWindowsPopulator.this.lambda$onWindowInfosChanged$0(inputWindowHandleArr, displayInfoArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onWindowInfosChangedInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onWindowInfosChanged$0(InputWindowHandle[] inputWindowHandleArr, WindowInfosListener.DisplayInfo[] displayInfoArr) {
        ArrayList arrayList = new ArrayList();
        for (InputWindowHandle inputWindowHandle : inputWindowHandleArr) {
            int i = inputWindowHandle.inputConfig;
            boolean z = (i & 2) == 0;
            boolean z2 = (i & 65536) == 0;
            boolean z3 = !inputWindowHandle.touchableRegion.isEmpty();
            boolean z4 = (inputWindowHandle.frameBottom == inputWindowHandle.frameTop || inputWindowHandle.frameLeft == inputWindowHandle.frameRight) ? false : true;
            if (z && z2 && z3 && z4) {
                arrayList.add(inputWindowHandle);
            }
        }
        HashMap<IBinder, Matrix> windowsTransformMatrix = getWindowsTransformMatrix(arrayList);
        synchronized (this.mLock) {
            this.mWindowsTransformMatrixMap.clear();
            this.mWindowsTransformMatrixMap.putAll(windowsTransformMatrix);
            this.mVisibleWindows.clear();
            this.mVisibleWindows.addAll(arrayList);
            this.mDisplayInfos.clear();
            for (WindowInfosListener.DisplayInfo displayInfo : displayInfoArr) {
                this.mDisplayInfos.put(displayInfo.mDisplayId, displayInfo);
            }
            if (this.mWindowsNotificationEnabled) {
                if (!this.mHandler.hasMessages(3)) {
                    this.mHandler.sendEmptyMessageDelayed(3, 450L);
                }
                populateVisibleWindowHandlesAndNotifyWindowsChangeIfNeeded();
            }
        }
    }

    private HashMap<IBinder, Matrix> getWindowsTransformMatrix(List<InputWindowHandle> list) {
        HashMap<IBinder, Matrix> hashMap;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                hashMap = new HashMap<>();
                Iterator<InputWindowHandle> it = list.iterator();
                while (it.hasNext()) {
                    IWindow window = it.next().getWindow();
                    WindowState windowState = window != null ? this.mService.mWindowMap.get(window.asBinder()) : null;
                    if (windowState != null && windowState.shouldMagnify()) {
                        Matrix matrix = new Matrix();
                        windowState.getTransformationMatrix(sTempFloats, matrix);
                        hashMap.put(window.asBinder(), matrix);
                    }
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return hashMap;
    }

    public void setWindowsNotification(boolean z) {
        synchronized (this.mLock) {
            if (this.mWindowsNotificationEnabled == z) {
                return;
            }
            this.mWindowsNotificationEnabled = z;
            if (z) {
                Pair register = register();
                lambda$onWindowInfosChanged$0((InputWindowHandle[]) register.first, (WindowInfosListener.DisplayInfo[]) register.second);
            } else {
                unregister();
                releaseResources();
            }
        }
    }

    public void setMagnificationSpec(int i, MagnificationSpec magnificationSpec) {
        synchronized (this.mLock) {
            MagnificationSpec magnificationSpec2 = this.mCurrentMagnificationSpec.get(i);
            if (magnificationSpec2 == null) {
                MagnificationSpec magnificationSpec3 = new MagnificationSpec();
                magnificationSpec3.setTo(magnificationSpec);
                this.mCurrentMagnificationSpec.put(i, magnificationSpec3);
            } else {
                MagnificationSpec magnificationSpec4 = this.mPreviousMagnificationSpec.get(i);
                if (magnificationSpec4 == null) {
                    magnificationSpec4 = new MagnificationSpec();
                    this.mPreviousMagnificationSpec.put(i, magnificationSpec4);
                }
                magnificationSpec4.setTo(magnificationSpec2);
                magnificationSpec2.setTo(magnificationSpec);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void populateVisibleWindowHandlesAndNotifyWindowsChangeIfNeeded() {
        SparseArray<List<InputWindowHandle>> sparseArray = new SparseArray<>();
        synchronized (this.mLock) {
            for (InputWindowHandle inputWindowHandle : this.mVisibleWindows) {
                List<InputWindowHandle> list = sparseArray.get(inputWindowHandle.displayId);
                if (list == null) {
                    list = new ArrayList<>();
                    sparseArray.put(inputWindowHandle.displayId, list);
                }
                list.add(inputWindowHandle);
            }
        }
        findMagnificationSpecInverseMatrixIfNeeded(sparseArray);
        ArrayList arrayList = new ArrayList();
        getDisplaysForWindowsChanged(arrayList, sparseArray, this.mInputWindowHandlesOnDisplays);
        this.mInputWindowHandlesOnDisplays.clear();
        for (int i = 0; i < sparseArray.size(); i++) {
            int keyAt = sparseArray.keyAt(i);
            this.mInputWindowHandlesOnDisplays.put(keyAt, sparseArray.get(keyAt));
        }
        if (!arrayList.isEmpty()) {
            if (this.mHandler.hasMessages(1)) {
                return;
            }
            this.mHandler.obtainMessage(1, arrayList).sendToTarget();
        } else {
            this.mHandler.removeMessages(2);
            this.mHandler.sendEmptyMessageDelayed(2, 35L);
        }
    }

    @GuardedBy({"mLock"})
    private static void getDisplaysForWindowsChanged(List<Integer> list, SparseArray<List<InputWindowHandle>> sparseArray, SparseArray<List<InputWindowHandle>> sparseArray2) {
        for (int i = 0; i < sparseArray.size(); i++) {
            int keyAt = sparseArray.keyAt(i);
            if (hasWindowsChanged(sparseArray.get(keyAt), sparseArray2.get(keyAt))) {
                list.add(Integer.valueOf(keyAt));
            }
        }
    }

    @GuardedBy({"mLock"})
    private static boolean hasWindowsChanged(List<InputWindowHandle> list, List<InputWindowHandle> list2) {
        if (list2 == null || list2.size() != list.size()) {
            return true;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            IWindow window = list.get(i).getWindow();
            IWindow window2 = list2.get(i).getWindow();
            boolean z = window != null;
            boolean z2 = window2 != null;
            if (z != z2) {
                return true;
            }
            if (z && z2 && !window.asBinder().equals(window2.asBinder())) {
                return true;
            }
        }
        return false;
    }

    @GuardedBy({"mLock"})
    private void findMagnificationSpecInverseMatrixIfNeeded(SparseArray<List<InputWindowHandle>> sparseArray) {
        for (int i = 0; i < sparseArray.size(); i++) {
            int keyAt = sparseArray.keyAt(i);
            List<InputWindowHandle> list = sparseArray.get(keyAt);
            MagnificationSpec magnificationSpec = this.mCurrentMagnificationSpec.get(keyAt);
            if (magnificationSpec != null) {
                MagnificationSpec magnificationSpec2 = new MagnificationSpec();
                magnificationSpec2.setTo(magnificationSpec);
                MagnificationSpec magnificationSpec3 = this.mPreviousMagnificationSpec.get(keyAt);
                if (magnificationSpec3 == null) {
                    Matrix matrix = new Matrix();
                    generateInverseMatrix(magnificationSpec2, matrix);
                    this.mMagnificationSpecInverseMatrix.put(keyAt, matrix);
                } else {
                    MagnificationSpec magnificationSpec4 = new MagnificationSpec();
                    magnificationSpec4.setTo(magnificationSpec3);
                    generateInverseMatrixBasedOnProperMagnificationSpecForDisplay(list, magnificationSpec2, magnificationSpec4);
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private void generateInverseMatrixBasedOnProperMagnificationSpecForDisplay(List<InputWindowHandle> list, MagnificationSpec magnificationSpec, MagnificationSpec magnificationSpec2) {
        for (int size = list.size() - 1; size >= 0; size--) {
            Matrix matrix = this.mTempMatrix2;
            InputWindowHandle inputWindowHandle = list.get(size);
            if (getWindowTransformMatrix(inputWindowHandle.getWindow() != null ? inputWindowHandle.getWindow().asBinder() : null, matrix)) {
                generateMagnificationSpecInverseMatrix(inputWindowHandle, magnificationSpec, magnificationSpec2, matrix);
                return;
            }
        }
    }

    @GuardedBy({"mLock"})
    private boolean getWindowTransformMatrix(IBinder iBinder, Matrix matrix) {
        Matrix matrix2 = iBinder != null ? this.mWindowsTransformMatrixMap.get(iBinder) : null;
        if (matrix2 == null) {
            return false;
        }
        matrix.set(matrix2);
        return true;
    }

    @GuardedBy({"mLock"})
    private void generateMagnificationSpecInverseMatrix(InputWindowHandle inputWindowHandle, MagnificationSpec magnificationSpec, MagnificationSpec magnificationSpec2, Matrix matrix) {
        float[] fArr = this.mTempFloat1;
        computeIdentityMatrix(inputWindowHandle, magnificationSpec, matrix, fArr);
        float[] fArr2 = this.mTempFloat2;
        computeIdentityMatrix(inputWindowHandle, magnificationSpec2, matrix, fArr2);
        Matrix matrix2 = new Matrix();
        if (selectProperMagnificationSpecByComparingIdentityDegree(fArr, fArr2)) {
            generateInverseMatrix(magnificationSpec, matrix2);
            this.mPreviousMagnificationSpec.remove(inputWindowHandle.displayId);
            if (magnificationSpec.isNop()) {
                this.mCurrentMagnificationSpec.remove(inputWindowHandle.displayId);
                this.mMagnificationSpecInverseMatrix.remove(inputWindowHandle.displayId);
                return;
            }
        } else {
            generateInverseMatrix(magnificationSpec2, matrix2);
        }
        this.mMagnificationSpecInverseMatrix.put(inputWindowHandle.displayId, matrix2);
    }

    @GuardedBy({"mLock"})
    private void computeIdentityMatrix(InputWindowHandle inputWindowHandle, MagnificationSpec magnificationSpec, Matrix matrix, float[] fArr) {
        Matrix matrix2 = this.mTempMatrix1;
        transformMagnificationSpecToMatrix(magnificationSpec, matrix2);
        Matrix matrix3 = new Matrix(inputWindowHandle.transform);
        matrix3.preConcat(matrix2);
        matrix3.preConcat(matrix);
        matrix3.getValues(fArr);
    }

    @GuardedBy({"mLock"})
    private boolean selectProperMagnificationSpecByComparingIdentityDegree(float[] fArr, float[] fArr2) {
        float[] fArr3 = this.mTempFloat3;
        Matrix.IDENTITY_MATRIX.getValues(fArr3);
        float abs = Math.abs(fArr3[0] - fArr[0]);
        float abs2 = Math.abs(fArr3[0] - fArr2[0]);
        float abs3 = Math.abs(fArr3[2] - fArr[2]);
        return Float.compare(abs2, abs) > 0 || (Float.compare(abs2, abs) == 0 && Float.compare(Math.abs(fArr3[2] - fArr2[2]) + Math.abs(fArr3[5] - fArr2[5]), abs3 + Math.abs(fArr3[5] - fArr[5])) > 0);
    }

    @GuardedBy({"mLock"})
    private static void generateInverseMatrix(MagnificationSpec magnificationSpec, Matrix matrix) {
        matrix.reset();
        Matrix matrix2 = new Matrix();
        transformMagnificationSpecToMatrix(magnificationSpec, matrix2);
        if (matrix2.invert(matrix)) {
            return;
        }
        Slog.e(TAG, "Can't inverse the magnification spec matrix with the magnification spec = " + magnificationSpec);
        matrix.reset();
    }

    @GuardedBy({"mLock"})
    private static void transformMagnificationSpecToMatrix(MagnificationSpec magnificationSpec, Matrix matrix) {
        matrix.reset();
        float f = magnificationSpec.scale;
        matrix.postScale(f, f);
        matrix.postTranslate(magnificationSpec.offsetX, magnificationSpec.offsetY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWindowsChanged(List<Integer> list) {
        this.mHandler.removeMessages(3);
        for (int i = 0; i < list.size(); i++) {
            this.mAccessibilityController.performComputeChangedWindowsNot(list.get(i).intValue(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceUpdateWindows() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mInputWindowHandlesOnDisplays.size(); i++) {
                arrayList.add(Integer.valueOf(this.mInputWindowHandlesOnDisplays.keyAt(i)));
            }
        }
        notifyWindowsChanged(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(final PrintWriter printWriter, final String str) {
        printWriter.print(str);
        printWriter.println("AccessibilityWindowsPopulator");
        String str2 = str + "  ";
        printWriter.print(str2);
        printWriter.print("mWindowsNotificationEnabled: ");
        printWriter.println(this.mWindowsNotificationEnabled);
        synchronized (this.mLock) {
            if (this.mVisibleWindows.isEmpty()) {
                printWriter.print(str2);
                printWriter.println("No visible windows");
            } else {
                printWriter.print(str2);
                printWriter.print(this.mVisibleWindows.size());
                printWriter.print(" visible windows: ");
                printWriter.println(this.mVisibleWindows);
            }
        }
        DumpUtils.KeyDumper keyDumper = new DumpUtils.KeyDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda0
            public final void dump(int i, int i2) {
                AccessibilityWindowsPopulator.lambda$dump$1(i, i2);
            }
        };
        DumpUtils.KeyDumper keyDumper2 = new DumpUtils.KeyDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda1
            public final void dump(int i, int i2) {
                AccessibilityWindowsPopulator.lambda$dump$2(printWriter, str, i, i2);
            }
        };
        DumpUtils.ValueDumper valueDumper = new DumpUtils.ValueDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda2
            public final void dump(Object obj) {
                printWriter.print((MagnificationSpec) obj);
            }
        };
        DumpUtils.dumpSparseArray(printWriter, str2, this.mDisplayInfos, "display info", keyDumper, new DumpUtils.ValueDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda3
            public final void dump(Object obj) {
                printWriter.print((WindowInfosListener.DisplayInfo) obj);
            }
        });
        DumpUtils.dumpSparseArray(printWriter, str2, this.mInputWindowHandlesOnDisplays, "window handles on display", keyDumper2, new DumpUtils.ValueDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda4
            public final void dump(Object obj) {
                printWriter.print((List) obj);
            }
        });
        DumpUtils.dumpSparseArray(printWriter, str2, this.mMagnificationSpecInverseMatrix, "magnification spec matrix", keyDumper, new DumpUtils.ValueDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda5
            public final void dump(Object obj) {
                ((Matrix) obj).dump(printWriter);
            }
        });
        DumpUtils.dumpSparseArray(printWriter, str2, this.mCurrentMagnificationSpec, "current magnification spec", keyDumper, valueDumper);
        DumpUtils.dumpSparseArray(printWriter, str2, this.mPreviousMagnificationSpec, "previous magnification spec", keyDumper, valueDumper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$2(PrintWriter printWriter, String str, int i, int i2) {
        printWriter.printf("%sDisplay #%d: ", str, Integer.valueOf(i2));
    }

    @GuardedBy({"mLock"})
    private void releaseResources() {
        this.mInputWindowHandlesOnDisplays.clear();
        this.mMagnificationSpecInverseMatrix.clear();
        synchronized (this.mLock) {
            this.mVisibleWindows.clear();
        }
        this.mDisplayInfos.clear();
        this.mCurrentMagnificationSpec.clear();
        this.mPreviousMagnificationSpec.clear();
        this.mWindowsTransformMatrixMap.clear();
        this.mWindowsNotificationEnabled = false;
        this.mHandler.removeCallbacksAndMessages(null);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class MyHandler extends Handler {
        public static final int MESSAGE_NOTIFY_WINDOWS_CHANGED = 1;
        public static final int MESSAGE_NOTIFY_WINDOWS_CHANGED_BY_TIMEOUT = 3;
        public static final int MESSAGE_NOTIFY_WINDOWS_CHANGED_BY_UI_STABLE = 2;

        MyHandler(Looper looper) {
            super(looper, null, false);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                AccessibilityWindowsPopulator.this.notifyWindowsChanged((List) message.obj);
            } else if (i == 2) {
                AccessibilityWindowsPopulator.this.forceUpdateWindows();
            } else {
                if (i != 3) {
                    return;
                }
                Slog.w(AccessibilityWindowsPopulator.TAG, "Windows change within in 2 frames continuously over 500 ms and notify windows changed immediately");
                AccessibilityWindowsPopulator.this.mHandler.removeMessages(2);
                AccessibilityWindowsPopulator.this.forceUpdateWindows();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class AccessibilityWindow {
        private int mDisplayId;
        private boolean mIgnoreDuetoRecentsAnimation;
        private int mInputConfig;
        private boolean mIsFocused;
        private boolean mIsPIPMenu;
        private int mPrivateFlags;
        private boolean mShouldMagnify;
        private final Region mTouchableRegionInScreen = new Region();
        private final Region mTouchableRegionInWindow = new Region();
        private int mType;
        private IWindow mWindow;
        private WindowInfo mWindowInfo;

        public static AccessibilityWindow initializeData(WindowManagerService windowManagerService, InputWindowHandle inputWindowHandle, Matrix matrix, IBinder iBinder, Matrix matrix2) {
            IWindow window = inputWindowHandle.getWindow();
            WindowState windowState = window != null ? windowManagerService.mWindowMap.get(window.asBinder()) : null;
            AccessibilityWindow accessibilityWindow = new AccessibilityWindow();
            accessibilityWindow.mWindow = window;
            accessibilityWindow.mDisplayId = inputWindowHandle.displayId;
            accessibilityWindow.mInputConfig = inputWindowHandle.inputConfig;
            accessibilityWindow.mType = inputWindowHandle.layoutParamsType;
            accessibilityWindow.mIsPIPMenu = window != null && window.asBinder().equals(iBinder);
            accessibilityWindow.mPrivateFlags = windowState != null ? windowState.mAttrs.privateFlags : 0;
            accessibilityWindow.mIsFocused = windowState != null && windowState.isFocused();
            accessibilityWindow.mShouldMagnify = windowState == null || windowState.shouldMagnify();
            RecentsAnimationController recentsAnimationController = windowManagerService.getRecentsAnimationController();
            accessibilityWindow.mIgnoreDuetoRecentsAnimation = (windowState == null || recentsAnimationController == null || !recentsAnimationController.shouldIgnoreForAccessibility(windowState)) ? false : true;
            getTouchableRegionInWindow(accessibilityWindow.mShouldMagnify, inputWindowHandle.touchableRegion, accessibilityWindow.mTouchableRegionInWindow, new Rect(inputWindowHandle.frameLeft, inputWindowHandle.frameTop, inputWindowHandle.frameRight, inputWindowHandle.frameBottom), matrix, matrix2);
            getUnMagnifiedTouchableRegion(accessibilityWindow.mShouldMagnify, inputWindowHandle.touchableRegion, accessibilityWindow.mTouchableRegionInScreen, matrix, matrix2);
            accessibilityWindow.mWindowInfo = windowState != null ? windowState.getWindowInfo() : getWindowInfoForWindowlessWindows(accessibilityWindow);
            Matrix matrix3 = new Matrix();
            inputWindowHandle.transform.invert(matrix3);
            matrix3.postConcat(matrix2);
            matrix3.getValues(accessibilityWindow.mWindowInfo.mTransformMatrix);
            Matrix matrix4 = new Matrix();
            if (accessibilityWindow.shouldMagnify() && matrix != null && !matrix.isIdentity()) {
                if (matrix.invert(matrix4)) {
                    matrix4.getValues(AccessibilityWindowsPopulator.sTempFloats);
                    MagnificationSpec magnificationSpec = accessibilityWindow.mWindowInfo.mMagnificationSpec;
                    magnificationSpec.scale = AccessibilityWindowsPopulator.sTempFloats[0];
                    magnificationSpec.offsetX = AccessibilityWindowsPopulator.sTempFloats[2];
                    magnificationSpec.offsetY = AccessibilityWindowsPopulator.sTempFloats[5];
                } else {
                    Slog.w(AccessibilityWindowsPopulator.TAG, "can't find spec");
                }
            }
            return accessibilityWindow;
        }

        public void getTouchableRegionInScreen(Region region) {
            region.set(this.mTouchableRegionInScreen);
        }

        public void getTouchableRegionInWindow(Region region) {
            region.set(this.mTouchableRegionInWindow);
        }

        public int getType() {
            return this.mType;
        }

        public int getPrivateFlag() {
            return this.mPrivateFlags;
        }

        public WindowInfo getWindowInfo() {
            return this.mWindowInfo;
        }

        public boolean shouldMagnify() {
            return this.mShouldMagnify;
        }

        public boolean isFocused() {
            return this.mIsFocused;
        }

        public boolean ignoreRecentsAnimationForAccessibility() {
            return this.mIgnoreDuetoRecentsAnimation;
        }

        public boolean isTrustedOverlay() {
            return (this.mInputConfig & 256) != 0;
        }

        public boolean isTouchable() {
            return (this.mInputConfig & 8) == 0;
        }

        public boolean isUntouchableNavigationBar() {
            if (this.mType != 2019) {
                return false;
            }
            return this.mTouchableRegionInScreen.isEmpty();
        }

        public boolean isPIPMenu() {
            return this.mIsPIPMenu;
        }

        private static void getTouchableRegionInWindow(boolean z, Region region, Region region2, Rect rect, Matrix matrix, Matrix matrix2) {
            Region region3 = new Region();
            region3.set(region);
            region3.op(rect, Region.Op.INTERSECT);
            getUnMagnifiedTouchableRegion(z, region3, region2, matrix, matrix2);
        }

        private static void getUnMagnifiedTouchableRegion(boolean z, Region region, final Region region2, final Matrix matrix, final Matrix matrix2) {
            if ((!z || matrix.isIdentity()) && matrix2.isIdentity()) {
                region2.set(region);
            } else {
                RegionUtils.forEachRect(region, new Consumer() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$AccessibilityWindow$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        AccessibilityWindowsPopulator.AccessibilityWindow.lambda$getUnMagnifiedTouchableRegion$0(matrix2, matrix, region2, (Rect) obj);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getUnMagnifiedTouchableRegion$0(Matrix matrix, Matrix matrix2, Region region, Rect rect) {
            RectF rectF = new RectF(rect);
            matrix.mapRect(rectF);
            matrix2.mapRect(rectF);
            region.union(new Rect((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        }

        private static WindowInfo getWindowInfoForWindowlessWindows(AccessibilityWindow accessibilityWindow) {
            WindowInfo obtain = WindowInfo.obtain();
            obtain.displayId = accessibilityWindow.mDisplayId;
            obtain.type = accessibilityWindow.mType;
            IWindow iWindow = accessibilityWindow.mWindow;
            obtain.token = iWindow != null ? iWindow.asBinder() : null;
            obtain.hasFlagWatchOutsideTouch = (accessibilityWindow.mInputConfig & 512) != 0;
            obtain.inPictureInPicture = accessibilityWindow.mIsPIPMenu;
            return obtain;
        }

        public String toString() {
            IWindow iWindow = this.mWindow;
            return "A11yWindow=[" + (iWindow != null ? iWindow.asBinder().toString() : "(no window token)") + ", displayId=" + this.mDisplayId + ", inputConfig=0x" + Integer.toHexString(this.mInputConfig) + ", type=" + this.mType + ", privateFlag=0x" + Integer.toHexString(this.mPrivateFlags) + ", focused=" + this.mIsFocused + ", shouldMagnify=" + this.mShouldMagnify + ", ignoreDuetoRecentsAnimation=" + this.mIgnoreDuetoRecentsAnimation + ", isTrustedOverlay=" + isTrustedOverlay() + ", regionInScreen=" + this.mTouchableRegionInScreen + ", touchableRegion=" + this.mTouchableRegionInWindow + ", isPIPMenu=" + this.mIsPIPMenu + ", windowInfo=" + this.mWindowInfo + "]";
        }
    }
}
