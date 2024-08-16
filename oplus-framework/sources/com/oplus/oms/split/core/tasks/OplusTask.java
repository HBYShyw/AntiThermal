package com.oplus.oms.split.core.tasks;

import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public abstract class OplusTask<T> {
    public abstract OplusTask<T> addOnCompleteListener(OplusOnCompleteListener<T> oplusOnCompleteListener);

    public abstract OplusTask<T> addOnCompleteListener(Executor executor, OplusOnCompleteListener<T> oplusOnCompleteListener);

    public abstract OplusTask<T> addOnFailureListener(OplusOnFailureListener oplusOnFailureListener);

    public abstract OplusTask<T> addOnFailureListener(Executor executor, OplusOnFailureListener oplusOnFailureListener);

    public abstract OplusTask<T> addOnSuccessListener(OplusOnSuccessListener<? super T> oplusOnSuccessListener);

    public abstract OplusTask<T> addOnSuccessListener(Executor executor, OplusOnSuccessListener<? super T> oplusOnSuccessListener);

    public abstract Exception getException();

    public abstract T getResult() throws IllegalStateException, OplusRuntimeExecutionException;

    public abstract <X extends Throwable> T getResult(Class<X> cls) throws Throwable;

    public abstract boolean isComplete();

    public abstract boolean isSuccessful();
}
