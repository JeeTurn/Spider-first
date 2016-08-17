package readIpc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.relation.RoleUnresolvedList;

import tools.FileTools;
import tools.JDBCconnector;

public class Recoder {
	public static void main(String[] args) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = "";
		try{
			for(int i=0;i<8;i++){
				char temp = (char) ('A'+i);
				//System.out.println(temp);
			fis = new FileInputStream("src/IpcInfo/"+temp+".txt");
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			boolean firstLine = true;
			while ((line = br.readLine()) != null) {
				if(firstLine)
			  //ReadFirst(line);
			  //ReadSPart(line,temp);
		      //ReadBClass(line,temp);
			  //ReadSClass(line,temp);
				firstLine = false;
		    }
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//获取部 每个文档第一行 字母--含义
	public static void ReadFirst(String line){
		String s[] = line.split("部——");
		JDBCconnector.SaveSql("insert into IpcClass(IpcText,IpcMean,Parent) values('"+s[0]+"','"+s[1]+"','"+0+"')");
	}
	
	
	//获取分部：部字母开头，接着俩数字（分部），接着是中文或者分号的   匹配内容
	public static void ReadSPart(String line,char ipcClass){
		String cText = "^"+ipcClass+"\\d{2}[\u4E00-\u9FA5&；\\s]+";
		Pattern pattern = Pattern.compile(cText);
		Matcher matcher = pattern.matcher(line);
		while(matcher.find()){
		  String result = matcher.group().replaceAll(" ", "");
		  JDBCconnector.SaveSql("insert into IpcClass(IpcText,IpcMean,Parent) values('"+result.substring(0, 3)+"','"+result.substring(3,result.length())+"','"+result.substring(0,1)+"')");
		}
	}
	//获取大类：部字母开头，接着俩数字（分部），接着一个字母（大类），接着是中文或者分号的   匹配内容
	public static void ReadBClass(String line,char ipcClass){
		String cText = "^"+ipcClass+"\\d{2}[A-Z]\\s*[\u4E00-\u9FA5&；]+";
		Pattern pattern = Pattern.compile(cText);
		Matcher matcher = pattern.matcher(line);
		int i=0;
		while(matcher.find()){
		  String result = matcher.group().replaceAll(" ", "");
		  JDBCconnector.SaveSql("insert into IpcClass(IpcText,IpcMean,Parent) values('"+result.substring(0, 4)+"','"+result.substring(4,result.length())+"','"+result.substring(0,3)+"')");
		}
	}
	//获取小类:部字母开头，接着俩数字（分部），接着一个字母（大类）接着数字 ／ 数字 ，接着是中文或者分号的   匹配内容 
	//Todo 小类不一定是4位 尝试用· 或者第一个汉字
//	public static void ReadSClass(String line,char ipcClass){
//		String cText = "^"+ipcClass+"\\d{2}[A-Z]\\s\\d+/\\d+[\u4E00-\u9FA5；A-Z0-9·\\s-/]+";
//		Pattern pattern = Pattern.compile(cText);
//		Matcher matcher = pattern.matcher(line);
//		int i=0;
//		while(matcher.find()){
//		  String result = matcher.group();
//		  result = result.replaceAll("·", "");
//		  result = result.replaceAll(" ", "");
//		  Pattern ipcPat = Pattern.compile("^"+ipcClass+"\\d{2}[A-Z]\\d+/\\d+");
//		  Matcher ipcMat = ipcPat.matcher(result);
//		  if(ipcMat.find()){
//			  String iResult = ipcMat.group();
//			  JDBCconnector.SaveSql("insert into IpcClass(IpcText,IpcMean,Parent) values('"+iResult+"','"+result.substring(iResult.length(),result.length())+"','"+result.substring(0,4)+"')");
//		  }
//		  
//		  //System.out.println(result+"     属于"+result.substring(0, 4));
//		}
//	}
	
	
	
	
}
