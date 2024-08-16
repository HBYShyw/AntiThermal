package com.oplus.putt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes.dex */
public class PuttParams implements Parcelable {
    public static final int ACTION_ENTER_ONEPUTT_GESTURE = 11000;
    public static final int ACTION_ENTER_RECENTS = 11001;
    public static final int ACTION_ENTER_START_ACTIVITY = 21000;
    public static final int ACTION_EXIT_APP_CRASH = 12005;
    public static final int ACTION_EXIT_BY_REMOVE_TASK = 12000;
    public static final int ACTION_EXIT_BY_VICE_DISPLAY_HANDLE = 12001;
    public static final int ACTION_EXIT_ONEPUTT_BY_DEFAULT_DISPLAY_HANDLE = 12101;
    public static final int ACTION_EXIT_RESTART_BY_LAUNCH = 12002;
    public static final int ACTION_EXIT_RESTART_BY_SPLITSCREEN = 12004;
    public static final int ACTION_EXIT_RESTART_BY_ZOOM = 12003;
    public static final int ANIMATION_EXIT_RETURN_BACKGROUND_NO_ANIMATION = 14000;
    public static final int ANIMATION_EXIT_RETURN_FOREGROUND_ANIMATION = 14002;
    public static final int ANIMATION_EXIT_RETURN_FOREGROUND_NO_ANIMATION = 14001;
    public static final Parcelable.Creator<PuttParams> CREATOR = new Parcelable.Creator<PuttParams>() { // from class: com.oplus.putt.PuttParams.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PuttParams createFromParcel(Parcel source) {
            return new PuttParams(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PuttParams[] newArray(int size) {
            return new PuttParams[size];
        }
    };
    public static final String PUTT_HASH = "Putt";
    private static final String PUTT_PACKAGE = "com.oplus.secondaryhome";
    private static final String PUTT_SERVICE = "com.oplus.putt.PuttCardService";
    public static final String TRANSIENT_STATE_HASH = "TransientState";
    private static final String TRANSIENT_STATE_PACKAGE = "com.oplus.secondaryhome";
    private static final String TRANSIENT_STATE_SERVICE = "com.oplus.putt.PuttCardService";
    public static final int TYPE_ONE_PYTT = 1;
    public static final int TYPE_PUTT = 3;
    public static final int TYPE_TRANSIENT_STATE = 2;
    public String clientPackage;
    public String clientService;
    public int enterAction;
    public int exitAction;
    public int exitAnimation;
    public Bundle extension;
    public Intent intent;
    public String puttHash;
    public int taskId;
    public int type;

    public PuttParams() {
    }

    public PuttParams(Parcel in) {
        this.type = in.readInt();
        this.puttHash = in.readString();
        this.enterAction = in.readInt();
        this.exitAction = in.readInt();
        this.exitAnimation = in.readInt();
        this.clientPackage = in.readString();
        this.clientService = in.readString();
        this.taskId = in.readInt();
        if (in.readInt() == 1) {
            this.intent = (Intent) Intent.CREATOR.createFromParcel(in);
        }
        this.extension = in.readBundle();
    }

    public PuttParams(PuttParams in) {
        if (in != null) {
            this.type = in.type;
            this.puttHash = in.puttHash;
            this.enterAction = in.enterAction;
            this.exitAction = in.exitAction;
            this.exitAnimation = in.exitAnimation;
            this.clientPackage = in.clientPackage;
            this.clientService = in.clientService;
            this.taskId = in.taskId;
            this.intent = in.intent;
            this.extension = in.extension;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static PuttParams createPuttParams(String puttHash, int enterAction, String clientPackage, String clientService, Intent intent, Bundle bundle) {
        PuttParams params = new PuttParams();
        params.puttHash = puttHash;
        params.type = 3;
        params.enterAction = enterAction;
        params.clientPackage = clientPackage;
        params.clientService = clientService;
        params.intent = intent;
        params.extension = bundle;
        return params;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static PuttParams createOnePuttParams(int onePuttTaskId, int enterAction, Bundle bundle) {
        PuttParams params = new PuttParams();
        params.puttHash = PUTT_HASH;
        params.type = 1;
        params.enterAction = enterAction;
        params.clientPackage = "com.oplus.secondaryhome";
        params.clientService = "com.oplus.putt.PuttCardService";
        params.taskId = onePuttTaskId;
        params.extension = bundle;
        return params;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static PuttParams createTransientStateParams(int enterAction, Intent intent, Bundle bundle) {
        PuttParams params = new PuttParams();
        params.puttHash = TRANSIENT_STATE_HASH;
        params.type = 2;
        params.enterAction = enterAction;
        params.clientPackage = "com.oplus.secondaryhome";
        params.clientService = "com.oplus.putt.PuttCardService";
        params.intent = intent;
        params.extension = bundle;
        return params;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PuttParams that = (PuttParams) object;
        if (this.type == that.type && Objects.equals(this.puttHash, that.puttHash)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.type), this.puttHash);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.puttHash);
        dest.writeInt(this.enterAction);
        dest.writeInt(this.exitAction);
        dest.writeInt(this.exitAnimation);
        dest.writeString(this.clientPackage);
        dest.writeString(this.clientService);
        dest.writeInt(this.taskId);
        if (this.intent == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            this.intent.writeToParcel(dest, flags);
        }
        dest.writeBundle(this.extension);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PuttParams = { ");
        sb.append(" type = " + this.type);
        sb.append(" puttHash = " + this.puttHash);
        sb.append(" enterAction = " + this.enterAction);
        sb.append(" exitAction = " + this.exitAction);
        sb.append(" exitAnimation = " + this.exitAnimation);
        sb.append(" clientPackage = " + this.clientPackage);
        sb.append(" clientService = " + this.clientService);
        sb.append(" taskId = " + this.taskId);
        sb.append(" Intent = " + this.intent);
        sb.append(" extension = " + this.extension);
        sb.append("}");
        return sb.toString();
    }
}
