package m;

import java.util.ArrayList;
import l.ArrayRow;
import l.LinearSystem;
import l.SolverVariable;
import m.ConstraintWidget;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Chain.java */
/* loaded from: classes.dex */
public class b {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i10) {
        int i11;
        int i12;
        ChainHead[] chainHeadArr;
        if (i10 == 0) {
            int i13 = constraintWidgetContainer.Q0;
            chainHeadArr = constraintWidgetContainer.T0;
            i12 = i13;
            i11 = 0;
        } else {
            i11 = 2;
            i12 = constraintWidgetContainer.R0;
            chainHeadArr = constraintWidgetContainer.S0;
        }
        for (int i14 = 0; i14 < i12; i14++) {
            ChainHead chainHead = chainHeadArr[i14];
            chainHead.a();
            b(constraintWidgetContainer, linearSystem, i10, i11, chainHead);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002d, code lost:
    
        if (r8 == 2) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0040, code lost:
    
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x003e, code lost:
    
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x003c, code lost:
    
        if (r8 == 2) goto L25;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0258 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x04cb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x04df  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x04e8  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x04ef  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x04ff  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0505 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:158:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x04eb  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x04e2  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x02b1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x039a  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x039b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:234:0x03a3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:242:0x03b6  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x0483  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x04b8  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01b4  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01d1  */
    /* JADX WARN: Type inference failed for: r2v56, types: [m.e] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v2, types: [m.e] */
    /* JADX WARN: Type inference failed for: r7v32 */
    /* JADX WARN: Type inference failed for: r7v33 */
    /* JADX WARN: Type inference failed for: r7v34 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static void b(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i10, int i11, ChainHead chainHead) {
        boolean z10;
        boolean z11;
        boolean z12;
        ArrayList<ConstraintWidget> arrayList;
        ConstraintWidget constraintWidget;
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        ConstraintAnchor constraintAnchor3;
        int i12;
        ConstraintWidget constraintWidget2;
        int i13;
        ConstraintAnchor constraintAnchor4;
        SolverVariable solverVariable;
        SolverVariable solverVariable2;
        ConstraintWidget constraintWidget3;
        ConstraintAnchor constraintAnchor5;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        ConstraintWidget constraintWidget4;
        SolverVariable solverVariable5;
        float f10;
        int size;
        int i14;
        ArrayList<ConstraintWidget> arrayList2;
        boolean z13;
        boolean z14;
        boolean z15;
        ConstraintWidget constraintWidget5;
        ConstraintWidget constraintWidget6;
        int i15;
        ConstraintWidget constraintWidget7 = chainHead.f14710a;
        ConstraintWidget constraintWidget8 = chainHead.f14712c;
        ConstraintWidget constraintWidget9 = chainHead.f14711b;
        ConstraintWidget constraintWidget10 = chainHead.f14713d;
        ConstraintWidget constraintWidget11 = chainHead.f14714e;
        float f11 = chainHead.f14720k;
        boolean z16 = constraintWidgetContainer.O[i10] == ConstraintWidget.b.WRAP_CONTENT;
        if (i10 == 0) {
            int i16 = constraintWidget11.f14795w0;
            z10 = i16 == 0;
            z11 = i16 == 1;
        } else {
            int i17 = constraintWidget11.f14797x0;
            z10 = i17 == 0;
            z11 = i17 == 1;
        }
        ?? r72 = constraintWidget7;
        boolean z17 = false;
        while (true) {
            if (z17) {
                break;
            }
            ConstraintAnchor constraintAnchor6 = r72.L[i11];
            int i18 = z12 ? 1 : 4;
            int c10 = constraintAnchor6.c();
            float f12 = f11;
            ConstraintWidget.b bVar = r72.O[i10];
            boolean z18 = z17;
            ConstraintWidget.b bVar2 = ConstraintWidget.b.MATCH_CONSTRAINT;
            if (bVar == bVar2 && r72.f14776n[i10] == 0) {
                z13 = z11;
                z14 = true;
            } else {
                z13 = z11;
                z14 = false;
            }
            ConstraintAnchor constraintAnchor7 = constraintAnchor6.f14735d;
            if (constraintAnchor7 != null && r72 != constraintWidget7) {
                c10 += constraintAnchor7.c();
            }
            int i19 = c10;
            if (!z12 || r72 == constraintWidget7 || r72 == constraintWidget9) {
                z15 = z10;
            } else {
                z15 = z10;
                i18 = 5;
            }
            ConstraintAnchor constraintAnchor8 = constraintAnchor6.f14735d;
            if (constraintAnchor8 != null) {
                if (r72 == constraintWidget9) {
                    constraintWidget5 = constraintWidget11;
                    constraintWidget6 = constraintWidget7;
                    linearSystem.h(constraintAnchor6.f14738g, constraintAnchor8.f14738g, i19, 6);
                } else {
                    constraintWidget5 = constraintWidget11;
                    constraintWidget6 = constraintWidget7;
                    linearSystem.h(constraintAnchor6.f14738g, constraintAnchor8.f14738g, i19, 8);
                }
                linearSystem.e(constraintAnchor6.f14738g, constraintAnchor6.f14735d.f14738g, i19, (!z14 || z12) ? i18 : 5);
            } else {
                constraintWidget5 = constraintWidget11;
                constraintWidget6 = constraintWidget7;
            }
            if (z16) {
                if (r72.P() == 8 || r72.O[i10] != bVar2) {
                    i15 = 0;
                } else {
                    ConstraintAnchor[] constraintAnchorArr = r72.L;
                    i15 = 0;
                    linearSystem.h(constraintAnchorArr[i11 + 1].f14738g, constraintAnchorArr[i11].f14738g, 0, 5);
                }
                linearSystem.h(r72.L[i11].f14738g, constraintWidgetContainer.L[i11].f14738g, i15, 8);
            }
            ConstraintAnchor constraintAnchor9 = r72.L[i11 + 1].f14735d;
            if (constraintAnchor9 != null) {
                ?? r22 = constraintAnchor9.f14733b;
                ConstraintAnchor[] constraintAnchorArr2 = r22.L;
                if (constraintAnchorArr2[i11].f14735d != null && constraintAnchorArr2[i11].f14735d.f14733b == r72) {
                    r21 = r22;
                }
            }
            if (r21 != null) {
                r72 = r21;
                z17 = z18;
            } else {
                z17 = true;
            }
            z10 = z15;
            f11 = f12;
            z11 = z13;
            constraintWidget11 = constraintWidget5;
            constraintWidget7 = constraintWidget6;
            r72 = r72;
        }
        ConstraintWidget constraintWidget12 = constraintWidget11;
        float f13 = f11;
        ConstraintWidget constraintWidget13 = constraintWidget7;
        boolean z19 = z10;
        boolean z20 = z11;
        if (constraintWidget10 != null) {
            int i20 = i11 + 1;
            if (constraintWidget8.L[i20].f14735d != null) {
                ConstraintAnchor constraintAnchor10 = constraintWidget10.L[i20];
                if ((constraintWidget10.O[i10] == ConstraintWidget.b.MATCH_CONSTRAINT && constraintWidget10.f14776n[i10] == 0) && !z12) {
                    ConstraintAnchor constraintAnchor11 = constraintAnchor10.f14735d;
                    if (constraintAnchor11.f14733b == constraintWidgetContainer) {
                        linearSystem.e(constraintAnchor10.f14738g, constraintAnchor11.f14738g, -constraintAnchor10.c(), 5);
                        linearSystem.j(constraintAnchor10.f14738g, constraintWidget8.L[i20].f14735d.f14738g, -constraintAnchor10.c(), 6);
                        if (z16) {
                            int i21 = i11 + 1;
                            SolverVariable solverVariable6 = constraintWidgetContainer.L[i21].f14738g;
                            ConstraintAnchor[] constraintAnchorArr3 = constraintWidget8.L;
                            linearSystem.h(solverVariable6, constraintAnchorArr3[i21].f14738g, constraintAnchorArr3[i21].c(), 8);
                        }
                        arrayList = chainHead.f14717h;
                        if (arrayList != null && (size = arrayList.size()) > 1) {
                            float f14 = (chainHead.f14727r || chainHead.f14729t) ? f13 : chainHead.f14719j;
                            float f15 = 0.0f;
                            float f16 = 0.0f;
                            ConstraintWidget constraintWidget14 = null;
                            i14 = 0;
                            while (i14 < size) {
                                ConstraintWidget constraintWidget15 = arrayList.get(i14);
                                float f17 = constraintWidget15.A0[i10];
                                if (f17 < f15) {
                                    if (chainHead.f14729t) {
                                        ConstraintAnchor[] constraintAnchorArr4 = constraintWidget15.L;
                                        linearSystem.e(constraintAnchorArr4[i11 + 1].f14738g, constraintAnchorArr4[i11].f14738g, 0, 4);
                                        arrayList2 = arrayList;
                                        i14++;
                                        arrayList = arrayList2;
                                        f15 = 0.0f;
                                    } else {
                                        f17 = 1.0f;
                                    }
                                }
                                if (f17 == f15) {
                                    ConstraintAnchor[] constraintAnchorArr5 = constraintWidget15.L;
                                    linearSystem.e(constraintAnchorArr5[i11 + 1].f14738g, constraintAnchorArr5[i11].f14738g, 0, 8);
                                    arrayList2 = arrayList;
                                    i14++;
                                    arrayList = arrayList2;
                                    f15 = 0.0f;
                                } else {
                                    if (constraintWidget14 != null) {
                                        ConstraintAnchor[] constraintAnchorArr6 = constraintWidget14.L;
                                        SolverVariable solverVariable7 = constraintAnchorArr6[i11].f14738g;
                                        int i22 = i11 + 1;
                                        SolverVariable solverVariable8 = constraintAnchorArr6[i22].f14738g;
                                        ConstraintAnchor[] constraintAnchorArr7 = constraintWidget15.L;
                                        SolverVariable solverVariable9 = constraintAnchorArr7[i11].f14738g;
                                        SolverVariable solverVariable10 = constraintAnchorArr7[i22].f14738g;
                                        arrayList2 = arrayList;
                                        ArrayRow r10 = linearSystem.r();
                                        r10.l(f16, f14, f17, solverVariable7, solverVariable8, solverVariable9, solverVariable10);
                                        linearSystem.d(r10);
                                    } else {
                                        arrayList2 = arrayList;
                                    }
                                    constraintWidget14 = constraintWidget15;
                                    f16 = f17;
                                    i14++;
                                    arrayList = arrayList2;
                                    f15 = 0.0f;
                                }
                            }
                        }
                        if (constraintWidget9 == null && (constraintWidget9 == constraintWidget10 || z12)) {
                            ConstraintAnchor constraintAnchor12 = constraintWidget13.L[i11];
                            int i23 = i11 + 1;
                            ConstraintAnchor constraintAnchor13 = constraintWidget8.L[i23];
                            ConstraintAnchor constraintAnchor14 = constraintAnchor12.f14735d;
                            SolverVariable solverVariable11 = constraintAnchor14 != null ? constraintAnchor14.f14738g : null;
                            ConstraintAnchor constraintAnchor15 = constraintAnchor13.f14735d;
                            SolverVariable solverVariable12 = constraintAnchor15 != null ? constraintAnchor15.f14738g : null;
                            ConstraintAnchor constraintAnchor16 = constraintWidget9.L[i11];
                            ConstraintAnchor constraintAnchor17 = constraintWidget10.L[i23];
                            if (solverVariable11 != null && solverVariable12 != null) {
                                if (i10 == 0) {
                                    f10 = constraintWidget12.f14757d0;
                                } else {
                                    f10 = constraintWidget12.f14759e0;
                                }
                                linearSystem.c(constraintAnchor16.f14738g, solverVariable11, constraintAnchor16.c(), f10, solverVariable12, constraintAnchor17.f14738g, constraintAnchor17.c(), 7);
                            }
                        } else if (z19 || constraintWidget9 == null) {
                            int i24 = 8;
                            if (z20 && constraintWidget9 != null) {
                                int i25 = chainHead.f14719j;
                                boolean z21 = i25 <= 0 && chainHead.f14718i == i25;
                                constraintWidget = constraintWidget9;
                                ConstraintWidget constraintWidget16 = constraintWidget;
                                while (constraintWidget != null) {
                                    ConstraintWidget constraintWidget17 = constraintWidget.C0[i10];
                                    while (constraintWidget17 != null && constraintWidget17.P() == i24) {
                                        constraintWidget17 = constraintWidget17.C0[i10];
                                    }
                                    if (constraintWidget == constraintWidget9 || constraintWidget == constraintWidget10 || constraintWidget17 == null) {
                                        constraintWidget2 = constraintWidget16;
                                        i13 = i24;
                                    } else {
                                        ConstraintWidget constraintWidget18 = constraintWidget17 == constraintWidget10 ? null : constraintWidget17;
                                        ConstraintAnchor constraintAnchor18 = constraintWidget.L[i11];
                                        SolverVariable solverVariable13 = constraintAnchor18.f14738g;
                                        ConstraintAnchor constraintAnchor19 = constraintAnchor18.f14735d;
                                        if (constraintAnchor19 != null) {
                                            SolverVariable solverVariable14 = constraintAnchor19.f14738g;
                                        }
                                        int i26 = i11 + 1;
                                        SolverVariable solverVariable15 = constraintWidget16.L[i26].f14738g;
                                        int c11 = constraintAnchor18.c();
                                        int c12 = constraintWidget.L[i26].c();
                                        if (constraintWidget18 != null) {
                                            constraintAnchor4 = constraintWidget18.L[i11];
                                            solverVariable = constraintAnchor4.f14738g;
                                            ConstraintAnchor constraintAnchor20 = constraintAnchor4.f14735d;
                                            solverVariable2 = constraintAnchor20 != null ? constraintAnchor20.f14738g : null;
                                        } else {
                                            constraintAnchor4 = constraintWidget10.L[i11];
                                            solverVariable = constraintAnchor4 != null ? constraintAnchor4.f14738g : null;
                                            solverVariable2 = constraintWidget.L[i26].f14738g;
                                        }
                                        if (constraintAnchor4 != null) {
                                            c12 += constraintAnchor4.c();
                                        }
                                        int i27 = c12;
                                        int c13 = constraintWidget16.L[i26].c() + c11;
                                        int i28 = z21 ? 8 : 4;
                                        if (solverVariable13 == null || solverVariable15 == null || solverVariable == null || solverVariable2 == null) {
                                            constraintWidget3 = constraintWidget18;
                                            constraintWidget2 = constraintWidget16;
                                            i13 = 8;
                                        } else {
                                            constraintWidget3 = constraintWidget18;
                                            constraintWidget2 = constraintWidget16;
                                            i13 = 8;
                                            linearSystem.c(solverVariable13, solverVariable15, c13, 0.5f, solverVariable, solverVariable2, i27, i28);
                                        }
                                        constraintWidget17 = constraintWidget3;
                                    }
                                    if (constraintWidget.P() == i13) {
                                        constraintWidget = constraintWidget2;
                                    }
                                    i24 = i13;
                                    constraintWidget16 = constraintWidget;
                                    constraintWidget = constraintWidget17;
                                }
                                ConstraintAnchor constraintAnchor21 = constraintWidget9.L[i11];
                                constraintAnchor = constraintWidget13.L[i11].f14735d;
                                int i29 = i11 + 1;
                                constraintAnchor2 = constraintWidget10.L[i29];
                                constraintAnchor3 = constraintWidget8.L[i29].f14735d;
                                if (constraintAnchor != null) {
                                    i12 = 5;
                                } else if (constraintWidget9 != constraintWidget10) {
                                    i12 = 5;
                                    linearSystem.e(constraintAnchor21.f14738g, constraintAnchor.f14738g, constraintAnchor21.c(), 5);
                                } else {
                                    i12 = 5;
                                    if (constraintAnchor3 != null) {
                                        linearSystem.c(constraintAnchor21.f14738g, constraintAnchor.f14738g, constraintAnchor21.c(), 0.5f, constraintAnchor2.f14738g, constraintAnchor3.f14738g, constraintAnchor2.c(), 5);
                                    }
                                }
                                if (constraintAnchor3 != null && constraintWidget9 != constraintWidget10) {
                                    linearSystem.e(constraintAnchor2.f14738g, constraintAnchor3.f14738g, -constraintAnchor2.c(), i12);
                                }
                            }
                        } else {
                            int i30 = chainHead.f14719j;
                            boolean z22 = i30 > 0 && chainHead.f14718i == i30;
                            ConstraintWidget constraintWidget19 = constraintWidget9;
                            ConstraintWidget constraintWidget20 = constraintWidget19;
                            while (constraintWidget19 != null) {
                                ConstraintWidget constraintWidget21 = constraintWidget19.C0[i10];
                                while (constraintWidget21 != null && constraintWidget21.P() == 8) {
                                    constraintWidget21 = constraintWidget21.C0[i10];
                                }
                                if (constraintWidget21 != null || constraintWidget19 == constraintWidget10) {
                                    ConstraintAnchor constraintAnchor22 = constraintWidget19.L[i11];
                                    SolverVariable solverVariable16 = constraintAnchor22.f14738g;
                                    ConstraintAnchor constraintAnchor23 = constraintAnchor22.f14735d;
                                    SolverVariable solverVariable17 = constraintAnchor23 != null ? constraintAnchor23.f14738g : null;
                                    if (constraintWidget20 != constraintWidget19) {
                                        solverVariable17 = constraintWidget20.L[i11 + 1].f14738g;
                                    } else if (constraintWidget19 == constraintWidget9 && constraintWidget20 == constraintWidget19) {
                                        ConstraintAnchor[] constraintAnchorArr8 = constraintWidget13.L;
                                        solverVariable17 = constraintAnchorArr8[i11].f14735d != null ? constraintAnchorArr8[i11].f14735d.f14738g : null;
                                    }
                                    int c14 = constraintAnchor22.c();
                                    int i31 = i11 + 1;
                                    int c15 = constraintWidget19.L[i31].c();
                                    if (constraintWidget21 != null) {
                                        constraintAnchor5 = constraintWidget21.L[i11];
                                        SolverVariable solverVariable18 = constraintAnchor5.f14738g;
                                        solverVariable4 = constraintWidget19.L[i31].f14738g;
                                        solverVariable3 = solverVariable18;
                                    } else {
                                        constraintAnchor5 = constraintWidget8.L[i31].f14735d;
                                        solverVariable3 = constraintAnchor5 != null ? constraintAnchor5.f14738g : null;
                                        solverVariable4 = constraintWidget19.L[i31].f14738g;
                                    }
                                    if (constraintAnchor5 != null) {
                                        c15 += constraintAnchor5.c();
                                    }
                                    if (constraintWidget20 != null) {
                                        c14 += constraintWidget20.L[i31].c();
                                    }
                                    if (solverVariable16 != null && solverVariable17 != null && solverVariable3 != null && solverVariable4 != null) {
                                        if (constraintWidget19 == constraintWidget9) {
                                            c14 = constraintWidget9.L[i11].c();
                                        }
                                        int i32 = c14;
                                        constraintWidget4 = constraintWidget21;
                                        linearSystem.c(solverVariable16, solverVariable17, i32, 0.5f, solverVariable3, solverVariable4, constraintWidget19 == constraintWidget10 ? constraintWidget10.L[i31].c() : c15, z22 ? 8 : 5);
                                        if (constraintWidget19.P() == 8) {
                                            constraintWidget20 = constraintWidget19;
                                        }
                                        constraintWidget19 = constraintWidget4;
                                    }
                                }
                                constraintWidget4 = constraintWidget21;
                                if (constraintWidget19.P() == 8) {
                                }
                                constraintWidget19 = constraintWidget4;
                            }
                        }
                        if ((z19 && !z20) || constraintWidget9 == null || constraintWidget9 == constraintWidget10) {
                            return;
                        }
                        ConstraintAnchor[] constraintAnchorArr9 = constraintWidget9.L;
                        ConstraintAnchor constraintAnchor24 = constraintAnchorArr9[i11];
                        int i33 = i11 + 1;
                        ConstraintAnchor constraintAnchor25 = constraintWidget10.L[i33];
                        ConstraintAnchor constraintAnchor26 = constraintAnchor24.f14735d;
                        solverVariable5 = constraintAnchor26 != null ? constraintAnchor26.f14738g : null;
                        ConstraintAnchor constraintAnchor27 = constraintAnchor25.f14735d;
                        SolverVariable solverVariable19 = constraintAnchor27 != null ? constraintAnchor27.f14738g : null;
                        if (constraintWidget8 != constraintWidget10) {
                            ConstraintAnchor constraintAnchor28 = constraintWidget8.L[i33].f14735d;
                            solverVariable19 = constraintAnchor28 != null ? constraintAnchor28.f14738g : null;
                        }
                        if (constraintWidget9 == constraintWidget10) {
                            constraintAnchor24 = constraintAnchorArr9[i11];
                            constraintAnchor25 = constraintAnchorArr9[i33];
                        }
                        if (solverVariable5 == null || solverVariable19 == null) {
                            return;
                        }
                        linearSystem.c(constraintAnchor24.f14738g, solverVariable5, constraintAnchor24.c(), 0.5f, solverVariable19, constraintAnchor25.f14738g, constraintWidget10.L[i33].c(), 5);
                        return;
                    }
                }
                if (z12) {
                    ConstraintAnchor constraintAnchor29 = constraintAnchor10.f14735d;
                    if (constraintAnchor29.f14733b == constraintWidgetContainer) {
                        linearSystem.e(constraintAnchor10.f14738g, constraintAnchor29.f14738g, -constraintAnchor10.c(), 4);
                    }
                }
                linearSystem.j(constraintAnchor10.f14738g, constraintWidget8.L[i20].f14735d.f14738g, -constraintAnchor10.c(), 6);
                if (z16) {
                }
                arrayList = chainHead.f14717h;
                if (arrayList != null) {
                    if (chainHead.f14727r) {
                    }
                    float f152 = 0.0f;
                    float f162 = 0.0f;
                    ConstraintWidget constraintWidget142 = null;
                    i14 = 0;
                    while (i14 < size) {
                    }
                }
                if (constraintWidget9 == null) {
                }
                if (z19) {
                }
                int i242 = 8;
                if (z20) {
                    int i252 = chainHead.f14719j;
                    if (i252 <= 0) {
                    }
                    constraintWidget = constraintWidget9;
                    ConstraintWidget constraintWidget162 = constraintWidget;
                    while (constraintWidget != null) {
                    }
                    ConstraintAnchor constraintAnchor212 = constraintWidget9.L[i11];
                    constraintAnchor = constraintWidget13.L[i11].f14735d;
                    int i292 = i11 + 1;
                    constraintAnchor2 = constraintWidget10.L[i292];
                    constraintAnchor3 = constraintWidget8.L[i292].f14735d;
                    if (constraintAnchor != null) {
                    }
                    if (constraintAnchor3 != null) {
                        linearSystem.e(constraintAnchor2.f14738g, constraintAnchor3.f14738g, -constraintAnchor2.c(), i12);
                    }
                }
                if (z19) {
                }
                ConstraintAnchor[] constraintAnchorArr92 = constraintWidget9.L;
                ConstraintAnchor constraintAnchor242 = constraintAnchorArr92[i11];
                int i332 = i11 + 1;
                ConstraintAnchor constraintAnchor252 = constraintWidget10.L[i332];
                ConstraintAnchor constraintAnchor262 = constraintAnchor242.f14735d;
                if (constraintAnchor262 != null) {
                }
                ConstraintAnchor constraintAnchor272 = constraintAnchor252.f14735d;
                if (constraintAnchor272 != null) {
                }
                if (constraintWidget8 != constraintWidget10) {
                }
                if (constraintWidget9 == constraintWidget10) {
                }
                if (solverVariable5 == null) {
                    return;
                } else {
                    return;
                }
            }
        }
        if (z16) {
        }
        arrayList = chainHead.f14717h;
        if (arrayList != null) {
        }
        if (constraintWidget9 == null) {
        }
        if (z19) {
        }
        int i2422 = 8;
        if (z20) {
        }
        if (z19) {
        }
        ConstraintAnchor[] constraintAnchorArr922 = constraintWidget9.L;
        ConstraintAnchor constraintAnchor2422 = constraintAnchorArr922[i11];
        int i3322 = i11 + 1;
        ConstraintAnchor constraintAnchor2522 = constraintWidget10.L[i3322];
        ConstraintAnchor constraintAnchor2622 = constraintAnchor2422.f14735d;
        if (constraintAnchor2622 != null) {
        }
        ConstraintAnchor constraintAnchor2722 = constraintAnchor2522.f14735d;
        if (constraintAnchor2722 != null) {
        }
        if (constraintWidget8 != constraintWidget10) {
        }
        if (constraintWidget9 == constraintWidget10) {
        }
        if (solverVariable5 == null) {
        }
    }
}
