import java.io.*;

public class LazyHashing {
    public static void main(String [ ] args) {
      // real data: /afs/kth.se/misc/info/kurser/DD2350/adk22/labb1
      //

      try {
        FileInputStream inputStream
            = new FileInputStream("exampletext.txt");

        BufferedInputStream buffInputStr
            = new BufferedInputStream(
                inputStream);

        while (buffInputStr.available() > 0) {

            char c = (char)buffInputStr.read();

            System.out.println("Char : " + c);
        }

      }
      catch(Exception e) {


      }
    }
}
