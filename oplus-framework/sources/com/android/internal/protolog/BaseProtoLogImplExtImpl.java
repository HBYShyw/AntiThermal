package com.android.internal.protolog;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class BaseProtoLogImplExtImpl implements IBaseProtoLogImplExt {
    private final BaseProtoLogImpl mBase;

    public BaseProtoLogImplExtImpl(Object base) {
        this.mBase = (BaseProtoLogImpl) base;
    }

    public int setLogging(boolean setTextLogging, boolean value, PrintWriter pw, String... groups) {
        List<String> groupsList = new ArrayList<>(Arrays.asList(groups));
        updateGroupList(groupsList);
        return this.mBase.getWrapper().setLogging(setTextLogging, value, pw, (String[]) groupsList.toArray(new String[0]));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x002a, code lost:
    
        if (r1.equals("defaultlog") != false) goto L40;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateGroupList(List<String> groupsList) {
        if (groupsList == null || groupsList.size() == 0) {
            return;
        }
        char c = 0;
        String cmd = groupsList.get(0);
        switch (cmd.hashCode()) {
            case -1853231955:
                if (cmd.equals("surface")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1354792126:
                if (cmd.equals("config")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -907689876:
                if (cmd.equals("screen")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -787751952:
                if (cmd.equals("window")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 96673:
                if (cmd.equals("all")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 2998801:
                if (cmd.equals("anim")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 97696046:
                if (cmd.equals("fresh")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 110620997:
                if (cmd.equals("trace")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 678664547:
                break;
            case 1187081336:
                if (cmd.equals("apptoken")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                groupsList.clear();
                groupsList.add(ProtoLogGroup.WM_DEBUG_CONTAINERS.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_ADD_REMOVE.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_LOCKTASK.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS.name());
                return;
            case 1:
                groupsList.clear();
                groupsList.add(ProtoLogGroup.WM_DEBUG_CONTAINERS.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_ADD_REMOVE.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_FOCUS.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_LOCKTASK.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_TASKS.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS.name());
                return;
            case 2:
                groupsList.clear();
                groupsList.add(ProtoLogGroup.WM_DEBUG_RESIZE.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_STATES.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_IME.name());
                return;
            case 3:
                groupsList.clear();
                groupsList.add(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_DRAW.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE.name());
                return;
            case 4:
                groupsList.clear();
                groupsList.add(ProtoLogGroup.WM_DEBUG_SCREEN_ON.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_BOOT.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON.name());
                return;
            case 5:
                groupsList.clear();
                groupsList.add(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_SWITCH.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS.name());
                return;
            case 6:
                groupsList.clear();
                groupsList.add(ProtoLogGroup.WM_DEBUG_ORIENTATION.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_CONFIGURATION.name());
                return;
            case 7:
                groupsList.clear();
                groupsList.add(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE.name());
                return;
            case '\b':
                groupsList.clear();
                groupsList.add(ProtoLogGroup.WM_SHOW_TRANSACTIONS.name());
                groupsList.add(ProtoLogGroup.WM_SHOW_SURFACE_ALLOC.name());
                groupsList.add(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE.name());
                return;
            case '\t':
                groupsList.clear();
                Set<String> set = this.mBase.getWrapper().getLogGroups().keySet();
                groupsList.addAll(set);
                groupsList.remove(ProtoLogGroup.WM_ERROR.name());
                groupsList.remove(ProtoLogGroup.WM_DEBUG_BACK_PREVIEW.name());
                groupsList.remove(ProtoLogGroup.WM_DEBUG_DREAM.name());
                groupsList.remove(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN.name());
                return;
            default:
                return;
        }
    }
}
