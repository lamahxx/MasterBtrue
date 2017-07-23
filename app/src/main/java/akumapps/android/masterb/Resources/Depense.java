package akumapps.android.masterb.Resources;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by prosk on 19/07/2017.
 */


public class Depense {



    private Float montantDepense;
    private String libelleDepense;
    private int day ;
    private int month;
    private int year;



    public Depense() {

    }



    public Depense(Float mD, String ld, int d, int m, int y) {
        montantDepense=mD;
        libelleDepense=ld;
        day=d;
        month=m;
        year=y;
    }

    public Float getMontantDepense() {
        return montantDepense;
    }

    public void setMontantDepense(Float montantDepense) {
        this.montantDepense = montantDepense;
    }

    public String getLibelleDepense() {
        return libelleDepense;
    }

    public void setLibelleDepense(String libelleDepense) {
        this.libelleDepense = libelleDepense;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public String toString()
    {

        return montantDepense.toString()+" "+libelleDepense+" "+day+"/"+ month +"/"+year;
    }
}
