package dk.moerks.ratebeermobile;

import android.os.Bundle;
import dk.moerks.ratebeermobile.activity.BetterRBDefaultActivity;

public class Dashboard extends BetterRBDefaultActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        initializeActionBar();
    }
}
