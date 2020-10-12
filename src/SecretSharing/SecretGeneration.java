package SecretSharing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hussain
 */
import java.math.BigInteger;
import java.util.Random;

public class SecretGeneration {

    static int maxNumSh = 1000, shar = 2, maxThre = 300;
    static int s;
    private static BigInteger[][] shares = new BigInteger[maxNumSh][maxNumSh];
    private static BigInteger[] share = new BigInteger[maxThre];
    private static BigInteger prime;
    private static BigInteger[] secret = new BigInteger[1000];
    private static BigInteger[] temp = new BigInteger[1000];
    private static BigInteger[] hvalue = new BigInteger[1000];
    private static BigInteger temp1, temp2;
    static float ee, ss, tt, st, en, tm;

    public static BigInteger findPrimeNumber(int num) {
        BigInteger s = BigInteger.ONE;
        BigInteger ss = BigInteger.valueOf(2);
        for (int i = 1; i <= num; i++) {
            s = s.multiply(ss);
        }
        return s.subtract(BigInteger.ONE);
    }

    /*Method to generate random integers between 1 and the declared
     prime number using Random() function*/
    public static BigInteger rndBigInt(BigInteger max) {
        Random rnd = new Random();
        do {
            BigInteger i = new BigInteger(max.bitLength(), rnd);
            if (i.compareTo(BigInteger.ZERO) > 0 && i.compareTo(max) < 0) {
                return i;
            }
        } while (true);
    }

    public static BigInteger generateRandom(BigInteger prime) {
        return rndBigInt(prime);
    }
}
