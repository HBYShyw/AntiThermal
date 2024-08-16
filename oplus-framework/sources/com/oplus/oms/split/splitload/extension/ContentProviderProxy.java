package com.oplus.oms.split.splitload.extension;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import com.oplus.oms.split.common.SplitLog;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class ContentProviderProxy extends ContentProvider {
    private static final String NAME_INFIX = "_Decorated_";
    private static final String TAG = "ContentProviderProxy";
    private ProviderInfo mProviderInfo;
    private ContentProvider mRealContentProvider;
    private String mRealContentProviderClassName;
    private String mSplitName;

    protected abstract boolean checkRealContentProviderInstallStatus(String str);

    /* JADX INFO: Access modifiers changed from: protected */
    public ContentProvider getRealContentProvider() {
        return this.mRealContentProvider;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRealContentProvider() {
        this.mRealContentProvider = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createAndActivateRealContentProvider(ClassLoader classLoader) throws AABExtensionException {
        if (this.mRealContentProviderClassName == null) {
            throw new AABExtensionException("Unable to read real content-provider for " + getClass().getName());
        }
        if (classLoader == null) {
            throw new AABExtensionException("classloader is null");
        }
        SplitLog.d(TAG, "realContentProviderClassName " + this.mRealContentProviderClassName, new Object[0]);
        Throwable error = null;
        try {
            Class<?> clazz = classLoader.loadClass(this.mRealContentProviderClassName);
            ContentProvider contentProvider = (ContentProvider) clazz.getDeclaredConstructor(null).newInstance(new Object[0]);
            this.mRealContentProvider = contentProvider;
            contentProvider.attachInfo(getContext(), this.mProviderInfo);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            error = e;
        }
        if (error != null) {
            throw new AABExtensionException(error);
        }
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public void attachInfo(Context context, ProviderInfo info) {
        String className = getClass().getName();
        String[] cuts = className.split(NAME_INFIX);
        this.mRealContentProviderClassName = cuts[0];
        this.mSplitName = cuts[1];
        super.attachInfo(context, info);
        this.mProviderInfo = new ProviderInfo(info);
        AABExtension.getInstance().put(this.mSplitName, this);
    }

    @Override // android.content.ContentProvider, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            this.mRealContentProvider.onConfigurationChanged(newConfig);
        }
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, Bundle queryArgs, CancellationSignal cancellationSignal) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.query(uri, projection, queryArgs, cancellationSignal);
        }
        return super.query(uri, projection, queryArgs, cancellationSignal);
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal);
        }
        return super.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal);
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.getType(uri);
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.applyBatch(operations);
        }
        return super.applyBatch(operations);
    }

    @Override // android.content.ContentProvider
    public Uri canonicalize(Uri url) {
        if (getRealContentProvider() != null) {
            return this.mRealContentProvider.canonicalize(url);
        }
        return super.canonicalize(url);
    }

    @Override // android.content.ContentProvider
    public Uri uncanonicalize(Uri url) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.uncanonicalize(url);
        }
        return super.uncanonicalize(url);
    }

    @Override // android.content.ContentProvider
    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.openAssetFile(uri, mode);
        }
        return super.openAssetFile(uri, mode);
    }

    @Override // android.content.ContentProvider
    public AssetFileDescriptor openAssetFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.openAssetFile(uri, mode, signal);
        }
        return super.openAssetFile(uri, mode, signal);
    }

    @Override // android.content.ContentProvider
    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts) throws FileNotFoundException {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.openTypedAssetFile(uri, mimeTypeFilter, opts);
        }
        return super.openTypedAssetFile(uri, mimeTypeFilter, opts);
    }

    @Override // android.content.ContentProvider
    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts, CancellationSignal signal) throws FileNotFoundException {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.openTypedAssetFile(uri, mimeTypeFilter, opts, signal);
        }
        return super.openTypedAssetFile(uri, mimeTypeFilter, opts, signal);
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.openFile(uri, mode);
        }
        return super.openFile(uri, mode);
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.openFile(uri, mode, signal);
        }
        return super.openFile(uri, mode, signal);
    }

    @Override // android.content.ContentProvider
    public <T> ParcelFileDescriptor openPipeHelper(Uri uri, String mimeType, Bundle opts, T args, ContentProvider.PipeDataWriter<T> func) throws FileNotFoundException {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.openPipeHelper(uri, mimeType, opts, args, func);
        }
        return super.openPipeHelper(uri, mimeType, opts, args, func);
    }

    @Override // android.content.ContentProvider
    public boolean refresh(Uri uri, Bundle args, CancellationSignal cancellationSignal) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.refresh(uri, args, cancellationSignal);
        }
        return super.refresh(uri, args, cancellationSignal);
    }

    @Override // android.content.ContentProvider
    public Bundle call(String method, String arg, Bundle extras) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.call(method, arg, extras);
        }
        return super.call(method, arg, extras);
    }

    @Override // android.content.ContentProvider
    public String[] getStreamTypes(Uri uri, String mimeTypeFilter) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.getStreamTypes(uri, mimeTypeFilter);
        }
        return super.getStreamTypes(uri, mimeTypeFilter);
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.insert(uri, values);
        }
        return null;
    }

    @Override // android.content.ContentProvider, android.content.ComponentCallbacks2
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        ContentProvider contentProvider = this.mRealContentProvider;
        if (contentProvider != null) {
            contentProvider.onTrimMemory(level);
        }
    }

    @Override // android.content.ContentProvider, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        ContentProvider contentProvider = this.mRealContentProvider;
        if (contentProvider != null) {
            contentProvider.onLowMemory();
        }
    }

    @Override // android.content.ContentProvider
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.bulkInsert(uri, values);
        }
        return super.bulkInsert(uri, values);
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.delete(uri, selection, selectionArgs);
        }
        return 0;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (checkRealContentProviderInstallStatus(this.mSplitName)) {
            return this.mRealContentProvider.update(uri, values, selection, selectionArgs);
        }
        return 0;
    }
}
