package com.blacklocus.sample.time;

import com.blacklocus.webapp.RunServer;
import com.blacklocus.webapp.util.ExceptingRunnable;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jason Dunkelberger (dirkraft)
 */
public class ConnectionTest {

    static Thread SERVER;

    static final int wait = 1000; // 1 second
    static final HttpClient httpClient = HttpClientFactory.createHttpClient(wait * 10, wait * 10);

    @BeforeClass
    public static void startServer() throws Exception {
        SERVER = new Thread(new RunServer());
//        SERVER.start(); // or use a separate jvm server
        Thread.sleep(2000L); // startup
    }

    private final ExecutorService requestPool = Executors.newCachedThreadPool();

    private final AtomicInteger counter = new AtomicInteger();

    private void makeConnection() {
        requestPool.submit(new ExceptingRunnable() {
            @Override
            public void go() throws Exception {
                HttpResponse res = null;
                try {
                    res = httpClient.execute(new HttpGet("http://127.0.0.1:8080/wait/" + wait));
                    System.out.println(IOUtils.toString(res.getEntity().getContent()));
                    counter.getAndIncrement();
                } finally {
                    if (res != null) {
                        EntityUtils.consumeQuietly(res.getEntity());
                    }
                }
            }
        });
    }

    @Test
    public void testOneConnection() throws InterruptedException {
        makeConnection();
        Thread.sleep(wait * 5);
        System.out.println(counter.get());
    }

    @Test
    public void testMaxConnections() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            makeConnection();
        }
        Thread.sleep(wait * 10);
        System.out.println(counter.get());
    }

    @AfterClass
    public static void shutdown() throws Exception {
        if (RunServer.SERVER != null) {
            RunServer.SERVER.stop();
        }
    }
}
