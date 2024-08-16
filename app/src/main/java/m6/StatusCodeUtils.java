package m6;

import com.oplus.deepthinker.sdk.app.awareness.AwarenessStatusCodes;
import kotlin.Metadata;
import za.k;

/* compiled from: StatusCodeUtils.kt */
@Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u000e\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000¨\u0006\u0004"}, d2 = {"", "code", "", "a", "com.oplus.deepthinker.sdk_release"}, k = 2, mv = {1, 6, 0})
/* renamed from: m6.b, reason: use source file name */
/* loaded from: classes.dex */
public final class StatusCodeUtils {
    public static final String a(int i10) {
        if (i10 == 0) {
            return "NOT_IMPLEMENTED";
        }
        if (i10 == 1) {
            return "SUCCESS";
        }
        if (i10 == 2) {
            return "EVENT_NOT_AVAILABLE";
        }
        if (i10 == 4) {
            return "PID_REGISTER_LIMITED";
        }
        if (i10 == 8) {
            return "OS_VERSION_NOT_SUPPORT";
        }
        if (i10 == 16) {
            return "INVALID_PARAMETERS";
        }
        if (i10 == 32) {
            return "SERVER_INTERNAL_ERROR";
        }
        if (i10 == 64) {
            return "UNSUPPORTED_PARAMETERS";
        }
        if (i10 == 128) {
            return "BINDER_TRANSACTION_ERROR";
        }
        if (i10 == 256) {
            return "PERMISSION_NOT_GRANT";
        }
        switch (i10) {
            case 501:
                return "TIMEOUT";
            case 502:
                return "INTERRUPTED";
            case 503:
                return "CANCELED";
            case 504:
                return "NOT_AVAILABLE";
            case 505:
                return "NOT_SUPPORTED";
            case 506:
                return "NOT_REGISTERED";
            case 507:
                return "REMOTE_EXCEPTION";
            default:
                switch (i10) {
                    case AwarenessStatusCodes.CAPABILITY_NOT_AVAILABLE /* 5001 */:
                        return "CAPABILITY_NOT_AVAILABLE";
                    case AwarenessStatusCodes.CAPABILITY_NOT_REGISTERED /* 5002 */:
                        return "CAPABILITY_NOT_REGISTERED";
                    case AwarenessStatusCodes.CAPABILITY_NOT_SUBSCRIBED /* 5003 */:
                        return "CAPABILITY_NOT_SUBSCRIBED";
                    case AwarenessStatusCodes.CAPABILITY_REGISTERED_REPEAT /* 5004 */:
                        return "CAPABILITY_REGISTERED_REPEAT";
                    case AwarenessStatusCodes.FENCE_NOT_AVAILABLE /* 5005 */:
                        return "FENCE_NOT_AVAILABLE";
                    case AwarenessStatusCodes.FENCE_NOT_REGISTERED /* 5006 */:
                        return "FENCE_NOT_REGISTERED";
                    case AwarenessStatusCodes.FENCE_REGISTRATIONS_LIMIT /* 5007 */:
                        return "FENCE_REGISTRATIONS_LIMIT";
                    case AwarenessStatusCodes.FENCE_REGISTERED_REPEAT /* 5008 */:
                        return "FENCE_REGISTERED_REPEAT";
                    default:
                        return k.l("unknown status code: ", Integer.valueOf(i10));
                }
        }
    }
}
