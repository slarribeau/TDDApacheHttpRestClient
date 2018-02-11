package test;

import junit.framework.Assert;
import main.ApacheHttpRestClient3;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApacheHttpRestClent3Test extends Mockito {
    String URI;

    @Before
    public void setUp() {
       URI = "http://api.metro.net/agencies/lametro/routes/4/";
    }

    @Test
    public void printUriWithoutInjection() {
        ApacheHttpRestClient3 ac3 = new ApacheHttpRestClient3();
        ac3.printUriWithoutInjection(URI);
    }

    @Test
    public void useInjectedMocksToReturnStatusTrue() throws ClientProtocolException, IOException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpGet httpGet = mock(HttpGet.class);
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(httpGet)).thenReturn(httpResponse);

        ApacheHttpRestClient3 client = new ApacheHttpRestClient3(httpClient, httpGet, "");
        assertTrue(client.getStatus());
    }

    @Test
    public void useInjectedMocksToReturnStatusFalse() throws ClientProtocolException, IOException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpGet httpGet = mock(HttpGet.class);
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(httpClient.execute(httpGet)).thenThrow(HttpHostConnectException.class);
        ApacheHttpRestClient3 client = new ApacheHttpRestClient3(httpClient, httpGet, "");
        boolean status = client.getStatus();
        assertFalse(client.getStatus());
    }

    @Test
    public void useInjectedRealObjectsToReturnRealStatus() throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGetRequest = new HttpGet(URI);
        HttpResponse httpResponse = httpClient.execute(httpGetRequest);
        StatusLine statusLine = httpResponse.getStatusLine();

        ApacheHttpRestClient3 client = new ApacheHttpRestClient3(URI);
        assertTrue(client.getStatus());
    }

    @Test
    public void useInjectedRealObjectsToPrintRealURI() throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGetRequest = new HttpGet(URI);
        HttpResponse httpResponse = httpClient.execute(httpGetRequest);
        StatusLine statusLine = httpResponse.getStatusLine();

        ApacheHttpRestClient3 client = new ApacheHttpRestClient3(URI);
        assertTrue(client.printUriWithInjection());
    }
}
