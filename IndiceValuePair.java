package org.cloudbus.cloudsim.examples;

public class IndiceValuePair{
    private int indice;
    private double value;

    public IndiceValuePair(int ind, double val){
        indice = ind;
        value = val;
    }
    public void setValue(double val){
        this.value = val;
    }
    public int getIndice(){
        return indice;
    }
    public double getValue(){
        return value;
    }
//    public static void main(String[] args){
//        int[] myArray = {21,2,34,5,6,21,18,25,17,31};
//        IndiceValuePair[] pairs = new IndiceValuePair[10];
//        for(int i = 0; i < myArray.length; i++) {
//            
////            
////            System.out.println(i+ ": " + myArray[i]);
//            for(int j = 0; j < pairs.length; j++){
//                //for the first five entries
//                if(pairs[j] == null){
//                    pairs[j] = new IndiceValuePair(i, myArray[i]);
//                    break;
//                }
//                else if(pairs[j].getValue() < myArray[i]){
//                    //inserts the new pair into its correct spot
//                    for(int k = 9; k > j; k--){
//                        pairs[k] = pairs [k-1];
//                    }
//                    pairs[j] = new IndiceValuePair(i, myArray[i]);
//                    break;
//                }
//            }
//        }
//        System.out.println("\n10 Max indices and their values");
//        for(int i = 0; i < pairs.length; i++){
//            System.out.println(pairs[i].getIndice() + ": " + pairs[i].getValue());
//        }
//    }
}