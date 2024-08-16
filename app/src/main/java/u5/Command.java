package u5;

import android.os.Parcel;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: CommandClient.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0012\n\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0002\u000b\u0007B%\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\b\b\u0002\u0010\t\u001a\u00020\u0002\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\r¢\u0006\u0004\b\u0012\u0010\u0013J\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u0017\u0010\u0005\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\t\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0019\u0010\u000e\u001a\u0004\u0018\u00010\r8\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0014"}, d2 = {"Lu5/b;", "", "", "toString", "", "methodType", "I", "b", "()I", "callbackId", "Ljava/lang/String;", "a", "()Ljava/lang/String;", "", "params", "[B", "c", "()[B", "<init>", "(ILjava/lang/String;[B)V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: u5.b, reason: use source file name and from toString */
/* loaded from: classes.dex */
public final class Command {

    /* renamed from: d, reason: collision with root package name */
    public static final a f18867d = new a(null);

    /* renamed from: a, reason: collision with root package name and from toString */
    private final int methodType;

    /* renamed from: b, reason: collision with root package name and from toString */
    private final String callbackId;

    /* renamed from: c, reason: collision with root package name and from toString */
    private final byte[] params;

    /* compiled from: CommandClient.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\r\u0010\u000eJ\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002R\u0014\u0010\u0007\u001a\u00020\u00068\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\u00068\u0006X\u0086T¢\u0006\u0006\n\u0004\b\t\u0010\bR\u0014\u0010\n\u001a\u00020\u00068\u0006X\u0086T¢\u0006\u0006\n\u0004\b\n\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00068\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000b\u0010\bR\u0014\u0010\f\u001a\u00020\u00068\u0006X\u0086T¢\u0006\u0006\n\u0004\b\f\u0010\b¨\u0006\u000f"}, d2 = {"Lu5/b$a;", "", "Landroid/os/Parcel;", "parcel", "Lma/f0;", "a", "", "DATA_ITEM_END", "I", "DATA_TYPE_BYTE_ARRAY", "DATA_TYPE_INT", "DATA_TYPE_STRING", "DATA_VERSION_OF_TYPE", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: u5.b$a */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void a(Parcel parcel) {
            k.e(parcel, "parcel");
            int i10 = 0;
            while (i10 != 100) {
                i10 = parcel.readInt();
                if (i10 == 1) {
                    parcel.readInt();
                } else if (i10 == 2) {
                    parcel.readString();
                } else if (i10 == 3) {
                    parcel.readByteArray(new byte[parcel.readInt()]);
                }
            }
        }
    }

    /* compiled from: CommandClient.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u000b\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000b\u0010\fR\u001a\u0010\u0003\u001a\u00020\u00028\u0006X\u0086D¢\u0006\f\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\u0007\u001a\u00020\u00028\u0006X\u0086D¢\u0006\f\n\u0004\b\u0007\u0010\u0004\u001a\u0004\b\b\u0010\u0006R\u001a\u0010\t\u001a\u00020\u00028\u0006X\u0086D¢\u0006\f\n\u0004\b\t\u0010\u0004\u001a\u0004\b\n\u0010\u0006¨\u0006\r"}, d2 = {"Lu5/b$b;", "", "", "Request", "I", "b", "()I", "RequestOnce", "c", "Observe", "a", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: u5.b$b */
    /* loaded from: classes.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private static final int f18871a = 0;

        /* renamed from: d, reason: collision with root package name */
        public static final b f18874d = new b();

        /* renamed from: b, reason: collision with root package name */
        private static final int f18872b = 1;

        /* renamed from: c, reason: collision with root package name */
        private static final int f18873c = 2;

        private b() {
        }

        public final int a() {
            return f18873c;
        }

        public final int b() {
            return f18871a;
        }

        public final int c() {
            return f18872b;
        }
    }

    public Command(int i10, String str, byte[] bArr) {
        k.e(str, "callbackId");
        this.methodType = i10;
        this.callbackId = str;
        this.params = bArr;
    }

    /* renamed from: a, reason: from getter */
    public final String getCallbackId() {
        return this.callbackId;
    }

    /* renamed from: b, reason: from getter */
    public final int getMethodType() {
        return this.methodType;
    }

    /* renamed from: c, reason: from getter */
    public final byte[] getParams() {
        return this.params;
    }

    public String toString() {
        return "Command(methodType=" + this.methodType + ", callbackId=" + this.callbackId + ", params=" + this.params + ')';
    }
}
