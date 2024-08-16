package androidx.lifecycle;

import androidx.lifecycle.h;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class FullLifecycleObserverAdapter implements LifecycleEventObserver {

    /* renamed from: e, reason: collision with root package name */
    private final FullLifecycleObserver f3075e;

    /* renamed from: f, reason: collision with root package name */
    private final LifecycleEventObserver f3076f;

    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f3077a;

        static {
            int[] iArr = new int[h.b.values().length];
            f3077a = iArr;
            try {
                iArr[h.b.ON_CREATE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3077a[h.b.ON_START.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3077a[h.b.ON_RESUME.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3077a[h.b.ON_PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f3077a[h.b.ON_STOP.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f3077a[h.b.ON_DESTROY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f3077a[h.b.ON_ANY.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FullLifecycleObserverAdapter(FullLifecycleObserver fullLifecycleObserver, LifecycleEventObserver lifecycleEventObserver) {
        this.f3075e = fullLifecycleObserver;
        this.f3076f = lifecycleEventObserver;
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        switch (a.f3077a[bVar.ordinal()]) {
            case 1:
                this.f3075e.onCreate(oVar);
                break;
            case 2:
                this.f3075e.onStart(oVar);
                break;
            case 3:
                this.f3075e.onResume(oVar);
                break;
            case 4:
                this.f3075e.onPause(oVar);
                break;
            case 5:
                this.f3075e.onStop(oVar);
                break;
            case 6:
                this.f3075e.onDestroy(oVar);
                break;
            case 7:
                throw new IllegalArgumentException("ON_ANY must not been send by anybody");
        }
        LifecycleEventObserver lifecycleEventObserver = this.f3076f;
        if (lifecycleEventObserver != null) {
            lifecycleEventObserver.a(oVar, bVar);
        }
    }
}
