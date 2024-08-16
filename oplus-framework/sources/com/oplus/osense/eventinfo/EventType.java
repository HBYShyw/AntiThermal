package com.oplus.osense.eventinfo;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class EventType {
    public static final ArrayList<Integer> ALLOWEVENTTYPELIST = new ArrayList<Integer>() { // from class: com.oplus.osense.eventinfo.EventType.1
        {
            add(101);
            add(102);
            add(103);
            add(104);
            add(105);
            add(106);
            add(107);
            add(108);
        }
    };
    public static final int EVENT_RES_PRESSURE_CPU = 103;
    public static final int EVENT_RES_PRESSURE_CPU_ALL = 105;
    public static final int EVENT_RES_PRESSURE_IO = 104;
    public static final int EVENT_RES_PRESSURE_MEM = 102;
    public static final int EVENT_RES_PRESSURE_THERMAL = 108;
    public static final int EVENT_RES_TERMINATE = 106;
    public static final int EVENT_RES_TERMINATE_OBSERVER = 107;
    public static final int EVENT_SCENE_OFFSET = 100;
    public static final int EVENT_SCENE_STATE = 101;

    /* loaded from: classes.dex */
    public class State {
        public static final int ENTER = 0;
        public static final int EXIT = 1;
        public static final int UPDATE = 2;

        public State() {
        }
    }
}
