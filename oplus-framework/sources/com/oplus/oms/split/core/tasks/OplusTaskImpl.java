package com.oplus.oms.split.core.tasks;

import java.util.concurrent.Executor;

/* loaded from: classes.dex */
class OplusTaskImpl<T> extends OplusTask<T> {
    private boolean mComplete;
    private Exception mException;
    private T mResult;
    private final Object mLock = new Object();
    private final InvocationListenerManager<T> mListenerManager = new InvocationListenerManager<>();

    private void assertComplete() {
        if (!this.mComplete) {
            throw new IllegalStateException("Task is not yet complete");
        }
    }

    private void invokeListeners() {
        boolean complete;
        synchronized (this.mLock) {
            complete = this.mComplete;
        }
        if (complete) {
            this.mListenerManager.invokeListener(this);
        }
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public T getResult() throws IllegalStateException, OplusRuntimeExecutionException {
        T result;
        synchronized (this.mLock) {
            assertComplete();
            if (this.mException == null) {
                result = this.mResult;
            } else {
                throw new OplusRuntimeExecutionException(this.mException);
            }
        }
        return result;
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public <X extends Throwable> T getResult(Class<X> aClass) throws Throwable {
        return null;
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public boolean isSuccessful() {
        boolean success;
        synchronized (this.mLock) {
            success = this.mComplete && this.mException == null;
        }
        return success;
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public boolean isComplete() {
        boolean complete;
        synchronized (this.mLock) {
            complete = this.mComplete;
        }
        return complete;
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public Exception getException() {
        Exception exception;
        synchronized (this.mLock) {
            exception = this.mException;
        }
        return exception;
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public OplusTask<T> addOnSuccessListener(Executor executor, OplusOnSuccessListener<? super T> listener) {
        this.mListenerManager.addInvocationListener(new InvokeSuccessListener(executor, listener));
        invokeListeners();
        return this;
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public OplusTask<T> addOnSuccessListener(OplusOnSuccessListener<? super T> listener) {
        return addOnSuccessListener(TaskExecutors.MAIN_THREAD, listener);
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public OplusTask<T> addOnFailureListener(Executor executor, OplusOnFailureListener listener) {
        this.mListenerManager.addInvocationListener(new InvokeFailureListener(executor, listener));
        invokeListeners();
        return this;
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public OplusTask<T> addOnFailureListener(OplusOnFailureListener listener) {
        return addOnFailureListener(TaskExecutors.MAIN_THREAD, listener);
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public OplusTask<T> addOnCompleteListener(OplusOnCompleteListener<T> listener) {
        return addOnCompleteListener(TaskExecutors.MAIN_THREAD, listener);
    }

    @Override // com.oplus.oms.split.core.tasks.OplusTask
    public OplusTask<T> addOnCompleteListener(Executor executor, OplusOnCompleteListener<T> listener) {
        this.mListenerManager.addInvocationListener(new InvokeCompleteListener(executor, listener));
        invokeListeners();
        return this;
    }

    public boolean setException(Exception exception) {
        synchronized (this.mLock) {
            if (this.mComplete) {
                return false;
            }
            this.mComplete = true;
            this.mException = exception;
            this.mListenerManager.invokeListener(this);
            return true;
        }
    }

    public boolean setResult(T result) {
        synchronized (this.mLock) {
            if (this.mComplete) {
                return false;
            }
            this.mComplete = true;
            this.mResult = result;
            this.mListenerManager.invokeListener(this);
            return true;
        }
    }
}
