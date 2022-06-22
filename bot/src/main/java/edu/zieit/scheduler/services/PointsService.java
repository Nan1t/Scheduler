package edu.zieit.scheduler.services;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.util.ConversionUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public final class PointsService {

    private final MainConfig conf;

    private final HttpPost loginRequest;
    private final HttpGet pointsRequest;
    private final RequestConfig requestConfig;

    @Inject
    public PointsService(MainConfig conf) {
        this.conf = conf;

        loginRequest = new HttpPost(conf.getPointsUrl());

        loginRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        loginRequest.addHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        loginRequest.addHeader("Accept-Encoding", "gzip, deflate");
        loginRequest.addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");

        pointsRequest = new HttpGet(conf.getPointsUrl());
        pointsRequest.addHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        pointsRequest.addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");

        requestConfig = RequestConfig.custom()
                .setConnectTimeout(conf.getPointsTimeout())
                .setSocketTimeout(conf.getPointsTimeout())
                .setConnectionRequestTimeout(conf.getPointsTimeout())
                .build();
    }

    public byte[] getPoints(Person person, String password) throws IOException {
        CookieStore cookies = new BasicCookieStore();
        HttpClient client = HttpClients.custom()
                .setDefaultCookieStore(cookies)
                .build();

        if(login(client, person, password)) {
            String html = getPointsHtml(client, cookies);

            if(html != null) {
                return ConversionUtil.renderHtml(html.replace("\n", ""));
            }
        }

        return null;
    }

    private boolean login(HttpClient client, Person person, String password) throws IOException {
        HttpPost post = new HttpPost(loginRequest.getURI());
        post.setConfig(requestConfig);
        post.setHeaders(loginRequest.getAllHeaders());

        Map<String, String> values = new HashMap<>();
        values.put("lastname", URLEncoder.encode(person.lastName(), "cp1251"));
        values.put("n1", URLEncoder.encode(person.firstName(), "cp1251"));
        values.put("n2", URLEncoder.encode(person.patronymic(), "cp1251"));
        values.put("password", URLEncoder.encode(password, "cp1251"));

        String data = StrSubstitutor.replace(conf.getPointsLoginQuery(), values);
        StringEntity entity = new StringEntity(data);

        post.setEntity(entity);

        HttpResponse response = client.execute(post);

        return response.getStatusLine().getStatusCode() == conf.getPointsLoginSuccessCode();
    }

    private String getPointsHtml(HttpClient client, CookieStore cookies) throws IOException {
        HttpGet request = new HttpGet(pointsRequest.getURI());
        request.setHeaders(pointsRequest.getAllHeaders());
        request.addHeader("Cookie", cookiesToString(cookies));
        HttpResponse response = client.execute(request);
        return IOUtils.toString(response.getEntity().getContent(), "cp1251");
    }

    private String cookiesToString(CookieStore cookies) {
        StringBuilder builder = new StringBuilder();
        for(Cookie c : cookies.getCookies()) {
            builder.append(c.getName());
            builder.append("=");
            builder.append(c.getValue());
            builder.append("; ");
        }
        return builder.toString();
    }

}
