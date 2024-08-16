package com.oplus.cardwidget.serviceLayer;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Process;
import com.oplus.backup.sdk.common.utils.Constants;
import com.oplus.channel.client.provider.ChannelClientProvider;
import com.oplus.deepthinker.sdk.app.feature.eventassociation.EventAssociationEntity;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections._Collections;
import kotlin.collections.r;
import s5.b;
import za.k;

/* compiled from: BaseCardStrategyProvider.kt */
@Metadata(bv = {}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b-\u0010.J\b\u0010\u0003\u001a\u00020\u0002H\u0016J&\u0010\t\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0005\u001a\u00020\u00042\b\u0010\u0006\u001a\u0004\u0018\u00010\u00042\b\u0010\b\u001a\u0004\u0018\u00010\u0007H\u0016J\u001c\u0010\u000e\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\n2\b\u0010\r\u001a\u0004\u0018\u00010\fH\u0016J3\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u000b\u001a\u00020\n2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00042\u0010\u0010\u0011\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0004\u0018\u00010\u0010H\u0016¢\u0006\u0004\b\u0013\u0010\u0014JQ\u0010\u0018\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u000b\u001a\u00020\n2\u0010\u0010\u0015\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0004\u0018\u00010\u00102\b\u0010\u000f\u001a\u0004\u0018\u00010\u00042\u0010\u0010\u0011\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0004\u0018\u00010\u00102\b\u0010\u0016\u001a\u0004\u0018\u00010\u0004H\u0016¢\u0006\u0004\b\u0018\u0010\u0019J=\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u000b\u001a\u00020\n2\b\u0010\r\u001a\u0004\u0018\u00010\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00042\u0010\u0010\u0011\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0004\u0018\u00010\u0010H\u0016¢\u0006\u0004\b\u001a\u0010\u001bJ\u0006\u0010\u001c\u001a\u00020\u0002R\u0014\u0010\u001f\u001a\u00020\u00048\u0002X\u0082D¢\u0006\u0006\n\u0004\b\u001d\u0010\u001eR\u0018\u0010#\u001a\u0004\u0018\u00010 8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b!\u0010\"R\u001a\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00120$8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b%\u0010&R&\u0010,\u001a\u0012\u0012\u0004\u0012\u00020\u00040(j\b\u0012\u0004\u0012\u00020\u0004`)8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b*\u0010+¨\u0006/"}, d2 = {"Lcom/oplus/cardwidget/serviceLayer/BaseCardStrategyProvider;", "Lcom/oplus/channel/client/provider/ChannelClientProvider;", "", "onCreate", "", Constants.MessagerConstants.METHOD_KEY, "arg", "Landroid/os/Bundle;", "extras", "call", "Landroid/net/Uri;", "uri", "Landroid/content/ContentValues;", "values", "insert", "selection", "", "selectionArgs", "", "delete", "(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I", "projection", "sortOrder", "Landroid/database/Cursor;", "query", "(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;", "update", "(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I", "n", "f", "Ljava/lang/String;", "TAG", "Landroid/content/pm/PackageManager;", "g", "Landroid/content/pm/PackageManager;", "packageManager", "", "h", "Ljava/util/List;", "sysAppUids", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "i", "Ljava/util/ArrayList;", "ALLOW_VISIT_PACKAGE_NAMES", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* loaded from: classes.dex */
public class BaseCardStrategyProvider extends ChannelClientProvider {

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private PackageManager packageManager;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private ArrayList<String> ALLOW_VISIT_PACKAGE_NAMES;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private final String TAG = "BaseCardStrategyProvider";

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private final List<Integer> sysAppUids = new ArrayList();

    public BaseCardStrategyProvider() {
        ArrayList<String> f10;
        f10 = r.f(EventAssociationEntity.APP_ASSISTANT_SCREEN, "com.oplus.assistantscreen", "com.oplus.cardservice", "com.oplus.smartengine");
        this.ALLOW_VISIT_PACKAGE_NAMES = f10;
    }

    @Override // com.oplus.channel.client.provider.ChannelClientProvider, android.content.ContentProvider
    public Bundle call(String method, String arg, Bundle extras) {
        k.e(method, Constants.MessagerConstants.METHOD_KEY);
        if (!n()) {
            b.f18066c.e(this.TAG, "call permission limit !");
            return null;
        }
        return super.call(method, arg, extras);
    }

    @Override // com.oplus.channel.client.provider.ChannelClientProvider, android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        k.e(uri, "uri");
        if (!n()) {
            b.f18066c.e(this.TAG, "delete permission limit !");
            return 0;
        }
        return super.delete(uri, selection, selectionArgs);
    }

    @Override // com.oplus.channel.client.provider.ChannelClientProvider, android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        k.e(uri, "uri");
        if (!n()) {
            b.f18066c.e(this.TAG, "insert permission limit !");
            return null;
        }
        return super.insert(uri, values);
    }

    public final boolean n() {
        boolean L;
        Context applicationContext;
        int callingUid = Binder.getCallingUid();
        boolean z10 = true;
        if (this.sysAppUids.contains(Integer.valueOf(callingUid))) {
            return true;
        }
        if (callingUid != Process.myUid() && callingUid != 1000) {
            L = _Collections.L(this.ALLOW_VISIT_PACKAGE_NAMES, getCallingPackage());
            if (L) {
                this.sysAppUids.add(Integer.valueOf(callingUid));
                return true;
            }
            PackageManager packageManager = this.packageManager;
            if (packageManager == null) {
                Context context = getContext();
                packageManager = (context == null || (applicationContext = context.getApplicationContext()) == null) ? null : applicationContext.getPackageManager();
            }
            this.packageManager = packageManager;
            boolean z11 = false;
            try {
                k.b(packageManager);
                String nameForUid = packageManager.getNameForUid(callingUid);
                if (nameForUid != null) {
                    PackageManager packageManager2 = this.packageManager;
                    k.b(packageManager2);
                    ApplicationInfo applicationInfo = packageManager2.getApplicationInfo(nameForUid, 0);
                    k.d(applicationInfo, "packageManager!!.getApplicationInfo(it, 0)");
                    if ((applicationInfo.flags & 1) == 1) {
                        this.sysAppUids.add(Integer.valueOf(callingUid));
                    } else {
                        z10 = false;
                    }
                    z11 = z10;
                }
            } catch (PackageManager.NameNotFoundException e10) {
                b.f18066c.e(this.TAG, "checkPermission e:" + e10);
            }
            b.f18066c.c(this.TAG, "checkPermission:result: " + z11);
            return z11;
        }
        this.sysAppUids.add(Integer.valueOf(callingUid));
        return true;
    }

    @Override // com.oplus.channel.client.provider.ChannelClientProvider, android.content.ContentProvider
    public boolean onCreate() {
        Context context = getContext();
        if (context != null) {
            ArrayList<String> arrayList = this.ALLOW_VISIT_PACKAGE_NAMES;
            k.d(context, "it");
            arrayList.add(context.getPackageName());
        }
        return super.onCreate();
    }

    @Override // com.oplus.channel.client.provider.ChannelClientProvider, android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        k.e(uri, "uri");
        if (!n()) {
            b.f18066c.e(this.TAG, "query permission limit !");
            return null;
        }
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override // com.oplus.channel.client.provider.ChannelClientProvider, android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        k.e(uri, "uri");
        if (!n()) {
            b.f18066c.e(this.TAG, "update permission limit !");
            return 0;
        }
        return super.update(uri, values, selection, selectionArgs);
    }
}
