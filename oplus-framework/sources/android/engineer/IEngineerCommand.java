package android.engineer;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class IEngineerCommand implements Parcelable {
    public static final Parcelable.Creator<IEngineerCommand> CREATOR = new Parcelable.Creator<IEngineerCommand>() { // from class: android.engineer.IEngineerCommand.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IEngineerCommand createFromParcel(Parcel in) {
            return new IEngineerCommand(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IEngineerCommand[] newArray(int size) {
            return new IEngineerCommand[size];
        }
    };
    private String[] mCommand;

    public String[] getCommand() {
        return this.mCommand;
    }

    public void setCommand(String[] command) {
        this.mCommand = command;
    }

    public IEngineerCommand(String[] command) {
        this.mCommand = command;
    }

    protected IEngineerCommand(Parcel in) {
        this.mCommand = in.readStringArray();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(this.mCommand);
    }
}
