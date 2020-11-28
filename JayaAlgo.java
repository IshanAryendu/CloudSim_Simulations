package org.cloudbus.cloudsim.examples;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.LongStream;

public class JayaAlgo {
	static int row = 15, i, j, temp, best, worst, count = 0;
	static double fit, flag;
	static int avgMs;

	static double fitness[]=new double [row];
	
	public static int[] init(long burstTime[], int col, int vms){
		double xnew[]=new double[col];
		int workArray[][] = new int[row][col];
		double r1[][]=new double[row][col];
		double r2[][]=new double[row][col];
		long vm[]=new long[vms];
		Random robj = new Random();
		//Create the random matrices
		for(i=0;i<row;i++)
		{
			for(j=0;j<col;j++)
			{
				r1[i][j]=Math.random();
				r2[i][j]=Math.random();
				workArray[i][j]= robj.nextInt(vms);
			}
		}
		//initialize the fitness array
     	for (i=0; i<row; i++) {
     		for(int k=0;k<vms;k++){
     			vm[k] = 0;
     		}
     		for (j=0; j<col; j++) {
     			temp =workArray[i][j];
     			vm[temp]+=burstTime[j];
     		}
     		fitness[i] = calculateFitness(vm, vms);
     	
     	}
     	best=calculateBest(fitness);
     	worst=calculateWorst(fitness);
     	//run the calculations in a loop
     	while(count<2000){
     		for(i=0;i<row;i++)
			{
				for(j=0;j<col;j++)
				{
     				xnew[j]=(workArray[i][j]+r1[i][j]*(workArray[best][j]-Math.abs(workArray[i][j]))-r2[i][j]*(workArray[worst][j]-Math.abs(workArray[i][j])));
     				flag=xnew[j];
     				if(flag<0){
     					xnew[j]=0;
     				}
     				else if (flag > vms-1) {
						xnew[j]=vms-1;
					}
     				else{
     					xnew[j]=Math.round(flag);
     				}
     				temp=(int)xnew[j];
            		vm[temp]+=burstTime[j];
            		
     			}
     			fit = calculateFitness(vm, vms);
     			if(fit>fitness[i])
      			{
					fitness[i]=fit;
         			for(int k = 0; k < col; k++){
         				workArray[i][k]=(int) xnew[k];
         			}
         			
         		}
     			for(int k=0;k<vms;k++){
         			vm[k] = 0;
         		}
       		}
      		best=calculateBest(fitness);
      		worst=calculateWorst(fitness);
      		count++;
      		for(int k=0;k<vms;k++){
     			vm[k] = 0;
     		}
		}
     	//print the final result
     	
		for(j=0;j<col;j++)
 		{
 			System.out.print(workArray[best][j]+ "   ");
 		}
		for(int k=0;k<vms;k++){
            vm[k] = 0;
        }
        for (j=0; j<col; j++) {
            temp =workArray[best][j];
            vm[temp]+=burstTime[j];
        }
        System.out.println();
        for(long a:vm){
        	System.out.print(a+"  ");
        }
		return workArray[best];
	}
	//Calculate the fitness as fitness = (average utilization)/(fitness)
	public static double calculateFitness(long vm[], int vms)
	{
		Arrays.sort(vm);
 		double avg=0.0,sum=0.0,count=0;
 		long ms=vm[vms-1];
 		avgMs+=ms;
 		sum = LongStream.of(vm).sum();
	 	for(long k : vm){
	 		if(k !=0){
	 			count++;
	 		}
	 	}
 		
        avg=sum/(ms*count);
        
        return avg/ms;
	}
	//Calculate the minimum fitness
	public static int calculateWorst( double arr[]) {
     	int res=0;
      	double temp=arr[0];
     	for(int i=1;i<arr.length;i++)
     	{
     		if(arr[i]<temp)
     		{
     			temp=arr[i];
     			res=i;
     		}
     	}
     	return res;
	}
	//Calculate the maximum fitness
 	public static int calculateBest(double ar[])
    {
		int res=0;
 		double temp=0.0;
		for (int i=0;i<ar.length;i++) {
			if(ar[i]>temp)
			{
				temp=ar[i];
				res=i;
			}
		}
		return res;
    }
//		
// 	public static void main(String[] args) {
// 		long burstTime[] = {80,140,80,140,80,140};
// 		
// 			init(burstTime, 6, 3);
//		
//		}
 	
}

