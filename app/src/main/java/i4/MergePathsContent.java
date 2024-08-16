package i4;

import android.annotation.TargetApi;
import android.graphics.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import n4.MergePaths;

/* compiled from: MergePathsContent.java */
@TargetApi(19)
/* renamed from: i4.l, reason: use source file name */
/* loaded from: classes.dex */
public class MergePathsContent implements PathContent, GreedyContent {

    /* renamed from: d, reason: collision with root package name */
    private final String f12586d;

    /* renamed from: f, reason: collision with root package name */
    private final MergePaths f12588f;

    /* renamed from: a, reason: collision with root package name */
    private final Path f12583a = new Path();

    /* renamed from: b, reason: collision with root package name */
    private final Path f12584b = new Path();

    /* renamed from: c, reason: collision with root package name */
    private final Path f12585c = new Path();

    /* renamed from: e, reason: collision with root package name */
    private final List<PathContent> f12587e = new ArrayList();

    /* compiled from: MergePathsContent.java */
    /* renamed from: i4.l$a */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f12589a;

        static {
            int[] iArr = new int[MergePaths.a.values().length];
            f12589a = iArr;
            try {
                iArr[MergePaths.a.MERGE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f12589a[MergePaths.a.ADD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f12589a[MergePaths.a.SUBTRACT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f12589a[MergePaths.a.INTERSECT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f12589a[MergePaths.a.EXCLUDE_INTERSECTIONS.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public MergePathsContent(MergePaths mergePaths) {
        this.f12586d = mergePaths.c();
        this.f12588f = mergePaths;
    }

    private void a() {
        for (int i10 = 0; i10 < this.f12587e.size(); i10++) {
            this.f12585c.addPath(this.f12587e.get(i10).getPath());
        }
    }

    @TargetApi(19)
    private void c(Path.Op op) {
        this.f12584b.reset();
        this.f12583a.reset();
        for (int size = this.f12587e.size() - 1; size >= 1; size--) {
            PathContent pathContent = this.f12587e.get(size);
            if (pathContent instanceof ContentGroup) {
                ContentGroup contentGroup = (ContentGroup) pathContent;
                List<PathContent> i10 = contentGroup.i();
                for (int size2 = i10.size() - 1; size2 >= 0; size2--) {
                    Path path = i10.get(size2).getPath();
                    path.transform(contentGroup.j());
                    this.f12584b.addPath(path);
                }
            } else {
                this.f12584b.addPath(pathContent.getPath());
            }
        }
        PathContent pathContent2 = this.f12587e.get(0);
        if (pathContent2 instanceof ContentGroup) {
            ContentGroup contentGroup2 = (ContentGroup) pathContent2;
            List<PathContent> i11 = contentGroup2.i();
            for (int i12 = 0; i12 < i11.size(); i12++) {
                Path path2 = i11.get(i12).getPath();
                path2.transform(contentGroup2.j());
                this.f12583a.addPath(path2);
            }
        } else {
            this.f12583a.set(pathContent2.getPath());
        }
        this.f12585c.op(this.f12583a, this.f12584b, op);
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
        for (int i10 = 0; i10 < this.f12587e.size(); i10++) {
            this.f12587e.get(i10).b(list, list2);
        }
    }

    @Override // i4.GreedyContent
    public void f(ListIterator<Content> listIterator) {
        while (listIterator.hasPrevious() && listIterator.previous() != this) {
        }
        while (listIterator.hasPrevious()) {
            Content previous = listIterator.previous();
            if (previous instanceof PathContent) {
                this.f12587e.add((PathContent) previous);
                listIterator.remove();
            }
        }
    }

    @Override // i4.PathContent
    public Path getPath() {
        this.f12585c.reset();
        if (this.f12588f.d()) {
            return this.f12585c;
        }
        int i10 = a.f12589a[this.f12588f.b().ordinal()];
        if (i10 == 1) {
            a();
        } else if (i10 == 2) {
            c(Path.Op.UNION);
        } else if (i10 == 3) {
            c(Path.Op.REVERSE_DIFFERENCE);
        } else if (i10 == 4) {
            c(Path.Op.INTERSECT);
        } else if (i10 == 5) {
            c(Path.Op.XOR);
        }
        return this.f12585c;
    }
}
