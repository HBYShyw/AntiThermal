package com.coui.responsiveui.config;

import java.util.Objects;

/* loaded from: classes.dex */
public class UIConfig {

    /* renamed from: a, reason: collision with root package name */
    private Status f8242a;

    /* renamed from: b, reason: collision with root package name */
    private int f8243b;

    /* renamed from: c, reason: collision with root package name */
    private UIScreenSize f8244c;

    /* renamed from: d, reason: collision with root package name */
    private WindowType f8245d;

    /* loaded from: classes.dex */
    public enum Status {
        FOLD("fd"),
        UNFOLDING("fding"),
        UNFOLD("ufd"),
        UNKNOWN("unknown");


        /* renamed from: e, reason: collision with root package name */
        private String f8247e;

        Status(String str) {
            this.f8247e = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.f8247e;
        }
    }

    /* loaded from: classes.dex */
    public enum WindowType {
        SMALL,
        MEDIUM,
        LARGE
    }

    public UIConfig(Status status, UIScreenSize uIScreenSize, int i10, WindowType windowType) {
        this.f8242a = status;
        this.f8244c = uIScreenSize;
        this.f8243b = i10;
        this.f8245d = windowType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(UIScreenSize uIScreenSize) {
        this.f8244c = uIScreenSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Status status) {
        this.f8242a = status;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(WindowType windowType) {
        this.f8245d = windowType;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UIConfig uIConfig = (UIConfig) obj;
        return this.f8243b == uIConfig.f8243b && this.f8242a == uIConfig.f8242a && Objects.equals(this.f8244c, uIConfig.f8244c);
    }

    public int getOrientation() {
        return this.f8243b;
    }

    public UIScreenSize getScreenSize() {
        return this.f8244c;
    }

    public Status getStatus() {
        return this.f8242a;
    }

    public WindowType getWindowType() {
        return this.f8245d;
    }

    public int hashCode() {
        return Objects.hash(this.f8242a, Integer.valueOf(this.f8243b), this.f8244c);
    }

    public String toString() {
        return "UIConfig{mStatus= " + this.f8242a + ", mOrientation=" + this.f8243b + ", mScreenSize=" + this.f8244c + ", mWindowType=" + this.f8245d + "}";
    }
}
