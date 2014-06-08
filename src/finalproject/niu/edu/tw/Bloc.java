package finalproject.niu.edu.tw;
import java.util.List;



import android.graphics.Color;
import android.graphics.RectF;


public class Bloc {

	
    enum  Type { BORDURE, START, ARRIVE, HOLE,HOLE2, NEUTRAL, PORTAL,TRIGGER,GATE,SWITCH};
    
    private float SIZE = Ball.R * 2;
	protected int tabx = 0; 
    private boolean solid = false;
    private int doorside = 0;
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
    public void changeType(Type type){
    	mType = type;
    	switch(mType){
    		case NEUTRAL:
    			mCouleur = Color.CYAN;
    			break;
    		case HOLE:
    			mCouleur = Color.GREEN;
    			break;
    	}
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
	        	this.setSolid(true);
	        	break;
	        case HOLE:
	        	this.mCouleur = Color.GREEN;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE); 
	        	break;
	        case HOLE2:
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
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE);
	        	this.setSolid(true);
	        	break;
	        case PORTAL:
	        	this.mCouleur = Color.MAGENTA;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE); 
	        	this.setNum(rebound);
	        	break;
	        case SWITCH:
	        	this.mCouleur = Color.CYAN;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE); 
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
	public Bloc(Type pType, int pX, int pY, byte rebound, int i) {
		// TODO Auto-generated constructor stub
		this.mType = pType;
        
        switch(mType){
        
	        case TRIGGER:
	        	this.mCouleur = Color.BLUE;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE); 
	        	this.setNum(i);
	        	this.setSolid(false);
	        	break;
	        case GATE:
	        	this.mCouleur = Color.BLACK;
	        	this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE);
	        	this.setSolid(true);
	        	this.setNum(i);
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

	//return the size of array bloc
	public int lenght(Bloc[] b)
	{
		int count = 0;
		for (tabx=0; tabx<b.length; tabx++)
		{
			if(b[tabx].getNum()!=-1){
				count++;
			}
		}
		return count;
	}
	public int lenght(List<Bloc> b)
	{
		return b.size();
	}
	
	public boolean isin( Bloc[] lb ) {
	    for (  Bloc b : lb )
	    {
	        if ( b == this )
	        { return true;}
	    }
	    return false;
	}
	public boolean isin( List<Bloc> lb ) {
	    for (  Bloc b : lb )
	    {
	        if ( b == this )
	        { return true;}
	    }
	    return false;
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

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public int getDoorside() {
		return doorside;
	}

	public void setDoorside(int doorside) {
		this.doorside = doorside;
	}
}