package com.android.server.companion;

import android.companion.AssociationInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface AssociationStore {
    public static final int CHANGE_TYPE_ADDED = 0;
    public static final int CHANGE_TYPE_REMOVED = 1;
    public static final int CHANGE_TYPE_UPDATED_ADDRESS_CHANGED = 2;
    public static final int CHANGE_TYPE_UPDATED_ADDRESS_UNCHANGED = 3;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface ChangeType {
    }

    AssociationInfo getAssociationById(int i);

    Collection<AssociationInfo> getAssociations();

    List<AssociationInfo> getAssociationsByAddress(String str);

    List<AssociationInfo> getAssociationsForPackage(int i, String str);

    AssociationInfo getAssociationsForPackageWithAddress(int i, String str, String str2);

    List<AssociationInfo> getAssociationsForUser(int i);

    void registerListener(OnChangeListener onChangeListener);

    void unregisterListener(OnChangeListener onChangeListener);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface OnChangeListener {
        default void onAssociationAdded(AssociationInfo associationInfo) {
        }

        default void onAssociationRemoved(AssociationInfo associationInfo) {
        }

        default void onAssociationUpdated(AssociationInfo associationInfo, boolean z) {
        }

        default void onAssociationChanged(int i, AssociationInfo associationInfo) {
            if (i == 0) {
                onAssociationAdded(associationInfo);
                return;
            }
            if (i == 1) {
                onAssociationRemoved(associationInfo);
            } else if (i == 2) {
                onAssociationUpdated(associationInfo, true);
            } else {
                if (i != 3) {
                    return;
                }
                onAssociationUpdated(associationInfo, false);
            }
        }
    }

    static String changeTypeToString(int i) {
        if (i == 0) {
            return "ASSOCIATION_ADDED";
        }
        if (i == 1) {
            return "ASSOCIATION_REMOVED";
        }
        if (i == 2) {
            return "ASSOCIATION_UPDATED";
        }
        if (i == 3) {
            return "ASSOCIATION_UPDATED_ADDRESS_UNCHANGED";
        }
        return "Unknown (" + i + ")";
    }
}
