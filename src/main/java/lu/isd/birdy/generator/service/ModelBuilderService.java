package lu.isd.birdy.generator.service;

import lu.isd.birdy.generator.model.ModelInfo;
import lu.isd.birdy.generator.model.RecordInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ModelBuilderService {

    static final Map<String, String> MODEL_TYPE_MAP = modelTypeMap();

    protected static Map<String, String> modelTypeMap() {
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


    public List<ModelInfo> generate(List<RecordInfo> fields) {
        List<ModelInfo> modelInfo = new ArrayList<>();
        String scope = "private";

        for (var f : fields) {
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

        return modelInfo;
    }

}
