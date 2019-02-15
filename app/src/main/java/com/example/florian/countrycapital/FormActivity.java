package com.example.florian.countrycapital;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lamudi.phonefield.PhoneEditText;

public class FormActivity extends AppCompatActivity {
    static Dialog d;
    static int year;
    static TextView b;
    static ImageView date;
    static ImageView local;
    static TextView userAddress;
    static EditText username;
    static public String EXTRA_MESSAGE = "username";
    PhoneEditText phoneEditText;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        year = Calendar.getInstance().get(Calendar.YEAR);
        b = (TextView) findViewById(R.id.button);

        date = (ImageView) findViewById(R.id.datePicker);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });

        phoneEditText = (PhoneEditText) findViewById(R.id.edit_text);
        button = (Button) findViewById(R.id.submit_button);


        phoneEditText.setHint(R.string.phone_hint);
        phoneEditText.setDefaultCountry("FR");

        local = (ImageView) findViewById(R.id.local);
        userAddress = (TextView) findViewById(R.id.textLocal);
        username = (EditText) findViewById(R.id.editText);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocalisation(v);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                if (phoneEditText.isValid()) {
                    phoneEditText.setError(null);
                } else {
                    phoneEditText.setError(getString(R.string.invalid_phone_number));
                    valid = false;
                }

                if (valid) {
                    Toast.makeText(FormActivity.this, R.string.valid_phone_number, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FormActivity.this, GameActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, username.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(FormActivity.this, R.string.invalid_phone_number, Toast.LENGTH_LONG).show();
                }

                // Return the phone number as follows
                String phoneNumber = phoneEditText.getPhoneNumber();
            }
        });
    }

    public void setDate(View view) {
        d = new Dialog(FormActivity.this);
        d.setTitle("Year Picker");
        d.setContentView(R.layout.yeardialog);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        TextView year_text=(TextView)d.findViewById(R.id.year_text);
        year_text.setText(""+year);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);
        Log.i("YEAR", "setDate: "+year);
        nopicker.setMaxValue(year+50);
        nopicker.setMinValue(year-50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                b.setText(String.valueOf(nopicker.getValue()));
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public void getLocalisation(View view) {
        d = new Dialog(FormActivity.this);
        d.setContentView(R.layout.locatiodialog);
        MapView mMapView = (MapView) d.findViewById(R.id.mapView);
        MapsInitializer.initialize(FormActivity.this);
        mMapView.onCreate(d.onSaveInstanceState());
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {

                LatLng sydney = new LatLng(-33.852, 151.211);
                googleMap.addMarker(new MarkerOptions().position(sydney)
                        .title("Marker in Sydney"));
                Geocoder geocoder = new Geocoder(FormActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(sydney.latitude, sydney.longitude, 1);
                    Log.e("toto", addresses.get(0).getAddressLine(0));
                    userAddress.setText(addresses.get(0).getAddressLine(0));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("toto", e.getLocalizedMessage());
                    userAddress.setText("Could not find nearest address");
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        });
        d.show();
    }


}
