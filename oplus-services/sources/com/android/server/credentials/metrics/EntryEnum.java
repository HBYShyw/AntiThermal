package com.android.server.credentials.metrics;

import android.util.Slog;
import com.android.server.credentials.ProviderCreateSession;
import com.android.server.credentials.ProviderGetSession;
import java.util.AbstractMap;
import java.util.Map;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v1 com.android.server.credentials.metrics.EntryEnum, still in use, count: 1, list:
  (r0v1 com.android.server.credentials.metrics.EntryEnum) from 0x003a: IGET (r0v1 com.android.server.credentials.metrics.EntryEnum) A[WRAPPED] com.android.server.credentials.metrics.EntryEnum.mInnerMetricCode int
	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:88)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:87)
	at jadx.core.utils.InsnRemover.removeAllAndUnbind(InsnRemover.java:238)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:180)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class EntryEnum {
    UNKNOWN(0),
    ACTION_ENTRY(1),
    CREDENTIAL_ENTRY(2),
    REMOTE_ENTRY(3),
    AUTHENTICATION_ENTRY(4);

    private static final String TAG = "EntryEnum";
    private static final Map<String, Integer> sKeyToEntryCode;
    private final int mInnerMetricCode;

    public static EntryEnum valueOf(String str) {
        return (EntryEnum) Enum.valueOf(EntryEnum.class, str);
    }

    public static EntryEnum[] values() {
        return (EntryEnum[]) $VALUES.clone();
    }

    static {
        EntryEnum entryEnum = CREDENTIAL_ENTRY;
        sKeyToEntryCode = Map.ofEntries(new AbstractMap.SimpleEntry(ProviderGetSession.ACTION_ENTRY_KEY, Integer.valueOf(new EntryEnum(1).mInnerMetricCode)), new AbstractMap.SimpleEntry(ProviderGetSession.AUTHENTICATION_ACTION_ENTRY_KEY, Integer.valueOf(r3.mInnerMetricCode)), new AbstractMap.SimpleEntry(ProviderGetSession.REMOTE_ENTRY_KEY, Integer.valueOf(new EntryEnum(3).mInnerMetricCode)), new AbstractMap.SimpleEntry(ProviderGetSession.CREDENTIAL_ENTRY_KEY, Integer.valueOf(entryEnum.mInnerMetricCode)), new AbstractMap.SimpleEntry(ProviderCreateSession.SAVE_ENTRY_KEY, Integer.valueOf(entryEnum.mInnerMetricCode)));
    }

    private EntryEnum(int i) {
        this.mInnerMetricCode = i;
    }

    public int getMetricCode() {
        return this.mInnerMetricCode;
    }

    public static int getMetricCodeFromString(String str) {
        Map<String, Integer> map = sKeyToEntryCode;
        if (!map.containsKey(str)) {
            Slog.i(TAG, "Attempted to use an unsupported string key entry type");
            return UNKNOWN.mInnerMetricCode;
        }
        return map.get(str).intValue();
    }
}
