package com.android.server.backup;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UserBackupPreferences {
    private static final String PREFERENCES_FILE = "backup_preferences";
    private final SharedPreferences.Editor mEditor;
    private final SharedPreferences mPreferences;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserBackupPreferences(Context context, File file) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(new File(file, PREFERENCES_FILE), 0);
        this.mPreferences = sharedPreferences;
        this.mEditor = sharedPreferences.edit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addExcludedKeys(String str, List<String> list) {
        HashSet hashSet = new HashSet(this.mPreferences.getStringSet(str, Collections.emptySet()));
        hashSet.addAll(list);
        this.mEditor.putStringSet(str, hashSet);
        this.mEditor.commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<String> getExcludedRestoreKeysForPackage(String str) {
        return this.mPreferences.getStringSet(str, Collections.emptySet());
    }
}
