package db.pojos;
// Generated 4/06/2014 10:53:47 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * User generated by hbm2java
 */
public class User  implements java.io.Serializable {


     private Integer iduser;
     private String user;
     private String password;
     private Set regcuentausers = new HashSet(0);
     private Set trackings = new HashSet(0);
     private Set permisosusers = new HashSet(0);

    public User() {
    }

	
    public User(String user) {
        this.user = user;
    }
    public User(String user, String password, Set regcuentausers, Set trackings, Set permisosusers) {
       this.user = user;
       this.password = password;
       this.regcuentausers = regcuentausers;
       this.trackings = trackings;
       this.permisosusers = permisosusers;
    }
   
    public Integer getIduser() {
        return this.iduser;
    }
    
    public void setIduser(Integer iduser) {
        this.iduser = iduser;
    }
    public String getUser() {
        return this.user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public Set getRegcuentausers() {
        return this.regcuentausers;
    }
    
    public void setRegcuentausers(Set regcuentausers) {
        this.regcuentausers = regcuentausers;
    }
    public Set getTrackings() {
        return this.trackings;
    }
    
    public void setTrackings(Set trackings) {
        this.trackings = trackings;
    }
    public Set getPermisosusers() {
        return this.permisosusers;
    }
    
    public void setPermisosusers(Set permisosusers) {
        this.permisosusers = permisosusers;
    }




}


