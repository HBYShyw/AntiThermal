package s5;

import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import kotlin.Metadata;
import sd.Charsets;
import za.k;

/* compiled from: StringCompressor.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006J\u000e\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002¨\u0006\u0007"}, d2 = {"Ls5/d;", "", "", "uncompressSrc", "a", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: s5.d, reason: use source file name */
/* loaded from: classes.dex */
public final class StringCompressor {

    /* renamed from: a, reason: collision with root package name */
    public static final StringCompressor f18069a = new StringCompressor();

    private StringCompressor() {
    }

    public final String a(String uncompressSrc) {
        k.e(uncompressSrc, "uncompressSrc");
        b.f18066c.c("StringCompressor", "- enCompress source size is " + uncompressSrc.length());
        Deflater deflater = new Deflater(9);
        byte[] bytes = uncompressSrc.getBytes(Charsets.f18469b);
        k.d(bytes, "(this as java.lang.String).getBytes(charset)");
        deflater.setInput(bytes);
        deflater.finish();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        byte[] bArr = new byte[512];
        while (!deflater.finished()) {
            byteArrayOutputStream.write(bArr, 0, deflater.deflate(bArr));
        }
        deflater.end();
        String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 1);
        k.d(encodeToString, "Base64.encodeToString(ou…ray(), Base64.NO_PADDING)");
        return encodeToString;
    }
}
