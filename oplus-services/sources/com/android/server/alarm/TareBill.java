package com.android.server.alarm;

import com.android.server.tare.EconomyManagerInternal;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class TareBill {
    static final EconomyManagerInternal.ActionBill ALARM_CLOCK = new EconomyManagerInternal.ActionBill(List.of(new EconomyManagerInternal.AnticipatedAction(1342177288, 1, 0)));
    static final EconomyManagerInternal.ActionBill NONWAKEUP_INEXACT_ALARM = new EconomyManagerInternal.ActionBill(List.of(new EconomyManagerInternal.AnticipatedAction(1342177287, 1, 0)));
    static final EconomyManagerInternal.ActionBill NONWAKEUP_INEXACT_ALLOW_WHILE_IDLE_ALARM = new EconomyManagerInternal.ActionBill(List.of(new EconomyManagerInternal.AnticipatedAction(1342177286, 1, 0)));
    static final EconomyManagerInternal.ActionBill NONWAKEUP_EXACT_ALARM = new EconomyManagerInternal.ActionBill(List.of(new EconomyManagerInternal.AnticipatedAction(1342177285, 1, 0)));
    static final EconomyManagerInternal.ActionBill NONWAKEUP_EXACT_ALLOW_WHILE_IDLE_ALARM = new EconomyManagerInternal.ActionBill(List.of(new EconomyManagerInternal.AnticipatedAction(1342177284, 1, 0)));
    static final EconomyManagerInternal.ActionBill WAKEUP_INEXACT_ALARM = new EconomyManagerInternal.ActionBill(List.of(new EconomyManagerInternal.AnticipatedAction(1342177283, 1, 0)));
    static final EconomyManagerInternal.ActionBill WAKEUP_INEXACT_ALLOW_WHILE_IDLE_ALARM = new EconomyManagerInternal.ActionBill(List.of(new EconomyManagerInternal.AnticipatedAction(1342177282, 1, 0)));
    static final EconomyManagerInternal.ActionBill WAKEUP_EXACT_ALARM = new EconomyManagerInternal.ActionBill(List.of(new EconomyManagerInternal.AnticipatedAction(1342177281, 1, 0)));
    static final EconomyManagerInternal.ActionBill WAKEUP_EXACT_ALLOW_WHILE_IDLE_ALARM = new EconomyManagerInternal.ActionBill(List.of(new EconomyManagerInternal.AnticipatedAction(1342177280, 1, 0)));

    /* JADX INFO: Access modifiers changed from: package-private */
    public static EconomyManagerInternal.ActionBill getAppropriateBill(Alarm alarm) {
        if (alarm.alarmClock != null) {
            return ALARM_CLOCK;
        }
        boolean z = (alarm.flags & 12) != 0;
        boolean z2 = alarm.windowLength == 0;
        if (alarm.wakeup) {
            if (z2) {
                if (z) {
                    return WAKEUP_EXACT_ALLOW_WHILE_IDLE_ALARM;
                }
                return WAKEUP_EXACT_ALARM;
            }
            if (z) {
                return WAKEUP_INEXACT_ALLOW_WHILE_IDLE_ALARM;
            }
            return WAKEUP_INEXACT_ALARM;
        }
        if (z2) {
            if (z) {
                return NONWAKEUP_EXACT_ALLOW_WHILE_IDLE_ALARM;
            }
            return NONWAKEUP_EXACT_ALARM;
        }
        if (z) {
            return NONWAKEUP_INEXACT_ALLOW_WHILE_IDLE_ALARM;
        }
        return NONWAKEUP_INEXACT_ALARM;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getName(EconomyManagerInternal.ActionBill actionBill) {
        if (actionBill.equals(ALARM_CLOCK)) {
            return "ALARM_CLOCK_BILL";
        }
        if (actionBill.equals(NONWAKEUP_INEXACT_ALARM)) {
            return "NONWAKEUP_INEXACT_ALARM_BILL";
        }
        if (actionBill.equals(NONWAKEUP_INEXACT_ALLOW_WHILE_IDLE_ALARM)) {
            return "NONWAKEUP_INEXACT_ALLOW_WHILE_IDLE_ALARM_BILL";
        }
        if (actionBill.equals(NONWAKEUP_EXACT_ALARM)) {
            return "NONWAKEUP_EXACT_ALARM_BILL";
        }
        if (actionBill.equals(NONWAKEUP_EXACT_ALLOW_WHILE_IDLE_ALARM)) {
            return "NONWAKEUP_EXACT_ALLOW_WHILE_IDLE_ALARM_BILL";
        }
        if (actionBill.equals(WAKEUP_INEXACT_ALARM)) {
            return "WAKEUP_INEXACT_ALARM_BILL";
        }
        if (actionBill.equals(WAKEUP_INEXACT_ALLOW_WHILE_IDLE_ALARM)) {
            return "WAKEUP_INEXACT_ALLOW_WHILE_IDLE_ALARM_BILL";
        }
        if (actionBill.equals(WAKEUP_EXACT_ALARM)) {
            return "WAKEUP_EXACT_ALARM_BILL";
        }
        if (actionBill.equals(WAKEUP_EXACT_ALLOW_WHILE_IDLE_ALARM)) {
            return "WAKEUP_EXACT_ALLOW_WHILE_IDLE_ALARM_BILL";
        }
        return "UNKNOWN_BILL (" + actionBill.toString() + ")";
    }

    private TareBill() {
    }
}
