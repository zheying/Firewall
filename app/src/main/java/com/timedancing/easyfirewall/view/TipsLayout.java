package com.timedancing.easyfirewall.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.timedancing.easyfirewall.R;

/**
 * Created by zengzheying on 16/1/14.
 */
public class TipsLayout extends RelativeLayout {

	private final int BORDER_STYLE_NONE = 0;
	private final int BORDER_STYLE_DASH = 1;

	private Paint mPaint;
	private Path mPath;
	private int mBorderColor;
	private int mBorderStyle;
	private int mBorderWidth;
	private int mTriangleWidth;
	private int mTriangleHeight;
	private int mDashWidth;
	private int mDashGap;


	public TipsLayout(Context context) {
		this(context, null);
	}

	public TipsLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TipsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		if (null == attrs) {
			throw new IllegalArgumentException("Attributes should be provided to this view,");
		}

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TipsLayout);
		mBorderColor = typedArray.getColor(R.styleable.TipsLayout_tl_border_color,
				context.getResources().getColor(R.color.tipslayout_border_color));
		mBorderStyle = typedArray.getInt(R.styleable.TipsLayout_tl_border_style, BORDER_STYLE_NONE);
		mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.TipsLayout_tl_border_width,
				context.getResources().getDimensionPixelSize(R.dimen.tl_border_width));
		mTriangleWidth = typedArray.getDimensionPixelSize(R.styleable.TipsLayout_tl_triangle_width,
				context.getResources().getDimensionPixelSize(R.dimen.tl_triangle_width));
		mTriangleHeight = typedArray.getDimensionPixelSize(R.styleable.TipsLayout_tl_triangle_height,
				context.getResources().getDimensionPixelSize(R.dimen.tl_triangle_height));
		mDashWidth = typedArray.getDimensionPixelSize(R.styleable.TipsLayout_tl_dash_width,
				context.getResources().getDimensionPixelSize(R.dimen.tl_dash_width));
		mDashGap = typedArray.getDimensionPixelSize(R.styleable.TipsLayout_tl_dash_gap,
				context.getResources().getDimensionPixelSize(R.dimen.tl_dash_gap));
		typedArray.recycle();

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(mBorderColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(mBorderWidth);

		if (mBorderStyle == BORDER_STYLE_DASH) {
			PathEffect effect = new DashPathEffect(new float[]{mDashWidth, mDashGap}, 1);
			mPaint.setPathEffect(effect);
		}

		mPath = new Path();

		setPadding(0, 0, 0, mTriangleHeight);

		setWillNotDraw(false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mPath.reset();
		mPath.moveTo(mBorderWidth, mBorderWidth);
		int y1 = getMeasuredHeight() - mBorderWidth - mTriangleHeight;
		mPath.lineTo(mBorderWidth, y1);
		mPath.lineTo((getMeasuredWidth() - mTriangleWidth) / 2, y1);
		mPath.lineTo(getMeasuredWidth() / 2, getMeasuredHeight() - mBorderWidth);
		mPath.lineTo((getMeasuredWidth() + mTriangleWidth) / 2, y1);
		mPath.lineTo(getMeasuredWidth() - mBorderWidth, y1);
		mPath.lineTo(getMeasuredWidth() - mBorderWidth, mBorderWidth);
		mPath.close();
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawPath(mPath, mPaint);
	}
}
