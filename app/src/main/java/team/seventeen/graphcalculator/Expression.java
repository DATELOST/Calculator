package team.seventeen.graphcalculator;

import android.util.Pair;

import java.util.ArrayList;

public class Expression {
    ArrayList<Pair<Float,Float>>p;
    float c;
    Expression(){ p=new ArrayList<>(); c=0;}
    public ArrayList<Pair<Float,Float>>getP(){ return p; }
    public void setC(float c) { this.c = c; }
    public float f(float x){
        float res=c;
        for(int i=0;i<p.size();++i)res+=p.get(i).first*Math.pow(x,p.get(i).second);
        return res;
    }
}
