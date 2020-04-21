package lu.isd.birdy.generator.dao;

import lu.isd.birdy.generator.config.Definition;
import lu.isd.birdy.generator.model.RecordInfo;
import lu.isd.birdy.generator.service.NamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class InformationDao {

    private final static Logger LOGGER = LoggerFactory.getLogger(InformationDao.class);


    @Autowired
    private NamingService namingService;

    @Autowired
    @Qualifier("mapperdbJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    protected String forcedTableForColumn( Definition def, String alias ) {
        if ( def.update != null && def.update.forceMapping != null && def.update.forceMapping.get(alias) != null ) {
            String[] parts = def.update.forceMapping.get(alias).split("\\.");
            return  parts[0];
        }
        return null;
    }


    // TODO use a simple List<> to return the right object.
    public List<List<RecordInfo>> findInfo(Definition def) {

        String query = def.query;
        Map<String, Object> params = Map.of();
        LOGGER.debug("sqlQuery={}, params={}", query, params);

        List<List<RecordInfo>> mappings = jdbcTemplate.query(query, params, (rs, i) -> {

            var met = rs.getMetaData();

            int max = met.getColumnCount();

            List<RecordInfo> riList = new ArrayList<>();

            for (int j = 1; j <= max; j++) {

                RecordInfo ri = new RecordInfo();

                ri.setFieldUuid(UUID.randomUUID().toString());
                ri.setSchema(met.getSchemaName(j));

                String name = met.getColumnLabel(j);
                if ( def.model.naming.equalsIgnoreCase("snakecase") ) {
                    // if the db aliases are in "snakecase", we convert them to camelcase, otherwise we do nothing.
                    name = namingService.snakeToCamel(name);
                }

                ri.setName(name);
                ri.setOriginalName(met.getColumnName(j));

                String tableName = met.getTableName(j);
                if (tableName == null || tableName.isEmpty()) {
                    // We look if a table name has not been defined for this alias
                    tableName = forcedTableForColumn(def, name);
                }

                ri.setTableName(tableName);
                ri.setColType(met.getColumnTypeName(j));
                ri.setPrecision(met.getPrecision(j));
                ri.setScale(met.getScale(j));
                ri.setNullable(met.isNullable(j) == 1);

                riList.add(ri);
            }

            return riList;

        });
        return mappings;
    }

}
