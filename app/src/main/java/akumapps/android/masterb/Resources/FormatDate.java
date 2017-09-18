package akumapps.android.masterb.Resources;


import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;

public class FormatDate {

    public String date;
    public String month;
    public String year;
    public String day;


    public FormatDate()
    {

        SimpleDateFormat sdf = new SimpleDateFormat("dd;MM;yyyy");

        date=sdf.format(new Date());

        String tabDate[]= date.split(";");

        day=tabDate[0];
        month=tabDate[1];
        year=tabDate[2];


    }


}