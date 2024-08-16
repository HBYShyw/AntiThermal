package com.oplus.startupapp.data;

import aa.StartupBackupRestoreUtils;
import aa.StartupDataUtils;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import b6.LocalLog;
import c0.SupportSQLiteQueryBuilder;
import com.oplus.startupapp.data.database.RecordDatabase;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import u9.StartupManager;
import u9.StartupManagerAction;
import x9.WhiteListUtils;
import y5.b;
import z9.AppToShow;
import z9.Record;

/* loaded from: classes2.dex */
public class StartupProvider extends ContentProvider {

    /* renamed from: g, reason: collision with root package name */
    private static String f10502g = "StartupManager";

    /* renamed from: h, reason: collision with root package name */
    private static UriMatcher f10503h;

    /* renamed from: e, reason: collision with root package name */
    private Context f10504e;

    /* renamed from: f, reason: collision with root package name */
    private ContentResolver f10505f;

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        f10503h = uriMatcher;
        uriMatcher.addURI("com.oplus.startup.provider", "record", 101);
        f10503h.addURI("com.oplus.startup.provider", "malicious_record", 102);
        f10503h.addURI("com.oplus.startup.provider", "malicious_detail_record", 103);
    }

    private String b(Uri uri) {
        switch (f10503h.match(uri)) {
            case 101:
                return "record";
            case 102:
                return "malicious_record";
            case 103:
                return "malicious_detail_record";
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
    }

    public ApplicationInfo a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            Context context = this.f10504e;
            if (context != null) {
                return context.getPackageManager().getApplicationInfo(str, 0);
            }
            return null;
        } catch (Exception e10) {
            LocalLog.m(f10502g, "getAppInfo fail!", e10);
            return null;
        }
    }

    public boolean c(String str, boolean z10) {
        boolean d10 = d(str);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(StartupDataUtils.h(this.f10504e).g(z10));
        LocalLog.a(f10502g, "hideList pkg = " + arrayList);
        return d10 || WhiteListUtils.l(str) || WhiteListUtils.i(this.f10504e, str) || arrayList.contains(str);
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        ArrayList<String> stringArrayList = (bundle == null || !bundle.containsKey("packageList")) ? null : bundle.getStringArrayList("packageList");
        Bundle bundle2 = new Bundle();
        RecordDatabase u7 = RecordDatabase.u(this.f10504e);
        if (bundle != null) {
            LocalLog.a(f10502g, "call:get data from provider called " + bundle.toString());
        } else {
            Log.d(f10502g, "call:get data from provider called null ");
        }
        if ("get_count_by_day".equals(str)) {
            Long valueOf = Long.valueOf(StartupManager.i(this.f10504e).g());
            List<Record> v7 = u7.v().v("0", valueOf);
            List<Record> v10 = u7.v().v("1", valueOf);
            Iterator<Record> it = v7.iterator();
            int i10 = 0;
            while (it.hasNext()) {
                i10 = (int) (i10 + it.next().f20330h);
            }
            Iterator<Record> it2 = v10.iterator();
            int i11 = 0;
            while (it2.hasNext()) {
                i11 = (int) (i11 + it2.next().f20330h);
            }
            Log.d(f10502g, " get autoCountByDay: " + i10 + " associateCountByDay: " + i11);
            bundle2.putInt("auto_count_day", i10);
            bundle2.putInt("associate_count_day", i11);
        }
        if ("get_count_by_month".equals(str)) {
            Long valueOf2 = Long.valueOf(StartupManager.i(this.f10504e).g());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(valueOf2.longValue());
            long timeInMillis = calendar.getTimeInMillis();
            calendar.add(2, -1);
            long timeInMillis2 = calendar.getTimeInMillis();
            List<Record> w10 = u7.v().w("0", Long.valueOf(timeInMillis2), Long.valueOf(timeInMillis));
            int i12 = 0;
            for (int i13 = 0; i13 < w10.size(); i13++) {
                i12 = (int) (i12 + w10.get(i13).f20330h);
            }
            List<Record> w11 = u7.v().w("1", Long.valueOf(timeInMillis2), Long.valueOf(timeInMillis));
            int i14 = 0;
            for (int i15 = 0; i15 < w11.size(); i15++) {
                i14 = (int) (i14 + w11.get(i15).f20330h);
            }
            Log.d(f10502g, "get auto count month:" + i12 + "   associate count month: " + i14);
            bundle2.putInt("auto_count_month", i12);
            bundle2.putInt("associate_count_month", i14);
            return bundle2;
        }
        if ("getStartupState".equals(str)) {
            if (!b.c()) {
                LocalLog.a(f10502g, " not China region! OR not AutoStartEnabled!");
                return bundle2;
            }
            ArrayList<String> d10 = StartupDataUtils.h(this.f10504e).d(true);
            if (stringArrayList == null) {
                return bundle2;
            }
            int j10 = StartupManager.i(this.f10504e).j();
            List<String> k10 = StartupDataUtils.h(this.f10504e).k(true);
            boolean z10 = StartupManager.i(this.f10504e).f(true) < j10;
            Iterator<String> it3 = stringArrayList.iterator();
            while (it3.hasNext()) {
                String next = it3.next();
                if (!c(next, true)) {
                    LocalLog.a(f10502g, "getStartupState packageList = " + stringArrayList);
                    if (d10 != null && d10.contains(next)) {
                        bundle2.putBoolean(next, true);
                    } else {
                        bundle2.putBoolean(next, false);
                    }
                    String concat = next.concat("_canBeOpened");
                    if (k10 != null && k10.contains(next)) {
                        bundle2.putBoolean(concat, true);
                    } else {
                        bundle2.putBoolean(concat, z10);
                    }
                } else {
                    LocalLog.a(f10502g, "getStartupState skipPkg = " + next);
                }
            }
            return bundle2;
        }
        if ("setStartupState".equals(str)) {
            if (!b.c()) {
                LocalLog.a(f10502g, " not China region! OR not AutoStartEnabled!");
                return bundle2;
            }
            if (stringArrayList == null) {
                return bundle2;
            }
            StartupDataUtils h10 = StartupDataUtils.h(this.f10504e);
            int j11 = StartupManager.i(this.f10504e).j();
            List<String> k11 = h10.k(true);
            int f10 = StartupManager.i(this.f10504e).f(true);
            LocalLog.d(f10502g, "maxAllowCount = " + j11 + " allowButNotWhiteCount = " + f10);
            boolean z11 = f10 < j11;
            ArrayMap arrayMap = new ArrayMap();
            Iterator<String> it4 = stringArrayList.iterator();
            while (it4.hasNext()) {
                String next2 = it4.next();
                if (c(next2, true)) {
                    LocalLog.a(f10502g, "setStartupState skipPkg = " + next2);
                } else {
                    boolean z12 = bundle.getBoolean(next2);
                    LocalLog.a(f10502g, "setStartupState pkg = " + next2 + " isChecked = " + z12 + " canOpen = " + z11);
                    if (!z12 || z11 || k11.contains(next2)) {
                        arrayMap.put(next2, Boolean.valueOf(z12));
                    }
                }
            }
            h10.O(arrayMap, true);
            return bundle2;
        }
        if ("getAssociateStartState".equals(str)) {
            if (!b.c()) {
                LocalLog.a(f10502g, " not China region! OR not AssociationStartEnabled!");
                return bundle2;
            }
            boolean z13 = false;
            ArrayList<String> d11 = StartupDataUtils.h(this.f10504e).d(false);
            if (stringArrayList == null) {
                return bundle2;
            }
            new ArrayList();
            Iterator<String> it5 = stringArrayList.iterator();
            while (it5.hasNext()) {
                String next3 = it5.next();
                if (c(next3, z13)) {
                    LocalLog.a(f10502g, "getAssociateStartState skipPkg = " + next3);
                } else {
                    LocalLog.a(f10502g, "getAssociateStartState packageList = " + stringArrayList);
                    if (d11 != null && d11.contains(next3)) {
                        bundle2.putBoolean(next3, true);
                    } else {
                        bundle2.putBoolean(next3, false);
                    }
                }
                z13 = false;
            }
            return bundle2;
        }
        if ("setAssociateStartState".equals(str)) {
            if (!b.c()) {
                LocalLog.a(f10502g, " not China region! OR not AssociationStartEnabled!");
                return bundle2;
            }
            if (stringArrayList == null) {
                return bundle2;
            }
            ArrayMap arrayMap2 = new ArrayMap();
            Iterator<String> it6 = stringArrayList.iterator();
            while (it6.hasNext()) {
                String next4 = it6.next();
                if (c(next4, false)) {
                    LocalLog.a(f10502g, "setAssociateStartState skipPkg = " + next4);
                } else {
                    boolean z14 = bundle.getBoolean(next4);
                    LocalLog.a(f10502g, "setAssociateStartState pkg = " + next4 + " isChecked = " + z14);
                    arrayMap2.put(next4, Boolean.valueOf(z14));
                }
            }
            StartupDataUtils.h(this.f10504e).O(arrayMap2, false);
            return bundle2;
        }
        if ("powerOptimizationGetAutoStartList".equals(str)) {
            List<AppToShow> e10 = StartupManager.i(this.f10504e).e();
            ArrayList<String> arrayList = new ArrayList<>();
            Iterator<AppToShow> it7 = e10.iterator();
            while (it7.hasNext()) {
                arrayList.add(it7.next().f20305b);
            }
            bundle2.putStringArrayList("packageList", arrayList);
            LocalLog.a(f10502g, "power_optimization get autoStart " + arrayList);
            return bundle2;
        }
        if ("powerOptimizationSetAutoStartList".equals(str)) {
            if (stringArrayList != null) {
                StartupDataUtils.h(this.f10504e).G(stringArrayList, false);
                LocalLog.a(f10502g, "power_optimization set autoStart " + stringArrayList);
            }
            bundle2.putString("returnValue", "true");
            return bundle2;
        }
        if ("powerOptimizationGetAssociateStartList".equals(str)) {
            if (!b.c()) {
                LocalLog.a(f10502g, " not China region! OR not AssociationStartEnabled!");
                return bundle2;
            }
            StartupDataUtils h11 = StartupDataUtils.h(this.f10504e);
            ArrayList<String> d12 = h11.d(false);
            List<String> k12 = h11.k(false);
            List<String> g6 = h11.g(false);
            ArrayList<String> arrayList2 = new ArrayList<>();
            for (String str3 : d12) {
                if (!k12.contains(str3) && !g6.contains(str3)) {
                    arrayList2.add(str3);
                }
            }
            bundle2.putStringArrayList("packageList", arrayList2);
            LocalLog.a(f10502g, "power_optimization get associateStart " + arrayList2);
            return bundle2;
        }
        if ("powerOptimizationSetAssociateStartList".equals(str)) {
            if (stringArrayList != null) {
                ArrayMap arrayMap3 = new ArrayMap();
                Iterator<String> it8 = stringArrayList.iterator();
                while (it8.hasNext()) {
                    arrayMap3.put(it8.next(), Boolean.FALSE);
                }
                StartupDataUtils.h(this.f10504e).O(arrayMap3, false);
                LocalLog.a(f10502g, "power_optimization set associateStart " + stringArrayList);
            }
            bundle2.putString("returnValue", "true");
            return bundle2;
        }
        if ("getStartupBackupData".equals(str)) {
            return StartupBackupRestoreUtils.a(this.f10504e).c();
        }
        if ("getAutoStartList".equals(str)) {
            LocalLog.a(f10502g, "getAutoStartAllowList from: " + getCallingPackage());
            bundle2.putStringArrayList("autoStartAllowList", StartupDataUtils.h(this.f10504e).d(true));
            return bundle2;
        }
        if ("getAssociateStartList".equals(str)) {
            LocalLog.a(f10502g, "getAssociateStartAllowList from: " + getCallingPackage());
            bundle2.putStringArrayList("associateStartAllowList", StartupDataUtils.h(this.f10504e).d(false));
            return bundle2;
        }
        if ("update_main_proc_list".equals(str)) {
            StartupDataUtils.h(this.f10504e.getApplicationContext()).K(bundle);
            return bundle2;
        }
        if (!"update_malicious_record".equals(str)) {
            return bundle2;
        }
        StartupManagerAction.o(this.f10504e).s(bundle);
        bundle2.putString(str, str);
        return bundle2;
    }

    public boolean d(String str) {
        ApplicationInfo a10 = a(str);
        return (a10 == null || (a10.flags & 1) == 0) ? false : true;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        int i10;
        try {
            i10 = RecordDatabase.u(this.f10504e).j().b().d(b(uri), str, strArr);
        } catch (Exception e10) {
            e10.printStackTrace();
            i10 = 0;
        }
        ContentResolver contentResolver = this.f10505f;
        if (contentResolver != null) {
            contentResolver.notifyChange(uri, null);
        }
        return i10;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        long K = RecordDatabase.u(this.f10504e).j().b().K(b(uri), 5, contentValues);
        if (K >= 0) {
            Uri withAppendedId = ContentUris.withAppendedId(uri, K);
            ContentResolver contentResolver = this.f10505f;
            if (contentResolver != null && withAppendedId != null) {
                contentResolver.notifyChange(withAppendedId, null);
            }
            return withAppendedId;
        }
        throw new SQLiteException("Unable to insert " + contentValues + " for " + uri);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        Context context = getContext();
        this.f10504e = context;
        this.f10505f = context.getContentResolver();
        return false;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Cursor q10 = RecordDatabase.u(this.f10504e).j().b().q(SupportSQLiteQueryBuilder.c(b(uri)).d(strArr).g(str2).h(str, strArr2).e());
        q10.setNotificationUri(this.f10505f, uri);
        return q10;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        int B = RecordDatabase.u(this.f10504e).j().b().B(b(uri), 5, contentValues, str, strArr);
        this.f10505f.notifyChange(uri, null);
        return B;
    }
}
