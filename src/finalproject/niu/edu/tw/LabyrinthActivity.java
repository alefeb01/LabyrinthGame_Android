package finalproject.niu.edu.tw;



import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LabyrinthActivity extends Activity {

	Bundle bundle;
	Intent intent;
    // Victory Dialog
	protected int tictimer = -1;
    public static final int VICTORY_DIALOG = 0;
    // Defeat Dialog
    public static final int DEFEAT_DIALOG = 1;
    //Cheat Dialog
    public static final int CHEAT_DIALOG = 2;
    public static final int CHEAT_ACCESS_DIALOG = 3;

    public static final String PREFS_NAME = "sharedPreferences.txt";
    public static final int MINUTE_FOR_A_LIFE = 5;
    public static final int SECOND_PER_MINUTE = 60;
    public static final int TIME_FOR_A_LIFE_S = MINUTE_FOR_A_LIFE * SECOND_PER_MINUTE;

    // Graphiq motor 
    private LabyrinthView mView = null;
    // Phys motor
    private LabyrinthEngine mEngine = null;

	// sound effect
	public MediaPlayer mpDefeat = null;
	public MediaPlayer mpVictory = null;
	
    TextView tLife;
    TextView tTime;
    EditText etCode;
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

//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        intent = this.getIntent();
        bundle = intent.getExtras();
        mpDefeat = MediaPlayer.create(this, R.raw.defeat);
        mpVictory = MediaPlayer.create(this, R.raw.victory);
        settings =getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        
        //Get currentlvl
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
	        //Log.i("LOG",Integer.toString(iLife));
	    }
	    else{
	    	
	      	iLife = Integer.parseInt(settings.getString("LIFE", ""));
	      	
	    }
	    //Time Line
	    if(!settings.contains("TIME")){ 
	    	
	        editor.putString("TIME", "0");
	        editor.commit();
	    }
	    else{
	    	if(iLife <5){
	    	lTime_sec = Long.parseLong(settings.getString("TIME", ""));
	    	}
	    }
	    
	  //declare like this


	    // call in oncreate()
	    handlerTime.postDelayed(runnable, 20); //20ms	    
        //Drawing widgets...TEST2
        FrameLayout LabyrinthGame = new FrameLayout(this);
        mView = new LabyrinthView(this);
        LinearLayout LabyrinthWidgets = new LinearLayout (this);
        LabyrinthWidgets.setOrientation(0);	// LinearLayout by vertical
        LinearLayout LabyrinthMenu = new LinearLayout (this);
        LabyrinthMenu.setOrientation(1);	// LinearLayout by vertical
        Button StartGameButton = new Button(this);
        Button PauseGameButton = new Button(this);
        Button EndGameButton = new Button(this);   

        

        tLife = new TextView(this);
        tTime = new TextView(this);
        TextView tMenu = new TextView(this);
        


        
        //END GAME
       // EndGameButton.setWidth(300);
        EndGameButton.setText("End Game");
        EndGameButton.setY(100);
        EndGameButton.setOnClickListener(new Button.OnClickListener()
        { 
          public void onClick(View v)
          {
        	  LabyrinthActivity.this.finish();
          }
        });
        
        //Start GAME
      //  StartGameButton.setWidth(300);
        StartGameButton.setText("Start Game");
        StartGameButton.setY(100);
        StartGameButton.setOnClickListener(new Button.OnClickListener()
        { 
          public void onClick(View v)
          {
        	  mEngine.resume();
          }
        });
        
        //PAUSE GAME
        PauseGameButton.setY(100);
        PauseGameButton.setText("Pause Game");
        PauseGameButton.setOnClickListener(new Button.OnClickListener()
        { 
          public void onClick(View v)
          {
        	 mEngine.stop();
          }
        });
        //Menu Text
        tMenu.setText("Level"+bundle.getInt("LEVEL")+"\nMENU");
        
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
//l

        LabyrinthWidgets.addView(tLife);
        LabyrinthWidgets.addView(tTime);
        LabyrinthMenu.addView(tMenu);           
        LabyrinthMenu.addView(StartGameButton);  
        LabyrinthMenu.addView(PauseGameButton); 
        LabyrinthMenu.addView(EndGameButton); 
        if(bundle.getInt("LEVEL")==999)
        {
            Button SetAccessButton = new Button(this);
            Button GetLifeButton = new Button(this);
            //Cheatlvl GAME
            SetAccessButton.setText("SetAccess");
            SetAccessButton.setY(100);
            SetAccessButton.setOnClickListener(new Button.OnClickListener()
            { 
              @SuppressWarnings("deprecation")
              public void onClick(View v)
              {//FUll access
            	  LabyrinthActivity.this.showDialog(LabyrinthActivity.CHEAT_ACCESS_DIALOG);
              }
            });
            
            //Cheatlife GAME
            GetLifeButton.setText("LifeAbundance");
            GetLifeButton.setY(100);
            GetLifeButton.setOnClickListener(new Button.OnClickListener()
            { 
              public void onClick(View v)
              {//FUll llife
            	  iLife= 5;
                  editor.putString("LIFE", Integer.toString(iLife));
                  editor.commit();
                  tTime.setText("(^_^)");
                  tLife.setText(Integer.toString(iLife));
              	Toast.makeText(LabyrinthActivity.this, "You Got Your Life Back", Toast.LENGTH_SHORT).show();
		           
              }
            });
            LabyrinthMenu.addView(SetAccessButton); 
            LabyrinthMenu.addView(GetLifeButton); 
        }

//
        LabyrinthGame.addView(mView, 720, 480);
        LabyrinthGame.addView(LabyrinthWidgets);
        LabyrinthMenu.setPadding(720, 0, 0, 0);
        LabyrinthGame.addView(LabyrinthMenu);
        
        setContentView(LabyrinthGame);
        mEngine = new LabyrinthEngine(this);
        Ball b = new Ball();
        mView.setBall(b);
        mEngine.setBall(b);

        List<Bloc> mList = mEngine.buildLabyrinthe("W"+bundle.getInt("LEVEL")/10+"/Lab"+bundle.getInt("LEVEL")+".txt");
        mView.setBlocks(mList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEngine.stop();

    } 

    @Override
    protected void onPause() {
        super.onStop();
        mEngine.stop();
    }
    @Override
    public void onStart(){
      super.onStart();
    }

    @Override
    public void onStop(){
      super.onStop();
    }
    @Override
    public void onRestart(){
      super.onRestart();
    }
    @Override
    public void onDestroy(){
      super.onDestroy();
    }
    @Override
    public Dialog onCreateDialog (int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
        case CHEAT_ACCESS_DIALOG:
        	etCode = new EditText(this);
        	etCode.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        	LinearLayout ll = new LinearLayout(this);
        	ll.setOrientation(1);
        	ll.addView(etCode);
            builder.setCancelable(false)
            .setMessage("Enter lvlAccess limit")
            .setTitle("CheatBox !")
            .setView(ll)
            .setNeutralButton("Valid", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	//Log.i("LOG",etCode.getText().toString());
                    editor.putString("CURRENT_LVL", etCode.getText().toString());
                    editor.commit();
                	Toast.makeText(LabyrinthActivity.this, "ACCESS GRANTED UNTIL "+etCode.getText().toString(), Toast.LENGTH_SHORT).show();
		               
                }
            });
            break;   
        case VICTORY_DIALOG:
        	mpVictory.start();
            builder.setCancelable(false)
            .setMessage("Victory!")
            .setTitle("Good News !")
            .setNeutralButton("Return", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	if(bundle.getInt("LEVEL")!=999
                	  &&bundle.getInt("LEVEL")==iCurrentLvl)
                	{
                		iCurrentLvl +=1;
                        editor.putString("CURRENT_LVL", Integer.toString(iCurrentLvl));
                        editor.commit();
                	}

                    LabyrinthActivity.this.finish();
                }
            });
            break;

        case DEFEAT_DIALOG:
        	mpDefeat.start();
            builder.setCancelable(false)
            .setMessage("Defeat!")
            .setTitle("Bad News...")
            .setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	if(iLife == 5)
                	{
                		lTime_sec = System.currentTimeMillis()/1000;
                		editor.putString("TIME", Long.toString(lTime_sec));
                		editor.commit();
                	}
                    iLife -=1;
                    if(iLife <= 0)
                    {
                    	iLife = 0;
	                    tLife.setText(Integer.toString(iLife));
	                    editor.putString("LIFE", Integer.toString(iLife));
	                    editor.commit();
	                    LabyrinthActivity.this.finish();
                    }else{
	                    tLife.setText(Integer.toString(iLife));
	                    editor.putString("LIFE", Integer.toString(iLife));
	                    editor.commit();
	                    mEngine.reset();
	                    mEngine.stop();
	                    LabyrinthActivity.this.recreate();
                    }
                }
            });
            break;
            
        }
        return builder.create();
    }

    @Override
    public void onPrepareDialog (int id, Dialog box) {
      // If dialog then stop
        mEngine.stop();
    }
    
    //inside this function which u have to run
    private Runnable runnable = new Runnable() {
        public void run() {
        	tictimer = (tictimer+1) % 150;
        	run_Coordinates_updates();
        	if(tictimer % 50 == 0){
        		//Modif Affichage Life + time
        		run_TimeLife_update();  
        		if(tictimer %150 == 0){
        			mEngine.getmLaser().run_ChangeStatus();
        		}
        	}
        	
        	
        	handlerTime.postDelayed(this, 20);
        }

		private void run_Coordinates_updates() {
			// TODO Auto-generated method stub
			
		}

		private void run_TimeLife_update() {
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
		}
    };
}