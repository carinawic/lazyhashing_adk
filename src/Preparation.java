
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.RandomAccessFile;
import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class Preparation {

    public static void main(String [ ] args) {
        long startTime = System.nanoTime();

        Helpers helper = new Helpers();
        long[] a = new long[30*30*30];
        Arrays.fill(a, -1);

        try {
            //HERE
            RandomAccessFile fileB = new RandomAccessFile("/var/tmp/b.txt", "rw");
            RandomAccessFile fileC = new RandomAccessFile("/var/tmp/c.txt", "rw");
            //RandomAccessFile fileB = new RandomAccessFile("b.txt", "rw");
            //RandomAccessFile fileC = new RandomAccessFile("c.txt", "rw");
            

            File file = new File("/afs/kth.se/misc/info/kurser/DD2350/adk22/labb1/rawindex.txt");
            // HERE File file = new File("rawindex.txt");
            Charset charset = StandardCharsets.ISO_8859_1;

            StringBuilder sb = new StringBuilder();
            InputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
            //FileReader fr = new FileReader();

            int individualWordCounter = 0;

            String prevWord = "";
            String line = "";

            while((line = br.readLine()) != null) {
                //sb.append(line + System.lineSeparator());
                String[] token = line.split("\\s");

                String word = token[0];
                int occurrence = Integer.parseInt(token[1]);

                //System.out.println(word + ", " + occurrence);
                // om ordet ?r nytt
                individualWordCounter ++;
                if (!prevWord.equals(word)){ // we have a new word!!
                    // fixa A
                    //System.out.println("nu ska vi hitta hashen f?r ordet: "+ word);
                    fileB.writeChars(' ' + String.valueOf(individualWordCounter) + '\n');

                    int hashOfWord = helper.getHash(word);
                    //System.out.println("hashen f?r det ordet ?r: "+ hashOfWord);
                    if (a[hashOfWord] == -1) {
                      // Ny kombination av 3 f?rsta bokst?verna
                      a[hashOfWord] = fileB.getFilePointer();
                    }
                    // L?gg till ordet och motsvarande f?rekomst i B
                    
                    fileB.writeChars(word + ' ' + String.valueOf(fileC.getFilePointer()));
                    individualWordCounter = 0;
                }

                // l?gg till f?rekomst i c
                fileC.writeChars(String.valueOf(occurrence) + '\n');
                prevWord = word;
            }

            fileB.writeChars(' ' + String.valueOf(individualWordCounter) + '\n');

            try
            {
                //HERE
                FileOutputStream fos = new FileOutputStream("/var/tmp/arrayA");
                //FileOutputStream fos = new FileOutputStream("arrayA");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(a);
                oos.close();
                fos.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }

            //fileA.writeChars(a.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("Time it took: " + (endTime - startTime)/1000000);
    }
}
