package finalproject.niu.edu.tw;
import java.util.List;

import finalproject.niu.edu.tw.Bloc.Type;
import android.graphics.RectF;
import android.util.Log;


public class Portal {
	private Door d_in;
	private Door d_out;
	private int num;
	
	public Portal(Bloc[] bin, Bloc[] bout) {
			this.setD_in(new Door(bin,bin[0].getNum()));
			this.setD_out(new Door(bout,bin[0].getNum()));
			this.num = bin[0].getNum();
    }



	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Door getD_out() {
		return d_out;
	}

	public void setD_out(Door d_out) {
		this.d_out = d_out;
	}



	public Door getD_in() {
		return d_in;
	}



	public void setD_in(Door d_in) {
		this.d_in = d_in;
	}

	
//	public void useStargate(bloc b_out){}
}
