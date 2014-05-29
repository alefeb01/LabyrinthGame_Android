package finalproject.niu.edu.tw;
import android.graphics.Color;
import android.graphics.RectF;


public class Bloc {

	
    enum  Type { BORDURE, START, ARRIVE, HOLE, NEUTRAL, PORTAL,TRIGGER,GATE};
    
    private float SIZE = Ball.R * 2;
    private boolean Rebound_Top = false;
    private boolean Rebound_Bottom = false;
    private boolean Rebound_Left = false;
    private boolean Rebound_Right = false;
    private int num = 0;
    private Type mType = null;
    private RectF mRectangle = null;
  
    // Color
    private int mCouleur = 0;
    public int getCouleur() {
        return mCouleur;
    }
    
    public void setCouleur(int c){
    	mCouleur = c;
    }
    
    public Type getType() {
        return mType;
    }

    public RectF getRectangle() {
        return mRectangle;
    }
    
    public Bloc(Type pType, int pX, int pY,byte rebound) {
        this.mType = pType;
        
        switch(mType){
        
	        case BORDURE:
	        	this.mCouleur = Color.BLACK;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE); 
	        	break;
	        case HOLE:
	        	this.mCouleur = Color.GREEN;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE); 
	        	break;
	        case START:
	        	this.mCouleur = Color.WHITE;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE); 
	        	break;
	        case ARRIVE:
	        	this.mCouleur = Color.RED;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE); 
	        	break;
	        case NEUTRAL:
	        	this.mCouleur = 0;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE); 
	        	break;
	        case TRIGGER:
	        	this.mCouleur = Color.BLUE;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE); 
	        	break;
	        case GATE:
	        	this.mCouleur = Color.BLACK;
	        	if(rebound == 4||rebound == 8){
	        		this.mRectangle = new RectF((pX-1) * SIZE, pY * SIZE, (pX + 2) * SIZE, (pY +1) * SIZE);
	        		rebound = 12;
	        	}else{
	        		this.mRectangle = new RectF(pX * SIZE, (pY-1) * SIZE, (pX + 1) * SIZE, (pY +2) * SIZE);
	        		rebound = 3;
	        	}
	        	break;
	        case PORTAL:
	        	this.mCouleur = Color.MAGENTA;
	        	if(rebound == 4||rebound == 8){
	        		this.mRectangle = new RectF((pX-1) * SIZE, pY * SIZE, (pX + 2) * SIZE, (pY +1) * SIZE);
	        	}else{
	        		this.mRectangle = new RectF(pX * SIZE, (pY-1) * SIZE, (pX + 1) * SIZE, (pY +2) * SIZE);
	        	}
	        	break;
        }

       //Set Rebound possibility
        	//rebound = REBOUND_L + REBOUND_T;
        //	Log.i("Byte",Byte.toString(rebound)+ " "+ Integer.toBinaryString(rebound).replace(" ", "0"));

         	if(((rebound >> 3)& 1) ==1 ) this.setRebound_Bottom(true);
         	if(((rebound >> 2)& 1) ==1 ) this.setRebound_Top(true);
         	if(((rebound >> 1)& 1) ==1 ) this.setRebound_Left(true);
         	if(((rebound >> 0)& 1) ==1 ) this.setRebound_Right(true);
  
    }

	public Bloc() {
		// TODO Auto-generated constructor stub
		this.num = -1;
	}

	public boolean isRebound_Right() {
		return Rebound_Right;
	}

	public void setRebound_Right(boolean rebound_Right) {
		Rebound_Right = rebound_Right;
	}

	public boolean isRebound_Left() {
		return Rebound_Left;
	}

	public void setRebound_Left(boolean rebound_Left) {
		Rebound_Left = rebound_Left;
	}

	public boolean isRebound_Top() {
		return Rebound_Top;
	}

	public void setRebound_Top(boolean rebound_Top) {
		Rebound_Top = rebound_Top;
	}

	public boolean isRebound_Bottom() {
		return Rebound_Bottom;
	}

	public void setRebound_Bottom(boolean rebound_Bottom) {
		Rebound_Bottom = rebound_Bottom;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}