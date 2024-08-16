package android.app.job;

import android.os.Parcel;
import android.util.Log;

/* loaded from: classes.dex */
public class JobParametersExtImpl implements IJobParametersExt {
    private int cpuLevel;
    private String oplusExtraStr;

    public JobParametersExtImpl(Object base) {
    }

    public void init(Parcel in) {
    }

    public void setIntValue(String type, int value) {
        char c;
        switch (type.hashCode()) {
            case -1821994690:
                if (type.equals("setCpuLevel")) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                this.cpuLevel = value;
                return;
            default:
                return;
        }
    }

    public void setStringValue(String type, String value) {
        char c;
        switch (type.hashCode()) {
            case -1047845784:
                if (type.equals("setOplusExtraStr")) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                this.oplusExtraStr = value;
                return;
            default:
                return;
        }
    }

    public int getIntValue(String type, int defValue) {
        char c;
        switch (type.hashCode()) {
            case -1733414862:
                if (type.equals("getCpuLevel")) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                int result = this.cpuLevel;
                return result;
            default:
                return defValue;
        }
    }

    public String getStringValue(String type, String defValue) {
        char c;
        switch (type.hashCode()) {
            case 783597044:
                if (type.equals("getOplusExtraStr")) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                String result = this.oplusExtraStr;
                return result;
            default:
                return defValue;
        }
    }

    public void initJobParameters(Object obj) {
        if (!(obj instanceof Parcel)) {
            Log.d("JobParametersExt", " init job parameters without parcel, error");
            return;
        }
        Parcel in = (Parcel) obj;
        this.cpuLevel = in.readInt();
        this.oplusExtraStr = in.readString();
    }

    public void writeToParcelJobParameters(Object obj) {
        if (!(obj instanceof Parcel)) {
            Log.d("JobParametersExt", " write job parameters without parcel, error");
            return;
        }
        Parcel dest = (Parcel) obj;
        dest.writeInt(this.cpuLevel);
        dest.writeString(this.oplusExtraStr);
    }
}
