package androidx.core.graphics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: TypefaceCompatBaseImpl.java */
/* renamed from: androidx.core.graphics.g, reason: use source file name */
/* loaded from: classes.dex */
public class TypefaceCompatBaseImpl {

    /* renamed from: a, reason: collision with root package name */
    @SuppressLint({"BanConcurrentHashMap"})
    private ConcurrentHashMap<Long, FontResourcesParserCompat.c> f2208a = new ConcurrentHashMap<>();

    public Typeface a(Context context, FontResourcesParserCompat.c cVar, Resources resources, int i10) {
        throw null;
    }

    public Typeface b(Context context, CancellationSignal cancellationSignal, FontsContractCompat.b[] bVarArr, int i10) {
        throw null;
    }

    public Typeface c(Context context, Resources resources, int i10, String str, int i11) {
        throw null;
    }
}
