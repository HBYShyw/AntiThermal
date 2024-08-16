package com.android.server.wm;

import android.R;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.server.wm.DisplayArea;
import com.android.server.wm.DisplayAreaPolicyBuilder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public abstract class DisplayAreaPolicy {
    protected final RootDisplayArea mRoot;
    protected final WindowManagerService mWmService;

    public abstract void addWindow(WindowToken windowToken);

    public abstract DisplayArea.Tokens findAreaForWindowType(int i, Bundle bundle, boolean z, boolean z2);

    public abstract TaskDisplayArea getDefaultTaskDisplayArea();

    public abstract List<DisplayArea<? extends WindowContainer>> getDisplayAreas(int i);

    public abstract TaskDisplayArea getTaskDisplayArea(Bundle bundle);

    /* JADX INFO: Access modifiers changed from: protected */
    public DisplayAreaPolicy(WindowManagerService windowManagerService, RootDisplayArea rootDisplayArea) {
        this.mWmService = windowManagerService;
        this.mRoot = rootDisplayArea;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class DefaultProvider implements Provider {
        DefaultProvider() {
        }

        @Override // com.android.server.wm.DisplayAreaPolicy.Provider
        public DisplayAreaPolicy instantiate(WindowManagerService windowManagerService, DisplayContent displayContent, RootDisplayArea rootDisplayArea, DisplayArea.Tokens tokens) {
            TaskDisplayArea taskDisplayArea = new TaskDisplayArea(displayContent, windowManagerService, "DefaultTaskDisplayArea", 1);
            ArrayList arrayList = new ArrayList();
            arrayList.add(taskDisplayArea);
            DisplayAreaPolicyBuilder.HierarchyBuilder hierarchyBuilder = new DisplayAreaPolicyBuilder.HierarchyBuilder(rootDisplayArea);
            hierarchyBuilder.setImeContainer(tokens).setTaskDisplayAreas(arrayList);
            if (displayContent.isTrusted()) {
                configureTrustedHierarchyBuilder(hierarchyBuilder, windowManagerService, displayContent);
            }
            return new DisplayAreaPolicyBuilder().setRootHierarchy(hierarchyBuilder).build(windowManagerService);
        }

        private void configureTrustedHierarchyBuilder(DisplayAreaPolicyBuilder.HierarchyBuilder hierarchyBuilder, WindowManagerService windowManagerService, DisplayContent displayContent) {
            hierarchyBuilder.addFeature(new DisplayAreaPolicyBuilder.Feature.Builder(windowManagerService.mPolicy, "WindowedMagnification", 4).upTo(2039).except(2039).setNewDisplayAreaSupplier(new DisplayAreaPolicyBuilder.NewDisplayAreaSupplier() { // from class: com.android.server.wm.DisplayAreaPolicy$DefaultProvider$$ExternalSyntheticLambda0
                @Override // com.android.server.wm.DisplayAreaPolicyBuilder.NewDisplayAreaSupplier
                public final DisplayArea create(WindowManagerService windowManagerService2, DisplayArea.Type type, String str, int i) {
                    return new DisplayArea.Dimmable(windowManagerService2, type, str, i);
                }
            }).build());
            if (displayContent.isDefaultDisplay) {
                hierarchyBuilder.addFeature(new DisplayAreaPolicyBuilder.Feature.Builder(windowManagerService.mPolicy, "HideDisplayCutout", 6).all().except(2019, 2024, 2000, 2040).build()).addFeature(new DisplayAreaPolicyBuilder.Feature.Builder(windowManagerService.mPolicy, "OneHanded", 3).all().except(2019, 2024, 2015).build());
            }
            hierarchyBuilder.addFeature(new DisplayAreaPolicyBuilder.Feature.Builder(windowManagerService.mPolicy, "FullscreenMagnification", 5).all().except(2039, 2011, 2012, 2027, 2019, 2024).build()).addFeature(new DisplayAreaPolicyBuilder.Feature.Builder(windowManagerService.mPolicy, "ImePlaceholder", 7).and(2011, 2012).build());
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface Provider {
        DisplayAreaPolicy instantiate(WindowManagerService windowManagerService, DisplayContent displayContent, RootDisplayArea rootDisplayArea, DisplayArea.Tokens tokens);

        static Provider fromResources(Resources resources) {
            String string = resources.getString(R.string.config_usbContaminantActivity);
            if (TextUtils.isEmpty(string)) {
                return new DefaultProvider();
            }
            try {
                return (Provider) Class.forName(string).newInstance();
            } catch (ClassCastException | ReflectiveOperationException e) {
                throw new IllegalStateException("Couldn't instantiate class " + string + " for config_deviceSpecificDisplayAreaPolicyProvider: make sure it has a public zero-argument constructor and implements DisplayAreaPolicy.Provider", e);
            }
        }
    }
}
