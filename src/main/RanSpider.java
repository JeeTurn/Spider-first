package main;

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

import tools.FileTools;
import tools.Random;

public class RanSpider {
	public static void main(String[] args) {
		try {
			String url = "https://www.google.com/patents/"+Random.RandomPatent()+"?cl=zh&hl=zh-CN";
			DefaultHttpClient client = new DefaultHttpClient();
			client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler());
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,2000);
			System.out.println("本次访问："+url);
			HttpGet get = new HttpGet(new URI(url));
			get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gb2312");
			RequestConfig requestConfig = RequestConfig.custom()  
			        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)  
			        .setSocketTimeout(5000).build();  
			get.setConfig(requestConfig);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				System.out.println(result);
			} else{
				System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
