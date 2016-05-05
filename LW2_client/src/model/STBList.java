package model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class STBList {
	
	private List<STBInfo> list;
	
	public STBList() {
		list = new ArrayList<STBInfo>();
	}

	public List<STBInfo> getList() {
		return list;
	}

	public void setList(List<STBInfo> list) {
		this.list = list;
	}
	
	public void addSTB(STBInfo s) {
		list.add(s);
	}

}
