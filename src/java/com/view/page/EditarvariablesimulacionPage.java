package com.view.page;

import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.TextField;


public class EditarvariablesimulacionPage extends BorderPage {

    private String[] noEditAttributeMethod;
    private String[] noEditAttributeName;
    private String[] editAttributeMethod;
    private String[] editAttributeName;
    private Form formEdit;
    
    @Override
    public void init() {
        formEdit=new Form("formEdit");
        title="Simulación de Capital - Editar Variable";
        
        //sacamos las caracteristicas editables y no editables del objeto
        noEditAttributeMethod = (String[]) getSessionVar("simNoEditColumns");
        noEditAttributeName = (String[]) getSessionVar("simNoEditColumnsDes");
        editAttributeMethod = (String[]) getSessionVar("simEditColumns");
        editAttributeName = (String[]) getSessionVar("simEditColumnsDes");
        
        FieldSet fSetNoEdit=new FieldSet("fsNoEdit", "Atributos No Editables");
        for(int t=0;t<noEditAttributeName.length;t++){
            TextField textFieldNoEdit=createTextField(noEditAttributeName[t]);
            textFieldNoEdit.setReadonly(true);
            fSetNoEdit.add(textFieldNoEdit);
        }
        formEdit.add(fSetNoEdit);
        
        FieldSet fSetEdit=new FieldSet("fsEdit", "Atributos Editables");
        for(int t=0;t<editAttributeName.length;t++){
            TextField textFieldEdit=createTextField(editAttributeName[t]);
            fSetEdit.add(textFieldEdit);
        }
        formEdit.add(fSetEdit);
        
        addControl(formEdit);
    }

    /**
     * crea un textfield dada la etiqueta deseada, el nombre sera la misma etiqueta sin espacios y en minusculas
     * @param label
     * @return 
     */
    private TextField createTextField(String label) {
        TextField textfield=new TextField(label.trim().toLowerCase(), label);
        return textfield;
    }

}