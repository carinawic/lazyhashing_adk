
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class Preparation {
    public static void main(String [ ] args) {

        Helpers helper = new Helpers();

        ArrayList<Integer> c = new ArrayList<Integer>();
        ArrayList<String> b1 = new ArrayList<String>();
        ArrayList<Integer> b2 = new ArrayList<Integer>();

        int[] a = new int[30*30*30];
        Arrays.fill(a, -1);

        try {
          File fileC = new File("c.txt");
          File fileB1 = new File("b1.txt");
          File fileB2 = new File("b2.txt");
          File fileA = new File("a.txt");
          if (fileC.createNewFile()) {
            System.out.println("File created: " + fileC.getName());
          } else {
            System.out.println("File already exists.");
          }
          if (fileB1.createNewFile()) {
            System.out.println("File created: " + fileB1.getName());
          } else {
            System.out.println("File already exists.");
          }
          if (fileB2.createNewFile()) {
            System.out.println("File created: " + fileB2.getName());
          } else {
            System.out.println("File already exists.");
          }
          if (fileA.createNewFile()) {
            System.out.println("File created: " + fileA.getName());
          } else {
            System.out.println("File already exists.");
          }
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }

        try {

            File file = new File("rawindex.txt");
            Charset charset = StandardCharsets.ISO_8859_1;

            StringBuilder sb = new StringBuilder();
            InputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));

            String line;
            int pointer_in_c = 0;
            int pointer_in_b = 0;

            int counter = 0;

            String prevWord = "";
            outer: while ((line = br.readLine()) != null) {
                //sb.append(line + System.lineSeparator());
                String[] token = line.split("\\s");

                String word = token[0];
                int occurrence = Integer.parseInt(token[1]);

                //System.out.println(word + ", " + occurrence);

                // om ordet är nytt
                if (!prevWord.equals(word)){ // we have a new word!!
                    // lägg in ordet i b1
                    b1.add(word);
                    // lägg till motsvarande förekomst i b2
                    b2.add(pointer_in_c);

                    // fixa a på nåt sätt tror jag
                    //System.out.println("nu ska vi hitta hashen för ordet: "+ word);
                    int hashOfWord = helper.getHash(word);
                    //System.out.println("hashen för det ordet är: "+ hashOfWord);
                    a[hashOfWord] = pointer_in_b;
                    //System.out.println("den platsen har value: "+ pointer_in_b);

                    pointer_in_b++;



                }

                // lägg till förekomst i c
                c.add(occurrence);
                pointer_in_c ++;

                prevWord = word;

                // counter++;
                // if (counter == 3){
                //     break outer;
                //
                // }
            }

            // System.out.println(c);
            // System.out.println(b1);
            // System.out.println(b2);

            // for( int i = 0; i < a.length; i ++ ){
            //     if( a[i] != -1 ){
            //         System.out.println( "på index " + i + " inuti a finns value: " + a[i] );
            //     }
            // }


        } catch (IOException e) {
            e.printStackTrace();
        }

        // try {
        //   FileWriter myWriterC = new FileWriter("c.txt");
        //   myWriterC.write(c.toString());
        //   myWriterC.close();
        //
        //   FileWriter myWriterB1 = new FileWriter("b1.txt");
        //   myWriterB1.write(b1.toString());
        //   myWriterB1.close();
        //
        //   FileWriter myWriterB2 = new FileWriter("b2.txt");
        //   myWriterB2.write(b2.toString());
        //   myWriterB2.close();
        //
        //   FileWriter myWriterA = new FileWriter("a.txt");
        //   myWriterA.write(Arrays.toString(a));
        //   myWriterA.close();
        //
        //   System.out.println("Successfully wrote to the files.");
        // } catch (IOException e) {
        //   System.out.println("An error occurred.");
        //   e.printStackTrace();
        // }

        //ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileOutputStream fos = new FileOutputStream("EmployeeObject.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        ObjectOutputStream out = null;
        try {
          out = new ObjectOutputStream(fos);
          out.writeObject(b1);
          out.flush();
        } finally {
          try {
            bos.close();
          } catch (IOException ex) {
            // ignore close exception
          }
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
        ObjectInput in = null;
        try {
          in = new ObjectInputStream(bis);
          Object o = in.readObject();
          
        } finally {
          try {
            if (in != null) {
              in.close();
            }
          } catch (IOException ex) {
            // ignore close exception
          }
        }

    }
}
