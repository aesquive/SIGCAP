package db.pojos;
// Generated 5/05/2014 10:56:00 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * User generated by hbm2java
 */
public class User  implements java.io.Serializable {


     private Integer iduser;
     private Tipousuario tipousuario;
     private String user;
     private String password;
     private Set regcuentausers = new HashSet(0);

    public User() {
    }

	
    public User(String user) {
        this.user = user;
    }
    public User(Tipousuario tipousuario, String user, String password, Set regcuentausers) {
       this.tipousuario = tipousuario;
       this.user = user;
       this.password = password;
       this.regcuentausers = regcuentausers;
    }
   
    public Integer getIduser() {
        return this.iduser;
    }
    
    public void setIduser(Integer iduser) {
        this.iduser = iduser;
    }
    public Tipousuario getTipousuario() {
        return this.tipousuario;
    }
    
    public void setTipousuario(Tipousuario tipousuario) {
        this.tipousuario = tipousuario;
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




}


