package readIpc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.relation.RoleUnresolvedList;

import tools.FileTools;

public class Recoder {
	public static void main(String[] args) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = "";
		try{
			for(int i=0;i<8;i++){
				char temp = (char) ('A'+i);
				System.out.println(temp);
			fis = new FileInputStream("src/IpcInfo/"+temp+".txt");
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
			  ReadSPart(line,temp);
		      ReadBClass(line,temp);
			  ReadSClass(line,temp);
		    }
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//获取分部：部字母开头，接着俩数字（分部），接着是中文或者分号的   匹配内容
	public static void ReadSPart(String line,char ipcClass){
		String cText = "^"+ipcClass+"\\d{2}[\u4E00-\u9FA5&；\\s]+";
		Pattern pattern = Pattern.compile(cText);
		Matcher matcher = pattern.matcher(line);
		while(matcher.find())
		  System.out.println(matcher.group());
	}
	//获取大类：部字母开头，接着俩数字（分部），接着一个字母（大类），接着是中文或者分号的   匹配内容
	public static void  ReadBClass(String line,char ipcClass){
		String cText = "^"+ipcClass+"\\d{2}[A-Z][\u4E00-\u9FA5&；]+";
		Pattern pattern = Pattern.compile(cText);
		Matcher matcher = pattern.matcher(line);
		int i=0;
		while(matcher.find()){
		  String result = matcher.group();
		  System.out.println(result+"     属于"+result.substring(0, 3));
		}
	}
	//获取小类:部字母开头，接着俩数字（分部），接着一个字母（大类），接着是中文或者分号的   匹配内容
	public static void ReadSClass(String line,char ipcClass){
		String cText = "^"+ipcClass+"\\d{2}[A-Z]\\s[0-9&/]{4}[\u4E00-\u9FA5；A-Z0-9·\\s-/]+";
		Pattern pattern = Pattern.compile(cText);
		Matcher matcher = pattern.matcher(line);
		int i=0;
		while(matcher.find()){
		  String result = matcher.group();
		  result = result.replaceAll("·", "");
		  System.out.println(result+"     属于"+result.substring(0, 4));
		}
	}
	
	
	
	
}
