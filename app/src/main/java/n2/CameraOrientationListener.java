package n2;

import android.content.Context;
import android.view.OrientationEventListener;
import fb._Ranges;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: CameraOrientationListener.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b&\u0018\u00002\u00020\u0001:\u0001\bB\u000f\u0012\u0006\u0010\n\u001a\u00020\t¢\u0006\u0004\b\u000b\u0010\fJ\u0018\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0002J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\b\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0002H&¨\u0006\r"}, d2 = {"Ln2/a;", "Landroid/view/OrientationEventListener;", "", "orientation", "lastOrientation", "b", "Lma/f0;", "onOrientationChanged", "a", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: n2.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class CameraOrientationListener extends OrientationEventListener {

    /* renamed from: b, reason: collision with root package name */
    public static final a f15634b = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private int f15635a;

    /* compiled from: CameraOrientationListener.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\r\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\r\u0010\u000eR\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004R\u0014\u0010\u0006\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0004R\u0014\u0010\u0007\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0007\u0010\u0004R\u0014\u0010\b\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\b\u0010\u0004R\u0014\u0010\t\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\t\u0010\u0004R\u0014\u0010\n\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\n\u0010\u0004R\u0014\u0010\u000b\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000b\u0010\u0004R\u0014\u0010\f\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\f\u0010\u0004¨\u0006\u000f"}, d2 = {"Ln2/a$a;", "", "", "ANGLE_0", "I", "ANGLE_180", "ANGLE_270", "ANGLE_30", "ANGLE_360", "ANGLE_45", "ANGLE_60", "ANGLE_90", "ANGLE_OFFSET", "<init>", "()V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: n2.a$a */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CameraOrientationListener(Context context) {
        super(context);
        k.e(context, "context");
        this.f15635a = -1;
    }

    private final int b(int orientation, int lastOrientation) {
        int f10;
        boolean z10 = true;
        if (lastOrientation != -1) {
            int abs = Math.abs(orientation - lastOrientation);
            f10 = _Ranges.f(abs, 360 - abs);
            if (f10 < 65) {
                z10 = false;
            }
        }
        return z10 ? (((orientation + 30) / 90) * 90) % 360 : lastOrientation;
    }

    public abstract void a(int i10);

    @Override // android.view.OrientationEventListener
    public void onOrientationChanged(int i10) {
        int b10;
        if (i10 == -1 || this.f15635a == (b10 = b(i10, this.f15635a))) {
            return;
        }
        this.f15635a = b10;
        a(b10);
    }
}
