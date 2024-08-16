package e1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zd.MediaType;
import zd.OkHttpClient;
import zd.RequestBody;
import zd.b0;
import zd.z;

/* compiled from: HttpUtil.java */
/* renamed from: e1.g, reason: use source file name */
/* loaded from: classes.dex */
public class HttpUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final Pattern f10955a = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");

    /* renamed from: b, reason: collision with root package name */
    private static final Pattern f10956b = Pattern.compile("([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}");

    /* renamed from: c, reason: collision with root package name */
    private static volatile OkHttpClient f10957c;

    private static OkHttpClient a() {
        if (f10957c == null) {
            synchronized (OkHttpClient.class) {
                if (f10957c == null) {
                    f10957c = new OkHttpClient();
                }
            }
        }
        return f10957c;
    }

    public static b0 b(String str, String str2) {
        return a().u(new z.a().m(str).g(RequestBody.c(MediaType.f("application/json; charset=utf-8"), str2)).b()).execute();
    }

    public static String c(String str, String str2) {
        if (str == null) {
            return null;
        }
        Matcher matcher = f10956b.matcher(str);
        if (!matcher.find()) {
            return null;
        }
        return "https://" + matcher.group() + str2;
    }
}
