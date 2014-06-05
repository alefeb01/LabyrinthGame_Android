package finalproject.niu.edu.tw;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LabyrinthView extends SurfaceView implements SurfaceHolder.Callback {
    Ball mBall;
    public Ball getBall() {
        return mBall;
    }

    public void setBall(Ball pBall) {
        this.mBall = pBall;
    }

    SurfaceHolder mSurfaceHolder;
    DrawingThread mThread;

    private List<Bloc> mBlocks = null;
    public List<Bloc> getBlocks() {
        return mBlocks;
    }

    public void setBlocks(List<Bloc> pBlocks) {
        this.mBlocks = pBlocks;
    }

    Paint mPaint; 

    public LabyrinthView(Context pContext) {
        super(pContext);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mThread = new DrawingThread();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

        mBall = new Ball();
    }

    @Override
    protected void onDraw(Canvas pCanvas) {
        // Draw Background first
        pCanvas.drawColor(Color.CYAN);
        mPaint.setStrokeWidth(0);
        if(mBlocks != null) {
            // Draw Bloks
            for(Bloc b : mBlocks) {
            	mPaint.setColor(b.getCouleur());
                switch(b.getType()) {
                case START:                    
                    pCanvas.drawRect(b.getRectangle(), mPaint);
                    break;
                case ARRIVE:
                    pCanvas.drawRect(b.getRectangle(), mPaint);
                    break;
                case BORDURE:
                    pCanvas.drawRect(b.getRectangle(), mPaint);
                    break;
                case HOLE:
                    pCanvas.drawRect(b.getRectangle(), mPaint);
                    break;
                case NEUTRAL:
                	break;
                case PORTAL:
                	pCanvas.drawRect(b.getRectangle(), mPaint);
                	break;
                case TRIGGER:
                	pCanvas.drawCircle(b.getRectangle().centerX(),b.getRectangle().centerY(),b.getRectangle().height()/2, mPaint);
                	break;
                case GATE:
                	pCanvas.drawRect(b.getRectangle(), mPaint);
                	break;	
                case SWITCH:
                	pCanvas.drawCircle(b.getRectangle().centerX(),b.getRectangle().centerY(),b.getRectangle().height()/2, mPaint);
                }
            }
        }

        // Draw ball
        if(mBall != null) {
            mPaint.setColor(mBall.getCouleur());
            pCanvas.drawCircle(mBall.getX(), mBall.getY(), Ball.R, mPaint);
        }
    }

    public void surfaceChanged(SurfaceHolder pHolder, int pFormat, int pWidth, int pHeight) {
        //
    }

    public void surfaceCreated(SurfaceHolder pHolder) {
        if (mThread.getState() == Thread.State.TERMINATED)
        {//Correct Resume of the surface View (if not thread create twice => fail)
        	 mThread = new DrawingThread();
        }
        mThread.keepDrawing = true;
        mThread.start();
        // Need her position
        if(mBall != null ) {
            this.mBall.setHeight(getHeight());
            this.mBall.setWidth(getWidth());
        }
    }

    public void surfaceDestroyed(SurfaceHolder pHolder) {
        mThread.keepDrawing = false;
        boolean retry = true;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
        
    }

    private class DrawingThread extends Thread {
        boolean keepDrawing = true;

        @Override
        public void run() {
            Canvas canvas;
            while (keepDrawing) {
                canvas = null;

                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    synchronized (mSurfaceHolder) {
                        onDraw(canvas);
                    }
                } finally {
                    if (canvas != null)
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                }

                // 50fps
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {}
            }
        }
    }
}