package lu.isd.birdy.generator.service;

import lu.isd.birdy.generator.config.Definition;
import lu.isd.birdy.generator.dao.InformationDao;
import lu.isd.birdy.generator.model.RecordInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InformationService {

    @Autowired
    private InformationDao infoDao;

    public List<RecordInfo> getInfo(Definition def) {
        List<RecordInfo> result = infoDao.findInfo(def).get(0);
        return result;
    }

}
