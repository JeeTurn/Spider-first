package main;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import po.Patent;
import tools.JDBCconnector;

public class ProxyTest {
	public static void main(String[] args) {
		try {
			// DefaultHttpClient client = new DefaultHttpClient();
			// String proxyIp = "45.62.117.248";
			// int port = 4128;
			// String uname = "luyi";
			// String pwd = "940321";
			// client.getCredentialsProvider().setCredentials(
			// new AuthScope(proxyIp, port),
			// new UsernamePasswordCredentials(uname, pwd));
			// HttpHost proxy = new HttpHost(proxyIp,port);
			// client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
			// proxy);
			// HttpGet get = new HttpGet(new URI(url));
			// HttpResponse response = client.execute(get);
			// if (response.getStatusLine().getStatusCode() == 200) {
			// HttpEntity entity = response.getEntity();
			// String result = EntityUtils.toString(entity);
			// System.out.println(result);
			// JDBCconnector jdbCconnector = new JDBCconnector();
			// jdbCconnector.save(new Patent());
			// }
			String url = "https://www.google.com/patents/CN101924798A?cl=zh&hl=zh-CN";
			CloseableHttpClient httpclient = HttpClients.createDefault();
			//HttpHost target = new HttpHost("localhost", 443, "https");
			HttpHost proxy = new HttpHost("127.0.0.1", 1080);
			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			HttpGet request = new HttpGet(url);
			request.setConfig(config);
			CloseableHttpResponse response = httpclient.execute(request);
			try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                EntityUtils.consume(response.getEntity());
            } finally {
                response.close();
                httpclient.close();
            }
       
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
