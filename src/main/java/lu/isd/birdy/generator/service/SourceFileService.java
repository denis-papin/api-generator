package lu.isd.birdy.generator.service;

import lu.isd.birdy.generator.SourceWriter;
import lu.isd.birdy.generator.config.Config;
import lu.isd.birdy.generator.model.ModelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class SourceFileService {

    static final Map<String, String> PACKAGE_TYPE_MAP = buildPackageType();

    @Autowired
    private ConfigService configService;

    @Autowired
    private NamingService namingService;

    protected static Map<String, String> buildPackageType() {
        var map = new HashMap<String, String>();
        map.put("BigInteger", "java.math.BigInteger");
        map.put("BigDecimal", "java.math.BigDecimal");
        map.put("Timestamp", "java.sql.Timestamp");
        map.put("OffsetDateTime", "java.time.OffsetDateTime");
        map.put("LocalDate", "java.time.LocalDate");
        map.put("Date", "java.sql.Date");
        map.put("JsonProperty", "com.fasterxml.jackson.annotation.JsonProperty" );
        map.put("JsonSerialize", "com.fasterxml.jackson.databind.annotation.JsonSerialize" );
        return map;
    }

    public void generate(String path, String packageName, String className, List<ModelInfo> fields) {

        Config conf = configService.getConfig();

        SourceWriter sw = null;
        try {

            String packagePath = namingService.packageToPath(packageName);
            Path p = Paths.get(path, packagePath,  className + ".java" );
            sw = new SourceWriter(p.toFile());

            Set<String> typeList = new HashSet<>();
            for ( var f : fields ) {
                if ( !f.getType().equals("String")) {
                    typeList.add(f.getType());
                }

                if ( f.getJsonName() != null && !f.getJsonName().isEmpty()) {
                    typeList.add("JsonProperty");
                }

                if ( conf.serializer != null ) {
                    typeList.add("JsonSerialize");
                }
            }

            // Package name
            sw.p( "package", packageName, ";" );

            sw.p();

           // Imports

            sw.p("import", "java.io.Serializable", ";" );
            sw.p("import java.util.ArrayList;");
            sw.p("import java.util.List;");

            sw.p("import ", conf.tools.packageName + ".*", ";");

            for ( var t : typeList) {

                var pack = PACKAGE_TYPE_MAP.get(t);
                if ( pack == null ) {
                    //pack = t;
                    continue;
                }

                sw.p( "import", pack, ";");
            }


            sw.p();

            // Class name
            sw.p("public class", className,  "implements Serializable", "{");

            sw.p();
            sw.inctab(1);

            // Write attributes
            for (var f : fields) {
                sw.p(f.getScope(), f.getType(), f.getIdentifier(), ";");
            }

            sw.p();

            // Write Getters and Setters

            for (var f : fields) {

                if (!f.getJsonName().isEmpty()) {
                    sw.p("@JsonProperty(", "\"" + f.getJsonName() + "\"", ")");
                }

                if ( f.getSerializerName() != null && !f.getSerializerName().isEmpty()) {
                    sw.p("@JsonSerialize(", "using = " ,  f.getSerializerName() + ".class", ")" );
                }

                sw.p("public", f.getType(), "get" + namingService.capitalize(f.getIdentifier()) + "()", "{");

                sw.inctab(1);

                sw.p("return", "this." + f.getIdentifier(), ";");

                sw.inctab(-1);

                sw.p("}");

                sw.p();

                sw.p("public", "void", "set" + namingService.capitalize(f.getIdentifier()) + "(", f.getType(), f.getIdentifier(), ")", "{");
                sw.inctab(1);
                sw.p("this." + f.getIdentifier(), "=", f.getIdentifier(), ";");
                sw.inctab(-1);
                sw.p("}");

                sw.p();

            }

            sw.inctab(-1);

            // Class closing
            sw.p("}");

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            sw.close();
        }

    }


}
