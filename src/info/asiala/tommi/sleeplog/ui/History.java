package info.asiala.tommi.sleeplog.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import info.asiala.tommi.sleeplog.R;
import info.asiala.tommi.sleeplog.database.SleepyDao;
import info.asiala.tommi.sleeplog.domain.SleepEntry;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormat;
import java.util.List;

public class History extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
    }

    @Override
    protected void onResume() {
        super.onResume();

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        XYSeries series = new XYSeries(null);
        List<SleepEntry> entries = new SleepyDao(getApplicationContext()).getEntriesForPastNDays(30);
        Double longestSleepLength = 1.0;
        for(SleepEntry entry : entries) {
            int hours = entry.getSleepLength().getHours();
            int minutes = entry.getSleepLength().getMinutes();
            double yValue = hours + (minutes/60.0);
            series.add(entries.indexOf(entry) + 1, yValue);
            if(yValue > longestSleepLength)
                longestSleepLength = yValue;
        }

        dataset.addSeries(series);

        XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
        seriesRenderer.setFillBelowLine(true);
        seriesRenderer.setFillBelowLineColor(Color.GRAY);
        seriesRenderer.setColor(Color.GREEN);
        seriesRenderer.setLineWidth(2.0f);

        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setXLabels(entries.size());
        renderer.setYLabels(longestSleepLength.intValue() + 1);
        renderer.addSeriesRenderer(seriesRenderer);
        renderer.setChartTitle(getString(R.string.history_chart_title));
        renderer.setShowLegend(false);
        renderer.setLabelsColor(Color.GREEN);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(longestSleepLength + 1);
        renderer.setXAxisMin(1);
        renderer.setXAxisMax(entries.size());
        renderer.setPanEnabled(false, false);

        GraphicalView view = ChartFactory.getLineChartView(this, dataset, renderer);
        LinearLayout historyChartLayout = (LinearLayout) findViewById(R.id.history_chart);
        historyChartLayout.removeAllViews();
        historyChartLayout.addView(view);
    }
}