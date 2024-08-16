package com.android.server.usb.descriptors.tree;

import com.android.server.usb.descriptors.UsbConfigDescriptor;
import com.android.server.usb.descriptors.report.ReportCanvas;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbDescriptorsConfigNode extends UsbDescriptorsTreeNode {
    private static final String TAG = "UsbDescriptorsConfigNode";
    private final UsbConfigDescriptor mConfigDescriptor;
    private final ArrayList<UsbDescriptorsInterfaceNode> mInterfaceNodes = new ArrayList<>();

    public UsbDescriptorsConfigNode(UsbConfigDescriptor usbConfigDescriptor) {
        this.mConfigDescriptor = usbConfigDescriptor;
    }

    public void addInterfaceNode(UsbDescriptorsInterfaceNode usbDescriptorsInterfaceNode) {
        this.mInterfaceNodes.add(usbDescriptorsInterfaceNode);
    }

    @Override // com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode, com.android.server.usb.descriptors.report.Reporting
    public void report(ReportCanvas reportCanvas) {
        this.mConfigDescriptor.report(reportCanvas);
        reportCanvas.openList();
        Iterator<UsbDescriptorsInterfaceNode> it = this.mInterfaceNodes.iterator();
        while (it.hasNext()) {
            it.next().report(reportCanvas);
        }
        reportCanvas.closeList();
    }
}
