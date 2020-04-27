package lu.isd.birdy.generator.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;
import java.sql.Date;

public class Season implements Serializable {

    private BigInteger id;
    private BigInteger grpId;
    private Date startDate;
    private Date endDate;
    private String description;
    
    public BigInteger getId() {
        return this.id;
    }
    
    public void setId( BigInteger id ) {
        this.id = id;
    }
    
    public BigInteger getGrpId() {
        return this.grpId;
    }
    
    public void setGrpId( BigInteger grpId ) {
        this.grpId = grpId;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate( Date startDate ) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate( Date endDate ) {
        this.endDate = endDate;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription( String description ) {
        this.description = description;
    }
    
}
