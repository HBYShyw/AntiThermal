package com.oplus.deepthinker.sdk.app.awareness.fence.impl;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.collections.s0;
import za.k;

/* compiled from: CompoundFence.kt */
@Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J!\u0010\u0014\u001a\u00020\u00152\u0012\u0010\u0016\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00150\u0017\"\u00020\u0015H\u0007¢\u0006\u0002\u0010\u0018J\u0016\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u0019H\u0007J\u0018\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u0015H\u0007J\u0016\u0010\u001d\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u001eH\u0002J\u0016\u0010\u001f\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u001eH\u0002J\u0010\u0010 \u001a\u00020\u00152\u0006\u0010!\u001a\u00020\u0015H\u0002J\u0016\u0010\"\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u001eH\u0002J$\u0010#\u001a\u00020\u00152\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00150$2\u0006\u0010%\u001a\u00020\u0004H\u0002J\u0016\u0010&\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u001eH\u0002J\u001e\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00150\u001e2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u0015H\u0002J'\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00150\u001e2\u0012\u0010\u0016\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00150\u0017\"\u00020\u0015H\u0002¢\u0006\u0002\u0010(J\u001e\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00150\u001e2\u000e\u0010\u0016\u001a\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u0019H\u0002J(\u0010)\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00150$2\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00150*H\u0002J\u0010\u0010+\u001a\u00020\u00152\u0006\u0010!\u001a\u00020\u0015H\u0007J!\u0010,\u001a\u00020\u00152\u0012\u0010\u0016\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00150\u0017\"\u00020\u0015H\u0007¢\u0006\u0002\u0010\u0018J\u0016\u0010,\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u0019H\u0007J$\u0010-\u001a\u00020\u00152\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00150*2\u0006\u0010%\u001a\u00020\u0004H\u0007J\u0018\u0010.\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u0011¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013¨\u0006/"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/CompoundFence;", "", "()V", "BUNDLE_KEY_AWARENESS_FENCE", "", "BUNDLE_KEY_COMBINE_MODE", "BUNDLE_KEY_COMBINE_SCRIPT", "COMBINE_FENCE_WITH_AND", "", "COMBINE_FENCE_WITH_BEFORE", "COMBINE_FENCE_WITH_NOT", "COMBINE_FENCE_WITH_OR", "COMBINE_FENCE_WITH_SCRIPT", "COMBINE_FENCE_WITH_THEN", "FENCE_NAME", "FENCE_TYPE", "SCRIPT_OPERATOR", "", "getSCRIPT_OPERATOR", "()Ljava/util/Set;", "and", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "fences", "", "([Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;)Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "", "before", "leftFence", "rightFence", "buildAndFence", "Ljava/util/ArrayList;", "buildBeforeFence", "buildNotFence", "fence", "buildOrFence", "buildScriptFence", "Ljava/util/HashMap;", "fenceScript", "buildThenFence", "convertToArrayList", "([Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;)Ljava/util/ArrayList;", "convertToHashMap", "", "not", "or", "script", "then", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class CompoundFence {
    public static final String BUNDLE_KEY_AWARENESS_FENCE = "awareness_fence";
    public static final String BUNDLE_KEY_COMBINE_MODE = "combine_mode";
    public static final String BUNDLE_KEY_COMBINE_SCRIPT = "combine_script";
    public static final int COMBINE_FENCE_WITH_AND = 2;
    public static final int COMBINE_FENCE_WITH_BEFORE = 5;
    public static final int COMBINE_FENCE_WITH_NOT = 4;
    public static final int COMBINE_FENCE_WITH_OR = 3;
    public static final int COMBINE_FENCE_WITH_SCRIPT = 1;
    public static final int COMBINE_FENCE_WITH_THEN = 6;
    public static final String FENCE_NAME = "compound_fence";
    public static final String FENCE_TYPE = "compound_fence";
    public static final CompoundFence INSTANCE = new CompoundFence();
    private static final Set<String> SCRIPT_OPERATOR;

    static {
        Set<String> h10;
        h10 = s0.h("and", "or", "not", "before", "then");
        SCRIPT_OPERATOR = h10;
    }

    private CompoundFence() {
    }

    public static final AwarenessFence and(AwarenessFence... fences) {
        k.e(fences, "fences");
        CompoundFence compoundFence = INSTANCE;
        return compoundFence.buildAndFence(compoundFence.convertToArrayList((AwarenessFence[]) Arrays.copyOf(fences, fences.length)));
    }

    public static final AwarenessFence before(AwarenessFence leftFence, AwarenessFence rightFence) {
        k.e(leftFence, "leftFence");
        k.e(rightFence, "rightFence");
        CompoundFence compoundFence = INSTANCE;
        return compoundFence.buildBeforeFence(compoundFence.convertToArrayList(leftFence, rightFence));
    }

    private final AwarenessFence buildAndFence(ArrayList<AwarenessFence> fences) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("compound_fence_", randomUUID));
        builder.setFenceType("compound_fence");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_COMBINE_MODE, 2);
        bundle.putParcelableArrayList("awareness_fence", fences);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(2);
        return builder.build();
    }

    private final AwarenessFence buildBeforeFence(ArrayList<AwarenessFence> fences) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("compound_fence_", randomUUID));
        builder.setFenceType("compound_fence");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_COMBINE_MODE, 5);
        bundle.putParcelableArrayList("awareness_fence", fences);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(2);
        return builder.build();
    }

    private final AwarenessFence buildNotFence(AwarenessFence fence) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("compound_fence_", randomUUID));
        builder.setFenceType("compound_fence");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_COMBINE_MODE, 4);
        bundle.putParcelable("awareness_fence", fence);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(2);
        return builder.build();
    }

    private final AwarenessFence buildOrFence(ArrayList<AwarenessFence> fences) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("compound_fence_", randomUUID));
        builder.setFenceType("compound_fence");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_COMBINE_MODE, 3);
        bundle.putParcelableArrayList("awareness_fence", fences);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(2);
        return builder.build();
    }

    private final AwarenessFence buildScriptFence(HashMap<String, AwarenessFence> fences, String fenceScript) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("compound_fence_", randomUUID));
        builder.setFenceType("compound_fence");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_COMBINE_MODE, 1);
        bundle.putSerializable("awareness_fence", fences);
        bundle.putString(BUNDLE_KEY_COMBINE_SCRIPT, fenceScript);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(2);
        return builder.build();
    }

    private final AwarenessFence buildThenFence(ArrayList<AwarenessFence> fences) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("compound_fence_", randomUUID));
        builder.setFenceType("compound_fence");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_COMBINE_MODE, 6);
        bundle.putParcelableArrayList("awareness_fence", fences);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(2);
        return builder.build();
    }

    private final ArrayList<AwarenessFence> convertToArrayList(AwarenessFence... fences) {
        int i10 = 0;
        if (fences.length == 0) {
            return new ArrayList<>();
        }
        ArrayList<AwarenessFence> arrayList = new ArrayList<>();
        int length = fences.length;
        while (i10 < length) {
            AwarenessFence awarenessFence = fences[i10];
            i10++;
            arrayList.add(awarenessFence);
        }
        return arrayList;
    }

    private final HashMap<String, AwarenessFence> convertToHashMap(Map<String, AwarenessFence> fences) {
        if (fences instanceof HashMap) {
            return (HashMap) fences;
        }
        HashMap<String, AwarenessFence> hashMap = new HashMap<>();
        for (Map.Entry<String, AwarenessFence> entry : fences.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    public static final AwarenessFence not(AwarenessFence fence) {
        k.e(fence, "fence");
        return INSTANCE.buildNotFence(fence);
    }

    public static final AwarenessFence or(AwarenessFence... fences) {
        k.e(fences, "fences");
        CompoundFence compoundFence = INSTANCE;
        return compoundFence.buildOrFence(compoundFence.convertToArrayList((AwarenessFence[]) Arrays.copyOf(fences, fences.length)));
    }

    public static final AwarenessFence script(Map<String, AwarenessFence> fences, String fenceScript) {
        k.e(fences, "fences");
        k.e(fenceScript, "fenceScript");
        CompoundFence compoundFence = INSTANCE;
        return compoundFence.buildScriptFence(compoundFence.convertToHashMap(fences), fenceScript);
    }

    public static final AwarenessFence then(AwarenessFence leftFence, AwarenessFence rightFence) {
        k.e(leftFence, "leftFence");
        k.e(rightFence, "rightFence");
        CompoundFence compoundFence = INSTANCE;
        return compoundFence.buildThenFence(compoundFence.convertToArrayList(leftFence, rightFence));
    }

    public final Set<String> getSCRIPT_OPERATOR() {
        return SCRIPT_OPERATOR;
    }

    public static final AwarenessFence and(Collection<AwarenessFence> fences) {
        k.e(fences, "fences");
        CompoundFence compoundFence = INSTANCE;
        return compoundFence.buildAndFence(compoundFence.convertToArrayList(fences));
    }

    public static final AwarenessFence or(Collection<AwarenessFence> fences) {
        k.e(fences, "fences");
        CompoundFence compoundFence = INSTANCE;
        return compoundFence.buildOrFence(compoundFence.convertToArrayList(fences));
    }

    private final ArrayList<AwarenessFence> convertToArrayList(Collection<AwarenessFence> fences) {
        if (fences == null || fences.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<AwarenessFence> arrayList = new ArrayList<>();
        Iterator<AwarenessFence> it = fences.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    private final ArrayList<AwarenessFence> convertToArrayList(AwarenessFence leftFence, AwarenessFence rightFence) {
        ArrayList<AwarenessFence> arrayList = new ArrayList<>();
        arrayList.add(leftFence);
        arrayList.add(rightFence);
        return arrayList;
    }
}
