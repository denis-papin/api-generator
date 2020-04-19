package com.globaljetlux.hubdb.service;

import com.globaljetlux.hubdb.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

@Component
public class ConfigService {

// TODO IT DOESN'T GET THE VALUE
    @Value("${generator.definition}")
    private String definitionPath;

    private Config config;

    public ConfigService() {
    }


    @PostConstruct
    public void init() {
        System.out.println("We are loading the configuation");

        Yaml yaml = new Yaml(new Constructor(Config.class));
//        InputStream inputStream = this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("conf.yml");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(Paths.get(definitionPath).toFile());
            config = yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Config getConfig() {
        return config;
    }
}
