package team.seventeen.graphcalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AxisView extends View implements View.OnTouchListener{
    float PI=(float)Math.PI;
    float canvasWidth,canvasHeight; //画布宽、高
    float width,height;             //自定义长宽
    float left,up;                  //自定义左上角位置
    ArrayList<String> f;                       //函数表达式
    public AxisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    //将自定义坐标转换成画布坐标的函数
    float PX(float x){return (x-left)*canvasWidth/width;}
    float PY(float y){return (up-y)*canvasHeight/height;}
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Init(canvas);
        Render(canvas);
    }
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return false;
    }
    //初始化全局参数。
    void Init(Canvas canvas){
        canvasWidth=(float)canvas.getWidth();
        canvasHeight=(float)canvas.getHeight();
        width=2*5;height=2*5*canvasHeight/canvasWidth;
        left=-width/2;up=height/2;
        f=Cache.getF();
    }
    //渲染画面。
    void Render(Canvas canvas){
        //新建画笔
        Paint paint=new Paint();
        //画网格线。
        paint.setARGB(255,50,50,50);
        DrawGrid(canvas,0.5f,0.5f,paint);
        //画坐标线。
        paint.setStrokeWidth(4);
        paint.setARGB(255,0,0,0);
        DrawCoord(canvas,paint);
        //显示横、纵坐标轴和原点名称
        paint.setARGB(255,0,0,0);
        DrawCoordName(canvas,paint,"x","f(x)","O");
        //画曲线
        DrawCurve(canvas, paint);
    }
    //画网格线
    void DrawGrid(Canvas canvas,float dx,float dy,Paint paint){
        //画纵向网格线
        float x=0;
        while(x>left){canvas.drawLine(PX(x),PY(up),PX(x),PY(up-height),paint);x-=dx;}
        x=0;
        while(x<width+left){canvas.drawLine(PX(x),PY(up),PX(x),PY(up-height),paint);x+=dx;}
        //画横向网格线
        float y=0;
        while(y<up){canvas.drawLine(PX(left),PY(y),PX(left+width),PY(y),paint);y+=dy;}
        y=0;
        while(y>up-height){canvas.drawLine(PX(left),PY(y),PX(left+width),PY(y),paint);y-=dy;}
    }
    //画坐标线。
    void DrawCoord(Canvas canvas,Paint paint){
        canvas.drawLine(PX(left),PY(0f),PX(left+width),PY(0f),paint);
        canvas.drawLine(PX(0f),PY(up),PX(0f),PY(up-height),paint);
    }
    //显示横、纵坐标轴名称和原点名称。
    void DrawCoordName(Canvas canvas,Paint paint,String xAxisName,String yAxisName,String originName){
        //设置文字大小
        paint.setTextSize(40f);
        //在适当位置显示x,y,O名称
        canvas.drawText(xAxisName,PX(left+width)-30f,PY(0f)+30f,paint);
        canvas.drawText(yAxisName,PX(0f),PY(up)+30f,paint);
        canvas.drawText(originName,PX(0f),PY(0f)+30f,paint);

    }
    //画点函数。使用自定义坐标。
    void DrawPoint(Canvas canvas,float x,float y,Paint paint){canvas.drawPoint(PX(x),PY(y),paint);}
    //通过表达式计算Y值
    float F(float x){
        float y=x;
        y+=(float)f.size();
        return y;
    }
    //画曲线函数
    void DrawCurve(Canvas canvas,Paint paint){//绘制曲线
        paint.setARGB(255,0,0,0);
        for(float x=left;x<left+width;x+=0.001f){
            float y=F(x);
            DrawPoint(canvas,x,y,paint);
        }
        canvas.drawText("f(x)=x+"+f.size(),PX(PI/2),PY((float)Math.sin(PI/2)),paint);
    }

    public void setF(ArrayList<String> f) {
        this.f = f;
    }
}

