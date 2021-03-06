package com.euyuil.alarmmap.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

import com.euyuil.alarmmap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class EditLocationActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        Toast.makeText(this, getIntent().getDataString(), Toast.LENGTH_SHORT).show();
    }

    private GoogleMap googleMap;

    @Override
    protected void onResume() {
        super.onResume();
        initializeGoogleMap();
    }

    private void initializeGoogleMap() {
        if (googleMap == null) {
            try {
                googleMap = ((SupportMapFragment)
                        getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (googleMap == null) {
                Toast.makeText(this, "Unable to show Google Maps.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_alarm, menu);
        return true;
    }
}
