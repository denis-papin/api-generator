package lu.isd.birdy.generator.test.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import lu.isd.birdy.generator.test.dto.Option;
import lu.isd.birdy.generator.test.dto.OptionDto;
import lu.isd.birdy.generator.test.dto.OptionValueDto;

public class OptionMapper {

    protected static OptionDto optionMap (  Option  model ) {
    
        var dto = new OptionDto ();
        dto.setGrpId (  model. getGrpId () );
        dto.setUsrId (  model. getUsrId () );
        dto.setOptId (  model. getOptId () );
        return dto;
    }
    
    protected static OptionValueDto optionValueMap (  Option  model ) {
    
        var dto = new OptionValueDto ();
        dto.setPage (  model. getPage () );
        dto.setKey (  model. getKey () );
        dto.setValue (  model. getValue () );
        return dto;
    }
    
    public static List< OptionDto > map ( List< Option > models ) {
        List< OptionDto > dtos = new ArrayList<>();
        var i = 0;
        int dtoCounter = 0;
        while (  i < models.size() ) { 
            var model = models.get(i); 
            var dto = optionMap ( model );
            dto.setOrder(dtoCounter);
            dtoCounter++;
            var subDtoList = new ArrayList<  OptionValueDto >();
            var start =  model. getUsrId ();
            int subCounter = 0;
            do {
                var subDto =  optionValueMap (model);
                subDto.setOrder(subCounter);
                subCounter++;
                subDtoList.add(subDto);
                i++;
                if ( i < models.size() ) {
                    model = models.get(i);
                }
            } while (  i < models.size() && model. getUsrId () .equals(start) );
            dto. setOptionValue (subDtoList);
            dtos.add(dto);
        } // while ()
        return dtos;
    }
    
    
}
