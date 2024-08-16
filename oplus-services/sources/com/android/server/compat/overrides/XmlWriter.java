package com.android.server.compat.overrides;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class XmlWriter implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private PrintWriter out;
    private StringBuilder outBuffer = new StringBuilder();
    private int indent = 0;
    private boolean startLine = true;

    public XmlWriter(PrintWriter printWriter) {
        this.out = printWriter;
    }

    private void printIndent() {
        for (int i = 0; i < this.indent; i++) {
            this.outBuffer.append("    ");
        }
        this.startLine = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void print(String str) {
        String[] split = str.split("\n", -1);
        int i = 0;
        while (i < split.length) {
            if (this.startLine && !split[i].isEmpty()) {
                printIndent();
            }
            this.outBuffer.append(split[i]);
            i++;
            if (i < split.length) {
                this.outBuffer.append("\n");
                this.startLine = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void increaseIndent() {
        this.indent++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void decreaseIndent() {
        this.indent--;
    }

    void printXml() {
        this.out.print(this.outBuffer.toString());
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        PrintWriter printWriter = this.out;
        if (printWriter != null) {
            printWriter.close();
        }
    }

    public static void write(XmlWriter xmlWriter, Overrides overrides) throws IOException {
        xmlWriter.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        if (overrides != null) {
            overrides.write(xmlWriter, "overrides");
        }
        xmlWriter.printXml();
    }
}
