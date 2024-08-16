package r5;

import android.os.Bundle;
import b5.CardAction;
import kotlin.Metadata;

/* compiled from: IDataHandle.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&J\u0010\u0010\b\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0006H&¨\u0006\t"}, d2 = {"Lr5/f;", "", "", "data", "Lb5/a;", "a", "Landroid/os/Bundle;", "bundle", "b", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: r5.f, reason: use source file name */
/* loaded from: classes.dex */
public interface IDataHandle {
    CardAction a(byte[] data);

    byte[] b(Bundle bundle);
}
