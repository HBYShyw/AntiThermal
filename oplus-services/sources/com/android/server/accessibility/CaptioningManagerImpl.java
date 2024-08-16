package com.android.server.accessibility;

import android.content.Context;
import android.os.Binder;
import android.provider.Settings;
import android.view.accessibility.CaptioningManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CaptioningManagerImpl implements CaptioningManager.SystemAudioCaptioningAccessing {
    private static final boolean SYSTEM_AUDIO_CAPTIONING_UI_DEFAULT_ENABLED = false;
    private final Context mContext;

    public CaptioningManagerImpl(Context context) {
        this.mContext = context;
    }

    public void setSystemAudioCaptioningEnabled(boolean z, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "odi_captions_enabled", z ? 1 : 0, i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean isSystemAudioCaptioningUiEnabled(int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "odi_captions_volume_ui_enabled", 0, i) == 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setSystemAudioCaptioningUiEnabled(boolean z, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "odi_captions_volume_ui_enabled", z ? 1 : 0, i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
