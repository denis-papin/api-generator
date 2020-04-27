package lu.isd.birdy.generator.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.sql.Date;

public class SeasonDto implements Serializable {

    private BigInteger id;
    private BigInteger grpId;
    private Date startDate;
    private Date endDate;
    private String description;
    private int order;
    
    @JsonProperty( "id" )
    public BigInteger getId() {
        return this.id;
    }
    
    public void setId( BigInteger id ) {
        this.id = id;
    }
    
    @JsonProperty( "grp-id" )
    public BigInteger getGrpId() {
        return this.grpId;
    }
    
    public void setGrpId( BigInteger grpId ) {
        this.grpId = grpId;
    }
    
    @JsonProperty( "start-date" )
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate( Date startDate ) {
        this.startDate = startDate;
    }
    
    @JsonProperty( "end-date" )
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate( Date endDate ) {
        this.endDate = endDate;
    }
    
    @JsonProperty( "description" )
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription( String description ) {
        this.description = description;
    }
    
    public int getOrder() {
        return this.order;
    }
    
    public void setOrder( int order ) {
        this.order = order;
    }
    
}
