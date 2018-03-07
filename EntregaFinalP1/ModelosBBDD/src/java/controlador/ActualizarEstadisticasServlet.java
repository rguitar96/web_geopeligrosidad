/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.ModeloDatos;
import modelo.Ruta;

/**
 *
 * @author Ricardo
 */
@WebServlet(name = "ActualizarEstadisticasServlet", urlPatterns = {"/ActualizarEstadisticasServlet"})
public class ActualizarEstadisticasServlet extends HttpServlet {

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
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        String usuario;
        
        usuario = request.getParameter("combobox");
        ArrayList<Ruta> rutas_obj_todas = modelodatos.obtenerRutas();
                
                for (Ruta r : rutas_obj_todas) {
                    r.setDistancia(modelodatos.getDistancia(r.getId_ruta()));
                    r.setPeligrosidad(modelodatos.getPeligrosidad(r.getId_ruta()));
                }
        
                session.setAttribute("rutas_obj_todas", rutas_obj_todas);
        session.setAttribute("usuarioEstadisticas", usuario);
        response.sendRedirect("/ModelosBBDD/Estadisticas.jsp");
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

}
