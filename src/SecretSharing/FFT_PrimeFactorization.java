/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecretSharing;

import java.util.ArrayList;
import java.math.BigInteger;

/**
 *
 * @author Hussain
 */
public class FFT_PrimeFactorization {

    /**
     * Reduces the parameter n into a product of only prime numbers and returns
     * a list of those prime number factors as longs.
     *
     * @param n the number to prime factorize
     * @return an ArrayList (of longs) of only prime factors of the parameter n
     */
    public static ArrayList<Long> primeFactorize(long n) {
        ArrayList<Long> primeFactors = new ArrayList<Long>();

        long primeFactor = 0L;

        for (long i = 2L; i <= n / i;) { // smallest prime factor to the square root of n (largest possible factor of n)
            if (n % i == 0) { // the prime number i is a factor of n (i will never go into n if it's composite since the prime factor of that compositite number would have already been tested)
                primeFactor = i; // therefore, this is a prime factor of n
                primeFactors.add(primeFactor);
                n /= i; // divide out that prime factor from n to get the rest of the prime factors
                // don't increment i: test if this same prime factor goes into n multiple times (e.g. 18 = 2*3*3)
            } else {
                i++; // i is not a prime factor of n, so increment
            }
        }

        if (primeFactor < n) { // n had no more prime factors, so n is a prime factor
            primeFactors.add(n);
        } else { // n was divided down to 1, meaning that the last prime factor divided itself out. therefore, it is the last prime factor
            primeFactors.add(primeFactor);
        }

        return primeFactors;
    }

    /**
     * Reduces the parameter n into a product of only prime numbers and returns
     * a list of those prime number factors as BigIntegers.
     *
     * @param n the number to prime factorize
     * @return an ArrayList (of BigIntegers) of only prime factors of the
     * parameter n
     */
    public static ArrayList<BigInteger> primeBigFactorize(BigInteger n) {
        ArrayList<BigInteger> primeFactors = new ArrayList<BigInteger>();
        BigInteger primeFactor = BigInteger.ZERO;

        for (BigInteger i = new BigInteger("2"); i.compareTo(n.divide(i)) <= 0;) {
            if (n.mod(i).longValue() == 0) {
                primeFactor = i;
                primeFactors.add(primeFactor);
                n = n.divide(i);
            } else {
                i = i.add(BigInteger.ONE);
            }
        }

        if (primeFactor.compareTo(n) < 0) {
            primeFactors.add(n);
        } else {
            primeFactors.add(primeFactor);
        }

        return primeFactors;
    }
}
