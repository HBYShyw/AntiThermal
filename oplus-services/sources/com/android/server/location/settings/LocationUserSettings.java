package com.android.server.location.settings;

import android.R;
import android.content.res.Resources;
import com.android.server.location.settings.SettingsStore;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class LocationUserSettings implements SettingsStore.VersionedSettings {
    private static final int VERSION = 1;
    private final boolean mAdasGnssLocationEnabled;

    @Override // com.android.server.location.settings.SettingsStore.VersionedSettings
    public int getVersion() {
        return 1;
    }

    private LocationUserSettings(boolean z) {
        this.mAdasGnssLocationEnabled = z;
    }

    public boolean isAdasGnssLocationEnabled() {
        return this.mAdasGnssLocationEnabled;
    }

    public LocationUserSettings withAdasGnssLocationEnabled(boolean z) {
        return z == this.mAdasGnssLocationEnabled ? this : new LocationUserSettings(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(this.mAdasGnssLocationEnabled);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LocationUserSettings read(Resources resources, int i, DataInput dataInput) throws IOException {
        boolean readBoolean;
        if (i != 1) {
            readBoolean = resources.getBoolean(R.bool.config_zramWriteback);
        } else {
            readBoolean = dataInput.readBoolean();
        }
        return new LocationUserSettings(readBoolean);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof LocationUserSettings) && this.mAdasGnssLocationEnabled == ((LocationUserSettings) obj).mAdasGnssLocationEnabled;
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.mAdasGnssLocationEnabled));
    }
}
