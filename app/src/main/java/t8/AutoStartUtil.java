package t8;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IContentProvider;
import android.net.Uri;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

/* compiled from: AutoStartUtil.java */
/* renamed from: t8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class AutoStartUtil {

    /* renamed from: a, reason: collision with root package name */
    public static final Uri f18634a = Uri.parse("content://com.oplus.startup.provider");

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0033, code lost:
    
        if (r0 != null) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0045, code lost:
    
        r10 = r9.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x004d, code lost:
    
        if (r10.hasNext() == false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004f, code lost:
    
        b6.LocalLog.a("AutoStartUtil", "getPowerOptList result  " + ((java.lang.String) r10.next()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x006c, code lost:
    
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x003e, code lost:
    
        r10.getContentResolver().releaseUnstableProvider(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x003c, code lost:
    
        if (r0 == null) goto L18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public List<String> a(Context context, String str) {
        Bundle call;
        ArrayList<String> stringArrayList;
        ArrayList arrayList = new ArrayList();
        IContentProvider iContentProvider = null;
        try {
            try {
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = f18634a;
                iContentProvider = contentResolver.acquireUnstableProvider(uri);
                if (iContentProvider != null && (call = iContentProvider.call(context.getAttributionSource(), uri.getAuthority(), str, (String) null, new Bundle())) != null && (stringArrayList = call.getStringArrayList("packageList")) != null) {
                    arrayList.addAll(stringArrayList);
                }
            } catch (Exception e10) {
                e10.printStackTrace();
            }
        } catch (Throwable th) {
            if (iContentProvider != null) {
                context.getContentResolver().releaseUnstableProvider(iContentProvider);
            }
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0043, code lost:
    
        b6.LocalLog.a("AutoStartUtil", "setPowerOptList result " + r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0059, code lost:
    
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0040, code lost:
    
        if (r0 == null) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String b(Context context, String str, ArrayList<String> arrayList) {
        String str2 = "false";
        IContentProvider iContentProvider = null;
        try {
            try {
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = f18634a;
                iContentProvider = contentResolver.acquireUnstableProvider(uri);
                if (iContentProvider != null) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("packageList", arrayList);
                    Bundle call = iContentProvider.call(context.getAttributionSource(), uri.getAuthority(), str, (String) null, bundle);
                    if (call != null) {
                        str2 = call.getString("returnValue");
                    }
                }
            } catch (Exception e10) {
                e10.printStackTrace();
            }
        } finally {
            if (iContentProvider != null) {
                context.getContentResolver().releaseUnstableProvider(iContentProvider);
            }
        }
    }
}
