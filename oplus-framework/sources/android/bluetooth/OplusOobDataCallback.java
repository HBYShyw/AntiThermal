package android.bluetooth;

/* loaded from: classes.dex */
public interface OplusOobDataCallback {
    void onError(int i);

    void onOobData(int i, OplusBluetoothOobData oplusBluetoothOobData);
}
