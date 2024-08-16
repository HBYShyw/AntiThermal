package com.oplus.direct;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.direct.IOplusDirectFindCallback;
import com.oplus.util.OplusLog;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class OplusDirectFindCmd implements Parcelable {
    private static final String CMD_TYPE = "cmd_type";
    public static final Parcelable.Creator<OplusDirectFindCmd> CREATOR = new Parcelable.Creator<OplusDirectFindCmd>() { // from class: com.oplus.direct.OplusDirectFindCmd.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusDirectFindCmd createFromParcel(Parcel in) {
            return new OplusDirectFindCmd(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusDirectFindCmd[] newArray(int size) {
            return new OplusDirectFindCmd[size];
        }
    };
    public static final String EXTRA_CMD = "direct_find_cmd";
    public static final String EXTRA_ID_NAMES = "id_names";
    private static final String OPLUS_DIRECT_FIND_PARAM = "mOplusDirectFindParam";
    private static final String PACKAGE_NAME = "package_name";
    private static final String RULE_PARAM = "rule_param";
    private static final String TAG = "OplusDirectFindCmd";
    private final Bundle mBundle;
    private IOplusDirectFindCallback mCallback;
    private String mOplusDirectFindParam;

    public OplusDirectFindCmd() {
        this.mBundle = new Bundle();
        this.mCallback = null;
        this.mOplusDirectFindParam = "";
    }

    public OplusDirectFindCmd(Parcel in) {
        this.mBundle = new Bundle();
        this.mCallback = null;
        this.mOplusDirectFindParam = "";
        readFromParcel(in);
    }

    public OplusDirectFindCmd(IOplusDirectFindCallback callback) {
        this(callback, "");
    }

    public OplusDirectFindCmd(IOplusDirectFindCallback callback, String param) {
        this.mBundle = new Bundle();
        this.mCallback = null;
        this.mOplusDirectFindParam = "";
        setOplusDirectFindParam(param);
        putCommand(getCmdTypeFromJSON(param).name());
        setCallback(callback);
    }

    public String toString() {
        return "Cmd=" + this.mBundle;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        this.mBundle.writeToParcel(out, flags);
        if (this.mCallback != null) {
            out.writeInt(1);
            out.writeStrongBinder(this.mCallback.asBinder());
        } else {
            out.writeInt(0);
        }
        if (!TextUtils.isEmpty(this.mOplusDirectFindParam)) {
            out.writeInt(1);
            out.writeString(this.mOplusDirectFindParam);
        } else {
            out.writeInt(0);
        }
    }

    public void readFromParcel(Parcel in) {
        this.mBundle.readFromParcel(in);
        if (in.readInt() == 1) {
            this.mCallback = IOplusDirectFindCallback.Stub.asInterface(in.readStrongBinder());
        }
        if (in.readInt() == 1) {
            this.mOplusDirectFindParam = in.readString();
        }
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public void putCommand(String cmd) {
        this.mBundle.putString(EXTRA_CMD, cmd);
    }

    public OplusDirectFindCmds getCommand() {
        try {
            try {
                OplusDirectFindCmds cmd = OplusDirectFindCmds.valueOf(this.mBundle.getString(EXTRA_CMD));
                if (cmd != null) {
                    return cmd;
                }
            } catch (Exception e) {
                OplusLog.e(TAG, "getCommand ERROR : " + Log.getStackTraceString(e));
                if (0 != 0) {
                    return null;
                }
            }
            OplusDirectFindCmds cmd2 = OplusDirectFindCmds.UNKNOWN;
            return cmd2;
        } catch (Throwable th) {
            if (0 == 0) {
                OplusDirectFindCmds cmd3 = OplusDirectFindCmds.UNKNOWN;
            }
            throw th;
        }
    }

    public void setCallback(IOplusDirectFindCallback callback) {
        this.mCallback = callback;
    }

    public IOplusDirectFindCallback getCallback() {
        return this.mCallback;
    }

    public void setOplusDirectFindParam(String findParam) {
        this.mOplusDirectFindParam = findParam;
    }

    public String getOplusDirectFindParam() {
        return this.mOplusDirectFindParam;
    }

    public String getPackageName() {
        if (TextUtils.isEmpty(this.mOplusDirectFindParam)) {
            return "";
        }
        try {
            JSONObject paramObj = new JSONObject(this.mOplusDirectFindParam);
            return paramObj.getString("package_name");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static OplusDirectFindCmds getCmdTypeFromJSON(String json) {
        if (TextUtils.isEmpty(json)) {
            return OplusDirectFindCmds.FIND_FAVORITE;
        }
        try {
            JSONObject paramObj = new JSONObject(json);
            String type = paramObj.getString(CMD_TYPE);
            return OplusDirectFindCmds.valueOf(type);
        } catch (JSONException e) {
            e.printStackTrace();
            return OplusDirectFindCmds.UNKNOWN;
        }
    }

    public static String getRuleParamFromJSON(String json) {
        try {
            JSONObject paramObj = new JSONObject(json);
            return paramObj.getString(RULE_PARAM);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
