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

    private static final Map<String, String> MODEL_TYPE_MAP = ModelTypeMap.modelTypeMap();

    public List<ModelInfo> generate(List<RecordInfo> fields) {
        List<ModelInfo> modelInfo = new ArrayList<>();
        String scope = "private";

        for (var f : fields) {
            String type = MODEL_TYPE_MAP.get(f.getColType());

            if ( type == null ) {
                System.out.println("!!!! Unknown db field type : " + f.getColType() );
            }

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
