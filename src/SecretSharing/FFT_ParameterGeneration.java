/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecretSharing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

//import com.sun.tools.classfile.Opcode.Set;

/**
 *
 * @author Hussain
 */
public class FFT_ParameterGeneration {

	static BigInteger val = BigInteger.valueOf(2);

	public static ArrayList<BigInteger> primeFactors = new ArrayList<BigInteger>();
	public static BigInteger prime_number;
	public static BigInteger qqq;
	public static BigInteger gen;

	public static boolean is_prime(BigInteger num) {
//        if (num.compareTo(BigInteger.valueOf(2)) < 0) {
//            return false;
//        }
//        for(int i=3;i<=num.intValue();i++)
//        {
//        if (num.intValue()== i)
//            return true;
//        if (num.intValue()%i==0)
//            return false;
//        }
		FFT_MillerRabin MR = new FFT_MillerRabin();
		return MR.isProbablePrime(num, 40);

	}

	public static BigInteger power(BigInteger x, BigInteger y, BigInteger p) {

		BigInteger res = BigInteger.ONE; // Initialize result

		// Update x if it is more than or
		// equal to p
		x = x.mod(p);

		while (y.intValue() > 0) {

			// If y is odd, multiply x with result
			if ((y.intValue() & 1) == 1) {
				res = (res.multiply(x)).mod(p);
			}

			// y must be even now
			y = y.shiftRight(1); // y = y/2
			x = (x.multiply(x)).mod(p);
		}

		return res;
	}

	public static BigInteger sample_prime(BigInteger bitsize) {

		Random rnd = new Random();
		BigInteger candidate;
		BigInteger i;

		while (true) {

			i = new BigInteger(bitsize.intValue(), rnd);
			// i = new BigInteger("175519");
			// i = new BigInteger("11");

//            System.out.println("random number in sample_prime= " + i);
//            System.out.println("random number bit length sample_prime= " + i.bitLength());
			if (is_prime(i)) {
				break;
			}
		}
		return i;
	}

	public static BigInteger find_prime(BigInteger bitsize, int order_divisor) {

		FFT_PrimeFactorization p = new FFT_PrimeFactorization();
		while (true) {
			BigInteger sample = sample_prime(bitsize);
			int k2 = 1;
			for (BigInteger k1 = ((sample.multiply(BigInteger.valueOf(order_divisor))).multiply(BigInteger.valueOf(k2)))
					.add(BigInteger.ONE);; k1 = k1.add(BigInteger.valueOf(order_divisor))) {
				// qqq =
				// ((k1.multiply(BigInteger.valueOf(order_divisor))).multiply(BigInteger.valueOf(k2))).add(BigInteger.ONE);
				// ;
			//	k1=BigInteger.valueOf(1912277);
				if (is_prime(k1)) {
					// primeFactors.add(sample);
					// primeFactors.addAll(p.primeBigFactorize(BigInteger.valueOf(k2)));
					primeFactors.addAll(p.primeBigFactorize(k1.subtract(BigInteger.ONE)));
					HashSet set=new HashSet(primeFactors);
					primeFactors.clear();
					primeFactors.addAll(set);
					ArrayList<BigInteger> s = null;
				
					for(int i = 0; i < primeFactors.size(); i++) {   
					    System.out.print("["+primeFactors.get(i)+"] ");
					} 
					System.out.println("k1=" + k1);
					return k1;

				}
			}
//              for (BigInteger k1=((sample.multiply(BigInteger.valueOf(order_divisor)))).add(BigInteger.ONE); ; k1=k1.add(BigInteger.valueOf(order_divisor))) {
//                
//              // qqq = ;
//                if (is_prime(k1)) {
//                   // primeFactors.add(k1);
//                   // primeFactors.addAll(p.primeBigFactorize(BigInteger.valueOf(k2)));
//                    primeFactors.addAll(p.primeBigFactorize(k1.subtract(BigInteger.ONE)));
//                    System.out.println("k1=" + k1);
//                    return k1;
//
//                }
//            }

		}

	}
	
	public static BigInteger find_generator_(BigInteger q) {
		BigInteger i = BigInteger.valueOf(2);
		BigInteger g = null;
		while (i.compareTo(q) < 0) {
			//if(!is_prime(i)) break;
			 g=MultiplicativeOrder.moTest(i, q);
			if(g.compareTo(BigInteger.valueOf(4))==0)
				break;
			i = i.add(BigInteger.ONE);
		}
		
		return i;
			
	}

	public static BigInteger find_generator(BigInteger q) {
		
		BigInteger order, exponent, factor;
		BigInteger d = null;
		BigInteger d1 = null;
		BigInteger i = BigInteger.valueOf(2);
		BigInteger g = BigInteger.ZERO;
		boolean gg = true;
		int a = 0;
		order = q.subtract(BigInteger.ONE);

		outerloop: while (i.compareTo(q) < 0) {
		//	System.out.println("i=  " + i);
			gg = true;
		//	System.out.println("i.modPow(order, q)  " +i.modPow(order, q) );
			
		//	if (!i.modPow(order, q).equals(BigInteger.ONE)) {
		//		gg = false;
		//		break;}
			
			for (int j = 0; j < primeFactors.size(); j++) {
			/*	System.out.println("i.modPow(order, q)  " +i.modPow(order, q) );
				if (!i.modPow(order, q).equals(BigInteger.ONE)) {
					gg = false;
					break;*/
				
				if (is_prime(i)) {
					factor = (BigInteger) primeFactors.get(j);

				//	 System.out.println("factor= " + factor);
					exponent = order.divide(factor);
					// d = Math.pow(i, exponent.doubleValue()) % q.doubleValue();
					d = power(i, exponent, q);
					d1 = i.mod(q);
					if (d.equals(BigInteger.valueOf(1))) // if (d==1)
					{
						gg = false;
						break;
					}
					a = j;
				} else {
					break;
				}
				if (gg == false)
					a = 0;
			}

			if (a == primeFactors.size() - 1) {
				gg = true;
				g = i;
				break outerloop;
			}
//            if (gg == true) {
//                g = i;
//                System.out.println("i= "+i);
//                return g;
//
//            }

			i = i.add(BigInteger.ONE);
		}
		// System.out.println("d= " +d.intValue());
		// System.out.println("d= " + d);
		System.out.println("g=  " + g);
		if (gg == true) {
			return g;
		} else {
			return BigInteger.ZERO.subtract(BigInteger.ONE);
		}
	}

	public static BigInteger generate_parameters(long bitsizee, int order_divisor) {
		BigInteger order, order2;
		BigInteger omega;
		BigInteger bitsize = BigInteger.valueOf(bitsizee);
		BigInteger om;

		prime_number = find_prime(bitsize, order_divisor);
		// qq=bitsiz;
		System.out.println("prime= "+prime_number);
		gen = find_generator(prime_number);
		
		//om=find_generator_(prime_number);
	//	System.out.println("om= "+om);
		order = prime_number.subtract(BigInteger.ONE);
		order2 = order.divide(BigInteger.valueOf(order_divisor));
		// omega = (Math.pow(gen, order2.doubleValue())) % qq.doubleValue();
		omega = power(gen, order2, prime_number);
		System.out.println("omega= "+omega);
		return omega;
	}

	public static BigInteger getPrime_number() {
		return prime_number;
	}

	public static void setPrime_number(BigInteger prime_number) {
		FFT_ParameterGeneration.prime_number = prime_number;
	}

	public static void main(String[] args) {
		// TODO code application logic here
		FFT_ParameterGeneration j = new FFT_ParameterGeneration();

		 System.out.println("omega= " + j.generate_parameters(20,4));
		// System.out.println("qq= " + prime_number);
	}

}
