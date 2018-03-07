/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.util.ArrayList;
import javax.json.JsonObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.ModeloDatos;
import modelo.Ruta;
import modelo.Usuario;
import modelo.Zona;
import org.json.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Rodrigo
 */
@WebServlet(name = "InsertarPuntosServlet", urlPatterns = {"/InsertarPuntosServlet"})
public class InsertarPuntosServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private ModeloDatos modelodatos = new ModeloDatos();

    public void init(ServletConfig cfg) throws ServletException {
        modelodatos = ModeloDatos.getInstancia();
        System.out.println("Abrir conexion");
        modelodatos.abrirConexion();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String usuario, puntos;

        usuario = request.getParameter("id_usuario");
        puntos = request.getParameter("waypoints");
        final JSONObject puntosJSON = new JSONObject(puntos);

        int id_ruta = modelodatos.insertarRuta(usuario, puntosJSON);

        //session.setAttribute("listaRutas", listaRutas);
        int peligrosidad = modelodatos.getPeligrosidad(id_ruta);
        String ruta_osrm = modelodatos.getRutaOSRM(id_ruta);
        ArrayList<String> rutas = new ArrayList<>();
        rutas.add(ruta_osrm);
        session.setAttribute("id_ruta", id_ruta);
        session.setAttribute("peligrosidad", peligrosidad);
        session.setAttribute("id_usuario", usuario);
        session.setAttribute("rutas", rutas);

        //response.sendRedirect("/ModelosBBDD/verRutasServlet.java");
        response.sendRedirect("/ModelosBBDD/menuUsuario.jsp");

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    public void destroy() {
        modelodatos.cerrarConexion();
        super.destroy();
    }
}
