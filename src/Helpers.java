public class Helpers{
    

    public int charToInt(char ch) {

      
        System.out.println("char is " + ch);

      if (ch == ' ') {
         return 0;
      }

      if(Character.isLetter(ch)) {

        if ((byte) ch == -27) {
          System.out.println("char is a with ring");
          return 27;
        } else if ((byte) ch == -28) {
          System.out.println("char is a with two dots");
          return 28;
        } else if ((byte)ch == -10) {
          System.out.println("char is o with dots");
          return 29;
        }
      }

      
      int value = ((int) ch );
      value = value - 96;
      //System.out.println("value: " + value);
      return value; 
    }

    public int getHash(String completeWord){
      // TODO: throw error if empty word
      completeWord = completeWord.toLowerCase();
      //System.out.println(completeWord);
      // if (completeWord.length() < 3) {
      //   completeWord = StringUtils.rightPad(completeWord, 3, " ");
      // }
      if (completeWord.length() == 1) {
        completeWord = completeWord + "  ";
      } else if (completeWord.length() == 2) {
        completeWord = completeWord + " ";
      }
      //System.out.println("." + completeWord + ".");

      int lazyHashValue = charToInt(completeWord.charAt(0))*900 + charToInt(completeWord.charAt(1))*30 + charToInt(completeWord.charAt(2));
      //System.out.println(lazyHashValue);
      return lazyHashValue;
    }
}