
import java.util.PriorityQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rizkiyanapp
 */
public class Simulation {

    public int n;
    public int[] bits;
    public double[] prob = new double[4];

    public double start(String nObs, String probs) {

        String[] splited = probs.split("\\s+");

        for (int i = 0; i < 4; i++) {
            this.prob[i] = Double.parseDouble(splited[i]);
        }

        this.n = Integer.parseInt(nObs);

        PriorityQueue<Nodes> pq = new PriorityQueue<Nodes>();

        // Run regular DP to get probabilities. (I actually don't need this, but had
        // started writing it in contest. I'll fix it after data is posted.)
        double[] dp = new double[(n + 1) * (n + 1) * (n + 1) * (n + 1)];
        dp[0] = 1;
        for (int i = 1; i < dp.length; i++) {
            int[] dim = getDim(i);
            int sum = count(dim);
            if (sum > n) {
                continue;
            }
            for (int j = 0; j < 4; j++) {
                if (dim[j] > 0) {
                    dim[j]--;
                    dp[i] += (dp[convert(dim)] * prob[j]);
                    dim[j]++;
                }
            }

            // Only nodes we care about.
            if (sum == n) {
                pq.add(new Nodes(0, dp[i] / multi(n, dim), multi(n, dim)));
            }
        }

        double res = -1;

        // Do Huffman...
        while (true) {

            Nodes next = pq.poll();

            // We are done!
            if (pq.size() == 0 && next.freq == 1) {
                res = next.bits;
                break;
            }

            // Just need to deal with this node.
            if (next.freq > 1) {
                Nodes[] newNodes = next.processMulti();
                for (int i = 0; i < newNodes.length; i++) {
                    pq.offer(newNodes[i]);
                }
            } else {

                Nodes second = pq.poll();
                second.subOne();
                Nodes tmp = new Nodes(second.bits, second.prob, 1);
                pq.offer(next.merge(tmp));

                // Only need to put this back in if there's anything in it.
                if (second.freq > 0) {
                    pq.offer(second);
                }
            }
        }

        // Here is our answer.
        return res;
    }

    // Returns the corresponding multinomial coefficient, n!/prod(dim[i])!
    public long multi(int n, int[] dim) {
        long res = fact(n);
        for (int i = 0; i < dim.length; i++) {
            res /= fact(dim[i]);
        }
        return res;
    }

    // Returns n!
    public long fact(int n) {
        long res = 1;
        for (int i = 1; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    // Returns the number of bits for this storage scheme.
    public int bitCount(int[] dim) {
        int res = 0;
        for (int i = 0; i < dim.length; i++) {
            res += (dim[i] * bits[i]);
        }
        return res;
    }

    // Returns the number of items in this scheme.
    public int count(int[] dim) {
        int sum = 0;
        for (int i = 0; i < dim.length; i++) {
            sum += dim[i];
        }
        return sum;
    }

    // Converts k to its 4 dimensions.
    public int[] getDim(int k) {
        int[] res = new int[4];
        for (int i = 0; i < 4; i++) {
            res[i] = k % (n + 1);
            k /= (n + 1);
        }
        return res;
    }

    // Converts arr to an integer.
    public int convert(int[] arr) {
        return arr[0] + (n + 1) * arr[1] + (n + 1) * (n + 1) * arr[2] + (n + 1) * (n + 1) * (n + 1) * arr[3];
    }

}
