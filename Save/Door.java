package finalproject.niu.edu.tw;

import java.util.List;

public class Door {
	
	public enum Type {GATE,PORTAL}

	private int lenght = 2;
	private List<Bloc> blocs = null;
	private Type type = Type.GATE;
	
	public Door(List<Bloc> lb, Door.Type type,int num) {
		
		this.setBlocs(lb);
		this.setType(type);
    }

	public List<Bloc> getBlocs() {
		return blocs;
	}

	public void setBlocs(List<Bloc> blocs) {
		this.blocs = blocs;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
