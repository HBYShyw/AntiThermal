package com.android.server.permission.access.appop;

import com.android.modules.utils.BinaryXmlPullParser;
import com.android.modules.utils.BinaryXmlSerializer;
import com.android.server.permission.access.AccessState;
import com.android.server.permission.access.SchemePolicy;
import org.jetbrains.annotations.NotNull;

/* compiled from: BaseAppOpPolicy.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class BaseAppOpPolicy extends SchemePolicy {

    @NotNull
    private final BaseAppOpPersistence persistence;

    @Override // com.android.server.permission.access.SchemePolicy
    @NotNull
    public String getObjectScheme() {
        return "app-op";
    }

    public BaseAppOpPolicy(@NotNull BaseAppOpPersistence baseAppOpPersistence) {
        this.persistence = baseAppOpPersistence;
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void parseUserState(@NotNull BinaryXmlPullParser binaryXmlPullParser, @NotNull AccessState accessState, int i) {
        this.persistence.parseUserState(binaryXmlPullParser, accessState, i);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void serializeUserState(@NotNull BinaryXmlSerializer binaryXmlSerializer, @NotNull AccessState accessState, int i) {
        this.persistence.serializeUserState(binaryXmlSerializer, accessState, i);
    }
}
