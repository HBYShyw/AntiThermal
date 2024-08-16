package com.android.server.accessibility;

import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Slog;
import android.view.MagnificationSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IAccessibilityInteractionConnection;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ActionReplacingCallback extends IAccessibilityInteractionConnectionCallback.Stub {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "ActionReplacingCallback";
    private final IAccessibilityInteractionConnection mConnectionWithReplacementActions;
    private final int mInteractionId;

    @GuardedBy({"mLock"})
    AccessibilityNodeInfo mNodeFromOriginalWindow;

    @GuardedBy({"mLock"})
    AccessibilityNodeInfo mNodeWithReplacementActions;
    private final int mNodeWithReplacementActionsInteractionId;

    @GuardedBy({"mLock"})
    List<AccessibilityNodeInfo> mNodesFromOriginalWindow;

    @GuardedBy({"mLock"})
    List<AccessibilityNodeInfo> mPrefetchedNodesFromOriginalWindow;

    @GuardedBy({"mLock"})
    private boolean mReplacementNodeIsReadyOrFailed;
    private final IAccessibilityInteractionConnectionCallback mServiceCallback;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    boolean mSetFindNodeFromOriginalWindowCalled = false;

    @GuardedBy({"mLock"})
    boolean mSetFindNodesFromOriginalWindowCalled = false;

    @GuardedBy({"mLock"})
    boolean mSetPrefetchFromOriginalWindowCalled = false;

    public ActionReplacingCallback(IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, IAccessibilityInteractionConnection iAccessibilityInteractionConnection, int i, int i2, long j) {
        this.mServiceCallback = iAccessibilityInteractionConnectionCallback;
        this.mConnectionWithReplacementActions = iAccessibilityInteractionConnection;
        this.mInteractionId = i;
        int i3 = i + 1;
        this.mNodeWithReplacementActionsInteractionId = i3;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                iAccessibilityInteractionConnection.findAccessibilityNodeInfoByAccessibilityId(AccessibilityNodeInfo.ROOT_NODE_ID, (Region) null, i3, this, 0, i2, j, (MagnificationSpec) null, (float[]) null, (Bundle) null);
            } catch (RemoteException unused) {
                this.mReplacementNodeIsReadyOrFailed = true;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setFindAccessibilityNodeInfoResult(AccessibilityNodeInfo accessibilityNodeInfo, int i) {
        synchronized (this.mLock) {
            if (i == this.mInteractionId) {
                this.mNodeFromOriginalWindow = accessibilityNodeInfo;
                this.mSetFindNodeFromOriginalWindowCalled = true;
            } else if (i == this.mNodeWithReplacementActionsInteractionId) {
                this.mNodeWithReplacementActions = accessibilityNodeInfo;
                this.mReplacementNodeIsReadyOrFailed = true;
            } else {
                Slog.e(LOG_TAG, "Callback with unexpected interactionId");
                return;
            }
            replaceInfoActionsAndCallServiceIfReady();
        }
    }

    public void setFindAccessibilityNodeInfosResult(List<AccessibilityNodeInfo> list, int i) {
        synchronized (this.mLock) {
            if (i == this.mInteractionId) {
                this.mNodesFromOriginalWindow = list;
                this.mSetFindNodesFromOriginalWindowCalled = true;
            } else if (i == this.mNodeWithReplacementActionsInteractionId) {
                setNodeWithReplacementActionsFromList(list);
                this.mReplacementNodeIsReadyOrFailed = true;
            } else {
                Slog.e(LOG_TAG, "Callback with unexpected interactionId");
                return;
            }
            replaceInfoActionsAndCallServiceIfReady();
        }
    }

    public void setPrefetchAccessibilityNodeInfoResult(List<AccessibilityNodeInfo> list, int i) throws RemoteException {
        synchronized (this.mLock) {
            if (i == this.mInteractionId) {
                this.mPrefetchedNodesFromOriginalWindow = list;
                this.mSetPrefetchFromOriginalWindowCalled = true;
                replaceInfoActionsAndCallServiceIfReady();
                return;
            }
            Slog.e(LOG_TAG, "Callback with unexpected interactionId");
        }
    }

    private void replaceInfoActionsAndCallServiceIfReady() {
        replaceInfoActionsAndCallService();
        replaceInfosActionsAndCallService();
        replacePrefetchInfosActionsAndCallService();
    }

    private void setNodeWithReplacementActionsFromList(List<AccessibilityNodeInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = list.get(i);
            if (accessibilityNodeInfo.getSourceNodeId() == AccessibilityNodeInfo.ROOT_NODE_ID) {
                this.mNodeWithReplacementActions = accessibilityNodeInfo;
            }
        }
    }

    public void setPerformAccessibilityActionResult(boolean z, int i) throws RemoteException {
        this.mServiceCallback.setPerformAccessibilityActionResult(z, i);
    }

    public void sendTakeScreenshotOfWindowError(int i, int i2) throws RemoteException {
        this.mServiceCallback.sendTakeScreenshotOfWindowError(i, i2);
    }

    private void replaceInfoActionsAndCallService() {
        boolean z;
        AccessibilityNodeInfo accessibilityNodeInfo;
        AccessibilityNodeInfo accessibilityNodeInfo2;
        synchronized (this.mLock) {
            z = this.mReplacementNodeIsReadyOrFailed && this.mSetFindNodeFromOriginalWindowCalled;
            if (z && (accessibilityNodeInfo2 = this.mNodeFromOriginalWindow) != null) {
                replaceActionsOnInfoLocked(accessibilityNodeInfo2);
                this.mSetFindNodeFromOriginalWindowCalled = false;
            }
            accessibilityNodeInfo = this.mNodeFromOriginalWindow;
        }
        if (z) {
            try {
                this.mServiceCallback.setFindAccessibilityNodeInfoResult(accessibilityNodeInfo, this.mInteractionId);
            } catch (RemoteException unused) {
            }
        }
    }

    private void replaceInfosActionsAndCallService() {
        boolean z;
        List<AccessibilityNodeInfo> list;
        synchronized (this.mLock) {
            z = this.mReplacementNodeIsReadyOrFailed && this.mSetFindNodesFromOriginalWindowCalled;
            if (z) {
                list = replaceActionsLocked(this.mNodesFromOriginalWindow);
                this.mSetFindNodesFromOriginalWindowCalled = false;
            } else {
                list = null;
            }
        }
        if (z) {
            try {
                this.mServiceCallback.setFindAccessibilityNodeInfosResult(list, this.mInteractionId);
            } catch (RemoteException unused) {
            }
        }
    }

    private void replacePrefetchInfosActionsAndCallService() {
        boolean z;
        List<AccessibilityNodeInfo> list;
        synchronized (this.mLock) {
            z = this.mReplacementNodeIsReadyOrFailed && this.mSetPrefetchFromOriginalWindowCalled;
            if (z) {
                list = replaceActionsLocked(this.mPrefetchedNodesFromOriginalWindow);
                this.mSetPrefetchFromOriginalWindowCalled = false;
            } else {
                list = null;
            }
        }
        if (z) {
            try {
                this.mServiceCallback.setPrefetchAccessibilityNodeInfoResult(list, this.mInteractionId);
            } catch (RemoteException unused) {
            }
        }
    }

    @GuardedBy({"mLock"})
    private List<AccessibilityNodeInfo> replaceActionsLocked(List<AccessibilityNodeInfo> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                replaceActionsOnInfoLocked(list.get(i));
            }
        }
        if (list == null) {
            return null;
        }
        return new ArrayList(list);
    }

    @GuardedBy({"mLock"})
    private void replaceActionsOnInfoLocked(AccessibilityNodeInfo accessibilityNodeInfo) {
        AccessibilityNodeInfo accessibilityNodeInfo2;
        accessibilityNodeInfo.removeAllActions();
        accessibilityNodeInfo.setClickable(false);
        accessibilityNodeInfo.setFocusable(false);
        accessibilityNodeInfo.setContextClickable(false);
        accessibilityNodeInfo.setScrollable(false);
        accessibilityNodeInfo.setLongClickable(false);
        accessibilityNodeInfo.setDismissable(false);
        if (accessibilityNodeInfo.getSourceNodeId() != AccessibilityNodeInfo.ROOT_NODE_ID || (accessibilityNodeInfo2 = this.mNodeWithReplacementActions) == null) {
            return;
        }
        List<AccessibilityNodeInfo.AccessibilityAction> actionList = accessibilityNodeInfo2.getActionList();
        if (actionList != null) {
            for (int i = 0; i < actionList.size(); i++) {
                accessibilityNodeInfo.addAction(actionList.get(i));
            }
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS);
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        }
        accessibilityNodeInfo.setClickable(this.mNodeWithReplacementActions.isClickable());
        accessibilityNodeInfo.setFocusable(this.mNodeWithReplacementActions.isFocusable());
        accessibilityNodeInfo.setContextClickable(this.mNodeWithReplacementActions.isContextClickable());
        accessibilityNodeInfo.setScrollable(this.mNodeWithReplacementActions.isScrollable());
        accessibilityNodeInfo.setLongClickable(this.mNodeWithReplacementActions.isLongClickable());
        accessibilityNodeInfo.setDismissable(this.mNodeWithReplacementActions.isDismissable());
    }
}
