package com.android.server.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.service.notification.SnoozeCriterion;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class NotificationRecordExtractorData {
    private final boolean mAllowBubble;
    private final NotificationChannel mChannel;
    private final String mGroupKey;
    private final int mImportance;
    private final boolean mIsBubble;
    private final boolean mIsConversation;
    private final ArrayList<String> mOverridePeople;
    private final int mPosition;
    private final int mProposedImportance;
    private final float mRankingScore;
    private final boolean mSensitiveContent;
    private final boolean mShowBadge;
    private final ArrayList<CharSequence> mSmartReplies;
    private final ArrayList<SnoozeCriterion> mSnoozeCriteria;
    private final Integer mSuppressVisually;
    private final ArrayList<Notification.Action> mSystemSmartActions;
    private final Integer mUserSentiment;
    private final int mVisibility;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NotificationRecordExtractorData(int i, int i2, boolean z, boolean z2, boolean z3, NotificationChannel notificationChannel, String str, ArrayList<String> arrayList, ArrayList<SnoozeCriterion> arrayList2, Integer num, Integer num2, ArrayList<Notification.Action> arrayList3, ArrayList<CharSequence> arrayList4, int i3, float f, boolean z4, int i4, boolean z5) {
        this.mPosition = i;
        this.mVisibility = i2;
        this.mShowBadge = z;
        this.mAllowBubble = z2;
        this.mIsBubble = z3;
        this.mChannel = notificationChannel;
        this.mGroupKey = str;
        this.mOverridePeople = arrayList;
        this.mSnoozeCriteria = arrayList2;
        this.mUserSentiment = num;
        this.mSuppressVisually = num2;
        this.mSystemSmartActions = arrayList3;
        this.mSmartReplies = arrayList4;
        this.mImportance = i3;
        this.mRankingScore = f;
        this.mIsConversation = z4;
        this.mProposedImportance = i4;
        this.mSensitiveContent = z5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasDiffForRankingLocked(NotificationRecord notificationRecord, int i) {
        return (this.mPosition == i && this.mVisibility == notificationRecord.getPackageVisibilityOverride() && this.mShowBadge == notificationRecord.canShowBadge() && this.mAllowBubble == notificationRecord.canBubble() && this.mIsBubble == notificationRecord.getNotification().isBubbleNotification() && Objects.equals(this.mChannel, notificationRecord.getChannel()) && Objects.equals(this.mGroupKey, notificationRecord.getGroupKey()) && Objects.equals(this.mOverridePeople, notificationRecord.getPeopleOverride()) && Objects.equals(this.mSnoozeCriteria, notificationRecord.getSnoozeCriteria()) && Objects.equals(this.mUserSentiment, Integer.valueOf(notificationRecord.getUserSentiment())) && Objects.equals(this.mSuppressVisually, Integer.valueOf(notificationRecord.getSuppressedVisualEffects())) && Objects.equals(this.mSystemSmartActions, notificationRecord.getSystemGeneratedSmartActions()) && Objects.equals(this.mSmartReplies, notificationRecord.getSmartReplies()) && this.mImportance == notificationRecord.getImportance() && this.mProposedImportance == notificationRecord.getProposedImportance() && this.mSensitiveContent == notificationRecord.hasSensitiveContent()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasDiffForLoggingLocked(NotificationRecord notificationRecord, int i) {
        return (this.mPosition == i && Objects.equals(this.mChannel, notificationRecord.getChannel()) && Objects.equals(this.mGroupKey, notificationRecord.getGroupKey()) && Objects.equals(this.mOverridePeople, notificationRecord.getPeopleOverride()) && Objects.equals(this.mSnoozeCriteria, notificationRecord.getSnoozeCriteria()) && Objects.equals(this.mUserSentiment, Integer.valueOf(notificationRecord.getUserSentiment())) && Objects.equals(this.mSystemSmartActions, notificationRecord.getSystemGeneratedSmartActions()) && Objects.equals(this.mSmartReplies, notificationRecord.getSmartReplies()) && this.mImportance == notificationRecord.getImportance() && notificationRecord.rankingScoreMatches(this.mRankingScore) && this.mIsConversation == notificationRecord.isConversation() && this.mProposedImportance == notificationRecord.getProposedImportance() && this.mSensitiveContent == notificationRecord.hasSensitiveContent()) ? false : true;
    }
}
