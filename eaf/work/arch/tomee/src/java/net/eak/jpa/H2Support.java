package tmp;

import java.sql.Connection;
import java.sql.DriverManager;
import org.h2.tools.Server;

class H2Support {
  static final String serverPort = "11111";
  static final String dbpath = "c:/Users/Andras/Desktop/stuff/melo/jpa_test/test/testdb";
  static final String tcpUrl = "jdbc:h2:tcp://localhost:" + serverPort + "/" + dbpath;
  private Server server;
  
  void startDatabase() throws Exception {
    final String[] args = new String[] {"-tcpAllowOthers", "-tcpPort", serverPort};
    server = Server.createTcpServer(args).start();
  }
  
  Connection connect() throws Exception {
    Class.forName("org.h2.Driver");
    final Connection conn = DriverManager.getConnection("jdbc:h2:" + dbpath, "sa", "sa");
    return conn;
  } 
    
  Connection connectTcp() throws Exception {
    Class.forName("org.h2.Driver");
    final Connection conn = DriverManager.getConnection(tcpUrl, "sa", "sa");
    return conn;
  } 
  
  void stopDatabase() throws Exception  {
    server.stop();
  }
}