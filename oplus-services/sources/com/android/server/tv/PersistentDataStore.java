package com.android.server.tv;

import android.content.Context;
import android.content.Intent;
import android.media.tv.TvContentRating;
import android.os.Environment;
import android.os.Handler;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PersistentDataStore {
    private static final String ATTR_ENABLED = "enabled";
    private static final String ATTR_STRING = "string";
    private static final String TAG = "TvInputManagerService";
    private static final String TAG_BLOCKED_RATINGS = "blocked-ratings";
    private static final String TAG_PARENTAL_CONTROLS = "parental-controls";
    private static final String TAG_RATING = "rating";
    private static final String TAG_TV_INPUT_MANAGER_STATE = "tv-input-manager-state";
    private final AtomicFile mAtomicFile;
    private boolean mBlockedRatingsChanged;
    private final Context mContext;
    private boolean mLoaded;
    private boolean mParentalControlsEnabled;
    private boolean mParentalControlsEnabledChanged;
    private final Handler mHandler = new Handler();
    private final List<TvContentRating> mBlockedRatings = Collections.synchronizedList(new ArrayList());
    private final Runnable mSaveRunnable = new Runnable() { // from class: com.android.server.tv.PersistentDataStore.1
        @Override // java.lang.Runnable
        public void run() {
            PersistentDataStore.this.save();
        }
    };

    public PersistentDataStore(Context context, int i) {
        this.mContext = context;
        File userSystemDirectory = Environment.getUserSystemDirectory(i);
        if (!userSystemDirectory.exists() && !userSystemDirectory.mkdirs()) {
            throw new IllegalStateException("User dir cannot be created: " + userSystemDirectory);
        }
        this.mAtomicFile = new AtomicFile(new File(userSystemDirectory, "tv-input-manager-state.xml"), "tv-input-state");
    }

    public boolean isParentalControlsEnabled() {
        loadIfNeeded();
        return this.mParentalControlsEnabled;
    }

    public void setParentalControlsEnabled(boolean z) {
        loadIfNeeded();
        if (this.mParentalControlsEnabled != z) {
            this.mParentalControlsEnabled = z;
            this.mParentalControlsEnabledChanged = true;
            postSave();
        }
    }

    public boolean isRatingBlocked(TvContentRating tvContentRating) {
        loadIfNeeded();
        synchronized (this.mBlockedRatings) {
            Iterator<TvContentRating> it = this.mBlockedRatings.iterator();
            while (it.hasNext()) {
                if (tvContentRating.contains(it.next())) {
                    return true;
                }
            }
            return false;
        }
    }

    public TvContentRating[] getBlockedRatings() {
        loadIfNeeded();
        List<TvContentRating> list = this.mBlockedRatings;
        return (TvContentRating[]) list.toArray(new TvContentRating[list.size()]);
    }

    public void addBlockedRating(TvContentRating tvContentRating) {
        loadIfNeeded();
        if (tvContentRating == null || this.mBlockedRatings.contains(tvContentRating)) {
            return;
        }
        this.mBlockedRatings.add(tvContentRating);
        this.mBlockedRatingsChanged = true;
        postSave();
    }

    public void removeBlockedRating(TvContentRating tvContentRating) {
        loadIfNeeded();
        if (tvContentRating == null || !this.mBlockedRatings.contains(tvContentRating)) {
            return;
        }
        this.mBlockedRatings.remove(tvContentRating);
        this.mBlockedRatingsChanged = true;
        postSave();
    }

    private void loadIfNeeded() {
        if (this.mLoaded) {
            return;
        }
        load();
        this.mLoaded = true;
    }

    private void clearState() {
        this.mBlockedRatings.clear();
        this.mParentalControlsEnabled = false;
    }

    private void load() {
        clearState();
        try {
            FileInputStream openRead = this.mAtomicFile.openRead();
            try {
                try {
                    loadFromXml(Xml.resolvePullParser(openRead));
                } catch (IOException | XmlPullParserException e) {
                    Slog.w(TAG, "Failed to load tv input manager persistent store data.", e);
                    clearState();
                }
            } finally {
                IoUtils.closeQuietly(openRead);
            }
        } catch (FileNotFoundException unused) {
        }
    }

    private void postSave() {
        this.mHandler.removeCallbacks(this.mSaveRunnable);
        this.mHandler.post(this.mSaveRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void save() {
        try {
            FileOutputStream startWrite = this.mAtomicFile.startWrite();
            try {
                TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                saveToXml(resolveSerializer);
                resolveSerializer.flush();
                this.mAtomicFile.finishWrite(startWrite);
                broadcastChangesIfNeeded();
            } catch (Throwable th) {
                this.mAtomicFile.failWrite(startWrite);
                throw th;
            }
        } catch (IOException e) {
            Slog.w(TAG, "Failed to save tv input manager persistent store data.", e);
        }
    }

    private void broadcastChangesIfNeeded() {
        if (this.mParentalControlsEnabledChanged) {
            this.mParentalControlsEnabledChanged = false;
            this.mContext.sendBroadcastAsUser(new Intent("android.media.tv.action.PARENTAL_CONTROLS_ENABLED_CHANGED"), UserHandle.ALL);
        }
        if (this.mBlockedRatingsChanged) {
            this.mBlockedRatingsChanged = false;
            this.mContext.sendBroadcastAsUser(new Intent("android.media.tv.action.BLOCKED_RATINGS_CHANGED"), UserHandle.ALL);
        }
    }

    private void loadFromXml(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
        XmlUtils.beginDocument(typedXmlPullParser, TAG_TV_INPUT_MANAGER_STATE);
        int depth = typedXmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
            if (typedXmlPullParser.getName().equals(TAG_BLOCKED_RATINGS)) {
                loadBlockedRatingsFromXml(typedXmlPullParser);
            } else if (typedXmlPullParser.getName().equals(TAG_PARENTAL_CONTROLS)) {
                this.mParentalControlsEnabled = typedXmlPullParser.getAttributeBoolean((String) null, "enabled");
            }
        }
    }

    private void loadBlockedRatingsFromXml(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
        int depth = typedXmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
            if (typedXmlPullParser.getName().equals(TAG_RATING)) {
                String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_STRING);
                if (TextUtils.isEmpty(attributeValue)) {
                    throw new XmlPullParserException("Missing string attribute on rating");
                }
                this.mBlockedRatings.add(TvContentRating.unflattenFromString(attributeValue));
            }
        }
    }

    private void saveToXml(TypedXmlSerializer typedXmlSerializer) throws IOException {
        typedXmlSerializer.startDocument((String) null, Boolean.TRUE);
        typedXmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        typedXmlSerializer.startTag((String) null, TAG_TV_INPUT_MANAGER_STATE);
        typedXmlSerializer.startTag((String) null, TAG_BLOCKED_RATINGS);
        synchronized (this.mBlockedRatings) {
            for (TvContentRating tvContentRating : this.mBlockedRatings) {
                typedXmlSerializer.startTag((String) null, TAG_RATING);
                typedXmlSerializer.attribute((String) null, ATTR_STRING, tvContentRating.flattenToString());
                typedXmlSerializer.endTag((String) null, TAG_RATING);
            }
        }
        typedXmlSerializer.endTag((String) null, TAG_BLOCKED_RATINGS);
        typedXmlSerializer.startTag((String) null, TAG_PARENTAL_CONTROLS);
        typedXmlSerializer.attributeBoolean((String) null, "enabled", this.mParentalControlsEnabled);
        typedXmlSerializer.endTag((String) null, TAG_PARENTAL_CONTROLS);
        typedXmlSerializer.endTag((String) null, TAG_TV_INPUT_MANAGER_STATE);
        typedXmlSerializer.endDocument();
    }
}
