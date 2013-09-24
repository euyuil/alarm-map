package com.euyuil.alarmmap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EditLocationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_location, menu);
        return true;
    }
    
}
