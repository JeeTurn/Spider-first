package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class FileTools {
	private static String tempString="";
	private static int counter=0;
	public static void SavePages(String path, String result) {
		File file = null;
		BufferedWriter writer = null;
		FileOutputStream fos = null;
		try {
			file = new File(path);
			file.createNewFile();
			fos = new FileOutputStream(file);
			writer = new BufferedWriter(new OutputStreamWriter(fos));
			writer.write(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public static void SaveError(String path , String page){
		File file = null;
		BufferedWriter writer = null;
		FileOutputStream fos = null;
		try {
			file = new File(path);
			file.createNewFile();
			fos = new FileOutputStream(file,true);
			writer = new BufferedWriter(new OutputStreamWriter(fos));
			writer.write(page+" ");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static String ReadFile(String path) {
		String result = "";
		File file = new File(path);
		BufferedReader reader = null;
		try {
			// 一次读一个字符
			reader = new BufferedReader(new FileReader(file),5*1024*1024);
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				if (((char) tempchar) != '\r') {
					result += (char) tempchar;
				}
			}
			reader.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String GetTempString(){
		return tempString;
	}

	public static void AppendTextOnVisitFile(String toAppend) {
		try {
			// 向tempString追加字符串 没加一次counter++ 如果counter=50时 把当前字符串保存入库
			//如果tempString为空 则读取文件
			if(tempString.length()==0){
				tempString = ReadFile("src/visited.txt");
			}
			
			counter++;
			if(counter<50){
				tempString = tempString+toAppend;
			}
			else{
				SavePages("src/visited.txt", tempString);
			}
			
			
//			FileWriter writer = new FileWriter(path, true);
//			writer.write(toAppend);
//			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	 public static void main(String[] args) {
	// AppendTextOnFile("src/result.txt", "");
	 System.out.println(ReadFile("src/result.txt"));
	 }

}
