package p4;

import android.util.Pair;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/* compiled from: NetworkCache.java */
/* renamed from: p4.g, reason: use source file name */
/* loaded from: classes.dex */
public class NetworkCache {

    /* renamed from: a, reason: collision with root package name */
    private final EffectiveNetworkCacheProvider f16583a;

    public NetworkCache(EffectiveNetworkCacheProvider effectiveNetworkCacheProvider) {
        this.f16583a = effectiveNetworkCacheProvider;
    }

    private static String b(String str, FileExtension fileExtension, boolean z10) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("effective_anim_cache_");
        sb2.append(str.replaceAll("\\W+", ""));
        sb2.append(z10 ? fileExtension.a() : fileExtension.f16582e);
        return sb2.toString();
    }

    private File c(String str) {
        File file = new File(d(), b(str, FileExtension.JSON, false));
        if (file.exists()) {
            return file;
        }
        File file2 = new File(d(), b(str, FileExtension.ZIP, false));
        if (file2.exists()) {
            return file2;
        }
        return null;
    }

    private File d() {
        File a10 = this.f16583a.a();
        if (a10.isFile()) {
            a10.delete();
        }
        if (!a10.exists()) {
            a10.mkdirs();
        }
        return a10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Pair<FileExtension, InputStream> a(String str) {
        FileExtension fileExtension;
        try {
            File c10 = c(str);
            if (c10 == null) {
                return null;
            }
            FileInputStream fileInputStream = new FileInputStream(c10);
            if (c10.getAbsolutePath().endsWith(".zip")) {
                fileExtension = FileExtension.ZIP;
            } else {
                fileExtension = FileExtension.JSON;
            }
            s4.e.a("Cache hit for " + str + " at " + c10.getAbsolutePath());
            return new Pair<>(fileExtension, fileInputStream);
        } catch (FileNotFoundException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(String str, FileExtension fileExtension) {
        File file = new File(d(), b(str, fileExtension, true));
        File file2 = new File(file.getAbsolutePath().replace(".temp", ""));
        boolean renameTo = file.renameTo(file2);
        s4.e.a("Copying temp file to real file (" + file2 + ")");
        if (renameTo) {
            return;
        }
        s4.e.c("Unable to rename cache file " + file.getAbsolutePath() + " to " + file2.getAbsolutePath() + ".");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public File f(String str, InputStream inputStream, FileExtension fileExtension) {
        File file = new File(d(), b(str, fileExtension, true));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read != -1) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileOutputStream.flush();
                        return file;
                    }
                }
            } finally {
                fileOutputStream.close();
            }
        } finally {
            inputStream.close();
        }
    }
}
