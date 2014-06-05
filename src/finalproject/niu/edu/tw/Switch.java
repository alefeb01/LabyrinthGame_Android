package finalproject.niu.edu.tw;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;



public class Switch {
	
	private List<Bloc> Switches = null;
	private int Active= 0;
	public List<Bloc> getSwitches() {
		return Switches;
	}

	public void setSwitches(List<Bloc> blocs) {
		this.Switches = blocs;
	}
	
	
	Switch(){
		setSwitches(new ArrayList<Bloc>());
	}

	public int getActive() {
		return Active;
	}

	public void ChangeActive(Ball mBall) {
		mBall.changeSwitchActive();
		getSwitches().get(Active).setCouleur(Color.CYAN);
		Active = (int) Math.round(Math.random()*(getSwitches().size()-1));
		getSwitches().get(Active).setCouleur(Color.YELLOW);
		
	}

	public void setActive(int active) {
		// TODO Auto-generated method stub
		this.Active = active;
	} 
	
}
