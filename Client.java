import java.io.File;
import java.util.Scanner;

public class Client{
    public static String testFileName = "specExample.txt";

    public static void main(String[] args) {
        File testFile = new File(testFileName);
        try{
            Scanner s = new Scanner(testFile);
            int k = s.nextInt();
            int n = s.nextInt();
            double[][] distances = new double[n][n];
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                        distances[i][j] = Double.parseDouble(s.next());
                }
            }
            s.close();
            Clusterer clusterer = new Clusterer(distances, k);
            System.out.println(clusterer.getCost());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}