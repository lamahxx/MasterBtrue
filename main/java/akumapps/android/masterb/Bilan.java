package akumapps.android.masterb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import akumapps.android.masterb.Resources.Depense;

public class Bilan extends AppCompatActivity {


    private Spinner  dropdown11;
    private ArrayAdapter<Depense> adapterDepense ;
    String spinnerFill;

    String fileNameList;
    String fileNameMontant_Courant;
    String fileNamebudgetMax;

    private ArrayList<Depense> listDepense ;
    private ArrayList<Depense> listCurrentCategorySelectedDepense ;
    private ListView mListView;
    ArrayList<String> finalItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilan);


        initScreen();


    }

    private void initScreen(){


        try {

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
                    String currentCategory=parent.getItemAtPosition(position).toString();
                    Float montantTotal=new Float(0);

                    adapterDepense.clear();


                    if(currentCategory.equals("ALL"))
                    {
                        for (int i = 0; i < listDepense.size(); i++) {

                                listCurrentCategorySelectedDepense.add(listDepense.get(i));
                                montantTotal=montantTotal+listDepense.get(i).getMontantDepense();
                        }
                    }else {
                        for (int i = 0; i < listDepense.size(); i++) {
                            if (listDepense.get(i).getLibelleDepense().equals(currentCategory)) {
                                listCurrentCategorySelectedDepense.add(listDepense.get(i));
                                montantTotal=montantTotal+listDepense.get(i).getMontantDepense();
                            }
                        }
                    }

                    TextView textViewMontantTotal= (TextView) findViewById(R.id.totalBilan);
                    textViewMontantTotal.setText(montantTotal.toString());
                    mListView.setAdapter(adapterDepense);


                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
        }
        catch (java.io.IOException e){
            e.getMessage();
        }
    }

}