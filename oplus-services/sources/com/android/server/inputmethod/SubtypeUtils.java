package com.android.server.inputmethod;

import android.content.res.Resources;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.inputmethod.LocaleUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class SubtypeUtils {
    public static final boolean DEBUG = false;
    static final int NOT_A_SUBTYPE_ID = -1;
    static final String SUBTYPE_MODE_ANY = null;
    static final String SUBTYPE_MODE_KEYBOARD = "keyboard";
    private static final String TAG = "SubtypeUtils";
    private static final String TAG_ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE = "EnabledWhenDefaultIsNotAsciiCapable";

    @GuardedBy({"sCacheLock"})
    private static InputMethodInfo sCachedInputMethodInfo;

    @GuardedBy({"sCacheLock"})
    private static ArrayList<InputMethodSubtype> sCachedResult;

    @GuardedBy({"sCacheLock"})
    private static LocaleList sCachedSystemLocales;
    private static final Object sCacheLock = new Object();
    private static final LocaleUtils.LocaleExtractor<InputMethodSubtype> sSubtypeToLocale = new LocaleUtils.LocaleExtractor() { // from class: com.android.server.inputmethod.SubtypeUtils$$ExternalSyntheticLambda0
        @Override // com.android.server.inputmethod.LocaleUtils.LocaleExtractor
        public final Locale get(Object obj) {
            Locale lambda$static$0;
            lambda$static$0 = SubtypeUtils.lambda$static$0((InputMethodSubtype) obj);
            return lambda$static$0;
        }
    };

    SubtypeUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean containsSubtypeOf(InputMethodInfo inputMethodInfo, Locale locale, boolean z, String str) {
        if (locale == null) {
            return false;
        }
        int subtypeCount = inputMethodInfo.getSubtypeCount();
        for (int i = 0; i < subtypeCount; i++) {
            InputMethodSubtype subtypeAt = inputMethodInfo.getSubtypeAt(i);
            if (z) {
                Locale localeObject = subtypeAt.getLocaleObject();
                if (localeObject == null) {
                    continue;
                } else if (TextUtils.equals(localeObject.getLanguage(), locale.getLanguage())) {
                    if (!TextUtils.equals(localeObject.getCountry(), locale.getCountry())) {
                        continue;
                    }
                    if (str != SUBTYPE_MODE_ANY || TextUtils.isEmpty(str) || str.equalsIgnoreCase(subtypeAt.getMode())) {
                        return true;
                    }
                } else {
                    continue;
                }
            } else {
                if (!TextUtils.equals(new Locale(LocaleUtils.getLanguageFromLocaleString(subtypeAt.getLocale())).getLanguage(), locale.getLanguage())) {
                    continue;
                }
                return str != SUBTYPE_MODE_ANY ? true : true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList<InputMethodSubtype> getSubtypes(InputMethodInfo inputMethodInfo) {
        ArrayList<InputMethodSubtype> arrayList = new ArrayList<>();
        int subtypeCount = inputMethodInfo.getSubtypeCount();
        for (int i = 0; i < subtypeCount; i++) {
            arrayList.add(inputMethodInfo.getSubtypeAt(i));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isValidSubtypeId(InputMethodInfo inputMethodInfo, int i) {
        return getSubtypeIdFromHashCode(inputMethodInfo, i) != -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getSubtypeIdFromHashCode(InputMethodInfo inputMethodInfo, int i) {
        if (inputMethodInfo == null) {
            return -1;
        }
        int subtypeCount = inputMethodInfo.getSubtypeCount();
        for (int i2 = 0; i2 < subtypeCount; i2++) {
            if (i == inputMethodInfo.getSubtypeAt(i2).hashCode()) {
                return i2;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Locale lambda$static$0(InputMethodSubtype inputMethodSubtype) {
        if (inputMethodSubtype != null) {
            return inputMethodSubtype.getLocaleObject();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public static ArrayList<InputMethodSubtype> getImplicitlyApplicableSubtypesLocked(Resources resources, InputMethodInfo inputMethodInfo) {
        LocaleList locales = resources.getConfiguration().getLocales();
        Object obj = sCacheLock;
        synchronized (obj) {
            if (locales.equals(sCachedSystemLocales) && sCachedInputMethodInfo == inputMethodInfo) {
                return new ArrayList<>(sCachedResult);
            }
            ArrayList<InputMethodSubtype> implicitlyApplicableSubtypesLockedImpl = getImplicitlyApplicableSubtypesLockedImpl(resources, inputMethodInfo);
            synchronized (obj) {
                sCachedSystemLocales = locales;
                sCachedInputMethodInfo = inputMethodInfo;
                sCachedResult = new ArrayList<>(implicitlyApplicableSubtypesLockedImpl);
            }
            return implicitlyApplicableSubtypesLockedImpl;
        }
    }

    private static ArrayList<InputMethodSubtype> getImplicitlyApplicableSubtypesLockedImpl(Resources resources, InputMethodInfo inputMethodInfo) {
        InputMethodSubtype findLastResortApplicableSubtypeLocked;
        boolean z;
        ArrayList<InputMethodSubtype> subtypes = getSubtypes(inputMethodInfo);
        LocaleList locales = resources.getConfiguration().getLocales();
        String locale = locales.get(0).toString();
        if (TextUtils.isEmpty(locale)) {
            return new ArrayList<>();
        }
        int size = subtypes.size();
        ArrayMap arrayMap = new ArrayMap();
        for (int i = 0; i < size; i++) {
            InputMethodSubtype inputMethodSubtype = subtypes.get(i);
            if (inputMethodSubtype.overridesImplicitlyEnabledSubtype()) {
                String mode = inputMethodSubtype.getMode();
                if (!arrayMap.containsKey(mode)) {
                    arrayMap.put(mode, inputMethodSubtype);
                }
            }
        }
        if (arrayMap.size() > 0) {
            return new ArrayList<>(arrayMap.values());
        }
        ArrayMap arrayMap2 = new ArrayMap();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < size; i2++) {
            InputMethodSubtype inputMethodSubtype2 = subtypes.get(i2);
            String mode2 = inputMethodSubtype2.getMode();
            if (SUBTYPE_MODE_KEYBOARD.equals(mode2)) {
                arrayList.add(inputMethodSubtype2);
            } else {
                if (!arrayMap2.containsKey(mode2)) {
                    arrayMap2.put(mode2, new ArrayList());
                }
                ((ArrayList) arrayMap2.get(mode2)).add(inputMethodSubtype2);
            }
        }
        ArrayList<InputMethodSubtype> arrayList2 = new ArrayList<>();
        LocaleUtils.filterByLanguage(arrayList, sSubtypeToLocale, locales, arrayList2);
        if (!arrayList2.isEmpty()) {
            int size2 = arrayList2.size();
            int i3 = 0;
            while (true) {
                if (i3 >= size2) {
                    z = false;
                    break;
                }
                if (arrayList2.get(i3).isAsciiCapable()) {
                    z = true;
                    break;
                }
                i3++;
            }
            if (!z) {
                int size3 = arrayList.size();
                for (int i4 = 0; i4 < size3; i4++) {
                    InputMethodSubtype inputMethodSubtype3 = (InputMethodSubtype) arrayList.get(i4);
                    if (SUBTYPE_MODE_KEYBOARD.equals(inputMethodSubtype3.getMode()) && inputMethodSubtype3.containsExtraValueKey(TAG_ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE)) {
                        arrayList2.add(inputMethodSubtype3);
                    }
                }
            }
        }
        if (arrayList2.isEmpty() && (findLastResortApplicableSubtypeLocked = findLastResortApplicableSubtypeLocked(resources, subtypes, SUBTYPE_MODE_KEYBOARD, locale, true)) != null) {
            arrayList2.add(findLastResortApplicableSubtypeLocked);
        }
        Iterator it = arrayMap2.values().iterator();
        while (it.hasNext()) {
            LocaleUtils.filterByLanguage((ArrayList) it.next(), sSubtypeToLocale, locales, arrayList2);
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InputMethodSubtype findLastResortApplicableSubtypeLocked(Resources resources, List<InputMethodSubtype> list, String str, String str2, boolean z) {
        InputMethodSubtype inputMethodSubtype = null;
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = resources.getConfiguration().locale.toString();
        }
        String languageFromLocaleString = LocaleUtils.getLanguageFromLocaleString(str2);
        int size = list.size();
        int i = 0;
        boolean z2 = false;
        InputMethodSubtype inputMethodSubtype2 = null;
        while (true) {
            if (i >= size) {
                break;
            }
            InputMethodSubtype inputMethodSubtype3 = list.get(i);
            String locale = inputMethodSubtype3.getLocale();
            String languageFromLocaleString2 = LocaleUtils.getLanguageFromLocaleString(locale);
            if (str == null || list.get(i).getMode().equalsIgnoreCase(str)) {
                if (inputMethodSubtype == null) {
                    inputMethodSubtype = inputMethodSubtype3;
                }
                if (str2.equals(locale)) {
                    inputMethodSubtype2 = inputMethodSubtype3;
                    break;
                }
                if (!z2 && languageFromLocaleString.equals(languageFromLocaleString2)) {
                    z2 = true;
                    inputMethodSubtype2 = inputMethodSubtype3;
                }
            }
            i++;
        }
        return (inputMethodSubtype2 == null && z) ? inputMethodSubtype : inputMethodSubtype2;
    }
}
