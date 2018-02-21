package com.masterclass.pigakura.commoners;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by root on 7/13/17.
 */

public class DecimalRemover extends PercentFormatter{
    protected DecimalFormat format;

    public DecimalRemover(DecimalFormat format){
        this.format = format;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataIndex, ViewPortHandler viewPortHandler){
        return format.format(value) + " %";
    }

}
