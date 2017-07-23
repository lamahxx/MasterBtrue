package akumapps.android.masterb.Resources;


import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;

public class FormatDate {

    public String date;

    public FormatDate()
    {

        SimpleDateFormat sdf = new SimpleDateFormat("dd;MM;yyyy");

        date=sdf.format(new Date());


        // *** note that it's "yyyy-MM-dd hh:mm:ss" not "yyyy-mm-dd hh:mm:ss"
        //SimpleDateFormat dt = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        // date = dt.parse(d);

    }
}