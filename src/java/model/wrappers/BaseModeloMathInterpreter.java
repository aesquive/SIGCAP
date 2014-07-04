package model.wrappers;

import db.controller.DAO;
import db.pojos.Captacion;
import db.pojos.Catalogominimo;
import db.pojos.Disponibilidad;
import db.pojos.Prestamo;
import db.pojos.Regcuenta;
import db.pojos.Reservas;
import db.pojos.Tarjetacredito;
import db.pojos.Valores;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import util.Reflector;

/**
 *
 * @author Admin
 */
public class BaseModeloMathInterpreter {

    private List<CatalogoMinimoWrapper> catalogoMinimoWrapper;
    private List<CaptacionWrapper> captacionWrapper;
    private List<DisponibilidadWrapper> disponibilidadWrapper;
    private List<PrestamoWrapper> prestamoWrapper;
    private List<TarjetaCreditoWrapper> tarjetaWrapper;
    private List<Valores> tenenciaWrapper;
    private List<Reservas> reservas;
    private Map<String, Double> mapValues;
    private Regcuenta regCuenta;

    public BaseModeloMathInterpreter(Regcuenta regCuenta) {
        mapCaptaciones(regCuenta.getCaptacions());
        mapCatalogoMinimo(regCuenta.getCatalogominimos());
        mapDisponibilidad(regCuenta.getDisponibilidads());
        mapPrestamos(regCuenta.getPrestamos());
        mapTarjetaCredito(regCuenta.getTarjetacreditos());
        mapTenencia(regCuenta.getValoreses());
        reservas = new LinkedList<Reservas>(regCuenta.getReservases());
    }

    private void mapCaptaciones(Set<Captacion> captacions) {
        captacionWrapper = new LinkedList<CaptacionWrapper>();
        for (Captacion cap : captacions) {
            captacionWrapper.add(new CaptacionWrapper(cap.getFecha(), cap.getCatalogocuenta() != null ? cap.getCatalogocuenta().getIdCatalogoCuenta().toString() : "", cap.getDescripcion(), cap.getIdCaptacion().toString(), cap.getMonto(), cap.getFechaVencimiento()));
        }
    }

    private void mapCatalogoMinimo(Set<Catalogominimo> catalogominimos) {
        catalogoMinimoWrapper = new LinkedList<CatalogoMinimoWrapper>();
        for (Catalogominimo cat : catalogominimos) {
            catalogoMinimoWrapper.add(new CatalogoMinimoWrapper(cat.getCatalogocuenta() != null ? cat.getCatalogocuenta().getIdCatalogoCuenta().toString() : "", cat.getValor()));
        }
    }

    private void mapDisponibilidad(Set<Disponibilidad> disponibilidads) {
        disponibilidadWrapper = new LinkedList<DisponibilidadWrapper>();
        for (Disponibilidad disp : disponibilidads) {
            disponibilidadWrapper.add(new DisponibilidadWrapper(disp.getFecha(), disp.getCatalogocuenta().getIdCatalogoCuenta().toString(), disp.getDescripcion(), disp.getMonto(), disp.getFechaVencimiento()));
        }
    }

    private void mapPrestamos(Set<Prestamo> prestamos) {
        prestamoWrapper = new LinkedList<PrestamoWrapper>();
        for (Prestamo pr : prestamos) {
            prestamoWrapper.add(new PrestamoWrapper(pr.getFecha(), pr.getCatalogocuenta() != null ? pr.getCatalogocuenta().getIdCatalogoCuenta().toString() : "", pr.getIdPrestamo().toString(), pr.getDescripcion(),
                    pr.getSaldo(), pr.getFechaDeCorte(), pr.getTipoPrestamo(), pr.getRelevante() == 1 ? "SI" : "NO"));
        }
    }

    private void mapTarjetaCredito(Set<Tarjetacredito> tarjetacreditos) {
        tarjetaWrapper = new LinkedList<TarjetaCreditoWrapper>();
        for (Tarjetacredito tar : tarjetacreditos) {
            tarjetaWrapper.add(new TarjetaCreditoWrapper(tar.getFecha(), tar.getCatalogocuenta() != null ? tar.getCatalogocuenta().getIdCatalogoCuenta().toString() : "", tar.getIdCredito(),
                    tar.getDescripcion(), tar.getSaldoInsoluto(), tar.getFechaCorte(), tar.getTipoTarjeta(), tar.getRelevante() == 1 ? "SI" : "NO"));
        }
    }

    private void mapTenencia(Set<Valores> valoreses) {
        tenenciaWrapper = new LinkedList<Valores>(valoreses);
    }

    /**
     * aplica las condiciones sobre los objetos pasados en la lista , si el
     * objeto cumple esa propiedad suma el parametro dado
     *
     * @param obj
     * @param conds
     * @return
     */
    public double interpSumIf(List objects, String parametroSumar, String condsConcat) {
        double sum = 0.0;
        for (Object ob : objects) {
            boolean cumple = true;
            if (condsConcat != null) {
                String[] conds = condsConcat.split(",");
                for (String cond : conds) {
                    if (cumple) {
                        String[] splitOperandos = cond.split("/");
                        String operacion = splitOperandos[1];
                        Object valor = Reflector.callMethod(ob, null, splitOperandos[0]);
                        //System.out.println(splitOperandos[0]+"="+valor);
                        if (operacion.equals("==")) {
                            Integer valorObj = (Integer) valor;
                            Integer ref = Integer.parseInt(splitOperandos[2]);
                            if (valorObj- ref!=0) {
                                cumple = false;
                            }
                        } else if (operacion.equals(">=")) {
                            Integer valorObj = (Integer) valor;
                            Integer ref = Integer.parseInt(splitOperandos[2]);
                            if (valorObj < ref) {
                                cumple = false;
                            }
                        } else if (operacion.equals("<=")) {
                            Integer valorObj = (Integer) valor;
                            Integer ref = Integer.parseInt(splitOperandos[2]);
                            if (valorObj > ref) {
                                cumple = false;
                            }
                        } else if (operacion.equals("eq")) {
                            String valorObj = (String) valor;
                            String reg = splitOperandos[2];
                         //   System.out.println("comparando "+valorObj+" vs "+reg);
                            if (!valorObj.toUpperCase().trim().equalsIgnoreCase(reg.toUpperCase().trim())) {
                       //         System.out.println("cae dentro");
                                cumple = false;
                            }
                        } else if (operacion.equalsIgnoreCase("ed")) {
                            Double valorD = Double.parseDouble(valor.toString());
                            Double reg = Double.parseDouble(splitOperandos[2]);
                            if (0.0 != (valorD - reg)) {
                                cumple = false;
                            }
                        }
                    }
                }
            }
            if (cumple) {
                Double sumParam = (Double) Reflector.callMethod(ob, null, parametroSumar);
                //System.out.println("a sumar "+sumParam);
                sum += sumParam;
            }
        }
        return sum;
    }

    public Map<String, Double> getMap() {
        return mapValues;
    }

    public void calculate() {
        mapValues = new HashMap<String, Double>();
        mapValues.put("93916", 208881802.0);
        mapValues.put("95560", 3284206650.0);
        mapValues.put("95645", 2562507.0);
        mapValues.put("95560", 3284206650.00);
        mapValues.put("95666", 2562507.00);
        mapValues.put("94020", 2848918269.00);
        mapValues.put("94030", 3177338934.00);
        mapValues.put("94050", 2968457132.00);
        mapValues.put("94100", 2562507.00);

        mapValues.put("10050", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/1,getPlazo/<=/7,getCuenta/eq/110100000000")
                + interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/1,getPlazo/<=/7,getCuenta/eq/110200000000")
                - interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/1,getPlazo/<=/7,getCuenta/eq/110202000000"));
        mapValues.put("10100", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/1,getPlazo/<=/7,getCuenta/eq/110403000000"));
        mapValues.put("10102", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/8,getPlazo/<=/31,getCuenta/eq/110403000000"));
        mapValues.put("10104", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/32,getPlazo/<=/92,getCuenta/eq/110403000000"));
        mapValues.put("10106", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/93,getPlazo/<=/184,getCuenta/eq/110403000000"));
        mapValues.put("10108", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/185,getPlazo/<=/366,getCuenta/eq/110403000000"));
        mapValues.put("10110", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/367,getPlazo/<=/731,getCuenta/eq/110403000000"));
        mapValues.put("10112", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/732,getPlazo/<=/1096,getCuenta/eq/110403000000"));
        mapValues.put("10114", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/1097,getPlazo/<=/1461,getCuenta/eq/110403000000"));
        mapValues.put("10116", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/1462,getPlazo/<=/1827,getCuenta/eq/110403000000"));
        mapValues.put("10118", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/1828,getPlazo/<=/2557,getCuenta/eq/110403000000"));
        mapValues.put("10120", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/2558,getPlazo/<=/3653,getCuenta/eq/110403000000"));
        mapValues.put("10122", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/3654,getPlazo/<=/5479,getCuenta/eq/110403000000"));
        mapValues.put("10124", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/5480,getPlazo/<=/7305,getCuenta/eq/110403000000"));
        mapValues.put("10126", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/7306,getPlazo/<=/100000,getCuenta/eq/110403000000"));

        mapValues.put("10150", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131101020000") * .006
                + (interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102010000")
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102020000")) * .0524);
        mapValues.put("10152", (interpSumIf(tarjetaWrapper, "getSaldo", "getCreditoRelevante/eq/NO")-interpSumIf(tarjetaWrapper, "getSaldo", "getCreditoRelevante/eq/SI"))*.8);
        mapValues.put("10154", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131101020000") * .1418
                + (interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102010000")
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102020000")) * .2853
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102030000") * .1655);
        mapValues.put("10156", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131101020000") * .2757
                + (interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102010000")
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102020000")) * .5804
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102030000") * .6244);
        mapValues.put("10158", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131101020000") * .4171
                + (interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102010000")
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102020000")) * .0004
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102030000") * .2101);
        mapValues.put("10160", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131101020000") * .1291
                + (interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102010000")
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102020000")) * .00
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131102030000") * 0);
        mapValues.put("10162", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/131101020000") * .0001);

        
        mapValues.put("10450", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/1,getPlazoRC01/<=/7,getMoneda/eq/MXN"));
        mapValues.put("10452", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/8,getPlazoRC01/<=/31,getMoneda/eq/MXN"));
        mapValues.put("10454", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/32,getPlazoRC01/<=/92,getMoneda/eq/MXN"));
        mapValues.put("10456", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/93,getPlazoRC01/<=/184,getMoneda/eq/MXN"));
        mapValues.put("10458", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/185,getPlazoRC01/<=/366,getMoneda/eq/MXN"));
        mapValues.put("10460", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/367,getPlazoRC01/<=/731,getMoneda/eq/MXN"));
        mapValues.put("10462", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/732,getPlazoRC01/<=/1096,getMoneda/eq/MXN"));
        mapValues.put("10464", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/1097,getPlazoRC01/<=/1461,getMoneda/eq/MXN"));
        mapValues.put("10466", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/1462,getPlazoRC01/<=/1827,getMoneda/eq/MXN"));
        mapValues.put("10468", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/1828,getPlazoRC01/<=/2557,getMoneda/eq/MXN"));
        mapValues.put("10470", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/2558,getPlazoRC01/<=/3653,getMoneda/eq/MXN"));
        mapValues.put("10472", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/3654,getPlazoRC01/<=/5479,getMoneda/eq/MXN"));
        mapValues.put("10474", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/5480,getPlazoRC01/<=/7305,getMoneda/eq/MXN"));
        mapValues.put("10476", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/7306,getPlazoRC01/<=/100000,getMoneda/eq/MXN"));

        mapValues.put("10550", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/1,getPlazoRC01/<=/7,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10552", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/8,getPlazoRC01/<=/31,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10554", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/32,getPlazoRC01/<=/92,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10556", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/93,getPlazoRC01/<=/184,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10558", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/185,getPlazoRC01/<=/366,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10560", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/367,getPlazoRC01/<=/731,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10562", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/732,getPlazoRC01/<=/1096,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10564", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/1097,getPlazoRC01/<=/1461,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10566", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/1462,getPlazoRC01/<=/1827,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10568", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/1828,getPlazoRC01/<=/2557,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10570", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/2558,getPlazoRC01/<=/3653,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10572", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/3654,getPlazoRC01/<=/5479,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10574", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/5480,getPlazoRC01/<=/7305,getIdCuentaContable/eq/121100000000"));
        mapValues.put("10576", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC01/>=/7306,getPlazoRC01/<=/100000,getIdCuentaContable/eq/121100000000"));

        mapValues.put("20450", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1,getPlazoRC02/<=/7,getSobretasa/eq/SI"));
        mapValues.put("20452", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/8,getPlazoRC02/<=/31,getSobretasa/eq/SI"));
        mapValues.put("20454", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/32,getPlazoRC02/<=/92,getSobretasa/eq/SI"));
        mapValues.put("20456", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/93,getPlazoRC02/<=/184,getSobretasa/eq/SI"));
        mapValues.put("20458", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/185,getPlazoRC02/<=/366,getSobretasa/eq/SI"));
        mapValues.put("20460", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/367,getPlazoRC02/<=/731,getSobretasa/eq/SI"));
        mapValues.put("20462", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/732,getPlazoRC02/<=/1096,getSobretasa/eq/SI"));
        mapValues.put("20464", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1097,getPlazoRC02/<=/1461,getSobretasa/eq/SI"));
        mapValues.put("20466", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1462,getPlazoRC02/<=/1827,getSobretasa/eq/SI"));
        mapValues.put("20468", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1828,getPlazoRC02/<=/2557,getSobretasa/eq/SI"));
        mapValues.put("20470", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/2558,getPlazoRC02/<=/3653,getSobretasa/eq/SI"));
        mapValues.put("20472", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/3654,getPlazoRC02/<=/5479,getSobretasa/eq/SI"));
        mapValues.put("20474", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/5480,getPlazoRC02/<=/7305,getSobretasa/eq/SI"));
        mapValues.put("20476", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/7306,getPlazoRC02/<=/100000,getSobretasa/eq/SI"));

        
        mapValues.put("23700", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1,getPlazoRC02/<=/7,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23702", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/8,getPlazoRC02/<=/31,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23704", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/32,getPlazoRC02/<=/92,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23706", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/93,getPlazoRC02/<=/184,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23708", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/185,getPlazoRC02/<=/366,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23710", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/367,getPlazoRC02/<=/731,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23712", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/732,getPlazoRC02/<=/1096,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23714", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1097,getPlazoRC02/<=/1461,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23716", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1462,getPlazoRC02/<=/1827,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23718", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1828,getPlazoRC02/<=/2557,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23720", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/2558,getPlazoRC02/<=/3653,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23722", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/3654,getPlazoRC02/<=/5479,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23724", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/5480,getPlazoRC02/<=/7305,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));
        mapValues.put("23726", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/7306,getPlazoRC02/<=/100000,getSobretasa/eq/SI,getIdCuentaContable/eq/121100000000"));

        mapValues.put("30450", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1,getPlazoRC02/<=/7,getMoneda/eq/UDI"));
        mapValues.put("30452", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/8,getPlazoRC02/<=/31,getMoneda/eq/UDI"));
        mapValues.put("30454", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/32,getPlazoRC02/<=/92,getMoneda/eq/UDI"));
        mapValues.put("30456", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/93,getPlazoRC02/<=/184,getMoneda/eq/UDI"));
        mapValues.put("30458", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/185,getPlazoRC02/<=/366,getMoneda/eq/UDI"));
        mapValues.put("30460", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/367,getPlazoRC02/<=/731,getMoneda/eq/UDI"));
        mapValues.put("30462", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/732,getPlazoRC02/<=/1096,getMoneda/eq/UDI"));
        mapValues.put("30464", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1097,getPlazoRC02/<=/1461,getMoneda/eq/UDI"));
        mapValues.put("30466", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1462,getPlazoRC02/<=/1827,getMoneda/eq/UDI"));
        mapValues.put("30468", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/1828,getPlazoRC02/<=/2557,getMoneda/eq/UDI"));
        mapValues.put("30470", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/2558,getPlazoRC02/<=/3653,getMoneda/eq/UDI"));
        mapValues.put("30472", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/3654,getPlazoRC02/<=/5479,getMoneda/eq/UDI"));
        mapValues.put("30474", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/5480,getPlazoRC02/<=/7305,getMoneda/eq/UDI"));
        mapValues.put("30476", interpSumIf(tenenciaWrapper, "getMonto", "getPlazoRC02/>=/7306,getPlazoRC02/<=/100000,getMoneda/eq/UDI"));

        mapValues.put("40050", interpSumIf(disponibilidadWrapper, "getMonto", "getPlazo/>=/1,getPlazo/<=/7,getCuenta/eq/110202000000"));

        mapValues.put("77000", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/I"));
        mapValues.put("77002", mapValues.get("13700"));
        mapValues.put("77004", mapValues.get("77000"));
        mapValues.put("77012", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Largo,getPonderador/ed/0.0"));
        mapValues.put("77014", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Largo,getPonderador/ed/0.2"));
        mapValues.put("77016", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Largo,getPonderador/ed/0.5"));
        mapValues.put("77018", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Largo,getPonderador/ed/1"));
        mapValues.put("77020", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Largo,getPonderador/ed/1.2"));
        mapValues.put("77022", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Largo,getPonderador/ed/1.5"));
        mapValues.put("77026", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Corto,getPonderador/ed/0.0"));
        mapValues.put("77028", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Corto,getPonderador/ed/0.2"));
        mapValues.put("77030", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Corto,getPonderador/ed/0.5"));
        mapValues.put("77032", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Corto,getPonderador/ed/1"));
        mapValues.put("77034", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Corto,getPonderador/ed/1.2"));
        mapValues.put("77036", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/II,getPlazo/eq/Corto,getPonderador/ed/1.5"));

        mapValues.put("77104", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Largo,getPonderador/ed/0.2,getGradoRiesgo/==/1"));
        mapValues.put("77106", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Largo,getPonderador/ed/0.2,getGradoRiesgo/==/2"));
        mapValues.put("77108", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Largo,getPonderador/ed/0.2,getGradoRiesgo/==/3"));
        mapValues.put("77110", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Largo,getPonderador/ed/0.2,getGradoRiesgo/==/4"));
        mapValues.put("77112", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Largo,getPonderador/ed/0.2,getGradoRiesgo/==/5"));
        mapValues.put("77114", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Largo,getPonderador/ed/0.2,getGradoRiesgo/==/6"));
        mapValues.put("77130", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Corto,getPonderador/ed/0.2,getGradoRiesgo/==/1"));
        mapValues.put("77134", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Corto,getPonderador/ed/0.5,getGradoRiesgo/==/2"));
        mapValues.put("77138", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Corto,getPonderador/ed/1,getGradoRiesgo/==/3"));
        mapValues.put("77142", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Corto,getPonderador/ed/1.2,getGradoRiesgo/==/4"));
        mapValues.put("77146", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/III,getPlazo/eq/Corto,getPonderador/ed/1.5,getGradoRiesgo/==/5"));
        mapValues.put("77308", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/IV,getPonderador/ed/0"));
        mapValues.put("77270", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/IV,getPonderador/ed/0.2"));
        mapValues.put("77316", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/VII,getPlazo/eq/Largo,getPonderador/ed/0.2,getGradoRiesgo/==/2"));
        mapValues.put("77364", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/VII,getPlazo/eq/Largo,getPonderador/ed/0.5,getGradoRiesgo/==/3"));
        mapValues.put("77324", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/VII,getPlazo/eq/Largo,getPonderador/ed/1,getGradoRiesgo/==/4"));
        mapValues.put("77340", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/VII,getPlazo/eq/Corto,getPonderador/ed/0.2,getGradoRiesgo/==/1"));
        mapValues.put("77344", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/MXN,getGrupoRc07/eq/VII,getPlazo/eq/Corto,getPonderador/ed/0.5,getGradoRiesgo/==/2"));
        mapValues.put("77484", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/UDI,getGrupoRc07/eq/I"));
        mapValues.put("77754", interpSumIf(tenenciaWrapper, "getMonto", "getMoneda/eq/UDI,getGrupoRc07/eq/IV,getPonderador/ed/0.2"));
        

        
        mapValues.put("13050", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1,getPlazo/<=/7,getCuenta/eq/210102010000"));
        mapValues.put("13052", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/8,getPlazo/<=/31,getCuenta/eq/210102010000"));
        mapValues.put("13054", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/32,getPlazo/<=/92,getCuenta/eq/210102010000"));
        mapValues.put("13056", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/93,getPlazo/<=/184,getCuenta/eq/210102010000"));
        mapValues.put("13058", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/185,getPlazo/<=/366,getCuenta/eq/210102010000"));
        mapValues.put("13060", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/367,getPlazo/<=/731,getCuenta/eq/210102010000"));
        mapValues.put("13062", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/732,getPlazo/<=/1096,getCuenta/eq/210102010000"));
        mapValues.put("13064", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1097,getPlazo/<=/1461,getCuenta/eq/210102010000"));
        mapValues.put("13066", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1462,getPlazo/<=/1827,getCuenta/eq/210102010000"));
        mapValues.put("13068", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1828,getPlazo/<=/2557,getCuenta/eq/210102010000"));
        mapValues.put("13070", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/2558,getPlazo/<=/3653,getCuenta/eq/210102010000"));
        mapValues.put("13072", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/3654,getPlazo/<=/5479,getCuenta/eq/210102010000"));
        mapValues.put("13074", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/5480,getPlazo/<=/7305,getCuenta/eq/210102010000"));
        mapValues.put("13076", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/7306,getPlazo/<=/100000,getCuenta/eq/210102010000"));

        mapValues.put("13150", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1,getPlazo/<=/7,getCuenta/eq/210100000000"));
        mapValues.put("13152", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/8,getPlazo/<=/31,getCuenta/eq/210100000000"));
        mapValues.put("13154", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/32,getPlazo/<=/92,getCuenta/eq/210100000000"));
        mapValues.put("13156", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/93,getPlazo/<=/184,getCuenta/eq/210100000000"));
        mapValues.put("13158", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/185,getPlazo/<=/366,getCuenta/eq/210100000000"));
        mapValues.put("13160", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/367,getPlazo/<=/731,getCuenta/eq/210100000000"));
        mapValues.put("13162", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/732,getPlazo/<=/1096,getCuenta/eq/210100000000"));
        mapValues.put("13164", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1097,getPlazo/<=/1461,getCuenta/eq/210100000000"));
        mapValues.put("13166", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1462,getPlazo/<=/1827,getCuenta/eq/210100000000"));
        mapValues.put("13168", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1828,getPlazo/<=/2557,getCuenta/eq/210100000000"));
        mapValues.put("13170", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/2558,getPlazo/<=/3653,getCuenta/eq/210100000000"));
        mapValues.put("13172", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/3654,getPlazo/<=/5479,getCuenta/eq/210100000000"));
        mapValues.put("13174", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/5480,getPlazo/<=/7305,getCuenta/eq/210100000000"));
        mapValues.put("13176", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/7306,getPlazo/<=/100000,getCuenta/eq/210100000000"));

        mapValues.put("13250", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1,getPlazo/<=/7,getCuenta/eq/211100000000"));
        mapValues.put("13252", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/8,getPlazo/<=/31,getCuenta/eq/211100000000"));
        mapValues.put("13254", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/32,getPlazo/<=/92,getCuenta/eq/211100000000"));
        mapValues.put("13256", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/93,getPlazo/<=/184,getCuenta/eq/211100000000"));
        mapValues.put("13258", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/185,getPlazo/<=/366,getCuenta/eq/211100000000"));
        mapValues.put("13260", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/367,getPlazo/<=/731,getCuenta/eq/211100000000"));
        mapValues.put("13262", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/732,getPlazo/<=/1096,getCuenta/eq/211100000000"));
        mapValues.put("13264", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1097,getPlazo/<=/1461,getCuenta/eq/211100000000"));
        mapValues.put("13266", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1462,getPlazo/<=/1827,getCuenta/eq/211100000000"));
        mapValues.put("13268", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/1828,getPlazo/<=/2557,getCuenta/eq/211100000000"));
        mapValues.put("13270", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/2558,getPlazo/<=/3653,getCuenta/eq/211100000000"));
        mapValues.put("13272", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/3654,getPlazo/<=/5479,getCuenta/eq/211100000000"));
        mapValues.put("13274", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/5480,getPlazo/<=/7305,getCuenta/eq/211100000000"));
        mapValues.put("13276", interpSumIf(captacionWrapper, "getMonto", "getPlazo/>=/7306,getPlazo/<=/100000,getCuenta/eq/211100000000"));

        mapValues.put("13700", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/781503000000"));

        mapValues.put("80001", interpSumIf(disponibilidadWrapper, "getMonto", "getCuenta/eq/110100000000"));
        mapValues.put("80651", interpSumIf(disponibilidadWrapper, "getMonto", "getCuenta/eq/110403000000"));
        mapValues.put("83002", interpSumIf(disponibilidadWrapper, "getMonto", "getCuenta/eq/110404000000")
                + interpSumIf(disponibilidadWrapper, "getMonto", "getCuenta/eq/110200000000")
                + interpSumIf(disponibilidadWrapper, "getMonto", "getCuenta/eq/110202000000"));
        mapValues.put("86000", interpSumIf(tarjetaWrapper, "getSaldo", "getCuenta/eq/131101000000")
                - interpSumIf(tarjetaWrapper, "getSaldo", "getCuenta/eq/131101000000,getCreditoRelevante/eq/SI"));
        mapValues.put("86006", interpSumIf(tarjetaWrapper, "getSaldo", "getCuenta/eq/136101000000"));
        mapValues.put("86012", interpSumIf(prestamoWrapper, "getSaldo", "getCuenta/eq/131102000000")
                - interpSumIf(prestamoWrapper, "getSaldo", "getCuenta/eq/131102010000"));
        mapValues.put("86018", interpSumIf(prestamoWrapper, "getSaldo", "getCuenta/eq/136102000000"));
        mapValues.put("84303", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/140000000000")
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/140204000000"));
        mapValues.put("84311", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/140204000000"));
        mapValues.put("84803", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/160100000000")
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/160200000000")
                - interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/161100000000"));
        mapValues.put("84921", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/190102030000"));
        mapValues.put("84903", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/190100000000")
                - mapValues.get("84921"));

        mapValues.put("86003", interpSumIf(reservas, "getMonto", "getEstatusCrediticio/eq/AA")
                + interpSumIf(reservas, "getMonto", "getEstatusCrediticio/eq/BA"));

        mapValues.put("86009", interpSumIf(reservas, "getMonto", "getEstatusCrediticio/eq/BT"));

        mapValues.put("86017", 0.0);

        mapValues.put("89900", 2000000.0);
        mapValues.put("89503", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/179002010000"));
        mapValues.put("95005", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/410100000000")
                + interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/410300000000"));

        mapValues.put("95045", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/420100000000"));
        mapValues.put("95050", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/420300000000"));
        mapValues.put("95055", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/500000000000")
                - interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/600000000000"));
        mapValues.put("95055", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/500000000000")
                - interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/600000000000"));
        mapValues.put("95283", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/139100000000")
                - mapValues.get("95666"));
        mapValues.put("95282", mapValues.get("95283"));
        mapValues.put("95310", mapValues.get("84903"));
        mapValues.put("95335", mapValues.get("84903"));
        mapValues.put("95340", mapValues.get("84903"));
        mapValues.put("95380", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/180000000000"));
        mapValues.put("95355", mapValues.get("95380"));
        mapValues.put("95390", interpSumIf(catalogoMinimoWrapper, "getValor", "getCuenta/eq/280000000000"));
        mapValues.put("95385", mapValues.get("95390"));
        mapValues.put("90075", mapValues.get("95005") + mapValues.get("95045") + mapValues.get("95050") + mapValues.get("95055"));
        mapValues.put("90080", mapValues.get("95075"));
        mapValues.put("93500", mapValues.get("95380"));
        mapValues.put("93520", mapValues.get("95500"));
        mapValues.put("93500", mapValues.get("95380"));
        mapValues.put("93700", mapValues.get("95340"));
        mapValues.put("93750", mapValues.get("95340"));
        mapValues.put("93755", mapValues.get("95340"));
        mapValues.put("95610", mapValues.get("95560"));
        mapValues.put("95615", mapValues.get("95560"));
        mapValues.put("95635", mapValues.get("95645"));
        mapValues.put("95640", mapValues.get("95645"));
        mapValues.put("95650", mapValues.get("95645") + mapValues.get("95615"));
        mapValues.put("95655", mapValues.get("95650"));
        mapValues.put("95660", mapValues.get("95650"));

        mapValues.put("103110", interpSumIf(tenenciaWrapper, "getMonto", "getGrupoRC10/eq/SI,getPonderador/ed/0.2"));
        mapValues.put("103125", interpSumIf(tenenciaWrapper, "getMonto", "getGrupoRC10/eq/SI,getPonderador/ed/0.5"));
        mapValues.put("103130", interpSumIf(tenenciaWrapper, "getMonto", "getGrupoRC10/eq/SI,getPonderador/ed/1.0"));
        mapValues.put("103140", interpSumIf(tenenciaWrapper, "getMonto", "getGrupoRC10/eq/SI,getPonderador/ed/3.5"));
        mapValues.put("104070", interpSumIf(tenenciaWrapper, "getMonto", "getGrupoRC10/eq/SI,getPonderador/ed/0.4"));

    }

    public static void main(String[] args) {
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        Regcuenta get = createQuery.get(1);
        System.out.println(get.getIdRegCuenta());
        BaseModeloMathInterpreter base = new BaseModeloMathInterpreter(get);
        base.calculate();
    }

    /**
     * @return the catalogoMinimoWrapper
     */
    public List<CatalogoMinimoWrapper> getCatalogoMinimoWrapper() {
        return catalogoMinimoWrapper;
    }

    /**
     * @param catalogoMinimoWrapper the catalogoMinimoWrapper to set
     */
    public void setCatalogoMinimoWrapper(List<CatalogoMinimoWrapper> catalogoMinimoWrapper) {
        this.catalogoMinimoWrapper = catalogoMinimoWrapper;
    }

    /**
     * @return the captacionWrapper
     */
    public List<CaptacionWrapper> getCaptacionWrapper() {
        return captacionWrapper;
    }

    /**
     * @param captacionWrapper the captacionWrapper to set
     */
    public void setCaptacionWrapper(List<CaptacionWrapper> captacionWrapper) {
        this.captacionWrapper = captacionWrapper;
    }

    /**
     * @return the disponibilidadWrapper
     */
    public List<DisponibilidadWrapper> getDisponibilidadWrapper() {
        return disponibilidadWrapper;
    }

    /**
     * @param disponibilidadWrapper the disponibilidadWrapper to set
     */
    public void setDisponibilidadWrapper(List<DisponibilidadWrapper> disponibilidadWrapper) {
        this.disponibilidadWrapper = disponibilidadWrapper;
    }

    /**
     * @return the prestamoWrapper
     */
    public List<PrestamoWrapper> getPrestamoWrapper() {
        return prestamoWrapper;
    }

    /**
     * @param prestamoWrapper the prestamoWrapper to set
     */
    public void setPrestamoWrapper(List<PrestamoWrapper> prestamoWrapper) {
        this.prestamoWrapper = prestamoWrapper;
    }

    /**
     * @return the tarjetaWrapper
     */
    public List<TarjetaCreditoWrapper> getTarjetaWrapper() {
        return tarjetaWrapper;
    }

    /**
     * @param tarjetaWrapper the tarjetaWrapper to set
     */
    public void setTarjetaWrapper(List<TarjetaCreditoWrapper> tarjetaWrapper) {
        this.tarjetaWrapper = tarjetaWrapper;
    }

    /**
     * @return the regCuenta
     */
    public Regcuenta getRegCuenta() {
        return regCuenta;
    }

    /**
     * @param regCuenta the regCuenta to set
     */
    public void setRegCuenta(Regcuenta regCuenta) {
        this.regCuenta = regCuenta;
    }

}
