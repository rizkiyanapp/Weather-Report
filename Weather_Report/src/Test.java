
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
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n;
        
        
        Scanner stdin = new Scanner(System.in);
        n = stdin.nextInt();
        double[] prob = new double[4];

        for (int i = 0; i < 4; i++) {
            prob[i] = stdin.nextDouble();
        }
        
        for (double x :prob) {
            System.out.println(x);
        }
        
    }

}
