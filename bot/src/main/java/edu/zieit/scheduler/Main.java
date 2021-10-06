package edu.zieit.scheduler;

public final class Main {

    public static void main(String[] args) throws Exception {
        //System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        System.setProperty("java.awt.headless", "true");

        new Scheduler().start();
    }

}
