package dk.moerks.ratebeermobile;

import android.os.Bundle;
import android.widget.ImageButton;
import dk.moerks.ratebeermobile.activity.BetterRBDefaultActivity;

public class Dashboard extends BetterRBDefaultActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        initializeActionBar();
        
        ImageButton profileButton = (ImageButton)findViewById(R.id.dashboard_profile_button);
        
    }
}
