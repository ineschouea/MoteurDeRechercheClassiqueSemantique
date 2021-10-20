package web;

import java.util.ArrayList;
import java.util.Vector;

import metier.Document;

public class SearchBeans {

	public Document doc=new Document();
	private ArrayList<Document> list=new ArrayList<Document>();
	
	
	public void addDoc(Document doc) {
		list.add(doc) ;
	}
	public ArrayList<Document> getList() {
		return list;
	}
	public void setList(ArrayList arrayList) {
		this.list = arrayList;
	}
	
}
