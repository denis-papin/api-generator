package lu.isd.birdy.generator.service;

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

    public Map<String, List<ModelInfo>> generate(List<RecordInfo> fields) {
        Map<String, List<ModelInfo>> modelInfoMap = new HashMap<>();
        String scope = "private";

        for (var f : fields) {

            if (f.getTableName() == null || f.getTableName().equals("")) {
                continue; // ignore sub queries and computed columns.
            }

            String tableModelName = namingService.capitalize(namingService.snakeToCamel(f.getTableName())) + "Table";
            if (!modelInfoMap.containsKey(tableModelName)) {
                modelInfoMap.put(tableModelName, new ArrayList<>());

            }

            List<ModelInfo> modelInfo = modelInfoMap.get(tableModelName);

            String type = MODEL_TYPE_MAP.get(f.getColType());
            String identifier = f.getName();

            var model = new ModelInfo();
            model.setFieldUuid(f.getFieldUuid());
            model.setScope(scope);
            model.setType(type);
            model.setIdentifier(identifier);
            model.setJsonName("");
            modelInfo.add(model);
        }

        // TODO insert the fk column used as join Ex :  "crm" FK to the crm_trip table model.
        var model = new ModelInfo();
        model.setFieldUuid("");
        model.setScope(scope);
        model.setType("BigInteger");
        model.setIdentifier("crm"); // FK to crm_route
        model.setJsonName("");
        // TODO
        modelInfoMap.get("CrmTripTable").add(model);

        return modelInfoMap;
    }

}
