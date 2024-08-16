package android.content;

import android.common.OplusFeatureCache;
import android.net.Uri;
import com.oplus.permission.IOplusPermissionCheckInjector;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ContentResolverExtImpl implements IContentResolverExt {
    public ContentResolverExtImpl(Object obj) {
    }

    public boolean hookQuery(Uri uri, int pid, int uid, String access) {
        return OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkUriPermission(uri, pid, uid, access);
    }

    public boolean hookInsert(Uri uri, int pid, int uid, String access) {
        return OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkUriPermission(uri, pid, uid, access);
    }

    public boolean hookDelete(Uri uri, int pid, int uid, String access) {
        return OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkUriPermission(uri, pid, uid, access);
    }

    public boolean hookApplyBatch(ArrayList<ContentProviderOperation> operations, int pid, int uid, String access) {
        return OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkApplyBatchPermission(operations, pid, uid, access);
    }
}
