package com.oplus.quicksettings;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import com.oplus.quicksettings.IOplusTileService;

/* loaded from: classes.dex */
public class OplusBaseTileService extends IOplusTileService.Stub {
    @Override // com.oplus.quicksettings.IOplusTileService
    public Tile getOplusTile(ComponentName name) {
        return null;
    }

    @Override // com.oplus.quicksettings.IOplusTileService
    public void updateOplusTile(Tile tile, ComponentName name) {
    }

    @Override // com.oplus.quicksettings.IOplusTileService
    public void updateRequest(Bundle bundle, ComponentName name) {
    }

    @Override // com.oplus.quicksettings.IOplusTileService
    public void setDeathRecipientToken(IBinder binder, ComponentName name) {
    }
}
