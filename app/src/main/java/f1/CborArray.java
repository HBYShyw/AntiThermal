package f1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: CborArray.java */
/* renamed from: f1.b, reason: use source file name */
/* loaded from: classes.dex */
public class CborArray extends CborObject {

    /* renamed from: c, reason: collision with root package name */
    private final ArrayList<CborObject> f11265c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f11266d;

    public CborArray() {
        this(-1L);
    }

    @Override // f1.CborObject
    public boolean equals(Object obj) {
        if (!(obj instanceof CborArray)) {
            return false;
        }
        CborArray cborArray = (CborArray) obj;
        return this.f11266d == cborArray.f11266d && super.equals(obj) && this.f11265c.equals(cborArray.f11265c);
    }

    public CborArray g(CborObject cborObject) {
        this.f11265c.add(cborObject);
        return this;
    }

    public List<CborObject> h() {
        return this.f11265c;
    }

    @Override // f1.CborObject
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Boolean.valueOf(this.f11266d), this.f11265c);
    }

    public boolean i() {
        return this.f11266d;
    }

    public CborArray j(boolean z10) {
        this.f11266d = z10;
        return this;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder("[");
        if (i()) {
            sb2.append("_ ");
        }
        sb2.append(Arrays.toString(this.f11265c.toArray()).substring(1));
        return sb2.toString();
    }

    public CborArray(long j10) {
        super(4, j10);
        this.f11266d = false;
        this.f11265c = new ArrayList<>();
    }

    public CborArray(JSONArray jSONArray) {
        this();
        CborObject d10;
        CborObject cborMap;
        for (int i10 = 0; i10 < jSONArray.length(); i10++) {
            if (jSONArray.isNull(i10)) {
                d10 = new CborSimpleValue(CborSimpleValueEnum.NULL);
            } else {
                Object opt = jSONArray.opt(i10);
                if (opt instanceof JSONArray) {
                    cborMap = new CborArray((JSONArray) opt);
                } else if (opt instanceof JSONObject) {
                    cborMap = new CborMap((JSONObject) opt);
                } else {
                    try {
                        d10 = CborHelper.d(opt);
                    } catch (CborException e10) {
                        e1.i.b("CborArray", "CborArray analyze error. " + e10);
                    }
                }
                d10 = cborMap;
            }
            this.f11265c.add(d10);
        }
    }

    public CborArray(Object[] objArr) {
        this();
        for (Object obj : objArr) {
            this.f11265c.add(CborHelper.d(obj));
        }
    }
}
