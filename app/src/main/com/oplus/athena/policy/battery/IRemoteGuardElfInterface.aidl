/***********************************************************
 ** Copyright (C), 2008-2017, OPLUS Mobile Comm Corp., Ltd.
 ** OPLUSOS_EDIT
 ** File:
 ** Description:
 ** Version:
 ** Date :
 ** Author:
 **
 ** ---------------------Revision History: ---------------------
 **  <author>    <data>    <version >    <desc>
 ****************************************************************/
// IRemoteGuardElfInterface.aidl
package com.oplus.athena.policy.battery;

// Declare any non-default types here with import statements

interface IRemoteGuardElfInterface {
    void setGuardElfSwitch(boolean isChecked, String pkgName);

    List<String> getList(String path);

    boolean removeRedDot(String pkgName);

    void removeRedDotList(in List<String> redDotList);

    void onBgFrozenPrefChanged(boolean isChecked, String pkgName);

    List<String> getTrustList();

    List<String> getHideTrustList();

    void onPowerProtectPolicyChange(String pkgName, int policy);

    void restoreDefaultWhitelist();
}
