package com.oplus.cardwidget.serviceLayer;

import android.content.Context;
import b5.CardAction;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import e5.IActionInvoker;
import f5.CardWidgetAction;
import gb.l;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import kotlin.collections.CollectionsJVM;
import ma.Unit;
import ma.h;
import p5.ICardState;
import r5.IDataHandle;
import s5.CardDataTranslater;
import s5.b;
import t5.ClientChannel;
import v5.c;
import za.PropertyReference0Impl;
import za.Reflection;
import za.k;

/* compiled from: BaseAppCardWidgetProvider.kt */
@Metadata(bv = {}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000 \"2\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0001\u0011B\u0007¢\u0006\u0004\b \u0010!J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0007H\u0016J$\u0010\r\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u00072\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\t0\u000bH\u0016J$\u0010\u0010\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u000e2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\t0\u000bH\u0016J\u0010\u0010\u0011\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u000eH\u0016J\u0018\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u000eH\u0016J\u0018\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u000eH\u0016J\u0018\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u000eH\u0017RH\u0010\u001c\u001a6\u0012\u0004\u0012\u00020\u000e\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\t0\u000b0\u0018j\u001a\u0012\u0004\u0012\u00020\u000e\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\t0\u000b`\u00198\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001a\u0010\u001bR\u0014\u0010\u001f\u001a\u00020\u000e8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001d\u0010\u001e¨\u0006%²\u0006\f\u0010$\u001a\u00020#8\nX\u008a\u0084\u0002"}, d2 = {"Lcom/oplus/cardwidget/serviceLayer/BaseAppCardWidgetProvider;", "Lcom/oplus/cardwidget/serviceLayer/BaseInterfaceLayerProvider;", "Lt5/c;", "Lp5/a;", "", "", "onCreate", "", "requestData", "Lma/f0;", "g", "Lkotlin/Function1;", "callback", "l", "", "observeResStr", "b", "a", "Landroid/content/Context;", "context", "widgetCode", "e", "j", "k", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "p", "Ljava/util/HashMap;", "channelMap", "q", "Ljava/lang/String;", "TAG", "<init>", "()V", "t", "Le5/a;", ThermalBaseConfig.Item.ATTR_VALUE, "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* loaded from: classes.dex */
public abstract class BaseAppCardWidgetProvider extends BaseInterfaceLayerProvider implements ICardState {

    /* renamed from: s, reason: collision with root package name */
    static final /* synthetic */ l[] f9816s = {Reflection.f(new PropertyReference0Impl(BaseAppCardWidgetProvider.class, ThermalBaseConfig.Item.ATTR_VALUE, "<v#0>", 0))};

    /* renamed from: p, reason: collision with root package name and from kotlin metadata */
    private final HashMap<String, ya.l<byte[], Unit>> channelMap = new HashMap<>();

    /* renamed from: q, reason: collision with root package name and from kotlin metadata */
    private final String TAG;

    /* renamed from: r, reason: collision with root package name */
    private IActionInvoker f9820r;

    public BaseAppCardWidgetProvider() {
        String simpleName = getClass().getSimpleName();
        k.d(simpleName, "this@BaseAppCardWidgetPr…ider.javaClass.simpleName");
        this.TAG = simpleName;
    }

    @Override // com.oplus.cardwidget.serviceLayer.BaseInterfaceLayerProvider, t5.c
    public void a(String str) {
        k.e(str, "observeResStr");
        String d10 = CardDataTranslater.d(str);
        b.f18066c.c(this.TAG, "--unObserve : " + str + " widgetCode : " + d10);
        if (d10 != null) {
            this.channelMap.remove(d10);
        }
    }

    @Override // com.oplus.cardwidget.serviceLayer.BaseInterfaceLayerProvider, t5.c
    public void b(String str, ya.l<? super byte[], Unit> lVar) {
        k.e(str, "observeResStr");
        k.e(lVar, "callback");
        String d10 = CardDataTranslater.d(str);
        if (d10 != null) {
            this.channelMap.put(d10, lVar);
        }
        b.f18066c.c(this.TAG, "--observe : " + str + " widgetCode : " + d10);
    }

    @Override // p5.ICardState
    public void e(Context context, String str) {
        k.e(context, "context");
        k.e(str, "widgetCode");
        b.f18066c.c(this.TAG, "onCreate widgetCode is " + str);
        CardWidgetAction.f11356a.b(str, i(str));
    }

    @Override // com.oplus.cardwidget.serviceLayer.BaseInterfaceLayerProvider, t5.c
    public void g(byte[] bArr) {
        k.e(bArr, "requestData");
        v5.b bVar = v5.b.f19122c;
        if (bVar.b().get(Reflection.b(IDataHandle.class)) != null) {
            h<?> hVar = bVar.b().get(Reflection.b(IDataHandle.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            CardAction a10 = ((IDataHandle) hVar.getValue()).a(bArr);
            b.f18066c.c(this.TAG, "request widgetCode: " + a10.getWidgetCode() + ", action = " + a10);
            IActionInvoker iActionInvoker = this.f9820r;
            if (iActionInvoker != null) {
                iActionInvoker.a(a10);
                return;
            }
            return;
        }
        throw new IllegalStateException("the class are not injected");
    }

    @Override // p5.ICardState
    public void j(Context context, String str) {
        k.e(context, "context");
        k.e(str, "widgetCode");
        b.f18066c.c(this.TAG, "onPause");
    }

    @Override // p5.ICardState
    public void k(Context context, String str) {
        k.e(context, "context");
        k.e(str, "widgetCode");
        b.f18066c.c(this.TAG, "onDestroy");
    }

    @Override // com.oplus.cardwidget.serviceLayer.BaseInterfaceLayerProvider, t5.c
    public void l(byte[] bArr, ya.l<? super byte[], Unit> lVar) {
        k.e(bArr, "requestData");
        k.e(lVar, "callback");
        b.f18066c.c(this.TAG, "requestOnce");
    }

    @Override // com.oplus.cardwidget.serviceLayer.BaseCardStrategyProvider, com.oplus.channel.client.provider.ChannelClientProvider, android.content.ContentProvider
    public boolean onCreate() {
        List<? extends Object> e10;
        String canonicalName = getClass().getCanonicalName();
        Context context = getContext();
        if (context != null) {
            ClientChannel clientChannel = ClientChannel.f18590d;
            k.d(context, "it");
            Context applicationContext = context.getApplicationContext();
            k.d(applicationContext, "it.applicationContext");
            ClientChannel.d(clientChannel, applicationContext, null, 2, null);
            k.d(canonicalName, "clientName");
            clientChannel.e("com.oplus.cardservice.repository.provider.CardServiceServerProvider", canonicalName, this);
            b.f18066c.c(this.TAG, "provider create and initial ClientChannel");
            v5.b bVar = v5.b.f19122c;
            Object[] objArr = new Object[0];
            if (bVar.a().get(Reflection.b(IActionInvoker.class)) != null) {
                ya.l<List<? extends Object>, ?> lVar = bVar.a().get(Reflection.b(IActionInvoker.class));
                if (lVar != null) {
                    k.d(lVar, "factoryInstanceMap[T::cl…actory are not injected\")");
                    e10 = CollectionsJVM.e(objArr);
                    this.f9820r = (IActionInvoker) new c(lVar.invoke(e10)).a(null, f9816s[0]);
                    return true;
                }
                throw new IllegalStateException("the factory are not injected");
            }
            throw new IllegalStateException("the class are not injected");
        }
        b.f18066c.e(this.TAG, "context is not allow not!");
        return false;
    }
}
