/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rizkiyanapp
 */
public class Nodes implements Comparable<Nodes> {

    public double bits;
    public double prob;
    public long freq;

    public Nodes(double myBits, double myProb, long myFreq) {
        bits = myBits;
        prob = myProb;;
        freq = myFreq;
    }

    // this and other must have freq 1.
    public Nodes merge(Nodes other) {
        double p = this.prob + other.prob;
        return new Nodes(1 + this.bits * this.prob / p + other.bits * other.prob / p, p, 1);
    }

    // For huffman, take smaller probabilities first.
    public int compareTo(Nodes other) {
        if (this.prob < other.prob) {
            return -1;
        }
        if (this.prob > other.prob) {
            return 1;
        }
        return 0;
    }

    // Return an array of nodes that occurs when you process this Huffman node.
    public Nodes[] processMulti() {
        Nodes[] res = null;

        // Easy case, can do all pairs.
        if (freq % 2 == 0) {
            res = new Nodes[1];
            res[0] = new Nodes(bits + 1, 2 * prob, freq / 2);
        } // Annoying case, keep odd item in its own node and don't process.
        else {
            res = new Nodes[2];
            res[0] = new Nodes(bits + 1, 2 * prob, freq / 2);
            res[1] = new Nodes(bits, prob, 1);
        }
        return res;
    }

    public void subOne() {
        freq--;
    }

}
