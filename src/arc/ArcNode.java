package arc;

import java.util.LinkedList;
import java.util.List;

public class ArcNode {
	private String data;
	private LinkedList<ArcNode> linkList;
	public ArcNode(String data){
		this.data = data;
		linkList = new LinkedList<ArcNode>();
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public LinkedList<ArcNode> getLinkList() {
		return linkList;
	}
	public void setLinkList(LinkedList<ArcNode> linkList) {
		this.linkList = linkList;
	}
}
