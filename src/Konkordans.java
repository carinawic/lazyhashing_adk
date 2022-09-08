import java.io.*;
//import org.apache.commons.lang3.StringUtils;

public class Konkordans {
    public static void main(String [ ] args) {
      // real data: /afs/kth.se/misc/info/kurser/DD2350/adk22/labb1
      if (args.length == 0) {
        System.err.println("Inget ord angivet.");
        return;
      }
      String completeWord = args[0];
      completeWord = completeWord.toLowerCase();
      System.out.println(completeWord);
      // if (completeWord.length() < 3) {
      //   completeWord = StringUtils.rightPad(completeWord, 3, " ");
      // }
      if (completeWord.length() == 1) {
        completeWord = completeWord + "  ";
      } else if (completeWord.length() == 2) {
        completeWord = completeWord + " ";
      }
      System.out.println("." + completeWord + ".");

      int lazyHashValue = charToInt(completeWord.charAt(0))*900 + charToInt(completeWord.charAt(1))*30 + charToInt(completeWord.charAt(2));
      System.out.println(lazyHashValue);

    }

    private static int charToInt(char ch) {
      if ((int) ch == ' ') {
         return 0;
      }
      // else if (ch == 'å') {
      //   return 27;
      // } else if (ch == 'ä') {
      //   return 28;
      // } else if (ch == 'ö') {
      //   return 29;
      // }
      int value = ((int) ch );
      value = value - 96;
      System.out.println("value: " + value);
      return value; 
    }
}
