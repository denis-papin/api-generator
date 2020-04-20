package lu.isd.birdy.generator.service;

import java.util.HashMap;
import java.util.Map;

public class ModelTypeMap {

    public static Map<String, String> modelTypeMap() {
        var map = new HashMap<String, String>();

        // Mysql
        map.put("BIGINT", "BigInteger");
        map.put("INT", "BigInteger");
        map.put("VARCHAR", "String");
        map.put("DATETIME", "Timestamp" );
        map.put("DATE", "Date" );

        // Postgres
        map.put("int8", "BigInteger");
        map.put("varchar", "String");
        map.put("datetime", "Timestamp" );
        map.put("date", "Date" );

        return map;
    }
}
