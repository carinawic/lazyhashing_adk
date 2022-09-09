import java.io.*;

//import org.apache.commons.lang3.StringUtils;

public class Konkordans {
    public static void main(String [] args) {

      System.setProperty("file.encoding","ISO_8859_1");
      Field charset = Charset.class.getDeclaredField("defaultCharset");
      charset.setAccessible(true);
      charset.set(null,null);

      Helpers helper = new Helpers();

      // real data: /afs/kth.se/misc/info/kurser/DD2350/adk22/labb1
      if (args.length == 0) {
        System.err.println("Inget ord angivet.");
        return;
      }

      String completeWord = args[0];

      int hashedValue = helper.getHash(completeWord);
      System.out.println(hashedValue);



    }
}
