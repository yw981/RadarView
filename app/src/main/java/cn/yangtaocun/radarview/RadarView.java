package cn.yangtaocun.radarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class RadarView extends View {

    private float mWidth;

    private float mHeight;

    private Paint mPaint, mPaint2, mPaint3, mPaint4, mLinePaint;

    private RectF mRectF;

    private float startAngle = 360;

    private float radius1, radius2, radius3, radius4;
    private Path path;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    public RadarView(Context context) {
        super(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
//        final TypedArray a = getContext().obtainStyledAttributes(
//                attrs, R.styleable.RadarView, defStyle, 0);
//        a.recycle();

        mPaint = new Paint();
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#ffffff"));

        mPaint2 = new Paint();
        mPaint2.setStrokeWidth(5);
        mPaint2.setColor(Color.parseColor("#00ff00"));

        mPaint3 = new Paint();
        mPaint3.setStrokeWidth(3);
        mPaint3.setAntiAlias(true);
        mPaint3.setStyle(Paint.Style.STROKE);
        mPaint3.setColor(Color.parseColor("#ffffff"));

        mPaint4 = new Paint();
        mPaint4.setStrokeWidth(2);
        mPaint4.setAntiAlias(true);
        mPaint4.setStyle(Paint.Style.STROKE);
        mPaint4.setColor(Color.parseColor("#ffffff"));

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor("#ffffff"));

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        mRectF = new RectF((float) (mWidth * 0.1), (float) (mWidth * 0.1),
                (float) (mWidth * 0.9), (float) (mWidth * 0.9));
        // 绘制渐变效果
        LinearGradient gradient = new LinearGradient((float) (mWidth * 0.3),
                (float) (mWidth * 0.9), (float) (mWidth * 0.1),
                (float) (mWidth * 0.5), new int[]{
                Color.parseColor("#458EFD"), Color.GREEN,
                Color.parseColor("#458EFD"), Color.WHITE,
                Color.parseColor("#458EFD")}, null,
                Shader.TileMode.CLAMP);
        mPaint2.setShader(gradient);
        // 四个圆的半径
        radius1 = (float) (mWidth * 0.4);
        radius2 = (float) (mWidth * 0.3);
        radius3 = (float) (mWidth * 0.2);
        radius4 = (float) (mWidth * 0.1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
//        int paddingLeft = getPaddingLeft();
//        int paddingTop = getPaddingTop();
//        int paddingRight = getPaddingRight();
//        int paddingBottom = getPaddingBottom();
//        int contentWidth = getWidth() - paddingLeft - paddingRight;
//        int contentHeight = getHeight() - paddingTop - paddingBottom;

        //canvasArc(canvas);
        canvasArc2(canvas);
        canvasCircle(canvas);
        canvasLine(canvas);
    }

    // 绘制旋转的扇形
    private void canvasArc(Canvas canvas) {
        canvas.drawArc(mRectF, startAngle, 100, true, mPaint2);
    }

    // 绘制旋转的扇形
    private void canvasArc2(Canvas canvas) {
        canvas.drawArc(mRectF, startAngle, 1, true, mPaint3);
    }

    // 绘制四个圆
    private void canvasCircle(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius1, mPaint3);
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius2, mPaint);
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius3, mPaint);
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius4, mPaint4);
    }

    // 绘制虚线
    private void canvasLine(Canvas canvas) {
        int lineCount = 8;
        for (int i = 0; i < lineCount; i++) {
            path.moveTo(mWidth / 2, mHeight / 2);
            path.lineTo(mWidth / 2, mHeight/10);
            PathEffect effects = new DashPathEffect(new float[]{
                    (float) (mWidth * 0.005), (float) (mWidth * 0.02),
                    (float) (mWidth * 0.005), (float) (mWidth * 0.02)}, 0);
            mLinePaint.setPathEffect(effects);
            canvas.drawPath(path, mLinePaint);
            canvas.rotate(45, mWidth / 2, mHeight / 2);
        }
    }

    class MyThread extends Thread {

        @Override
        public void run() {

            while (true) {
                if (running) {
                    SystemClock.sleep(100);
                    handler.sendEmptyMessage(2);
                }
            }

        }
    }

    private boolean running = true;

    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            synchronized (this) {
                if (startAngle < 1) {
                    startAngle = 360;
                } else {
                    startAngle--;
                    invalidate();
                }
            }
        }

    };

    private MyThread thread;

    // 开启动画
    public void setStartAngle() {
        thread = new MyThread();
        thread.start();

    }

    // 重新开启动画
    public void startAnge() {
        running = true;
    }

    // 暂停动画
    public void stopAnge() {
        running = false;
    }

    // 是否在运动
    public boolean isRunning() {
        return running;
    }


}
