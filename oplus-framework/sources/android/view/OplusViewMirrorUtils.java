package android.view;

import com.google.android.collect.Sets;
import com.oplus.bracket.OplusBracketLog;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusViewMirrorUtils {
    private static final float WIDTH_SALT = 0.75f;
    private static String TAG = "OplusViewMirrorUtils";
    public static final String TITLE_DOU_YU_PLAYER_ACTIVITY = "air.tv.douyu.android/tv.douyu.view.activity.PlayerActivity";
    public static final Set<String> ADJUST_SYSTEM_UI_VISIBILITY_ACTIVITIES = Sets.newArraySet(new String[]{"com.tencent.qqlive/com.tencent.qqlive.ona.activity.VideoDetailActivity", TITLE_DOU_YU_PLAYER_ACTIVITY});

    public static boolean checkViewSizeConstraints(boolean viewSizeLimit, View v, ViewRootImpl viewRoot) {
        if (viewRoot != null && v != null) {
            int hostWidth = viewRoot.getWidth();
            int viewWidth = v.getWidth();
            if (viewSizeLimit && hostWidth > 0 && viewWidth > 0) {
                OplusBracketLog.d(TAG, "checkTargetViewSizeIfNeeded, hostWidth:" + hostWidth + ", viewWidth:" + viewWidth);
                return ((float) v.getWidth()) > ((float) hostWidth) * 0.75f;
            }
        }
        return true;
    }

    public static boolean shouldAdjustSysUIVisibility(CharSequence title) {
        if (title == null) {
            return false;
        }
        return ADJUST_SYSTEM_UI_VISIBILITY_ACTIVITIES.contains(title.toString());
    }

    public static boolean alwaysUpdateOriginViewSysUIVisibility(CharSequence title) {
        if (title == null) {
            return false;
        }
        return !TITLE_DOU_YU_PLAYER_ACTIVITY.equals(title.toString());
    }
}
