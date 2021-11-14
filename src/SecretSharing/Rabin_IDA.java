package SecretSharing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 *
 * @author Hussain
 */
class Rabin_IDA {

	static int maxNumSh = 9999, shar = 2, maxThre = 9999;
	// maxThre indicates the max shares used in reconstruction, maxNumSh is max
	// shares generated
	static int s;

	public static BigInteger[][] shares = new BigInteger[1000][1000];
	// Array to store the share number and the generated share

	private static BigInteger[] share = new BigInteger[maxThre];
	// Array to store the collected shares

	// static BigInteger prime2 = new BigInteger( (long) Math.pow(2, 10));
	// static long prime1= (long) (Math.pow(2,44497)-1);
	// static BigInteger prime = BigDecimal.valueOf(prime1).toBigInteger();
	static BigInteger prime;

	private static BigInteger[] secret = new BigInteger[1000];
	private static BigInteger[] temp = new BigInteger[1000];
	private static BigInteger hvalue;
	private static BigInteger temp1;
	private static int[][] c;
	private static String[][] sharesn = new String[maxNumSh][10];
	private static BigInteger[][] aa;
	private static double a[][];
	static float ee, ss, tt, st, en, tm;
	static int n;

	// hvalue is to store the calculated hash value of the secret

	/*
	 * Method to generate random integers between 1 and the declared prime number
	 * using Random() function
	 */

	/*
	 * Method to split the secret divided is generated using rndBigInt method and
	 * depending on particNum and threshold the shares are calculated and the secret
	 * is set to 0 after its hash value is calculated
	 */
	public static BigInteger[] secretSplit(int particNum, int threshold, BigInteger secret, BigInteger prime) {

		String str = prime.toString();

		int arr[] = new int[str.length()];
		// Integer aux[] = new Integer[str.length()];
		for (int i = 0; i < str.length(); i++) {
			arr[i] = Integer.parseInt(str.substring(i, i + 1));
		}
		st = java.lang.System.currentTimeMillis();
		int n = particNum;
		int m = threshold;
		IDA ida = new IDA(n, m);

		int a[][] = new int[n][m];
		aa = new BigInteger[n][m];

		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < m; ++j) {
				a[i][j] = ida.mulInGF256(i, j);
				// aa[i][j]=BigDecimal.valueOf(a[i][j]).toBigInteger();

			}
		}
		c = ida.encode(arr);

		hvalue = BigInteger.valueOf(Arrays.hashCode(IDA.integerSeq));

		secret = BigInteger.ZERO;

		// temp[groupNum] = BigInteger.ZERO;
		BigInteger[][] bigs = new BigInteger[c.length][c[0].length];
		ArrayList<BigInteger> list = new ArrayList<BigInteger>();
		BigInteger[] shares = new BigInteger[bigs[0].length * bigs.length];

		en = java.lang.System.currentTimeMillis();

		for (int i = 0; i < particNum; i++) {
			for (int j = 0; j < c[i].length; j++) {
				bigs[i][j] = BigInteger.valueOf(c[i][j]);
				list.add(bigs[i][j]);
			}
		}

		shares = list.toArray(new BigInteger[bigs[0].length * bigs.length]);

		return  shares;
	}

	/*
	 * In this method the secret is constructed using the received threshold value
	 * and the shares, after the secret is reconstructed a hash value is generated
	 * and compared with the calculated hash value of the secret in the secret
	 * spliting method
	 */
	public static BigInteger secretReconstruct(int particnum,int threshold,BigInteger[] shares, int[] fid) {
		int k;
		String rme = "";
		int m = threshold;
		int n=particnum;
		

		a = new double[m][m];
		double ia[][] = new double[m][m];
		int[] dm;
		
		/*
		 * for (int i = 0; i < m; ++i) { System.out.println("fid===" + fid[i]); }
		 */
//        Inverse in=new Inverse();
//    	ia=in.invert(a);
//       for(int i=0;i<c.length;++i){
//       	 	for(int p=0;p<m;++p){
//       	 		//dm[i]=dm[i].add((ia[i%m][p].multiply(BigDecimal.valueOf(c[p][i/m]).toBigInteger())));
//                        dm[i]+=ia[i%m][p]*c[p][i/m];
//       	 	}
//       }
		/*
		 * int l=0; for (int i = 0; i < particnum; i++) { for (int j = 0; j < threshold;
		 * j++) { c[i][j]=shares[l].intValue(); l++; } }
		 */
		IDA ida = new IDA(n, m);
		dm = ida.decode(c, fid);
//       for(int i=0;i<dm.length;++i){
//    		rme+=Math.round(dm[i])+" ";
//    	}
		//System.out.println("dm=");
//       for  (int i = 0; i < dm.length; i++) {
//               System.out.print(dm[i]);
////               System.out.println("rme="+rme);
//           }
		if (hvalue.compareTo(BigInteger.valueOf(Arrays.hashCode(dm))) == 0) {
			System.out.println("success");
		} else {
			
			System.out.println("failedddd");
		}

		return BigInteger.valueOf(dm[0]);
	}

	/*
	 * Method to store the received shares from the clients that are required to
	 * reconstruct the secret
	 */
	public static void sendShare(int j, BigInteger sh) {
		share[j] = sh;
	}

	/*
	 * Method to send the calculated shares to the clients after the secret is
	 * generated and split
	 */
	public static BigInteger recivShare(int j) {
		return shares[j][1];
	}

	public static void setSec(int j) {
		s = j;
	}

}
