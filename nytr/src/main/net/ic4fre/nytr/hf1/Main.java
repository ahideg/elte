package net.ic4fre.nytr.hf1;

public class Main {
  public static void main(String[] sa) {
    try {  
      final Rdp rdp = new Rdp();
      final Rdp.ParsedResult r = rdp.parse(System.in);
      System.out.println("YES");
    } catch(Exception e) {
      //e.printStackTrace();
      System.out.println("NO");
    }
  }
}