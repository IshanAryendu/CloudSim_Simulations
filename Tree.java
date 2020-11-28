package org.cloudbus.cloudsim.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree{
	public int age;
	public List<Integer> solution;
	public double fitness;
	
	public Tree() {
		this.age=0;
		Random random=new Random();
		solution=new ArrayList<Integer>();
		for (int i = 0; i < Forest.burstTime.length; i++) {
			solution.add(random.nextInt(Forest.vms));
		}
	}

	public Tree getCopyOfTree() {
		// TODO Auto-generated method stub
		Tree newTree=new Tree();
		newTree.age=this.age;
		List<Integer> tempList=new ArrayList<Integer>();
		for(Integer item : this.solution){
			tempList.add(item);
		}
		newTree.solution=tempList;
		//System.out.println(this);
		//System.out.println(newTree);
		return newTree;
	}
}
