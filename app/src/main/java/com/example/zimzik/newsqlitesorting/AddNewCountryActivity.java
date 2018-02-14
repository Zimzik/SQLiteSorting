package com.example.zimzik.newsqlitesorting;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddNewCountryActivity extends AppCompatActivity {
    DBHelper dbHelper;
    TextView emptyRegionError;
    EditText etName, etPopulation;
    Spinner spinner;
    String region;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_country);

        etName = findViewById(R.id.et_name);
        etPopulation = findViewById(R.id.et_population);
        emptyRegionError = findViewById(R.id.tv_empty_region);

        dbHelper = new DBHelper(this);

        // spinner logic
        spinner = findViewById(R.id.regions_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.regions_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                region = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                region = "";
            }
        });

        // Save button handler
        findViewById(R.id.save_button).setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            String name = etName.getText().toString();
            int population = Integer.parseInt(etPopulation.getText().toString());
            cv.put(DBHelper.KEY_NAME, name);
            cv.put(DBHelper.KEY_POPULATION, population);
            if (region.equalsIgnoreCase("")) {
                emptyRegionError.setText("Please, choose a region");
            } else {
                emptyRegionError.setText("");
                cv.put(DBHelper.KEY_REGION, region);
                db.insert(DBHelper.TABLE_COUNTRIES, null, cv);
                etName.setText("");
                etPopulation.setText("");
                db.close();
            }
        });

        // Cancel button handler
        findViewById(R.id.cancel_button).setOnClickListener(v -> finish());

    }
}
