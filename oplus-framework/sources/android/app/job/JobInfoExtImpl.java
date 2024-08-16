package android.app.job;

import android.app.job.IJobInfoExt;
import android.os.Parcel;
import android.util.Log;

/* loaded from: classes.dex */
public class JobInfoExtImpl implements IJobInfoExt {
    private boolean hasCpuConstraint;
    private boolean hasProtectSceneConstraint;
    private boolean hasTemperatureConstraint;
    private boolean isNotSysApp = false;
    private boolean isOplusJob;
    public boolean isOplusJobBak;
    private boolean mIsFastIdle;
    private String oplusExtraStr;
    private int protectForeType;
    private int protectScene;
    private boolean requireBattIdle;
    private boolean requireProtectFore;

    public JobInfoExtImpl(Object base) {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public boolean getBooleanConstraint(String type, boolean defValue) {
        char c;
        switch (type.hashCode()) {
            case -1826954563:
                if (type.equals("isNotSysApp")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1090153538:
                if (type.equals("isOplusJob")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -859814924:
                if (type.equals("hasProtectSceneConstraint")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -444640264:
                if (type.equals("requireBattIdle")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 175023514:
                if (type.equals("isFastIdle")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 473341318:
                if (type.equals("requireProtectFore")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1161148407:
                if (type.equals("hasTemperatureConstraint")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1745398059:
                if (type.equals("hasCpuConstraint")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                boolean result = this.hasTemperatureConstraint;
                return result;
            case 1:
                boolean result2 = this.hasProtectSceneConstraint;
                return result2;
            case 2:
                boolean result3 = this.requireProtectFore;
                return result3;
            case 3:
                boolean result4 = this.hasCpuConstraint;
                return result4;
            case 4:
                boolean result5 = this.isOplusJob;
                return result5;
            case 5:
                boolean result6 = this.requireBattIdle;
                return result6;
            case 6:
                boolean result7 = !this.isNotSysApp;
                return result7;
            case 7:
                boolean result8 = this.mIsFastIdle;
                return result8;
            default:
                return defValue;
        }
    }

    public String getStringConstraint(String type, String defValue) {
        char c;
        switch (type.hashCode()) {
            case 1901396394:
                if (type.equals("oplusExtraStr")) {
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public int getIntConstraint(String type, int defValue) {
        char c;
        switch (type.hashCode()) {
            case 33307389:
                if (type.equals("protectScene")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1364872709:
                if (type.equals("protectForeType")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                int result = this.protectScene;
                return result;
            case 1:
                int result2 = this.protectForeType;
                return result2;
            default:
                return defValue;
        }
    }

    public long getLongConstraint(String type, long defValue) {
        return defValue;
    }

    public void setBooleanConstraint(String type, boolean value) {
        char c;
        switch (type.hashCode()) {
            case 594021142:
                if (type.equals("setSysApp")) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                this.isNotSysApp = !value;
                return;
            default:
                return;
        }
    }

    public void setStringConstraint(String type, String value) {
    }

    public void setIntConstraint(String type, int value) {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void setLongConstraint(JobInfo job, String type, long value) {
        char c;
        switch (type.hashCode()) {
            case -2135450573:
                if (type.equals("setMaxExecutionDelayMillis")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1755199597:
                if (type.equals("setIntervalMillis")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                job.maxExecutionDelayMillis = value;
                return;
            case 1:
                job.intervalMillis = value;
                return;
            default:
                return;
        }
    }

    public void initJobInfo(Object obj) {
        if (!(obj instanceof Parcel)) {
            Log.d("JobInfoExt", " init jobinfo without parcel, error");
            return;
        }
        Parcel in = (Parcel) obj;
        this.requireBattIdle = in.readInt() == 1;
        this.isOplusJob = in.readInt() == 1;
        this.requireProtectFore = in.readInt() == 1;
        this.hasCpuConstraint = in.readInt() == 1;
        this.oplusExtraStr = in.readString();
        this.protectForeType = in.readInt();
        this.isNotSysApp = in.readInt() == 1;
        this.hasTemperatureConstraint = in.readInt() == 1;
        this.hasProtectSceneConstraint = in.readInt() == 1;
        this.protectScene = in.readInt();
        this.mIsFastIdle = in.readInt() == 1;
    }

    public void initJobInfoPure(IJobInfoExt.JobBuilderExt jobBuilderExt) {
        if (jobBuilderExt == null) {
            return;
        }
        this.requireBattIdle = jobBuilderExt.mRequiresBattIdle;
        this.isOplusJob = jobBuilderExt.mIsOplusJob;
        this.requireProtectFore = jobBuilderExt.mRequiresProtectFore;
        this.hasCpuConstraint = jobBuilderExt.mHasCpuConstraint;
        this.oplusExtraStr = jobBuilderExt.mOplusExtraStr;
        this.protectForeType = jobBuilderExt.mProtectForeType;
        this.hasTemperatureConstraint = jobBuilderExt.mHasTemperatureConstraint;
        this.hasProtectSceneConstraint = jobBuilderExt.mHasProtectSceneConstraint;
        this.protectScene = jobBuilderExt.mProtectScene;
        this.mIsFastIdle = jobBuilderExt.mIsFastIdle;
    }

    public void writeToParcelJobInfo(Object obj, int i) {
        if (!(obj instanceof Parcel)) {
            Log.d("JobInfoExt", " write jobinfo without parcel, error");
            return;
        }
        Parcel parcel = (Parcel) obj;
        parcel.writeInt(this.requireBattIdle ? 1 : 0);
        parcel.writeInt(this.isOplusJob ? 1 : 0);
        parcel.writeInt(this.requireProtectFore ? 1 : 0);
        parcel.writeInt(this.hasCpuConstraint ? 1 : 0);
        parcel.writeString(this.oplusExtraStr);
        parcel.writeInt(this.protectForeType);
        parcel.writeInt(this.isNotSysApp ? 1 : 0);
        parcel.writeInt(this.hasTemperatureConstraint ? 1 : 0);
        parcel.writeInt(this.hasProtectSceneConstraint ? 1 : 0);
        parcel.writeInt(this.protectScene);
        parcel.writeInt(this.mIsFastIdle ? 1 : 0);
    }
}
