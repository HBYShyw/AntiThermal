package android.app;

import android.os.Parcel;
import android.text.TextUtils;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: classes.dex */
public class OplusNotificationChannelExtImpl implements INotificationChannelExt {
    private static final String ATT_BADGE_OPTION = "badge_option";
    private static final String ATT_CHANGEABLE_FOLD = "changeable_fold";
    private static final String ATT_CHANGEABLE_SHOW_ICON = "changeable_show_icon";
    private static final String ATT_FOLD = "fold";
    private static final String ATT_MAX_MESSAGES = "max_messages";
    private static final String ATT_OPUSH = "opush";
    private static final String ATT_SHOW_BANNER = "show_banner";
    private static final String ATT_SHOW_ICON = "show_icon";
    private static final String ATT_SUPPORT_NUM_BADGE = "support_num_badge";
    private static final String ATT_TEMP_SHOW_BADGE = "temp_show_badge";
    private static final String ATT_UNIMPORTANT = "unimportant";
    private static final int CONSTANT_HASH_CODE = 31;
    private NotificationChannel mBase;
    private boolean mFold = false;
    private boolean mOpush = false;
    private boolean mShowBanner = false;
    private boolean mShowIcon = false;
    private int mMaxMessages = -1;
    private boolean mSupportNumBadge = false;
    private int mBadgeOption = 0;
    private boolean mChangeableFold = true;
    private boolean mChangeableShowIcon = true;
    private boolean mUnimportant = false;
    private boolean mTempShowBadge = true;

    public OplusNotificationChannelExtImpl(Object base) {
        this.mBase = (NotificationChannel) base;
    }

    public void init(String id, CharSequence name, int importance) {
        this.mShowBanner = importance >= 4;
        this.mUnimportant = isUnimportantChannel(importance);
    }

    protected OplusNotificationChannelExtImpl(Parcel in) {
    }

    public void readFromParcel(Parcel in) {
        this.mShowBanner = in.readBoolean();
        this.mFold = in.readBoolean();
        this.mOpush = in.readBoolean();
        this.mShowIcon = in.readBoolean();
        this.mMaxMessages = in.readInt();
        this.mSupportNumBadge = in.readBoolean();
        this.mBadgeOption = in.readInt();
        this.mChangeableFold = in.readBoolean();
        this.mChangeableShowIcon = in.readBoolean();
        this.mUnimportant = in.readBoolean();
        this.mTempShowBadge = in.readBoolean();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mShowBanner);
        dest.writeBoolean(this.mFold);
        dest.writeBoolean(this.mOpush);
        dest.writeBoolean(this.mShowIcon);
        dest.writeInt(this.mMaxMessages);
        dest.writeBoolean(this.mSupportNumBadge);
        dest.writeInt(this.mBadgeOption);
        dest.writeBoolean(this.mChangeableFold);
        dest.writeBoolean(this.mChangeableShowIcon);
        dest.writeBoolean(this.mUnimportant);
        dest.writeBoolean(this.mTempShowBadge);
    }

    public boolean canShowBanner() {
        return this.mShowBanner;
    }

    public void setShowBanner(boolean show) {
        this.mShowBanner = show;
    }

    public boolean isUnimportant() {
        return this.mUnimportant;
    }

    public void setUnimportant(boolean isUnimportant) {
        this.mUnimportant = isUnimportant;
    }

    public boolean isFold() {
        return this.mFold;
    }

    public void setFold(boolean fold) {
        this.mFold = fold;
    }

    public void setOpush(boolean isOpush) {
        this.mOpush = isOpush;
    }

    public boolean isOpush() {
        return this.mOpush;
    }

    public void setMaxMessage(int max) {
        this.mMaxMessages = max;
    }

    public int getMaxMessages() {
        return this.mMaxMessages;
    }

    public void setShowIcon(boolean show) {
        this.mShowIcon = show;
    }

    public boolean canShowIcon() {
        return this.mShowIcon;
    }

    public void setSupportNumBadge(boolean supportNumBadge) {
        this.mSupportNumBadge = supportNumBadge;
    }

    public boolean isSupportNumBadge() {
        return this.mSupportNumBadge;
    }

    public void setBadgeOption(int badgeOption) {
        this.mBadgeOption = badgeOption;
    }

    public int getBadgeOption() {
        return this.mBadgeOption;
    }

    public boolean isChangeableFold() {
        return this.mChangeableFold;
    }

    public void setChangeableFold(boolean changeable) {
        this.mChangeableFold = changeable;
    }

    public boolean isChangeableShowIcon() {
        return this.mChangeableShowIcon;
    }

    public void setChangeableShowIcon(boolean changeable) {
        this.mChangeableShowIcon = changeable;
    }

    public void populateFromXml(XmlPullParser parser) {
        setShowBanner(safeBool(parser, ATT_SHOW_BANNER, false));
        setFold(safeBool(parser, ATT_FOLD, false));
        setOpush(safeBool(parser, ATT_OPUSH, false));
        setShowIcon(safeBool(parser, ATT_SHOW_ICON, false));
        setMaxMessage(safeInt(parser, ATT_MAX_MESSAGES, -1));
        setSupportNumBadge(safeBool(parser, ATT_SUPPORT_NUM_BADGE, false));
        setBadgeOption(safeInt(parser, ATT_BADGE_OPTION, 0));
        setChangeableFold(safeBool(parser, ATT_CHANGEABLE_FOLD, true));
        setChangeableShowIcon(safeBool(parser, ATT_CHANGEABLE_SHOW_ICON, true));
        setUnimportant(safeBool(parser, ATT_UNIMPORTANT, false));
        setTempShowBadge(safeBool(parser, ATT_TEMP_SHOW_BADGE, false));
    }

    public void writeXml(XmlSerializer out) throws IOException {
        if (canShowBanner()) {
            out.attribute(null, ATT_SHOW_BANNER, Boolean.toString(canShowBanner()));
        }
        if (isFold()) {
            out.attribute(null, ATT_FOLD, Boolean.toString(isFold()));
        }
        if (isOpush()) {
            out.attribute(null, ATT_OPUSH, Boolean.toString(isOpush()));
        }
        if (canShowIcon()) {
            out.attribute(null, ATT_SHOW_ICON, Boolean.toString(canShowIcon()));
        }
        if (getMaxMessages() != -1) {
            out.attribute(null, ATT_MAX_MESSAGES, String.valueOf(getMaxMessages()));
        }
        if (isSupportNumBadge()) {
            out.attribute(null, ATT_SUPPORT_NUM_BADGE, Boolean.toString(isSupportNumBadge()));
        }
        if (getBadgeOption() != 0) {
            out.attribute(null, ATT_BADGE_OPTION, String.valueOf(getBadgeOption()));
        }
        if (isChangeableFold()) {
            out.attribute(null, ATT_CHANGEABLE_FOLD, String.valueOf(isChangeableFold()));
        }
        if (isChangeableShowIcon()) {
            out.attribute(null, ATT_CHANGEABLE_SHOW_ICON, String.valueOf(isChangeableShowIcon()));
        }
        if (isUnimportant()) {
            out.attribute(null, ATT_UNIMPORTANT, String.valueOf(isUnimportant()));
        }
        if (canTempShowBadge()) {
            out.attribute(null, ATT_TEMP_SHOW_BADGE, String.valueOf(canTempShowBadge()));
        }
    }

    public void toJson(JSONObject record) throws JSONException {
        record.put(ATT_SHOW_BANNER, canShowBanner());
        record.put(ATT_FOLD, isFold());
        record.put(ATT_OPUSH, isOpush());
        record.put(ATT_SHOW_ICON, canShowIcon());
        record.put(ATT_MAX_MESSAGES, getMaxMessages());
        record.put(ATT_SUPPORT_NUM_BADGE, isSupportNumBadge());
        record.put(ATT_BADGE_OPTION, getBadgeOption());
        record.put(ATT_CHANGEABLE_FOLD, isChangeableFold());
        record.put(ATT_CHANGEABLE_SHOW_ICON, isChangeableShowIcon());
        record.put(ATT_UNIMPORTANT, isUnimportant());
        record.put(ATT_TEMP_SHOW_BADGE, canTempShowBadge());
    }

    public boolean equals(INotificationChannelExt that) {
        return this.mShowBanner == that.canShowBanner() && this.mFold == that.isFold() && this.mOpush == that.isOpush() && this.mShowIcon == that.canShowIcon() && this.mMaxMessages == that.getMaxMessages() && this.mSupportNumBadge == that.isSupportNumBadge() && this.mBadgeOption == that.getBadgeOption() && this.mChangeableFold == that.isChangeableFold() && this.mChangeableShowIcon == that.isChangeableShowIcon() && this.mUnimportant == that.isUnimportant() && this.mTempShowBadge == that.canTempShowBadge();
    }

    public int hashCode(int i) {
        return (((((((((((((((((((((i * 31) + (canShowBanner() ? 1 : 0)) * 31) + (isFold() ? 1 : 0)) * 31) + (isOpush() ? 1 : 0)) * 31) + (canShowIcon() ? 1 : 0)) * 31) + getMaxMessages()) * 31) + (isSupportNumBadge() ? 1 : 0)) * 31) + getBadgeOption()) * 31) + (isChangeableFold() ? 1 : 0)) * 31) + (isChangeableShowIcon() ? 1 : 0)) * 31) + (isUnimportant() ? 1 : 0)) * 31) + (canTempShowBadge() ? 1 : 0);
    }

    public String toString() {
        return ", mShowBanner=" + this.mShowBanner + ", mFold=" + this.mFold + ", mOpush=" + this.mOpush + ", mShowIcon=" + this.mShowIcon + ", mMaxMessages=" + this.mMaxMessages + ", mSupportNumBadge=" + this.mSupportNumBadge + ", mBadgeOption=" + this.mBadgeOption + ", mChangeableFold=" + this.mChangeableFold + ", mChangeableShowIcon=" + this.mChangeableShowIcon + ", mUnimportant=" + this.mUnimportant + ", mTempShowBadge=" + this.mTempShowBadge;
    }

    private static boolean safeBool(XmlPullParser parser, String att, boolean defValue) {
        String value = parser.getAttributeValue(null, att);
        if (TextUtils.isEmpty(value)) {
            return defValue;
        }
        return Boolean.parseBoolean(value);
    }

    private static int safeInt(XmlPullParser parser, String att, int defValue) {
        String val = parser.getAttributeValue(null, att);
        return tryParseInt(val, defValue);
    }

    private static int tryParseInt(String value, int defValue) {
        if (TextUtils.isEmpty(value)) {
            return defValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    private boolean isUnimportantChannel(int importance) {
        return importance == 2 || importance == 1;
    }

    public boolean canTempShowBadge() {
        return this.mTempShowBadge;
    }

    public void setTempShowBadge(boolean tempShowBadge) {
        this.mTempShowBadge = tempShowBadge;
    }
}
