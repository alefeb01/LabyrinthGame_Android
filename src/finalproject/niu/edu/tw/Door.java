package finalproject.niu.edu.tw;

import java.util.ArrayList;
import java.util.List;

//import android.util.Log;

public class Door {
	protected int tabx = 0; 
	public enum Type {GATE,PORTAL}
	public static final int LENGHT_MAX = 4;
	private int lenght = 2;
	private List<Bloc> blocs = null;
	private double centerX = 0;
	private double centerY = 0;
	private int num= 0;
	private int Entry = 0;
	
	public Door(List<Bloc> bin, int num) {
		this.blocs =new ArrayList<Bloc>();
		this.setBlocs(bin);
		this.setNum(num);
		this.setLenght(bin.size());
		for(Bloc b : bin)
		{
			setCenterX(getCenterX() + b.getRectangle().centerX());
			setCenterY(getCenterY() + b.getRectangle().centerY());
		}
		setCenterX(getCenterX() / lenght);
		setCenterY(getCenterY() / lenght);
		this.setEntry(bin.get(lenght-1).getDoorside());
    }
	
	public Door(Bloc[] bin, int num) {
		this.blocs =new ArrayList<Bloc>();
		this.setBlocs(bin);
		this.setNum(num);
		this.setLenght(this.getBlocs().size());
		for(Bloc b : this.getBlocs())
		{
			setCenterX(getCenterX() + b.getRectangle().centerX());
			setCenterY(getCenterY() + b.getRectangle().centerY());
		}
		setCenterX(getCenterX() / lenght);
		setCenterY(getCenterY() / lenght);
		this.setEntry(bin[bin[0].lenght(bin)-1].getDoorside());
    }

	public Door() {
		// TODO Auto-generated constructor stub
		this.blocs =new ArrayList<Bloc>();
		this.setNum(-1);
	}

	private void setBlocs(Bloc[] bin) {
		
		for( tabx = 0; tabx < bin[0].lenght(bin);tabx++){
			this.blocs.add(bin[tabx]);
		}

	}

	public List<Bloc> getBlocs() {
		return blocs;
	}

	public void setBlocs(List<Bloc> bin) {
		this.blocs = bin;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getLenght() {
		return lenght;
	}

	public void setLenght(int lenght) {
		this.lenght = lenght;
	}

	public double getCenterX() {
		return centerX;
	}

	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	public float[] getDoorFront()
    {
    	float f[] = {0,0} ;
    	if(this.getEntry() == LabyrinthEngine.REBOUND_R)
    	{
    		f[0] = (float) (this.getCenterX() + 2*Ball.R);
    		f[1] = (float) this.getCenterY();
    	}else if(this.getEntry() == LabyrinthEngine.REBOUND_L){
    		f[0] = (float) (this.getCenterX() - 2*Ball.R);
    		f[1] = (float) this.getCenterY();
    	}else if(this.getEntry() == LabyrinthEngine.REBOUND_B){	
    		f[0] = (float) this.getCenterX();
    		f[1] = (float) (this.getCenterY() + 2*Ball.R);
    	}else if(this.getEntry() == LabyrinthEngine.REBOUND_T)
    	{
    		f[0] = (float) this.getCenterX();
    		f[1] = (float) (this.getCenterY() - 2*Ball.R);
    	}
		return f;
    }

	public int getEntry() {
		return Entry;
	}

	public void setEntry(int entry) {
		Entry = entry;
	}

	public void update() {

		this.setLenght(this.getBlocs().size());
		for(Bloc b : this.getBlocs())
		{
			setCenterX(getCenterX() + b.getRectangle().centerX());
			setCenterY(getCenterY() + b.getRectangle().centerY());
		}
		setCenterX(getCenterX() / lenght);
		setCenterY(getCenterY() / lenght);
	}
	
	public int setEntry() {

		if(Math.abs(this.getBlocs().get(0).getRectangle().centerX() - this.getBlocs().get(lenght-1).getRectangle().centerX()) == 0)
		{
			setEntry(LabyrinthEngine.REBOUND_R);
			return LabyrinthEngine.REBOUND_R;
		}else{
			setEntry(LabyrinthEngine.REBOUND_T);
			return LabyrinthEngine.REBOUND_T;
		}
	}
	
	
}
