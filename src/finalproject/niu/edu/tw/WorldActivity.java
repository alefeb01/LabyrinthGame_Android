package finalproject.niu.edu.tw;


import java.io.IOException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.format.Time;
import android.util.Log;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WorldActivity extends Activity {

	Bundle bundle;
	Intent intent;
	protected int tabx = 0; 
    public static final String PREFS_NAME = "sharedPreferences.txt";
    public static final int MINUTE_FOR_A_LIFE = 5;
    public static final int SECOND_PER_MINUTE = 60;
    public static final int TIME_FOR_A_LIFE_S = MINUTE_FOR_A_LIFE * SECOND_PER_MINUTE;
    private int lvl_Number= 0;
 
    TextView tLife;
    TextView tTime;
    //timer
    private Handler handlerTime = new Handler();
    Time tCurrentTime = new Time();
 	
    //Memory : 
    private SharedPreferences settings = null ;//PreferenceManager.getDefaultSharedPreferences(this);
    private SharedPreferences.Editor editor = null;//settings.edit();
    
    Button[] btnArray;
    private boolean bSound;
    private int iCurrentLvl;
    private int iLife;
    private int iTime_min;
    private int iTime_sec;
    private int iTime_since;
    private long lTime_sec;
    private int iTime_Life;
    private PowerManager pm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent=this.getIntent();
        bundle = intent.getExtras();
        
        // initialize receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        pm =(PowerManager) getSystemService(MapActivity.POWER_SERVICE);

        
        setLvl_Number();
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        
        
	  //declare like this


	    // call in oncreate()
	    handlerTime.postDelayed(runnable, 1000); //1sec


	    
	    
        //Drawing widgets...TEST2
	    
	    FrameLayout LabyrinthGame = new FrameLayout(this);
      //  LinearLayout LabyrinthMapLvl = new LinearLayout (this);
        //LabyrinthMapLvl.setOrientation(1);
        LinearLayout LabyrinthWidgets = new LinearLayout (this);
        LabyrinthWidgets.setOrientation(0);	// LinearLayout by horizontal
        Button bSoundButton = new Button(this);
        Button bLeaveGameButton = new Button(this);
        tLife = new TextView(this);
        tTime = new TextView(this);
        TextView tMenu = new TextView(this);
        //getsetup
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
        	WorldActivity.this.finish();
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
        	  if(MapActivity.player.isPlaying()==true)
        	  {
        		  MapActivity.player.stop();
        	  }else{ MapActivity.player.reset();MapActivity.player = MapActivity.player_copie;
        	  MapActivity.player.setLooping(true); // Set looping 
        	  MapActivity.player.setVolume(10,10); MapActivity.player.start() ;}
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


        LabyrinthGame.addView(LabyrinthWidgets);
        LabyrinthGame.addView(createLvl(), 620, 480);
        setContentView(LabyrinthGame);

    }
    
    @Override
    protected void onResume() {
        // only when screen turns on
        updateLvlUp();
        getsetup();
        if (!ScreenReceiver.wasScreenOn) {
            // this is when onResume() is called due to a screen state change
        	if(bSound){
    		  MapActivity.player.reset();
      		  MapActivity.player = MapActivity.player_copie;
      		  MapActivity.player.setLooping(true); // Set looping 
      		  MapActivity. player.setVolume(10,10); MapActivity.player.start() ;
        	}
            Log.i("LOG","SCREEN TURNED ON");
        } else {
            // this is when onResume() is called when the screen state has not changed
        }
        super.onResume();

    } 

    @Override
    protected void onPause() {

    	// when the screen is about to turn off
    	// Use the PowerManager to see if the screen is turning off
    	if (pm.isScreenOn() == false) {
    	// this is the case when onPause() is called by the system due to the screen turning off
    		if(MapActivity.player.isPlaying()){MapActivity.player.stop();}
    	Log.i("LOG","SCREEN TURNED OFF");
    	} else {
    	// this is when onPause() is called when the screen has not turned off
    	}
        super.onStop();
    }
    @Override
    public void onStart(){
      super.onStart();
      updateLvlUp();
      getsetup();
    }
    
    public void onPrepared(MediaPlayer play) {
        play.start();
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
	    if(!settings.contains("SOUND")){ 
	    	bSound = true;
	        editor.putString("SOUND", Boolean.toString(bSound));
	        editor.commit();
	    }
	    else{
	    	
	      	bSound = Boolean.parseBoolean(settings.getString("SOUND", ""));
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
    	for(tabx = 0; tabx< getLvl_Number(); tabx++){
    		final int LOC = bundle.getInt("WORLD")*10+tabx;
    		if(tabx<=iCurrentLvl-(iCurrentLvl/10)*10 && bundle.getInt("WORLD")<=(iCurrentLvl/10) || bundle.getInt("WORLD")<(iCurrentLvl/10)  ){
    			btnArray[tabx].setBackgroundDrawable(d);
    			btnArray[tabx].setOnClickListener(new Button.OnClickListener(){
	                public void onClick(View v){
	              	 if(iLife>0){
	                	Intent lvl_intent = new Intent(WorldActivity.this, LabyrinthActivity.class);
	              	  Bundle mBundle = new Bundle();
	              	  
	              	  mBundle.putInt("LEVEL", LOC);
	              	  lvl_intent.putExtras(mBundle);
	              	  startActivity(lvl_intent);
	                 
	              	 }else{
		                Toast.makeText(WorldActivity.this, "Wait for a Life", Toast.LENGTH_SHORT).show();}
	                }
	              }); 	
    		}
    		else{
    			btnArray[tabx].setBackgroundDrawable(d_lock);
    			btnArray[tabx].setOnClickListener(new Button.OnClickListener(){
	                public void onClick(View v){
		                Toast.makeText(WorldActivity.this, "Access Refused", Toast.LENGTH_SHORT).show();}
	                
	              }); 	
    		}
    	}
    	return true;
    }
    //CREATE A WORLD 10LVL
   
    private LinearLayout createLvl()
    {
	        LinearLayout LabyrinthMapLvl = new LinearLayout (this);
	        LinearLayout LabyrinthSv = new LinearLayout (this);
	        LabyrinthMapLvl.setOrientation(1);	// LinearLayout by horizontal	
	        LabyrinthSv.setOrientation(0);
	        TextView tLvl = new TextView(this);
	        tLvl.setText("World "+bundle.getInt("WORLD")+" Level Selection");
	        tLvl.setX(270);
	        HorizontalScrollView  svLvl = new HorizontalScrollView(this);
	        btnArray = new Button[getLvl_Number()];
	        for(tabx = 0; tabx < getLvl_Number(); tabx++) 
	        {
	        	Button b = new Button(this);
	            final int LOC = bundle.getInt("WORLD")*10+tabx;
	            //Log.i("LOG",Integer.toString(i));
	            b.setHeight(100);
	            b.setWidth(100);
	            if(LOC <= iCurrentLvl)
	            {
	            	b.setText("Lvl\n"+tabx);
	            	b.setOnClickListener(new Button.OnClickListener(){
		                public void onClick(View v){
		              	 if(iLife>0){
		                	Intent lvl_intent = new Intent(WorldActivity.this, LabyrinthActivity.class);
		              	  Bundle mBundle = new Bundle();
		              	  
		              	  mBundle.putInt("LEVEL", LOC);
		              	  lvl_intent.putExtras(mBundle);
		              	 // Log.i("LOG","bundleready");
		              	  startActivity(lvl_intent);
		                 
		              	 }else{
			                Toast.makeText(WorldActivity.this, "Wait for a Life", Toast.LENGTH_SHORT).show();}
		                }
		              }); 	
	            }else{
	            	Drawable d = getResources().getDrawable(R.drawable.lock);
	            	b.setText("Lvl\n"+tabx);
	            	b.setBackgroundDrawable(d);
	            	b.setOnClickListener(new Button.OnClickListener(){
		                public void onClick(View v){
			                Toast.makeText(WorldActivity.this, "Access Refused", Toast.LENGTH_SHORT).show();}
		                
		              }); 		            	
	            }
	            btnArray[tabx]=b;
	            LabyrinthSv.addView(btnArray[tabx]);
	        }
	        svLvl.addView(LabyrinthSv);
	        LabyrinthMapLvl.addView(tLvl);
	        LabyrinthMapLvl.addView(svLvl); 
	        LabyrinthMapLvl.setY(150);

    	return LabyrinthMapLvl; 
    }
    public int getLvl_Number() {
		return lvl_Number;
	}

	public void setLvl_Number() {
		lvl_Number = 0;
		String[] fileNames = null;
		try {
			fileNames = getAssets().list("W"+bundle.getInt("WORLD"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String name:fileNames){
			if(name.startsWith("Lab")){
				lvl_Number++;  
			}
		}
		return;
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