package com.example.expeditionhacks2018;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.maps.model.LatLng;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class PieChart extends android.support.v4.app.Fragment implements OnChartValueSelectedListener {

    View myView;
    DataRelay dataRelay;
    public PieChart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         myView =  inflater.inflate(R.layout.fragment_pie_chart, container, false);
        dataRelay = (DataRelay) getActivity().getApplicationContext();

        setUpPieChart();
        return myView;
    }

    public void setUpPieChart()
    {
        com.github.mikephil.charting.charts.PieChart mChart = (com.github.mikephil.charting.charts.PieChart) myView.findViewById(R.id.piechart1);
        mChart.setVisibility(View.VISIBLE);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setOnChartValueSelectedListener(this);

        //ENTRIES BELOW

        HashMap<LatLng, AnalysisResults> analysisResults = dataRelay.analysisResults;

        HashMap<String, Integer> entityMap = new HashMap<>();
        AnalysisResults testing = analysisResults.get(new LatLng(dataRelay.someLocation.getLatitude(), dataRelay.someLocation.getLongitude()));
        float anger = (float) testing.getEmotion().getDocument().getEmotion().getAnger().floatValue();
        float disgust = (float)testing.getEmotion().getDocument().getEmotion().getDisgust().floatValue();
        float fear = (float) testing.getEmotion().getDocument().getEmotion().getFear().floatValue();
        float joy = (float) testing.getEmotion().getDocument().getEmotion().getJoy().floatValue();
        float sadness = (float) testing.getEmotion().getDocument().getEmotion().getSadness().floatValue();
        ArrayList<Float> list = new ArrayList<>();
        list.add(anger);
        list.add(disgust);
        list.add(fear);
        list.add(joy);
        list.add(sadness);


        float mult = 100;
        int count = 5;
        String[] mParties = new String[] {
                "Anger", "Disgust", "Fear", "Joy", "Sadness", "Party F", "Party G", "Party H",
                "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
                "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
                "Party Y", "Party Z"};
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) (list.get(i)),
                    mParties[i % mParties.length],
                    getResources().getDrawable(R.drawable.icon_pi)));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : com.github.mikephil.charting.utils.ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : com.github.mikephil.charting.utils.ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : com.github.mikephil.charting.utils.ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : com.github.mikephil.charting.utils.ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(com.github.mikephil.charting.utils.ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
//        mChart.highlightValues(null);
        mChart.invalidate();


        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);










    }
    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Traffick Data");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(.8f), 0, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(com.github.mikephil.charting.utils.ColorTemplate.getHoloBlue()), 0, s.length(), 0);
        return s;
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}
