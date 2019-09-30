<%-- 
    Document   : men_prin
    Created on : 28/09/2019, 08:26:49 PM
    Author     : victor
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="utf-8">
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="estilos/styles.css">
    <title>Advanced Search Form</title>
</head>

<body>
    <div class="container" id="advanced-search-form">
        <h2>Filtrado Avanzado</h2>
       <form name="frm" action="SubirArchivo" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="first-name">Seleccione archivo</label>
                <input type="file" name="archivo" title="seleccionar fichero" class="form-control"  id="first-name" accept=".xls,.xlsx" />
            </div>
            <div class="clearfix"></div>
            <button type="submit" class="btn btn-info btn-lg btn-responsive" id="search"> <span class="glyphicon glyphicon-search"></span> Subir</button>
        </form>
    </div>
</body>

</html>

