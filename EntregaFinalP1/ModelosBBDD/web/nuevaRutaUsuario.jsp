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
    String id_nombre = (String) session.getAttribute("id_nombre");
    String mensaje = (String) session.getAttribute("mensaje");
    ArrayList<String> usuarios = (ArrayList<String>) session.getAttribute("usuarios");

       
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
        <script type="text/javascript" src="Leaflet.Icon.Glyph.js"></script>


        <title>Menú Usuario</title>
    </head>

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
                //onEachFeature: onEachFeature
            }).addTo(mymap);

            function onEachFeature(feature, layer) {
                if (feature.properties && feature.properties.peligrosidad) {
                    var popupContent = "Nivel de peligrosidad: " + feature.properties.peligrosidad;
                    layer.bindPopup(popupContent);
                }
            }
            
                        var nodos = 0;
            var color;
            var r = Math.floor(Math.random() * 255);
            var g = Math.floor(Math.random() * 255);
            var b = Math.floor(Math.random() * 255);
            color = "rgb(" + r + " ," + g + "," + b + ")";
            var routing = L.Routing.control({
                waypoints: [
                    //                
                    //                L.latLng(40.507258,-3.354736),
                    //                L.latLng(40.508041,-3.347698),
                    //                L.latLng(40.508825,-3.342376)
                    null
                ],
                show: false,
                lineOptions: {
                    styles: [{color: color, opacity: 1, weight: 2}]
                }
            }).addTo(mymap);


            function createButton(label, container) {
                var btn = L.DomUtil.create('button', '', container);
                btn.setAttribute('type', 'button');
                btn.innerHTML = label;
                return btn;
            }



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

            //var arrayPuntos = new Array();

            mymap.on('click', function (e) {
                var container = L.DomUtil.create('div'),
                        newPunto = createButton('Nuevo punto', container);
                quitarUltimo = createButton('Eliminar último', container);

                if (nodos >= 2)
                    crearRuta = createButton("Crear ruta", container)

                var pop = L.popup()
                        .setContent(container)
                        .setLatLng(e.latlng)
                        .openOn(mymap);

                L.DomEvent.on(newPunto, 'click', function () {
                    if (nodos === 0) {
                        routing.spliceWaypoints(0, 1, e.latlng);
                        nodos++;
                        /*var jsonLatLng = new Object()
                         jsonLatLng.lat = e.latLng.lat
                         jsonLatLng.lng = e.latLng.lng
                         arrayPuntos.push(jsonLatLng)*/
                    } else if (nodos === 1) {

                        routing.spliceWaypoints(routing.getWaypoints().length - 1, 1, e.latlng);
                        nodos++;
                        /*var jsonLatLng = new Object();
                         jsonLatLng.lat = e.latLng.lat;
                         jsonLatLng.lng = e.latLng.lng;
                         arrayPuntos.push(jsonLatLng)*/
                    } else {
                        routing.spliceWaypoints(routing.getWaypoints().length, 1, e.latlng);
                        nodos++;
                        //arrayPuntos.pop()
                    }
                    mymap.closePopup();
                });

                L.DomEvent.on(quitarUltimo, 'click', function () {
                    routing.spliceWaypoints(routing.getWaypoints().length - 1, 1);
                    mymap.closePopup();
                });

                L.DomEvent.on(crearRuta, 'click', function () {
                    mymap.closePopup();
                    var obj = routing.getWaypoints();
                    var json = {puntos: obj}
                    post("/ModelosBBDD/InsertarPuntosServlet", {id_usuario: localStorage.id_usuario, waypoints: JSON.stringify(json)});//,{waypoints: routing.getWaypoints()})

                })
            });

        </script>

    </body>
</html>