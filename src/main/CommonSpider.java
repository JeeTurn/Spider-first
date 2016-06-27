package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.conn.params.ConnRouteParams;

public class CommonSpider {
	public static void main(String[] args) {
		try{
		HttpClient client = new HttpClient();
	    
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"gb2312");
		String url ="https://www.google.com/patents/CN102541733B?cl=zh&hl=zh-CN";
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gb2312");
		int statusCode = client.executeMethod(getMethod);
		if (statusCode == HttpStatus.SC_OK) {
			byte[] cres = getMethod.getResponseBody();
			String result = new String(cres,"gb2312");
			System.out.println(result);
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static boolean matcher(String s, String pattern) {
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
		Matcher matcher = p.matcher(s);
		if (matcher.find()) {

			return true;

		} else {

			return false;

		}

	}

	public static String getContentCharset(HttpResponse response) {

		String charset = "ISO_8859-1";

		Header header = response.getEntity().getContentType();
		if (header != null) {
			String s = header.getValue();
			if (matcher(s, "(charset)\\s?=\\s?(utf-?8)")) {

				charset = "utf-8";

			} else if (matcher(s, "(charset)\\s?=\\s?(gbk)")) {

				charset = "gbk";

			} else if (matcher(s, "(charset)\\s?=\\s?(gb2312)")) {

				charset = "gb2312";

			}

		}

		return charset;

	}
	
	
}
