package p4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/* compiled from: DefaultEffectiveFetchResult.java */
/* renamed from: p4.a, reason: use source file name */
/* loaded from: classes.dex */
public class DefaultEffectiveFetchResult implements EffectiveFetchResult {

    /* renamed from: e, reason: collision with root package name */
    private final HttpURLConnection f16578e;

    public DefaultEffectiveFetchResult(HttpURLConnection httpURLConnection) {
        this.f16578e = httpURLConnection;
    }

    private String b(HttpURLConnection httpURLConnection) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        StringBuilder sb2 = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb2.append(readLine);
                    sb2.append('\n');
                } else {
                    try {
                        break;
                    } catch (Exception unused) {
                    }
                }
            } finally {
                try {
                    bufferedReader.close();
                } catch (Exception unused2) {
                }
            }
        }
        return sb2.toString();
    }

    @Override // p4.EffectiveFetchResult
    public String D() {
        return this.f16578e.getContentType();
    }

    @Override // p4.EffectiveFetchResult
    public String G() {
        try {
            if (c0()) {
                return null;
            }
            return "Unable to fetch " + this.f16578e.getURL() + ". Failed with " + this.f16578e.getResponseCode() + "\n" + b(this.f16578e);
        } catch (IOException e10) {
            s4.e.d("get error failed ", e10);
            return e10.getMessage();
        }
    }

    @Override // p4.EffectiveFetchResult
    public InputStream Q() {
        return this.f16578e.getInputStream();
    }

    @Override // p4.EffectiveFetchResult
    public boolean c0() {
        try {
            return this.f16578e.getResponseCode() / 100 == 2;
        } catch (IOException unused) {
            return false;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f16578e.disconnect();
    }
}
