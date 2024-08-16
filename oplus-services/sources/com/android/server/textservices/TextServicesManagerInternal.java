package com.android.server.textservices;

import android.view.textservice.SpellCheckerInfo;
import com.android.server.LocalServices;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class TextServicesManagerInternal {
    private static final TextServicesManagerInternal NOP = new TextServicesManagerInternal() { // from class: com.android.server.textservices.TextServicesManagerInternal.1
        @Override // com.android.server.textservices.TextServicesManagerInternal
        public SpellCheckerInfo getCurrentSpellCheckerForUser(int i) {
            return null;
        }
    };

    public abstract SpellCheckerInfo getCurrentSpellCheckerForUser(int i);

    public static TextServicesManagerInternal get() {
        TextServicesManagerInternal textServicesManagerInternal = (TextServicesManagerInternal) LocalServices.getService(TextServicesManagerInternal.class);
        return textServicesManagerInternal != null ? textServicesManagerInternal : NOP;
    }
}
