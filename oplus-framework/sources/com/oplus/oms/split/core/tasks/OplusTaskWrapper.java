package com.oplus.oms.split.core.tasks;

/* loaded from: classes.dex */
public class OplusTaskWrapper<T> {
    private final OplusTaskImpl<T> mTask = new OplusTaskImpl<>();

    public final OplusTask<T> getTask() {
        return this.mTask;
    }

    public final boolean setException(Exception exception) {
        return this.mTask.setException(exception);
    }

    public final boolean setResult(T result) {
        return this.mTask.setResult(result);
    }
}
