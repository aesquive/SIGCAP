package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.executor.ModelExecutor;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.NumberField;
import util.Util;

public class EditarvariablesimulacionPage extends BorderPage {

    //variables de sesion que nos indican los campos que corresponde a edicion asi como su descripcion
    private String[] noEditAttributeMethod;
    private String[] noEditAttributeName;
    private String[] editAttributeMethod;
    private String[] editAttributeName;
    //objeto sobre el cual se va a editar 
    private Object target;
    private Form formEdit;
    //nombre que se muestra en la pantalla
    public String screenName;
    //este mapeo solo guardara aquellas variables que son editables
    private Map<String, Field> mapNamesFields;

    @Override
    public void init() {
        message=null;
        formEdit = new Form("formEdit");
        title = "Simulación de Capital - Editar Variable";
        screenName = (String) getSessionVar("simEditScreenName");
        target = getSessionVar("simEditTarget");
        //sacamos las caracteristicas editables y no editables del objeto
        noEditAttributeMethod = (String[]) getSessionVar("simNoEditColumns");
        noEditAttributeName = (String[]) getSessionVar("simNoEditColumnsDes");
        editAttributeMethod = (String[]) getSessionVar("simEditColumns");
        editAttributeName = (String[]) getSessionVar("simEditColumnsDes");

        mapNamesFields = new HashMap<String, Field>();

        //agregamos los datos no editables
        FieldSet fSetNoEdit = new FieldSet("fsNoEdit", "Atributos No Editables");
        for (int t = 0; t < noEditAttributeName.length; t++) {
            Field field = createTextField(noEditAttributeName[t], noEditAttributeMethod[t]);
            field.setTextAlign("left");
            field.setReadonly(true);
            fSetNoEdit.add(field);

        }
        formEdit.add(fSetNoEdit);

        //agregamos los datos editables    
        FieldSet fSetEdit = new FieldSet("fsEdit", "Atributos Editables");
        for (int t = 0; t < editAttributeName.length; t++) {
            Field field = createTextField(editAttributeName[t], editAttributeMethod[t]);
            field.setTextAlign("left");
            field.setRequired(true);
            mapNamesFields.put(editAttributeMethod[t], field);
            fSetEdit.add(field);
        }
        formEdit.add(fSetEdit);

        formEdit.add(new Submit("subsave", "Guardar", this, "guardar"));
        formEdit.add(new Submit("subcancel", "Cancelar", this, "cancelar"));

        addControl(formEdit);
    }

    public boolean guardar() {
        if (formEdit.isValid()) {
            Set<String> nameVars = mapNamesFields.keySet();
            boolean saveObject = true;
            for (String nameVar : nameVars) {
                Field fieldContainer = mapNamesFields.get(nameVar);
                Class returnGetter = Util.getReturnType(Util.getMethod(target.getClass(), "get" + nameVar, null));
                Method setter = Util.getMethod(target.getClass(), "set" + nameVar, returnGetter);
                Object fieldValue = getFieldValue(returnGetter, fieldContainer);
                boolean reflectionInvokeSet = Util.reflectionInvokeSet(target, setter, fieldValue);
                System.out.println("el set de " + nameVar + "el valor es " + fieldValue + " fue " + reflectionInvokeSet);
                if (!reflectionInvokeSet) {
                    saveObject = false;
                }
            }
            if (saveObject) {
                DAO.update(target);
                System.out.println("guardo el dato sobre " + target);
                DAO.saveRecordt(user, "modifico un dato de "+screenName);
                setRedirect(SimulaciondataPage.class);
                //message="Edición de variable completa";
                executeModel();
                return true;
            }
        }
        message="Favor de completar los campos pendientes";
        return false;
    }

    private Object getFieldValue(Class claseParametro, Field fieldContainer) {
        if (claseParametro == Date.class) {
            DateField cast = (DateField) fieldContainer;
            return cast.getDate();
        }
        if (claseParametro == Integer.class) {
            NumberField cast = (NumberField) fieldContainer;
            return cast.getNumber().intValue();
        }
        if (claseParametro == Double.class) {
            NumberField cast = (NumberField) fieldContainer;
            return cast.getNumber().doubleValue();
        }
        if (claseParametro == String.class) {
            TextField cast = (TextField) fieldContainer;
            return cast.getValue();
        }
        return null;
    }

    public boolean cancelar() {
        setRedirect(SimulaciondataPage.class);
        return true;
    }

    /**
     * Genera un campo para poner dentro de la pagina de acuerdo si es numero, o
     * letras o fechas el nombre del campo es el nameMethod pasado como
     * argumento, para facilitar los setters posteriormente
     *
     * @param nameField
     * @param nameMethod
     * @return
     */
    private Field createTextField(String nameField, String nameMethod) {

        Class returnClass = Util.getReturnType(Util.getMethod(target.getClass(), "get" + nameMethod));

        //en caso de que el tipo de retorno sea una fecha
        if (returnClass == Date.class) {
            DateField field = new DateField(nameMethod, nameField);
            field.setFormatPattern("dd/MM/yyyy");
            field.setDate((Date) Util.reflectionInvoke(target, "get" + nameMethod));
            return field;
        }
        //en caso de que el tipo de retorno sea un numero
        if (returnClass == Number.class || returnClass == Double.class || returnClass == Integer.class) {
            NumberField field = new NumberField(nameMethod, nameField);
            field.setPattern("###,###,###.#####");
            field.setNumber((Number) Util.reflectionInvoke(target, "get" + nameMethod));
            return field;
        }
        //en caso de que el tipo de retorno sea texto
        if (returnClass == String.class || returnClass == Long.class) {
            TextField text = new TextField(nameMethod, nameField);
            text.setValue(Util.reflectionString(target, "get" + nameMethod));
            return text;
        }
        return null;
    }

    private void executeModel() {
        try {

            Regcuenta tmp = (Regcuenta) getSessionVar("prySim");
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            for (Regcuenta r : createQuery) {
                if (r.getIdRegCuenta() == tmp.getIdRegCuenta()) {

                    ModelExecutor executor = new ModelExecutor(r, true);
                    executor.start();

                }
            }
        } catch (Exception e) {
            System.out.println("Error EditarVariablesCalculo.executeModel() " + e.getStackTrace().toString());
        }
    }

    @Override
    public Integer getPermisoNumber() {
        return -1;
    }

}
