package SecretSharing;

import java.math.BigInteger;
import java.util.Random;

import org.cloudbus.cloudsim.Log;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Hussain
 */
public class Shamir {

    static int maxNumSh = 1000, shar = 2, maxThre = 300;
    //maxThre indicates the max shares used in reconstruction, maxNumSh is max shares generated
    static int s;

    private static BigInteger[] shares = new BigInteger[maxNumSh];
    //Array to store the share number and the generated share

    private static BigInteger[] share = new BigInteger[maxThre];
    //Array to store the collected shares

   
    //prime number which is greater than 2^256
    // private static BigInteger[] secret = new BigInteger[1000];
    private static BigInteger temp;
    private static BigInteger hvalue;
    public static BigInteger getHvalue() {
		return hvalue;
	}

	public static void setHvalue(BigInteger hvalue) {
		Shamir.hvalue = hvalue;
	}

	private static BigInteger temp1, temp2;

    static float ee, ss, tt, st, en, tm;

    public static BigInteger[] secretSplit(int particNum, int threshold, BigInteger secret, BigInteger[] coeff) {

        temp1 = secret;
        
        for (int x = 0; x < particNum; x++) {
            temp1 = secret;
            //System.out.println("temp Number: " + temp[groupNum]);
            for (int exp = 0; exp < threshold; exp++) {
                temp1
                        = (temp1.add(coeff[exp].multiply((BigInteger.valueOf(x+1).pow(exp)).mod(MyCloudlet.getPrime()))).mod(MyCloudlet.getPrime())).mod(MyCloudlet.getPrime());
            }
            temp = temp1;
            shares[x] = temp;

            
          
            //System.out.println("Share " + shares[x-1][1]);
        }
//          for (int i = 0; i < particNum; i++) {
//            System.out.println("Share " + i + "   " + shares[i]);
//        }
        hvalue = BigInteger.valueOf(secret.hashCode());

        secret = BigInteger.ZERO;

        temp = BigInteger.ZERO;
        en = java.lang.System.currentTimeMillis();

        return shares;
        //System.out.println("split time >>>"+(en-ss));
    }

    public static BigInteger secretReconstruct( int threshold,BigInteger prime,BigInteger[] shares)
    {
    int k;

    BigInteger secret = main.getSecret();
	for(int formula = 0; formula < threshold; formula++)
    {
    BigInteger numer = BigInteger.ONE;

    BigInteger denom = BigInteger.ONE;

    //System.out.println("Number of shares is: "+threshold);
    for(int count = 0; count < threshold; count++)
    {
    if(formula == count)
    continue;
    BigInteger sposition = shares[formula];

    BigInteger nposition = shares[count];

    numer = numer.multiply(nposition.negate()).mod(prime);

    denom =
   denom.multiply((sposition.subtract(nposition))).mod(prime);

    }
   // System.out.println("The share is"+ shares[formula]);

    BigInteger tmp = shares[formula].multiply(numer) .multiply(denom.modInverse(prime));

    secret = prime.add(secret).add(tmp) . mod(prime);
    //secret[groupNum]=secret1;
    }
  System.out.println("The secret is: " + secret + "\n");

    if(hvalue.compareTo(BigInteger.valueOf(secret.hashCode()))==0)
     
    	Log.printLine("success");
    	
    else
    Log.printLine("failed");

    return secret;
    }
    public static void sendShare(int j, BigInteger sh) {
        share[j] = sh;
    }

    /*Method to send the calculated shares to the clients after the
     secret is generated and split*/
    public static void setSec(int j) {
        s = j;
    }

//    public static BigInteger getPrime() {
//        return prime;
//    }

}
