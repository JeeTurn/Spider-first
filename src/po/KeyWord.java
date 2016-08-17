package po;

public class KeyWord implements Comparable<KeyWord>{
	private String word;
	private double count;
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	@Override
	public int compareTo(KeyWord o) {
		// TODO Auto-generated method stub
		if (this.count < o.count) {
			return 1;
		} else {
			return -1;
		}
	}
	public KeyWord(String word, double count) {
		super();
		this.word = word;
		this.count = count;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return word+"  "+count;
	}
}
