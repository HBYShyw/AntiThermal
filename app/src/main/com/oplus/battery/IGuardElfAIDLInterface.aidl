/************************************************************************************
 ** File: - IGuardElfAIDLInterface.aidl
 ** VENDOR_EDIT
 ** Copyright (C), 2008-2014, OPLUS Mobile Comm Corp., Ltd
 **
 ** Description:
 **      This file,which defines an AIDL interface,is added
 ** for modifying the UI responses and methods to read
 ** or write files.
 **
 ** Version: 0.1
 ** Date created: 2016-05-06
 ** Author: Jiadong.Qiao@Plf.Framework
 **
 ** --------------------------- Revision History: --------------------------------
 **     <author>                                <data>                  <desc>
 **     Jiadong.Qiao@Plf.Framework            2016-05-06            First Version
 ************************************************************************************/

package com.oplus.battery;

interface IGuardElfAIDLInterface {

    void onChiefPrefChanged(boolean isChecked);

    List<String> getList(String path);
}