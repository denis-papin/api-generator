package lu.isd.birdy.generator.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;

public class OptionDto implements Serializable {

    private BigInteger grpId;
    private BigInteger usrId;
    private BigInteger optId;
    private List<OptionValueDto> optionValue;
    private int order;
    
    @JsonProperty( "grp-id" )
    public BigInteger getGrpId() {
        return this.grpId;
    }
    
    public void setGrpId( BigInteger grpId ) {
        this.grpId = grpId;
    }
    
    @JsonProperty( "usr-id" )
    public BigInteger getUsrId() {
        return this.usrId;
    }
    
    public void setUsrId( BigInteger usrId ) {
        this.usrId = usrId;
    }
    
    @JsonProperty( "opt-id" )
    public BigInteger getOptId() {
        return this.optId;
    }
    
    public void setOptId( BigInteger optId ) {
        this.optId = optId;
    }
    
    public List<OptionValueDto> getOptionValue() {
        return this.optionValue;
    }
    
    public void setOptionValue( List<OptionValueDto> optionValue ) {
        this.optionValue = optionValue;
    }
    
    public int getOrder() {
        return this.order;
    }
    
    public void setOrder( int order ) {
        this.order = order;
    }
    
}
