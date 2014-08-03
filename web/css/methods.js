function waitPage() {
    document.getElementById("divProcess").style.display = "block";
    document.getElementById("divForm").style.display = "none";
}

function stopWaiting() {
    document.getElementById("divProcess").style.display = "none";
    document.getElementById("divForm").style.display = "block";
}


function reportes() {
    var project = document.getElementsByName("selectProject")[0].value;
    var report = document.getElementsByName("selectReport")[0].value;
    window.open("http://54.191.48.171:8080/SICAP/vistareportes.htm?typ=0&pro=" + project + "&rep=" + report, "Reporte");
    return true;
}

function trackingLog() {
    var fecIni = document.getElementsByName("init")[0].value;
    var fecFin = document.getElementsByName("end")[0].value;
    window.open("http://54.191.48.171:8080/SICAP/vistareportes.htm?typ=1&ini=" + fecIni + "&end=" + fecFin, "Tracking Log");
    return true;
}


function createComparativo() {
    var pra = document.getElementsByName("hiddenPrimer")[0].value;
    var prb = document.getElementsByName("hiddenSegundo")[0].value;
    var cambio = document.getElementsByName("hiddenVar")[0].value;
    window.open("http://54.191.48.171:8080/SICAP/vistareportes.htm?typ=2&pra=" + pra + "&prb=" + prb + "&var=" + cambio + "&num=-1", "Comparativo");
    return true;
}

function comparativoCongruencia() {

    var pra = document.getElementsByName("pra")[0].value;
    var prb = document.getElementsByName("prb")[0].value;
    var cambio = document.getElementsByName("tolerancia")[0].value / 100;
    window.open("http://54.191.48.171:8080/SICAP/vistareportes.htm?typ=3&pra=" + pra + "&prb=" + prb + "&var=" + cambio + "&num=-1", "Comparativo");
    return true;
}


function createConsistencia() {
    var pra = document.getElementByName("select")[0].value;
    window.open("http://54.191.48.171:8080/SICAP/vistareportes.htm?typ=4&pra=" + pra, "Consistencia");
    return true;
}


function createTenencia() {
    var idRegCta = document.getElementsByName("pryName")[0].value;
    window.open("http://54.191.48.171:8080/SICAP/vistareportes.htm?typ=5&pro=" + idRegCta, "Reporte");
    return true;
}
