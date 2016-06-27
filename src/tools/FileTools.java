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
	
	//大文件读 不大好使 最好用按字节
//	public static String ReadFile(String path) {
//		File file=new File(path);
//        BufferedReader reader=null;
//        String result = "";
//        try{
//        	new BufferedReader(new FileReader(file),5*1024*1024);
//        	String temp;
//        	while((temp=reader.readLine())!=null)
//        	{
//        		result+=temp;
//           }
//            reader.close();
//        }catch(Exception e){
//        	e.printStackTrace();
//        }finally{
//            if(reader!=null)
//          {
//                try{
//                       reader.close();
//                }catch(IOException e)
//
//              {
//              e.printStackTrace();
//
//             }
//          }
//        }
//        return result;
//
//	}

	public static void AppendTextOnFile(String path, String toAppend) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(path, true);
			writer.write(toAppend);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	 public static void main(String[] args) {
	// AppendTextOnFile("src/result.txt", "");
	 System.out.println(ReadFile("src/result.txt"));
	 }

}
