<%-- 
    Document   : Estadisticas
    Created on : 27-feb-2018, 17:46:45
    Author     : Enrique
--%>

<%@page import="modelo.Zona"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="modelo.ModeloDatos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.Ruta"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ArrayList<Ruta> rutas_obj = (ArrayList<Ruta>) session.getAttribute("rutas_obj");
    ArrayList<Zona> zonas_obj = (ArrayList<Zona>) session.getAttribute("zonas_obj");
%>
<html>
    <head>
        <title>Portal de Geolocalizacion</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link REL=StyleSheet HREF="estilo.css" TYPE="text/css" MEDIA=screen>
    </head>

    
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
        <br>
    <br>
    <br>
    <br>
    <br>
    <h2>Estadísticas sobre rutas:</h2>
    <table style="width:100%">
        <tr>
            <th>Número de rutas</th>
            <th>Metros recorridos</th> 
            <th>Metros por ruta</th>
        </tr>
        <tr>
            <td align="center"><%=rutas_obj.size() %></td>
            <td align="center"><%double total = 0;
                                for (int i=0;i<rutas_obj.size();i++) {%>
                                    <%total+=rutas_obj.get(i).getDistancia();%>
                                <%}%>
                                <%=total%></td> 
            <td align="center"> <%=new DecimalFormat("#.##").format(total/rutas_obj.size())%></td>
        </tr>
    </table>
    <br>
    <h2>Estadísticas sobre peligrosidad:</h2>
    <table style="width:100%">
        <tr>
            <th>Peligrosidad acumulada</th>
            <th>Peligrosidad por metro</th> 
            <th>Peligrosidad media de rutas</th>
        </tr>
        <tr>
            <td align="center"><%double peligrosidad_total = 0;
                                for (int i=0;i<rutas_obj.size();i++) {%>
                                    <%peligrosidad_total+=rutas_obj.get(i).getPeligrosidad();%>
                                <%}%>
                                <%=peligrosidad_total%></td>
            <td align="center"><%=new DecimalFormat("#.##").format(peligrosidad_total/total)%></td> 
            <td align="center"><%=new DecimalFormat("#.##").format(peligrosidad_total/rutas_obj.size())%></td>
        </tr>
    </table>
    <br>
    <h2>Estadísticas sobre zonas:</h2>
    <table style="width:100%">
        <tr>
            <th>Zonas totales</th>
            <th>Zonas poco peligrosas</th> 
            <th>Zonas peligrosas</th>
            <th>Zonas muy peligrosas</th>
            <th>Media de zonas</th>
        </tr>
        <tr>
            <td align="center"><%=zonas_obj.size()%></td>
            <td align="center"><%int menores = 0;
                                int medianos = 0;
                                int mayores = 0;
                                int peligrosidad_tot = 0;
                                for (int i=0;i<zonas_obj.size();i++) {
                    if (zonas_obj.get(i).getPeligrosidad()<34) {menores+=1;}
                    else if (zonas_obj.get(i).getPeligrosidad()<67) {medianos+=1;}
                    else {mayores+=1;}
                    peligrosidad_tot += zonas_obj.get(i).getPeligrosidad();
                                }%>
            <%=menores%></td> 
            <td align="center"><%=medianos%></td>
            <td align="center"><%=mayores%></td>
            <td align="center"><%=peligrosidad_tot/zonas_obj.size()%></td>
        </tr>
    </table>


<br>
<br>
<br>

    <footer>
        <section class="links">                                
            <a href="index.html">Desconectar</a>
        </section>

        <section class="autor">
            <p>@Practica 1 de Modelos Avanzados de Bases de Datos</p>
        </section>
    </footer>

</html>
