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


    /**
     * 1- One table , no pivot =>
     *          1 model , 1 dto , 1 mapper
     *
     * Ex : Seasons : SeasonTable, SeasonDto, SeasonTableReversedMapper
     *
     *          SeasonTable mapper( SeasonDto )
     *
     * 2- One table, 1 pivot/group =>
     *          1 model, 1 dto composite, 1 mapper
     *
     * Option :  OptionTable, OptionDto, OptionTableReversedMapper
     *
     *   protected static OptionsTable optionsTableMap (  OptionDto  dtoModel, OptionValueDto valueDto ) {

             var tableModel = new OptionsTable ();
             tableModel.setGrpId (  dtoModel.getGrpId() );
             tableModel.setUsrId (  dtoModel.getUsrId() );
             tableModel.setOptId (  dtoModel.getOptId() );

             //--- Extended Reversed Mapper
             tableModel.setKey(valueDto.getKey());
             tableModel.setPage(valueDto.getPage());

             return tableModel;
     * }
     *
     * 3- Two (main) tables (1-1) , no pivot =>
     *          2 models (one with FK), 1 linear dto, 2 mappers
     *
     * If there are 2 main tables, we must fill an update section ???
     *
     *          NO CASE FOR NOW. SHOULD BE HANDLED BY CASE 4.
     *
     * 4- Two tables (1-N), 1 pivot =>
     *          2 models (one with FK), 1 dto composite, 2 mappers.
     *
     * Ex : contact -> resa
     *
     *          ContactTable mapper(ResaContactDto)
     *
     *          ReservationTable mapper(ReservationDto)
     *
     *
     * The goal is to get perfect ModelTable and DTOs and partial reversed mappers.
     *
     * @param def
     * @param path
     * @param packageName
     * @param className
     * @param tableModelMap
     * @param dtoModelMap
     */
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
            sw.p( "import", conf.tools.packageName , ".DateConvert", ";");
            sw.p( "import", conf.tools.packageName , ".StringConvert", ";");

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

        sw.p("public static", dataModelName, mapperFunctionName, "( ", dtoModelName," dtoModel )",  "{");

        sw.p();

        sw.inctab(1);

        sw.p("var tableModel = new", dataModelName, "()", ";");

        boolean firstTime = true;
        // Write attributes
        for (var field : tableModel) {

            ModelInfo dtoField = findDtoAttribute(field, dtoModel);

            if (dtoField != null) { // if the dtoField is part of the upper dtoModel

                String getter = "dtoModel." + "get" + namingService.capitalize(dtoField.getIdentifier()) + "()";

                switch( dtoField.getType() ) {

                    case "OffsetDateTime" :
                        getter =  "DateConvert.offsetUTCAsTimestamp(" + getter + ")";
                        break;
                    case "LocalDate" :

                        getter =  "DateConvert.localUTCAsDate(" + getter + ")";
                        break;

                    case "String" :
                        getter =  "StringConvert.from(" + getter + ", \"N/A\", 60)"; // TODO get the max size from the db column definition
                        break;

                }

                sw.p("tableModel.set" + namingService.capitalize(field.getIdentifier()),
                        "( ", getter , ")", ";");

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
