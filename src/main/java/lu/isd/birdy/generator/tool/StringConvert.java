package lu.isd.birdy.generator.tool;

public class StringConvert {

    public static String ifNull( String str, String defaultStr ) {
        if ( str == null ) {
            return defaultStr;
        }
        return str;
    }

    public static String from( String str, String defaultStr, int maxSize) {
        if ( str == null ) {
            return defaultStr;
        }

        if ( str.length() > maxSize ) {
            return str.substring(0, maxSize);
        } else {
            return str;
        }
    }

}
