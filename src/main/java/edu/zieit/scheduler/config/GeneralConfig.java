package edu.zieit.scheduler.config;

import java.io.IOException;

public final class GeneralConfig {

    /**
     * Load configuration from file
     * @throws IOException If config serialization fails
     */
    public void load() throws IOException {
        Configuration conf = new Configuration("/config.yml", this);

        conf.load();


    }

}
