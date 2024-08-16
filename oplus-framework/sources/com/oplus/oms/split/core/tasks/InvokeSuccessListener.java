package com.oplus.oms.split.core.tasks;

import java.util.concurrent.Executor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class InvokeSuccessListener<T> implements InvocationListener<T> {
    private final Executor mExecutor;
    final OplusOnSuccessListener<? super T> mListener;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InvokeSuccessListener(Executor executor, OplusOnSuccessListener<? super T> listener) {
        this.mExecutor = executor;
        this.mListener = listener;
    }

    @Override // com.oplus.oms.split.core.tasks.InvocationListener
    public void invoke(OplusTask<T> task) {
        if (this.mListener != null && task.isSuccessful()) {
            this.mExecutor.execute(new OplusTaskSuccessRunnable(this, task));
        }
    }
}
