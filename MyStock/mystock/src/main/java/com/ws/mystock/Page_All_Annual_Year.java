package com.ws.mystock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ws.libmystock.Cls_StockAnnual;

import java.util.ArrayList;

public class Page_All_Annual_Year extends LinearLayout {
    String TAG = this.getClass().getSimpleName();
    Context mCtx;
    LinearLayout mLytTableHeader;
    ListView mLvData;
    ArrayAdaptor_AllAnnualYear mAdpData;

    public Page_All_Annual_Year(Context context) {
        super(context);
        onCreated(context);
    }

    public Page_All_Annual_Year(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreated(context);
    }

    public Page_All_Annual_Year(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreated(context);
    }

    private void onCreated(Context context) {
        String[] lblHeaders;

        mCtx = context;
        mAdpData = new ArrayAdaptor_AllAnnualYear(mCtx, new ArrayList<>());
        lblHeaders = new String[]{
                "Stock",
                "Price",
                "Qty",
                "Market Value",
                "Invested",
                "Dividend",
                "Total Dividend",
                "Gain",
                "Total Gain",
        };
        inflate(mCtx, R.layout.page_all_annual_year, this);
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

    public void updateData(ArrayList<Cls_StockAnnual> data) {
        mAdpData.clear();
        mAdpData.addAll(data);
    }
}
