package tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CosCounter {
	public static float CountCos(String key1,String key2){
		//      <单词,[专利1的频率,专利2的频率]>
		Map<String,float[]> vectorSpace = new HashMap<>();
		float[] itemCounts = null;
		//按空格分隔出每一项
		String ss1[] = key1.split(" ");
		String ss2[] = key2.split(" ");
		//直接录入专利1的频率 对应的专利2暂时设0
		for (String string : ss1) {
			String key = string.split(",")[0];
			float value = Float.parseFloat(string.split(",")[1]);
			itemCounts = new float[]{value, 0.00f};
			vectorSpace.put(key, itemCounts);
		}
		
		//开始录入专利2的频率 存在的key更改频率 不存在的key专利1设0.00f
		for (String string : ss2) {
			String key = string.split(",")[0];
			float value = Float.parseFloat(string.split(",")[1]);
			if(vectorSpace.containsKey(key)){
				vectorSpace.get(key)[1]= value;
			}else{
				itemCounts = new float[]{0.00f, value};
				vectorSpace.put(key, itemCounts);
			}
		}
		
		//计算余弦相似度
		float vector1Modulo =0.00f; //向量1的模
		float vector2Modulo =0.00f;
		float vectorMulti = 0.00f;  //内积
		
		Iterator iterator = vectorSpace.entrySet().iterator();
		while(iterator.hasNext()){
			 Map.Entry entry = (Map.Entry)iterator.next(); 
             float temp[] = (float[])entry.getValue(); 
             vector1Modulo += temp[0]*temp[0];
             vector2Modulo += temp[1]*temp[1];
             vectorMulti += temp[0]*temp[1];
		}
		vector1Modulo = (float) Math.sqrt(vector1Modulo); 
        vector2Modulo = (float)Math.sqrt(vector2Modulo); 
		
		float simi = vectorMulti/(vector1Modulo*vector2Modulo);
		return simi;
	}
	
	public static void main(String[] args) {
			String paId = "";
			List<String> list = JDBCconnector.ExecuteQuery("select PatentId,Keywords from patent where PatentId ='"+paId+"'");
			Map<String,Float> map = new HashMap();
			
			for (String s : list) {
				//CountCos(list1.get(0), s);
				
			}
		}
		//System.out.println(CountCos(string, string));
	
}
