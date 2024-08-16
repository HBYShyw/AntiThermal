package com.android.server.wm;

import android.util.ArraySet;
import android.util.Slog;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class AnimatingActivityRegistry {
    private boolean mEndingDeferredFinish;
    private ArraySet<ActivityRecord> mAnimatingActivities = new ArraySet<>();
    private LinkedHashMap<ActivityRecord, Runnable> mFinishedTokens = new LinkedHashMap<>();
    private ArrayList<Runnable> mTmpRunnableList = new ArrayList<>();
    private AnimatingActivityRegistryWrapper mWrapper = new AnimatingActivityRegistryWrapper();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyStarting(ActivityRecord activityRecord) {
        if (WindowManagerDebugConfig.DEBUG_ANIM) {
            Slog.i("AnimatingActivityRegistry", "Animation of " + activityRecord + " is starting.");
        }
        this.mAnimatingActivities.add(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyFinished(ActivityRecord activityRecord) {
        if (WindowManagerDebugConfig.DEBUG_ANIM) {
            Slog.i("AnimatingActivityRegistry", "Animation of " + activityRecord + " is finished.");
        }
        this.mAnimatingActivities.remove(activityRecord);
        this.mFinishedTokens.remove(activityRecord);
        if (this.mAnimatingActivities.isEmpty()) {
            endDeferringFinished();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean notifyAboutToFinish(ActivityRecord activityRecord, Runnable runnable) {
        if (!this.mAnimatingActivities.remove(activityRecord)) {
            return false;
        }
        if (WindowManagerDebugConfig.DEBUG_ANIM) {
            Slog.i("AnimatingActivityRegistry", "Animation of " + activityRecord + " is about to finish.");
        }
        if (this.mAnimatingActivities.isEmpty() && !this.mWrapper.getExtImpl().shouldDeferAnimatingActivityFinished(activityRecord)) {
            this.mFinishedTokens.put(activityRecord, runnable);
            endDeferringFinished();
            return false;
        }
        this.mFinishedTokens.put(activityRecord, runnable);
        return true;
    }

    private void endDeferringFinished() {
        if (this.mEndingDeferredFinish) {
            return;
        }
        try {
            this.mEndingDeferredFinish = true;
            this.mWrapper.getExtImpl().makeRunnableList(this.mFinishedTokens, this.mTmpRunnableList);
            this.mFinishedTokens.clear();
            for (int i = 0; i < this.mTmpRunnableList.size(); i++) {
                this.mTmpRunnableList.get(i).run();
            }
            this.mTmpRunnableList.clear();
        } finally {
            this.mEndingDeferredFinish = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, String str2) {
        if (this.mAnimatingActivities.isEmpty() && this.mFinishedTokens.isEmpty()) {
            return;
        }
        printWriter.print(str2);
        printWriter.println(str);
        String str3 = str2 + "  ";
        printWriter.print(str3);
        printWriter.print("mAnimatingActivities=");
        printWriter.println(this.mAnimatingActivities);
        printWriter.print(str3);
        printWriter.print("mFinishedTokens=");
        printWriter.println(this.mFinishedTokens);
    }

    public IAnimatingActivityRegistryWrapper getWrapper() {
        return this.mWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AnimatingActivityRegistryWrapper implements IAnimatingActivityRegistryWrapper {
        private IAnimatingActivityRegistryExt mAnimatingActivityRegistryExt;

        private AnimatingActivityRegistryWrapper() {
            this.mAnimatingActivityRegistryExt = (IAnimatingActivityRegistryExt) ExtLoader.type(IAnimatingActivityRegistryExt.class).base(AnimatingActivityRegistry.this).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IAnimatingActivityRegistryExt getExtImpl() {
            return this.mAnimatingActivityRegistryExt;
        }

        @Override // com.android.server.wm.IAnimatingActivityRegistryWrapper
        public LinkedHashMap<ActivityRecord, Runnable> getFinishedTokens() {
            return AnimatingActivityRegistry.this.mFinishedTokens;
        }
    }
}
