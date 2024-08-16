package com.android.server.autofill;

import android.util.ArraySet;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class HintsHelper {
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE = "creditCardExpirationDate";
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY = "creditCardExpirationDay";
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH = "creditCardExpirationMonth";
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR = "creditCardExpirationYear";
    public static final String AUTOFILL_HINT_CREDIT_CARD_NUMBER = "creditCardNumber";
    public static final String AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE = "creditCardSecurityCode";
    public static final String AUTOFILL_HINT_EMAIL_ADDRESS = "emailAddress";
    public static final String AUTOFILL_HINT_NEW_PASSWORD = "newPassword";
    public static final String AUTOFILL_HINT_NEW_USERNAME = "newUsername";
    public static final String AUTOFILL_HINT_PASSWORD = "password";
    public static final String AUTOFILL_HINT_PHONE = "phone";
    public static final String AUTOFILL_HINT_PHONE_COUNTRY_CODE = "phoneCountryCode";
    public static final String AUTOFILL_HINT_PHONE_NATIONAL = "phoneNational";
    public static final String AUTOFILL_HINT_PHONE_NUMBER = "phoneNumber";
    public static final String AUTOFILL_HINT_PHONE_NUMBER_DEVICE = "phoneNumberDevice";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS = "postalAddress";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS_APT_NUMBER = "aptNumber";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS_COUNTRY = "addressCountry";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS_DEPENDENT_LOCALITY = "dependentLocality";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS_EXTENDED_ADDRESS = "extendedAddress";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS_EXTENDED_POSTAL_CODE = "extendedPostalCode";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS_LOCALITY = "addressLocality";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS_REGION = "addressRegion";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS_STREET_ADDRESS = "streetAddress";
    public static final String AUTOFILL_HINT_POSTAL_CODE = "postalCode";
    public static final String AUTOFILL_HINT_USERNAME = "username";

    private HintsHelper() {
    }

    public static Set<String> getHintsForSaveType(int i) {
        ArraySet arraySet = new ArraySet();
        if (i == 1) {
            arraySet.add(AUTOFILL_HINT_NEW_USERNAME);
            arraySet.add(AUTOFILL_HINT_USERNAME);
            arraySet.add(AUTOFILL_HINT_NEW_PASSWORD);
            arraySet.add(AUTOFILL_HINT_PASSWORD);
            return arraySet;
        }
        if (i != 2) {
            if (i != 4) {
                if (i == 8) {
                    arraySet.add(AUTOFILL_HINT_NEW_USERNAME);
                    arraySet.add(AUTOFILL_HINT_USERNAME);
                    return arraySet;
                }
                if (i == 16) {
                    arraySet.add(AUTOFILL_HINT_EMAIL_ADDRESS);
                    return arraySet;
                }
                if (i != 32 && i != 64 && i != 128) {
                    return arraySet;
                }
            }
            arraySet.add(AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE);
            arraySet.add(AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY);
            arraySet.add(AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH);
            arraySet.add(AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR);
            arraySet.add(AUTOFILL_HINT_CREDIT_CARD_NUMBER);
            arraySet.add(AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE);
            return arraySet;
        }
        arraySet.add(AUTOFILL_HINT_POSTAL_ADDRESS);
        arraySet.add(AUTOFILL_HINT_POSTAL_ADDRESS_APT_NUMBER);
        arraySet.add(AUTOFILL_HINT_POSTAL_ADDRESS_COUNTRY);
        arraySet.add(AUTOFILL_HINT_POSTAL_ADDRESS_DEPENDENT_LOCALITY);
        arraySet.add(AUTOFILL_HINT_POSTAL_ADDRESS_EXTENDED_ADDRESS);
        arraySet.add(AUTOFILL_HINT_POSTAL_ADDRESS_EXTENDED_POSTAL_CODE);
        arraySet.add(AUTOFILL_HINT_POSTAL_ADDRESS_LOCALITY);
        arraySet.add(AUTOFILL_HINT_POSTAL_ADDRESS_REGION);
        arraySet.add(AUTOFILL_HINT_POSTAL_ADDRESS_STREET_ADDRESS);
        arraySet.add(AUTOFILL_HINT_POSTAL_CODE);
        return arraySet;
    }
}
