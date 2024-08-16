package com.android.server.wm;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import com.android.internal.annotations.Keep;

@Keep
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
class DisplayAreaGroup extends RootDisplayArea {
    DisplayAreaGroup(WindowManagerService windowManagerService, String str, int i) {
        super(windowManagerService, str, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.RootDisplayArea
    public boolean isOrientationDifferentFromDisplay() {
        return isOrientationDifferentFromDisplay(getBounds());
    }

    private boolean isOrientationDifferentFromDisplay(Rect rect) {
        DisplayContent displayContent = this.mDisplayContent;
        if (displayContent == null) {
            return false;
        }
        Rect bounds = displayContent.getBounds();
        return (rect.width() < rect.height()) != (bounds.width() < bounds.height());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    public int getOrientation(int i) {
        int orientation = super.getOrientation(i);
        return isOrientationDifferentFromDisplay() ? ActivityInfo.reverseOrientation(orientation) : orientation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.ConfigurationContainer
    public void resolveOverrideConfiguration(Configuration configuration) {
        super.resolveOverrideConfiguration(configuration);
        Configuration resolvedOverrideConfiguration = getResolvedOverrideConfiguration();
        if (resolvedOverrideConfiguration.orientation != 0) {
            return;
        }
        Rect bounds = resolvedOverrideConfiguration.windowConfiguration.getBounds();
        if (bounds.isEmpty()) {
            bounds = configuration.windowConfiguration.getBounds();
        }
        if (isOrientationDifferentFromDisplay(bounds)) {
            int i = configuration.orientation;
            if (i == 1) {
                resolvedOverrideConfiguration.orientation = 2;
            } else if (i == 2) {
                resolvedOverrideConfiguration.orientation = 1;
            }
        }
    }
}
