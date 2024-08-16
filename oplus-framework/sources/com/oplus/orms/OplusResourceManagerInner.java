package com.oplus.orms;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.oplus.orms.info.OrmsCtrlDataParam;
import com.oplus.orms.info.OrmsNotifyParam;
import com.oplus.orms.info.OrmsSaParam;
import java.security.SecureRandom;
import java.util.Arrays;

/* loaded from: classes.dex */
class OplusResourceManagerInner {
    private static final String DESCRIPTOR = "com.oplus.orms.OplusResourceManagerService";
    private static final int ORMS_BINDER_CHECK_ACCESS_PERMISSION = 1;
    private static final int ORMS_BINDER_CLEAR_SCENE_ACTION = 3;
    private static final int ORMS_BINDER_GET_MODE_STATUS = 9;
    private static final int ORMS_BINDER_GET_PERF_LIMIT = 10;
    private static final int ORMS_BINDER_READ_FILE = 7;
    private static final int ORMS_BINDER_READ_FILE_LIST = 8;
    private static final int ORMS_BINDER_RESET_CTRL_DATA = 6;
    private static final int ORMS_BINDER_SET_CTRL_DATA = 5;
    private static final int ORMS_BINDER_SET_NOTIFICATION = 4;
    private static final int ORMS_BINDER_SET_SCENE_ACTION = 2;
    private static final String ORMS_SERVICE = "OplusResourceManagerService";
    private static final String TAG = "Orms_ManagerInner";
    private static IBinder sRemote;
    private static volatile OplusResourceManagerInner sOrmsManagerInner = null;
    private static IBinder.DeathRecipient sDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.orms.OplusResourceManagerInner.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Slog.e(OplusResourceManagerInner.TAG, "OplusResourceManagerService binderDied!!!");
            OplusResourceManagerInner.sRemote = null;
        }
    };

    private OplusResourceManagerInner() {
        connectOrmsCoreService();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static OplusResourceManagerInner getInstance() {
        if (sOrmsManagerInner == null) {
            synchronized (OplusResourceManagerInner.class) {
                if (sOrmsManagerInner == null) {
                    sOrmsManagerInner = new OplusResourceManagerInner();
                }
            }
        }
        return sOrmsManagerInner;
    }

    private static IBinder connectOrmsCoreService() {
        IBinder checkService = ServiceManager.checkService(ORMS_SERVICE);
        sRemote = checkService;
        if (checkService != null) {
            try {
                checkService.linkToDeath(sDeathRecipient, 0);
            } catch (RemoteException e) {
                sRemote = null;
            }
        }
        return sRemote;
    }

    private static boolean ormsCoreEnable() {
        if (sRemote == null && connectOrmsCoreService() == null) {
            Slog.e(TAG, "cannot connect to orms core service!");
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int checkAccessPermission(String identity) {
        int result = -1;
        if (!ormsCoreEnable()) {
            return -1;
        }
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeString(identity);
                sRemote.transact(1, data, reply, 0);
                result = reply.readInt() == 1 ? 1 : 0;
            } catch (Exception e) {
                Slog.e(TAG, "check access permission failed! because " + e.getMessage());
                e.printStackTrace();
            }
            return result;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    private long obtainRandomHandle() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return Math.abs(random.nextLong());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long setSceneAction(String identity, OrmsSaParam ormsSaParam) {
        if (!ormsCoreEnable()) {
            return -1L;
        }
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        long request = obtainRandomHandle();
        try {
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeString(identity);
                data.writeString(ormsSaParam.scene);
                data.writeString(ormsSaParam.action);
                data.writeInt(ormsSaParam.timeout);
                data.writeLong(request);
                sRemote.transact(2, data, reply, 1);
            } catch (Exception e) {
                Slog.e(TAG, "try to set scene action failed! because " + e.getMessage());
                e.printStackTrace();
            }
            return request;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clrSceneAction(String identity, long request) {
        if (!ormsCoreEnable()) {
            return;
        }
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeString(identity);
                data.writeLong(request);
                sRemote.transact(3, data, reply, 1);
            } catch (Exception e) {
                Slog.e(TAG, "try to clear scene action failed! because " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setNotification(String identity, OrmsNotifyParam ormsNotifyParam) {
        if (!ormsCoreEnable()) {
            return;
        }
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeString(identity);
                data.writeInt(ormsNotifyParam.msgSrc);
                data.writeInt(ormsNotifyParam.msgType);
                data.writeInt(ormsNotifyParam.param1);
                data.writeInt(ormsNotifyParam.param2);
                data.writeInt(ormsNotifyParam.param3);
                data.writeString(ormsNotifyParam.param4);
                sRemote.transact(4, data, reply, 1);
            } catch (Exception e) {
                Slog.e(TAG, "try to set notification failed! because " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setCtrlData(String identity, OrmsCtrlDataParam ctrlData) {
        if (!ormsCoreEnable()) {
            return;
        }
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeString(identity);
                int cpuClusterNum = ctrlData.cpuClusterNum;
                int gpuClusterNum = ctrlData.gpuClusterNum;
                data.writeInt(cpuClusterNum);
                data.writeInt(gpuClusterNum);
                for (int m = 0; m < cpuClusterNum; m++) {
                    data.writeInt(ctrlData.cpuCoreCtrlData[m][0]);
                    data.writeInt(ctrlData.cpuCoreCtrlData[m][1]);
                    data.writeInt(ctrlData.cpuFreqCtrlData[m][0]);
                    data.writeInt(ctrlData.cpuFreqCtrlData[m][1]);
                    data.writeInt(ctrlData.cpuCtrlType[m]);
                }
                for (int m2 = 0; m2 < gpuClusterNum; m2++) {
                    data.writeInt(ctrlData.gpuCoreCtrlData[m2][0]);
                    data.writeInt(ctrlData.gpuCoreCtrlData[m2][1]);
                    data.writeInt(ctrlData.gpuFreqCtrlData[m2][0]);
                    data.writeInt(ctrlData.gpuFreqCtrlData[m2][1]);
                    data.writeInt(ctrlData.gpuCtrlType[m2]);
                }
                for (int m3 = 0; m3 < cpuClusterNum - 1; m3++) {
                    data.writeInt(ctrlData.cpuMigData[m3][0]);
                    data.writeInt(ctrlData.cpuMigData[m3][1]);
                }
                sRemote.transact(5, data, reply, 1);
            } catch (Exception e) {
                Slog.e(TAG, "try to set ctrl data failed! because " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clrCtrlData(String identity) {
        if (!ormsCoreEnable()) {
            return;
        }
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeString(identity);
                sRemote.transact(6, data, reply, 1);
            } catch (Exception e) {
                Slog.e(TAG, "try to clear ctrl data failed! because " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getModeStatus(String identity, int mode) {
        int result = -1;
        if (!ormsCoreEnable()) {
            return -1;
        }
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeString(identity);
                data.writeInt(mode);
                sRemote.transact(9, data, reply, 0);
                result = reply.readInt();
            } catch (Exception e) {
                Slog.e(TAG, "try to get mode status failed! because " + e.getMessage());
                e.printStackTrace();
            }
            return result;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long[][][] getPerfLimit(String identity) {
        Parcel reply;
        char c = 0;
        long[][][] result = new long[0][];
        long[] jArr = new long[0];
        if (!ormsCoreEnable()) {
            return null;
        }
        Parcel data = Parcel.obtain();
        Parcel reply2 = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
        } catch (Exception e) {
            e = e;
        } catch (Throwable th) {
            e = th;
        }
        try {
            data.writeString(identity);
            sRemote.transact(10, data, reply2, 0);
            reply2.readException();
            long[] temp = reply2.createLongArray();
            data.recycle();
            reply2.recycle();
            if (temp != null) {
                try {
                    if (temp.length >= 5) {
                        int index = 0 + 1;
                        try {
                            int timeLen = (int) temp[0];
                            int index2 = index + 1;
                            try {
                                int cpuLen = (int) temp[index];
                                int index3 = index2 + 1;
                                try {
                                    int gpuLen = (int) temp[index2];
                                    int index4 = index3 + 1;
                                    try {
                                        int schedLen = (int) temp[index3];
                                        int index5 = index4 + 1;
                                        try {
                                            int limitLen = (int) temp[index4];
                                            result = new long[limitLen][];
                                            long total = (((cpuLen * 4) + timeLen + (gpuLen * 4) + (schedLen * 2)) * limitLen) + 5;
                                            if (temp.length == total) {
                                                int i = 0;
                                                while (i < limitLen) {
                                                    long[][] limit = new long[11];
                                                    int i2 = index5 + timeLen;
                                                    int index6 = i2;
                                                    try {
                                                        limit[c] = Arrays.copyOfRange(temp, index5, i2);
                                                        int index7 = index6 + cpuLen;
                                                        limit[1] = Arrays.copyOfRange(temp, index6, index7);
                                                        int index8 = index7 + cpuLen;
                                                        limit[2] = Arrays.copyOfRange(temp, index7, index8);
                                                        int index9 = index8 + cpuLen;
                                                        limit[3] = Arrays.copyOfRange(temp, index8, index9);
                                                        int index10 = index9 + cpuLen;
                                                        limit[4] = Arrays.copyOfRange(temp, index9, index10);
                                                        int index11 = index10 + gpuLen;
                                                        limit[5] = Arrays.copyOfRange(temp, index10, index11);
                                                        int index12 = index11 + gpuLen;
                                                        limit[6] = Arrays.copyOfRange(temp, index11, index12);
                                                        int index13 = index12 + gpuLen;
                                                        limit[7] = Arrays.copyOfRange(temp, index12, index13);
                                                        int index14 = index13 + gpuLen;
                                                        limit[8] = Arrays.copyOfRange(temp, index13, index14);
                                                        int i3 = index14 + schedLen;
                                                        index6 = i3;
                                                        limit[9] = Arrays.copyOfRange(temp, index14, i3);
                                                        int index15 = index6 + schedLen;
                                                        try {
                                                            limit[10] = Arrays.copyOfRange(temp, index6, index15);
                                                            result[i] = limit;
                                                            i++;
                                                            index5 = index15;
                                                            c = 0;
                                                        } catch (Exception e2) {
                                                            e = e2;
                                                            e.printStackTrace();
                                                            return null;
                                                        }
                                                    } catch (Exception e3) {
                                                        e = e3;
                                                    }
                                                }
                                            }
                                            return result;
                                        } catch (Exception e4) {
                                            e = e4;
                                        }
                                    } catch (Exception e5) {
                                        e = e5;
                                    }
                                } catch (Exception e6) {
                                    e = e6;
                                }
                            } catch (Exception e7) {
                                e = e7;
                            }
                        } catch (Exception e8) {
                            e = e8;
                        }
                    }
                } catch (Exception e9) {
                    e = e9;
                }
            }
            return result;
        } catch (Exception e10) {
            e = e10;
            reply = reply2;
            try {
                Slog.e(TAG, "try to get performance limit! because " + e.getMessage());
                e.printStackTrace();
                data.recycle();
                reply.recycle();
                return null;
            } catch (Throwable th2) {
                e = th2;
                data.recycle();
                reply.recycle();
                throw e;
            }
        } catch (Throwable th3) {
            e = th3;
            reply = reply2;
            data.recycle();
            reply.recycle();
            throw e;
        }
    }
}
