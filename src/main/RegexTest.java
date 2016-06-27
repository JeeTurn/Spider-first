package main;

import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import po.Patent;
import tools.JDBCconnector;
import tools.ReadText;

public class RegexTest {
	public static void main(String[] args) {
		String result = ReadText.readFileByChars("src/result.txt");
		Patent patent = new Patent();
		Document doc = Jsoup.parse(result);
		JDBCconnector jdbCconnector= new JDBCconnector();
		// 获取标题
		Elements spans = doc.getElementsByTag("span");
		for (Element span : spans) {
			if (span.attr("class").equals("patent-title")) {
				Element celement = span.children().first();
				System.out.println("标题：" + celement.text());
				patent.setTitle(celement.text());
			} else if (span.attr("class").equals("patent-number")) {
				System.out.println("专利号：" + span.text().replaceAll(" ", ""));
				patent.setPatentId(span.text().replaceAll(" ", ""));
			}
		}
		
		// 获取申请号、申请人、发明者
		Elements tds = doc.getElementsByTag("td");
		for(Element td : tds){
			if(td.text().equals("专利申请号")){
				Element apply = td.nextElementSibling();
				System.out.println("申请号："+ apply.text());
				patent.setApplyId(apply.text());
			}
			else if(td.text().equals("申请人")){
				Element applyCompany = td.nextElementSibling();
				System.out.println("申请人："+ applyCompany.text());
				patent.setProposer(applyCompany.text());
			}
			else if(td.text().equals("发明者")){
				Element inventor = td.nextElementSibling();
				System.out.println("发明者："+ inventor.text());
				patent.setAuthor(inventor.text());
			}
		}
		
		// 获取摘要 权利要求
		Elements divs = doc.getElementsByTag("div");
		for(Element div:divs){
			if(div.attr("class").equals("abstract")){
				System.out.println("摘要：");
				System.out.println(div.text());
				patent.setPatentAbstract(div.text());
			}
			else if(div.attr("class").equals("claim-text")){
				System.out.println("权利要求：");
				System.out.println(div.text());
				patent.setClaim(div.text());
			}
			else if(div.attr("class").equals("patent-section patent-description-section")){
				Elements patentTextDivs = div.children();
				for(Element patentText:patentTextDivs){
					if(patentText.attr("class").equals("patent-text")){
						System.out.println("说明：");
						System.out.println(patentText.text());
						patent.setPatentText(patentText.text());
					}
				}
				
			}
		}
		jdbCconnector.save(patent);
		

		// 获取需要爬取的下一个页面的url
		System.out.println("--------------------下次链接------------------------------");
		Elements alist = doc.getElementsByTag("a");
		for (Element a : alist) {
			if (a.parent().attr("class").equals("patent-data-table-td citation-patent")) {
				String href = a.attr("href"); 
				String test = a.text();
				System.out.println("https://www.google.com"+href+"&hl=zh-CN" + "--------" + test);
			}
		}

	}
}
