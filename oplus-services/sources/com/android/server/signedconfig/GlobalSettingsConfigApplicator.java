package com.android.server.signedconfig;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.server.signedconfig.SignedConfig;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class GlobalSettingsConfigApplicator {
    private static final Set<String> ALLOWED_KEYS = Collections.unmodifiableSet(new ArraySet(Arrays.asList("hidden_api_policy", "hidden_api_blacklist_exemptions")));
    private static final Map<String, String> HIDDEN_API_POLICY_KEY_MAP;
    private static final Map<String, Map<String, String>> KEY_VALUE_MAPPERS;
    private static final String TAG = "SignedConfig";
    private final Context mContext;
    private final SignedConfigEvent mEvent;
    private final String mSourcePackage;
    private final SignatureVerifier mVerifier;

    static {
        Map<String, String> makeMap = makeMap("DEFAULT", String.valueOf(-1), "DISABLED", String.valueOf(0), "JUST_WARN", String.valueOf(1), "ENABLED", String.valueOf(2));
        HIDDEN_API_POLICY_KEY_MAP = makeMap;
        KEY_VALUE_MAPPERS = makeMap("hidden_api_policy", makeMap);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <K, V> Map<K, V> makeMap(Object... objArr) {
        if (objArr.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        int length = objArr.length / 2;
        ArrayMap arrayMap = new ArrayMap(length);
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            arrayMap.put(objArr[i2], objArr[i2 + 1]);
        }
        return Collections.unmodifiableMap(arrayMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GlobalSettingsConfigApplicator(Context context, String str, SignedConfigEvent signedConfigEvent) {
        this.mContext = context;
        this.mSourcePackage = str;
        this.mEvent = signedConfigEvent;
        this.mVerifier = new SignatureVerifier(signedConfigEvent);
    }

    private boolean checkSignature(String str, String str2) {
        try {
            return this.mVerifier.verifySignature(str, str2);
        } catch (GeneralSecurityException e) {
            Slog.e(TAG, "Failed to verify signature", e);
            this.mEvent.status = 4;
            return false;
        }
    }

    private int getCurrentConfigVersion() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "signed_config_version", 0);
    }

    private void updateCurrentConfig(int i, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Settings.Global.putString(this.mContext.getContentResolver(), entry.getKey(), entry.getValue());
        }
        Settings.Global.putInt(this.mContext.getContentResolver(), "signed_config_version", i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyConfig(String str, String str2) {
        if (!checkSignature(str, str2)) {
            Slog.e(TAG, "Signature check on global settings in package " + this.mSourcePackage + " failed; ignoring");
            return;
        }
        try {
            SignedConfig parse = SignedConfig.parse(str, ALLOWED_KEYS, KEY_VALUE_MAPPERS);
            this.mEvent.version = parse.version;
            int currentConfigVersion = getCurrentConfigVersion();
            if (currentConfigVersion >= parse.version) {
                Slog.i(TAG, "Global settings from package " + this.mSourcePackage + " is older than existing: " + parse.version + "<=" + currentConfigVersion);
                this.mEvent.status = 6;
                return;
            }
            Slog.i(TAG, "Got new global settings from package " + this.mSourcePackage + ": version " + parse.version + " replacing existing version " + currentConfigVersion);
            SignedConfig.PerSdkConfig matchingConfig = parse.getMatchingConfig(Build.VERSION.SDK_INT);
            if (matchingConfig == null) {
                Slog.i(TAG, "Settings is not applicable to current SDK version; ignoring");
                this.mEvent.status = 8;
                return;
            }
            Slog.i(TAG, "Updating global settings to version " + parse.version);
            updateCurrentConfig(parse.version, matchingConfig.values);
            this.mEvent.status = 1;
        } catch (InvalidConfigException e) {
            Slog.e(TAG, "Failed to parse global settings from package " + this.mSourcePackage, e);
            this.mEvent.status = 5;
        }
    }
}
