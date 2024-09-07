package com.ws.mystock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ws.libmystock.Cls_StockTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ArrayAdaptor_StockTransaction extends ArrayAdapter<Cls_StockTransaction> {
    private final Context context;
    private final ArrayList<Cls_StockTransaction> values;

    public ArrayAdaptor_StockTransaction(Context context, ArrayList<Cls_StockTransaction> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cls_StockTransaction transaction = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.array_adapter_stock_transaction, parent, false);
        }
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvTransactionType = (TextView) convertView.findViewById(R.id.tvTransactionType);
        TextView tvQuantity = (TextView) convertView.findViewById(R.id.tvQuantity);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        TextView tvFee = (TextView) convertView.findViewById(R.id.tvFee);
        TextView tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
        TextView tvUnit = (TextView) convertView.findViewById(R.id.tvUnit);
        TextView tvActualUnit = (TextView) convertView.findViewById(R.id.tvActualUnit);
        TextView tvInvested = (TextView) convertView.findViewById(R.id.tvInvested);
        TextView tvCostPerUnit = (TextView) convertView.findViewById(R.id.tvCostPerUnit);
        tvDate.setText(new SimpleDateFormat("dd/MM/yy").format(transaction.getDate()));
        tvTransactionType.setText(transaction.getTransactionType());
        tvQuantity.setText(String.format("%.0f", transaction.getQuantity()));
        tvPrice.setText(String.format("%.4f", transaction.getPrice()));
        tvFee.setText(String.format("%.2f", transaction.getFee()));
        tvAmount.setText(String.format("%.2f", transaction.getAmount()));
        tvUnit.setText(String.format("%.0f", transaction.getUnit()));
        tvActualUnit.setText(String.format("%.0f", transaction.getActualUnit()));
        tvInvested.setText(String.format("%.2f", transaction.getInvested()));
        tvCostPerUnit.setText(String.format("%.4f", transaction.getCostPerUnit()));
        return convertView;
    }
}
