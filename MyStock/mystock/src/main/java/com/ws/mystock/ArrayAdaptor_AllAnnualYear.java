package com.ws.mystock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ws.libmystock.Cls_StockAnnual;

import java.util.ArrayList;

public class ArrayAdaptor_AllAnnualYear extends ArrayAdapter<Cls_StockAnnual> {
    private final Context context;
    private final ArrayList<Cls_StockAnnual> values;

    public ArrayAdaptor_AllAnnualYear(Context context, ArrayList<Cls_StockAnnual> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.e("TransactionArrayAdaptor", "position: " + position);
        Cls_StockAnnual annual = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.array_adapter_all_annual_year, parent, false);
        }
        TextView tvStockName = (TextView) convertView.findViewById(R.id.tvStockName);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        TextView tvQuantity = (TextView) convertView.findViewById(R.id.tvQuantity);
        TextView tvMarketValue = (TextView) convertView.findViewById(R.id.tvMarketValue);
        TextView tvInvested = (TextView) convertView.findViewById(R.id.tvInvested);
        TextView tvDividend = (TextView) convertView.findViewById(R.id.tvDividend);
        TextView tvTotalDividend = (TextView) convertView.findViewById(R.id.tvTotalDividend);
        TextView tvGain = (TextView) convertView.findViewById(R.id.tvGain);
        TextView tvTotalGain = (TextView) convertView.findViewById(R.id.tvTotalGain);
        tvStockName.setText(String.format("%s", annual.getStockName()));
        tvPrice.setText(String.format("%.3f", annual.getPrice()));
        tvQuantity.setText(String.format("%.0f", annual.getQuantity()));
        tvMarketValue.setText(String.format("%.2f", annual.getMarketValue()));
        tvInvested.setText(String.format("%.2f", annual.getInvested()));
        tvDividend.setText(String.format("%.2f", annual.getDividend()));
        tvTotalDividend.setText(String.format("%.2f", annual.getTotalDividend()));
        tvGain.setText(String.format("%.2f", annual.getGain()));
        tvTotalGain.setText(String.format("%.2f", annual.getTotalGain()));
        return convertView;
    }
}
