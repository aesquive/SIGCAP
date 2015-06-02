

function checkMessages() {
    var valor = document.getElementById("message").textContent;
    if(valor!="" && valor!=null && valor!="$message"){
        alert("MENSAJE SIGCAP "+valor);
        
    }
    
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
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var project = document.getElementsByName("selectProject")[0].value;
    var report = document.getElementsByName("selectReport")[0].value;
    window.open(direccionReportes + "?typ=0&pro=" + project + "&rep=" + report, "Reporte");
    return true;
}

function trackingLog() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var fecIni = document.getElementsByName("init")[0].value;
    var fecFin = document.getElementsByName("end")[0].value;
    window.open(direccionReportes + "?typ=1&ini=" + fecIni + "&end=" + fecFin, "Tracking Log");
    return true;
}


function createComparativo() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var pra = document.getElementsByName("hiddenPrimer")[0].value;
    var prb = document.getElementsByName("hiddenSegundo")[0].value;
    var cambio = document.getElementsByName("hiddenVar")[0].value;
    window.open(direccionReportes + "?typ=2&pra=" + pra + "&prb=" + prb + "&var=" + cambio + "&num=-1", "Comparativo");
    return true;
}

function comparativoCongruencia() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var pra = document.getElementsByName("pra")[0].value;
    var prb = document.getElementsByName("prb")[0].value;
    var cambio = document.getElementsByName("tolerancia")[0].value / 100;
    window.open(direccionReportes + "?typ=3&pra=" + pra + "&prb=" + prb + "&var=" + cambio + "&num=-1", "Comparativo");
    return true;
}


function comparativoCons() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var pra = document.getElementsByName("pra")[0].value;
    window.open(direccionReportes + "?typ=4&pra=" + pra + "&num=-1", "Comparativo");
    return true;
}


function createTenencia() {
    var direccionReportes=document.getElementById("direccionReportes").textContent;
    var idRegCta = document.getElementsByName("pryName")[0].value;
    window.open(direccionReportes + "?typ=5&pro=" + idRegCta, "Reporte");
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
