package y;

import c0.SupportSQLiteProgram;
import c0.SupportSQLiteQuery;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/* compiled from: RoomSQLiteQuery.java */
/* renamed from: y.d, reason: use source file name */
/* loaded from: classes.dex */
public class RoomSQLiteQuery implements SupportSQLiteQuery, SupportSQLiteProgram {

    /* renamed from: m, reason: collision with root package name */
    static final TreeMap<Integer, RoomSQLiteQuery> f19733m = new TreeMap<>();

    /* renamed from: e, reason: collision with root package name */
    private volatile String f19734e;

    /* renamed from: f, reason: collision with root package name */
    final long[] f19735f;

    /* renamed from: g, reason: collision with root package name */
    final double[] f19736g;

    /* renamed from: h, reason: collision with root package name */
    final String[] f19737h;

    /* renamed from: i, reason: collision with root package name */
    final byte[][] f19738i;

    /* renamed from: j, reason: collision with root package name */
    private final int[] f19739j;

    /* renamed from: k, reason: collision with root package name */
    final int f19740k;

    /* renamed from: l, reason: collision with root package name */
    int f19741l;

    private RoomSQLiteQuery(int i10) {
        this.f19740k = i10;
        int i11 = i10 + 1;
        this.f19739j = new int[i11];
        this.f19735f = new long[i11];
        this.f19736g = new double[i11];
        this.f19737h = new String[i11];
        this.f19738i = new byte[i11];
    }

    public static RoomSQLiteQuery m(String str, int i10) {
        TreeMap<Integer, RoomSQLiteQuery> treeMap = f19733m;
        synchronized (treeMap) {
            Map.Entry<Integer, RoomSQLiteQuery> ceilingEntry = treeMap.ceilingEntry(Integer.valueOf(i10));
            if (ceilingEntry != null) {
                treeMap.remove(ceilingEntry.getKey());
                RoomSQLiteQuery value = ceilingEntry.getValue();
                value.u(str, i10);
                return value;
            }
            RoomSQLiteQuery roomSQLiteQuery = new RoomSQLiteQuery(i10);
            roomSQLiteQuery.u(str, i10);
            return roomSQLiteQuery;
        }
    }

    private static void v() {
        TreeMap<Integer, RoomSQLiteQuery> treeMap = f19733m;
        if (treeMap.size() <= 15) {
            return;
        }
        int size = treeMap.size() - 10;
        Iterator<Integer> it = treeMap.descendingKeySet().iterator();
        while (true) {
            int i10 = size - 1;
            if (size <= 0) {
                return;
            }
            it.next();
            it.remove();
            size = i10;
        }
    }

    @Override // c0.SupportSQLiteProgram
    public void C(int i10, byte[] bArr) {
        this.f19739j[i10] = 5;
        this.f19738i[i10] = bArr;
    }

    @Override // c0.SupportSQLiteProgram
    public void Y(int i10) {
        this.f19739j[i10] = 1;
    }

    @Override // c0.SupportSQLiteQuery
    public String b() {
        return this.f19734e;
    }

    @Override // c0.SupportSQLiteQuery
    public void c(SupportSQLiteProgram supportSQLiteProgram) {
        for (int i10 = 1; i10 <= this.f19741l; i10++) {
            int i11 = this.f19739j[i10];
            if (i11 == 1) {
                supportSQLiteProgram.Y(i10);
            } else if (i11 == 2) {
                supportSQLiteProgram.y(i10, this.f19735f[i10]);
            } else if (i11 == 3) {
                supportSQLiteProgram.r(i10, this.f19736g[i10]);
            } else if (i11 == 4) {
                supportSQLiteProgram.j(i10, this.f19737h[i10]);
            } else if (i11 == 5) {
                supportSQLiteProgram.C(i10, this.f19738i[i10]);
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // c0.SupportSQLiteProgram
    public void j(int i10, String str) {
        this.f19739j[i10] = 4;
        this.f19737h[i10] = str;
    }

    @Override // c0.SupportSQLiteProgram
    public void r(int i10, double d10) {
        this.f19739j[i10] = 3;
        this.f19736g[i10] = d10;
    }

    void u(String str, int i10) {
        this.f19734e = str;
        this.f19741l = i10;
    }

    public void w() {
        TreeMap<Integer, RoomSQLiteQuery> treeMap = f19733m;
        synchronized (treeMap) {
            treeMap.put(Integer.valueOf(this.f19740k), this);
            v();
        }
    }

    @Override // c0.SupportSQLiteProgram
    public void y(int i10, long j10) {
        this.f19739j[i10] = 2;
        this.f19735f[i10] = j10;
    }
}
