package akumapps.android.masterb;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.text.TimeZoneNames;
import android.icu.util.Output;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import akumapps.android.masterb.Resources.Depense;
import akumapps.android.masterb.Resources.FormatDate;


public class MasterActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayAdapter<Depense> adapter ;
    private ArrayList<Depense> listDepense ;
    private Spinner dropdown;
    ArrayList<String> finalItems = new ArrayList<>();
    TextView depense;

    Float budgetMax = 0f;
    Float montantTotCourant = 0f;



    String spinnerFill; //fichier
    String fileNameList;
    String fileNameMontant_Courant;
    String fileNamebudgetMax;



    //*******************MAIN********************//
    //******************************************//
    //*****************************************//

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
       //initScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);


        initScreen();

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);


        final Button buttonAdd= (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(OnClickAdd());

        final Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(OnClickReset());




    }


    //****************BOUTONS**************************//
    //************************************************//
    //***********************************************//


    /*                      BOUTON ADD                  */

    public View.OnClickListener OnClickAdd() {
        View.OnClickListener x = new View.OnClickListener() {
            @Override
            public void onClick(View v){

                EditText montant= (EditText) findViewById(R.id.montant);
                TextView montantTotal = (TextView) findViewById(R.id.montantTotal);

                Boolean test = false;

                String montantIString=montant.getText().toString();

                if(montantIString.isEmpty())
                {
                    montantIString="0";
                }


                Float montantI= Float.parseFloat(montantIString);
                if( montantI > 9999999 | montantI< 0)
                {
                    montantI = 0f;
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.notEnough), Toast.LENGTH_SHORT);
                    toast.show();
                    test = true;
                }

                String montantTotalString=montantTotal.getText().toString();

                if(montantTotalString.isEmpty())
                {
                    montantTotalString="0";

                }

                Float montantTotalI= Float.parseFloat(montantTotalString);

                montantTotalI+=montantI;

                setText(montantTotal,montantTotalI.toString(), MODE_PRIVATE);
                setProgress(budgetMax, montantTotalI);
                if(montantIString.isEmpty() || montantIString.equals("0"))
                {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.emptyEntry), Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    if (test) {
                        montant.setText(null);
                    }
                    else{
                        FormatDate fdate = new FormatDate();
                        addToList(montantI, dropdown.getSelectedItem().toString(), fdate.date);
                        setList(listDepense, MODE_PRIVATE);
                        montant.setText(null);
                    }
                }

            }


        };
        return x;
    }



/*                             BOUTON RESET                */

    public View.OnClickListener OnClickReset()
    {
       View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ouverture et fermeture du fichier pour effacer le fichier : MONTANT TOTAL
                resetFile(fileNameMontant_Courant, MODE_PRIVATE);

                //On Affiche 0 dans le TEXTVIEW
                TextView montantTotal= (TextView) findViewById(R.id.montantTotal);
                montantTotal.setText("0");

               //Ouverture et fermeture du fichier pour effacer le fichier : LISTDEPENSE
                resetFile(fileNameList, MODE_PRIVATE);

                //On Affiche rien dans la LISTEVIEW
                adapter.clear();

                //ProgressBar reInit
                setProgress(budgetMax, 0f);

            }
        };
        return on;

    }







                    //********************FONCTIONS********************//
                    //************************************************//
                    //***********************************************//


    private void addToList(Float montantI, String libelle,String date) {

            String tabDate[] = parseDate(date);
            Depense a = new Depense(montantI, libelle,Integer.parseInt(tabDate[0]),
                    Integer.parseInt(tabDate[1]),
                    Integer.parseInt(tabDate[2]));
            listDepense.add(a);
            mListView.setAdapter(adapter);



    }

    private String[] parseDate(String date) {

         return date.split(";");
    }

    public void initScreen() {

        //Init des variables



        mListView = (ListView) findViewById(R.id.list);
        depense = (TextView) findViewById(R.id.montantTotal);
        listDepense = new ArrayList<>();
        adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1,listDepense);
        dropdown = (Spinner)findViewById(R.id.spinner1);
        String items[] = new String[]{};
        fileNameList = getString(R.string.fileNameList);
        fileNameMontant_Courant = getString(R.string.fileNameMontant_Courant);



        //Chargement du budgetMax à partir du fichier
        fileNamebudgetMax= getString(R.string.fileNameBudget_max);
        try{
            FileInputStream fis = openFileInput(fileNamebudgetMax);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            line = br.readLine();
            if (line!=null){
                budgetMax = Float.parseFloat(line);
            }
            else budgetMax = 0f;
        }
        catch(java.io.IOException e){
            e.getMessage();
        }




        //Appel dans  resources du fichier spinner_fr
        InputStream inputStream_spinner = getResources().openRawResource(
                getResources().getIdentifier("spinner_fr",
                        "raw", getPackageName()));




        //Chargement tableau à partir du fichier spinner_fr
        try {
            InputStreamReader inputreader = new InputStreamReader(inputStream_spinner);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            int i = 0;
            line = buffreader.readLine();
            while (line != null)
            {
                items  = line.split(",");
                i = i+i;
                line = buffreader.readLine();
            }
            buffreader.close();

        }
        catch (java.io.IOException e){
            e.getMessage();
        }




        //Chargement tableau finalItems à partir du tableau obtenu par la lecture du fichier resource spinner_fr

        int p = 0;
        while(p<items.length){
            finalItems.add(items[p]);
            p++;
        }


        //Lecture du fichier fileNameSpinner pour charger complètement finalItems.

        spinnerFill = getString(R.string.fileNameSpinner);

        try{
            FileInputStream fileInputSpinnerPref = openFileInput(spinnerFill);
            BufferedReader brSpinnerPref = new BufferedReader(new InputStreamReader(fileInputSpinnerPref));

            String lineSpinner;
            lineSpinner = brSpinnerPref.readLine();
            if(lineSpinner!=null){
                while(lineSpinner!=null) {
                    finalItems.add(lineSpinner);
                    lineSpinner = brSpinnerPref.readLine();
                }
            }
            brSpinnerPref.close();
        }
        catch(java.io.IOException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }



        //Chargement du Spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, finalItems);
        dropdown.setAdapter(adapterSpinner);





        //Chargement du montant total
        try
        {

            FileInputStream fis = openFileInput(fileNameMontant_Courant);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;

            line=br.readLine();
            if(line==null)
            {
                line="0";

            }
            depense.setText(line);
            montantTotCourant = Float.parseFloat(line);
            br.close();
        }
        catch(java.io.IOException e)
        {
            e.getMessage();
        }
        setProgress(budgetMax, montantTotCourant);



        //Chargement de la liste
        try
        {
            FileInputStream fisList = openFileInput(fileNameList);
            BufferedReader brList = new BufferedReader(new InputStreamReader(fisList));
            String lineList;

            lineList=brList.readLine();
            while (lineList != null)
            {

                String tabList[] = lineList.split(" ");

                Depense d = new Depense(Float.parseFloat(tabList[0]), tabList[1],
                        Integer.parseInt(tabList[2].split("/")[0]),
                        Integer.parseInt(tabList[2].split("/")[1]),
                        Integer.parseInt(tabList[2].split("/")[2]));
                listDepense.add(d);
                lineList=brList.readLine();
            }
            brList.close();
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
            FileOutputStream fos = openFileOutput(fileNameMontant_Courant,mode);
            PrintWriter pw = new PrintWriter( new OutputStreamWriter(fos));
            pw.print(montantTotalI);
            pw.close();

        }

        catch(java.io.IOException  e)
        {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
            //ENREGISTREMENT DE LA LISTE DANS LE FICHIER fileNameList

    private void setList(ArrayList<Depense> listDepense, int mode){
        try{
            FileOutputStream fosList = openFileOutput(fileNameList, mode);
            PrintWriter pww = new PrintWriter(new OutputStreamWriter(fosList));

            String line;
            int t = listDepense.size();
            int i = 0;

            while (i < t )
            {
                line = listDepense.get(i).toString().trim();
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



    private void resetFile(String nomFichier, int mode){
        try{
            FileOutputStream fos = openFileOutput(nomFichier, mode);
            PrintWriter pw = new PrintWriter( new OutputStreamWriter(fos));
            pw.close();

        }
        catch(java.io.IOException e){
            e.getMessage();
        }

    }

    private void setProgress(Float budget, Float montant){

        ProgressBar progress = (ProgressBar) findViewById(R.id.progressbar);
        if(budget == 0){
            progress.setProgress(0);
        }
        else {
            Integer progValue = Math.round((montant * 100) / budget);
            progress.setProgress(progValue);
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

        if (id == R.id.parametres)
        {

            Intent intentParametres = new Intent(MasterActivity.this, Parameters.class);
            startActivity(intentParametres);
            return true;
        }

        if(id == R.id.bilan)
        {
            Intent intentBilan = new Intent(MasterActivity.this, Bilan.class);
            startActivity(intentBilan);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
