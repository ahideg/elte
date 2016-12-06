package net.ic4fre.nytr.hf2;

public class MainClass {
  public static void main(String[] sa) {
    try {  
      new Rdp().parse(System.in).stream().forEach(e -> System.out.println(e));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}

