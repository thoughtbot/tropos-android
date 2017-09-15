package com.thoughtbot.tropos.testUtils

import com.thoughtbot.tropos.data.Condition
import java.util.*



/**
 * Created by mike on 9/15/17.
 */
class DateHelperUtil {
    companion object {

        //calculates yesterdays date
        fun getYesterdayDate(condition: Condition): Condition {
            val calendar = Calendar.getInstance();
            calendar.setTime(condition.date);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            return condition.copy(date =  calendar.getTime())
        }

        //convert time to PST timezone.
        fun getTimeInPST(condition: Condition) : Condition {
            val calendar = Calendar.getInstance();
            //set timezone as per PST
            calendar.timeZone = TimeZone.getTimeZone("America/Los_Angeles");
            calendar.setTime(condition.date);
            return condition.copy(date =  calendar.getTime())
        }
    }


}