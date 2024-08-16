package com.oplus.resolver;

import android.common.OplusFeatureCache;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.IOplusThemeManager;
import android.content.res.IUxIconPackageManagerExt;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.oplus.Telephony;
import android.telephony.OplusTelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public class OplusResolverApkPreView {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final File DEVICE_ROOT = new File("/");
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG = "OplusResolverApkPreView";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    private Context mContext;
    private StagingAsyncTask mStagingAsyncTask;

    public OplusResolverApkPreView(Context context) {
        this.mContext = context;
    }

    public void execute(Uri uri, ImageView imageView, TextView textView) {
        textView.setVisibility(4);
        imageView.setVisibility(4);
        StagingAsyncTask stagingAsyncTask = new StagingAsyncTask(imageView, textView);
        this.mStagingAsyncTask = stagingAsyncTask;
        stagingAsyncTask.execute(uri);
    }

    public void onDestroy() {
        StagingAsyncTask stagingAsyncTask = this.mStagingAsyncTask;
        if (stagingAsyncTask != null) {
            stagingAsyncTask.cancel(true);
        }
    }

    /* loaded from: classes.dex */
    private final class StagingAsyncTask extends AsyncTask<Uri, Void, String> {
        private ImageView mImageView;
        private TextView mTextView;

        StagingAsyncTask(ImageView imageView, TextView textView) {
            this.mImageView = imageView;
            this.mTextView = textView;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(Uri... params) {
            if (params == null || params.length <= 0) {
                return null;
            }
            Uri packageUri = params[0];
            return getRealFilePath(OplusResolverApkPreView.this.mContext, packageUri);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String path) {
            ApplicationInfo applicationInfo;
            IUxIconPackageManagerExt uxIconPackageManagerExt;
            if (path == null) {
                this.mImageView.setImageResource(201850979);
            } else {
                PackageManager packageManager = OplusResolverApkPreView.this.mContext.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, 1);
                if (packageInfo == null) {
                    this.mImageView.setImageResource(201850979);
                } else {
                    try {
                        applicationInfo = packageManager.getApplicationInfo(packageInfo.packageName, 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.d(OplusResolverApkPreView.TAG, "StagingAsyncTask: onPostExecute: " + e.getMessage());
                        applicationInfo = packageInfo.applicationInfo;
                        applicationInfo.publicSourceDir = path;
                        applicationInfo.sourceDir = path;
                        applicationInfo.dataDir = path;
                    }
                    CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
                    IUxIconPackageManagerExt uxIconPackageManagerExt2 = (IUxIconPackageManagerExt) packageManager.mPackageManagerExt.getUxIconPackageManagerExt();
                    if (uxIconPackageManagerExt2 != null) {
                        uxIconPackageManagerExt = uxIconPackageManagerExt2;
                    } else {
                        uxIconPackageManagerExt = IUxIconPackageManagerExt.DEFAULT;
                    }
                    Drawable drawable = ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).loadOverlayResolverDrawable(uxIconPackageManagerExt, packageInfo.packageName, applicationInfo.icon, applicationInfo, null);
                    if (drawable == null) {
                        drawable = OplusResolverApkPreView.this.mContext.getDrawable(201850979);
                    }
                    this.mImageView.setImageDrawable(drawable);
                    this.mTextView.setText(applicationLabel);
                }
            }
            if (OplusResolverUtils.invokeMethod(this.mImageView.getContext(), "getChooserWrapper", new Object[0]) != null) {
                int radius = this.mImageView.getContext().getResources().getDimensionPixelOffset(201654476);
                List<Class> paramCls = new ArrayList<>();
                paramCls.add(Integer.TYPE);
                OplusResolverUtils.invokeMethod(this.mImageView, "setRadius", paramCls, Integer.valueOf(radius));
            }
            this.mImageView.setVisibility(0);
            this.mTextView.setVisibility(0);
        }

        public String getRealFilePath(Context context, Uri uri) {
            int index;
            if (uri == null) {
                return null;
            }
            String scheme = uri.getScheme();
            String data = null;
            if (scheme == null) {
                data = uri.getPath();
            } else if ("file".equals(scheme)) {
                data = uri.getPath();
            } else if (OplusTelephonyManager.BUNDLE_CONTENT.equals(scheme)) {
                try {
                    Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst() && (index = cursor.getColumnIndex(Telephony.Mms.Part._DATA)) > -1) {
                            data = cursor.getString(index);
                        }
                        cursor.close();
                    }
                } catch (Exception e) {
                    Log.d(OplusResolverApkPreView.TAG, "getRealFilePath error:" + e.getMessage());
                    data = OplusResolverApkPreView.this.getFPUriToPath(context, uri);
                }
            }
            Log.d(OplusResolverApkPreView.TAG, "get path:" + data);
            return data;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getFPUriToPath(Context context, Uri uri) {
        String authority = uri.getAuthority();
        if (authority == null) {
            return null;
        }
        try {
            Pair<String, File> pair = parsePathStrategy(context, authority);
            File file = getFileForUri(uri, pair);
            if (file != null) {
                return file.getAbsolutePath();
            }
        } catch (Exception e) {
        }
        return null;
    }

    private Pair<String, File> parsePathStrategy(Context context, String authority) throws IOException, XmlPullParserException {
        ProviderInfo info = context.getPackageManager().resolveContentProvider(authority, 128);
        if (info == null) {
            throw new IllegalArgumentException("Couldn't find meta-data for provider with authority " + authority);
        }
        XmlResourceParser in = info.loadXmlMetaData(context.getPackageManager(), META_DATA_FILE_PROVIDER_PATHS);
        if (in == null) {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        }
        while (true) {
            int type = in.next();
            if (type == 1) {
                return null;
            }
            if (type == 2) {
                String tag = in.getName();
                String name = in.getAttributeValue(null, "name");
                String path = in.getAttributeValue(null, ATTR_PATH);
                File target = null;
                if (TAG_ROOT_PATH.equals(tag)) {
                    target = DEVICE_ROOT;
                } else if (TAG_FILES_PATH.equals(tag)) {
                    target = context.getFilesDir();
                } else if (TAG_CACHE_PATH.equals(tag)) {
                    target = context.getCacheDir();
                } else if (TAG_EXTERNAL.equals(tag)) {
                    target = Environment.getExternalStorageDirectory();
                } else if (TAG_EXTERNAL_FILES.equals(tag)) {
                    File[] externalFilesDirs = context.getExternalFilesDirs(null);
                    if (externalFilesDirs.length > 0) {
                        target = externalFilesDirs[0];
                    }
                } else if (TAG_EXTERNAL_CACHE.equals(tag)) {
                    File[] externalCacheDirs = context.getExternalCacheDirs();
                    if (externalCacheDirs.length > 0) {
                        target = externalCacheDirs[0];
                    }
                } else if (TAG_EXTERNAL_MEDIA.equals(tag)) {
                    File[] externalMediaDirs = context.getExternalMediaDirs();
                    if (externalMediaDirs.length > 0) {
                        target = externalMediaDirs[0];
                    }
                }
                if (target != null) {
                    return new Pair<>(name, new File(target, path));
                }
            }
        }
    }

    public File getFileForUri(Uri uri, Pair<String, File> roots) {
        String path = uri.getEncodedPath();
        if (roots == null || path == null) {
            return null;
        }
        int splitIndex = path.indexOf(47, 1);
        String tag = Uri.decode(path.substring(1, splitIndex));
        String path2 = Uri.decode(path.substring(splitIndex + 1));
        if (!tag.equals(roots.first)) {
            throw new IllegalArgumentException("Unable to find configured root for " + uri);
        }
        File root = (File) roots.second;
        File file = new File(root, path2);
        try {
            File file2 = file.getCanonicalFile();
            if (file2.getPath().startsWith(root.getPath())) {
                return file2;
            }
            throw new SecurityException("Resolved path jumped beyond configured root");
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
        }
    }
}
