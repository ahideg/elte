package tmp;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TemporalType;
import javax.persistence.OneToOne;
import javax.persistence.ManyToMany;
import javax.persistence.Entity;
import javax.persistence.TableGenerator;

import java.sql.Date;
import java.util.Collection;

@Entity
@Table(name="houses", schema="persistance_test")
public class House extends Building {
  @Id 
  @TableGenerator(name="houses_key",
    table="pk_generator",
    pkColumnName="key_identifier",
    valueColumnName="generated_value",
    initialValue=1000)
  private long id;
  
  @Temporal(TemporalType.DATE)
  private Date constructionDate;
  
  @OneToOne
  private AddressInformation addressInformation;
  
  @ManyToMany
  private Collection<Person> owners;
  
  public long getId() {
    return id;
  }
  
  public void setId(final long l) {
    id = l;
  }
  
  public Date getConstructionDate() {
    return constructionDate;
  }  
  
  public void setConstructionDate(final Date d) {
    constructionDate = d;
  }  
  
  public AddressInformation getAddressInformation() {
    return addressInformation;
  }  
  
  public void setAddressInformation(final AddressInformation d) {
    addressInformation = d;
  }
  
  public Collection<Person> getOwners() {
    return owners;
  }
  
  public void setId(final Collection<Person> c) {
    owners = c;
  }  
}