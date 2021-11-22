package edu.zieit.scheduler.panel;

import org.rapidoid.buffer.Buf;
import org.rapidoid.http.AbstractHttpServer;
import org.rapidoid.http.HttpStatus;
import org.rapidoid.net.abstracts.Channel;
import org.rapidoid.net.impl.RapidoidHelper;

public class Main extends AbstractHttpServer {

    @Override
    protected HttpStatus handle(Channel ctx, Buf buf, RapidoidHelper data) {
        return HttpStatus.NOT_FOUND;
    }

    public static void main(String[] args) throws Exception {
        byte[] mainPage = Main.class.getResourceAsStream("/html/main.html")
                .readAllBytes();
        byte[] schedulePage = Main.class.getResourceAsStream("/html/schedule.html")
                .readAllBytes();

        new Main().listen(8080);
    }

}
