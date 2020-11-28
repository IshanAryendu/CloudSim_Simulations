package org.cloudbus.cloudsim.examples;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.LongStream;

public class GA {
        static int row = 6, i, j, temp,temp1,temp2, best, worst, count = 0,a,b,parent1,parent2;
        static double fit1,fit2, flag1,flag2,r,B,D;


        static double fitness[]=new double [row];

        public static int[] init(long burstTime[], int col, int vms){
                double child1[]=new double[col];
                double child2[]=new double[col];

                int workArray[][] = new int[row][col];
               // double r1[][]=new double[row][col];
                //double r2[][]=new double[row][col];
                long vm1[]=new long[vms];
                long vm2[]=new long[vms];
                long vm[]=new long[vms];

                Random robj = new Random();
                //Create the random matrices
                for(i=0;i<row;i++)
                {
                        for(j=0;j<col;j++)
                        {
                               // r1[i][j]=Math.random();
                                //r2[i][j]=Math.random();
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
        //worst=calculateWorst(fitness);
        //run the calculations in a loop
        while(count<100){
                for(i=0;i<row/2;i++)
                        {
                         for( int p=0;p<10;p++)
                         {
                           a=robj.nextInt(row);
                           b=robj.nextInt(row);
                           if(a==b)
                           {
                            continue;
                           }else
                           break;
                       }

                       if(fitness[a]>fitness[b])
                       {
                        parent1=a;
                       }
                       else
                        parent1=b;

                        for( int p=0;p<10;p++)
                         {
                           a=robj.nextInt(row);
                           b=robj.nextInt(row);
                           if(a==b)
                           {
                            continue;
                           }else
                           break;
                       }

                          if(fitness[a]>fitness[b])
                       {
                        parent2=a;
                       }
                       else
                        parent2=b;

                           r=Math.random();
                           if(r>0.5)
                               B=Math.pow((1.0/(2.0*(1-r))),(1.0/(0.6+1)));
                             else
                               B=Math.pow((2.0*r),(1.0/(0.6+1)));
                             for(j=0;j<col;j++)
                             {
                                child1[j]=0.5*((1+B)*workArray[parent1][j]+(1-B)*workArray[parent2][j]);
                               child2[j]=0.5*((1-B)*workArray[parent1][j]+(1-B)*workArray[parent2][j]);

                               }
                             r=Math.random();
                            if(r>0.5)
                              D=1-Math.pow((2*(1-r)),(1/(0.6+1)));
                             else
                              D=Math.pow((2*(r)),(1/(0.6+1)))-1;
                              for(int k=0;k<col;k++)
                              {
                                 child1[k]+=D;
                                 child2[k]+=D;

                              }
                              for(int k=0;k<col;k++)
                              {
                                 flag1=child1[k];
                                 
                                if(flag1<0){
                                        child1[k]=0;
                                }
                                else if (flag1 > vms-1) {
                                                child1[k]=vms-1;
                                        }
                                else{
                                         child1[k]=Math.round(flag1);
                                }
                                temp1=(int)child1[k];
                                  vm1[temp1]+=burstTime[k];
                                  


                              }
                              for(int k=0;k<col;k++)
                              {
                                 flag2=child2[k];
                                 
                                if(flag2<0){
                                        child2[k]=0;
                                }
                                else if (flag2 > vms-1) {
                                                child2[k]=vms-1;
                                        }
                                else{
                                         child2[k]=Math.round(flag2);
                                }
                                temp2=(int)child2[k];
                                  vm2[temp2]+=burstTime[k];
                                  


                              }
                             fit1 = calculateFitness(vm1, vms);
                              fit2 = calculateFitness(vm2, vms);
                              if(fit1>fitness[parent1])
                              {
                                for(int k=0;k<col;k++)
                                {
                                    workArray[parent1][k]=(int)child1[k];
                                }
                                fitness[parent1]=fit1;
                              }
                              if(fit2>fitness[parent2])
                              {
                                for(int k=0;k<col;k++)
                                {
                                    workArray[parent2][k]=(int)child1[k];
                                }
                                fitness[parent2]=fit2;
                              }

                            
                                for(int k=0;k<vms;k++){
                        vm1[k] = 0;}
                          for(int k=0;k<vms;k++){
                                vm2[k] = 0;}


                         }
                     

                                      
                
                best=calculateBest(fitness);
               // worst=calculateWorst(fitness);
                count++;
               
                }
        //print the final result

                for(j=0;j<col;j++)
                {
                        System.out.print(workArray[best][j]+ "   ");
                }
                return workArray[best];
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
//     	public static void main(String[] args) {
// 		long burstTime[] = {80,140,80,140,80,140};
// 		
// 			init(burstTime, 6, 3);
//		
//		}
}