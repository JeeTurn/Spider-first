package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class ReadText {
	public static String readFileByChars(String fileName) {
		String result = "";
		File file = new File(fileName);
		Reader reader = null;
		try {
			// 一次读一个字符
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				// 对于windows下，\r\n这两个字符在一起时，表示一个换行。
				// 但如果这两个字符分开显示时，会换两次行。
				// 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
				if (((char) tempchar) != '\r') {
					result += ((char) tempchar);
				}
			}
			reader.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
