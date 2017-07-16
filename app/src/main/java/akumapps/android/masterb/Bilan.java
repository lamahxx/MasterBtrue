package akumapps.android.masterb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Bilan extends AppCompatActivity {


    private Spinner dropdown11;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilan);


        initScreen();


    }

    private void initScreen(){
        Spinner dropdown11 = (Spinner) findViewById(R.id.spinner2);

        String[] items = new String[]{"COURSES", "BIERES", "CLOPES", "DEPENSE INUTILE"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        dropdown11.setAdapter(adapter);
    }

}