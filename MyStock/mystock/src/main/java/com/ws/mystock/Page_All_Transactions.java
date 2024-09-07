package com.ws.mystock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ws.libmystock.Cls_AllTransaction;

import java.util.ArrayList;

public class Page_All_Transactions extends LinearLayout {
    String TAG = this.getClass().getSimpleName();
    Context mCtx;
    LinearLayout mLytTableHeader;
    ListView mLvData;
    ArrayAdaptor_AllTransaction mAdpData;

    public Page_All_Transactions(Context context) {
        super(context);
        onCreated(context);
    }

    public Page_All_Transactions(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreated(context);
    }

    public Page_All_Transactions(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreated(context);
    }

    private void onCreated(Context context) {
        String[] lblHeaders;

        mCtx = context;
        mAdpData = new ArrayAdaptor_AllTransaction(mCtx, new ArrayList<>());
        lblHeaders = new String[]{
                "Date",
                "Trans",
                "Stock",
                "Qty",
                "Price",
                "Fee",
                "Amount",
                "Balance",
                "Invested"
        };
        inflate(mCtx, R.layout.page_all_transactions, this);
        mLytTableHeader = findViewById(R.id.lytTableHeader);
        LayoutParams lp;
        lp = (LayoutParams) mLytTableHeader.getLayoutParams();
        lp.height = 72;
        mLytTableHeader.setLayoutParams(lp);
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

    public void updateData(ArrayList<Cls_AllTransaction> data) {
        mAdpData.clear();
        mAdpData.addAll(data);
    }
}

