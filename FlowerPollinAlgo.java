package org.cloudbus.cloudsim.examples;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.LongStream;

public class FlowerPollinAlgo{  
    // ND: Normal distribution
    private static final double meanND = 0.0;
    private static final double stdDevND = 1.0;
    static int row = 30, i, j, temp, best, count = 0,rand1,rand2 ;
    static double p=0.7575, fit, flag, epsilon, rand0, tempAB;
    static int avgMs;
    static double fitness[] =  new double[row];
    static Random robj = new Random();
    public static int[] init(long burstTime[], int col, int vms){
        double xnew[]=new double[col];
        double levyArr[] =  new double[col];
        int workArray[][] = new int[row][col];
        long vm[]=new long[vms];
        tempAB = vms/2;
        for(i=0;i<row/2;i++)
        {
            for(j=0;j<col;j++)
            {
                fit = vms*robj.nextDouble();
                if(fit<0)
                    workArray[i][j] = 0;
                else if(fit>vms-1)
                    workArray[i][j] = vms-1;
                else 
                    workArray[i][j] = (int) Math.round(fit);
                if(workArray[i][j]<tempAB){
                    fit = tempAB + robj.nextDouble()*(vms-workArray[i][j]-tempAB );
                }
                else{
                    fit = vms-workArray[i][j] + robj.nextDouble()*(tempAB-vms+workArray[i][j]);
                }
                if(fit<0)
                    workArray[row-1-i][j] = 0;
                else if(fit>vms-1)
                    workArray[row-i-1][j] = vms-1;
                else 
                    workArray[row-1-i][j] = (int) Math.round(fit);
            }
        }
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
        while(count < 2000){
            for (i =0; i<row; i++) {                
                    rand0 = robj.nextDouble();
                    if(rand0 < p){
                        levyArr = levy(vms, col);
                        for (j=0; j<col; j++) {
                        xnew[j] = workArray[i][j]+levyArr[j]*(workArray[i][j]-workArray[best][j]);
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
                    }
                    else{
                        do{
                            rand1 = robj.nextInt(row);
                        }while(rand1 == i);
                        do{
                            rand2 = robj.nextInt(row);
                        }while(rand2 == rand1);
                        epsilon = robj.nextDouble();
                        for (j=0; j<col; j++) {
                            xnew[j] = workArray[i][j]+epsilon*(workArray[rand1][j] - workArray[rand2][j]);
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
        System.out.println();
        for (j=0; j<col; j++) {
            temp =workArray[best][j];
            vm[temp]+=burstTime[j];
        }
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
    /** creates Levy flight samples */
    private static double[] levy(int vms, int col) {
        double[] step = new double[col];
        double lambda = 1.5;
        double sigma = Math.pow(Gamma.gamma(1 + lambda) * Math.sin(Math.PI * lambda / 2)
                / (Gamma.gamma((1 + lambda) / 2) * lambda * Math.pow(2, (lambda - 1) / 2)), (1 / lambda));

        for (int i = 0; i < col; i++) {

            double u = Distribution.normal(robj, meanND, stdDevND) * sigma;
            double v = Distribution.normal(robj, meanND, stdDevND);

            step[i] = 0.01 * u / (Math.pow(Math.abs(v), (1 - lambda)));
        }
        return step;
    }
//        public static void main(String[] args) {
//        long burstTime[] = {80,140,80,140,80,140};
//        
//            init(burstTime, 6, 3);
//        
//        }

}
