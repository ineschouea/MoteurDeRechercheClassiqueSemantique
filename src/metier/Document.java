package metier;


public class Document    {
	private long id;
	private String nom;
	private String path;
	private String url;
	private float score;
	private String desc;
	
	public Document(String nom) {
		
		this.nom = nom;
	}

	
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getScore() {
		return score;
	}

	
	public void setId(long id) {
		this.id = id;
	}

	public Document(long id, String nom, String path, String url, float score) {
		super();
		this.id = id;
		this.nom = nom;
		this.path = path;
		this.url = url;
		this.score = score;
	}


	public long getId() {
		return id;
	}



	public void setScore(float score) {
		this.score = score;
	}




	public Document(String nom, String path, String url, float score) {
		super();
		this.nom = nom;
		this.path = path;
		this.url = url;
		this.score = score;
	}




	public Document(String nom, String path) {
		super();
		this.nom = nom;
		this.path = path;
	}

	
	

	public Document(String nom, String path, String url) {
		super();
		this.nom = nom;
		this.path = path;
		this.url = url;
	}


	public Document() {
		super();
		// TODO Auto-generated constructor stub
	}
	


	

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/*  public static Comparator<Document> ComparatorScore = new Comparator<Document>() {
		     
	        
	        public int compare(Document d1, Document d2) {
	            return (int) (d1.getScore() - d2.getScore());
	        }
	    };*/

	
}