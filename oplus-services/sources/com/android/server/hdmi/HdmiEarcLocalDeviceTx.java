package com.android.server.hdmi;

import android.media.AudioDescriptor;
import android.os.Handler;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import com.android.server.hdmi.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HdmiEarcLocalDeviceTx extends HdmiEarcLocalDevice {
    private static final int EARC_CAPS_DATA_START = 3;
    private static final int EARC_CAPS_LENGTH_MASK = 31;
    private static final int EARC_CAPS_PAYLOAD_LENGTH = 2;
    private static final int EARC_CAPS_TAGCODE_MASK = 224;
    private static final int EARC_CAPS_TAGCODE_SHIFT = 5;
    private static final int EXTENDED_TAGCODE_VSADB = 17;
    static final long REPORT_CAPS_MAX_DELAY_MS = 2000;
    private static final String TAG = "HdmiEarcLocalDeviceTx";
    private static final int TAGCODE_AUDIO_DATA_BLOCK = 1;
    private static final int TAGCODE_SADB_DATA_BLOCK = 4;
    private static final int TAGCODE_USE_EXTENDED_TAG = 7;
    private static final String[] earcStatusNames = {"HDMI_EARC_STATUS_IDLE", "HDMI_EARC_STATUS_EARC_PENDING", "HDMI_EARC_STATUS_ARC_PENDING", "HDMI_EARC_STATUS_EARC_CONNECTED"};
    private Handler mReportCapsHandler;
    private ReportCapsRunnable mReportCapsRunnable;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiEarcLocalDeviceTx(HdmiControlService hdmiControlService) {
        super(hdmiControlService, 0);
        synchronized (this.mLock) {
            this.mEarcStatus = 1;
        }
        this.mReportCapsHandler = new Handler(hdmiControlService.getServiceLooper());
        this.mReportCapsRunnable = new ReportCapsRunnable();
    }

    private String earcStatusToString(int i) {
        return earcStatusNames[i];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.hdmi.HdmiEarcLocalDevice
    public void handleEarcStateChange(@Constants.EarcStatus int i) {
        int i2;
        synchronized (this.mLock) {
            HdmiLogger.debug("eARC state change [old: %s(%d) new: %s(%d)]", earcStatusToString(this.mEarcStatus), Integer.valueOf(this.mEarcStatus), earcStatusToString(i), Integer.valueOf(i));
            i2 = this.mEarcStatus;
            this.mEarcStatus = i;
        }
        this.mReportCapsHandler.removeCallbacksAndMessages(null);
        if (i == 0) {
            this.mService.notifyEarcStatusToAudioService(false, new ArrayList());
            this.mService.startArcAction(false, null);
            return;
        }
        if (i == 2) {
            this.mService.notifyEarcStatusToAudioService(false, new ArrayList());
            this.mService.startArcAction(true, null);
        } else if (i == 1 && i2 == 2) {
            this.mService.startArcAction(false, null);
        } else if (i == 3) {
            if (i2 == 2) {
                this.mService.startArcAction(false, null);
            }
            this.mReportCapsHandler.postDelayed(this.mReportCapsRunnable, REPORT_CAPS_MAX_DELAY_MS);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.hdmi.HdmiEarcLocalDevice
    public void handleEarcCapabilitiesReported(byte[] bArr) {
        synchronized (this.mLock) {
            if (this.mEarcStatus == 3 && this.mReportCapsHandler.hasCallbacks(this.mReportCapsRunnable)) {
                this.mReportCapsHandler.removeCallbacksAndMessages(null);
                this.mService.notifyEarcStatusToAudioService(true, parseCapabilities(bArr));
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class ReportCapsRunnable implements Runnable {
        private ReportCapsRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (HdmiEarcLocalDeviceTx.this.mLock) {
                HdmiEarcLocalDeviceTx hdmiEarcLocalDeviceTx = HdmiEarcLocalDeviceTx.this;
                if (hdmiEarcLocalDeviceTx.mEarcStatus == 3) {
                    hdmiEarcLocalDeviceTx.mService.notifyEarcStatusToAudioService(true, new ArrayList());
                }
            }
        }
    }

    private List<AudioDescriptor> parseCapabilities(byte[] bArr) {
        ArrayList arrayList = new ArrayList();
        if (bArr.length < 4) {
            Slog.i(TAG, "Raw eARC capabilities array doesn´t contain any blocks.");
            return arrayList;
        }
        int i = bArr[2];
        if (bArr.length < i) {
            Slog.i(TAG, "Raw eARC capabilities array is shorter than the reported payload length.");
            return arrayList;
        }
        int i2 = 3;
        while (i2 < i) {
            int i3 = bArr[i2];
            int i4 = (i3 & 224) >> 5;
            int i5 = i3 & 31;
            if (i5 == 0) {
                break;
            }
            if (i4 == 1) {
                int i6 = i5 % 3;
                if (i6 != 0) {
                    Slog.e(TAG, "Invalid length of SAD block: expected a factor of 3 but got " + i6);
                } else {
                    byte[] bArr2 = new byte[i5];
                    System.arraycopy(bArr, i2 + 1, bArr2, 0, i5);
                    int i7 = 0;
                    while (i7 < i5) {
                        int i8 = i7 + 3;
                        arrayList.add(new AudioDescriptor(1, 0, Arrays.copyOfRange(bArr2, i7, i8)));
                        i7 = i8;
                    }
                }
            } else if (i4 == 4) {
                int i9 = i5 + 1;
                byte[] bArr3 = new byte[i9];
                System.arraycopy(bArr, i2, bArr3, 0, i9);
                arrayList.add(new AudioDescriptor(2, 0, bArr3));
            } else if (i4 == 7) {
                if (bArr[i2 + 1] == 17) {
                    int i10 = i5 + 1;
                    byte[] bArr4 = new byte[i10];
                    System.arraycopy(bArr, i2, bArr4, 0, i10);
                    arrayList.add(new AudioDescriptor(3, 0, bArr4));
                }
            } else {
                Slog.w(TAG, "This tagcode was not handled: " + i4);
            }
            i2 += i5 + 1;
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.hdmi.HdmiEarcLocalDevice
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        synchronized (this.mLock) {
            indentingPrintWriter.println("TX, mEarcStatus: " + this.mEarcStatus);
        }
    }
}
