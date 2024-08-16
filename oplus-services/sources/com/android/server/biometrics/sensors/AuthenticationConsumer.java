package com.android.server.biometrics.sensors;

import android.hardware.biometrics.BiometricAuthenticator;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface AuthenticationConsumer {
    void onAuthenticated(BiometricAuthenticator.Identifier identifier, boolean z, ArrayList<Byte> arrayList);
}
