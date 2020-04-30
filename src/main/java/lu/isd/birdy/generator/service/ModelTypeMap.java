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
        map.put("TEXT", "String");
        map.put("DATETIME", "Timestamp" );
        map.put("DATE", "Date" );
        map.put("BIT", "Boolean" );
        map.put("TIMESTAMP", "Timestamp" );

        // Postgres
        map.put("int8", "BigInteger");
        map.put("varchar", "String");
        map.put("datetime", "Timestamp" );
        map.put("date", "Date" );
        map.put("text", "String");

        return map;
    }
}
