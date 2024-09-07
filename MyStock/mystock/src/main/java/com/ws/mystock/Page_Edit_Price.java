package com.ws.mystock;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ws.libmystock.Cls_Price;
import com.ws.libmystock.Cls_Stock;
import com.ws.libmystock.Cls_StockAnnual;
import com.ws.libmystock.MyStockData;
import com.ws.libmyui.MyDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class Page_Edit_Price extends Dialog {
    String TAG = this.getClass().getSimpleName();
    Context mCtx;
    PageListener mPageListener;

    LinearLayout mLlScroll;
    List<EditText> mEtStockPrices;
    EditText mEtYear;
    ImageButton mIbtnOK;
    ImageButton mIbtnCancel;

    List<String> mListStockNames;
    ArrayList<Cls_StockAnnual> mStockAnnual;

    public interface PageListener {
        void onDataChanged(int year, ArrayList<Cls_Price> prices);
    }

    public Page_Edit_Price(@NonNull Context context) {
        super(context);
        mCtx = context;
        mListStockNames = Arrays.asList(MyStockData.getStockNames());
        mStockAnnual = MyStockData.getAllAnnualYears(Calendar.getInstance().get(Calendar.YEAR));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_edit_price);
        setCancelable(false);
        mLlScroll = findViewById(R.id.llScroll);
        mEtStockPrices = new ArrayList<>();
        for (Cls_StockAnnual s : mStockAnnual) {
            addStockPriceEditView(s.getStockName(), s.getPrice());
        }
        mEtYear = findViewById(R.id.etYear);
        mEtYear.setText("" + Calendar.getInstance().get(Calendar.YEAR));
        mIbtnOK = findViewById(R.id.btnOK);
        mIbtnOK.setOnClickListener(view -> {
            ArrayList<Cls_Price> prices;
            double price;
            int year;

            year = Integer.parseInt(mEtYear.getText().toString());
            if (year < Cls_Stock.BASE_YEAR || year > Calendar.getInstance().get(Calendar.YEAR)) {
                return;
            }
            prices = new ArrayList<>();
            for (int i = 0; i < mStockAnnual.size(); i++) {
                price = Double.parseDouble(mEtStockPrices.get(i).getText().toString());
                prices.add(new Cls_Price(mStockAnnual.get(i).getStockName(), price));
            }
            MyDialog.showDialogConfirm(mCtx,
                    "Price",
                    "Save Price for year " + year + "?",
                    (dialog, which) -> {
                        dismiss();
                        if (mPageListener != null) {
                            mPageListener.onDataChanged(year, prices);
                        }
                    },
                    null);
        });
        mIbtnCancel = findViewById(R.id.btnCancel);
        mIbtnCancel.setOnClickListener(view -> dismiss());

    }

    public void setPageListener(PageListener listener) {
        mPageListener = listener;
    }

    private void addStockPriceEditView(String stockName, double price) {
        LinearLayout ll;
        TextView tv;
        EditText et;
        LinearLayout.LayoutParams lp;

        Log.e(TAG, "addStockPriceEditView: " + stockName);
        ll = new LinearLayout(mCtx);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setBackgroundColor(0xffafafaf);
        ll.setPadding(1, 1, 1, 1);

        tv = new TextView(mCtx);
        tv.setLayoutParams(new LinearLayout.LayoutParams(150 * 2, 40 * 2));
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setBackgroundColor(0xff5f5f5f);
        tv.setPadding(10 * 2, 0, 0, 0);
        tv.setTextSize(16);
        tv.setTextColor(0xffffffff);
        tv.setText(stockName);

        et = new EditText(mCtx);
        et.setLayoutParams(new LinearLayout.LayoutParams(100 * 2, 40 * 2));
        lp = (LinearLayout.LayoutParams) et.getLayoutParams();
        lp.setMargins(1, 0, 0, 0);
        et.setLayoutParams(lp);
        et.setGravity(Gravity.CENTER);
        et.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et.setBackgroundColor(0xffffffff);
        et.setTextSize(16);
        et.setTextColor(0xff00007f);
        et.setText("" + price);

        ll.addView(tv);
        ll.addView(et);
        mLlScroll.addView(ll);

        mEtStockPrices.add(et);
    }
}
