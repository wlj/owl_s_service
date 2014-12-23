package edu.pku.util;

public class Add {
	
	public int add(int[] a, int length) {
		int res = 0;
		for (int i = 0; i < length; i++) res += a[i];
		return res;
	}

	public static void main(String[] args) {
		
		Add add_ = new Add();
		
		int[] basis = new int[6];
		basis[0] = 133;
		basis[1] = 69;
		basis[2] = 158;
		basis[3] = 104;
		basis[4] = 172;
		
		basis[5] = add_.add(basis, 5);
		
		int[] model = new int[7];
		model[0] = 23;
		model[1] = 101;
		model[2] = 357;
		model[3] = 219;
		model[4] = 161;
		model[5] = 51;
		
		model[6] = add_.add(model, 6);
		
		int[] condition = new int[3];
		condition[0] = 757;
		condition[1] = 465;
		
		condition[2] = add_.add(condition, 2);
		
		int[] effect = new int[2];
		effect[0] = 754;
		
		effect[1] = add_.add(effect, 1);
		
		int[] recall = new int[5];
		recall[0] = 113;
		recall[1] = 93;
		recall[2] = 34;
		recall[3] = 11;
		
		recall[4] = add_.add(recall, 4);
		
		int[] dos = new int[2];
		dos[0] = 539;
		
		dos[1] = add_.add(dos, 1);
		
		int[] func = new int[7];
		func[0] = 190;
		func[1] = 71;
		func[2] = 118;
		func[3] = 618;
		func[4] = 33;
		func[5] = 185;
		
		func[6] = add_.add(func, 6);
		
		int[] qos = new int[2];
		qos[0] = 13;
		
		qos[1] = add_.add(qos, 1);
		 
		int[] test = new int[4];
		test[0] = 29;
		test[1] = 36;
		test[2] = 25;
		
		test[3] = add_.add(test, 3);
		
		int[] util = new int[10];
		util[0] = 137;
		util[1] = 10;
		util[2] = 34;
		util[3] = 265;
		util[4] = 339;
		util[5] = 141;
		util[6] = 54;
		util[7] = 63;
		util[8] = 63;
		
		util[9] = add_.add(util, 9);
		
		System.out.println(basis[5]);
		System.out.println(model[6]);
		System.out.println(condition[2]);
		System.out.println(effect[1]);
		System.out.println(recall[4]);
		System.out.println(dos[1]);
		System.out.println(func[6]);
		System.out.println(qos[1]);
		System.out.println(test[3]);
		System.out.println(util[9]);
		
		System.out.println("total:");
		
		System.out.println(basis[5] + model[6] + condition[2] + effect[1] + recall[4] + dos[1] + func[6] + qos[1] + test[3] + util[9]);
		
	}
}
