<%-- 
    Document   : menuUsuario
    Created on : 07-feb-2018, 0:39:24
    Author     : Rodrigo
--%>

<%@page import="modelo.ModeloDatos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.Ruta"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    String poligonos = (String) session.getAttribute("poligonos");
    int i = 0;
    int j = 0;
%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


        <%--<link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.1/dist/leaflet.css" integrity="sha512-Rksm5RenBEKSKFjgI3a41vrjkw4EVPlJ3+OiI65vTjIdo9brlAacEuKOiQ5OFh7cOI1bkDwLqdLw3Zg0cRJAAQ==" crossorigin=""/>
        <script src="https://unpkg.com/leaflet@1.3.1/dist/leaflet.js" integrity="sha512-/Nsx9X4HebavoBvEBuyp3I7od5tA0UzAxs+j83KgC8PU0kgB4XiK4Lfe4y4cgBtaRJQEIFCW+oC506aPT2L1zw==" crossorigin=""></script>
        --%>
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.1/dist/leaflet.css" />
        <link REL=StyleSheet HREF="estilo.css" TYPE="text/css" MEDIA=screen>
        <link rel="stylesheet" href="https://unpkg.com/leaflet-routing-machine@latest/dist/leaflet-routing-machine.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet.draw/0.4.2/leaflet.draw.css"/>  
        <script src="https://unpkg.com/leaflet@1.3.1/dist/leaflet.js"></script>
        <script src="https://unpkg.com/leaflet-routing-machine@latest/dist/leaflet-routing-machine.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet.draw/0.4.2/leaflet.draw.js"></script>



        <title>Menú Usuario</title>
    </head>
    <body>
        <body>
        
        <header>
            <div class="logo">			 	
                <a href="index.html">Portal de Geolocalizacion</a>                                
            </div>

            <nav>
                <ul>
                    <li><a href="menuUsuario.jsp">Mapa</a></li>                                
                    <li><a href="nuevaRutaUsuario.jsp">Nueva ruta</a>
                    </li>
                    <li><a href="nuevaZonaUsuario.jsp">Nueva zona</a>
                    </li>
                    <li><a href="EstadisticasUsuario.jsp">Estadísticas</a>
                    </li>
                    <li><a href="index.html">Desconectar</a></li>
                </ul>
            </nav>

        </header>

         <div class="mapa">
            <div id="mapid" style="width: 600px; height: 400px;"></div>
        </div>
        
        <footer>
            <section class="links">                                
                <a href="index.html">Desconectar</a>
            </section>

            <section class="autor">
                <p>@Practica 1 de Modelos Avanzados de Bases de Datos</p>
            </section>
        </footer>
        <script>

            var mymap = L.map('mapid').setView([40.513229, -3.349886], 15);

            var mapa = L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
                maxZoom: 18,
                attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
                        '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                        'Imagery © <a href="http://mapbox.com">Mapbox</a>',
                id: 'mapbox.streets'
            }).addTo(mymap);

            var editableLayers = new L.FeatureGroup();
            mymap.addLayer(editableLayers);

            L.drawLocal.draw.toolbar.buttons.polygon = 'Dibuja un polígono';
            L.drawLocal.draw.toolbar.actions.title = 'Para cancelar el dibujado';
            L.drawLocal.draw.toolbar.actions.text = 'Cancelar';
            L.drawLocal.draw.toolbar.finish.title = 'Para finalizar el dibujado';
            L.drawLocal.draw.toolbar.finish.text = 'Acabar';
            L.drawLocal.draw.toolbar.undo.title = 'Para eliminar el último punto dibujado';
            L.drawLocal.draw.toolbar.undo.text = 'Eliminar último';
            L.drawLocal.draw.handlers.polygon.tooltip.start = 'Selecciona para iniciar el poligono';
            L.drawLocal.draw.handlers.polygon.tooltip.cont = 'Selecciona para continuar con el poligono';
            L.drawLocal.draw.handlers.polygon.tooltip.end = 'Selecciona para continuar el poligono';
            L.drawLocal.edit.toolbar.buttons.edit = 'Editar puntos';
            L.drawLocal.edit.toolbar.buttons.editDisabled = 'No hay puntos para editar';
            L.drawLocal.edit.toolbar.actions.save.title = 'Guardar cambios';
            L.drawLocal.edit.toolbar.actions.save.text = 'Guardar';
            L.drawLocal.edit.toolbar.actions.cancel.title = 'Cancelar cambios';
            L.drawLocal.edit.toolbar.actions.cancel.text = 'Cancelar';
            L.drawLocal.edit.handlers.edit.tooltip.text = 'Mueve los marcadores para editar la figura';
            L.drawLocal.edit.handlers.edit.tooltip.subtext = 'Para deshacer pincha en cancelar';


            var drawPluginOptions = {
                position: 'topright',
                draw: {
                    polygon: {
                        allowIntersection: true, // Restricts shapes to simple polygons

                        drawError: {
                            color: '#e1e100', // Color the shape will turn when intersects
                            message: '<strong>Oh snap!<strong> you can\'t draw that!' // Message that will show when intersect
                        },
                        shapeOptions: {
                            color: '#97009c'
                        }
                    },
                    // disable toolbar item by setting it to false
                    polyline: false,
                    circle: false,
                    rectangle: false,
                    marker: false
                },
                edit: {
                    featureGroup: editableLayers, //REQUIRED!!
                    remove: false
                }
            };
            
            function post(path, params, method) {
                method = method || "post"; // Set method to post by default if not specified.

                // The rest of this code assumes you are not using a library.
                // It can be made less wordy if you use one.
                var form = document.createElement("form");
                form.setAttribute("method", method);
                form.setAttribute("action", path);

                for (var key in params) {
                    if (params.hasOwnProperty(key)) {
                        var hiddenField = document.createElement("input");
                        hiddenField.setAttribute("type", "hidden");
                        hiddenField.setAttribute("name", key);
                        hiddenField.setAttribute("value", params[key]);

                        form.appendChild(hiddenField);
                    }
                }

                document.body.appendChild(form);
                form.submit();
            }

            // Initialise the draw control and pass it the FeatureGroup of editable layers
            var drawControl = new L.Control.Draw(drawPluginOptions);
            mymap.addControl(drawControl);
            
            function createButton(label, container) {
                var btn = L.DomUtil.create('button', '', container);
                btn.setAttribute('type', 'button');
                btn.innerHTML = label;
                return btn;
            }
            
            function createInput(label, container, hidden = false, valueHidden = null) {
                var inp = L.DomUtil.create('input', 'form-control input-sm', container);
                if(hidden){
                    inp.setAttribute('type', 'hidden');
                    inp.setAttribute('value', valueHidden);
                }else {
                inp.setAttribute('type', 'input');
            }
                inp.setAttribute('id',label)
                inp.innerHTML = label;
                return inp;
            }
            function isNumber(n) {
            return !isNaN(parseFloat(n)) && isFinite(n);
            }
            
            mymap.on('draw:created', function (e) {
                var type = e.layerType,
                        layer = e.layer;

                if (type === 'polygon') {
                    
                    var container = L.DomUtil.create('div'),
                        peligrosidad = createInput('peligrosidad', container),
                        newZona = createButton('Insertar Peligrosidad', container),
                        jsonZona = createInput('inputOculto',container, true, JSON.stringify(layer.toGeoJSON()));
                        
                        var pop = L.popup()
                            .setContent(container)
                            .setLatLng(e.latlng);
                    
                            layer.bindPopup(pop);
                            
                    
                    
                    var points = layer._latlngs;
                    var geojson = layer.toGeoJSON();
                    
                    L.DomEvent.on(newZona, 'click', function () {
                
                    var json = document.getElementById("inputOculto").value;
                    var peligrosidad = document.getElementById("peligrosidad").value;
                    if(!isNumber(peligrosidad) || (peligrosidad>100 || peligrosidad<0)){
                      alert("Debe introducir un número entre 0 y 100")  
                    }
                    else{
                    var parametros = {peligrosidad: peligrosidad, jsonZona: json}
                    post('/ModelosBBDD/InsertarZonaServlet',parametros)
                    mymap.closePopup();
                    }
                });

                }
                editableLayers.addLayer(layer);
            });
            
            
            
            
            
            
//            mymap.on('draw:created', function (e) {
//                var type = e.layerType,
//                        layer = e.layer;
//
//                if (type === 'polygon') {
//                    
//                    /*
//                    var container = L.DomUtil.create('div'),
//                        newPunto = createButton('Nuevo punto', container);
//                        var pop = L.popup()
//                        .setContent(container)
//                        .setLatLng(e.latlng)
//                        .openOn(mymap);*/
//                        
//                    var points = layer._latlngs;
//
//                    // here you can get it in geojson format
//                    var geojson = layer.toGeoJSON();
//
//                    document.getElementById("demo").innerHTML = JSON.stringify(geojson);
//
//                }
//                editableLayers.addLayer(layer);
//            });
            
            /*
mymap.on('draw:created', function (e) {
    var type = e.layerType,
        layer = e.layer;

    if (type === 'polygon') {
        // structure the geojson object
        var geojson = {};
        geojson['type'] = 'Feature';
        geojson['geometry'] = {};
        geojson['geometry']['type'] = "Polygon";

        // export the coordinates from the layer
        coordinates = [];
        latlngs = layer.getLatLngs();
        for (var i = 0; i < latlngs.length; i++) {
            coordinates.push([latlngs[i].lng, latlngs[i].lat])
        }

        // push the coordinates to the json geometry
        geojson['geometry']['coordinates'] = [coordinates];

        // Finally, show the poly as a geojson object in the console
        console.log(JSON.stringify(geojson));

    }

    drawnItems.addLayer(layer);
});
*/



            var poligonos = <%=poligonos%>
            //Dibuja los poligonos en el mapa
            L.geoJSON(poligonos, {
                style: function (feature) {
                    if (feature.properties.peligrosidad < 34) {
                        return {color: "#00ff00"};
                    } else //peligrosidad baja
                    if (feature.properties.peligrosidad < 67) {
                        return {color: "#FFA500"};
                    } else //peligrosidad media
                    {
                        return {color: "#ff0000"};
                    }
                    ; //peligrosidad alta
                },
                onEachFeature: onEachFeature
            }).addTo(mymap);

            function onEachFeature(feature, layer) {
                if (feature.properties && feature.properties.peligrosidad) {
                    var popupContent = "Nivel de peligrosidad: " + feature.properties.peligrosidad;
                    layer.bindPopup(popupContent);
                }
            }
        </script>
    </body>
</html>
</html>
