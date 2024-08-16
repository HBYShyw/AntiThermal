package com.android.server.companion.securechannel;

import android.content.Context;
import android.os.Bundle;
import android.security.attestationverification.AttestationProfile;
import android.security.attestationverification.AttestationVerificationManager;
import android.security.attestationverification.VerificationToken;
import com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda1;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AttestationVerifier {
    private static final long ATTESTATION_VERIFICATION_TIMEOUT_SECONDS = 10;
    private static final String PARAM_OWNED_BY_SYSTEM = "android.key_owned_by_system";
    private final Context mContext;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AttestationVerifier(Context context) {
        this.mContext = context;
    }

    public int verifyAttestation(byte[] bArr, byte[] bArr2) throws SecureChannelException {
        Bundle bundle = new Bundle();
        bundle.putByteArray("localbinding.challenge", bArr2);
        bundle.putBoolean(PARAM_OWNED_BY_SYSTEM, true);
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ((AttestationVerificationManager) this.mContext.getSystemService(AttestationVerificationManager.class)).verifyAttestation(new AttestationProfile(3), 3, bundle, bArr, new SystemServerInitThreadPool$$ExternalSyntheticLambda1(), new BiConsumer() { // from class: com.android.server.companion.securechannel.AttestationVerifier$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AttestationVerifier.lambda$verifyAttestation$0(atomicInteger, countDownLatch, (Integer) obj, (VerificationToken) obj2);
            }
        });
        try {
            if (!countDownLatch.await(ATTESTATION_VERIFICATION_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new SecureChannelException("Attestation verification timed out.");
            }
            return atomicInteger.get();
        } catch (InterruptedException e) {
            throw new SecureChannelException("Attestation verification was interrupted", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$verifyAttestation$0(AtomicInteger atomicInteger, CountDownLatch countDownLatch, Integer num, VerificationToken verificationToken) {
        atomicInteger.set(num.intValue());
        countDownLatch.countDown();
    }
}
