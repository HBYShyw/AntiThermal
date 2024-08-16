package android.app;

/* loaded from: classes.dex */
public class OplusKeyguardManager {
    private static final String TAG = "OplusKeyguardManager";

    /* loaded from: classes.dex */
    public interface IKeyguardApp {
        void onCommand(String str);

        void onSyncCommand(String str);
    }

    public void registerKeyguardCallback(IKeyguardApp callback, String module) {
    }

    private boolean isSyncCommand(String command) {
        return false;
    }

    private void scheduleArriveSyncCommand(String command) {
    }

    private void scheduleArriveCommand(String command) {
    }

    private void handleCommand(String command) {
    }

    public void requestKeyguard(String command) {
    }
}
