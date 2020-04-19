package com.globaljetlux.hubdb.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Configuration of datasources.
 */
@Configuration
public class DatasourcesConfig {

    @Bean
    @Qualifier("mapperdbDataSource")
    @ConfigurationProperties(prefix="mapperdb.datasource")
    DataSource mapperdbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("mapperdbJdbcTemplate")
    NamedParameterJdbcTemplate mapperdbJdbcTemplate(@Qualifier("mapperdbDataSource")DataSource mapperdbDataSource) {
        return new NamedParameterJdbcTemplate(mapperdbDataSource);
    }

}
