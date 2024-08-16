package n0;

import android.os.Handler;
import android.os.Looper;
import o0.SceneDataMemoryDataSource;
import s0.n;

/* compiled from: CryptoSceneDataRepository.java */
/* renamed from: n0.b, reason: use source file name */
/* loaded from: classes.dex */
public class CryptoSceneDataRepository {

    /* renamed from: a, reason: collision with root package name */
    private final SceneDataMemoryDataSource f15624a = new SceneDataMemoryDataSource();

    /* renamed from: b, reason: collision with root package name */
    private final Handler f15625b = new Handler(Looper.getMainLooper());

    public void b(final String str, final n nVar) {
        this.f15624a.a(str, nVar);
        this.f15625b.postDelayed(new Runnable() { // from class: n0.a
            @Override // java.lang.Runnable
            public final void run() {
                CryptoSceneDataRepository.this.d(str, nVar);
            }
        }, nVar.c());
    }

    public n c(String str, String str2) {
        return this.f15624a.c(str, str2);
    }

    /* renamed from: e, reason: merged with bridge method [inline-methods] */
    public void d(String str, n nVar) {
        this.f15624a.d(str, nVar);
    }
}
