package k9;

import android.os.Bundle;
import android.os.Parcelable;
import com.oplus.sceneservice.sdk.sceneprovider.api.IClient;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.Metadata;
import kotlin.collections.p;
import l9.LogUtils;

/* compiled from: ClientManager.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nJ \u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\u0010\u0010\u0006\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0018\u00010\u0004¨\u0006\u000b"}, d2 = {"Lk9/a;", "", "", "sceneId", "", "Landroid/os/Parcelable;", "sceneData", "Lma/f0;", "a", "<init>", "()V", "scenesdk_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: k9.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class ClientManager {

    /* renamed from: a, reason: collision with root package name */
    public static final ClientManager f14219a = new ClientManager();

    /* renamed from: b, reason: collision with root package name */
    private static final List<IClient> f14220b = new CopyOnWriteArrayList();

    private ClientManager() {
    }

    public final void a(int i10, List<? extends Parcelable> list) {
        Parcelable parcelable = list == null ? null : (Parcelable) p.V(list);
        Objects.requireNonNull(parcelable, "null cannot be cast to non-null type android.os.Bundle");
        Bundle bundle = (Bundle) parcelable;
        for (IClient iClient : f14220b) {
            iClient.handleSceneEvent(i10, list);
            iClient.handleSceneEvent(i10, bundle);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("dispatchSceneDataToClient:");
        sb2.append(f14220b.size());
        sb2.append(" + data.size=");
        sb2.append(list != null ? Integer.valueOf(list.size()) : null);
        LogUtils.a("ClientManager", sb2.toString());
    }
}
