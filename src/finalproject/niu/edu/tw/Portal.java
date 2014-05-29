package finalproject.niu.edu.tw;
import java.util.List;

import finalproject.niu.edu.tw.Bloc.Type;
import android.graphics.RectF;
import android.util.Log;


public class Portal {
	private Bloc b_in;
	private Bloc b_out;
	private int num;
	
	public Portal(Bloc bin, Bloc bout) {

			this.setB_in(bin);
			this.setB_out(bout);
			this.num = bin.getNum();
    }

	public Bloc getB_in() {
		return b_in;
	}

	public void setB_in(Bloc b_in) {
		this.b_in = b_in;
	}

	public Bloc getB_out() {
		return b_out;
	}

	public void setB_out(Bloc b_out) {
		this.b_out = b_out;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
//	public void useStargate(bloc b_out){}
}
