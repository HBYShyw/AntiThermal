package com.android.server.pm;

import android.os.Bundle;
import android.os.UserManager;
import android.util.IntArray;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.BundleUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RestrictionsSet {
    private static final String TAG_RESTRICTIONS = "restrictions";
    private static final String TAG_RESTRICTIONS_USER = "restrictions_user";
    private static final String USER_ID = "user_id";
    private final SparseArray<Bundle> mUserRestrictions;

    public RestrictionsSet() {
        this.mUserRestrictions = new SparseArray<>(0);
    }

    public RestrictionsSet(int i, Bundle bundle) {
        SparseArray<Bundle> sparseArray = new SparseArray<>(0);
        this.mUserRestrictions = sparseArray;
        if (bundle.isEmpty()) {
            throw new IllegalArgumentException("empty restriction bundle cannot be added.");
        }
        sparseArray.put(i, bundle);
    }

    public boolean updateRestrictions(int i, Bundle bundle) {
        if (!(!UserRestrictionsUtils.areEqual(this.mUserRestrictions.get(i), bundle))) {
            return false;
        }
        if (!BundleUtils.isEmpty(bundle)) {
            this.mUserRestrictions.put(i, bundle);
        } else {
            this.mUserRestrictions.delete(i);
        }
        return true;
    }

    public boolean removeRestrictionsForAllUsers(String str) {
        boolean z = false;
        for (int i = 0; i < this.mUserRestrictions.size(); i++) {
            Bundle valueAt = this.mUserRestrictions.valueAt(i);
            if (UserRestrictionsUtils.contains(valueAt, str)) {
                valueAt.remove(str);
                z = true;
            }
        }
        return z;
    }

    public void moveRestriction(RestrictionsSet restrictionsSet, String str) {
        int i = 0;
        while (i < this.mUserRestrictions.size()) {
            int keyAt = this.mUserRestrictions.keyAt(i);
            Bundle valueAt = this.mUserRestrictions.valueAt(i);
            if (UserRestrictionsUtils.contains(valueAt, str)) {
                valueAt.remove(str);
                Bundle restrictions = restrictionsSet.getRestrictions(keyAt);
                if (restrictions == null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(str, true);
                    restrictionsSet.updateRestrictions(keyAt, bundle);
                } else {
                    restrictions.putBoolean(str, true);
                }
                if (valueAt.isEmpty()) {
                    this.mUserRestrictions.removeAt(i);
                    i--;
                }
            }
            i++;
        }
    }

    public boolean isEmpty() {
        return this.mUserRestrictions.size() == 0;
    }

    public Bundle mergeAll() {
        Bundle bundle = new Bundle();
        for (int i = 0; i < this.mUserRestrictions.size(); i++) {
            UserRestrictionsUtils.merge(bundle, this.mUserRestrictions.valueAt(i));
        }
        return bundle;
    }

    public List<UserManager.EnforcingUser> getEnforcingUsers(String str, int i) {
        ArrayList arrayList = new ArrayList();
        if (getRestrictionsNonNull(i).containsKey(str)) {
            arrayList.add(new UserManager.EnforcingUser(i, 4));
        }
        if (getRestrictionsNonNull(-1).containsKey(str)) {
            arrayList.add(new UserManager.EnforcingUser(-1, 2));
        }
        return arrayList;
    }

    public Bundle getRestrictions(int i) {
        return this.mUserRestrictions.get(i);
    }

    public Bundle getRestrictionsNonNull(int i) {
        return UserRestrictionsUtils.nonNull(this.mUserRestrictions.get(i));
    }

    public boolean remove(int i) {
        boolean contains = this.mUserRestrictions.contains(i);
        this.mUserRestrictions.remove(i);
        return contains;
    }

    public void removeAllRestrictions() {
        this.mUserRestrictions.clear();
    }

    public void writeRestrictions(TypedXmlSerializer typedXmlSerializer, String str) throws IOException {
        typedXmlSerializer.startTag((String) null, str);
        for (int i = 0; i < this.mUserRestrictions.size(); i++) {
            typedXmlSerializer.startTag((String) null, TAG_RESTRICTIONS_USER);
            typedXmlSerializer.attributeInt((String) null, USER_ID, this.mUserRestrictions.keyAt(i));
            UserRestrictionsUtils.writeRestrictions(typedXmlSerializer, this.mUserRestrictions.valueAt(i), TAG_RESTRICTIONS);
            typedXmlSerializer.endTag((String) null, TAG_RESTRICTIONS_USER);
        }
        typedXmlSerializer.endTag((String) null, str);
    }

    public static RestrictionsSet readRestrictions(TypedXmlPullParser typedXmlPullParser, String str) throws IOException, XmlPullParserException {
        RestrictionsSet restrictionsSet = new RestrictionsSet();
        int i = 0;
        while (true) {
            int next = typedXmlPullParser.next();
            if (next != 1) {
                String name = typedXmlPullParser.getName();
                if (next == 3 && str.equals(name)) {
                    return restrictionsSet;
                }
                if (next == 2 && TAG_RESTRICTIONS_USER.equals(name)) {
                    i = typedXmlPullParser.getAttributeInt((String) null, USER_ID);
                } else if (next == 2 && TAG_RESTRICTIONS.equals(name)) {
                    restrictionsSet.updateRestrictions(i, UserRestrictionsUtils.readRestrictions(typedXmlPullParser));
                }
            } else {
                throw new XmlPullParserException("restrictions cannot be read as xml is malformed.");
            }
        }
    }

    public void dumpRestrictions(PrintWriter printWriter, String str) {
        boolean z = true;
        int i = 0;
        while (i < this.mUserRestrictions.size()) {
            printWriter.println(str + "User Id: " + this.mUserRestrictions.keyAt(i));
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("  ");
            UserRestrictionsUtils.dumpRestrictions(printWriter, sb.toString(), this.mUserRestrictions.valueAt(i));
            i++;
            z = false;
        }
        if (z) {
            printWriter.println(str + "none");
        }
    }

    public IntArray getUserIds() {
        IntArray intArray = new IntArray(this.mUserRestrictions.size());
        for (int i = 0; i < this.mUserRestrictions.size(); i++) {
            intArray.add(this.mUserRestrictions.keyAt(i));
        }
        return intArray;
    }

    public boolean containsKey(int i) {
        return this.mUserRestrictions.contains(i);
    }

    @VisibleForTesting
    public int size() {
        return this.mUserRestrictions.size();
    }

    @VisibleForTesting
    public int keyAt(int i) {
        return this.mUserRestrictions.keyAt(i);
    }

    @VisibleForTesting
    public Bundle valueAt(int i) {
        return this.mUserRestrictions.valueAt(i);
    }
}
