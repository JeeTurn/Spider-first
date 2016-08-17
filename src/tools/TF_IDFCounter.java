package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import po.KeyWord;

public class TF_IDFCounter {
	// 传入需要计算的词语 和 正文的分词结果 标题的分词结果 返回该词的tf值
	// 标题的权重增加 标题中出现的词相当于正文中出现5次
	// 计算公式：单词出现次数/总词数
	public double CountTf(String word, String content, String title) {
		String[] conSegs = content.split(" ");
		String[] titleSegs = title.split(" ");
		double toCount = conSegs.length + titleSegs.length * 5;
		double woCount = 0;
		for (String string : titleSegs) {
			if (word.equals(string)) {
				woCount += 5;
			}
		}
		for (String string : conSegs) {
			if (word.equals(string))
				woCount++;
		}
		return woCount / toCount;
	}

	// 计算idf 直接利用数据库全文索引 统计词数
	// 传入需要计算的词 和 总专利数
	// 计算公式 log(总文档数/在多少个文档出现过+1)
	public double CountIdf(String word, float toCount) {
		String sql = "select count(*) from Patent where match (PatentTextSeg,TitleSeg) against ('" + word + "')";
		int sCount = JDBCconnector.ExecuteQueryCount(sql);
		double result = Math.log(toCount) / Math.log(sCount);
		return result;
	}

	public void KeyWordCompute() {
		int count =0;
		int toCount = 0;
		List<String> stopWords = ReadStopWords();
		String sql = "select count(*) from Patent";
		toCount = JDBCconnector.ExecuteQueryCount(sql);
		String sql1 = "select PatentTextSeg,TitleSeg,PatentId from Patent";
		List<List> lList = JDBCconnector.ExecuteQuerySeg(sql1);
		//计算过idf的词语保存在map中 不再进行查询
		Map<String, Double> savedIdfMap = new HashMap<>();
		
		for (List list : lList) {
		//List list = lList.get(0);
			Map<String, KeyWord> resultMap = new HashMap<>();
			String patentSeg = (String) list.get(0);
			String titleSeg = (String) list.get(1);
			String pId = (String)list.get(2);
			String[] words = (patentSeg + " " + titleSeg).split(" ");
			for (String word : words) {
				// 该词不再停用词中 也没计算过
				word = word.trim();
				if (NotIn(word, stopWords) && resultMap.get(word) == null&&word.length()!=0) {
					double tf = CountTf(word, patentSeg, titleSeg);
					double idf;
					if(savedIdfMap.get(word)==null){
					   idf = CountIdf(word, toCount);
					   savedIdfMap.put(word, idf);
					}
					else
						idf = savedIdfMap.get(word);
					double result = tf / idf;
					result *=1000;
					if(result>0.001){
					KeyWord keyWord = new KeyWord(word, result);
					resultMap.put(word, keyWord);}
				}
			}
			TreeSet<KeyWord> treeSet = new TreeSet<KeyWord>(resultMap.values());
			ArrayList<KeyWord> arrayList = new ArrayList<KeyWord>(treeSet);
			List<KeyWord> tlist = null;
			//取20个关键字
			if (treeSet.size() > 20) {
				tlist = (List<KeyWord>) arrayList.subList(0, 20);
			}
			else
				tlist = (List<KeyWord>)arrayList;
			//拼接成可识别的字符串 保存入数据库
			StringBuffer kBuffer = new StringBuffer();
			for (KeyWord keyWord : tlist) {
				kBuffer.append(keyWord.getWord()+","+keyWord.getCount()+" ");
			}
			String keyResult = kBuffer.toString().trim();
			String sql2 = "update Patent set Keywords = '"+keyResult+"' where PatentId = '"+pId+"'";
			JDBCconnector.ExecuteUpdate(sql2);
			System.out.println(count++);
		}

	}

	public static void main(String[] args) {
		TF_IDFCounter fCounter = new TF_IDFCounter();
	    fCounter.KeyWordCompute();	
	}

	private List<String> ReadStopWords() {
		FileReader reader = null;
		BufferedReader br = null;
		List<String> wlList = new ArrayList<>();
		try {
			reader = new FileReader("src/library/StopWord.txt");
			br = new BufferedReader(reader);

			String str = null;

			while ((str = br.readLine()) != null) {

				wlList.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		HashSet<String> hSet = new HashSet<>(wlList);
		wlList.clear();
		wlList.addAll(hSet);
		return wlList;
	}

	private boolean NotIn(String word, List<String> words) {
		for (String string : words) {
			if (word.equals(string))
				return false;
		}
		return true;
	}

}
