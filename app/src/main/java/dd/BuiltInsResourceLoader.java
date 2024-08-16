package dd;

import com.oplus.backup.sdk.common.utils.Constants;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import za.k;

/* compiled from: BuiltInsResourceLoader.kt */
/* renamed from: dd.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class BuiltInsResourceLoader {
    public final InputStream a(String str) {
        k.e(str, Constants.MessagerConstants.PATH_KEY);
        ClassLoader classLoader = BuiltInsResourceLoader.class.getClassLoader();
        if (classLoader == null) {
            return ClassLoader.getSystemResourceAsStream(str);
        }
        URL resource = classLoader.getResource(str);
        if (resource == null) {
            return null;
        }
        URLConnection openConnection = resource.openConnection();
        openConnection.setUseCaches(false);
        return openConnection.getInputStream();
    }
}
