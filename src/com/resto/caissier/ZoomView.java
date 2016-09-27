package com.resto.caissier;

import Model.Breadcrumb;
import Parser.OrderParser;
import Parser.PlanTableParser;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;

public class ZoomView extends View {

    public static float MIN_ZOOM = 0.5f;
    public static float MAX_ZOOM = 5f;
    public static int NONE = 0;
    public static int DRAG = 1;
    public static int ZOOM = 2;
    public int mode;
    public int i;
    
    public boolean dragged = true;
    public float startX = 0f;
    public float startY = 0f;

    public float translateX = 0f;
    public float translateY = 0f;
    
    public float previousTranslateX = 0f;
    public float previousTranslateY = 0f;
    public float displayWidth=10;
    public float displayHeight=10;
    public Canvas canvas;
    public Bitmap bg;
    public float scaleFactor = 1.f;
    public ScaleGestureDetector detector;
    public PlanFragment planFragment;

    @SuppressWarnings("deprecation")
	public ZoomView(Context context, PlanFragment planFragment) {
        super(context);
        this.planFragment=planFragment;
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        displayWidth = display.getWidth();
        displayHeight = display.getHeight();
        bg = Bitmap.createBitmap(Math.round(displayWidth),Math.round(displayHeight), Bitmap.Config.ARGB_8888);
   	 	canvas = new Canvas(bg);
        
        Log.i("displayWidth",""+displayWidth);
        
    }

    @SuppressLint("ClickableViewAccessibility") @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
		boolean click = false;
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) 
    	{
        	case MotionEvent.ACTION_DOWN:
        		Log.i("ACTION_DOWN","ACTION_DOWN");
        		mode = ZoomView.DRAG;
        		startX = event.getX() - previousTranslateX;
        		startY = event.getY() - previousTranslateY;
            break;
        	case MotionEvent.ACTION_MOVE:
        		Log.i("ACTION_MOVE","ACTION_MOVE");
        		translateX = event.getX() - startX;
        		Log.i("zv.translateX",""+translateX);
        		translateY = event.getY() - startY;
        		Log.i("zv.translateY",""+translateY);
                double distance = Math.sqrt(Math.pow(event.getX() - (startX + previousTranslateX), 2) + Math.pow(event.getY() - (startY + previousTranslateY), 2));
                if(distance > 0)
                {
                	Log.i("zv.dragged",""+dragged);
                	dragged = true;
                }        
            break;
        	case MotionEvent.ACTION_POINTER_DOWN:
        		Log.i("ACTION_POINTER_DOWN","ACTION_POINTER_DOWN");
        		mode = ZoomView.ZOOM;
            break;
        	case MotionEvent.ACTION_UP:
        		Log.i("ACTION_UP","ACTION_UP");
        		mode = ZoomView.NONE;
        		dragged = false;
        		previousTranslateX = translateX;
        		previousTranslateY = translateY;
        		click=true;
        		Log.i("event", "event");
            break;
        	case MotionEvent.ACTION_POINTER_UP:
        		Log.i("ACTION_POINTER_UP","ACTION_POINTER_UP");
        		mode = ZoomView.DRAG;
        		previousTranslateX = translateX;
        		previousTranslateY = translateY;
            break;
    	}
    	
    	
		detector.onTouchEvent(event);
        if (( dragged) || mode == ZoomView.ZOOM) {
        	Log.i("invalide","invalide");
        	invalidate();
        }
		if(click && !planFragment.parser.tables.isEmpty())
		{
			boolean ok=false;
			i=0;
			do{
				float x=event.getX();
				float y=event.getY();
				x=x-translateX;
				x=x*scaleFactor;
				
				y=y-translateY;
				y=y*scaleFactor;
				ok=planFragment.parser.tables.get(i).isClicked(x,y);
				i++;
				
			}while(!ok && i<planFragment.parser.tables.size());
			if(ok){
				ok=false;
				MainActivity.tableOpen=planFragment.parser.tables.get(i-1);
				Log.i("Table", MainActivity.tableOpen.getName());
				if(planFragment.parser.tables.get(i-1).getIsOpen()==0)
				{
					Handler handProd = new Handler() {
						public void handleMessage(Message msg) {
						
						}
					};
					planFragment.orderParser = new OrderParser("order/"+planFragment.parser.tables.get(i-1).getTable_id(), handProd);
					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(planFragment.getActivity());
					String token = prefs.getString("token", "");
					planFragment.orderParser.fetchJSON(token);
				}
				else
				{
					if(MainActivity.clic==0){
						MainActivity.clic=1;
						Breadcrumb br=new Breadcrumb();
						br.setTitle("Table"+planFragment.parser.tables.get(i-1).getName());
						br.setId(0);
						MainActivity.breadcrumb.clear();
						MainActivity.breadcrumb.add(br);
						
						final TableFragment carteFragment = new TableFragment(planFragment.parser.tables.get(i-1).getTable_id(),planFragment.parser.tables.get(i-1).getName());
						//MainActivity.mDrawerLayout.closeDrawer(MainActivity.mDrawerList);
						if (carteFragment != null) {
							Handler handProd = new Handler() {
								public void handleMessage(Message msg) {
									MainActivity.tableOpen = planFragment.parser.tables.get(i-1);
									MainActivity.order= planFragment.orderParser.order;
									MainActivity.order=planFragment.orderParser.order;
									FragmentManager fragmentManager = planFragment.getFragmentManager();
									fragmentManager.beginTransaction()
											.setCustomAnimations(R.animator.right_to_left, R.animator.left_to_right)
											.replace(R.id.fragmentContainer, carteFragment)						
											.commit();
								}
							};
							planFragment.orderParser = new OrderParser("order/"+planFragment.parser.tables.get(i-1).getTable_id(), handProd);
							SharedPreferences prefs = PreferenceManager
									.getDefaultSharedPreferences(planFragment.getActivity());
							String token = prefs.getString("token", "");
							planFragment.orderParser.fetchJSON(token);
					}
					}
				}							
			}
		}
		return true;
	
    }

    @Override
    public void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	canvas.save();
    	Matrix mat= canvas.getMatrix();
    	mat.postScale(scaleFactor, scaleFactor);
    	mat.postTranslate(translateX, translateY);
        canvas.drawBitmap (bg,mat,null);
        canvas.restore();
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            return true;
        }
    }
}