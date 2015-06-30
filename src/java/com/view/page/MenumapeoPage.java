package com.view.page;


import com.view.page.BorderPage;
import com.view.page.MapeoPage;
import db.controller.DAO;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;

/**
 *
 * @author desarrollo
 */
public class MenumapeoPage extends BorderPage {

    public Form form;
    List<Regcuenta> regcuentas;
    Select select;
    @Override
    public void init() {
        message=null;
        form=new Form("form");
        title="Mapeo de datos";
        regcuentas=getNotMappedRegCuentas();
        if(regcuentas.isEmpty()){
            form.add(new Label("name","Actualmente no hay datos por mapear"));
            return;
        }
        select=new Select("select", "Nombre Ejercicio: ",true);
        for(Regcuenta r:regcuentas){
            select.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
        }
        form.add(select);
        form.add(new Submit("sub","Mapear datos", this, "startMapping"));
        
    }
    
    public boolean startMapping(){
        Regcuenta regCtaSelec=null;
        for(Regcuenta r:regcuentas){
            if(r.getIdRegCuenta().toString().equals(select.getValue())){
                regCtaSelec=r;
            }
        }
        addSessionVar("mapeoRegCuenta",regCtaSelec);
        setRedirect(MapeoPage.class);
        return true;
    }

    @Override
    public Integer getPermisoNumber() {
        return 2;
    }

    private List<Regcuenta> getNotMappedRegCuentas() {
        List<Regcuenta> resultado=new LinkedList<Regcuenta>();
        List<Cuenta> createQuery = DAO.createQuery(Cuenta.class, null);
        Set<Integer> idsRegs=new HashSet<Integer>();
        for(Cuenta c:createQuery){
            if(c.getCatalogocuenta().getIdCatalogoCuenta()==1){
                idsRegs.add(c.getRegcuenta().getIdRegCuenta());
            }
        }
        List<Regcuenta> regCtas = DAO.createQuery(Regcuenta.class,null);
        for(Regcuenta r:regCtas){
            if(!idsRegs.contains(r.getIdRegCuenta())){
                resultado.add(r);
            }
        }
        return resultado;
    }

}
