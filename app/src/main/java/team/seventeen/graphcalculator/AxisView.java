package team.seventeen.graphcalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AxisView extends View implements View.OnTouchListener{
    float PI=(float)Math.PI;
    float canvasWidth,canvasHeight; //画布宽、高
    float width,height;             //自定义长宽
    float left,up;                  //自定义左上角位置
    ArrayList<String> f;            //输入字符串
    ArrayList<Expression> ff;       //函数表达式
    public ArrayList<String> getF() { return f; }
    public void setF(ArrayList<String> f) { this.f = f; }
    public ArrayList<Expression> getFf() { return ff; }
    public void setFf(ArrayList<Expression> f) { this.ff = f; }

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
        ff=new ArrayList<>();
        getExpressions();
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
    //画曲线函数
    void DrawCurve(Canvas canvas,Paint paint){//绘制曲线
        Random ra =new Random();
        ArrayList<Integer>color=new ArrayList<>();
        for(int i=0;i<ff.size()*3;++i) color.add(ra.nextInt(256));
        int op=0;
        if(ff.size()==2)++op;
        ArrayList<Pair<Float,Float>>p=new ArrayList<>();
        for(float x=left;x<left+width;x+=0.001f){
            for(int i=0;i<ff.size();++i){
                float X=x*2;
                float y=F(X,i)/2;
                paint.setARGB(255,color.get(i*3),color.get(i*3+1),color.get(i*3+2));
                DrawPoint(canvas,x,y,paint);
            }
            if(op==1&&Math.abs(F(x,0)-F(x,1))<0.001f&&p.size()<4){
                int sz=p.size();
                if(sz>0&&Math.abs(p.get(sz-1).first-x)<0.01f&&Math.abs(p.get(sz-1).second-F(x,0))<0.01f){}
                else p.add(new Pair<Float, Float>(x,F(x,0)));
            }
        }
        if(p.size()>0) {
            String str="交点";
            str+="("+String.format("%.2f",p.get(0).first+0.001f)+","+String.format("%.2f",p.get(0).second+0.001f)+") ";
            if(p.size()>1)
                str+="("+String.format("%.2f",p.get(1).first+0.001f)+","+String.format("%.2f",p.get(1).second+0.001f)+") ";
            if(p.size()>2)
                str+="("+String.format("%.2f",p.get(2).first+0.001f)+","+String.format("%.2f",p.get(2).second+0.001f)+") ";
            Toast toast=Toast.makeText(this.getContext(),str, Toast.LENGTH_LONG);
            showMyToast(toast,11*1000);
        }
    }
    //将输入函数表达式f(x)=∑ax^b+c的abc数据存储在p中
    void getExpressions(){
        for(int i=0;i<f.size();++i){
            int flag=0;
            Expression ex=new Expression();
            try {
                String str=f.get(i);
                if(str.length()<3)continue;
                if(str.charAt(0)!='y'||str.charAt(1)!='=')throw new Exception("抛出异常");
                str=str.substring(2);
                str=str.toLowerCase();
                str='#'+str+'#';//判断结束
                for (int j = 0; j < str.length(); ++j) {
                    char c = str.charAt(j);
                    if (c == 'x') {
                        float a = (float) 1.0,b = (float) 1.0;
                        if (str.charAt(j - 1) != '#') {
                            String aa = "";
                            for (int k = j - 1; k >= 0; --k) {
                                char ch = str.charAt(k);
                                if (ch == '#'||ch=='+') break;
                                else if (ch == '-') {
                                    aa = ch + aa;break;
                                }else aa = ch + aa;
                            }
                            if(aa.length()==1&&aa.charAt(0)=='-')aa+="1";
                            if(aa.length()>0)a = Float.parseFloat(aa);
                        }
                        if (str.charAt(j + 1) == '^') {
                            String bb = "";
                            int k;
                            for (k = j + 2; k < str.length(); ++k) {
                                char ch = str.charAt(k);
                                if (k == j + 2 && ch == '-') {//负数
                                    bb += ch;continue;
                                }
                                if (ch == '+' || ch == '-' ||ch=='#') break;
                                bb += ch;
                            }
                            j=k-1;
                            b = Float.parseFloat(bb);
                        }
                        ex.getP().add(new Pair<Float, Float>(a, b));
                        flag = 1;
                    }
                    if (c == '+' || c == '-' ||c=='#') {
                        if (str.indexOf('x', j + 1) != -1) continue;
                        String cc = "";
                        for (int k = j; k < str.length(); ++k) {
                            char ch = str.charAt(k);
                            if (ch == '#') break;
                            else if(ch!='+')cc += ch;
                        }
                        if(cc!="")ex.setC(Float.parseFloat(cc));
                        flag = 1;
                    }
                }
            }catch (NumberFormatException e) {
                    Toast.makeText(this.getContext(),"函数表达式错误", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this.getContext(),"函数表达式错误", Toast.LENGTH_SHORT).show();
            }
            if(flag==1)ff.add(ex);
        }
    }
    //通过表达式计算Y值
    float F(float x,int index){ return ff.get(index).f(x); }
    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() { toast.show(); }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
}

