package com.android.server.midi;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.XmlResourceParser;
import android.media.MediaMetrics;
import android.media.midi.IBluetoothMidiService;
import android.media.midi.IMidiDeviceListener;
import android.media.midi.IMidiDeviceOpenCallback;
import android.media.midi.IMidiDeviceServer;
import android.media.midi.IMidiManager;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiDeviceStatus;
import android.media.midi.MidiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.EventLog;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.content.PackageMonitor;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.SystemService;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class MidiService extends IMidiManager.Stub {
    private static final int MAX_CONNECTIONS_PER_CLIENT = 64;
    private static final int MAX_DEVICE_SERVERS_PER_UID = 16;
    private static final int MAX_LISTENERS_PER_CLIENT = 16;
    private static final String MIDI_LEGACY_STRING = "MIDI 1.0";
    private static final String MIDI_UNIVERSAL_STRING = "MIDI 2.0";
    private static final String TAG = "MidiService";
    private final BroadcastReceiver mBleMidiReceiver;
    private int mBluetoothServiceUid;
    private final Context mContext;
    private final HashSet<ParcelUuid> mNonMidiUUIDs;
    private final PackageManager mPackageManager;
    private final PackageMonitor mPackageMonitor;
    private static final UUID MIDI_SERVICE = UUID.fromString("03B80E5A-EDE8-4B33-A751-6CE34EC4C700");
    private static final MidiDeviceInfo[] EMPTY_DEVICE_INFO_ARRAY = new MidiDeviceInfo[0];
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private final HashMap<IBinder, Client> mClients = new HashMap<>();
    private final HashMap<MidiDeviceInfo, Device> mDevicesByInfo = new HashMap<>();
    private final HashMap<BluetoothDevice, Device> mBluetoothDevices = new HashMap<>();
    private final HashMap<BluetoothDevice, MidiDevice> mBleMidiDeviceMap = new HashMap<>();
    private final HashMap<IBinder, Device> mDevicesByServer = new HashMap<>();
    private int mNextDeviceId = 1;
    private final Object mUsbMidiLock = new Object();

    @GuardedBy({"mUsbMidiLock"})
    private final HashMap<String, Integer> mUsbMidiLegacyDeviceOpenCount = new HashMap<>();

    @GuardedBy({"mUsbMidiLock"})
    private final HashSet<String> mUsbMidiUniversalDeviceInUse = new HashSet<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Lifecycle extends SystemService {
        private MidiService mMidiService;

        public Lifecycle(Context context) {
            super(context);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.midi.MidiService, android.os.IBinder] */
        public void onStart() {
            ?? midiService = new MidiService(getContext());
            this.mMidiService = midiService;
            publishBinderService("midi", (IBinder) midiService);
        }

        public void onUserUnlocking(SystemService.TargetUser targetUser) {
            if (targetUser.getUserIdentifier() == 0) {
                this.mMidiService.onUnlockUser();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class Client implements IBinder.DeathRecipient {
        private static final String TAG = "MidiService.Client";
        private final IBinder mToken;
        private final HashMap<IBinder, IMidiDeviceListener> mListeners = new HashMap<>();
        private final HashMap<IBinder, DeviceConnection> mDeviceConnections = new HashMap<>();
        private final int mUid = Binder.getCallingUid();
        private final int mPid = Binder.getCallingPid();

        public Client(IBinder iBinder) {
            this.mToken = iBinder;
        }

        public int getUid() {
            return this.mUid;
        }

        public void addListener(IMidiDeviceListener iMidiDeviceListener) {
            if (this.mListeners.size() >= 16) {
                throw new SecurityException("too many MIDI listeners for UID = " + this.mUid);
            }
            this.mListeners.put(iMidiDeviceListener.asBinder(), iMidiDeviceListener);
        }

        public void removeListener(IMidiDeviceListener iMidiDeviceListener) {
            this.mListeners.remove(iMidiDeviceListener.asBinder());
            if (this.mListeners.size() == 0 && this.mDeviceConnections.size() == 0) {
                close();
            }
        }

        public void addDeviceConnection(Device device, IMidiDeviceOpenCallback iMidiDeviceOpenCallback) {
            Log.d(TAG, "addDeviceConnection() device:" + device);
            if (this.mDeviceConnections.size() >= 64) {
                Log.i(TAG, "too many MIDI connections for UID = " + this.mUid);
                throw new SecurityException("too many MIDI connections for UID = " + this.mUid);
            }
            DeviceConnection deviceConnection = new DeviceConnection(device, this, iMidiDeviceOpenCallback);
            this.mDeviceConnections.put(deviceConnection.getToken(), deviceConnection);
            device.addDeviceConnection(deviceConnection);
        }

        public void removeDeviceConnection(IBinder iBinder) {
            DeviceConnection remove = this.mDeviceConnections.remove(iBinder);
            if (remove != null) {
                remove.getDevice().removeDeviceConnection(remove);
            }
            if (this.mListeners.size() == 0 && this.mDeviceConnections.size() == 0) {
                close();
            }
        }

        public void removeDeviceConnection(DeviceConnection deviceConnection) {
            this.mDeviceConnections.remove(deviceConnection.getToken());
            if (this.mListeners.size() == 0 && this.mDeviceConnections.size() == 0) {
                close();
            }
        }

        public void deviceAdded(Device device) {
            if (device.isUidAllowed(this.mUid)) {
                MidiDeviceInfo deviceInfo = device.getDeviceInfo();
                try {
                    Iterator<IMidiDeviceListener> it = this.mListeners.values().iterator();
                    while (it.hasNext()) {
                        it.next().onDeviceAdded(deviceInfo);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "remote exception", e);
                }
            }
        }

        public void deviceRemoved(Device device) {
            if (device.isUidAllowed(this.mUid)) {
                MidiDeviceInfo deviceInfo = device.getDeviceInfo();
                try {
                    Iterator<IMidiDeviceListener> it = this.mListeners.values().iterator();
                    while (it.hasNext()) {
                        it.next().onDeviceRemoved(deviceInfo);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "remote exception", e);
                }
            }
        }

        public void deviceStatusChanged(Device device, MidiDeviceStatus midiDeviceStatus) {
            if (device.isUidAllowed(this.mUid)) {
                try {
                    Iterator<IMidiDeviceListener> it = this.mListeners.values().iterator();
                    while (it.hasNext()) {
                        it.next().onDeviceStatusChanged(midiDeviceStatus);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "remote exception", e);
                }
            }
        }

        private void close() {
            synchronized (MidiService.this.mClients) {
                MidiService.this.mClients.remove(this.mToken);
                this.mToken.unlinkToDeath(this, 0);
            }
            for (DeviceConnection deviceConnection : this.mDeviceConnections.values()) {
                deviceConnection.getDevice().removeDeviceConnection(deviceConnection);
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d(TAG, "Client died: " + this);
            close();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Client: UID: ");
            sb.append(this.mUid);
            sb.append(" PID: ");
            sb.append(this.mPid);
            sb.append(" listener count: ");
            sb.append(this.mListeners.size());
            sb.append(" Device Connections:");
            for (DeviceConnection deviceConnection : this.mDeviceConnections.values()) {
                sb.append(" <device ");
                sb.append(deviceConnection.getDevice().getDeviceInfo().getId());
                sb.append(">");
            }
            return sb.toString();
        }
    }

    private Client getClient(IBinder iBinder) {
        Client client;
        synchronized (this.mClients) {
            client = this.mClients.get(iBinder);
            if (client == null) {
                client = new Client(iBinder);
                try {
                    iBinder.linkToDeath(client, 0);
                    this.mClients.put(iBinder, client);
                } catch (RemoteException unused) {
                    return null;
                }
            }
        }
        return client;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class Device implements IBinder.DeathRecipient {
        private static final String TAG = "MidiService.Device";
        private final BluetoothDevice mBluetoothDevice;
        private final ArrayList<DeviceConnection> mDeviceConnections;
        private AtomicInteger mDeviceConnectionsAdded;
        private AtomicInteger mDeviceConnectionsRemoved;
        private MidiDeviceInfo mDeviceInfo;
        private MidiDeviceStatus mDeviceStatus;
        private Instant mPreviousCounterInstant;
        private IMidiDeviceServer mServer;
        private ServiceConnection mServiceConnection;
        private final ServiceInfo mServiceInfo;
        private AtomicInteger mTotalInputBytes;
        private AtomicInteger mTotalOutputBytes;
        private AtomicLong mTotalTimeConnectedNs;
        private final int mUid;

        public Device(IMidiDeviceServer iMidiDeviceServer, MidiDeviceInfo midiDeviceInfo, ServiceInfo serviceInfo, int i) {
            this.mDeviceConnections = new ArrayList<>();
            this.mDeviceConnectionsAdded = new AtomicInteger();
            this.mDeviceConnectionsRemoved = new AtomicInteger();
            this.mTotalTimeConnectedNs = new AtomicLong();
            this.mPreviousCounterInstant = null;
            this.mTotalInputBytes = new AtomicInteger();
            this.mTotalOutputBytes = new AtomicInteger();
            this.mDeviceInfo = midiDeviceInfo;
            this.mServiceInfo = serviceInfo;
            this.mUid = i;
            this.mBluetoothDevice = (BluetoothDevice) midiDeviceInfo.getProperties().getParcelable("bluetooth_device", BluetoothDevice.class);
            setDeviceServer(iMidiDeviceServer);
        }

        public Device(BluetoothDevice bluetoothDevice) {
            this.mDeviceConnections = new ArrayList<>();
            this.mDeviceConnectionsAdded = new AtomicInteger();
            this.mDeviceConnectionsRemoved = new AtomicInteger();
            this.mTotalTimeConnectedNs = new AtomicLong();
            this.mPreviousCounterInstant = null;
            this.mTotalInputBytes = new AtomicInteger();
            this.mTotalOutputBytes = new AtomicInteger();
            this.mBluetoothDevice = bluetoothDevice;
            this.mServiceInfo = null;
            this.mUid = MidiService.this.mBluetoothServiceUid;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDeviceServer(IMidiDeviceServer iMidiDeviceServer) {
            Log.i(TAG, "setDeviceServer()");
            if (iMidiDeviceServer != null) {
                if (this.mServer != null) {
                    Log.e(TAG, "mServer already set in setDeviceServer");
                    return;
                }
                IBinder asBinder = iMidiDeviceServer.asBinder();
                try {
                    asBinder.linkToDeath(this, 0);
                    this.mServer = iMidiDeviceServer;
                    MidiService.this.mDevicesByServer.put(asBinder, this);
                } catch (RemoteException unused) {
                    this.mServer = null;
                    return;
                }
            } else {
                IMidiDeviceServer iMidiDeviceServer2 = this.mServer;
                if (iMidiDeviceServer2 != null) {
                    this.mServer = null;
                    IBinder asBinder2 = iMidiDeviceServer2.asBinder();
                    MidiService.this.mDevicesByServer.remove(asBinder2);
                    this.mDeviceStatus = null;
                    try {
                        iMidiDeviceServer2.closeDevice();
                        asBinder2.unlinkToDeath(this, 0);
                    } catch (RemoteException unused2) {
                    }
                    iMidiDeviceServer = iMidiDeviceServer2;
                }
            }
            ArrayList<DeviceConnection> arrayList = this.mDeviceConnections;
            if (arrayList != null) {
                synchronized (arrayList) {
                    Iterator<DeviceConnection> it = this.mDeviceConnections.iterator();
                    while (it.hasNext()) {
                        it.next().notifyClient(iMidiDeviceServer);
                    }
                }
            }
        }

        public MidiDeviceInfo getDeviceInfo() {
            return this.mDeviceInfo;
        }

        public void setDeviceInfo(MidiDeviceInfo midiDeviceInfo) {
            this.mDeviceInfo = midiDeviceInfo;
        }

        public MidiDeviceStatus getDeviceStatus() {
            return this.mDeviceStatus;
        }

        public void setDeviceStatus(MidiDeviceStatus midiDeviceStatus) {
            this.mDeviceStatus = midiDeviceStatus;
        }

        public IMidiDeviceServer getDeviceServer() {
            return this.mServer;
        }

        public ServiceInfo getServiceInfo() {
            return this.mServiceInfo;
        }

        public String getPackageName() {
            ServiceInfo serviceInfo = this.mServiceInfo;
            if (serviceInfo == null) {
                return null;
            }
            return serviceInfo.packageName;
        }

        public int getUid() {
            return this.mUid;
        }

        public boolean isUidAllowed(int i) {
            return !this.mDeviceInfo.isPrivate() || this.mUid == i;
        }

        public void addDeviceConnection(DeviceConnection deviceConnection) {
            Intent intent;
            Log.d(TAG, "addDeviceConnection() [A] connection:" + deviceConnection);
            synchronized (this.mDeviceConnections) {
                this.mDeviceConnectionsAdded.incrementAndGet();
                if (this.mPreviousCounterInstant == null) {
                    this.mPreviousCounterInstant = Instant.now();
                }
                Log.d(TAG, "  mServer:" + this.mServer);
                if (this.mServer != null) {
                    Log.i(TAG, "++++ A");
                    this.mDeviceConnections.add(deviceConnection);
                    deviceConnection.notifyClient(this.mServer);
                } else if (this.mServiceConnection == null && (this.mServiceInfo != null || this.mBluetoothDevice != null)) {
                    Log.i(TAG, "++++ B");
                    this.mDeviceConnections.add(deviceConnection);
                    this.mServiceConnection = new ServiceConnection() { // from class: com.android.server.midi.MidiService.Device.1
                        @Override // android.content.ServiceConnection
                        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                            IMidiDeviceServer asInterface;
                            Log.i(Device.TAG, "++++ onServiceConnected() mBluetoothDevice:" + Device.this.mBluetoothDevice);
                            if (Device.this.mBluetoothDevice != null) {
                                IBluetoothMidiService asInterface2 = IBluetoothMidiService.Stub.asInterface(iBinder);
                                Log.i(Device.TAG, "++++ mBluetoothMidiService:" + asInterface2);
                                if (asInterface2 != null) {
                                    try {
                                        asInterface = IMidiDeviceServer.Stub.asInterface(asInterface2.addBluetoothDevice(Device.this.mBluetoothDevice));
                                    } catch (RemoteException e) {
                                        Log.e(Device.TAG, "Could not call addBluetoothDevice()", e);
                                    } catch (NullPointerException e2) {
                                        Log.e(Device.TAG, "Could not call addBluetoothDevice()", e2);
                                    }
                                }
                                asInterface = null;
                            } else {
                                asInterface = IMidiDeviceServer.Stub.asInterface(iBinder);
                            }
                            Device.this.setDeviceServer(asInterface);
                        }

                        @Override // android.content.ServiceConnection
                        public void onServiceDisconnected(ComponentName componentName) {
                            Device.this.setDeviceServer(null);
                            Device.this.mServiceConnection = null;
                        }
                    };
                    if (this.mBluetoothDevice != null) {
                        intent = new Intent("android.media.midi.BluetoothMidiService");
                        intent.setComponent(new ComponentName("com.android.bluetoothmidiservice", "com.android.bluetoothmidiservice.BluetoothMidiService"));
                    } else {
                        intent = new Intent("android.media.midi.MidiDeviceService");
                        ServiceInfo serviceInfo = this.mServiceInfo;
                        intent.setComponent(new ComponentName(serviceInfo.packageName, serviceInfo.name));
                    }
                    if (!MidiService.this.mContext.bindService(intent, this.mServiceConnection, 1)) {
                        Log.e(TAG, "Unable to bind service: " + intent);
                        setDeviceServer(null);
                        this.mServiceConnection = null;
                    }
                } else {
                    Log.e(TAG, "No way to connect to device in addDeviceConnection");
                    deviceConnection.notifyClient(null);
                }
            }
        }

        public void removeDeviceConnection(DeviceConnection deviceConnection) {
            synchronized (MidiService.this.mDevicesByInfo) {
                synchronized (this.mDeviceConnections) {
                    int incrementAndGet = this.mDeviceConnectionsRemoved.incrementAndGet();
                    Instant instant = this.mPreviousCounterInstant;
                    if (instant != null) {
                        this.mTotalTimeConnectedNs.addAndGet(Duration.between(instant, Instant.now()).toNanos());
                    }
                    if (incrementAndGet >= this.mDeviceConnectionsAdded.get()) {
                        this.mPreviousCounterInstant = null;
                    } else {
                        this.mPreviousCounterInstant = Instant.now();
                    }
                    logMetrics(false);
                    this.mDeviceConnections.remove(deviceConnection);
                    if (deviceConnection.getDevice().getDeviceInfo().getType() == 1) {
                        synchronized (MidiService.this.mUsbMidiLock) {
                            MidiService.this.removeUsbMidiDeviceLocked(deviceConnection.getDevice().getDeviceInfo());
                        }
                    }
                    if (this.mDeviceConnections.size() == 0 && this.mServiceConnection != null) {
                        MidiService.this.mContext.unbindService(this.mServiceConnection);
                        this.mServiceConnection = null;
                        if (this.mBluetoothDevice != null) {
                            closeLocked();
                        } else {
                            setDeviceServer(null);
                        }
                    }
                }
            }
        }

        public void closeLocked() {
            synchronized (this.mDeviceConnections) {
                Iterator<DeviceConnection> it = this.mDeviceConnections.iterator();
                while (it.hasNext()) {
                    DeviceConnection next = it.next();
                    if (next.getDevice().getDeviceInfo().getType() == 1) {
                        synchronized (MidiService.this.mUsbMidiLock) {
                            MidiService.this.removeUsbMidiDeviceLocked(next.getDevice().getDeviceInfo());
                        }
                    }
                    next.getClient().removeDeviceConnection(next);
                }
                this.mDeviceConnections.clear();
                if (this.mPreviousCounterInstant != null) {
                    Instant now = Instant.now();
                    this.mTotalTimeConnectedNs.addAndGet(Duration.between(this.mPreviousCounterInstant, now).toNanos());
                    this.mPreviousCounterInstant = now;
                }
                logMetrics(true);
            }
            setDeviceServer(null);
            if (this.mServiceInfo == null) {
                MidiService.this.removeDeviceLocked(this);
            } else {
                this.mDeviceStatus = new MidiDeviceStatus(this.mDeviceInfo);
            }
            if (this.mBluetoothDevice != null) {
                MidiService.this.mBluetoothDevices.remove(this.mBluetoothDevice);
            }
        }

        private void logMetrics(boolean z) {
            int i = this.mDeviceConnectionsAdded.get();
            if (this.mDeviceInfo == null || i <= 0) {
                return;
            }
            new MediaMetrics.Item("audio.midi").setUid(this.mUid).set(MediaMetrics.Property.DEVICE_ID, Integer.valueOf(this.mDeviceInfo.getId())).set(MediaMetrics.Property.INPUT_PORT_COUNT, Integer.valueOf(this.mDeviceInfo.getInputPortCount())).set(MediaMetrics.Property.OUTPUT_PORT_COUNT, Integer.valueOf(this.mDeviceInfo.getOutputPortCount())).set(MediaMetrics.Property.HARDWARE_TYPE, Integer.valueOf(this.mDeviceInfo.getType())).set(MediaMetrics.Property.DURATION_NS, Long.valueOf(this.mTotalTimeConnectedNs.get())).set(MediaMetrics.Property.OPENED_COUNT, Integer.valueOf(i)).set(MediaMetrics.Property.CLOSED_COUNT, Integer.valueOf(this.mDeviceConnectionsRemoved.get())).set(MediaMetrics.Property.DEVICE_DISCONNECTED, z ? "true" : "false").set(MediaMetrics.Property.IS_SHARED, !this.mDeviceInfo.isPrivate() ? "true" : "false").set(MediaMetrics.Property.SUPPORTS_MIDI_UMP, this.mDeviceInfo.getDefaultProtocol() != -1 ? "true" : "false").set(MediaMetrics.Property.USING_ALSA, this.mDeviceInfo.getProperties().get("alsa_card") == null ? "false" : "true").set(MediaMetrics.Property.EVENT, "deviceClosed").set(MediaMetrics.Property.TOTAL_INPUT_BYTES, Integer.valueOf(this.mTotalInputBytes.get())).set(MediaMetrics.Property.TOTAL_OUTPUT_BYTES, Integer.valueOf(this.mTotalOutputBytes.get())).record();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d(TAG, "Device died: " + this);
            synchronized (MidiService.this.mDevicesByInfo) {
                closeLocked();
            }
        }

        public void updateTotalBytes(int i, int i2) {
            this.mTotalInputBytes.set(i);
            this.mTotalOutputBytes.set(i2);
        }

        public String toString() {
            return "Device Info: " + this.mDeviceInfo + " Status: " + this.mDeviceStatus + " UID: " + this.mUid + " DeviceConnection count: " + this.mDeviceConnections.size() + " mServiceConnection: " + this.mServiceConnection;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class DeviceConnection {
        private static final String TAG = "MidiService.DeviceConnection";
        private IMidiDeviceOpenCallback mCallback;
        private final Client mClient;
        private final Device mDevice;
        private final IBinder mToken = new Binder();

        public DeviceConnection(Device device, Client client, IMidiDeviceOpenCallback iMidiDeviceOpenCallback) {
            this.mDevice = device;
            this.mClient = client;
            this.mCallback = iMidiDeviceOpenCallback;
        }

        public Device getDevice() {
            return this.mDevice;
        }

        public Client getClient() {
            return this.mClient;
        }

        public IBinder getToken() {
            return this.mToken;
        }

        public void notifyClient(IMidiDeviceServer iMidiDeviceServer) {
            IBinder iBinder;
            Log.d(TAG, "notifyClient");
            IMidiDeviceOpenCallback iMidiDeviceOpenCallback = this.mCallback;
            if (iMidiDeviceOpenCallback != null) {
                if (iMidiDeviceServer == null) {
                    iBinder = null;
                } else {
                    try {
                        iBinder = this.mToken;
                    } catch (RemoteException unused) {
                    }
                }
                iMidiDeviceOpenCallback.onDeviceOpened(iMidiDeviceServer, iBinder);
                this.mCallback = null;
            }
        }

        public String toString() {
            Device device = this.mDevice;
            if (device == null || device.getDeviceInfo() == null) {
                return "null";
            }
            return "" + this.mDevice.getDeviceInfo().getId();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBLEMIDIDevice(BluetoothDevice bluetoothDevice) {
        ParcelUuid[] uuids = bluetoothDevice.getUuids();
        if (uuids != null) {
            for (ParcelUuid parcelUuid : uuids) {
                if (parcelUuid.getUuid().equals(MIDI_SERVICE)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dumpIntentExtras(Intent intent) {
        Log.d(TAG, "Intent: " + intent.getAction());
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String str : extras.keySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append("  ");
                sb.append(str);
                sb.append(" : ");
                sb.append(extras.get(str) != null ? extras.get(str) : "NULL");
                Log.d(TAG, sb.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isBleTransport(Intent intent) {
        Bundle extras = intent.getExtras();
        return extras != null && extras.getInt("android.bluetooth.device.extra.TRANSPORT", 0) == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpUuids(BluetoothDevice bluetoothDevice) {
        ParcelUuid[] uuids = bluetoothDevice.getUuids();
        StringBuilder sb = new StringBuilder();
        sb.append("dumpUuids(");
        sb.append(bluetoothDevice);
        sb.append(") numParcels:");
        sb.append(uuids != null ? uuids.length : 0);
        Log.d(TAG, sb.toString());
        if (uuids == null) {
            Log.d(TAG, "No UUID Parcels");
            return;
        }
        for (ParcelUuid parcelUuid : uuids) {
            Log.d(TAG, " uuid:" + parcelUuid.getUuid());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasNonMidiUuids(BluetoothDevice bluetoothDevice) {
        ParcelUuid[] uuids = bluetoothDevice.getUuids();
        if (uuids != null) {
            for (ParcelUuid parcelUuid : uuids) {
                if (this.mNonMidiUUIDs.contains(parcelUuid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public MidiService(Context context) {
        HashSet<ParcelUuid> hashSet = new HashSet<>();
        this.mNonMidiUUIDs = hashSet;
        this.mPackageMonitor = new PackageMonitor() { // from class: com.android.server.midi.MidiService.1
            public void onPackageAdded(String str, int i) {
                MidiService.this.addPackageDeviceServers(str);
            }

            public void onPackageModified(String str) {
                MidiService.this.removePackageDeviceServers(str);
                MidiService.this.addPackageDeviceServers(str);
            }

            public void onPackageRemoved(String str, int i) {
                MidiService.this.removePackageDeviceServers(str);
            }
        };
        this.mBleMidiReceiver = new BroadcastReceiver() { // from class: com.android.server.midi.MidiService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    Log.w(MidiService.TAG, "MidiService, action is null");
                    return;
                }
                char c = 65535;
                switch (action.hashCode()) {
                    case -377527494:
                        if (action.equals("android.bluetooth.device.action.UUID")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -301431627:
                        if (action.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1821585647:
                        if (action.equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 2116862345:
                        if (action.equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 3:
                        Log.d(MidiService.TAG, "ACTION_UUID");
                        BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", BluetoothDevice.class);
                        MidiService.this.dumpUuids(bluetoothDevice);
                        if (MidiService.this.isBLEMIDIDevice(bluetoothDevice)) {
                            Log.d(MidiService.TAG, "BT MIDI DEVICE");
                            MidiService.this.openBluetoothDevice(bluetoothDevice);
                            return;
                        }
                        return;
                    case 1:
                        Log.d(MidiService.TAG, "ACTION_ACL_CONNECTED");
                        MidiService.dumpIntentExtras(intent);
                        if (!MidiService.isBleTransport(intent)) {
                            Log.i(MidiService.TAG, "No BLE transport - NOT MIDI");
                            return;
                        }
                        Log.d(MidiService.TAG, "BLE Device");
                        BluetoothDevice bluetoothDevice2 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", BluetoothDevice.class);
                        MidiService.this.dumpUuids(bluetoothDevice2);
                        if (MidiService.this.hasNonMidiUuids(bluetoothDevice2)) {
                            Log.d(MidiService.TAG, "Non-MIDI service UUIDs found. NOT MIDI");
                            return;
                        } else {
                            Log.d(MidiService.TAG, "Potential MIDI Device.");
                            MidiService.this.openBluetoothDevice(bluetoothDevice2);
                            return;
                        }
                    case 2:
                        Log.d(MidiService.TAG, "ACTION_ACL_DISCONNECTED");
                        BluetoothDevice bluetoothDevice3 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", BluetoothDevice.class);
                        if (MidiService.this.isBLEMIDIDevice(bluetoothDevice3)) {
                            MidiService.this.closeBluetoothDevice(bluetoothDevice3);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mBluetoothServiceUid = -1;
        hashSet.add(BluetoothUuid.A2DP_SINK);
        hashSet.add(BluetoothUuid.A2DP_SOURCE);
        hashSet.add(BluetoothUuid.ADV_AUDIO_DIST);
        hashSet.add(BluetoothUuid.AVRCP_CONTROLLER);
        hashSet.add(BluetoothUuid.HFP);
        hashSet.add(BluetoothUuid.HSP);
        hashSet.add(BluetoothUuid.HID);
        hashSet.add(BluetoothUuid.LE_AUDIO);
        hashSet.add(BluetoothUuid.HOGP);
        hashSet.add(BluetoothUuid.HEARING_AID);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUnlockUser() {
        ApplicationInfo applicationInfo;
        PackageInfo packageInfo = null;
        this.mPackageMonitor.register(this.mContext, (Looper) null, true);
        List<ResolveInfo> queryIntentServices = this.mPackageManager.queryIntentServices(new Intent("android.media.midi.MidiDeviceService"), 128);
        if (queryIntentServices != null) {
            int size = queryIntentServices.size();
            for (int i = 0; i < size; i++) {
                ServiceInfo serviceInfo = queryIntentServices.get(i).serviceInfo;
                if (serviceInfo != null) {
                    addPackageDeviceServer(serviceInfo);
                }
            }
        }
        try {
            packageInfo = this.mPackageManager.getPackageInfo("com.android.bluetoothmidiservice", 0);
        } catch (PackageManager.NameNotFoundException unused) {
        }
        if (packageInfo != null && (applicationInfo = packageInfo.applicationInfo) != null) {
            this.mBluetoothServiceUid = applicationInfo.uid;
        } else {
            this.mBluetoothServiceUid = -1;
        }
    }

    public void registerListener(IBinder iBinder, IMidiDeviceListener iMidiDeviceListener) {
        Client client = getClient(iBinder);
        if (client == null) {
            return;
        }
        client.addListener(iMidiDeviceListener);
        updateStickyDeviceStatus(client.mUid, iMidiDeviceListener);
    }

    public void unregisterListener(IBinder iBinder, IMidiDeviceListener iMidiDeviceListener) {
        Client client = getClient(iBinder);
        if (client == null) {
            return;
        }
        client.removeListener(iMidiDeviceListener);
    }

    private void updateStickyDeviceStatus(int i, IMidiDeviceListener iMidiDeviceListener) {
        synchronized (this.mDevicesByInfo) {
            for (Device device : this.mDevicesByInfo.values()) {
                if (device.isUidAllowed(i)) {
                    try {
                        MidiDeviceStatus deviceStatus = device.getDeviceStatus();
                        if (deviceStatus != null) {
                            iMidiDeviceListener.onDeviceStatusChanged(deviceStatus);
                        }
                    } catch (RemoteException e) {
                        Log.e(TAG, "remote exception", e);
                    }
                }
            }
        }
    }

    public MidiDeviceInfo[] getDevices() {
        return getDevicesForTransport(1);
    }

    public MidiDeviceInfo[] getDevicesForTransport(int i) {
        ArrayList arrayList = new ArrayList();
        int callingUid = Binder.getCallingUid();
        synchronized (this.mDevicesByInfo) {
            for (Device device : this.mDevicesByInfo.values()) {
                if (device.isUidAllowed(callingUid)) {
                    if (i == 2) {
                        if (device.getDeviceInfo().getDefaultProtocol() != -1) {
                            arrayList.add(device.getDeviceInfo());
                        }
                    } else if (i == 1 && device.getDeviceInfo().getDefaultProtocol() == -1) {
                        arrayList.add(device.getDeviceInfo());
                    }
                }
            }
        }
        return (MidiDeviceInfo[]) arrayList.toArray(EMPTY_DEVICE_INFO_ARRAY);
    }

    public void openDevice(IBinder iBinder, MidiDeviceInfo midiDeviceInfo, IMidiDeviceOpenCallback iMidiDeviceOpenCallback) {
        Device device;
        Client client = getClient(iBinder);
        Log.d(TAG, "openDevice() client:" + client);
        if (client == null) {
            return;
        }
        synchronized (this.mDevicesByInfo) {
            device = this.mDevicesByInfo.get(midiDeviceInfo);
            Log.d(TAG, "  device:" + device);
            if (device == null) {
                throw new IllegalArgumentException("device does not exist: " + midiDeviceInfo);
            }
            if (!device.isUidAllowed(Binder.getCallingUid())) {
                throw new SecurityException("Attempt to open private device with wrong UID");
            }
        }
        if (midiDeviceInfo.getType() == 1) {
            synchronized (this.mUsbMidiLock) {
                if (isUsbMidiDeviceInUseLocked(midiDeviceInfo)) {
                    throw new IllegalArgumentException("device already in use: " + midiDeviceInfo);
                }
                addUsbMidiDeviceLocked(midiDeviceInfo);
            }
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Log.i(TAG, "addDeviceConnection() [B] device:" + device);
            client.addDeviceConnection(device, iMidiDeviceOpenCallback);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openBluetoothDevice(final BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "openBluetoothDevice() device: " + bluetoothDevice);
        ((MidiManager) this.mContext.getSystemService(MidiManager.class)).openBluetoothDevice(bluetoothDevice, new MidiManager.OnDeviceOpenedListener() { // from class: com.android.server.midi.MidiService.3
            @Override // android.media.midi.MidiManager.OnDeviceOpenedListener
            public void onDeviceOpened(MidiDevice midiDevice) {
                synchronized (MidiService.this.mBleMidiDeviceMap) {
                    Log.i(MidiService.TAG, "onDeviceOpened() device:" + midiDevice);
                    MidiService.this.mBleMidiDeviceMap.put(bluetoothDevice, midiDevice);
                }
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeBluetoothDevice(BluetoothDevice bluetoothDevice) {
        MidiDevice remove;
        Log.d(TAG, "closeBluetoothDevice() device: " + bluetoothDevice);
        synchronized (this.mBleMidiDeviceMap) {
            remove = this.mBleMidiDeviceMap.remove(bluetoothDevice);
        }
        if (remove != null) {
            try {
                remove.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception closing BLE-MIDI device" + e);
            }
        }
    }

    public void openBluetoothDevice(IBinder iBinder, BluetoothDevice bluetoothDevice, IMidiDeviceOpenCallback iMidiDeviceOpenCallback) {
        Device device;
        Log.d(TAG, "openBluetoothDevice()");
        Client client = getClient(iBinder);
        if (client == null) {
            return;
        }
        Log.i(TAG, "alloc device...");
        synchronized (this.mDevicesByInfo) {
            device = this.mBluetoothDevices.get(bluetoothDevice);
            if (device == null) {
                device = new Device(bluetoothDevice);
                this.mBluetoothDevices.put(bluetoothDevice, device);
            }
        }
        Log.i(TAG, "device: " + device);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Log.i(TAG, "addDeviceConnection() [C] device:" + device);
            client.addDeviceConnection(device, iMidiDeviceOpenCallback);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void closeDevice(IBinder iBinder, IBinder iBinder2) {
        Client client = getClient(iBinder);
        if (client == null) {
            return;
        }
        client.removeDeviceConnection(iBinder2);
    }

    public MidiDeviceInfo registerDeviceServer(IMidiDeviceServer iMidiDeviceServer, int i, int i2, String[] strArr, String[] strArr2, Bundle bundle, int i3, int i4) {
        MidiDeviceInfo addDeviceLocked;
        int callingUid = Binder.getCallingUid();
        if (i3 == 1 && callingUid != 1000) {
            throw new SecurityException("only system can create USB devices");
        }
        if (i3 == 3 && callingUid != this.mBluetoothServiceUid) {
            throw new SecurityException("only MidiBluetoothService can create Bluetooth devices");
        }
        synchronized (this.mDevicesByInfo) {
            addDeviceLocked = addDeviceLocked(i3, i, i2, strArr, strArr2, bundle, iMidiDeviceServer, null, false, callingUid, i4);
        }
        return addDeviceLocked;
    }

    public void unregisterDeviceServer(IMidiDeviceServer iMidiDeviceServer) {
        synchronized (this.mDevicesByInfo) {
            Device device = this.mDevicesByServer.get(iMidiDeviceServer.asBinder());
            if (device != null) {
                device.closeLocked();
            }
        }
    }

    public MidiDeviceInfo getServiceDeviceInfo(String str, String str2) {
        int callingUid = Binder.getCallingUid();
        synchronized (this.mDevicesByInfo) {
            for (Device device : this.mDevicesByInfo.values()) {
                ServiceInfo serviceInfo = device.getServiceInfo();
                if (serviceInfo != null && str.equals(serviceInfo.packageName) && str2.equals(serviceInfo.name)) {
                    if (device.isUidAllowed(callingUid)) {
                        return device.getDeviceInfo();
                    }
                    EventLog.writeEvent(1397638484, "185796676", -1, "");
                    return null;
                }
            }
            return null;
        }
    }

    public MidiDeviceStatus getDeviceStatus(MidiDeviceInfo midiDeviceInfo) {
        Device device = this.mDevicesByInfo.get(midiDeviceInfo);
        if (device == null) {
            throw new IllegalArgumentException("no such device for " + midiDeviceInfo);
        }
        int callingUid = Binder.getCallingUid();
        if (device.isUidAllowed(callingUid)) {
            return device.getDeviceStatus();
        }
        Log.e(TAG, "getDeviceStatus() invalid UID = " + callingUid);
        EventLog.writeEvent(1397638484, "203549963", Integer.valueOf(callingUid), "getDeviceStatus: invalid uid");
        return null;
    }

    public void setDeviceStatus(IMidiDeviceServer iMidiDeviceServer, MidiDeviceStatus midiDeviceStatus) {
        Device device = this.mDevicesByServer.get(iMidiDeviceServer.asBinder());
        if (device != null) {
            if (Binder.getCallingUid() != device.getUid()) {
                throw new SecurityException("setDeviceStatus() caller UID " + Binder.getCallingUid() + " does not match device's UID " + device.getUid());
            }
            device.setDeviceStatus(midiDeviceStatus);
            notifyDeviceStatusChanged(device, midiDeviceStatus);
        }
    }

    private void notifyDeviceStatusChanged(Device device, MidiDeviceStatus midiDeviceStatus) {
        synchronized (this.mClients) {
            Iterator<Client> it = this.mClients.values().iterator();
            while (it.hasNext()) {
                it.next().deviceStatusChanged(device, midiDeviceStatus);
            }
        }
    }

    private MidiDeviceInfo addDeviceLocked(int i, int i2, int i3, String[] strArr, String[] strArr2, Bundle bundle, IMidiDeviceServer iMidiDeviceServer, ServiceInfo serviceInfo, boolean z, int i4, int i5) {
        BluetoothDevice bluetoothDevice;
        Iterator<Device> it = this.mDevicesByInfo.values().iterator();
        int i6 = 0;
        while (it.hasNext()) {
            if (it.next().getUid() == i4) {
                i6++;
            }
        }
        if (i6 >= 16) {
            throw new SecurityException("too many MIDI devices already created for UID = " + i4);
        }
        int i7 = this.mNextDeviceId;
        this.mNextDeviceId = i7 + 1;
        MidiDeviceInfo midiDeviceInfo = new MidiDeviceInfo(i, i7, i2, i3, strArr, strArr2, bundle, z, i5);
        Device device = null;
        if (iMidiDeviceServer != null) {
            try {
                iMidiDeviceServer.setDeviceInfo(midiDeviceInfo);
            } catch (RemoteException unused) {
                Log.e(TAG, "RemoteException in setDeviceInfo()");
                return null;
            }
        }
        if (i == 3) {
            BluetoothDevice bluetoothDevice2 = (BluetoothDevice) bundle.getParcelable("bluetooth_device", BluetoothDevice.class);
            Device device2 = this.mBluetoothDevices.get(bluetoothDevice2);
            if (device2 != null) {
                device2.setDeviceInfo(midiDeviceInfo);
            }
            bluetoothDevice = bluetoothDevice2;
            device = device2;
        } else {
            bluetoothDevice = null;
        }
        if (device == null) {
            device = new Device(iMidiDeviceServer, midiDeviceInfo, serviceInfo, i4);
        }
        this.mDevicesByInfo.put(midiDeviceInfo, device);
        if (bluetoothDevice != null) {
            this.mBluetoothDevices.put(bluetoothDevice, device);
        }
        synchronized (this.mClients) {
            Iterator<Client> it2 = this.mClients.values().iterator();
            while (it2.hasNext()) {
                it2.next().deviceAdded(device);
            }
        }
        return midiDeviceInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDeviceLocked(Device device) {
        IMidiDeviceServer deviceServer = device.getDeviceServer();
        if (deviceServer != null) {
            this.mDevicesByServer.remove(deviceServer.asBinder());
        }
        this.mDevicesByInfo.remove(device.getDeviceInfo());
        synchronized (this.mClients) {
            Iterator<Client> it = this.mClients.values().iterator();
            while (it.hasNext()) {
                it.next().deviceRemoved(device);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addPackageDeviceServers(String str) {
        try {
            ServiceInfo[] serviceInfoArr = this.mPackageManager.getPackageInfo(str, 132).services;
            if (serviceInfoArr == null) {
                return;
            }
            for (ServiceInfo serviceInfo : serviceInfoArr) {
                addPackageDeviceServer(serviceInfo);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "handlePackageUpdate could not find package " + str, e);
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:89:? -> B:85:0x01db). Please report as a decompilation issue!!! */
    private void addPackageDeviceServer(ServiceInfo serviceInfo) {
        int i;
        ArrayList arrayList;
        ArrayList arrayList2;
        int i2;
        HashMap<MidiDeviceInfo, Device> hashMap;
        HashMap<MidiDeviceInfo, Device> hashMap2;
        String str;
        String str2;
        if (serviceInfo == null) {
            return;
        }
        XmlResourceParser xmlResourceParser = null;
        try {
            try {
                XmlResourceParser loadXmlMetaData = serviceInfo.loadXmlMetaData(this.mPackageManager, "android.media.midi.MidiDeviceService");
                if (loadXmlMetaData == null) {
                    if (loadXmlMetaData != null) {
                        loadXmlMetaData.close();
                        return;
                    }
                    return;
                }
                try {
                    if (!"android.permission.BIND_MIDI_DEVICE_SERVICE".equals(serviceInfo.permission)) {
                        Log.w(TAG, "Skipping MIDI device service " + serviceInfo.packageName + ": it does not require the permission android.permission.BIND_MIDI_DEVICE_SERVICE");
                        loadXmlMetaData.close();
                        return;
                    }
                    ArrayList arrayList3 = new ArrayList();
                    ArrayList arrayList4 = new ArrayList();
                    int i3 = 0;
                    int i4 = 0;
                    int i5 = 0;
                    boolean z = false;
                    Bundle bundle = null;
                    while (true) {
                        int next = loadXmlMetaData.next();
                        if (next == 1) {
                            loadXmlMetaData.close();
                            return;
                        }
                        if (next == 2) {
                            String name = loadXmlMetaData.getName();
                            if ("device".equals(name)) {
                                if (bundle != null) {
                                    Log.w(TAG, "nested <device> elements in metadata for " + serviceInfo.packageName);
                                } else {
                                    bundle = new Bundle();
                                    bundle.putParcelable("service_info", serviceInfo);
                                    int attributeCount = loadXmlMetaData.getAttributeCount();
                                    int i6 = i3;
                                    z = i6;
                                    while (i6 < attributeCount) {
                                        String attributeName = loadXmlMetaData.getAttributeName(i6);
                                        String attributeValue = loadXmlMetaData.getAttributeValue(i6);
                                        if ("private".equals(attributeName)) {
                                            z = "true".equals(attributeValue);
                                        } else {
                                            bundle.putString(attributeName, attributeValue);
                                        }
                                        i6++;
                                    }
                                    i4 = i3;
                                    i5 = i4;
                                }
                            } else if ("input-port".equals(name)) {
                                if (bundle == null) {
                                    Log.w(TAG, "<input-port> outside of <device> in metadata for " + serviceInfo.packageName);
                                } else {
                                    i4++;
                                    int attributeCount2 = loadXmlMetaData.getAttributeCount();
                                    int i7 = i3;
                                    while (true) {
                                        if (i7 >= attributeCount2) {
                                            str2 = null;
                                            break;
                                        }
                                        String attributeName2 = loadXmlMetaData.getAttributeName(i7);
                                        str2 = loadXmlMetaData.getAttributeValue(i7);
                                        if ("name".equals(attributeName2)) {
                                            break;
                                        } else {
                                            i7++;
                                        }
                                    }
                                    arrayList3.add(str2);
                                }
                            } else if ("output-port".equals(name)) {
                                if (bundle == null) {
                                    Log.w(TAG, "<output-port> outside of <device> in metadata for " + serviceInfo.packageName);
                                } else {
                                    i5++;
                                    int attributeCount3 = loadXmlMetaData.getAttributeCount();
                                    int i8 = i3;
                                    while (true) {
                                        if (i8 >= attributeCount3) {
                                            str = null;
                                            break;
                                        }
                                        String attributeName3 = loadXmlMetaData.getAttributeName(i8);
                                        str = loadXmlMetaData.getAttributeValue(i8);
                                        if ("name".equals(attributeName3)) {
                                            break;
                                        } else {
                                            i8++;
                                        }
                                    }
                                    arrayList4.add(str);
                                }
                            }
                        } else if (next == 3 && "device".equals(loadXmlMetaData.getName()) && bundle != null) {
                            if (i4 == 0 && i5 == 0) {
                                Log.w(TAG, "<device> with no ports in metadata for " + serviceInfo.packageName);
                            } else {
                                try {
                                    i2 = this.mPackageManager.getApplicationInfo(serviceInfo.packageName, i3).uid;
                                    hashMap = this.mDevicesByInfo;
                                } catch (PackageManager.NameNotFoundException unused) {
                                    i = i3;
                                    arrayList = arrayList4;
                                    arrayList2 = arrayList3;
                                    Log.e(TAG, "could not fetch ApplicationInfo for " + serviceInfo.packageName);
                                }
                                synchronized (hashMap) {
                                    try {
                                        String[] strArr = EMPTY_STRING_ARRAY;
                                        hashMap2 = hashMap;
                                        i = i3;
                                        arrayList = arrayList4;
                                        arrayList2 = arrayList3;
                                        try {
                                            addDeviceLocked(2, i4, i5, (String[]) arrayList3.toArray(strArr), (String[]) arrayList4.toArray(strArr), bundle, null, serviceInfo, z, i2, -1);
                                            arrayList2.clear();
                                            arrayList.clear();
                                            bundle = null;
                                            arrayList3 = arrayList2;
                                            i3 = i;
                                            arrayList4 = arrayList;
                                        } catch (Throwable th) {
                                            th = th;
                                            throw th;
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        hashMap2 = hashMap;
                                        throw th;
                                    }
                                }
                            }
                        }
                        i = i3;
                        arrayList = arrayList4;
                        arrayList2 = arrayList3;
                        arrayList3 = arrayList2;
                        i3 = i;
                        arrayList4 = arrayList;
                    }
                } catch (Exception e) {
                    e = e;
                    xmlResourceParser = loadXmlMetaData;
                    Log.w(TAG, "Unable to load component info " + serviceInfo.toString(), e);
                    if (xmlResourceParser != null) {
                        xmlResourceParser.close();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    xmlResourceParser = loadXmlMetaData;
                    if (xmlResourceParser != null) {
                        xmlResourceParser.close();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removePackageDeviceServers(String str) {
        synchronized (this.mDevicesByInfo) {
            Iterator<Device> it = this.mDevicesByInfo.values().iterator();
            while (it.hasNext()) {
                Device next = it.next();
                if (str.equals(next.getPackageName())) {
                    it.remove();
                    removeDeviceLocked(next);
                }
            }
        }
    }

    public void updateTotalBytes(IMidiDeviceServer iMidiDeviceServer, int i, int i2) {
        synchronized (this.mDevicesByInfo) {
            Device device = this.mDevicesByServer.get(iMidiDeviceServer.asBinder());
            if (device != null) {
                device.updateTotalBytes(i, i2);
            }
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
            indentingPrintWriter.println("MIDI Manager State:");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println("Devices:");
            indentingPrintWriter.increaseIndent();
            synchronized (this.mDevicesByInfo) {
                Iterator<Device> it = this.mDevicesByInfo.values().iterator();
                while (it.hasNext()) {
                    indentingPrintWriter.println(it.next().toString());
                }
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("Clients:");
            indentingPrintWriter.increaseIndent();
            synchronized (this.mClients) {
                Iterator<Client> it2 = this.mClients.values().iterator();
                while (it2.hasNext()) {
                    indentingPrintWriter.println(it2.next().toString());
                }
            }
            indentingPrintWriter.decreaseIndent();
        }
    }

    @GuardedBy({"mUsbMidiLock"})
    private boolean isUsbMidiDeviceInUseLocked(MidiDeviceInfo midiDeviceInfo) {
        String string = midiDeviceInfo.getProperties().getString("name");
        if (string.length() < 8) {
            return false;
        }
        String extractUsbDeviceName = extractUsbDeviceName(string);
        String extractUsbDeviceTag = extractUsbDeviceTag(string);
        Log.i(TAG, "Checking " + extractUsbDeviceName + " " + extractUsbDeviceTag);
        if (this.mUsbMidiUniversalDeviceInUse.contains(extractUsbDeviceName)) {
            return true;
        }
        return extractUsbDeviceTag.equals(MIDI_UNIVERSAL_STRING) && this.mUsbMidiLegacyDeviceOpenCount.containsKey(extractUsbDeviceName);
    }

    @GuardedBy({"mUsbMidiLock"})
    void addUsbMidiDeviceLocked(MidiDeviceInfo midiDeviceInfo) {
        String string = midiDeviceInfo.getProperties().getString("name");
        if (string.length() < 8) {
            return;
        }
        String extractUsbDeviceName = extractUsbDeviceName(string);
        String extractUsbDeviceTag = extractUsbDeviceTag(string);
        Log.i(TAG, "Adding " + extractUsbDeviceName + " " + extractUsbDeviceTag);
        if (extractUsbDeviceTag.equals(MIDI_UNIVERSAL_STRING)) {
            this.mUsbMidiUniversalDeviceInUse.add(extractUsbDeviceName);
        } else if (extractUsbDeviceTag.equals(MIDI_LEGACY_STRING)) {
            this.mUsbMidiLegacyDeviceOpenCount.put(extractUsbDeviceName, Integer.valueOf(this.mUsbMidiLegacyDeviceOpenCount.getOrDefault(extractUsbDeviceName, 0).intValue() + 1));
        }
    }

    @GuardedBy({"mUsbMidiLock"})
    void removeUsbMidiDeviceLocked(MidiDeviceInfo midiDeviceInfo) {
        String string = midiDeviceInfo.getProperties().getString("name");
        if (string.length() < 8) {
            return;
        }
        String extractUsbDeviceName = extractUsbDeviceName(string);
        String extractUsbDeviceTag = extractUsbDeviceTag(string);
        Log.i(TAG, "Removing " + extractUsbDeviceName + " " + extractUsbDeviceTag);
        if (extractUsbDeviceTag.equals(MIDI_UNIVERSAL_STRING)) {
            this.mUsbMidiUniversalDeviceInUse.remove(extractUsbDeviceName);
            return;
        }
        if (extractUsbDeviceTag.equals(MIDI_LEGACY_STRING) && this.mUsbMidiLegacyDeviceOpenCount.containsKey(extractUsbDeviceName)) {
            int intValue = this.mUsbMidiLegacyDeviceOpenCount.get(extractUsbDeviceName).intValue();
            if (intValue > 1) {
                this.mUsbMidiLegacyDeviceOpenCount.put(extractUsbDeviceName, Integer.valueOf(intValue - 1));
            } else {
                this.mUsbMidiLegacyDeviceOpenCount.remove(extractUsbDeviceName);
            }
        }
    }

    String extractUsbDeviceName(String str) {
        return str.substring(0, str.length() - 8);
    }

    String extractUsbDeviceTag(String str) {
        return str.substring(str.length() - 8);
    }
}
