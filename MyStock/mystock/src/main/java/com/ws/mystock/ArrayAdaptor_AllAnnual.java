package com.ws.mystock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ws.libmystock.Cls_AllAnnual;

import java.util.ArrayList;

public class ArrayAdaptor_AllAnnual extends ArrayAdapter<Cls_AllAnnual> {
    private final Context context;
    private final ArrayList<Cls_AllAnnual> values;

    public ArrayAdaptor_AllAnnual(Context context, ArrayList<Cls_AllAnnual> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cls_AllAnnual annual = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.array_adapter_all_annual, parent, false);
        }
        TextView tvYear = (TextView) convertView.findViewById(R.id.tvYear);
        TextView tvMarketValue = (TextView) convertView.findViewById(R.id.tvMarketValue);
        TextView tvInvested = (TextView) convertView.findViewById(R.id.tvInvested);
        TextView tvDividend = (TextView) convertView.findViewById(R.id.tvDividend);
        TextView tvTotalDividend = (TextView) convertView.findViewById(R.id.tvTotalDividend);
        TextView tvGain = (TextView) convertView.findViewById(R.id.tvGain);
        TextView tvTotalGain = (TextView) convertView.findViewById(R.id.tvTotalGain);
        TextView tvPctGain = (TextView) convertView.findViewById(R.id.tvPctGain);
        TextView tvPctTotalGain = (TextView) convertView.findViewById(R.id.tvPctTotalGain);
        tvYear.setText(String.format("%d", annual.getYear()));
        tvMarketValue.setText(String.format("%.2f", annual.getMarketValue()));
        tvInvested.setText(String.format("%.2f", annual.getInvested()));
        tvDividend.setText(String.format("%.2f", annual.getDividend()));
        tvTotalDividend.setText(String.format("%.2f", annual.getTotalDividend()));
        tvGain.setText(String.format("%.2f", annual.getGain()));
        tvTotalGain.setText(String.format("%.2f", annual.getTotalGain()));
        tvPctGain.setText(String.format("%.2f%%", annual.getPctGain()));
        tvPctTotalGain.setText(String.format("%.2f%%", annual.getPctTotalGain()));
        return convertView;
    }
}
