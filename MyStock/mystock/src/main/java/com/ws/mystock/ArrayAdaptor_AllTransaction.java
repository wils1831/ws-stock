package com.ws.mystock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ws.libmystock.Cls_AllTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ArrayAdaptor_AllTransaction extends ArrayAdapter<Cls_AllTransaction> {
    private final Context context;
    private final ArrayList<Cls_AllTransaction> values;

    public ArrayAdaptor_AllTransaction(Context context, ArrayList<Cls_AllTransaction> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.e("TransactionArrayAdaptor", "position: " + position);
        Cls_AllTransaction transactions = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.array_adapter_all_transaction, parent, false);
        }
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvTransactionType = (TextView) convertView.findViewById(R.id.tvTransactionType);
        TextView tvStockName = (TextView) convertView.findViewById(R.id.tvStockName);
        TextView tvQuantity = (TextView) convertView.findViewById(R.id.tvQuantity);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        TextView tvFee = (TextView) convertView.findViewById(R.id.tvFee);
        TextView tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
        TextView tvBalance = (TextView) convertView.findViewById(R.id.tvBalance);
        TextView tvInvested = (TextView) convertView.findViewById(R.id.tvInvested);
        tvDate.setText(new SimpleDateFormat("dd/MM/yy").format(transactions.getDate()));
        tvTransactionType.setText(transactions.getTransactionType());
        tvStockName.setText(transactions.getStockName());
        tvQuantity.setText(String.format("%.0f", transactions.getQuantity()));
        tvPrice.setText(String.format("%.4f", transactions.getPrice()));
        tvFee.setText(String.format("%.2f", transactions.getFee()));
        tvAmount.setText(String.format("%.2f", transactions.getAmount()));
        tvBalance.setText(String.format("%.2f", transactions.getBalance()));
        tvInvested.setText(String.format("%.2f", transactions.getInvested()));
        return convertView;
    }
}
