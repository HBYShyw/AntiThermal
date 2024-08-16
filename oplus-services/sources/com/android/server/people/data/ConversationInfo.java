package com.android.server.people.data;

import android.app.people.ConversationStatus;
import android.content.LocusId;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Slog;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ConversationInfo {
    private static final boolean DEBUG = false;
    private static final int FLAG_BUBBLED = 4;
    private static final int FLAG_CONTACT_STARRED = 32;
    private static final int FLAG_DEMOTED = 64;
    private static final int FLAG_IMPORTANT = 1;
    private static final int FLAG_NOTIFICATION_SILENCED = 2;
    private static final int FLAG_PERSON_BOT = 16;
    private static final int FLAG_PERSON_IMPORTANT = 8;
    private static final String TAG = "ConversationInfo";
    private static final int VERSION = 1;
    private String mContactPhoneNumber;
    private Uri mContactUri;
    private int mConversationFlags;
    private long mCreationTimestamp;
    private Map<String, ConversationStatus> mCurrStatuses;
    private long mLastEventTimestamp;
    private LocusId mLocusId;
    private String mNotificationChannelId;
    private String mParentNotificationChannelId;
    private int mShortcutFlags;
    private String mShortcutId;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface ConversationFlags {
    }

    private ConversationInfo(Builder builder) {
        this.mShortcutId = builder.mShortcutId;
        this.mLocusId = builder.mLocusId;
        this.mContactUri = builder.mContactUri;
        this.mContactPhoneNumber = builder.mContactPhoneNumber;
        this.mNotificationChannelId = builder.mNotificationChannelId;
        this.mParentNotificationChannelId = builder.mParentNotificationChannelId;
        this.mLastEventTimestamp = builder.mLastEventTimestamp;
        this.mCreationTimestamp = builder.mCreationTimestamp;
        this.mShortcutFlags = builder.mShortcutFlags;
        this.mConversationFlags = builder.mConversationFlags;
        this.mCurrStatuses = builder.mCurrStatuses;
    }

    public String getShortcutId() {
        return this.mShortcutId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocusId getLocusId() {
        return this.mLocusId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Uri getContactUri() {
        return this.mContactUri;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getContactPhoneNumber() {
        return this.mContactPhoneNumber;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getNotificationChannelId() {
        return this.mNotificationChannelId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getParentNotificationChannelId() {
        return this.mParentNotificationChannelId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLastEventTimestamp() {
        return this.mLastEventTimestamp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getCreationTimestamp() {
        return this.mCreationTimestamp;
    }

    public boolean isShortcutLongLived() {
        return hasShortcutFlags(8192);
    }

    public boolean isShortcutCachedForNotification() {
        return hasShortcutFlags(16384);
    }

    public boolean isImportant() {
        return hasConversationFlags(1);
    }

    public boolean isNotificationSilenced() {
        return hasConversationFlags(2);
    }

    public boolean isBubbled() {
        return hasConversationFlags(4);
    }

    public boolean isDemoted() {
        return hasConversationFlags(64);
    }

    public boolean isPersonImportant() {
        return hasConversationFlags(8);
    }

    public boolean isPersonBot() {
        return hasConversationFlags(16);
    }

    public boolean isContactStarred() {
        return hasConversationFlags(32);
    }

    public Collection<ConversationStatus> getStatuses() {
        return this.mCurrStatuses.values();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConversationInfo)) {
            return false;
        }
        ConversationInfo conversationInfo = (ConversationInfo) obj;
        return Objects.equals(this.mShortcutId, conversationInfo.mShortcutId) && Objects.equals(this.mLocusId, conversationInfo.mLocusId) && Objects.equals(this.mContactUri, conversationInfo.mContactUri) && Objects.equals(this.mContactPhoneNumber, conversationInfo.mContactPhoneNumber) && Objects.equals(this.mNotificationChannelId, conversationInfo.mNotificationChannelId) && Objects.equals(this.mParentNotificationChannelId, conversationInfo.mParentNotificationChannelId) && Objects.equals(Long.valueOf(this.mLastEventTimestamp), Long.valueOf(conversationInfo.mLastEventTimestamp)) && this.mCreationTimestamp == conversationInfo.mCreationTimestamp && this.mShortcutFlags == conversationInfo.mShortcutFlags && this.mConversationFlags == conversationInfo.mConversationFlags && Objects.equals(this.mCurrStatuses, conversationInfo.mCurrStatuses);
    }

    public int hashCode() {
        return Objects.hash(this.mShortcutId, this.mLocusId, this.mContactUri, this.mContactPhoneNumber, this.mNotificationChannelId, this.mParentNotificationChannelId, Long.valueOf(this.mLastEventTimestamp), Long.valueOf(this.mCreationTimestamp), Integer.valueOf(this.mShortcutFlags), Integer.valueOf(this.mConversationFlags), this.mCurrStatuses);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConversationInfo {");
        sb.append("shortcutId=");
        sb.append(this.mShortcutId);
        sb.append(", locusId=");
        sb.append(this.mLocusId);
        sb.append(", contactUri=");
        sb.append(this.mContactUri);
        sb.append(", phoneNumber=");
        sb.append(this.mContactPhoneNumber);
        sb.append(", notificationChannelId=");
        sb.append(this.mNotificationChannelId);
        sb.append(", parentNotificationChannelId=");
        sb.append(this.mParentNotificationChannelId);
        sb.append(", lastEventTimestamp=");
        sb.append(this.mLastEventTimestamp);
        sb.append(", creationTimestamp=");
        sb.append(this.mCreationTimestamp);
        sb.append(", statuses=");
        sb.append(this.mCurrStatuses);
        sb.append(", shortcutFlags=0x");
        sb.append(Integer.toHexString(this.mShortcutFlags));
        sb.append(" [");
        if (isShortcutLongLived()) {
            sb.append("Liv");
        }
        if (isShortcutCachedForNotification()) {
            sb.append("Cac");
        }
        sb.append("]");
        sb.append(", conversationFlags=0x");
        sb.append(Integer.toHexString(this.mConversationFlags));
        sb.append(" [");
        if (isImportant()) {
            sb.append("Imp");
        }
        if (isNotificationSilenced()) {
            sb.append("Sil");
        }
        if (isBubbled()) {
            sb.append("Bub");
        }
        if (isDemoted()) {
            sb.append("Dem");
        }
        if (isPersonImportant()) {
            sb.append("PIm");
        }
        if (isPersonBot()) {
            sb.append("Bot");
        }
        if (isContactStarred()) {
            sb.append("Sta");
        }
        sb.append("]}");
        return sb.toString();
    }

    private boolean hasShortcutFlags(int i) {
        return (this.mShortcutFlags & i) == i;
    }

    private boolean hasConversationFlags(int i) {
        return (this.mConversationFlags & i) == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeToProto(ProtoOutputStream protoOutputStream) {
        protoOutputStream.write(1138166333441L, this.mShortcutId);
        if (this.mLocusId != null) {
            long start = protoOutputStream.start(1146756268034L);
            protoOutputStream.write(1138166333441L, this.mLocusId.getId());
            protoOutputStream.end(start);
        }
        Uri uri = this.mContactUri;
        if (uri != null) {
            protoOutputStream.write(1138166333443L, uri.toString());
        }
        String str = this.mNotificationChannelId;
        if (str != null) {
            protoOutputStream.write(1138166333444L, str);
        }
        String str2 = this.mParentNotificationChannelId;
        if (str2 != null) {
            protoOutputStream.write(1138166333448L, str2);
        }
        protoOutputStream.write(1112396529673L, this.mLastEventTimestamp);
        protoOutputStream.write(1112396529674L, this.mCreationTimestamp);
        protoOutputStream.write(1120986464261L, this.mShortcutFlags);
        protoOutputStream.write(1120986464262L, this.mConversationFlags);
        String str3 = this.mContactPhoneNumber;
        if (str3 != null) {
            protoOutputStream.write(1138166333447L, str3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getBackupPayload() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF(this.mShortcutId);
            LocusId locusId = this.mLocusId;
            dataOutputStream.writeUTF(locusId != null ? locusId.getId() : "");
            Uri uri = this.mContactUri;
            dataOutputStream.writeUTF(uri != null ? uri.toString() : "");
            String str = this.mNotificationChannelId;
            if (str == null) {
                str = "";
            }
            dataOutputStream.writeUTF(str);
            dataOutputStream.writeInt(this.mShortcutFlags);
            dataOutputStream.writeInt(this.mConversationFlags);
            String str2 = this.mContactPhoneNumber;
            if (str2 == null) {
                str2 = "";
            }
            dataOutputStream.writeUTF(str2);
            String str3 = this.mParentNotificationChannelId;
            dataOutputStream.writeUTF(str3 != null ? str3 : "");
            dataOutputStream.writeLong(this.mLastEventTimestamp);
            dataOutputStream.writeInt(1);
            dataOutputStream.writeLong(this.mCreationTimestamp);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Slog.e(TAG, "Failed to write fields to backup payload.", e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ConversationInfo readFromProto(ProtoInputStream protoInputStream) throws IOException {
        Builder builder = new Builder();
        while (protoInputStream.nextField() != -1) {
            switch (protoInputStream.getFieldNumber()) {
                case 1:
                    builder.setShortcutId(protoInputStream.readString(1138166333441L));
                    break;
                case 2:
                    long start = protoInputStream.start(1146756268034L);
                    while (protoInputStream.nextField() != -1) {
                        if (protoInputStream.getFieldNumber() == 1) {
                            builder.setLocusId(new LocusId(protoInputStream.readString(1138166333441L)));
                        }
                    }
                    protoInputStream.end(start);
                    break;
                case 3:
                    builder.setContactUri(Uri.parse(protoInputStream.readString(1138166333443L)));
                    break;
                case 4:
                    builder.setNotificationChannelId(protoInputStream.readString(1138166333444L));
                    break;
                case 5:
                    builder.setShortcutFlags(protoInputStream.readInt(1120986464261L));
                    break;
                case 6:
                    builder.setConversationFlags(protoInputStream.readInt(1120986464262L));
                    break;
                case 7:
                    builder.setContactPhoneNumber(protoInputStream.readString(1138166333447L));
                    break;
                case 8:
                    builder.setParentNotificationChannelId(protoInputStream.readString(1138166333448L));
                    break;
                case 9:
                    builder.setLastEventTimestamp(protoInputStream.readLong(1112396529673L));
                    break;
                case 10:
                    builder.setCreationTimestamp(protoInputStream.readLong(1112396529674L));
                    break;
                default:
                    Slog.w(TAG, "Could not read undefined field: " + protoInputStream.getFieldNumber());
                    break;
            }
        }
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ConversationInfo readFromBackupPayload(byte[] bArr) {
        Builder builder = new Builder();
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        try {
            builder.setShortcutId(dataInputStream.readUTF());
            String readUTF = dataInputStream.readUTF();
            if (!TextUtils.isEmpty(readUTF)) {
                builder.setLocusId(new LocusId(readUTF));
            }
            String readUTF2 = dataInputStream.readUTF();
            if (!TextUtils.isEmpty(readUTF2)) {
                builder.setContactUri(Uri.parse(readUTF2));
            }
            String readUTF3 = dataInputStream.readUTF();
            if (!TextUtils.isEmpty(readUTF3)) {
                builder.setNotificationChannelId(readUTF3);
            }
            builder.setShortcutFlags(dataInputStream.readInt());
            builder.setConversationFlags(dataInputStream.readInt());
            String readUTF4 = dataInputStream.readUTF();
            if (!TextUtils.isEmpty(readUTF4)) {
                builder.setContactPhoneNumber(readUTF4);
            }
            String readUTF5 = dataInputStream.readUTF();
            if (!TextUtils.isEmpty(readUTF5)) {
                builder.setParentNotificationChannelId(readUTF5);
            }
            builder.setLastEventTimestamp(dataInputStream.readLong());
            if (maybeReadVersion(dataInputStream) == 1) {
                builder.setCreationTimestamp(dataInputStream.readLong());
            }
            return builder.build();
        } catch (IOException e) {
            Slog.e(TAG, "Failed to read conversation info fields from backup payload.", e);
            return null;
        }
    }

    private static int maybeReadVersion(DataInputStream dataInputStream) throws IOException {
        try {
            return dataInputStream.readInt();
        } catch (EOFException unused) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Builder {
        private String mContactPhoneNumber;
        private Uri mContactUri;
        private int mConversationFlags;
        private long mCreationTimestamp;
        private Map<String, ConversationStatus> mCurrStatuses;
        private long mLastEventTimestamp;
        private LocusId mLocusId;
        private String mNotificationChannelId;
        private String mParentNotificationChannelId;
        private int mShortcutFlags;
        private String mShortcutId;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder() {
            this.mCurrStatuses = new HashMap();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(ConversationInfo conversationInfo) {
            this.mCurrStatuses = new HashMap();
            String str = this.mShortcutId;
            if (str == null) {
                this.mShortcutId = conversationInfo.mShortcutId;
            } else {
                Preconditions.checkArgument(str.equals(conversationInfo.mShortcutId));
            }
            this.mLocusId = conversationInfo.mLocusId;
            this.mContactUri = conversationInfo.mContactUri;
            this.mContactPhoneNumber = conversationInfo.mContactPhoneNumber;
            this.mNotificationChannelId = conversationInfo.mNotificationChannelId;
            this.mParentNotificationChannelId = conversationInfo.mParentNotificationChannelId;
            this.mLastEventTimestamp = conversationInfo.mLastEventTimestamp;
            this.mCreationTimestamp = conversationInfo.mCreationTimestamp;
            this.mShortcutFlags = conversationInfo.mShortcutFlags;
            this.mConversationFlags = conversationInfo.mConversationFlags;
            this.mCurrStatuses = conversationInfo.mCurrStatuses;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setShortcutId(String str) {
            this.mShortcutId = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setLocusId(LocusId locusId) {
            this.mLocusId = locusId;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setContactUri(Uri uri) {
            this.mContactUri = uri;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setContactPhoneNumber(String str) {
            this.mContactPhoneNumber = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setNotificationChannelId(String str) {
            this.mNotificationChannelId = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setParentNotificationChannelId(String str) {
            this.mParentNotificationChannelId = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setLastEventTimestamp(long j) {
            this.mLastEventTimestamp = j;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setCreationTimestamp(long j) {
            this.mCreationTimestamp = j;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setShortcutFlags(int i) {
            this.mShortcutFlags = i;
            return this;
        }

        Builder setConversationFlags(int i) {
            this.mConversationFlags = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setImportant(boolean z) {
            return setConversationFlag(1, z);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setNotificationSilenced(boolean z) {
            return setConversationFlag(2, z);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setBubbled(boolean z) {
            return setConversationFlag(4, z);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setDemoted(boolean z) {
            return setConversationFlag(64, z);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setPersonImportant(boolean z) {
            return setConversationFlag(8, z);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setPersonBot(boolean z) {
            return setConversationFlag(16, z);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setContactStarred(boolean z) {
            return setConversationFlag(32, z);
        }

        private Builder setConversationFlag(int i, boolean z) {
            if (z) {
                return addConversationFlags(i);
            }
            return removeConversationFlags(i);
        }

        private Builder addConversationFlags(int i) {
            this.mConversationFlags = i | this.mConversationFlags;
            return this;
        }

        private Builder removeConversationFlags(int i) {
            this.mConversationFlags = (~i) & this.mConversationFlags;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setStatuses(List<ConversationStatus> list) {
            this.mCurrStatuses.clear();
            if (list != null) {
                for (ConversationStatus conversationStatus : list) {
                    this.mCurrStatuses.put(conversationStatus.getId(), conversationStatus);
                }
            }
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder addOrUpdateStatus(ConversationStatus conversationStatus) {
            this.mCurrStatuses.put(conversationStatus.getId(), conversationStatus);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder clearStatus(String str) {
            this.mCurrStatuses.remove(str);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ConversationInfo build() {
            Objects.requireNonNull(this.mShortcutId);
            return new ConversationInfo(this);
        }
    }
}
