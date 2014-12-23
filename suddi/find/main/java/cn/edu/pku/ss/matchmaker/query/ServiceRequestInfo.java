package cn.edu.pku.ss.matchmaker.query;

public class ServiceRequestInfo {
	private String owlsURI;
	private String owlsContent;
	private String grlURI;
	private String grlContent;
	
	public ServiceRequestInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public ServiceRequestInfo(String owlsURI, String owlsContent,
			String grlURI, String grlContent) {
		this.owlsURI = owlsURI;
		this.owlsContent = owlsContent;
		this.grlURI = grlURI;
		this.grlContent = grlContent;
	}
	
	public String getOwlsURI() {
		return owlsURI;
	}
	public void setOwlsURI(String owlsURI) {
		this.owlsURI = owlsURI;
	}
	public String getOwlsContent() {
		return owlsContent;
	}
	public void setOwlsContent(String owlsContent) {
		this.owlsContent = owlsContent;
	}
	public String getGrlURI() {
		return grlURI;
	}
	public void setGrlURI(String grlURI) {
		this.grlURI = grlURI;
	}
	public String getGrlContent() {
		return grlContent;
	}
	public void setGrlContent(String grlContent) {
		this.grlContent = grlContent;
	}
}
