package SecretSharing;


import java.math.BigInteger;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Hussain
 */
public class XOR {

    static int maxNumSh = 1000, shar = 2, maxThre = 300;
    //maxThre indicates the max shares used in reconstruction, maxNumSh is max shares generated
    static int s;

    private static BigInteger[] shares = new BigInteger[maxNumSh];
    //Array to store the share number and the generated share

    private static BigInteger[] share = new BigInteger[maxThre];
    //Array to store the collected shares

    private static BigInteger prime;
    //prime number which is greater than 2^256
   // private static BigInteger[] secret = new BigInteger[1000];
    private static BigInteger temp ;
    private static BigInteger hvalue ;
    private static BigInteger temp1, temp2;

    static float ee, ss, tt, st, en, tm;
   

	public static BigInteger[] secretSplit(int particNum, int threshold, BigInteger secret, BigInteger[] coeff) {

		temp1 = secret;
		for (int i = 1; i < particNum; i++) {
			shares[i - 1] = coeff[i - 1];
			temp1 = temp1.xor(coeff[i - 1]);
		}
		shares[particNum - 1] = temp1;
//        for (int i = 0; i < particNum; i++) {
//            System.out.println("Share " + i + "   " + shares[i]);
//        }
		hvalue = BigInteger.valueOf(secret.hashCode());

		secret = BigInteger.ZERO;

		temp = BigInteger.ZERO;
		// en = java.lang.System.currentTimeMillis();

		return shares;
		// System.out.println("split time >>>"+(en-ss));
	}

    /*In this method the secret is constructed using the received
     threshold value and the shares, after the secret is reconstructed a
     hash value is generated and compared with the calculated hash value of
     the secret in the secret spliting method*/
	 public static BigInteger secretReconstruct( int particnum,BigInteger [] shares)
	 {
	 
	 temp2=shares[particnum-1];
	 for (int i = particnum-2; i >= 0; i--) {
	        temp2=temp2.xor(shares[i]);
	       // System.out.println("The share is"+ shares[groupNum][i]);
	    }
	 BigInteger secret = temp2;
	 
	 
	 if(hvalue.compareTo(BigInteger.valueOf(secret.hashCode()))==0)
	 {  //System.out.println("The secret is: " + secret + "\n");
	 }
	 else
	 {
	 System.out.println("nulllllllllllllllllll");
	 }

	 return secret;
	 }

 

    public static void setSec(int j) {
        s = j;
    }

    public static BigInteger getPrime() {
        return prime;
    }

}
