package lu.isd.birdy.generator.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OptionValueDto implements Serializable {

    private String page;
    private String key;
    private String value;
    private int order;
    
    @JsonProperty( "page" )
    public String getPage() {
        return this.page;
    }
    
    public void setPage( String page ) {
        this.page = page;
    }
    
    @JsonProperty( "key" )
    public String getKey() {
        return this.key;
    }
    
    public void setKey( String key ) {
        this.key = key;
    }
    
    @JsonProperty( "value" )
    public String getValue() {
        return this.value;
    }
    
    public void setValue( String value ) {
        this.value = value;
    }
    
    public int getOrder() {
        return this.order;
    }
    
    public void setOrder( int order ) {
        this.order = order;
    }
    
}
