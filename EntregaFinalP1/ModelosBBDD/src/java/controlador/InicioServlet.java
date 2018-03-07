/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
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
import modelo.Usuario;
import modelo.Zona;

/**
 *
 * @author Rodrigo
 */
@WebServlet(name = "InicioServlet", urlPatterns = {"/InicioServlet"})
public class InicioServlet extends HttpServlet {

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
        String usuario, pass;

        usuario = request.getParameter("txtUsuario");
        pass = request.getParameter("txtPass");

        if (modelodatos.loguearUsuario(usuario, pass)) {
            //Usuario u = modelodatos.obtenerUsuario(usuario);
            Usuario u = new Usuario(usuario, "asd");
            session.setAttribute("usuario", u);

            ArrayList<Zona> listaZonas = modelodatos.obtenerZonas();
            session.setAttribute("listaZonas", listaZonas);

            String poligonos = modelodatos.getPoligonos();

            ArrayList<Ruta> rutas_obj = modelodatos.obtenerRutas(usuario);
            
            for (int i=0;i<rutas_obj.size();i++) {
                rutas_obj.get(i).setDistancia(modelodatos.getDistancia(rutas_obj.get(i).getId_ruta()));
                rutas_obj.get(i).setPeligrosidad(modelodatos.getPeligrosidad(rutas_obj.get(i).getId_ruta()));
            }
            
            
            
            ArrayList<Zona> zonas_obj = modelodatos.obtenerZonas();
            
            session.setAttribute("rutas_obj", rutas_obj);
            session.setAttribute("zonas_obj", zonas_obj);
            
            //nuevo
            ArrayList<String> rutas = new ArrayList<>();
            int cantRutas = 10;
            int peligrosidad = 0;
            int id_ruta = -1;
            ArrayList<Integer> id_rutas = modelodatos.getIdRutas(cantRutas, 0, usuario);

            //for (int i = 0; i < 1; i++) {
            if(!id_rutas.isEmpty()){
            String ruta_osrm = modelodatos.getRutaOSRM(id_rutas.get(0));
            rutas.add(ruta_osrm);
            peligrosidad = modelodatos.getPeligrosidad(id_rutas.get(0));
            id_ruta = id_rutas.get(0);
            }
            //}

            if (u.getId_usuario().equals("admin")) {
                session.setAttribute("usuarioEstadisticas", "admin");
                ArrayList<Ruta> rutas_obj_todas = modelodatos.obtenerRutas();
                
                for (Ruta r : rutas_obj_todas) {
                    r.setDistancia(modelodatos.getDistancia(r.getId_ruta()));
                    r.setPeligrosidad(modelodatos.getPeligrosidad(r.getId_ruta()));
                }
                
                ArrayList<String> usuarios = modelodatos.obtenerUsuarios();
                
                session.setAttribute("usuarios", usuarios);
                
                session.setAttribute("admin", "admin");
                session.setAttribute("rutas_obj_todas", rutas_obj_todas);

                //session.setAttribute("listaRutas", listaRutas);
                session.setAttribute("rutas", rutas);
                session.setAttribute("poligonos", poligonos);
                session.setAttribute("peligrosidad", peligrosidad);
                session.setAttribute("id_rutas", id_rutas);
                //int id_ruta = id_rutas.get(0);
                session.setAttribute("id_ruta", id_ruta);
                session.setAttribute("pagina", 0);

                session.setAttribute("id_usuario", usuario);

                response.sendRedirect("/ModelosBBDD/menuAdmin.jsp");
            } else {
                session.setAttribute("rutas", rutas);
                session.setAttribute("poligonos", poligonos);
                session.setAttribute("peligrosidad", peligrosidad);
                session.setAttribute("id_rutas", id_rutas);                
                session.setAttribute("id_ruta", id_ruta);
                session.setAttribute("pagina", 0);

                session.setAttribute("poligonos", poligonos);
                session.setAttribute("id_usuario", usuario);
                response.sendRedirect("/ModelosBBDD/menuUsuario.jsp");
            }
        } else {
            response.getOutputStream().println("<script>"
                    + "alert('Usuario y/o contraseña incorrectos'); "
                    + "window.location.href = 'InicioSesion.html'"
                    + "</script>");
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
