package com.ws.mystock;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ws.libmygeneric.conversion.MyEnum;
import com.ws.libmystock.Cls_TransactionBase;
import com.ws.libmystock.MyStockData;
import com.ws.libmyui.MyDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Page_Edit_Transaction_Data extends Dialog {
    String TAG = this.getClass().getSimpleName();
    Context mCtx;
    PageListener mPageListener;

    TextView mTvDate;
    Spinner mSpTransaction;
    Spinner mSpSockName;
    EditText mEtQty;
    EditText mEtPrice;
    EditText mEtFee;
    EditText mEtAmount;
    TextView mTvPriceLabel;
    ImageButton mIbtnOK;
    ImageButton mIbtnCancel;

    List<String> mListTxType;
    List<String> mListStockNames;

    public interface PageListener {
        void onDataChanged(Cls_TransactionBase transactionBase);
    }

    public Page_Edit_Transaction_Data(Context context) {
        super(context);
        mCtx = context;
        mListTxType = new ArrayList<>();
        for (Cls_TransactionBase.EN_TXTYPE e : Cls_TransactionBase.EN_TXTYPE.values()) {
            mListTxType.add(e.toString());
        }
        mListStockNames = Arrays.asList(MyStockData.getStockNames());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_edit_transaction);
        setCancelable(false);
        mTvDate = findViewById(R.id.tvDate);
        mTvDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog;
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(mCtx,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        // set day of month , month and year value in the edit text
                        mTvDate.setText(String.format("%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year - 2000));
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
        mSpTransaction = findViewById(R.id.spTxType);
        mSpTransaction.setAdapter(new ArrayAdapter<>(mCtx, android.R.layout.simple_spinner_item, mListTxType));
        mSpTransaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cls_TransactionBase.EN_TXTYPE txType;

                txType = Enum.valueOf(Cls_TransactionBase.EN_TXTYPE.class, mListTxType.get(position));
                switch (txType) {
                    case Buy:
                    case Sell:
                        mSpSockName.setEnabled(true);
                        mEtQty.setEnabled(true);
                        mEtQty.setBackgroundColor(Color.WHITE);
                        mTvPriceLabel.setText("Price");
                        mEtPrice.setEnabled(true);
                        mEtPrice.setBackgroundColor(Color.WHITE);
                        mEtFee.setEnabled(true);
                        mEtFee.setBackgroundColor(Color.WHITE);
                        mEtAmount.setEnabled(false);
                        mEtAmount.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case Dividend:
                        mSpSockName.setEnabled(true);
                        mEtQty.setEnabled(false);
                        mEtQty.setBackgroundColor(Color.TRANSPARENT);
                        mTvPriceLabel.setText("DPS");
                        mEtPrice.setEnabled(true);
                        mEtPrice.setBackgroundColor(Color.WHITE);
                        mEtFee.setEnabled(false);
                        mEtFee.setBackgroundColor(Color.TRANSPARENT);
                        mEtAmount.setEnabled(false);
                        mEtAmount.setBackgroundColor(Color.TRANSPARENT);
                        mEtAmount.setText("0");
                        break;
                    case Bonus:
                        mSpSockName.setEnabled(true);
                        mEtQty.setEnabled(false);
                        mEtQty.setBackgroundColor(Color.TRANSPARENT);
                        mTvPriceLabel.setText("Ratio");
                        mEtPrice.setEnabled(true);
                        mEtPrice.setBackgroundColor(Color.WHITE);
                        mEtFee.setEnabled(false);
                        mEtFee.setBackgroundColor(Color.TRANSPARENT);
                        mEtAmount.setEnabled(false);
                        mEtAmount.setBackgroundColor(Color.TRANSPARENT);
                        mEtAmount.setText("0");
                        break;
                    case Deposit:
                    case Withdrawal:
                    case Interest:
                        mSpSockName.setEnabled(false);
                        mEtQty.setEnabled(false);
                        mEtQty.setBackgroundColor(Color.TRANSPARENT);
                        mTvPriceLabel.setText("Price");
                        mEtPrice.setEnabled(true);
                        mEtPrice.setBackgroundColor(Color.TRANSPARENT);
                        mEtFee.setEnabled(false);
                        mEtFee.setBackgroundColor(Color.TRANSPARENT);
                        mEtAmount.setEnabled(true);
                        mEtAmount.setBackgroundColor(Color.WHITE);
                        break;
                }
                calculateAmount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpSockName = findViewById(R.id.spStockName);
        mSpSockName.setAdapter(new ArrayAdapter<>(mCtx, android.R.layout.simple_spinner_item, mListStockNames));
        mEtQty = findViewById(R.id.etQty);
        mEtQty.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calculateAmount();
            }
            return false;
        });
        mTvPriceLabel = findViewById(R.id.tvPriceLabel);
        mEtPrice = findViewById(R.id.etPrice);
        mEtPrice.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calculateAmount();
            }
            return false;
        });
        mEtFee = findViewById(R.id.etFee);
        mEtFee.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calculateAmount(false);
            }
            return false;
        });
        mEtAmount = findViewById(R.id.etAmount);
        mIbtnOK = findViewById(R.id.btnOK);
        mIbtnOK.setOnClickListener(view -> {
            Date date;
            Cls_TransactionBase.EN_TXTYPE txType;
            String stockName;
            int qty;
            double price;
            double fee;
            double amount;
            String message;
            Cls_TransactionBase transactionBase;

            calculateAmount(false);
            try {
                date = new SimpleDateFormat("dd/MM/yy").parse(mTvDate.getText().toString());
            } catch (ParseException e) {
                date = Calendar.getInstance().getTime();
            }
            txType = Enum.valueOf(Cls_TransactionBase.EN_TXTYPE.class, mListTxType.get(mSpTransaction.getSelectedItemPosition()));
            stockName = mSpSockName.getSelectedItem().toString();
            qty = Integer.parseInt(mEtQty.getText().toString());
            price = Double.parseDouble(mEtPrice.getText().toString());
            fee = Double.parseDouble(mEtFee.getText().toString());
            amount = Double.parseDouble(mEtAmount.getText().toString());
            message = String.format(
                    "Date: %s" +
                            "\nType: %s",
                    mTvDate.getText().toString(),
                    txType);

            message = String.format(
                    "Date: %s" +
                            "\nType: %s" +
                            "\nStock: %s" +
                            "\nQty: %d" +
                            "\nPrice %.4f" +
                            "\nFee: %.2f" +
                            "\nAmount: %.2f",
                    mTvDate.getText().toString(),
                    txType,
                    stockName,
                    qty,
                    price,
                    fee,
                    amount);

            transactionBase = new Cls_TransactionBase();
            transactionBase.setDate(date);
            transactionBase.setTransactionType(txType.toString());
            switch (txType) {
                case Buy:
                case Sell:
                case Dividend:
                case Bonus:
                    transactionBase.setStockName(stockName);
                    break;
                case Deposit:
                case Withdrawal:
                case Interest:
                    transactionBase.setStockName("");
                    break;
            }
            transactionBase.setQuantity(qty);
            transactionBase.setPrice(price);
            transactionBase.setFee(fee);
            transactionBase.setAmount(amount);
            if (mPageListener != null) {
                mPageListener.onDataChanged(transactionBase);
            }
            MyDialog.showDialogOK(mCtx, "Add Data", "Data Added: " +String.format("%.2f", amount));
//            MyDialog.showDialogConfirm(mCtx,
//                    "Transaction",
//                    message,
//                    (dialog, which) -> {
//                        dismiss();
//                        if (mPageListener != null) {
//                            mPageListener.onDataChanged(transactionBase);
//                        }
//                    },
//                    null);
        });
        mIbtnCancel = findViewById(R.id.btnCancel);
        mIbtnCancel.setOnClickListener(view -> dismiss());

        calculateAmount();
        mTvDate.setText(new SimpleDateFormat("dd/MM/yy").format(Calendar.getInstance().getTime()));

    }


    public void setPageListener(PageListener listener) {
        mPageListener = listener;
    }

    private void calculateAmount() {
        calculateAmount(true);
    }
    private void calculateAmount(boolean autoFee) {
        Cls_TransactionBase.EN_TXTYPE txType;
        int qty;
        double price;
        double fee;
        double amount;

        txType = Enum.valueOf(Cls_TransactionBase.EN_TXTYPE.class, mListTxType.get(mSpTransaction.getSelectedItemPosition()));
        qty = Integer.parseInt(mEtQty.getText().toString());
        price = Double.parseDouble(mEtPrice.getText().toString());
        if (autoFee) {
            fee = calculateFee(qty, price);// Double.parseDouble(mEtFee.getText().toString());
        }else {
            fee = Double.parseDouble(mEtFee.getText().toString());
        }
        amount = Double.parseDouble(mEtAmount.getText().toString());
        switch (txType) {
            case Buy:
                amount = qty * price + fee;
                break;
            case Sell:
                amount = qty * price - fee;
                break;
            case Dividend:
            case Bonus:
                qty = 0;
                fee = 0;
                amount = 0;
                break;
            case Deposit:
            case Withdrawal:
            case Interest:
                qty = 0;
                price = 0;
                fee = 0;
                break;
        }
        mEtQty.setText("" + qty);
        mEtPrice.setText(String.format("%.4f", price));
        mEtFee.setText(String.format("%.2f", fee));
        mEtAmount.setText(String.format("%.2f", amount));
    }

    private double calculateFee(int qty, double price) {
        double f1, f2, f3;
        double total;

        total = qty * price;
        if (total < 10000) {
            f1 = 8;
        } else {
            f1 = 0.0008 * total;
        }
        f2 = Math.ceil(total / 1000.0) * 1.5;
        f3 = Math.round(total * 0.03)/100.0;

        Log.e(TAG, String.format("F1=%.2f, F2=%.2f, F3=%.2f", f1, f2, f3));
        return f1 + f2 + f3;
    }
}