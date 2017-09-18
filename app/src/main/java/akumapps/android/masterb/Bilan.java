package akumapps.android.masterb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import akumapps.android.masterb.Resources.Depense;
import akumapps.android.masterb.Resources.FormatDate;
import akumapps.android.masterb.Resources.OnSwipeTouchListener;

public class Bilan extends AppCompatActivity {


    private Spinner  dropdown11;
    private ArrayAdapter<Depense> adapterDepense ;
    String spinnerFill;

    String fileNameList;
    String fileNameMontant_Courant;
    String fileNamebudgetMax;
    SurfaceView swipeView;
    TextView textView_mois;
    private ArrayList<Depense> listDepense ;
    private ArrayList<Depense> listCurrentCategorySelectedDepense ;
    private ListView mListView;
    ArrayList<String> finalItems = new ArrayList<>();
    private String currentCategory;

    Integer selectedYear;
    Integer selectedMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilan);


        initScreen();


    }

    private void initScreen(){


        try {

            FormatDate currentDate = new FormatDate();
            textView_mois = (TextView) findViewById(R.id.tVmois);
            selectedYear = Integer.parseInt(currentDate.year);
            selectedMonth =  Integer.parseInt(currentDate.month);

            textView_mois.setText(selectedMonth +"/" + selectedYear);

            mListView = (ListView) findViewById(R.id.listBilan);
            listDepense = new ArrayList<>();
            fileNameList = getString(R.string.fileNameList);
            listCurrentCategorySelectedDepense = new ArrayList<>();
            adapterDepense = new ArrayAdapter<Depense>(getBaseContext(),
                    android.R.layout.simple_list_item_1,listCurrentCategorySelectedDepense);

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




            Spinner dropdown11 = (Spinner) findViewById(R.id.spinner2);
            //Appel dans  resources du fichier spinner_fr
            InputStream inputStream_spinner = getResources().openRawResource(
                    getResources().getIdentifier("spinner_fr",
                            "raw", getPackageName()));
            //Chargement de la list de base spinner_fr
            String[] items;
            String line;
            InputStreamReader inputreader = new InputStreamReader(inputStream_spinner);
            BufferedReader buffreader = new BufferedReader(inputreader);

            line = buffreader.readLine();
            items  = line.split(",");
            buffreader.close();
            //ajout des categories de base a la liste
            finalItems.add("ALL");
            for(int i=0; i<items.length; i++ )
            {
                finalItems.add(items[i]);
            }

            //recuperation du mon du fichier contenant les catégories ajoutees par l'utilisateur
            spinnerFill = getString(R.string.fileNameSpinner);

                //Ouverture en mode lecture du fichier des categories ajoutees par l'utilisateur
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


            //Ajout des catégorie au spinner
            final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, finalItems);
            dropdown11.setAdapter(adapter);


            dropdown11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    currentCategory=parent.getItemAtPosition(position).toString();
                    Float montantTotal=new Float(0);




                    onParamChange(currentCategory, montantTotal);



                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });





            textView_mois.setOnTouchListener(new OnSwipeTouchListener(this)
                                         {
                                             @Override
                                             public void onSwipeLeft() {
                                                 super.onSwipeLeft();

                                                 changeDate("LEFT", textView_mois.getText().toString());
                                                 textView_mois.setText(selectedMonth + "/" + selectedYear);
                                                 onParamChange(currentCategory, new Float(0));

                                             }

                                             @Override
                                             public void onSwipeRight() {
                                                 super.onSwipeRight();
                                                 changeDate("RIGHT",textView_mois.getText().toString());
                                                 textView_mois.setText(selectedMonth + "/" + selectedYear);
                                                 onParamChange(currentCategory,new Float(0));

                                             }
                                         }

            );




        }
        catch (java.io.IOException e){
            e.getMessage();
        }



    }



    public void changeDate(String motion, String currentMonth) {

        if (motion.equals("RIGHT")) {
            selectedMonth --;
            if(selectedMonth== 0){
                selectedMonth = 12;
                selectedYear --;
            }
        }


        if (motion.equals("LEFT")) {
            selectedMonth ++;
            if(selectedMonth == 13){
                selectedMonth = 1;
                selectedYear ++;
            }
        }

    }

    public void onParamChange(String currentCategory, Float montantTotal){
        adapterDepense.clear();
        if(currentCategory.equals("ALL"))
        {
            for (int i = 0; i < listDepense.size(); i++) {

                if(listDepense.get(i).getYear()==selectedYear&&listDepense.get(i).getMonth()==selectedMonth)
                {
                    listCurrentCategorySelectedDepense.add(listDepense.get(i));
                    montantTotal=montantTotal+listDepense.get(i).getMontantDepense();
                }

            }
        }else {
            for (int i = 0; i < listDepense.size(); i++) {
                if (listDepense.get(i).getLibelleDepense().equals(currentCategory)) {
                    if(listDepense.get(i).getYear()==selectedYear&&listDepense.get(i).getMonth()==selectedMonth) {
                        listCurrentCategorySelectedDepense.add(listDepense.get(i));
                        montantTotal = montantTotal + listDepense.get(i).getMontantDepense();
                    }
                }
            }
        }
        TextView textViewMontantTotal= (TextView) findViewById(R.id.totalBilan);
        textViewMontantTotal.setText(montantTotal.toString());
        mListView.setAdapter(adapterDepense);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_master, menu);
        return true;
    }

}