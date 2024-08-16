package t5;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import com.oplus.cardwidget.proto.CardActionProto;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import kotlin.Metadata;
import kotlin.collections._Collections;
import kotlin.collections.r;
import ma.Unit;
import sd.StringsJVM;
import sd.v;
import u5.Command;
import u5.DataConverterUtil;
import ya.l;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.Reflection;
import za.k;

/* compiled from: ClientProxy.kt */
@Metadata(bv = {}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0003./0B\u001f\u0012\u0006\u0010$\u001a\u00020\u0002\u0012\u0006\u0010(\u001a\u00020\u0002\u0012\u0006\u0010+\u001a\u00020*¢\u0006\u0004\b,\u0010-J\u0010\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0002J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0007H\u0002J\u0010\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\u0002H\u0002J\u0010\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0007H\u0002J\u0010\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0007H\u0002J\u0010\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0002H\u0002J\u0006\u0010\u0013\u001a\u00020\u0005J\u0006\u0010\u0015\u001a\u00020\u0014J\u0014\u0010\u0018\u001a\u00020\u00052\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00070\u0016R\u001b\u0010\u001e\u001a\u00020\u00198BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u001a\u0010\u001b\u001a\u0004\b\u001c\u0010\u001dR\u001b\u0010#\u001a\u00020\u001f8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b \u0010\u001b\u001a\u0004\b!\u0010\"R\u0017\u0010$\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b$\u0010%\u001a\u0004\b&\u0010'R\u0017\u0010(\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b(\u0010%\u001a\u0004\b)\u0010'¨\u00061"}, d2 = {"Lt5/b;", "", "", "target", "i", "Lma/f0;", "s", "Lu5/b;", "command", "Lt5/b$a;", "f", "resUri", "", "m", "cmd", "o", "n", "observeRes", "p", "q", "Lt5/b$c;", "r", "", "commandClients", "l", "Landroid/content/Context;", "context$delegate", "Lma/h;", "h", "()Landroid/content/Context;", "context", "Lv5/f;", "workHandler$delegate", "k", "()Lv5/f;", "workHandler", "serverAuthority", "Ljava/lang/String;", "j", "()Ljava/lang/String;", "clientName", "g", "Lt5/c;", "iClient", "<init>", "(Ljava/lang/String;Ljava/lang/String;Lt5/c;)V", "a", "b", "c", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: t5.b, reason: use source file name */
/* loaded from: classes.dex */
public final class ClientProxy {

    /* renamed from: k, reason: collision with root package name */
    public static final b f18594k = new b(null);

    /* renamed from: a, reason: collision with root package name */
    private final ma.h f18595a;

    /* renamed from: b, reason: collision with root package name */
    private final Uri f18596b;

    /* renamed from: c, reason: collision with root package name */
    private final ma.h f18597c;

    /* renamed from: d, reason: collision with root package name */
    private final List<String> f18598d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f18599e;

    /* renamed from: f, reason: collision with root package name */
    private final String f18600f;

    /* renamed from: g, reason: collision with root package name */
    private final d f18601g;

    /* renamed from: h, reason: collision with root package name */
    private final String f18602h;

    /* renamed from: i, reason: collision with root package name */
    private final String f18603i;

    /* renamed from: j, reason: collision with root package name */
    private final t5.c f18604j;

    /* compiled from: ClientProxy.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\t\u001a\u00020\u0002\u0012\u0006\u0010\n\u001a\u00020\u0002\u0012\u0006\u0010\u000b\u001a\u00020\u0002\u0012\u0006\u0010\f\u001a\u00020\u0002¢\u0006\u0004\b\r\u0010\u000eJ\t\u0010\u0003\u001a\u00020\u0002HÖ\u0001J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\b\u001a\u00020\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001HÖ\u0003¨\u0006\u000f"}, d2 = {"Lt5/b$a;", "", "", "toString", "", "hashCode", "other", "", "equals", "type", "cardId", "hostId", "action", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: t5.b$a, reason: from toString */
    /* loaded from: classes.dex */
    public static final /* data */ class ActionIdentify {

        /* renamed from: a, reason: collision with root package name and from toString */
        private final String type;

        /* renamed from: b, reason: collision with root package name and from toString */
        private final String cardId;

        /* renamed from: c, reason: collision with root package name and from toString */
        private final String hostId;

        /* renamed from: d, reason: collision with root package name and from toString */
        private final String action;

        public ActionIdentify(String str, String str2, String str3, String str4) {
            k.e(str, "type");
            k.e(str2, "cardId");
            k.e(str3, "hostId");
            k.e(str4, "action");
            this.type = str;
            this.cardId = str2;
            this.hostId = str3;
            this.action = str4;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ActionIdentify)) {
                return false;
            }
            ActionIdentify actionIdentify = (ActionIdentify) other;
            return k.a(this.type, actionIdentify.type) && k.a(this.cardId, actionIdentify.cardId) && k.a(this.hostId, actionIdentify.hostId) && k.a(this.action, actionIdentify.action);
        }

        public int hashCode() {
            String str = this.type;
            int hashCode = (str != null ? str.hashCode() : 0) * 31;
            String str2 = this.cardId;
            int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
            String str3 = this.hostId;
            int hashCode3 = (hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31;
            String str4 = this.action;
            return hashCode3 + (str4 != null ? str4.hashCode() : 0);
        }

        public String toString() {
            return "ActionIdentify(type=" + this.type + ", cardId=" + this.cardId + ", hostId=" + this.hostId + ", action=" + this.action + ")";
        }
    }

    /* compiled from: ClientProxy.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0007\u0010\bR\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004R\u0014\u0010\u0006\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0004¨\u0006\t"}, d2 = {"Lt5/b$b;", "", "", "CLIENT_NAME_ASSISTANT", "Ljava/lang/String;", "CLIENT_NAME_LAUNCHER", "TAG", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: t5.b$b */
    /* loaded from: classes.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: ClientProxy.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001d\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0002\u0012\u0006\u0010\t\u001a\u00020\b¢\u0006\u0004\b\r\u0010\u000eR\u001d\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u00028\u0006¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\t\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\f¨\u0006\u000f"}, d2 = {"Lt5/b$c;", "", "", "Lu5/b;", "commandClients", "Ljava/util/List;", "a", "()Ljava/util/List;", "", "idleState", "Z", "b", "()Z", "<init>", "(Ljava/util/List;Z)V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: t5.b$c */
    /* loaded from: classes.dex */
    public static final class c {

        /* renamed from: a, reason: collision with root package name */
        private final List<Command> f18609a;

        /* renamed from: b, reason: collision with root package name */
        private final boolean f18610b;

        public c(List<Command> list, boolean z10) {
            k.e(list, "commandClients");
            this.f18609a = list;
            this.f18610b = z10;
        }

        public final List<Command> a() {
            return this.f18609a;
        }

        /* renamed from: b, reason: from getter */
        public final boolean getF18610b() {
            return this.f18610b;
        }
    }

    /* compiled from: ClientProxy.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"t5/b$d", "Landroid/database/ContentObserver;", "", "selfChange", "Lma/f0;", "onChange", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: t5.b$d */
    /* loaded from: classes.dex */
    public static final class d extends ContentObserver {
        d(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            v5.e eVar = v5.e.f19125b;
            if (eVar.c()) {
                eVar.a(ClientProxy.this.f18600f, "onChange selfChange = [" + z10 + ']');
            }
            ClientProxy.this.q();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClientProxy.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0003\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "run", "()V", "<anonymous>"}, k = 3, mv = {1, 4, 2})
    /* renamed from: t5.b$e */
    /* loaded from: classes.dex */
    public static final class e implements Runnable {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ String f18613f;

        /* compiled from: ClientProxy.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"", "result", "Lma/f0;", "a", "([B)V"}, k = 3, mv = {1, 4, 2})
        /* renamed from: t5.b$e$a */
        /* loaded from: classes.dex */
        static final class a extends Lambda implements l<byte[], Unit> {
            a() {
                super(1);
            }

            public final void a(byte[] bArr) {
                k.e(bArr, "result");
                ContentProviderClient acquireUnstableContentProviderClient = ClientProxy.this.h().getContentResolver().acquireUnstableContentProviderClient(ClientProxy.this.getF18602h());
                if (acquireUnstableContentProviderClient != null) {
                    k.d(acquireUnstableContentProviderClient, "context.contentResolver.…        ?: return@observe");
                    String f18603i = ClientProxy.this.getF18603i();
                    Bundle bundle = new Bundle();
                    v5.e eVar = v5.e.f19125b;
                    if (eVar.c()) {
                        eVar.a("DataChannel.ClientProxy.", "processObserve size is: " + bArr.length);
                    }
                    bundle.putString("RESULT_CALLBACK_ID", e.this.f18613f);
                    bundle.putByteArray("RESULT_CALLBACK_DATA", bArr);
                    Unit unit = Unit.f15173a;
                    acquireUnstableContentProviderClient.call("callback", f18603i, bundle);
                    acquireUnstableContentProviderClient.close();
                }
            }

            @Override // ya.l
            public /* bridge */ /* synthetic */ Unit invoke(byte[] bArr) {
                a(bArr);
                return Unit.f15173a;
            }
        }

        e(String str) {
            this.f18613f = str;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ClientProxy.this.f18604j.b(this.f18613f, new a());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClientProxy.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0003\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "run", "()V", "<anonymous>"}, k = 3, mv = {1, 4, 2})
    /* renamed from: t5.b$f */
    /* loaded from: classes.dex */
    public static final class f implements Runnable {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ byte[] f18616f;

        f(byte[] bArr) {
            this.f18616f = bArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ClientProxy.this.f18604j.g(this.f18616f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClientProxy.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0003\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "run", "()V", "<anonymous>"}, k = 3, mv = {1, 4, 2})
    /* renamed from: t5.b$g */
    /* loaded from: classes.dex */
    public static final class g implements Runnable {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ byte[] f18618f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ Command f18619g;

        /* compiled from: ClientProxy.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"", "result", "Lma/f0;", "a", "([B)V"}, k = 3, mv = {1, 4, 2})
        /* renamed from: t5.b$g$a */
        /* loaded from: classes.dex */
        static final class a extends Lambda implements l<byte[], Unit> {
            a() {
                super(1);
            }

            public final void a(byte[] bArr) {
                k.e(bArr, "result");
                ContentProviderClient acquireUnstableContentProviderClient = ClientProxy.this.h().getContentResolver().acquireUnstableContentProviderClient(ClientProxy.this.getF18602h());
                if (acquireUnstableContentProviderClient != null) {
                    k.d(acquireUnstableContentProviderClient, "context.contentResolver.…    ?: return@requestOnce");
                    String f18603i = ClientProxy.this.getF18603i();
                    Bundle bundle = new Bundle();
                    bundle.putString("RESULT_CALLBACK_ID", g.this.f18619g.getCallbackId());
                    bundle.putByteArray("RESULT_CALLBACK_DATA", bArr);
                    Unit unit = Unit.f15173a;
                    acquireUnstableContentProviderClient.call("callback", f18603i, bundle);
                    acquireUnstableContentProviderClient.close();
                }
            }

            @Override // ya.l
            public /* bridge */ /* synthetic */ Unit invoke(byte[] bArr) {
                a(bArr);
                return Unit.f15173a;
            }
        }

        g(byte[] bArr, Command command) {
            this.f18618f = bArr;
            this.f18619g = command;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ClientProxy.this.f18604j.l(this.f18618f, new a());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClientProxy.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0003\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "run", "()V", "<anonymous>"}, k = 3, mv = {1, 4, 2})
    /* renamed from: t5.b$h */
    /* loaded from: classes.dex */
    public static final class h implements Runnable {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ String f18622f;

        h(String str) {
            this.f18622f = str;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ClientProxy.this.f18604j.a(this.f18622f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClientProxy.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0003\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "run", "()V", "<anonymous>"}, k = 3, mv = {1, 4, 2})
    /* renamed from: t5.b$i */
    /* loaded from: classes.dex */
    public static final class i implements Runnable {
        i() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            c cVar;
            List j10;
            if (ClientProxy.this.f18599e) {
                ClientProxy.this.s();
            }
            try {
                cVar = ClientProxy.this.r();
            } catch (Exception e10) {
                v5.e eVar = v5.e.f19125b;
                if (eVar.c()) {
                    eVar.b(ClientProxy.this.f18600f, "pullAndRunCommand exception = " + e10 + ' ');
                }
                j10 = r.j();
                cVar = new c(j10, true);
            }
            if (cVar.getF18610b()) {
                v5.e eVar2 = v5.e.f19125b;
                if (eVar2.c()) {
                    eVar2.a(ClientProxy.this.f18600f, "pullAndRunCommand pullResult.idleState = true ");
                    return;
                }
                return;
            }
            List<Command> a10 = cVar.a();
            v5.e eVar3 = v5.e.f19125b;
            if (eVar3.c()) {
                eVar3.a(ClientProxy.this.f18600f, "pullAndRunCommand commandList = " + a10 + ' ');
            }
            ClientProxy.this.l(a10);
        }
    }

    public ClientProxy(String str, String str2, t5.c cVar) {
        k.e(str, "serverAuthority");
        k.e(str2, "clientName");
        k.e(cVar, "iClient");
        this.f18602h = str;
        this.f18603i = str2;
        this.f18604j = cVar;
        v5.b bVar = v5.b.f19122c;
        if (bVar.b().get(Reflection.b(Context.class)) != null) {
            ma.h<?> hVar = bVar.b().get(Reflection.b(Context.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            this.f18595a = hVar;
            this.f18596b = Uri.parse("content://" + str + "/pull/" + str2);
            if (bVar.b().get(Reflection.b(v5.f.class)) != null) {
                ma.h<?> hVar2 = bVar.b().get(Reflection.b(v5.f.class));
                Objects.requireNonNull(hVar2, "null cannot be cast to non-null type kotlin.Lazy<T>");
                this.f18597c = hVar2;
                this.f18598d = new ArrayList();
                this.f18599e = true;
                this.f18600f = "DataChannel.ClientProxy." + i(str2);
                this.f18601g = new d(k());
                q();
                return;
            }
            throw new IllegalStateException("the class are not injected");
        }
        throw new IllegalStateException("the class are not injected");
    }

    private final ActionIdentify f(Command command) {
        String callbackId;
        String str;
        String str2;
        int methodType = command.getMethodType();
        Command.b bVar = Command.b.f18874d;
        String str3 = "";
        if (methodType == bVar.b()) {
            if (command.getParams() != null) {
                CardActionProto parseFrom = CardActionProto.parseFrom(command.getParams());
                k.d(parseFrom, "cardActionProto");
                str3 = String.valueOf(parseFrom.getCardType());
                callbackId = String.valueOf(parseFrom.getCardId());
                str = String.valueOf(parseFrom.getHostId());
                str2 = DataConverterUtil.a(parseFrom);
            }
            str2 = "";
            callbackId = str2;
            str = callbackId;
        } else {
            if (methodType == bVar.a()) {
                String valueOf = String.valueOf(command.getMethodType());
                callbackId = command.getCallbackId();
                str = "";
                str3 = valueOf;
                str2 = str;
            }
            str2 = "";
            callbackId = str2;
            str = callbackId;
        }
        return new ActionIdentify(str3, callbackId, str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Context h() {
        return (Context) this.f18595a.getValue();
    }

    private final String i(String target) {
        List q02;
        try {
            q02 = v.q0(target, new String[]{"."}, false, 0, 6, null);
            return (String) q02.get(q02.size() - 1);
        } catch (Exception unused) {
            v5.e.f19125b.a("DataChannel.ClientProxy.", "client name is " + target);
            return target;
        }
    }

    private final v5.f k() {
        return (v5.f) this.f18597c.getValue();
    }

    private final boolean m(String resUri) {
        if (this.f18598d.contains(resUri)) {
            return false;
        }
        v5.b bVar = v5.b.f19122c;
        if (bVar.b().get(Reflection.b(ExecutorService.class)) != null) {
            ma.h<?> hVar = bVar.b().get(Reflection.b(ExecutorService.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            ((ExecutorService) hVar.getValue()).submit(new e(resUri));
            return true;
        }
        throw new IllegalStateException("the class are not injected");
    }

    private final void n(Command command) {
        byte[] params = command.getParams();
        if (params == null) {
            v5.e eVar = v5.e.f19125b;
            if (eVar.c()) {
                eVar.d(this.f18600f, "processCommandList error " + command + ' ');
                return;
            }
            return;
        }
        v5.b bVar = v5.b.f19122c;
        if (bVar.b().get(Reflection.b(ExecutorService.class)) != null) {
            ma.h<?> hVar = bVar.b().get(Reflection.b(ExecutorService.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            ((ExecutorService) hVar.getValue()).submit(new f(params));
            return;
        }
        throw new IllegalStateException("the class are not injected");
    }

    private final void o(Command command) {
        boolean s7;
        byte[] params = command.getParams();
        if (params != null) {
            s7 = StringsJVM.s(command.getCallbackId());
            if (!s7) {
                v5.b bVar = v5.b.f19122c;
                if (bVar.b().get(Reflection.b(ExecutorService.class)) != null) {
                    ma.h<?> hVar = bVar.b().get(Reflection.b(ExecutorService.class));
                    Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
                    ((ExecutorService) hVar.getValue()).submit(new g(params, command));
                    return;
                }
                throw new IllegalStateException("the class are not injected");
            }
        }
        v5.e eVar = v5.e.f19125b;
        if (eVar.c()) {
            eVar.d(this.f18600f, "processCommandList error " + command + ' ');
        }
    }

    private final void p(String str) {
        v5.b bVar = v5.b.f19122c;
        if (bVar.b().get(Reflection.b(ExecutorService.class)) != null) {
            ma.h<?> hVar = bVar.b().get(Reflection.b(ExecutorService.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            ((ExecutorService) hVar.getValue()).submit(new h(str));
            return;
        }
        throw new IllegalStateException("the class are not injected");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void s() {
        v5.e eVar = v5.e.f19125b;
        if (eVar.c()) {
            eVar.a(this.f18600f, "tryRegisterContentObserver");
        }
        try {
            h().getContentResolver().registerContentObserver(this.f18596b, false, this.f18601g);
            this.f18599e = false;
        } catch (Exception e10) {
            v5.e eVar2 = v5.e.f19125b;
            if (eVar2.c()) {
                eVar2.a(this.f18600f, "try registerContentObserver error " + e10);
            }
            this.f18599e = true;
        }
    }

    /* renamed from: g, reason: from getter */
    public final String getF18603i() {
        return this.f18603i;
    }

    /* renamed from: j, reason: from getter */
    public final String getF18602h() {
        return this.f18602h;
    }

    public final void l(List<Command> list) {
        List o02;
        List M;
        List o03;
        k.e(list, "commandClients");
        String str = this.f18603i;
        int hashCode = str.hashCode();
        if (hashCode == 225091385 ? str.equals("card_service_launcher") : !(hashCode != 446552198 || !str.equals("card_service"))) {
            v5.e.f19125b.a(this.f18600f, "processCommandList: clientName = " + this.f18603i);
        } else {
            ArrayList arrayList = new ArrayList();
            o02 = _Collections.o0(list);
            HashSet hashSet = new HashSet();
            ArrayList arrayList2 = new ArrayList();
            for (Object obj : o02) {
                ActionIdentify f10 = f((Command) obj);
                arrayList.add(f10);
                if (hashSet.add(f10)) {
                    arrayList2.add(obj);
                }
            }
            list = _Collections.o0(arrayList2);
            v5.e eVar = v5.e.f19125b;
            if (eVar.c()) {
                eVar.a(this.f18600f, "processCommandList: distinct processCommands = " + list);
                String str2 = this.f18600f;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("processCommandList: detail processCommands = ");
                M = _Collections.M(arrayList);
                o03 = _Collections.o0(M);
                sb2.append(o03);
                eVar.a(str2, sb2.toString());
            }
        }
        boolean z10 = false;
        ArrayList arrayList3 = new ArrayList();
        for (Command command : list) {
            int methodType = command.getMethodType();
            Command.b bVar = Command.b.f18874d;
            if (methodType == bVar.b()) {
                n(command);
            } else if (methodType == bVar.c()) {
                o(command);
            } else if (methodType == bVar.a()) {
                String callbackId = command.getCallbackId();
                arrayList3.add(callbackId);
                if (m(callbackId)) {
                    z10 = true;
                }
            }
        }
        for (String str3 : this.f18598d) {
            if (!arrayList3.contains(str3)) {
                p(str3);
                z10 = true;
            }
        }
        if (z10) {
            this.f18604j.c(arrayList3);
        }
        this.f18598d.clear();
        this.f18598d.addAll(arrayList3);
    }

    public final void q() {
        k().post(new i());
    }

    public final c r() {
        List j10;
        List j11;
        ContentProviderClient acquireUnstableContentProviderClient = h().getContentResolver().acquireUnstableContentProviderClient(this.f18602h);
        if (acquireUnstableContentProviderClient != null) {
            k.d(acquireUnstableContentProviderClient, "context.contentResolver.… false)\n                }");
            Bundle call = acquireUnstableContentProviderClient.call("pullCommand", this.f18603i, null);
            acquireUnstableContentProviderClient.close();
            byte[] byteArray = call != null ? call.getByteArray("RESULT_COMMAND_LIST") : null;
            boolean z10 = call != null ? call.getBoolean("RESULT_IDLE_STATE", false) : false;
            if (byteArray == null) {
                j11 = r.j();
                return new c(j11, z10);
            }
            Parcel obtain = Parcel.obtain();
            k.d(obtain, "Parcel.obtain()");
            ArrayList arrayList = new ArrayList();
            try {
                obtain.unmarshall(byteArray, 0, byteArray.length);
                obtain.setDataPosition(0);
                if (obtain.readInt() == 1) {
                    int readInt = obtain.readInt();
                    for (int i10 = 0; i10 < readInt; i10++) {
                        obtain.readInt();
                        int readInt2 = obtain.readInt();
                        obtain.readInt();
                        String readString = obtain.readString();
                        if (readString == null) {
                            readString = "";
                        }
                        k.d(readString, "parcel.readString() ?: \"\"");
                        obtain.readInt();
                        byte[] bArr = new byte[obtain.readInt()];
                        obtain.readByteArray(bArr);
                        arrayList.add(new Command(readInt2, readString, bArr));
                        Command.f18867d.a(obtain);
                    }
                }
                obtain.recycle();
                return new c(arrayList, z10);
            } catch (Throwable th) {
                obtain.recycle();
                throw th;
            }
        }
        v5.e eVar = v5.e.f19125b;
        if (eVar.c()) {
            eVar.a(this.f18600f, "pullCommand with null client ");
        }
        j10 = r.j();
        return new c(j10, false);
    }
}
