package lu.isd.birdy.generator.service;

import lu.isd.birdy.generator.SourceWriter;
import lu.isd.birdy.generator.config.Config;
import lu.isd.birdy.generator.config.Definition;
import lu.isd.birdy.generator.model.ModelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class ReversedMappingBuilderService {

    static final Map<String, String> PACKAGE_TYPE_MAP = buildPackageType();

    @Autowired
    private ConfigService configService;

    @Autowired
    private NamingService namingService;

    protected static Map<String, String> buildPackageType() {
        var map = new HashMap<String, String>();
        map.put("BigInteger", "java.math.BigInteger");
        map.put("Timestamp", "java.sql.Timestamp");
        map.put("Date", "java.sql.Date");
        map.put("JsonProperty", "com.fasterxml.jackson.annotation.JsonProperty" );
        return map;
    }


    public void generate(Definition def, String path, String packageName, String className, Map<String, List<ModelInfo>> tableModelMap, Map<String, List<ModelInfo>> dtoModelMap) {

        Config conf = configService.getConfig();

        SourceWriter sw = null;

        try {

            String packagePath = namingService.packageToPath(packageName);

            Path p = Paths.get(path, packagePath, className + ".java" );
            sw = new SourceWriter(p.toFile());

            Set<String> typeList = new HashSet<>();
            tableModelMap.forEach( (k,v) -> {
                        for (var f : v) {
                            if (!f.getType().equals("String")) {
                                typeList.add(f.getType());
                            }

                            if (f.getJsonName() != null && !f.getJsonName().isEmpty()) {
                                typeList.add("JsonProperty");
                            }
                        }
                    }
            );

            // Package name
            sw.p( "package", packageName, ";" );

            sw.p();

            // Imports

            for ( var t : typeList) {

                var pack = PACKAGE_TYPE_MAP.get(t);
                if ( pack == null ) {
                    //pack = t;
                    continue;
                }

                sw.p( "import", pack, ";");
            }


            sw.p("import java.util.ArrayList;");
            sw.p("import java.util.List;");

            sw.p();

            sw.p( "import", def.model.packageName + ".*", ";" );

            for ( Map.Entry<String, List<ModelInfo>> e : dtoModelMap.entrySet()) {
                sw.p("import", def.dto.packageName + "." + e.getKey(), ";" );
            }

            sw.p();

            // Class name
            sw.p("public class", className, "{");

            sw.p();
            sw.inctab(1);

            // Create the reversed mappers for all the table models

            for ( Map.Entry<String, List<ModelInfo>> e : tableModelMap.entrySet()) {
                // find out the ha hoc dtoModel
                String dtoModelName = findOutRightDtoModel(e.getValue(), dtoModelMap );
                List<ModelInfo> rightDtoModel = dtoModelMap.get(dtoModelName);
                createMapper(sw, def,  e.getKey(), e.getValue(), dtoModelName, rightDtoModel);
            }


            sw.p();


            sw.p();

            sw.inctab(-1);

            // Class closing
            sw.p("}");

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            sw.close();
        }

    }



    protected void createMapper(  SourceWriter sw, Definition def, String dataModelName, List<ModelInfo> tableModel,
                                  String dtoModelName, List<ModelInfo> dtoModel) throws IOException {

        Config conf = configService.getConfig();

        // Change CrmTripDto into CrmTrip
        String mapperFunctionName = namingService.uncapitalize( namingService.dtoToModel(dataModelName) ) + "Map";

        sw.p("protected static", dataModelName, mapperFunctionName, "( ", dtoModelName," dtoModel )",  "{");

        sw.p();

        sw.inctab(1);

        sw.p("var tableModel = new", dataModelName, "()", ";");

        boolean firstTime = true;
        // Write attributes
        for (var field : tableModel) {

            ModelInfo dtoField = findDtoAttribute(field, dtoModel);

            if (dtoField != null) { // if the dtoField is part of the upper dtoModel

                sw.p("tableModel.set" + namingService.capitalize(field.getIdentifier()),
                        "( ", "dtoModel.", "get" + namingService.capitalize(dtoField.getIdentifier())
                                , "()", ")", ";");

            }
        }

        sw.p("return tableModel;" );

        sw.inctab(-1);

        sw.p("}");

        sw.p();

    }

    protected String findOutRightDtoModel(List<ModelInfo> tableModel , Map<String, List<ModelInfo>> dtoModelMap ) {
        String uuid = tableModel.get(0).getFieldUuid();

        for ( Map.Entry<String, List<ModelInfo>> e : dtoModelMap.entrySet()) {
            ModelInfo found = findDtoAttribute(tableModel.get(0), e.getValue());
            if (found != null) {
                return e.getKey();
            }
        }

        return null;
    }

    protected ModelInfo findDtoAttribute(ModelInfo field, List<ModelInfo> dtoModel)  {

        if ( field.getFieldUuid() == null || field.getFieldUuid().isEmpty()) {
            return null;
        }


        ModelInfo dtoField = null;

        for (var dtoF : dtoModel ) {
            if ( dtoF.getFieldUuid().equals(field.getFieldUuid())) {
                dtoField = dtoF;
                break;
            }
        }

        return dtoField;
    }

}
