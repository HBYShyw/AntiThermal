package co.nstant.in.cbor.model;

import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class LanguageTaggedString extends Array {
    public LanguageTaggedString(String str, String str2) {
        this(new UnicodeString(str), new UnicodeString(str2));
    }

    public LanguageTaggedString(UnicodeString unicodeString, UnicodeString unicodeString2) {
        setTag(38);
        Objects.requireNonNull(unicodeString);
        add(unicodeString);
        Objects.requireNonNull(unicodeString2);
        add(unicodeString2);
    }

    public UnicodeString getLanguage() {
        return (UnicodeString) getDataItems().get(0);
    }

    public UnicodeString getString() {
        return (UnicodeString) getDataItems().get(1);
    }
}
