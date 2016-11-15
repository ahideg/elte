package eaf.jpa;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.TableGenerator;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;

import java.util.Collection;

@Entity
@Table(name="persons", schema="persistance_test")
public class Person {
  @Id 
  @TableGenerator(name="persons_key",
    table="pk_generator",
    pkColumnName="key_identifier",
    valueColumnName="generated_value",
    initialValue=1000)
  private long id;
  
  @OneToMany
  private Collection<Vehicle> vehicles;
  
  @ManyToMany
  private Collection<Person> owners;
  
  private String[] names;
  
  public long getId() {
    return id;
  }
  
  public void setId(final long l) {
    id = l;
  }  
  
  public String[] getNames() {
    return names;
  }
  
  public void setNames(final String[] s) {
    names = s;
  }
  
  public Collection<Person> getOwners() {
    return owners;
  }
  
  public void setId(final Collection<Person> c) {
    owners = c;
  }  
}