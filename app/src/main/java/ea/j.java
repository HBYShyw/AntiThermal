package ea;

import android.text.TextUtils;
import android.util.SparseArray;
import b6.LocalLog;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* compiled from: State.java */
/* loaded from: classes2.dex */
public class j {

    /* renamed from: d, reason: collision with root package name */
    private static final Object f11016d = new Object();

    /* renamed from: e, reason: collision with root package name */
    @VisibleForTesting
    static j f11017e;

    /* renamed from: f, reason: collision with root package name */
    @VisibleForTesting
    static int f11018f;

    /* renamed from: g, reason: collision with root package name */
    @VisibleForTesting
    static int f11019g;

    /* renamed from: h, reason: collision with root package name */
    private static final SparseArray<String> f11020h;

    /* renamed from: a, reason: collision with root package name */
    @VisibleForTesting
    j f11021a;

    /* renamed from: b, reason: collision with root package name */
    private int f11022b;

    /* renamed from: c, reason: collision with root package name */
    private String f11023c;

    static {
        SparseArray<String> sparseArray = new SparseArray<>();
        f11020h = sparseArray;
        sparseArray.append(101, "watch_video");
        sparseArray.append(102, "audio_call");
        sparseArray.append(103, "voice_call");
        sparseArray.append(104, "playing_game");
        sparseArray.append(105, "video_live");
        sparseArray.append(106, "ai_navigation");
        sparseArray.append(107, "music_play");
        f11019g = sparseArray.size();
    }

    public j(int i10) {
        this.f11022b = i10;
        String str = f11020h.get(i10);
        if (TextUtils.isEmpty(str)) {
            this.f11023c = "null_state";
        } else {
            this.f11023c = str;
        }
    }

    public static List<Integer> a() {
        ArrayList arrayList = new ArrayList();
        int i10 = 0;
        while (true) {
            SparseArray<String> sparseArray = f11020h;
            if (i10 >= sparseArray.size()) {
                return arrayList;
            }
            arrayList.add(Integer.valueOf(sparseArray.keyAt(i10)));
            i10++;
        }
    }

    public static boolean c(int i10) {
        return !TextUtils.isEmpty(f11020h.get(i10));
    }

    public static j d(int i10) {
        j jVar;
        synchronized (f11016d) {
            j jVar2 = f11017e;
            if (jVar2 != null) {
                while (jVar2.b() != i10 && (jVar = jVar2.f11021a) != null) {
                    jVar2 = jVar;
                }
                if (jVar2.b() == i10) {
                    return jVar2;
                }
                j jVar3 = new j(i10);
                int i11 = f11018f;
                if (i11 >= f11019g) {
                    LocalLog.l("State", "the pool size " + f11018f + " is or more than max pool size " + f11019g);
                    return jVar3;
                }
                f11018f = i11 + 1;
                jVar2.f11021a = jVar3;
                return jVar3;
            }
            j jVar4 = new j(i10);
            f11017e = jVar4;
            f11018f++;
            return jVar4;
        }
    }

    public int b() {
        return this.f11022b;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj.getClass() == getClass() && this.f11022b == ((j) obj).f11022b;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.f11022b));
    }

    public String toString() {
        return "name:" + this.f11023c + ",value:" + this.f11022b;
    }
}
