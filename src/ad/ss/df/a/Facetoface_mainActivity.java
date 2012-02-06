package ad.ss.df.a;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class Facetoface_mainActivity extends Activity {
    /** Called when the activity is first created. */
	
	TabHost tab;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tab = (TabHost) findViewById(R.id.tabhost);
        tab.setup();
        
        tab.setCurrentTab(0);
        
        TabHost.TabSpec spec1 = tab.newTabSpec("friends");
        TabHost.TabSpec spec2 = tab.newTabSpec("game friends");
        TabHost.TabSpec spec3 = tab.newTabSpec("invite");
    
        
        spec1.setIndicator("friends");
        spec2.setIndicator("game firends");
        spec3.setIndicator("invite");
        
        spec1.setContent(R.id.test);
        spec2.setContent(R.id.test);
        spec3.setContent(R.id.test);
        
        //=3=
        
        tab.addTab(spec1);
        tab.addTab(spec2);
        tab.addTab(spec3);
        
    }
}