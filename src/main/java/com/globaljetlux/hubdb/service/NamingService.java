package com.globaljetlux.hubdb.service;

import org.springframework.stereotype.Component;

@Component
public class NamingService {


    /**
     * Transform a snake case identifier to a camel case identifier.
     * @return
     */
    public String snakeToCamel( String text ) {
        String out = text;
        while(out.contains("_")) {
            out = out.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(out.charAt(out.indexOf("_") + 1))));
        }
        return out;
    }


    /**
     * Transform a camel case identifier to a dash separated identifier.
     * @return
     */
    public String dashCase( String text) {
        return text.replaceAll("([^_A-Z])([A-Z])", "$1-$2").toLowerCase();
    }

    /**
     *
     * @param str
     * @return
     */
    public String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    /**
     * Transform a camel case identifier to a dash separated identifier.
     * @return
     */
    public String packageToPath( String text ) {
        return text.replaceAll("\\.", "/");
    }


    public String  uncapitalize( String str ) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }


    public String dtoToModel( String dtoName ) {
        return dtoName.replace("Dto", "" );
    }

}
