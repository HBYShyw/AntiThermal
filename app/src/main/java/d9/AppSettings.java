package d9;

import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.Settings;
import android.util.Log;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import e9.OplusVersionUtils;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: AppSettings.java */
/* renamed from: d9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class AppSettings implements BaseColumns {

    /* renamed from: a, reason: collision with root package name */
    private static final String f10825a = (String) b();

    /* renamed from: b, reason: collision with root package name */
    public static final Uri f10826b = Settings.System.getUriFor("video_call");

    /* renamed from: c, reason: collision with root package name */
    public static final Uri f10827c = Settings.System.getUriFor("sip_call");

    /* renamed from: d, reason: collision with root package name */
    public static final String[] f10828d = {"user_preferred_sub1", "user_preferred_sub2"};

    /* renamed from: e, reason: collision with root package name */
    public static final Uri f10829e = Settings.System.getUriFor("mms_notification");

    /* renamed from: f, reason: collision with root package name */
    public static final Uri f10830f = Settings.System.getUriFor("ringtone_sim2");

    /* renamed from: g, reason: collision with root package name */
    public static final Uri f10831g = Settings.System.getUriFor("notification_sim2");

    /* renamed from: h, reason: collision with root package name */
    public static final Uri f10832h = Settings.System.getUriFor("calendar_sound");

    /* renamed from: i, reason: collision with root package name */
    public static final String[] f10833i = {"user_preferred_sub1", "user_preferred_sub2", "user_preferred_sub3"};

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AppSettings.java */
    /* renamed from: d9.a$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: b, reason: collision with root package name */
        private final Uri f10835b;

        /* renamed from: a, reason: collision with root package name */
        private final Object f10834a = new Object();

        /* renamed from: c, reason: collision with root package name */
        private final ConcurrentHashMap<Integer, ContentProviderClient> f10836c = new ConcurrentHashMap<>();

        public a(Uri uri) {
            this.f10835b = uri;
        }

        private Uri b(int i10) {
            if (i10 == -100) {
                return this.f10835b;
            }
            Uri.Builder buildUpon = this.f10835b.buildUpon();
            buildUpon.encodedAuthority("" + i10 + "@" + this.f10835b.getEncodedAuthority());
            return buildUpon.build();
        }

        public ContentProviderClient a(ContentResolver contentResolver, int i10) {
            if (i10 != -100 && i10 < 0) {
                Log.e("AppSettings", "Cannot support user id (below zero) : " + i10 + " . Please use @link [ActivityManager.getCurrentUser] instead.");
                return null;
            }
            synchronized (this.f10834a) {
                ContentProviderClient contentProviderClient = this.f10836c.get(Integer.valueOf(i10));
                if (contentProviderClient == null) {
                    contentProviderClient = contentResolver.acquireUnstableContentProviderClient(b(i10).getAuthority());
                    if (contentProviderClient == null) {
                        Log.e("AppSettings", "getProviderForUser contentProvider == null,uid:" + i10 + " ,uri: " + b(i10).getAuthority());
                        return null;
                    }
                    this.f10836c.put(Integer.valueOf(i10), contentProviderClient);
                }
                return contentProviderClient;
            }
        }

        public ContentProviderClient c(ContentResolver contentResolver, int i10) {
            synchronized (this.f10834a) {
                if (this.f10836c.containsKey(Integer.valueOf(i10))) {
                    this.f10836c.remove(Integer.valueOf(i10));
                }
            }
            return a(contentResolver, i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AppSettings.java */
    /* renamed from: d9.a$b */
    /* loaded from: classes2.dex */
    public static class b {

        /* renamed from: e, reason: collision with root package name */
        private static final String[] f10837e = {ThermalBaseConfig.Item.ATTR_VALUE};

        /* renamed from: a, reason: collision with root package name */
        private final Uri f10838a;

        /* renamed from: b, reason: collision with root package name */
        private final a f10839b;

        /* renamed from: c, reason: collision with root package name */
        private final String f10840c;

        /* renamed from: d, reason: collision with root package name */
        private final String f10841d;

        public b(Uri uri, String str, String str2, a aVar) {
            this.f10838a = uri;
            this.f10840c = str;
            this.f10841d = str2;
            this.f10839b = aVar;
        }

        @SuppressLint({"NewApi"})
        public String a(ContentResolver contentResolver, String str, int i10) {
            int i11;
            Cursor query;
            ContentProviderClient a10 = this.f10839b.a(contentResolver, i10);
            try {
                if (a10 == null) {
                    Log.w("AppSettings", "Can't get provider for " + this.f10838a + " ,due to ContentProviderClient == null");
                    if (a10 != null) {
                        a10.close();
                    }
                    return null;
                }
                try {
                    i11 = 0;
                } catch (RemoteException unused) {
                    i11 = 0;
                }
                try {
                    query = a10.query(this.f10838a, f10837e, "name=?", new String[]{str}, null);
                    try {
                        if (query == null) {
                            Log.w("AppSettings", "Can't get key " + str + " from " + this.f10838a + " ,due to cursor is null");
                            if (query != null) {
                                query.close();
                            }
                            a10.close();
                            return null;
                        }
                        String string = query.moveToNext() ? query.getString(0) : null;
                        query.close();
                        a10.close();
                        return string;
                    } finally {
                    }
                } catch (RemoteException unused2) {
                    Log.w("AppSettings", "query RemoteException Can't get key " + str + " from " + this.f10838a);
                    try {
                        int i12 = i11;
                        ContentProviderClient c10 = this.f10839b.c(contentResolver, i10);
                        if (c10 == null) {
                            Log.w("AppSettings", "retry reacquireProviderForUser failed");
                            if (c10 != null) {
                                c10.close();
                            }
                            return null;
                        }
                        Uri uri = this.f10838a;
                        String[] strArr = f10837e;
                        String[] strArr2 = new String[1];
                        strArr2[i12] = str;
                        query = c10.query(uri, strArr, "name=?", strArr2, null);
                        try {
                            if (query == null) {
                                Log.w("AppSettings", "Can't get key " + str + " from " + this.f10838a + " ,due to cursor is null");
                                if (query != null) {
                                    query.close();
                                }
                                c10.close();
                                return null;
                            }
                            String string2 = query.moveToNext() ? query.getString(i12) : null;
                            query.close();
                            c10.close();
                            return string2;
                        } finally {
                        }
                    } catch (RemoteException unused3) {
                        if (this.f10840c != null) {
                            try {
                                Bundle call = a10.call(this.f10840c, str, new Bundle());
                                if (call != null) {
                                    String string3 = call.getString(ThermalBaseConfig.Item.ATTR_VALUE);
                                    a10.close();
                                    return string3;
                                }
                            } catch (RemoteException e10) {
                                Log.e("AppSettings", "call RemoteException Can't get key " + str + " from " + this.f10838a, e10);
                            }
                        }
                        if (a10 != null) {
                            a10.close();
                        }
                        return null;
                    }
                }
            } catch (Throwable th) {
                if (a10 != null) {
                    a10.close();
                }
                throw th;
            }
        }
    }

    /* compiled from: AppSettings.java */
    /* renamed from: d9.a$c */
    /* loaded from: classes2.dex */
    public static class c implements BaseColumns {
        protected static Uri a(Uri uri, String str) {
            return Uri.withAppendedPath(uri, str);
        }
    }

    /* compiled from: AppSettings.java */
    /* renamed from: d9.a$d */
    /* loaded from: classes2.dex */
    public static final class d extends c {

        /* renamed from: a, reason: collision with root package name */
        private static final String f10842a;

        /* renamed from: b, reason: collision with root package name */
        public static final Uri f10843b;

        /* renamed from: c, reason: collision with root package name */
        private static final a f10844c;

        /* renamed from: d, reason: collision with root package name */
        private static final b f10845d;

        static {
            String str = "content://" + AppSettings.f10825a + "/nonpersist";
            f10842a = str;
            Uri parse = Uri.parse(str);
            f10843b = parse;
            a aVar = new a(parse);
            f10844c = aVar;
            f10845d = new b(parse, "GET_nonpersist", "PUT_nonpersist", aVar);
        }

        public static String b(ContentResolver contentResolver, String str) {
            return c(contentResolver, str, -100);
        }

        public static String c(ContentResolver contentResolver, String str, int i10) {
            return f10845d.a(contentResolver, str, i10);
        }

        public static Uri d(String str) {
            return c.a(f10843b, str);
        }
    }

    /* compiled from: AppSettings.java */
    /* renamed from: d9.a$e */
    /* loaded from: classes2.dex */
    public static final class e extends c {

        /* renamed from: a, reason: collision with root package name */
        private static final String f10846a;

        /* renamed from: b, reason: collision with root package name */
        public static final Uri f10847b;

        /* renamed from: c, reason: collision with root package name */
        private static final a f10848c;

        /* renamed from: d, reason: collision with root package name */
        private static final b f10849d;

        static {
            String str = "content://" + AppSettings.f10825a + "/system";
            f10846a = str;
            Uri parse = Uri.parse(str);
            f10847b = parse;
            a aVar = new a(parse);
            f10848c = aVar;
            f10849d = new b(parse, "GET_system", "PUT_system", aVar);
        }

        public static int b(ContentResolver contentResolver, String str, int i10) {
            String c10 = c(contentResolver, str);
            if (c10 == null) {
                return i10;
            }
            try {
                return Integer.parseInt(c10);
            } catch (NumberFormatException e10) {
                Log.e("AppSettings", "System getInt has Exception: " + e10.getMessage());
                return i10;
            }
        }

        public static String c(ContentResolver contentResolver, String str) {
            return d(contentResolver, str, -100);
        }

        public static String d(ContentResolver contentResolver, String str, int i10) {
            return f10849d.a(contentResolver, str, i10);
        }

        public static Uri e(String str) {
            return c.a(f10847b, str);
        }
    }

    private static Object b() {
        if (OplusVersionUtils.f10973a) {
            return "com.oplus.appplatform.settings";
        }
        return null;
    }
}
