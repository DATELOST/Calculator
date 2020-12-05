package team.seventeen.graphcalculator;

import java.util.ArrayList;

public class Cache {
    public static ArrayList<String>f=new ArrayList<>();
    public static ArrayList<String> getF() {
        return f;
    }
    public static void add(String str) { f.add(str);}
    public static void del() { if(f.size()>0)f.remove(f.size()-1);}
    public static void setF(ArrayList<String> ff) {
        f = ff;
    }
}
