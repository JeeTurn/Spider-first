package main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import po.Patent;
import tools.FileTools;
import tools.JDBCconnector;

public class Recoder {
	public static void main(String[] args) throws Exception {
		File flist = new File("src/pages1");
		File pages[] = flist.listFiles();

		for (int i = 1; i < pages.length; i++) {
			String text = "";
			String title = "";
			File file = pages[i];
			// File file = new File("src/pages/CN101572320A.txt");
			String result = FileTools.ReadFile(file.getPath());
			Patent patent = new Patent();	
			patent.setClaim("");//避免循环拼接claim时空指针
			Document doc = Jsoup.parse(result);
			// 获取标题
			Elements spans = doc.getElementsByTag("span");
			for (Element span : spans) {
				if (span.attr("class").equals("patent-title")) {
					Element celement = span.children().first();
					// System.out.println("标题：" + celement.text());
					patent.setTitle(ToUtf(celement.text()));
					title = ToUtf(celement.text());
				} else if (span.attr("class").equals("patent-number")) {
					// System.out.println("专利号：" + span.text().replaceAll(" ",
					// ""));
					patent.setPatentId(ToUtf(span.text().replaceAll(" ", "")));
				}
			}

			// 获取申请号、申请人、发明者
			Elements tds = doc.getElementsByTag("td");
			for (Element td : tds) {
				if (td.text().equals("专利申请号")) {
					Element apply = td.nextElementSibling();
					// System.out.println("申请号："+ apply.text());
					patent.setApplyId(apply.text());
				}
				if (td.text().equals("国际分类号")) {
					Element apply = td.nextElementSibling();
					// System.out.println("分类号："+ apply.text());
					Elements ipcSpans = apply.children();
					String ipc = "";
					for (Element element : ipcSpans) {
						ipc += element.text() + " ";
					}
					ipc = ipc.substring(0, ipc.length() - 1);
					patent.setPatentIpc(ipc);
				} else if (td.text().equals("申请人")) {
					Element applyCompany = td.nextElementSibling();
					// System.out.println("申请人："+ applyCompany.text());
					patent.setProposer(ToUtf(applyCompany.text()));
				} else if (td.text().equals("发明者")) {
					Element inventor = td.nextElementSibling();
					// System.out.println("发明者："+ inventor.text());
					patent.setAuthor(ToUtf(inventor.text()));
				}
			}

			// 获取摘要 权利要求
			Elements divs = doc.getElementsByTag("div");
			for (Element div : divs) {
				if (div.attr("class").equals("abstract")) {
					// System.out.println("摘要：");
					// System.out.println(div.text());
					patent.setPatentAbstract(ToUtf(div.text()));
					text += ToUtf(div.text());
				} else if (div.attr("class").equals("claim-text")) {
					//因为权利要求存在于多个div中 故应拼接字符串
					patent.setClaim(patent.getClaim()+"<p>"+ToUtf(div.text())+"</p>");
					text += ToUtf(div.text());
				} else if (div.attr("class").equals("patent-section patent-description-section")) {
					Elements patentTextDivs = div.children();
					for (Element patentText : patentTextDivs) {
						if (patentText.attr("class").equals("patent-text")) {
							Element element = patentText.child(0);
							patent.setPatentText(ToUtf(element.html()));
							text += ToUtf(div.text());
						}
					}

				}
			}
			Elements as = doc.getElementsByTag("a");
			for (Element a : as) {
				if (a.attr("id").equals("forward-citations")) {
					Element pa = a.parent();
					Elements temp = pa.getElementsByTag("a");
					for (Element element : temp) {
						if (element.parent().attr("class").equals("patent-data-table-td citation-patent")) {
							patent.setBeQuote( patent.getBeQuote()+ element.text() + " ");
						}
					}
					patent.setBeQuote(patent.getBeQuote().trim());
				} else if (a.id().equals("backward-citations")) {
					Element pa = a.parent();
					Elements temp = pa.getElementsByTag("a");
					for (Element element : temp) {
						if (element.parent().attr("class").equals("patent-data-table-td citation-patent")) {
							patent.setQuote(patent.getQuote() + element.text() + " ");
						}
					}
					patent.setQuote(patent.getQuote().trim());
				}
			}
			
			Result textSegResult = ToAnalysis.parse(text);
			Result tittleSegResult = ToAnalysis.parse(title);
			// 正文的分词 没个词之间用空格隔开
			String fteseg = "";
			// 标题的分词 每个词用空格隔开
			String ftiseg = "";

			Iterator<Term> teIterator = textSegResult.iterator();
			while (teIterator.hasNext()) {
				Term term = teIterator.next();
				fteseg += term.getName() + " ";
			}
			// 最后会多一个空格 需要去掉
			if(fteseg.length()!=0)
			fteseg = fteseg.substring(0, fteseg.length() - 1);
			patent.setPatentTextSeg(fteseg);

			Iterator<Term> tiIterator = tittleSegResult.iterator();
			while (tiIterator.hasNext()) {
				Term term = tiIterator.next();
				ftiseg += term.getName() + " ";
			}
			// 最后会多一个空格 需要去掉
			ftiseg = ftiseg.substring(0, ftiseg.length() - 1);
			patent.setTitleSeg(ftiseg);

			// 获取关键字及其权值
			KeyWordComputer kComputer = new KeyWordComputer(20);
			List<Keyword> kList = kComputer.computeArticleTfidf(title, text);
			String keyString = "";
			// 关键字与权值之间用,分割 关键字之间用 分割
			for (Keyword keyword : kList) {
				// System.out.println(keyword.getName()+keyword.getScore());
				keyString += keyword.getName() + "," + keyword.getScore() + " ";
			}
			if(keyString.length()!=0)
			keyString = keyString.substring(0, keyString.length() - 1);
			patent.setKeyWords(keyString);
			System.out.println(i);
			if (!JDBCconnector.save(patent)){
				FileTools.SaveError("src/ErrorText.txt", patent.getPatentId());
			    }
			else {
				if (patent.getPatentIpc()!= null) {
					String ipcs[] = patent.getPatentIpc().split(" ");
					for (String string : ipcs) {
						String sql = "insert into IpcToPatent values('" + patent.getPatentId() + "','"
								+ string.substring(0, 4) + "')";
						JDBCconnector.SaveSql(sql);
					}
				}
			}

		}
	}

	public static String ToUtf(String s) {
		try {
			// return new String(s.getBytes(), "utf-8");
			return s;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

}
