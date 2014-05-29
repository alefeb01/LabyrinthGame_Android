package finalproject.niu.edu.tw;


import android.graphics.Color;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.Log;

public class Ball {
	

    // Rayon
    public static final int R = 15;
    public static final int FILTER_BOUND_LIMIT = R*(R+1);
    // Color
    private int mCouleur = Color.GREEN;
    public int getCouleur() {
        return mCouleur;
    }
    
    // spd max
    private static final float MAX_SPEED = 20.0f;
    
    // slow acc
    private static final float COMPENSATEUR = 4.0f;
    
    // slow shock
    private static final float REBOND = 1.75f;
    
    // init location
    private RectF mInitialRectangle = null;
    //Var coorX Y
    private float centerX = 0;
    private float centerY = 0;
    //private boolean mInMovement = false;
    // Initial Rectangle

    public void setInitialRectangle(RectF pInitialRectangle) {
        this.mInitialRectangle = pInitialRectangle;
        this.mX = pInitialRectangle.left + R;
        this.mY = pInitialRectangle.top + R;
    }
    
    // Collision rectangle
    private RectF mRectangle = null;
    
    //Coordinates X Y
    private float mX;
    public float getX()
    {
        return mX;
    }
    public void setPosX(float pPosX)//x du bloc
    {
    	mX = pPosX;
	
	        // Rebond if only if reach rectangle limit
	        if(mX < R)
	        {
	            mX = R;
	            mSpeedY = -mSpeedY / REBOND;
	        } else if(mX > mWidth - R) 
	        {
	            mX = mWidth - R;
	            mSpeedY = -mSpeedY / REBOND;
	        }
    
    }
    
    
    private float mY;
    public float getY()
    {
        return mY;
    }

    public void setPosY(float pPosY) 
    {
        mY = pPosY;
        //collide with limit
        if(mY < R) 
        {
            mY = R;
            mSpeedX = -mSpeedX / REBOND;
        } else if(mY > mHeight - R)
        {
            mY = mHeight - R;
            mSpeedX = -mSpeedX / REBOND;
        }
    }
    
    // Speed setting
    private float mSpeedX = 0;
    private float mSpeedY = 0;


    
    // screen height
    private int mHeight = -1;
    public void setHeight(int pHeight) {
        this.mHeight = pHeight;
    }
    
    // screen width
    private int mWidth = -1;
    public void setWidth(int pWidth) {
        this.mWidth = pWidth;
    }

    public Ball() {
        setmRectangle(new RectF());
    }

    // update coordinates
   public RectF collisionXY(Bloc Block)
   {   

	   centerX = Block.getRectangle().centerX();
	   centerY = Block.getRectangle().centerY();
	   //collisionX
	   float mX_old = mX;
	   float mY_old = mY;
		 if(mX < 2*R + centerX && mX > centerX && Block.isRebound_Right()&& (mX_old-2*R-centerX)*(mX_old-2*R-centerX)< FILTER_BOUND_LIMIT)
	        {
	            mX = 2*R+centerX;
	            mSpeedY = -mSpeedY / REBOND;
	            //Log.i("LOG","Right x="+Float.toString(Block.getRectangle().centerX()/30)+" y= "+Float.toString(Block.getRectangle().centerY()/30));
	        } else if(mX> centerX - 2*R &&  mX < centerX && Block.isRebound_Left()&& (mX_old+2*R-centerX)*(mX_old+2*R-centerX)< FILTER_BOUND_LIMIT) 
	        {
	            mX = centerX - 2*R;
	            mSpeedY = -mSpeedY / REBOND;
	           // Log.i("LOG","Left x="+Float.toString(Block.getRectangle().centerX()/30)+" y= "+Float.toString(Block.getRectangle().centerY()/30));
	        }
		 //collision Y
	        else if(mY < 2*R + centerY && mY > centerY && Block.isRebound_Top()&& (mY_old-2*R-centerY)*(mY_old-2*R-centerY)< FILTER_BOUND_LIMIT) 
	        {
	            mY = 2*R + centerY;
	            mSpeedX = -mSpeedX / REBOND;
	            //Log.i("LOG","Top x="+Float.toString(Block.getRectangle().centerX()/30)+" y= "+Float.toString(Block.getRectangle().centerY()/30));
	        } else if(mY > centerY - 2*R && mY < centerY && Block.isRebound_Bottom()&& (mY_old+2*R-centerY)*(mY_old+2*R-centerY)< FILTER_BOUND_LIMIT)
	        {
	            mY = centerY - 2*R;
	            //Log.i("LOG","bottom x="+Float.toString(Block.getRectangle().centerX()/30)+" y= "+Float.toString(Block.getRectangle().centerY()/30));
	            mSpeedX = -mSpeedX / REBOND;
	        }
	   
       // If collision
       getmRectangle().set(mX - R, mY - R, mX + R, mY + R);
       
       return getmRectangle();	   
   }
    public RectF putXAndY(float pX, float pY) {

            mSpeedX += pX / COMPENSATEUR;
            if(mSpeedX > MAX_SPEED)
                mSpeedX = MAX_SPEED;
            if(mSpeedX < -MAX_SPEED)
                mSpeedX = -MAX_SPEED;
                
            mSpeedY += pY / COMPENSATEUR;
            if(mSpeedY > MAX_SPEED)
                mSpeedY = MAX_SPEED;
            if(mSpeedY < -MAX_SPEED)
                mSpeedY = -MAX_SPEED;
            
            setPosX(mX + mSpeedY);
            setPosY(mY + mSpeedX);

        
        // If collision
        getmRectangle().set(mX - R, mY - R, mX + R, mY + R);
        
        return getmRectangle();
    }
    
    public float[] getGateFront(Bloc gate)
    {
    	float f[] = {0,0} ;
    	if(gate.isRebound_Right())
    	{
    		f[0] = gate.getRectangle().centerX() + 2*R;
    		f[1] = gate.getRectangle().centerY();
    	}else if(gate.isRebound_Left()){
    		f[0] = gate.getRectangle().centerX() - 2*R;
    		f[1] = gate.getRectangle().centerY();
    	}else if(gate.isRebound_Top()){	
    		f[0] = gate.getRectangle().centerX();
    		f[1] = gate.getRectangle().centerY()+ 2*R ;
    	}else if(gate.isRebound_Bottom())
    	{
    		f[0] = gate.getRectangle().centerX();
    		f[1] = gate.getRectangle().centerY()- 2*R ;
    	}
		return f;
    }
    //Use StarGate
    public RectF useStarGate(Portal p, int i){
        // If collision
    	Bloc b,a;
    	if(i == 0)
    	{
    		b = p.getB_out();
    		a =	p.getB_in();
    	}else{
    		b = p.getB_in();
    		a = p.getB_out();
    	}
    	
 	   centerX = a.getRectangle().centerX();
 	   centerY = a.getRectangle().centerY();
 	   //collisionX
 		 if(mX < 2*R + centerX && mX > centerX && a.isRebound_Right())
 	        {	 	   	     
	 			 mX = getGateFront(b)[0]; 
		  		 mY = getGateFront(b)[1];
	 	         mSpeedY = 0;
	 	         mSpeedX=  0;
	 	         
	 			 
 	        } else if(mX > centerX - 2*R &&  mX < centerX && a.isRebound_Left()) 
 	        {
	 	   	     mX = getGateFront(b)[0]; 
	 	  		 mY = getGateFront(b)[1];
	 	         mSpeedY = 0;
	 	         mSpeedX=  0;	        	
 	        }
 		 //collision Y
 	        else if(mY < 2*R + centerY && mY > centerY && a.isRebound_Top()) 
 	        {
	 	   	     mX = getGateFront(b)[0]; 
	 	  		 mY = getGateFront(b)[1];
	 	         mSpeedY = 0;
	 	         mSpeedX=  0;
 	        	
 	        } else if(mY > centerY - 2*R && mY < centerY && a.isRebound_Bottom())
 	        {
 	     	   //collisionX
	 	   	     mX = getGateFront(b)[0]; 
	 	  		 mY = getGateFront(b)[1];
	 	         mSpeedY = 0;
	 	         mSpeedX=  0;
 	        }



		getmRectangle().set(mX- R, mY - R, mX + R, mY + R);
        return getmRectangle();	
    }
    
    
    // Reset ball position
     public void reset() {
        mSpeedX = 0;
        mSpeedY = 0;
        this.mX = mInitialRectangle.left + R;
        this.mY = mInitialRectangle.top + R;
    }
	public RectF getmRectangle() {
		return mRectangle;
	}
	public void setmRectangle(RectF mRectangle) {
		this.mRectangle = mRectangle;
	}
	public void openGate(Gate gate) {
		gate.setActive(false);
		gate.getGate().setRebound_Bottom(false);
		gate.getGate().setRebound_Top(false);
		gate.getGate().setRebound_Right(false);
		gate.getGate().setRebound_Left(false);
		gate.getGate().setCouleur(Color.GRAY);
		gate.getTrigger().setCouleur(Color.CYAN);
		
	}
}