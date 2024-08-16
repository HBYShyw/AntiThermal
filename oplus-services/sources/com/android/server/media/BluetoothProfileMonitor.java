package com.android.server.media;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHearingAid;
import android.bluetooth.BluetoothLeAudio;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class BluetoothProfileMonitor {
    static final long GROUP_ID_NO_GROUP = -1;
    private BluetoothA2dp mA2dpProfile;
    private final BluetoothAdapter mBluetoothAdapter;
    private final Context mContext;
    private BluetoothHearingAid mHearingAidProfile;
    private BluetoothLeAudio mLeAudioProfile;
    private OnProfileChangedListener mOnProfileChangedListener;
    private final ProfileListener mProfileListener = new ProfileListener();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    interface OnProfileChangedListener {
        void onProfileChange(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothProfileMonitor(Context context, BluetoothAdapter bluetoothAdapter) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(bluetoothAdapter);
        this.mContext = context;
        this.mBluetoothAdapter = bluetoothAdapter;
    }

    synchronized void setOnProfileChangedListener(OnProfileChangedListener onProfileChangedListener) {
        this.mOnProfileChangedListener = onProfileChangedListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 2);
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 21);
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 22);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isProfileSupported(int i, BluetoothDevice bluetoothDevice) {
        BluetoothProfile bluetoothProfile;
        synchronized (this) {
            if (i == 2) {
                bluetoothProfile = this.mA2dpProfile;
            } else if (i == 21) {
                bluetoothProfile = this.mHearingAidProfile;
            } else if (i == 22) {
                bluetoothProfile = this.mLeAudioProfile;
            } else {
                throw new IllegalArgumentException(i + " is not supported as Bluetooth profile");
            }
        }
        if (bluetoothProfile == null) {
            return false;
        }
        return bluetoothProfile.getConnectedDevices().contains(bluetoothDevice);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getGroupId(int i, BluetoothDevice bluetoothDevice) {
        synchronized (this) {
            long j = -1;
            if (i == 2) {
                return -1L;
            }
            if (i == 21) {
                BluetoothHearingAid bluetoothHearingAid = this.mHearingAidProfile;
                if (bluetoothHearingAid != null) {
                    j = bluetoothHearingAid.getHiSyncId(bluetoothDevice);
                }
                return j;
            }
            if (i == 22) {
                BluetoothLeAudio bluetoothLeAudio = this.mLeAudioProfile;
                if (bluetoothLeAudio != null) {
                    j = bluetoothLeAudio.getGroupId(bluetoothDevice);
                }
                return j;
            }
            throw new IllegalArgumentException(i + " is not supported as Bluetooth profile");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class ProfileListener implements BluetoothProfile.ServiceListener {
        private ProfileListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            synchronized (BluetoothProfileMonitor.this) {
                if (i == 2) {
                    BluetoothProfileMonitor.this.mA2dpProfile = (BluetoothA2dp) bluetoothProfile;
                } else if (i == 21) {
                    BluetoothProfileMonitor.this.mHearingAidProfile = (BluetoothHearingAid) bluetoothProfile;
                } else {
                    if (i != 22) {
                        return;
                    }
                    BluetoothProfileMonitor.this.mLeAudioProfile = (BluetoothLeAudio) bluetoothProfile;
                }
                OnProfileChangedListener onProfileChangedListener = BluetoothProfileMonitor.this.mOnProfileChangedListener;
                if (onProfileChangedListener != null) {
                    onProfileChangedListener.onProfileChange(i);
                }
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            synchronized (BluetoothProfileMonitor.this) {
                if (i == 2) {
                    BluetoothProfileMonitor.this.mA2dpProfile = null;
                } else if (i == 21) {
                    BluetoothProfileMonitor.this.mHearingAidProfile = null;
                } else if (i != 22) {
                    return;
                } else {
                    BluetoothProfileMonitor.this.mLeAudioProfile = null;
                }
                OnProfileChangedListener onProfileChangedListener = BluetoothProfileMonitor.this.mOnProfileChangedListener;
                if (onProfileChangedListener != null) {
                    onProfileChangedListener.onProfileChange(i);
                }
            }
        }
    }
}
