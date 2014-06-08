package finalproject.niu.edu.tw;

import java.util.ArrayList;
import java.util.List;

import finalproject.niu.edu.tw.Bloc.Type;

public class Wheel {
	private List<Bloc> Spokes;
	@SuppressWarnings("unused")
	private static final int SIZE_SMALL = 3;
	@SuppressWarnings("unused")
	private static final int SIZE_NORMAL = 5;
	@SuppressWarnings("unused")
	private static final int SIZE_LARGE = 7;
	private static final double ANG_VELOCITY = 4;//3s/1
	private static final double PI = 3.14;
	private static final double THETA = 20 *0.001* PI /ANG_VELOCITY;
	private static final double COSTHETA = Math.cos(THETA);
	private static final double SINTHETA = Math.sin(THETA);
	protected int tabx = 0; 
	private int size = 0;
	private float centerX = 0;
	private float centerY = 0;
	public List<Bloc> getSpokes() {
		return Spokes;
	}

	public void setSpokes(List<Bloc> spokes) {
		Spokes = spokes;
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	Wheel(int pX, int pY, int psize){
		setSpokes(new ArrayList<Bloc>());
		setSize(psize);
		getSpokes().add(new Bloc(Type.HOLE2,pX,pY,LabyrinthEngine.REBOUND_0));
		centerX = getSpokes().get(0).getRectangle().centerX();
		centerY = getSpokes().get(0).getRectangle().centerY();
		for(tabx = 1; tabx <= Math.floor((size-1)/2); tabx++ ){
			getSpokes().add(new Bloc(Type.HOLE2,pX-2*tabx,pY,LabyrinthEngine.REBOUND_0));
			getSpokes().add(new Bloc(Type.HOLE2,pX+2*tabx,pY,LabyrinthEngine.REBOUND_0));
			getSpokes().add(new Bloc(Type.HOLE2,pX,pY-2*tabx,LabyrinthEngine.REBOUND_0));
			getSpokes().add(new Bloc(Type.HOLE2,pX,pY+2*tabx,LabyrinthEngine.REBOUND_0));
		}
	}
	
	public void run_WheelMvmt(){
		for(Bloc b : getSpokes()){
			b.getRectangle().set((float)(COSTHETA*(b.getRectangle().centerX() - centerX) - SINTHETA*(b.getRectangle().centerY()-centerY) + centerX - Ball.R),
								 (float)(SINTHETA*(b.getRectangle().centerX() - centerX) + COSTHETA*(b.getRectangle().centerY()-centerY) + centerY - Ball.R),
								 (float)(COSTHETA*(b.getRectangle().centerX() - centerX) - SINTHETA*(b.getRectangle().centerY()-centerY) + centerX + Ball.R),
								 (float)(SINTHETA*(b.getRectangle().centerX() - centerX) + COSTHETA*(b.getRectangle().centerY()-centerY) + centerY + Ball.R)); 
		}
	}


	public float getCenterX() {
		return centerX;
	}

	public void setCenterX(float centerX) {
		this.centerX = centerX;
	}

	public float getCenterY() {
		return centerY;
	}

	public void setCenterY(float centerY) {
		this.centerY = centerY;
	}
}
