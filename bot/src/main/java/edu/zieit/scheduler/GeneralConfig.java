package edu.zieit.scheduler;

import edu.zieit.scheduler.api.Configuration;

import java.io.IOException;

public final class GeneralConfig {

    /**
     * Load configuration from file
     * @throws IOException If edu.zieit.scheduler.api.config serialization fails
     */
    public void load() throws IOException {
        Configuration conf = new Configuration("/config.yml", this);

        conf.load();


    }

}
