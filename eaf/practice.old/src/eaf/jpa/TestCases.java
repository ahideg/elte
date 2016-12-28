package eaf.jpa;

import java.sql.Connection;
import java.io.ByteArrayInputStream;

import org.hibernate.cfg.Configuration;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;

public class TestCases {
  private static final H2Support h2Support = new H2Support();
  
  public static void main(String[] sa) {
    echo("Starting tests...");
    try {
      //casePingServer();
      casePersistenceWarmup();
    } catch(Exception e) {
      echo("\n*************************\nmain() error: " + e.getMessage());
      e.printStackTrace();
    }
  }
 
  static void casePingServer() throws Exception {
    echo("\n---------- casePingServer()");
    h2Support.startDatabase();
    echo("server started");
    Thread.sleep(1000);
    final Connection conn = h2Support.connectTcp();
    echo("connected to db");
    Thread.sleep(1000);
    conn.close();
    echo("server stopped");
    h2Support.stopDatabase();
    Thread.sleep(1000);
  }
   
  static void casePersistenceWarmup() throws Exception {
    echo("\n---------- casePersistenceWarmup()");
    h2Support.startDatabase();
    
    Thread.sleep(1000);
    //final Connection connection = h2Support.connect();
    
    /*final BootstrapServiceRegistryBuilder bootstrapServiceRegistryBuilder =
      new BootstrapServiceRegistryBuilder();
    
    final BootstrapServiceRegistry bootstrapServiceRegistry =
      bootstrapServiceRegistryBuilder.build();*/
      
	  final MetadataSources metadataSources = new MetadataSources();
	  
	  metadataSources.addInputStream(new ByteArrayInputStream(HibernateSupport.configData.getBytes()));  
	    
	  final MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();    
	  final Metadata metadata = metadataBuilder.build();
	  //final Metadata metadata = metadataSources.buildMetadata();
    
    Thread.sleep(2000);
    //connection.close();
    h2Support.stopDatabase();
  }
  
  static void echo(final Object o) {
    try {
      System.out.println(o.toString());
    } catch(Exception e) {
      System.out.println("Echo error: " + e.getMessage());
    }
  }
}