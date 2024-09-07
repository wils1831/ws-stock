package com.ws.mystock;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ws.libmygeneric.file.MyFile;
import com.ws.libmyfirebaseservice.MyFirebaseService;
import com.ws.libmyfirebaseservice.MyRunnableRequestDone;
import com.ws.libmystock.FilePrice;
import com.ws.libmystock.FileTransactions;

import java.io.File;
import java.util.Calendar;

public class MyServiceEngine {
    static String TAG = MyServiceEngine.class.getSimpleName();

    static final String KEY_WHAT = "KEY_WHAT";
    static final String KEY_TASK = "KEY_TASK";
    static final String KEY_STATUS = "KEY_STATUS";
    static final String KEY_MESSAGE = "KEY_MESSAGE";
    static final String WHAT_REQUEST = "REQUEST";
    static final String WHAT_STATUS = "STATUS";
    static final String TEMP_FOLDER_NAME = "temp";

    static Context mCtx;
    static LocalBroadcastManager mLBM;
    static MyFirebaseService mFirebaseService;
    static int mPriceYear;

    static ServiceConnection FirebaseConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            File file;

            mFirebaseService = ((MyFirebaseService.LocalBinder) service).getService();
            mCtx = mFirebaseService.getApplicationContext();
            mLBM = LocalBroadcastManager.getInstance(mCtx);
            MyFile.setBaseDir(new File(Environment.getExternalStorageDirectory() + "/" + mCtx.getString(R.string.app_name)));
            file = MyFile.toFolder(TEMP_FOLDER_NAME);
            if (!file.exists()) {
                file.mkdir();
            }
            authenticateFirebase();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    static class MyReqDone extends MyRunnableRequestDone {
        String mReqName;

        public MyReqDone(String reqName) {
            mReqName = reqName;
        }

        @Override
        public void runOK() {
            Log.e(TAG, mReqName + " OK");
        }

        @Override
        public void runNG() {
            Log.e(TAG, mReqName + "  failed: " + ErrorMessage);
        }
    }

    static void authenticateFirebase() {
        String reqName;

        reqName = mFirebaseService.REQ_AUTHENTICATE;
        sendBroadcast(WHAT_REQUEST, reqName, true, "");
        mFirebaseService.exeRequest(
                reqName,
                null,
                new MyReqDone(reqName) {
                    @Override
                    public void run() {
                        super.run();
                        sendBroadcast(WHAT_STATUS, reqName, Status, ErrorMessage);
                        downloadFile_Data();
                    }
                });
    }

    static void downloadFile_Data() {
        String reqName;

        reqName = mFirebaseService.REQ_DOWNLOAD_FILE;
        sendBroadcast(WHAT_REQUEST, reqName, true, "Downloading Transactions");
        mFirebaseService.exeRequest(
                reqName,
                new Object[]{
                        FileTransactions.FILE_NAME,
                        FileTransactions.FILE_NAME + "." + FileTransactions.DEFAULT_FILE_EXTENSION,
                        TEMP_FOLDER_NAME
                },
                new MyReqDone(reqName) {
                    @Override
                    public void run() {
                        super.run();
                        mPriceYear = 2016;
                        downloadFile_Price();
                    }
                });
    }

    static void downloadFile_Price() {
        String reqName;
        int latestYear = Calendar.getInstance().get(Calendar.YEAR);

        reqName = mFirebaseService.REQ_DOWNLOAD_FILE;
        sendBroadcast(WHAT_REQUEST, reqName, true, "Downloading Price " + mPriceYear);
        mFirebaseService.exeRequest(
                reqName,
                new Object[]{
                        FilePrice.FILE_NAME,
                        FilePrice.FILE_NAME + "_" + mPriceYear + "." + FilePrice.DEFAULT_FILE_EXTENSION,
                        TEMP_FOLDER_NAME
                },
                new MyReqDone(reqName) {
                    @Override
                    public void run() {
                        super.run();
                        mPriceYear++;
                        if (mPriceYear > latestYear) {
                            sendBroadcast(WHAT_STATUS, reqName, Status, ErrorMessage);
                            return;
                        }
                        downloadFile_Price();
                    }
                });
    }

    static void uploadFile_Data() {
        String reqName;

        reqName = mFirebaseService.REQ_UPLOAD_FILE;
        sendBroadcast(WHAT_REQUEST, reqName, true, "");
        mFirebaseService.exeRequest(
                reqName,
                new Object[]{
                        TEMP_FOLDER_NAME,
                        FileTransactions.FILE_NAME + "." + FileTransactions.DEFAULT_FILE_EXTENSION,
                        FileTransactions.FILE_NAME
                },
                new MyReqDone(reqName) {
                    @Override
                    public void run() {
                        super.run();
                        sendBroadcast(WHAT_STATUS, reqName, Status, ErrorMessage);
                    }
                });
    }

    static void uploadFile_Price(int year) {
        String reqName;

        reqName = mFirebaseService.REQ_UPLOAD_FILE;
        sendBroadcast(WHAT_REQUEST, reqName, true, "");
        mFirebaseService.exeRequest(
                reqName,
                new Object[]{
                        TEMP_FOLDER_NAME,
                        FilePrice.FILE_NAME + "_" + year + "." + FilePrice.DEFAULT_FILE_EXTENSION,
                        FilePrice.FILE_NAME
                },
                new MyReqDone(reqName) {
                    @Override
                    public void run() {
                        super.run();
                        sendBroadcast(WHAT_STATUS, reqName, Status, ErrorMessage);
                    }
                });
    }

    static void sendBroadcast(String what, String task, boolean status, String errorMessage) {
        Intent intent;

        intent = new Intent(TAG);
        intent.putExtra(KEY_WHAT, what);
        intent.putExtra(KEY_TASK, task);
        intent.putExtra(KEY_STATUS, status);
        intent.putExtra(KEY_MESSAGE, errorMessage);
        mLBM.sendBroadcast(intent);
    }
}
