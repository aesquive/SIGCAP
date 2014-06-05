package db.pojos;
// Generated 4/06/2014 10:53:47 PM by Hibernate Tools 3.6.0



/**
 * Permisosuser generated by hbm2java
 */
public class Permisosuser  implements java.io.Serializable,Comparable {


     private Integer idRegla;
     private User user;
     private Permisos permisos;
     private Integer valor;

    public Permisosuser() {
    }

    public Permisosuser(User user, Permisos permisos, Integer valor) {
       this.user = user;
       this.permisos = permisos;
       this.valor = valor;
    }
   
    public Integer getIdRegla() {
        return this.idRegla;
    }
    
    public void setIdRegla(Integer idRegla) {
        this.idRegla = idRegla;
    }
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    public Permisos getPermisos() {
        return this.permisos;
    }
    
    public void setPermisos(Permisos permisos) {
        this.permisos = permisos;
    }
    public Integer getValor() {
        return this.valor;
    }
    
    public void setValor(Integer valor) {
        this.valor = valor;
    }

    @Override
    public int compareTo(Object t) {
        Permisosuser compare=(Permisosuser)t;
        return this.getPermisos().getIdPermiso().compareTo(compare.getPermisos().getIdPermiso());
    }




}


