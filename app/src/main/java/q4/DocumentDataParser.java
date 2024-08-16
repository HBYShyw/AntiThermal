package q4;

import l4.DocumentData;
import r4.c;

/* compiled from: DocumentDataParser.java */
/* renamed from: q4.i, reason: use source file name */
/* loaded from: classes.dex */
public class DocumentDataParser implements ValueParser<DocumentData> {

    /* renamed from: a, reason: collision with root package name */
    public static final DocumentDataParser f16852a = new DocumentDataParser();

    /* renamed from: b, reason: collision with root package name */
    private static final c.a f16853b = c.a.a("t", "f", "s", "j", "tr", "lh", "ls", "fc", "sc", "sw", "of");

    private DocumentDataParser() {
    }

    @Override // q4.ValueParser
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public DocumentData a(r4.c cVar, float f10) {
        DocumentData.a aVar = DocumentData.a.CENTER;
        cVar.m();
        DocumentData.a aVar2 = aVar;
        String str = null;
        String str2 = null;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        float f11 = 0.0f;
        float f12 = 0.0f;
        float f13 = 0.0f;
        float f14 = 0.0f;
        boolean z10 = true;
        while (cVar.w()) {
            switch (cVar.i0(f16853b)) {
                case 0:
                    str = cVar.X();
                    break;
                case 1:
                    str2 = cVar.X();
                    break;
                case 2:
                    f11 = (float) cVar.N();
                    break;
                case 3:
                    int S = cVar.S();
                    aVar2 = DocumentData.a.CENTER;
                    if (S <= aVar2.ordinal() && S >= 0) {
                        aVar2 = DocumentData.a.values()[S];
                        break;
                    }
                    break;
                case 4:
                    i10 = cVar.S();
                    break;
                case 5:
                    f12 = (float) cVar.N();
                    break;
                case 6:
                    f13 = (float) cVar.N();
                    break;
                case 7:
                    i11 = JsonUtils.d(cVar);
                    break;
                case 8:
                    i12 = JsonUtils.d(cVar);
                    break;
                case 9:
                    f14 = (float) cVar.N();
                    break;
                case 10:
                    z10 = cVar.L();
                    break;
                default:
                    cVar.j0();
                    cVar.m0();
                    break;
            }
        }
        cVar.v();
        return new DocumentData(str, str2, f11, aVar2, i10, f12, f13, i11, i12, f14, z10);
    }
}
