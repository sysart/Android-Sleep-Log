package info.asiala.tommi.sleeplog.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import info.asiala.tommi.sleeplog.R;

public class SleepTabWidget extends TabActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        Resources res = getResources();
        TabHost tabHost = getTabHost();

        Intent enterIntent = new Intent().setClass(this, Enter.class);
        TabHost.TabSpec enterSpec = tabHost.newTabSpec("enter").setIndicator("Enter",
                res.getDrawable(R.drawable.ic_tab_zzz))
                .setContent(enterIntent);
        tabHost.addTab(enterSpec);

        Intent statisticsIntent = new Intent().setClass(this, Statistics.class);
        TabHost.TabSpec statisticsSpec = tabHost.newTabSpec("statistics").setIndicator("Statistics",
                res.getDrawable(R.drawable.ic_tab_7))
                .setContent(statisticsIntent);
        tabHost.addTab(statisticsSpec);

        Intent historyIntent = new Intent().setClass(this, History.class);
        TabHost.TabSpec historySpec = tabHost.newTabSpec("history").setIndicator("History",
                res.getDrawable(R.drawable.ic_tab_30))
                .setContent(historyIntent);
        tabHost.addTab(historySpec);

        tabHost.setCurrentTab(0);

    }
}