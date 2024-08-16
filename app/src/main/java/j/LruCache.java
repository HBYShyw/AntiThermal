package j;

import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.InnerUtils;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/* compiled from: LruCache.java */
/* renamed from: j.e, reason: use source file name */
/* loaded from: classes.dex */
public class LruCache<K, V> {

    /* renamed from: a, reason: collision with root package name */
    private final LinkedHashMap<K, V> f12894a;

    /* renamed from: b, reason: collision with root package name */
    private int f12895b;

    /* renamed from: c, reason: collision with root package name */
    private int f12896c;

    /* renamed from: d, reason: collision with root package name */
    private int f12897d;

    /* renamed from: e, reason: collision with root package name */
    private int f12898e;

    /* renamed from: f, reason: collision with root package name */
    private int f12899f;

    /* renamed from: g, reason: collision with root package name */
    private int f12900g;

    /* renamed from: h, reason: collision with root package name */
    private int f12901h;

    public LruCache(int i10) {
        if (i10 > 0) {
            this.f12896c = i10;
            this.f12894a = new LinkedHashMap<>(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    private int e(K k10, V v7) {
        int f10 = f(k10, v7);
        if (f10 >= 0) {
            return f10;
        }
        throw new IllegalStateException("Negative size: " + k10 + InnerUtils.EQUAL + v7);
    }

    protected V a(K k10) {
        return null;
    }

    protected void b(boolean z10, K k10, V v7, V v10) {
    }

    public final V c(K k10) {
        V v7;
        Objects.requireNonNull(k10, "key == null");
        synchronized (this) {
            V v10 = this.f12894a.get(k10);
            if (v10 != null) {
                this.f12900g++;
                return v10;
            }
            this.f12901h++;
            V a10 = a(k10);
            if (a10 == null) {
                return null;
            }
            synchronized (this) {
                this.f12898e++;
                v7 = (V) this.f12894a.put(k10, a10);
                if (v7 != null) {
                    this.f12894a.put(k10, v7);
                } else {
                    this.f12895b += e(k10, a10);
                }
            }
            if (v7 != null) {
                b(false, k10, a10, v7);
                return v7;
            }
            g(this.f12896c);
            return a10;
        }
    }

    public final V d(K k10, V v7) {
        V put;
        if (k10 != null && v7 != null) {
            synchronized (this) {
                this.f12897d++;
                this.f12895b += e(k10, v7);
                put = this.f12894a.put(k10, v7);
                if (put != null) {
                    this.f12895b -= e(k10, put);
                }
            }
            if (put != null) {
                b(false, k10, put, v7);
            }
            g(this.f12896c);
            return put;
        }
        throw new NullPointerException("key == null || value == null");
    }

    protected int f(K k10, V v7) {
        return 1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0070, code lost:
    
        throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void g(int i10) {
        K key;
        V value;
        while (true) {
            synchronized (this) {
                if (this.f12895b >= 0 && (!this.f12894a.isEmpty() || this.f12895b == 0)) {
                    if (this.f12895b <= i10 || this.f12894a.isEmpty()) {
                        break;
                    }
                    Map.Entry<K, V> next = this.f12894a.entrySet().iterator().next();
                    key = next.getKey();
                    value = next.getValue();
                    this.f12894a.remove(key);
                    this.f12895b -= e(key, value);
                    this.f12899f++;
                } else {
                    break;
                }
            }
            b(true, key, value, null);
        }
    }

    public final synchronized String toString() {
        int i10;
        int i11;
        i10 = this.f12900g;
        i11 = this.f12901h + i10;
        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.f12896c), Integer.valueOf(this.f12900g), Integer.valueOf(this.f12901h), Integer.valueOf(i11 != 0 ? (i10 * 100) / i11 : 0));
    }
}
