package android.os;

import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.OplusWindowManager;

/* loaded from: classes.dex */
public final class OplusExManager {
    private static final String OPLUS_EX_CHANNEL_NAME = "OplusExInputReceiver";
    public static final String SERVICE_NAME = "OPLUSExService";
    public static final String TAG = "OplusExManager";
    private InputChannel mExInputChannel;
    private OplusExInputEventReceiver mExInputEventReceiver;
    OplusWindowManager mWindowManager;

    /* loaded from: classes.dex */
    public interface IExInputEventReceiverCallback {
        boolean onInputEvent(InputEvent inputEvent);
    }

    public OplusExManager(IOplusExService service) {
        this.mExInputEventReceiver = null;
        this.mExInputChannel = new InputChannel();
        this.mWindowManager = new OplusWindowManager();
    }

    public OplusExManager() {
        this(null);
    }

    public boolean enableInputReceiver(Binder token, IExInputEventReceiverCallback callback) {
        return false;
    }

    public void disableInputReceiver() {
    }

    /* loaded from: classes.dex */
    private final class OplusExInputEventReceiver extends InputEventReceiver {
        private IExInputEventReceiverCallback mCallback;

        public void setCallback(IExInputEventReceiverCallback callback) {
            this.mCallback = callback;
        }

        public OplusExInputEventReceiver(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
        }

        public void onInputEvent(InputEvent event) {
            IExInputEventReceiverCallback iExInputEventReceiverCallback = this.mCallback;
            if (iExInputEventReceiverCallback != null) {
                boolean consumed = iExInputEventReceiverCallback.onInputEvent(event);
                finishInputEvent(event, consumed);
            }
        }
    }
}
