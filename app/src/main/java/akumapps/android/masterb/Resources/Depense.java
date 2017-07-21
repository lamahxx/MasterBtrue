package akumapps.android.masterb.Resources;

import java.util.Date;

/**
 * Created by prosk on 19/07/2017.
 */


public class Depense {



    private Float montantDepense;
    private String libelleDepense;
    private Date dateDepense ;



    public Depense() {

    }



    public Depense(Float mD, String ld, Date dp) {
            montantDepense=mD;
            libelleDepense=ld;
            dateDepense=dp;
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

    public Date getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(Date dateDepense) {
        this.dateDepense = dateDepense;
    }

    public String toString()
    {
        return montantDepense.toString()+" "+libelleDepense+" "+dateDepense;
    }
}
