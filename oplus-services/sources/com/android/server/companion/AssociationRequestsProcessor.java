package com.android.server.companion;

import android.R;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.companion.AssociatedDevice;
import android.companion.AssociationInfo;
import android.companion.AssociationRequest;
import android.companion.IAssociationRequestCallback;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManagerInternal;
import android.net.MacAddress;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FunctionalUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"LongLogTag"})
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AssociationRequestsProcessor {
    private static final int ASSOCIATE_WITHOUT_PROMPT_MAX_PER_TIME_WINDOW = 5;
    private static final long ASSOCIATE_WITHOUT_PROMPT_WINDOW_MS = 3600000;
    private static final ComponentName ASSOCIATION_REQUEST_APPROVAL_ACTIVITY = ComponentName.createRelative("com.android.companiondevicemanager", ".CompanionDeviceActivity");
    private static final String EXTRA_APPLICATION_CALLBACK = "application_callback";
    private static final String EXTRA_ASSOCIATION = "association";
    private static final String EXTRA_ASSOCIATION_REQUEST = "association_request";
    private static final String EXTRA_FORCE_CANCEL_CONFIRMATION = "cancel_confirmation";
    private static final String EXTRA_MAC_ADDRESS = "mac_address";
    private static final String EXTRA_RESULT_RECEIVER = "result_receiver";
    private static final int RESULT_CODE_ASSOCIATION_APPROVED = 0;
    private static final int RESULT_CODE_ASSOCIATION_CREATED = 0;
    private static final String TAG = "CDM_AssociationRequestsProcessor";
    private final AssociationStoreImpl mAssociationStore;
    private final Context mContext;
    private final ResultReceiver mOnRequestConfirmationReceiver = new ResultReceiver(Handler.getMain()) { // from class: com.android.server.companion.AssociationRequestsProcessor.1
        @Override // android.os.ResultReceiver
        protected void onReceiveResult(int i, Bundle bundle) {
            MacAddress macAddress;
            if (i != 0) {
                Slog.w(AssociationRequestsProcessor.TAG, "Unknown result code:" + i);
                return;
            }
            AssociationRequest associationRequest = (AssociationRequest) bundle.getParcelable(AssociationRequestsProcessor.EXTRA_ASSOCIATION_REQUEST, AssociationRequest.class);
            IAssociationRequestCallback asInterface = IAssociationRequestCallback.Stub.asInterface(bundle.getBinder(AssociationRequestsProcessor.EXTRA_APPLICATION_CALLBACK));
            ResultReceiver resultReceiver = (ResultReceiver) bundle.getParcelable(AssociationRequestsProcessor.EXTRA_RESULT_RECEIVER, ResultReceiver.class);
            Objects.requireNonNull(associationRequest);
            Objects.requireNonNull(asInterface);
            Objects.requireNonNull(resultReceiver);
            if (associationRequest.isSelfManaged()) {
                macAddress = null;
            } else {
                macAddress = (MacAddress) bundle.getParcelable(AssociationRequestsProcessor.EXTRA_MAC_ADDRESS, MacAddress.class);
                Objects.requireNonNull(macAddress);
            }
            AssociationRequestsProcessor.this.processAssociationRequestApproval(associationRequest, asInterface, resultReceiver, macAddress);
        }
    };
    private final PackageManagerInternal mPackageManager;
    private final CompanionDeviceManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AssociationRequestsProcessor(CompanionDeviceManagerService companionDeviceManagerService, AssociationStoreImpl associationStoreImpl) {
        this.mContext = companionDeviceManagerService.getContext();
        this.mService = companionDeviceManagerService;
        this.mPackageManager = companionDeviceManagerService.mPackageManagerInternal;
        this.mAssociationStore = associationStoreImpl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void processNewAssociationRequest(AssociationRequest associationRequest, String str, int i, IAssociationRequestCallback iAssociationRequestCallback) {
        Objects.requireNonNull(associationRequest, "Request MUST NOT be null");
        if (associationRequest.isSelfManaged()) {
            Objects.requireNonNull(associationRequest.getDisplayName(), "AssociationRequest.displayName MUST NOT be null.");
        }
        Objects.requireNonNull(str, "Package name MUST NOT be null");
        Objects.requireNonNull(iAssociationRequestCallback, "Callback MUST NOT be null");
        int packageUid = this.mPackageManager.getPackageUid(str, 0L, i);
        PermissionsUtils.enforcePermissionsForAssociation(this.mContext, associationRequest, packageUid);
        PackageUtils.enforceUsesCompanionDeviceFeature(this.mContext, i, str);
        if (associationRequest.isSelfManaged() && !associationRequest.isForceConfirmation() && !willAddRoleHolder(associationRequest, str, i)) {
            createAssociationAndNotifyApplication(associationRequest, str, i, null, iAssociationRequestCallback, null);
            return;
        }
        associationRequest.setPackageName(str);
        associationRequest.setUserId(i);
        associationRequest.setSkipPrompt(mayAssociateWithoutPrompt(str, i));
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ASSOCIATION_REQUEST, associationRequest);
        bundle.putBinder(EXTRA_APPLICATION_CALLBACK, iAssociationRequestCallback.asBinder());
        bundle.putParcelable(EXTRA_RESULT_RECEIVER, Utils.prepareForIpc(this.mOnRequestConfirmationReceiver));
        Intent intent = new Intent();
        intent.setComponent(ASSOCIATION_REQUEST_APPROVAL_ACTIVITY);
        intent.putExtras(bundle);
        try {
            iAssociationRequestCallback.onAssociationPending(createPendingIntent(packageUid, intent));
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PendingIntent buildAssociationCancellationIntent(String str, int i) {
        Objects.requireNonNull(str, "Package name MUST NOT be null");
        PackageUtils.enforceUsesCompanionDeviceFeature(this.mContext, i, str);
        int packageUid = this.mPackageManager.getPackageUid(str, 0L, i);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_FORCE_CANCEL_CONFIRMATION, true);
        Intent intent = new Intent();
        intent.setComponent(ASSOCIATION_REQUEST_APPROVAL_ACTIVITY);
        intent.putExtras(bundle);
        return createPendingIntent(packageUid, intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAssociationRequestApproval(AssociationRequest associationRequest, IAssociationRequestCallback iAssociationRequestCallback, ResultReceiver resultReceiver, MacAddress macAddress) {
        String packageName = associationRequest.getPackageName();
        int userId = associationRequest.getUserId();
        try {
            PermissionsUtils.enforcePermissionsForAssociation(this.mContext, associationRequest, this.mPackageManager.getPackageUid(packageName, 0L, userId));
            createAssociationAndNotifyApplication(associationRequest, packageName, userId, macAddress, iAssociationRequestCallback, resultReceiver);
        } catch (SecurityException e) {
            try {
                iAssociationRequestCallback.onFailure(e.getMessage());
            } catch (RemoteException unused) {
            }
        }
    }

    private void createAssociationAndNotifyApplication(AssociationRequest associationRequest, String str, int i, MacAddress macAddress, IAssociationRequestCallback iAssociationRequestCallback, ResultReceiver resultReceiver) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            createAssociation(i, str, macAddress, associationRequest.getDisplayName(), associationRequest.getDeviceProfile(), associationRequest.getAssociatedDevice(), associationRequest.isSelfManaged(), iAssociationRequestCallback, resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void createAssociation(final int i, final String str, MacAddress macAddress, CharSequence charSequence, final String str2, AssociatedDevice associatedDevice, boolean z, final IAssociationRequestCallback iAssociationRequestCallback, final ResultReceiver resultReceiver) {
        final AssociationInfo associationInfo = new AssociationInfo(this.mService.getNewAssociationIdForPackage(i, str), i, str, macAddress, charSequence, str2, associatedDevice, z, false, false, System.currentTimeMillis(), Long.MAX_VALUE, 0);
        if (str2 != null) {
            RolesUtils.addRoleHolderForAssociation(this.mService.getContext(), associationInfo, new Consumer() { // from class: com.android.server.companion.AssociationRequestsProcessor$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AssociationRequestsProcessor.this.lambda$createAssociation$0(associationInfo, str2, iAssociationRequestCallback, resultReceiver, i, str, (Boolean) obj);
                }
            });
        } else {
            addAssociationToStore(associationInfo, null);
            sendCallbackAndFinish(associationInfo, iAssociationRequestCallback, resultReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createAssociation$0(AssociationInfo associationInfo, String str, IAssociationRequestCallback iAssociationRequestCallback, ResultReceiver resultReceiver, int i, String str2, Boolean bool) {
        if (bool.booleanValue()) {
            addAssociationToStore(associationInfo, str);
            sendCallbackAndFinish(associationInfo, iAssociationRequestCallback, resultReceiver);
            return;
        }
        Slog.e(TAG, "Failed to add u" + i + "\\" + str2 + " to the list of " + str + " holders.");
        sendCallbackAndFinish(null, iAssociationRequestCallback, resultReceiver);
    }

    public void enableSystemDataSync(int i, int i2) {
        AssociationInfo associationById = this.mAssociationStore.getAssociationById(i);
        this.mAssociationStore.updateAssociation(AssociationInfo.builder(associationById).setSystemDataSyncFlags(associationById.getSystemDataSyncFlags() | i2).build());
    }

    public void disableSystemDataSync(int i, int i2) {
        AssociationInfo associationById = this.mAssociationStore.getAssociationById(i);
        this.mAssociationStore.updateAssociation(AssociationInfo.builder(associationById).setSystemDataSyncFlags(associationById.getSystemDataSyncFlags() & (~i2)).build());
    }

    private void addAssociationToStore(AssociationInfo associationInfo, String str) {
        Slog.i(TAG, "New CDM association created=" + associationInfo);
        this.mAssociationStore.addAssociation(associationInfo);
        this.mService.updateSpecialAccessPermissionForAssociatedPackage(associationInfo);
        MetricUtils.logCreateAssociation(str);
    }

    private void sendCallbackAndFinish(AssociationInfo associationInfo, IAssociationRequestCallback iAssociationRequestCallback, ResultReceiver resultReceiver) {
        if (associationInfo == null) {
            if (iAssociationRequestCallback != null) {
                try {
                    iAssociationRequestCallback.onFailure("internal_error");
                } catch (RemoteException unused) {
                }
            }
            if (resultReceiver != null) {
                resultReceiver.send(3, new Bundle());
                return;
            }
            return;
        }
        if (iAssociationRequestCallback != null) {
            try {
                iAssociationRequestCallback.onAssociationCreated(associationInfo);
            } catch (RemoteException unused2) {
            }
        }
        if (resultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_ASSOCIATION, associationInfo);
            resultReceiver.send(0, bundle);
        }
    }

    private boolean willAddRoleHolder(AssociationRequest associationRequest, final String str, final int i) {
        if (associationRequest.getDeviceProfile() == null) {
            return false;
        }
        return !((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0019: RETURN 
              (wrap:boolean:NOT 
              (wrap:boolean:0x0013: INVOKE 
              (wrap:java.lang.Boolean:0x0011: CHECK_CAST (java.lang.Boolean) (wrap:java.lang.Object:0x000d: INVOKE 
              (wrap:com.android.internal.util.FunctionalUtils$ThrowingSupplier:0x000a: CONSTRUCTOR 
              (r1v0 'this' com.android.server.companion.AssociationRequestsProcessor A[DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'i' int A[DONT_INLINE])
              (r3v0 'str' java.lang.String A[DONT_INLINE])
              (r2 I:java.lang.String A[DONT_INLINE])
             A[DONT_GENERATE, MD:(com.android.server.companion.AssociationRequestsProcessor, int, java.lang.String, java.lang.String):void (m), REMOVE, WRAPPED] (LINE:386) call: com.android.server.companion.AssociationRequestsProcessor$$ExternalSyntheticLambda1.<init>(com.android.server.companion.AssociationRequestsProcessor, int, java.lang.String, java.lang.String):void type: CONSTRUCTOR)
             STATIC call: android.os.Binder.withCleanCallingIdentity(com.android.internal.util.FunctionalUtils$ThrowingSupplier):java.lang.Object A[DONT_GENERATE, MD:(com.android.internal.util.FunctionalUtils$ThrowingSupplier):java.lang.Object (s), REMOVE, WRAPPED] (LINE:386)))
             VIRTUAL call: java.lang.Boolean.booleanValue():boolean A[DONT_GENERATE, MD:():boolean (c), REMOVE, WRAPPED] (LINE:386))
             A[WRAPPED])
             (LINE:386) in method: com.android.server.companion.AssociationRequestsProcessor.willAddRoleHolder(android.companion.AssociationRequest, java.lang.String, int):boolean, file: C:\Users\HuangYW\Desktop\Realmeﾷﾴﾱ￠ￒ￫\services\classes.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:310)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:273)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:94)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:297)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:276)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:406)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:335)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:301)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            Caused by: java.lang.NullPointerException
            	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:810)
            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:730)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:418)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:145)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:121)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:108)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1117)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:884)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:422)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:145)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:121)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:108)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:345)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:145)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:121)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:108)
            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:97)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:852)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:422)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:145)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:121)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:108)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
            	at jadx.core.codegen.InsnGen.oneArgInsn(InsnGen.java:689)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:362)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:145)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:121)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:108)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:303)
            	... 19 more
            */
        /*
            this = this;
            java.lang.String r2 = r2.getDeviceProfile()
            if (r2 != 0) goto L8
            r1 = 0
            return r1
        L8:
            com.android.server.companion.AssociationRequestsProcessor$$ExternalSyntheticLambda1 r0 = new com.android.server.companion.AssociationRequestsProcessor$$ExternalSyntheticLambda1
            r0.<init>()
            java.lang.Object r1 = android.os.Binder.withCleanCallingIdentity(r0)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            r1 = r1 ^ 1
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.companion.AssociationRequestsProcessor.willAddRoleHolder(android.companion.AssociationRequest, java.lang.String, int):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$willAddRoleHolder$1(int i, String str, String str2) throws Exception {
        return Boolean.valueOf(RolesUtils.isRoleHolder(this.mContext, i, str, str2));
    }

    private PendingIntent createPendingIntent(int i, Intent intent) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return PendingIntent.getActivityAsUser(this.mContext, i, intent, 1409286144, null, UserHandle.CURRENT);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean mayAssociateWithoutPrompt(String str, int i) {
        String[] stringArray = this.mContext.getResources().getStringArray(R.array.config_displayCompositionColorModes);
        boolean z = false;
        if (!ArrayUtils.contains(stringArray, str)) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        Iterator<AssociationInfo> it = this.mAssociationStore.getAssociationsForPackage(i, str).iterator();
        int i2 = 0;
        while (true) {
            if (it.hasNext()) {
                if ((currentTimeMillis - it.next().getTimeApprovedMs() < 3600000) && (i2 = i2 + 1) >= 5) {
                    Slog.w(TAG, "Too many associations: " + str + " already associated " + i2 + " devices within the last 3600000ms");
                    return false;
                }
            } else {
                String[] stringArray2 = this.mContext.getResources().getStringArray(R.array.config_disabledUntilUsedPreinstalledImes);
                HashSet hashSet = new HashSet();
                for (int i3 = 0; i3 < stringArray.length; i3++) {
                    if (stringArray[i3].equals(str)) {
                        hashSet.add(stringArray2[i3].replaceAll(":", ""));
                    }
                }
                String[] computeSignaturesSha256Digests = android.util.PackageUtils.computeSignaturesSha256Digests(this.mPackageManager.getPackage(str).getSigningDetails().getSignatures());
                int length = computeSignaturesSha256Digests.length;
                int i4 = 0;
                while (true) {
                    if (i4 >= length) {
                        break;
                    }
                    if (hashSet.contains(computeSignaturesSha256Digests[i4])) {
                        z = true;
                        break;
                    }
                    i4++;
                }
                if (!z) {
                    Slog.w(TAG, "Certificate mismatch for allowlisted package " + str);
                }
                return z;
            }
        }
    }
}
