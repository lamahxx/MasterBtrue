package akumapps.android.masterb;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.TimeZoneNames;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class MasterActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView depense;
    private ArrayAdapter<String> adapter ;
    private ArrayList<String> listDepense ;
    private Spinner dropdown;
    private float depenseInutiles;
    private float depenseInutilesTriggerer;


    private Date semaine [];
    private Date mois [];


    private String fileName = "montantCourant";
    private String fileNameList = "listDepense";
    private String fileNameDico = "montantCourant";


    //*******************MAIN********************//
    //******************************************//
    //*****************************************//


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);


        depenseInutilesTriggerer = 500.0f;
        initScreen();



        final Button buttonAdd= (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(OnClickAdd());

        final Button buttonBilan= (Button) findViewById(R.id.bilan);
        buttonBilan.setOnClickListener(OnClickBilan());

        final Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(OnClickReset());


    }




    //****************BOUTONS**************************//
    //************************************************//
    //***********************************************//


    public View.OnClickListener OnClickAdd() {
        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v){

                EditText montant= (EditText) findViewById(R.id.montant);
             EditText nomDepense= (EditText) findViewById(R.id.libelle);
                TextView montantTotal = (TextView) findViewById(R.id.montantTotal);




                String montantIString=montant.getText().toString();

                if(montantIString.isEmpty())
                {
                    montantIString="0.0";
                }


                Float montantI= Float.parseFloat(montantIString);
                if( montantI > 9999999 | montantI< 0)
                {
                    montantI = 0.0f;
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "T'es pas si riche !", Toast.LENGTH_SHORT);
                    toast.show();
                }

                String montantTotalString=montantTotal.getText().toString();

                if(montantTotalString.isEmpty())
                {
                    montantTotalString="0.0";

                }

                Float montantTotalI= Float.parseFloat(montantTotalString);

                montantTotalI+=montantI;
                if(dropdown.getSelectedItem().toString().equals("DEPENSE INUTILE"))
                {
                    depenseInutiles+= montantI;
                }
                if(depenseInutiles >= depenseInutilesTriggerer)
                {

                    Toast toast = new Toast(getApplicationContext());
                    ImageView view = new ImageView(getApplicationContext());
                    view.setImageResource(R.drawable.logo);
                    toast.setView(view);
                    toast.show();
                    //Toast test1 = Toast.makeText(getApplicationContext(),
                    // "ATTENTION LA C EST CHAUD", Toast.LENGTH_LONG);
                    //test1.show();
                }
                setText(montantTotal,montantTotalI.toString(), MODE_PRIVATE);
                if(montantIString.isEmpty() || nomDepense.getText().toString().isEmpty() ||
                        montantIString.equals("0.0"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Faut tout remplir Negrillon", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    addToList(montantIString, nomDepense.getText().toString(),
                            dropdown.getSelectedItem().toString() );
                    setList(listDepense, MODE_PRIVATE);
                    montant.setText(null);
                    nomDepense.setText(null);
                }
            }


        };
        return on;
    }




    public View.OnClickListener OnClickReset()
    {
       View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ouverture et fermeture du fichier pour effacer le fichier : MONTANT TOTAL
                try{
                    FileOutputStream fos = openFileOutput(fileName,MODE_PRIVATE);
                    PrintWriter pw = new PrintWriter( new OutputStreamWriter(fos));
                    pw.close();

                }
                catch(java.io.IOException e){
                    e.getMessage();
                }

                //On Affiche 0 dans le TEXTVIEW
                TextView montantTotal= (TextView) findViewById(R.id.montantTotal);
                montantTotal.setText("0");

               //Ouverture et fermeture du fichier pour effacer le fichier : LISTDEPENSE
                try
                {
                    FileOutputStream fosi = openFileOutput(fileNameList,MODE_PRIVATE);
                    PrintWriter pww = new PrintWriter( new OutputStreamWriter(fosi));
                    pww.close();
                }
                catch(java.io.IOException e)
                {
                    e.getMessage();
                }

                //On Affiche rien dans la LISTEVIEW
                adapter.clear();

            }
        };
        return on;

    }



    public View.OnClickListener OnClickBilan() {
        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasterActivity.this, Bilan.class);
                startActivity(intent);
            }

        };
        return on;
    }





    //********************FONCTIONS********************//
    //************************************************//
    //***********************************************//


    private void addToList(String montantI, String nomDepense, String libelle ) {

            listDepense.add(montantI.trim() + " " + nomDepense.trim() + " " + libelle);
            mListView.setAdapter(adapter);
    }

    private void initScreen() {

        //Init des variables
        mListView = (ListView) findViewById(R.id.list);
        depense = (TextView) findViewById(R.id.montantTotal);
        listDepense = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1,listDepense);
        dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"COURSES", "BIERES", "CLOPES", "DEPENSE INUTILE"};

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapterSpinner);
        depenseInutilesTriggerer = 500;

        //Chargement du montant total
        try
        {

            FileInputStream fis = openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;

            line=br.readLine();
            if(line==null)
            {
                line="0";

            }
            depense.setText(line);
            br.close();
        }
        catch(java.io.IOException e)
        {
            e.getMessage();
        }

        //Chargement de la liste
        try
        {
            FileInputStream fisList = openFileInput(fileNameList);
            BufferedReader brList = new BufferedReader(new InputStreamReader(fisList));
            String lineList;

            lineList=brList.readLine();
            while (lineList != null)
            {
                listDepense.add(lineList);
                lineList=brList.readLine();
            }
            mListView.setAdapter(adapter);
        }
        catch(java.io.IOException e)
        {
            e.getMessage();
        }
    }





    private void setText(TextView montantTotal, String montantTotalI, int mode)  {

        montantTotal.setText(montantTotalI);


        try
        {
            FileOutputStream fos = openFileOutput(fileName,mode);
            PrintWriter pw = new PrintWriter( new OutputStreamWriter(fos));

            pw.print(montantTotalI);
            pw.close();

        }

        catch(java.io.IOException  e)
        {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
            //ENREGISTREMENT DE LA LISTE DANS LE FICHIER
    private void setList(ArrayList<String> listDepense, int mode){
        try{
            FileOutputStream fosList = openFileOutput(fileNameList, mode);
            PrintWriter pww = new PrintWriter(new OutputStreamWriter(fosList));

            String line;
            int t = listDepense.size();
            int i = 0;

            while (i < t )
            {
                line = listDepense.get(i).trim();
                pww.print(line+'\n');
                i++;
            }
            pww.close();


        }
        catch(IOException e)
        {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
