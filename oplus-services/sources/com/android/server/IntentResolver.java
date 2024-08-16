package com.android.server;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.FastImmutableArraySet;
import android.util.LogPrinter;
import android.util.MutableInt;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.FastPrintWriter;
import com.android.server.pm.Computer;
import com.android.server.pm.snapshot.PackageDataSnapshot;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class IntentResolver<F, R> {
    private static final boolean DEBUG = false;
    private static final String TAG = "IntentResolver";
    private static final boolean localLOGV = false;
    private static final boolean localVerificationLOGV = false;
    private static final Comparator mResolvePrioritySorter = new Comparator() { // from class: com.android.server.IntentResolver.1
        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            int priority = ((IntentFilter) obj).getPriority();
            int priority2 = ((IntentFilter) obj2).getPriority();
            if (priority > priority2) {
                return -1;
            }
            return priority < priority2 ? 1 : 0;
        }
    }.thenComparing(new Comparator() { // from class: com.android.server.IntentResolver$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int lambda$static$0;
            lambda$static$0 = IntentResolver.lambda$static$0(obj, obj2);
            return lambda$static$0;
        }
    });
    protected final ArraySet<F> mFilters = new ArraySet<>();
    private final ArrayMap<String, F[]> mTypeToFilter = new ArrayMap<>();
    private final ArrayMap<String, F[]> mBaseTypeToFilter = new ArrayMap<>();
    private final ArrayMap<String, F[]> mWildTypeToFilter = new ArrayMap<>();
    private final ArrayMap<String, F[]> mSchemeToFilter = new ArrayMap<>();
    private final ArrayMap<String, F[]> mActionToFilter = new ArrayMap<>();
    private final ArrayMap<String, F[]> mTypedActionToFilter = new ArrayMap<>();

    protected boolean allowFilterResult(F f, List<R> list) {
        return true;
    }

    protected void filterResults(List<R> list) {
    }

    protected Object filterToLabel(F f) {
        return "IntentFilter";
    }

    protected abstract IntentFilter getIntentFilter(F f);

    protected boolean isFilterStopped(Computer computer, F f, int i) {
        return false;
    }

    protected abstract boolean isPackageForFilter(String str, F f);

    protected abstract F[] newArray(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public R newResult(Computer computer, F f, int i, int i2, long j) {
        return f;
    }

    protected F snapshot(F f) {
        return f;
    }

    public void addFilter(PackageDataSnapshot packageDataSnapshot, F f) {
        IntentFilter intentFilter = getIntentFilter(f);
        this.mFilters.add(f);
        int register_intent_filter = register_intent_filter(f, intentFilter.schemesIterator(), this.mSchemeToFilter, "      Scheme: ");
        int register_mime_types = register_mime_types(f, "      Type: ");
        if (register_intent_filter == 0 && register_mime_types == 0) {
            register_intent_filter(f, intentFilter.actionsIterator(), this.mActionToFilter, "      Action: ");
        }
        if (register_mime_types != 0) {
            register_intent_filter(f, intentFilter.actionsIterator(), this.mTypedActionToFilter, "      TypedAction: ");
        }
    }

    public static boolean intentMatchesFilter(IntentFilter intentFilter, Intent intent, String str) {
        boolean z = (intent.getFlags() & 8) != 0;
        LogPrinter logPrinter = z ? new LogPrinter(2, TAG, 3) : null;
        if (z) {
            Slog.v(TAG, "Intent: " + intent);
            Slog.v(TAG, "Matching against filter: " + intentFilter);
            intentFilter.dump(logPrinter, "  ");
        }
        int match = intentFilter.match(intent.getAction(), str, intent.getScheme(), intent.getData(), intent.getCategories(), TAG);
        if (match >= 0) {
            if (z) {
                Slog.v(TAG, "Filter matched!  match=0x" + Integer.toHexString(match));
            }
            return true;
        }
        if (z) {
            Slog.v(TAG, "Filter did not match: " + (match != -4 ? match != -3 ? match != -2 ? match != -1 ? "unknown reason" : "type" : "data" : "action" : "category"));
        }
        return false;
    }

    private ArrayList<F> collectFilters(F[] fArr, IntentFilter intentFilter) {
        F f;
        ArrayList<F> arrayList = null;
        if (fArr != null) {
            for (int i = 0; i < fArr.length && (f = fArr[i]) != null; i++) {
                if (IntentFilter.filterEquals(getIntentFilter(f), intentFilter)) {
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    arrayList.add(f);
                }
            }
        }
        return arrayList;
    }

    public ArrayList<F> findFilters(IntentFilter intentFilter) {
        if (intentFilter.countDataSchemes() == 1) {
            return collectFilters(this.mSchemeToFilter.get(intentFilter.getDataScheme(0)), intentFilter);
        }
        if (intentFilter.countDataTypes() != 0 && intentFilter.countActions() == 1) {
            return collectFilters(this.mTypedActionToFilter.get(intentFilter.getAction(0)), intentFilter);
        }
        if (intentFilter.countDataTypes() == 0 && intentFilter.countDataSchemes() == 0 && intentFilter.countActions() == 1) {
            return collectFilters(this.mActionToFilter.get(intentFilter.getAction(0)), intentFilter);
        }
        Iterator<F> it = this.mFilters.iterator();
        ArrayList<F> arrayList = null;
        while (it.hasNext()) {
            F next = it.next();
            if (IntentFilter.filterEquals(getIntentFilter(next), intentFilter)) {
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public void removeFilter(F f) {
        removeFilterInternal(f);
        this.mFilters.remove(f);
    }

    protected void removeFilterInternal(F f) {
        IntentFilter intentFilter = getIntentFilter(f);
        int unregister_intent_filter = unregister_intent_filter(f, intentFilter.schemesIterator(), this.mSchemeToFilter, "      Scheme: ");
        int unregister_mime_types = unregister_mime_types(f, "      Type: ");
        if (unregister_intent_filter == 0 && unregister_mime_types == 0) {
            unregister_intent_filter(f, intentFilter.actionsIterator(), this.mActionToFilter, "      Action: ");
        }
        if (unregister_mime_types != 0) {
            unregister_intent_filter(f, intentFilter.actionsIterator(), this.mTypedActionToFilter, "      TypedAction: ");
        }
    }

    boolean dumpMap(PrintWriter printWriter, String str, String str2, String str3, ArrayMap<String, F[]> arrayMap, String str4, boolean z, boolean z2) {
        boolean z3;
        String str5;
        F f;
        PrintWriterPrinter printWriterPrinter;
        boolean z4;
        ArrayMap<String, F[]> arrayMap2 = arrayMap;
        String str6 = str3 + "  ";
        String str7 = str3 + "    ";
        ArrayMap arrayMap3 = new ArrayMap();
        String str8 = str2;
        int i = 0;
        boolean z5 = false;
        PrintWriterPrinter printWriterPrinter2 = null;
        while (i < arrayMap.size()) {
            F[] valueAt = arrayMap2.valueAt(i);
            int length = valueAt.length;
            if (!z2 || z) {
                z3 = z5;
                str8 = str8;
                printWriterPrinter2 = printWriterPrinter2;
                int i2 = 0;
                boolean z6 = false;
                while (i2 < length) {
                    F f2 = valueAt[i2];
                    if (f2 != null) {
                        if (str4 == null || isPackageForFilter(str4, f2)) {
                            if (str8 != null) {
                                printWriter.print(str);
                                printWriter.println(str8);
                                str8 = null;
                            }
                            if (!z6) {
                                printWriter.print(str6);
                                printWriter.print(arrayMap2.keyAt(i));
                                printWriter.println(":");
                                z6 = true;
                            }
                            dumpFilter(printWriter, str7, f2);
                            if (z) {
                                if (printWriterPrinter2 == null) {
                                    printWriterPrinter2 = new PrintWriterPrinter(printWriter);
                                }
                                getIntentFilter(f2).dump(printWriterPrinter2, str7 + "  ");
                            }
                            z3 = true;
                        }
                        i2++;
                        arrayMap2 = arrayMap;
                    }
                }
            } else {
                arrayMap3.clear();
                int i3 = 0;
                while (true) {
                    str5 = str8;
                    if (i3 >= length || (f = valueAt[i3]) == null) {
                        break;
                    }
                    if (str4 == null || isPackageForFilter(str4, f)) {
                        Object filterToLabel = filterToLabel(f);
                        printWriterPrinter = printWriterPrinter2;
                        int indexOfKey = arrayMap3.indexOfKey(filterToLabel);
                        if (indexOfKey < 0) {
                            z4 = z5;
                            arrayMap3.put(filterToLabel, new MutableInt(1));
                        } else {
                            z4 = z5;
                            ((MutableInt) arrayMap3.valueAt(indexOfKey)).value++;
                        }
                    } else {
                        z4 = z5;
                        printWriterPrinter = printWriterPrinter2;
                    }
                    i3++;
                    str8 = str5;
                    printWriterPrinter2 = printWriterPrinter;
                    z5 = z4;
                }
                z3 = z5;
                PrintWriterPrinter printWriterPrinter3 = printWriterPrinter2;
                str8 = str5;
                int i4 = 0;
                boolean z7 = false;
                while (i4 < arrayMap3.size()) {
                    if (str8 != null) {
                        printWriter.print(str);
                        printWriter.println(str8);
                        str8 = null;
                    }
                    if (!z7) {
                        printWriter.print(str6);
                        printWriter.print(arrayMap2.keyAt(i));
                        printWriter.println(":");
                        z7 = true;
                    }
                    dumpFilterLabel(printWriter, str7, arrayMap3.keyAt(i4), ((MutableInt) arrayMap3.valueAt(i4)).value);
                    i4++;
                    z3 = true;
                }
                printWriterPrinter2 = printWriterPrinter3;
            }
            z5 = z3;
            i++;
            arrayMap2 = arrayMap;
        }
        return z5;
    }

    void writeProtoMap(ProtoOutputStream protoOutputStream, long j, ArrayMap<String, F[]> arrayMap) {
        int size = arrayMap.size();
        for (int i = 0; i < size; i++) {
            long start = protoOutputStream.start(j);
            protoOutputStream.write(1138166333441L, arrayMap.keyAt(i));
            for (F f : arrayMap.valueAt(i)) {
                if (f != null) {
                    protoOutputStream.write(2237677961218L, f.toString());
                }
            }
            protoOutputStream.end(start);
        }
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        writeProtoMap(protoOutputStream, 2246267895809L, this.mTypeToFilter);
        writeProtoMap(protoOutputStream, 2246267895810L, this.mBaseTypeToFilter);
        writeProtoMap(protoOutputStream, 2246267895811L, this.mWildTypeToFilter);
        writeProtoMap(protoOutputStream, 2246267895812L, this.mSchemeToFilter);
        writeProtoMap(protoOutputStream, 2246267895813L, this.mActionToFilter);
        writeProtoMap(protoOutputStream, 2246267895814L, this.mTypedActionToFilter);
        protoOutputStream.end(start);
    }

    public boolean dump(PrintWriter printWriter, String str, String str2, String str3, boolean z, boolean z2) {
        String str4 = str2 + "  ";
        String str5 = "\n" + str2;
        String str6 = str + "\n" + str2;
        if (dumpMap(printWriter, str6, "Full MIME Types:", str4, this.mTypeToFilter, str3, z, z2)) {
            str6 = str5;
        }
        if (dumpMap(printWriter, str6, "Base MIME Types:", str4, this.mBaseTypeToFilter, str3, z, z2)) {
            str6 = str5;
        }
        if (dumpMap(printWriter, str6, "Wild MIME Types:", str4, this.mWildTypeToFilter, str3, z, z2)) {
            str6 = str5;
        }
        if (dumpMap(printWriter, str6, "Schemes:", str4, this.mSchemeToFilter, str3, z, z2)) {
            str6 = str5;
        }
        if (dumpMap(printWriter, str6, "Non-Data Actions:", str4, this.mActionToFilter, str3, z, z2)) {
            str6 = str5;
        }
        if (dumpMap(printWriter, str6, "MIME Typed Actions:", str4, this.mTypedActionToFilter, str3, z, z2)) {
            str6 = str5;
        }
        return str6 == str5;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class IteratorWrapper implements Iterator<F> {
        private F mCur;
        private final Iterator<F> mI;

        IteratorWrapper(Iterator<F> it) {
            this.mI = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.mI.hasNext();
        }

        @Override // java.util.Iterator
        public F next() {
            F next = this.mI.next();
            this.mCur = next;
            return next;
        }

        @Override // java.util.Iterator
        public void remove() {
            F f = this.mCur;
            if (f != null) {
                IntentResolver.this.removeFilterInternal(f);
            }
            this.mI.remove();
        }
    }

    public Iterator<F> filterIterator() {
        return new IteratorWrapper(this.mFilters.iterator());
    }

    public Set<F> filterSet() {
        return Collections.unmodifiableSet(this.mFilters);
    }

    public List<R> queryIntentFromList(Computer computer, Intent intent, String str, boolean z, ArrayList<F[]> arrayList, int i, long j) {
        ArrayList arrayList2 = new ArrayList();
        boolean z2 = (intent.getFlags() & 8) != 0;
        FastImmutableArraySet<String> fastIntentCategories = getFastIntentCategories(intent);
        String scheme = intent.getScheme();
        int i2 = 0;
        for (int size = arrayList.size(); i2 < size; size = size) {
            buildResolveList(computer, intent, fastIntentCategories, z2, z, str, scheme, arrayList.get(i2), arrayList2, i, j);
            i2++;
        }
        filterResults(arrayList2);
        sortResults(arrayList2);
        return arrayList2;
    }

    public List<R> queryIntent(PackageDataSnapshot packageDataSnapshot, Intent intent, String str, boolean z, int i) {
        return queryIntent(packageDataSnapshot, intent, str, z, i, 0L);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0193 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0210  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x022c  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01ed  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x018f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected final List<R> queryIntent(PackageDataSnapshot packageDataSnapshot, Intent intent, String str, boolean z, int i, long j) {
        F[] fArr;
        F[] fArr2;
        F[] fArr3;
        F[] fArr4;
        F[] fArr5;
        String str2;
        ArrayList arrayList;
        int i2;
        int indexOf;
        F[] fArr6;
        String scheme = intent.getScheme();
        ArrayList arrayList2 = new ArrayList();
        boolean z2 = (intent.getFlags() & 8) != 0;
        if (z2) {
            Slog.v(TAG, "Resolving type=" + str + " scheme=" + scheme + " defaultOnly=" + z + " userId=" + i + " of " + intent);
        }
        if (str != null && (indexOf = str.indexOf(47)) > 0) {
            String substring = str.substring(0, indexOf);
            if (!substring.equals("*")) {
                if (str.length() != indexOf + 2 || str.charAt(indexOf + 1) != '*') {
                    fArr = this.mTypeToFilter.get(str);
                    if (z2) {
                        Slog.v(TAG, "First type cut: " + Arrays.toString(fArr));
                    }
                    fArr6 = this.mWildTypeToFilter.get(substring);
                    if (z2) {
                        Slog.v(TAG, "Second type cut: " + Arrays.toString(fArr6));
                    }
                } else {
                    fArr = this.mBaseTypeToFilter.get(substring);
                    if (z2) {
                        Slog.v(TAG, "First type cut: " + Arrays.toString(fArr));
                    }
                    fArr6 = this.mWildTypeToFilter.get(substring);
                    if (z2) {
                        Slog.v(TAG, "Second type cut: " + Arrays.toString(fArr6));
                    }
                }
                F[] fArr7 = this.mWildTypeToFilter.get("*");
                if (z2) {
                    Slog.v(TAG, "Third type cut: " + Arrays.toString(fArr7));
                }
                fArr3 = fArr7;
                fArr2 = fArr6;
                if (scheme != null) {
                    F[] fArr8 = this.mSchemeToFilter.get(scheme);
                    if (z2) {
                        Slog.v(TAG, "Scheme list: " + Arrays.toString(fArr8));
                    }
                    fArr4 = fArr8;
                } else {
                    fArr4 = null;
                }
                if (str == null || scheme != null || intent.getAction() == null) {
                    fArr5 = fArr;
                } else {
                    F[] fArr9 = this.mActionToFilter.get(intent.getAction());
                    if (z2) {
                        Slog.v(TAG, "Action list: " + Arrays.toString(fArr9));
                    }
                    fArr5 = fArr9;
                }
                FastImmutableArraySet<String> fastIntentCategories = getFastIntentCategories(intent);
                Computer computer = (Computer) packageDataSnapshot;
                if (fArr5 != null) {
                    arrayList = arrayList2;
                    str2 = TAG;
                    i2 = 0;
                    buildResolveList(computer, intent, fastIntentCategories, z2, z, str, scheme, fArr5, arrayList2, i, j);
                } else {
                    str2 = TAG;
                    arrayList = arrayList2;
                    i2 = 0;
                }
                if (fArr2 != null) {
                    buildResolveList(computer, intent, fastIntentCategories, z2, z, str, scheme, fArr2, arrayList, i, j);
                }
                if (fArr3 != null) {
                    buildResolveList(computer, intent, fastIntentCategories, z2, z, str, scheme, fArr3, arrayList, i, j);
                }
                if (fArr4 != null) {
                    buildResolveList(computer, intent, fastIntentCategories, z2, z, str, scheme, fArr4, arrayList, i, j);
                }
                ArrayList arrayList3 = arrayList;
                filterResults(arrayList3);
                sortResults(arrayList3);
                if (z2) {
                    Slog.v(str2, "Final result list:");
                    for (int i3 = i2; i3 < arrayList3.size(); i3++) {
                        Slog.v(str2, "  " + arrayList3.get(i3));
                    }
                }
                return arrayList3;
            }
            if (intent.getAction() != null) {
                fArr = this.mTypedActionToFilter.get(intent.getAction());
                if (z2) {
                    Slog.v(TAG, "Typed Action list: " + Arrays.toString(fArr));
                }
                fArr2 = null;
                fArr3 = null;
                if (scheme != null) {
                }
                if (str == null) {
                }
                fArr5 = fArr;
                FastImmutableArraySet<String> fastIntentCategories2 = getFastIntentCategories(intent);
                Computer computer2 = (Computer) packageDataSnapshot;
                if (fArr5 != null) {
                }
                if (fArr2 != null) {
                }
                if (fArr3 != null) {
                }
                if (fArr4 != null) {
                }
                ArrayList arrayList32 = arrayList;
                filterResults(arrayList32);
                sortResults(arrayList32);
                if (z2) {
                }
                return arrayList32;
            }
        }
        fArr = null;
        fArr2 = null;
        fArr3 = null;
        if (scheme != null) {
        }
        if (str == null) {
        }
        fArr5 = fArr;
        FastImmutableArraySet<String> fastIntentCategories22 = getFastIntentCategories(intent);
        Computer computer22 = (Computer) packageDataSnapshot;
        if (fArr5 != null) {
        }
        if (fArr2 != null) {
        }
        if (fArr3 != null) {
        }
        if (fArr4 != null) {
        }
        ArrayList arrayList322 = arrayList;
        filterResults(arrayList322);
        sortResults(arrayList322);
        if (z2) {
        }
        return arrayList322;
    }

    protected boolean isFilterVerified(F f) {
        return getIntentFilter(f).isVerified();
    }

    protected void sortResults(List<R> list) {
        Collections.sort(list, mResolvePrioritySorter);
    }

    protected void dumpFilter(PrintWriter printWriter, String str, F f) {
        printWriter.print(str);
        printWriter.println(f);
    }

    protected void dumpFilterLabel(PrintWriter printWriter, String str, Object obj, int i) {
        printWriter.print(str);
        printWriter.print(obj);
        printWriter.print(": ");
        printWriter.println(i);
    }

    private final void addFilter(ArrayMap<String, F[]> arrayMap, String str, F f) {
        F[] fArr = arrayMap.get(str);
        if (fArr == null) {
            F[] newArray = newArray(2);
            arrayMap.put(str, newArray);
            newArray[0] = f;
            return;
        }
        int length = fArr.length;
        int i = length;
        while (i > 0 && fArr[i - 1] == null) {
            i--;
        }
        if (i < length) {
            fArr[i] = f;
            return;
        }
        F[] newArray2 = newArray((length * 3) / 2);
        System.arraycopy(fArr, 0, newArray2, 0, length);
        newArray2[length] = f;
        arrayMap.put(str, newArray2);
    }

    private final int register_mime_types(F f, String str) {
        String str2;
        Iterator<String> typesIterator = getIntentFilter(f).typesIterator();
        if (typesIterator == null) {
            return 0;
        }
        int i = 0;
        while (typesIterator.hasNext()) {
            String next = typesIterator.next();
            i++;
            int indexOf = next.indexOf(47);
            if (indexOf > 0) {
                str2 = next.substring(0, indexOf).intern();
            } else {
                str2 = next;
                next = next + "/*";
            }
            addFilter(this.mTypeToFilter, next, f);
            if (indexOf > 0) {
                addFilter(this.mBaseTypeToFilter, str2, f);
            } else {
                addFilter(this.mWildTypeToFilter, str2, f);
            }
        }
        return i;
    }

    private final int unregister_mime_types(F f, String str) {
        String str2;
        Iterator<String> typesIterator = getIntentFilter(f).typesIterator();
        if (typesIterator == null) {
            return 0;
        }
        int i = 0;
        while (typesIterator.hasNext()) {
            String next = typesIterator.next();
            i++;
            int indexOf = next.indexOf(47);
            if (indexOf > 0) {
                str2 = next.substring(0, indexOf).intern();
            } else {
                str2 = next;
                next = next + "/*";
            }
            remove_all_objects(this.mTypeToFilter, next, f);
            if (indexOf > 0) {
                remove_all_objects(this.mBaseTypeToFilter, str2, f);
            } else {
                remove_all_objects(this.mWildTypeToFilter, str2, f);
            }
        }
        return i;
    }

    protected final int register_intent_filter(F f, Iterator<String> it, ArrayMap<String, F[]> arrayMap, String str) {
        int i = 0;
        if (it == null) {
            return 0;
        }
        while (it.hasNext()) {
            i++;
            addFilter(arrayMap, it.next(), f);
        }
        return i;
    }

    protected final int unregister_intent_filter(F f, Iterator<String> it, ArrayMap<String, F[]> arrayMap, String str) {
        int i = 0;
        if (it == null) {
            return 0;
        }
        while (it.hasNext()) {
            i++;
            remove_all_objects(arrayMap, it.next(), f);
        }
        return i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void remove_all_objects(ArrayMap<String, F[]> arrayMap, String str, F f) {
        F[] fArr = arrayMap.get(str);
        if (fArr != null) {
            int length = fArr.length - 1;
            while (length >= 0 && fArr[length] == null) {
                length--;
            }
            int i = length;
            while (length >= 0) {
                Object obj = fArr[length];
                if (obj != null && getIntentFilter(obj) == getIntentFilter(f)) {
                    int i2 = i - length;
                    if (i2 > 0) {
                        System.arraycopy(fArr, length + 1, fArr, length, i2);
                    }
                    fArr[i] = null;
                    i--;
                }
                length--;
            }
            if (i < 0) {
                arrayMap.remove(str);
            } else if (i < fArr.length / 2) {
                F[] newArray = newArray(i + 2);
                System.arraycopy(fArr, 0, newArray, 0, i + 1);
                arrayMap.put(str, newArray);
            }
        }
    }

    private static FastImmutableArraySet<String> getFastIntentCategories(Intent intent) {
        Set<String> categories = intent.getCategories();
        if (categories == null) {
            return null;
        }
        return new FastImmutableArraySet<>((String[]) categories.toArray(new String[categories.size()]));
    }

    private void buildResolveList(Computer computer, Intent intent, FastImmutableArraySet<String> fastImmutableArraySet, boolean z, boolean z2, String str, String str2, F[] fArr, List<R> list, int i, long j) {
        FastPrintWriter fastPrintWriter;
        LogPrinter logPrinter;
        String str3;
        int i2;
        int i3;
        FastPrintWriter fastPrintWriter2;
        F[] fArr2 = fArr;
        String action = intent.getAction();
        Uri data = intent.getData();
        String str4 = intent.getPackage();
        boolean isExcludingStopped = intent.isExcludingStopped();
        if (z) {
            LogPrinter logPrinter2 = new LogPrinter(2, TAG, 3);
            logPrinter = logPrinter2;
            fastPrintWriter = new FastPrintWriter(logPrinter2);
        } else {
            fastPrintWriter = null;
            logPrinter = null;
        }
        int length = fArr2 != null ? fArr2.length : 0;
        int i4 = 0;
        boolean z3 = false;
        while (i4 < length) {
            F f = fArr2[i4];
            if (f == null) {
                break;
            }
            if (z) {
                Slog.v(TAG, "Matching against filter " + f);
            }
            if (isExcludingStopped && isFilterStopped(computer, f, i)) {
                if (z) {
                    Slog.v(TAG, "  Filter's target is stopped; skipping");
                }
            } else if (str4 == null || isPackageForFilter(str4, f)) {
                IntentFilter intentFilter = getIntentFilter(f);
                if (intentFilter.getAutoVerify() && z) {
                    Slog.v(TAG, "  Filter verified: " + isFilterVerified(f));
                    int i5 = 0;
                    for (int countDataAuthorities = intentFilter.countDataAuthorities(); i5 < countDataAuthorities; countDataAuthorities = countDataAuthorities) {
                        Slog.v(TAG, "   " + intentFilter.getDataAuthority(i5).getHost());
                        i5++;
                    }
                }
                if (allowFilterResult(f, list)) {
                    String str5 = action;
                    str3 = action;
                    i2 = i4;
                    i3 = length;
                    fastPrintWriter2 = fastPrintWriter;
                    int match = intentFilter.match(str5, str, str2, data, fastImmutableArraySet, TAG);
                    if (match >= 0) {
                        if (z) {
                            Slog.v(TAG, "  Filter matched!  match=0x" + Integer.toHexString(match) + " hasDefault=" + intentFilter.hasCategory("android.intent.category.DEFAULT"));
                        }
                        if (!z2 || intentFilter.hasCategory("android.intent.category.DEFAULT")) {
                            R newResult = newResult(computer, f, match, i, j);
                            if (z) {
                                Slog.v(TAG, "    Created result: " + newResult);
                            }
                            if (newResult != null) {
                                list.add(newResult);
                                if (z) {
                                    dumpFilter(fastPrintWriter2, "    ", f);
                                    fastPrintWriter2.flush();
                                    intentFilter.dump(logPrinter, "    ");
                                }
                            }
                        } else {
                            z3 = true;
                        }
                    } else if (z) {
                        Slog.v(TAG, "  Filter did not match: " + (match != -4 ? match != -3 ? match != -2 ? match != -1 ? "unknown reason" : "type" : "data" : "action" : "category"));
                    }
                    i4 = i2 + 1;
                    fArr2 = fArr;
                    fastPrintWriter = fastPrintWriter2;
                    action = str3;
                    length = i3;
                } else if (z) {
                    Slog.v(TAG, "  Filter's target already added");
                }
            } else if (z) {
                Slog.v(TAG, "  Filter is not from package " + str4 + "; skipping");
            }
            i2 = i4;
            i3 = length;
            str3 = action;
            fastPrintWriter2 = fastPrintWriter;
            i4 = i2 + 1;
            fArr2 = fArr;
            fastPrintWriter = fastPrintWriter2;
            action = str3;
            length = i3;
        }
        if (z && z3) {
            if (list.size() == 0) {
                Slog.v(TAG, "resolveIntent failed: found match, but none with CATEGORY_DEFAULT");
            } else if (list.size() > 1) {
                Slog.v(TAG, "resolveIntent: multiple matches, only some with CATEGORY_DEFAULT");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$0(Object obj, Object obj2) {
        int originPriority = ((IntentFilter) obj).mIntentFilterExt.getOriginPriority();
        int originPriority2 = ((IntentFilter) obj2).mIntentFilterExt.getOriginPriority();
        if (originPriority > originPriority2) {
            return -1;
        }
        return originPriority < originPriority2 ? 1 : 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void copyInto(ArrayMap<String, F[]> arrayMap, ArrayMap<String, F[]> arrayMap2) {
        int size = arrayMap2.size();
        arrayMap.clear();
        arrayMap.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            F[] valueAt = arrayMap2.valueAt(i);
            String keyAt = arrayMap2.keyAt(i);
            Object[] copyOf = Arrays.copyOf(valueAt, valueAt.length);
            for (int i2 = 0; i2 < copyOf.length; i2++) {
                copyOf[i2] = snapshot(copyOf[i2]);
            }
            arrayMap.put(keyAt, copyOf);
        }
    }

    protected void copyInto(ArraySet<F> arraySet, ArraySet<F> arraySet2) {
        arraySet.clear();
        int size = arraySet2.size();
        arraySet.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            arraySet.append(snapshot(arraySet2.valueAt(i)));
        }
    }

    protected void copyFrom(IntentResolver intentResolver) {
        copyInto(this.mFilters, intentResolver.mFilters);
        copyInto(this.mTypeToFilter, intentResolver.mTypeToFilter);
        copyInto(this.mBaseTypeToFilter, intentResolver.mBaseTypeToFilter);
        copyInto(this.mWildTypeToFilter, intentResolver.mWildTypeToFilter);
        copyInto(this.mSchemeToFilter, intentResolver.mSchemeToFilter);
        copyInto(this.mActionToFilter, intentResolver.mActionToFilter);
        copyInto(this.mTypedActionToFilter, intentResolver.mTypedActionToFilter);
    }
}
