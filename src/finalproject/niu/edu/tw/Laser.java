package finalproject.niu.edu.tw;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import finalproject.niu.edu.tw.Bloc.Type;

public class Laser {
	
	private List<Bloc> Lasers = null;
	private boolean Active = false;
	public List<Bloc> getLasers() {
		return Lasers;
	}

	public void setLasers(List<Bloc> lasers) {
		Lasers = lasers;
	}
	
	Laser(){
		setLasers(new ArrayList<Bloc>());
	}
	
	public void run_ChangeStatus(){
		if(Active){
			for(Bloc b : Lasers){
				b.changeType(Type.NEUTRAL);
			}
			Active = ! Active;
		}else{
			for(Bloc b : Lasers){
				b.changeType(Type.HOLE);
			}
			Active = ! Active;
		}
	}
}
