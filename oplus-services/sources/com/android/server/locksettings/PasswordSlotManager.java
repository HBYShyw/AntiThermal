package com.android.server.locksettings;

import android.os.SystemProperties;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class PasswordSlotManager {
    private static final String GSI_RUNNING_PROP = "ro.gsid.image_running";
    private static final String SLOT_MAP_DIR = "/metadata/password_slots";
    private static final String TAG = "PasswordSlotManager";
    private Set<Integer> mActiveSlots;
    private Map<Integer, String> mSlotMap;

    @VisibleForTesting
    protected String getSlotMapDir() {
        return SLOT_MAP_DIR;
    }

    @VisibleForTesting
    protected int getGsiImageNumber() {
        return SystemProperties.getInt(GSI_RUNNING_PROP, 0);
    }

    public void refreshActiveSlots(Set<Integer> set) throws RuntimeException {
        if (this.mSlotMap == null) {
            this.mActiveSlots = new HashSet(set);
            return;
        }
        HashSet hashSet = new HashSet();
        for (Map.Entry<Integer, String> entry : this.mSlotMap.entrySet()) {
            if (entry.getValue().equals(getMode())) {
                hashSet.add(entry.getKey());
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            this.mSlotMap.remove((Integer) it.next());
        }
        Iterator<Integer> it2 = set.iterator();
        while (it2.hasNext()) {
            this.mSlotMap.put(it2.next(), getMode());
        }
        saveSlotMap();
    }

    public void markSlotInUse(int i) throws RuntimeException {
        ensureSlotMapLoaded();
        if (this.mSlotMap.containsKey(Integer.valueOf(i)) && !this.mSlotMap.get(Integer.valueOf(i)).equals(getMode())) {
            throw new IllegalStateException("password slot " + i + " is not available");
        }
        this.mSlotMap.put(Integer.valueOf(i), getMode());
        saveSlotMap();
    }

    public void markSlotDeleted(int i) throws RuntimeException {
        ensureSlotMapLoaded();
        if (this.mSlotMap.containsKey(Integer.valueOf(i)) && !this.mSlotMap.get(Integer.valueOf(i)).equals(getMode())) {
            throw new IllegalStateException("password slot " + i + " cannot be deleted");
        }
        this.mSlotMap.remove(Integer.valueOf(i));
        saveSlotMap();
    }

    public Set<Integer> getUsedSlots() {
        ensureSlotMapLoaded();
        return Collections.unmodifiableSet(this.mSlotMap.keySet());
    }

    private File getSlotMapFile() {
        return Paths.get(getSlotMapDir(), "slot_map").toFile();
    }

    private String getMode() {
        int gsiImageNumber = getGsiImageNumber();
        if (gsiImageNumber <= 0) {
            return "host";
        }
        return "gsi" + gsiImageNumber;
    }

    @VisibleForTesting
    protected Map<Integer, String> loadSlotMap(InputStream inputStream) throws IOException {
        HashMap hashMap = new HashMap();
        Properties properties = new Properties();
        properties.load(inputStream);
        for (String str : properties.stringPropertyNames()) {
            int parseInt = Integer.parseInt(str);
            hashMap.put(Integer.valueOf(parseInt), properties.getProperty(str));
        }
        return hashMap;
    }

    private Map<Integer, String> loadSlotMap() {
        File slotMapFile = getSlotMapFile();
        if (slotMapFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(slotMapFile);
                try {
                    Map<Integer, String> loadSlotMap = loadSlotMap(fileInputStream);
                    fileInputStream.close();
                    return loadSlotMap;
                } finally {
                }
            } catch (Exception e) {
                Slog.e(TAG, "Could not load slot map file", e);
            }
        }
        return new HashMap();
    }

    private void ensureSlotMapLoaded() {
        if (this.mSlotMap == null) {
            this.mSlotMap = loadSlotMap();
            Set<Integer> set = this.mActiveSlots;
            if (set != null) {
                refreshActiveSlots(set);
                this.mActiveSlots = null;
            }
        }
    }

    @VisibleForTesting
    protected void saveSlotMap(OutputStream outputStream) throws IOException {
        if (this.mSlotMap == null) {
            return;
        }
        Properties properties = new Properties();
        for (Map.Entry<Integer, String> entry : this.mSlotMap.entrySet()) {
            properties.setProperty(entry.getKey().toString(), entry.getValue());
        }
        properties.store(outputStream, "");
    }

    private void saveSlotMap() {
        if (this.mSlotMap == null) {
            return;
        }
        if (!getSlotMapFile().getParentFile().exists()) {
            Slog.w(TAG, "Not saving slot map, " + getSlotMapDir() + " does not exist");
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(getSlotMapFile());
            try {
                saveSlotMap(fileOutputStream);
                fileOutputStream.close();
            } finally {
            }
        } catch (IOException e) {
            Slog.e(TAG, "failed to save password slot map", e);
        }
    }
}
