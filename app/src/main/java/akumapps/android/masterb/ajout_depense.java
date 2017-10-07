package akumapps.android.masterb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import akumapps.android.masterb.Resources.Depense;
import akumapps.android.masterb.Resources.FormatDate;

import static android.content.Context.MODE_PRIVATE;


public class ajout_depense extends Fragment implements View.OnClickListener{
    public ajout_depense(){}

    private Button submit;
    private Spinner dropdownFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajout_depense, container, false);


       MasterActivity mainActivity = (MasterActivity) getActivity();

        dropdownFragment = (Spinner) view.findViewById(R.id.fragmentSpinner);
        //Chargement du Spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, mainActivity.finalItems);
        dropdownFragment.setAdapter(adapterSpinner);


        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(this);




        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                saveDepense();
                break;
            default:
                break;
        }

    }

    @Override
    public void onStop(){
        super.onStop();

        ((MasterActivity)getActivity()).fragment_View = false;
    }




    private void saveDepense() {


        MasterActivity mainActivity = (MasterActivity) getActivity();

        EditText montant= (EditText) getActivity().findViewById(R.id.montantFragment);
        TextView montantTotal = (TextView) getActivity().findViewById(R.id.montantTotal);


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
            Toast toast = Toast.makeText(getActivity(),
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

        mainActivity.setText(montantTotal,montantTotalI.toString(), MODE_PRIVATE);

        mainActivity.setProgress(1000f, montantTotalI);

        if(montantIString.isEmpty() || montantIString.equals("0"))
        {

            Toast toast = Toast.makeText(getActivity(),
                    getString(R.string.emptyEntry), Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            if (test) {
                montant.setText(null);
            }
            else{
                FormatDate fdate = new FormatDate();
                mainActivity.addToList(montantI, dropdownFragment.getSelectedItem().toString(), fdate.date);
                mainActivity.setList(mainActivity.listDepense, MODE_PRIVATE);
                montant.setText(null);
                mainActivity.add_ThresholdAmount_button(dropdownFragment.getSelectedItem().toString(), montantI);
                mainActivity.check_Threshold(dropdownFragment.getSelectedItem().toString());
                mainActivity.threshold_text_display();
            }
        }


    }



}
