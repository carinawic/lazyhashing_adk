import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

public class Konkordans {
  /*
  what to do on ubuntu computer:
  1. change files to correct files
  2. make sure special characters in swedish alphabet works
  3. if fetching occurrences takes too much time, then first calculate and afterwards append to arraylist
  4. stringbuilder instead of string += part
  */

    static long lengthOfC;
    static long lengthOfB;
    static long lengthOfText;
    static long offset = 30;


    public static void main(String [] args) {
      long startTime = System.nanoTime();



      

      //System.out.println((byte)args[0].charAt(0));
      
      Helpers helper = new Helpers();

      // real data: /afs/kth.se/misc/info/kurser/DD2350/adk22/labb1
      if (args.length == 0) {
        System.out.println("Inget ord angivet.");
        return;
      }
      

      long[] a = retriveAFromFile();
      String word = args[0]; //.toLowerCase();
      System.out.println(word);
      int hashedValue = helper.getHash(word);
      // System.out.println("the hashed value of "+ word+ " is " + hashedValue);

      // get the first row1 hash index
      long index_1_from_a = a[hashedValue];
      // ordet finns tidigast p? plats index_1_from_a, om ordet finns
      if (index_1_from_a == -1){
        System.out.println("Ordet " + word + " existerar inte i texten :(");
        return;
      }

      long index_2_from_a = -1;
      int hashCounter = 1;
      File fileB = new File("/var/tmp/b.txt");
      File fileC = new File("/var/tmp/c.txt");
      //HERE File fileB = new File("b.txt");
      //File fileC = new File("c.txt");

      lengthOfB = fileB.length();
      lengthOfC = fileC.length();


      do{
        if (hashedValue + hashCounter >= 30*30*30) {
          index_2_from_a = lengthOfB+1;  // l?ngden av b + 1
          break;
        }
        index_2_from_a = a[hashedValue + hashCounter];
        hashCounter++;
      } while(index_2_from_a == -1);
      // ordet finns innan index_2_from_a, om ordet finns

      // binary search between index_1_from_a and index_2_from_a
      long[] indexes_from_B = getIndexesInB(index_1_from_a, index_2_from_a, word);
      // System.out.println("finshed binary search");
      if (indexes_from_B == null) {
        System.out.println("Ordet " + word + " existerar inte i texten :(");
        return;
      }
      // System.out.println("indexes_from_B[0]: "+indexes_from_B[0]+" \nindexes_from_B[1]: " + indexes_from_B[1]);

      // find row1 at index in c


      long endTime1 = System.nanoTime();
      System.out.println("Time it took: " + (endTime1 - startTime)/1000000);


      

      System.out.println("Det finns "+indexes_from_B[2]+" f?rekomster av ordet.");

      
      if (indexes_from_B[2] > 25) { 

        Scanner scanner = new Scanner(System.in);
        System.out.println("Vill du fortfarande printa alla f?rekomster? y/n");

        String answer = scanner.nextLine().toLowerCase();

        if(answer.equals("n")){
          // System.out.println("we dont want to print");
          return;
        }
      }


      try{
        RandomAccessFile fileText = new RandomAccessFile("/afs/kth.se/misc/info/kurser/DD2350/adk22/labb1/korpus", "r"); 
        //HERE RandomAccessFile fileText = new RandomAccessFile("exampletext.txt", "r"); 
        lengthOfText = fileText.length();



        char readChar = ' ';
        String siffran = "";

        long index = indexes_from_B[0];
        long index2 = indexes_from_B[1];

        // System.out.println(" we look in C between indexes " + index + " and " + index2);

        long startOfLine = 0;
        long endOfLine = 0;

        try {
          RandomAccessFile file = new RandomAccessFile("/var/tmp/c.txt", "r");
          // HERE RandomAccessFile file = new RandomAccessFile("c.txt", "r");

          while(true){


            file.seek(index);

            byte[] bs = new byte[1];
            file.read(bs);
            readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);

            index++;
            file.seek(index);

            file.read(bs);
            readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);
            index++;


            if(readChar == '\n') {
              
              // nu har vi siffran, vi ska anv?nda siffran!!
              
              // System.out.println("time to print the line " + siffran);

              startOfLine = Long.valueOf(siffran) - offset;


              if (startOfLine < 0){
                startOfLine = 0;
              }

              endOfLine = Long.valueOf(siffran) + offset + word.length();

              if (endOfLine > lengthOfText){
                endOfLine = lengthOfText;
              }


              byte[] konkordans_for_this_word = new byte[(int)(endOfLine-startOfLine)];


              fileText.seek(startOfLine);
              fileText.read(konkordans_for_this_word);

              for (byte bit : konkordans_for_this_word){
                if (bit == '\n'){
                  bit = ' ';
                }
                System.out.print((char) bit);
              }
              System.out.println();

              siffran = "";

              
            } else {
              siffran += readChar;
            }


            if (index >= index2) {
              // System.out.println("done with everything");
              break;
            }



          }
          
        
          file.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

        



      } catch (IOException e) {
          e.printStackTrace();
      }

    }

    // Hitta ordet 'row1' some finns tidigast p? plats 'index1' och innan plats 'index2' om den finns.
    // Om ordet inte finns, retunera '-1'.
    private static long[] getIndexesInB(long index1, long index2, String word) {
      // System.out.println("binary search between "+index1+" and "+index2);

      if (index1 >= index2) {
        System.out.println("REASON FOR NULL IS THIS");
        return null;
      }

      long mid = (index1 + index2) / 2;
      String[] rows_at_mid = getRowsFromB(mid);
      String word1 = rows_at_mid[0].split(" ")[0]; // row [0] before the space


      //System.out.println("rows_at_mid[0]: ."+rows_at_mid[0]+".");
      //System.out.println("rows_at_mid[1]: ."+rows_at_mid[1]+".");

      int compare = word1.compareTo(word);

      if (compare == 0) {
        long[] occurrences = new long[3];
        occurrences[0] = Long.parseLong(rows_at_mid[0].split(" ")[1]); // row [0] after the space

        if (!rows_at_mid[1].replaceAll("[^0-9]", "").equals("")) {
          occurrences[1] = Long.parseLong(rows_at_mid[1].split(" ")[1]); // row [1] after the space
        } else {
          occurrences[1] = lengthOfC+1;
        }


        occurrences[2] = Long.parseLong(rows_at_mid[0].split(" ")[2]); // times word occurred
        return occurrences;
      } else if (compare < 0) {
        // System.out.println(word + " is after " + word1);
        return getIndexesInB(mid+1, index2, word);
      } else if (compare > 0) {

        // System.out.println(word + " is before " + word1);
        return getIndexesInB(index1, mid, word);
      }
      // ska aldrig komma hit.
      return null;
    }

    private static String[] getRowsFromB(long seek) {
      String row1 = "";
      String row2 = "";
      try {
        RandomAccessFile file = new RandomAccessFile("/var/tmp/b.txt", "r");
        // HERE RandomAccessFile file = new RandomAccessFile("b.txt", "r");
        long fileLength = file.length();

        // Hitta b?rjan av ordet.
        char readChar = ' ';
        while(readChar != '\n'){
          if (seek < 0) {
            // System.out.println("Start of file b reached.");
            seek = -2;
            break;
          }
          file.seek(seek);

          byte[] bytes = new byte[1];
          file.read(bytes);

          readChar = (char) bytes[0];
          //System.out.println("char: ."+readChar+".");

          seek--;
        }
        seek = seek+2;

        // Hitta resten av raden.

        while(true){ // find row 1
          if (seek > fileLength) {
            // System.out.println("Fil B tog slut!");
            break;
          }
          file.seek(seek);

          byte[] bs = new byte[1];
          file.read(bs);
          readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);

          seek++;
          file.seek(seek);

          file.read(bs);
          readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);
          seek++;
          if(readChar == '\n') break;
          row1 += readChar;

        }

        while(true){ // find row 2
          if (seek > fileLength) {
            // System.out.println("Fil B tog slut!");
            break;
          }
          file.seek(seek);

          byte[] bs = new byte[1];
          file.read(bs);
          readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);

          seek++;
          file.seek(seek);

          file.read(bs);
          readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);
          seek++;
          if(readChar == '\n') break;
          row2 += readChar;

        }

        file.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      // System.out.println("Retunerar ordet ."+row1+".");

      String[] returnRows = {row1, row2};
      return returnRows;
	   }

    private static byte[] readCharsFromFile(String filePath, long seek, int chars) throws IOException {
      try {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(seek);
        byte[] bytes = new byte[chars];
        file.read(bytes);
        file.close();
        return bytes;
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
	   }

     // L?ss in arrayA fr?n fil och retunera arrayen.
     private static long[] retriveAFromFile() {
       long[] a = new long[30*30*30];
       try
        {
            FileInputStream fis = new FileInputStream("/var/tmp/arrayA");
            // HERE FileInputStream fis = new FileInputStream("arrayA");
            ObjectInputStream ois = new ObjectInputStream(fis);
            a = (long[]) ois.readObject();
            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }

        return a;
     }
}
