package lu.isd.birdy.generator;

import lu.isd.birdy.generator.config.Config;
import lu.isd.birdy.generator.config.Definition;
import lu.isd.birdy.generator.model.ModelInfo;
import lu.isd.birdy.generator.model.RecordInfo;
import lu.isd.birdy.generator.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    private ConfigService configService;


    @Autowired
    private InformationService metaInfo;

    @Autowired
    private ModelBuilderService modelBuilder;

    @Autowired
    private TableModelBuilderService tableModelBuilder;

    @Autowired
    private DtoBuilderService dtoBuilder;

    @Autowired
    private SourceFileService sourceFile;

    @Autowired
    private MappingBuilderService mappingBuilder;

    @Autowired
    private ReversedMappingBuilderService reversedMappingBuilder;

    @Autowired
    private NamingService namingService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");

        Config conf = configService.getConfig();

        // String PROJECT_BASE = "/home/dcrespe/prj/api-generator/src/main/java";
        String PROJECT_BASE = conf.projectBase;

        for (Definition def : conf.definition ) {

            List<RecordInfo> fields = metaInfo.getInfo(def);

            // Generate a list of fields for the dao model.
            List<ModelInfo> modelInfo = modelBuilder.generate(fields);

            // Generate a fields of fields for the DTOs
            Map<String, List<ModelInfo>> dtoModelList = dtoBuilder.generate(def, modelInfo);

            // Generate Model Java files
            String modelName = namingService.capitalize(def.model.name);
            sourceFile.generate(PROJECT_BASE, def.model.packageName, modelName, modelInfo);

            // Generate the DTOs Java files.
            for (var e : dtoModelList.entrySet()) {
                sourceFile.generate(PROJECT_BASE, def.dto.packageName, e.getKey(), e.getValue());
            }

            // Generate the field mapper class
            mappingBuilder.generate(def, PROJECT_BASE, def.mapper.packageName, modelName + "Mapper", modelInfo, dtoModelList);

            // # Update

            // ## Generate all the table dao models.
            Map<String, List<ModelInfo>> tableModelInfoMap = tableModelBuilder.generate(def, fields);

            for( var e : tableModelInfoMap.entrySet()) {
                sourceFile.generate(PROJECT_BASE, def.model.packageName, e.getKey(), e.getValue());
            }

            // ## Generate the Dto to field reversed mapper class
            reversedMappingBuilder.generate(def, PROJECT_BASE, def.mapper.packageName, modelName + "ReversedMapper", tableModelInfoMap, dtoModelList);

        }

    }

}