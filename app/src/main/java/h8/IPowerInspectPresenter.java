package h8;

import a7.PowerConsumeStats;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import com.oplus.powermanager.powerusage.view.PowerCheckboxPreference;
import java.util.ArrayList;
import t8.PowerUsageManager;

/* compiled from: IPowerInspectPresenter.java */
/* renamed from: h8.e, reason: use source file name */
/* loaded from: classes.dex */
public interface IPowerInspectPresenter extends PowerUsageManager.e, PowerCheckboxPreference.b, View.OnClickListener {
    ArrayList<PackageInfo> Q(ArrayList<PowerConsumeStats.b> arrayList);

    void V();

    void onCreate(Bundle bundle);

    void onDestroy();
}
