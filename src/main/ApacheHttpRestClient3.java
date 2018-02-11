package main;

import java.io.*;
import java.util.logging.Logger;

import jdk.net.SocketFlow;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.spi.LoggerFactory;

public class ApacheHttpRestClient3 {
    private String targetUrl = "";
    private HttpClient client = null;
    HttpGet httpGet = null;

    public ApacheHttpRestClient3(HttpClient client, HttpGet httpGet, String targetUrl) {
        this.client = client;
        this.httpGet = httpGet;
        this.targetUrl = targetUrl;
    }

    public ApacheHttpRestClient3(String targetUrl) {
        this.targetUrl = targetUrl;
        this.client = HttpClientBuilder.create().build();
        this.httpGet = new HttpGet(targetUrl);
    }

    public ApacheHttpRestClient3() {}

    public boolean getStatus() {
        BufferedReader rd = null;
        boolean status = false;
        try{
            System.out.println("Requesting status: " + targetUrl);
            HttpResponse response = client.execute(httpGet);

            if(response.getStatusLine().getStatusCode() == 200) {
               System.out.println("Is online.");
               status = true;
            }

        } catch(Exception e) {
            System.out.println("Error getting the status"/*, e*/);
        } finally {
            if (rd != null) {
                try{
                    rd.close();
                } catch (IOException ioe) {
                    System.out.println("Error while closing the Buffered Reader used for reading the status"/*, ioe*/);
                }
            }
        }
        return status;
    }

    public boolean printUriWithInjection() {
        BufferedReader rd = null;
        boolean status = false;
        try{
            //LOG.debug("Requesting status: " + targetUrl);
            System.out.println("Requesting status: " + targetUrl);

            HttpResponse httpResponse = client.execute(httpGet);

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("----------------------------------------");

            HttpEntity entity = httpResponse.getEntity();
            if(httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Is online.");
                System.out.println(httpResponse.getStatusLine().getStatusCode());

                status = true;
            }
            byte[] buffer = new byte[1024];
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try {
                    int bytesRead = 0;
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        String chunk = new String(buffer, 0, bytesRead);
                        System.out.println(chunk);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try { inputStream.close(); } catch (Exception ignore) {}
                }
            }


        } catch(Exception e) {
            System.out.println("Error getting the status"/*, e*/);
        } finally {
            if (rd != null) {
                try{
                    rd.close();
                } catch (IOException ioe) {
                    System.out.println("Error while closing the Buffered Reader used for reading the status"/*, ioe*/);
                }
            }
        }
        return status;
    }


    public boolean printUriWithoutInjection(String uri)  {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet httpGetRequest = new HttpGet(uri);
            //HttpGet httpGetRequest = new HttpGet("http://api.metro.net/agencies/lametro/routes/4/");

            HttpResponse httpResponse = httpClient.execute(httpGetRequest);

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("----------------------------------------");

            HttpEntity entity = httpResponse.getEntity();

            byte[] buffer = new byte[1024];
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try {
                    int bytesRead = 0;
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        String chunk = new String(buffer, 0, bytesRead);
                        System.out.println(chunk);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try { inputStream.close(); } catch (Exception ignore) {}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //httpClient.getConnectionManager().shutdown();
        }
    return true;
    }
}
