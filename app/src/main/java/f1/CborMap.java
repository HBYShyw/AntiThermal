package f1;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: CborMap.java */
/* renamed from: f1.k, reason: use source file name */
/* loaded from: classes.dex */
public class CborMap extends CborObject {

    /* renamed from: c, reason: collision with root package name */
    private final LinkedHashMap<CborObject, CborObject> f11277c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f11278d;

    public CborMap() {
        this(-1L);
    }

    private String g(String str) {
        if (str != null) {
            return str;
        }
        throw new CborException("Key must be non-null");
    }

    @Override // f1.CborObject
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CborMap)) {
            return false;
        }
        CborMap cborMap = (CborMap) obj;
        return this.f11278d == cborMap.l() && this.f11277c.equals(cborMap.f11277c) && super.equals(obj);
    }

    public boolean h(CborObject cborObject) {
        return this.f11277c.containsKey(cborObject);
    }

    @Override // f1.CborObject
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Boolean.valueOf(this.f11278d), this.f11277c);
    }

    public boolean i(String str) {
        return this.f11277c.containsKey(new CborTextString(str));
    }

    public CborObject j(CborObject cborObject) {
        return this.f11277c.get(cborObject);
    }

    public CborObject k(String str) {
        return this.f11277c.get(new CborTextString(g(str)));
    }

    public boolean l() {
        return this.f11278d;
    }

    public Set<CborObject> m() {
        return this.f11277c.keySet();
    }

    public CborMap n(CborObject cborObject, CborObject cborObject2) {
        this.f11277c.put(cborObject, cborObject2);
        return this;
    }

    public CborMap o(String str, CborObject cborObject) {
        this.f11277c.put(new CborTextString(g(str)), cborObject);
        return this;
    }

    public CborMap p(boolean z10) {
        this.f11278d = z10;
        return this;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        if (l()) {
            sb2.append("{_ ");
        } else {
            sb2.append("{");
        }
        for (CborObject cborObject : this.f11277c.keySet()) {
            sb2.append(cborObject.toString());
            sb2.append(": ");
            sb2.append(this.f11277c.get(cborObject).toString());
            sb2.append(", ");
        }
        if (sb2.toString().endsWith(", ")) {
            sb2.setLength(sb2.length() - 2);
        }
        sb2.append("}");
        return sb2.toString();
    }

    public CborMap(long j10) {
        super(5, j10);
        this.f11278d = false;
        this.f11277c = new LinkedHashMap<>();
    }

    public CborMap(JSONObject jSONObject) {
        this();
        CborObject cborSimpleValue;
        CborObject cborArray;
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            if (jSONObject.isNull(next)) {
                cborSimpleValue = new CborSimpleValue(CborSimpleValueEnum.NULL);
            } else {
                Object opt = jSONObject.opt(next);
                if (opt != null) {
                    if (opt instanceof JSONArray) {
                        cborArray = new CborArray((JSONArray) opt);
                    } else if (opt instanceof JSONObject) {
                        cborArray = new CborMap((JSONObject) opt);
                    } else {
                        try {
                            cborSimpleValue = CborHelper.d(opt);
                        } catch (CborException e10) {
                            e1.i.b("CborMap", "CborMap analyze error. " + e10);
                        }
                    }
                    cborSimpleValue = cborArray;
                }
            }
            this.f11277c.put(new CborTextString(next), cborSimpleValue);
        }
    }

    public CborMap(Map<?, ?> map) {
        this();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            this.f11277c.put(CborHelper.d(entry.getKey()), CborHelper.d(entry.getValue()));
        }
    }
}
