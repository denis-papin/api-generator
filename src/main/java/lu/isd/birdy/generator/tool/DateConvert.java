package lu.isd.birdy.generator.tool;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateConvert {


    /**
     Used for mysql, seems to be ok.
     Example, ts = 1970-03-03 01:00:00 +01:00
     Converted into odt = 1970-03-03 00:00:00 Z
     */
    public static OffsetDateTime timestampAsUTCOffset(Timestamp ts ) {
        if ( ts == null ) {
            return null;
        }
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneId.of("UTC"));
    }

    /**
        Example, the  odt = 1970-03-03 00:00:00 Z
        Converted into ts = 1970-03-03 01:00:00 +01:00
    */
    public static Timestamp  offsetUTCAsTimestamp( OffsetDateTime odt ) {
        if ( odt == null ) {
            return null;
        }

        long epoch = odt.toEpochSecond();
        Timestamp ts = new Timestamp(epoch * 1000);
        return ts;
    }


    public static LocalDate dateAsUTCLocal(Date dt) {
        if ( dt == null ) {
            return null;
        }
        return LocalDate.ofInstant(Instant.ofEpochMilli(dt.getTime()), ZoneId.of("UTC"));
    }


    public static Date localUTCAsDate(LocalDate ldt) {
        if ( ldt == null ) {
            return null;
        }

        long epoch_s = ldt.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC);

        Date dt = new Date(epoch_s * 1000);
        return dt;
    }


}
