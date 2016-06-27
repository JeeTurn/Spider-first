package tools;

import java.net.URI;

import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class Random {

	public static String RandomPatent(){
		try {
			int x = (int)(Math.random()*10000000);
			String url = "https://www.google.com/patents/CN10"+x+"A?cl=zh&hl=zh-CN";
			DefaultHttpClient client = new DefaultHttpClient();
			client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler());
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,2000);
			HttpGet get = new HttpGet(new URI(url));
			get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gb2312");
			RequestConfig requestConfig = RequestConfig.custom()  
			        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)  
			        .setSocketTimeout(5000).build();  
			get.setConfig(requestConfig);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println("访问随机专利");
				return "CN10"+x+"A";
			} else{
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
