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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;



public class Parameters extends AppCompatActivity {


    private Spinner dropdown;
    private ArrayAdapter<String> adapter;
    private String spinnerFill;
    private ArrayList<String> list = new ArrayList();


   @Override
    public void onBackPressed() {
       startActivity(new Intent(this,MasterActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);


        spinnerFill = getString(R.string.fileNameSpinner);

        initScreenParam();

        //dropdown = (Spinner)findViewById(R.id.spinner2);
        //adapter = new ArrayAdapter<String>(getBaseContext(),
                //android.R.layout.simple_list_item_1,list);


        final Button addSpinner = (Button) findViewById(R.id.addSpinner);
        addSpinner.setOnClickListener(OnClickAdd());

        final Button resetSpinner = (Button) findViewById(R.id.resetSpinner);
        resetSpinner.setOnClickListener(onClickReset_spinner());


    }



    /*                                                                      */
    /*                            BOUTONS                                   */
    /*                                                                      */
    /*                                                                      */


    public View.OnClickListener onClickReset_spinner() {
        View.OnClickListener rs = new View.OnClickListener(){
            @Override
            public void onClick(View v){

                resetFile(spinnerFill);

            }

            private void resetFile(String spinnerFill) {
                

            }
        };
        return rs;
    }







    public View.OnClickListener OnClickAdd() {
        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dropdown = (Spinner)findViewById(R.id.spinner2);

                EditText editSpinner = (EditText) findViewById(R.id.editSpinner);
                String userCat;
                userCat = editSpinner.getText().toString().toUpperCase().trim();
                System.out.println("LAAAAAAAAAAAA");

                if(userCat.isEmpty()){
                   Toast.makeText(getApplicationContext(), getString(R.string.emptyEntry), Toast.LENGTH_LONG).show();
                    editSpinner.setText(null);
                }
                else {


                    try {
                        FileOutputStream fis = openFileOutput(spinnerFill, MODE_APPEND);
                        PrintWriter pw = new PrintWriter(fis);
                        System.out.println("ICIIIIIIIIIIII");
                        pw.print(userCat + '\n');
                        pw.close();
                    }
                    catch (java.io.IOException e) {
                        System.out.println(e.getMessage());
                    }

                    list.add(userCat);
                    dropdown.setAdapter(adapter);
                    editSpinner.setText(null);
                }

            }
        };
        return on;
    }




    /*                          FONCTIONS                                   */
    /*                                                                      */
    /*                                                                      */
    /*                                                                      */

    private void initScreenParam() {



        //Appel dans  resources du fichier spinner_fr
        InputStream inputStream_spinner = getResources().openRawResource(
                getResources().getIdentifier("spinner_fr",
                        "raw", getPackageName()));
        dropdown = (Spinner)findViewById(R.id.spinner2);
        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1,list);





        //Chargement du Spinner initial
        try{
            InputStreamReader fis = new InputStreamReader(inputStream_spinner);
            BufferedReader br = new BufferedReader(fis);
            String line;
            line = br.readLine();

            while (line != null)
            {
                String lineTab[] = line.split(",");
                list.add(lineTab[0]);
                list.add(lineTab[1]);
                list.add(lineTab[2]);
                line = br.readLine();
            }
            br.close();
            dropdown.setAdapter(adapter);
        }
        catch (java.io.IOException e){
            e.getMessage();
        }
        //Chargement du Spinner à partir du fichier fileNameSpinner ( Entrée Utilisateur )
        try {
            FileInputStream fis2 = openFileInput(spinnerFill);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
            String line2;
            line2 = br2.readLine();

            while (line2!=null)
            {
                list.add(line2);
                line2 = br2.readLine();
            }
            br2.close();
            dropdown.setAdapter(adapter);
        }
        catch(java.io.IOException e)
        {
            e.getMessage();
        }
    }
}