package s0;

/* compiled from: NegotiationAlgorithmEnum.java */
/* renamed from: s0.k, reason: use source file name */
/* loaded from: classes.dex */
public enum NegotiationAlgorithmEnum {
    RSA,
    EC,
    NOISE_NN,
    NOISE_NK,
    NOISE_KK,
    NOISE_IK,
    NOISE_IX;

    public static NegotiationAlgorithmEnum a(String str) {
        str.hashCode();
        char c10 = 65535;
        switch (str.hashCode()) {
            case 2206:
                if (str.equals("EC")) {
                    c10 = 0;
                    break;
                }
                break;
            case 81440:
                if (str.equals("RSA")) {
                    c10 = 1;
                    break;
                }
                break;
            case 2062296199:
                if (str.equals("NOISE_IK")) {
                    c10 = 2;
                    break;
                }
                break;
            case 2062296212:
                if (str.equals("NOISE_IX")) {
                    c10 = 3;
                    break;
                }
                break;
            case 2062296261:
                if (str.equals("NOISE_KK")) {
                    c10 = 4;
                    break;
                }
                break;
            case 2062296354:
                if (str.equals("NOISE_NK")) {
                    c10 = 5;
                    break;
                }
                break;
            case 2062296357:
                if (str.equals("NOISE_NN")) {
                    c10 = 6;
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                return EC;
            case 1:
                return RSA;
            case 2:
                return NOISE_IK;
            case 3:
                return NOISE_IX;
            case 4:
                return NOISE_KK;
            case 5:
                return NOISE_NK;
            case 6:
                return NOISE_NN;
            default:
                throw new IllegalStateException("Unexpected value: " + str);
        }
    }
}
