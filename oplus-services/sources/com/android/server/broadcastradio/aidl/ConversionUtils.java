package com.android.server.broadcastradio.aidl;

import android.annotation.SuppressLint;
import android.app.compat.CompatChanges;
import android.hardware.broadcastradio.AmFmBandRange;
import android.hardware.broadcastradio.AmFmRegionConfig;
import android.hardware.broadcastradio.DabTableEntry;
import android.hardware.broadcastradio.Metadata;
import android.hardware.broadcastradio.ProgramFilter;
import android.hardware.broadcastradio.ProgramIdentifier;
import android.hardware.broadcastradio.ProgramInfo;
import android.hardware.broadcastradio.ProgramListChunk;
import android.hardware.broadcastradio.Properties;
import android.hardware.broadcastradio.VendorKeyValue;
import android.hardware.radio.Announcement;
import android.hardware.radio.ProgramList;
import android.hardware.radio.ProgramSelector;
import android.hardware.radio.RadioManager;
import android.hardware.radio.RadioMetadata;
import android.os.ParcelableException;
import android.os.ServiceSpecificException;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IntArray;
import com.android.server.broadcastradio.aidl.Utils;
import com.android.server.utils.Slogf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ConversionUtils {
    public static final long RADIO_U_VERSION_REQUIRED = 261770108;
    private static final String TAG = "BcRadioAidlSrv.convert";

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int halResultToTunerResult(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                return 7;
        }
    }

    private static int identifierTypeToProgramType(int i) {
        switch (i) {
            case 1:
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
            case 11:
            default:
                if (i < 1000 || i > 1999) {
                    return 0;
                }
                return i;
            case 5:
            case 6:
            case 7:
            case 8:
            case 14:
                return 5;
            case 9:
            case 10:
                return 6;
            case 12:
            case 13:
                return 7;
        }
    }

    private static boolean isVendorIdentifierType(int i) {
        return i >= 1000 && i <= 1999;
    }

    private ConversionUtils() {
        throw new UnsupportedOperationException("ConversionUtils class is noninstantiable");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"AndroidFrameworkRequiresPermission"})
    public static boolean isAtLeastU(int i) {
        return CompatChanges.isChangeEnabled(RADIO_U_VERSION_REQUIRED, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RuntimeException throwOnError(RuntimeException runtimeException, String str) {
        if (!(runtimeException instanceof ServiceSpecificException)) {
            return new ParcelableException(new RuntimeException(str + ": unknown error"));
        }
        int i = ((ServiceSpecificException) runtimeException).errorCode;
        switch (i) {
            case 1:
                return new ParcelableException(new RuntimeException(str + ": INTERNAL_ERROR"));
            case 2:
                return new IllegalArgumentException(str + ": INVALID_ARGUMENTS");
            case 3:
                return new IllegalStateException(str + ": INVALID_STATE");
            case 4:
                return new UnsupportedOperationException(str + ": NOT_SUPPORTED");
            case 5:
                return new ParcelableException(new RuntimeException(str + ": TIMEOUT"));
            case 6:
                return new IllegalStateException(str + ": CANCELED");
            case 7:
                return new ParcelableException(new RuntimeException(str + ": UNKNOWN_ERROR"));
            default:
                return new ParcelableException(new RuntimeException(str + ": unknown error (" + i + ")"));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static VendorKeyValue[] vendorInfoToHalVendorKeyValues(Map<String, String> map) {
        if (map == null) {
            return new VendorKeyValue[0];
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            VendorKeyValue vendorKeyValue = new VendorKeyValue();
            vendorKeyValue.key = entry.getKey();
            String value = entry.getValue();
            vendorKeyValue.value = value;
            String str = vendorKeyValue.key;
            if (str == null || value == null) {
                Slogf.w(TAG, "VendorKeyValue contains invalid entry: key = %s, value = %s", new Object[]{str, value});
            } else {
                arrayList.add(vendorKeyValue);
            }
        }
        return (VendorKeyValue[]) arrayList.toArray(new IntFunction() { // from class: com.android.server.broadcastradio.aidl.ConversionUtils$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                VendorKeyValue[] lambda$vendorInfoToHalVendorKeyValues$0;
                lambda$vendorInfoToHalVendorKeyValues$0 = ConversionUtils.lambda$vendorInfoToHalVendorKeyValues$0(i);
                return lambda$vendorInfoToHalVendorKeyValues$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ VendorKeyValue[] lambda$vendorInfoToHalVendorKeyValues$0(int i) {
        return new VendorKeyValue[i];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Map<String, String> vendorInfoFromHalVendorKeyValues(VendorKeyValue[] vendorKeyValueArr) {
        String str;
        if (vendorKeyValueArr == null) {
            return Collections.emptyMap();
        }
        ArrayMap arrayMap = new ArrayMap();
        for (VendorKeyValue vendorKeyValue : vendorKeyValueArr) {
            String str2 = vendorKeyValue.key;
            if (str2 == null || (str = vendorKeyValue.value) == null) {
                Slogf.w(TAG, "VendorKeyValue contains invalid entry: key = %s, value = %s", new Object[]{str2, vendorKeyValue.value});
            } else {
                arrayMap.put(str2, str);
            }
        }
        return arrayMap;
    }

    private static int[] identifierTypesToProgramTypes(int[] iArr) {
        ArraySet arraySet = new ArraySet();
        int i = 0;
        for (int i2 : iArr) {
            int identifierTypeToProgramType = identifierTypeToProgramType(i2);
            if (identifierTypeToProgramType != 0) {
                arraySet.add(Integer.valueOf(identifierTypeToProgramType));
                if (identifierTypeToProgramType == 2) {
                    arraySet.add(1);
                }
                if (identifierTypeToProgramType == 4) {
                    arraySet.add(3);
                }
            }
        }
        int[] iArr2 = new int[arraySet.size()];
        Iterator it = arraySet.iterator();
        while (it.hasNext()) {
            iArr2[i] = ((Integer) it.next()).intValue();
            i++;
        }
        return iArr2;
    }

    private static RadioManager.BandDescriptor[] amfmConfigToBands(AmFmRegionConfig amFmRegionConfig) {
        if (amFmRegionConfig == null) {
            return new RadioManager.BandDescriptor[0];
        }
        int length = amFmRegionConfig.ranges.length;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < length; i++) {
            Utils.FrequencyBand band = Utils.getBand(amFmRegionConfig.ranges[i].lowerBound);
            if (band == Utils.FrequencyBand.UNKNOWN) {
                Slogf.e(TAG, "Unknown frequency band at %d kHz", new Object[]{Integer.valueOf(amFmRegionConfig.ranges[i].lowerBound)});
            } else if (band == Utils.FrequencyBand.FM) {
                AmFmBandRange amFmBandRange = amFmRegionConfig.ranges[i];
                arrayList.add(new RadioManager.FmBandDescriptor(0, 1, amFmBandRange.lowerBound, amFmBandRange.upperBound, amFmBandRange.spacing, true, true, true, true, true));
            } else {
                AmFmBandRange amFmBandRange2 = amFmRegionConfig.ranges[i];
                arrayList.add(new RadioManager.AmBandDescriptor(0, 0, amFmBandRange2.lowerBound, amFmBandRange2.upperBound, amFmBandRange2.spacing, true));
            }
        }
        return (RadioManager.BandDescriptor[]) arrayList.toArray(new IntFunction() { // from class: com.android.server.broadcastradio.aidl.ConversionUtils$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final Object apply(int i2) {
                RadioManager.BandDescriptor[] lambda$amfmConfigToBands$1;
                lambda$amfmConfigToBands$1 = ConversionUtils.lambda$amfmConfigToBands$1(i2);
                return lambda$amfmConfigToBands$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ RadioManager.BandDescriptor[] lambda$amfmConfigToBands$1(int i) {
        return new RadioManager.BandDescriptor[i];
    }

    private static Map<String, Integer> dabConfigFromHalDabTableEntries(DabTableEntry[] dabTableEntryArr) {
        if (dabTableEntryArr == null) {
            return null;
        }
        ArrayMap arrayMap = new ArrayMap();
        for (DabTableEntry dabTableEntry : dabTableEntryArr) {
            arrayMap.put(dabTableEntry.label, Integer.valueOf(dabTableEntry.frequencyKhz));
        }
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RadioManager.ModuleProperties propertiesFromHalProperties(int i, String str, Properties properties, AmFmRegionConfig amFmRegionConfig, DabTableEntry[] dabTableEntryArr) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(properties);
        return new RadioManager.ModuleProperties(i, str, 0, properties.maker, properties.product, properties.version, properties.serial, 1, 1, false, false, amfmConfigToBands(amFmRegionConfig), true, identifierTypesToProgramTypes(properties.supportedIdentifierTypes), properties.supportedIdentifierTypes, dabConfigFromHalDabTableEntries(dabTableEntryArr), vendorInfoFromHalVendorKeyValues(properties.vendorInfo));
    }

    static ProgramIdentifier identifierToHalProgramIdentifier(ProgramSelector.Identifier identifier) {
        ProgramIdentifier programIdentifier = new ProgramIdentifier();
        int type = identifier.getType();
        programIdentifier.type = type;
        if (type == 14) {
            programIdentifier.type = 5;
        }
        programIdentifier.value = identifier.getValue();
        return programIdentifier;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ProgramSelector.Identifier identifierFromHalProgramIdentifier(ProgramIdentifier programIdentifier) {
        int i = programIdentifier.type;
        if (i == 0) {
            return null;
        }
        if (i == 5) {
            i = 14;
        }
        return new ProgramSelector.Identifier(i, programIdentifier.value);
    }

    private static boolean isValidHalProgramSelector(android.hardware.broadcastradio.ProgramSelector programSelector) {
        int i = programSelector.primaryId.type;
        return i == 1 || i == 2 || i == 3 || i == 5 || i == 9 || i == 12 || isVendorIdentifierType(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static android.hardware.broadcastradio.ProgramSelector programSelectorToHalProgramSelector(ProgramSelector programSelector) {
        android.hardware.broadcastradio.ProgramSelector programSelector2 = new android.hardware.broadcastradio.ProgramSelector();
        programSelector2.primaryId = identifierToHalProgramIdentifier(programSelector.getPrimaryId());
        ProgramSelector.Identifier[] secondaryIds = programSelector.getSecondaryIds();
        ArrayList arrayList = new ArrayList(secondaryIds.length);
        for (ProgramSelector.Identifier identifier : secondaryIds) {
            arrayList.add(identifierToHalProgramIdentifier(identifier));
        }
        programSelector2.secondaryIds = (ProgramIdentifier[]) arrayList.toArray(new IntFunction() { // from class: com.android.server.broadcastradio.aidl.ConversionUtils$$ExternalSyntheticLambda1
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                ProgramIdentifier[] lambda$programSelectorToHalProgramSelector$2;
                lambda$programSelectorToHalProgramSelector$2 = ConversionUtils.lambda$programSelectorToHalProgramSelector$2(i);
                return lambda$programSelectorToHalProgramSelector$2;
            }
        });
        if (isValidHalProgramSelector(programSelector2)) {
            return programSelector2;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ProgramIdentifier[] lambda$programSelectorToHalProgramSelector$2(int i) {
        return new ProgramIdentifier[i];
    }

    private static boolean isEmpty(android.hardware.broadcastradio.ProgramSelector programSelector) {
        ProgramIdentifier programIdentifier = programSelector.primaryId;
        return programIdentifier.type == 0 && programIdentifier.value == 0 && programSelector.secondaryIds.length == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ProgramSelector programSelectorFromHalProgramSelector(android.hardware.broadcastradio.ProgramSelector programSelector) {
        if (isEmpty(programSelector) || !isValidHalProgramSelector(programSelector)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            ProgramIdentifier[] programIdentifierArr = programSelector.secondaryIds;
            if (i < programIdentifierArr.length) {
                ProgramIdentifier programIdentifier = programIdentifierArr[i];
                if (programIdentifier != null) {
                    arrayList.add(identifierFromHalProgramIdentifier(programIdentifier));
                }
                i++;
            } else {
                int identifierTypeToProgramType = identifierTypeToProgramType(programSelector.primaryId.type);
                ProgramSelector.Identifier identifierFromHalProgramIdentifier = identifierFromHalProgramIdentifier(programSelector.primaryId);
                Objects.requireNonNull(identifierFromHalProgramIdentifier);
                return new ProgramSelector(identifierTypeToProgramType, identifierFromHalProgramIdentifier, (ProgramSelector.Identifier[]) arrayList.toArray(new ProgramSelector.Identifier[0]), (long[]) null);
            }
        }
    }

    private static RadioMetadata radioMetadataFromHalMetadata(Metadata[] metadataArr) {
        RadioMetadata.Builder builder = new RadioMetadata.Builder();
        for (int i = 0; i < metadataArr.length; i++) {
            switch (metadataArr[i].getTag()) {
                case 0:
                    builder.putString("android.hardware.radio.metadata.RDS_PS", metadataArr[i].getRdsPs());
                    break;
                case 1:
                    builder.putInt("android.hardware.radio.metadata.RDS_PTY", metadataArr[i].getRdsPty());
                    break;
                case 2:
                    builder.putInt("android.hardware.radio.metadata.RBDS_PTY", metadataArr[i].getRbdsPty());
                    break;
                case 3:
                    builder.putString("android.hardware.radio.metadata.RDS_RT", metadataArr[i].getRdsRt());
                    break;
                case 4:
                    builder.putString("android.hardware.radio.metadata.TITLE", metadataArr[i].getSongTitle());
                    break;
                case 5:
                    builder.putString("android.hardware.radio.metadata.ARTIST", metadataArr[i].getSongArtist());
                    break;
                case 6:
                    builder.putString("android.hardware.radio.metadata.ALBUM", metadataArr[i].getSongAlbum());
                    break;
                case 7:
                    builder.putInt("android.hardware.radio.metadata.ICON", metadataArr[i].getStationIcon());
                    break;
                case 8:
                    builder.putInt("android.hardware.radio.metadata.ART", metadataArr[i].getAlbumArt());
                    break;
                case 9:
                    builder.putString("android.hardware.radio.metadata.PROGRAM_NAME", metadataArr[i].getProgramName());
                    break;
                case 10:
                    builder.putString("android.hardware.radio.metadata.DAB_ENSEMBLE_NAME", metadataArr[i].getDabEnsembleName());
                    break;
                case 11:
                    builder.putString("android.hardware.radio.metadata.DAB_ENSEMBLE_NAME_SHORT", metadataArr[i].getDabEnsembleNameShort());
                    break;
                case 12:
                    builder.putString("android.hardware.radio.metadata.DAB_SERVICE_NAME", metadataArr[i].getDabServiceName());
                    break;
                case 13:
                    builder.putString("android.hardware.radio.metadata.DAB_SERVICE_NAME_SHORT", metadataArr[i].getDabServiceNameShort());
                    break;
                case 14:
                    builder.putString("android.hardware.radio.metadata.DAB_COMPONENT_NAME", metadataArr[i].getDabComponentName());
                    break;
                case 15:
                    builder.putString("android.hardware.radio.metadata.DAB_COMPONENT_NAME_SHORT", metadataArr[i].getDabComponentNameShort());
                    break;
                default:
                    Slogf.w(TAG, "Ignored unknown metadata entry: %s", new Object[]{metadataArr[i]});
                    break;
            }
        }
        return builder.build();
    }

    private static boolean isValidLogicallyTunedTo(ProgramIdentifier programIdentifier) {
        int i = programIdentifier.type;
        return i == 1 || i == 2 || i == 3 || i == 5 || i == 9 || i == 12 || isVendorIdentifierType(i);
    }

    private static boolean isValidPhysicallyTunedTo(ProgramIdentifier programIdentifier) {
        int i = programIdentifier.type;
        return i == 1 || i == 8 || i == 10 || i == 13 || isVendorIdentifierType(i);
    }

    private static boolean isValidHalProgramInfo(ProgramInfo programInfo) {
        return isValidHalProgramSelector(programInfo.selector) && isValidLogicallyTunedTo(programInfo.logicallyTunedTo) && isValidPhysicallyTunedTo(programInfo.physicallyTunedTo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RadioManager.ProgramInfo programInfoFromHalProgramInfo(ProgramInfo programInfo) {
        if (!isValidHalProgramInfo(programInfo)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (programInfo.relatedContent != null) {
            int i = 0;
            while (true) {
                ProgramIdentifier[] programIdentifierArr = programInfo.relatedContent;
                if (i >= programIdentifierArr.length) {
                    break;
                }
                ProgramSelector.Identifier identifierFromHalProgramIdentifier = identifierFromHalProgramIdentifier(programIdentifierArr[i]);
                if (identifierFromHalProgramIdentifier != null) {
                    arrayList.add(identifierFromHalProgramIdentifier);
                }
                i++;
            }
        }
        ProgramSelector programSelectorFromHalProgramSelector = programSelectorFromHalProgramSelector(programInfo.selector);
        Objects.requireNonNull(programSelectorFromHalProgramSelector);
        return new RadioManager.ProgramInfo(programSelectorFromHalProgramSelector, identifierFromHalProgramIdentifier(programInfo.logicallyTunedTo), identifierFromHalProgramIdentifier(programInfo.physicallyTunedTo), arrayList, programInfo.infoFlags, programInfo.signalQuality, radioMetadataFromHalMetadata(programInfo.metadata), vendorInfoFromHalVendorKeyValues(programInfo.vendorInfo));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ProgramFilter filterToHalProgramFilter(ProgramList.Filter filter) {
        if (filter == null) {
            filter = new ProgramList.Filter();
        }
        ProgramFilter programFilter = new ProgramFilter();
        IntArray intArray = new IntArray(filter.getIdentifierTypes().size());
        ArrayList arrayList = new ArrayList();
        Iterator it = filter.getIdentifierTypes().iterator();
        while (it.hasNext()) {
            intArray.add(((Integer) it.next()).intValue());
        }
        Iterator it2 = filter.getIdentifiers().iterator();
        while (it2.hasNext()) {
            arrayList.add(identifierToHalProgramIdentifier((ProgramSelector.Identifier) it2.next()));
        }
        programFilter.identifierTypes = intArray.toArray();
        programFilter.identifiers = (ProgramIdentifier[]) arrayList.toArray(new IntFunction() { // from class: com.android.server.broadcastradio.aidl.ConversionUtils$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                ProgramIdentifier[] lambda$filterToHalProgramFilter$3;
                lambda$filterToHalProgramFilter$3 = ConversionUtils.lambda$filterToHalProgramFilter$3(i);
                return lambda$filterToHalProgramFilter$3;
            }
        });
        programFilter.includeCategories = filter.areCategoriesIncluded();
        programFilter.excludeModifications = filter.areModificationsExcluded();
        return programFilter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ProgramIdentifier[] lambda$filterToHalProgramFilter$3(int i) {
        return new ProgramIdentifier[i];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ProgramList.Chunk chunkFromHalProgramListChunk(ProgramListChunk programListChunk) {
        ArraySet arraySet = new ArraySet(programListChunk.modified.length);
        int i = 0;
        int i2 = 0;
        while (true) {
            ProgramInfo[] programInfoArr = programListChunk.modified;
            if (i2 >= programInfoArr.length) {
                break;
            }
            RadioManager.ProgramInfo programInfoFromHalProgramInfo = programInfoFromHalProgramInfo(programInfoArr[i2]);
            if (programInfoFromHalProgramInfo == null) {
                Slogf.w(TAG, "Program info %s in program list chunk is not valid", new Object[]{programListChunk.modified[i2]});
            } else {
                arraySet.add(programInfoFromHalProgramInfo);
            }
            i2++;
        }
        ArraySet arraySet2 = new ArraySet();
        if (programListChunk.removed != null) {
            while (true) {
                ProgramIdentifier[] programIdentifierArr = programListChunk.removed;
                if (i >= programIdentifierArr.length) {
                    break;
                }
                ProgramSelector.Identifier identifierFromHalProgramIdentifier = identifierFromHalProgramIdentifier(programIdentifierArr[i]);
                if (identifierFromHalProgramIdentifier != null) {
                    arraySet2.add(identifierFromHalProgramIdentifier);
                }
                i++;
            }
        }
        return new ProgramList.Chunk(programListChunk.purge, programListChunk.complete, arraySet, arraySet2);
    }

    private static boolean isNewIdentifierInU(ProgramSelector.Identifier identifier) {
        return identifier.getType() == 14;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean programSelectorMeetsSdkVersionRequirement(ProgramSelector programSelector, int i) {
        if (isAtLeastU(i)) {
            return true;
        }
        if (programSelector.getPrimaryId().getType() == 14) {
            return false;
        }
        for (ProgramSelector.Identifier identifier : programSelector.getSecondaryIds()) {
            if (isNewIdentifierInU(identifier)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean programInfoMeetsSdkVersionRequirement(RadioManager.ProgramInfo programInfo, int i) {
        if (isAtLeastU(i)) {
            return true;
        }
        if (!programSelectorMeetsSdkVersionRequirement(programInfo.getSelector(), i) || isNewIdentifierInU(programInfo.getLogicallyTunedTo()) || isNewIdentifierInU(programInfo.getPhysicallyTunedTo())) {
            return false;
        }
        Iterator it = programInfo.getRelatedContent().iterator();
        while (it.hasNext()) {
            if (isNewIdentifierInU((ProgramSelector.Identifier) it.next())) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ProgramList.Chunk convertChunkToTargetSdkVersion(ProgramList.Chunk chunk, int i) {
        if (isAtLeastU(i)) {
            return chunk;
        }
        ArraySet arraySet = new ArraySet();
        for (RadioManager.ProgramInfo programInfo : chunk.getModified()) {
            if (programInfoMeetsSdkVersionRequirement(programInfo, i)) {
                arraySet.add(programInfo);
            }
        }
        ArraySet arraySet2 = new ArraySet();
        for (ProgramSelector.Identifier identifier : chunk.getRemoved()) {
            if (!isNewIdentifierInU(identifier)) {
                arraySet2.add(identifier);
            }
        }
        return new ProgramList.Chunk(chunk.isPurge(), chunk.isComplete(), arraySet, arraySet2);
    }

    public static Announcement announcementFromHalAnnouncement(android.hardware.broadcastradio.Announcement announcement) {
        ProgramSelector programSelectorFromHalProgramSelector = programSelectorFromHalProgramSelector(announcement.selector);
        Objects.requireNonNull(programSelectorFromHalProgramSelector, "Program selector can not be null");
        return new Announcement(programSelectorFromHalProgramSelector, announcement.type, vendorInfoFromHalVendorKeyValues(announcement.vendorInfo));
    }
}
