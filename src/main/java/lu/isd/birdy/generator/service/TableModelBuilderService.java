package lu.isd.birdy.generator.service;

import lu.isd.birdy.generator.config.Definition;
import lu.isd.birdy.generator.model.ModelInfo;
import lu.isd.birdy.generator.model.RecordInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TableModelBuilderService {

    private static final Map<String, String> MODEL_TYPE_MAP = ModelTypeMap.modelTypeMap();

    @Autowired
    private NamingService namingService;

    public Map<String, List<ModelInfo>> generate(Definition def, List<RecordInfo> fields) {
        Map<String, List<ModelInfo>> modelInfoMap = new HashMap<>();
        String scope = "private";

        for (var f : fields) {

            String tableName = f.getTableName();
            String identifier = f.getName();

            if (tableName == null || tableName.equals("")) {
                continue; // ignore sub queries and computed columns.
            }

            String tableModelName = namingService.capitalize(namingService.snakeToCamel(tableName)) + "Table";
            if (!modelInfoMap.containsKey(tableModelName)) {
                modelInfoMap.put(tableModelName, new ArrayList<>());
            }

            List<ModelInfo> modelInfo = modelInfoMap.get(tableModelName);

            String type = MODEL_TYPE_MAP.get(f.getColType());

            var model = new ModelInfo();
            model.setFieldUuid(f.getFieldUuid());
            model.setScope(scope);
            model.setType(type);
            model.setIdentifier(identifier);
            model.setJsonName("");
            modelInfo.add(model);
        }

        // Insert the fk column used as join Ex :  "crm" FK to the crm_trip table model.
        if ( def.update != null && def.update.table != null && def.update.joinColumn != null ) {
            String tableModelName = namingService.capitalize(namingService.snakeToCamel(def.update.table)) + "Table";
            var model = new ModelInfo();
            model.setFieldUuid("");
            model.setScope(scope);
            model.setType("BigInteger");
            model.setIdentifier(def.update.joinColumn); // FK to crm_route
            model.setJsonName("");
            modelInfoMap.get(tableModelName).add(model);

        }

        // Insert the extra column defined in the forceColumn mapping
        // tmpIdCrm: crm_route.tmp_id_crm  => tmp_id_crm, INTEGER => setTmpIdCrm(  )

        def.update.forceMapping.forEach(
            // tmpIdCrm -> crm_route.tmp_id_crm
            (k,v) -> {
                String[] parts = v.split("\\."); //  [crm_route, tmp_id_crm]
                String tableModelName = namingService.capitalize(namingService.snakeToCamel(parts[0])) + "Table";
                var model = new ModelInfo();
                model.setFieldUuid("");
                model.setScope(scope);
                model.setType("BigInteger"); // TODO find the type in the definition of the DB tables from parts[1]
                model.setIdentifier(k); // tmpIdCrm (java field name)
                model.setJsonName("");

                // Search if k exists in the list of fields of the current table model.
                long count = modelInfoMap.get(tableModelName).stream().filter((item) -> item.getIdentifier().equals(k)).count();
                if (count == 0) {
                    modelInfoMap.get(tableModelName).add(model);
                }
            }
        );

        return modelInfoMap;
    }

}
