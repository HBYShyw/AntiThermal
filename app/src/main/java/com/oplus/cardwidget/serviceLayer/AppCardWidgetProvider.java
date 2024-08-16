package com.oplus.cardwidget.serviceLayer;

import android.content.Context;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import d5.GlobalDIConfig;
import f5.CardWidgetAction;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import l5.CardStateProcessor;
import ma.Unit;
import p5.ICardState;
import s5.b;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: AppCardWidgetProvider.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010!\n\u0002\b\u0012\b&\u0018\u00002\u00020\u00012\u00020\u0002B\u0007¢\u0006\u0004\b#\u0010$J\b\u0010\u0004\u001a\u00020\u0003H\u0016J\b\u0010\u0006\u001a\u00020\u0005H\u0017J\u0018\u0010\u000b\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0017J\u001e\u0010\u000e\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\fH\u0017J\u0018\u0010\u000f\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0017J\u0018\u0010\u0010\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0017J\u0018\u0010\u0011\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0016J\u0018\u0010\u0012\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0016J\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\t0\u0013R\u0014\u0010\u0017\u001a\u00020\t8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0015\u0010\u0016R\u001a\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\t0\u00138\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0018\u0010\u0019R*\u0010\"\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u00038F@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!¨\u0006%"}, d2 = {"Lcom/oplus/cardwidget/serviceLayer/AppCardWidgetProvider;", "Lcom/oplus/cardwidget/serviceLayer/BaseInterfaceLayerProvider;", "Lp5/a;", "", "onCreate", "Lma/f0;", "A", "Landroid/content/Context;", "context", "", "widgetCode", "e", "", "widgetCodes", "f", "j", "k", "d", "h", "", "z", "p", "Ljava/lang/String;", "TAG", "q", "Ljava/util/List;", "cardShowedList", ThermalBaseConfig.Item.ATTR_VALUE, "r", "Z", "y", "()Z", "setLazyInitial", "(Z)V", "lazyInitial", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* loaded from: classes.dex */
public abstract class AppCardWidgetProvider extends BaseInterfaceLayerProvider implements ICardState {

    /* renamed from: p, reason: collision with root package name and from kotlin metadata */
    private final String TAG;

    /* renamed from: q, reason: collision with root package name and from kotlin metadata */
    private final List<String> cardShowedList;

    /* renamed from: r, reason: collision with root package name and from kotlin metadata */
    private boolean lazyInitial;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppCardWidgetProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Lcom/oplus/cardwidget/serviceLayer/AppCardWidgetProvider;", "Lma/f0;", "a", "(Lcom/oplus/cardwidget/serviceLayer/AppCardWidgetProvider;)V"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    public static final class a extends Lambda implements l<AppCardWidgetProvider, Unit> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f9815e = new a();

        a() {
            super(1);
        }

        public final void a(AppCardWidgetProvider appCardWidgetProvider) {
            k.e(appCardWidgetProvider, "$receiver");
            CardStateProcessor.f14631b.a(appCardWidgetProvider);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(AppCardWidgetProvider appCardWidgetProvider) {
            a(appCardWidgetProvider);
            return Unit.f15173a;
        }
    }

    public AppCardWidgetProvider() {
        String simpleName = getClass().getSimpleName();
        k.d(simpleName, "this@AppCardWidgetProvider.javaClass.simpleName");
        this.TAG = simpleName;
        this.cardShowedList = new ArrayList();
    }

    public void A() {
        b.f18066c.c(this.TAG, "onCardWidgetInitial...");
        Context context = getContext();
        if (context != null) {
            GlobalDIConfig globalDIConfig = GlobalDIConfig.f10714b;
            k.d(context, "it");
            globalDIConfig.a(context);
        }
        x(this, a.f9815e);
        w();
    }

    @Override // p5.ICardState
    public void d(Context context, String str) {
        k.e(context, "context");
        k.e(str, "widgetCode");
        b.f18066c.c(this.TAG, "subscribed widgetCode:" + str);
    }

    @Override // p5.ICardState
    public void e(Context context, String str) {
        k.e(context, "context");
        k.e(str, "widgetCode");
        b.f18066c.c(this.TAG, "onCardCreate widgetCode is " + str);
        CardWidgetAction.f11356a.b(str, i(str));
    }

    @Override // p5.ICardState
    public void f(Context context, List<String> list) {
        k.e(context, "context");
        k.e(list, "widgetCodes");
        b.f18066c.c(this.TAG, "onCardObserve widgetCode list size is " + list.size() + ')');
        synchronized (this.cardShowedList) {
            this.cardShowedList.clear();
            this.cardShowedList.addAll(list);
        }
    }

    @Override // p5.ICardState
    public void h(Context context, String str) {
        k.e(context, "context");
        k.e(str, "widgetCode");
        b.f18066c.c(this.TAG, "unSubscribed widgetCode:" + str);
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

    @Override // com.oplus.cardwidget.serviceLayer.BaseCardStrategyProvider, com.oplus.channel.client.provider.ChannelClientProvider, android.content.ContentProvider
    public boolean onCreate() {
        b.f18066c.c(this.TAG, "onCreate lazyInitial:" + getLazyInitial());
        if (!getLazyInitial()) {
            A();
        }
        return super.onCreate();
    }

    /* renamed from: y, reason: from getter */
    public final boolean getLazyInitial() {
        return this.lazyInitial;
    }

    public final List<String> z() {
        List<String> list;
        synchronized (this.cardShowedList) {
            list = this.cardShowedList;
        }
        return list;
    }
}
