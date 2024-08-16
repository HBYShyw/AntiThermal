package vendor.oplus.hardware.ormsHalService;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOrmsAidlHalService extends IInterface {
    public static final String DESCRIPTOR = "vendor$oplus$hardware$ormsHalService$IOrmsAidlHalService".replace('$', '.');
    public static final String HASH = "45b9fba87d14d35c461848d6bb6cc13324f947c3";
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements IOrmsAidlHalService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public String getInterfaceHash() {
            return "";
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsBoostAcquire(int i, int[] iArr) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsBoostRelease(int i) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsEnableCpuBouncing(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public String ormsReadDdrAvailFreq(int i) throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public String ormsReadFile(String str) throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public String ormsReadGpuFreq(int i) throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteAboveHispeedDelay(int i, int i2, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteBgCpuUclampMin(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteBusyDownThres(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteBusyUpThres(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCameraTracingEvents(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCoreCtlEnable(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuBouncing(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuCoreNum(int i, int i2, int i3) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuCpuDdrBwMin(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuCpuDdrLatMin(int i, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuDdrLatfloorMax(int i, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuDdrLatfloorMax2(int i, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuDdrLatfloorMin(int i, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuDdrLatfloorMin2(int i, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuL3LatMax(int i, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuLlccLatMax(int i, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuOnline(int i, int i2) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteFgCpuUclampMin(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteForceStep(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteFpsgo(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteHwmonHystOpt(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteInputBoostEnabled(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteInputBoostFreq(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteLlccDdrLatMax(int i, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteLowPowerMode(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteMemlatL3Opt(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteNodeCommon(int i, int i2, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWritePreferSilverEnabled(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteRulerEnable(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteSchedAsymcapBoost(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteSchedtuneColocate(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteSchedtunePreferIdle(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteSleepDisabled(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteSlideBoost(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteTargetLoads(int i, int i2, String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteTopCpuUclampMin(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteTouchBoost(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteTracingSetEvent(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteUclampLatencySensitive(String str) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWritehalUfsPlusCtrl(String str, String str2) throws RemoteException {
        }
    }

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void ormsBoostAcquire(int i, int[] iArr) throws RemoteException;

    void ormsBoostRelease(int i) throws RemoteException;

    void ormsEnableCpuBouncing(String str) throws RemoteException;

    String ormsReadDdrAvailFreq(int i) throws RemoteException;

    String ormsReadFile(String str) throws RemoteException;

    String ormsReadGpuFreq(int i) throws RemoteException;

    void ormsWriteAboveHispeedDelay(int i, int i2, String str) throws RemoteException;

    void ormsWriteBgCpuUclampMin(String str) throws RemoteException;

    void ormsWriteBusyDownThres(String str) throws RemoteException;

    void ormsWriteBusyUpThres(String str) throws RemoteException;

    void ormsWriteCameraTracingEvents(String str) throws RemoteException;

    void ormsWriteCoreCtlEnable(String str) throws RemoteException;

    void ormsWriteCpuBouncing(String str) throws RemoteException;

    void ormsWriteCpuCoreNum(int i, int i2, int i3) throws RemoteException;

    void ormsWriteCpuCpuDdrBwMin(String str) throws RemoteException;

    void ormsWriteCpuCpuDdrLatMin(int i, String str) throws RemoteException;

    void ormsWriteCpuDdrLatfloorMax(int i, String str) throws RemoteException;

    void ormsWriteCpuDdrLatfloorMax2(int i, String str) throws RemoteException;

    void ormsWriteCpuDdrLatfloorMin(int i, String str) throws RemoteException;

    void ormsWriteCpuDdrLatfloorMin2(int i, String str) throws RemoteException;

    void ormsWriteCpuL3LatMax(int i, String str) throws RemoteException;

    void ormsWriteCpuLlccLatMax(int i, String str) throws RemoteException;

    void ormsWriteCpuOnline(int i, int i2) throws RemoteException;

    void ormsWriteFgCpuUclampMin(String str) throws RemoteException;

    void ormsWriteForceStep(String str) throws RemoteException;

    void ormsWriteFpsgo(String str) throws RemoteException;

    void ormsWriteHwmonHystOpt(String str) throws RemoteException;

    void ormsWriteInputBoostEnabled(String str) throws RemoteException;

    void ormsWriteInputBoostFreq(String str) throws RemoteException;

    void ormsWriteLlccDdrLatMax(int i, String str) throws RemoteException;

    void ormsWriteLowPowerMode(String str) throws RemoteException;

    void ormsWriteMemlatL3Opt(String str) throws RemoteException;

    void ormsWriteNodeCommon(int i, int i2, String str) throws RemoteException;

    void ormsWritePreferSilverEnabled(String str) throws RemoteException;

    void ormsWriteRulerEnable(String str) throws RemoteException;

    void ormsWriteSchedAsymcapBoost(String str) throws RemoteException;

    void ormsWriteSchedtuneColocate(String str) throws RemoteException;

    void ormsWriteSchedtunePreferIdle(String str) throws RemoteException;

    void ormsWriteSleepDisabled(String str) throws RemoteException;

    void ormsWriteSlideBoost(String str) throws RemoteException;

    void ormsWriteTargetLoads(int i, int i2, String str) throws RemoteException;

    void ormsWriteTopCpuUclampMin(String str) throws RemoteException;

    void ormsWriteTouchBoost(String str) throws RemoteException;

    void ormsWriteTracingSetEvent(String str) throws RemoteException;

    void ormsWriteUclampLatencySensitive(String str) throws RemoteException;

    void ormsWritehalUfsPlusCtrl(String str, String str2) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements IOrmsAidlHalService {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_ormsBoostAcquire = 1;
        static final int TRANSACTION_ormsBoostRelease = 2;
        static final int TRANSACTION_ormsEnableCpuBouncing = 3;
        static final int TRANSACTION_ormsReadDdrAvailFreq = 4;
        static final int TRANSACTION_ormsReadFile = 5;
        static final int TRANSACTION_ormsReadGpuFreq = 6;
        static final int TRANSACTION_ormsWriteAboveHispeedDelay = 7;
        static final int TRANSACTION_ormsWriteBgCpuUclampMin = 8;
        static final int TRANSACTION_ormsWriteBusyDownThres = 9;
        static final int TRANSACTION_ormsWriteBusyUpThres = 10;
        static final int TRANSACTION_ormsWriteCameraTracingEvents = 11;
        static final int TRANSACTION_ormsWriteCoreCtlEnable = 12;
        static final int TRANSACTION_ormsWriteCpuBouncing = 13;
        static final int TRANSACTION_ormsWriteCpuCoreNum = 14;
        static final int TRANSACTION_ormsWriteCpuCpuDdrBwMin = 44;
        static final int TRANSACTION_ormsWriteCpuCpuDdrLatMin = 43;
        static final int TRANSACTION_ormsWriteCpuDdrLatfloorMax = 15;
        static final int TRANSACTION_ormsWriteCpuDdrLatfloorMax2 = 16;
        static final int TRANSACTION_ormsWriteCpuDdrLatfloorMin = 17;
        static final int TRANSACTION_ormsWriteCpuDdrLatfloorMin2 = 18;
        static final int TRANSACTION_ormsWriteCpuL3LatMax = 19;
        static final int TRANSACTION_ormsWriteCpuLlccLatMax = 20;
        static final int TRANSACTION_ormsWriteCpuOnline = 21;
        static final int TRANSACTION_ormsWriteFgCpuUclampMin = 22;
        static final int TRANSACTION_ormsWriteForceStep = 46;
        static final int TRANSACTION_ormsWriteFpsgo = 23;
        static final int TRANSACTION_ormsWriteHwmonHystOpt = 24;
        static final int TRANSACTION_ormsWriteInputBoostEnabled = 25;
        static final int TRANSACTION_ormsWriteInputBoostFreq = 26;
        static final int TRANSACTION_ormsWriteLlccDdrLatMax = 27;
        static final int TRANSACTION_ormsWriteLowPowerMode = 28;
        static final int TRANSACTION_ormsWriteMemlatL3Opt = 29;
        static final int TRANSACTION_ormsWriteNodeCommon = 45;
        static final int TRANSACTION_ormsWritePreferSilverEnabled = 30;
        static final int TRANSACTION_ormsWriteRulerEnable = 31;
        static final int TRANSACTION_ormsWriteSchedAsymcapBoost = 42;
        static final int TRANSACTION_ormsWriteSchedtuneColocate = 32;
        static final int TRANSACTION_ormsWriteSchedtunePreferIdle = 33;
        static final int TRANSACTION_ormsWriteSleepDisabled = 34;
        static final int TRANSACTION_ormsWriteSlideBoost = 35;
        static final int TRANSACTION_ormsWriteTargetLoads = 36;
        static final int TRANSACTION_ormsWriteTopCpuUclampMin = 37;
        static final int TRANSACTION_ormsWriteTouchBoost = 38;
        static final int TRANSACTION_ormsWriteTracingSetEvent = 39;
        static final int TRANSACTION_ormsWriteUclampLatencySensitive = 40;
        static final int TRANSACTION_ormsWritehalUfsPlusCtrl = 41;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "ormsBoostAcquire";
                case 2:
                    return "ormsBoostRelease";
                case 3:
                    return "ormsEnableCpuBouncing";
                case 4:
                    return "ormsReadDdrAvailFreq";
                case 5:
                    return "ormsReadFile";
                case 6:
                    return "ormsReadGpuFreq";
                case 7:
                    return "ormsWriteAboveHispeedDelay";
                case 8:
                    return "ormsWriteBgCpuUclampMin";
                case 9:
                    return "ormsWriteBusyDownThres";
                case 10:
                    return "ormsWriteBusyUpThres";
                case 11:
                    return "ormsWriteCameraTracingEvents";
                case 12:
                    return "ormsWriteCoreCtlEnable";
                case 13:
                    return "ormsWriteCpuBouncing";
                case 14:
                    return "ormsWriteCpuCoreNum";
                case 15:
                    return "ormsWriteCpuDdrLatfloorMax";
                case 16:
                    return "ormsWriteCpuDdrLatfloorMax2";
                case 17:
                    return "ormsWriteCpuDdrLatfloorMin";
                case 18:
                    return "ormsWriteCpuDdrLatfloorMin2";
                case 19:
                    return "ormsWriteCpuL3LatMax";
                case 20:
                    return "ormsWriteCpuLlccLatMax";
                case 21:
                    return "ormsWriteCpuOnline";
                case 22:
                    return "ormsWriteFgCpuUclampMin";
                case 23:
                    return "ormsWriteFpsgo";
                case 24:
                    return "ormsWriteHwmonHystOpt";
                case 25:
                    return "ormsWriteInputBoostEnabled";
                case 26:
                    return "ormsWriteInputBoostFreq";
                case 27:
                    return "ormsWriteLlccDdrLatMax";
                case 28:
                    return "ormsWriteLowPowerMode";
                case 29:
                    return "ormsWriteMemlatL3Opt";
                case 30:
                    return "ormsWritePreferSilverEnabled";
                case 31:
                    return "ormsWriteRulerEnable";
                case 32:
                    return "ormsWriteSchedtuneColocate";
                case 33:
                    return "ormsWriteSchedtunePreferIdle";
                case 34:
                    return "ormsWriteSleepDisabled";
                case 35:
                    return "ormsWriteSlideBoost";
                case 36:
                    return "ormsWriteTargetLoads";
                case 37:
                    return "ormsWriteTopCpuUclampMin";
                case 38:
                    return "ormsWriteTouchBoost";
                case 39:
                    return "ormsWriteTracingSetEvent";
                case 40:
                    return "ormsWriteUclampLatencySensitive";
                case 41:
                    return "ormsWritehalUfsPlusCtrl";
                case 42:
                    return "ormsWriteSchedAsymcapBoost";
                case 43:
                    return "ormsWriteCpuCpuDdrLatMin";
                case 44:
                    return "ormsWriteCpuCpuDdrBwMin";
                case 45:
                    return "ormsWriteNodeCommon";
                case 46:
                    return "ormsWriteForceStep";
                default:
                    switch (i) {
                        case TRANSACTION_getInterfaceHash /* 16777214 */:
                            return "getInterfaceHash";
                        case TRANSACTION_getInterfaceVersion /* 16777215 */:
                            return "getInterfaceVersion";
                        default:
                            return null;
                    }
            }
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return TRANSACTION_getInterfaceHash;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IOrmsAidlHalService.DESCRIPTOR);
        }

        public static IOrmsAidlHalService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOrmsAidlHalService.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IOrmsAidlHalService)) {
                return (IOrmsAidlHalService) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IOrmsAidlHalService.DESCRIPTOR;
            if (i >= 1 && i <= TRANSACTION_getInterfaceVersion) {
                parcel.enforceInterface(str);
            }
            switch (i) {
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    parcel2.writeNoException();
                    parcel2.writeString(getInterfaceHash());
                    return true;
                case TRANSACTION_getInterfaceVersion /* 16777215 */:
                    parcel2.writeNoException();
                    parcel2.writeInt(getInterfaceVersion());
                    return true;
                case 1598968902:
                    parcel2.writeString(str);
                    return true;
                default:
                    switch (i) {
                        case 1:
                            int readInt = parcel.readInt();
                            int[] createIntArray = parcel.createIntArray();
                            parcel.enforceNoDataAvail();
                            ormsBoostAcquire(readInt, createIntArray);
                            parcel2.writeNoException();
                            return true;
                        case 2:
                            int readInt2 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            ormsBoostRelease(readInt2);
                            parcel2.writeNoException();
                            return true;
                        case 3:
                            String readString = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsEnableCpuBouncing(readString);
                            parcel2.writeNoException();
                            return true;
                        case 4:
                            int readInt3 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            String ormsReadDdrAvailFreq = ormsReadDdrAvailFreq(readInt3);
                            parcel2.writeNoException();
                            parcel2.writeString(ormsReadDdrAvailFreq);
                            return true;
                        case 5:
                            String readString2 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            String ormsReadFile = ormsReadFile(readString2);
                            parcel2.writeNoException();
                            parcel2.writeString(ormsReadFile);
                            return true;
                        case 6:
                            int readInt4 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            String ormsReadGpuFreq = ormsReadGpuFreq(readInt4);
                            parcel2.writeNoException();
                            parcel2.writeString(ormsReadGpuFreq);
                            return true;
                        case 7:
                            int readInt5 = parcel.readInt();
                            int readInt6 = parcel.readInt();
                            String readString3 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteAboveHispeedDelay(readInt5, readInt6, readString3);
                            parcel2.writeNoException();
                            return true;
                        case 8:
                            String readString4 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteBgCpuUclampMin(readString4);
                            parcel2.writeNoException();
                            return true;
                        case 9:
                            String readString5 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteBusyDownThres(readString5);
                            parcel2.writeNoException();
                            return true;
                        case 10:
                            String readString6 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteBusyUpThres(readString6);
                            parcel2.writeNoException();
                            return true;
                        case 11:
                            String readString7 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCameraTracingEvents(readString7);
                            parcel2.writeNoException();
                            return true;
                        case 12:
                            String readString8 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCoreCtlEnable(readString8);
                            parcel2.writeNoException();
                            return true;
                        case 13:
                            String readString9 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuBouncing(readString9);
                            parcel2.writeNoException();
                            return true;
                        case 14:
                            int readInt7 = parcel.readInt();
                            int readInt8 = parcel.readInt();
                            int readInt9 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuCoreNum(readInt7, readInt8, readInt9);
                            parcel2.writeNoException();
                            return true;
                        case 15:
                            int readInt10 = parcel.readInt();
                            String readString10 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuDdrLatfloorMax(readInt10, readString10);
                            parcel2.writeNoException();
                            return true;
                        case 16:
                            int readInt11 = parcel.readInt();
                            String readString11 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuDdrLatfloorMax2(readInt11, readString11);
                            parcel2.writeNoException();
                            return true;
                        case 17:
                            int readInt12 = parcel.readInt();
                            String readString12 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuDdrLatfloorMin(readInt12, readString12);
                            parcel2.writeNoException();
                            return true;
                        case 18:
                            int readInt13 = parcel.readInt();
                            String readString13 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuDdrLatfloorMin2(readInt13, readString13);
                            parcel2.writeNoException();
                            return true;
                        case 19:
                            int readInt14 = parcel.readInt();
                            String readString14 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuL3LatMax(readInt14, readString14);
                            parcel2.writeNoException();
                            return true;
                        case 20:
                            int readInt15 = parcel.readInt();
                            String readString15 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuLlccLatMax(readInt15, readString15);
                            parcel2.writeNoException();
                            return true;
                        case 21:
                            int readInt16 = parcel.readInt();
                            int readInt17 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuOnline(readInt16, readInt17);
                            parcel2.writeNoException();
                            return true;
                        case 22:
                            String readString16 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteFgCpuUclampMin(readString16);
                            parcel2.writeNoException();
                            return true;
                        case 23:
                            String readString17 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteFpsgo(readString17);
                            parcel2.writeNoException();
                            return true;
                        case 24:
                            String readString18 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteHwmonHystOpt(readString18);
                            parcel2.writeNoException();
                            return true;
                        case 25:
                            String readString19 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteInputBoostEnabled(readString19);
                            parcel2.writeNoException();
                            return true;
                        case 26:
                            String readString20 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteInputBoostFreq(readString20);
                            parcel2.writeNoException();
                            return true;
                        case 27:
                            int readInt18 = parcel.readInt();
                            String readString21 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteLlccDdrLatMax(readInt18, readString21);
                            parcel2.writeNoException();
                            return true;
                        case 28:
                            String readString22 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteLowPowerMode(readString22);
                            parcel2.writeNoException();
                            return true;
                        case 29:
                            String readString23 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteMemlatL3Opt(readString23);
                            parcel2.writeNoException();
                            return true;
                        case 30:
                            String readString24 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWritePreferSilverEnabled(readString24);
                            parcel2.writeNoException();
                            return true;
                        case 31:
                            String readString25 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteRulerEnable(readString25);
                            parcel2.writeNoException();
                            return true;
                        case 32:
                            String readString26 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteSchedtuneColocate(readString26);
                            parcel2.writeNoException();
                            return true;
                        case 33:
                            String readString27 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteSchedtunePreferIdle(readString27);
                            parcel2.writeNoException();
                            return true;
                        case 34:
                            String readString28 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteSleepDisabled(readString28);
                            parcel2.writeNoException();
                            return true;
                        case 35:
                            String readString29 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteSlideBoost(readString29);
                            parcel2.writeNoException();
                            return true;
                        case 36:
                            int readInt19 = parcel.readInt();
                            int readInt20 = parcel.readInt();
                            String readString30 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteTargetLoads(readInt19, readInt20, readString30);
                            parcel2.writeNoException();
                            return true;
                        case 37:
                            String readString31 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteTopCpuUclampMin(readString31);
                            parcel2.writeNoException();
                            return true;
                        case 38:
                            String readString32 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteTouchBoost(readString32);
                            parcel2.writeNoException();
                            return true;
                        case 39:
                            String readString33 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteTracingSetEvent(readString33);
                            parcel2.writeNoException();
                            return true;
                        case 40:
                            String readString34 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteUclampLatencySensitive(readString34);
                            parcel2.writeNoException();
                            return true;
                        case 41:
                            String readString35 = parcel.readString();
                            String readString36 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWritehalUfsPlusCtrl(readString35, readString36);
                            parcel2.writeNoException();
                            return true;
                        case 42:
                            String readString37 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteSchedAsymcapBoost(readString37);
                            parcel2.writeNoException();
                            return true;
                        case 43:
                            int readInt21 = parcel.readInt();
                            String readString38 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuCpuDdrLatMin(readInt21, readString38);
                            parcel2.writeNoException();
                            return true;
                        case 44:
                            String readString39 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteCpuCpuDdrBwMin(readString39);
                            parcel2.writeNoException();
                            return true;
                        case 45:
                            int readInt22 = parcel.readInt();
                            int readInt23 = parcel.readInt();
                            String readString40 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteNodeCommon(readInt22, readInt23, readString40);
                            parcel2.writeNoException();
                            return true;
                        case 46:
                            String readString41 = parcel.readString();
                            parcel.enforceNoDataAvail();
                            ormsWriteForceStep(readString41);
                            parcel2.writeNoException();
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements IOrmsAidlHalService {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOrmsAidlHalService.DESCRIPTOR;
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsBoostAcquire(int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsBoostAcquire is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsBoostRelease(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsBoostRelease is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsEnableCpuBouncing(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsEnableCpuBouncing is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public String ormsReadDdrAvailFreq(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsReadDdrAvailFreq is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public String ormsReadFile(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsReadFile is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public String ormsReadGpuFreq(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsReadGpuFreq is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteAboveHispeedDelay(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteAboveHispeedDelay is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteBgCpuUclampMin(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteBgCpuUclampMin is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteBusyDownThres(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteBusyDownThres is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteBusyUpThres(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(10, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteBusyUpThres is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCameraTracingEvents(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(11, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCameraTracingEvents is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCoreCtlEnable(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(12, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCoreCtlEnable is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuBouncing(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(13, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuBouncing is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuCoreNum(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (!this.mRemote.transact(14, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuCoreNum is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuDdrLatfloorMax(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(15, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuDdrLatfloorMax is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuDdrLatfloorMax2(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(16, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuDdrLatfloorMax2 is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuDdrLatfloorMin(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(17, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuDdrLatfloorMin is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuDdrLatfloorMin2(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(18, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuDdrLatfloorMin2 is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuL3LatMax(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(19, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuL3LatMax is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuLlccLatMax(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(20, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuLlccLatMax is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuOnline(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(21, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuOnline is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteFgCpuUclampMin(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(22, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteFgCpuUclampMin is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteFpsgo(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(23, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteFpsgo is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteHwmonHystOpt(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(24, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteHwmonHystOpt is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteInputBoostEnabled(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(25, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteInputBoostEnabled is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteInputBoostFreq(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(26, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteInputBoostFreq is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteLlccDdrLatMax(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(27, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteLlccDdrLatMax is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteLowPowerMode(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(28, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteLowPowerMode is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteMemlatL3Opt(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(29, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteMemlatL3Opt is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWritePreferSilverEnabled(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(30, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWritePreferSilverEnabled is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteRulerEnable(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(31, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteRulerEnable is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteSchedtuneColocate(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(32, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteSchedtuneColocate is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteSchedtunePreferIdle(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(33, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteSchedtunePreferIdle is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteSleepDisabled(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(34, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteSleepDisabled is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteSlideBoost(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(35, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteSlideBoost is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteTargetLoads(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(36, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteTargetLoads is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteTopCpuUclampMin(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(37, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteTopCpuUclampMin is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteTouchBoost(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(38, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteTouchBoost is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteTracingSetEvent(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(39, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteTracingSetEvent is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteUclampLatencySensitive(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(40, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteUclampLatencySensitive is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWritehalUfsPlusCtrl(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (!this.mRemote.transact(41, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWritehalUfsPlusCtrl is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteSchedAsymcapBoost(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(42, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteSchedAsymcapBoost is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuCpuDdrLatMin(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(43, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuCpuDdrLatMin is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuCpuDdrBwMin(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(44, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteCpuCpuDdrBwMin is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteNodeCommon(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(45, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteNodeCommon is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteForceStep(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(46, obtain, obtain2, 0)) {
                        throw new RemoteException("Method ormsWriteForceStep is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceVersion, obtain, obtain2, 0);
                        obtain2.readException();
                        this.mCachedVersion = obtain2.readInt();
                    } finally {
                        obtain2.recycle();
                        obtain.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IOrmsAidlHalService.DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceHash, obtain, obtain2, 0);
                        obtain2.readException();
                        this.mCachedHash = obtain2.readString();
                        obtain2.recycle();
                        obtain.recycle();
                    } catch (Throwable th) {
                        obtain2.recycle();
                        obtain.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }
    }
}
