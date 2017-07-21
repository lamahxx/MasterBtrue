package akumapps.android.masterb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class Parameters extends AppCompatActivity {


    private Spinner dropdown;
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        Intent intent = getIntent();
        ArrayList<String> spinner = intent.getStringArrayListExtra("string-array");
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, spinner);


        final Button addSpinner = (Button) findViewById(R.id.addSpinner);
        addSpinner.setOnClickListener(onClickAdd(adapterSpinner));


        dropdown = (Spinner) findViewById(R.id.spinner2);


        dropdown.setAdapter(adapterSpinner);
    }

    public View.OnClickListener onClickAdd(final ArrayAdapter s) {
        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText editSpinner = (EditText) findViewById(R.id.editSpinner);

                String libelle = editSpinner.getText().toString().toUpperCase();
                Intent intent2 = getIntent();

                ArrayList<String> spinner = intent2.getStringArrayListExtra("string-array");
                editSpinner.setText(null);
                spinner.add(libelle);
                dropdown.setAdapter(s);


            }

        };
        return on;

    }
}