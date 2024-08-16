package com.android.server.usb.descriptors.tree;

import com.android.server.usb.descriptors.UsbACInterface;
import com.android.server.usb.descriptors.UsbConfigDescriptor;
import com.android.server.usb.descriptors.UsbDescriptor;
import com.android.server.usb.descriptors.UsbDescriptorParser;
import com.android.server.usb.descriptors.UsbDeviceDescriptor;
import com.android.server.usb.descriptors.UsbEndpointDescriptor;
import com.android.server.usb.descriptors.UsbInterfaceDescriptor;
import com.android.server.usb.descriptors.report.ReportCanvas;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbDescriptorsTree {
    private static final String TAG = "UsbDescriptorsTree";
    private UsbDescriptorsConfigNode mConfigNode;
    private UsbDescriptorsDeviceNode mDeviceNode;
    private UsbDescriptorsInterfaceNode mInterfaceNode;

    private void addDeviceDescriptor(UsbDeviceDescriptor usbDeviceDescriptor) {
        this.mDeviceNode = new UsbDescriptorsDeviceNode(usbDeviceDescriptor);
    }

    private void addConfigDescriptor(UsbConfigDescriptor usbConfigDescriptor) {
        UsbDescriptorsConfigNode usbDescriptorsConfigNode = new UsbDescriptorsConfigNode(usbConfigDescriptor);
        this.mConfigNode = usbDescriptorsConfigNode;
        this.mDeviceNode.addConfigDescriptorNode(usbDescriptorsConfigNode);
    }

    private void addInterfaceDescriptor(UsbInterfaceDescriptor usbInterfaceDescriptor) {
        UsbDescriptorsInterfaceNode usbDescriptorsInterfaceNode = new UsbDescriptorsInterfaceNode(usbInterfaceDescriptor);
        this.mInterfaceNode = usbDescriptorsInterfaceNode;
        this.mConfigNode.addInterfaceNode(usbDescriptorsInterfaceNode);
    }

    private void addEndpointDescriptor(UsbEndpointDescriptor usbEndpointDescriptor) {
        this.mInterfaceNode.addEndpointNode(new UsbDescriptorsEndpointNode(usbEndpointDescriptor));
    }

    private void addACInterface(UsbACInterface usbACInterface) {
        this.mInterfaceNode.addACInterfaceNode(new UsbDescriptorsACInterfaceNode(usbACInterface));
    }

    public void parse(UsbDescriptorParser usbDescriptorParser) {
        ArrayList<UsbDescriptor> descriptors = usbDescriptorParser.getDescriptors();
        for (int i = 0; i < descriptors.size(); i++) {
            UsbDescriptor usbDescriptor = descriptors.get(i);
            byte type = usbDescriptor.getType();
            if (type == 1) {
                addDeviceDescriptor((UsbDeviceDescriptor) usbDescriptor);
            } else if (type == 2) {
                addConfigDescriptor((UsbConfigDescriptor) usbDescriptor);
            } else if (type == 4) {
                addInterfaceDescriptor((UsbInterfaceDescriptor) usbDescriptor);
            } else if (type == 5) {
                addEndpointDescriptor((UsbEndpointDescriptor) usbDescriptor);
            }
        }
    }

    public void report(ReportCanvas reportCanvas) {
        this.mDeviceNode.report(reportCanvas);
    }
}
