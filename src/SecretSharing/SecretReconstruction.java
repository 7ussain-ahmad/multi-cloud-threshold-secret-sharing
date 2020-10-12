package SecretSharing;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class SecretReconstruction {
	  private static int particNum;
	  private static int threshold;
	  private static int algoID;
	  private static List<MyVm> vmlist;
	  static BigInteger secret;
	  static int max;  
	  private static int VmCount;
	  static int maxThre = 300;
	  private static BigInteger[] share = new BigInteger[maxThre];
	  private static BigInteger primeNumber;
	  private static BigInteger [] coeff;
	  
	public SecretReconstruction(int algoID,int particNum,int threshold) {
			this.setParticNum(particNum);
			this.setThreshold(threshold);
			this.setAlgoID(algoID);
			

	        Log.printLine("Starting Secret Reconstruction...");
	        try {


	            @SuppressWarnings("unused")
	        //    Datacenter datacenter0 = createDatacenter("Datacenter_0");

	            DatacenterBroker broker =createBroker();
	            int brokerId = broker.getId();
	 
	            VmCount=getParticNum();
	            vmlist = createVM(brokerId, VmCount); //creating 20 vms
	            cloudletList = createCloudlet(brokerId, getParticNum(), 0); // creating 40 cloudlets
	            broker.submitVmList(vmlist);
	            broker.submitCloudletList(cloudletList);
	            
	            /////////////////////////////////////////////////////////////////////////

	            for (int i = 0; i < VmCount; i++) {
	                broker.bindCloudletToVm(i, i);
	            }
	            // Fifth step: Starts the simulation
	          //  CloudSim.startSimulation();

	            // Final step: Print results when simulation is over
	            List<Cloudlet> newList = broker.getCloudletReceivedList();

	           // CloudSim.stopSimulation();

	            printCloudletList(newList);

	            Log.printLine("reconstruction finished!");
	        } catch (Exception e) {
	            e.printStackTrace();
	            Log.printLine("The simulation has been terminated due to an unexpected error");
	        }
	    
			
		}

    private static List<MyCloudlet> cloudletList;
   

    public static List<MyCloudlet> getCloudletList() {
        return cloudletList;
    }


    private static List<MyVm> createVM(int userId, int vms) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<MyVm> list = new LinkedList<MyVm>();
        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 32; //vm memory (MB)
        int mips = 10;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name
        //create VMs
        MyVm[] vm = new MyVm[vms];
        for (int i = 0; i < vms; i++) {
            vm[i] = new MyVm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared(), null);
			//for creating a VM with a space shared scheduling policy for cloudlets:
            //vm[i] = Vm(i, userId, mips, pesNumber, ram, bw, size, priority, vmm, new CloudletSchedulerSpaceShared());
            list.add(vm[i]);
        }
        return list;
    }

    private static List<MyCloudlet> createCloudlet(int userId, int cloudlets, int idShift) {
        // Creates a container to store Cloudlets
        LinkedList<MyCloudlet> list = new LinkedList<MyCloudlet>();

        //cloudlet parameters
        long length = 0;
        long fileSize = 10;
        long outputSize = 10;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        MyCloudlet[] cloudlet = new MyCloudlet[cloudlets];

        for (int i = 0; i < cloudlets; i++) {
            cloudlet[i] = new MyCloudlet(idShift + i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel,null);
            // setting the owner of these Cloudlets
            cloudlet[i].setUserId(userId);
            list.add(cloudlet[i]);
        }

        return list;
    }

    public static void main(String[] args) {}



    private static Datacenter createDatacenter(String name) {

		// Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<Host>();

        List<Pe> peList1 = new ArrayList<Pe>();

        int mips = 1000;

		// 3. Create PEs and add these into the list.
        //for a quad-core machine, a list of 4 PEs is required:
        peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
//        peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
//        peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
//        peList1.add(new Pe(3, new PeProvisionerSimple(mips)));

        //Another list, for a dual-core machine
        List<Pe> peList2 = new ArrayList<Pe>();

        peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
        peList2.add(new Pe(1, new PeProvisionerSimple(mips)));

        //4. Create Hosts with its id and list of PEs and add them to the list of machines
        int hostId = 0;
        int ram = 2048; //host memory (MB)
        long storage = 1000000; //host storage
        int bw = 10000000;

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

//		hostList.add(
//    			new Host(
//    				hostId,
//    				new RamProvisionerSimple(ram),
//    				new BwProvisionerSimple(bw),
//    				storage,
//    				peList2,
//    				new VmSchedulerTimeShared(peList2)
//    			)
//    		); // Second machine
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
 
    private static DatacenterBroker createBroker() {

        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent + indent + dft.format(cloudlet.getActualCPUTime())
                        + indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }

    }


	public static int getParticNum() {
		return particNum;
	}


	public static void setParticNum(int particNum) {
		SecretReconstruction.particNum = particNum;
	}


	public static int getThreshold() {
		return threshold;
	}


	public static void setThreshold(int threshold) {
		SecretReconstruction.threshold = threshold;
	}


	public static int getAlgoID() {
		return algoID;
	}


	public static void setAlgoID(int algoID) {
		SecretReconstruction.algoID = algoID;
	}


}