package android.view.autofill;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.view.autofill.AutofillManager;
import com.android.internal.util.SyncResultReceiver;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/* loaded from: classes.dex */
public class AutofillManagerExtImpl implements IAutofillManagerExt {
    private static final int COMMIT_REASON_OPLUS_AUTOFILL_ACTIVITY_FINISHED = Integer.MIN_VALUE;
    private static final long MAX_REQUEST_SERVICE_COMPONENT_NAME_INTERVAL = 50;
    private static final int NATIVE_FLAG_MANUAL_REQUEST = 1;
    private static final String OPLUS_AUTOFILL_COMPONENT_NAME = "com.coloros.codebook";
    private static final int OPLUS_SYNC_CALLS_TIMEOUT_MS = 1000;
    private static final String TAG = AutofillManagerExtImpl.class.getSimpleName();
    private HashSet<String> AUTOFILL_START_SESSION_ACTIVITY_BLACK_LIST_SET;
    private boolean mIsOplusAutofillService = false;
    private long mLastRequestServiceComponentMillis = 0;

    public AutofillManagerExtImpl(Object base) {
    }

    public boolean hookSaveOnFinish(IAutoFillManager services, boolean saveOnFinish) {
        if (saveOnFinish) {
            return true;
        }
        return getAutofillServiceComponentNameInternal(services);
    }

    private boolean getAutofillServiceComponentNameInternal(IAutoFillManager services) {
        boolean z = false;
        if (services == null) {
            return false;
        }
        SyncResultReceiver receiver = new SyncResultReceiver(1000);
        try {
            services.getAutofillServiceComponentName(receiver);
            ComponentName componentName = (ComponentName) receiver.getParcelableResult();
            if (componentName != null) {
                if (componentName.getPackageName().equals(OPLUS_AUTOFILL_COMPONENT_NAME)) {
                    z = true;
                }
            }
            boolean result = z;
            return result;
        } catch (SyncResultReceiver.TimeoutException e) {
            Log.e(TAG, "getAutofillServiceComponentName error: " + e.getMessage());
            return false;
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    public int hookActivityFinishingCommitReason(int commitReason, boolean saveOnFinish) {
        if (!saveOnFinish) {
            return Integer.MIN_VALUE;
        }
        return commitReason;
    }

    public boolean hookShouldStartSession(Context context, AutofillManager.AutofillClient client, int flag) {
        ComponentName clientActivity;
        boolean result = true;
        if (client == null || (clientActivity = client.autofillClientGetComponentName()) == null) {
            return true;
        }
        ensureStartSessionBlackList(context);
        if (this.AUTOFILL_START_SESSION_ACTIVITY_BLACK_LIST_SET.contains(clientActivity.getClassName()) && (flag & 1) == 0) {
            result = false;
        }
        if (!result && Helper.sVerbose) {
            Slog.v(TAG, "hookShouldStartSession: current activity: " + clientActivity.getClassName() + " should not start autofill session!");
        }
        return result;
    }

    private void ensureStartSessionBlackList(Context context) {
        if (this.AUTOFILL_START_SESSION_ACTIVITY_BLACK_LIST_SET == null) {
            List<String> blackList = Arrays.asList(context.getResources().getStringArray(201785417));
            this.AUTOFILL_START_SESSION_ACTIVITY_BLACK_LIST_SET = new HashSet<>(blackList);
        }
    }

    public boolean hookShouldIncludeAllChildrenViewInAssistStructure(IAutoFillManager services) {
        long currentMillis = System.currentTimeMillis();
        if (currentMillis - this.mLastRequestServiceComponentMillis <= 50) {
            this.mLastRequestServiceComponentMillis = currentMillis;
            return this.mIsOplusAutofillService;
        }
        this.mIsOplusAutofillService = getAutofillServiceComponentNameInternal(services);
        this.mLastRequestServiceComponentMillis = System.currentTimeMillis();
        return this.mIsOplusAutofillService;
    }
}
