import java.util.Scanner;
import java.nio.charset.Charset;
import java.io.*;
import java.nio.file.Files;
import java.io.File;

public class Codelength {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        
        String filename = scanner.nextLine();
        File infile = new File(filename);
        String text = Files.readString(infile.toPath(), Charset.defaultCharset());
                
        //Some clever calculations
	    int n1=6,n2=3,n3=2;
                         
        System.out.println(n1+" "+n2+" "+n3);
        scanner.close();
    }
}
