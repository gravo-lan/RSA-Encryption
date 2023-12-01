import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println("Enter a message");
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        char[] letters = message.toCharArray();

        int[] pq = genPQ();
        int c = carmichael(pq[0],pq[1]);
        BigInteger n = BigInteger.valueOf((long) pq[0] * pq[1]);
        int e = genE(c,pq[0],pq[1]);
        int d = genD(e,c);

        StringBuilder result = new StringBuilder();
        for (char l : letters) {
            result.append(encrypt(l,e,n));
            result.append(".");
        }
        System.out.println("Here is your encrypted message:\n"+ result.substring(0,result.length()-1));
        System.out.println(crack(result.toString(),d,n));
    }

    public static String crack(String m, int d, BigInteger n) throws UnsupportedEncodingException {
        String[] chars = m.split("\\.");
        StringBuilder result = new StringBuilder();
        for (String s: chars) {
            result.append((decrypt(BigInteger.valueOf(Long.parseLong(s)),d,n)));
        }
        return result.toString();
    }

    public static String decrypt(BigInteger l, int d, BigInteger n) throws UnsupportedEncodingException {
        BigInteger val = ((l.pow(d)).mod(n));
        return new String(val.toByteArray(), StandardCharsets.UTF_8);
    }

    public static BigInteger encrypt(char l, int e, BigInteger n) {
        BigInteger x = new BigInteger(String.valueOf((int)l));
        return (x.pow(e)).mod(n);
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

    public static int genD (int e, int c) {
        for (int i = 0; i < c; i++) {
            if ((e*i) % c == 1) return i;
        }
        return 1;
    }

    public static int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }

    private static boolean isPrime(int inputNum){
        if (inputNum <= 3 || inputNum % 2 == 0)
            return inputNum != 2 && inputNum != 3; //this returns false if number is <=1 & true if number = 2 or 3
        int divisor = 3;
        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0))
            divisor += 2; //iterates through all possible divisors
        return inputNum % divisor == 0; //returns true/false
    }
}