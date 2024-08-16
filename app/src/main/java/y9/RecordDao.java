package y9;

import android.util.ArrayMap;
import android.util.Log;
import androidx.lifecycle.LiveData;
import java.util.Iterator;
import java.util.List;
import z9.AppToShow;
import z9.MaliciousDetailRecord;
import z9.MaliciousRecord;
import z9.Record;
import z9.UnstableRecord;

/* compiled from: RecordDao.java */
/* renamed from: y9.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class RecordDao {
    public abstract void A(AppToShow... appToShowArr);

    public abstract List<Long> B(List<AppToShow> list);

    public abstract void C(List<MaliciousDetailRecord> list);

    public abstract void D(MaliciousRecord maliciousRecord);

    public abstract void E(Record record);

    public abstract void F(UnstableRecord unstableRecord);

    public abstract long G();

    public abstract List<MaliciousRecord> H(String str, String str2, long j10, String str3);

    public abstract long I();

    public abstract int J(boolean z10, List<String> list, boolean z11);

    public abstract int K(boolean z10, String str, boolean z11);

    public void L(ArrayMap<String, MaliciousRecord> arrayMap) {
        Iterator<String> it = arrayMap.keySet().iterator();
        while (it.hasNext()) {
            MaliciousRecord maliciousRecord = arrayMap.get(it.next());
            List<MaliciousRecord> H = H(maliciousRecord.f20318c, maliciousRecord.f20317b, maliciousRecord.f20321f, maliciousRecord.f20322g);
            if (H != null && !H.isEmpty()) {
                M(maliciousRecord.f20318c, maliciousRecord.f20317b, maliciousRecord.f20321f, maliciousRecord.f20320e, maliciousRecord.f20319d + H.get(0).f20319d, maliciousRecord.f20322g);
            } else {
                D(maliciousRecord);
            }
        }
    }

    public abstract void M(String str, String str2, long j10, long j11, long j12, String str3);

    public abstract void N(String str, String str2, String str3, String str4, long j10, long j11, long j12, long j13, long j14, String str5);

    public abstract void O(UnstableRecord unstableRecord);

    public abstract void a();

    public void b(long j10, long j11) {
        long G = G();
        if (G >= j11) {
            long I = I();
            if (I != j10) {
                Log.w("StartupManager", "need to clean MaliciousDetailRecord, size: " + G + "date" + I);
                d(I);
            }
        }
    }

    public abstract void c(String str);

    public abstract void d(long j10);

    public abstract void e(long j10, long j11);

    public abstract void f(long j10, long j11);

    public abstract void g(long j10, long j11);

    public abstract void h(long j10, String str);

    public abstract void i(String str);

    public abstract void j(String str);

    public abstract void k(String str);

    public abstract LiveData<List<AppToShow>> l(boolean z10, boolean z11);

    public abstract List<AppToShow> m(boolean z10, boolean z11, boolean z12);

    public abstract List<AppToShow> n(boolean z10);

    public abstract List<Record> o();

    public abstract LiveData<List<Record>> p();

    public abstract List<Record> q();

    public abstract LiveData<List<Record>> r();

    public abstract List<AppToShow> s(boolean z10, boolean z11);

    public abstract LiveData<List<AppToShow>> t(boolean z10, boolean z11, boolean z12);

    public abstract List<AppToShow> u(boolean z10, boolean z11, boolean z12);

    public abstract List<Record> v(String str, Long l10);

    public abstract List<Record> w(String str, Long l10, Long l11);

    public abstract List<Record> x(String str, String str2, String str3, String str4, Long l10, String str5);

    public abstract List<UnstableRecord> y();

    public abstract List<UnstableRecord> z(String str);
}
