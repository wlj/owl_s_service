package edu.pku.test;

import java.util.regex.Pattern;

public class Regex {
	
	public static void main(String[] args) {
		
//		// ����һ��Pattern,ͬʱ����һ��������ʽ 
//		Pattern p = Pattern.compile("[/]+"); 
//		
//		//��Pattern��split()�������ַ�����"/"�ָ� 
//		String[] result = p.split( 
//		"Kevin has seen��LEON��seveal times,because it is a good film." 
//		+"/ �����Ѿ����������ɱ�ֲ�̫�䡷�����ˣ���Ϊ����һ��" 
//		+"�õ�Ӱ��/����:���ġ�");
//		
//		for (int i=0; i<result.length; i++) 
//			System.out.println(result[i]); 
		
		String str = "[a[b[cd]e]f]";
		System.out.println(str.replace("[", "").replace("]", ""));
	}
}
