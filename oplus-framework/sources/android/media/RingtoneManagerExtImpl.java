package android.media;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.OplusThemeResources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.oplus.Telephony;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import com.oplus.util.OplusResolverIntentUtil;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import libcore.io.IoUtils;

/* loaded from: classes.dex */
public class RingtoneManagerExtImpl implements IRingtoneManagerExt {
    public static final String CALENDAR_REMINDER_SOUND = "calendar_sound";
    private static final String CARRIER_CUSTOM_DEFAULT_RINGTONE_VERSION_PROP = "ro.oplus.carrier.ringtone.version";
    private static final String CARRIER_CUSTOM_DEFAULT_RINGTONE_VERSION_SETTINGS = "carrier_custom_default_ringtone_version";
    private static final String CARRIER_OTA_VERSION_BACKUP_NAME = "carrier_ota_version_backup";
    private static final String CARRIER_OTA_VERSION_NAME = "ro.oplus.image.my_carrier.date";
    private static final String COMPANY_CUSTOM_DEFAULT_RINGTONE_VERSION_PROP = "ro.oplus.company.ringtone.version";
    private static final String COMPANY_CUSTOM_DEFAULT_RINGTONE_VERSION_SETTINGS = "company_custom_default_ringtone_version";
    private static final String COMPANY_OTA_VERSION_BACKUP_NAME = "company_ota_version_backup";
    private static final String COMPANY_OTA_VERSION_NAME = "ro.oplus.image.my_company.date";
    public static final String DEFAULT_CALENDAR_REMINDER_SOUND = "calendar_default_sound";
    private static final String FILE_NO_MEDIA = "/.nomedia";
    public static final String NOTIFICATION_SOUND_SIM2 = "notification_sim2";
    public static final String OPLUS_DEFAULT_ALARM = "oplus_customize_default_alarm";
    public static final String OPLUS_DEFAULT_NOTIFICATION = "oplus_customize_default_notification";
    public static final String OPLUS_DEFAULT_NOTIFICATION_SIM2 = "oplus_customize_default_notification_sim2";
    public static final String OPLUS_DEFAULT_RINGTONE = "oplus_customize_default_ringtone";
    public static final String OPLUS_DEFAULT_RINGTONE_SIM2 = "oplus_customize_default_ringtone_sim2";
    public static final String OPLUS_DEFAULT_SMS_NOTIFICATION = "oplus_customize_default_sms_notification_sound";
    public static final String OPLUS_SMS_NOTIFICATION_SOUND = "oplus_customize_sms_notification_sound";
    private static final String PATH_MICROMSG = "/tencent/MicroMsg";
    public static final String RINGTONE_SIM2 = "ringtone_sim2";
    private static final String TAG = "OplusRingtoneManager";
    private static RingtoneManagerExtImpl sInstance;
    private HashMap<String, String> mTitleMaps = new HashMap<>();
    private static final String[] BLACKLIST_DIRECTORY = {"image2", "voice2", "emoji", "avatar", "sns", "openapi", "package", OplusResolverIntentUtil.DEFAULT_APP_VIDEO};
    private static final String[] NEW_BLACKLIST_DIRECTORY = {"wxafiles", "wxanewfiles"};

    public static RingtoneManagerExtImpl getInstance(Object obj) {
        RingtoneManagerExtImpl ringtoneManagerExtImpl;
        synchronized (RingtoneManagerExtImpl.class) {
            if (sInstance == null) {
                sInstance = new RingtoneManagerExtImpl();
            }
            ringtoneManagerExtImpl = sInstance;
        }
        return ringtoneManagerExtImpl;
    }

    public static void setRingtoneIfNotSet(Context context, String settingName, Uri uri) {
        Log.d(TAG, "setRingtoneIfNotSet: settingName:" + settingName + ", uri:" + uri);
        if (isNeedSet(context, settingName)) {
            Settings.System.putString(context.getContentResolver(), settingName, uri.toString());
        }
    }

    private static boolean isNeedSet(Context context, String settingName) {
        String existingSettingValue = Settings.System.getString(context.getContentResolver(), settingName);
        Log.d(TAG, "isNeedSet:" + TextUtils.isEmpty(existingSettingValue));
        return TextUtils.isEmpty(existingSettingValue);
    }

    public void setRingtonesUri(Context context, int type, Uri ringtoneUri) {
        Log.d(TAG, "setRingtonesUri type:" + type + "ringtoneUri" + ringtoneUri);
        switch (type) {
            case 1:
                if (isNeedSet(context, "ringtone")) {
                    RingtoneManager.setActualDefaultRingtoneUri(context, type, ringtoneUri);
                }
                setRingtoneIfNotSet(context, "oplus_customize_default_ringtone", ringtoneUri);
                return;
            case 2:
                if (isNeedSet(context, "notification_sound")) {
                    RingtoneManager.setActualDefaultRingtoneUri(context, type, ringtoneUri);
                }
                setRingtoneIfNotSet(context, "oplus_customize_default_notification", ringtoneUri);
                return;
            case 4:
                if (isNeedSet(context, "alarm_alert")) {
                    RingtoneManager.setActualDefaultRingtoneUri(context, type, ringtoneUri);
                }
                setRingtoneIfNotSet(context, "oplus_customize_default_alarm", ringtoneUri);
                return;
            case 8:
                setRingtoneIfNotSet(context, OPLUS_SMS_NOTIFICATION_SOUND, ringtoneUri);
                setRingtoneIfNotSet(context, "oplus_customize_default_sms_notification_sound", ringtoneUri);
                return;
            case 16:
                setRingtoneIfNotSet(context, NOTIFICATION_SOUND_SIM2, ringtoneUri);
                setRingtoneIfNotSet(context, "oplus_customize_default_notification_sim2", ringtoneUri);
                return;
            case 32:
                setRingtoneIfNotSet(context, CALENDAR_REMINDER_SOUND, ringtoneUri);
                setRingtoneIfNotSet(context, DEFAULT_CALENDAR_REMINDER_SOUND, ringtoneUri);
                return;
            case 64:
                setRingtoneIfNotSet(context, RINGTONE_SIM2, ringtoneUri);
                setRingtoneIfNotSet(context, "oplus_customize_default_ringtone_sim2", ringtoneUri);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean isComponentVersionChange(Context context) {
        String olderCompanyVersion = Settings.System.getString(context.getContentResolver(), COMPANY_OTA_VERSION_BACKUP_NAME);
        String olderCarrierVersion = Settings.System.getString(context.getContentResolver(), CARRIER_OTA_VERSION_BACKUP_NAME);
        String newCompanyVersion = SystemProperties.get(COMPANY_OTA_VERSION_NAME, "");
        String newCarrierVersion = SystemProperties.get(CARRIER_OTA_VERSION_NAME, "");
        if (isVersionStringChanged(newCompanyVersion, olderCompanyVersion)) {
            Settings.System.putString(context.getContentResolver(), COMPANY_OTA_VERSION_BACKUP_NAME, newCompanyVersion);
            Log.d(TAG, "my_company changed");
            return true;
        }
        if (isVersionStringChanged(newCarrierVersion, olderCarrierVersion)) {
            Settings.System.putString(context.getContentResolver(), CARRIER_OTA_VERSION_BACKUP_NAME, newCarrierVersion);
            Log.d(TAG, "my_carrier changed");
            return true;
        }
        return false;
    }

    public boolean isCustomDefaultRingtoneNeeded(Context context) {
        String companyCustomRingtoneVersion = Settings.System.getString(context.getContentResolver(), COMPANY_CUSTOM_DEFAULT_RINGTONE_VERSION_SETTINGS);
        String carrierCustomRingtoneVersion = Settings.System.getString(context.getContentResolver(), CARRIER_CUSTOM_DEFAULT_RINGTONE_VERSION_SETTINGS);
        String newCompanyCustomRingtoneVersion = SystemProperties.get(COMPANY_CUSTOM_DEFAULT_RINGTONE_VERSION_PROP, "");
        String newCarrierCustomRingtoneVersion = SystemProperties.get(CARRIER_CUSTOM_DEFAULT_RINGTONE_VERSION_PROP, "");
        if (isCustRingtoneVersionUpdate(newCompanyCustomRingtoneVersion, companyCustomRingtoneVersion)) {
            Settings.System.putString(context.getContentResolver(), COMPANY_CUSTOM_DEFAULT_RINGTONE_VERSION_SETTINGS, newCompanyCustomRingtoneVersion);
            Log.d(TAG, "company custom ringtone version change, need update config");
            return true;
        }
        if (isCustRingtoneVersionUpdate(newCarrierCustomRingtoneVersion, carrierCustomRingtoneVersion)) {
            Settings.System.putString(context.getContentResolver(), CARRIER_CUSTOM_DEFAULT_RINGTONE_VERSION_SETTINGS, newCarrierCustomRingtoneVersion);
            Log.d(TAG, "carrier custom ringtone version change, need update config");
            return true;
        }
        return false;
    }

    private static boolean isVersionStringChanged(String newVeresion, String olerVersion) {
        if (newVeresion.equals(SplitPathManager.DEFAULT)) {
            return false;
        }
        if (newVeresion.equals("") || newVeresion.equals("nconf")) {
            return false;
        }
        String compareStr = olerVersion != null ? olerVersion : "";
        return !compareStr.equals(newVeresion);
    }

    private static boolean isCustRingtoneVersionUpdate(String newVersion, String olderVersion) {
        String newVersionStr = TextUtils.isEmpty(newVersion) ? "" : newVersion;
        String compareStr = TextUtils.isEmpty(olderVersion) ? "" : olderVersion;
        return !newVersionStr.equals(compareStr);
    }

    public void clearDefaultRingtonesHistory(Context context) {
        Log.d(TAG, "component update, clearDefaultRingtonesHistory");
        Settings.System.putInt(context.getContentResolver(), "ringtone_set", 0);
        Settings.System.putInt(context.getContentResolver(), "ringtone_sim2_set", 0);
        Settings.System.putInt(context.getContentResolver(), "notification_sound_set", 0);
        Settings.System.putInt(context.getContentResolver(), "notification_sound_sms_set", 0);
        Settings.System.putInt(context.getContentResolver(), "notification_sound_sim2_set", 0);
        Settings.System.putString(context.getContentResolver(), "ringtone", "");
        Settings.System.putString(context.getContentResolver(), "oplus_customize_default_ringtone", "");
        Settings.System.putString(context.getContentResolver(), "notification_sound", "");
        Settings.System.putString(context.getContentResolver(), "oplus_customize_default_notification", "");
        Settings.System.putString(context.getContentResolver(), OPLUS_SMS_NOTIFICATION_SOUND, "");
        Settings.System.putString(context.getContentResolver(), "oplus_customize_default_sms_notification_sound", "");
        Settings.System.putString(context.getContentResolver(), NOTIFICATION_SOUND_SIM2, "");
        Settings.System.putString(context.getContentResolver(), "oplus_customize_default_notification_sim2", "");
        Settings.System.putString(context.getContentResolver(), RINGTONE_SIM2, "");
        Settings.System.putString(context.getContentResolver(), "oplus_customize_default_ringtone_sim2", "");
    }

    public void setRingtoneUriAgainIfNeeded(Context context, String filename, Uri baseuri, int type) {
        if (type != 4 || (RingtoneManager.getActualDefaultRingtoneUri(context, 4) != null && !isNeedSet(context, "oplus_customize_default_alarm"))) {
            Log.d(TAG, "no need to refind alarm data");
            return;
        }
        Cursor cursor = context.getContentResolver().query(baseuri, new String[]{"_id"}, "_display_name=? AND is_ringtone=?", new String[]{filename, "1"}, null);
        try {
            if (cursor.moveToFirst()) {
                Uri ringtoneUri = context.getContentResolver().canonicalizeOrElse(ContentUris.withAppendedId(baseuri, cursor.getLong(0)));
                Log.d(TAG, "refind alarm type uri data");
                setRingtonesUri(context, type, ringtoneUri);
                Settings.System.putInt(context.getContentResolver(), "alarm_alert_set", 1);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public void putSettingRingCacheTitleAndPath(Context context, int type, String title, String path) {
        long token = Binder.clearCallingIdentity();
        try {
            try {
                ContentResolver resolver = context.getContentResolver();
                if (type == 64) {
                    Settings.System.putString(resolver, "ringtone_cache_title_sim2", title);
                    Settings.System.putString(resolver, "ringtone_cache_path_sim2", path);
                } else if (type == 1) {
                    Settings.System.putString(resolver, "ringtone_cache_title", title);
                    Settings.System.putString(resolver, "ringtone_cache_path", path);
                }
            } catch (Exception e) {
                Log.d(TAG, "putSettingRingCacheTitleAndPath error maybe no permission " + e.getMessage());
            }
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0082, code lost:
    
        if (r4 != null) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0084, code lost:
    
        libcore.io.IoUtils.closeQuietly(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00ad, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00aa, code lost:
    
        if (r4 == null) goto L44;
     */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00b1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Pair<String, String> getRingtoneTitlePath(Context context, Uri ringtoneUri) {
        Cursor cursor = null;
        Pair<String, String> result = null;
        try {
            cursor = context.getContentResolver().query(ringtoneUri, new String[]{"_display_name", Telephony.Mms.Part._DATA, "is_ringtone", "is_notification"}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                boolean isRingtone = cursor.getInt(cursor.getColumnIndex("is_ringtone")) == 1;
                boolean isNotification = cursor.getInt(cursor.getColumnIndex("is_notification")) == 1;
                if (!isRingtone && !isNotification) {
                }
                try {
                    try {
                        String displayName = removeExtension(cursor.getString(cursor.getColumnIndex("_display_name")));
                        String path = cursor.getString(cursor.getColumnIndex(Telephony.Mms.Part._DATA));
                        if (!TextUtils.isEmpty(displayName) && !TextUtils.isEmpty(path)) {
                            result = new Pair<>(displayName, path);
                        }
                        if (cursor != null) {
                            IoUtils.closeQuietly(cursor);
                        }
                        return result;
                    } catch (Throwable th) {
                        th = th;
                        if (cursor != null) {
                            IoUtils.closeQuietly(cursor);
                        }
                        throw th;
                    }
                } catch (Exception e) {
                    e = e;
                    Log.d(TAG, "getRingtoneDisplayNamePath error " + e.getMessage());
                }
            }
        } catch (Exception e2) {
            e = e2;
        } catch (Throwable th2) {
            th = th2;
            if (cursor != null) {
            }
            throw th;
        }
    }

    private String removeExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int extensionPos = filename.lastIndexOf(46);
        int lastUnixPos = filename.lastIndexOf(47);
        int lastWindowsPos = filename.lastIndexOf(92);
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        if (index == -1) {
            return filename;
        }
        return filename.substring(0, index);
    }

    public void updateRingtoneUriIfNeeded(Context context, int type, String setting) {
        String oldId;
        String newTitle;
        String[] titleArray;
        String newTitle2;
        String newTitle3;
        int i;
        String[] titleArray2;
        deleteIllagalInternalColumns(context, type);
        Uri defRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, type);
        if (defRingtoneUri != null) {
            String ringtoneUri = defRingtoneUri.toString();
            Log.i(TAG, "getActualDefaultRingtoneUri is:" + ringtoneUri);
            if (ringtoneUri.startsWith("content://media/internal")) {
                int indexBegin = ringtoneUri.lastIndexOf("/");
                int indexEnd = ringtoneUri.lastIndexOf("?");
                if (indexBegin == -1 || indexEnd == -1 || indexBegin + 1 >= indexEnd) {
                    oldId = "";
                } else {
                    String oldId2 = ringtoneUri.substring(indexBegin + 1, indexEnd);
                    oldId = oldId2;
                }
                if (!updateRingtoneUriSettings(context, defRingtoneUri, oldId, type, setting)) {
                    String title = defRingtoneUri.getQueryParameter("title");
                    String newTitle4 = null;
                    String label = context.getResources().getString(201589167);
                    String[] titleArray3 = context.getResources().getStringArray(201785405);
                    if (TextUtils.isEmpty(title) || titleArray3.length <= 0 || "P".equals(label) || !this.mTitleMaps.isEmpty()) {
                        newTitle = null;
                        titleArray = titleArray3;
                    } else {
                        int length = titleArray3.length;
                        int i2 = 0;
                        while (i2 < length) {
                            String item = titleArray3[i2];
                            if (TextUtils.isEmpty(item)) {
                                newTitle3 = newTitle4;
                                i = length;
                                titleArray2 = titleArray3;
                            } else {
                                newTitle3 = newTitle4;
                                String[] child = TextUtils.split(item, "/");
                                i = length;
                                if (child.length != 2) {
                                    titleArray2 = titleArray3;
                                } else {
                                    titleArray2 = titleArray3;
                                    this.mTitleMaps.put(child[0], child[1]);
                                }
                            }
                            i2++;
                            titleArray3 = titleArray2;
                            newTitle4 = newTitle3;
                            length = i;
                        }
                        newTitle = newTitle4;
                        titleArray = titleArray3;
                    }
                    if (this.mTitleMaps.isEmpty()) {
                        newTitle2 = newTitle;
                    } else {
                        newTitle2 = this.mTitleMaps.get(title);
                    }
                    if (!TextUtils.isEmpty(newTitle2)) {
                        Uri baseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
                        Uri newUri = baseUri.buildUpon().appendEncodedPath(String.valueOf(oldId)).appendQueryParameter("title", newTitle2).appendQueryParameter("canonical", "1").build();
                        Log.i(TAG, "try modify old title :" + title + " to uri:" + newUri);
                        updateRingtoneUriSettings(context, newUri, oldId, type, setting);
                    }
                }
            }
        }
    }

    public void hookforMediaProviderCustomized(Context context) {
        if (context == null) {
            Log.d(TAG, "hookforMediaProviderCustomized context is Null");
            return;
        }
        String pkgName = context.getPackageName();
        Log.d(TAG, "hookforMediaProviderCustomized pkgName" + pkgName);
        if ("com.android.providers.media.module".equals(pkgName) || "com.google.android.providers.media.module".equals(pkgName)) {
            OifaceBindUtils.bindTask();
            Log.d(TAG, "hookforMediaProviderCustomized curThread" + Thread.currentThread());
            filterBlacklistDirectory();
        }
    }

    private void deleteIllagalInternalColumns(Context context, int type) {
        boolean mtkPlatform;
        int i;
        if (type != 1) {
            return;
        }
        Uri baseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Cursor cursor = null;
        try {
            try {
                mtkPlatform = isMtkPlatform();
                StringBuilder selection = new StringBuilder("is_notification").append(" = ?");
                selection.append(" OR ").append("is_ringtone").append(" = ?");
                if (mtkPlatform) {
                    selection.append(" OR ").append("is_music").append(" = ?").append(" OR ").append("is_alarm").append(" = ?");
                }
                i = 0;
                String[] args = mtkPlatform ? new String[]{String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1)} : new String[]{String.valueOf(1), String.valueOf(1)};
                cursor = context.getContentResolver().query(baseUri, new String[]{"_id", "title", Telephony.Mms.Part._DATA}, selection.toString(), args, null);
            } catch (Exception e) {
                Log.w(TAG, "deleteIllagalInternalColumns ex:" + e);
                if (cursor == null) {
                    return;
                }
            }
            if (cursor == null) {
                if (cursor != null) {
                    cursor.close();
                    return;
                }
                return;
            }
            while (cursor.moveToNext()) {
                long id = cursor.getLong(i);
                cursor.getString(1);
                String path = cursor.getString(2);
                File file = new File(path);
                if (!file.exists() || (mtkPlatform && path != null && path.startsWith("/system/system_ext/media/audio"))) {
                    Uri delUri = context.getContentResolver().canonicalizeOrElse(ContentUris.withAppendedId(baseUri, id));
                    context.getContentResolver().delete(delUri, null);
                }
                i = 0;
            }
            if (cursor == null) {
                return;
            }
            cursor.close();
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private boolean isMtkPlatform() {
        return SystemProperties.get("ro.board.platform", OplusThemeResources.OPLUS_PACKAGE).toLowerCase().startsWith("mt");
    }

    private void filterBlacklistDirectory() {
        File[] microMsgFiles;
        String rootDir;
        String microMsgPath;
        File microMsgFile;
        File[] microMsgFiles2;
        File[] childFiles;
        int i;
        String str;
        String rootDir2 = "";
        try {
            if ("mounted".equalsIgnoreCase(Environment.getExternalStorageState())) {
                rootDir2 = Environment.getExternalStorageDirectory().toString();
            }
        } catch (Exception e) {
        }
        Log.d(TAG, "filterBlacklistDirectory: rootDir = " + rootDir2);
        if (TextUtils.isEmpty(rootDir2)) {
            return;
        }
        String microMsgPath2 = rootDir2 + PATH_MICROMSG;
        File microMsgFile2 = new File(microMsgPath2);
        if (!microMsgFile2.exists() || (microMsgFiles = microMsgFile2.listFiles()) == null) {
            return;
        }
        List<String> blacklists = Arrays.asList(BLACKLIST_DIRECTORY);
        List<String> newBlacklists = Arrays.asList(NEW_BLACKLIST_DIRECTORY);
        int length = microMsgFiles.length;
        int i2 = 0;
        while (i2 < length) {
            File parentFile = microMsgFiles[i2];
            if (!parentFile.isDirectory()) {
                rootDir = rootDir2;
                microMsgPath = microMsgPath2;
                microMsgFile = microMsgFile2;
                microMsgFiles2 = microMsgFiles;
            } else {
                String parentName = parentFile.getName();
                String parentPath = parentFile.getPath();
                rootDir = rootDir2;
                String rootDir3 = "filterBlacklistDirectory: ready to insert file in ";
                if (newBlacklists.contains(parentName)) {
                    Log.d(TAG, "filterBlacklistDirectory: ready to insert file in " + parentName);
                    File parentNomediaFile = new File(parentPath + FILE_NO_MEDIA);
                    if (parentNomediaFile.exists()) {
                        Log.d(TAG, "filterBlacklistDirectory: .nomedia file exists, continue");
                        microMsgPath = microMsgPath2;
                        microMsgFile = microMsgFile2;
                        microMsgFiles2 = microMsgFiles;
                    } else {
                        try {
                            parentNomediaFile.createNewFile();
                            microMsgPath = microMsgPath2;
                            microMsgFile = microMsgFile2;
                            microMsgFiles2 = microMsgFiles;
                        } catch (IOException e2) {
                            Log.w(TAG, "filterBlacklistDirectory: " + e2.getMessage());
                            microMsgPath = microMsgPath2;
                            microMsgFile = microMsgFile2;
                            microMsgFiles2 = microMsgFiles;
                        }
                    }
                } else {
                    microMsgPath = microMsgPath2;
                    File[] childFiles2 = parentFile.listFiles();
                    if (childFiles2 == null) {
                        microMsgFile = microMsgFile2;
                        microMsgFiles2 = microMsgFiles;
                    } else {
                        microMsgFile = microMsgFile2;
                        int length2 = childFiles2.length;
                        microMsgFiles2 = microMsgFiles;
                        int i3 = 0;
                        while (i3 < length2) {
                            File childFile = childFiles2[i3];
                            if (!childFile.isDirectory()) {
                                str = rootDir3;
                                childFiles = childFiles2;
                                i = length2;
                            } else {
                                childFiles = childFiles2;
                                String fileName = childFile.getName();
                                i = length2;
                                String filePath = childFile.getPath();
                                if (!blacklists.contains(fileName)) {
                                    str = rootDir3;
                                } else {
                                    Log.d(TAG, rootDir3 + fileName);
                                    str = rootDir3;
                                    File nomediaFile = new File(filePath + FILE_NO_MEDIA);
                                    if (nomediaFile.exists()) {
                                        Log.d(TAG, "filterBlacklistDirectory: .nomedia file exists, continue");
                                    } else {
                                        try {
                                            nomediaFile.createNewFile();
                                        } catch (IOException e3) {
                                            Log.w(TAG, "filterBlacklistDirectory: " + e3.getMessage());
                                        }
                                    }
                                }
                            }
                            i3++;
                            childFiles2 = childFiles;
                            length2 = i;
                            rootDir3 = str;
                        }
                    }
                }
            }
            i2++;
            rootDir2 = rootDir;
            microMsgPath2 = microMsgPath;
            microMsgFile2 = microMsgFile;
            microMsgFiles = microMsgFiles2;
        }
    }

    public void setOplusDefaultRingtoneUriIfNotSet(Context context, int type, String filename, String whichAudio) {
        String where = "_display_name=? AND " + whichAudio + "=?";
        Uri baseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Uri ringtoneUri = null;
        Cursor cursor = context.getContentResolver().query(baseUri, new String[]{"_id"}, where, new String[]{filename, "1"}, null);
        try {
            if (cursor.moveToFirst()) {
                ringtoneUri = context.getContentResolver().canonicalizeOrElse(ContentUris.withAppendedId(baseUri, cursor.getLong(0)));
            }
            if (cursor != null) {
                cursor.close();
            }
            if (type == 4 && ringtoneUri == null) {
                cursor = context.getContentResolver().query(baseUri, new String[]{"_id"}, "_display_name=? AND is_ringtone=?", new String[]{filename, "1"}, null);
                try {
                    if (cursor.moveToFirst()) {
                        ringtoneUri = context.getContentResolver().canonicalizeOrElse(ContentUris.withAppendedId(baseUri, cursor.getLong(0)));
                        Log.d(TAG, "refind alarm type uri:" + ringtoneUri);
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } finally {
                }
            }
            if (ringtoneUri == null) {
                Log.w(TAG, "do not find ringtone uri!");
                return;
            }
            String defaultUri = getDefaultUriFromSettings(context, type);
            Log.d(TAG, "default uri:" + defaultUri + "uri from database:" + ringtoneUri);
            if (defaultUri.equals(ringtoneUri)) {
                Log.d(TAG, "default uri is the same with new no need to update default settings");
                return;
            }
            switch (type) {
                case 1:
                    Settings.System.putString(context.getContentResolver(), "oplus_customize_default_ringtone", ringtoneUri.toString());
                    return;
                case 2:
                    Settings.System.putString(context.getContentResolver(), "oplus_customize_default_notification", ringtoneUri.toString());
                    return;
                case 4:
                    Settings.System.putString(context.getContentResolver(), "oplus_customize_default_alarm", ringtoneUri.toString());
                    return;
                case 8:
                    Settings.System.putString(context.getContentResolver(), "oplus_customize_default_sms_notification_sound", ringtoneUri.toString());
                    return;
                case 16:
                    Settings.System.putString(context.getContentResolver(), "oplus_customize_default_notification_sim2", ringtoneUri.toString());
                    return;
                case 32:
                    Settings.System.putString(context.getContentResolver(), DEFAULT_CALENDAR_REMINDER_SOUND, ringtoneUri.toString());
                    return;
                case 64:
                    Settings.System.putString(context.getContentResolver(), "oplus_customize_default_ringtone_sim2", ringtoneUri.toString());
                    return;
                default:
                    throw new IllegalArgumentException();
            }
        } finally {
        }
    }

    private boolean isOplusDefaultRingtoneUriEmpty(Context context, int type) {
        switch (type) {
            case 1:
                if (!isNeedSet(context, "oplus_customize_default_ringtone")) {
                    return false;
                }
                return true;
            case 2:
                if (!isNeedSet(context, "oplus_customize_default_notification")) {
                    return false;
                }
                return true;
            case 4:
                if (!isNeedSet(context, "oplus_customize_default_alarm")) {
                    return false;
                }
                return true;
            case 8:
                if (!isNeedSet(context, "oplus_customize_default_sms_notification_sound")) {
                    return false;
                }
                return true;
            case 16:
                if (!isNeedSet(context, "oplus_customize_default_notification_sim2")) {
                    return false;
                }
                return true;
            case 32:
                if (!isNeedSet(context, DEFAULT_CALENDAR_REMINDER_SOUND)) {
                    return false;
                }
                return true;
            case 64:
                if (!isNeedSet(context, "oplus_customize_default_ringtone_sim2")) {
                    return false;
                }
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }

    private String getDefaultUriFromSettings(Context context, int type) {
        String defaultUriFromSettings = "";
        switch (type) {
            case 1:
                defaultUriFromSettings = Settings.System.getString(context.getContentResolver(), "oplus_customize_default_ringtone");
                break;
            case 2:
                defaultUriFromSettings = Settings.System.getString(context.getContentResolver(), "oplus_customize_default_notification");
                break;
            case 4:
                defaultUriFromSettings = Settings.System.getString(context.getContentResolver(), "oplus_customize_default_alarm");
                break;
            case 8:
                defaultUriFromSettings = Settings.System.getString(context.getContentResolver(), "oplus_customize_default_sms_notification_sound");
                break;
            case 16:
                defaultUriFromSettings = Settings.System.getString(context.getContentResolver(), "oplus_customize_default_notification_sim2");
                break;
            case 32:
                defaultUriFromSettings = Settings.System.getString(context.getContentResolver(), DEFAULT_CALENDAR_REMINDER_SOUND);
                break;
            case 64:
                defaultUriFromSettings = Settings.System.getString(context.getContentResolver(), "oplus_customize_default_ringtone_sim2");
                break;
        }
        return defaultUriFromSettings == null ? "" : defaultUriFromSettings;
    }

    private boolean updateRingtoneUriSettings(Context context, Uri defRingtoneUri, String oldId, int type, String setting) {
        boolean update = false;
        Cursor cursor = context.getContentResolver().query(defRingtoneUri, new String[]{"_id"}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    long id = cursor.getLong(0);
                    String newId = String.valueOf(id);
                    if (!oldId.equals("") && !oldId.equals(newId)) {
                        Uri baseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
                        Uri actualRingtoneUri = context.getContentResolver().canonicalizeOrElse(ContentUris.withAppendedId(baseUri, id));
                        Log.i(TAG, "update uri from:" + defRingtoneUri + " to:" + actualRingtoneUri);
                        RingtoneManager.setActualDefaultRingtoneUri(context, type, actualRingtoneUri);
                        Settings.System.putInt(context.getContentResolver(), setting, 1);
                        update = true;
                    }
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return update;
    }
}
