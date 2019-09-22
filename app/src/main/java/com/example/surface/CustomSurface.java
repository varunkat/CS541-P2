package com.example.surface;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class CustomSurface extends SurfaceView implements SurfaceHolder.Callback {

    MySurfaceThread thread;
    String tag = "debugging";
    Paint paint1;
    float x,y;
    boolean run = true;

    public CustomSurface(Context Context) {
        super(Context);
        init();
    }

    private void init() {
        thread = new MySurfaceThread(getHolder(),this);
        getHolder().addCallback(this);
        paint1 = new Paint();
        paint1.setTextSize(40);

    }

    public CustomSurface(Context Context, AttributeSet att) {
        super(Context,att);
        init();
    }

    public CustomSurface(Context Context,AttributeSet att, int defstyle ) {
        super(Context,att,defstyle);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread.execute((Void[])null);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawRGB(255,0 ,0);
        canvas.drawLine(0,0, canvas.getWidth(),canvas.getHeight(),paint1);
       canvas.drawText(Float.toString(x),30,30,paint1);
       canvas.drawText(Float.toString(y),30,100,paint1);
        Log.i(tag, "onDraw"+Float.toString(x)+""+Float.toString(y));
    }


    public class MySurfaceThread extends AsyncTask<Void, Void, Void>{

        SurfaceHolder msurfaceholder;
        CustomSurface csurfaceview;

        public MySurfaceThread(SurfaceHolder sh, CustomSurface cs){
            msurfaceholder = sh;
            csurfaceview = cs;
            csurfaceview.setOnTouchListener(new OnTouchListener(){
                public boolean onTouch(View v, MotionEvent event){
                    x = event.getX();
                    y = event.getY();
                    return true;


            }
        });
    }

        @Override
        protected Void doInBackground(Void... voids) {
            while(run) {
                Canvas canvas = null;
                try {
                    canvas = msurfaceholder.lockCanvas(null);
                    synchronized (msurfaceholder) {
                        csurfaceview.onDraw(canvas);
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                } finally {
                    if (canvas != null) {
                        msurfaceholder.unlockCanvasAndPost(canvas);
                    }
                }
            }
            return null;
        }
    }
}
