package countPrime;

import java.util.ArrayList;

public class Printprime {

	public static void main(String[] args) {
		int i=0;
		int num=0;
		ArrayList<Integer> primeNo = new ArrayList<Integer>();
		for (i = 1; i <= 100; i++) 
		 {
			 int count=0;
			 for(num =i; num>=1; num--)
			 {
				 if(i%num==0)
				 {
					 count++;
				 }
			 }
			 if(count==2)
			 {
				primeNo.add(i);
				
			 }
		 }
		 System.out.println("Prime nos from 1 to 100 are:" + primeNo);
			
}
}