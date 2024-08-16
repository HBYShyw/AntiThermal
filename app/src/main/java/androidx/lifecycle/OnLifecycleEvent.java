package androidx.lifecycle;

import androidx.lifecycle.h;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* compiled from: OnLifecycleEvent.java */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
/* renamed from: androidx.lifecycle.w, reason: use source file name */
/* loaded from: classes.dex */
public @interface OnLifecycleEvent {
    h.b value();
}
