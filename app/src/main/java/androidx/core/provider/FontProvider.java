package androidx.core.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneFlightData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* compiled from: FontProvider.java */
/* renamed from: androidx.core.provider.d, reason: use source file name */
/* loaded from: classes.dex */
class FontProvider {

    /* renamed from: a, reason: collision with root package name */
    private static final Comparator<byte[]> f2228a = new Comparator() { // from class: androidx.core.provider.c
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int g6;
            g6 = FontProvider.g((byte[]) obj, (byte[]) obj2);
            return g6;
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FontProvider.java */
    /* renamed from: androidx.core.provider.d$a */
    /* loaded from: classes.dex */
    public static class a {
        static Cursor a(ContentResolver contentResolver, Uri uri, String[] strArr, String str, String[] strArr2, String str2, Object obj) {
            return contentResolver.query(uri, strArr, str, strArr2, str2, (CancellationSignal) obj);
        }
    }

    private static List<byte[]> b(Signature[] signatureArr) {
        ArrayList arrayList = new ArrayList();
        for (Signature signature : signatureArr) {
            arrayList.add(signature.toByteArray());
        }
        return arrayList;
    }

    private static boolean c(List<byte[]> list, List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i10 = 0; i10 < list.size(); i10++) {
            if (!Arrays.equals(list.get(i10), list2.get(i10))) {
                return false;
            }
        }
        return true;
    }

    private static List<List<byte[]>> d(FontRequest fontRequest, Resources resources) {
        if (fontRequest.b() != null) {
            return fontRequest.b();
        }
        return FontResourcesParserCompat.c(resources, fontRequest.c());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FontsContractCompat.a e(Context context, FontRequest fontRequest, CancellationSignal cancellationSignal) {
        ProviderInfo f10 = f(context.getPackageManager(), fontRequest, context.getResources());
        if (f10 == null) {
            return FontsContractCompat.a.a(1, null);
        }
        return FontsContractCompat.a.a(0, h(context, fontRequest, f10.authority, cancellationSignal));
    }

    static ProviderInfo f(PackageManager packageManager, FontRequest fontRequest, Resources resources) {
        String e10 = fontRequest.e();
        ProviderInfo resolveContentProvider = packageManager.resolveContentProvider(e10, 0);
        if (resolveContentProvider != null) {
            if (resolveContentProvider.packageName.equals(fontRequest.f())) {
                List<byte[]> b10 = b(packageManager.getPackageInfo(resolveContentProvider.packageName, 64).signatures);
                Collections.sort(b10, f2228a);
                List<List<byte[]>> d10 = d(fontRequest, resources);
                for (int i10 = 0; i10 < d10.size(); i10++) {
                    ArrayList arrayList = new ArrayList(d10.get(i10));
                    Collections.sort(arrayList, f2228a);
                    if (c(b10, arrayList)) {
                        return resolveContentProvider;
                    }
                }
                return null;
            }
            throw new PackageManager.NameNotFoundException("Found content provider " + e10 + ", but package was not " + fontRequest.f());
        }
        throw new PackageManager.NameNotFoundException("No package found for authority: " + e10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int g(byte[] bArr, byte[] bArr2) {
        int i10;
        int i11;
        if (bArr.length != bArr2.length) {
            i10 = bArr.length;
            i11 = bArr2.length;
        } else {
            for (int i12 = 0; i12 < bArr.length; i12++) {
                if (bArr[i12] != bArr2[i12]) {
                    i10 = bArr[i12];
                    i11 = bArr2[i12];
                }
            }
            return 0;
        }
        return i10 - i11;
    }

    static FontsContractCompat.b[] h(Context context, FontRequest fontRequest, String str, CancellationSignal cancellationSignal) {
        int i10;
        Uri withAppendedId;
        int i11;
        boolean z10;
        ArrayList arrayList = new ArrayList();
        Uri build = new Uri.Builder().scheme("content").authority(str).build();
        Uri build2 = new Uri.Builder().scheme("content").authority(str).appendPath("file").build();
        Cursor cursor = null;
        try {
            int i12 = 0;
            cursor = a.a(context.getContentResolver(), build, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{fontRequest.g()}, null, cancellationSignal);
            if (cursor != null && cursor.getCount() > 0) {
                int columnIndex = cursor.getColumnIndex("result_code");
                ArrayList arrayList2 = new ArrayList();
                int columnIndex2 = cursor.getColumnIndex("_id");
                int columnIndex3 = cursor.getColumnIndex("file_id");
                int columnIndex4 = cursor.getColumnIndex("font_ttc_index");
                int columnIndex5 = cursor.getColumnIndex("font_weight");
                int columnIndex6 = cursor.getColumnIndex("font_italic");
                while (cursor.moveToNext()) {
                    int i13 = columnIndex != -1 ? cursor.getInt(columnIndex) : i12;
                    int i14 = columnIndex4 != -1 ? cursor.getInt(columnIndex4) : i12;
                    if (columnIndex3 == -1) {
                        i10 = i13;
                        withAppendedId = ContentUris.withAppendedId(build, cursor.getLong(columnIndex2));
                    } else {
                        i10 = i13;
                        withAppendedId = ContentUris.withAppendedId(build2, cursor.getLong(columnIndex3));
                    }
                    int i15 = columnIndex5 != -1 ? cursor.getInt(columnIndex5) : SceneFlightData.INVALID_LATITUDE_LONGITUDE;
                    if (columnIndex6 == -1 || cursor.getInt(columnIndex6) != 1) {
                        i11 = i10;
                        z10 = false;
                    } else {
                        i11 = i10;
                        z10 = true;
                    }
                    arrayList2.add(FontsContractCompat.b.a(withAppendedId, i14, i15, z10, i11));
                    i12 = 0;
                }
                arrayList = arrayList2;
            }
            return (FontsContractCompat.b[]) arrayList.toArray(new FontsContractCompat.b[0]);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
