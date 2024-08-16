package androidx.core.app;

import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;
import androidx.versionedparcelable.VersionedParcel;

/* loaded from: classes.dex */
public class RemoteActionCompatParcelizer {
    public static RemoteActionCompat read(VersionedParcel versionedParcel) {
        RemoteActionCompat remoteActionCompat = new RemoteActionCompat();
        remoteActionCompat.f2105a = (IconCompat) versionedParcel.v(remoteActionCompat.f2105a, 1);
        remoteActionCompat.f2106b = versionedParcel.l(remoteActionCompat.f2106b, 2);
        remoteActionCompat.f2107c = versionedParcel.l(remoteActionCompat.f2107c, 3);
        remoteActionCompat.f2108d = (PendingIntent) versionedParcel.r(remoteActionCompat.f2108d, 4);
        remoteActionCompat.f2109e = versionedParcel.h(remoteActionCompat.f2109e, 5);
        remoteActionCompat.f2110f = versionedParcel.h(remoteActionCompat.f2110f, 6);
        return remoteActionCompat;
    }

    public static void write(RemoteActionCompat remoteActionCompat, VersionedParcel versionedParcel) {
        versionedParcel.x(false, false);
        versionedParcel.M(remoteActionCompat.f2105a, 1);
        versionedParcel.D(remoteActionCompat.f2106b, 2);
        versionedParcel.D(remoteActionCompat.f2107c, 3);
        versionedParcel.H(remoteActionCompat.f2108d, 4);
        versionedParcel.z(remoteActionCompat.f2109e, 5);
        versionedParcel.z(remoteActionCompat.f2110f, 6);
    }
}
