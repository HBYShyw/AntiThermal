package com.oplus.sceneservice.sdk.dataprovider.bean;

import androidx.annotation.Keep;

@Keep
/* loaded from: classes2.dex */
public class SceneStatusInfo {
    public String mBusinessId;
    public String mExtraData;
    public String mSceneEndTime;
    public int mSceneId;
    public String mSceneName;
    public String mSceneStartTime;
    public int mSceneStatus;

    @Keep
    /* loaded from: classes2.dex */
    public static final class SceneConstant {
        public static final int HOTEL_IN = 2000;
        public static final int HOTEL_PLAN = 1000;
        public static final int SCENE_FLIGHT_ID = 110;
        public static final int SCENE_HOTEL_ID = 113;
        public static final int SCENE_ID_COMPANY = 102;
        public static final int SCENE_ID_DRIVING = 104;
        public static final int SCENE_ID_HOME = 101;
        public static final int SCENE_ID_MEETING = 105;
        public static final int SCENE_ID_READING = 103;
        public static final int SCENE_ID_SLEEPING = 106;
        public static final int SCENE_MOVIE_ID = 112;
        public static final String SCENE_NAME_COMPANY = "company";
        public static final String SCENE_NAME_DRIVING = "driving";
        public static final String SCENE_NAME_FLIGHT = "flight";
        public static final String SCENE_NAME_HOME = "home";
        public static final String SCENE_NAME_HOTEL = "hotel";
        public static final String SCENE_NAME_MEETING = "meeting";
        public static final String SCENE_NAME_READING = "reading";
        public static final String SCENE_NAME_SLEEPING = "sleeping";
        public static final String SCENE_NAME_TRAIN = "train";
        public static final int SCENE_STATUS_ACCURATE_ENTER = 2;
        public static final int SCENE_STATUS_ENTER = 1;
        public static final int SCENE_STATUS_EXIT = 0;
        public static final int SCENE_STATUS_OTHER = -1;
        public static final int SCENE_TRAIN_ID = 111;
        public static final int TRIP_ARRIVE_END_STATION_IN_TIME = 7000;
        public static final int TRIP_ARRIVE_END_STATION_IN_TIME_AND_LOCATION = 8000;
        public static final int TRIP_ARRIVE_START_STATION = 4000;
        public static final int TRIP_BOARDING = 5000;
        public static final int TRIP_END = 9000;
        public static final int TRIP_GO_TO_STATION = 3000;
        public static final int TRIP_IN_JOURNEY = 6000;
        public static final int TRIP_PLAN = 1000;
        public static final int TRIP_PREPARATION = 2000;
    }

    public String toString() {
        return "SceneStatusInfo{mSceneId=" + this.mSceneId + ", mSceneStatus=" + this.mSceneStatus + ", mSceneName='" + this.mSceneName + "', mSceneEndTime='" + this.mSceneEndTime + "', mSceneStartTime='" + this.mSceneStartTime + "', mBusinessId='" + this.mBusinessId + "', mExtraData='" + this.mExtraData + "'}";
    }
}
