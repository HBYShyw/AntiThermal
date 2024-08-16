package com.cdo.oaps.api.host;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import g1.SimpleStringConverter;
import h1.IHostCallback;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class CallbackProvider extends ContentProvider {

    /* renamed from: e, reason: collision with root package name */
    private Map<String, WeakReference<IHostCallback>> f5353e = new ConcurrentHashMap();

    private String a(Context context, long j10, long j11) {
        return getCallingPackage();
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        IHostCallback iHostCallback;
        String a10 = a(getContext(), Binder.getCallingPid(), Binder.getCallingUid());
        if (getContext() != null) {
            if (!(getContext().getPackageManager().checkPermission("oppo.permission.OPPO_COMPONENT_SAFE", a10) == 0)) {
                return null;
            }
            String packageName = getContext().getPackageName();
            if (((packageName != null && packageName.equals(a10)) || SimpleStringConverter.a().equals(a10) || SimpleStringConverter.c().equals(a10) || SimpleStringConverter.d().equals(a10)) && bundle != null && !bundle.isEmpty()) {
                String string = bundle.getString("ckey");
                if (EventType.STATE_PACKAGE_CHANGED_ADD.equals(str) && packageName != null && packageName.equals(a10)) {
                    Serializable serializable = bundle.getSerializable(InternalApiCall.CALLBACK);
                    if (serializable instanceof IHostCallback) {
                        this.f5353e.put(string, new WeakReference<>((IHostCallback) serializable));
                    }
                } else if ("delete".equals(str)) {
                    this.f5353e.remove(string);
                } else if ("invoke".equals(str) && this.f5353e.get(string) != null && (iHostCallback = this.f5353e.get(string).get()) != null) {
                    iHostCallback.a(getContext(), bundle);
                }
            }
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }
}
