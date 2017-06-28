package akumapps.android.masterb;

import android.content.ClipData;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;


public class MasterActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView depense;
    private ArrayAdapter<String> adapter ;
    private ArrayList<String> listDepense ;
    private String mescouiilles;


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


        initScreen();



        final Button buttonAdd= (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(OnClickAdd());


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
                    Toast toast = Toast.makeText(getApplicationContext(), "T'es pas si riche !", Toast.LENGTH_SHORT);
                    toast.show();
                }

                String montantTotalString=montantTotal.getText().toString();

                if(montantTotalString.isEmpty())
                {
                    montantTotalString="0.0";

                }

                Float montantTotalI= Float.parseFloat(montantTotalString);

                montantTotalI+=montantI;

                setText(montantTotal,montantTotalI.toString(), MODE_PRIVATE);
                addToList(montantIString, nomDepense.getText().toString());
                //setList(listDepense, MODE_PRIVATE);
                montant.setText(null);
                nomDepense.setText(null);
            }


        };
        return on;
    }




    public View.OnClickListener OnClickReset()
    {
       View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    FileOutputStream fos = openFileOutput(fileName,MODE_PRIVATE);
                    PrintWriter pw = new PrintWriter( new OutputStreamWriter(fos));
                    pw.close();
                    TextView montantTotal= (TextView) findViewById(R.id.montantTotal);
                    montantTotal.setText("0");
                }
                catch(java.io.IOException e){}

            }
        };
        return on;

    }



    //********************FONCTIONS********************//
    //************************************************//
    //***********************************************//


    private void addToList(String montantI, String nomDepense ) {
        listDepense.add(montantI+ " "+ nomDepense);
        mListView.setAdapter(adapter);
    }

    private void initScreen() {

        //Init des variables
        mListView = (ListView) findViewById(R.id.list);
        depense = (TextView) findViewById(R.id.montantTotal);
        listDepense = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,listDepense);


        //chargement du montant total
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
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT);
        }

    }

    private void setList(ArrayList<String> listDepense, int mode){
        try{
            FileOutputStream fosList = openFileOutput(fileNameList, mode);
            PrintWriter pw= new PrintWriter(new OutputStreamWriter(fosList));

            while (listDepense!=null){
                pw.print(listDepense);
            }
            pw.close();


        }
        catch(java.io.IOException e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
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
