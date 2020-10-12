/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecretSharing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import SecretSharing.FFT_ParameterGeneration;

/**
 *
 * @author Hussain
 */
class FFT {

    static int maxNumSh = 9999, shar = 2, maxThre = 9999;
    //maxThre indicates the max shares used in reconstruction, maxNumSh is max shares generated
    static int s;

    private static BigInteger[] shares = new BigInteger[maxNumSh];
    //Array to store the share number and the generated share

    private static BigInteger[] share = new BigInteger[maxThre];
 //Array to store the collected shares

 //static  BigInteger prime2 = new BigInteger( (long) Math.pow(2, 10));
  //static  long prime1=   (long) (Math.pow(2,44497)-1);
    //static BigInteger prime = BigDecimal.valueOf(prime1).toBigInteger();
    static BigInteger prime;

    private static BigInteger secret;
    private static BigInteger temp ;
    private static BigInteger hvalue ;

    private static BigInteger[] coefff = new BigInteger[1000];
    private static BigInteger temp1;
    static float ee, ss, tt, st, en, tm;

 //hvalue is to store the calculated hash value of the secret

    /*Method to generate random integers between 1 and the declared
     prime number using Random() function*/
    public static BigInteger f(int num) {
        BigInteger s = BigInteger.ONE;
        BigInteger ss = BigInteger.valueOf(2);
        for (int i = 1; i <= num; i++) {
            s = s.multiply(ss);
        }
        return s;
    }

    public static BigInteger rndBigInt(BigInteger max) {

        Random rnd = new Random();
        do {
            BigInteger i = new BigInteger(max.bitLength(), rnd);

            if (i.compareTo(BigInteger.ZERO) > 0 && i.compareTo(max) < 0) {
                return i;
            }
        } while (true);
    }

    /*Method to split the secret divided is generated using rndBigInt
     method and depending on particNum and threshold the shares are
     calculated and the secret is set to 0 after its hash value is
     calculated*/
    public static BigInteger[] secretSplit(int particNum, int threshold,BigInteger sec, BigInteger [] coeff, BigInteger primee, BigInteger omega) {

      

//     omega=BigInteger.valueOf(179);
//     prime=BigInteger.valueOf(433);
    //    System.out.println("omega= " + omega);
    // System.out.println("qq= " + qq);

        // System.out.println("bit length   ="+prime.bitLength());
        prime = primee;
      //  System.out.println("Prime Number: " + prime);//
        //System.out.println("prime "+prime);
        //  System.out.println("bit length  "+prime.bitLength());
        secret = sec;

     //System.out.println("generation tmie=  "+(ee-ss));
     
//System.out.println("secret Number: " + secret[groupNum]);
      

        coeff[0] = secret;
        // System.out.println("coeff[0]  ="+coeff[0]);

        for (int i = 1; i < threshold; i++) {
            coeff[i] = rndBigInt(prime);
           // System.out.println("coeff[" + i + "]  =" + coeff[i]);
        }

        for (int i = threshold; i < particNum; i++) {
            coeff[i] = BigInteger.ZERO;
          //  System.out.println("coeff[" + i + "]  =" + coeff[i]);
        }
        coefff = FFT2_Forward(coeff, omega);

        for (int x = 0; x < particNum; x++) {

            shares[x] = coefff[x];

          //  shares[x] = BigInteger.valueOf(x);

         //   System.out.println("Share " + shares[x]);
        }

        hvalue = BigInteger.valueOf(secret.hashCode());

        secret = BigInteger.ZERO;

        temp = BigInteger.ZERO;
        en = java.lang.System.currentTimeMillis();
        // System.out.println("ssssssssp time  "+(en-st));
		return coeff;

    }

    public static BigInteger[] FFT2_Forward(BigInteger coef[], BigInteger omega) {
        BigInteger omegaSquared;
        BigInteger[] A_Values = new BigInteger[9999];
        BigInteger[] B_Values = new BigInteger[9999];
        BigInteger[] C_Values = new BigInteger[9999];
        BigInteger B_coeff[] = new BigInteger[coef.length / 2];
        BigInteger C_coeff[] = new BigInteger[coef.length / 2];
        if (coef.length == 1) {
            return coef;
        }
        int ii = 0;
        for (int i = 0; i < coef.length; i = i + 2) {
            B_coeff[ii] = coef[i];
            C_coeff[ii] = coef[i + 1];
            ii++;
        }
        FFT_ParameterGeneration pm = new FFT_ParameterGeneration();
        omegaSquared = omega.pow(2).mod(prime);
        B_Values = FFT2_Forward(B_coeff, omegaSquared);
        C_Values = FFT2_Forward(C_coeff, omegaSquared);
        int L_Half = coef.length / 2;
        int j=1;
        BigInteger x;
        for (int i = 0; i < L_Half; i++) {
            j = i;
            x = pm.power(omega, BigInteger.valueOf(j), prime);
            A_Values[j] = (B_Values[i].add(x.multiply(C_Values[i]))).mod(prime);
            j = i + L_Half;
            x = pm.power(omega, BigInteger.valueOf(j), prime);
            A_Values[j] = (B_Values[i].add(x.multiply(C_Values[i]))).mod(prime);
        }
//        for (int i = 0; i < L_Half; i++) {
//            x=BigInteger.valueOf(j);
//            A_Values[i] = (B_Values[i].add(x.multiply(C_Values[i]))).mod(prime);
//            A_Values[i + L_Half] = (B_Values[i].subtract(x.multiply(C_Values[i]))).mod(prime);
//            j=(j*omega.intValue())%prime.intValue();
//        }
        return A_Values;
    }

    /*In this method the secret is constructed using the received
     threshold value and the shares, after the secret is reconstructed a
     hash value is generated and compared with the calculated hash value of
     the secret in the secret spliting method*/
    public static int secretReconstruct(int threshold, int groupNum) {
        int k;

        for (int formula = 0; formula < threshold; formula++) {
            BigInteger numer = BigInteger.ONE;

            BigInteger denom = BigInteger.ONE;

            //System.out.println("Number of shares is: "+threshold);
            for (int count = 0; count < threshold; count++) {
                if (formula == count) {
                    continue;
                }
                BigInteger sposition = shares[formula];

                BigInteger nposition = shares[count];

                numer = numer.multiply(nposition.negate()).mod(prime);

                denom
                        = denom.multiply((sposition.subtract(nposition))).mod(prime);

            }
            System.out.println("The share is" + share[formula]);

            BigInteger tmp = share[formula].multiply(numer).multiply(denom.modInverse(prime));

            secret = prime.add(secret).add(tmp).mod(prime);
            //secret[groupNum]=secret1;
        }
//System.out.println("The secret is: " + secret[groupNum] +"for group number "+groupNum+ "\n");

        if (hvalue.compareTo(BigInteger.valueOf(secret.hashCode())) == 0) {
            k = 1;
        } else {
            k = 0;
        }

        return k;
    }

    /*Method to store the received shares from the clients that are
     required to reconstruct the secret*/
    public static void sendShare(int j, BigInteger sh) {
        share[j] = sh;
    }

  
}
