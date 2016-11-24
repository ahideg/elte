* Altalanos infok

   Java 1.8 alatt fordul es fut a program.

   Windows 8-on keszult el es ugyanitt teszteltem.


* Fuggosegek

  A JRE 8 -on kivul a  <project_home>/lib/javax.json-1.0.4  JSON parser library. 


* Hasznalat

  (1) Maven-nel (3.1.1 vagy magasabb verzio)

    mvn package 

      erre lefordul
  
    echo { \"con\": { \"x\":\"Str\"}, \"exp\": { \"op\":\"Var\",\"args\":\"x\"}, \"ty\": \"Str\"} | mvn exec:java
    
      Erre parse-olja az inputot Windows shellben.
      Figyelem, a double quotation mark (") karaktereket escape-elni kell backslash-sel,
      es lehet, hogy az mvn parancsot absolute path-tel kell meghivni.
      A JSON parser library-t a Maven online keresi meg, nem lokalban.
      Nagyon fontos, hogy a command line inputban windowson a curly bracket ({) es a double quote (") koze 
      MINDIG KELL EGY SPACE, kulonben a program nem kepes parse-olni a JSON-t.      


  (2) Ha (1) valamiert nem mukodik, akkor Ant-tal (1.9.4 vagy magasabb verzio)
   
    ant test
      
      ez leteszteli hogy OK -e a forditas es a program
      
    echo { \"con\": { \"x\":\"Str\"}, \"exp\": { \"op\":\"Var\",\"args\":\"x\"}, \"ty\": \"Str\"} | ant run
       
      Ez lefuttatja a parsert a standard input bemenettel.
      Figyelem, a double quotation mark (") karaktereket escape-elni kell backslash-sel,
      es lehet, hogy az ant parancsot absolute path-tel kell meghivni.
      Nagyon fontos, hogy a command line inputban windowson a curly bracket ({) es az 
      escaped double quote (\") koze MINDIG KELL EGY SPACE, kulonben a program nem kepes parse-olni a JSON-t.
    
  
  (3) Ha (1) es (2) nem mukodnek
     
    Legyszi kuldj egy mail-t a  hidega@caesar.elte.hu  -ra es javitom.
    
    
    
Hideg Andras
IC4FRE  
MSc esti 
2016-11-24    
