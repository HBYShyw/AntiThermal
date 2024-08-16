package com.android.server.powerstats;

import android.hardware.power.stats.Channel;
import android.hardware.power.stats.EnergyConsumer;
import android.hardware.power.stats.EnergyConsumerAttribution;
import android.hardware.power.stats.EnergyConsumerResult;
import android.hardware.power.stats.EnergyMeasurement;
import android.hardware.power.stats.PowerEntity;
import android.hardware.power.stats.State;
import android.hardware.power.stats.StateResidency;
import android.hardware.power.stats.StateResidencyResult;
import android.util.Slog;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import android.util.proto.ProtoUtils;
import android.util.proto.WireTypeMismatchException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ProtoStreamUtils {
    private static final String TAG = "ProtoStreamUtils";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class PowerEntityUtils {
        PowerEntityUtils() {
        }

        public static byte[] getProtoBytes(PowerEntity[] powerEntityArr) {
            ProtoOutputStream protoOutputStream = new ProtoOutputStream();
            packProtoMessage(powerEntityArr, protoOutputStream);
            return protoOutputStream.getBytes();
        }

        public static void packProtoMessage(PowerEntity[] powerEntityArr, ProtoOutputStream protoOutputStream) {
            if (powerEntityArr == null) {
                return;
            }
            for (int i = 0; i < powerEntityArr.length; i++) {
                long start = protoOutputStream.start(2246267895809L);
                protoOutputStream.write(1120986464257L, powerEntityArr[i].id);
                protoOutputStream.write(1138166333442L, powerEntityArr[i].name);
                State[] stateArr = powerEntityArr[i].states;
                if (stateArr != null) {
                    int length = stateArr.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        State state = powerEntityArr[i].states[i2];
                        long start2 = protoOutputStream.start(2246267895811L);
                        protoOutputStream.write(1120986464257L, state.id);
                        protoOutputStream.write(1138166333442L, state.name);
                        protoOutputStream.end(start2);
                    }
                }
                protoOutputStream.end(start);
            }
        }

        public static void print(PowerEntity[] powerEntityArr) {
            if (powerEntityArr == null) {
                return;
            }
            for (int i = 0; i < powerEntityArr.length; i++) {
                Slog.d(ProtoStreamUtils.TAG, "powerEntityId: " + powerEntityArr[i].id + ", powerEntityName: " + powerEntityArr[i].name);
                if (powerEntityArr[i].states != null) {
                    for (int i2 = 0; i2 < powerEntityArr[i].states.length; i2++) {
                        Slog.d(ProtoStreamUtils.TAG, "  StateId: " + powerEntityArr[i].states[i2].id + ", StateName: " + powerEntityArr[i].states[i2].name);
                    }
                }
            }
        }

        public static void dumpsys(PowerEntity[] powerEntityArr, PrintWriter printWriter) {
            if (powerEntityArr == null) {
                return;
            }
            for (int i = 0; i < powerEntityArr.length; i++) {
                printWriter.println("PowerEntityId: " + powerEntityArr[i].id + ", PowerEntityName: " + powerEntityArr[i].name);
                if (powerEntityArr[i].states != null) {
                    for (int i2 = 0; i2 < powerEntityArr[i].states.length; i2++) {
                        printWriter.println("  StateId: " + powerEntityArr[i].states[i2].id + ", StateName: " + powerEntityArr[i].states[i2].name);
                    }
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class StateResidencyResultUtils {
        StateResidencyResultUtils() {
        }

        public static void adjustTimeSinceBootToEpoch(StateResidencyResult[] stateResidencyResultArr, long j) {
            if (stateResidencyResultArr == null) {
                return;
            }
            for (int i = 0; i < stateResidencyResultArr.length; i++) {
                int length = stateResidencyResultArr[i].stateResidencyData.length;
                for (int i2 = 0; i2 < length; i2++) {
                    stateResidencyResultArr[i].stateResidencyData[i2].lastEntryTimestampMs += j;
                }
            }
        }

        public static byte[] getProtoBytes(StateResidencyResult[] stateResidencyResultArr) {
            ProtoOutputStream protoOutputStream = new ProtoOutputStream();
            packProtoMessage(stateResidencyResultArr, protoOutputStream);
            return protoOutputStream.getBytes();
        }

        public static void packProtoMessage(StateResidencyResult[] stateResidencyResultArr, ProtoOutputStream protoOutputStream) {
            if (stateResidencyResultArr == null) {
                return;
            }
            for (int i = 0; i < stateResidencyResultArr.length; i++) {
                int length = stateResidencyResultArr[i].stateResidencyData.length;
                long j = 2246267895810L;
                long start = protoOutputStream.start(2246267895810L);
                long j2 = 1120986464257L;
                protoOutputStream.write(1120986464257L, stateResidencyResultArr[i].id);
                int i2 = 0;
                while (i2 < length) {
                    StateResidency stateResidency = stateResidencyResultArr[i].stateResidencyData[i2];
                    long start2 = protoOutputStream.start(j);
                    protoOutputStream.write(j2, stateResidency.id);
                    protoOutputStream.write(1112396529666L, stateResidency.totalTimeInStateMs);
                    protoOutputStream.write(1112396529667L, stateResidency.totalStateEntryCount);
                    protoOutputStream.write(1112396529668L, stateResidency.lastEntryTimestampMs);
                    protoOutputStream.end(start2);
                    i2++;
                    j = 2246267895810L;
                    j2 = 1120986464257L;
                }
                protoOutputStream.end(start);
            }
        }

        public static StateResidencyResult[] unpackProtoMessage(byte[] bArr) throws IOException {
            ProtoInputStream protoInputStream = new ProtoInputStream(new ByteArrayInputStream(bArr));
            ArrayList arrayList = new ArrayList();
            while (true) {
                try {
                    int nextField = protoInputStream.nextField();
                    new StateResidencyResult();
                    if (nextField == 2) {
                        long start = protoInputStream.start(2246267895810L);
                        arrayList.add(unpackStateResidencyResultProto(protoInputStream));
                        protoInputStream.end(start);
                    } else {
                        if (nextField == -1) {
                            return (StateResidencyResult[]) arrayList.toArray(new StateResidencyResult[arrayList.size()]);
                        }
                        Slog.e(ProtoStreamUtils.TAG, "Unhandled field in PowerStatsServiceResidencyProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                    }
                } catch (WireTypeMismatchException unused) {
                    Slog.e(ProtoStreamUtils.TAG, "Wire Type mismatch in PowerStatsServiceResidencyProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
            }
        }

        private static StateResidencyResult unpackStateResidencyResultProto(ProtoInputStream protoInputStream) throws IOException {
            int nextField;
            StateResidencyResult stateResidencyResult = new StateResidencyResult();
            ArrayList arrayList = new ArrayList();
            while (true) {
                try {
                    nextField = protoInputStream.nextField();
                } catch (WireTypeMismatchException unused) {
                    Slog.e(ProtoStreamUtils.TAG, "Wire Type mismatch in StateResidencyResultProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
                if (nextField == -1) {
                    stateResidencyResult.stateResidencyData = (StateResidency[]) arrayList.toArray(new StateResidency[arrayList.size()]);
                    return stateResidencyResult;
                }
                if (nextField == 1) {
                    stateResidencyResult.id = protoInputStream.readInt(1120986464257L);
                } else if (nextField == 2) {
                    long start = protoInputStream.start(2246267895810L);
                    arrayList.add(unpackStateResidencyProto(protoInputStream));
                    protoInputStream.end(start);
                } else {
                    Slog.e(ProtoStreamUtils.TAG, "Unhandled field in StateResidencyResultProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
            }
        }

        private static StateResidency unpackStateResidencyProto(ProtoInputStream protoInputStream) throws IOException {
            int nextField;
            StateResidency stateResidency = new StateResidency();
            while (true) {
                try {
                    nextField = protoInputStream.nextField();
                } catch (WireTypeMismatchException unused) {
                    Slog.e(ProtoStreamUtils.TAG, "Wire Type mismatch in StateResidencyProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
                if (nextField == -1) {
                    return stateResidency;
                }
                if (nextField == 1) {
                    stateResidency.id = protoInputStream.readInt(1120986464257L);
                } else if (nextField == 2) {
                    stateResidency.totalTimeInStateMs = protoInputStream.readLong(1112396529666L);
                } else if (nextField == 3) {
                    stateResidency.totalStateEntryCount = protoInputStream.readLong(1112396529667L);
                } else if (nextField == 4) {
                    stateResidency.lastEntryTimestampMs = protoInputStream.readLong(1112396529668L);
                } else {
                    Slog.e(ProtoStreamUtils.TAG, "Unhandled field in StateResidencyProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
            }
        }

        public static void print(StateResidencyResult[] stateResidencyResultArr) {
            if (stateResidencyResultArr == null) {
                return;
            }
            for (int i = 0; i < stateResidencyResultArr.length; i++) {
                Slog.d(ProtoStreamUtils.TAG, "PowerEntityId: " + stateResidencyResultArr[i].id);
                for (int i2 = 0; i2 < stateResidencyResultArr[i].stateResidencyData.length; i2++) {
                    Slog.d(ProtoStreamUtils.TAG, "  StateId: " + stateResidencyResultArr[i].stateResidencyData[i2].id + ", TotalTimeInStateMs: " + stateResidencyResultArr[i].stateResidencyData[i2].totalTimeInStateMs + ", TotalStateEntryCount: " + stateResidencyResultArr[i].stateResidencyData[i2].totalStateEntryCount + ", LastEntryTimestampMs: " + stateResidencyResultArr[i].stateResidencyData[i2].lastEntryTimestampMs);
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class ChannelUtils {
        ChannelUtils() {
        }

        public static byte[] getProtoBytes(Channel[] channelArr) {
            ProtoOutputStream protoOutputStream = new ProtoOutputStream();
            packProtoMessage(channelArr, protoOutputStream);
            return protoOutputStream.getBytes();
        }

        public static void packProtoMessage(Channel[] channelArr, ProtoOutputStream protoOutputStream) {
            if (channelArr == null) {
                return;
            }
            for (int i = 0; i < channelArr.length; i++) {
                long start = protoOutputStream.start(2246267895809L);
                protoOutputStream.write(1120986464257L, channelArr[i].id);
                protoOutputStream.write(1138166333442L, channelArr[i].name);
                protoOutputStream.write(1138166333443L, channelArr[i].subsystem);
                protoOutputStream.end(start);
            }
        }

        public static void print(Channel[] channelArr) {
            if (channelArr == null) {
                return;
            }
            for (int i = 0; i < channelArr.length; i++) {
                Slog.d(ProtoStreamUtils.TAG, "ChannelId: " + channelArr[i].id + ", ChannelName: " + channelArr[i].name + ", ChannelSubsystem: " + channelArr[i].subsystem);
            }
        }

        public static void dumpsys(Channel[] channelArr, PrintWriter printWriter) {
            if (channelArr == null) {
                return;
            }
            for (int i = 0; i < channelArr.length; i++) {
                printWriter.println("ChannelId: " + channelArr[i].id + ", ChannelName: " + channelArr[i].name + ", ChannelSubsystem: " + channelArr[i].subsystem);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class EnergyMeasurementUtils {
        EnergyMeasurementUtils() {
        }

        public static void adjustTimeSinceBootToEpoch(EnergyMeasurement[] energyMeasurementArr, long j) {
            if (energyMeasurementArr == null) {
                return;
            }
            for (EnergyMeasurement energyMeasurement : energyMeasurementArr) {
                energyMeasurement.timestampMs += j;
            }
        }

        public static byte[] getProtoBytes(EnergyMeasurement[] energyMeasurementArr) {
            ProtoOutputStream protoOutputStream = new ProtoOutputStream();
            packProtoMessage(energyMeasurementArr, protoOutputStream);
            return protoOutputStream.getBytes();
        }

        public static void packProtoMessage(EnergyMeasurement[] energyMeasurementArr, ProtoOutputStream protoOutputStream) {
            if (energyMeasurementArr == null) {
                return;
            }
            for (int i = 0; i < energyMeasurementArr.length; i++) {
                long start = protoOutputStream.start(2246267895810L);
                protoOutputStream.write(1120986464257L, energyMeasurementArr[i].id);
                protoOutputStream.write(1112396529666L, energyMeasurementArr[i].timestampMs);
                protoOutputStream.write(1112396529668L, energyMeasurementArr[i].durationMs);
                protoOutputStream.write(1112396529667L, energyMeasurementArr[i].energyUWs);
                protoOutputStream.end(start);
            }
        }

        public static EnergyMeasurement[] unpackProtoMessage(byte[] bArr) throws IOException {
            ProtoInputStream protoInputStream = new ProtoInputStream(new ByteArrayInputStream(bArr));
            ArrayList arrayList = new ArrayList();
            while (true) {
                try {
                    int nextField = protoInputStream.nextField();
                    new EnergyMeasurement();
                    if (nextField == 2) {
                        long start = protoInputStream.start(2246267895810L);
                        arrayList.add(unpackEnergyMeasurementProto(protoInputStream));
                        protoInputStream.end(start);
                    } else {
                        if (nextField == -1) {
                            return (EnergyMeasurement[]) arrayList.toArray(new EnergyMeasurement[arrayList.size()]);
                        }
                        Slog.e(ProtoStreamUtils.TAG, "Unhandled field in proto: " + ProtoUtils.currentFieldToString(protoInputStream));
                    }
                } catch (WireTypeMismatchException unused) {
                    Slog.e(ProtoStreamUtils.TAG, "Wire Type mismatch in proto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
            }
        }

        private static EnergyMeasurement unpackEnergyMeasurementProto(ProtoInputStream protoInputStream) throws IOException {
            int nextField;
            EnergyMeasurement energyMeasurement = new EnergyMeasurement();
            while (true) {
                try {
                    nextField = protoInputStream.nextField();
                } catch (WireTypeMismatchException unused) {
                    Slog.e(ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyMeasurementProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
                if (nextField == -1) {
                    return energyMeasurement;
                }
                if (nextField == 1) {
                    energyMeasurement.id = protoInputStream.readInt(1120986464257L);
                } else if (nextField == 2) {
                    energyMeasurement.timestampMs = protoInputStream.readLong(1112396529666L);
                } else if (nextField == 3) {
                    energyMeasurement.energyUWs = protoInputStream.readLong(1112396529667L);
                } else if (nextField == 4) {
                    energyMeasurement.durationMs = protoInputStream.readLong(1112396529668L);
                } else {
                    Slog.e(ProtoStreamUtils.TAG, "Unhandled field in EnergyMeasurementProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
            }
        }

        public static void print(EnergyMeasurement[] energyMeasurementArr) {
            if (energyMeasurementArr == null) {
                return;
            }
            for (int i = 0; i < energyMeasurementArr.length; i++) {
                Slog.d(ProtoStreamUtils.TAG, "ChannelId: " + energyMeasurementArr[i].id + ", Timestamp (ms): " + energyMeasurementArr[i].timestampMs + ", Duration (ms): " + energyMeasurementArr[i].durationMs + ", Energy (uWs): " + energyMeasurementArr[i].energyUWs);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class EnergyConsumerUtils {
        EnergyConsumerUtils() {
        }

        public static byte[] getProtoBytes(EnergyConsumer[] energyConsumerArr) {
            ProtoOutputStream protoOutputStream = new ProtoOutputStream();
            packProtoMessage(energyConsumerArr, protoOutputStream);
            return protoOutputStream.getBytes();
        }

        public static void packProtoMessage(EnergyConsumer[] energyConsumerArr, ProtoOutputStream protoOutputStream) {
            if (energyConsumerArr == null) {
                return;
            }
            for (int i = 0; i < energyConsumerArr.length; i++) {
                long start = protoOutputStream.start(2246267895809L);
                protoOutputStream.write(1120986464257L, energyConsumerArr[i].id);
                protoOutputStream.write(1120986464258L, energyConsumerArr[i].ordinal);
                protoOutputStream.write(1120986464259L, (int) energyConsumerArr[i].type);
                protoOutputStream.write(1138166333444L, energyConsumerArr[i].name);
                protoOutputStream.end(start);
            }
        }

        public static EnergyConsumer[] unpackProtoMessage(byte[] bArr) throws IOException {
            ProtoInputStream protoInputStream = new ProtoInputStream(new ByteArrayInputStream(bArr));
            ArrayList arrayList = new ArrayList();
            while (true) {
                try {
                    int nextField = protoInputStream.nextField();
                    new EnergyConsumer();
                    if (nextField == 1) {
                        long start = protoInputStream.start(2246267895809L);
                        arrayList.add(unpackEnergyConsumerProto(protoInputStream));
                        protoInputStream.end(start);
                    } else {
                        if (nextField == -1) {
                            return (EnergyConsumer[]) arrayList.toArray(new EnergyConsumer[arrayList.size()]);
                        }
                        Slog.e(ProtoStreamUtils.TAG, "Unhandled field in proto: " + ProtoUtils.currentFieldToString(protoInputStream));
                    }
                } catch (WireTypeMismatchException unused) {
                    Slog.e(ProtoStreamUtils.TAG, "Wire Type mismatch in proto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
            }
        }

        private static EnergyConsumer unpackEnergyConsumerProto(ProtoInputStream protoInputStream) throws IOException {
            int nextField;
            EnergyConsumer energyConsumer = new EnergyConsumer();
            while (true) {
                try {
                    nextField = protoInputStream.nextField();
                } catch (WireTypeMismatchException unused) {
                    Slog.e(ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyConsumerProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
                if (nextField == -1) {
                    return energyConsumer;
                }
                if (nextField == 1) {
                    energyConsumer.id = protoInputStream.readInt(1120986464257L);
                } else if (nextField == 2) {
                    energyConsumer.ordinal = protoInputStream.readInt(1120986464258L);
                } else if (nextField == 3) {
                    energyConsumer.type = (byte) protoInputStream.readInt(1120986464259L);
                } else if (nextField == 4) {
                    energyConsumer.name = protoInputStream.readString(1138166333444L);
                } else {
                    Slog.e(ProtoStreamUtils.TAG, "Unhandled field in EnergyConsumerProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
            }
        }

        public static void print(EnergyConsumer[] energyConsumerArr) {
            if (energyConsumerArr == null) {
                return;
            }
            for (int i = 0; i < energyConsumerArr.length; i++) {
                Slog.d(ProtoStreamUtils.TAG, "EnergyConsumerId: " + energyConsumerArr[i].id + ", Ordinal: " + energyConsumerArr[i].ordinal + ", Type: " + ((int) energyConsumerArr[i].type) + ", Name: " + energyConsumerArr[i].name);
            }
        }

        public static void dumpsys(EnergyConsumer[] energyConsumerArr, PrintWriter printWriter) {
            if (energyConsumerArr == null) {
                return;
            }
            for (int i = 0; i < energyConsumerArr.length; i++) {
                printWriter.println("EnergyConsumerId: " + energyConsumerArr[i].id + ", Ordinal: " + energyConsumerArr[i].ordinal + ", Type: " + ((int) energyConsumerArr[i].type) + ", Name: " + energyConsumerArr[i].name);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class EnergyConsumerResultUtils {
        EnergyConsumerResultUtils() {
        }

        public static void adjustTimeSinceBootToEpoch(EnergyConsumerResult[] energyConsumerResultArr, long j) {
            if (energyConsumerResultArr == null) {
                return;
            }
            for (EnergyConsumerResult energyConsumerResult : energyConsumerResultArr) {
                energyConsumerResult.timestampMs += j;
            }
        }

        public static byte[] getProtoBytes(EnergyConsumerResult[] energyConsumerResultArr, boolean z) {
            ProtoOutputStream protoOutputStream = new ProtoOutputStream();
            packProtoMessage(energyConsumerResultArr, protoOutputStream, z);
            return protoOutputStream.getBytes();
        }

        public static void packProtoMessage(EnergyConsumerResult[] energyConsumerResultArr, ProtoOutputStream protoOutputStream, boolean z) {
            if (energyConsumerResultArr == null) {
                return;
            }
            for (int i = 0; i < energyConsumerResultArr.length; i++) {
                long start = protoOutputStream.start(2246267895810L);
                long j = 1120986464257L;
                protoOutputStream.write(1120986464257L, energyConsumerResultArr[i].id);
                protoOutputStream.write(1112396529666L, energyConsumerResultArr[i].timestampMs);
                protoOutputStream.write(1112396529667L, energyConsumerResultArr[i].energyUWs);
                if (z) {
                    int length = energyConsumerResultArr[i].attribution.length;
                    int i2 = 0;
                    while (i2 < length) {
                        EnergyConsumerAttribution energyConsumerAttribution = energyConsumerResultArr[i].attribution[i2];
                        long start2 = protoOutputStream.start(2246267895812L);
                        protoOutputStream.write(j, energyConsumerAttribution.uid);
                        protoOutputStream.write(1112396529666L, energyConsumerAttribution.energyUWs);
                        protoOutputStream.end(start2);
                        i2++;
                        j = 1120986464257L;
                    }
                }
                protoOutputStream.end(start);
            }
        }

        public static EnergyConsumerResult[] unpackProtoMessage(byte[] bArr) throws IOException {
            ProtoInputStream protoInputStream = new ProtoInputStream(new ByteArrayInputStream(bArr));
            ArrayList arrayList = new ArrayList();
            while (true) {
                try {
                    int nextField = protoInputStream.nextField();
                    new EnergyConsumerResult();
                    if (nextField == 2) {
                        long start = protoInputStream.start(2246267895810L);
                        arrayList.add(unpackEnergyConsumerResultProto(protoInputStream));
                        protoInputStream.end(start);
                    } else {
                        if (nextField == -1) {
                            return (EnergyConsumerResult[]) arrayList.toArray(new EnergyConsumerResult[arrayList.size()]);
                        }
                        Slog.e(ProtoStreamUtils.TAG, "Unhandled field in proto: " + ProtoUtils.currentFieldToString(protoInputStream));
                    }
                } catch (WireTypeMismatchException unused) {
                    Slog.e(ProtoStreamUtils.TAG, "Wire Type mismatch in proto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
            }
        }

        private static EnergyConsumerAttribution unpackEnergyConsumerAttributionProto(ProtoInputStream protoInputStream) throws IOException {
            int nextField;
            EnergyConsumerAttribution energyConsumerAttribution = new EnergyConsumerAttribution();
            while (true) {
                try {
                    nextField = protoInputStream.nextField();
                } catch (WireTypeMismatchException unused) {
                    Slog.e(ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyConsumerAttributionProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
                if (nextField == -1) {
                    return energyConsumerAttribution;
                }
                if (nextField == 1) {
                    energyConsumerAttribution.uid = protoInputStream.readInt(1120986464257L);
                } else if (nextField == 2) {
                    energyConsumerAttribution.energyUWs = protoInputStream.readLong(1112396529666L);
                } else {
                    Slog.e(ProtoStreamUtils.TAG, "Unhandled field in EnergyConsumerAttributionProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
            }
        }

        private static EnergyConsumerResult unpackEnergyConsumerResultProto(ProtoInputStream protoInputStream) throws IOException {
            int nextField;
            EnergyConsumerResult energyConsumerResult = new EnergyConsumerResult();
            ArrayList arrayList = new ArrayList();
            while (true) {
                try {
                    nextField = protoInputStream.nextField();
                } catch (WireTypeMismatchException unused) {
                    Slog.e(ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyConsumerResultProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
                if (nextField == -1) {
                    energyConsumerResult.attribution = (EnergyConsumerAttribution[]) arrayList.toArray(new EnergyConsumerAttribution[arrayList.size()]);
                    return energyConsumerResult;
                }
                if (nextField == 1) {
                    energyConsumerResult.id = protoInputStream.readInt(1120986464257L);
                } else if (nextField == 2) {
                    energyConsumerResult.timestampMs = protoInputStream.readLong(1112396529666L);
                } else if (nextField == 3) {
                    energyConsumerResult.energyUWs = protoInputStream.readLong(1112396529667L);
                } else if (nextField == 4) {
                    long start = protoInputStream.start(2246267895812L);
                    arrayList.add(unpackEnergyConsumerAttributionProto(protoInputStream));
                    protoInputStream.end(start);
                } else {
                    Slog.e(ProtoStreamUtils.TAG, "Unhandled field in EnergyConsumerResultProto: " + ProtoUtils.currentFieldToString(protoInputStream));
                }
            }
        }

        public static void print(EnergyConsumerResult[] energyConsumerResultArr) {
            if (energyConsumerResultArr == null) {
                return;
            }
            for (EnergyConsumerResult energyConsumerResult : energyConsumerResultArr) {
                Slog.d(ProtoStreamUtils.TAG, "EnergyConsumerId: " + energyConsumerResult.id + ", Timestamp (ms): " + energyConsumerResult.timestampMs + ", Energy (uWs): " + energyConsumerResult.energyUWs);
                int length = energyConsumerResult.attribution.length;
                for (int i = 0; i < length; i++) {
                    EnergyConsumerAttribution energyConsumerAttribution = energyConsumerResult.attribution[i];
                    Slog.d(ProtoStreamUtils.TAG, "  UID: " + energyConsumerAttribution.uid + "  Energy (uWs): " + energyConsumerAttribution.energyUWs);
                }
            }
        }
    }
}
