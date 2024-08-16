package q5;

import android.content.Context;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import kotlin.Metadata;
import s5.b;
import sd.Charsets;
import wa.ReadWrite;
import za.k;

/* compiled from: FileSourceHelper.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0012\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\u001a\u0014\u0010\u0004\u001a\u0004\u0018\u00010\u0003*\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0001¨\u0006\u0005"}, d2 = {"", "Landroid/content/Context;", "context", "", "a", "com.oplus.card.widget.cardwidget"}, k = 2, mv = {1, 4, 2})
/* renamed from: q5.a, reason: use source file name */
/* loaded from: classes.dex */
public final class FileSourceHelper {
    public static final byte[] a(String str, Context context) {
        k.e(str, "$this$loadFromAsset");
        k.e(context, "context");
        try {
            InputStream open = context.getAssets().open(str);
            k.d(open, "context.assets.open(this)");
            Charset charset = Charsets.f18469b;
            InputStreamReader inputStreamReader = new InputStreamReader(open, charset);
            String c10 = ReadWrite.c(inputStreamReader);
            inputStreamReader.close();
            if (c10 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            byte[] bytes = c10.getBytes(charset);
            k.d(bytes, "(this as java.lang.String).getBytes(charset)");
            return bytes;
        } catch (Exception e10) {
            b.f18066c.e("FileSourceHelper", "loadFromAsset error: " + e10);
            return null;
        }
    }
}
