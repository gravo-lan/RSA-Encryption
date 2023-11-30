import java.math.BigInteger;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter a message");
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        char[] letters = message.toCharArray();

        int[] pq = genPQ();
        int c = carmichael(pq[0],pq[1]);
        int n = pq[0] * pq[1];
        int e = genE(c,pq[0],pq[1]);
        int d = genD(e,c);

        StringBuilder result = new StringBuilder();
        for (char l : letters) {
            System.out.println((int)l);
            result.append(encrypt(l,e,n));
            result.append(".");
        }
        System.out.println("Here is your encrypted message:\n"+ result);
        System.out.println(crack(result.toString(),d,n));
    }

    public static String crack(String m, int d, int n) {
        String[] chars = m.split("\\.");
        StringBuilder result = new StringBuilder();
        for (String s: chars) {
            result.append((decrypt(Integer.parseInt(s),d,n)));
        }
        return result.toString();
    }

    public static String decrypt(int l, int d, int n) {
        System.out.println(l);
        System.out.println((BigInteger.valueOf((long) Math.pow(l,d))).mod(BigInteger.valueOf(n)));
        return (BigInteger.valueOf((long) Math.pow(l,d))).mod(BigInteger.valueOf(n)).toString();
    }

    public static BigInteger encrypt(char l, int e, int n) {
        System.out.println(BigInteger.valueOf((long) Math.pow((int) l,e)));
        return (BigInteger.valueOf((long) Math.pow((int) l,e))).mod(BigInteger.valueOf(n));
    }

    public static int[] genPQ() {
        int p, q;
        Random rand = new Random();
        p = Math.abs(rand.nextInt(2147483647));
        q = Math.abs(rand.nextInt(2147483647));
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
        return (1/e) % c;
    }

    public static int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }
}