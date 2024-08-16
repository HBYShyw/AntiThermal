package android.app.servertransaction;

import android.app.ActivityThread;
import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.OplusColorModeManager;

/* loaded from: classes.dex */
public class ColorModeChangeItem extends ClientTransactionItem {
    public static final Parcelable.Creator<ColorModeChangeItem> CREATOR = new Parcelable.Creator<ColorModeChangeItem>() { // from class: android.app.servertransaction.ColorModeChangeItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ColorModeChangeItem createFromParcel(Parcel in) {
            return new ColorModeChangeItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ColorModeChangeItem[] newArray(int size) {
            return new ColorModeChangeItem[size];
        }
    };
    private int mColorMode;

    public void preExecute(ClientTransactionHandler client, IBinder token) {
        if (ActivityThread.isSystem()) {
            return;
        }
        OplusColorModeManager.getInstance().setColorMode(this.mColorMode);
        Log.d("ColorModeChangeItem", "preExecute mColorMode=" + this.mColorMode + ",token=" + token);
    }

    public void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
    }

    private ColorModeChangeItem() {
        this.mColorMode = -1;
    }

    public static ColorModeChangeItem obtain(int colorMode) {
        ColorModeChangeItem instance = ObjectPool.obtain(ColorModeChangeItem.class);
        if (instance == null) {
            instance = new ColorModeChangeItem();
        }
        instance.mColorMode = colorMode;
        return instance;
    }

    public void recycle() {
        this.mColorMode = -1;
        ObjectPool.recycle(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mColorMode);
    }

    private ColorModeChangeItem(Parcel in) {
        this.mColorMode = -1;
        this.mColorMode = in.readInt();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ColorModeChangeItem other = (ColorModeChangeItem) o;
        if (this.mColorMode == other.mColorMode) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (17 * 31) + this.mColorMode;
        return result;
    }

    public String toString() {
        return "ColorModeChangeItem{mode=" + this.mColorMode + "}";
    }
}
