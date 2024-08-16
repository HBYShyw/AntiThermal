package com.android.server.selectiontoolbar;

import android.content.Context;
import android.util.Slog;
import android.view.selectiontoolbar.ISelectionToolbarCallback;
import android.view.selectiontoolbar.ISelectionToolbarManager;
import android.view.selectiontoolbar.ShowInfo;
import com.android.internal.util.DumpUtils;
import com.android.server.infra.AbstractMasterSystemService;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SelectionToolbarManagerService extends AbstractMasterSystemService<SelectionToolbarManagerService, SelectionToolbarManagerServiceImpl> {
    private static final String TAG = "SelectionToolbarManagerService";

    public void onStart() {
        publishBinderService("selection_toolbar", new SelectionToolbarManagerServiceStub());
    }

    public SelectionToolbarManagerService(Context context) {
        super(context, new SelectionToolbarServiceNameResolver(), null, 4);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public SelectionToolbarManagerServiceImpl newServiceLocked(int i, boolean z) {
        return new SelectionToolbarManagerServiceImpl(this, this.mLock, i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class SelectionToolbarManagerServiceStub extends ISelectionToolbarManager.Stub {
        SelectionToolbarManagerServiceStub() {
        }

        public void showToolbar(ShowInfo showInfo, ISelectionToolbarCallback iSelectionToolbarCallback, int i) {
            synchronized (((AbstractMasterSystemService) SelectionToolbarManagerService.this).mLock) {
                SelectionToolbarManagerServiceImpl selectionToolbarManagerServiceImpl = (SelectionToolbarManagerServiceImpl) SelectionToolbarManagerService.this.getServiceForUserLocked(i);
                if (selectionToolbarManagerServiceImpl != null) {
                    selectionToolbarManagerServiceImpl.showToolbar(showInfo, iSelectionToolbarCallback);
                } else {
                    Slog.v(SelectionToolbarManagerService.TAG, "showToolbar(): no service for " + i);
                }
            }
        }

        public void hideToolbar(long j, int i) {
            synchronized (((AbstractMasterSystemService) SelectionToolbarManagerService.this).mLock) {
                SelectionToolbarManagerServiceImpl selectionToolbarManagerServiceImpl = (SelectionToolbarManagerServiceImpl) SelectionToolbarManagerService.this.getServiceForUserLocked(i);
                if (selectionToolbarManagerServiceImpl != null) {
                    selectionToolbarManagerServiceImpl.hideToolbar(j);
                } else {
                    Slog.v(SelectionToolbarManagerService.TAG, "hideToolbar(): no service for " + i);
                }
            }
        }

        public void dismissToolbar(long j, int i) {
            synchronized (((AbstractMasterSystemService) SelectionToolbarManagerService.this).mLock) {
                SelectionToolbarManagerServiceImpl selectionToolbarManagerServiceImpl = (SelectionToolbarManagerServiceImpl) SelectionToolbarManagerService.this.getServiceForUserLocked(i);
                if (selectionToolbarManagerServiceImpl != null) {
                    selectionToolbarManagerServiceImpl.dismissToolbar(j);
                } else {
                    Slog.v(SelectionToolbarManagerService.TAG, "dismissToolbar(): no service for " + i);
                }
            }
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(SelectionToolbarManagerService.this.getContext(), SelectionToolbarManagerService.TAG, printWriter)) {
                synchronized (((AbstractMasterSystemService) SelectionToolbarManagerService.this).mLock) {
                    SelectionToolbarManagerService.this.dumpLocked("", printWriter);
                }
            }
        }
    }
}
