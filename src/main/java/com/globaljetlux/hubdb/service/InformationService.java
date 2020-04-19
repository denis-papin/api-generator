package com.globaljetlux.hubdb.service;

import com.globaljetlux.hubdb.config.Definition;
import com.globaljetlux.hubdb.dao.InformationDao;
import com.globaljetlux.hubdb.model.RecordInfo;
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
