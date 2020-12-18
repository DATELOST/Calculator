package team.seventeen.graphcalculator;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
//逆波兰表达式实现计算器
public class Calc{
    public static int calc(String str){
        Stack<String> operator = new Stack<>();//操作符栈
        List<String> list = new ArrayList<>();//结果集
        String expression="";
        if(str.length()>0)expression+=str.charAt(0);
        for(int i=0;i<str.length();++i){
            if(i>0&&str.charAt(i)==str.charAt(i-1)&&str.charAt(i)==' ')continue;
            expression+=" "+str.charAt(i);
        }
        String[] s = expression.split(" ");
        Stack<Integer> nums = new Stack<>();
        try {
            for (int i = 0; i < s.length; i++) {
                if (s[i].matches("\\d+")) list.add(s[i]);
                else if (s[i].equals("("))operator.push(s[i]);
                else if (s[i].equals(")")){
                    while (!operator.peek().equals("(")) list.add(operator.pop());
                    operator.pop();
                }else{
                    while (!operator.isEmpty() && priority(operator.peek()) >= priority(s[i]))
                        list.add(operator.pop());
                    operator.push(s[i]);
                }
            }
            while (!operator.isEmpty()) list.add(operator.pop());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals("+")) {
                    nums.push(nums.pop() + nums.pop());
                } else if (list.get(i).equals("-")) {
                    nums.push(-(nums.pop() - nums.pop()));
                } else if (list.get(i).equals("*")) {
                    nums.push(nums.pop() * nums.pop());
                } else if (list.get(i).equals("/")) {
                    int num1 = nums.pop();
                    int num2 = nums.pop();
                    nums.push(num2 / num1);
                } else nums.push(Integer.parseInt(list.get(i)));
            }
        }catch (NumberFormatException e) { }
        catch (ArithmeticException e){ nums.push(2333333);}
        catch (EmptyStackException e){ }
        if(!nums.empty())return nums.pop();
        else return 2333333;
    }
    //计算符优先级
    public static int priority(String oper){
        if (oper.equals("+") || oper.equals("-")) return 0;
        else if (oper.equals("*") || oper.equals("/")) return 1;
        else return -1;
    }
}
