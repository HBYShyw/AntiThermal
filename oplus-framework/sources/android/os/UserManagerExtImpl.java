package android.os;

/* loaded from: classes.dex */
public class UserManagerExtImpl implements IUserManagerExt {
    private UserManager mBase;

    public UserManagerExtImpl(Object base) {
        this.mBase = (UserManager) base;
    }

    public boolean isMultiAppUserId(int userId) {
        return 999 == userId;
    }
}
