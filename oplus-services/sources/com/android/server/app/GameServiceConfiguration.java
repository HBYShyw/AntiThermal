package com.android.server.app;

import android.content.ComponentName;
import android.os.UserHandle;
import android.text.TextUtils;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class GameServiceConfiguration {
    private final GameServiceComponentConfiguration mGameServiceComponentConfiguration;
    private final String mPackageName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GameServiceConfiguration(String str, GameServiceComponentConfiguration gameServiceComponentConfiguration) {
        Objects.requireNonNull(str);
        this.mPackageName = str;
        this.mGameServiceComponentConfiguration = gameServiceComponentConfiguration;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public GameServiceComponentConfiguration getGameServiceComponentConfiguration() {
        return this.mGameServiceComponentConfiguration;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GameServiceConfiguration)) {
            return false;
        }
        GameServiceConfiguration gameServiceConfiguration = (GameServiceConfiguration) obj;
        return TextUtils.equals(this.mPackageName, gameServiceConfiguration.mPackageName) && Objects.equals(this.mGameServiceComponentConfiguration, gameServiceConfiguration.mGameServiceComponentConfiguration);
    }

    public int hashCode() {
        return Objects.hash(this.mPackageName, this.mGameServiceComponentConfiguration);
    }

    public String toString() {
        return "GameServiceConfiguration{packageName=" + this.mPackageName + ", gameServiceComponentConfiguration=" + this.mGameServiceComponentConfiguration + '}';
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class GameServiceComponentConfiguration {
        private final ComponentName mGameServiceComponentName;
        private final ComponentName mGameSessionServiceComponentName;
        private final UserHandle mUserHandle;

        /* JADX INFO: Access modifiers changed from: package-private */
        public GameServiceComponentConfiguration(UserHandle userHandle, ComponentName componentName, ComponentName componentName2) {
            Objects.requireNonNull(userHandle);
            Objects.requireNonNull(componentName);
            Objects.requireNonNull(componentName2);
            this.mUserHandle = userHandle;
            this.mGameServiceComponentName = componentName;
            this.mGameSessionServiceComponentName = componentName2;
        }

        public UserHandle getUserHandle() {
            return this.mUserHandle;
        }

        public ComponentName getGameServiceComponentName() {
            return this.mGameServiceComponentName;
        }

        public ComponentName getGameSessionServiceComponentName() {
            return this.mGameSessionServiceComponentName;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof GameServiceComponentConfiguration)) {
                return false;
            }
            GameServiceComponentConfiguration gameServiceComponentConfiguration = (GameServiceComponentConfiguration) obj;
            return this.mUserHandle.equals(gameServiceComponentConfiguration.mUserHandle) && this.mGameServiceComponentName.equals(gameServiceComponentConfiguration.mGameServiceComponentName) && this.mGameSessionServiceComponentName.equals(gameServiceComponentConfiguration.mGameSessionServiceComponentName);
        }

        public int hashCode() {
            return Objects.hash(this.mUserHandle, this.mGameServiceComponentName, this.mGameSessionServiceComponentName);
        }

        public String toString() {
            return "GameServiceComponentConfiguration{userHandle=" + this.mUserHandle + ", gameServiceComponentName=" + this.mGameServiceComponentName + ", gameSessionServiceComponentName=" + this.mGameSessionServiceComponentName + "}";
        }
    }
}
