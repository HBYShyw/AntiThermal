package com.oplus.util;

import android.content.Context;
import android.net.wifi.OplusWifiManager;
import android.os.OplusPropertyList;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/* loaded from: classes.dex */
public class OplusUnitConversionUtils {
    private static final String B = " B";
    private static final String B_S = " B/s";
    private static final String GB = " GB";
    private static final String GB_S = " GB/s";
    private static final double HUNDRED = 100.0d;
    private static final String KB = " KB";
    private static final String KB_S = " KB/s";
    private static final String MB = " MB";
    private static final String MB_S = " MB/s";
    private static final double MILLION = 1000000.0d;
    private static final String NOPOINT = "0";
    private static final String ONEPOINT = "0.0";
    private static final String PB = " PB";
    private static final String PB_S = " PB/s";
    private static final String SIXPOINT = "0.00000";
    private static final double SPECIAL = 1024.0d;
    private static final int SQUARE_FIVE = 5;
    private static final int SQUARE_FOUR = 4;
    private static final int SQUARE_THREE = 3;
    private static final String TAG = "OplusUnitConversionUtils";
    private static final String TB = " TB";
    private static final String TB_S = " TB/s";
    private static final double TEN = 10.0d;
    private static final double THOUSAND = 1000.0d;
    private static final String TWOPOINT = "0.00";
    private Context mContext;
    private String mMoreDownLoad;
    private String mMostDownLoad;
    private String mSpecialPoint;

    public OplusUnitConversionUtils(Context context) {
        this.mMoreDownLoad = null;
        this.mMostDownLoad = null;
        this.mSpecialPoint = "0.98";
        this.mContext = context;
        this.mMoreDownLoad = context.getResources().getString(201588939);
        this.mMostDownLoad = context.getResources().getString(201588940);
        this.mSpecialPoint = formatLocaleNumber(0.98d, TWOPOINT);
    }

    private boolean isChinese() {
        String country = this.mContext.getResources().getConfiguration().locale.getCountry();
        if (country != null) {
            if (country.equalsIgnoreCase(OplusPropertyList.OPLUS_VERSION) || country.equalsIgnoreCase("TW") || country.equalsIgnoreCase("HK")) {
                return true;
            }
            return false;
        }
        return false;
    }

    private String formatNumber(double number, String pointNum, boolean isRound) {
        DecimalFormat df = new DecimalFormat(pointNum, new DecimalFormatSymbols(Locale.CHINA));
        if (!isRound) {
            df.setRoundingMode(RoundingMode.FLOOR);
        } else {
            df.setRoundingMode(RoundingMode.HALF_UP);
        }
        String stringNum = df.format(number);
        return stringNum;
    }

    private String formatLocaleNumber(double number, String pointNum) {
        DecimalFormat df = new DecimalFormat(pointNum, new DecimalFormatSymbols(this.mContext.getResources().getConfiguration().locale));
        String stringNum = df.format(number);
        return stringNum;
    }

    private String getChineseDownloadValue(long number) {
        if (0 <= number && number < 10000.0d) {
            if (number == 0) {
                number++;
            }
            String downloadValue = number + " ";
            return downloadValue;
        }
        if (10000.0d <= number && number < 100000.0d) {
            double value = Double.valueOf(formatNumber(number / 10000.0d, ONEPOINT, true)).doubleValue();
            int temp = (int) value;
            if (value == temp) {
                String downloadValue2 = temp + this.mMoreDownLoad;
                return downloadValue2;
            }
            String downloadValue3 = value + this.mMoreDownLoad;
            return downloadValue3;
        }
        if (100000.0d <= number && number < MILLION) {
            double value2 = Double.valueOf(formatNumber(number / 10000.0d, ONEPOINT, true)).doubleValue();
            int temp2 = (int) value2;
            if (value2 == temp2) {
                String downloadValue4 = temp2 + this.mMoreDownLoad;
                return downloadValue4;
            }
            String downloadValue5 = value2 + this.mMoreDownLoad;
            return downloadValue5;
        }
        if (MILLION <= number && number < 1.0E7d) {
            double value3 = Double.valueOf(formatNumber(number / 10000.0d, TWOPOINT, true)).doubleValue();
            String downloadValue6 = ((int) value3) + this.mMoreDownLoad;
            return downloadValue6;
        }
        if (1.0E7d <= number && number < 1.0E8d) {
            double value4 = Double.valueOf(formatNumber(number / 10000.0d, TWOPOINT, true)).doubleValue();
            String downloadValue7 = ((int) value4) + this.mMoreDownLoad;
            return downloadValue7;
        }
        if (number >= 1.0E8d) {
            double value5 = Double.valueOf(formatNumber(number / 1.0E8d, SIXPOINT, true)).doubleValue();
            String downloadValue8 = formatNumber(value5, ONEPOINT, false) + this.mMostDownLoad;
            return downloadValue8;
        }
        throw new IllegalArgumentException("the value of the incoming is wrong");
    }

    private String getEnglishDownloadValue(long number) {
        long number2 = number;
        if (0 <= number2 && number2 < 10000.0d) {
            if (number2 == 0) {
                number2++;
            }
            String downloadValue = number2 + " ";
            return downloadValue;
        }
        if (10000.0d <= number2 && number2 < 100000.0d) {
            String downloadValue2 = ((int) (TEN * Double.valueOf(formatNumber(number2 / 10000.0d, ONEPOINT, true)).doubleValue())) + this.mMoreDownLoad;
            return downloadValue2;
        }
        if (100000.0d <= number2 && number2 < MILLION) {
            String downloadValue3 = ((int) (TEN * Double.valueOf(formatNumber(number2 / 10000.0d, ONEPOINT, true)).doubleValue())) + this.mMoreDownLoad;
            return downloadValue3;
        }
        if (MILLION <= number2 && number2 < 1.0E7d) {
            String tempString = formatNumber(number2 / 10000.0d, TWOPOINT, true);
            double value = Double.valueOf(tempString).doubleValue() / HUNDRED;
            int temp = (int) value;
            if (value == temp) {
                String downloadValue4 = temp + this.mMostDownLoad;
                return downloadValue4;
            }
            String downloadValue5 = Double.valueOf(tempString) + this.mMostDownLoad;
            return downloadValue5;
        }
        if (1.0E7d > number2 || number2 >= 1.0E8d) {
            if (number2 >= 1.0E8d) {
                String downloadValue6 = ((int) (Double.valueOf(formatNumber(Double.valueOf(formatNumber(number2 / 1.0E8d, SIXPOINT, true)).doubleValue(), ONEPOINT, false)).doubleValue() * HUNDRED)) + this.mMostDownLoad;
                return downloadValue6;
            }
            throw new IllegalArgumentException("the value of the incoming is wrong");
        }
        String tempString2 = formatNumber(number2 / 10000.0d, TWOPOINT, true);
        double value2 = Double.valueOf(tempString2).doubleValue() / HUNDRED;
        int temp2 = (int) value2;
        if (value2 == temp2) {
            String downloadValue7 = temp2 + this.mMostDownLoad;
            return downloadValue7;
        }
        String downloadValue8 = Double.valueOf(tempString2) + this.mMostDownLoad;
        return downloadValue8;
    }

    private String getChineseStripValue(long number) {
        if (0 <= number && number < 10000.0d) {
            String stripValue = number + " ";
            return stripValue;
        }
        if (10000.0d > number || number >= MILLION) {
            if (MILLION <= number && number < 1.0E8d) {
                String stripValue2 = formatNumber(number / 10000.0d, "0", true) + this.mMoreDownLoad;
                return stripValue2;
            }
            throw new IllegalArgumentException("the value of the incoming is wrong");
        }
        double value = Double.valueOf(formatNumber(number / 10000.0d, ONEPOINT, true)).doubleValue();
        int temp = (int) value;
        if (value == temp) {
            String stripValue3 = temp + this.mMoreDownLoad;
            return stripValue3;
        }
        String stripValue4 = value + this.mMoreDownLoad;
        return stripValue4;
    }

    private String getEnglishStripValue(long number) {
        if (0 <= number && number < 10000.0d) {
            String stripValue = number + " ";
            return stripValue;
        }
        if (10000.0d <= number && number < MILLION) {
            double value = Double.valueOf(formatNumber(number / 10000.0d, ONEPOINT, true)).doubleValue();
            String stripValue2 = ((int) (TEN * value)) + this.mMoreDownLoad;
            return stripValue2;
        }
        if (MILLION <= number && number < 1.0E8d) {
            String stripValue3 = ((int) (Double.valueOf(formatNumber(number / 10000.0d, "0", true)).doubleValue() * TEN)) + this.mMoreDownLoad;
            return stripValue3;
        }
        throw new IllegalArgumentException("the value of the incoming is wrong");
    }

    public String getUnitValue(long number) {
        return getTransformUnitValue(number, SPECIAL);
    }

    public String getUnitThousandValue(long number) {
        return getTransformUnitValue(number, THOUSAND);
    }

    public String getTransformUnitValue(long number, double unit) throws IllegalArgumentException {
        String str;
        String str2;
        String str3;
        String unitValue;
        long j;
        String str4;
        String str5;
        if (0 <= number && number < THOUSAND) {
            String tempString = formatNumber(number, "0", true);
            long temp = Long.valueOf(tempString).longValue();
            String tempString2 = formatLocaleNumber(Double.valueOf(tempString).doubleValue(), "0");
            if (THOUSAND <= temp && temp < SPECIAL) {
                return getUnitValue(temp);
            }
            String unitValue2 = tempString2 + B;
            return unitValue2;
        }
        if (THOUSAND <= number && number < 1024000.0d) {
            String tempString3 = formatNumber(number / unit, "0", true);
            long temp2 = Long.valueOf(tempString3).longValue() * ((long) unit);
            String tempString4 = formatLocaleNumber(Double.valueOf(tempString3).doubleValue(), "0");
            if (1024000.0d <= temp2 && temp2 < Math.pow(SPECIAL, 2.0d) * HUNDRED) {
                return getTransformUnitValue(temp2, unit);
            }
            String unitValue3 = tempString4 + KB;
            return unitValue3;
        }
        if (1024000.0d > number) {
            str = MB;
        } else {
            if (number < Math.pow(SPECIAL, 2.0d) * HUNDRED) {
                String tempString5 = formatNumber(number / Math.pow(unit, 2.0d), ONEPOINT, true);
                long temp3 = (long) (Double.valueOf(tempString5).doubleValue() * Math.pow(unit, 2.0d));
                String tempString6 = formatLocaleNumber(Double.valueOf(tempString5).doubleValue(), ONEPOINT);
                if (Math.pow(SPECIAL, 2.0d) * HUNDRED <= temp3 && temp3 < Math.pow(SPECIAL, 2.0d) * THOUSAND) {
                    return getTransformUnitValue(temp3, unit);
                }
                String unitValue4 = tempString6 + MB;
                return unitValue4;
            }
            str = MB;
        }
        if (Math.pow(SPECIAL, 2.0d) * HUNDRED > number) {
            str2 = "0";
        } else {
            if (number < Math.pow(SPECIAL, 2.0d) * THOUSAND) {
                String tempString7 = formatNumber(number / Math.pow(unit, 2.0d), "0", true);
                long temp4 = (long) (Double.valueOf(tempString7).doubleValue() * Math.pow(unit, 2.0d));
                String tempString8 = formatLocaleNumber(Double.valueOf(tempString7).doubleValue(), "0");
                if (Math.pow(SPECIAL, 2.0d) * THOUSAND <= temp4 && temp4 < Math.pow(SPECIAL, 3.0d)) {
                    return getTransformUnitValue(temp4, unit);
                }
                String unitValue5 = tempString8 + str;
                return unitValue5;
            }
            str2 = "0";
        }
        if (Math.pow(SPECIAL, 2.0d) * THOUSAND <= number) {
            unitValue = null;
            if (number < Math.pow(SPECIAL, 3.0d)) {
                if (unit == THOUSAND) {
                    String tempString9 = formatNumber(number / Math.pow(unit, 3.0d), TWOPOINT, true);
                    return formatLocaleNumber(Double.valueOf(tempString9).doubleValue(), TWOPOINT) + GB;
                }
                if (unit != SPECIAL) {
                    str4 = null;
                    String unitValue6 = str4;
                    return unitValue6;
                }
                if (number > Math.pow(SPECIAL, 2.0d) * 1023.0d) {
                    return getUnitValue((long) Math.pow(SPECIAL, 3.0d));
                }
                String unitValue7 = this.mSpecialPoint + GB;
                return unitValue7;
            }
            str3 = GB;
        } else {
            str3 = GB;
            unitValue = null;
        }
        if (Math.pow(SPECIAL, 3.0d) > number) {
            j = number;
        } else {
            if (number < Math.pow(SPECIAL, 3.0d) * TEN) {
                String tempString10 = formatNumber(number / Math.pow(unit, 3.0d), TWOPOINT, true);
                long temp5 = (long) (Double.valueOf(tempString10).doubleValue() * Math.pow(unit, 3.0d));
                String tempString11 = formatLocaleNumber(Double.valueOf(tempString10).doubleValue(), TWOPOINT);
                if (Math.pow(SPECIAL, 3.0d) * TEN <= temp5 && temp5 < Math.pow(SPECIAL, 3.0d) * HUNDRED) {
                    return getTransformUnitValue(temp5, unit);
                }
                String unitValue8 = tempString11 + str3;
                return unitValue8;
            }
            j = number;
        }
        str4 = unitValue;
        if (Math.pow(SPECIAL, 3.0d) * TEN <= j && j < Math.pow(SPECIAL, 3.0d) * HUNDRED) {
            String tempString12 = formatNumber(j / Math.pow(unit, 3.0d), ONEPOINT, true);
            long temp6 = (long) (Double.valueOf(tempString12).doubleValue() * Math.pow(unit, 3.0d));
            String tempString13 = formatLocaleNumber(Double.valueOf(tempString12).doubleValue(), ONEPOINT);
            if (Math.pow(SPECIAL, 3.0d) * HUNDRED <= temp6 && temp6 < Math.pow(SPECIAL, 3.0d) * THOUSAND) {
                return getTransformUnitValue(temp6, unit);
            }
            return tempString13 + str3;
        }
        if (Math.pow(SPECIAL, 3.0d) * HUNDRED <= j && j < Math.pow(SPECIAL, 3.0d) * THOUSAND) {
            String tempString14 = formatNumber(j / Math.pow(unit, 3.0d), str2, true);
            long temp7 = (long) (Double.valueOf(tempString14).doubleValue() * Math.pow(unit, 3.0d));
            String tempString15 = formatLocaleNumber(Double.valueOf(tempString14).doubleValue(), str2);
            if (Math.pow(SPECIAL, 3.0d) * THOUSAND <= temp7 && temp7 < Math.pow(SPECIAL, 4.0d)) {
                return getTransformUnitValue(temp7, unit);
            }
            return tempString15 + str3;
        }
        if (Math.pow(SPECIAL, 3.0d) * THOUSAND <= j) {
            str5 = str2;
            if (j < Math.pow(SPECIAL, 4.0d)) {
                if (unit == THOUSAND) {
                    String tempString16 = formatNumber(j / Math.pow(unit, 4.0d), TWOPOINT, true);
                    return formatLocaleNumber(Double.valueOf(tempString16).doubleValue(), TWOPOINT) + TB;
                }
                if (unit == SPECIAL) {
                    if (j > Math.pow(SPECIAL, 3.0d) * 1023.0d) {
                        return getUnitValue((long) Math.pow(SPECIAL, 4.0d));
                    }
                    String unitValue9 = this.mSpecialPoint + TB;
                    return unitValue9;
                }
                String unitValue62 = str4;
                return unitValue62;
            }
        } else {
            str5 = str2;
        }
        if (Math.pow(SPECIAL, 4.0d) <= j && j < Math.pow(SPECIAL, 4.0d) * TEN) {
            String tempString17 = formatNumber(j / Math.pow(unit, 4.0d), TWOPOINT, true);
            long temp8 = (long) (Double.valueOf(tempString17).doubleValue() * Math.pow(unit, 4.0d));
            String tempString18 = formatLocaleNumber(Double.valueOf(tempString17).doubleValue(), TWOPOINT);
            if (Math.pow(SPECIAL, 4.0d) * TEN <= temp8 && temp8 < Math.pow(SPECIAL, 4.0d) * HUNDRED) {
                return getTransformUnitValue(temp8, unit);
            }
            String unitValue10 = tempString18 + TB;
            return unitValue10;
        }
        if (Math.pow(SPECIAL, 4.0d) * TEN <= j && j < Math.pow(SPECIAL, 4.0d) * HUNDRED) {
            String tempString19 = formatNumber(j / Math.pow(unit, 4.0d), ONEPOINT, true);
            long temp9 = (long) (Double.valueOf(tempString19).doubleValue() * Math.pow(unit, 4.0d));
            String tempString20 = formatLocaleNumber(Double.valueOf(tempString19).doubleValue(), ONEPOINT);
            if (Math.pow(SPECIAL, 4.0d) * HUNDRED <= temp9 && temp9 < Math.pow(SPECIAL, 4.0d) * THOUSAND) {
                return getTransformUnitValue(temp9, unit);
            }
            String unitValue11 = tempString20 + TB;
            return unitValue11;
        }
        if (Math.pow(SPECIAL, 4.0d) * HUNDRED <= j && j < Math.pow(SPECIAL, 4.0d) * THOUSAND) {
            String tempString21 = formatNumber(j / Math.pow(unit, 4.0d), str5, true);
            long temp10 = (long) (Double.valueOf(tempString21).doubleValue() * Math.pow(unit, 4.0d));
            String tempString22 = formatLocaleNumber(Double.valueOf(tempString21).doubleValue(), str5);
            if (Math.pow(SPECIAL, 4.0d) * THOUSAND <= temp10 && temp10 < Math.pow(SPECIAL, 5.0d)) {
                return getTransformUnitValue(temp10, unit);
            }
            String unitValue12 = tempString22 + TB;
            return unitValue12;
        }
        if (Math.pow(SPECIAL, 4.0d) * THOUSAND <= j && j < Math.pow(SPECIAL, 5.0d)) {
            if (unit == THOUSAND) {
                String tempString23 = formatNumber(j / Math.pow(unit, 5.0d), TWOPOINT, true);
                return formatLocaleNumber(Double.valueOf(tempString23).doubleValue(), TWOPOINT) + PB;
            }
            if (unit == SPECIAL) {
                if (j > Math.pow(SPECIAL, 4.0d) * 1023.0d) {
                    return getUnitValue((long) Math.pow(SPECIAL, 5.0d));
                }
                String unitValue13 = this.mSpecialPoint + PB;
                return unitValue13;
            }
            String unitValue622 = str4;
            return unitValue622;
        }
        if (Math.pow(SPECIAL, 5.0d) <= j && j < Math.pow(SPECIAL, 5.0d) * TEN) {
            String tempString24 = formatNumber(j / Math.pow(SPECIAL, 5.0d), TWOPOINT, true);
            long temp11 = (long) (Double.valueOf(tempString24).doubleValue() * Math.pow(SPECIAL, 5.0d));
            String tempString25 = formatLocaleNumber(Double.valueOf(tempString24).doubleValue(), TWOPOINT);
            if (Math.pow(SPECIAL, 5.0d) * TEN <= temp11 && temp11 < Math.pow(SPECIAL, 5.0d) * HUNDRED) {
                return getUnitValue(temp11);
            }
            String unitValue14 = tempString25 + PB;
            return unitValue14;
        }
        if (Math.pow(SPECIAL, 5.0d) * TEN > j || j >= Math.pow(SPECIAL, 5.0d) * HUNDRED) {
            if (Math.pow(SPECIAL, 5.0d) * HUNDRED <= j && j < Math.pow(SPECIAL, 5.0d) * THOUSAND) {
                String unitValue15 = formatLocaleNumber(j / Math.pow(SPECIAL, 5.0d), str5) + PB;
                return unitValue15;
            }
            throw new IllegalArgumentException("the value of the incoming is wrong");
        }
        String tempString26 = formatNumber(j / Math.pow(SPECIAL, 5.0d), ONEPOINT, true);
        long temp12 = (long) (Double.valueOf(tempString26).doubleValue() * Math.pow(SPECIAL, 5.0d));
        String tempString27 = formatLocaleNumber(Double.valueOf(tempString26).doubleValue(), ONEPOINT);
        if (Math.pow(SPECIAL, 5.0d) * HUNDRED <= temp12 && temp12 < Math.pow(SPECIAL, 5.0d) * THOUSAND) {
            return getUnitValue(temp12);
        }
        String unitValue16 = tempString27 + PB;
        return unitValue16;
    }

    public String getSpeedValue(long number) throws IllegalArgumentException {
        String str;
        String str2;
        String str3;
        if (0 <= number && number < THOUSAND) {
            String tempString = formatNumber(number, "0", true);
            long temp = Long.valueOf(tempString).longValue();
            String tempString2 = formatLocaleNumber(Double.valueOf(tempString).doubleValue(), "0");
            if (THOUSAND <= temp && temp < SPECIAL) {
                return getUnitValue(temp);
            }
            String speedValue = tempString2 + B_S;
            return speedValue;
        }
        if (THOUSAND <= number && number < 1024000.0d) {
            String tempString3 = formatNumber(number / SPECIAL, "0", true);
            long temp2 = Long.valueOf(tempString3).longValue() * OplusWifiManager.OPLUS_WIFI_FEATURE_IOTConnect;
            String tempString4 = formatLocaleNumber(Double.valueOf(tempString3).doubleValue(), "0");
            if (1024000.0d <= temp2 && temp2 < Math.pow(SPECIAL, 2.0d) * HUNDRED) {
                return getUnitValue(temp2);
            }
            String speedValue2 = tempString4 + KB_S;
            return speedValue2;
        }
        if (1024000.0d <= number && number < Math.pow(SPECIAL, 2.0d) * HUNDRED) {
            String tempString5 = formatNumber(number / Math.pow(SPECIAL, 2.0d), ONEPOINT, true);
            long temp3 = (long) (Double.valueOf(tempString5).doubleValue() * Math.pow(SPECIAL, 2.0d));
            String tempString6 = formatLocaleNumber(Double.valueOf(tempString5).doubleValue(), ONEPOINT);
            if (Math.pow(SPECIAL, 2.0d) * HUNDRED <= temp3 && temp3 < Math.pow(SPECIAL, 2.0d) * THOUSAND) {
                return getUnitValue(temp3);
            }
            String speedValue3 = tempString6 + MB_S;
            return speedValue3;
        }
        if (Math.pow(SPECIAL, 2.0d) * HUNDRED <= number && number < Math.pow(SPECIAL, 2.0d) * THOUSAND) {
            String tempString7 = formatNumber(number / Math.pow(SPECIAL, 2.0d), "0", true);
            long temp4 = (long) (Double.valueOf(tempString7).doubleValue() * Math.pow(SPECIAL, 2.0d));
            String tempString8 = formatLocaleNumber(Double.valueOf(tempString7).doubleValue(), "0");
            if (Math.pow(SPECIAL, 2.0d) * THOUSAND <= temp4 && temp4 < Math.pow(SPECIAL, 3.0d)) {
                return getUnitValue(temp4);
            }
            String speedValue4 = tempString8 + MB_S;
            return speedValue4;
        }
        if (Math.pow(SPECIAL, 2.0d) * THOUSAND <= number) {
            str = "0";
            if (number < Math.pow(SPECIAL, 3.0d)) {
                if (number > Math.pow(SPECIAL, 2.0d) * 1023.0d) {
                    return getUnitValue((long) Math.pow(SPECIAL, 3.0d));
                }
                String speedValue5 = this.mSpecialPoint + GB_S;
                return speedValue5;
            }
        } else {
            str = "0";
        }
        if (Math.pow(SPECIAL, 3.0d) > number) {
            str2 = GB_S;
        } else {
            if (number < Math.pow(SPECIAL, 3.0d) * TEN) {
                String tempString9 = formatNumber(number / Math.pow(SPECIAL, 3.0d), TWOPOINT, true);
                long temp5 = (long) (Double.valueOf(tempString9).doubleValue() * Math.pow(SPECIAL, 3.0d));
                String tempString10 = formatLocaleNumber(Double.valueOf(tempString9).doubleValue(), TWOPOINT);
                if (Math.pow(SPECIAL, 3.0d) * TEN <= temp5 && temp5 < Math.pow(SPECIAL, 3.0d) * HUNDRED) {
                    return getUnitValue(temp5);
                }
                String speedValue6 = tempString10 + GB_S;
                return speedValue6;
            }
            str2 = GB_S;
        }
        String str4 = str2;
        if (Math.pow(SPECIAL, 3.0d) * TEN <= number && number < Math.pow(SPECIAL, 3.0d) * HUNDRED) {
            String tempString11 = formatNumber(number / Math.pow(SPECIAL, 3.0d), ONEPOINT, true);
            long temp6 = (long) (Double.valueOf(tempString11).doubleValue() * Math.pow(SPECIAL, 3.0d));
            String tempString12 = formatLocaleNumber(Double.valueOf(tempString11).doubleValue(), ONEPOINT);
            if (Math.pow(SPECIAL, 3.0d) * HUNDRED <= temp6 && temp6 < Math.pow(SPECIAL, 3.0d) * THOUSAND) {
                return getUnitValue(temp6);
            }
            String speedValue7 = tempString12 + str4;
            return speedValue7;
        }
        if (Math.pow(SPECIAL, 3.0d) * HUNDRED <= number) {
            str3 = ONEPOINT;
            if (number < Math.pow(SPECIAL, 3.0d) * THOUSAND) {
                String str5 = str;
                String tempString13 = formatNumber(number / Math.pow(SPECIAL, 3.0d), str5, true);
                long temp7 = (long) (Double.valueOf(tempString13).doubleValue() * Math.pow(SPECIAL, 3.0d));
                String tempString14 = formatLocaleNumber(Double.valueOf(tempString13).doubleValue(), str5);
                if (Math.pow(SPECIAL, 3.0d) * THOUSAND > temp7 || temp7 >= Math.pow(SPECIAL, 4.0d)) {
                    String speedValue8 = tempString14 + str4;
                    return speedValue8;
                }
                return getUnitValue(temp7);
            }
        } else {
            str3 = ONEPOINT;
        }
        String str6 = str;
        if (Math.pow(SPECIAL, 3.0d) * THOUSAND <= number && number < Math.pow(SPECIAL, 4.0d)) {
            if (number > Math.pow(SPECIAL, 3.0d) * 1023.0d) {
                return getUnitValue((long) Math.pow(SPECIAL, 4.0d));
            }
            String speedValue9 = this.mSpecialPoint + TB_S;
            return speedValue9;
        }
        if (Math.pow(SPECIAL, 4.0d) <= number && number < Math.pow(SPECIAL, 4.0d) * TEN) {
            String tempString15 = formatNumber(number / Math.pow(SPECIAL, 4.0d), TWOPOINT, true);
            long temp8 = (long) (Double.valueOf(tempString15).doubleValue() * Math.pow(SPECIAL, 4.0d));
            String tempString16 = formatLocaleNumber(Double.valueOf(tempString15).doubleValue(), TWOPOINT);
            if (Math.pow(SPECIAL, 4.0d) * TEN <= temp8 && temp8 < Math.pow(SPECIAL, 4.0d) * HUNDRED) {
                return getUnitValue(temp8);
            }
            String speedValue10 = tempString16 + TB_S;
            return speedValue10;
        }
        if (Math.pow(SPECIAL, 4.0d) * TEN <= number && number < Math.pow(SPECIAL, 4.0d) * HUNDRED) {
            String str7 = str3;
            String tempString17 = formatNumber(number / Math.pow(SPECIAL, 4.0d), str7, true);
            long temp9 = (long) (Double.valueOf(tempString17).doubleValue() * Math.pow(SPECIAL, 4.0d));
            String tempString18 = formatLocaleNumber(Double.valueOf(tempString17).doubleValue(), str7);
            if (Math.pow(SPECIAL, 4.0d) * HUNDRED <= temp9 && temp9 < Math.pow(SPECIAL, 4.0d) * THOUSAND) {
                return getUnitValue(temp9);
            }
            String speedValue11 = tempString18 + TB_S;
            return speedValue11;
        }
        String str8 = str3;
        if (Math.pow(SPECIAL, 4.0d) * HUNDRED <= number && number < Math.pow(SPECIAL, 4.0d) * THOUSAND) {
            String tempString19 = formatNumber(number / Math.pow(SPECIAL, 4.0d), str6, true);
            long temp10 = (long) (Double.valueOf(tempString19).doubleValue() * Math.pow(SPECIAL, 4.0d));
            if (Math.pow(SPECIAL, 4.0d) * THOUSAND <= temp10 && temp10 < Math.pow(SPECIAL, 5.0d)) {
                return getUnitValue(temp10);
            }
            String speedValue12 = tempString19 + TB_S;
            return speedValue12;
        }
        if (Math.pow(SPECIAL, 4.0d) * THOUSAND <= number && number < Math.pow(SPECIAL, 5.0d)) {
            if (number > Math.pow(SPECIAL, 4.0d) * 1023.0d) {
                return getUnitValue((long) Math.pow(SPECIAL, 5.0d));
            }
            String speedValue13 = this.mSpecialPoint + PB_S;
            return speedValue13;
        }
        if (Math.pow(SPECIAL, 5.0d) <= number && number < Math.pow(SPECIAL, 5.0d) * TEN) {
            String tempString20 = formatNumber(number / Math.pow(SPECIAL, 5.0d), TWOPOINT, true);
            long temp11 = (long) (Double.valueOf(tempString20).doubleValue() * Math.pow(SPECIAL, 5.0d));
            String tempString21 = formatLocaleNumber(Double.valueOf(tempString20).doubleValue(), TWOPOINT);
            if (Math.pow(SPECIAL, 5.0d) * TEN <= temp11 && temp11 < Math.pow(SPECIAL, 5.0d) * HUNDRED) {
                return getUnitValue(temp11);
            }
            String speedValue14 = tempString21 + PB_S;
            return speedValue14;
        }
        if (Math.pow(SPECIAL, 5.0d) * TEN > number || number >= Math.pow(SPECIAL, 5.0d) * HUNDRED) {
            if (Math.pow(SPECIAL, 5.0d) * HUNDRED <= number && number < Math.pow(SPECIAL, 5.0d) * THOUSAND) {
                String speedValue15 = formatLocaleNumber(number / Math.pow(SPECIAL, 5.0d), str6) + PB_S;
                return speedValue15;
            }
            throw new IllegalArgumentException("the value of the incoming is wrong");
        }
        String tempString22 = formatNumber(number / Math.pow(SPECIAL, 5.0d), str8, true);
        long temp12 = (long) (Double.valueOf(tempString22).doubleValue() * Math.pow(SPECIAL, 5.0d));
        String tempString23 = formatLocaleNumber(Double.valueOf(tempString22).doubleValue(), str8);
        if (Math.pow(SPECIAL, 5.0d) * HUNDRED <= temp12 && temp12 < Math.pow(SPECIAL, 5.0d) * THOUSAND) {
            return getUnitValue(temp12);
        }
        String speedValue16 = tempString23 + PB_S;
        return speedValue16;
    }

    public String getDownLoadValue(long number) throws IllegalArgumentException {
        if (isChinese()) {
            String downloadValue = getChineseDownloadValue(number);
            return downloadValue;
        }
        String downloadValue2 = getEnglishDownloadValue(number);
        return downloadValue2;
    }

    public String getStripValue(long number) throws IllegalArgumentException {
        if (isChinese()) {
            String stripValue = getChineseStripValue(number);
            return stripValue;
        }
        String stripValue2 = getEnglishStripValue(number);
        return stripValue2;
    }
}
