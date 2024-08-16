package f4;

import com.google.android.play.core.b.OnFailureListenerWrapper;
import com.google.android.play.core.b.OnSuccessListenerWrapper;
import com.oplus.oms.split.core.tasks.OplusTask;
import g4.OnFailureListener;
import g4.c;
import g4.d;

/* compiled from: TaskWrapper.java */
/* renamed from: f4.b, reason: use source file name */
/* loaded from: classes.dex */
public class TaskWrapper<T> extends d<T> {

    /* renamed from: a, reason: collision with root package name */
    public final OplusTask<T> f11355a;

    public TaskWrapper(OplusTask<T> oplusTask) {
        this.f11355a = oplusTask;
    }

    @Override // g4.d
    public d<T> a(OnFailureListener onFailureListener) {
        this.f11355a.addOnFailureListener(new OnFailureListenerWrapper(onFailureListener));
        return this;
    }

    @Override // g4.d
    public d<T> b(c<? super T> cVar) {
        this.f11355a.addOnSuccessListener(new OnSuccessListenerWrapper(cVar));
        return this;
    }
}
