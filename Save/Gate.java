package finalproject.niu.edu.tw;

import java.util.List;

import finalproject.niu.edu.tw.Bloc.Type;

public class Gate {

	
	private Bloc trigger;
	private Bloc Gate;
	private int num;
	private boolean active;
	
	public Gate(Bloc bgate,Bloc btrigger) {
		
		this.setTrigger(btrigger);
		this.setGate(bgate);
		this.num = bgate.getNum();
		this.setActive(true);
    }

	public Bloc getTrigger() {
		return trigger;
	}

	public void setTrigger(Bloc trigger) {
		this.trigger = trigger;
	}

	public Bloc getGate() {
		return Gate;
	}

	public void setGate(Bloc gate) {
		Gate = gate;
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
}
