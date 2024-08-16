package com.coui.responsiveui.config;

import java.util.Objects;

/* loaded from: classes.dex */
public class UIScreenSize {

    /* renamed from: a, reason: collision with root package name */
    private int f8249a;

    /* renamed from: b, reason: collision with root package name */
    private int f8250b;

    /* renamed from: c, reason: collision with root package name */
    private int f8251c;

    public UIScreenSize(int i10, int i11) {
        this.f8249a = i10;
        this.f8250b = i11;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a() {
        return this.f8251c;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UIScreenSize uIScreenSize = (UIScreenSize) obj;
        return this.f8249a == uIScreenSize.f8249a && this.f8250b == uIScreenSize.f8250b;
    }

    public int getHeightDp() {
        return this.f8250b;
    }

    public int getWidthDp() {
        return this.f8249a;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.f8249a), Integer.valueOf(this.f8250b), Integer.valueOf(this.f8251c));
    }

    public void setHeightDp(int i10) {
        this.f8250b = i10;
    }

    public void setWidthDp(int i10) {
        this.f8249a = i10;
    }

    public String toString() {
        return "UIScreenSize{W-Dp=" + this.f8249a + ", H-Dp=" + this.f8250b + ", SW-Dp=" + this.f8251c + "}";
    }

    public UIScreenSize(int i10, int i11, int i12) {
        this.f8249a = i10;
        this.f8250b = i11;
        this.f8251c = i12;
    }
}
