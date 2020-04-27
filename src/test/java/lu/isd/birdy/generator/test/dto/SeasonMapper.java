package lu.isd.birdy.generator.test.dto;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lu.isd.birdy.generator.test.dto.Season;
import lu.isd.birdy.generator.test.dto.SeasonDto;

public class SeasonMapper {

    protected static SeasonDto seasonMap (  Season  model ) {
    
        var dto = new SeasonDto ();
        dto.setId (  model. getId () );
        dto.setGrpId (  model. getGrpId () );
        dto.setStartDate (  model. getStartDate () );
        dto.setEndDate (  model. getEndDate () );
        dto.setDescription (  model. getDescription () );
        return dto;
    }
    
    public static List< SeasonDto > map ( List< Season > models ) {
        List< SeasonDto > dtos = new ArrayList<>();
        var i = 0;
        int dtoCounter = 0;
        while (  i < models.size() ) { 
            var model = models.get(i); 
            var dto = seasonMap ( model );
            dto.setOrder(dtoCounter);
            dtoCounter++;
            dtos.add(dto);
        } // while ()
        return dtos;
    }
    
    
}
