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

/**
 *
 * @author Rodrigo
 */
@WebServlet(name = "VerRutasServlet", urlPatterns = {"/VerRutasServlet"})
public class VerRutasServlet extends HttpServlet {

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
        int id_ruta;
        int pagina;
        int cantRutas = 10;
        String id_usuario;
        ArrayList<String> rutas = new ArrayList<>();

        id_ruta = Integer.parseInt(request.getParameter("id_ruta"));
        pagina = Integer.parseInt(request.getParameter("pagina"));
        id_usuario = request.getParameter("id_usuario");

        if (pagina < 0) {
            pagina = 0;
        }
        int offset = cantRutas * pagina;

        ArrayList<Integer> id_rutas = modelodatos.getIdRutas(cantRutas, offset, id_usuario);

        if (id_rutas.size() != 0 && id_ruta < 0) {
            id_ruta = id_rutas.get(0);
        }

        String ruta_osrm = modelodatos.getRutaOSRM(id_ruta);
        rutas.add(ruta_osrm);
        int peligrosidad = modelodatos.getPeligrosidad(id_ruta);

        if (id_rutas.size() != 0) {
            session.setAttribute("id_ruta", id_ruta);
            session.setAttribute("id_rutas", id_rutas);
            session.setAttribute("pagina", pagina);
            session.setAttribute("rutas", rutas);
            session.setAttribute("peligrosidad", peligrosidad);
            session.setAttribute("id_usuario", id_usuario);
        }

        if (!id_usuario.equals("admin")) {
            response.sendRedirect("/ModelosBBDD/menuUsuario.jsp");
        } else {
            response.sendRedirect("/ModelosBBDD/menuAdmin.jsp");
        }

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
