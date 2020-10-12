/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecretSharing;

import java.math.BigInteger;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;


/**
 *
 * @author Hussain
 */
public class MyVm extends Vm{
    
     static int maxNumSh = 1000, shar = 2, maxThre = 300;
    //maxThre indicates the max shares used in reconstruction, maxNumSh is max shares generated
    static int s;

    private static BigInteger[][] shares = new BigInteger[maxNumSh][maxNumSh];
    //Array to store the share number and the generated share

    private static BigInteger[] sharee = new BigInteger[maxThre];
    private static BigInteger share;

    public MyVm(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler,BigInteger  share) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
        this.share=share;
    }

    public static BigInteger getShare() {
        return share;
    }

    public void setShare(BigInteger share) {
        MyVm.share = share;
    }
    
}
