package com.android.server.usb.descriptors.report;

import com.android.server.usb.descriptors.UsbDescriptorParser;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HTMLReportCanvas extends ReportCanvas {
    private static final String TAG = "HTMLReportCanvas";
    private final StringBuilder mStringBuilder;

    public HTMLReportCanvas(UsbDescriptorParser usbDescriptorParser, StringBuilder sb) {
        super(usbDescriptorParser);
        this.mStringBuilder = sb;
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void write(String str) {
        this.mStringBuilder.append(str);
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openHeader(int i) {
        StringBuilder sb = this.mStringBuilder;
        sb.append("<h");
        sb.append(i);
        sb.append('>');
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeHeader(int i) {
        StringBuilder sb = this.mStringBuilder;
        sb.append("</h");
        sb.append(i);
        sb.append('>');
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openParagraph(boolean z) {
        if (z) {
            this.mStringBuilder.append("<p style=\"color:red\">");
        } else {
            this.mStringBuilder.append("<p>");
        }
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeParagraph() {
        this.mStringBuilder.append("</p>");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void writeParagraph(String str, boolean z) {
        openParagraph(z);
        this.mStringBuilder.append(str);
        closeParagraph();
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openList() {
        this.mStringBuilder.append("<ul>");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeList() {
        this.mStringBuilder.append("</ul>");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openListItem() {
        this.mStringBuilder.append("<li>");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeListItem() {
        this.mStringBuilder.append("</li>");
    }
}
