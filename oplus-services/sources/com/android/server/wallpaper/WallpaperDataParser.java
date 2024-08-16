package com.android.server.wallpaper;

import android.R;
import android.app.WallpaperColors;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.FileUtils;
import android.os.SystemProperties;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.JournaledFile;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.pm.PackageManagerService;
import com.android.server.wallpaper.WallpaperDisplayHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WallpaperDataParser {
    private static final boolean DEBUG = false;
    private static final String TAG = "WallpaperDataParser";
    private final Context mContext;
    private final ComponentName mImageWallpaper;
    private final boolean mIsLockscreenLiveWallpaperEnabled = SystemProperties.getBoolean("persist.wm.debug.lockscreen_live_wallpaper", false);
    private final WallpaperCropper mWallpaperCropper;
    private final WallpaperDisplayHelper mWallpaperDisplayHelper;
    private IWallpaperManagerServiceExt mWallpaperManagerServiceExt;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WallpaperDataParser(Context context, WallpaperDisplayHelper wallpaperDisplayHelper, WallpaperCropper wallpaperCropper, IWallpaperManagerServiceExt iWallpaperManagerServiceExt) {
        this.mContext = context;
        this.mWallpaperDisplayHelper = wallpaperDisplayHelper;
        this.mWallpaperCropper = wallpaperCropper;
        this.mImageWallpaper = ComponentName.unflattenFromString(context.getResources().getString(R.string.mediasize_chinese_prc_1));
        this.mWallpaperManagerServiceExt = iWallpaperManagerServiceExt;
    }

    private JournaledFile makeJournaledFile(int i) {
        String absolutePath = new File(this.mWallpaperManagerServiceExt.getWallpaperDir(i, this.mWallpaperManagerServiceExt.getCachePhysicalDisplayId(), WallpaperUtils.getWallpaperDir(i)), "wallpaper_info.xml").getAbsolutePath();
        return new JournaledFile(new File(absolutePath), new File(absolutePath + ".tmp"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class WallpaperLoadingResult {
        private final WallpaperData mLockWallpaperData;
        private final boolean mSuccess;
        private final WallpaperData mSystemWallpaperData;

        private WallpaperLoadingResult(WallpaperData wallpaperData, WallpaperData wallpaperData2, boolean z) {
            this.mSystemWallpaperData = wallpaperData;
            this.mLockWallpaperData = wallpaperData2;
            this.mSuccess = z;
        }

        public WallpaperData getSystemWallpaperData() {
            return this.mSystemWallpaperData;
        }

        public WallpaperData getLockWallpaperData() {
            return this.mLockWallpaperData;
        }

        public boolean success() {
            return this.mSuccess;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:62:0x0272  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x02a0  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x029c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public WallpaperLoadingResult loadSettingsLocked(int i, boolean z, WallpaperData wallpaperData, WallpaperData wallpaperData2, int i2) {
        WallpaperData wallpaperData3;
        WallpaperData wallpaperData4;
        WallpaperData wallpaperData5;
        int i3;
        FileInputStream fileInputStream;
        boolean z2;
        WallpaperData wallpaperData6;
        String str;
        FileInputStream fileInputStream2;
        String str2 = "wp";
        File chooseForRead = makeJournaledFile(i).chooseForRead();
        int cachePhysicalDisplayId = this.mWallpaperManagerServiceExt.getCachePhysicalDisplayId();
        boolean z3 = wallpaperData == null;
        boolean z4 = this.mIsLockscreenLiveWallpaperEnabled;
        boolean z5 = (z4 && (i2 & 1) == 0) ? false : true;
        int i4 = 2;
        boolean z6 = (z4 && (i2 & 2) == 0) ? false : true;
        if (z4) {
            wallpaperData3 = null;
            wallpaperData4 = null;
        } else {
            wallpaperData3 = wallpaperData;
            wallpaperData4 = wallpaperData2;
        }
        if (wallpaperData3 == null && z5) {
            if (z3 && cachePhysicalDisplayId == 1) {
                migrateFromOld();
            }
            wallpaperData3 = new WallpaperData(i, this.mWallpaperManagerServiceExt.getWhichValue(1, cachePhysicalDisplayId));
            wallpaperData3.mWallpaperDataExt.setPhysicalDisplayId(cachePhysicalDisplayId);
            wallpaperData3.allowBackup = true;
            if (!wallpaperData3.cropExists()) {
                if (wallpaperData3.sourceExists()) {
                    this.mWallpaperCropper.generateCrop(wallpaperData3);
                } else {
                    Slog.i(TAG, "No static wallpaper imagery; defaults will be shown");
                }
            }
        }
        WallpaperDisplayHelper.DisplayData displayDataOrCreate = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(0);
        try {
            fileInputStream = new FileInputStream(chooseForRead);
            try {
                TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                while (true) {
                    int next = resolvePullParser.next();
                    if (next == i4) {
                        String name = resolvePullParser.getName();
                        fileInputStream2 = fileInputStream;
                        if (str2.equals(name) && z5) {
                            wallpaperData5 = wallpaperData3;
                        } else {
                            try {
                                if ("kwp".equals(name)) {
                                    wallpaperData5 = wallpaperData3;
                                    if (!this.mIsLockscreenLiveWallpaperEnabled || !z6) {
                                        str = str2;
                                    }
                                } else {
                                    str = str2;
                                    wallpaperData5 = wallpaperData3;
                                }
                                if ("kwp".equals(name) && !this.mIsLockscreenLiveWallpaperEnabled) {
                                    if (wallpaperData4 == null) {
                                        WallpaperData wallpaperData7 = new WallpaperData(i, this.mWallpaperManagerServiceExt.getWhichValue(2, cachePhysicalDisplayId));
                                        try {
                                            wallpaperData7.mWallpaperDataExt.setPhysicalDisplayId(cachePhysicalDisplayId);
                                            wallpaperData4 = wallpaperData7;
                                        } catch (FileNotFoundException unused) {
                                            fileInputStream = fileInputStream2;
                                            wallpaperData4 = wallpaperData7;
                                            i3 = 1;
                                            Slog.w(TAG, "no current wallpaper -- first boot?");
                                            z2 = false;
                                            IoUtils.closeQuietly(fileInputStream);
                                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                            if (z5) {
                                            }
                                            if (z6) {
                                            }
                                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                                        } catch (IOException e) {
                                            e = e;
                                            fileInputStream = fileInputStream2;
                                            wallpaperData4 = wallpaperData7;
                                            i3 = 1;
                                            Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                                            z2 = false;
                                            IoUtils.closeQuietly(fileInputStream);
                                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                            if (z5) {
                                            }
                                            if (z6) {
                                            }
                                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                                        } catch (IndexOutOfBoundsException e2) {
                                            e = e2;
                                            fileInputStream = fileInputStream2;
                                            wallpaperData4 = wallpaperData7;
                                            i3 = 1;
                                            Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                                            z2 = false;
                                            IoUtils.closeQuietly(fileInputStream);
                                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                            if (z5) {
                                            }
                                            if (z6) {
                                            }
                                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                                        } catch (NullPointerException e3) {
                                            e = e3;
                                            fileInputStream = fileInputStream2;
                                            wallpaperData4 = wallpaperData7;
                                            i3 = 1;
                                            Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                                            z2 = false;
                                            IoUtils.closeQuietly(fileInputStream);
                                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                            if (z5) {
                                            }
                                            if (z6) {
                                            }
                                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                                        } catch (NumberFormatException e4) {
                                            e = e4;
                                            fileInputStream = fileInputStream2;
                                            wallpaperData4 = wallpaperData7;
                                            i3 = 1;
                                            Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                                            z2 = false;
                                            IoUtils.closeQuietly(fileInputStream);
                                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                            if (z5) {
                                            }
                                            if (z6) {
                                            }
                                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                                        } catch (XmlPullParserException e5) {
                                            e = e5;
                                            fileInputStream = fileInputStream2;
                                            wallpaperData4 = wallpaperData7;
                                            i3 = 1;
                                            Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                                            z2 = false;
                                            IoUtils.closeQuietly(fileInputStream);
                                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                            if (z5) {
                                            }
                                            if (z6) {
                                            }
                                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                                        }
                                    }
                                    parseWallpaperAttributes(resolvePullParser, wallpaperData4, false);
                                }
                            } catch (FileNotFoundException unused2) {
                                wallpaperData5 = wallpaperData3;
                                fileInputStream = fileInputStream2;
                                i3 = 1;
                                Slog.w(TAG, "no current wallpaper -- first boot?");
                                z2 = false;
                                IoUtils.closeQuietly(fileInputStream);
                                this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                if (z5) {
                                }
                                if (z6) {
                                }
                                return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                            } catch (IOException e6) {
                                e = e6;
                                wallpaperData5 = wallpaperData3;
                                fileInputStream = fileInputStream2;
                                i3 = 1;
                                Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                                z2 = false;
                                IoUtils.closeQuietly(fileInputStream);
                                this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                if (z5) {
                                }
                                if (z6) {
                                }
                                return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                            } catch (IndexOutOfBoundsException e7) {
                                e = e7;
                                wallpaperData5 = wallpaperData3;
                                fileInputStream = fileInputStream2;
                                i3 = 1;
                                Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                                z2 = false;
                                IoUtils.closeQuietly(fileInputStream);
                                this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                if (z5) {
                                }
                                if (z6) {
                                }
                                return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                            } catch (NullPointerException e8) {
                                e = e8;
                                wallpaperData5 = wallpaperData3;
                                fileInputStream = fileInputStream2;
                                i3 = 1;
                                Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                                z2 = false;
                                IoUtils.closeQuietly(fileInputStream);
                                this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                if (z5) {
                                }
                                if (z6) {
                                }
                                return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                            } catch (NumberFormatException e9) {
                                e = e9;
                                wallpaperData5 = wallpaperData3;
                                fileInputStream = fileInputStream2;
                                i3 = 1;
                                Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                                z2 = false;
                                IoUtils.closeQuietly(fileInputStream);
                                this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                if (z5) {
                                }
                                if (z6) {
                                }
                                return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                            } catch (XmlPullParserException e10) {
                                e = e10;
                                wallpaperData5 = wallpaperData3;
                                fileInputStream = fileInputStream2;
                                i3 = 1;
                                Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                                z2 = false;
                                IoUtils.closeQuietly(fileInputStream);
                                this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                                if (z5) {
                                }
                                if (z6) {
                                }
                                return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                            }
                        }
                        try {
                            if ("kwp".equals(name) && wallpaperData4 == null) {
                                wallpaperData4 = new WallpaperData(i, 2);
                            }
                            WallpaperData wallpaperData8 = str2.equals(name) ? wallpaperData5 : wallpaperData4;
                            parseWallpaperAttributes(resolvePullParser, wallpaperData8, z);
                            str = str2;
                            String attributeValue = resolvePullParser.getAttributeValue((String) null, "component");
                            ComponentName unflattenFromString = attributeValue != null ? ComponentName.unflattenFromString(attributeValue) : null;
                            wallpaperData8.nextWallpaperComponent = unflattenFromString;
                            if (unflattenFromString == null || PackageManagerService.PLATFORM_PACKAGE_NAME.equals(unflattenFromString.getPackageName())) {
                                wallpaperData8.nextWallpaperComponent = this.mImageWallpaper;
                            }
                        } catch (FileNotFoundException unused3) {
                            fileInputStream = fileInputStream2;
                            i3 = 1;
                            Slog.w(TAG, "no current wallpaper -- first boot?");
                            z2 = false;
                            IoUtils.closeQuietly(fileInputStream);
                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                            if (z5) {
                            }
                            if (z6) {
                            }
                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                        } catch (IOException e11) {
                            e = e11;
                            fileInputStream = fileInputStream2;
                            i3 = 1;
                            Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                            z2 = false;
                            IoUtils.closeQuietly(fileInputStream);
                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                            if (z5) {
                            }
                            if (z6) {
                            }
                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                        } catch (IndexOutOfBoundsException e12) {
                            e = e12;
                            fileInputStream = fileInputStream2;
                            i3 = 1;
                            Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                            z2 = false;
                            IoUtils.closeQuietly(fileInputStream);
                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                            if (z5) {
                            }
                            if (z6) {
                            }
                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                        } catch (NullPointerException e13) {
                            e = e13;
                            fileInputStream = fileInputStream2;
                            i3 = 1;
                            Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                            z2 = false;
                            IoUtils.closeQuietly(fileInputStream);
                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                            if (z5) {
                            }
                            if (z6) {
                            }
                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                        } catch (NumberFormatException e14) {
                            e = e14;
                            fileInputStream = fileInputStream2;
                            i3 = 1;
                            Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                            z2 = false;
                            IoUtils.closeQuietly(fileInputStream);
                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                            if (z5) {
                            }
                            if (z6) {
                            }
                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                        } catch (XmlPullParserException e15) {
                            e = e15;
                            fileInputStream = fileInputStream2;
                            i3 = 1;
                            Slog.w(TAG, "failed parsing " + chooseForRead + " " + e);
                            z2 = false;
                            IoUtils.closeQuietly(fileInputStream);
                            this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
                            if (z5) {
                            }
                            if (z6) {
                            }
                            return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
                        }
                    } else {
                        str = str2;
                        fileInputStream2 = fileInputStream;
                        wallpaperData5 = wallpaperData3;
                    }
                    i3 = 1;
                    fileInputStream = fileInputStream2;
                    if (next == 1) {
                        break;
                    }
                    wallpaperData3 = wallpaperData5;
                    str2 = str;
                    i4 = 2;
                }
                z2 = true;
            } catch (FileNotFoundException unused4) {
                wallpaperData5 = wallpaperData3;
            } catch (IOException e16) {
                e = e16;
                wallpaperData5 = wallpaperData3;
            } catch (IndexOutOfBoundsException e17) {
                e = e17;
                wallpaperData5 = wallpaperData3;
            } catch (NullPointerException e18) {
                e = e18;
                wallpaperData5 = wallpaperData3;
            } catch (NumberFormatException e19) {
                e = e19;
                wallpaperData5 = wallpaperData3;
            } catch (XmlPullParserException e20) {
                e = e20;
                wallpaperData5 = wallpaperData3;
            }
        } catch (FileNotFoundException unused5) {
            wallpaperData5 = wallpaperData3;
            i3 = 1;
            fileInputStream = null;
        } catch (IOException e21) {
            e = e21;
            wallpaperData5 = wallpaperData3;
            i3 = 1;
            fileInputStream = null;
        } catch (IndexOutOfBoundsException e22) {
            e = e22;
            wallpaperData5 = wallpaperData3;
            i3 = 1;
            fileInputStream = null;
        } catch (NullPointerException e23) {
            e = e23;
            wallpaperData5 = wallpaperData3;
            i3 = 1;
            fileInputStream = null;
        } catch (NumberFormatException e24) {
            e = e24;
            wallpaperData5 = wallpaperData3;
            i3 = 1;
            fileInputStream = null;
        } catch (XmlPullParserException e25) {
            e = e25;
            wallpaperData5 = wallpaperData3;
            i3 = 1;
            fileInputStream = null;
        }
        IoUtils.closeQuietly(fileInputStream);
        this.mWallpaperDisplayHelper.ensureSaneWallpaperDisplaySize(displayDataOrCreate, 0);
        if (z5) {
            if (!z2) {
                wallpaperData6 = wallpaperData5;
                wallpaperData6.cropHint.set(0, 0, 0, 0);
                displayDataOrCreate.mPadding.set(0, 0, 0, 0);
                wallpaperData6.name = "";
            } else {
                wallpaperData6 = wallpaperData5;
                if (wallpaperData6.wallpaperId <= 0) {
                    wallpaperData6.wallpaperId = WallpaperUtils.makeWallpaperIdLocked();
                }
            }
            ensureSaneWallpaperData(wallpaperData6);
            wallpaperData6.mWhich = wallpaperData4 != null ? i3 : 3;
        } else {
            wallpaperData6 = wallpaperData5;
        }
        if (z6) {
            WallpaperData wallpaperData9 = !z2 ? null : wallpaperData4;
            if (wallpaperData9 != null) {
                ensureSaneWallpaperData(wallpaperData9);
                wallpaperData9.mWhich = 2;
            }
            wallpaperData4 = wallpaperData9;
        }
        return new WallpaperLoadingResult(wallpaperData6, wallpaperData4, z2);
    }

    private void ensureSaneWallpaperData(WallpaperData wallpaperData) {
        if (wallpaperData.cropHint.width() < 0 || wallpaperData.cropHint.height() < 0) {
            wallpaperData.cropHint.set(0, 0, 0, 0);
        }
    }

    private void migrateFromOld() {
        File file = new File(WallpaperUtils.getWallpaperDir(0), "wallpaper");
        File file2 = new File("/data/data/com.android.settings/files/wallpaper");
        File file3 = new File(WallpaperUtils.getWallpaperDir(0), "wallpaper_orig");
        if (file.exists()) {
            if (file3.exists()) {
                return;
            }
            FileUtils.copyFile(file, file3);
        } else if (file2.exists()) {
            File file4 = new File("/data/system/wallpaper_info.xml");
            if (file4.exists()) {
                file4.renameTo(new File(WallpaperUtils.getWallpaperDir(0), "wallpaper_info.xml"));
            }
            FileUtils.copyFile(file2, file);
            file2.renameTo(file3);
        }
    }

    @VisibleForTesting
    void parseWallpaperAttributes(TypedXmlPullParser typedXmlPullParser, WallpaperData wallpaperData, boolean z) throws XmlPullParserException {
        int attributeInt = typedXmlPullParser.getAttributeInt((String) null, "id", -1);
        if (attributeInt != -1) {
            wallpaperData.wallpaperId = attributeInt;
            if (attributeInt > WallpaperUtils.getCurrentWallpaperId()) {
                WallpaperUtils.setCurrentWallpaperId(attributeInt);
            }
        } else {
            wallpaperData.wallpaperId = WallpaperUtils.makeWallpaperIdLocked();
        }
        WallpaperDisplayHelper.DisplayData displayDataOrCreate = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(0);
        if (!z) {
            displayDataOrCreate.mWidth = typedXmlPullParser.getAttributeInt((String) null, "width");
            displayDataOrCreate.mHeight = typedXmlPullParser.getAttributeInt((String) null, "height");
        }
        wallpaperData.cropHint.left = getAttributeInt(typedXmlPullParser, "cropLeft", 0);
        wallpaperData.cropHint.top = getAttributeInt(typedXmlPullParser, "cropTop", 0);
        wallpaperData.cropHint.right = getAttributeInt(typedXmlPullParser, "cropRight", 0);
        wallpaperData.cropHint.bottom = getAttributeInt(typedXmlPullParser, "cropBottom", 0);
        displayDataOrCreate.mPadding.left = getAttributeInt(typedXmlPullParser, "paddingLeft", 0);
        displayDataOrCreate.mPadding.top = getAttributeInt(typedXmlPullParser, "paddingTop", 0);
        displayDataOrCreate.mPadding.right = getAttributeInt(typedXmlPullParser, "paddingRight", 0);
        displayDataOrCreate.mPadding.bottom = getAttributeInt(typedXmlPullParser, "paddingBottom", 0);
        wallpaperData.mWallpaperDimAmount = getAttributeFloat(typedXmlPullParser, "dimAmount", 0.0f);
        int attributeInt2 = getAttributeInt(typedXmlPullParser, "dimAmountsCount", 0);
        if (attributeInt2 > 0) {
            SparseArray<Float> sparseArray = new SparseArray<>(attributeInt2);
            for (int i = 0; i < attributeInt2; i++) {
                sparseArray.put(getAttributeInt(typedXmlPullParser, "dimUID" + i, 0), Float.valueOf(getAttributeFloat(typedXmlPullParser, "dimValue" + i, 0.0f)));
            }
            wallpaperData.mUidToDimAmount = sparseArray;
        }
        int attributeInt3 = getAttributeInt(typedXmlPullParser, "colorsCount", 0);
        int attributeInt4 = getAttributeInt(typedXmlPullParser, "allColorsCount", 0);
        if (attributeInt4 > 0) {
            HashMap hashMap = new HashMap(attributeInt4);
            for (int i2 = 0; i2 < attributeInt4; i2++) {
                hashMap.put(Integer.valueOf(getAttributeInt(typedXmlPullParser, "allColorsValue" + i2, 0)), Integer.valueOf(getAttributeInt(typedXmlPullParser, "allColorsPopulation" + i2, 0)));
            }
            wallpaperData.primaryColors = new WallpaperColors(hashMap, getAttributeInt(typedXmlPullParser, "colorHints", 0));
        } else if (attributeInt3 > 0) {
            Color color = null;
            Color color2 = null;
            Color color3 = null;
            for (int i3 = 0; i3 < attributeInt3; i3++) {
                Color valueOf = Color.valueOf(getAttributeInt(typedXmlPullParser, "colorValue" + i3, 0));
                if (i3 == 0) {
                    color = valueOf;
                } else if (i3 == 1) {
                    color2 = valueOf;
                } else if (i3 != 2) {
                    break;
                } else {
                    color3 = valueOf;
                }
            }
            wallpaperData.primaryColors = new WallpaperColors(color, color2, color3, getAttributeInt(typedXmlPullParser, "colorHints", 0));
        }
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, "name");
        wallpaperData.name = attributeValue;
        if (attributeValue == null) {
            wallpaperData.name = "";
        }
        wallpaperData.allowBackup = typedXmlPullParser.getAttributeBoolean((String) null, "backup", false);
    }

    private int getAttributeInt(TypedXmlPullParser typedXmlPullParser, String str, int i) {
        return typedXmlPullParser.getAttributeInt((String) null, str, i);
    }

    private float getAttributeFloat(TypedXmlPullParser typedXmlPullParser, String str, float f) {
        return typedXmlPullParser.getAttributeFloat((String) null, str, f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveSettingsLocked(int i, WallpaperData wallpaperData, WallpaperData wallpaperData2) {
        FileOutputStream fileOutputStream;
        JournaledFile makeJournaledFile = makeJournaledFile(i);
        FileOutputStream fileOutputStream2 = null;
        try {
            fileOutputStream = new FileOutputStream(makeJournaledFile.chooseForWrite(), false);
        } catch (IOException unused) {
        }
        try {
            TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(fileOutputStream);
            resolveSerializer.startDocument((String) null, Boolean.TRUE);
            if (wallpaperData != null) {
                writeWallpaperAttributes(resolveSerializer, "wp", wallpaperData);
            }
            if (wallpaperData2 != null) {
                writeWallpaperAttributes(resolveSerializer, "kwp", wallpaperData2);
            }
            resolveSerializer.endDocument();
            fileOutputStream.flush();
            FileUtils.sync(fileOutputStream);
            fileOutputStream.close();
            makeJournaledFile.commit();
        } catch (IOException unused2) {
            fileOutputStream2 = fileOutputStream;
            IoUtils.closeQuietly(fileOutputStream2);
            makeJournaledFile.rollback();
        }
    }

    @VisibleForTesting
    void writeWallpaperAttributes(TypedXmlSerializer typedXmlSerializer, String str, WallpaperData wallpaperData) throws IllegalArgumentException, IllegalStateException, IOException {
        int i = 0;
        WallpaperDisplayHelper.DisplayData displayDataOrCreate = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(0);
        typedXmlSerializer.startTag((String) null, str);
        typedXmlSerializer.attributeInt((String) null, "id", wallpaperData.wallpaperId);
        typedXmlSerializer.attributeInt((String) null, "width", displayDataOrCreate.mWidth);
        typedXmlSerializer.attributeInt((String) null, "height", displayDataOrCreate.mHeight);
        typedXmlSerializer.attributeInt((String) null, "cropLeft", wallpaperData.cropHint.left);
        typedXmlSerializer.attributeInt((String) null, "cropTop", wallpaperData.cropHint.top);
        typedXmlSerializer.attributeInt((String) null, "cropRight", wallpaperData.cropHint.right);
        typedXmlSerializer.attributeInt((String) null, "cropBottom", wallpaperData.cropHint.bottom);
        int i2 = displayDataOrCreate.mPadding.left;
        if (i2 != 0) {
            typedXmlSerializer.attributeInt((String) null, "paddingLeft", i2);
        }
        int i3 = displayDataOrCreate.mPadding.top;
        if (i3 != 0) {
            typedXmlSerializer.attributeInt((String) null, "paddingTop", i3);
        }
        int i4 = displayDataOrCreate.mPadding.right;
        if (i4 != 0) {
            typedXmlSerializer.attributeInt((String) null, "paddingRight", i4);
        }
        int i5 = displayDataOrCreate.mPadding.bottom;
        if (i5 != 0) {
            typedXmlSerializer.attributeInt((String) null, "paddingBottom", i5);
        }
        typedXmlSerializer.attributeFloat((String) null, "dimAmount", wallpaperData.mWallpaperDimAmount);
        int size = wallpaperData.mUidToDimAmount.size();
        typedXmlSerializer.attributeInt((String) null, "dimAmountsCount", size);
        if (size > 0) {
            int i6 = 0;
            for (int i7 = 0; i7 < wallpaperData.mUidToDimAmount.size(); i7++) {
                typedXmlSerializer.attributeInt((String) null, "dimUID" + i6, wallpaperData.mUidToDimAmount.keyAt(i7));
                typedXmlSerializer.attributeFloat((String) null, "dimValue" + i6, wallpaperData.mUidToDimAmount.valueAt(i7).floatValue());
                i6++;
            }
        }
        WallpaperColors wallpaperColors = wallpaperData.primaryColors;
        if (wallpaperColors != null) {
            int size2 = wallpaperColors.getMainColors().size();
            typedXmlSerializer.attributeInt((String) null, "colorsCount", size2);
            if (size2 > 0) {
                for (int i8 = 0; i8 < size2; i8++) {
                    typedXmlSerializer.attributeInt((String) null, "colorValue" + i8, ((Color) wallpaperData.primaryColors.getMainColors().get(i8)).toArgb());
                }
            }
            int size3 = wallpaperData.primaryColors.getAllColors().size();
            typedXmlSerializer.attributeInt((String) null, "allColorsCount", size3);
            if (size3 > 0) {
                for (Map.Entry entry : wallpaperData.primaryColors.getAllColors().entrySet()) {
                    typedXmlSerializer.attributeInt((String) null, "allColorsValue" + i, ((Integer) entry.getKey()).intValue());
                    typedXmlSerializer.attributeInt((String) null, "allColorsPopulation" + i, ((Integer) entry.getValue()).intValue());
                    i++;
                }
            }
            typedXmlSerializer.attributeInt((String) null, "colorHints", wallpaperData.primaryColors.getColorHints());
        }
        typedXmlSerializer.attribute((String) null, "name", wallpaperData.name);
        ComponentName componentName = wallpaperData.wallpaperComponent;
        if (componentName != null && !componentName.equals(this.mImageWallpaper)) {
            typedXmlSerializer.attribute((String) null, "component", wallpaperData.wallpaperComponent.flattenToShortString());
        }
        if (wallpaperData.allowBackup) {
            typedXmlSerializer.attributeBoolean((String) null, "backup", true);
        }
        typedXmlSerializer.endTag((String) null, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x017f, code lost:
    
        if (r3 != 0) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0130, code lost:
    
        libcore.io.IoUtils.closeQuietly((java.lang.AutoCloseable) r2);
        libcore.io.IoUtils.closeQuietly((java.lang.AutoCloseable) r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x012d, code lost:
    
        android.os.FileUtils.sync(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0157, code lost:
    
        if (r3 != 0) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x012b, code lost:
    
        if (r3 != 0) goto L73;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v18, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v10, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r2v11, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v17 */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v22, types: [java.io.FileOutputStream, java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r2v25 */
    /* JADX WARN: Type inference failed for: r2v26 */
    /* JADX WARN: Type inference failed for: r2v27 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.io.FileOutputStream, java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r2v8, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r2v9 */
    /* JADX WARN: Type inference failed for: r3v10, types: [java.io.FileOutputStream, java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r3v14, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v17 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v19 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v21 */
    /* JADX WARN: Type inference failed for: r3v22 */
    /* JADX WARN: Type inference failed for: r3v23 */
    /* JADX WARN: Type inference failed for: r3v25 */
    /* JADX WARN: Type inference failed for: r3v26 */
    /* JADX WARN: Type inference failed for: r3v27 */
    /* JADX WARN: Type inference failed for: r3v28 */
    /* JADX WARN: Type inference failed for: r3v29, types: [java.io.FileOutputStream, java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v32 */
    /* JADX WARN: Type inference failed for: r3v33 */
    /* JADX WARN: Type inference failed for: r3v34 */
    /* JADX WARN: Type inference failed for: r3v35 */
    /* JADX WARN: Type inference failed for: r3v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean restoreNamedResourceLocked(WallpaperData wallpaperData) {
        ?? r2 = 4;
        if (wallpaperData.name.length() > 4 && "res:".equals(wallpaperData.name.substring(0, 4))) {
            String substring = wallpaperData.name.substring(4);
            int indexOf = substring.indexOf(58);
            InputStream inputStream = null;
            String substring2 = indexOf > 0 ? substring.substring(0, indexOf) : null;
            int lastIndexOf = substring.lastIndexOf(47);
            String substring3 = lastIndexOf > 0 ? substring.substring(lastIndexOf + 1) : null;
            ?? substring4 = (indexOf <= 0 || lastIndexOf <= 0 || lastIndexOf - indexOf <= 1) ? 0 : substring.substring(indexOf + 1, lastIndexOf);
            if (substring2 != null && substring3 != null && substring4 != 0) {
                int i = -1;
                try {
                    try {
                        Resources resources = this.mContext.createPackageContext(substring2, 4).getResources();
                        i = resources.getIdentifier(substring, null, null);
                        if (i == 0) {
                            Slog.e(TAG, "couldn't resolve identifier pkg=" + substring2 + " type=" + substring4 + " ident=" + substring3);
                            IoUtils.closeQuietly((AutoCloseable) null);
                            IoUtils.closeQuietly((AutoCloseable) null);
                            IoUtils.closeQuietly((AutoCloseable) null);
                            return false;
                        }
                        InputStream openRawResource = resources.openRawResource(i);
                        try {
                            if (wallpaperData.wallpaperFile.exists()) {
                                wallpaperData.wallpaperFile.delete();
                                wallpaperData.cropFile.delete();
                            }
                            r2 = new FileOutputStream(wallpaperData.wallpaperFile);
                            try {
                                substring4 = new FileOutputStream(wallpaperData.cropFile);
                                try {
                                    byte[] bArr = new byte[32768];
                                    while (true) {
                                        int read = openRawResource.read(bArr);
                                        if (read <= 0) {
                                            Slog.v(TAG, "Restored wallpaper: " + substring);
                                            IoUtils.closeQuietly(openRawResource);
                                            FileUtils.sync(r2);
                                            FileUtils.sync(substring4);
                                            IoUtils.closeQuietly((AutoCloseable) r2);
                                            IoUtils.closeQuietly((AutoCloseable) substring4);
                                            return true;
                                        }
                                        r2.write(bArr, 0, read);
                                        substring4.write(bArr, 0, read);
                                    }
                                } catch (PackageManager.NameNotFoundException unused) {
                                    inputStream = openRawResource;
                                    r2 = r2;
                                    substring4 = substring4;
                                    Slog.e(TAG, "Package name " + substring2 + " not found");
                                    IoUtils.closeQuietly(inputStream);
                                    if (r2 != 0) {
                                        FileUtils.sync(r2);
                                    }
                                } catch (Resources.NotFoundException unused2) {
                                    inputStream = openRawResource;
                                    r2 = r2;
                                    substring4 = substring4;
                                    Slog.e(TAG, "Resource not found: " + i);
                                    IoUtils.closeQuietly(inputStream);
                                    if (r2 != 0) {
                                        FileUtils.sync(r2);
                                    }
                                } catch (IOException e) {
                                    e = e;
                                    inputStream = openRawResource;
                                    e = e;
                                    r2 = r2;
                                    substring4 = substring4;
                                    Slog.e(TAG, "IOException while restoring wallpaper ", e);
                                    IoUtils.closeQuietly(inputStream);
                                    if (r2 != 0) {
                                        FileUtils.sync(r2);
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    inputStream = openRawResource;
                                    th = th;
                                    IoUtils.closeQuietly(inputStream);
                                    if (r2 != 0) {
                                        FileUtils.sync(r2);
                                    }
                                    if (substring4 != 0) {
                                        FileUtils.sync(substring4);
                                    }
                                    IoUtils.closeQuietly((AutoCloseable) r2);
                                    IoUtils.closeQuietly((AutoCloseable) substring4);
                                    throw th;
                                }
                            } catch (PackageManager.NameNotFoundException unused3) {
                                substring4 = 0;
                            } catch (Resources.NotFoundException unused4) {
                                substring4 = 0;
                            } catch (IOException e2) {
                                e = e2;
                                substring4 = 0;
                            } catch (Throwable th2) {
                                th = th2;
                                substring4 = 0;
                            }
                        } catch (PackageManager.NameNotFoundException unused5) {
                            r2 = 0;
                            substring4 = 0;
                        } catch (Resources.NotFoundException unused6) {
                            r2 = 0;
                            substring4 = 0;
                        } catch (IOException e3) {
                            e = e3;
                            r2 = 0;
                            substring4 = 0;
                        } catch (Throwable th3) {
                            th = th3;
                            r2 = 0;
                            substring4 = 0;
                        }
                    } catch (PackageManager.NameNotFoundException unused7) {
                        r2 = 0;
                        substring4 = 0;
                    } catch (Resources.NotFoundException unused8) {
                        r2 = 0;
                        substring4 = 0;
                    } catch (IOException e4) {
                        e = e4;
                        r2 = 0;
                        substring4 = 0;
                    } catch (Throwable th4) {
                        th = th4;
                        r2 = 0;
                        substring4 = 0;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }
        }
        return false;
    }
}
