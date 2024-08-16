package com.oplus.media;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/* loaded from: classes.dex */
public class OplusMediaMetadata extends OplusMediaEventData {
    public static final Parcelable.Creator<OplusMediaMetadata> CREATOR = new Parcelable.Creator<OplusMediaMetadata>() { // from class: com.oplus.media.OplusMediaMetadata.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaMetadata createFromParcel(Parcel in) {
            return new OplusMediaMetadata(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusMediaMetadata[] newArray(int size) {
            return new OplusMediaMetadata[size];
        }
    };
    public static final String KEY_ALBUM = "media.metadata.album";
    public static final String KEY_ALBUM_ART = "media.metadata.album_art";
    public static final String KEY_ARTIST = "media.metadata.artist";
    public static final String KEY_DURATION = "media.metadata.duration";
    public static final String KEY_REMOTE_ART = "media.metadata.remote_art";
    public static final String KEY_TITLE = "media.metadata.title";
    private Map<String, Object> mMediaMetadataMap;

    public OplusMediaMetadata() {
        this.mMediaMetadataMap = new HashMap();
    }

    public OplusMediaMetadata(Parcel in) {
        HashMap hashMap = new HashMap();
        this.mMediaMetadataMap = hashMap;
        in.readMap(hashMap, getClass().getClassLoader());
    }

    public void add(String key, String value) {
        this.mMediaMetadataMap.put(key, value);
    }

    public void add(String key, long value) {
        this.mMediaMetadataMap.put(key, Long.valueOf(value));
    }

    public void add(String key, int value) {
        this.mMediaMetadataMap.put(key, Integer.valueOf(value));
    }

    public void add(String key, Bitmap value) {
        this.mMediaMetadataMap.put(key, value);
    }

    public void add(String key, byte[] value) {
        this.mMediaMetadataMap.put(key, value);
    }

    public int getInt(String key) {
        return ((Integer) this.mMediaMetadataMap.get(key)).intValue();
    }

    public long getLong(String key) {
        return ((Long) this.mMediaMetadataMap.get(key)).longValue();
    }

    public String getString(String key) {
        return (String) this.mMediaMetadataMap.get(key);
    }

    public Optional<Bitmap> getBitmap(String key) {
        Optional<Bitmap> optional = Optional.empty();
        if (this.mMediaMetadataMap.get(key) instanceof Bitmap) {
            return Optional.ofNullable((Bitmap) this.mMediaMetadataMap.get(key));
        }
        return optional;
    }

    public byte[] getBytes(String key) {
        return (byte[]) this.mMediaMetadataMap.get(key);
    }

    public boolean has(String key) {
        return this.mMediaMetadataMap.containsKey(key);
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(this.mMediaMetadataMap);
    }

    @Override // com.oplus.media.OplusMediaEventData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
