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
</head>
<body>
    <form>
        <div class="form-group">
            <label for="columnName">Columna a filtrar</label>
            <select class="form-control" id="columnName">
                <c:forEach items="${requestScope.columnasFiltrables}" var="column">
                    <option value="${column.idColumna}">${column.nombreColumna}</option> 
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="estadoColumna">Estado a filtrar</label>
            <select class="form-control" id="estadoColumna">
                <option>1</option>
                <option>2</option>
                <option>3</option>
                <option>4</option>
                <option>5</option>
            </select>
        </div>

        <div class="form-group">
            <label for="exampleFormControlSelect1">Seleccine tipo de grafica</label>
            <label class="checkbox-inline"><input type="checkbox" value="">Diagram de barras</label>
            <label class="checkbox-inline"><input type="checkbox" value="">Diagrama de tortas</label>
        </div>

        <button type="submit" class="btn btn-info btn-lg btn-responsive" id="search"> <span class="glyphicon glyphicon-search"></span> Añadir filtro</button>

    </form>



    <div class="container">
        <h2>Columnas seleccionadas</h2>
        <p>filtros seleccionados:</p>            
        <table class="table table-hover">
            <thead>
                <tr>
                    <th>Firstname</th>
                    <th>Lastname</th>
                    <th>Email</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>John</td>
                    <td>Doe</td>
                    <td>john@example.com</td>
                </tr>
                <tr>
                    <td>Mary</td>
                    <td>Moe</td>
                    <td>mary@example.com</td>
                </tr>
                <tr>
                    <td>July</td>
                    <td>Dooley</td>
                    <td>july@example.com</td>
                </tr>
            </tbody>
        </table>
    </div>
    <button  class="btn btn-info btn-lg btn-responsive" id="btnFiltrar" onclick="javascript: alerta();"> <span class="glyphicon glyphicon-search"></span> filtrar</button>
</body>


<script language="javascript">
    $(document).on("change", "#columnName", function () {
        var columnaSelected = $('#columnName').val();//When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
        $.get("ProcesarArchivo?idColumna=" + columnaSelected, function (responseJson) {                 //Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
            var select = $("#estadoColumna");                           //Locate HTML DOM element with ID "someselect".
            select.find("option").remove();
            alert("remueve");//Find all child elements with tag name "option" and remove them (just to prevent duplicate options when button is pressed again).
            $.each(responseJson, function (key, value) {               //Iterate over the JSON object.
                $("<option>").val(value).text(key).appendTo(select); //Create HTML <option> element, set its value with currently iterated key and its text content with currently iterated item and finally append it to the <select>.
            });
        });


     

    });
    
    function alerta(){
        alert("hola");
            //var orderId =  $("#orderId").val();
            alert("va filtrar");
            var selectEstado = document.getElementById("estadoColumna");
            
             var estadoFiltro= selectEstado.options[selectEstado.selectedIndex].text
             alert("filtro: " + estadoFiltro)
               var columnaSelected = $('#columnName').val();
             console.log(estadoFiltro);
            $.post("ProcesarArchivo", {filtro: estadoFiltro, idColumna : columnaSelected },
                    function (data) {
                        alert("Data Loaded: " + data);
                    });
    }
      
</script>
