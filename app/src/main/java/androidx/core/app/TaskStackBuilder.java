package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: TaskStackBuilder.java */
/* renamed from: androidx.core.app.j, reason: use source file name */
/* loaded from: classes.dex */
public final class TaskStackBuilder implements Iterable<Intent> {

    /* renamed from: e, reason: collision with root package name */
    private final ArrayList<Intent> f2121e = new ArrayList<>();

    /* renamed from: f, reason: collision with root package name */
    private final Context f2122f;

    /* compiled from: TaskStackBuilder.java */
    /* renamed from: androidx.core.app.j$a */
    /* loaded from: classes.dex */
    public interface a {
        Intent getSupportParentActivityIntent();
    }

    private TaskStackBuilder(Context context) {
        this.f2122f = context;
    }

    public static TaskStackBuilder f(Context context) {
        return new TaskStackBuilder(context);
    }

    public TaskStackBuilder c(Intent intent) {
        this.f2121e.add(intent);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public TaskStackBuilder d(Activity activity) {
        Intent supportParentActivityIntent = activity instanceof a ? ((a) activity).getSupportParentActivityIntent() : null;
        if (supportParentActivityIntent == null) {
            supportParentActivityIntent = NavUtils.a(activity);
        }
        if (supportParentActivityIntent != null) {
            ComponentName component = supportParentActivityIntent.getComponent();
            if (component == null) {
                component = supportParentActivityIntent.resolveActivity(this.f2122f.getPackageManager());
            }
            e(component);
            c(supportParentActivityIntent);
        }
        return this;
    }

    public TaskStackBuilder e(ComponentName componentName) {
        int size = this.f2121e.size();
        try {
            Intent b10 = NavUtils.b(this.f2122f, componentName);
            while (b10 != null) {
                this.f2121e.add(size, b10);
                b10 = NavUtils.b(this.f2122f, b10.getComponent());
            }
            return this;
        } catch (PackageManager.NameNotFoundException e10) {
            Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
            throw new IllegalArgumentException(e10);
        }
    }

    public void g() {
        h(null);
    }

    public void h(Bundle bundle) {
        if (!this.f2121e.isEmpty()) {
            Intent[] intentArr = (Intent[]) this.f2121e.toArray(new Intent[0]);
            intentArr[0] = new Intent(intentArr[0]).addFlags(268484608);
            if (ContextCompat.h(this.f2122f, intentArr, bundle)) {
                return;
            }
            Intent intent = new Intent(intentArr[intentArr.length - 1]);
            intent.addFlags(268435456);
            this.f2122f.startActivity(intent);
            return;
        }
        throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
    }

    @Override // java.lang.Iterable
    @Deprecated
    public Iterator<Intent> iterator() {
        return this.f2121e.iterator();
    }
}
