package android.os;

import android.os.IBinder;
import android.util.Slog;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class RpmAidlManager implements IBinder.DeathRecipient {
    private static final String CLASS_PowerStateSubsystemSleepState = "vendor.oplus.hardware.rpmh.PowerStateSubsystemSleepState";
    private static final String IRPMH_AIDL_CLASS_NAME = "vendor.oplus.hardware.rpmh.IRpmh";
    private static final String IRPMH_SERVICE_CLASS_NAME = "vendor.oplus.hardware.rpmh.IRpmh/default";
    private static final String IRPMH_STUB_CLASS_NAME = "vendor.oplus.hardware.rpmh.IRpmh$Stub";
    private static volatile RpmAidlManager sRpmAidl = null;
    private Object mRpmh;
    private final String TAG = "RpmAidlManager";
    private final int MAX_TRACE_DEPTH = 5;
    private ArrayList<PowerStateSubsystemSleepState> subsystemSleepStateArrayList = new ArrayList<>();

    private RpmAidlManager() {
        try {
            Class<?> rpmhStub = Class.forName(IRPMH_STUB_CLASS_NAME);
            Method asInterface = rpmhStub.getMethod("asInterface", IBinder.class);
            IBinder rpmhBinder = ServiceManager.getService(IRPMH_SERVICE_CLASS_NAME);
            if (rpmhBinder == null) {
                Slog.d("RpmAidlManager", "rpmhBinder is null");
            } else {
                this.mRpmh = asInterface.invoke(rpmhStub, rpmhBinder);
                rpmhBinder.linkToDeath(this, 0);
            }
        } catch (Exception e) {
            Slog.d("RpmAidlManager", "get Rpmh service exception " + e.getMessage());
            StackTraceElement[] traces = e.getStackTrace();
            for (int i = 0; i < 5 && i < traces.length; i++) {
                Slog.d("RpmAidlManager", "get Rpmh service exception " + traces[i]);
            }
            this.mRpmh = null;
        }
    }

    public static RpmAidlManager getInstance() {
        if (sRpmAidl == null || !sRpmAidl.isAvailable()) {
            synchronized (RpmAidlManager.class) {
                if (sRpmAidl == null || !sRpmAidl.isAvailable()) {
                    sRpmAidl = new RpmAidlManager();
                }
            }
        }
        return sRpmAidl;
    }

    public boolean isAvailable() {
        return this.mRpmh != null;
    }

    public ArrayList<PowerStateSubsystemSleepState> getPowerStateSubsystemSleepStateList() {
        if (this.mRpmh == null) {
            return null;
        }
        try {
            Class<?> rpmhAidlClass = Class.forName(IRPMH_AIDL_CLASS_NAME);
            List<Object> powerStateSubsystemSleepStateList = new ArrayList<>();
            Method getPowerStateSubsystemSleepStateListMethod = rpmhAidlClass.getMethod("getPowerStateSubsystemSleepStateList", List.class);
            int ret = ((Integer) getPowerStateSubsystemSleepStateListMethod.invoke(this.mRpmh, powerStateSubsystemSleepStateList)).intValue();
            Slog.d("RpmAidlManager", "getPowerStateSubsystemSleepStateListMethod return code: " + ret);
            if (ret == 0) {
                this.subsystemSleepStateArrayList.clear();
                for (Object object : powerStateSubsystemSleepStateList) {
                    this.subsystemSleepStateArrayList.add(convetToPowerStateSubsystemSleepState(object));
                }
                return this.subsystemSleepStateArrayList;
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PowerStateSubsystemSleepState getPowerStateSubsystemSleepState(String name) {
        if (this.mRpmh == null) {
            return null;
        }
        try {
            Class<?> rpmhAidlClass = Class.forName(IRPMH_AIDL_CLASS_NAME);
            Class<?> PowerStateSubsystemSleepStateClass = Class.forName(CLASS_PowerStateSubsystemSleepState);
            Object powerStateSubsystemSleepState = PowerStateSubsystemSleepStateClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            Method getPowerStateSubsystemSleepStateListMethod = rpmhAidlClass.getMethod("getPowerStateSubsystemSleepState", String.class, PowerStateSubsystemSleepStateClass);
            getPowerStateSubsystemSleepStateListMethod.invoke(this.mRpmh, name, powerStateSubsystemSleepState);
            PowerStateSubsystemSleepState subsystemSleepState = convetToPowerStateSubsystemSleepState(powerStateSubsystemSleepState);
            Slog.d("RpmAidlManager", subsystemSleepState.toString());
            return subsystemSleepState;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static PowerStateSubsystemSleepState convetToPowerStateSubsystemSleepState(Object data) {
        try {
            Class<?> PowerStateSubsystemSleepStateClass = Class.forName(CLASS_PowerStateSubsystemSleepState);
            Object stateObj = PowerStateSubsystemSleepStateClass.cast(data);
            Method toStringMethod = PowerStateSubsystemSleepStateClass.getMethod("toString", new Class[0]);
            Field nameField = PowerStateSubsystemSleepStateClass.getDeclaredField("name");
            Field totalTransitionsField = PowerStateSubsystemSleepStateClass.getDeclaredField("totalTransitions");
            Field residencyInMsecSinceBootField = PowerStateSubsystemSleepStateClass.getDeclaredField("residencyInMsecSinceBoot");
            PowerStateSubsystemSleepStateClass.getFields();
            nameField.setAccessible(true);
            totalTransitionsField.setAccessible(true);
            residencyInMsecSinceBootField.setAccessible(true);
            PowerStateSubsystemSleepState state = new PowerStateSubsystemSleepState();
            if (nameField.getType().equals(String.class)) {
                state.name = (String) nameField.get(stateObj);
            }
            if (totalTransitionsField.getType().getTypeName().equals("long")) {
                state.totalTransitions = ((Long) totalTransitionsField.get(stateObj)).longValue();
            }
            if (residencyInMsecSinceBootField.getType().getTypeName().equals("long")) {
                state.residencyInMsecSinceBoot = ((Long) residencyInMsecSinceBootField.get(stateObj)).longValue();
            }
            return state;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        Slog.d("RpmAidlManager", "Rpmh aidl service died.");
        synchronized (RpmAidlManager.class) {
            this.mRpmh = null;
            sRpmAidl = null;
        }
    }
}
