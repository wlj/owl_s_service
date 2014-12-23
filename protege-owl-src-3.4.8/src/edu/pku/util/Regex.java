package edu.pku.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

	/** 
     * ������ڸ�ʽ 
     * @param date 
     * @return 
     */  
    public static boolean checkDate(String date) {  
        String eL = "^((//d{2}(([02468][048])|([13579][26]))[//-/////s]?((((0?[13578])|(1[02]))[//-/////s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[//-/////s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[//-/////s]?((0?[1-9])|([1-2][0-9])))))|(//d{2}(([02468][1235679])|([13579][01345789]))[//-/////s]?((((0?[13578])|(1[02]))[//-/////s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[//-/////s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[//-/////s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(//s(((0?[0-9])|([1][0-9])|([2][0-3]))//:([0-5]?[0-9])((//s)|(//:([0-5]?[0-9])))))?$";  
        Pattern p = Pattern.compile(eL);  
        Matcher m = p.matcher(date);  
        boolean b = m.matches();  
        return b;  
    }  
    
    /** 
     * ������� 
     * @param num 
     * @param type "0+":�Ǹ����� "+":������ "-0":�������� "-":������ "":���� 
     * @return 
     */  
    public static boolean checkNumber(String num,String type){  
        String eL = "";  
        if(type.equals("0+"))eL = "^//d+$";//�Ǹ�����  
        else if(type.equals("+"))eL = "^//d*[1-9]//d*$";//������  
        else if(type.equals("-0"))eL = "^((-//d+)|(0+))$";//��������  
        else if(type.equals("-"))eL = "^-//d*[1-9]//d*$";//������  
        else eL = "^-?//d+$";//����  
        Pattern p = Pattern.compile(eL);  
        Matcher m = p.matcher(num);  
        boolean b = m.matches();  
        return b;  
    }  
    
    /** 
     * ��鸡���� 
     * @param num 
     * @param type "0+":�Ǹ������� "+":�������� "-0":���������� "-":�������� "":������ 
     * @return 
     */  
    public static boolean checkFloat(String num,String type){  
        String eL = "";  
        if(type.equals("0+"))eL = "^//d+(//.//d+)?$";//�Ǹ�������  
        else if(type.equals("+"))eL = "^((//d+//.//d*[1-9]//d*)|(//d*[1-9]//d*//.//d+)|(//d*[1-9]//d*))$";//��������  
        else if(type.equals("-0"))eL = "^((-//d+(//.//d+)?)|(0+(//.0+)?))$";//����������  
        else if(type.equals("-"))eL = "^(-((//d+//.//d*[1-9]//d*)|(//d*[1-9]//d*//.//d+)|(//d*[1-9]//d*)))$";//��������  
        else eL = "^(-?//d+)(//.//d+)?$";//������  
        Pattern p = Pattern.compile(eL);  
        Matcher m = p.matcher(num);  
        boolean b = m.matches();  
        return b;  
    }  
    
    public static void main(String[] args) {
    	
    }
}
 