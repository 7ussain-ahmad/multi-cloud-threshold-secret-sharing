package SecretSharing;


import java.math.BigInteger;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hussain
 */



public class MyCloudlet extends Cloudlet{
     static int maxNumSh = 1000, shar = 2, maxThre = 300;
    //maxThre indicates the max shares used in reconstruction, maxNumSh is max shares generated
    static int s;

    private static BigInteger[][] shares = new BigInteger[maxNumSh][maxNumSh];
    //Array to store the share number and the generated share

    private static BigInteger share ;
    //Array to store the collected shares
    private static BigInteger threshold;
    private static BigInteger prime;
    //prime number which is greater than 2^256
    private static BigInteger[] secret = new BigInteger[1000];
    private static BigInteger[] temp = new BigInteger[1000];
    private static BigInteger[] hvalue = new BigInteger[1000];
    private static BigInteger temp1, temp2;
     static float ee, ss, tt, st, en, tm;
     
     
    public MyCloudlet(int cloudletId, long cloudletLength, int pesNumber, long cloudletFileSize,
            long cloudletOutputSize, UtilizationModel utilizationModelCpu,
            UtilizationModel utilizationModelRam, UtilizationModel utilizationModelBw,BigInteger share) {
        super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu, utilizationModelRam, utilizationModelBw);
        this.share=share;
    }

    public  BigInteger getShare() {
        return share;
    }

    public  void setShare(BigInteger share) {
        MyCloudlet.share = share;
    }
    
    
    public BigInteger generateKey(int exp ){
                        BigInteger secret;
                        SecretGeneration sec=new SecretGeneration();
                        prime=sec.findPrimeNumber(exp);
                        secret=sec.generateRandom(prime);
                        return secret;
        }

    public static BigInteger getPrime() {
        return prime;
    }
}
    