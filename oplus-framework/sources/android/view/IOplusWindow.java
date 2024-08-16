package android.view;

/* loaded from: classes.dex */
public interface IOplusWindow extends IOplusLongshotWindow, IOplusDirectWindow {
    public static final String DESCRIPTOR = "android.view.IWindow";
    public static final int DIRECT_FIND_CMD = 10008;
    public static final int LONGSHOT_COLLECT_WINDOW = 10004;
    public static final int LONGSHOT_DUMP = 10003;
    public static final int LONGSHOT_INJECT_INPUT = 10005;
    public static final int LONGSHOT_INJECT_INPUT_BEGIN = 10006;
    public static final int LONGSHOT_INJECT_INPUT_END = 10007;
    public static final int LONGSHOT_NOTIFY_CONNECTED = 10002;
    public static final int OPLUS_CALL_TRANSACTION_INDEX = 10000;
    public static final int OPLUS_FIRST_CALL_TRANSACTION = 10001;
    public static final int REQUEST_SCROLL_CAPTURE = 10010;
    public static final int SCREENSHOT_DUMP = 10009;
}
