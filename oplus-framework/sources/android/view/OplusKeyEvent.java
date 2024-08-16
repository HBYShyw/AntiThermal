package android.view;

/* loaded from: classes.dex */
public class OplusKeyEvent extends KeyEvent {
    public static final int KEYCODE_GIMBAL_POWER = 717;
    public static final int KEYCODE_GIMBAL_SWITCH_CAMERA = 706;
    public static final int KEYCODE_SHOULDER_DOWN = 760;
    public static final int KEYCODE_SHOULDER_UP = 761;

    private OplusKeyEvent(int action, int code) {
        super(action, code);
    }
}
