package main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import po.Patent;
import tools.FileTools;
import tools.JDBCconnector;
import tools.ReadText;

public class Recoder {
	public static void main(String[] args) throws Exception {
		File flist = new File("src/pages");
		File pages[] = flist.listFiles();

		for (int i=1;i<pages.length;i++) {
		    File file = pages[i];
			String result = FileTools.ReadFile(file.getPath());
			Patent patent = new Patent();
			Document doc = Jsoup.parse(result);
			JDBCconnector jdbCconnector= new JDBCconnector();
			// 获取标题
			Elements spans = doc.getElementsByTag("span");
			for (Element span : spans) {
				if (span.attr("class").equals("patent-title")) {
					Element celement = span.children().first();
					System.out.println("标题：" + celement.text());
					patent.setTitle(ToUtf(celement.text()));
					
				} else if (span.attr("class").equals("patent-number")) {
					System.out.println("专利号：" + span.text().replaceAll(" ", ""));
					patent.setPatentId(ToUtf(span.text().replaceAll(" ", "")));
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
					patent.setProposer(ToUtf(applyCompany.text()));
				}
				else if(td.text().equals("发明者")){
					Element inventor = td.nextElementSibling();
					System.out.println("发明者："+ inventor.text());
					patent.setAuthor(ToUtf(inventor.text()));
				}
			}
			
			// 获取摘要 权利要求
			Elements divs = doc.getElementsByTag("div");
			for(Element div:divs){
				if(div.attr("class").equals("abstract")){
					System.out.println("摘要：");
					System.out.println(div.text());
					patent.setPatentAbstract(ToUtf(div.text()));
				}
				else if(div.attr("class").equals("claim-text")){
					System.out.println("权利要求：");
					System.out.println(div.text());
					patent.setClaim(ToUtf(div.text()));
				}
				else if(div.attr("class").equals("patent-section patent-description-section")){
					Elements patentTextDivs = div.children();
					for(Element patentText:patentTextDivs){
						if(patentText.attr("class").equals("patent-text")){
							System.out.println("说明：");
							System.out.println(patentText.text());
							patent.setPatentText(ToUtf(patentText.text()));
						}
					}
					
				}
			}
			jdbCconnector.save(patent);
		}
	}


public static String ToUtf(String s){
	try {
		return new String(s.getBytes(), "utf-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return s;
}

}

