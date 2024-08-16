package com.android.server.selectiontoolbar;

import android.service.selectiontoolbar.DefaultSelectionToolbarRenderService;
import com.android.server.infra.ServiceNameResolver;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class SelectionToolbarServiceNameResolver implements ServiceNameResolver {
    private static final String SELECTION_TOOLBAR_SERVICE_NAME = "android/" + DefaultSelectionToolbarRenderService.class.getName();

    @Override // com.android.server.infra.ServiceNameResolver
    public String getDefaultServiceName(int i) {
        return SELECTION_TOOLBAR_SERVICE_NAME;
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void dumpShort(PrintWriter printWriter) {
        printWriter.print("service=");
        printWriter.print(SELECTION_TOOLBAR_SERVICE_NAME);
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void dumpShort(PrintWriter printWriter, int i) {
        printWriter.print("defaultService=");
        printWriter.print(getDefaultServiceName(i));
    }
}
