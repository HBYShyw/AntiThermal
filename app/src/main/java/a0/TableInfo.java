package a0;

import android.database.Cursor;
import c0.SupportSQLiteDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/* compiled from: TableInfo.java */
/* renamed from: a0.d, reason: use source file name */
/* loaded from: classes.dex */
public class TableInfo {

    /* renamed from: a, reason: collision with root package name */
    public final String f4a;

    /* renamed from: b, reason: collision with root package name */
    public final Map<String, a> f5b;

    /* renamed from: c, reason: collision with root package name */
    public final Set<b> f6c;

    /* renamed from: d, reason: collision with root package name */
    public final Set<d> f7d;

    /* compiled from: TableInfo.java */
    /* renamed from: a0.d$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        public final String f8a;

        /* renamed from: b, reason: collision with root package name */
        public final String f9b;

        /* renamed from: c, reason: collision with root package name */
        public final int f10c;

        /* renamed from: d, reason: collision with root package name */
        public final boolean f11d;

        /* renamed from: e, reason: collision with root package name */
        public final int f12e;

        public a(String str, String str2, boolean z10, int i10) {
            this.f8a = str;
            this.f9b = str2;
            this.f11d = z10;
            this.f12e = i10;
            this.f10c = a(str2);
        }

        private static int a(String str) {
            if (str == null) {
                return 5;
            }
            String upperCase = str.toUpperCase(Locale.US);
            if (upperCase.contains("INT")) {
                return 3;
            }
            if (upperCase.contains("CHAR") || upperCase.contains("CLOB") || upperCase.contains("TEXT")) {
                return 2;
            }
            if (upperCase.contains("BLOB")) {
                return 5;
            }
            return (upperCase.contains("REAL") || upperCase.contains("FLOA") || upperCase.contains("DOUB")) ? 4 : 1;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            a aVar = (a) obj;
            return this.f12e == aVar.f12e && this.f8a.equals(aVar.f8a) && this.f11d == aVar.f11d && this.f10c == aVar.f10c;
        }

        public int hashCode() {
            return (((((this.f8a.hashCode() * 31) + this.f10c) * 31) + (this.f11d ? 1231 : 1237)) * 31) + this.f12e;
        }

        public String toString() {
            return "Column{name='" + this.f8a + "', type='" + this.f9b + "', affinity='" + this.f10c + "', notNull=" + this.f11d + ", primaryKeyPosition=" + this.f12e + '}';
        }
    }

    /* compiled from: TableInfo.java */
    /* renamed from: a0.d$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        public final String f13a;

        /* renamed from: b, reason: collision with root package name */
        public final String f14b;

        /* renamed from: c, reason: collision with root package name */
        public final String f15c;

        /* renamed from: d, reason: collision with root package name */
        public final List<String> f16d;

        /* renamed from: e, reason: collision with root package name */
        public final List<String> f17e;

        public b(String str, String str2, String str3, List<String> list, List<String> list2) {
            this.f13a = str;
            this.f14b = str2;
            this.f15c = str3;
            this.f16d = Collections.unmodifiableList(list);
            this.f17e = Collections.unmodifiableList(list2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            b bVar = (b) obj;
            if (this.f13a.equals(bVar.f13a) && this.f14b.equals(bVar.f14b) && this.f15c.equals(bVar.f15c) && this.f16d.equals(bVar.f16d)) {
                return this.f17e.equals(bVar.f17e);
            }
            return false;
        }

        public int hashCode() {
            return (((((((this.f13a.hashCode() * 31) + this.f14b.hashCode()) * 31) + this.f15c.hashCode()) * 31) + this.f16d.hashCode()) * 31) + this.f17e.hashCode();
        }

        public String toString() {
            return "ForeignKey{referenceTable='" + this.f13a + "', onDelete='" + this.f14b + "', onUpdate='" + this.f15c + "', columnNames=" + this.f16d + ", referenceColumnNames=" + this.f17e + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TableInfo.java */
    /* renamed from: a0.d$c */
    /* loaded from: classes.dex */
    public static class c implements Comparable<c> {

        /* renamed from: e, reason: collision with root package name */
        final int f18e;

        /* renamed from: f, reason: collision with root package name */
        final int f19f;

        /* renamed from: g, reason: collision with root package name */
        final String f20g;

        /* renamed from: h, reason: collision with root package name */
        final String f21h;

        c(int i10, int i11, String str, String str2) {
            this.f18e = i10;
            this.f19f = i11;
            this.f20g = str;
            this.f21h = str2;
        }

        @Override // java.lang.Comparable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compareTo(c cVar) {
            int i10 = this.f18e - cVar.f18e;
            return i10 == 0 ? this.f19f - cVar.f19f : i10;
        }
    }

    /* compiled from: TableInfo.java */
    /* renamed from: a0.d$d */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        public final String f22a;

        /* renamed from: b, reason: collision with root package name */
        public final boolean f23b;

        /* renamed from: c, reason: collision with root package name */
        public final List<String> f24c;

        public d(String str, boolean z10, List<String> list) {
            this.f22a = str;
            this.f23b = z10;
            this.f24c = list;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            d dVar = (d) obj;
            if (this.f23b != dVar.f23b || !this.f24c.equals(dVar.f24c)) {
                return false;
            }
            if (this.f22a.startsWith("index_")) {
                return dVar.f22a.startsWith("index_");
            }
            return this.f22a.equals(dVar.f22a);
        }

        public int hashCode() {
            return ((((this.f22a.startsWith("index_") ? -1184239155 : this.f22a.hashCode()) * 31) + (this.f23b ? 1 : 0)) * 31) + this.f24c.hashCode();
        }

        public String toString() {
            return "Index{name='" + this.f22a + "', unique=" + this.f23b + ", columns=" + this.f24c + '}';
        }
    }

    public TableInfo(String str, Map<String, a> map, Set<b> set, Set<d> set2) {
        this.f4a = str;
        this.f5b = Collections.unmodifiableMap(map);
        this.f6c = Collections.unmodifiableSet(set);
        this.f7d = set2 == null ? null : Collections.unmodifiableSet(set2);
    }

    public static TableInfo a(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        return new TableInfo(str, b(supportSQLiteDatabase, str), d(supportSQLiteDatabase, str), f(supportSQLiteDatabase, str));
    }

    private static Map<String, a> b(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        Cursor H = supportSQLiteDatabase.H("PRAGMA table_info(`" + str + "`)");
        HashMap hashMap = new HashMap();
        try {
            if (H.getColumnCount() > 0) {
                int columnIndex = H.getColumnIndex("name");
                int columnIndex2 = H.getColumnIndex("type");
                int columnIndex3 = H.getColumnIndex("notnull");
                int columnIndex4 = H.getColumnIndex("pk");
                while (H.moveToNext()) {
                    String string = H.getString(columnIndex);
                    hashMap.put(string, new a(string, H.getString(columnIndex2), H.getInt(columnIndex3) != 0, H.getInt(columnIndex4)));
                }
            }
            return hashMap;
        } finally {
            H.close();
        }
    }

    private static List<c> c(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("id");
        int columnIndex2 = cursor.getColumnIndex("seq");
        int columnIndex3 = cursor.getColumnIndex("from");
        int columnIndex4 = cursor.getColumnIndex("to");
        int count = cursor.getCount();
        ArrayList arrayList = new ArrayList();
        for (int i10 = 0; i10 < count; i10++) {
            cursor.moveToPosition(i10);
            arrayList.add(new c(cursor.getInt(columnIndex), cursor.getInt(columnIndex2), cursor.getString(columnIndex3), cursor.getString(columnIndex4)));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static Set<b> d(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        HashSet hashSet = new HashSet();
        Cursor H = supportSQLiteDatabase.H("PRAGMA foreign_key_list(`" + str + "`)");
        try {
            int columnIndex = H.getColumnIndex("id");
            int columnIndex2 = H.getColumnIndex("seq");
            int columnIndex3 = H.getColumnIndex("table");
            int columnIndex4 = H.getColumnIndex("on_delete");
            int columnIndex5 = H.getColumnIndex("on_update");
            List<c> c10 = c(H);
            int count = H.getCount();
            for (int i10 = 0; i10 < count; i10++) {
                H.moveToPosition(i10);
                if (H.getInt(columnIndex2) == 0) {
                    int i11 = H.getInt(columnIndex);
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    for (c cVar : c10) {
                        if (cVar.f18e == i11) {
                            arrayList.add(cVar.f20g);
                            arrayList2.add(cVar.f21h);
                        }
                    }
                    hashSet.add(new b(H.getString(columnIndex3), H.getString(columnIndex4), H.getString(columnIndex5), arrayList, arrayList2));
                }
            }
            return hashSet;
        } finally {
            H.close();
        }
    }

    private static d e(SupportSQLiteDatabase supportSQLiteDatabase, String str, boolean z10) {
        Cursor H = supportSQLiteDatabase.H("PRAGMA index_xinfo(`" + str + "`)");
        try {
            int columnIndex = H.getColumnIndex("seqno");
            int columnIndex2 = H.getColumnIndex("cid");
            int columnIndex3 = H.getColumnIndex("name");
            if (columnIndex != -1 && columnIndex2 != -1 && columnIndex3 != -1) {
                TreeMap treeMap = new TreeMap();
                while (H.moveToNext()) {
                    if (H.getInt(columnIndex2) >= 0) {
                        treeMap.put(Integer.valueOf(H.getInt(columnIndex)), H.getString(columnIndex3));
                    }
                }
                ArrayList arrayList = new ArrayList(treeMap.size());
                arrayList.addAll(treeMap.values());
                return new d(str, z10, arrayList);
            }
            return null;
        } finally {
            H.close();
        }
    }

    private static Set<d> f(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        Cursor H = supportSQLiteDatabase.H("PRAGMA index_list(`" + str + "`)");
        try {
            int columnIndex = H.getColumnIndex("name");
            int columnIndex2 = H.getColumnIndex("origin");
            int columnIndex3 = H.getColumnIndex("unique");
            if (columnIndex != -1 && columnIndex2 != -1 && columnIndex3 != -1) {
                HashSet hashSet = new HashSet();
                while (H.moveToNext()) {
                    if ("c".equals(H.getString(columnIndex2))) {
                        String string = H.getString(columnIndex);
                        boolean z10 = true;
                        if (H.getInt(columnIndex3) != 1) {
                            z10 = false;
                        }
                        d e10 = e(supportSQLiteDatabase, string, z10);
                        if (e10 == null) {
                            return null;
                        }
                        hashSet.add(e10);
                    }
                }
                return hashSet;
            }
            return null;
        } finally {
            H.close();
        }
    }

    public boolean equals(Object obj) {
        Set<d> set;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TableInfo tableInfo = (TableInfo) obj;
        String str = this.f4a;
        if (str == null ? tableInfo.f4a != null : !str.equals(tableInfo.f4a)) {
            return false;
        }
        Map<String, a> map = this.f5b;
        if (map == null ? tableInfo.f5b != null : !map.equals(tableInfo.f5b)) {
            return false;
        }
        Set<b> set2 = this.f6c;
        if (set2 == null ? tableInfo.f6c != null : !set2.equals(tableInfo.f6c)) {
            return false;
        }
        Set<d> set3 = this.f7d;
        if (set3 == null || (set = tableInfo.f7d) == null) {
            return true;
        }
        return set3.equals(set);
    }

    public int hashCode() {
        String str = this.f4a;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        Map<String, a> map = this.f5b;
        int hashCode2 = (hashCode + (map != null ? map.hashCode() : 0)) * 31;
        Set<b> set = this.f6c;
        return hashCode2 + (set != null ? set.hashCode() : 0);
    }

    public String toString() {
        return "TableInfo{name='" + this.f4a + "', columns=" + this.f5b + ", foreignKeys=" + this.f6c + ", indices=" + this.f7d + '}';
    }
}
