package android.os.customize;

import android.content.Context;
import android.os.customize.IOplusCustomizeContactManagerService;
import android.util.Slog;

/* loaded from: classes.dex */
public class OplusCustomizeContactManager {
    public static final int BLACK_LIST_BLOCK_PATTERN_BLACK_LIST = 1;
    public static final int BLACK_LIST_BLOCK_PATTERN_INVALID = 0;
    public static final int BLACK_LIST_BLOCK_PATTERN_WHITE_LIST = 2;
    public static final int BLACK_LIST_MATCH_PATTERN_ALL = 0;
    public static final int BLACK_LIST_MATCH_PATTERN_ALLOW_ALL = 4;
    public static final int BLACK_LIST_MATCH_PATTERN_FUZZY = 2;
    public static final int BLACK_LIST_MATCH_PATTERN_INTERCEPT_ALL = 3;
    public static final int BLACK_LIST_MATCH_PATTERN_PREFIX = 1;
    public static final int BLACK_LIST_OUTGO_OR_INCOME_PATTERN_ALL = 2;
    public static final int BLACK_LIST_OUTGO_OR_INCOME_PATTERN_INCOME = 1;
    public static final int BLACK_LIST_OUTGO_OR_INCOME_PATTERN_OUTGO = 0;
    public static final int FORBID_CALL_LOG_DISABLE = 0;
    public static final int FORBID_CALL_LOG_ENABLE = 1;
    public static final int NUMBER_MASK_HIDE_DISABLE = 2;
    public static final int NUMBER_MASK_HIDE_ENABLE = 1;
    public static final int NUMBER_MASK_HIDE_MODE_END = 2;
    public static final int NUMBER_MASK_HIDE_MODE_MIDDLE = 1;
    private static final String SERVICE_NAME = "OplusCustomizeContactManagerService";
    private static final String TAG = "OplusCustomizeContactManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeContactManager sInstance;
    private IOplusCustomizeContactManagerService mOplusCustomizeContactManagerService;

    private OplusCustomizeContactManager() {
        getOplusCustomizeContactManagerService();
    }

    public static final OplusCustomizeContactManager getInstance(Context context) {
        OplusCustomizeContactManager oplusCustomizeContactManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeContactManager();
                }
                oplusCustomizeContactManager = sInstance;
            }
            return oplusCustomizeContactManager;
        }
        return sInstance;
    }

    private IOplusCustomizeContactManagerService getOplusCustomizeContactManagerService() {
        IOplusCustomizeContactManagerService iOplusCustomizeContactManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeContactManagerService == null) {
                this.mOplusCustomizeContactManagerService = IOplusCustomizeContactManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            iOplusCustomizeContactManagerService = this.mOplusCustomizeContactManagerService;
        }
        return iOplusCustomizeContactManagerService;
    }

    public boolean setContactBlackListEnable(boolean enable) {
        try {
            boolean result = this.mOplusCustomizeContactManagerService.setContactBlackListEnable(enable);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setContactBlackListEnable Error :" + e);
            return false;
        }
    }

    public boolean isContactBlackListEnable() {
        try {
            boolean result = this.mOplusCustomizeContactManagerService.isContactBlackListEnable();
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "isContactBlackListEnable Error :" + e);
            return false;
        }
    }

    public boolean setContactBlockPattern(int blockPattern) {
        try {
            boolean result = this.mOplusCustomizeContactManagerService.setContactBlockPattern(blockPattern);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setContactBlockPattern Error :" + e);
            return false;
        }
    }

    public int getContactBlockPattern() {
        try {
            int result = this.mOplusCustomizeContactManagerService.getContactBlockPattern();
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "getContactBlockPattern Error :" + e);
            return -1;
        }
    }

    public boolean setContactMatchPattern(int matchPattern) {
        try {
            boolean result = this.mOplusCustomizeContactManagerService.setContactMatchPattern(matchPattern);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setContactMatchPattern Error :" + e);
            return false;
        }
    }

    public int getContactMatchPattern() {
        try {
            int result = this.mOplusCustomizeContactManagerService.getContactMatchPattern();
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "getContactMatchPattern Error :" + e);
            return -1;
        }
    }

    public boolean setContactOutgoOrIncomePattern(int outgoOrIncomePattern) {
        try {
            boolean result = this.mOplusCustomizeContactManagerService.setContactOutgoOrIncomePattern(outgoOrIncomePattern);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setContactOutgoOrIncomePattern Error :" + e);
            return false;
        }
    }

    public int getContactOutgoOrIncomePattern() {
        try {
            int result = this.mOplusCustomizeContactManagerService.getContactOutgoOrIncomePattern();
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "getContactOutgoOrIncomePattern Error :" + e);
            return -1;
        }
    }

    public boolean setContactNumberHideMode(int mode) {
        try {
            boolean result = this.mOplusCustomizeContactManagerService.setContactNumberHideMode(mode);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setContactNumberHideMode Error :" + e);
            return false;
        }
    }

    public int getContactNumberHideMode() {
        try {
            int result = this.mOplusCustomizeContactManagerService.getContactNumberHideMode();
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "getContactNumberHideMode Error :" + e);
            return -1;
        }
    }

    public boolean setContactNumberMaskEnable(int switcher) {
        try {
            boolean result = this.mOplusCustomizeContactManagerService.setContactNumberMaskEnable(switcher);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setContactNumberMaskEnable Error :" + e);
            return false;
        }
    }

    public int getContactNumberMaskEnable() {
        try {
            int result = this.mOplusCustomizeContactManagerService.getContactNumberMaskEnable();
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "getContactNumberMaskEnable Error :" + e);
            return -1;
        }
    }

    public boolean setForbidCallLogEnable(int forbid) {
        try {
            boolean result = this.mOplusCustomizeContactManagerService.setForbidCallLogEnable(forbid);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setForbidCallLogEnable Error :" + e);
            return false;
        }
    }

    public boolean isForbidCallLogEnable() {
        try {
            boolean result = this.mOplusCustomizeContactManagerService.isForbidCallLogEnable();
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "isForbidCallLogEnable Error :" + e);
            return false;
        }
    }
}
