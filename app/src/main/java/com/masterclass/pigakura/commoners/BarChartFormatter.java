package com.masterclass.pigakura.commoners;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by tirgei on 6/23/17.
 */

public class BarChartFormatter implements IAxisValueFormatter {
    private final String[] labels;

    public BarChartFormatter(String[] labels) {
        this.labels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return labels[((int) value)-1];
    }
}
