package com.oplus.wrapper.content.res;

import com.oplus.wrapper.app.WindowConfiguration;

/* loaded from: classes.dex */
public class Configuration {
    private final android.content.res.Configuration mConfiguration;

    public Configuration(android.content.res.Configuration configuration) {
        this.mConfiguration = configuration;
    }

    public WindowConfiguration getWindowConfiguration() {
        return new WindowConfiguration(this.mConfiguration.windowConfiguration);
    }

    public boolean getUserSetLocale() {
        return this.mConfiguration.userSetLocale;
    }

    public void setUserSetLocale(boolean userSetLocale) {
        this.mConfiguration.userSetLocale = userSetLocale;
    }
}
