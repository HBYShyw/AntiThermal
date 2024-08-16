package com.android.server.textclassifier;

import android.net.Uri;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.pm.PackageManagerService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class IconsUriHelper {
    public static final String AUTHORITY = "com.android.textclassifier.icons";
    private static final String TAG = "IconsUriHelper";
    private final Supplier<String> mIdSupplier;

    @GuardedBy({"mPackageIds"})
    private final Map<String, String> mPackageIds;
    private static final Supplier<String> DEFAULT_ID_SUPPLIER = new Supplier() { // from class: com.android.server.textclassifier.IconsUriHelper$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            String lambda$static$0;
            lambda$static$0 = IconsUriHelper.lambda$static$0();
            return lambda$static$0;
        }
    };
    private static final IconsUriHelper sSingleton = new IconsUriHelper(null);

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$static$0() {
        return UUID.randomUUID().toString();
    }

    private IconsUriHelper(Supplier<String> supplier) {
        ArrayMap arrayMap = new ArrayMap();
        this.mPackageIds = arrayMap;
        this.mIdSupplier = supplier == null ? DEFAULT_ID_SUPPLIER : supplier;
        arrayMap.put(PackageManagerService.PLATFORM_PACKAGE_NAME, PackageManagerService.PLATFORM_PACKAGE_NAME);
    }

    public static IconsUriHelper newInstanceForTesting(Supplier<String> supplier) {
        return new IconsUriHelper(supplier);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IconsUriHelper getInstance() {
        return sSingleton;
    }

    public Uri getContentUri(String str, int i) {
        Uri build;
        Objects.requireNonNull(str);
        synchronized (this.mPackageIds) {
            if (!this.mPackageIds.containsKey(str)) {
                this.mPackageIds.put(str, this.mIdSupplier.get());
            }
            build = new Uri.Builder().scheme("content").authority(AUTHORITY).path(this.mPackageIds.get(str)).appendPath(Integer.toString(i)).build();
        }
        return build;
    }

    public ResourceInfo getResourceInfo(Uri uri) {
        if (!"content".equals(uri.getScheme()) || !AUTHORITY.equals(uri.getAuthority())) {
            return null;
        }
        List<String> pathSegments = uri.getPathSegments();
        try {
        } catch (Exception e) {
            Log.v(TAG, "Could not get resource info. Reason: " + e.getMessage());
        }
        synchronized (this.mPackageIds) {
            String str = pathSegments.get(0);
            int parseInt = Integer.parseInt(pathSegments.get(1));
            for (String str2 : this.mPackageIds.keySet()) {
                if (str.equals(this.mPackageIds.get(str2))) {
                    return new ResourceInfo(str2, parseInt);
                }
            }
            return null;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ResourceInfo {
        public final int id;
        public final String packageName;

        private ResourceInfo(String str, int i) {
            Objects.requireNonNull(str);
            this.packageName = str;
            this.id = i;
        }
    }
}
