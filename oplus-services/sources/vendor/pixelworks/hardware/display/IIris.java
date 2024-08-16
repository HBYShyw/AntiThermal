package vendor.pixelworks.hardware.display;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import vendor.pixelworks.hardware.display.IIrisCallback;
import vendor.pixelworks.hardware.display.ISoftIrisClient;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IIris extends IInterface {
    public static final String DESCRIPTOR = "vendor$pixelworks$hardware$display$IIris".replace('$', '.');
    public static final String HASH = "02c8c5526cbde39f502b3bf8cccaf196c81de25f";
    public static final int VERSION = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements IIris {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void buildLayerStack(long j, LayerStack layerStack) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void changeLayerType(long j, long j2) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int commitLayerStack(long j, int i) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void configureIrisHdrMode(int i) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int configureIrisMaxcll(int i) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void createLayer(long j, long j2) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void destroyLayer(long j, long j2) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void enableSecondaryDisplay(boolean z) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public IrisFixedConfig getCurrentConfig(long j) throws RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public String getDumpString(long j) throws RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public String getInterfaceHash() {
            return "";
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public LutData getLayerToneMappingLut(long j, int i) throws RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int getOsdStatus(int i) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int handleDisplayEvent(long j, int i, int i2) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void initialize(DisplayConfigVariableInfo displayConfigVariableInfo) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public String irisConfigureBatch(int i, String str) throws RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int irisConfigureBuffer(int i, long j, ParcelFileDescriptor parcelFileDescriptor, int i2) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int[] irisConfigureGet(int i, int[] iArr) throws RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int irisConfigureSet(int i, int[] iArr) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public byte[] panelReadWrite(boolean z, int i, int i2, boolean z2, byte[] bArr, int i3) throws RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int present(long j) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int presentDisplay(long j) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void registerCallback(long j, IIrisCallback iIrisCallback) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void registerSoftIrisClient(long j, ISoftIrisClient iSoftIrisClient) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void reportDualChannelStatus(int i) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setActiveConfig(long j, DisplayConfigVariableInfo displayConfigVariableInfo) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setClientTarget(long j, int i) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int setColorModeWithRenderIntent(long j, int i, int i2) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int setColorTransform(float[] fArr) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setDisplayConnected(long j, boolean z) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerBuffer(long j, long j2, BufferInfo bufferInfo) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerCompositionType(long j, long j2, int i) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerDisplayFrame(long j, long j2, HwcRect hwcRect) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerProperty(long j, int i, long j2) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerSetEmpty(long j, boolean z) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerSourceCrop(long j, long j2, HwcRect hwcRect) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerTransform(long j, long j2, int i) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerZOrder(long j, long j2, int i) throws RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int setOsdAutoRefresh(int i) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int setPowerMode(long j, int i, boolean z, boolean z2) throws RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int[] updateDisplayBrightness(long j, int i, int[] iArr) throws RemoteException {
            return null;
        }
    }

    void buildLayerStack(long j, LayerStack layerStack) throws RemoteException;

    void changeLayerType(long j, long j2) throws RemoteException;

    int commitLayerStack(long j, int i) throws RemoteException;

    void configureIrisHdrMode(int i) throws RemoteException;

    int configureIrisMaxcll(int i) throws RemoteException;

    void createLayer(long j, long j2) throws RemoteException;

    void destroyLayer(long j, long j2) throws RemoteException;

    void enableSecondaryDisplay(boolean z) throws RemoteException;

    IrisFixedConfig getCurrentConfig(long j) throws RemoteException;

    String getDumpString(long j) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    LutData getLayerToneMappingLut(long j, int i) throws RemoteException;

    int getOsdStatus(int i) throws RemoteException;

    int handleDisplayEvent(long j, int i, int i2) throws RemoteException;

    void initialize(DisplayConfigVariableInfo displayConfigVariableInfo) throws RemoteException;

    String irisConfigureBatch(int i, String str) throws RemoteException;

    int irisConfigureBuffer(int i, long j, ParcelFileDescriptor parcelFileDescriptor, int i2) throws RemoteException;

    int[] irisConfigureGet(int i, int[] iArr) throws RemoteException;

    int irisConfigureSet(int i, int[] iArr) throws RemoteException;

    byte[] panelReadWrite(boolean z, int i, int i2, boolean z2, byte[] bArr, int i3) throws RemoteException;

    int present(long j) throws RemoteException;

    int presentDisplay(long j) throws RemoteException;

    void registerCallback(long j, IIrisCallback iIrisCallback) throws RemoteException;

    void registerSoftIrisClient(long j, ISoftIrisClient iSoftIrisClient) throws RemoteException;

    void reportDualChannelStatus(int i) throws RemoteException;

    void setActiveConfig(long j, DisplayConfigVariableInfo displayConfigVariableInfo) throws RemoteException;

    void setClientTarget(long j, int i) throws RemoteException;

    int setColorModeWithRenderIntent(long j, int i, int i2) throws RemoteException;

    int setColorTransform(float[] fArr) throws RemoteException;

    void setDisplayConnected(long j, boolean z) throws RemoteException;

    void setLayerBuffer(long j, long j2, BufferInfo bufferInfo) throws RemoteException;

    void setLayerCompositionType(long j, long j2, int i) throws RemoteException;

    void setLayerDisplayFrame(long j, long j2, HwcRect hwcRect) throws RemoteException;

    void setLayerProperty(long j, int i, long j2) throws RemoteException;

    void setLayerSetEmpty(long j, boolean z) throws RemoteException;

    void setLayerSourceCrop(long j, long j2, HwcRect hwcRect) throws RemoteException;

    void setLayerTransform(long j, long j2, int i) throws RemoteException;

    void setLayerZOrder(long j, long j2, int i) throws RemoteException;

    int setOsdAutoRefresh(int i) throws RemoteException;

    int setPowerMode(long j, int i, boolean z, boolean z2) throws RemoteException;

    int[] updateDisplayBrightness(long j, int i, int[] iArr) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements IIris {
        static final int TRANSACTION_buildLayerStack = 1;
        static final int TRANSACTION_changeLayerType = 2;
        static final int TRANSACTION_commitLayerStack = 3;
        static final int TRANSACTION_configureIrisHdrMode = 40;
        static final int TRANSACTION_configureIrisMaxcll = 4;
        static final int TRANSACTION_createLayer = 5;
        static final int TRANSACTION_destroyLayer = 6;
        static final int TRANSACTION_enableSecondaryDisplay = 7;
        static final int TRANSACTION_getCurrentConfig = 8;
        static final int TRANSACTION_getDumpString = 9;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getLayerToneMappingLut = 10;
        static final int TRANSACTION_getOsdStatus = 11;
        static final int TRANSACTION_handleDisplayEvent = 12;
        static final int TRANSACTION_initialize = 13;
        static final int TRANSACTION_irisConfigureBatch = 14;
        static final int TRANSACTION_irisConfigureBuffer = 15;
        static final int TRANSACTION_irisConfigureGet = 16;
        static final int TRANSACTION_irisConfigureSet = 17;
        static final int TRANSACTION_panelReadWrite = 18;
        static final int TRANSACTION_present = 19;
        static final int TRANSACTION_presentDisplay = 20;
        static final int TRANSACTION_registerCallback = 21;
        static final int TRANSACTION_registerSoftIrisClient = 22;
        static final int TRANSACTION_reportDualChannelStatus = 23;
        static final int TRANSACTION_setActiveConfig = 24;
        static final int TRANSACTION_setClientTarget = 25;
        static final int TRANSACTION_setColorModeWithRenderIntent = 26;
        static final int TRANSACTION_setColorTransform = 39;
        static final int TRANSACTION_setDisplayConnected = 27;
        static final int TRANSACTION_setLayerBuffer = 28;
        static final int TRANSACTION_setLayerCompositionType = 29;
        static final int TRANSACTION_setLayerDisplayFrame = 30;
        static final int TRANSACTION_setLayerProperty = 31;
        static final int TRANSACTION_setLayerSetEmpty = 32;
        static final int TRANSACTION_setLayerSourceCrop = 33;
        static final int TRANSACTION_setLayerTransform = 34;
        static final int TRANSACTION_setLayerZOrder = 35;
        static final int TRANSACTION_setOsdAutoRefresh = 36;
        static final int TRANSACTION_setPowerMode = 37;
        static final int TRANSACTION_updateDisplayBrightness = 38;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "buildLayerStack";
                case 2:
                    return "changeLayerType";
                case 3:
                    return "commitLayerStack";
                case 4:
                    return "configureIrisMaxcll";
                case 5:
                    return "createLayer";
                case 6:
                    return "destroyLayer";
                case 7:
                    return "enableSecondaryDisplay";
                case 8:
                    return "getCurrentConfig";
                case 9:
                    return "getDumpString";
                case 10:
                    return "getLayerToneMappingLut";
                case 11:
                    return "getOsdStatus";
                case 12:
                    return "handleDisplayEvent";
                case 13:
                    return "initialize";
                case 14:
                    return "irisConfigureBatch";
                case 15:
                    return "irisConfigureBuffer";
                case 16:
                    return "irisConfigureGet";
                case 17:
                    return "irisConfigureSet";
                case 18:
                    return "panelReadWrite";
                case 19:
                    return "present";
                case 20:
                    return "presentDisplay";
                case 21:
                    return "registerCallback";
                case 22:
                    return "registerSoftIrisClient";
                case 23:
                    return "reportDualChannelStatus";
                case 24:
                    return "setActiveConfig";
                case 25:
                    return "setClientTarget";
                case 26:
                    return "setColorModeWithRenderIntent";
                case 27:
                    return "setDisplayConnected";
                case 28:
                    return "setLayerBuffer";
                case 29:
                    return "setLayerCompositionType";
                case 30:
                    return "setLayerDisplayFrame";
                case 31:
                    return "setLayerProperty";
                case 32:
                    return "setLayerSetEmpty";
                case 33:
                    return "setLayerSourceCrop";
                case 34:
                    return "setLayerTransform";
                case 35:
                    return "setLayerZOrder";
                case 36:
                    return "setOsdAutoRefresh";
                case 37:
                    return "setPowerMode";
                case 38:
                    return "updateDisplayBrightness";
                case 39:
                    return "setColorTransform";
                case 40:
                    return "configureIrisHdrMode";
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
            attachInterface(this, IIris.DESCRIPTOR);
        }

        public static IIris asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IIris.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IIris)) {
                return (IIris) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = IIris.DESCRIPTOR;
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
                            long readLong = parcel.readLong();
                            LayerStack layerStack = (LayerStack) parcel.readTypedObject(LayerStack.CREATOR);
                            parcel.enforceNoDataAvail();
                            buildLayerStack(readLong, layerStack);
                            parcel2.writeNoException();
                            return true;
                        case 2:
                            long readLong2 = parcel.readLong();
                            long readLong3 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            changeLayerType(readLong2, readLong3);
                            parcel2.writeNoException();
                            return true;
                        case 3:
                            long readLong4 = parcel.readLong();
                            int readInt = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int commitLayerStack = commitLayerStack(readLong4, readInt);
                            parcel2.writeNoException();
                            parcel2.writeInt(commitLayerStack);
                            return true;
                        case 4:
                            int readInt2 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int configureIrisMaxcll = configureIrisMaxcll(readInt2);
                            parcel2.writeNoException();
                            parcel2.writeInt(configureIrisMaxcll);
                            return true;
                        case 5:
                            long readLong5 = parcel.readLong();
                            long readLong6 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            createLayer(readLong5, readLong6);
                            parcel2.writeNoException();
                            return true;
                        case 6:
                            long readLong7 = parcel.readLong();
                            long readLong8 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            destroyLayer(readLong7, readLong8);
                            parcel2.writeNoException();
                            return true;
                        case 7:
                            boolean readBoolean = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            enableSecondaryDisplay(readBoolean);
                            parcel2.writeNoException();
                            return true;
                        case 8:
                            long readLong9 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            IrisFixedConfig currentConfig = getCurrentConfig(readLong9);
                            parcel2.writeNoException();
                            parcel2.writeTypedObject(currentConfig, 1);
                            return true;
                        case 9:
                            long readLong10 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            String dumpString = getDumpString(readLong10);
                            parcel2.writeNoException();
                            parcel2.writeString(dumpString);
                            return true;
                        case 10:
                            long readLong11 = parcel.readLong();
                            int readInt3 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            LutData layerToneMappingLut = getLayerToneMappingLut(readLong11, readInt3);
                            parcel2.writeNoException();
                            parcel2.writeTypedObject(layerToneMappingLut, 1);
                            return true;
                        case 11:
                            int readInt4 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int osdStatus = getOsdStatus(readInt4);
                            parcel2.writeNoException();
                            parcel2.writeInt(osdStatus);
                            return true;
                        case 12:
                            long readLong12 = parcel.readLong();
                            int readInt5 = parcel.readInt();
                            int readInt6 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int handleDisplayEvent = handleDisplayEvent(readLong12, readInt5, readInt6);
                            parcel2.writeNoException();
                            parcel2.writeInt(handleDisplayEvent);
                            return true;
                        case 13:
                            DisplayConfigVariableInfo displayConfigVariableInfo = (DisplayConfigVariableInfo) parcel.readTypedObject(DisplayConfigVariableInfo.CREATOR);
                            parcel.enforceNoDataAvail();
                            initialize(displayConfigVariableInfo);
                            parcel2.writeNoException();
                            return true;
                        case 14:
                            int readInt7 = parcel.readInt();
                            String readString = parcel.readString();
                            parcel.enforceNoDataAvail();
                            String irisConfigureBatch = irisConfigureBatch(readInt7, readString);
                            parcel2.writeNoException();
                            parcel2.writeString(irisConfigureBatch);
                            return true;
                        case 15:
                            int readInt8 = parcel.readInt();
                            long readLong13 = parcel.readLong();
                            ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) parcel.readTypedObject(ParcelFileDescriptor.CREATOR);
                            int readInt9 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int irisConfigureBuffer = irisConfigureBuffer(readInt8, readLong13, parcelFileDescriptor, readInt9);
                            parcel2.writeNoException();
                            parcel2.writeInt(irisConfigureBuffer);
                            return true;
                        case 16:
                            int readInt10 = parcel.readInt();
                            int[] createIntArray = parcel.createIntArray();
                            parcel.enforceNoDataAvail();
                            int[] irisConfigureGet = irisConfigureGet(readInt10, createIntArray);
                            parcel2.writeNoException();
                            parcel2.writeIntArray(irisConfigureGet);
                            return true;
                        case 17:
                            int readInt11 = parcel.readInt();
                            int[] createIntArray2 = parcel.createIntArray();
                            parcel.enforceNoDataAvail();
                            int irisConfigureSet = irisConfigureSet(readInt11, createIntArray2);
                            parcel2.writeNoException();
                            parcel2.writeInt(irisConfigureSet);
                            return true;
                        case 18:
                            boolean readBoolean2 = parcel.readBoolean();
                            int readInt12 = parcel.readInt();
                            int readInt13 = parcel.readInt();
                            boolean readBoolean3 = parcel.readBoolean();
                            byte[] createByteArray = parcel.createByteArray();
                            int readInt14 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            byte[] panelReadWrite = panelReadWrite(readBoolean2, readInt12, readInt13, readBoolean3, createByteArray, readInt14);
                            parcel2.writeNoException();
                            parcel2.writeByteArray(panelReadWrite);
                            return true;
                        case 19:
                            long readLong14 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            int present = present(readLong14);
                            parcel2.writeNoException();
                            parcel2.writeInt(present);
                            return true;
                        case 20:
                            long readLong15 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            int presentDisplay = presentDisplay(readLong15);
                            parcel2.writeNoException();
                            parcel2.writeInt(presentDisplay);
                            return true;
                        case 21:
                            long readLong16 = parcel.readLong();
                            IIrisCallback asInterface = IIrisCallback.Stub.asInterface(parcel.readStrongBinder());
                            parcel.enforceNoDataAvail();
                            registerCallback(readLong16, asInterface);
                            parcel2.writeNoException();
                            return true;
                        case 22:
                            long readLong17 = parcel.readLong();
                            ISoftIrisClient asInterface2 = ISoftIrisClient.Stub.asInterface(parcel.readStrongBinder());
                            parcel.enforceNoDataAvail();
                            registerSoftIrisClient(readLong17, asInterface2);
                            parcel2.writeNoException();
                            return true;
                        case 23:
                            int readInt15 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            reportDualChannelStatus(readInt15);
                            parcel2.writeNoException();
                            return true;
                        case 24:
                            long readLong18 = parcel.readLong();
                            DisplayConfigVariableInfo displayConfigVariableInfo2 = (DisplayConfigVariableInfo) parcel.readTypedObject(DisplayConfigVariableInfo.CREATOR);
                            parcel.enforceNoDataAvail();
                            setActiveConfig(readLong18, displayConfigVariableInfo2);
                            parcel2.writeNoException();
                            return true;
                        case 25:
                            long readLong19 = parcel.readLong();
                            int readInt16 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            setClientTarget(readLong19, readInt16);
                            parcel2.writeNoException();
                            return true;
                        case 26:
                            long readLong20 = parcel.readLong();
                            int readInt17 = parcel.readInt();
                            int readInt18 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int colorModeWithRenderIntent = setColorModeWithRenderIntent(readLong20, readInt17, readInt18);
                            parcel2.writeNoException();
                            parcel2.writeInt(colorModeWithRenderIntent);
                            return true;
                        case 27:
                            long readLong21 = parcel.readLong();
                            boolean readBoolean4 = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            setDisplayConnected(readLong21, readBoolean4);
                            parcel2.writeNoException();
                            return true;
                        case 28:
                            long readLong22 = parcel.readLong();
                            long readLong23 = parcel.readLong();
                            BufferInfo bufferInfo = (BufferInfo) parcel.readTypedObject(BufferInfo.CREATOR);
                            parcel.enforceNoDataAvail();
                            setLayerBuffer(readLong22, readLong23, bufferInfo);
                            parcel2.writeNoException();
                            return true;
                        case 29:
                            long readLong24 = parcel.readLong();
                            long readLong25 = parcel.readLong();
                            int readInt19 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            setLayerCompositionType(readLong24, readLong25, readInt19);
                            parcel2.writeNoException();
                            return true;
                        case 30:
                            long readLong26 = parcel.readLong();
                            long readLong27 = parcel.readLong();
                            HwcRect hwcRect = (HwcRect) parcel.readTypedObject(HwcRect.CREATOR);
                            parcel.enforceNoDataAvail();
                            setLayerDisplayFrame(readLong26, readLong27, hwcRect);
                            parcel2.writeNoException();
                            return true;
                        case 31:
                            long readLong28 = parcel.readLong();
                            int readInt20 = parcel.readInt();
                            long readLong29 = parcel.readLong();
                            parcel.enforceNoDataAvail();
                            setLayerProperty(readLong28, readInt20, readLong29);
                            parcel2.writeNoException();
                            return true;
                        case 32:
                            long readLong30 = parcel.readLong();
                            boolean readBoolean5 = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            setLayerSetEmpty(readLong30, readBoolean5);
                            parcel2.writeNoException();
                            return true;
                        case 33:
                            long readLong31 = parcel.readLong();
                            long readLong32 = parcel.readLong();
                            HwcRect hwcRect2 = (HwcRect) parcel.readTypedObject(HwcRect.CREATOR);
                            parcel.enforceNoDataAvail();
                            setLayerSourceCrop(readLong31, readLong32, hwcRect2);
                            parcel2.writeNoException();
                            return true;
                        case 34:
                            long readLong33 = parcel.readLong();
                            long readLong34 = parcel.readLong();
                            int readInt21 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            setLayerTransform(readLong33, readLong34, readInt21);
                            parcel2.writeNoException();
                            return true;
                        case 35:
                            long readLong35 = parcel.readLong();
                            long readLong36 = parcel.readLong();
                            int readInt22 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            setLayerZOrder(readLong35, readLong36, readInt22);
                            parcel2.writeNoException();
                            return true;
                        case 36:
                            int readInt23 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            int osdAutoRefresh = setOsdAutoRefresh(readInt23);
                            parcel2.writeNoException();
                            parcel2.writeInt(osdAutoRefresh);
                            return true;
                        case 37:
                            long readLong37 = parcel.readLong();
                            int readInt24 = parcel.readInt();
                            boolean readBoolean6 = parcel.readBoolean();
                            boolean readBoolean7 = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            int powerMode = setPowerMode(readLong37, readInt24, readBoolean6, readBoolean7);
                            parcel2.writeNoException();
                            parcel2.writeInt(powerMode);
                            return true;
                        case 38:
                            long readLong38 = parcel.readLong();
                            int readInt25 = parcel.readInt();
                            int[] createIntArray3 = parcel.createIntArray();
                            parcel.enforceNoDataAvail();
                            int[] updateDisplayBrightness = updateDisplayBrightness(readLong38, readInt25, createIntArray3);
                            parcel2.writeNoException();
                            parcel2.writeIntArray(updateDisplayBrightness);
                            return true;
                        case 39:
                            float[] createFloatArray = parcel.createFloatArray();
                            parcel.enforceNoDataAvail();
                            int colorTransform = setColorTransform(createFloatArray);
                            parcel2.writeNoException();
                            parcel2.writeInt(colorTransform);
                            return true;
                        case 40:
                            int readInt26 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            configureIrisHdrMode(readInt26);
                            parcel2.writeNoException();
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements IIris {
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
                return IIris.DESCRIPTOR;
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void buildLayerStack(long j, LayerStack layerStack) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeTypedObject(layerStack, 0);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0)) {
                        throw new RemoteException("Method buildLayerStack is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void changeLayerType(long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0)) {
                        throw new RemoteException("Method changeLayerType is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int commitLayerStack(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0)) {
                        throw new RemoteException("Method commitLayerStack is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int configureIrisMaxcll(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0)) {
                        throw new RemoteException("Method configureIrisMaxcll is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void createLayer(long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0)) {
                        throw new RemoteException("Method createLayer is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void destroyLayer(long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0)) {
                        throw new RemoteException("Method destroyLayer is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void enableSecondaryDisplay(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0)) {
                        throw new RemoteException("Method enableSecondaryDisplay is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public IrisFixedConfig getCurrentConfig(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getCurrentConfig is unimplemented.");
                    }
                    obtain2.readException();
                    return (IrisFixedConfig) obtain2.readTypedObject(IrisFixedConfig.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public String getDumpString(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getDumpString is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public LutData getLayerToneMappingLut(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(10, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getLayerToneMappingLut is unimplemented.");
                    }
                    obtain2.readException();
                    return (LutData) obtain2.readTypedObject(LutData.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int getOsdStatus(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(11, obtain, obtain2, 0)) {
                        throw new RemoteException("Method getOsdStatus is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int handleDisplayEvent(long j, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(12, obtain, obtain2, 0)) {
                        throw new RemoteException("Method handleDisplayEvent is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void initialize(DisplayConfigVariableInfo displayConfigVariableInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeTypedObject(displayConfigVariableInfo, 0);
                    if (!this.mRemote.transact(13, obtain, obtain2, 0)) {
                        throw new RemoteException("Method initialize is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public String irisConfigureBatch(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(14, obtain, obtain2, 0)) {
                        throw new RemoteException("Method irisConfigureBatch is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int irisConfigureBuffer(int i, long j, ParcelFileDescriptor parcelFileDescriptor, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    obtain.writeTypedObject(parcelFileDescriptor, 0);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(15, obtain, obtain2, 0)) {
                        throw new RemoteException("Method irisConfigureBuffer is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int[] irisConfigureGet(int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(16, obtain, obtain2, 0)) {
                        throw new RemoteException("Method irisConfigureGet is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.createIntArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int irisConfigureSet(int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(17, obtain, obtain2, 0)) {
                        throw new RemoteException("Method irisConfigureSet is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public byte[] panelReadWrite(boolean z, int i, int i2, boolean z2, byte[] bArr, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeBoolean(z2);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i3);
                    if (!this.mRemote.transact(18, obtain, obtain2, 0)) {
                        throw new RemoteException("Method panelReadWrite is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int present(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    if (!this.mRemote.transact(19, obtain, obtain2, 0)) {
                        throw new RemoteException("Method present is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int presentDisplay(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    if (!this.mRemote.transact(20, obtain, obtain2, 0)) {
                        throw new RemoteException("Method presentDisplay is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void registerCallback(long j, IIrisCallback iIrisCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeStrongInterface(iIrisCallback);
                    if (!this.mRemote.transact(21, obtain, obtain2, 0)) {
                        throw new RemoteException("Method registerCallback is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void registerSoftIrisClient(long j, ISoftIrisClient iSoftIrisClient) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeStrongInterface(iSoftIrisClient);
                    if (!this.mRemote.transact(22, obtain, obtain2, 0)) {
                        throw new RemoteException("Method registerSoftIrisClient is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void reportDualChannelStatus(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(23, obtain, obtain2, 0)) {
                        throw new RemoteException("Method reportDualChannelStatus is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setActiveConfig(long j, DisplayConfigVariableInfo displayConfigVariableInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeTypedObject(displayConfigVariableInfo, 0);
                    if (!this.mRemote.transact(24, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setActiveConfig is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setClientTarget(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(25, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setClientTarget is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int setColorModeWithRenderIntent(long j, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(26, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setColorModeWithRenderIntent is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setDisplayConnected(long j, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeBoolean(z);
                    if (!this.mRemote.transact(27, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setDisplayConnected is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerBuffer(long j, long j2, BufferInfo bufferInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeTypedObject(bufferInfo, 0);
                    if (!this.mRemote.transact(28, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setLayerBuffer is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerCompositionType(long j, long j2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(29, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setLayerCompositionType is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerDisplayFrame(long j, long j2, HwcRect hwcRect) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeTypedObject(hwcRect, 0);
                    if (!this.mRemote.transact(30, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setLayerDisplayFrame is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerProperty(long j, int i, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeLong(j2);
                    if (!this.mRemote.transact(31, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setLayerProperty is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerSetEmpty(long j, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeBoolean(z);
                    if (!this.mRemote.transact(32, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setLayerSetEmpty is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerSourceCrop(long j, long j2, HwcRect hwcRect) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeTypedObject(hwcRect, 0);
                    if (!this.mRemote.transact(33, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setLayerSourceCrop is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerTransform(long j, long j2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(34, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setLayerTransform is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerZOrder(long j, long j2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(35, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setLayerZOrder is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int setOsdAutoRefresh(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(36, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setOsdAutoRefresh is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int setPowerMode(long j, int i, boolean z, boolean z2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    obtain.writeBoolean(z2);
                    if (!this.mRemote.transact(37, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setPowerMode is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int[] updateDisplayBrightness(long j, int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(38, obtain, obtain2, 0)) {
                        throw new RemoteException("Method updateDisplayBrightness is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.createIntArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int setColorTransform(float[] fArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeFloatArray(fArr);
                    if (!this.mRemote.transact(39, obtain, obtain2, 0)) {
                        throw new RemoteException("Method setColorTransform is unimplemented.");
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void configureIrisHdrMode(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIris.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(40, obtain, obtain2, 0)) {
                        throw new RemoteException("Method configureIrisHdrMode is unimplemented.");
                    }
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IIris.DESCRIPTOR);
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

            @Override // vendor.pixelworks.hardware.display.IIris
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain(asBinder());
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(IIris.DESCRIPTOR);
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
