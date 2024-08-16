package com.oplus.sceneservice.sdk.sceneprovider.api;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Keep;
import java.util.List;
import kotlin.Metadata;

/* compiled from: IClient.kt */
@Keep
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u0000 \n2\u00020\u0001:\u0001\u000bJ\"\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\u0010\u0010\u0006\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0018\u00010\u0004H\u0017J\u001a\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0006\u001a\u0004\u0018\u00010\tH&¨\u0006\f"}, d2 = {"Lcom/oplus/sceneservice/sdk/sceneprovider/api/IClient;", "", "", "sceneEventId", "", "Landroid/os/Parcelable;", "sceneData", "Lma/f0;", "handleSceneEvent", "Landroid/os/Bundle;", "Companion", "a", "scenesdk_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes2.dex */
public interface IClient {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = Companion.f10466a;
    public static final String KEY_SCENE_STATUS_DATA = "scene_status_data";

    /* compiled from: IClient.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Lcom/oplus/sceneservice/sdk/sceneprovider/api/IClient$a;", "", "<init>", "()V", "scenesdk_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: com.oplus.sceneservice.sdk.sceneprovider.api.IClient$a, reason: from kotlin metadata */
    /* loaded from: classes2.dex */
    public static final class Companion {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ Companion f10466a = new Companion();

        private Companion() {
        }
    }

    void handleSceneEvent(int i10, Bundle bundle);

    void handleSceneEvent(int i10, List<? extends Parcelable> list);
}
