package androidx.window.embedding;

import androidx.window.embedding.EmbeddingInterfaceCompat;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import ma.Unit;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: EmbeddingCompat.kt */
@Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\n\u0010\u0001\u001a\u0006\u0012\u0002\b\u00030\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"", "values", "Lma/f0;", "a", "(Ljava/util/List;)V"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class EmbeddingCompat$setEmbeddingCallback$1 extends Lambda implements l<List<?>, Unit> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ EmbeddingInterfaceCompat.EmbeddingCallbackInterface f4363e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ EmbeddingCompat f4364f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public EmbeddingCompat$setEmbeddingCallback$1(EmbeddingInterfaceCompat.EmbeddingCallbackInterface embeddingCallbackInterface, EmbeddingCompat embeddingCompat) {
        super(1);
        this.f4363e = embeddingCallbackInterface;
        this.f4364f = embeddingCompat;
    }

    public final void a(List<?> list) {
        EmbeddingAdapter embeddingAdapter;
        k.e(list, "values");
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            if (obj instanceof androidx.window.extensions.embedding.SplitInfo) {
                arrayList.add(obj);
            }
        }
        EmbeddingInterfaceCompat.EmbeddingCallbackInterface embeddingCallbackInterface = this.f4363e;
        embeddingAdapter = this.f4364f.adapter;
        embeddingCallbackInterface.a(embeddingAdapter.b(arrayList));
    }

    @Override // ya.l
    public /* bridge */ /* synthetic */ Unit invoke(List<?> list) {
        a(list);
        return Unit.f15173a;
    }
}
