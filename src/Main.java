import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter a message");
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        char[] letters = message.toCharArray();

        //int[] pq = genPQ();
        int[] pq = new int[]{61,53};
        int c = carmichael(pq[0],pq[1]);
        System.out.println(c);
        BigInteger n = BigInteger.valueOf((long) pq[0] * pq[1]);
        int e = genE(c,pq[0],pq[1]);
        BigInteger d = genD(BigInteger.valueOf(e),BigInteger.valueOf(c));
        System.out.println((BigInteger.valueOf(e).multiply(d)).mod(BigInteger.valueOf(c)));

        StringBuilder result = new StringBuilder();
        for (char l : letters) {
            result.append(encrypt(l,e,n));
            result.append(".");
        }
        System.out.println("Here is your encrypted message:\n"+ result.substring(0,result.length()-1));
        System.out.println(crack(result.toString(),d,n));
    }

    public static String crack(String m, BigInteger d, BigInteger n) {
        String[] chars = m.split("\\.");
        StringBuilder result = new StringBuilder();
        for (String s: chars) {
            result.append((decrypt(BigInteger.valueOf(Long.parseLong(s)),d,n)));
        }
        return result.toString();
    }

    public static String decrypt(BigInteger l, BigInteger d, BigInteger n) {
        BigInteger val = l.modPow(d,n);
        return new String(val.toByteArray(), StandardCharsets.UTF_8);
    }

    public static BigInteger encrypt(char l, int e, BigInteger n) {
        BigInteger x = new BigInteger(String.valueOf((int)l));
        return x.modPow(BigInteger.valueOf(e),n);
    }

    public static int[] genPQ() {
        int p = 4, q = 4;
        Random rand = new Random();
        while (isPrime(q) && isPrime(p)) {
            p = rand.nextInt(10000);
            q = rand.nextInt(10000);
        }
        return new int[]{p,q};
    }

    public static int carmichael(int p, int q) {
        int a = p-1, b = q-1;
        return a / gcd(a,b) * b;
    }

    public static int genE (int c, int p, int q) {
        int e = Math.max(p,q);
        while (!(2<e && e<c) && !(gcd(e,c)==1)) e++;
        return e;
    }

    public static BigInteger genD (BigInteger e, BigInteger c) {
        for (BigInteger x = BigInteger.valueOf(0); x.compareTo(c) < 0; x=x.add(BigInteger.ONE)) {
            if ((e.multiply(x)).mod(c).equals(BigInteger.valueOf(1))) return x;
        }
        System.out.println("No valid d found");
        return BigInteger.valueOf(1);
    }

    public static int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }

    private static boolean isPrime(int inputNum){
        if (inputNum <= 3 || inputNum % 2 == 0)
            return inputNum != 2 && inputNum != 3;
        int divisor = 3;
        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0))
            divisor += 2;
        return inputNum % divisor == 0;
    }
}