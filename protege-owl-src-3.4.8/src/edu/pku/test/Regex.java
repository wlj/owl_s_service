package edu.pku.test;

import java.util.regex.Pattern;

public class Regex {
	
	public static void main(String[] args) {
		
//		// 生成一个Pattern,同时编译一个正则表达式 
//		Pattern p = Pattern.compile("[/]+"); 
//		
//		//用Pattern的split()方法把字符串按"/"分割 
//		String[] result = p.split( 
//		"Kevin has seen《LEON》seveal times,because it is a good film." 
//		+"/ 凯文已经看过《这个杀手不太冷》几次了，因为它是一部" 
//		+"好电影。/名词:凯文。");
//		
//		for (int i=0; i<result.length; i++) 
//			System.out.println(result[i]); 
		
		String str = "[a[b[cd]e]f]";
		System.out.println(str.replace("[", "").replace("]", ""));
	}
}
