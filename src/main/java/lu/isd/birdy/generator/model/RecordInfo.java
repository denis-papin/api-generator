package lu.isd.birdy.generator.model;

public class RecordInfo {

    private String fieldUuid;

    private String schema;

    private String name;

    private String originalName;

    private String tableName;

    private String colType;

    private Integer precision;

    private Integer scale;

    private Boolean nullable;

    public RecordInfo() {
    }

    public String getFieldUuid() {
        return fieldUuid;
    }

    public void setFieldUuid(String fieldUuid) {
        this.fieldUuid = fieldUuid;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "RecordInfo{" +
                "fieldUuid='" + fieldUuid + '\'' +
                ", schema='" + schema + '\'' +
                ", name='" + name + '\'' +
                ", originalName='" + originalName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", colType='" + colType + '\'' +
                ", precision=" + precision +
                ", scale=" + scale +
                ", nullable=" + nullable +
                '}';
    }
}
