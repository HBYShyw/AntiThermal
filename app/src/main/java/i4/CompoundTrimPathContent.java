package i4;

import android.graphics.Path;
import java.util.ArrayList;
import java.util.List;

/* compiled from: CompoundTrimPathContent.java */
/* renamed from: i4.b, reason: use source file name */
/* loaded from: classes.dex */
public class CompoundTrimPathContent {

    /* renamed from: a, reason: collision with root package name */
    private final List<TrimPathContent> f12520a = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(TrimPathContent trimPathContent) {
        this.f12520a.add(trimPathContent);
    }

    public void b(Path path) {
        for (int size = this.f12520a.size() - 1; size >= 0; size--) {
            s4.h.b(path, this.f12520a.get(size));
        }
    }
}
