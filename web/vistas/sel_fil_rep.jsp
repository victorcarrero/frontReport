<%-- 
    Document   : Sel_fil_rep
    Created on : 29/09/2019, 12:27:20 PM
    Author     : victor
--%>

<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% List lista = (List) request.getAttribute("columnasFiltrables");%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
    <script
        src="https://code.jquery.com/jquery-3.4.1.min.js"
        integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
    crossorigin="anonymous"></script>
    <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.11/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="estilos/jtable.min.css">
    <script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.11/js/jquery.dataTables.js"></script>
    <script type="text/javascript" charset="utf8" src="scripts/jquery-ui.min.js"></script>
    <script type="text/javascript" charset="utf8" src="scripts/jquery.jtable.min.js"></script>

</head>
<body>
    <form>
        <div class="form-group">
            <label for="columnName">Columna a filtrar</label>
            <select class="form-control" id="columnName" onchange="javascript:listarColumnas();">
                <c:forEach items="${requestScope.columnasFiltrables}" var="column">
                    <option value="${column.idColumna}">${column.nombreColumna}</option> 
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="estadoColumna">Estado a filtrar</label>
            <select class="form-control" id="estadoColumna">

            </select>
        </div>

        <div class="form-group">
            <label for="exampleFormControlSelect1">Seleccine tipo de grafica</label>
            <label class="checkbox-inline"><input type="checkbox" value="">Diagram de barras</label>
            <label class="checkbox-inline"><input type="checkbox" value="">Diagrama de tortas</label>
        </div>

        

    </form>
<button  class="btn btn-info btn-lg btn-responsive" id="btnAgregarFiltro"> <span class="glyphicon glyphicon-search"></span> Añadir filtro</button>



    <div  id="divFacturas"  >
        <hr>
        <div class="panel-heading">
            <div class="pull-left">                                                                   
                <h6 id="agregar-inf" class="txt-dark capitalize-font" onclick=""><i class="zmdi zmdi-account mr-10 icon-25"></i>Filtros seleccionados</h6>
            </div>
            <div class="clearfix"></div>
        </div>
        <table id="tablaFacturas" class="table table-bordered table-hover">
            <thead>
                <tr>
                    <th>Columna</th>
                    <th>Filtro</th>
                </tr>
            </thead>
            <tbody id="contenidoFacturas">
            </tbody>    
        </table>
    </div>

    <button  class="btn btn-info btn-lg btn-responsive" id="btnFiltrar" onclick="javascript: alerta();"> <span class="glyphicon glyphicon-search"></span> filtrar</button>
</body>


<script language="javascript">


    $('#tablaFacturas').dataTable();
    function listarColumnas() {
        var columnaSelected = $('#columnName').val();//When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
        $.get("ProcesarArchivo?idColumna=" + columnaSelected, function (responseJson) {                 //Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
            var select = $("#estadoColumna");                           //Locate HTML DOM element with ID "someselect".
            select.find("option").remove();
            $.each(responseJson, function (key, value) {               //Iterate over the JSON object.
                $("<option>").val(value).text(key).appendTo(select); //Create HTML <option> element, set its value with currently iterated key and its text content with currently iterated item and finally append it to the <select>.
            });
        });
    }


    $("#btnAgregarFiltro").click(function () {
        var selectColumna = document.getElementById("columnName");
        var nombreColumna = selectColumna.options[selectColumna.selectedIndex].text;
        var selectEstado = document.getElementById("estadoColumna");
        var estadoFiltro = selectEstado.options[selectEstado.selectedIndex].text
        var newRowContent = "<tr><td>"+ nombreColumna +"</td><td>"+estadoFiltro+"</td></tr>"
        $("#tablaFacturas tbody").append(newRowContent);
    });





    function alerta() {
        //var orderId =  $("#orderId").val();
        var selectEstado = document.getElementById("estadoColumna");

        var estadoFiltro = selectEstado.options[selectEstado.selectedIndex].text
        var columnaSelected = $('#columnName').val();
        console.log(estadoFiltro);
        $.post("ProcesarArchivo", {filtro: estadoFiltro, idColumna: columnaSelected},
                function (data) {
                    console.log("Data Loaded: " + data);
                });
    }


    $('#PersonTableContainer').jtable({
        title: 'Table of people',
        actions: {
            listAction: '/GettingStarted/PersonList',
            createAction: '/GettingStarted/CreatePerson',
            updateAction: '/GettingStarted/UpdatePerson',
            deleteAction: '/GettingStarted/DeletePerson'
        },
        fields: {
            PersonId: {
                key: true,
                list: false
            },
            Name: {
                title: 'Author Name',
                width: '40%'
            },
            Age: {
                title: 'Age',
                width: '20%'
            },
            RecordDate: {
                title: 'Record date',
                width: '30%',
                type: 'date',
                create: false,
                edit: false
            }
        }
    });

</script>
