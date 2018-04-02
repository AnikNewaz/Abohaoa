package com.appmaester.abohaoa.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appmaester.abohaoa.R;

public class ChangeCityController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city_layout);

        final EditText etCityName = (EditText) findViewById(R.id.queryET);
        ImageButton backButtonChangeCity = (ImageButton) findViewById(R.id.backButton);

        // Sends back to Main Activity
        backButtonChangeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etCityName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String newCityName = etCityName.getText().toString();
                Intent newCityIntent = new Intent(ChangeCityController.this, AbohaoaController.class);
                newCityIntent.putExtra("City", newCityName);
                startActivity(newCityIntent);
                return false;
            }
        });
    }
}
