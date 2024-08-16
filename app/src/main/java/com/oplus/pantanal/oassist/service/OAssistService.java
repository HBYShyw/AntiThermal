package com.oplus.pantanal.oassist.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.oplus.backup.sdk.common.utils.Constants;
import com.oplus.pantanal.oassist.base.OAssistInput;
import com.oplus.pantanal.oassist.base.OAssistInputParam;
import com.oplus.pantanal.oassist.base.OAssistOutput;
import com.oplus.pantanal.oassist.base.OAssistOutputBody;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import m7.OAssistContract;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: OAssistService.kt */
/* loaded from: classes.dex */
public abstract class OAssistService extends Service {
    private static final long API_CALL_TIMEOUT = 2000;
    public static final b Companion = new b(null);
    private static final String SERVICE_ACTION = "com.oplus.pantanal.oassist.service";
    private static final String TAG = "OAssistService";
    private final c binder = new c();

    /* compiled from: OAssistService.kt */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final Class<?> f9976a;

        /* renamed from: b, reason: collision with root package name */
        private final Method f9977b;

        /* renamed from: c, reason: collision with root package name */
        private final List<Object> f9978c;

        public a(Class<?> cls, Method method, List<? extends Object> list) {
            k.e(cls, "clz");
            k.e(method, Constants.MessagerConstants.METHOD_KEY);
            k.e(list, "params");
            this.f9976a = cls;
            this.f9977b = method;
            this.f9978c = list;
        }

        public final Method a() {
            return this.f9977b;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return k.a(this.f9976a, aVar.f9976a) && k.a(this.f9977b, aVar.f9977b) && k.a(this.f9978c, aVar.f9978c);
        }

        public int hashCode() {
            return (((this.f9976a.hashCode() * 31) + this.f9977b.hashCode()) * 31) + this.f9978c.hashCode();
        }

        public String toString() {
            return "ApiMatchResult(clz=" + this.f9976a + ", method=" + this.f9977b + ", params=" + this.f9978c + ')';
        }
    }

    /* compiled from: OAssistService.kt */
    /* loaded from: classes.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: OAssistService.kt */
    /* loaded from: classes.dex */
    public static final class c extends OAssistContract.a {
        c() {
        }

        @Override // m7.OAssistContract
        public OAssistOutput i(OAssistInput oAssistInput) {
            k.e(oAssistInput, "input");
            try {
                a matchApi = OAssistService.this.matchApi(oAssistInput.j(), oAssistInput.k());
                if (matchApi == null) {
                    return new OAssistOutput(oAssistInput, 102, OAssistOutputBody.f9968g.a());
                }
                try {
                    OAssistOutputBody oAssistOutputBody = OAssistService.this.callApi(matchApi).get(OAssistService.API_CALL_TIMEOUT, TimeUnit.MILLISECONDS);
                    k.d(oAssistOutputBody, "body");
                    return new OAssistOutput(oAssistInput, 0, oAssistOutputBody);
                } catch (TimeoutException unused) {
                    Log.e(OAssistService.TAG, "Timeout(2000 ms) while executing api " + oAssistInput.j());
                    return new OAssistOutput(oAssistInput, 103, OAssistOutputBody.f9968g.a());
                } catch (Throwable th) {
                    Log.e(OAssistService.TAG, "Exception happened while execute callApi for api name " + oAssistInput.j(), th);
                    return new OAssistOutput(oAssistInput, 104, OAssistOutputBody.f9968g.a());
                }
            } catch (Throwable th2) {
                Log.e(OAssistService.TAG, "Exception happened while execute matchApi  for api name " + oAssistInput.j(), th2);
                return new OAssistOutput(oAssistInput, 105, OAssistOutputBody.f9968g.a());
            }
        }

        @Override // m7.OAssistContract
        public int m(OAssistInput oAssistInput) {
            k.e(oAssistInput, "input");
            return OAssistService.this.matchApi(oAssistInput.j(), oAssistInput.k()) == null ? 102 : 0;
        }
    }

    public abstract Future<OAssistOutputBody> callApi(a aVar);

    public abstract a matchApi(String str, List<OAssistInputParam> list);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        k.e(intent, Constants.MessagerConstants.INTENT_KEY);
        return this.binder;
    }
}
