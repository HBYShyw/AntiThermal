package com.android.server.theia;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TheiaSocket {
    private static final String TAG = "TheiaSocketClient";
    private static volatile TheiaSocket theiaSocket;
    private LocalSocketAddress address;
    private LocalSocket client;
    private BufferedReader in;
    HandlerThread mSendThread;
    TheiaSender mSender;
    private PrintWriter out;
    private final String SOCKET_NAME = "theia_socket";
    private boolean isConnected = false;
    private int connectTime = 1;
    private ConnectSocketThread connectSocketThread = new ConnectSocketThread();

    public TheiaSender getSender() {
        return this.mSender;
    }

    public static TheiaSocket getInstance() {
        if (theiaSocket == null) {
            synchronized (TheiaSocket.class) {
                if (theiaSocket == null) {
                    theiaSocket = new TheiaSocket();
                }
            }
        }
        return theiaSocket;
    }

    public TheiaSocket() {
        Log.d(TAG, "TheiaSocket ready to Start");
        TheiaSocketStart();
    }

    private void TheiaSocketStart() {
        this.client = new LocalSocket();
        this.address = new LocalSocketAddress("theia_socket", LocalSocketAddress.Namespace.RESERVED);
        HandlerThread handlerThread = new HandlerThread("TheiaSender");
        this.mSendThread = handlerThread;
        handlerThread.start();
        this.mSender = new TheiaSender(this.mSendThread.getLooper());
        this.connectSocketThread.start();
    }

    public void sendMessage(String str) {
        Message obtain = Message.obtain();
        obtain.what = 1;
        obtain.obj = str;
        this.mSender.sendMessage(obtain);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class ConnectSocketThread extends Thread {
        private ConnectSocketThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!TheiaSocket.this.isConnected && TheiaSocket.this.connectTime <= 10) {
                try {
                    Thread.sleep(1000L);
                    Log.d(TheiaSocket.TAG, "Try to connect socket;ConnectTime:" + TheiaSocket.this.connectTime);
                    TheiaSocket.this.client.connect(TheiaSocket.this.address);
                    TheiaSocket.this.out = new PrintWriter(TheiaSocket.this.client.getOutputStream());
                    TheiaSocket.this.in = new BufferedReader(new InputStreamReader(TheiaSocket.this.client.getInputStream()));
                    TheiaSocket.this.isConnected = true;
                    TheiaSocket.this.client.setSoTimeout(1200);
                    Log.d(TheiaSocket.TAG, "TheiaSocket Connect Success");
                } catch (Exception e) {
                    TheiaSocket.this.connectTime++;
                    Log.d(TheiaSocket.TAG, "Connect fail; Reason: " + e.toString());
                }
            }
        }
    }

    public void destroy() {
        try {
            BufferedReader bufferedReader = this.in;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            PrintWriter printWriter = this.out;
            if (printWriter != null) {
                printWriter.close();
            }
            LocalSocket localSocket = this.client;
            if (localSocket != null) {
                localSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class TheiaSender extends Handler implements Runnable {
        @Override // java.lang.Runnable
        public void run() {
        }

        public TheiaSender(Looper looper) {
            super(looper);
            Log.d(TheiaSocket.TAG, "entering TheiaSender Constructor");
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            String obj = message.obj.toString();
            Log.d(TheiaSocket.TAG, "TheiaSend receive content : " + obj);
            if (!TheiaSocket.this.connectSocketThread.isAlive() && !TheiaSocket.this.isConnected) {
                Log.d(TheiaSocket.TAG, "Socket unConnected, restart connectSocketThread");
                TheiaSocket.this.connectSocketThread = new ConnectSocketThread();
                TheiaSocket.this.connectSocketThread.start();
            }
            if (TheiaSocket.this.out != null) {
                TheiaSocket.this.out.println(obj);
                TheiaSocket.this.out.flush();
                Log.d(TheiaSocket.TAG, "send message success");
            }
        }
    }
}
