import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String message;
        int opt, gen;
        /*if (args.length!=0) {
            try {
                if (args.length != 3) throw new IllegalArgumentException();
                message = args[0];
                if (args[1].length() > 1 || args[2].length() > 1) throw new IllegalArgumentException();
                if (!Character.isDigit(args[1].charAt(0)) || !Character.isDigit(args[2].charAt(0)))
                    throw new IllegalArgumentException();
                opt = Integer.parseInt(args[1]);
                gen = Integer.parseInt(args[2]);
            } catch (IllegalArgumentException e) {
                System.out.println("Usage: [message] [encrypt/decrypt] [new keys/existing keys]");
            }
        }*/

        System.out.println("Enter a message");
        Scanner scanner = new Scanner(System.in);
        message = scanner.nextLine();
        opt = 0;

        System.out.println("Encrypt [1] or Decrypt [2]?");
        opt = getOpt(scanner, opt);

        switch (opt) {
            case 1 -> {
                gen = 0;
                System.out.println("Generate new keys [1] or use existing keys [2]?");
                gen = getOpt(scanner, gen);
                BigInteger e = null, n = null, d = null;
                switch (gen) {
                    case 1 -> {
                        BigInteger p = BigInteger.probablePrime(128, new Random());
                        BigInteger q = BigInteger.probablePrime(128, new Random());
                        BigInteger c = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
                        n = p.multiply(q);
                        e = genE(c);
                        d=e.modInverse(c);
                    }
                    case 2 -> {
                        n = getKey("n",scanner);
                        e = getKey("e",scanner);
                        d = getKey("d",scanner);
                        for (BigInteger key : new BigInteger[]{n,e,d}) {
                            if (key.compareTo(BigInteger.ZERO)<0) {
                                System.out.println("Enter a valid key.");
                                System.exit(0);
                            }
                        }
                    }
                }
                char[] letters = message.toCharArray();
                StringBuilder result = new StringBuilder();
                for (char l : letters) {
                    result.append(new BigInteger(String.valueOf((int)l)).modPow(e,n));
                    result.append(".");
                }
                System.out.println("Here is your encrypted message:\n"+ result.substring(0,result.length()-1));
                System.out.println("Public Key n: " + n + "\nPublic Key e: " + e + "\nPrivate Key d: " + d);
            }
            case 2 -> System.out.println("Here is your decrypted message:\n" + decrypt(message,scanner));
        }
    }

    private static int getOpt(Scanner scanner, int opt) {
        while (opt==0) {
            try {
                opt = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Enter a valid number.");
                scanner.nextLine();
                continue;
            }
            if (!(opt==1 || opt==2)) {
                System.out.println("Enter a valid number.");
                scanner.nextLine();
            }
        }
        return opt;
    }

    public static String decrypt(String m, Scanner scanner) {
        BigInteger n = getKey("n",scanner);
        BigInteger d = getKey("d",scanner);
        String[] chars = m.split("\\.");
        StringBuilder result = new StringBuilder();
        for (String s: chars) {
            result.append(new String(new BigInteger(s).modPow(d,n).toByteArray(), StandardCharsets.UTF_8));
        }
        return result.toString();
    }

    private static BigInteger genE (BigInteger c) {
        BigInteger e = BigInteger.TWO;
        while (e.compareTo(c) < 0 && !e.gcd(c).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
        }
        return e;
    }

    private static BigInteger getKey(String key, Scanner scanner) {
        BigInteger value = null;
        while (value == null) {
            try {
                System.out.print("Enter key " + key + ": ");
                value = scanner.nextBigInteger();
            } catch (Exception e) {
                System.out.println("Enter a valid key.");
                scanner.nextLine();
            }
            assert value != null;
            if (value.compareTo(BigInteger.ZERO)<0) {
                System.out.println("Key must be positive");
                scanner.nextLine();
            }
        }
        return value;
    }
}