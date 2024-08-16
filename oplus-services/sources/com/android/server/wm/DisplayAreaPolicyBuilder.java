package com.android.server.wm;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.window.WindowContainerToken;
import com.android.internal.annotations.Keep;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.wm.DisplayArea;
import com.android.server.wm.DisplayAreaPolicyBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;

@Keep
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
class DisplayAreaPolicyBuilder {
    private final ArrayList<HierarchyBuilder> mDisplayAreaGroupHierarchyBuilders = new ArrayList<>();
    private HierarchyBuilder mRootHierarchyBuilder;
    private BiFunction<Integer, Bundle, RootDisplayArea> mSelectRootForWindowFunc;
    private Function<Bundle, TaskDisplayArea> mSelectTaskDisplayAreaFunc;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface NewDisplayAreaSupplier {
        DisplayArea create(WindowManagerService windowManagerService, DisplayArea.Type type, String str, int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayAreaPolicyBuilder setRootHierarchy(HierarchyBuilder hierarchyBuilder) {
        this.mRootHierarchyBuilder = hierarchyBuilder;
        return this;
    }

    DisplayAreaPolicyBuilder addDisplayAreaGroupHierarchy(HierarchyBuilder hierarchyBuilder) {
        this.mDisplayAreaGroupHierarchyBuilders.add(hierarchyBuilder);
        return this;
    }

    DisplayAreaPolicyBuilder setSelectRootForWindowFunc(BiFunction<Integer, Bundle, RootDisplayArea> biFunction) {
        this.mSelectRootForWindowFunc = biFunction;
        return this;
    }

    DisplayAreaPolicyBuilder setSelectTaskDisplayAreaFunc(Function<Bundle, TaskDisplayArea> function) {
        this.mSelectTaskDisplayAreaFunc = function;
        return this;
    }

    private void validate() {
        if (this.mRootHierarchyBuilder == null) {
            throw new IllegalStateException("Root must be set for the display area policy.");
        }
        ArraySet arraySet = new ArraySet();
        ArraySet arraySet2 = new ArraySet();
        validateIds(this.mRootHierarchyBuilder, arraySet, arraySet2);
        boolean z = this.mRootHierarchyBuilder.mImeContainer != null;
        boolean containsDefaultTaskDisplayArea = containsDefaultTaskDisplayArea(this.mRootHierarchyBuilder);
        for (int i = 0; i < this.mDisplayAreaGroupHierarchyBuilders.size(); i++) {
            HierarchyBuilder hierarchyBuilder = this.mDisplayAreaGroupHierarchyBuilders.get(i);
            validateIds(hierarchyBuilder, arraySet, arraySet2);
            if (hierarchyBuilder.mTaskDisplayAreas.isEmpty()) {
                throw new IllegalStateException("DisplayAreaGroup must contain at least one TaskDisplayArea.");
            }
            if (z) {
                if (hierarchyBuilder.mImeContainer != null) {
                    throw new IllegalStateException("Only one DisplayArea hierarchy can contain the IME container");
                }
            } else {
                z = hierarchyBuilder.mImeContainer != null;
            }
            if (containsDefaultTaskDisplayArea) {
                if (containsDefaultTaskDisplayArea(hierarchyBuilder)) {
                    throw new IllegalStateException("Only one TaskDisplayArea can have the feature id of FEATURE_DEFAULT_TASK_CONTAINER");
                }
            } else {
                containsDefaultTaskDisplayArea = containsDefaultTaskDisplayArea(hierarchyBuilder);
            }
        }
        if (!z) {
            throw new IllegalStateException("IME container must be set.");
        }
        if (!containsDefaultTaskDisplayArea) {
            throw new IllegalStateException("There must be a default TaskDisplayArea with id of FEATURE_DEFAULT_TASK_CONTAINER.");
        }
    }

    private static boolean containsDefaultTaskDisplayArea(HierarchyBuilder hierarchyBuilder) {
        for (int i = 0; i < hierarchyBuilder.mTaskDisplayAreas.size(); i++) {
            if (((TaskDisplayArea) hierarchyBuilder.mTaskDisplayAreas.get(i)).mFeatureId == 1) {
                return true;
            }
        }
        return false;
    }

    private static void validateIds(HierarchyBuilder hierarchyBuilder, Set<Integer> set, Set<Integer> set2) {
        int i = hierarchyBuilder.mRoot.mFeatureId;
        if (!set2.add(Integer.valueOf(i)) || !set.add(Integer.valueOf(i))) {
            throw new IllegalStateException("RootDisplayArea must have unique id, but id=" + i + " is not unique.");
        }
        if (i > 20001) {
            throw new IllegalStateException("RootDisplayArea should not have an id greater than FEATURE_VENDOR_LAST.");
        }
        for (int i2 = 0; i2 < hierarchyBuilder.mTaskDisplayAreas.size(); i2++) {
            int i3 = ((TaskDisplayArea) hierarchyBuilder.mTaskDisplayAreas.get(i2)).mFeatureId;
            if (!set2.add(Integer.valueOf(i3)) || !set.add(Integer.valueOf(i3))) {
                throw new IllegalStateException("TaskDisplayArea must have unique id, but id=" + i3 + " is not unique.");
            }
            if (i3 > 20001) {
                throw new IllegalStateException("TaskDisplayArea declared in the policy should nothave an id greater than FEATURE_VENDOR_LAST.");
            }
        }
        ArraySet arraySet = new ArraySet();
        for (int i4 = 0; i4 < hierarchyBuilder.mFeatures.size(); i4++) {
            int id = ((Feature) hierarchyBuilder.mFeatures.get(i4)).getId();
            if (set.contains(Integer.valueOf(id))) {
                throw new IllegalStateException("Feature must not have same id with any RootDisplayArea or TaskDisplayArea, but id=" + id + " is used");
            }
            if (!arraySet.add(Integer.valueOf(id))) {
                throw new IllegalStateException("Feature below the same root must have unique id, but id=" + id + " is not unique.");
            }
            if (id > 20001) {
                throw new IllegalStateException("Feature should not have an id greater than FEATURE_VENDOR_LAST.");
            }
        }
        set2.addAll(arraySet);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Result build(WindowManagerService windowManagerService) {
        validate();
        this.mRootHierarchyBuilder.build(this.mDisplayAreaGroupHierarchyBuilders);
        ArrayList arrayList = new ArrayList(this.mDisplayAreaGroupHierarchyBuilders.size());
        for (int i = 0; i < this.mDisplayAreaGroupHierarchyBuilders.size(); i++) {
            HierarchyBuilder hierarchyBuilder = this.mDisplayAreaGroupHierarchyBuilders.get(i);
            hierarchyBuilder.build();
            arrayList.add(hierarchyBuilder.mRoot);
        }
        if (this.mSelectRootForWindowFunc == null) {
            this.mSelectRootForWindowFunc = new DefaultSelectRootForWindowFunction(this.mRootHierarchyBuilder.mRoot, arrayList);
        }
        return new Result(windowManagerService, this.mRootHierarchyBuilder.mRoot, arrayList, this.mSelectRootForWindowFunc, this.mSelectTaskDisplayAreaFunc);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class DefaultSelectRootForWindowFunction implements BiFunction<Integer, Bundle, RootDisplayArea> {
        final List<RootDisplayArea> mDisplayAreaGroupRoots;
        final RootDisplayArea mDisplayRoot;

        DefaultSelectRootForWindowFunction(RootDisplayArea rootDisplayArea, List<RootDisplayArea> list) {
            this.mDisplayRoot = rootDisplayArea;
            this.mDisplayAreaGroupRoots = Collections.unmodifiableList(list);
        }

        @Override // java.util.function.BiFunction
        public RootDisplayArea apply(Integer num, Bundle bundle) {
            if (this.mDisplayAreaGroupRoots.isEmpty()) {
                return this.mDisplayRoot;
            }
            if (bundle != null && bundle.containsKey("root_display_area_id")) {
                int i = bundle.getInt("root_display_area_id");
                RootDisplayArea rootDisplayArea = this.mDisplayRoot;
                if (rootDisplayArea.mFeatureId == i) {
                    return rootDisplayArea;
                }
                for (int size = this.mDisplayAreaGroupRoots.size() - 1; size >= 0; size--) {
                    if (this.mDisplayAreaGroupRoots.get(size).mFeatureId == i) {
                        return this.mDisplayAreaGroupRoots.get(size);
                    }
                }
            }
            return this.mDisplayRoot;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private static class DefaultSelectTaskDisplayAreaFunction implements Function<Bundle, TaskDisplayArea> {
        private final TaskDisplayArea mDefaultTaskDisplayArea;
        private final int mDisplayId;

        DefaultSelectTaskDisplayAreaFunction(TaskDisplayArea taskDisplayArea) {
            this.mDefaultTaskDisplayArea = taskDisplayArea;
            this.mDisplayId = taskDisplayArea.getDisplayId();
        }

        @Override // java.util.function.Function
        public TaskDisplayArea apply(Bundle bundle) {
            if (bundle == null) {
                return this.mDefaultTaskDisplayArea;
            }
            WindowContainerToken launchTaskDisplayArea = new ActivityOptions(bundle).getLaunchTaskDisplayArea();
            if (launchTaskDisplayArea == null) {
                return this.mDefaultTaskDisplayArea;
            }
            TaskDisplayArea asTaskDisplayArea = WindowContainer.fromBinder(launchTaskDisplayArea.asBinder()).asTaskDisplayArea();
            if (asTaskDisplayArea == null) {
                if (ProtoLogCache.WM_DEBUG_WINDOW_ORGANIZER_enabled) {
                    ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 1874559932, 0, (String) null, new Object[]{String.valueOf(launchTaskDisplayArea)});
                }
                return this.mDefaultTaskDisplayArea;
            }
            if (asTaskDisplayArea.getDisplayId() == this.mDisplayId) {
                return asTaskDisplayArea;
            }
            throw new IllegalArgumentException("The specified TaskDisplayArea must attach to Display#" + this.mDisplayId + ", but it is in Display#" + asTaskDisplayArea.getDisplayId());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class HierarchyBuilder {
        private static final int LEAF_TYPE_IME_CONTAINERS = 2;
        private static final int LEAF_TYPE_TASK_CONTAINERS = 1;
        private static final int LEAF_TYPE_TOKENS = 0;
        private DisplayArea.Tokens mImeContainer;
        private final RootDisplayArea mRoot;
        private final ArrayList<Feature> mFeatures = new ArrayList<>();
        private final ArrayList<TaskDisplayArea> mTaskDisplayAreas = new ArrayList<>();

        /* JADX INFO: Access modifiers changed from: package-private */
        public HierarchyBuilder(RootDisplayArea rootDisplayArea) {
            this.mRoot = rootDisplayArea;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public HierarchyBuilder addFeature(Feature feature) {
            this.mFeatures.add(feature);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public HierarchyBuilder setTaskDisplayAreas(List<TaskDisplayArea> list) {
            this.mTaskDisplayAreas.clear();
            this.mTaskDisplayAreas.addAll(list);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public HierarchyBuilder setImeContainer(DisplayArea.Tokens tokens) {
            this.mImeContainer = tokens;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void build() {
            build(null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void build(List<HierarchyBuilder> list) {
            WindowManagerPolicy windowManagerPolicy = this.mRoot.mWmService.mPolicy;
            int maxWindowLayer = windowManagerPolicy.getMaxWindowLayer() + 1;
            DisplayArea.Tokens[] tokensArr = new DisplayArea.Tokens[maxWindowLayer];
            ArrayMap arrayMap = new ArrayMap(this.mFeatures.size());
            int i = 0;
            for (int i2 = 0; i2 < this.mFeatures.size(); i2++) {
                arrayMap.put(this.mFeatures.get(i2), new ArrayList());
            }
            PendingArea[] pendingAreaArr = new PendingArea[maxWindowLayer];
            PendingArea pendingArea = new PendingArea(null, 0, null);
            Arrays.fill(pendingAreaArr, pendingArea);
            int size = this.mFeatures.size();
            int i3 = 0;
            while (i3 < size) {
                Feature feature = this.mFeatures.get(i3);
                PendingArea pendingArea2 = null;
                for (int i4 = i; i4 < maxWindowLayer; i4++) {
                    if (feature.mWindowLayers[i4]) {
                        if (pendingArea2 == null || pendingArea2.mParent != pendingAreaArr[i4]) {
                            pendingArea2 = new PendingArea(feature, i4, pendingAreaArr[i4]);
                            pendingAreaArr[i4].mChildren.add(pendingArea2);
                        }
                        pendingAreaArr[i4] = pendingArea2;
                    } else {
                        pendingArea2 = null;
                    }
                }
                i3++;
                i = 0;
            }
            PendingArea pendingArea3 = null;
            int i5 = 0;
            for (int i6 = 0; i6 < maxWindowLayer; i6++) {
                int typeOfLayer = typeOfLayer(windowManagerPolicy, i6);
                if (pendingArea3 == null || pendingArea3.mParent != pendingAreaArr[i6] || typeOfLayer != i5) {
                    pendingArea3 = new PendingArea(null, i6, pendingAreaArr[i6]);
                    pendingAreaArr[i6].mChildren.add(pendingArea3);
                    if (typeOfLayer == 1) {
                        addTaskDisplayAreasToApplicationLayer(pendingAreaArr[i6]);
                        addDisplayAreaGroupsToApplicationLayer(pendingAreaArr[i6], list);
                        pendingArea3.mSkipTokens = true;
                    } else if (typeOfLayer == 2) {
                        pendingArea3.mExisting = this.mImeContainer;
                        pendingArea3.mSkipTokens = true;
                    }
                    i5 = typeOfLayer;
                }
                pendingArea3.mMaxLayer = i6;
            }
            pendingArea.computeMaxLayer();
            pendingArea.instantiateChildren(this.mRoot, tokensArr, 0, arrayMap);
            this.mRoot.onHierarchyBuilt(this.mFeatures, tokensArr, arrayMap);
        }

        private void addTaskDisplayAreasToApplicationLayer(PendingArea pendingArea) {
            int size = this.mTaskDisplayAreas.size();
            for (int i = 0; i < size; i++) {
                PendingArea pendingArea2 = new PendingArea(null, 2, pendingArea);
                pendingArea2.mExisting = this.mTaskDisplayAreas.get(i);
                pendingArea2.mMaxLayer = 2;
                pendingArea.mChildren.add(pendingArea2);
            }
        }

        private void addDisplayAreaGroupsToApplicationLayer(PendingArea pendingArea, List<HierarchyBuilder> list) {
            if (list == null) {
                return;
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                PendingArea pendingArea2 = new PendingArea(null, 2, pendingArea);
                pendingArea2.mExisting = list.get(i).mRoot;
                pendingArea2.mMaxLayer = 2;
                pendingArea.mChildren.add(pendingArea2);
            }
        }

        private static int typeOfLayer(WindowManagerPolicy windowManagerPolicy, int i) {
            if (i == 2) {
                return 1;
            }
            return (i == windowManagerPolicy.getWindowLayerFromTypeLw(2011) || i == windowManagerPolicy.getWindowLayerFromTypeLw(2012)) ? 2 : 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Feature {
        private final int mId;
        private final String mName;
        private final NewDisplayAreaSupplier mNewDisplayAreaSupplier;
        private final boolean[] mWindowLayers;

        private Feature(String str, int i, boolean[] zArr, NewDisplayAreaSupplier newDisplayAreaSupplier) {
            this.mName = str;
            this.mId = i;
            this.mWindowLayers = zArr;
            this.mNewDisplayAreaSupplier = newDisplayAreaSupplier;
        }

        public int getId() {
            return this.mId;
        }

        public String toString() {
            return "Feature(\"" + this.mName + "\", " + this.mId + '}';
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public static class Builder {
            private final int mId;
            private final boolean[] mLayers;
            private final String mName;
            private final WindowManagerPolicy mPolicy;
            private NewDisplayAreaSupplier mNewDisplayAreaSupplier = new NewDisplayAreaSupplier() { // from class: com.android.server.wm.DisplayAreaPolicyBuilder$Feature$Builder$$ExternalSyntheticLambda0
                @Override // com.android.server.wm.DisplayAreaPolicyBuilder.NewDisplayAreaSupplier
                public final DisplayArea create(WindowManagerService windowManagerService, DisplayArea.Type type, String str, int i) {
                    return new DisplayArea(windowManagerService, type, str, i);
                }
            };
            private boolean mExcludeRoundedCorner = true;

            /* JADX INFO: Access modifiers changed from: package-private */
            public Builder(WindowManagerPolicy windowManagerPolicy, String str, int i) {
                this.mPolicy = windowManagerPolicy;
                this.mName = str;
                this.mId = i;
                this.mLayers = new boolean[windowManagerPolicy.getMaxWindowLayer() + 1];
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public Builder all() {
                Arrays.fill(this.mLayers, true);
                return this;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public Builder and(int... iArr) {
                for (int i : iArr) {
                    set(i, true);
                }
                return this;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public Builder except(int... iArr) {
                for (int i : iArr) {
                    set(i, false);
                }
                return this;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public Builder upTo(int i) {
                int layerFromType = layerFromType(i, false);
                for (int i2 = 0; i2 < layerFromType; i2++) {
                    this.mLayers[i2] = true;
                }
                set(i, true);
                return this;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public Builder setNewDisplayAreaSupplier(NewDisplayAreaSupplier newDisplayAreaSupplier) {
                this.mNewDisplayAreaSupplier = newDisplayAreaSupplier;
                return this;
            }

            Builder setExcludeRoundedCornerOverlay(boolean z) {
                this.mExcludeRoundedCorner = z;
                return this;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public Feature build() {
                if (this.mExcludeRoundedCorner) {
                    this.mLayers[this.mPolicy.getMaxWindowLayer()] = false;
                }
                return new Feature(this.mName, this.mId, (boolean[]) this.mLayers.clone(), this.mNewDisplayAreaSupplier);
            }

            private void set(int i, boolean z) {
                this.mLayers[layerFromType(i, true)] = z;
                if (i == 2038) {
                    this.mLayers[layerFromType(i, true)] = z;
                    this.mLayers[layerFromType(2003, false)] = z;
                    this.mLayers[layerFromType(2006, false)] = z;
                    this.mLayers[layerFromType(2010, false)] = z;
                }
            }

            private int layerFromType(int i, boolean z) {
                return this.mPolicy.getWindowLayerFromTypeLw(i, z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Result extends DisplayAreaPolicy {
        private final TaskDisplayArea mDefaultTaskDisplayArea;
        final List<RootDisplayArea> mDisplayAreaGroupRoots;
        final BiFunction<Integer, Bundle, RootDisplayArea> mSelectRootForWindowFunc;
        private final Function<Bundle, TaskDisplayArea> mSelectTaskDisplayAreaFunc;

        Result(WindowManagerService windowManagerService, RootDisplayArea rootDisplayArea, List<RootDisplayArea> list, BiFunction<Integer, Bundle, RootDisplayArea> biFunction, Function<Bundle, TaskDisplayArea> function) {
            super(windowManagerService, rootDisplayArea);
            this.mDisplayAreaGroupRoots = Collections.unmodifiableList(list);
            this.mSelectRootForWindowFunc = biFunction;
            TaskDisplayArea taskDisplayArea = (TaskDisplayArea) this.mRoot.getItemFromTaskDisplayAreas(new Function() { // from class: com.android.server.wm.DisplayAreaPolicyBuilder$Result$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    TaskDisplayArea lambda$new$0;
                    lambda$new$0 = DisplayAreaPolicyBuilder.Result.lambda$new$0((TaskDisplayArea) obj);
                    return lambda$new$0;
                }
            });
            this.mDefaultTaskDisplayArea = taskDisplayArea;
            if (taskDisplayArea == null) {
                throw new IllegalStateException("No display area with FEATURE_DEFAULT_TASK_CONTAINER");
            }
            this.mSelectTaskDisplayAreaFunc = function == null ? new DefaultSelectTaskDisplayAreaFunction(taskDisplayArea) : function;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ TaskDisplayArea lambda$new$0(TaskDisplayArea taskDisplayArea) {
            if (taskDisplayArea.mFeatureId == 1) {
                return taskDisplayArea;
            }
            return null;
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public void addWindow(WindowToken windowToken) {
            findAreaForToken(windowToken).addChild(windowToken);
        }

        @VisibleForTesting
        DisplayArea.Tokens findAreaForToken(WindowToken windowToken) {
            return this.mSelectRootForWindowFunc.apply(Integer.valueOf(windowToken.windowType), windowToken.mOptions).findAreaForTokenInLayer(windowToken);
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public DisplayArea.Tokens findAreaForWindowType(int i, Bundle bundle, boolean z, boolean z2) {
            return this.mSelectRootForWindowFunc.apply(Integer.valueOf(i), bundle).findAreaForWindowTypeInLayer(i, z, z2);
        }

        @VisibleForTesting
        List<Feature> getFeatures() {
            ArraySet arraySet = new ArraySet();
            arraySet.addAll(this.mRoot.mFeatures);
            for (int i = 0; i < this.mDisplayAreaGroupRoots.size(); i++) {
                arraySet.addAll(this.mDisplayAreaGroupRoots.get(i).mFeatures);
            }
            return new ArrayList(arraySet);
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public List<DisplayArea<? extends WindowContainer>> getDisplayAreas(int i) {
            ArrayList arrayList = new ArrayList();
            getDisplayAreas(this.mRoot, i, arrayList);
            for (int i2 = 0; i2 < this.mDisplayAreaGroupRoots.size(); i2++) {
                getDisplayAreas(this.mDisplayAreaGroupRoots.get(i2), i, arrayList);
            }
            return arrayList;
        }

        private static void getDisplayAreas(RootDisplayArea rootDisplayArea, int i, List<DisplayArea<? extends WindowContainer>> list) {
            List<Feature> list2 = rootDisplayArea.mFeatures;
            for (int i2 = 0; i2 < list2.size(); i2++) {
                Feature feature = list2.get(i2);
                if (feature.mId == i) {
                    list.addAll(rootDisplayArea.mFeatureToDisplayAreas.get(feature));
                }
            }
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public TaskDisplayArea getDefaultTaskDisplayArea() {
            return this.mDefaultTaskDisplayArea;
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public TaskDisplayArea getTaskDisplayArea(Bundle bundle) {
            return this.mSelectTaskDisplayAreaFunc.apply(bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class PendingArea {
        DisplayArea mExisting;
        final Feature mFeature;
        int mMaxLayer;
        final int mMinLayer;
        final PendingArea mParent;
        final ArrayList<PendingArea> mChildren = new ArrayList<>();
        boolean mSkipTokens = false;

        PendingArea(Feature feature, int i, PendingArea pendingArea) {
            this.mMinLayer = i;
            this.mFeature = feature;
            this.mParent = pendingArea;
        }

        int computeMaxLayer() {
            for (int i = 0; i < this.mChildren.size(); i++) {
                this.mMaxLayer = Math.max(this.mMaxLayer, this.mChildren.get(i).computeMaxLayer());
            }
            return this.mMaxLayer;
        }

        void instantiateChildren(DisplayArea<DisplayArea> displayArea, DisplayArea.Tokens[] tokensArr, int i, Map<Feature, List<DisplayArea<WindowContainer>>> map) {
            this.mChildren.sort(Comparator.comparingInt(new ToIntFunction() { // from class: com.android.server.wm.DisplayAreaPolicyBuilder$PendingArea$$ExternalSyntheticLambda0
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    int i2;
                    i2 = ((DisplayAreaPolicyBuilder.PendingArea) obj).mMinLayer;
                    return i2;
                }
            }));
            for (int i2 = 0; i2 < this.mChildren.size(); i2++) {
                PendingArea pendingArea = this.mChildren.get(i2);
                DisplayArea createArea = pendingArea.createArea(displayArea, tokensArr);
                if (createArea != null) {
                    displayArea.addChild((DisplayArea<DisplayArea>) createArea, Integer.MAX_VALUE);
                    Feature feature = pendingArea.mFeature;
                    if (feature != null) {
                        map.get(feature).add(createArea);
                    }
                    pendingArea.instantiateChildren(createArea, tokensArr, i + 1, map);
                }
            }
        }

        private DisplayArea createArea(DisplayArea<DisplayArea> displayArea, DisplayArea.Tokens[] tokensArr) {
            DisplayArea.Type type;
            DisplayArea displayArea2 = this.mExisting;
            if (displayArea2 != null) {
                if (displayArea2.asTokens() != null) {
                    fillAreaForLayers(this.mExisting.asTokens(), tokensArr);
                }
                return this.mExisting;
            }
            if (this.mSkipTokens) {
                return null;
            }
            if (this.mMinLayer > 2) {
                type = DisplayArea.Type.ABOVE_TASKS;
            } else if (this.mMaxLayer < 2) {
                type = DisplayArea.Type.BELOW_TASKS;
            } else {
                type = DisplayArea.Type.ANY;
            }
            Feature feature = this.mFeature;
            if (feature == null) {
                DisplayArea.Tokens tokens = new DisplayArea.Tokens(displayArea.mWmService, type, "Leaf:" + this.mMinLayer + ":" + this.mMaxLayer);
                fillAreaForLayers(tokens, tokensArr);
                return tokens;
            }
            return feature.mNewDisplayAreaSupplier.create(displayArea.mWmService, type, this.mFeature.mName + ":" + this.mMinLayer + ":" + this.mMaxLayer, this.mFeature.mId);
        }

        private void fillAreaForLayers(DisplayArea.Tokens tokens, DisplayArea.Tokens[] tokensArr) {
            for (int i = this.mMinLayer; i <= this.mMaxLayer; i++) {
                tokensArr[i] = tokens;
            }
        }
    }
}
