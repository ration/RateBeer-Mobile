package dk.moerks.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import dk.moerks.ratebeermobile.R;

public class DashboardButton extends ImageView {
	private static final String LOGTAG = "DashboardButton";
	private Drawable drawableFocused;
	private Drawable drawable;
	
	public DashboardButton(Context context) {
		super(context);
	}

	public DashboardButton(Context context, AttributeSet attributes){
		super(context, attributes);
		init(attributes);
		setImageDrawable(drawable);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			setImageDrawable(drawableFocused);
			Log.d(LOGTAG, "ACTION_DOWN");
		} else {
			setImageDrawable(drawable);
			Log.d(LOGTAG, "ACTION_UP");
		}
		return super.onTouchEvent(event);
	}
	
	private void init(AttributeSet attributes) {
		TypedArray a = getContext().obtainStyledAttributes(attributes,R.styleable.DashboardButton);
		drawable = a.getDrawable(R.styleable.DashboardButton_drawable);
		drawableFocused = a.getDrawable(R.styleable.DashboardButton_drawableFocused);
	}
}
