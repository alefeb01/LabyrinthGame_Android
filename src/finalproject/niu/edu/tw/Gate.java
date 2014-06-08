package finalproject.niu.edu.tw;


import android.graphics.Color;
//import android.util.Log;


public class Gate {

	
	private Bloc trigger;
	private Door Gate;
	private int num;
	private boolean active;
	protected int tabx = 0; 
	public Gate(Door gate,Bloc btrigger) {
		
		this.setTrigger(btrigger);
		this.setGate(gate);
		this.num = btrigger.getNum();
		this.setActive(true);
    }

	public Bloc getTrigger() {
		return trigger;
	}

	public void setTrigger(Bloc trigger) {
		this.trigger = trigger;
	}

	public Door getGate() {
		return Gate;
	}

	public void setGate(Door gate2) {
		Gate = gate2;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void openGate(){
		this.setActive(false);

		if(this.getGate().setEntry()== LabyrinthEngine.REBOUND_R  ){
			this.getGate().getBlocs().get(0).setRebound_Top(true);
			this.getGate().getBlocs().get(this.getGate().getLenght()-1).setRebound_Bottom(true);
		}else if(this.getGate().getEntry()== LabyrinthEngine.REBOUND_T)
		{
			this.getGate().getBlocs().get(0).setRebound_Right(true);
			this.getGate().getBlocs().get(this.getGate().getLenght()-1).setRebound_Left(true);
		}
		for(tabx = 1; tabx<this.getGate().getLenght()-1; tabx++ )
		{
			this.getGate().getBlocs().get(tabx).setCouleur(Color.GRAY);
			this.getGate().getBlocs().get(tabx).setSolid(false);
		}
		this.getTrigger().setCouleur(Color.CYAN);
		
	}
}
