import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter a message");
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        int opt = 0;

        System.out.println("Encrypt [1] or Decrypt [2]?");
        try {
            opt = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Enter a valid number.");
        }

        switch (opt) {
            case 1 -> {
                char[] letters = message.toCharArray();
                BigInteger[] pq = genPQ();
                BigInteger c = pq[0].subtract(BigInteger.ONE).multiply(pq[1].subtract(BigInteger.ONE));
                BigInteger n = pq[0].multiply(pq[1]);
                BigInteger e = genE(c);
                BigInteger d = e.modInverse(c);

                StringBuilder result = new StringBuilder();
                for (char l : letters) {
                    result.append(encrypt(l,e,n));
                    result.append(".");
                }
                System.out.println("Here is your encrypted message:\n"+ result.substring(0,result.length()-1));
                System.out.println("Public Key n: " + n + "\nPublic Key e: " + e + "\nPrivate Key d: " + d);
            }
            case 2 -> {
                System.out.println("Enter key n:");
                BigInteger n = scanner.nextBigInteger();
                System.out.println("Enter key d:");
                BigInteger d = scanner.nextBigInteger();
                System.out.println(crack(message,d,n));

            }
        }
    }

    public static String crack(String m, BigInteger d, BigInteger n) {
        String[] chars = m.split("\\.");
        StringBuilder result = new StringBuilder();
        for (String s: chars) {
            result.append((decrypt(new BigInteger(s),d,n)));
        }
        return result.toString();
    }

    public static String decrypt(BigInteger l, BigInteger d, BigInteger n) {
        BigInteger val = l.modPow(d,n);
        return new String(val.toByteArray(), StandardCharsets.UTF_8);
    }

    public static BigInteger encrypt(char l, BigInteger e, BigInteger n) {
        BigInteger x = new BigInteger(String.valueOf((int)l));
        return x.modPow(e,n);
    }

    private static BigInteger[] genPQ() {
        BigInteger p = BigInteger.probablePrime(128, new Random());
        BigInteger q = BigInteger.probablePrime(128, new Random());
        return new BigInteger[]{p,q};
    }

    private static BigInteger genE (BigInteger c) {
        BigInteger e = BigInteger.TWO;
        while (e.compareTo(c) < 0 && !e.gcd(c).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
        }
        if (e.compareTo(c) >= 0) {
            System.out.println("No valid value of e found. Choose different prime numbers.");
        }
        return e;
    }
}