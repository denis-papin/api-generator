package lu.isd.birdy.generator.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;

public class Option implements Serializable {

    private BigInteger grpId;
    private BigInteger usrId;
    private BigInteger optId;
    private String page;
    private String key;
    private String value;
    
    public BigInteger getGrpId() {
        return this.grpId;
    }
    
    public void setGrpId( BigInteger grpId ) {
        this.grpId = grpId;
    }
    
    public BigInteger getUsrId() {
        return this.usrId;
    }
    
    public void setUsrId( BigInteger usrId ) {
        this.usrId = usrId;
    }
    
    public BigInteger getOptId() {
        return this.optId;
    }
    
    public void setOptId( BigInteger optId ) {
        this.optId = optId;
    }
    
    public String getPage() {
        return this.page;
    }
    
    public void setPage( String page ) {
        this.page = page;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey( String key ) {
        this.key = key;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue( String value ) {
        this.value = value;
    }
    
}
