package a5;

import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import ma.Unit;
import za.k;

/* compiled from: CardParamCache.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\b\u0010\tJ\u001a\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0004\u001a\u0004\u0018\u00010\u0002H\u0016J\u0012\u0010\u0007\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\n"}, d2 = {"La5/c;", "La5/b;", "", "key", ThermalBaseConfig.Item.ATTR_VALUE, "", "c", "a", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: a5.c, reason: use source file name */
/* loaded from: classes.dex */
public final class CardParamCache extends BaseKeyValueCache {

    /* renamed from: b, reason: collision with root package name */
    private final String f50b = "CardParamCache";

    /* renamed from: c, reason: collision with root package name */
    private final Map<String, String> f51c = new LinkedHashMap();

    /* renamed from: d, reason: collision with root package name */
    private SharedPreferences f52d;

    public CardParamCache() {
        SharedPreferences b10 = PreferenceManager.b(b());
        k.d(b10, "PreferenceManager.getDef…haredPreferences(context)");
        this.f52d = b10;
    }

    @Override // a5.BaseKeyValueCache
    public String a(String key) {
        String str;
        k.e(key, "key");
        s5.b.f18066c.c(this.f50b, "get card param key: " + key + ' ');
        synchronized (this.f51c) {
            str = this.f51c.get(key);
            if (str == null) {
                str = this.f52d.getString(key, null);
                if (str != null) {
                    this.f51c.put(key, str);
                } else {
                    str = null;
                }
            }
        }
        return str;
    }

    @Override // a5.BaseKeyValueCache
    public boolean c(String key, String value) {
        k.e(key, "key");
        s5.b bVar = s5.b.f18066c;
        String str = this.f50b;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("update key: ");
        sb2.append(key);
        sb2.append(" value size is null : ");
        sb2.append(value == null);
        bVar.c(str, sb2.toString());
        synchronized (this.f51c) {
            this.f51c.put(key, value);
            this.f52d.edit().putString(key, value).apply();
            Unit unit = Unit.f15173a;
        }
        return true;
    }
}
