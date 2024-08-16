package b;

import android.content.Context;
import android.content.Intent;
import android.view.result.ActivityResult;
import com.oplus.backup.sdk.common.utils.Constants;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ActivityResultContracts.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0001\u000eB\u0007¢\u0006\u0004\b\f\u0010\rJ\u0018\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0002H\u0016J\u001a\u0010\u000b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\b2\b\u0010\n\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\u000f"}, d2 = {"Lb/c;", "Lb/a;", "Landroid/content/Intent;", "Landroidx/activity/result/ActivityResult;", "Landroid/content/Context;", "context", "input", "d", "", "resultCode", Constants.MessagerConstants.INTENT_KEY, "e", "<init>", "()V", "a", "activity_release"}, k = 1, mv = {1, 7, 1})
/* loaded from: classes.dex */
public final class c extends ActivityResultContract<Intent, ActivityResult> {

    /* renamed from: a, reason: collision with root package name */
    public static final a f4517a = new a(null);

    /* compiled from: ActivityResultContracts.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lb/c$a;", "", "", "EXTRA_ACTIVITY_OPTIONS_BUNDLE", "Ljava/lang/String;", "<init>", "()V", "activity_release"}, k = 1, mv = {1, 7, 1})
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Override // b.ActivityResultContract
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public Intent a(Context context, Intent input) {
        k.e(context, "context");
        k.e(input, "input");
        return input;
    }

    @Override // b.ActivityResultContract
    /* renamed from: e, reason: merged with bridge method [inline-methods] */
    public ActivityResult c(int resultCode, Intent intent) {
        return new ActivityResult(resultCode, intent);
    }
}
