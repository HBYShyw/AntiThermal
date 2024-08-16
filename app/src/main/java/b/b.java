package b;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import b.ActivityResultContract;
import com.oplus.backup.sdk.common.utils.Constants;
import fb._Ranges;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.MapsJVM;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import ma.o;
import ma.u;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ActivityResultContracts.kt */
@Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\u0010$\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\u0018\u00002 \u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u0002\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u00040\u0001:\u0001\u0015B\u0007¢\u0006\u0004\b\u0013\u0010\u0014J%\u0010\n\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0016¢\u0006\u0004\b\n\u0010\u000bJ9\u0010\r\u001a\u0016\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u0004\u0018\u00010\f2\u0006\u0010\u0007\u001a\u00020\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0016¢\u0006\u0004\b\r\u0010\u000eJ&\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0010\u001a\u00020\u000f2\b\u0010\u0011\u001a\u0004\u0018\u00010\tH\u0016¨\u0006\u0016"}, d2 = {"Lb/b;", "Lb/a;", "", "", "", "", "Landroid/content/Context;", "context", "input", "Landroid/content/Intent;", "d", "(Landroid/content/Context;[Ljava/lang/String;)Landroid/content/Intent;", "Lb/a$a;", "e", "(Landroid/content/Context;[Ljava/lang/String;)Lb/a$a;", "", "resultCode", Constants.MessagerConstants.INTENT_KEY, "f", "<init>", "()V", "a", "activity_release"}, k = 1, mv = {1, 7, 1})
/* loaded from: classes.dex */
public final class b extends ActivityResultContract<String[], Map<String, Boolean>> {

    /* renamed from: a, reason: collision with root package name */
    public static final a f4516a = new a(null);

    /* compiled from: ActivityResultContracts.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ\u001d\u0010\u0006\u001a\u00020\u00052\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0000¢\u0006\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\u00038\u0006X\u0086T¢\u0006\u0006\n\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u00038\u0006X\u0086T¢\u0006\u0006\n\u0004\b\n\u0010\tR\u0014\u0010\u000b\u001a\u00020\u00038\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000b\u0010\t¨\u0006\u000e"}, d2 = {"Lb/b$a;", "", "", "", "input", "Landroid/content/Intent;", "a", "([Ljava/lang/String;)Landroid/content/Intent;", "ACTION_REQUEST_PERMISSIONS", "Ljava/lang/String;", "EXTRA_PERMISSIONS", "EXTRA_PERMISSION_GRANT_RESULTS", "<init>", "()V", "activity_release"}, k = 1, mv = {1, 7, 1})
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Intent a(String[] input) {
            k.e(input, "input");
            Intent putExtra = new Intent("androidx.activity.result.contract.action.REQUEST_PERMISSIONS").putExtra("androidx.activity.result.contract.extra.PERMISSIONS", input);
            k.d(putExtra, "Intent(ACTION_REQUEST_PE…EXTRA_PERMISSIONS, input)");
            return putExtra;
        }
    }

    @Override // b.ActivityResultContract
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public Intent a(Context context, String[] input) {
        k.e(context, "context");
        k.e(input, "input");
        return f4516a.a(input);
    }

    @Override // b.ActivityResultContract
    /* renamed from: e, reason: merged with bridge method [inline-methods] */
    public ActivityResultContract.a<Map<String, Boolean>> b(Context context, String[] input) {
        int e10;
        int c10;
        Map i10;
        k.e(context, "context");
        k.e(input, "input");
        boolean z10 = true;
        if (input.length == 0) {
            i10 = m0.i();
            return new ActivityResultContract.a<>(i10);
        }
        int length = input.length;
        int i11 = 0;
        while (true) {
            if (i11 >= length) {
                break;
            }
            if (!(ContextCompat.a(context, input[i11]) == 0)) {
                z10 = false;
                break;
            }
            i11++;
        }
        if (!z10) {
            return null;
        }
        e10 = MapsJVM.e(input.length);
        c10 = _Ranges.c(e10, 16);
        LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
        for (String str : input) {
            o a10 = u.a(str, Boolean.TRUE);
            linkedHashMap.put(a10.c(), a10.d());
        }
        return new ActivityResultContract.a<>(linkedHashMap);
    }

    @Override // b.ActivityResultContract
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public Map<String, Boolean> c(int resultCode, Intent intent) {
        Map<String, Boolean> i10;
        List y4;
        List G0;
        Map<String, Boolean> q10;
        Map<String, Boolean> i11;
        Map<String, Boolean> i12;
        if (resultCode != -1) {
            i12 = m0.i();
            return i12;
        }
        if (intent == null) {
            i11 = m0.i();
            return i11;
        }
        String[] stringArrayExtra = intent.getStringArrayExtra("androidx.activity.result.contract.extra.PERMISSIONS");
        int[] intArrayExtra = intent.getIntArrayExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS");
        if (intArrayExtra != null && stringArrayExtra != null) {
            ArrayList arrayList = new ArrayList(intArrayExtra.length);
            for (int i13 : intArrayExtra) {
                arrayList.add(Boolean.valueOf(i13 == 0));
            }
            y4 = _Arrays.y(stringArrayExtra);
            G0 = _Collections.G0(y4, arrayList);
            q10 = m0.q(G0);
            return q10;
        }
        i10 = m0.i();
        return i10;
    }
}
