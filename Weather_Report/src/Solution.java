
import java.util.PriorityQueue;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author NANON
 */
public class Solution {

    public static int n;
    public static int[] bits;

    public static void main(String[] args) {

        Scanner stdin = new Scanner(System.in);
        n = stdin.nextInt();
        double[] prob = new double[4];

        for (int i = 0; i < 4; i++) {
            prob[i] = stdin.nextDouble();
        }

        PriorityQueue<node> pq = new PriorityQueue<node>();

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
                pq.add(new node(0, dp[i] / multi(n, dim), multi(n, dim)));
            }
        }

        double res = -1;

        // Do Huffman...
        while (true) {

            node next = pq.poll();

            // We are done!
            if (pq.size() == 0 && next.freq == 1) {
                res = next.bits;
                break;
            }

            // Just need to deal with this node.
            if (next.freq > 1) {
                node[] newNodes = next.processMulti();
                for (int i = 0; i < newNodes.length; i++) {
                    pq.offer(newNodes[i]);
                }
            } else {

                node second = pq.poll();
                second.subOne();
                node tmp = new node(second.bits, second.prob, 1);
                pq.offer(next.merge(tmp));

                // Only need to put this back in if there's anything in it.
                if (second.freq > 0) {
                    pq.offer(second);
                }
            }
        }

        // Here is our answer.
        System.out.println(res);
    }

    // Returns the corresponding multinomial coefficient, n!/prod(dim[i])!
    public static long multi(int n, int[] dim) {
        long res = fact(n);
        for (int i = 0; i < dim.length; i++) {
            res /= fact(dim[i]);
        }
        return res;
    }

    // Returns n!
    public static long fact(int n) {
        long res = 1;
        for (int i = 1; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    // Returns the number of bits for this storage scheme.
    public static int bitCount(int[] dim) {
        int res = 0;
        for (int i = 0; i < dim.length; i++) {
            res += (dim[i] * bits[i]);
        }
        return res;
    }

    // Returns the number of items in this scheme.
    public static int count(int[] dim) {
        int sum = 0;
        for (int i = 0; i < dim.length; i++) {
            sum += dim[i];
        }
        return sum;
    }

    // Converts k to its 4 dimensions.
    public static int[] getDim(int k) {
        int[] res = new int[4];
        for (int i = 0; i < 4; i++) {
            res[i] = k % (n + 1);
            k /= (n + 1);
        }
        return res;

    }

    // Converts arr to an integer.
    public static int convert(int[] arr) {
        return arr[0] + (n + 1) * arr[1] + (n + 1) * (n + 1) * arr[2] + (n + 1) * (n + 1) * (n + 1) * arr[3];
    }

}

class node implements Comparable<node> {

    public double bits;
    public double prob;
    public long freq;

    public node(double myBits, double myProb, long myFreq) {
        bits = myBits;
        prob = myProb;;
        freq = myFreq;
    }

    // this and other must have freq 1.
    public node merge(node other) {
        double p = this.prob + other.prob;
        return new node(1 + this.bits * this.prob / p + other.bits * other.prob / p, p, 1);
    }

    // For huffman, take smaller probabilities first.
    public int compareTo(node other) {
        if (this.prob < other.prob) {
            return -1;
        }
        if (this.prob > other.prob) {
            return 1;
        }
        return 0;
    }

    // Return an array of nodes that occurs when you process this Huffman node.
    public node[] processMulti() {
        node[] res = null;

        // Easy case, can do all pairs.
        if (freq % 2 == 0) {
            res = new node[1];
            res[0] = new node(bits + 1, 2 * prob, freq / 2);
        } // Annoying case, keep odd item in its own node and don't process.
        else {
            res = new node[2];
            res[0] = new node(bits + 1, 2 * prob, freq / 2);
            res[1] = new node(bits, prob, 1);
        }
        return res;
    }

    public void subOne() {
        freq--;
    }

}
