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
		
		//恢复上次的爬取状态
		String savedQueue = FileTools.ReadFile("src/SavedQueue.txt");
		if(savedQueue.length()!=0){
			String que[] = savedQueue.split(",");
			for (String string : que) {
				urlQueue.offer(string);
			}
		}
		
		//开启爬取线程 根据队列信息爬取
		SpiderThread spiderThread = new SpiderThread();
		spiderThread.start();
		
		//等待输入，保存爬取状态
		Scanner scanner = new Scanner(System.in);
		status = scanner.nextInt();
		SaveQueue();
		System.out.println("保存完成");
	}
	
	
	public static class SpiderThread extends Thread{
	    @Override
	    public void run() {
			while(status==0){   //当队列不空
				//每爬取引用专利200条以后，清空队列并随机查找一个专利
				if(Rnumber==200||urlQueue.isEmpty()){
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
							if(!Visited(string))
						    urlQueue.offer(string);
						}
					}
					SetVisited(toVisit);
					}
					else{
						urlQueue.offer(toVisit);
					}
				}
				try {
					System.out.println("-----waiting-----");
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	    }
	}
	
	
	//TODO
	public static boolean Visited(String url){
		String visitString = FileTools.ReadFile("src/visited.txt");
		String visits[] = visitString.split(",");
		for (String string : visits) {
			if(string.equals(url))
				return true;
		}
		return false;
	}
	//TODO
	public static void SetVisited(String toAppend){
		if(FileTools.ReadFile("src/visited.txt").length()!=0)
		   FileTools.AppendTextOnFile("src/visited.txt", ","+toAppend);
		else
		   FileTools.AppendTextOnFile("src/visited.txt",toAppend);
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
			get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gb2312");
			RequestConfig requestConfig = RequestConfig.custom()  
			        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)  
			        .setSocketTimeout(5000).build();  
			get.setConfig(requestConfig);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				FileTools.SavePages("src/pages/" + NumberGet(result) + ".txt", result);
				Rnumber++;
				System.out.println(result);
				System.out.println(Rnumber);
				return result;
			} else{
				System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			SaveQueue();
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
	
	public static void SaveQueue(){
		String toWriteQueue = "";
		for (String string : urlQueue) {
			if(toWriteQueue.length()==0)
			toWriteQueue+=string;
			else 
				toWriteQueue+=","+string;
		}
		FileTools.SavePages("src/SavedQueue.txt", toWriteQueue);
	}
	

	// public static boolean matcher(String s, String pattern) {
	// Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE +
	// Pattern.UNICODE_CASE);
	// Matcher matcher = p.matcher(s);
	// if (matcher.find()) {
	//
	// return true;
	//
	// } else {
	//
	// return false;
	//
	// }
	//
	// }
	//
	// public static String getContentCharset(HttpResponse response) {
	//
	// String charset = "ISO_8859-1";
	//
	// Header header = response.getEntity().getContentType();
	// if (header != null) {
	// String s = header.getValue();
	// if (matcher(s, "(charset)\\s?=\\s?(utf-?8)")) {
	//
	// charset = "utf-8";
	//
	// } else if (matcher(s, "(charset)\\s?=\\s?(gbk)")) {
	//
	// charset = "gbk";
	//
	// } else if (matcher(s, "(charset)\\s?=\\s?(gb2312)")) {
	//
	// charset = "gb2312";
	//
	// }
	//
	// }
	//
	// return charset;
	//
	// }

}
