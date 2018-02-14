package com.example.zimzik.newsqlitesorting;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText etFunction, etPopulation, etRegion;
    RadioGroup rgSort;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find EditText views
        etFunction = findViewById(R.id.et_function);
        etPopulation = findViewById(R.id.et_population);
        etRegion = findViewById(R.id.et_reg_pop);
        rgSort = findViewById(R.id.rgSort);
        // Conntect to DB
        dbHelper = new DBHelper(this);

        // Show all records in log 
        findViewById(R.id.all_records_button).setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.query(DBHelper.TABLE_COUNTRIES, null, null, null, null, null, null, null);
            showData(c);
        });

        // Show records by function
        findViewById(R.id.function_button).setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String columns[] = new String[] {etFunction.getText().toString()};
            Cursor c = db.query(DBHelper.TABLE_COUNTRIES, columns, null, null, null, null, null);
            showData(c);
        });

        // Show records with population higher, than indicated

        findViewById(R.id.population_button).setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selection = "population > ?";
            String selectionArgs[] = new String[] {etPopulation.getText().toString()};
            Cursor c = db.query(DBHelper.TABLE_COUNTRIES, null, selection, selectionArgs, null, null, null);
            showData(c);
        });


        // Show population of region
        findViewById(R.id.reg_pop_button).setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String columns[] = new String[] {"region", "sum(population) as population"};
            String groupBy = "region";
            Cursor c = db.query(DBHelper.TABLE_COUNTRIES, columns, null, null, groupBy, null, null);
            showData(c);
        });

        // Show population by region higher tha indicated
        findViewById(R.id.reg_pop_value_button).setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String columns[] = new String[] {"region", "sum(population) as population"};
            String groupBy = "region";
            String having = "sum(population) > " + etRegion.getText().toString();
            Cursor c = db.query(DBHelper.TABLE_COUNTRIES, columns, null, null, groupBy, having, null);
            showData(c);
        });

        //Show sorted DB by checkox condition
        findViewById(R.id.sorting_button).setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String orderedBy = "";
            switch (rgSort.getCheckedRadioButtonId()) {
                case R.id.r_name:
                    orderedBy = "name";
                    break;
                case  R.id.r_pop:
                    orderedBy = "population";
                    break;
                case R.id.r_region:
                    orderedBy = "region";
                    break;
            }
            Cursor c = db.query(DBHelper.TABLE_COUNTRIES, null, null, null, null, null, orderedBy);
            showData(c);
        });


        // Temporary button for deleting DB
        findViewById(R.id.delete_db_button).setOnClickListener(v -> {
            this.deleteDatabase(DBHelper.DATABASE_NAME);
        });
    }

    public void showData(Cursor c) {
        String str;
        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = c.getColumnIndex(DBHelper.KEY_NAME);
            int popIndex = c.getColumnIndex(DBHelper.KEY_POPULATION);
            int regIndex = c.getColumnIndex(DBHelper.KEY_REGION);
            do {
                str = "";
                for (String cn: c.getColumnNames()) {
                    str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                }
                Log.d(TAG, str);
            } while (c.moveToNext());
        } else {
            Log.d(TAG, "DB is empty!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_add:
                Intent intent = new Intent(MainActivity.this, AddNewCountryActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
