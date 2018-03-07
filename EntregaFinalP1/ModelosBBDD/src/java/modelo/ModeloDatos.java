/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.json.*;

/**
 *
 * @author Rodrigo
 */
public class ModeloDatos {

    private static ModeloDatos instancia = new ModeloDatos(); // Inicialización inline de la unica instacia.
    private Connection conexion;
    private Statement set;
    private Statement setAux;
    private ResultSet resultSet;
    private ResultSet resultSetAux;
    private String url;

    private String direccion = "localhost";
    private String puerto = "5432";
    private String bbdd = "practicaSIG";

    //Nombres de los campos que se utilizarán en las consultas SQL
    private String tablaPuntos = "puntos";
    private String tablaPoligonos = "poligonos";
    private String tablaRutas = "rutas";
    private String idPunto = "id_punto";
    private String idRuta = "idRuta";
    private String punto = "punto";
    private String idPoligono = "idPoligono";
    private String zona = "poligono";
    private String peligrosidad = "peligrosidad";

    public ModeloDatos() {
    }

    public static ModeloDatos getInstancia() {
        return instancia;
    }

    public void abrirConexion() {
        try {
            url = "jdbc:postgresql://localhost:5432/practicaSIG";
            url = "jdbc:postgresql://" + direccion + ":" + puerto + "/" + bbdd;
            Class.forName("org.postgresql.Driver");

            conexion = DriverManager.getConnection(url, "postgres", "postgres");

            System.out.println("Se ha conectado con la BD.");
        } catch (Exception ex) {
            System.out.println("No se ha conectado con la BD.");
        }
    }

    public void cerrarConexion() {
        try {
            conexion.close();
        } catch (Exception ex) {
        }
    }

    public boolean registrarUsuario(Usuario u) {
        boolean existe = false;
        String usuario = u.getId_usuario();
        String contraseña = u.getContra();

        try {

            set = conexion.createStatement();
            String consulta = "SELECT id_usuario FROM \"personas\" WHERE id_usuario='" + usuario + "'";
            resultSet = set.executeQuery(consulta);
            int cont;
            if (resultSet.next()) {
                System.out.println("El usuario ya existe.");
            } else {
                
               
                cont=(int)obtenerTotal()+1;
                String query = "INSERT INTO personas( idpersona, id_usuario, \"contraseña\") "
                               + "VALUES ("+ cont + ", '" + usuario +"', '" + contraseña +"');";
                set.executeUpdate(query);
                existe = true;
            }

            resultSet.close();
            set.close();
            return existe;
        } catch (Exception ex) {
            System.out.println("Excepción: no se ha insertado en la tabla el personas.");
        }
        return existe;
    }

    public boolean loguearUsuario(String usuario, String contra) {
        boolean correcto = false;

        try {
            set = conexion.createStatement();
            resultSet = set.executeQuery("SELECT id_usuario FROM personas WHERE id_usuario='" + usuario + "'");
            if (resultSet.next()) {
                resultSet = set.executeQuery("SELECT contraseña FROM personas WHERE contraseña='" + contra + "'");

                if (resultSet.next()) {
                    correcto = true;
                } else {
                    System.out.println("Contraseña incorrecta.");
                }

            } else {
                System.out.println("Usuario incorrecto.");
            }

            resultSet.close();
            set.close();
            return correcto;
        } catch (Exception ex) {
            System.out.println("Excepción: no se ha logueado el usuario.");
        }
        return correcto;
    }
    
    public long obtenerTotal(){
        long total=0;
        try {
            set = conexion.createStatement();

            resultSet = set.executeQuery("SELECT count(id_usuario) FROM personas");
                if(resultSet.next()){
                total = resultSet.getLong("count");
                }
            return total;
        } catch (Exception ex) {
            System.out.println("No se ha encontrado el numero total" );
            return 0;
        }
        
    }

    
    public Usuario obtenerUsuario(String usuario) {

        String usuariobd;
        Usuario u = new Usuario();

        try {
            set = conexion.createStatement();

            resultSet = set.executeQuery("SELECT * FROM \"personas\"");
            while (resultSet.next()) {
                usuariobd = resultSet.getString("id_usuario");
                usuariobd = usuariobd.trim();
                if (usuariobd.equals(usuario.trim())) {

                    u = new Usuario(usuariobd, resultSet.getString("contra"));
                }
            }
        } catch (Exception ex) {
            System.out.println("No se ha encontrado el usuario: " + usuario);
        }
        return u;
    }
    
    public ArrayList<String> obtenerUsuarios() {
        ArrayList<String> usuarios = new ArrayList<>();
        String usuario = "";

        try {
            set = conexion.createStatement();

            resultSet = set.executeQuery("SELECT * FROM \"personas\"");
            while (resultSet.next()) {
                usuario = resultSet.getString("id_usuario");
                usuario = usuario.trim();
                usuarios.add(usuario);
            }
        } catch (Exception ex) {
            System.out.println("Error en obtenerUsuarios(). ");
        }
        return usuarios;
    }

    public ArrayList<Zona> obtenerZonas() {
        //arraylist de rutas, con un array de puntos para cada una de ellas
        ArrayList<Zona> zonas = new ArrayList();

        try {
            set = conexion.createStatement();

            resultSet = set.executeQuery("SELECT * FROM poligonos"); //UNA RUTA TIENE COMO MÍNIMO UN PUNTO

            
            while (resultSet.next()) {
                
                    int peligrosidad = 0;
                    int id_zona = 0;
                    String poligono = "";


                    peligrosidad = resultSet.getInt("peligrosidad");
                    id_zona = resultSet.getInt("idpoligono");
                    poligono = resultSet.getString("poligono");
                    
                    zonas.add(new Zona(id_zona, poligono, peligrosidad));
            }
        } catch (Exception ex) {
            System.out.println("Excepción al obtener lista de rutas.");
        }

        return zonas;
    }

  

    public ArrayList getIdRutas(int limit, int offset, String id_usuario) {
        ArrayList<Integer> result = new ArrayList<>();
        String condicion = "";
        if (!id_usuario.equals("admin")) {
            condicion = "where id_usuario= '" + id_usuario + "'";
        }
        String query = "select idRuta from rutas " + condicion + " limit " + limit + " offset " + offset;
        try {
            set = conexion.createStatement();
            resultSet = set.executeQuery(query);
            while (resultSet.next()) {
                result.add(resultSet.getInt(1));
            }
        } catch (Exception e) {
            System.out.println("Error en getIdRutas");
        }
        return result;
    }

    public String getRuta(int intRuta) {
        String result = "";
        intRuta = 2;
        try {
            set = conexion.createStatement();
            resultSet = set.executeQuery("SELECT row_to_json(fc)\n"
                    + "FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features\n"
                    + "   FROM (SELECT 'Feature' As type\n"
                    + "    , ST_AsGeoJSON(punto, 4)::json As geometry\n"
                    + "    , row_to_json((SELECT l FROM (SELECT " + idPunto + ", " + idRuta + ") As l\n"
                    + "      )) As properties\n"
                    + "   FROM " + tablaPuntos + " As lg ) As f ) As fc;");

            while (resultSet.next()) {
                result = resultSet.getString(1);
            }
        } catch (Exception ex) {
            System.out.println("Error en getRuta()");
        }
        return result;
    }

    public String getRutaOSRM(int intRuta) {
        String result = "";
        try {
            set = conexion.createStatement();
            resultSet = set.executeQuery("select replace(array_to_json(array_agg(concat('L.latLng(',st_y(" + punto + "::geometry),',', st_x(" + punto + "::geometry),')')))::text,'\"','') as waypoints from " + tablaPuntos + " where " + idRuta + " =  " + intRuta);

            while (resultSet.next()) {
                result = resultSet.getString(1);
            }
        } catch (Exception ex) {
            return "";
        }
        return result;
    }

    public String getPoligonos() {
        String result = "";
        try {
            set = conexion.createStatement();
            resultSet = set.executeQuery("SELECT row_to_json(fc)\n"
                    + "FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features\n"
                    + "   FROM (SELECT 'Feature' As type\n"
                    + "    , ST_AsGeoJSON(poligono)::json As geometry\n"
                    + "    , row_to_json((SELECT l FROM (SELECT " + idPoligono + ", " + peligrosidad + ") As l\n"
                    + "      )) As properties\n"
                    + "   FROM " + tablaPoligonos + " As lg ) As f ) As fc;");

            while (resultSet.next()) {
                result = resultSet.getString(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error en getPoligonos()");
        }

        return result;
    }

    //Devuelve las lineas de la ruta osrm
    public JSONObject getLinesOSRM(int numRuta) {
        JSONObject result = null;
        String puntos = "";
        String query = "select st_x(" + punto + "::geometry), st_y(" + punto + "::geometry) from puntos where " + idRuta + " = " + numRuta;
        try {
            set = conexion.createStatement();
            resultSet = set.executeQuery(query);
            while (resultSet.next()) {
                puntos += resultSet.getString(1) + "," + resultSet.getString(2) + ";";
            }

                puntos = puntos.substring(0, puntos.length() - 1);

            
            String url = "http://router.project-osrm.org/route/v1/driving/" + puntos + "?overview=full&geometries=geojson";
            //-3.36304999999999987,40.4813890000000001;-3.36403199999999991,40.4812179999999984;-3.36414500000000016,40.4806790000000021;-3.36503500000000022,40.480316000000002;-3.36622099999999991,40.479965;-3.36711600000000022,40.4796789999999973;-3.36795299999999997,40.4797730000000016
            //http://router.project-osrm.org/route/v1/driving/-3.36304999999999987,40.4813890000000001;-3.36403199999999991,40.4812179999999984;-3.36414500000000016,40.4806790000000021;-3.36503500000000022,40.480316000000002;-3.36622099999999991,40.479965;-3.36711600000000022,40.4796789999999973;-3.36795299999999997,40.4797730000000016?overview=false
            URL osrm = new URL(url);
            URLConnection yc = osrm.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                final JSONObject rutaJSON = new JSONObject(inputLine);
                //final JSONObject matchings = rutaJSON.getJSONObject("routes");

                result = rutaJSON;
                //System.out.println(geometry.toString());
            }

            in.close();
            return result;
        } catch (Exception e) {
            System.out.println("Error en getLinesOSRM()");
            return null;
        }
    }

    //Obtiene la peligrosidad de la totalidad de la ruta indicada
    //Con velocidad constante
    public int getPeligrosidad(int intRuta) {
        int result = 0;
        int longitud;
        int longitudAcumulada = 0;
        int peligro = 0;
        String poligono;
        String lineString; //Guarda en forma de array todos los puntos de la ruta

        JSONObject geometria = getLinesOSRM(intRuta);
        if (geometria == null) {
            return result;
        }
        final JSONArray routes = geometria.getJSONArray("routes");
        final JSONObject objArray = routes.getJSONObject(0);
        final JSONObject geometry = objArray.getJSONObject("geometry");
        //final JSONObject distanciaJSON = objArray.getJSONObject("distance");
        int distancia = objArray.getInt("distance");
        
        try {
            set = conexion.createStatement();
            setAux = conexion.createStatement();

            //nº de poligonos
            resultSet = set.executeQuery("Select count(" + idPoligono + ") from " + tablaPoligonos + "");
            resultSet.next();
            String geoString = geometry.toString();
            String query = "SELECT ST_AsText(ST_GeomFromGeoJSON('" + geometry.toString() + "')) As wkt;";
            //"LINESTRING(-3.36305 40.481389,-3.364032 40.481218,-3.364145 40.480679,-3.365035 40.480316,-3.366221 40.479965,-3.367116 40.479679,-3.367953 40.479773,1 2,-3.34795475006104 40.5187908038825,-3.35113048553467 40.5142559226724,-3.35756778717041 40.51223307098 (...)"
            resultSet = set.executeQuery(query);
            //resultSet = set.executeQuery("SELECT ST_AsText(ST_MakeLine(ARRAY(SELECT ST_Centroid(" + punto + "::geometry) FROM " + tablaPuntos + ")))");
            resultSet.next();
            //array de todos los puntos
            lineString = resultSet.getString(1);

            resultSet = set.executeQuery("select st_astext(" + zona + ")," + peligrosidad + " from " + tablaPoligonos);
            /*Por cada poligono comprobar si la linea está en su interior*/
            while (resultSet.next()) {
                poligono = resultSet.getString(1);
                peligro = resultSet.getInt(2);
                resultSetAux = setAux.executeQuery("SELECT st_length(ST_Intersection('" + lineString + "'::geometry, '" + poligono + "')::geography);");
                resultSetAux.next();
                longitud = resultSetAux.getInt(1);
                longitudAcumulada += longitud;
                result = result + longitud * peligro;
            }
            result = result / distancia;
            return result;

        } catch (SQLException ex) {
            System.out.println("Error en getPeligrosidad()");
            return result;
        }

        
    }

    public ArrayList<Ruta> obtenerRutas() {
        //arraylist de rutas, con un array de puntos para cada una de ellas
        ArrayList<Ruta> rutas = new ArrayList();

        try {
            set = conexion.createStatement();

            resultSet = set.executeQuery("SELECT *, ST_X(puntos.punto::geometry), ST_Y(puntos.punto::geometry) FROM rutas NATURAL JOIN puntos"); //UNA RUTA TIENE COMO MÍNIMO UN PUNTO

            int id_ruta = -1;
            String id_usuario;
            int id_punto;
            double latitud;
            double longitud;
            ArrayList<Punto> puntos = new ArrayList();

            while (resultSet.next()) {
                if (resultSet.getInt("idruta") == id_ruta) {
                    //AÑADIR PUNTO
                    id_punto = resultSet.getInt("idpunto");
                    latitud = resultSet.getDouble("st_x");
                    longitud = resultSet.getDouble("st_y");
                    puntos.add(new Punto(id_punto, id_ruta, latitud, longitud));
                    rutas.get(rutas.size() - 1).setPuntos(puntos);
                } else {
                    id_ruta = -1;
                    id_usuario = "";
                    id_punto = -1;
                    latitud = -1;
                    longitud = -1;
                    puntos = new ArrayList();

                    Ruta r;

                    id_ruta = resultSet.getInt("idruta");
                    id_usuario = resultSet.getString("id_usuario").trim();
                    //PRIMER PUNTO
                    id_punto = resultSet.getInt("idpunto");
                    latitud = resultSet.getDouble("st_x");
                    longitud = resultSet.getDouble("st_y");
                    puntos.add(new Punto(id_punto, id_ruta, latitud, longitud));

                    r = new Ruta(id_ruta, id_usuario, puntos);
                    rutas.add(r);
                }
            }
        } catch (Exception ex) {
            System.out.println("Excepción al obtener lista de rutas.");
        }

        return rutas;
    }

    public int insertarRuta(String id_user, JSONObject puntos) {
        int result = -1;
        String query;
        int id_ruta;
        try {
            set = conexion.createStatement();

            /*
            ## Hace falta cambiar el atributo de id_ruta a serial para que se autoincremente ##
            ## Hay que hacer lo mismo con el campo id_punto de la tabla puntos ##
            CREATE SEQUENCE id_ruta_seq            
            ALTER TABLE rutas ALTER COLUMN id_ruta SET DEFAULT nextval('id_ruta_seq');
            ALTER TABLE rutas ALTER COLUMN id_ruta SET NOT NULL;
            ALTER SEQUENCE id_ruta_seq OWNED BY rutas.id_ruta; 
            SELECT MAX(id_ruta) FROM rutas;
            SELECT setval('id_ruta_seq', x);  -- sustituir x por SELECT MAX result
            
            CREATE SEQUENCE id_punto_seq            
            ALTER TABLE puntos ALTER COLUMN id_ruta SET DEFAULT nextval('id_punto_seq');
            ALTER TABLE puntos ALTER COLUMN id_ruta SET NOT NULL;
            ALTER SEQUENCE id_punto_seq OWNED BY puntos.id_punto; 
            SELECT MAX(id_punto) FROM rutas;
            SELECT setval('id_punto_seq', x);  -- sustituir x por SELECT MAX result
             */
            query = "insert into " + tablaRutas + " (id_usuario) values ('" + id_user + "')";
            set.executeUpdate(query);
            query = "select max(" + idRuta + ") from " + tablaRutas + ";";
            resultSet = set.executeQuery(query);
            resultSet.next();

            id_ruta = resultSet.getInt(1);
            final JSONArray arrayPuntos = puntos.getJSONArray("puntos");
            final int n = arrayPuntos.length();
            for (int i = 0; i < n; ++i) {
                final JSONObject objArray = arrayPuntos.getJSONObject(i);
                final JSONObject latLng = objArray.getJSONObject("latLng");
                double lat = latLng.getDouble("lat");
                double lng = latLng.getDouble("lng");
                query = "insert into " + tablaPuntos + " (" + idRuta + ", " + punto + ") values (" + id_ruta + ",ST_GeographyFromText('SRID=4326;point( " + lng + " " + lat + ")'))";
                set.executeUpdate(query);
                result = id_ruta;
            }

        } catch (Exception e) {
            System.out.println("Error en insertarRuta()");
        }
        return result;
    }

    public String insertarZona(int peligrosidad, String stringJSON) {
        String result = "";
        String query;
        try {
            set = conexion.createStatement();

            /*
            CREATE SEQUENCE id_zona_seq            
            ALTER TABLE poligonos ALTER COLUMN id_poligono SET DEFAULT nextval('id_zona_seq');
            ALTER TABLE poligonos ALTER COLUMN id_poligono SET NOT NULL;
            ALTER SEQUENCE id_ruta_seq OWNED BY poligonos.id_poligono; 
            SELECT MAX(id_poligono) FROM poligonos;
            SELECT setval('id_zona_seq', x);  -- sustituir x por SELECT MAX result

             */
            query = "insert into " + tablaPoligonos + " (peligrosidad, " + zona + ") values (" + peligrosidad + ",ST_GeomFromGeoJSON('" + stringJSON + "'))";
            set.executeUpdate(query);
            result = "Zona insertada";

        } catch (Exception e) {
            System.out.println("Error en insertarZona()");
            result = "Error al insertar la zona";
            return result;
        }
        return result;
    }
    public ArrayList<Ruta> obtenerRutas(String usuario) {
        //arraylist de rutas, con un array de puntos para cada una de ellas
        ArrayList<Ruta> rutas = new ArrayList();

        try {
            set = conexion.createStatement();

            resultSet = set.executeQuery("SELECT *, ST_X(puntos.punto::geometry), ST_Y(puntos.punto::geometry) FROM rutas NATURAL JOIN puntos WHERE id_usuario='"+usuario+"'"); //UNA RUTA TIENE COMO MÍNIMO UN PUNTO

            
            
            
            int id_ruta = -1;
            String id_usuario;
            int id_punto;
            double latitud;
            double longitud;
            ArrayList<Punto> puntos = new ArrayList();

            while (resultSet.next()) {
                if (resultSet.getInt("idruta") == id_ruta) {
                    //AÑADIR PUNTO
                    id_punto = resultSet.getInt("idpunto");
                    latitud = resultSet.getDouble("st_x");
                    longitud = resultSet.getDouble("st_y");
                    puntos.add(new Punto(id_punto, id_ruta, latitud, longitud));
                    rutas.get(rutas.size() - 1).setPuntos(puntos);
                } else {
                    id_ruta = -1;
                    id_usuario = "";
                    id_punto = -1;
                    latitud = -1;
                    longitud = -1;
                    puntos = new ArrayList();

                    Ruta r;

                    id_ruta = resultSet.getInt("idruta");
                    
                    id_usuario = resultSet.getString("id_usuario").trim();
                    //PRIMER PUNTO
                    id_punto = resultSet.getInt("idpunto");
                    latitud = resultSet.getDouble("st_x");
                    longitud = resultSet.getDouble("st_y");
                    puntos.add(new Punto(id_punto, id_ruta, latitud, longitud));
                    
                    r = new Ruta(id_ruta, id_usuario, puntos);
                    rutas.add(r);
                }
            }
        } catch (Exception ex) {
            System.out.println("Excepción al obtener lista de rutas.");
        }

        return rutas;
    }
    
    public int getDistancia(int id_ruta) {
        JSONObject geometria = getLinesOSRM(id_ruta);
        
                    final JSONArray routes = geometria.getJSONArray("routes");
                    final JSONObject objArray = routes.getJSONObject(0);
                    final JSONObject geometry = objArray.getJSONObject("geometry");
                    //final JSONObject distanciaJSON = objArray.getJSONObject("distance");
                    int distancia = objArray.getInt("distance");
        return distancia;
    }
    
}
