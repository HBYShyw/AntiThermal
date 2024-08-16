package com.ibm.icu.impl;

import java.util.Date;
import java.util.TimeZone;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class CalendarAstronomer {
    public static final long DAY_MS = 86400000;
    private static final double DEG_RAD = 0.017453292519943295d;
    static final long EPOCH_2000_MS = 946598400000L;
    public static final int HOUR_MS = 3600000;
    private static final double INVALID = Double.MIN_VALUE;
    static final double JD_EPOCH = 2447891.5d;
    public static final long JULIAN_EPOCH_MS = -210866760000000L;
    public static final int MINUTE_MS = 60000;
    private static final double PI2 = 6.283185307179586d;
    private static final double RAD_DEG = 57.29577951308232d;
    private static final double RAD_HOUR = 3.819718634205488d;
    public static final int SECOND_MS = 1000;
    public static final double SIDEREAL_DAY = 23.93446960027d;
    public static final double SIDEREAL_MONTH = 27.32166d;
    public static final double SIDEREAL_YEAR = 365.25636d;
    public static final double SOLAR_DAY = 24.065709816d;
    static final double SUN_E = 0.016713d;
    static final double SUN_ETA_G = 4.87650757829735d;
    static final double SUN_OMEGA_G = 4.935239984568769d;
    public static final double SYNODIC_MONTH = 29.530588853d;
    public static final double TROPICAL_YEAR = 365.242191d;
    static final double moonA = 384401.0d;
    static final double moonE = 0.0549d;
    static final double moonI = 0.08980357792017056d;
    static final double moonL0 = 5.556284436750021d;
    static final double moonN0 = 5.559050068029439d;
    static final double moonP0 = 0.6342598060246725d;
    static final double moonPi = 0.016592845198710092d;
    static final double moonT0 = 0.009042550854582622d;
    private transient double eclipObliquity;
    private long fGmtOffset;
    private double fLatitude;
    private double fLongitude;
    private transient double julianCentury;
    private transient double julianDay;
    private transient double meanAnomalySun;
    private transient double moonEclipLong;
    private transient double moonLongitude;
    private transient Equatorial moonPosition;
    private transient double siderealT0;
    private transient double siderealTime;
    private transient double sunLongitude;
    private long time;
    public static final SolarLongitude VERNAL_EQUINOX = new SolarLongitude(0.0d);
    public static final SolarLongitude SUMMER_SOLSTICE = new SolarLongitude(1.5707963267948966d);
    private static final double PI = 3.141592653589793d;
    public static final SolarLongitude AUTUMN_EQUINOX = new SolarLongitude(PI);
    public static final SolarLongitude WINTER_SOLSTICE = new SolarLongitude(4.71238898038469d);
    public static final MoonAge NEW_MOON = new MoonAge(0.0d);
    public static final MoonAge FIRST_QUARTER = new MoonAge(1.5707963267948966d);
    public static final MoonAge FULL_MOON = new MoonAge(PI);
    public static final MoonAge LAST_QUARTER = new MoonAge(4.71238898038469d);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface AngleFunc {
        double eval();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface CoordFunc {
        Equatorial eval();
    }

    public CalendarAstronomer() {
        this(System.currentTimeMillis());
    }

    public CalendarAstronomer(Date date) {
        this(date.getTime());
    }

    public CalendarAstronomer(long j) {
        this.fLongitude = 0.0d;
        this.fLatitude = 0.0d;
        this.fGmtOffset = 0L;
        this.julianDay = INVALID;
        this.julianCentury = INVALID;
        this.sunLongitude = INVALID;
        this.meanAnomalySun = INVALID;
        this.moonLongitude = INVALID;
        this.moonEclipLong = INVALID;
        this.eclipObliquity = INVALID;
        this.siderealT0 = INVALID;
        this.siderealTime = INVALID;
        this.moonPosition = null;
        this.time = j;
    }

    public CalendarAstronomer(double d, double d2) {
        this();
        this.fLongitude = normPI(d * DEG_RAD);
        this.fLatitude = normPI(d2 * DEG_RAD);
        this.fGmtOffset = (long) (((this.fLongitude * 24.0d) * 3600000.0d) / PI2);
    }

    public void setTime(long j) {
        this.time = j;
        clearCache();
    }

    public void setDate(Date date) {
        setTime(date.getTime());
    }

    public void setJulianDay(double d) {
        this.time = ((long) (8.64E7d * d)) + JULIAN_EPOCH_MS;
        clearCache();
        this.julianDay = d;
    }

    public long getTime() {
        return this.time;
    }

    public Date getDate() {
        return new Date(this.time);
    }

    public double getJulianDay() {
        if (this.julianDay == INVALID) {
            this.julianDay = (this.time - JULIAN_EPOCH_MS) / 8.64E7d;
        }
        return this.julianDay;
    }

    public double getJulianCentury() {
        if (this.julianCentury == INVALID) {
            this.julianCentury = (getJulianDay() - 2415020.0d) / 36525.0d;
        }
        return this.julianCentury;
    }

    public double getGreenwichSidereal() {
        if (this.siderealTime == INVALID) {
            this.siderealTime = normalize(getSiderealOffset() + (normalize(this.time / 3600000.0d, 24.0d) * 1.002737909d), 24.0d);
        }
        return this.siderealTime;
    }

    private double getSiderealOffset() {
        if (this.siderealT0 == INVALID) {
            double floor = ((Math.floor(getJulianDay() - 0.5d) + 0.5d) - 2451545.0d) / 36525.0d;
            this.siderealT0 = normalize((2400.051336d * floor) + 6.697374558d + (2.5862E-5d * floor * floor), 24.0d);
        }
        return this.siderealT0;
    }

    public double getLocalSidereal() {
        return normalize(getGreenwichSidereal() + (this.fGmtOffset / 3600000.0d), 24.0d);
    }

    private long lstToUT(double d) {
        double normalize = normalize((d - getSiderealOffset()) * 0.9972695663d, 24.0d);
        long j = this.time;
        long j2 = this.fGmtOffset;
        return ((((j + j2) / DAY_MS) * DAY_MS) - j2) + ((long) (normalize * 3600000.0d));
    }

    public final Equatorial eclipticToEquatorial(Ecliptic ecliptic) {
        return eclipticToEquatorial(ecliptic.longitude, ecliptic.latitude);
    }

    public final Equatorial eclipticToEquatorial(double d, double d2) {
        double eclipticObliquity = eclipticObliquity();
        double sin = Math.sin(eclipticObliquity);
        double cos = Math.cos(eclipticObliquity);
        double sin2 = Math.sin(d);
        return new Equatorial(Math.atan2((sin2 * cos) - (Math.tan(d2) * sin), Math.cos(d)), Math.asin((Math.sin(d2) * cos) + (Math.cos(d2) * sin * sin2)));
    }

    public final Equatorial eclipticToEquatorial(double d) {
        return eclipticToEquatorial(d, 0.0d);
    }

    public Horizon eclipticToHorizon(double d) {
        Equatorial eclipticToEquatorial = eclipticToEquatorial(d);
        double localSidereal = ((getLocalSidereal() * PI) / 12.0d) - eclipticToEquatorial.ascension;
        double sin = Math.sin(localSidereal);
        double cos = Math.cos(localSidereal);
        double sin2 = Math.sin(eclipticToEquatorial.declination);
        double cos2 = Math.cos(eclipticToEquatorial.declination);
        double sin3 = Math.sin(this.fLatitude);
        double cos3 = Math.cos(this.fLatitude);
        double asin = Math.asin((sin2 * sin3) + (cos2 * cos3 * cos));
        return new Horizon(Math.atan2((-cos2) * cos3 * sin, sin2 - (sin3 * Math.sin(asin))), asin);
    }

    public double getSunLongitude() {
        if (this.sunLongitude == INVALID) {
            double[] sunLongitude = getSunLongitude(getJulianDay());
            this.sunLongitude = sunLongitude[0];
            this.meanAnomalySun = sunLongitude[1];
        }
        return this.sunLongitude;
    }

    double[] getSunLongitude(double d) {
        double norm2PI = norm2PI((norm2PI((d - JD_EPOCH) * 0.017202791632524146d) + SUN_ETA_G) - SUN_OMEGA_G);
        return new double[]{norm2PI(trueAnomaly(norm2PI, SUN_E) + SUN_OMEGA_G), norm2PI};
    }

    public Equatorial getSunPosition() {
        return eclipticToEquatorial(getSunLongitude(), 0.0d);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private static class SolarLongitude {
        double value;

        SolarLongitude(double d) {
            this.value = d;
        }
    }

    public long getSunTime(double d, boolean z) {
        return timeOfAngle(new AngleFunc() { // from class: com.ibm.icu.impl.CalendarAstronomer.1
            @Override // com.ibm.icu.impl.CalendarAstronomer.AngleFunc
            public double eval() {
                return CalendarAstronomer.this.getSunLongitude();
            }
        }, d, 365.242191d, 60000L, z);
    }

    public long getSunTime(SolarLongitude solarLongitude, boolean z) {
        return getSunTime(solarLongitude.value, z);
    }

    public long getSunRiseSet(boolean z) {
        long j = this.time;
        long j2 = this.fGmtOffset;
        setTime(((((j + j2) / DAY_MS) * DAY_MS) - j2) + 43200000 + ((z ? -6L : 6L) * 3600000));
        long riseOrSet = riseOrSet(new CoordFunc() { // from class: com.ibm.icu.impl.CalendarAstronomer.2
            @Override // com.ibm.icu.impl.CalendarAstronomer.CoordFunc
            public Equatorial eval() {
                return CalendarAstronomer.this.getSunPosition();
            }
        }, z, 0.009302604913129777d, 0.009890199094634533d, 5000L);
        setTime(j);
        return riseOrSet;
    }

    public Equatorial getMoonPosition() {
        if (this.moonPosition == null) {
            double sunLongitude = getSunLongitude();
            double julianDay = getJulianDay() - JD_EPOCH;
            double norm2PI = norm2PI((0.22997150421858628d * julianDay) + moonL0);
            double norm2PI2 = norm2PI((norm2PI - (0.001944368345221015d * julianDay)) - moonP0);
            double sin = Math.sin(((norm2PI - sunLongitude) * 2.0d) - norm2PI2) * 0.022233749341155764d;
            double sin2 = Math.sin(this.meanAnomalySun) * 0.003242821750205464d;
            double sin3 = norm2PI2 + ((sin - sin2) - (Math.sin(this.meanAnomalySun) * 0.00645771823237902d));
            double sin4 = (((norm2PI + sin) + (Math.sin(sin3) * 0.10975677534091541d)) - sin2) + (Math.sin(sin3 * 2.0d) * 0.0037350045992678655d);
            this.moonLongitude = sin4;
            this.moonLongitude += Math.sin((sin4 - sunLongitude) * 2.0d) * 0.011489502465878671d;
            double norm2PI3 = norm2PI(moonN0 - (julianDay * 9.242199067718253E-4d)) - (Math.sin(this.meanAnomalySun) * 0.0027925268031909274d);
            double sin5 = Math.sin(this.moonLongitude - norm2PI3);
            this.moonEclipLong = Math.atan2(Math.cos(moonI) * sin5, Math.cos(this.moonLongitude - norm2PI3)) + norm2PI3;
            this.moonPosition = eclipticToEquatorial(this.moonEclipLong, Math.asin(sin5 * Math.sin(moonI)));
        }
        return this.moonPosition;
    }

    public double getMoonAge() {
        getMoonPosition();
        return norm2PI(this.moonEclipLong - this.sunLongitude);
    }

    public double getMoonPhase() {
        return (1.0d - Math.cos(getMoonAge())) * 0.5d;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private static class MoonAge {
        double value;

        MoonAge(double d) {
            this.value = d;
        }
    }

    public long getMoonTime(double d, boolean z) {
        return timeOfAngle(new AngleFunc() { // from class: com.ibm.icu.impl.CalendarAstronomer.3
            @Override // com.ibm.icu.impl.CalendarAstronomer.AngleFunc
            public double eval() {
                return CalendarAstronomer.this.getMoonAge();
            }
        }, d, 29.530588853d, 60000L, z);
    }

    public long getMoonTime(MoonAge moonAge, boolean z) {
        return getMoonTime(moonAge.value, z);
    }

    public long getMoonRiseSet(boolean z) {
        return riseOrSet(new CoordFunc() { // from class: com.ibm.icu.impl.CalendarAstronomer.4
            @Override // com.ibm.icu.impl.CalendarAstronomer.CoordFunc
            public Equatorial eval() {
                return CalendarAstronomer.this.getMoonPosition();
            }
        }, z, 0.009302604913129777d, 0.009890199094634533d, 60000L);
    }

    private long timeOfAngle(AngleFunc angleFunc, double d, double d2, long j, boolean z) {
        double eval = angleFunc.eval();
        double d3 = 8.64E7d * d2;
        double norm2PI = ((norm2PI(d - eval) + (z ? 0.0d : -6.283185307179586d)) * d3) / PI2;
        long j2 = this.time;
        setTime(((long) norm2PI) + j2);
        while (true) {
            double eval2 = angleFunc.eval();
            double abs = Math.abs(norm2PI / normPI(eval2 - eval)) * normPI(d - eval2);
            if (Math.abs(abs) > Math.abs(norm2PI)) {
                long j3 = (long) (d3 / 8.0d);
                if (!z) {
                    j3 = -j3;
                }
                setTime(j2 + j3);
                return timeOfAngle(angleFunc, d, d2, j, z);
            }
            setTime(this.time + ((long) abs));
            if (Math.abs(abs) <= j) {
                return this.time;
            }
            norm2PI = abs;
            eval = eval2;
        }
    }

    private long riseOrSet(CoordFunc coordFunc, boolean z, double d, double d2, long j) {
        Equatorial eval;
        long j2;
        double tan = Math.tan(this.fLatitude);
        int i = 0;
        do {
            eval = coordFunc.eval();
            double acos = Math.acos((-tan) * Math.tan(eval.declination));
            if (z) {
                acos = PI2 - acos;
            }
            long lstToUT = lstToUT(((acos + eval.ascension) * 24.0d) / PI2);
            j2 = lstToUT - this.time;
            setTime(lstToUT);
            i++;
            if (i >= 5) {
                break;
            }
        } while (Math.abs(j2) > j);
        double cos = Math.cos(eval.declination);
        long asin = (long) ((((Math.asin(Math.sin((d / 2.0d) + d2) / Math.sin(Math.acos(Math.sin(this.fLatitude) / cos))) * 240.0d) * RAD_DEG) / cos) * 1000.0d);
        long j3 = this.time;
        if (z) {
            asin = -asin;
        }
        return j3 + asin;
    }

    private static final double normalize(double d, double d2) {
        return d - (d2 * Math.floor(d / d2));
    }

    private static final double norm2PI(double d) {
        return normalize(d, PI2);
    }

    private static final double normPI(double d) {
        return normalize(d + PI, PI2) - PI;
    }

    private double trueAnomaly(double d, double d2) {
        double sin;
        double d3 = d;
        do {
            sin = (d3 - (Math.sin(d3) * d2)) - d;
            d3 -= sin / (1.0d - (Math.cos(d3) * d2));
        } while (Math.abs(sin) > 1.0E-5d);
        return Math.atan(Math.tan(d3 / 2.0d) * Math.sqrt((d2 + 1.0d) / (1.0d - d2))) * 2.0d;
    }

    private double eclipticObliquity() {
        if (this.eclipObliquity == INVALID) {
            double julianDay = (getJulianDay() - 2451545.0d) / 36525.0d;
            this.eclipObliquity = (((23.439292d - (0.013004166666666666d * julianDay)) - ((1.6666666666666665E-7d * julianDay) * julianDay)) + (5.027777777777778E-7d * julianDay * julianDay * julianDay)) * DEG_RAD;
        }
        return this.eclipObliquity;
    }

    private void clearCache() {
        this.julianDay = INVALID;
        this.julianCentury = INVALID;
        this.sunLongitude = INVALID;
        this.meanAnomalySun = INVALID;
        this.moonLongitude = INVALID;
        this.moonEclipLong = INVALID;
        this.eclipObliquity = INVALID;
        this.siderealTime = INVALID;
        this.siderealT0 = INVALID;
        this.moonPosition = null;
    }

    public String local(long j) {
        return new Date(j - TimeZone.getDefault().getRawOffset()).toString();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class Ecliptic {
        public final double latitude;
        public final double longitude;

        public Ecliptic(double d, double d2) {
            this.latitude = d;
            this.longitude = d2;
        }

        public String toString() {
            return Double.toString(this.longitude * CalendarAstronomer.RAD_DEG) + "," + (this.latitude * CalendarAstronomer.RAD_DEG);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class Equatorial {
        public final double ascension;
        public final double declination;

        public Equatorial(double d, double d2) {
            this.ascension = d;
            this.declination = d2;
        }

        public String toString() {
            return Double.toString(this.ascension * CalendarAstronomer.RAD_DEG) + "," + (this.declination * CalendarAstronomer.RAD_DEG);
        }

        public String toHmsString() {
            return CalendarAstronomer.radToHms(this.ascension) + "," + CalendarAstronomer.radToDms(this.declination);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class Horizon {
        public final double altitude;
        public final double azimuth;

        public Horizon(double d, double d2) {
            this.altitude = d;
            this.azimuth = d2;
        }

        public String toString() {
            return Double.toString(this.altitude * CalendarAstronomer.RAD_DEG) + "," + (this.azimuth * CalendarAstronomer.RAD_DEG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String radToHms(double d) {
        double d2 = d * RAD_HOUR;
        int i = (int) d2;
        double d3 = d2 - i;
        int i2 = (int) (d3 * 60.0d);
        return Integer.toString(i) + "h" + i2 + "m" + ((int) ((d3 - (i2 / 60.0d)) * 3600.0d)) + "s";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String radToDms(double d) {
        double d2 = d * RAD_DEG;
        int i = (int) d2;
        double d3 = d2 - i;
        int i2 = (int) (d3 * 60.0d);
        return Integer.toString(i) + "°" + i2 + "'" + ((int) ((d3 - (i2 / 60.0d)) * 3600.0d)) + "\"";
    }
}
