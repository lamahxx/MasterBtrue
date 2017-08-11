package akumapps.android.masterb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;


public class Parameters extends AppCompatActivity {


    private Spinner dropdown;
    private Spinner dropdownThresh_1;
    private ArrayAdapter<String> adapter;
    private String spinnerFill; //fichier
    private String fileBudgetMax; //fichier
    private String fileThreshold; //fichier
    String fileNameThreshAmount; //fichier
    private ArrayList<String> list = new ArrayList();
    private ArrayAdapter<String> adapterThresh;
    private ArrayList<String> listThresh = new ArrayList();
    int nb_threshold_items;



    @Override
    public void onBackPressed() {
        super.onBackPressed();


        startActivity(new Intent(this, MasterActivity.class));
        this.finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);


        initScreenParam();

        final Button addThreshold = (Button) findViewById(R.id.buttonThreshold_1);
        addThreshold.setOnClickListener(OnClickAddThreshold());

        final Button resetThreshold = (Button) findViewById(R.id.buttonDeleteThresh);
        resetThreshold.setOnClickListener(OnClick_Reset_Threshold());


        final Button addSpinner = (Button) findViewById(R.id.addSpinner);
        addSpinner.setOnClickListener(OnClickAdd());

        final Button resetSpinner = (Button) findViewById(R.id.resetSpinner);
        resetSpinner.setOnClickListener(onClickReset_spinner());

        final Button submit = (Button) findViewById(R.id.buttonValider);
        submit.setOnClickListener(onClickSubmit());

        final Button deleteItem = (Button) findViewById(R.id.deleteItem);
        deleteItem.setOnClickListener(onClick_delete_Item());
    }







    /*                                                                      */
    /*                            BOUTONS                                   */
    /*                                                                      */
    /*                                                                      */



    /*                           buttonDeleteItem                           */
    public View.OnClickListener onClick_delete_Item(){
        View.OnClickListener v = new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String selectedLabel;
                selectedLabel = dropdown.getSelectedItem().toString();
                delete_selected_label(selectedLabel);
                setFile();
                dropdown.setAdapter(adapter);
            }
        };
        return v;
    }

    /*                           buttonSubmit                               */
    public View.OnClickListener onClickSubmit() {
        View.OnClickListener rs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String budgetMaxString;
                //on code ici
                EditText editBudgetMax = (EditText) findViewById(R.id.max_threshold_value);
                budgetMaxString = editBudgetMax.getText().toString().trim();

                if (budgetMaxString.isEmpty()) {

                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.emptyEntry),
                            Toast.LENGTH_LONG);
                    toast.show();

                } else {

                    try {
                        FileOutputStream fos = openFileOutput(fileBudgetMax, MODE_PRIVATE);
                        PrintWriter pw = new PrintWriter(fos);
                        pw.print(budgetMaxString);
                        pw.close();

                    } catch (java.io.IOException e) {
                        e.getMessage();
                    }

                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.maximumBudget_toast) + " " + budgetMaxString + " €", Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        };
        return rs;
    }



    /*                          buttonReset                                 */

    public View.OnClickListener onClickReset_spinner() {
        View.OnClickListener rs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputStream inputStream_spinner = getResources().openRawResource(
                        getResources().getIdentifier("spinner_fr",
                                "raw", getPackageName()));
                dropdown = (Spinner) findViewById(R.id.spinner2);
                dropdownThresh_1 = (Spinner) findViewById(R.id.spinnerThreshold_1);
                adapter = new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_list_item_1, list);

                resetFile(fileThreshold);
                adapterThresh.clear();
                resetFile(spinnerFill);
                adapter.clear();
                try {
                    InputStreamReader fis = new InputStreamReader(inputStream_spinner);
                    BufferedReader br = new BufferedReader(fis);
                    String line;
                    line = br.readLine();

                    while (line != null) {
                        String lineTab[] = line.split(",");
                        list.add(lineTab[0]);
                        list.add(lineTab[1]);
                        list.add(lineTab[2]);
                        line = br.readLine();
                    }
                    br.close();
                    dropdown.setAdapter(adapter);
                    dropdownThresh_1.setAdapter(adapter);
                } catch (java.io.IOException e) {
                    e.getMessage();
                }
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.reset_ok),
                        Toast.LENGTH_LONG);
                toast.show();

            }

            private void resetFile(String spinnerFill) {

                try {
                    FileOutputStream fis = openFileOutput(spinnerFill, MODE_PRIVATE);
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(fis));
                    pw.close();
                } catch (java.io.IOException e) {
                    e.getMessage();
                }

            }
        };
        return rs;
    }



    /*                                  buttonAdd                       */

    public View.OnClickListener OnClickAdd() {
        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dropdown = (Spinner) findViewById(R.id.spinner2);
                EditText editSpinner = (EditText) findViewById(R.id.editSpinner);
                String userCat;
                userCat = editSpinner.getText().toString().toUpperCase().trim();

                if (userCat.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.emptyEntry), Toast.LENGTH_LONG).show();
                    editSpinner.setText(null);

                }
                boolean a = check_if_exists(userCat);
                if (a) {
                    Toast.makeText(getApplicationContext(), "ALREADY EXISTS", Toast.LENGTH_SHORT).show();
                    editSpinner.setText(null);
                }

                if (!userCat.isEmpty() && !a) {

                    try {
                        FileOutputStream fis = openFileOutput(spinnerFill, MODE_APPEND);
                        PrintWriter pw = new PrintWriter(fis);
                        pw.print(userCat + '\n');
                        pw.close();
                    } catch (java.io.IOException e) {
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


    /*                              buttonAddThresholdItem                      */
    public View.OnClickListener OnClickAddThreshold() {
        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String threshold_item;
                threshold_item = dropdownThresh_1.getSelectedItem().toString();

                ListView listViewThresh = (ListView) findViewById(R.id.listThresholds);

                boolean a = check_if_exists_thresh(threshold_item);


                if (nb_threshold_items < 3 && !a) {
                    listThresh.add(threshold_item);
                    listViewThresh.setAdapter(adapterThresh);

                    try {
                        FileOutputStream fis = openFileOutput(fileThreshold, MODE_APPEND);
                        PrintWriter pw = new PrintWriter(fis);
                        pw.print(threshold_item + '\n');
                        pw.close();

                    } catch (java.io.IOException e) {
                        e.getMessage();
                    }
                    nb_threshold_items++;
           //         add_set_Threshold_Amount();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Either Label already added or can't add more", Toast.LENGTH_SHORT).show();
                }


            }
        };
        return on;
    }


    /*                          buttonResetThreshold                    */
    public View.OnClickListener OnClick_Reset_Threshold() {
        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nb_threshold_items > 0) {
                    ListView listViewThresh = (ListView) findViewById(R.id.listThresholds);


                    String line;
                    int k;
                    k = listThresh.size() - 2;


                    try {
                        FileOutputStream fos = openFileOutput(fileThreshold, MODE_PRIVATE);
                        PrintWriter pw = new PrintWriter(fos);
                        while (k > -1) {
                            line = listThresh.get(k);
                            pw.print(line + '\n');
                            k--;
                        }
                        pw.close();
                    } catch (java.io.IOException e) {
                        e.getMessage();
                    }
                    listThresh.remove(listThresh.size() - 1);
                    listViewThresh.setAdapter(adapterThresh);
                    nb_threshold_items--;
    //               remove_Threshold();

                } else {
                    Toast.makeText(getApplicationContext(), "No Thresholds Set", Toast.LENGTH_SHORT).show();
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
        dropdown = (Spinner) findViewById(R.id.spinner2);
        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1, list);
        adapterThresh = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1, listThresh);

        // Fichiers
        spinnerFill = getString(R.string.fileNameSpinner);
        fileBudgetMax = getString(R.string.fileNameBudget_max);
        fileThreshold = getString(R.string.fileNameThreshold);

        EditText editTextBudget = (EditText) findViewById(R.id.max_threshold_value);
        dropdownThresh_1 = (Spinner) findViewById(R.id.spinnerThreshold_1);

        //Affichage du budget maximum
        try {
            FileInputStream fis = openFileInput(fileBudgetMax);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            line = br.readLine();
            if (line != null) {
                editTextBudget.setText(line);
            } else {
                editTextBudget.setText("0");
            }
            br.close();
        } catch (java.io.IOException e) {
            e.getMessage();
        }

        //Chargement valeur des Threshold

//       init_ThresholdAmounts();

        //Chargement du Spinner initial
        try {
            InputStreamReader fis = new InputStreamReader(inputStream_spinner);
            BufferedReader br = new BufferedReader(fis);
            String line;
            line = br.readLine();

            while (line != null) {
                String lineTab[] = line.split(",");
                list.add(lineTab[0]);
                list.add(lineTab[1]);
                list.add(lineTab[2]);
                line = br.readLine();
            }
            br.close();
            dropdown.setAdapter(adapter);
            dropdownThresh_1.setAdapter(adapter);

        } catch (java.io.IOException e) {
            e.getMessage();
        }
        //Chargement du Spinner à partir du fichier fileNameSpinner ( Entrée Utilisateur )
        try {
            FileInputStream fis2 = openFileInput(spinnerFill);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
            String line2;
            line2 = br2.readLine();

            while (line2 != null) {
                list.add(line2);
                line2 = br2.readLine();
            }
            br2.close();
            dropdown.setAdapter(adapter);
            dropdownThresh_1.setAdapter(adapter);
        } catch (java.io.IOException e) {
            e.getMessage();
        }

        //Chargement de la ListView

        ListView listView = (ListView) findViewById(R.id.listThresholds);


        try {
            FileInputStream fis = openFileInput(fileThreshold);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            line = br.readLine();
            while (line != null && nb_threshold_items < 3) {
                nb_threshold_items++;
                listThresh.add(line);
                line = br.readLine();
            }
            br.close();
            listView.setAdapter(adapterThresh);
        } catch (java.io.IOException e) {
            e.getMessage();
        }
    }


    private boolean check_if_exists(String enteredLabel) {
        int i = 0;
        while (i < list.size()) {
            if (enteredLabel.trim().equals(list.get(i))) {
                return true;
            } else {
                i++;
            }
        }
        return false;
    }



    private boolean check_if_exists_thresh(String submittedLabel){
        int i = 0;
        while ( i < listThresh.size()){
            if ( submittedLabel.trim().equals(listThresh.get(i))){
                return true;
            }
            else{
                i++;
            }
        }
        return false;
    }

    private void delete_selected_label(String selectedLabel){
        int i = 0;
        while (i < list.size()){
            if(selectedLabel.equals(list.get(i))){
                list.remove(i);
            }
            else{i++;}
        }
    }

    private void setFile(){

        try{
            FileOutputStream fos = openFileOutput(spinnerFill, MODE_PRIVATE);
            PrintWriter pw = new PrintWriter(fos);
            int i = 0;
            while (i <list.size()){
                pw.print(list.get(i)+"\n");
                i++;
            }
            pw.close();
        }
        catch(java.io.IOException e){
            e.getMessage();
        }
    }

}