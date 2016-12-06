package net.ic4fre.nytr.hf2;

import java.util.List;

public class MainClass {
  public static void main(String[] sa) {
    try {  
      final Rdp rdp = new Rdp();
      final List<Rdp.ParsedResult> results = rdp.parse(System.in);
      results.stream().forEach(e -> System.out.println(e.success()));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}