package finalproject.niu.edu.tw;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.widget.Toast;
import finalproject.niu.edu.tw.Bloc.Type;
import android.app.Service;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class LabyrinthEngine {
	public static final byte REBOUND_0 = 0;
	public static final byte REBOUND_R = 1;
	public static final byte REBOUND_L = 2;
	public static final byte REBOUND_T = 4;
	public static final byte REBOUND_B = 8;
	public static final int I_SIZE = 13;
	public static final int J_SIZE = 22;
	public static final int MAX_GATE_NUMBER = 5;
	public static final int MAX_PORTAL_NUMBER = 5;
    private Ball mBall = null;
    public Ball getBall() {
        return getmBall();
    }

    public void setBall(Ball pBall) {
        this.setmBall(pBall);
    }

    // labyrinth
    private List<Bloc> mBlocks = null;
    private Portal mPortals[] = null;
    private Gate mGates[] = null;



    private LabyrinthActivity mActivity = null;

    private SensorManager mManager = null;
    private Sensor mAccelerometre = null;

    SensorEventListener mSensorEventListener = new SensorEventListener() {

        @SuppressWarnings("deprecation")
		public void onSensorChanged(SensorEvent pEvent) {
        	
            float x = pEvent.values[0];
            float y = pEvent.values[1];

            if(getmBall() != null) {
                // Ball coordinate update
                RectF hitBox = getmBall().putXAndY(x, y);
                RectF inter = null; 
                // for Blocks
                for(Bloc block : mBlocks) {
                    // recreate block
                    inter = new RectF(block.getRectangle());
                    if(inter.intersect(hitBox)) {
                        // depend on block
                        switch(block.getType()) {
                        case BORDURE:
                          
                        	//Rebound
                        	getmBall().collisionXY(block);
                            break;
                        case START:
                            break;                    
                        case ARRIVE:
                            mActivity.showDialog(LabyrinthActivity.VICTORY_DIALOG);
                            break;
                        case HOLE:
                            mActivity.showDialog(LabyrinthActivity.DEFEAT_DIALOG);
                              break;
                        case PORTAL:

                        	Log.i("LOG","num "+block.getNum());
                        	Log.i("LOG",Integer.toString(mPortals[0].getD_in().getBlocs().size()));
                        	if(block.isin(mPortals[block.getNum()-1].getD_in().getBlocs()))
                        	{
                        		getmBall().useStarGate(mPortals[block.getNum()-1], 0);
                        	}else{
                        		getmBall().useStarGate(mPortals[block.getNum()-1], 1);
                        	}
                        	break;
                        case GATE: 
                        	//Rebound
                        	if(block.isSolid()){
                        		getmBall().collisionXY(block);
                        	}
                            break;
                        case TRIGGER: 
                        	//Rebound
                        	if(mGates[block.getNum()-1].isActive()){
                        		Toast.makeText(mActivity, "OpenGate", Toast.LENGTH_SHORT).show();		
                        		mGates[block.getNum()-1].openGate();
                        	}
                            break;
                        }
                    }
                }
             }
            }

        public void onAccuracyChanged(Sensor pSensor, int pAccuracy) {

        }
    };

    public LabyrinthEngine(LabyrinthActivity pView) {
        mActivity = pView;
        mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        mAccelerometre = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        Gates = new Bloc[MAX_GATE_NUMBER][2];

          mPortals = new Portal[MAX_GATE_NUMBER];
          mGates = new Gate[MAX_GATE_NUMBER];

    }

    // reset ball position
    public void reset() {
        getmBall().reset();
    }

    // stop captor
    public void stop() {
        mManager.unregisterListener(mSensorEventListener, mAccelerometre);
    }

    // resume captor
    public void resume() {
        mManager.registerListener(mSensorEventListener, mAccelerometre, SensorManager.SENSOR_DELAY_GAME);
    }
    
    // construct 
    public List<Bloc> buildLabyrinthe(String sLabX) {
        mBlocks = new ArrayList<Bloc>();
        InputStreamReader input = null;
        BufferedReader reader = null;
        Bloc bloc = null;
        try { 
        	InputStream is = mActivity.getAssets().open(sLabX);
        	input = new InputStreamReader(is, Charset.forName("UTF-8"));
          reader = new BufferedReader(input);
      	  Bloc[][][] Portals = null;
      	  Door[] Gates= null;
      	  Bloc[] Triggers = null;
          Portals = new Bloc[MAX_PORTAL_NUMBER][2][Door.LENGHT_MAX];
          Gates = new Door[MAX_PORTAL_NUMBER];
          Triggers = new Bloc[MAX_PORTAL_NUMBER];
          for(int i = 0 ; i< MAX_PORTAL_NUMBER;i++)
          {
        	Gates[i]= new Door();
        	Triggers[i] = new Bloc();
          	for(int j = 0 ; j< Door.LENGHT_MAX;j++)
          	{
              	Portals[i][0][j]= new Bloc();
              	Portals[i][1][j]= new Bloc(); 
              	
          	}
          }
          //table index
          int tabi = 0;
          int tabj = 0;
          // flux value
          int c;
          // while !=-1 read
          byte rebound= REBOUND_0;
          while((c = reader.read()) != -1) {
        	
           
            if((char) c == '.'|| (char) c == '<' ||(char) c == '>'||(char) c == '-'||(char) c == '_'){
            	//Neutral Block (fill the list)
            	if(mBlocks.size()<=J_SIZE+1||mBlocks.size()% J_SIZE == 0){}else{
            		if(mBlocks.get(mBlocks.size()-1).isSolid()==true){mBlocks.get(mBlocks.size()-1).setRebound_Right(true);}
                    if(mBlocks.get(mBlocks.size()-J_SIZE).isSolid()==true){mBlocks.get(mBlocks.size()-J_SIZE).setRebound_Top(true);}
            	}
            	bloc = new Bloc(Type.NEUTRAL, tabi, tabj, REBOUND_0);
            }
            else if((char) c == 'b'){
            	
            	if(mBlocks.get(mBlocks.size()-1).isSolid()==false){ rebound += (byte) (REBOUND_L) ;}
            	if(mBlocks.get(mBlocks.size()-J_SIZE).isSolid()==false){ rebound += (byte) (REBOUND_B) ;}

            		bloc = new Bloc(Type.BORDURE, tabi, tabj,rebound);
                 	
            } else if((char) c == 's'){
            	
        		if(mBlocks.get(mBlocks.size()-1).isSolid()==true){mBlocks.get(mBlocks.size()-1).setRebound_Right(true);}
                if(mBlocks.get(mBlocks.size()-J_SIZE).isSolid()==true){mBlocks.get(mBlocks.size()-J_SIZE).setRebound_Top(true);}
            	bloc = new Bloc(Type.START, tabi, tabj,REBOUND_0);getmBall().setInitialRectangle(new RectF(bloc.getRectangle()));
           
            }else if((char) c == 'h'){
            	
        		if(mBlocks.get(mBlocks.size()-1).isSolid()==true){mBlocks.get(mBlocks.size()-1).setRebound_Right(true);}
                if(mBlocks.get(mBlocks.size()-J_SIZE).isSolid()==true){mBlocks.get(mBlocks.size()-J_SIZE).setRebound_Top(true);}
            	bloc = new Bloc(Type.HOLE, tabi, tabj, REBOUND_0);
              
            }else if((char) c == 'a'){
            	
        		if(mBlocks.get(mBlocks.size()-1).isSolid()==true){mBlocks.get(mBlocks.size()-1).setRebound_Right(true);}
                if(mBlocks.get(mBlocks.size()-J_SIZE).isSolid()==true){mBlocks.get(mBlocks.size()-J_SIZE).setRebound_Top(true);}
            	bloc = new Bloc(Type.ARRIVE, tabi, tabj, REBOUND_0);
              
            }else if((char) c == 'i'|| c == 'j'){
        		if(mBlocks.get(mBlocks.size()-1).isSolid()==true){mBlocks.get(mBlocks.size()-1).setRebound_Right(true);}
                if(mBlocks.get(mBlocks.size()-J_SIZE).isSolid()==true){mBlocks.get(mBlocks.size()-J_SIZE).setRebound_Top(true);}  
                
                bloc = new Bloc(Type.PORTAL, tabi, tabj, (byte)  Character.getNumericValue(reader.read())); 
                if(c=='j'){bloc.setDoorside( (byte) Character.getNumericValue(reader.read()));}
                Portals[bloc.getNum()-1][0][bloc.lenght(Portals[bloc.getNum()-1][0])] = bloc;
           
            }else if(c == 'o'|| c== 'p'){
        		if(mBlocks.get(mBlocks.size()-1).isSolid()==true){mBlocks.get(mBlocks.size()-1).setRebound_Right(true);}
                if(mBlocks.get(mBlocks.size()-J_SIZE).isSolid()==true){mBlocks.get(mBlocks.size()-J_SIZE).setRebound_Top(true);}  
                
                bloc = new Bloc(Type.PORTAL, tabi, tabj, (byte) Character.getNumericValue(reader.read()));
                Log.i("LOG","ICI "+Integer.toString(bloc.getNum()));
                if(c=='p'){bloc.setDoorside( (byte) Character.getNumericValue(reader.read()));}
                Portals[bloc.getNum()-1][1][bloc.lenght(Portals[bloc.getNum()-1][1])] = bloc;  
            }else if((char) c == 'g'){
            		if(mBlocks.get(mBlocks.size()-1).isSolid()==false){ rebound += (byte) (REBOUND_L) ;}
                	if(mBlocks.get(mBlocks.size()-J_SIZE).isSolid()==false){ rebound += (byte) (REBOUND_B) ;}
                    bloc = new Bloc(Type.GATE, tabi, tabj, rebound,Character.getNumericValue(reader.read()));
                    Gates[bloc.getNum()-1].getBlocs().add(bloc);
                    Gates[bloc.getNum()-1].update();
            }else if((char) c == 't'){
        		if(mBlocks.get(mBlocks.size()-1).isSolid()==true){mBlocks.get(mBlocks.size()-1).setRebound_Right(true);}
                if(mBlocks.get(mBlocks.size()-22).isSolid()==true){mBlocks.get(mBlocks.size()-22).setRebound_Top(true);}    
                bloc = new Bloc(Type.TRIGGER, tabi, tabj, REBOUND_0,Character.getNumericValue(reader.read()));   
                Triggers[bloc.getNum()-1] = bloc;
            }else if ((char) c == '\n') {
              // Change line!
              tabi = -1;
              tabj++;
            }
//add bloc
            if(bloc != null)
            {          
              mBlocks.add(bloc);
            }
              tabi++; bloc = null;rebound= REBOUND_0;
            
            }//end of while
          
          //delete neutral blocks from list
        //  Log.i("Size","before "+ Integer.toString(mBlocks.size()));
          List<Bloc> toRemove = new ArrayList<Bloc>();
          //List<Bloc> toCreatePortal = new ArrayList<Bloc>();
          for(Bloc b : mBlocks) {
        	  if(b.getType() == Type.NEUTRAL) {
        		  toRemove.add(b);
              }
          }
          mBlocks.removeAll(toRemove);
          toRemove.clear();
          tabi=0;
          tabj=0;

  		  while(Portals[tabi][0][0].getNum()!=-1){
  			mPortals[tabi] = new Portal(Portals[tabi][0],Portals[tabi][1]);
  			tabi++;
  		  }
  		  tabi= 0;
  		  while(Triggers[tabi].getNum()!=-1){
  			mGates[tabi] = new Gate(Gates[tabi],Triggers[tabi]);
  			tabi++;
  		  }

        } catch (IllegalCharsetNameException e) {
          e.printStackTrace();
        } catch (UnsupportedCharsetException e) {
          e.printStackTrace();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if(input != null)
            try {
              input.close();
            } catch (IOException e1) {
              e1.printStackTrace();
            }
          if(reader != null)
            try {
              reader.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
        }
        return mBlocks;
    }

	public Ball getmBall() {
		return mBall;
	}

	public void setmBall(Ball mBall) {
		this.mBall = mBall;
	}

}