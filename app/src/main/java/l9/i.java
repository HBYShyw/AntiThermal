package l9;

import android.net.Uri;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ScenesProviderUtils.kt */
@Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0002¨\u0006\u0003"}, d2 = {"Ll9/i;", "", "a", "scenesdk_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes2.dex */
public final class i {

    /* renamed from: a, reason: collision with root package name */
    public static final a f14655a = new a(null);

    /* compiled from: ScenesProviderUtils.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0007R\u0014\u0010\b\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\n\u0010\tR\u0014\u0010\u000b\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000b\u0010\tR\u0014\u0010\f\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\f\u0010\tR\u0014\u0010\r\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\r\u0010\tR\u0014\u0010\u000e\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000e\u0010\t¨\u0006\u0011"}, d2 = {"Ll9/i$a;", "", "", "tableName", "", "notify", "Landroid/net/Uri;", "a", "AUTHORITY", "Ljava/lang/String;", "NOTIFY_SUFFIX", "NO_NOTIFY_SUFFIX", "PARAMETER_NOTIFY", "TAG", "URI_STRING", "<init>", "()V", "scenesdk_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Uri a(String tableName, boolean notify) {
            Uri parse;
            String str;
            k.e(tableName, "tableName");
            if (notify) {
                parse = Uri.parse(k.l("content://com.oplus.sceneservice.scenesprovider/", tableName));
                str = "parse(\"$URI_STRING/$tableName\")";
            } else {
                parse = Uri.parse("content://com.oplus.sceneservice.scenesprovider/" + tableName + "?notify=false");
                str = "parse(\"$URI_STRING/$tableName$NO_NOTIFY_SUFFIX\")";
            }
            k.d(parse, str);
            return parse;
        }
    }

    public static final Uri a(String str, boolean z10) {
        return f14655a.a(str, z10);
    }
}
