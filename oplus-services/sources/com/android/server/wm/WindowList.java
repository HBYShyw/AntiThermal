package com.android.server.wm;

import android.util.Slog;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowList<E> extends ArrayList<E> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public void addFirst(E e) {
        add(0, e);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public E peekLast() {
        if (size() > 0) {
            return get(size() - 1);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public E peekFirst() {
        if (size() > 0) {
            return get(0);
        }
        return null;
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(Object obj) {
        boolean remove = super.remove(obj);
        if (obj instanceof DisplayContent) {
            Slog.w("WindowManager", "obj = " + obj + ", willRemove = " + remove, new Throwable());
        }
        return remove;
    }
}
