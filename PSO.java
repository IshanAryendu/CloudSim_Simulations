package org.cloudbus.cloudsim.examples;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.LongStream;

public class PSO {
        static int row = 6, i, j, temp, gbest, count = 0;
        static double  flag;


        static double fitness[]=new double [row];
        static double fit[]=new double [row];


        public static int[] initPSO(long burstTime[], int col, int vms){
                double xnew[][]=new double[row][col];
                int pbest[][] = new int[row][col];
                double vel[][]=new double[row][col];
                double w= 0.8+(new Random().nextDouble()*0.4);
                double c1=1+new Random().nextDouble();
                double c2=2+new Random().nextDouble();
                double r1=Math.random();
                double r2=Math.random();


              //  double r2[][]=new double[row][col];
                long vm[]=new long[vms];
                Random robj = new Random();
                //Create the random matrices
                for(i=0;i<row;i++)
                {
                        for(j=0;j<col;j++)
                        {
                                vel[i][j]=Math.random();
                               // r2[i][j]=Math.random();
                                pbest[i][j]= robj.nextInt(vms);
                        }
                }
                //initialize the fitness array
        for (i=0; i<row; i++) {
                for(int k=0;k<vms;k++){
                        vm[k] = 0;
                }
                for (j=0; j<col; j++) {
                        temp =pbest[i][j];
                        vm[temp]+=burstTime[j];
                }
                fitness[i] = calculateFitness(vm, vms);

        }
        gbest=calculateBest(fitness);
        for(i=0;i<row;i++)
        {
            for(j=0;j<col;j++)
            {
                xnew[i][j]=(double)pbest[i][j];
            }
        }
      //  worst=calculateWorst(fitness);
        //run the calculations in a loop
        while(count<100){
                for(i=0;i<row;i++)
                        {
                			for(int k=0;k<vms;k++){
                    		vm[k] = 0;
                			}
                                for(j=0;j<col;j++)
                                {
                                vel[i][j]=w*vel[i][j]+c1*r1*(pbest[i][j]-xnew[i][j])+c2*r2*(pbest[gbest][j]-xnew[i][j]);
                                xnew[i][j]=xnew[i][j]+Math.abs(vel[i][j]);
                                flag=xnew[i][j];
                                if(flag<0){
                                        xnew[i][j]=0;
                                }
                                else if (flag > vms-1) {
                                                xnew[i][j]=vms-1;
                                        }
                                else{
                                        xnew[i][j]=Math.round(flag);
                                }
                                temp=(int)xnew[i][j];
                        vm[temp]+=burstTime[j];

                        }
                        fit[i] = calculateFitness(vm, vms);
//                         for(int k=0;k<vms;k++){
//                        vm[k] = 0;
//                }           
                }
                for(i=0;i<row;i++)
                {
                    if(fit[i]>fitness[i])
                    { 
                        fitness[i]=fit[i];
                        for(j=0;j<col;j++)
                        {
                            pbest[i][j]=(int)xnew[i][j];
                        }
                    }
                }
                gbest=calculateBest(fitness);
                //worst=calculateWorst(fitness);
                count++;
               
                }
        //print the final result

                for(j=0;j<col;j++)
                {
                        System.out.print(pbest[gbest][j]+ "   ");
                }
               return pbest[gbest];
        }
        //Calculate the fitness as fitness = (average utilization)/(fitness)
        public static double calculateFitness(long vm[], int vms)
        {
                Arrays.sort(vm);
                double avg=0.0,sum=0.0,count=0;
                long ms=vm[vms-1];
                sum = LongStream.of(vm).sum();
                for(long k : vm){
                        if(k !=0){
                                count++;
                        }
                }

        avg=sum/(ms*count);
        return avg/ms;
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
//      public static void main(String[] args) {
//              long burstTime[] = {80,140,80,140,80,140};
//
//                     initPSO(burstTime, 6,3);
//            }
}