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
public class MappingBuilderService {

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


    public void generate(Definition def, String path, String packageName, String className, List<ModelInfo> fields, Map<String, List<ModelInfo>> dtoModelMap) {

        Config conf = configService.getConfig();

        SourceWriter sw = null;

        try {

            String packagePath = namingService.packageToPath(packageName);

            Path p = Paths.get(path, packagePath, className + ".java" );
            sw = new SourceWriter(p.toFile());

            Set<String> typeList = new HashSet<>();
            for ( var f : fields ) {
                if ( !f.getType().equals("String")) {
                    typeList.add(f.getType());
                }

                if ( f.getJsonName() != null && !f.getJsonName().isEmpty()) {
                    typeList.add("JsonProperty");
                }
            }

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

            sw.p( "import", def.model.packageName + "."  + namingService.capitalize(def.model.name), ";" );

            for ( Map.Entry<String, List<ModelInfo>> e : dtoModelMap.entrySet()) {
                sw.p("import", def.dto.packageName + "." + e.getKey(), ";" );
            }

            sw.p();

            // Class name
            sw.p("public class", className, "{");

            sw.p();
            sw.inctab(1);

            // Create the mappers for all the DTOs

            for ( Map.Entry<String, List<ModelInfo>> e : dtoModelMap.entrySet()) {
                createMapper(sw, def,  e.getKey(), e.getValue(), fields);
            }

            createGeneralMapper(sw, def);

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

    protected void createGeneralMapper(SourceWriter sw, Definition def ) throws IOException {

//        public static List<CrmTripDto> map(List<CrmTrip> models) {
//            List<CrmTripDto> dtos = new ArrayList<>();
//
//            var i= 0;
//            while (  i < models.size() ) {
//
//                var model = models.get(i);
//
//                var dto = crmTripMap(model);
//
//                var subDtoList = new ArrayList<TripDto>();
//                var start = model.getRouteId();
//
//                do {
//                    var subDto = tripMap ( model );
//                    subDtoList.add(subDto);
//                    i++;
//                    if ( i < models.size() ) {
//                        model = models.get(i);
//                    }
//                } while (  i < models.size() &&  model.getRouteId().equals(start) );
//
//                dto. setTrip(subDtoList);
//
//                dtos.add(dto);
//            }
//
//            return dtos;
//        }


        Config conf = configService.getConfig();

        boolean hasPivot = def.pivot != null;

        String modelName = namingService.capitalize(def.model.name); // "CrmTrip"
        String firstDtoName =  modelName  + "Dto"; // "CrmTripDto";

        String secondDtoName = null;
        if (hasPivot) {
            secondDtoName = namingService.capitalize(def.pivot.name) + "Dto"; // TripDto;
        }


        sw.p("public static",  "List<", firstDtoName , ">", "map", "(", "List<",  modelName , ">","models ) {");

        sw.inctab(1);

        //  List<CrmTripDto> dtos = new ArrayList<>();
        sw.p("List<", firstDtoName , ">", "dtos = new ArrayList<>()" , ";" );

        //  var i= 0;
        sw.p( "var i = 0;" );
        sw.p("int dtoCounter = 0;");
        // while (  i < models.size() ) {
        sw.p( "while (  i < models.size() ) { " );
        sw.inctab(1);

        //var model = models.get(i);
        sw.p( "var model = models.get(i); " );


        // var dto = crmTripMap(model);
        sw.p("var dto =", def.model.name+"Map", "( model )", ";" );
        // dto.setOrder(dtoCounter);
        sw.p( "dto.setOrder(dtoCounter);" );
        sw.p("dtoCounter++;");

        if ( hasPivot ) {

            // var subDtoList = new ArrayList<TripDto>();
            sw.p("var subDtoList = new ArrayList< ", secondDtoName, ">();");

            // var start = model.getRouteId();
            sw.p("var start = ", "model.", "get" + namingService.capitalize(def.pivot.grouping), "()", ";");

            sw.p("int subCounter = 0;");

//        do {
            sw.p("do {");
            sw.inctab(1);

//            var subDto = tripMap ( model );
            sw.p("var subDto = ", def.pivot.name + "Map", "(model)", ";");

            // subDto.setOrder(subCounter);
            sw.p("subDto.setOrder(subCounter);");
            sw.p("subCounter++;");

//            subDtoList.add(subDto);
            sw.p("subDtoList.add(subDto);");


    //            i++;
            sw.p("i++;");

    //            if ( i < models.size() ) {
            sw.p("if ( i < models.size() ) {");
            sw.inctab(1);
    //                model = models.get(i);
            sw.p("model = models.get(i);");

    //            }
            sw.inctab(-1);
            sw.p("}");


    //        } while (  i < models.size() &&  model.getRouteId().equals(start) );
            sw.inctab(-1);
            sw.p("} while (  i < models.size() &&",
                    "model.", "get" + namingService.capitalize(def.pivot.grouping), "()", ".equals(start)", ")", ";");

            // dto. setTrip(subDtoList);
            sw.p("dto.", "set" + namingService.capitalize(def.pivot.name), "(subDtoList)", ";" );

        } // hasPivot

        // dtos.add(dto);
        sw.p("dtos.add(dto);");

        sw.inctab(-1);

        // }
        sw.p("} // while ()");

        sw.p("return dtos", ";");

        sw.inctab(-1);

        sw.p("}");


    }


    protected void createMapper(  SourceWriter sw, Definition def, String dtoName, List<ModelInfo> dtoModel, List<ModelInfo> model) throws IOException {

        Config conf = configService.getConfig();

        // Change CrmTripDto into CrmTrip
        String mapperFunctionName = namingService.uncapitalize( namingService.dtoToModel(dtoName) ) + "Map";

        sw.p("protected static", dtoName, mapperFunctionName, "( ", namingService.capitalize(def.model.name) ," model )",  "{");

        sw.p();

        sw.inctab(1);

        sw.p("var dto = new", dtoName, "()", ";");

        boolean firstTime = true;
        // Write attributes
        for (var dtoField : dtoModel) {

            ModelInfo field = findModelAttribute(dtoField, model);

            if (field != null) { // if the field is part of the upper model

                sw.p("dto.set" + namingService.capitalize(field.getIdentifier()),
                        "( ", "model.", "get" + namingService.capitalize(dtoField.getIdentifier()), "()", ")", ";");

            }
        }

        sw.p("return dto;" );

        sw.inctab(-1);

        sw.p("}");

        sw.p();

    }

    protected ModelInfo findModelAttribute(ModelInfo dtoField, List<ModelInfo> model)  {
        ModelInfo field = null;

        for (var f : model ) {
            if ( f.getFieldUuid().equals(dtoField.getFieldUuid())) {
                field = f;
                break;
            }
        }

        return field;
    }

    protected ModelInfo findDtoAttribute(ModelInfo field, List<ModelInfo> dtoModel)  {
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
