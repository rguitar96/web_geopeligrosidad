<%-- 
    Document   : menuUsuario
    Created on : 07-feb-2018, 0:39:24
    Author     : Rodrigo
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="modelo.Ruta"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    ArrayList<String> rutas = (ArrayList<String>) session.getAttribute("rutas");
    String poligonos = (String) session.getAttribute("poligonos");
    String id_usuario = (String) session.getAttribute("id_usuario");
    ArrayList<Integer> id_rutas = (ArrayList<Integer>) session.getAttribute("id_rutas");
    int id_ruta = (Integer) session.getAttribute("id_ruta");
    int pagina = (Integer) session.getAttribute("pagina");
    int peligrosidad = (Integer) session.getAttribute("peligrosidad");
    int i = 0;
    int j = 0;
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


        <%--<link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.1/dist/leaflet.css" integrity="sha512-Rksm5RenBEKSKFjgI3a41vrjkw4EVPlJ3+OiI65vTjIdo9brlAacEuKOiQ5OFh7cOI1bkDwLqdLw3Zg0cRJAAQ==" crossorigin=""/>
        <script src="https://unpkg.com/leaflet@1.3.1/dist/leaflet.js" integrity="sha512-/Nsx9X4HebavoBvEBuyp3I7od5tA0UzAxs+j83KgC8PU0kgB4XiK4Lfe4y4cgBtaRJQEIFCW+oC506aPT2L1zw==" crossorigin=""></script>
        --%>
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.2.0/dist/leaflet.css" />
        <link REL=StyleSheet HREF="estilo.css" TYPE="text/css" MEDIA=screen>
        <link rel="stylesheet" href="https://unpkg.com/leaflet-routing-machine@latest/dist/leaflet-routing-machine.css" />
        <script src="https://unpkg.com/leaflet@1.2.0/dist/leaflet.js"></script>
        <script src="https://unpkg.com/leaflet-routing-machine@latest/dist/leaflet-routing-machine.js"></script>

        <script type="text/javascript" src="Leaflet.Icon.Glyph.js"></script>

        <title>Menú Admin</title>
    </head>
    <body>
        <header>
            <div class="logo">				
                <a href="index.html">Portal de Geolocalizacion</a>                                
            </div>

            <nav>
                <ul>
                    <li><a href="menuAdmin.jsp">Rutas y zonas</a></li>                                
                    <li><a href="Estadisticas.jsp">Estadísticas</a></li> 
                    <li><a href="index.html">Desconectar</a></li>
                </ul>
            </nav>

        </header>

        <div class="mapa">
            <div id="mapid" style="width: 700px; height: 500px;margin-right: 30px;"></div>
            <div style="padding: 100">
                <h2>Ruta <%=id_ruta%></h2>
                <ul>
                    <%for (int k = 0; k < id_rutas.size(); k++) {%>
                    <li><a href="#" onclick="post('/ModelosBBDD/VerRutasServlet', {id_ruta: <%=id_rutas.get(k)%>, pagina: <%=pagina%>, id_usuario: localStorage.id_usuario})">Ruta <%=id_rutas.get(k)%></a></li>
                        <%}%>
                </ul>
                <a  href="#" onclick="post('/ModelosBBDD/VerRutasServlet', {id_ruta: -1, pagina: <%=pagina - 1%>, id_usuario: localStorage.id_usuario})"> << </a> 
                | 
                <a href="#" onclick="post('/ModelosBBDD/VerRutasServlet', {id_ruta: -1, pagina: <%=pagina + 1%>, id_usuario: localStorage.id_usuario})"> >> </a>
                <%{%> <p>Peligrosidad total: <%=peligrosidad%></p> <%}%>
            </div>

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

            localStorage.id_usuario = "<%=id_usuario%>"

            var mymap = L.map('mapid').setView([40.513229, -3.349886], 13);

            L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
                maxZoom: 18,
                attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
                        '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                        'Imagery © <a href="http://mapbox.com">Mapbox</a>',
                id: 'mapbox.streets'
            }).addTo(mymap);


            function onEachFeature(feature, layer) {
                if (feature.properties && feature.properties.peligrosidad) {
                    var popupContent = "Nivel de peligrosidad: " + feature.properties.peligrosidad;
                    layer.bindPopup(popupContent);
                }
            }

            function createButton(label, container) {
                var btn = L.DomUtil.create('button', '', container);
                btn.setAttribute('type', 'button');
                btn.innerHTML = label;
                return btn;
            }

            function createInput(label, container) {
                var inp = L.DomUtil.create('input', 'form-control input-sm', container);
                inp.setAttribute('type', 'input');
                inp.innerHTML = label;
                return inp;
            }
            var container = L.DomUtil.create('div'),
                    peligrosidad = createInput('peligrosidad', container),
                    botonPunto = createButton('Selecciona punto', container);
            var vecesPulsado = 0;

            var color;
            //Dibuja la ruta a partir de los puntos
            <%while (rutas.size() > i) {%>
            var r = Math.floor(Math.random() * 255);
            var g = Math.floor(Math.random() * 255);
            var b = Math.floor(Math.random() * 255);
            color = "rgb(" + r + " ," + g + "," + b + ")";
            var routing = L.Routing.control({
                "waypoints": <%=rutas.get(i)%>,
                createMarker: function (i, wp) {
                    return L.marker(wp.latLng, {
                        icon: L.icon.glyph({
                            prefix: '',
                            glyph: String.fromCharCode(65 + i)
                        }),
                        draggable: true
                    }).bindPopup(container);
                },
                show: false,
                addWaypoints: false,
                draggableWaypoints: false,

                lineOptions: {
                    styles: [{color: color, opacity: 1, weight: 2}]
                }
            }).addTo(mymap);
            <% i++;
                }%>

            /*L.DomEvent.on(botonPunto, 'click', function () {
             vecesPulsado += 1;
             
             mymap.closePopup();
             });*/



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



            var popup = L.popup();

            function onMapClick(e) {
                popup
                        .setLatLng(e.latlng)
                        .setContent("Has pinchado el mapa en " + e.latlng.toString())
                        .openOn(mymap);
            }

            mymap.on('click', onMapClick);

        </script>
    </body>
</html>