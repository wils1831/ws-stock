package com.ws.mystock;

import static android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ws.libmyfirebaseservice.MyFirebaseService;
import com.ws.libmygeneric.MyApp;
import com.ws.libmygeneric.file.MyFile;
import com.ws.libmystock.Cls_AllTransaction;
import com.ws.libmystock.Cls_Stock;
import com.ws.libmystock.Cls_StockTransaction;
import com.ws.libmystock.MyStockData;
import com.ws.libmyui.MyDialog;
import com.ws.libmyui.MyMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Page_App extends AppCompatActivity {
    String TAG = Page_App.class.getSimpleName();

    Context mCtx;
    Cls_Stock mStock;
    int mCurrentYear;

    TextView mTvRequest;

    Spinner mSpTransYear;
    ArrayAdapter<Integer> mAdpTransYearSelection;
    Spinner mSpStockName;
    ArrayAdapter<String> mAdpStockSelection;
    Spinner mSpType;
    ArrayAdapter<String> mAdpType;

    Page_All_Transactions mPgAllTransactions;
    Page_All_Annuals mPgAllAnnuals;
    Page_All_Annual_Year mPgAllAnnualYear;
    Page_Stock_Transactions mPgStockTransactions;
    Page_Stock_Annuals mPgStockAnnuals;

    boolean mSelectAnnuals;

    public static final String MENU_ADD_DATA = "Add Data";
    public static final String MENU_SAVE_DATA = "Save Data";
    public static final String MENU_PRICE = "Price";
    public static final String MENU_VERSION = "Version";
    public static final String MENU_EXIT = "Exit";

    public static List<String> MENU_LIST = new ArrayList<>(
            Arrays.asList(
                    MENU_ADD_DATA,
                    MENU_SAVE_DATA,
                    MENU_PRICE,
                    MENU_VERSION,
                    MENU_EXIT
            )
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_app);
        mCtx = this;
        mCurrentYear = -1;
        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

//            Log.e(TAG, "****isExternalStorageManager");
//        if (Environment.isExternalStorageManager()){
//            Log.e(TAG, "****isExternalStorageManager");
//        }else {
//            Log.e(TAG, "****isNOTExternalStorageManager");
//        }
//        if (!Settings.System.canWrite(mCtx)) {
//            Log.e(TAG, "****System Settings permission NOT granted");
//            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//            startActivity(intent);
//            return;
//        }
//        requestPermissions(new String[]{Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION}, 101);
//
//
//        try {
//            MyFile.setBaseDir(new File(Environment.getExternalStorageDirectory() + "/" + mCtx.getString(R.string.app_name)));
//            File f = MyFile.toFolder(MyServiceEngine.TEMP_FOLDER_NAME);
//            if (!f.exists()){
//                Log.e(TAG, "OutputStream not exists: " + f.getAbsolutePath());
//                f.mkdir();
//            }
//            else {
//                Log.e(TAG, "OutputStream f exists: " + f.getAbsolutePath());
//            }
//            File destTempFile = MyFile.toFolder(MyServiceEngine.TEMP_FOLDER_NAME+"/Trans.txt");
//            OutputStream os = new FileOutputStream(destTempFile);
//            Log.e(TAG, "OutputStream f OK " + f.getAbsolutePath());
//        } catch (Exception e) {
//            Log.e(TAG, "OutputStream exception: " + e);
//        }
//return;
        registerBroadcastReceiver();
        mCtx.bindService(new Intent(mCtx, MyFirebaseService.class), MyServiceEngine.FirebaseConnection, Context.BIND_AUTO_CREATE);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.e(TAG, "onRequestPermissionsResult: " + requestCode);
//        if (requestCode == 101) {
//            if (grantResults.length > 0) {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.e(TAG, "onRequestPermissionsResult: Permission allowed.");
//                } else {
//                    // permission denied, boo! Disable the functionality that depends on this permission.
//                    Log.e(TAG, "onRequestPermissionsResult: ***Permission denied.");
//                }
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    public void onBackPressed() {
        runOnUiThread(MenuSelect_Exit);
    }

    @Override
    protected void onDestroy() {
        unRegisterBroadcastReceiver();
        mCtx.unbindService(MyServiceEngine.FirebaseConnection);
        super.onDestroy();
    }

    private void initUI() {
        mSelectAnnuals = false;

        mTvRequest = findViewById(R.id.tv_request);

        MyMenu mMenu;

        mMenu = ((MyMenu) findViewById(R.id.menuApp));
        mMenu.setList(MENU_LIST);
        mMenu.setSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (MENU_LIST.get(position)) {
                    case MENU_EXIT:
                        runOnUiThread(MenuSelect_Exit);
                        break;
                    case MENU_VERSION:
                        runOnUiThread(MenuSelect_Version);
                        break;
                    case MENU_PRICE:
                        runOnUiThread(MenuSelect_Price);
                        break;
                    case MENU_SAVE_DATA:
                        runOnUiThread(MenuSelect_SaveData);
                        break;
                    case MENU_ADD_DATA:
                        runOnUiThread(MenuSelect_AddData);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAdpTransYearSelection = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        mAdpTransYearSelection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpTransYear = findViewById(R.id.spTransYear);
        mSpTransYear.setAdapter(mAdpTransYearSelection);
        mSpTransYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                runOnUiThread(Runnable_UpdateViewTable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAdpStockSelection = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        mAdpStockSelection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpStockName = findViewById(R.id.spStockName);
        mSpStockName.setAdapter(mAdpStockSelection);
        mSpStockName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                runOnUiThread(Runnable_UpdateViewTable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAdpType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        mAdpType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdpType.addAll(new String[]{"Transactions", "Annuals"});
        mSpType = findViewById(R.id.spType);
        mSpType.setAdapter(mAdpType);
        mSpType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                mSelectAnnuals = position == 1;
                runOnUiThread(Runnable_UpdateViewTable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPgAllTransactions = findViewById(R.id.pgAllTransactions);
        mPgAllAnnuals = findViewById(R.id.pgAllAnnuals);
        mPgAllAnnualYear = findViewById(R.id.pgAllAnnualYear);
        mPgStockTransactions = findViewById(R.id.pgStockTransactions);
        mPgStockAnnuals = findViewById(R.id.pgStockAnnuals);

    }

    private int getSpinnerYear() {
        Object obj;

        obj = mSpTransYear.getSelectedItem();
        if (obj == null || !(obj instanceof Integer)) {
            Log.e(TAG, "obj is null");
            return -1;
        }
        Log.e(TAG, "getSpinnerYear: " + obj);
        return (Integer) obj;
    }

    private String getSpinnerStockName() {
        Object obj;

        obj = mSpStockName.getSelectedItem();
        if (obj == null || !(obj instanceof String)) {
            Log.e(TAG, "obj is null");
            return null;
        }
        return (String) obj;
    }

    private void updateViewTable() {
        int year;
        String stockName;

        year = getSpinnerYear();
        Log.e(TAG, "onItemSelected: year=" + year);
        if (year < 0) {
            return;
        }
        mCurrentYear = year;
        Log.e(TAG, "onItemSelected: mCurrentYear=" + mCurrentYear);

        stockName = getSpinnerStockName();
        if (stockName == null) {
            Log.e(TAG, "stockName: " + "NULL");
            return;
        }
        Log.e(TAG, "stockName: " + stockName);
        mStock = MyStockData.getStock(stockName);

        if (mStock == null) {
            Log.e(TAG, "updateViewTable mStock is NULL");
            return;
        }

        if (mStock.StockName.equals("All")) {
            if (!mSelectAnnuals) {
                mPgStockTransactions.setVisibility(View.GONE);
                mPgStockAnnuals.setVisibility(View.GONE);
                mPgAllTransactions.setVisibility(View.VISIBLE);
                mPgAllAnnuals.setVisibility(View.GONE);
                mPgAllAnnualYear.setVisibility(View.GONE);
                updateViewAllTransactions();
            } else {
                if (mCurrentYear == 0) {
                    mPgStockTransactions.setVisibility(View.GONE);
                    mPgStockAnnuals.setVisibility(View.GONE);
                    mPgAllTransactions.setVisibility(View.GONE);
                    mPgAllAnnuals.setVisibility(View.VISIBLE);
                    mPgAllAnnualYear.setVisibility(View.GONE);
                    updateViewAllAnnuals();
                } else {
                    mPgStockTransactions.setVisibility(View.GONE);
                    mPgStockAnnuals.setVisibility(View.GONE);
                    mPgAllTransactions.setVisibility(View.GONE);
                    mPgAllAnnuals.setVisibility(View.GONE);
                    mPgAllAnnualYear.setVisibility(View.VISIBLE);
                    updateViewAllAnnualYears();
                }
            }
        } else {
            if (!mSelectAnnuals) {
                mPgAllTransactions.setVisibility(View.GONE);
                mPgAllAnnuals.setVisibility(View.GONE);
                mPgAllAnnualYear.setVisibility(View.GONE);
                mPgStockTransactions.setVisibility(View.VISIBLE);
                mPgStockAnnuals.setVisibility(View.GONE);
                updateViewStockTransactions();
            } else {
                mPgAllTransactions.setVisibility(View.GONE);
                mPgAllAnnuals.setVisibility(View.GONE);
                mPgAllAnnualYear.setVisibility(View.GONE);
                mPgStockTransactions.setVisibility(View.GONE);
                mPgStockAnnuals.setVisibility(View.VISIBLE);
                updateViewStockAnnuals();
            }
        }
    }

    private void updateViewAllTransactions() {
        ArrayList<Cls_AllTransaction> transactions;
        int tYear;
        if (mCurrentYear < 0) {
            return;
        }
        transactions = MyStockData.getAllTransactions();
        if (mCurrentYear != 0) {
            transactions = new ArrayList<>();
            for (Cls_AllTransaction transaction : MyStockData.getAllTransactions()) {
                tYear = transaction.getDateAsCalendar().get(Calendar.YEAR);
                if (tYear == mCurrentYear) {
                    transactions.add(transaction);
                }
                if (tYear > mCurrentYear) {
                    break;
                }
            }
        }
        mPgAllTransactions.updateData(transactions);
    }

    private void updateViewAllAnnuals() {
        mPgAllAnnuals.updateData(MyStockData.getAllAnnuals());
    }

    private void updateViewAllAnnualYears() {
        mPgAllAnnualYear.updateData(MyStockData.getAllAnnualYears(mCurrentYear));
    }

    private void updateViewStockTransactions() {
        ArrayList<Cls_StockTransaction> transactions;
        int tYear;

        if (mStock == null) {
            return;
        }
        transactions = mStock.getTransactions();
        if (mCurrentYear != 0) {
            transactions = new ArrayList<>();
            for (Cls_StockTransaction transaction : mStock.getTransactions()) {
                tYear = transaction.getDateAsCalendar().get(Calendar.YEAR);
                if (tYear == mCurrentYear) {
                    transactions.add(transaction);
                }
                if (tYear > mCurrentYear) {
                    break;
                }
            }
        }
        mPgStockTransactions.updateData(transactions);
    }

    private void updateViewStockAnnuals() {
        mPgStockAnnuals.updateData(mStock.getAnnuals());
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter;

        intentFilter = new IntentFilter();
        intentFilter.addAction(MyServiceEngine.class.getSimpleName());
        LocalBroadcastManager.getInstance(mCtx).registerReceiver(MainBroadcastReceiver, intentFilter);
    }

    private void unRegisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(mCtx).unregisterReceiver(MainBroadcastReceiver);
    }

    private final BroadcastReceiver MainBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action;
            String what;
            String task;
            boolean status;

            action = intent.getAction();
            what = intent.getStringExtra(MyServiceEngine.KEY_WHAT);
            Log.e(TAG, "onReceive: " + action + ", " + what);

            if (action.equals(MyServiceEngine.class.getSimpleName())) {
                switch (what) {
                    case MyServiceEngine.WHAT_REQUEST:
                        task = intent.getStringExtra(MyServiceEngine.KEY_MESSAGE);
                        Log.e(TAG, "onReceive: " + what + ", " + task);
                        mTvRequest.setVisibility(View.VISIBLE);
                        mTvRequest.setText(task + "...");
                        break;
                    case MyServiceEngine.WHAT_STATUS:
                        task = intent.getStringExtra(MyServiceEngine.KEY_TASK);
                        status = intent.getBooleanExtra(MyServiceEngine.KEY_STATUS, false);
                        if (status) {
                            switch (task) {
                                case MyFirebaseService.REQ_DOWNLOAD_FILE:
                                case MyFirebaseService.REQ_UPLOAD_FILE:
                                    MyStockData.retrieveFromFiles("temp");
                                    mTvRequest.setVisibility(View.GONE);
                                    mAdpTransYearSelection.clear();
                                    mAdpTransYearSelection.insert(0, 0);
                                    mAdpTransYearSelection.addAll(MyStockData.getPriceYears());
                                    mAdpStockSelection.clear();
                                    mAdpStockSelection.addAll(MyStockData.getStockNames());
                                    mAdpStockSelection.insert("All", 0);
                                    new Handler(Looper.myLooper()).post(() -> updateViewTable());
                                    break;
                            }
                        }
                        break;
                }
            }
        }
    };


    private Runnable MenuSelect_Exit = () -> {
        MyDialog.showDialogConfirm(
                mCtx,
                "Exit",
                "Exit application?",
                (dialogInterface, i) -> finishAffinity(),
                null);
    };
    private Runnable MenuSelect_Version = () -> {
        SimpleDateFormat sdf;
        String versionInfo;

        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        versionInfo = String.format("App Name: %s\nVersion: %s\nLast Updated: %s",
                MyApp.getApplicationName(mCtx),
                MyApp.getVersionName(mCtx),
                sdf.format(MyApp.getApplicationLastUpdated(mCtx)));
        MyDialog.showDialogOK(mCtx, "Version", versionInfo);
    };
    private Runnable Runnable_UpdateViewTable = () -> updateViewTable();
    private Runnable MenuSelect_Price = () -> {
        Page_Edit_Price page;

        page = new Page_Edit_Price(mCtx);
        page.setPageListener((year, prices) -> {
            MyStockData.updateFilePrice(year, prices);
            MyServiceEngine.uploadFile_Price(year);
        });
        page.show();
    };
    private Runnable MenuSelect_SaveData = () -> {
        MyDialog.showDialogConfirm(
                mCtx,
                "Save",
                "Save data to server?",
                (dialog, which) -> {
                    MyStockData.saveTransactionData();
                    MyServiceEngine.uploadFile_Data();
                }, null);
    };
    private Runnable MenuSelect_AddData = () -> {
        Page_Edit_Transaction_Data page;

        page = new Page_Edit_Transaction_Data(mCtx);
        page.setPageListener(transactionBase -> {
            MyStockData.insertNewTransaction(transactionBase);
        });
        page.show();
    };

}