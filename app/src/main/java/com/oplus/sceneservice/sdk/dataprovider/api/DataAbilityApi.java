package com.oplus.sceneservice.sdk.dataprovider.api;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import androidx.annotation.Keep;
import com.oplus.sceneservice.sdk.dataprovider.bean.PhoneStatusInfo;
import com.oplus.sceneservice.sdk.dataprovider.bean.SceneStatusInfo;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData;
import i9.PhoneStatusManager;
import i9.SceneDataManager;
import i9.SceneStatusManager;
import i9.SettingInterface;
import i9.UserProfileManager;
import java.util.List;
import kotlin.Metadata;
import za.k;

/* compiled from: DataAbilityApi.kt */
@Keep
@Metadata(bv = {}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b)\u0010*J\b\u0010\u0003\u001a\u00020\u0002H\u0007J\u0016\u0010\t\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006J\u0010\u0010\r\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000b\u001a\u00020\nJ\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\nJ\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\nJ\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u000b\u001a\u00020\nJ\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u000b\u001a\u00020\nJ\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u000b\u001a\u00020\nJ\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u000b\u001a\u00020\nJ\u001e\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u001c\u0018\u00010\u001b2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\u0019J \u0010\u001f\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u0011J\u001e\u0010#\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020\u0002J\u0016\u0010$\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010!\u001a\u00020 J\u001e\u0010%\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020\u0002J\u0016\u0010&\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010!\u001a\u00020 R\u0014\u0010'\u001a\u00020\u00118\u0002X\u0082T¢\u0006\u0006\n\u0004\b'\u0010(¨\u0006+"}, d2 = {"Lcom/oplus/sceneservice/sdk/dataprovider/api/DataAbilityApi;", "", "", "getStatementStateSync", "Landroid/app/Activity;", "activity", "", "requestCode", "Lma/f0;", "authorizeStatementState", "Landroid/content/Context;", "context", "Lcom/oplus/sceneservice/sdk/dataprovider/bean/PhoneStatusInfo;", "queryPhoneStatus", "sceneId", "Lcom/oplus/sceneservice/sdk/dataprovider/bean/SceneStatusInfo;", "querySceneStatusById", "", "sceneName", "querySceneStatusByName", "Lcom/oplus/sceneservice/sdk/dataprovider/bean/UserProfileInfo;", "querySmartUserProfile", "queryManualUserProfile", "queryPureManualProfile", "queryFinalUserProfile", "", "type", "", "Lcom/oplus/sceneservice/sdk/dataprovider/bean/scene/SceneData;", "querySceneDataWithType", "matchKey", "querySceneData", "Landroid/database/ContentObserver;", "observer", "notifyForDescendants", "registerFinalUserProfileObserver", "unregisterFinalUserProfileObserver", "registerPhoneStatusObserver", "unregisterPhoneStatusObserver", "TAG", "Ljava/lang/String;", "<init>", "()V", "scenesdk_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes2.dex */
public final class DataAbilityApi {
    public static final DataAbilityApi INSTANCE = new DataAbilityApi();
    private static final String TAG = "DataAbilityApi";

    private DataAbilityApi() {
    }

    public final void authorizeStatementState(Activity activity, int i10) {
        k.e(activity, "activity");
        SettingInterface.a(activity, i10);
    }

    public final boolean getStatementStateSync() {
        return SettingInterface.c();
    }

    public final UserProfileInfo queryFinalUserProfile(Context context) {
        k.e(context, "context");
        return UserProfileManager.g(context).i();
    }

    public final UserProfileInfo queryManualUserProfile(Context context) {
        k.e(context, "context");
        return UserProfileManager.g(context).j();
    }

    public final PhoneStatusInfo queryPhoneStatus(Context context) {
        k.e(context, "context");
        return PhoneStatusManager.g(context).h();
    }

    public final UserProfileInfo queryPureManualProfile(Context context) {
        k.e(context, "context");
        return UserProfileManager.g(context).k();
    }

    public final SceneData querySceneData(Context context, int type, String matchKey) {
        k.e(context, "context");
        k.e(matchKey, "matchKey");
        return SceneDataManager.h(context).j(type, matchKey);
    }

    public final List<SceneData> querySceneDataWithType(Context context, int[] type) {
        k.e(context, "context");
        k.e(type, "type");
        return SceneDataManager.h(context).k(type);
    }

    public final SceneStatusInfo querySceneStatusById(int sceneId, Context context) {
        k.e(context, "context");
        return SceneStatusManager.g(context).h(sceneId);
    }

    public final SceneStatusInfo querySceneStatusByName(String sceneName, Context context) {
        k.e(sceneName, "sceneName");
        k.e(context, "context");
        return SceneStatusManager.g(context).i(sceneName);
    }

    public final UserProfileInfo querySmartUserProfile(Context context) {
        k.e(context, "context");
        return UserProfileManager.g(context).l();
    }

    public final boolean registerFinalUserProfileObserver(Context context, ContentObserver observer, boolean notifyForDescendants) {
        k.e(context, "context");
        k.e(observer, "observer");
        return UserProfileManager.g(context).m(context, observer, notifyForDescendants);
    }

    public final boolean registerPhoneStatusObserver(Context context, ContentObserver observer, boolean notifyForDescendants) {
        k.e(context, "context");
        k.e(observer, "observer");
        return PhoneStatusManager.g(context).i(context, observer, notifyForDescendants);
    }

    public final boolean unregisterFinalUserProfileObserver(Context context, ContentObserver observer) {
        k.e(context, "context");
        k.e(observer, "observer");
        return UserProfileManager.g(context).n(context, observer);
    }

    public final boolean unregisterPhoneStatusObserver(Context context, ContentObserver observer) {
        k.e(context, "context");
        k.e(observer, "observer");
        return PhoneStatusManager.g(context).j(context, observer);
    }
}
