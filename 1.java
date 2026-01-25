import java.io.*;
import java.math.BigInteger;
import java.util.*;
// code by superyovae(tôi quên tên thông cảm)
public class Main {
    static final long M = 1234567891L;
    static final int[] P = {2,3,5,7,13,17,19,31,61,89,107,127};

    public static void main(String[] a) throws Exception {
        n F = new n(System.in);
        int n = F.n();
        HashMap<BigInteger, int[]> m = new HashMap<>();
        BigInteger b = BigInteger.ZERO;
        for (int i = 0; i < n; ++i) {
            BigInteger x = new BigInteger(F.s());
            if (x.compareTo(b) > 0) b = x;
            int v = x.getLowestSetBit();
            BigInteger o = x.shiftRight(v);
            int[] c = m.get(o);
            if (c == null) {
                c = new int[128];
                m.put(o, c);
            }
            c[v]++;
        }

        int E = 127;
        long[] I = new long[E + 1];
        for (int i = 1; i <= E; ++i) I[i] = modPow(i, M - 2, M);

        long r = 0;
        int[] t = m.getOrDefault(BigInteger.ONE, new int[128]);

        for (int p : P) {
            BigInteger z = BigInteger.ONE.shiftLeft(p).subtract(BigInteger.ONE);
            if (z.compareTo(b) > 0) continue;
            int[] q = m.get(z);
            if (q == null) continue;

            int s = p - 1;
            long[] w = new long[s + 1];
            w[0] = 1;

            for (int e = 0; e <= E; ++e) {
                int cnt = t[e];
                if (cnt == 0) continue;

                if (e == 0) {
                    long pow2 = modPow(2, cnt, M);
                    for (int i = 0; i <= s; ++i) {
                        if (w[i] != 0) w[i] = (w[i] * pow2) % M;
                    }
                    continue;
                }

                int T = Math.min(cnt, s / e);
                if (T == 0) continue;

                long[] C = new long[T + 1];
                C[0] = 1;
                for (int i = 1; i <= T; ++i) {
                    C[i] = C[i - 1] * (((cnt - i + 1) % M + M) % M) % M * I[i] % M;
                }

                long[] nw = new long[s + 1];
                for (int i = 0; i <= s; ++i) {
                    if (w[i] == 0) continue;
                    long base = w[i];
                    for (int j = 0; j <= T; ++j) {
                        int ns = i + j * e;
                        if (ns > s) break;
                        nw[ns] = (nw[ns] + base * C[j]) % M;
                    }
                }
                w = nw;
            }

            for (int f = 0; f <= E; ++f) {
                int c = q[f];
                if (c == 0) continue;
                int need = s - f;
                if (need < 0) continue;
                long add = w[need];
                if (add == 0) continue;
                r = (r + (add * (c % M)) % M) % M;
            }
        }

        System.out.println(r % M);
    }

    static long modPow(long a, long e, long mod) {
        long r = 1 % mod;
        a %= mod;
        while (e > 0) {
            if ((e & 1) == 1) r = (r * a) % mod;
            a = (a * a) % mod;
            e >>= 1;
        }
        return r;
    }

    static class n {
        private final InputStream in;
        private final byte[] b = new byte[1 << 16];
        private int p = 0, l = 0;
        n(InputStream is) { in = is; }
        private int r() throws IOException {
            if (p >= l) {
                l = in.read(b); p = 0;
                if (l <= 0) return -1;
            }
            return b[p++];
        }
        String s() throws IOException {
            StringBuilder S = new StringBuilder();
            int c;
            while ((c = r()) <= ' ') {
                if (c == -1) return null;
            }
            do {
                S.append((char)c);
                c = r();
            } while (c > ' ');
            return S.toString();
        }
        int n() throws IOException { return Integer.parseInt(s()); }
    }
}
