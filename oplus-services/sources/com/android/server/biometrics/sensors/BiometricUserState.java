package com.android.server.biometrics.sensors;

import android.content.Context;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.BiometricAuthenticator.Identifier;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemProperties;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class BiometricUserState<T extends BiometricAuthenticator.Identifier> {
    private static final String ATTR_INVALIDATION = "authenticatorIdInvalidation_attr";
    private static final boolean DEBUG_FINGER = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String TAG = "UserState";
    private static final String TAG_INVALIDATION = "authenticatorIdInvalidation_tag";
    protected final Context mContext;
    protected final File mFile;
    protected boolean mInvalidationInProgress;

    @GuardedBy({"this"})
    protected final ArrayList<T> mBiometrics = new ArrayList<>();
    private final Runnable mWriteStateRunnable = new Runnable() { // from class: com.android.server.biometrics.sensors.BiometricUserState$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            BiometricUserState.this.doWriteStateInternal();
        }
    };

    protected abstract void doWriteState(TypedXmlSerializer typedXmlSerializer) throws Exception;

    protected abstract String getBiometricsTag();

    protected abstract ArrayList<T> getCopy(ArrayList<T> arrayList);

    protected abstract int getNameTemplateResource();

    protected abstract void parseBiometricsLocked(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException;

    /* JADX INFO: Access modifiers changed from: private */
    public void doWriteStateInternal() {
        FileOutputStream startWrite;
        AtomicFile atomicFile = new AtomicFile(this.mFile);
        FileOutputStream fileOutputStream = null;
        try {
            startWrite = atomicFile.startWrite();
        } catch (Throwable th) {
            th = th;
        }
        try {
            TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
            resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            resolveSerializer.startDocument((String) null, Boolean.TRUE);
            resolveSerializer.startTag((String) null, TAG_INVALIDATION);
            resolveSerializer.attributeBoolean((String) null, ATTR_INVALIDATION, this.mInvalidationInProgress);
            resolveSerializer.endTag((String) null, TAG_INVALIDATION);
            doWriteState(resolveSerializer);
            resolveSerializer.endDocument();
            atomicFile.finishWrite(startWrite);
            IoUtils.closeQuietly(startWrite);
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream = startWrite;
            try {
                Slog.wtf(TAG, "Failed to write settings, restoring backup", th);
                atomicFile.failWrite(fileOutputStream);
                throw new IllegalStateException("Failed to write to file: " + this.mFile.toString(), th);
            } catch (Throwable th3) {
                IoUtils.closeQuietly(fileOutputStream);
                throw th3;
            }
        }
    }

    public BiometricUserState(Context context, int i, String str) {
        this.mFile = getFileForUser(i, str);
        this.mContext = context;
        synchronized (this) {
            readStateSyncLocked();
        }
    }

    public void setInvalidationInProgress(boolean z) {
        synchronized (this) {
            this.mInvalidationInProgress = z;
            scheduleWriteStateLocked();
        }
    }

    public boolean isInvalidationInProgress() {
        boolean z;
        synchronized (this) {
            z = this.mInvalidationInProgress;
        }
        return z;
    }

    public void addBiometric(T t) {
        synchronized (this) {
            this.mBiometrics.add(t);
            scheduleWriteStateLocked();
        }
    }

    public void removeBiometric(int i) {
        synchronized (this) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.mBiometrics.size()) {
                    break;
                }
                if (this.mBiometrics.get(i2).getBiometricId() == i) {
                    this.mBiometrics.remove(i2);
                    scheduleWriteStateLocked();
                    break;
                }
                i2++;
            }
        }
    }

    public void renameBiometric(int i, CharSequence charSequence) {
        synchronized (this) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.mBiometrics.size()) {
                    break;
                }
                if (this.mBiometrics.get(i2).getBiometricId() == i) {
                    this.mBiometrics.get(i2).setName(charSequence);
                    scheduleWriteStateLocked();
                    break;
                }
                i2++;
            }
        }
    }

    public List<T> getBiometrics() {
        ArrayList<T> copy;
        synchronized (this) {
            copy = getCopy(this.mBiometrics);
        }
        return copy;
    }

    public String getUniqueName() {
        String string;
        int i = 1;
        while (true) {
            string = this.mContext.getString(getNameTemplateResource(), Integer.valueOf(i));
            if (isUnique(string)) {
                break;
            }
            i++;
        }
        if (string != null && DEBUG_FINGER) {
            Slog.d(TAG, "getUniqueName name:" + string + " nameSize:" + string.length());
        }
        return string;
    }

    private boolean isUnique(String str) {
        Iterator<T> it = this.mBiometrics.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equals(str)) {
                return false;
            }
        }
        return true;
    }

    private File getFileForUser(int i, String str) {
        return new File(Environment.getUserSystemDirectory(i), str);
    }

    private void scheduleWriteStateLocked() {
        AsyncTask.execute(this.mWriteStateRunnable);
    }

    @GuardedBy({"this"})
    private void readStateSyncLocked() {
        if (this.mFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(this.mFile);
                try {
                    try {
                        parseStateLocked(Xml.resolvePullParser(fileInputStream));
                    } catch (IOException | XmlPullParserException e) {
                        throw new IllegalStateException("Failed parsing settings file: " + this.mFile, e);
                    }
                } finally {
                    IoUtils.closeQuietly(fileInputStream);
                }
            } catch (FileNotFoundException unused) {
                Slog.i(TAG, "No fingerprint state");
            }
        }
    }

    @GuardedBy({"this"})
    private void parseStateLocked(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && typedXmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next != 3 && next != 4) {
                String name = typedXmlPullParser.getName();
                if (name.equals(getBiometricsTag())) {
                    parseBiometricsLocked(typedXmlPullParser);
                } else if (name.equals(TAG_INVALIDATION)) {
                    this.mInvalidationInProgress = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_INVALIDATION);
                }
            }
        }
    }
}
