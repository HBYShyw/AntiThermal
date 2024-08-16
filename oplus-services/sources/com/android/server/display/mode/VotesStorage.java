package com.android.server.display.mode;

import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class VotesStorage {
    private static final int GLOBAL_ID = -1;
    private static final String TAG = "VotesStorage";
    private final Listener mListener;
    private boolean mLoggingEnabled;
    private final Object mStorageLock = new Object();

    @GuardedBy({"mStorageLock"})
    private final SparseArray<SparseArray<Vote>> mVotesByDisplay = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Listener {
        void onChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VotesStorage(Listener listener) {
        this.mListener = listener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLoggingEnabled(boolean z) {
        this.mLoggingEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseArray<Vote> getVotes(int i) {
        SparseArray<Vote> clone;
        SparseArray<Vote> clone2;
        synchronized (this.mStorageLock) {
            SparseArray<Vote> sparseArray = this.mVotesByDisplay.get(i);
            clone = sparseArray != null ? sparseArray.clone() : new SparseArray<>();
            SparseArray<Vote> sparseArray2 = this.mVotesByDisplay.get(-1);
            clone2 = sparseArray2 != null ? sparseArray2.clone() : new SparseArray<>();
        }
        for (int i2 = 0; i2 < clone2.size(); i2++) {
            int keyAt = clone2.keyAt(i2);
            if (!clone.contains(keyAt)) {
                clone.put(keyAt, clone2.valueAt(i2));
            }
        }
        return clone;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateGlobalVote(int i, Vote vote) {
        updateVote(-1, i, vote);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateVote(int i, int i2, Vote vote) {
        SparseArray<Vote> sparseArray;
        if (this.mLoggingEnabled) {
            Slog.i(TAG, "updateVoteLocked(displayId=" + i + ", priority=" + Vote.priorityToString(i2) + ", vote=" + vote + ")");
        }
        if (i2 < 0 || i2 > 14) {
            Slog.w(TAG, "Received a vote with an invalid priority, ignoring: priority=" + Vote.priorityToString(i2) + ", vote=" + vote);
            return;
        }
        synchronized (this.mStorageLock) {
            if (this.mVotesByDisplay.contains(i)) {
                sparseArray = this.mVotesByDisplay.get(i);
            } else {
                sparseArray = new SparseArray<>();
                this.mVotesByDisplay.put(i, sparseArray);
            }
            if (vote != null) {
                sparseArray.put(i2, vote);
            } else {
                sparseArray.remove(i2);
            }
        }
        if (this.mLoggingEnabled) {
            Slog.i(TAG, "Updated votes for display=" + i + " votes=" + sparseArray);
        }
        this.mListener.onChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        int i;
        SparseArray sparseArray = new SparseArray();
        synchronized (this.mStorageLock) {
            for (int i2 = 0; i2 < this.mVotesByDisplay.size(); i2++) {
                sparseArray.put(this.mVotesByDisplay.keyAt(i2), this.mVotesByDisplay.valueAt(i2).clone());
            }
        }
        printWriter.println("  mVotesByDisplay:");
        for (i = 0; i < sparseArray.size(); i++) {
            SparseArray sparseArray2 = (SparseArray) sparseArray.valueAt(i);
            if (sparseArray2.size() != 0) {
                printWriter.println("    " + sparseArray.keyAt(i) + ":");
                for (int i3 = 14; i3 >= 0; i3--) {
                    Vote vote = (Vote) sparseArray2.get(i3);
                    if (vote != null) {
                        printWriter.println("      " + Vote.priorityToString(i3) + " -> " + vote);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void injectVotesByDisplay(SparseArray<SparseArray<Vote>> sparseArray) {
        synchronized (this.mStorageLock) {
            this.mVotesByDisplay.clear();
            for (int i = 0; i < sparseArray.size(); i++) {
                this.mVotesByDisplay.put(sparseArray.keyAt(i), sparseArray.valueAt(i));
            }
        }
    }
}
