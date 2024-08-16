package com.android.server.wm;

import android.os.Trace;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import android.view.InsetsSource;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.WindowInsets;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.wm.InsetsStateController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class InsetsStateController {
    private final DisplayContent mDisplayContent;
    private final InsetsState mLastState = new InsetsState();
    private final InsetsState mState = new InsetsState();
    private final SparseArray<InsetsSourceProvider> mProviders = new SparseArray<>();
    private final ArrayMap<InsetsControlTarget, ArrayList<InsetsSourceProvider>> mControlTargetProvidersMap = new ArrayMap<>();
    private final SparseArray<InsetsControlTarget> mIdControlTargetMap = new SparseArray<>();
    private final SparseArray<InsetsControlTarget> mIdFakeControlTargetMap = new SparseArray<>();
    private final ArraySet<InsetsControlTarget> mPendingControlChanged = new ArraySet<>();
    private final Consumer<WindowState> mDispatchInsetsChanged = new Consumer() { // from class: com.android.server.wm.InsetsStateController$$ExternalSyntheticLambda3
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            InsetsStateController.lambda$new$0((WindowState) obj);
        }
    };
    private final InsetsControlTarget mEmptyImeControlTarget = new AnonymousClass1();

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(WindowState windowState) {
        if (windowState.isReadyToDispatchInsetsState()) {
            windowState.notifyInsetsChanged();
        }
    }

    /* renamed from: com.android.server.wm.InsetsStateController$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    class AnonymousClass1 implements InsetsControlTarget {
        AnonymousClass1() {
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public void notifyInsetsControlChanged() {
            InsetsSourceControl[] controlsForDispatch = InsetsStateController.this.getControlsForDispatch(this);
            if (controlsForDispatch == null) {
                return;
            }
            for (InsetsSourceControl insetsSourceControl : controlsForDispatch) {
                if (insetsSourceControl != null && insetsSourceControl.getType() == WindowInsets.Type.ime()) {
                    InsetsStateController.this.mDisplayContent.mWmService.mH.post(new Runnable() { // from class: com.android.server.wm.InsetsStateController$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            InsetsStateController.AnonymousClass1.lambda$notifyInsetsControlChanged$0();
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$notifyInsetsControlChanged$0() {
            InputMethodManagerInternal.get().removeImeSurface();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsStateController(DisplayContent displayContent) {
        this.mDisplayContent = displayContent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsState getRawInsetsState() {
        return this.mState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsSourceControl[] getControlsForDispatch(InsetsControlTarget insetsControlTarget) {
        ArrayList<InsetsSourceProvider> arrayList = this.mControlTargetProvidersMap.get(insetsControlTarget);
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        InsetsSourceControl[] insetsSourceControlArr = new InsetsSourceControl[size];
        for (int i = 0; i < size; i++) {
            insetsSourceControlArr[i] = arrayList.get(i).getControl(insetsControlTarget);
        }
        return insetsSourceControlArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseArray<InsetsSourceProvider> getSourceProviders() {
        return this.mProviders;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsSourceProvider getOrCreateSourceProvider(int i, int i2) {
        InsetsSourceProvider insetsSourceProvider;
        InsetsSourceProvider insetsSourceProvider2 = this.mProviders.get(i);
        if (insetsSourceProvider2 != null) {
            return insetsSourceProvider2;
        }
        InsetsSource orCreateSource = this.mState.getOrCreateSource(i, i2);
        if (i == InsetsSource.ID_IME) {
            insetsSourceProvider = new ImeInsetsSourceProvider(orCreateSource, this, this.mDisplayContent);
        } else {
            insetsSourceProvider = new InsetsSourceProvider(orCreateSource, this, this.mDisplayContent);
        }
        this.mProviders.put(i, insetsSourceProvider);
        return insetsSourceProvider;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImeInsetsSourceProvider getImeSourceProvider() {
        return (ImeInsetsSourceProvider) getOrCreateSourceProvider(InsetsSource.ID_IME, WindowInsets.Type.ime());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeSourceProvider(int i) {
        if (i != InsetsSource.ID_IME) {
            this.mState.removeSource(i);
            this.mProviders.remove(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPostLayout() {
        Trace.traceBegin(32L, "ISC.onPostLayout");
        for (int size = this.mProviders.size() - 1; size >= 0; size--) {
            this.mProviders.valueAt(size).onPostLayout();
        }
        if (!this.mLastState.equals(this.mState)) {
            this.mLastState.set(this.mState, true);
            notifyInsetsChanged();
        }
        Trace.traceEnd(32L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAboveInsetsState(boolean z) {
        InsetsState insetsState = new InsetsState();
        insetsState.set(this.mState, WindowInsets.Type.displayCutout() | WindowInsets.Type.systemGestures() | WindowInsets.Type.mandatorySystemGestures());
        SparseArray<InsetsSource> sparseArray = new SparseArray<>();
        ArraySet<WindowState> arraySet = new ArraySet<>();
        this.mDisplayContent.updateAboveInsetsState(insetsState, sparseArray, arraySet);
        if (z) {
            for (int size = arraySet.size() - 1; size >= 0; size--) {
                this.mDispatchInsetsChanged.accept(arraySet.valueAt(size));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDisplayFramesUpdated(boolean z) {
        final ArrayList arrayList = new ArrayList();
        this.mDisplayContent.forAllWindows(new Consumer() { // from class: com.android.server.wm.InsetsStateController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InsetsStateController.this.lambda$onDisplayFramesUpdated$1(arrayList, (WindowState) obj);
            }
        }, true);
        if (z) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                this.mDispatchInsetsChanged.accept((WindowState) arrayList.get(size));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayFramesUpdated$1(ArrayList arrayList, WindowState windowState) {
        windowState.mAboveInsetsState.set(this.mState, WindowInsets.Type.displayCutout());
        arrayList.add(windowState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onInsetsModified(InsetsControlTarget insetsControlTarget) {
        boolean z = false;
        for (int size = this.mProviders.size() - 1; size >= 0; size--) {
            z |= this.mProviders.valueAt(size).updateClientVisibility(insetsControlTarget);
        }
        if (z) {
            notifyInsetsChanged();
            this.mDisplayContent.updateSystemGestureExclusion();
            this.mDisplayContent.updateKeepClearAreas();
            this.mDisplayContent.getDisplayPolicy().updateSystemBarAttributes();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getFakeControllingTypes(InsetsControlTarget insetsControlTarget) {
        int i = 0;
        for (int size = this.mProviders.size() - 1; size >= 0; size--) {
            InsetsSourceProvider valueAt = this.mProviders.valueAt(size);
            if (insetsControlTarget == valueAt.getFakeControlTarget()) {
                i |= valueAt.getSource().getType();
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onImeControlTargetChanged(InsetsControlTarget insetsControlTarget) {
        if (insetsControlTarget == null) {
            insetsControlTarget = this.mEmptyImeControlTarget;
        }
        onControlTargetChanged(getImeSourceProvider(), insetsControlTarget, false);
        if (ProtoLogCache.WM_DEBUG_IME_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_IME, 1658605381, 0, (String) null, new Object[]{String.valueOf(insetsControlTarget != null ? insetsControlTarget.getWindow() : "null")});
        }
        notifyPendingInsetsControlChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBarControlTargetChanged(InsetsControlTarget insetsControlTarget, InsetsControlTarget insetsControlTarget2, InsetsControlTarget insetsControlTarget3, InsetsControlTarget insetsControlTarget4) {
        for (int size = this.mProviders.size() - 1; size >= 0; size--) {
            InsetsSourceProvider valueAt = this.mProviders.valueAt(size);
            int type = valueAt.getSource().getType();
            if (type == WindowInsets.Type.statusBars()) {
                onControlTargetChanged(valueAt, insetsControlTarget, false);
                onControlTargetChanged(valueAt, insetsControlTarget2, true);
            } else if (type == WindowInsets.Type.navigationBars()) {
                onControlTargetChanged(valueAt, insetsControlTarget3, false);
                onControlTargetChanged(valueAt, insetsControlTarget4, true);
            }
        }
        notifyPendingInsetsControlChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyControlRevoked(InsetsControlTarget insetsControlTarget, InsetsSourceProvider insetsSourceProvider) {
        removeFromControlMaps(insetsControlTarget, insetsSourceProvider, false);
    }

    private void onControlTargetChanged(InsetsSourceProvider insetsSourceProvider, InsetsControlTarget insetsControlTarget, boolean z) {
        InsetsControlTarget insetsControlTarget2;
        if (z) {
            insetsControlTarget2 = this.mIdFakeControlTargetMap.get(insetsSourceProvider.getSource().getId());
        } else {
            insetsControlTarget2 = this.mIdControlTargetMap.get(insetsSourceProvider.getSource().getId());
        }
        if (insetsControlTarget != insetsControlTarget2 && insetsSourceProvider.isControllable()) {
            if (z) {
                insetsSourceProvider.updateFakeControlTarget(insetsControlTarget);
            } else {
                insetsSourceProvider.updateControlForTarget(insetsControlTarget, false);
                insetsControlTarget = insetsSourceProvider.getControlTarget();
                if (insetsControlTarget == insetsControlTarget2) {
                    return;
                }
            }
            if (insetsControlTarget2 != null) {
                removeFromControlMaps(insetsControlTarget2, insetsSourceProvider, z);
                this.mPendingControlChanged.add(insetsControlTarget2);
            }
            if (insetsControlTarget != null) {
                addToControlMaps(insetsControlTarget, insetsSourceProvider, z);
                this.mPendingControlChanged.add(insetsControlTarget);
            }
        }
    }

    private void removeFromControlMaps(InsetsControlTarget insetsControlTarget, InsetsSourceProvider insetsSourceProvider, boolean z) {
        ArrayList<InsetsSourceProvider> arrayList = this.mControlTargetProvidersMap.get(insetsControlTarget);
        if (arrayList == null) {
            return;
        }
        arrayList.remove(insetsSourceProvider);
        if (arrayList.isEmpty()) {
            this.mControlTargetProvidersMap.remove(insetsControlTarget);
        }
        if (z) {
            this.mIdFakeControlTargetMap.remove(insetsSourceProvider.getSource().getId());
        } else {
            this.mIdControlTargetMap.remove(insetsSourceProvider.getSource().getId());
        }
    }

    private void addToControlMaps(InsetsControlTarget insetsControlTarget, InsetsSourceProvider insetsSourceProvider, boolean z) {
        this.mControlTargetProvidersMap.computeIfAbsent(insetsControlTarget, new Function() { // from class: com.android.server.wm.InsetsStateController$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ArrayList lambda$addToControlMaps$2;
                lambda$addToControlMaps$2 = InsetsStateController.lambda$addToControlMaps$2((InsetsControlTarget) obj);
                return lambda$addToControlMaps$2;
            }
        }).add(insetsSourceProvider);
        if (z) {
            this.mIdFakeControlTargetMap.put(insetsSourceProvider.getSource().getId(), insetsControlTarget);
        } else {
            this.mIdControlTargetMap.put(insetsSourceProvider.getSource().getId(), insetsControlTarget);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ArrayList lambda$addToControlMaps$2(InsetsControlTarget insetsControlTarget) {
        return new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyControlChanged(InsetsControlTarget insetsControlTarget) {
        this.mPendingControlChanged.add(insetsControlTarget);
        notifyPendingInsetsControlChanged();
    }

    private void notifyPendingInsetsControlChanged() {
        if (this.mPendingControlChanged.isEmpty()) {
            return;
        }
        this.mDisplayContent.mWmService.mAnimator.addAfterPrepareSurfacesRunnable(new Runnable() { // from class: com.android.server.wm.InsetsStateController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                InsetsStateController.this.lambda$notifyPendingInsetsControlChanged$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyPendingInsetsControlChanged$3() {
        for (int size = this.mProviders.size() - 1; size >= 0; size--) {
            this.mProviders.valueAt(size).onSurfaceTransactionApplied();
        }
        ArraySet arraySet = new ArraySet();
        for (int size2 = this.mPendingControlChanged.size() - 1; size2 >= 0; size2--) {
            InsetsControlTarget valueAt = this.mPendingControlChanged.valueAt(size2);
            valueAt.notifyInsetsControlChanged();
            if (this.mControlTargetProvidersMap.containsKey(valueAt)) {
                arraySet.add(valueAt);
            }
        }
        this.mPendingControlChanged.clear();
        for (int size3 = arraySet.size() - 1; size3 >= 0; size3--) {
            onInsetsModified((InsetsControlTarget) arraySet.valueAt(size3));
        }
        arraySet.clear();
    }

    void notifyInsetsChanged() {
        this.mDisplayContent.notifyInsetsChanged(this.mDispatchInsetsChanged);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(String str, PrintWriter printWriter) {
        printWriter.println(str + "WindowInsetsStateController");
        String str2 = str + "  ";
        this.mState.dump(str2, printWriter);
        printWriter.println(str2 + "Control map:");
        for (int size = this.mControlTargetProvidersMap.size() + (-1); size >= 0; size--) {
            InsetsControlTarget keyAt = this.mControlTargetProvidersMap.keyAt(size);
            printWriter.print(str2 + "  ");
            printWriter.print(keyAt);
            printWriter.println(":");
            ArrayList<InsetsSourceProvider> valueAt = this.mControlTargetProvidersMap.valueAt(size);
            for (int size2 = valueAt.size() - 1; size2 >= 0; size2--) {
                InsetsSourceProvider insetsSourceProvider = valueAt.get(size2);
                if (insetsSourceProvider != null) {
                    printWriter.print(str2 + "    ");
                    if (keyAt == insetsSourceProvider.getFakeControlTarget()) {
                        printWriter.print("(fake) ");
                    }
                    printWriter.println(insetsSourceProvider.getControl(keyAt));
                }
            }
        }
        if (this.mControlTargetProvidersMap.isEmpty()) {
            printWriter.print(str2 + "  none");
        }
        printWriter.println(str2 + "InsetsSourceProviders:");
        for (int size3 = this.mProviders.size() + (-1); size3 >= 0; size3 += -1) {
            this.mProviders.valueAt(size3).dump(printWriter, str2 + "  ");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, int i) {
        for (int size = this.mProviders.size() - 1; size >= 0; size--) {
            InsetsSourceProvider valueAt = this.mProviders.valueAt(size);
            valueAt.dumpDebug(protoOutputStream, valueAt.getSource().getType() == WindowInsets.Type.ime() ? 1146756268063L : 2246267895843L, i);
        }
    }
}
