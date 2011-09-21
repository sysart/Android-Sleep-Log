package info.asiala.tommi.sleeplog.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import info.asiala.tommi.sleeplog.R;
import info.asiala.tommi.sleeplog.domain.SleepLength;

public class Enter extends Activity {

    private SelectionController selectionController;
    private TextView amountOfSleepView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter);

        amountOfSleepView = (TextView) findViewById(R.id.amount_of_sleep);
        amountOfSleepView.setOnTouchListener(new GestureListener(this));

        selectionController = new SelectionController(getApplicationContext());
        updateSleepLengthView(selectionController.getCurrent());
    }

    private void selectPrevious(int steps) {
        updateSleepLengthView(selectionController.previous(steps));
    }

    private void selectNext(int steps) {
        updateSleepLengthView(selectionController.next(steps));
    }

    private void updateSleepLengthView(SleepLength sleepLength) {
        amountOfSleepView.setText(sleepLength.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        selectionController.save();
    }

    class GestureListener implements View.OnTouchListener {
        private static final int STEP = 30;
        private final Enter enterSleepActivity;
        private float previousX;
        private float previousY;


        public GestureListener(Enter enterSleepActivity) {
            this.enterSleepActivity = enterSleepActivity;
        }


        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN: {
                    previousX = event.getX();
                    previousY = event.getX();
                    return true;
                }

                case MotionEvent.ACTION_MOVE: {
                    float deltaY = previousY - event.getY();
                    float deltaX = previousX - event.getX();

                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > STEP) {
                            if (deltaX > 0)
                                this.onRightToLeftSwipe((int) (deltaX / STEP));
                            else
                                this.onLeftToRightSwipe(Math.abs((int) (deltaX / STEP)));
                            previousX = event.getX();
                            previousY = event.getY();
                        }
                    } else {
                        if (Math.abs(deltaY) > STEP) {
                            if (deltaY > 0)
                                this.onRightToLeftSwipe((int) (deltaY / STEP));
                            else
                                this.onLeftToRightSwipe(Math.abs((int) (deltaY / STEP)));
                            previousX = event.getX();
                            previousY = event.getY();
                        }
                    }
                    return true;
                }
            }
            return false;

        }

        private void onRightToLeftSwipe(int steps) {
            enterSleepActivity.selectNext(steps);
        }

        private void onLeftToRightSwipe(int steps) {
            enterSleepActivity.selectPrevious(steps);
        }
    }
}
