package za;

import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import java.util.Iterator;

/* compiled from: ArrayIterator.kt */
/* loaded from: classes2.dex */
public final class b {
    public static final <T> Iterator<T> a(T[] tArr) {
        k.e(tArr, ThermalWindowConfigInfo.TAG_ARRAY);
        return new a(tArr);
    }
}
