import net.ic4fre.nytr.hf2.MainClass;

public class Main {
  public static final void main(String[] sa) {
    try {  
      new Rdp().parse(System.in).stream().forEach(e -> System.out.println(e));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}