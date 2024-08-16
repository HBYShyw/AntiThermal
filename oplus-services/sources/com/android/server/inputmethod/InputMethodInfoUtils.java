package com.android.server.inputmethod;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.inputmethod.InputMethodInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class InputMethodInfoUtils {
    private static final String TAG = "InputMethodInfoUtils";
    private static final Locale[] SEARCH_ORDER_OF_FALLBACK_LOCALES = {Locale.ENGLISH, Locale.US, Locale.UK};
    private static final Locale ENGLISH_LOCALE = new Locale("en");

    InputMethodInfoUtils() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class InputMethodListBuilder {
        private final LinkedHashSet<InputMethodInfo> mInputMethodSet;

        private InputMethodListBuilder() {
            this.mInputMethodSet = new LinkedHashSet<>();
        }

        InputMethodListBuilder fillImes(ArrayList<InputMethodInfo> arrayList, Context context, boolean z, Locale locale, boolean z2, String str) {
            for (int i = 0; i < arrayList.size(); i++) {
                InputMethodInfo inputMethodInfo = arrayList.get(i);
                if (InputMethodInfoUtils.isSystemImeThatHasSubtypeOf(inputMethodInfo, context, z, locale, z2, str)) {
                    this.mInputMethodSet.add(inputMethodInfo);
                }
            }
            return this;
        }

        InputMethodListBuilder fillAuxiliaryImes(ArrayList<InputMethodInfo> arrayList, Context context) {
            Iterator<InputMethodInfo> it = this.mInputMethodSet.iterator();
            while (it.hasNext()) {
                if (it.next().isAuxiliaryIme()) {
                    return this;
                }
            }
            boolean z = false;
            for (int i = 0; i < arrayList.size(); i++) {
                InputMethodInfo inputMethodInfo = arrayList.get(i);
                if (InputMethodInfoUtils.isSystemAuxilialyImeThatHasAutomaticSubtype(inputMethodInfo, context, true)) {
                    this.mInputMethodSet.add(inputMethodInfo);
                    z = true;
                }
            }
            if (z) {
                return this;
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                InputMethodInfo inputMethodInfo2 = arrayList.get(i2);
                if (InputMethodInfoUtils.isSystemAuxilialyImeThatHasAutomaticSubtype(inputMethodInfo2, context, false)) {
                    this.mInputMethodSet.add(inputMethodInfo2);
                }
            }
            return this;
        }

        public boolean isEmpty() {
            return this.mInputMethodSet.isEmpty();
        }

        public ArrayList<InputMethodInfo> build() {
            return new ArrayList<>(this.mInputMethodSet);
        }
    }

    private static InputMethodListBuilder getMinimumKeyboardSetWithSystemLocale(ArrayList<InputMethodInfo> arrayList, Context context, Locale locale, Locale locale2) {
        InputMethodListBuilder inputMethodListBuilder = new InputMethodListBuilder();
        inputMethodListBuilder.fillImes(arrayList, context, true, locale, true, "keyboard");
        if (!inputMethodListBuilder.isEmpty()) {
            return inputMethodListBuilder;
        }
        inputMethodListBuilder.fillImes(arrayList, context, true, locale, false, "keyboard");
        if (!inputMethodListBuilder.isEmpty()) {
            return inputMethodListBuilder;
        }
        inputMethodListBuilder.fillImes(arrayList, context, true, locale2, true, "keyboard");
        if (!inputMethodListBuilder.isEmpty()) {
            return inputMethodListBuilder;
        }
        inputMethodListBuilder.fillImes(arrayList, context, true, locale2, false, "keyboard");
        if (!inputMethodListBuilder.isEmpty()) {
            return inputMethodListBuilder;
        }
        inputMethodListBuilder.fillImes(arrayList, context, false, locale2, true, "keyboard");
        if (!inputMethodListBuilder.isEmpty()) {
            return inputMethodListBuilder;
        }
        inputMethodListBuilder.fillImes(arrayList, context, false, locale2, false, "keyboard");
        if (!inputMethodListBuilder.isEmpty()) {
            return inputMethodListBuilder;
        }
        Slog.w(TAG, "No software keyboard is found. imis=" + Arrays.toString(arrayList.toArray()) + " systemLocale=" + locale + " fallbackLocale=" + locale2);
        return inputMethodListBuilder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList<InputMethodInfo> getDefaultEnabledImes(Context context, ArrayList<InputMethodInfo> arrayList, boolean z) {
        Locale fallbackLocaleForDefaultIme = getFallbackLocaleForDefaultIme(arrayList, context);
        Locale systemLocaleFromContext = LocaleUtils.getSystemLocaleFromContext(context);
        InputMethodListBuilder minimumKeyboardSetWithSystemLocale = getMinimumKeyboardSetWithSystemLocale(arrayList, context, systemLocaleFromContext, fallbackLocaleForDefaultIme);
        if (!z) {
            minimumKeyboardSetWithSystemLocale.fillImes(arrayList, context, true, systemLocaleFromContext, true, SubtypeUtils.SUBTYPE_MODE_ANY).fillAuxiliaryImes(arrayList, context);
        }
        return minimumKeyboardSetWithSystemLocale.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList<InputMethodInfo> getDefaultEnabledImes(Context context, ArrayList<InputMethodInfo> arrayList) {
        return getDefaultEnabledImes(context, arrayList, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InputMethodInfo chooseSystemVoiceIme(ArrayMap<String, InputMethodInfo> arrayMap, String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        InputMethodInfo inputMethodInfo = arrayMap.get(str2);
        if (inputMethodInfo != null && inputMethodInfo.isSystem() && inputMethodInfo.getPackageName().equals(str)) {
            return inputMethodInfo;
        }
        int size = arrayMap.size();
        InputMethodInfo inputMethodInfo2 = null;
        for (int i = 0; i < size; i++) {
            InputMethodInfo valueAt = arrayMap.valueAt(i);
            if (valueAt.isSystem() && TextUtils.equals(valueAt.getPackageName(), str)) {
                if (inputMethodInfo2 != null) {
                    Slog.e(TAG, "At most one InputMethodService can be published in systemSpeechRecognizer: " + str + ". Ignoring all of them.");
                    return null;
                }
                inputMethodInfo2 = valueAt;
            }
        }
        return inputMethodInfo2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InputMethodInfo getMostApplicableDefaultIME(List<InputMethodInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int size = list.size();
        int i = -1;
        while (size > 0) {
            size--;
            InputMethodInfo inputMethodInfo = list.get(size);
            if (!inputMethodInfo.isAuxiliaryIme()) {
                if (inputMethodInfo.isSystem() && SubtypeUtils.containsSubtypeOf(inputMethodInfo, ENGLISH_LOCALE, false, "keyboard")) {
                    return inputMethodInfo;
                }
                if (i < 0 && inputMethodInfo.isSystem()) {
                    i = size;
                }
            }
        }
        return list.get(Math.max(i, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSystemAuxilialyImeThatHasAutomaticSubtype(InputMethodInfo inputMethodInfo, Context context, boolean z) {
        if (!inputMethodInfo.isSystem()) {
            return false;
        }
        if ((z && !inputMethodInfo.isDefault(context)) || !inputMethodInfo.isAuxiliaryIme()) {
            return false;
        }
        int subtypeCount = inputMethodInfo.getSubtypeCount();
        for (int i = 0; i < subtypeCount; i++) {
            if (inputMethodInfo.getSubtypeAt(i).overridesImplicitlyEnabledSubtype()) {
                return true;
            }
        }
        return false;
    }

    private static Locale getFallbackLocaleForDefaultIme(ArrayList<InputMethodInfo> arrayList, Context context) {
        for (Locale locale : SEARCH_ORDER_OF_FALLBACK_LOCALES) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (isSystemImeThatHasSubtypeOf(arrayList.get(i), context, true, locale, true, "keyboard")) {
                    return locale;
                }
            }
        }
        for (Locale locale2 : SEARCH_ORDER_OF_FALLBACK_LOCALES) {
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (isSystemImeThatHasSubtypeOf(arrayList.get(i2), context, false, locale2, true, "keyboard")) {
                    return locale2;
                }
            }
        }
        Slog.w(TAG, "Found no fallback locale. imis=" + Arrays.toString(arrayList.toArray()));
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSystemImeThatHasSubtypeOf(InputMethodInfo inputMethodInfo, Context context, boolean z, Locale locale, boolean z2, String str) {
        if (!inputMethodInfo.isSystem()) {
            return false;
        }
        if (!z || inputMethodInfo.isDefault(context)) {
            return SubtypeUtils.containsSubtypeOf(inputMethodInfo, locale, z2, str);
        }
        return false;
    }
}
