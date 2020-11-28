/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */


package org.cloudbus.cloudsim.examples;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
//import java.util.Random;
import java.util.Scanner;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


/**
 * An example showing how to create
 * scalable simulations.
 */
public class CloudsimExample6Flower {

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;

	private static List<Vm> createVM(int userId, int vms) {

		//Creates a container to store VMs. This list is passed to the broker later
		LinkedList<Vm> list = new LinkedList<Vm>();

		//VM Parameters
		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
		int mips = 1000;
		long bw = 1000;
		int pesNumber = 1; //number of cpus
		String vmm = "Xen"; //VMM name

		//create VMs
		Vm[] vm = new Vm[vms];

		for(int i=0;i<vms;i++){
						vm[i] = new Vm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
			//for creating a VM with a space shared scheduling policy for cloudlets:
			//vm[i] = Vm(i, userId, mips, pesNumber, ram, bw, size, priority, vmm, new CloudletSchedulerSpaceShared());

			list.add(vm[i]);
		}

		return list;
	}


	private static List<Cloudlet> createCloudlet(int userId, int cloudlets){
		// Creates a container to store Cloudlets
		LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

		//cloudlet parameters
		long length = 1000;
		long fileSize = 300;
		long outputSize = 300;
		int pesNumber = 1;
		UtilizationModel utilizationModel = new UtilizationModelFull();

		Cloudlet[] cloudlet = new Cloudlet[cloudlets];
//		Random robj = new Random();
		for(int i=0;i<cloudlets;i++){
			cloudlet[i] = new Cloudlet(i, (length+(5*i)%10), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			// setting the owner of these Cloudlets
			cloudlet[i].setUserId(userId);
			list.add(cloudlet[i]);
		}

		return list;
	}


	////////////////////////// STATIC METHODS ///////////////////////

	/**
	 * Creates main() to run this example
	 */
	public static void main(String[] args) {
		Log.printLine("Starting CloudSimExample6Flower...");

		try {
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.
			int num_user = 1;   // number of grid users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;  // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
			@SuppressWarnings("unused")
			Datacenter datacenter0 = createDatacenter("Datacenter_0");
			@SuppressWarnings("unused")
			Datacenter datacenter1 = createDatacenter("Datacenter_1");
//			@SuppressWarnings("unused")
//			Datacenter datacenter2 = createDatacenter("Datacenter_2");
//			@SuppressWarnings("unused")
//			Datacenter datacenter3 = createDatacenter("Datacenter_3");
//			@SuppressWarnings("unused")
//			Datacenter datacenter4 = createDatacenter("Datacenter_4");
//			@SuppressWarnings("unused")
//			Datacenter datacenter5 = createDatacenter("Datacenter_5");
//			@SuppressWarnings("unused")
//			Datacenter datacenter6 = createDatacenter("Datacenter_6");
//			@SuppressWarnings("unused")
//			Datacenter datacenter7 = createDatacenter("Datacenter_7");
//			@SuppressWarnings("unused")
//			Datacenter datacenter8 = createDatacenter("Datacenter_8");
//			@SuppressWarnings("unused")
//			Datacenter datacenter9 = createDatacenter("Datacenter_9");
//			@SuppressWarnings("unused")
//			Datacenter datacenter10 = createDatacenter("Datacenter_10");
//			@SuppressWarnings("unused")
//			Datacenter datacenter11 = createDatacenter("Datacenter_11");
//			@SuppressWarnings("unused")
//			Datacenter datacenter12 = createDatacenter("Datacenter_12");
//			@SuppressWarnings("unused")
//			Datacenter datacenter13 = createDatacenter("Datacenter_13");
//			@SuppressWarnings("unused")
//			Datacenter datacenter14 = createDatacenter("Datacenter_14");
//			@SuppressWarnings("unused")
//			Datacenter datacenter15 = createDatacenter("Datacenter_15");
//			@SuppressWarnings("unused")
//			Datacenter datacenter16 = createDatacenter("Datacenter_16");

			//Third step: Create Broker
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			//Fourth step: Create VMs and Cloudlets and send them to broker
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter the number of VMs(less than 12): ");
			int vms = scanner.nextInt();
			System.out.println("Enter the number of Cloudlets: ");
			int cloudlets=scanner.nextInt();
			vmlist = createVM(brokerId,vms); //creating  vms
			cloudletList = createCloudlet(brokerId,cloudlets); // creating  cloudlets

			broker.submitVmList(vmlist);
			broker.submitCloudletList(cloudletList);
			
			long burstTime[] = new long[cloudlets];
			for (int i = 0; i < cloudlets; i++) {
				burstTime[i] = cloudletList.get(i).getCloudletLength();
			}
			
			//int arr[] = PSO.initPSO(burstTime, cloudlets, vms);
			List<Double> arr=ModifiedFPA.init(burstTime,vms);
			for(int i=0;i<cloudlets;i++){
				Integer integer=arr.get(i).intValue();
				broker.bindCloudletToVm(cloudletList.get(i).getCloudletId(),integer);
			}
			// Fifth step: Starts the simulation
			CloudSim.startSimulation();

			// Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();

			CloudSim.stopSimulation();

			printCloudletList(newList);

			Log.printLine("CloudSimExample6 finished!");
			scanner.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
	}

	private static Datacenter createDatacenter(String name){

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store one or more
		//    Machines
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
		//    create a list to store these PEs before creating
		//    a Machine.
		List<Pe> peList1 = new ArrayList<Pe>();

		int mips = 1000;

		// 3. Create PEs and add these into the list.
		//for a quad-core machine, a list of 4 PEs is required:
		peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
		peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(3, new PeProvisionerSimple(mips)));
//		peList1.add(new Pe(4, new PeProvisionerSimple(mips)));
//		peList1.add(new Pe(5, new PeProvisionerSimple(mips)));
//		peList1.add(new Pe(6, new PeProvisionerSimple(mips)));
//		peList1.add(new Pe(7, new PeProvisionerSimple(mips)));

		//Another list, for a dual-core machine
		List<Pe> peList2 = new ArrayList<Pe>();

		peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
		peList2.add(new Pe(1, new PeProvisionerSimple(mips)));
//		peList2.add(new Pe(2, new PeProvisionerSimple(mips)));
//		peList2.add(new Pe(3, new PeProvisionerSimple(mips)));
//		peList2.add(new Pe(4, new PeProvisionerSimple(mips)));
//		peList2.add(new Pe(5, new PeProvisionerSimple(mips)));
//		peList2.add(new Pe(6, new PeProvisionerSimple(mips)));
//		peList2.add(new Pe(7, new PeProvisionerSimple(mips)));

		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = 2048; //host memory (MB)
		long storage = 1000000; //host storage
		int bw = 10000;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList1,
    				new VmSchedulerTimeShared(peList1)
    			)
    		); // This is our first machine

		hostId++;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList2,
    				new VmSchedulerTimeShared(peList2)
    			)
    		); // Second machine


		//To create a host with a space-shared allocation policy for PEs to VMs:
		//hostList.add(
    	//		new Host(
    	//			hostId,
    	//			new CpuProvisionerSimple(peList1),
    	//			new RamProvisionerSimple(ram),
    	//			new BwProvisionerSimple(bw),
    	//			storage,
    	//			new VmSchedulerSpaceShared(peList1)
    	//		)
    	//	);

		//To create a host with a oportunistic space-shared allocation policy for PEs to VMs:
		//hostList.add(
    	//		new Host(
    	//			hostId,
    	//			new CpuProvisionerSimple(peList1),
    	//			new RamProvisionerSimple(ram),
    	//			new BwProvisionerSimple(bw),
    	//			storage,
    	//			new VmSchedulerOportunisticSpaceShared(peList1)
    	//		)
    	//	);


		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	//to the specific rules of the simulated scenario
	private static DatacenterBroker createBroker(){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects
	 * @param list  list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;
		int count=0;
		double avgResponse = 0, totExec = 0;
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + indent + "Execution Time" + indent + "Start Time" + indent + "Finish Time" + indent + "Submission Time" + indent +"Response Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");
				count++;
				avgResponse += cloudlet.getFinishTime()-cloudlet.getSubmissionTime();
				totExec += cloudlet.getActualCPUTime();
				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() + indent +
						indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent +
						indent + indent + indent + dft.format(cloudlet.getExecStartTime())+ indent + indent + indent + indent + dft.format(cloudlet.getFinishTime())+ indent + indent + indent + indent + dft.format(cloudlet.getSubmissionTime()) + indent + indent + indent + indent + dft.format(cloudlet.getFinishTime()-cloudlet.getSubmissionTime()) );
			}
		}
		Log.printLine("Average response time: "+avgResponse/count+"\nTotal execution time: "+totExec);
	}
}
