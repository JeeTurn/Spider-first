package po;
//一个不经常进行插入和删除的表，不需要进行过复杂的设计 只需要考虑优化查询操作即可
public class Patent {
	private String patentId;
	private String applyId;
	private String title;
	private String proposer;
	private String author;
	private String patentAbstract;
	private String claim;
	private String patentText;
	private String TitleSeg;
	private String PatentTextSeg;
	private String PatentIpc;
	private String KeyWords;
	private String Quote;
	private String BeQuote;
	public String getQuote() {
		return Quote;
	}
	public void setQuote(String quote) {
		Quote = quote;
	}
	public String getBeQuote() {
		return BeQuote;
	}
	public void setBeQuote(String beQuote) {
		BeQuote = beQuote;
	}
	public String getKeyWords() {
		return KeyWords;
	}
	public void setKeyWords(String keyWords) {
		KeyWords = keyWords;
	}
	public String getPatentId() {
		return patentId;
	}
	public void setPatentId(String patentId) {
		this.patentId = patentId;
	}
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProposer() {
		return proposer;
	}
	public void setProposer(String proposer) {
		this.proposer = proposer;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPatentAbstract() {
		return patentAbstract;
	}
	public void setPatentAbstract(String patentAbstract) {
		this.patentAbstract = patentAbstract;
	}
	public String getClaim() {
		return claim;
	}
	public void setClaim(String claim) {
		this.claim = claim;
	}
	public String getPatentText() {
		return patentText;
	}
	public void setPatentText(String patentText) {
		this.patentText = patentText;
	}
	public String getTitleSeg() {
		return TitleSeg;
	}
	public void setTitleSeg(String titleSeg) {
		TitleSeg = titleSeg;
	}
	public String getPatentTextSeg() {
		return PatentTextSeg;
	}
	public void setPatentTextSeg(String patentTextSeg) {
		PatentTextSeg = patentTextSeg;
	}
	public String getPatentIpc() {
		return PatentIpc;
	}
	public void setPatentIpc(String patentIpc) {
		PatentIpc = patentIpc;
	}
}
