package com.oplus.internal.telephony;

/* loaded from: classes.dex */
public class IccCardConstantsExt {
    public static final int SML_SLOT_LOCK_POLICY_ALL_SLOTS_INDIVIDUAL = 3;
    public static final int SML_SLOT_LOCK_POLICY_LK_SLOT1 = 4;
    public static final int SML_SLOT_LOCK_POLICY_LK_SLOT2 = 5;
    public static final int SML_SLOT_LOCK_POLICY_LK_SLOTA = 6;
    public static final int SML_SLOT_LOCK_POLICY_LK_SLOTA_RESTRICT_INVALID_CS = 7;
    public static final int SML_SLOT_LOCK_POLICY_LK_SLOTA_RESTRICT_INVALID_ECC_FOR_VALID_NO_SERVICE = 9;
    public static final int SML_SLOT_LOCK_POLICY_LK_SLOTA_RESTRICT_REVERSE = 8;
    public static final int SML_SLOT_LOCK_POLICY_LOCK_STATE_NO = 1;
    public static final int SML_SLOT_LOCK_POLICY_LOCK_STATE_UNKNOWN = -1;
    public static final int SML_SLOT_LOCK_POLICY_LOCK_STATE_YES = 0;
    public static final int SML_SLOT_LOCK_POLICY_NONE = 0;
    public static final int SML_SLOT_LOCK_POLICY_ONLY_SLOT1 = 1;
    public static final int SML_SLOT_LOCK_POLICY_ONLY_SLOT2 = 2;
    public static final int SML_SLOT_LOCK_POLICY_SERVICE_CAPABILITY_CS_INCOMING_ONLY = 6;
    public static final int SML_SLOT_LOCK_POLICY_SERVICE_CAPABILITY_CS_ONLY = 1;
    public static final int SML_SLOT_LOCK_POLICY_SERVICE_CAPABILITY_ECC_ONLY = 3;
    public static final int SML_SLOT_LOCK_POLICY_SERVICE_CAPABILITY_FULL = 0;
    public static final int SML_SLOT_LOCK_POLICY_SERVICE_CAPABILITY_FULL_INACTIVE = 7;
    public static final int SML_SLOT_LOCK_POLICY_SERVICE_CAPABILITY_NO_SERVICE = 4;
    public static final int SML_SLOT_LOCK_POLICY_SERVICE_CAPABILITY_PS_ONLY = 2;
    public static final int SML_SLOT_LOCK_POLICY_SERVICE_CAPABILITY_UNKNOWN = -1;
    public static final int SML_SLOT_LOCK_POLICY_SERVICE_CAPABILITY_VOICE_ONLY = 5;
    public static final int SML_SLOT_LOCK_POLICY_UNKNOWN = -1;
    public static final int SML_SLOT_LOCK_POLICY_UNLOCK_GENERAL_FAIL = 2;
    public static final int SML_SLOT_LOCK_POLICY_UNLOCK_INCORRECT_PASSWORD = 1;
    public static final int SML_SLOT_LOCK_POLICY_UNLOCK_NO_LOCK_POLICY = 0;
    public static final int SML_SLOT_LOCK_POLICY_UNLOCK_SUCCESS = 3;
    public static final int SML_SLOT_LOCK_POLICY_UNLOCK_UNKNOWN = -1;
    public static final int SML_SLOT_LOCK_POLICY_VALID_CARD_ABSENT = 2;
    public static final int SML_SLOT_LOCK_POLICY_VALID_CARD_NO = 1;
    public static final int SML_SLOT_LOCK_POLICY_VALID_CARD_UNKNOWN = -1;
    public static final int SML_SLOT_LOCK_POLICY_VALID_CARD_YES = 0;

    @Deprecated
    /* loaded from: classes.dex */
    public enum CardType {
        UIM_CARD(1),
        SIM_CARD(2),
        UIM_SIM_CARD(3),
        UNKNOW_CARD(4),
        CT_3G_UIM_CARD(5),
        CT_UIM_SIM_CARD(6),
        PIN_LOCK_CARD(7),
        CT_4G_UICC_CARD(8),
        NOT_CT_UICC_CARD(9),
        CT_EXCEL_GG_CARD(10),
        LOCKED_CARD(18),
        CARD_NOT_INSERTED(255);

        private int mValue;

        CardType(int value) {
            this.mValue = value;
        }

        public int getValue() {
            return this.mValue;
        }

        public boolean is4GCard() {
            return this == CT_4G_UICC_CARD || this == NOT_CT_UICC_CARD;
        }

        public static CardType getCardTypeFromInt(int cardTypeInt) {
            CardType cardType = UNKNOW_CARD;
            CardType[] cardTypes = values();
            if (cardTypes != null) {
                for (int i = 0; i < cardTypes.length; i++) {
                    if (cardTypes[i].getValue() == cardTypeInt) {
                        return cardTypes[i];
                    }
                }
                return cardType;
            }
            return cardType;
        }
    }

    /* loaded from: classes.dex */
    public enum VsimType {
        LOCAL_SIM,
        REMOTE_SIM,
        SOFT_AKA_SIM,
        PHYSICAL_AKA_SIM,
        PHYSICAL_SIM;

        public boolean isUserDataAllowed() {
            return this == SOFT_AKA_SIM || this == PHYSICAL_AKA_SIM;
        }

        public boolean isDataRoamingAllowed() {
            return this == SOFT_AKA_SIM || this == REMOTE_SIM;
        }

        public boolean isAllowVsimConnection() {
            return this == SOFT_AKA_SIM || this == PHYSICAL_AKA_SIM;
        }

        public boolean isAllowReqNonVsimNetwork() {
            return this != SOFT_AKA_SIM;
        }

        public boolean isAllowOnlyVsimNetwork() {
            return this == SOFT_AKA_SIM;
        }

        public boolean isVsimCard() {
            return (this == PHYSICAL_SIM || this == PHYSICAL_AKA_SIM) ? false : true;
        }
    }
}
