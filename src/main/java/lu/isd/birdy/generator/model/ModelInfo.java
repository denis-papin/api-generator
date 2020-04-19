package lu.isd.birdy.generator.model;

/**
 * This class can represent the Java fields needed to generate a POJO
 * for the Dao Model and the service DTO.
 */
public class ModelInfo {

    String fieldUuid;
    String scope;
    String type;
    String identifier;
    String jsonName;

    public ModelInfo() {
    }

    public String getFieldUuid() {
        return fieldUuid;
    }

    public void setFieldUuid(String fieldUuid) {
        this.fieldUuid = fieldUuid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getJsonName() {
        return jsonName;
    }

    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }

    @Override
    public String toString() {
        return "ModelInfo{" +
                "fieldUuid='" + fieldUuid + '\'' +
                ", scope='" + scope + '\'' +
                ", type='" + type + '\'' +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
