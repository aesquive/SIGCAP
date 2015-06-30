
function alertIntel(mensaje){
    var anterior=sessionStorage.getItem("message");
    if(anterior==mensaje){
        return;
    }
    sessionStorage.setItem("message",mensaje);
    alert(mensaje);
}

function waitPage() {
    document.getElementById("divProcess").style.display = "block";
    document.getElementById("divDerecha").style.display = "none";
}

function stopWaiting() {
    document.getElementById("divProcess").style.display = "none";
    document.getElementById("divDerecha").style.display = "block";
}


function waitPageLogin() {
    document.getElementById("divProcess").style.display = "block";
    document.getElementById("divForm").style.display = "none";
}

function stopWaitingLogin() {
    document.getElementById("divProcess").style.display = "none";
    document.getElementById("divForm").style.display = "block";
}

function reportes() {
    var acumulado="";
    for(var t=0;t<20;t++){
        var element=document.getElementById("check_box_"+t);
        if(element!=null && element.checked==true){
            acumulado=acumulado+t+",";
        }
    }
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var project = document.getElementsByName("selectProject")[0].value;
    window.open(direccionReportes + "?typ=0&pro=" + project + "&rep=" + acumulado, "Reporte"+acumulado,"resizable=1,width=150,height=150");
    return true;
}


function trackingLog() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var fecIni = document.getElementsByName("init")[0].value;
    var fecFin = document.getElementsByName("end")[0].value;
    window.open(direccionReportes + "?typ=1&ini=" + fecIni + "&end=" + fecFin, "Tracking Log","resizable=1,width=150,height=150");
    return true;
}


function createComparativo() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var pra = document.getElementsByName("hiddenPrimer")[0].value;
    var prb = document.getElementsByName("hiddenSegundo")[0].value;
    var cambio = document.getElementsByName("hiddenVar")[0].value;
    window.open(direccionReportes + "?typ=2&pra=" + pra + "&prb=" + prb + "&var=" + cambio + "&num=-1", "Comparativo","resizable=1,width=150,height=150");
    return true;
}

function comparativoCongruencia() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var pra = document.getElementsByName("pra")[0].value;
    var prb = document.getElementsByName("prb")[0].value;
    var cambio = document.getElementsByName("tolerancia")[0].value / 100;
    window.open(direccionReportes + "?typ=3&pra=" + pra + "&prb=" + prb + "&var=" + cambio + "&num=-1", "Congruencia","resizable=1,width=150,height=150");
    return true;
}


function comparativoCons() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var pra = document.getElementsByName("pra")[0].value;
    window.open(direccionReportes + "?typ=4&pra=" + pra + "&num=-1", "Consistencia","resizable=1,width=150,height=150");
    return true;
}





function createTenencia() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var idRegCta = document.getElementsByName("pryName")[0].value;
    window.open(direccionReportes + "?typ=5&pro=" + idRegCta, "Reporte","resizable=1,width=150,height=150");
    return true;
}

function reporteBase(){
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    window.open(direccionReportes+"?typ=6","Base","resizable=1,width=150,height=150");
    return true;
}

function reporteIntegridad() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var pra = document.getElementsByName("pra")[0].value;
    window.open(direccionReportes + "?typ=7&pra=" + pra + "&num=-1", "Integridad","resizable=1,width=150,height=150");
    return true;
}

function reporteBanxico(){
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var pra = document.getElementsByName("pra")[0].value;
    window.open(direccionReportes + "?typ=8&pra=" + pra + "&num=-1", "Integridad","resizable=1,width=150,height=150");
    return true;
}


function mostrarSubmenu(numSubmenu) {
    var idsSubsShow = numSubmenu;
    var split = idsSubsShow.split("|");
    quitarSubMenus();
    for (var t = 0; t < split.length; t++) {
        var element = document.getElementById("sub" + split[t]);
        element.style.display = "block";
        element.parentNode.parentNode.style.display = "block";
    }
}


function quitarSubMenus() {
    for (var t = 1; t < 20; t++) {
        var element = document.getElementById("sub" + t);
        if (element != null) {
            element.style.display = "none";
            element.parentNode.parentNode.style.display = "none";
        }
    }
}



function checkMessages() {
    var valor = document.getElementById("message").textContent;
    if(valor!="" && valor!=null && valor!="$message"){
        alertIntel("MENSAJE SIGCAP "+valor);
        
    }
}