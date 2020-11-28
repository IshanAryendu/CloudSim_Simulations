package org.cloudbus.cloudsim.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class Forest {
	
	static int lifeTime=6 , LSC=6 , GSC=6 , areaLimit=30 , transferRate=10,initialPopulation=30,vms;
	static long burstTime[];
	static List<Tree> forest,newForest,candidatePopulation;
	
	public static List<Integer> runForest(long[] burstTime, int vms) {
		Random random=new Random();
		Forest.vms=vms;
		Forest.burstTime=burstTime;
		forest=new ArrayList<Tree>();
		newForest=new ArrayList<Tree>();
		candidatePopulation=new ArrayList<Tree>();
		
		//Create the forest by adding trees 
		for(int i=0;i<initialPopulation;i++){
			forest.add(new Tree());
		}
		//For displaying all the tree
//		System.out.println("initial population");
//		for (Iterator<Tree> iterator = forest.iterator(); iterator.hasNext();) {
//			Tree tree = (Tree) iterator.next();
//			System.out.println(tree.age +"  "+tree.solution);
//		}
		
		//Repeat until stop condition is not satisfied --- in this case predefined no of iteration
		int count=10;
		while (count>0) {
			//Applying local seeding
			for (Iterator<Tree> iterator = forest.iterator(); iterator.hasNext();) {
				Tree tree = (Tree) iterator.next();
				if (tree.age==0) {
					for(int i=0;i<LSC;i++){
						Tree newTree = tree.getCopyOfTree();
						int index=random.nextInt(burstTime.length);
						double flag=newTree.solution.get(index);
						int temp1=random.nextInt(1);
						if (temp1==0) {
							flag += Math.random();
						} else {
							flag -= Math.random();
						}
						
						if (flag<0) {
							flag=0;
						} else if (flag>vms-1) {
							flag=vms-1;
						}else {
							flag=Math.round(flag);
						}
						int temp=(int)flag;
						newTree.solution.set(index, temp);
//						System.out.println(newTree.age +" Child Solution "+newTree.solution);
//						System.out.println(tree.age +" Parent Solution "+tree.solution);
						newForest.add(newTree);
					}
				}
				tree.age++;
			}
			forest.addAll(newForest);
			newForest.clear();
			
//			System.out.println("----------------After local seeding--------------------------------");
//			for (Iterator<Tree> iterator = forest.iterator(); iterator.hasNext();) {
//				Tree tree = (Tree) iterator.next();
////				System.out.println(tree.age +"  "+tree.solution+"   "+tree.fitness);
//			}
			//Applying population limiting
			for(int i=0;i<forest.size();i++){
				Tree tree=forest.get(i);
				if (tree.age > lifeTime) {
					candidatePopulation.add(tree);
					forest.remove(i);
					i--;
				}
				//calculate fitness value
				calculateFitness(tree);
			}
			//sort according to fitness
			sortForest();
			if (forest.size()>areaLimit) {
				candidatePopulation.addAll(forest.subList(areaLimit, forest.size()));
				forest.subList(areaLimit, forest.size()).clear();
			}
//			if (count==10) {
//				System.out.println("----------------After population limiting forest--------------------------------");
//				for (Iterator<Tree> iterator = forest.iterator(); iterator.hasNext();) {
//					Tree tree = (Tree) iterator.next();
//					System.out.println(tree.age +"  "+tree.solution+"   "+tree.fitness);
//				} 
//			}
			//printing candidate population
			//System.out.println("----------------After population limiting candidate solution--------------------------------");
//			for (Iterator<Tree> iterator = candidatePopulation.iterator(); iterator.hasNext();) {
//				Tree tree = (Tree) iterator.next();
//				//System.out.println(tree.age +"  "+tree.solution+"   "+tree.fitness);
//			}
			//System.out.println("----------------global seeding--------------------------------");
			//Applying global seeding
			int percentTransferRate=Math.round((candidatePopulation.size()*transferRate)/100);
			for(int i=0;i<percentTransferRate;i++){
				Tree tree=candidatePopulation.get(random.nextInt(candidatePopulation.size()));
				for(int j=0;j<GSC;j++){
					tree.solution.set(random.nextInt(burstTime.length), random.nextInt(vms));
				}
				tree.age=0;
				calculateFitness(tree);
				forest.add(tree);
				//System.out.println(tree.age +"  "+tree.solution+"   "+tree.fitness);
				candidatePopulation.remove(tree);
			}
			candidatePopulation.clear();
			
			//Updating best tree so far
			sortForest();
			forest.get(0).age = 0;
			
			//System.out.println("-------- After "+(11-count)+"th iteration--------------------");
//			for (Iterator<Tree> iterator = forest.iterator(); iterator.hasNext();) {
//				Tree tree = (Tree) iterator.next();
//				//System.out.println(tree.age +"  "+tree.solution+"   "+tree.fitness);
//			}
			count--;
//			if (count==9) {
//				System.out.println("------------------Global seeding------------------------------");
//				for (Iterator<Tree> iterator = forest.iterator(); iterator.hasNext();) {
//					Tree tree = (Tree) iterator.next();
//					System.out.println(tree.age + "  " + tree.solution + "   " + tree.fitness);
//				} 
//			}
		}
//		System.out.println("------------------Final------------------------------");
//		for (Iterator<Tree> iterator = forest.iterator(); iterator.hasNext();) {
//			Tree tree = (Tree) iterator.next();
//			System.out.println(tree.age +"  "+tree.solution+"   "+tree.fitness);
//		}
//		System.out.println("------------------------------------------------");
//		System.out.println(forest.get(0).age+"	"+forest.get(0).solution+"	"+forest.get(0).fitness);
		TreeMap<Integer, Long> process = new TreeMap<Integer,Long>();
		int k=0;
		for(int i:forest.get(0).solution){
			if(process.containsKey(i)){
				long temp=process.get(i);
				temp += burstTime[k++];
				process.put(i, temp);
			}else {
				process.put(i, burstTime[k++]);
			}
		}
//		System.out.println(process);
		return forest.get(0).solution;
	}

	private static Double calculateFitness(Tree tree) {
		// TODO Auto-generated method stub
		int k=0;
		TreeMap<Integer, Long> process = new TreeMap<Integer,Long>();
		for(int i:tree.solution){
			if(process.containsKey(i)){
				long temp=process.get(i);
				temp += burstTime[k++];
				process.put(i, temp);
			}else {
				process.put(i, burstTime[k++]);
			}
		}
		//System.out.println(process+"   " +tree.solution);
		long makeSpan=Collections.max(process.values());
		//System.out.println("MakeSpan = "+makeSpan);
		double sum=process.values().stream().mapToDouble(Number::doubleValue).sum();
		//System.out.println("Sum = "+sum);
		double avg=sum/(double)(makeSpan*process.size());
		//System.out.println("Avg = "+avg);
		tree.fitness=avg/makeSpan;
		//System.out.println("Fitness = "+tree.fitness);
		return tree.fitness;
	}

	private static void sortForest() {
		// TODO Auto-generated method stub
		Collections.sort(forest, new Comparator<Tree>() {
			@Override
			public int compare(Tree o1, Tree o2) {
				// TODO Auto-generated method stub
				return Double.compare(o1.fitness, o2.fitness);
			}
		});
		Collections.reverse(forest);
	}
//	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		System.out.println("Starting Forest");
//		long burstTime[] ={80,140,80,140,80,140,80,140,80,140};//{48,84,53,71,76,55,89,62,77,59}; 
//		System.out.println(runForest(burstTime,5));
//	}
}
