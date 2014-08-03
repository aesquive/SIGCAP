package com.view.page;

import db.controller.DAO;
import db.pojos.Captacion;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.extras.control.FormTable;

/**
 *
 * @author Admin
 */
public class SimulacioncaptacionPage extends SimulacionPage {

    private FormTable table;
    private List<Captacion> values;
    
    public SimulacioncaptacionPage() {
        table = new FormTable("dataTable", getForm());
        table.setPageNumber(0);
        String[] columnNames = Captacion.getSimColumns();
        String[] columnDescriptions = Captacion.getSimDesColumns();
        for (int t = 0; t < columnNames.length; t++) {
            Column c = new Column(columnNames[t], columnDescriptions[t]);
            table.addColumn(c);
        }
        values = new LinkedList<Captacion>(getRegCta().getCaptacions());
        Collections.sort(values);
        for (int t = 0; t < values.size(); t++) {
            ActionLink actionLink = new ActionLink("edit" + values.get(t).getIdCaptacion(), "Editar", this, "onEditClick");
            actionLink.setValue(values.get(t).getIdCaptacion().toString());
            values.get(t).setSimEdit(actionLink);
            addControl(actionLink);
            
            ActionLink delete = new ActionLink("delete" + values.get(t).getIdCaptacion(), "Borrar", this, "onDeleteClick");
            delete.setValue(values.get(t).getIdCaptacion().toString());
            values.get(t).setSimDelete(delete);
            addControl(delete);
            
        }
        Column c=new Column("accion","Acción");
        c.setDecorator(new Decorator() {
            @Override
            public String render(Object object, Context context) {
                Captacion c=(Captacion)object;
                return " "+c.getSimEdit().toString()+" | "+c.getSimDelete().toString()+" ";
            }
        });
        table.addColumn(c);
        FieldSet fs = new FieldSet("fs", getRegCta().getDesRegCuenta());
        fs.add(table);
        getForm().add(fs);        
        table.setRowList(values);
    }

    
    public boolean onEditClick(){
        Captacion clicked=null;
        for(Captacion c:values){
            if(c.getSimEdit().isClicked()){
                clicked=c;
            }
        }
        addSessionVar("editReg", clicked);
        setRedirect(CaptacioneditPage.class);
        return true;
    }
    
    public boolean onDeleteClick(){
       Captacion clicked=null;
        for(Captacion c:values){
            if(c.getSimDelete().isClicked()){
                clicked=c;
            }
        }
        DAO.delete(clicked);
        message="Registro eliminado";
        return true;
    }
    
}
