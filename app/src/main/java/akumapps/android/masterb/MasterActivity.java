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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.w3c.dom.Text;

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
    ArrayList<Depense> listDepense ;
    private Spinner dropdown;
    ArrayList<String> finalItems = new ArrayList<>();
    TextView depense;
    TextView thresh1txt;
    TextView thresh2txt;
    TextView thresh3txt;
    View touchView;

    Float budgetMax = 0f;
    Float montantTotCourant = 0f;

    Float thresholdAmount1 = 0f;
    Float thresholdAmount2 = 0f;
    Float thresholdAmount3 = 0f;

    private String thresh1;
    private String thresh2;
    private String thresh3;

    boolean fragment_View = false;


    //fichiers
    String spinnerFill;
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


        //final Button buttonAdd= (Button) findViewById(R.id.buttonAdd);
        //buttonAdd.setOnClickListener(OnClickAdd());

      //  touchView.setOnClickListener(OnClickAdd());







    }



    //****************BOUTONS**************************//
    //************************************************//
    //***********************************************//



/*                             BOUTON RESET                */

   public void OnClickReset()
    {
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

                //Amounts Reinit
                thresholdAmount1 = 0f;
                thresholdAmount2 = 0f;
                thresholdAmount3 = 0f;
                threshold_text_display();
    }

    /*                  buttonRemoveItem                    */
    public View.OnClickListener onClick_remove_item(){
        View.OnClickListener v = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listDepense.size() >= 1) {
                    Depense d = listDepense.get(listDepense.size() -1);
                    Float montant;
                    String label;
                    label = d.getLibelleDepense().trim();
                    montant = d.getMontantDepense();
                    if(label.equals(thresh1) || label.equals(thresh2) || label.equals(thresh3)){
                        if(label.equals(thresh1)){
                            thresholdAmount1 = thresholdAmount1 - montant;
                            String number = thresholdAmount1.toString();
                            thresh1txt.setText(thresh1+" --- "+number);
                        }
                        if(label.equals(thresh2)){
                            thresholdAmount2 = thresholdAmount2 - montant;
                            String number = thresholdAmount2.toString();
                            thresh2txt.setText(thresh2+" --- "+number);
                        }
                        if(label.equals(thresh3)){
                            thresholdAmount3 = thresholdAmount3 - montant;
                            String number = thresholdAmount3.toString();
                            thresh3txt.setText(thresh3+" --- "+number);
                        }
                    }
                    Float montantCourant =  Float.parseFloat(depense.getText().toString());
                    montantCourant = montantCourant - montant;
                    setText(depense,montantCourant.toString(),MODE_PRIVATE);
                    listDepense.remove(listDepense.size() - 1);
                    setList(listDepense, MODE_PRIVATE);
                    mListView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getApplicationContext(),"There is nothing to remove",Toast.LENGTH_SHORT).show();
                }
            }
        };
        return v;
    }







                    //********************FONCTIONS********************//
                    //************************************************//
                    //***********************************************//


    public void addToList(Float montantI, String libelle,String date) {

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
        thresh1txt = (TextView) findViewById(R.id.thresh1);
        thresh2txt = (TextView) findViewById(R.id.thresh2);
        thresh3txt = (TextView) findViewById(R.id.thresh3);
        mListView = (ListView) findViewById(R.id.list);
        depense = (TextView) findViewById(R.id.montantTotal);
        listDepense = new ArrayList<>();
        touchView =  findViewById(R.id.touchView);
        adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1,listDepense);
        //dropdown = (Spinner)findViewById(R.id.spinner1);

        String items[] = new String[]{};
        fileNameList = getString(R.string.fileNameList);
        fileNameMontant_Courant = getString(R.string.fileNameMontant_Courant);

        //Chargement des Thresholds
        get_Threshold_Names();

        //Context Menu de la ListView

        registerForContextMenu(mListView);




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




        //Chargement tableau finalItems à partir du tableau obtenu par la lecture du fichier
        // resource spinner_fr

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
       // ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
         //       android.R.layout.simple_spinner_dropdown_item, finalItems);
        // dropdown.setAdapter(adapterSpinner);





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

                FormatDate currentDate = new FormatDate();

               if(Integer.parseInt(currentDate.month)==d.getMonth()&&Integer.parseInt(currentDate.year)==d.getYear() ){
                    listDepense.add(d);
                }
                lineList=brList.readLine();
            }
            brList.close();
            mListView.setAdapter(adapter);
        }
        catch(java.io.IOException e)
        {
            e.getMessage();
        }

        //Chargement montant Threshold
        set_Threshold_Amounts();

        //Affichage des Threshold en Cours
        threshold_text_display();
    }






    public void setText(TextView montantTotal, String montantTotalI, int mode)  {

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

    public void setList(ArrayList<Depense> listDepense, int mode){
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

    public void setProgress(Float budget, Float montant){

        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        /*
        if(budget == 0 || montant == 0){

            progress.setVisibility(View.INVISIBLE);
        }
        else {
        */
            progress.setVisibility(View.VISIBLE);
            Integer progValue = Math.round((montant * 100) / budget);
            if (progValue <= 100){
                progress.setProgress(progValue);
            }
            else{
                progress.setProgress(100);
            }



    }


    //BON CODE DE MERDE MAIS CA MARCHE

    private void set_Threshold_Amounts(){

        int i = 0;
        String label;
        String tmp [];
        while(i <listDepense.size()){
            tmp = listDepense.get(i).toString().split(" ");
            label = tmp[1];
            if(label.equals(thresh1) || label.equals(thresh2) || label.equals(thresh3)) {
                if (label.equals(thresh1)) {
                    thresholdAmount1 = thresholdAmount1 + Float.parseFloat(tmp[0]);

                }
                if (label.equals(thresh2)) {
                    thresholdAmount2 = thresholdAmount2 + Float.parseFloat(tmp[0]);

                }
                if (label.equals(thresh3)) {
                    thresholdAmount3 = thresholdAmount3 + Float.parseFloat(tmp[0]);

                }
                i++;
            }
            else{
                i++;
            }
        }

    }

    private void get_Threshold_Names(){

        String fileNameThresh = getString(R.string.fileNameThreshold);

        try{
            FileInputStream fis = openFileInput(fileNameThresh);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            ArrayList<String> tmp = new ArrayList<>();
            String line;
            line = br.readLine();
            while (line != null){
                tmp.add(line);
                line =br.readLine();
            }
            br.close();

            int i = tmp.size();

            if(i == 0){
                thresh1 = null;
                thresh2 = null;
                thresh3 = null;
            }
            if (i == 1){
                thresh1 = tmp.get(0);
                thresh2 = null;
                thresh3 = null;
            }
            if (i == 2){
                thresh1 = tmp.get(0);
                thresh2 = tmp.get(1);
                thresh3 = null;
            }
            if( i == 3){
                thresh1 = tmp.get(0);
                thresh2 = tmp.get(1);
                thresh3 = tmp.get(2);
            }

        }
        catch(java.io.IOException e){
            e.getMessage();
        }

    }



    public void check_Threshold(String threshLabel){
        Float tmp;

        if (budgetMax != 0) {

            if (threshLabel.equals(thresh1)) {
                tmp = (thresholdAmount1 * 100) / budgetMax;

                if (tmp > 30 && tmp <=49) {
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.cust_toast_layout,
                            (ViewGroup) findViewById(R.id.relativeLayout1));

                    Toast toast = new Toast(this);
                    toast.setView(view);
                    toast.show();
                }
                if(tmp > 50 && tmp <= 99){
                    Toast.makeText(getApplicationContext(), "BABYLONE2", Toast.LENGTH_SHORT).show();
                }
                if(tmp >= 100){
                    Toast.makeText(getApplicationContext(),"BABYLONNNNE",Toast.LENGTH_SHORT).show();
                }
            }
            if (threshLabel.equals(thresh2)) {
                tmp = (thresholdAmount2 * 100) / budgetMax;
                if (tmp >= 30 && tmp <= 49) {
                    Toast.makeText(getApplicationContext(), "NOO", Toast.LENGTH_SHORT).show();
                }
                if(tmp >= 50 && tmp <= 99){
                    Toast.makeText(getApplicationContext(), "NOO2", Toast.LENGTH_SHORT).show();
                }
                if(tmp >= 100){
                    Toast.makeText(getApplicationContext(),"NOO3",Toast.LENGTH_SHORT).show();
                }
            }
            if (threshLabel.equals(thresh3)) {
                tmp = (thresholdAmount3 * 100) / budgetMax;
                if (tmp >= 30 && tmp <=49) {
                    Toast.makeText(getApplicationContext(), "YES", Toast.LENGTH_SHORT).show();
                }
                if(tmp >= 50 && tmp <=99){
                    Toast.makeText(getApplicationContext(), "YES2", Toast.LENGTH_SHORT).show();
                }
                if(tmp >= 100){
                    Toast.makeText(getApplicationContext(),"YES3",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void add_ThresholdAmount_button(String label, Float montantI){
        if(label.equals(thresh1)){
            thresholdAmount1 = thresholdAmount1 + montantI;
        }
        if(label.equals(thresh2)){
            thresholdAmount2 = thresholdAmount2 + montantI;
        }
        if(label.equals(thresh3)){
            thresholdAmount3 = thresholdAmount3 + montantI;
        }
    }

    public void threshold_text_display(){

        if(thresh1!=null){
            thresh1txt.setText(thresh1+" --- "+thresholdAmount1);
        }
        if(thresh2!=null){
            thresh2txt.setText(thresh2+" --- "+thresholdAmount2);
        }
        if(thresh3!=null){
            thresh3txt.setText(thresh3+" --- "+thresholdAmount3);
        }

    }






    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Delete");//groupId, itemId, order, title

    }


    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Delete"){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int listPosition = info.position;


            if(listDepense.size() >= 1) {
                Depense d = listDepense.get(listPosition);
                Float montant;
                String label;
                label = d.getLibelleDepense().trim();
                montant = d.getMontantDepense();
                if (label.equals(thresh1) || label.equals(thresh2) || label.equals(thresh3)) {
                    if (label.equals(thresh1)) {
                        thresholdAmount1 = thresholdAmount1 - montant;
                        String number = thresholdAmount1.toString();
                        thresh1txt.setText(thresh1 + " --- " + number);
                    }
                    if (label.equals(thresh2)) {
                        thresholdAmount2 = thresholdAmount2 - montant;
                        String number = thresholdAmount2.toString();
                        thresh2txt.setText(thresh2 + " --- " + number);
                    }
                    if (label.equals(thresh3)) {
                        thresholdAmount3 = thresholdAmount3 - montant;
                        String number = thresholdAmount3.toString();
                        thresh3txt.setText(thresh3 + " --- " + number);
                    }
                }
                Float montantCourant = Float.parseFloat(depense.getText().toString());
                montantCourant = montantCourant - montant;
                setText(depense, montantCourant.toString(), MODE_PRIVATE);
                listDepense.remove(listPosition);
                setList(listDepense, MODE_PRIVATE);
                adapter.notifyDataSetChanged();
            }
        }

        else{
            return false;
        }
        return true;
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

        if(id == R.id.reset){
            OnClickReset();
        }


        return super.onOptionsItemSelected(item);
    }


    public void showFragment(View view) {


        if (!fragment_View) {
            FragmentManager ff = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = ff.beginTransaction();
            ajout_depense f1 = new ajout_depense();
            fragmentTransaction.add(R.id.fragment_container, f1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            fragment_View = true;
        }

    }
}
