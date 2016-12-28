package tmp;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TableGenerator;
import javax.persistence.Entity;

@Entity
@Table(name="addresses", schema="persistance_test")
public class AddressInformation {
  @Id 
  @TableGenerator(name="addresses_key",
    table="pk_generator",
    pkColumnName="key_identifier",
    valueColumnName="generated_value",
    initialValue=1000)
  private long id;
  
  private String zipCode;
  
  public long getId() {
    return id;
  }
  
  public void setId(final long l) {
    id = l;
  }
  
  public String getZipCode() {
    return zipCode;
  }
  
  public void setZipCode(final String s) {
    zipCode = s;
  }  
}