package android.net.shared;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ParcelableUtil {
    public static <ParcelableType, BaseType> ParcelableType[] toParcelableArray(Collection<BaseType> collection, Function<BaseType, ParcelableType> function, Class<ParcelableType> cls) {
        ParcelableType[] parcelabletypeArr = (ParcelableType[]) ((Object[]) Array.newInstance((Class<?>) cls, collection.size()));
        Iterator<BaseType> it = collection.iterator();
        int i = 0;
        while (it.hasNext()) {
            parcelabletypeArr[i] = function.apply(it.next());
            i++;
        }
        return parcelabletypeArr;
    }

    public static <ParcelableType, BaseType> ArrayList<BaseType> fromParcelableArray(ParcelableType[] parcelabletypeArr, Function<ParcelableType, BaseType> function) {
        ArrayList<BaseType> arrayList = new ArrayList<>(parcelabletypeArr.length);
        for (ParcelableType parcelabletype : parcelabletypeArr) {
            arrayList.add(function.apply(parcelabletype));
        }
        return arrayList;
    }
}
