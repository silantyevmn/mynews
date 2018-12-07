package silantyevmn.ru.mynews.utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by silan on 01.12.2018.
 */

public class DateManager {
    public static String getTimePublished(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date tempDate;
        try {
            tempDate = format.parse(dateString);
            return new PrettyTime().format(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
