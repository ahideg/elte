package eaf.jpa;

class HibernateSupport {
  static final String configData =
     "<!DOCTYPE hibernate-configuration PUBLIC                                                               " +
     "       \"-//Hibernate/Hibernate Configuration DTD 3.0//EN\"                                               " +
     "       \"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd\">                                  " +
     "                                                                                                        " +
     " <hibernate-configuration>                                                                              " +
     "     <session-factory>                                                                                   " +
     "         <!-- Database connection settings -->                                                          " +
     "         <property name=\"hibernate.connection.driver_class\">org.h2.Driver</property>                  " +
     "         <property name=\"hibernate.connection.url\">" + H2Support.tcpUrl + "</property>                 " +
     "         <property name=\"hibernate.connection.username\">sax</property>                                  " +
     "         <property name=\"hibernate.connection.password\">sa</property>                               " +
     "                                                                                                        " +
     "         <!-- JDBC connection pool (use the built-in) -->                                               " +
     "         <property name=\"connection.pool_size\">1</property>                                             " +
     "                                                                                                        " +
     "         <!-- SQL dialect -->                                                                           " +
     "         <property name=\"hibernate.dialect\">org.hibernate.dialect.H2Dialect</property>                 " +
     "                                                                                                        " +
     "         <!-- Disable the second-level cache  -->                                                       " +
     "         <property name=\"cache.provider_class\">org.hibernate.cache.internal.NoCacheProvider</property>  " +
     "                                                                                                        " +
     "         <!-- Echo all executed SQL to stdout -->                                                       " +
     "         <property name=\"show_sql\">true</property>                                                      " +
     "                                                                                                        " +
     "         <!-- Drop and re-create the database schema on startup -->                                     " +
     "         <property name=\"hbm2ddl.auto\">create</property>                                                " +
     "                                                                                                        " +
     "         <!-- Names the annotated entity class -->                                                      " +
     "         <mapping class=\"tmp.House\"/>                                                                " +
     "         <mapping class=\"tmp.Vehicle\"/>                                                              " +
     "         <mapping class=\"tmp.Person\"/>                                                               " +
     "         <mapping class=\"tmp.AddressInformation\"/>                                                   " +  
     "     </session-factory>                                                                                 " +
     " </hibernate-configuration>                                                                             ";  
  
}