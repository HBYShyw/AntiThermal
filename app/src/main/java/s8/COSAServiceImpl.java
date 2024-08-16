package s8;

import android.util.Log;
import com.oplus.epona.Epona;
import com.oplus.epona.Request;
import com.oplus.epona.Response;
import java.util.ArrayList;

/* compiled from: COSAServiceImpl.java */
/* renamed from: s8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class COSAServiceImpl {

    /* renamed from: b, reason: collision with root package name */
    private static volatile COSAServiceImpl f18150b;

    /* renamed from: a, reason: collision with root package name */
    private Request.Builder f18151a = new Request.Builder().setComponentName("com.oplus.cosa.exported");

    public static COSAServiceImpl b() {
        if (f18150b == null) {
            synchronized (COSAServiceImpl.class) {
                if (f18150b == null) {
                    f18150b = new COSAServiceImpl();
                }
            }
        }
        return f18150b;
    }

    public ArrayList<String> a(int i10) {
        Response execute = Epona.newCall(this.f18151a.setActionName("get_game_list").withInt("type", i10).build()).execute();
        if (execute.isSuccessful()) {
            ArrayList<String> stringArrayList = execute.getBundle().getStringArrayList("game_list");
            if (stringArrayList != null) {
                return stringArrayList;
            }
            Log.w("COSAServiceImpl", "response getGameList is null");
            return null;
        }
        Log.w("COSAServiceImpl", "response error for action getGameList");
        return null;
    }
}
