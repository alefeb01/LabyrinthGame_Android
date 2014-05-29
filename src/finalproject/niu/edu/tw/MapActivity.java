package finalproject.niu.edu.tw;



import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends Activity {

    //Cheat Dialog
    public static final int CHEAT_DIALOG = 2;
    
	public static final String PREFS_NAME = "sharedPreferences.txt";

    public static final int MINUTE_FOR_A_LIFE = 5;
    public static final int SECOND_PER_MINUTE = 60;
    public static final int TIME_FOR_A_LIFE_S = MINUTE_FOR_A_LIFE * SECOND_PER_MINUTE;
    //DEV VAR
    public static final int WORLD_DEV_LIMIT = 2;
	public static final String ADMIN_CODE = "qwerty";

	

	Activity act = this;
    TextView tLife;
    TextView tTime;
    EditText etCode;
    Button[] btnArray;
    static MediaPlayer player;
    static MediaPlayer player_copie;
    //timer
    private Handler handlerTime = new Handler();
    Time tCurrentTime = new Time();
   
    //Memory : 
    private SharedPreferences settings = null ;//PreferenceManager.getDefaultSharedPreferences(this);
    private SharedPreferences.Editor editor = null;//settings.edit();
    private int iCurrentLvl;
    private int iLife;
    private int iTime_min;
    private int iTime_sec;
    private int iTime_since;
    private long lTime_sec;
    private int iTime_Life;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        
        
	    
	  //declare like this
        player_copie =MediaPlayer.create(this, R.raw.piano); 
        player = MediaPlayer.create(this, R.raw.piano); 
        player.setLooping(true); // Set looping 
        player.setVolume(10,10); 
        if(player.isPlaying()==false){ player.start(); }
	    // call in oncreate()
	    handlerTime.postDelayed(runnable, 1000); //1sec


	    
	    
        //Drawing widgets...TEST2
	    FrameLayout LabyrinthGame = new FrameLayout(this);
      //  LinearLayout LabyrinthMapLvl = new LinearLayout (this);
        //LabyrinthMapLvl.setOrientation(1);
        LinearLayout LabyrinthWidgets = new LinearLayout (this);
        LabyrinthWidgets.setOrientation(0);	// LinearLayout by horizontal
        Button bSoundButton = new Button(this);
        Button bCheatButton = new Button(this);
        Button bLeaveGameButton = new Button(this);
        tLife = new TextView(this);
        tTime = new TextView(this);
        TextView tMenu = new TextView(this);

        getsetup();
        //END GAME
        bLeaveGameButton.setWidth(300);
        bLeaveGameButton.setText("Leave Game");
        bLeaveGameButton.setX(605);
        bLeaveGameButton.setY(250);
        bLeaveGameButton.setOnClickListener(new Button.OnClickListener()
        { 
          public void onClick(View v)
          {
        	MapActivity.this.finish();
          }
        });
        //Cheat Test
        bCheatButton.setWidth(300);
        bCheatButton.setText("CHEAT");
        bCheatButton.setX(5);
        bCheatButton.setY(50);
        bCheatButton.setOnClickListener(new Button.OnClickListener()
        { 
          @SuppressWarnings("deprecation")
		public void onClick(View v)
          {
        	//Cheat
        	  act.showDialog(MapActivity.CHEAT_DIALOG);
          }
        });
        
        //Music on/off
        bSoundButton.setWidth(300);
        bSoundButton.setText("Sound");
        bSoundButton.setX(305);
        bSoundButton.setY(150);
        bSoundButton.setOnClickListener(new Button.OnClickListener()
        { 
          public void onClick(View v)
          {
        	// change music status
        	  if(player.isPlaying()==true)
        	  {
        		  player.stop();
        	  }else{ player.reset();player = MediaPlayer.create(MapActivity.this, R.raw.piano);
              player.setLooping(true); // Set looping 
              player.setVolume(10,10); player.start() ;}
          }
        });
        //Menu Text
        tMenu.setText("MENU");
        tMenu.setX(780);
        tMenu.setY(0);
        //Life Text
        tLife.setText(Integer.toString(iLife));
        tLife.setTextSize(30);
        tLife.setTextColor(Color.DKGRAY);
        tLife.setX(635);
        tLife.setY(0);
        //Time Life
        tTime.setText("(^_^)");
        tTime.setTextSize(25);
        tTime.setTextColor(Color.DKGRAY);
        tTime.setX(640);
        tTime.setY(0);
//
        

        //ADD IN LAYOUT
        LabyrinthWidgets.addView(tLife);
        LabyrinthWidgets.addView(tTime);
        LabyrinthWidgets.addView(tMenu);
        LabyrinthWidgets.addView(bLeaveGameButton); 
        LabyrinthWidgets.addView(bSoundButton);
        LabyrinthWidgets.addView(bCheatButton);

        LabyrinthGame.addView(LabyrinthWidgets);
        LabyrinthGame.addView(createWorld(), 620, 480);
        setContentView(LabyrinthGame);

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        updateLvlUp();
        getsetup();

    } 

    @Override
    protected void onPause() {
        super.onStop();
    }
    @Override
    public void onStart(){
      super.onStart();
      updateLvlUp();
      getsetup();
    }

    @Override
    public void onStop(){
      super.onStop();
    }
    @Override
    public void onRestart(){
      super.onRestart();
      updateLvlUp();
      getsetup();
    }
    @Override
    public void onDestroy(){
      super.onDestroy();
    }
    
    public void onPrepared(MediaPlayer play) {
        play.start();
    }
    @Override
    public Dialog onCreateDialog (int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
        case CHEAT_DIALOG:
        	etCode = new EditText(this);
        	LinearLayout ll = new LinearLayout(this);
        	ll.setOrientation(1);
        	ll.addView(etCode);
            builder.setCancelable(false)
            .setMessage("Enter code")
            .setTitle("CheatBox !")
            .setView(ll)
            .setNeutralButton("Valid", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	//Log.i("LOG",etCode.getText().toString() + "a "+ADMIN_CODE);
                	if(etCode.getText().toString().equals(ADMIN_CODE))
                	{
                		//StartActivity
                		Bundle mBundle = new Bundle();
	              	    mBundle.putInt("LEVEL", 999);
                		Intent cheat_intent = new Intent(MapActivity.this, LabyrinthActivity.class);
                		cheat_intent.putExtras(mBundle);
	                	Toast.makeText(MapActivity.this, "CHEAT BOX OPPEN", Toast.LENGTH_SHORT).show();
			               
                		startActivity( cheat_intent);
                	}
                }
            });
            break;            
        }
        return builder.create();
    }

    @Override
    public void onPrepareDialog (int id, Dialog box) {

    }
    
    private boolean getsetup()
    {

    	if(!settings.contains("CURRENT_LVL"))
        {
	        editor.putString("CURRENT_LVL", "0");
	        editor.commit(); iCurrentLvl = 0;
        }else
        {
        	iCurrentLvl = Integer.parseInt(settings.getString("CURRENT_LVL", ""));
        }
        
        //Get the current Life!
	    if(!settings.contains("LIFE")){ 
	    	
	        editor.putString("LIFE", "5");
	        editor.commit(); iLife = 5;
	      //  Log.i("LOG","oc"+Integer.toString(iLife));
	    }
	    else{
	    	
	      	iLife = Integer.parseInt(settings.getString("LIFE", ""));
	      	tLife.setText(Integer.toString(iLife));
	    }
	    //Time Line
	    if(!settings.contains("TIME")){ 
	    	
	        editor.putString("TIME", "");
	        editor.commit();
	    }
	    else{
	    	if(iLife<5){
	    	lTime_sec = Long.parseLong(settings.getString("TIME", ""));
	    	}else{tTime.setText("(^_^)");}			
	    }
		return true;
    }
    //Update LvlUp :
    private boolean updateLvlUp()
    {
    	Button b = new Button(this);
    	Drawable d = b.getBackground();
    	Drawable d_lock = getResources().getDrawable(R.drawable.lock);
    	for(int i = 0; i< WORLD_DEV_LIMIT+1; i++){
    		 if(i < WORLD_DEV_LIMIT)
    		 {
    			 if(i <= iCurrentLvl/10){
    				 btnArray[i].setBackgroundDrawable(d);
    				 final int LOC = i;
    				 btnArray[i].setOnClickListener(new Button.OnClickListener(){
    					 public void onClick(View v){
    						 Intent map_intent = new Intent(MapActivity.this, WorldActivity.class);
    						 Bundle mBundle = new Bundle();

    						 mBundle.putInt("WORLD", LOC);
    						 map_intent.putExtras(mBundle);
    						 startActivity(map_intent);
    					 }
    				 });}
    			 else{
    				 btnArray[i].setBackgroundDrawable(d_lock);
    				 btnArray[i].setOnClickListener(new Button.OnClickListener(){
    					 public void onClick(View v){
    						 Toast.makeText(MapActivity.this, "Access Refused", Toast.LENGTH_SHORT).show();				             
    					 }
    				 });
    			 }
    		 }
    	}
    	return true;
    }
    //CREATE A WORLD 10LVL
   
    private LinearLayout createWorld()
    {


	        LinearLayout LabyrinthMapWorld = new LinearLayout (this);
	        LinearLayout LabyrinthSv = new LinearLayout (this);
	        LabyrinthMapWorld.setOrientation(1);	// LinearLayout by horizontal	
	        LabyrinthSv.setOrientation(0);
	        TextView tWorld = new TextView(this);
	        tWorld.setText("World Selection");
	        tWorld.setX(270);
	        HorizontalScrollView  svLvl = new HorizontalScrollView(this);
	        btnArray = new Button[WORLD_DEV_LIMIT+1];
	        for(int i = 0; i <= WORLD_DEV_LIMIT; i++) 
	        {
	            Button b = new Button(this);
	            if(i < WORLD_DEV_LIMIT)
	            {
	            	if(i <= iCurrentLvl/10){
			            b.setText("World\n"+i);
			            b.setHeight(100);
			            b.setWidth(100);
			            final int LOC = i;
			            b.setOnClickListener(new Button.OnClickListener(){
			                public void onClick(View v){
			              	  Intent map_intent = new Intent(MapActivity.this, WorldActivity.class);
			              	  Bundle mBundle = new Bundle();
			              	  
			              	  mBundle.putInt("WORLD", LOC);
			              	  map_intent.putExtras(mBundle);
			              	  startActivity(map_intent);
			                }
			              });
	            	}else{
		            	Drawable d = getResources().getDrawable(R.drawable.lock);
			            b.setBackgroundDrawable(d);
			            b.setText("World\n"+i);
			            b.setHeight(100);
			            b.setWidth(100);
			            b.setOnClickListener(new Button.OnClickListener(){
			                public void onClick(View v){
			                 	Toast.makeText(MapActivity.this, "Access Refused", Toast.LENGTH_SHORT).show();				             
			                }
			              });
	            		
	            	}
	            }else{
	            	Drawable d = getResources().getDrawable(R.drawable.warning);
	            	//b.setText("World\n"+i);
		            b.setBackgroundDrawable(d);
		            b.setHeight(100);
		            b.setWidth(100);
		            b.setOnClickListener(new Button.OnClickListener(){
		                public void onClick(View v){
		                	Toast.makeText(MapActivity.this, "New adventures will come soon", Toast.LENGTH_SHORT).show();
		                }
		              });
	            	
	            }	           
	            btnArray[i]=b;
	            LabyrinthSv.addView(btnArray[i]);
	            
	        }
	        svLvl.addView(LabyrinthSv);
	        LabyrinthMapWorld.addView(tWorld);
	        LabyrinthMapWorld.addView(svLvl); 
	        LabyrinthMapWorld.setY(150);

    	return LabyrinthMapWorld; 
    }
    //inside this function which u have to run
    private Runnable runnable = new Runnable() {
        public void run() {
        	if(iLife<5)
        	{//Recover Life process
        		iTime_since = (int) ((System.currentTimeMillis()/1000) - lTime_sec) 	;	
            	iTime_min = (TIME_FOR_A_LIFE_S -iTime_since )/ 60;
            	iTime_sec = (TIME_FOR_A_LIFE_S - iTime_since) % 60;
            	//Log.i("LOG","timesince "+Integer.toString(iTime_since)+"min "+Integer.toString(iTime_min)+"sec "+Integer.toString(iTime_sec));
            	tTime.setText(String.format("%02d", iTime_min)+":"+String.format("%02d", iTime_sec));
            	iTime_Life = (int) (iTime_since / TIME_FOR_A_LIFE_S);
            	//Log.i("LOG","life="+Integer.toString(iTime_Life)+"since "+Integer.toString(iTime_since));
            	if(iTime_Life>0 )
            	{
            		iLife += iTime_Life;
            		if(iLife >= 5)
            		{
            			iLife = 5;
                		editor.putString("TIME", "" );
                		editor.commit();
                		tTime.setText("(^_^)");
            		}else
            		{
            			editor.putString("TIME", Long.toString(iTime_since + lTime_sec) );
            			editor.commit();
            			lTime_sec = Long.parseLong(settings.getString("TIME", ""));
            		}
            		
            		editor.putString("LIFE", Integer.toString(iLife));
            		editor.commit();
            		tLife.setText(Integer.toString(iLife));

            	}
        	}
        	handlerTime.postDelayed(this, 1000);
        }
    };   
}