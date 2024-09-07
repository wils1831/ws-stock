package com.ws.mystock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ws.libmystock.Cls_StockTransaction;

import java.util.ArrayList;

public class Page_Stock_Transactions extends LinearLayout {
    String TAG = this.getClass().getSimpleName();
    Context mCtx;
    LinearLayout mLytTableHeader;
    ListView mLvData;
    ArrayAdaptor_StockTransaction mAdpData;

    public Page_Stock_Transactions(Context context) {
        super(context);
        onCreated(context);
    }

    public Page_Stock_Transactions(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreated(context);
    }

    public Page_Stock_Transactions(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreated(context);
    }
    private void onCreated(Context context) {
        String[] lblHeaders;

        mCtx = context;
        mAdpData = new ArrayAdaptor_StockTransaction(mCtx, new ArrayList<>());
        lblHeaders = new String[]{
                "Date",
                "Trans",
                "Qty",
                "Price",
                "Fee",
                "Amount",
                "Unit",
                "Actual Unit",
                "Invested",
                "Cost /Unit"
        };
        inflate(mCtx, R.layout.page_stock_transactions, this);
        mLytTableHeader = findViewById(R.id.lytTableHeader);
        for (int i = 0; i < mLytTableHeader.getChildCount(); i++) {
            TextView tv;

            tv = ((TextView) mLytTableHeader.getChildAt(i));
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTextColor(0xffffffff);
            tv.setBackgroundColor(0xffaaaaaa);
            if (i < lblHeaders.length) {
                tv.setText(lblHeaders[i]);
            }
        }
        mLvData = findViewById(R.id.lvData);
        mLvData.setAdapter(mAdpData);
    }

    public void updateData(ArrayList<Cls_StockTransaction> data) {
        mAdpData.clear();
        mAdpData.addAll(data);
    }
}

