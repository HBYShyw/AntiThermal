package o9;

import android.content.Context;
import android.util.Log;
import b6.LocalLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/* compiled from: HighPowerShuffleUtil.java */
/* renamed from: o9.f, reason: use source file name */
/* loaded from: classes2.dex */
public class HighPowerShuffleUtil {

    /* renamed from: a, reason: collision with root package name */
    private static a f16303a;

    /* compiled from: HighPowerShuffleUtil.java */
    /* renamed from: o9.f$a */
    /* loaded from: classes2.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        public double f16304a = 5.0d;

        /* renamed from: b, reason: collision with root package name */
        public double f16305b = 0.002d;

        /* renamed from: c, reason: collision with root package name */
        public ArrayList<String> f16306c;

        /* renamed from: d, reason: collision with root package name */
        public int f16307d;

        /* renamed from: e, reason: collision with root package name */
        public int f16308e;

        public a(Context context) {
            StringBuilder sb2;
            ArrayList<String> arrayList = new ArrayList<>();
            this.f16306c = arrayList;
            this.f16307d = 1000;
            this.f16308e = 50;
            InputStream inputStream = null;
            try {
                try {
                    arrayList.clear();
                    inputStream = context.getAssets().open("high_power_shuffle_config");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        this.f16306c.add(readLine.trim());
                        Log.d("HighPowerShuffleUtil", "tmp.trime(): " + readLine.trim());
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e10) {
                            e = e10;
                            sb2 = new StringBuilder();
                            sb2.append("can not close stream: ");
                            sb2.append(e);
                            LocalLog.b("HighPowerShuffleUtil", sb2.toString());
                        }
                    }
                } catch (IOException e11) {
                    LocalLog.b("HighPowerShuffleUtil", "can not open high power shuffle config: " + e11);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e12) {
                            e = e12;
                            sb2 = new StringBuilder();
                            sb2.append("can not close stream: ");
                            sb2.append(e);
                            LocalLog.b("HighPowerShuffleUtil", sb2.toString());
                        }
                    }
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e13) {
                        LocalLog.b("HighPowerShuffleUtil", "can not close stream: " + e13);
                    }
                }
                throw th;
            }
        }

        public void a(Collection<String> collection, Collection<String> collection2) {
            if (this.f16306c == null) {
                LocalLog.b("HighPowerShuffleUtil", "shuffle: prior is null");
                return;
            }
            ArrayList arrayList = new ArrayList(collection2);
            Collections.shuffle(arrayList);
            int min = Math.min(this.f16307d, arrayList.size());
            for (int i10 = 0; i10 < min; i10++) {
                collection.add((String) arrayList.get(i10));
                LocalLog.a("HighPowerShuffleUtil", "getShuffleDE: origin " + i10 + " " + ((String) arrayList.get(i10)));
            }
            Collections.shuffle(this.f16306c);
            int min2 = Math.min(this.f16308e, this.f16306c.size());
            for (int i11 = 0; i11 < min2; i11++) {
                collection.add(this.f16306c.get(i11));
                LocalLog.a("HighPowerShuffleUtil", "getShuffleDE: prior " + i11 + " " + this.f16306c.get(i11));
            }
            LocalLog.a("HighPowerShuffleUtil", "getShuffleDE: origin size=" + arrayList.size() + " prior size=" + this.f16306c.size() + " swapSize:" + collection.size());
        }
    }

    public static ka.a a(Context context, Collection<String> collection) {
        if (f16303a == null) {
            LocalLog.b("HighPowerShuffleUtil", "ShuffleDE param is null");
            f16303a = new a(context);
        }
        ArrayList arrayList = new ArrayList();
        f16303a.a(arrayList, collection);
        a aVar = f16303a;
        return new ka.a(aVar.f16304a, aVar.f16305b, aVar.f16306c, arrayList);
    }
}
