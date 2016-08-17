package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLEngineResult.Status;

import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tools.FileTools;
import tools.Random;

public class Spider {
	public static Queue<String> urlQueue;
	public static int status = 0;
	public static int Rnumber = 0;
	public static void main(String[] args) {
		urlQueue = new LinkedList<String>();
		
		
		
		//开启爬取线程 根据队列信息爬取
		SpiderThread spiderThread = new SpiderThread();
		spiderThread.start();
		
		//等待输入，保存爬取状态
		Scanner scanner = new Scanner(System.in);
		status = scanner.nextInt();
		//SaveQueue();
		FileTools.SavePages("src/visited.txt", FileTools.GetTempString());
		System.out.println("保存完成");
	}
	
	
	public static class SpiderThread extends Thread{
	    @Override
	    public void run() {
			while(status==0){   //当队列不空
				//每爬取引用专利200条以后，清空队列并随机查找一个专利
				if(Rnumber==50||urlQueue.isEmpty()){
					Rnumber=0;
					String tPatent = "";
					while(tPatent.length()==0){
						tPatent = Random.RandomPatent();
					}
					urlQueue.clear();
					urlQueue.offer(tPatent);
				}
				String toVisit = urlQueue.poll();    //取队首元素
				if(!Visited(toVisit)){       //若该元素未访问过
					String result = GetResultByUrl(toVisit);
					if(!result.equals("error")){
					List<String> urlList = FindUrl(result);
					for (String string : urlList) {
						if(MatchPatentNumber(string)){
							System.out.println(string+"        matches");
							if(!Visited(string)){
						        urlQueue.offer(string);
							}
						}
					}
					SetVisited(toVisit);
					}
					else{
						urlQueue.offer(toVisit);
					}
				}
//				try {
//					System.out.println("-----waiting-----");
//					sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			
	    }
	}
	
	
	//TODO
	public static boolean Visited(String url){
		String visitString = FileTools.GetTempString();
		String visits[] = visitString.split(",");
		for (String string : visits) {
			if(string.equals(url))
				return true;
		}
		return false;
	}
	//TODO
	public static void SetVisited(String toAppend){
		if(FileTools.GetTempString().length()!=0)
		   FileTools.AppendTextOnVisitFile( ","+toAppend);
		else
		   FileTools.AppendTextOnVisitFile(toAppend);
	}

	public static String NumberGet(String result) {
		Document doc = Jsoup.parse(result);
		Elements spans = doc.getElementsByTag("span");
		for (Element span : spans) {
			if (span.attr("class").equals("patent-number")) {
				return span.text().replaceAll(" ", "");
			}
		}
		return null;
	}
	//下载指定url的网址并保存到文件、返回网页内容
	public static String GetResultByUrl(String url){
		try {
			//String url = "https://www.google.com/patents/CN101924798A?cl=zh&hl=zh-CN";
			DefaultHttpClient client = new DefaultHttpClient();
			client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler());
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,2000);
			url = "https://www.google.com/patents/"+url+"?cl=zh&hl=zh-CN";
			System.out.println("本次访问："+url);
			HttpGet get = new HttpGet(new URI(url));
			//get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gb2312");
			//get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			RequestConfig requestConfig = RequestConfig.custom()  
			        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)  
			        .setSocketTimeout(5000).build();  
			get.setConfig(requestConfig);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				FileTools.SavePages("src/pages1/" + NumberGet(result) + ".txt", result);
				Rnumber++;
				System.out.println(result);
				System.out.println(Rnumber);
				return result;
			} else{
				System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileTools.SavePages("src/visited.txt", FileTools.GetTempString());
		}
		return "error";
	}
	
	public static List<String> FindUrl(String result){
		List<String> urlList = new ArrayList<>();
		Document doc = Jsoup.parse(result);
		Elements alist = doc.getElementsByTag("a");
		for (Element a : alist) {
			if (a.parent().attr("class").equals("patent-data-table-td citation-patent")) {
				String href = a.attr("href"); 
				String test = a.text();				
				urlList.add(test);
			}
		}
		return urlList;

	}
	
	public static boolean MatchPatentNumber(String Num){
		//只保留 中文 申请书
		Pattern pattern = Pattern.compile("CN\\d{9}A");
		Matcher matcher = pattern.matcher(Num);
		return matcher.matches();
	}
	

	



}
