package com.android.server.devicepolicy;

import android.app.admin.Authority;
import android.app.admin.DeviceAdminAuthority;
import android.app.admin.DpcAuthority;
import android.app.admin.RoleAuthority;
import android.app.admin.UnknownAuthority;
import android.content.ComponentName;
import android.os.UserHandle;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.role.RoleManagerLocal;
import com.android.server.LocalManagerRegistry;
import com.android.server.utils.Slogf;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class EnforcingAdmin {
    private static final String ATTR_AUTHORITIES = "authorities";
    private static final String ATTR_AUTHORITIES_SEPARATOR = ";";
    private static final String ATTR_CLASS_NAME = "class-name";
    private static final String ATTR_IS_ROLE = "is-role";
    private static final String ATTR_PACKAGE_NAME = "package-name";
    private static final String ATTR_USER_ID = "user-id";
    static final String DEFAULT_AUTHORITY = "default";
    static final String DEVICE_ADMIN_AUTHORITY = "device_admin";
    static final String DPC_AUTHORITY = "enterprise";
    static final String ROLE_AUTHORITY_PREFIX = "role:";
    static final String TAG = "EnforcingAdmin";
    private final ActiveAdmin mActiveAdmin;
    private Set<String> mAuthorities;
    private final ComponentName mComponentName;
    private final boolean mIsRoleAuthority;
    private final String mPackageName;
    private final int mUserId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static EnforcingAdmin createEnforcingAdmin(String str, int i, ActiveAdmin activeAdmin) {
        Objects.requireNonNull(str);
        return new EnforcingAdmin(str, i, activeAdmin);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static EnforcingAdmin createEnterpriseEnforcingAdmin(ComponentName componentName, int i) {
        Objects.requireNonNull(componentName);
        return new EnforcingAdmin(componentName.getPackageName(), componentName, Set.of(DPC_AUTHORITY), i, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static EnforcingAdmin createEnterpriseEnforcingAdmin(ComponentName componentName, int i, ActiveAdmin activeAdmin) {
        Objects.requireNonNull(componentName);
        return new EnforcingAdmin(componentName.getPackageName(), componentName, Set.of(DPC_AUTHORITY), i, activeAdmin);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static EnforcingAdmin createDeviceAdminEnforcingAdmin(ComponentName componentName, int i, ActiveAdmin activeAdmin) {
        Objects.requireNonNull(componentName);
        return new EnforcingAdmin(componentName.getPackageName(), componentName, Set.of(DEVICE_ADMIN_AUTHORITY), i, activeAdmin);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getRoleAuthorityOf(String str) {
        return ROLE_AUTHORITY_PREFIX + str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Authority getParcelableAuthority(String str) {
        if (str == null || str.isEmpty()) {
            return UnknownAuthority.UNKNOWN_AUTHORITY;
        }
        if (DPC_AUTHORITY.equals(str)) {
            return DpcAuthority.DPC_AUTHORITY;
        }
        if (DEVICE_ADMIN_AUTHORITY.equals(str)) {
            return DeviceAdminAuthority.DEVICE_ADMIN_AUTHORITY;
        }
        if (str.startsWith(ROLE_AUTHORITY_PREFIX)) {
            return new RoleAuthority(Set.of(str.substring(5)));
        }
        return UnknownAuthority.UNKNOWN_AUTHORITY;
    }

    private EnforcingAdmin(String str, ComponentName componentName, Set<String> set, int i, ActiveAdmin activeAdmin) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(set);
        this.mIsRoleAuthority = false;
        this.mPackageName = str;
        this.mComponentName = componentName;
        this.mAuthorities = new HashSet(set);
        this.mUserId = i;
        this.mActiveAdmin = activeAdmin;
    }

    private EnforcingAdmin(String str, int i, ActiveAdmin activeAdmin) {
        Objects.requireNonNull(str);
        this.mIsRoleAuthority = true;
        this.mPackageName = str;
        this.mUserId = i;
        this.mComponentName = null;
        this.mAuthorities = null;
        this.mActiveAdmin = activeAdmin;
    }

    private static Set<String> getRoleAuthoritiesOrDefault(String str, int i) {
        Set<String> roles = getRoles(str, i);
        HashSet hashSet = new HashSet();
        Iterator<String> it = roles.iterator();
        while (it.hasNext()) {
            hashSet.add(ROLE_AUTHORITY_PREFIX + it.next());
        }
        return hashSet.isEmpty() ? Set.of(DEFAULT_AUTHORITY) : hashSet;
    }

    private static Set<String> getRoles(String str, int i) {
        RoleManagerLocal roleManagerLocal = (RoleManagerLocal) LocalManagerRegistry.getManager(RoleManagerLocal.class);
        HashSet hashSet = new HashSet();
        Map rolesAndHolders = roleManagerLocal.getRolesAndHolders(i);
        for (String str2 : rolesAndHolders.keySet()) {
            if (((Set) rolesAndHolders.get(str2)).contains(str)) {
                hashSet.add(str2);
            }
        }
        return hashSet;
    }

    private Set<String> getAuthorities() {
        if (this.mAuthorities == null && this.mIsRoleAuthority) {
            this.mAuthorities = getRoleAuthoritiesOrDefault(this.mPackageName, this.mUserId);
        }
        return this.mAuthorities;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reloadRoleAuthorities() {
        if (this.mIsRoleAuthority) {
            this.mAuthorities = getRoleAuthoritiesOrDefault(this.mPackageName, this.mUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAuthority(String str) {
        return getAuthorities().contains(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getPackageName() {
        return this.mPackageName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getUserId() {
        return this.mUserId;
    }

    public ActiveAdmin getActiveAdmin() {
        return this.mActiveAdmin;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public android.app.admin.EnforcingAdmin getParcelableAdmin() {
        UnknownAuthority unknownAuthority;
        if (this.mIsRoleAuthority) {
            Set<String> roles = getRoles(this.mPackageName, this.mUserId);
            if (roles.isEmpty()) {
                unknownAuthority = UnknownAuthority.UNKNOWN_AUTHORITY;
            } else {
                unknownAuthority = new RoleAuthority(roles);
            }
        } else if (this.mAuthorities.contains(DPC_AUTHORITY)) {
            unknownAuthority = DpcAuthority.DPC_AUTHORITY;
        } else if (this.mAuthorities.contains(DEVICE_ADMIN_AUTHORITY)) {
            unknownAuthority = DeviceAdminAuthority.DEVICE_ADMIN_AUTHORITY;
        } else {
            unknownAuthority = UnknownAuthority.UNKNOWN_AUTHORITY;
        }
        return new android.app.admin.EnforcingAdmin(this.mPackageName, unknownAuthority, UserHandle.of(this.mUserId));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || EnforcingAdmin.class != obj.getClass()) {
            return false;
        }
        EnforcingAdmin enforcingAdmin = (EnforcingAdmin) obj;
        return Objects.equals(this.mPackageName, enforcingAdmin.mPackageName) && Objects.equals(this.mComponentName, enforcingAdmin.mComponentName) && Objects.equals(Boolean.valueOf(this.mIsRoleAuthority), Boolean.valueOf(enforcingAdmin.mIsRoleAuthority)) && hasMatchingAuthorities(this, enforcingAdmin);
    }

    private static boolean hasMatchingAuthorities(EnforcingAdmin enforcingAdmin, EnforcingAdmin enforcingAdmin2) {
        if (enforcingAdmin.mIsRoleAuthority && enforcingAdmin2.mIsRoleAuthority) {
            return true;
        }
        return enforcingAdmin.getAuthorities().equals(enforcingAdmin2.getAuthorities());
    }

    public int hashCode() {
        if (this.mIsRoleAuthority) {
            return Objects.hash(this.mPackageName, Integer.valueOf(this.mUserId));
        }
        Object[] objArr = new Object[3];
        Object obj = this.mComponentName;
        if (obj == null) {
            obj = this.mPackageName;
        }
        objArr[0] = obj;
        objArr[1] = Integer.valueOf(this.mUserId);
        objArr[2] = getAuthorities();
        return Objects.hash(objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveToXml(TypedXmlSerializer typedXmlSerializer) throws IOException {
        typedXmlSerializer.attribute((String) null, ATTR_PACKAGE_NAME, this.mPackageName);
        typedXmlSerializer.attributeBoolean((String) null, ATTR_IS_ROLE, this.mIsRoleAuthority);
        typedXmlSerializer.attributeInt((String) null, ATTR_USER_ID, this.mUserId);
        if (this.mIsRoleAuthority) {
            return;
        }
        ComponentName componentName = this.mComponentName;
        if (componentName != null) {
            typedXmlSerializer.attribute((String) null, ATTR_CLASS_NAME, componentName.getClassName());
        }
        typedXmlSerializer.attribute((String) null, ATTR_AUTHORITIES, String.join(ATTR_AUTHORITIES_SEPARATOR, getAuthorities()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static EnforcingAdmin readFromXml(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException {
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_PACKAGE_NAME);
        boolean attributeBoolean = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_IS_ROLE);
        String attributeValue2 = typedXmlPullParser.getAttributeValue((String) null, ATTR_AUTHORITIES);
        int attributeInt = typedXmlPullParser.getAttributeInt((String) null, ATTR_USER_ID);
        if (attributeBoolean) {
            if (attributeValue == null) {
                Slogf.wtf(TAG, "Error parsing EnforcingAdmin with RoleAuthority, packageName is null.");
                return null;
            }
            return new EnforcingAdmin(attributeValue, attributeInt, null);
        }
        if (attributeValue == null || attributeValue2 == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error parsing EnforcingAdmin, packageName is ");
            if (attributeValue == null) {
                attributeValue = "null";
            }
            sb.append(attributeValue);
            sb.append(", and authorities is ");
            if (attributeValue2 == null) {
                attributeValue2 = "null";
            }
            sb.append(attributeValue2);
            sb.append(".");
            Slogf.wtf(TAG, sb.toString());
            return null;
        }
        String attributeValue3 = typedXmlPullParser.getAttributeValue((String) null, ATTR_CLASS_NAME);
        return new EnforcingAdmin(attributeValue, attributeValue3 != null ? new ComponentName(attributeValue, attributeValue3) : null, Set.of((Object[]) attributeValue2.split(ATTR_AUTHORITIES_SEPARATOR)), attributeInt, null);
    }

    public String toString() {
        return "EnforcingAdmin { mPackageName= " + this.mPackageName + ", mComponentName= " + this.mComponentName + ", mAuthorities= " + this.mAuthorities + ", mUserId= " + this.mUserId + ", mIsRoleAuthority= " + this.mIsRoleAuthority + " }";
    }
}
