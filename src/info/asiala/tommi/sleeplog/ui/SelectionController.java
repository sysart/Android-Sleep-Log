package info.asiala.tommi.sleeplog.ui;

import android.content.Context;
import info.asiala.tommi.sleeplog.database.SleepyDao;
import info.asiala.tommi.sleeplog.domain.SleepEntry;
import info.asiala.tommi.sleeplog.domain.SleepLength;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectionController {
    private final List<SleepLength> sleepLengths;
    private SleepLength currentSelection;
    private SleepyDao sleepyDao;

    public SelectionController(Context applicationContext) {
        sleepLengths = createSleepLengths();
        currentSelection = getDefault();
        sleepyDao = new SleepyDao(applicationContext);
        List<SleepEntry> pastEntry = sleepyDao.getEntriesForPastNDays(1);

        if(pastEntry.size() == 1)
            setCurrent(pastEntry.get(0).getSleepLength());
    }

    private List<SleepLength> createSleepLengths() {
        List<SleepLength> sleepLengths = new ArrayList<SleepLength>();
        for (int i = 0; i < 24; i++) {
            sleepLengths.add(new SleepLength(i, 0));
            sleepLengths.add(new SleepLength(i, 15));
            sleepLengths.add(new SleepLength(i, 30));
            sleepLengths.add(new SleepLength(i, 45));
        }
        return sleepLengths;
    }

    public SleepLength getDefault() {
        return sleepLengths.get(sleepLengths.size() / 3);
    }

    private void setCurrent(SleepLength selection) {
        for(SleepLength sleepLength : sleepLengths)
            if(sleepLength.equals(selection))
                currentSelection = selection;
    }

    public SleepLength previous(int steps) {
        int currentSleepLengthIndex = sleepLengths.indexOf(getCurrent());
        int desiredSelectionIndex = currentSleepLengthIndex - steps;
        if(desiredSelectionIndex  <= 0)
            setCurrent(sleepLengths.get(0));
        else
            setCurrent(sleepLengths.get(currentSleepLengthIndex - steps));
        return getCurrent();
    }

    public SleepLength next(int steps) {
        int currentSleepLengthIndex = sleepLengths.indexOf(getCurrent());
        int desiredSelectionIndex = currentSleepLengthIndex + steps;
        if(desiredSelectionIndex  >= sleepLengths.size())
            setCurrent(sleepLengths.get(sleepLengths.size() - 1));
        else
            setCurrent(sleepLengths.get(currentSleepLengthIndex + steps));
        return getCurrent();
    }

    public SleepLength getCurrent() {
        return currentSelection;
    }

    public void save() {
        int twentyFourHours = 1000 * 60 * 60 * 24;
        Date enteringDate = new Date();
        Date nightDate = new Date(enteringDate.getTime() - twentyFourHours);
        SleepEntry sleepEntry = new SleepEntry(nightDate, getCurrent());
        sleepyDao.save(sleepEntry);
    }
}
