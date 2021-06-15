package edu.zieit.scheduler;

public final class Scheduler {

    public void start() throws Exception {
        GeneralConfig generalConf = new GeneralConfig();
        generalConf.load();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Safe shutdown thread"));
    }

    public void shutdown() {

    }

}
