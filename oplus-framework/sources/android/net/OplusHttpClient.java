package android.net;

import android.content.Context;
import android.os.HapticPlayer;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.format.Time;
import android.util.Log;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: classes.dex */
public class OplusHttpClient {
    private static final long AVERAGE_RECEIVE_TIME = 832;
    private static final boolean DEBUG = true;
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 123;
    private static final long GMT_BEIJING_OFFSET = 28800000;
    private static final String TAG = "OplusHttpClient";
    private static final long VALID_LAST_TIME_THRESHOLD = 1500;
    private static long mLastGotSuccessLocaltime = 0;
    private long mHttpTime;
    private long mHttpTimeReference;
    private long mRoundTripTime;
    private InetSocketAddress mServerSocketAddress;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MyX509TrustManager implements X509TrustManager {
        private MyX509TrustManager() {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {
        }

        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public boolean requestTime(Context context, int selServerUrl, int timeout) {
        return forceRefreshTimeFromOplusServer(context, selServerUrl, timeout);
    }

    private boolean forceRefreshTimeFromOplusServer(Context context, int selServerUrl, int timeout) {
        HttpsURLConnection httpconn;
        int timeout2;
        URL url;
        StringBuffer sb;
        String mDateTimeXmlString;
        InputStreamReader mInputStreamReader;
        Log.d(TAG, "Enter forceRefreshTimeFromOplusServer run");
        try {
            String oplusServerURL = SystemProperties.get("ro.oplus.server_url_1", SplitPathManager.DEFAULT);
            if (selServerUrl > 0) {
                try {
                    oplusServerURL = SystemProperties.get("ro.oplus.server_url_2", SplitPathManager.DEFAULT);
                } catch (Exception e) {
                    e = e;
                    Log.e(TAG, "oplusServer exception: " + e);
                    return false;
                }
            }
            if (SplitPathManager.DEFAULT.equals(oplusServerURL)) {
                Log.e(TAG, "Property return default.");
                return false;
            }
            String oplusServerURL2 = oplusServerURL + System.currentTimeMillis();
            SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, new TrustManager[]{new MyX509TrustManager()}, new SecureRandom());
            URL url2 = new URL(oplusServerURL2);
            try {
                Log.i(TAG, "Cur http request:" + oplusServerURL2);
                HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() { // from class: android.net.OplusHttpClient.1
                    @Override // javax.net.ssl.HostnameVerifier
                    public boolean verify(String s, SSLSession sslsession) {
                        Log.i(OplusHttpClient.TAG, "WARNING: Hostname is not matched for cert.");
                        return true;
                    }
                };
                String proxyHost = Proxy.getDefaultHost();
                int proxyPort = Proxy.getDefaultPort();
                HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
                HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
                Log.d(TAG, "OplusServer proxyHost = " + proxyHost + " proxyPort = " + proxyPort);
                if (getNetType(context)) {
                    Log.d(TAG, "Get network type success!");
                    this.mServerSocketAddress = new InetSocketAddress(DEFAULT_HOST, 123);
                    httpconn = (HttpsURLConnection) url2.openConnection();
                    Log.d(TAG, "HttpURLConnection open openConnection success!");
                } else {
                    Log.d(TAG, "Use http proxy!");
                    this.mServerSocketAddress = new InetSocketAddress(DEFAULT_HOST, 123);
                    java.net.Proxy proxy = new java.net.Proxy(Proxy.Type.HTTP, this.mServerSocketAddress);
                    httpconn = (HttpsURLConnection) url2.openConnection(proxy);
                }
                httpconn.setRequestMethod("GET");
                httpconn.setDoInput(true);
                httpconn.setUseCaches(false);
                httpconn.setInstanceFollowRedirects(false);
                httpconn.setRequestProperty("Accept-Charset", "UTF-8");
                if (selServerUrl <= 0) {
                    timeout2 = timeout;
                } else {
                    timeout2 = timeout * 3;
                }
                try {
                    Log.d(TAG, "timeout:" + timeout2);
                    httpconn.setConnectTimeout(timeout2);
                    httpconn.setReadTimeout(timeout2);
                    long requestTicks = SystemClock.elapsedRealtime();
                    Log.d(TAG, "Strart to connect http server!");
                    httpconn.connect();
                    Log.d(TAG, "Connect http server success!");
                    BufferedReader mBufferedReader = null;
                    StringBuffer sb2 = new StringBuffer();
                    long mBeginParseTime = 0;
                    this.mHttpTimeReference = 0L;
                    int responseCode = httpconn.getResponseCode();
                    Log.d(TAG, "Http responseCode:" + responseCode);
                    if (responseCode != 200) {
                        url = url2;
                        sb = sb2;
                        mDateTimeXmlString = "";
                        mInputStreamReader = null;
                    } else {
                        mBeginParseTime = System.currentTimeMillis();
                        try {
                            mInputStreamReader = new InputStreamReader(httpconn.getInputStream(), "utf-8");
                            mBufferedReader = new BufferedReader(mInputStreamReader);
                            mDateTimeXmlString = "";
                            while (true) {
                                String lineString = mBufferedReader.readLine();
                                if (lineString == null) {
                                    break;
                                }
                                StringBuffer sb3 = sb2;
                                sb3.append(lineString);
                                url = url2;
                                try {
                                    Log.d(TAG, "Read response, lineString=" + lineString + ",sb=" + sb3.toString());
                                    mDateTimeXmlString = lineString;
                                    sb2 = sb3;
                                    url2 = url;
                                } catch (Exception e2) {
                                    e = e2;
                                    Log.e(TAG, "oplusServer exception: " + e);
                                    return false;
                                }
                            }
                            sb = sb2;
                            url = url2;
                            Log.d(TAG, "Read response data success! mDateTimeXmlString=" + mDateTimeXmlString);
                        } catch (Exception e3) {
                            e = e3;
                            Log.e(TAG, "oplusServer exception: " + e);
                            return false;
                        }
                    }
                    long responseTicks = SystemClock.elapsedRealtime();
                    this.mHttpTimeReference = SystemClock.elapsedRealtime();
                    if (mBufferedReader != null) {
                        mBufferedReader.close();
                    }
                    if (mInputStreamReader != null) {
                        mInputStreamReader.close();
                    }
                    httpconn.disconnect();
                    Log.d(TAG, "Start to parser http response data!");
                    SAXParserFactory mSaxParserFactory = SAXParserFactory.newInstance();
                    SAXParser mSaxParser = mSaxParserFactory.newSAXParser();
                    XMLReader mXmlReader = mSaxParser.getXMLReader();
                    DateTimeXmlParseHandler mDateTimeXmlParseHandler = new DateTimeXmlParseHandler();
                    mXmlReader.setContentHandler(mDateTimeXmlParseHandler);
                    mXmlReader.parse(new InputSource(new StringReader(mDateTimeXmlString)));
                    String mDateString = mDateTimeXmlParseHandler.getDate();
                    String[] dateStrings = mDateString.split("-");
                    int[] mIntDateData = new int[3];
                    int i = 0;
                    while (true) {
                        String mDateString2 = mDateString;
                        if (i >= dateStrings.length) {
                            break;
                        }
                        mIntDateData[i] = Integer.parseInt(dateStrings[i]);
                        i++;
                        mDateString = mDateString2;
                    }
                    String mTimeString = mDateTimeXmlParseHandler.getTime();
                    String[] timeStrings = mTimeString.split(":");
                    int[] mIntTimeData = new int[3];
                    int i2 = 0;
                    while (true) {
                        String[] dateStrings2 = dateStrings;
                        if (i2 < timeStrings.length) {
                            mIntTimeData[i2] = Integer.parseInt(timeStrings[i2]);
                            i2++;
                            dateStrings = dateStrings2;
                        } else {
                            Time mOplusServerTime = new Time("Asia/Shanghai");
                            Log.d(TAG, "Parser time success, hour= " + mIntTimeData[0] + " minute = " + mIntTimeData[1] + "seconds =" + mIntTimeData[2]);
                            mOplusServerTime.set(mIntTimeData[2], mIntTimeData[1], mIntTimeData[0], mIntDateData[2], mIntDateData[1] - 1, mIntDateData[0]);
                            long mEndParseTime = System.currentTimeMillis();
                            this.mHttpTime = mOplusServerTime.toMillis(true) + (mEndParseTime - mBeginParseTime) + AVERAGE_RECEIVE_TIME;
                            this.mRoundTripTime = responseTicks - requestTicks;
                            return true;
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                }
            } catch (Exception e5) {
                e = e5;
            }
        } catch (Exception e6) {
            e = e6;
        }
    }

    private boolean getNetType(Context context) {
        NetworkInfo info;
        String apn;
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService("connectivity");
        if (conn == null || (info = conn.getActiveNetworkInfo()) == null) {
            return false;
        }
        String type = info.getTypeName();
        if (type.equalsIgnoreCase("WIFI")) {
            return true;
        }
        return ((type.equalsIgnoreCase("MOBILE") || type.equalsIgnoreCase("GPRS")) && (apn = info.getExtraInfo()) != null && apn.equalsIgnoreCase("cmwap")) ? false : true;
    }

    /* loaded from: classes.dex */
    public class DateTimeXmlParseHandler extends DefaultHandler {
        private boolean mIsTimeZoneFlag = false;
        private boolean mIsDateFlag = false;
        private boolean mIsTimeFlag = false;
        private String mTimeZoneString = "";
        private String mDateString = "";
        private String mTimeString = "";

        public DateTimeXmlParseHandler() {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            if (this.mIsTimeZoneFlag) {
                this.mTimeZoneString = new String(ch, start, length);
            } else if (this.mIsDateFlag) {
                this.mDateString = new String(ch, start, length);
            } else if (this.mIsTimeFlag) {
                this.mTimeString = new String(ch, start, length);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (localName.equals("TimeZone")) {
                this.mIsTimeZoneFlag = false;
            } else if (localName.equals("Date")) {
                this.mIsDateFlag = false;
            } else if (localName.equals(HapticPlayer.EVENT_KEY_HE_CURVE_POINT_TIME)) {
                this.mIsTimeFlag = false;
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (localName.equals("TimeZone")) {
                this.mIsTimeZoneFlag = true;
            } else if (localName.equals("Date")) {
                this.mIsDateFlag = true;
            } else if (localName.equals(HapticPlayer.EVENT_KEY_HE_CURVE_POINT_TIME)) {
                this.mIsTimeFlag = true;
            }
        }

        public String getTimeZone() {
            return this.mTimeZoneString;
        }

        public String getDate() {
            return this.mDateString;
        }

        public String getTime() {
            return this.mTimeString;
        }
    }

    public long getHttpTime() {
        return this.mHttpTime;
    }

    public long getHttpTimeReference() {
        return this.mHttpTimeReference;
    }

    public long getRoundTripTime() {
        return this.mRoundTripTime;
    }

    public InetSocketAddress getServerSocketAddress() {
        return this.mServerSocketAddress;
    }
}
