package dao;
import conexion.CreateConection;
import modelo.ClienteModelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.sql.ResultSet;
/**
 *
 * @author Alan
 */
public class ClienteDao {
    CreateConection cc = new CreateConection();
    
    public boolean insertar(ClienteModelo c) {
    try{
        String sql = "INSERT INTO cliente(nombre, apellido, telefono, email, direccion) VALUES(?, ?, ?, ?, ?)";
        Connection conn = cc.getConection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, c.getNombre());
        ps.setString(2, c.getApellido());
        ps.setString(3, c.getTelefono());
        ps.setString(4, c.getEmail());
        ps.setString(5, c.getDireccion());
        ps.executeUpdate();
        conn.close();
        
        return true;
        
    }catch(Exception e){
        System.out.println("No se pudo insertar" + e.getMessage());
        return false;
    }
        
}
    
    public boolean modificar(ClienteModelo c){

        try{
            String sql = "UPDATE cliente SET nombre=?, apellido=?, telefono=?, email=?, direccion=? WHERE id=?";
            Connection conn = cc.getConection();
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDireccion());
            ps.setInt(6, c.getIdCliente());
            ps.executeUpdate();
            conn.close();
            return true;
           
        }catch(Exception e){
            System.out.println("No se pudo modificar" + e.getMessage());
            return false;
        }
    }
    
    public ArrayList<ClienteModelo> listarClientes(){
        ArrayList<ClienteModelo> lista = new ArrayList<>();
        try{
            String sql = "SELECT * FROM cliente WHERE estado=true ORDER BY id";
            Connection conn = cc.getConection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                ClienteModelo cm = new ClienteModelo();
                cm.setIdCliente(rs.getInt("id"));
                cm.setNombre(rs.getString("nombre"));
                cm.setApellido(rs.getString("apellido"));
                cm.setTelefono(rs.getString("telefono"));
                cm.setEmail(rs.getString("email"));
                cm.setDireccion(rs.getString("direccion"));
                
                lista.add(cm);              
            }
        }catch(Exception e){
            System.out.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean eliminar(int Id){
        try{
            String sql = "UPDATE cliente SET estado=false WHERE id=?";
            Connection conn = cc.getConection();
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1,Id);
            ps.executeUpdate();
            conn.close();
            return true;
        }catch(Exception e){
            System.out.println("No se pudo eliminar" + e.getMessage());
            return false;
        }
    }
    
    public ArrayList<ClienteModelo> buscarCliente(String dato){
        ArrayList<ClienteModelo> lista = new ArrayList<>();
        
        try{
          String sql = "SELECT * FROM cliente "
           + "WHERE estado=true AND "
           + "(nombre ILIKE ? OR apellido ILIKE ? OR email ILIKE ?)";
          Connection conn = cc.getConection();
            PreparedStatement ps = conn.prepareStatement(sql);
            
              ps.setString(1, "%" + dato + "%");
              ps.setString(2, "%" + dato + "%");
              ps.setString(3, "%" + dato + "%");
              
              ResultSet rs = ps.executeQuery();
              
              while(rs.next()){
                  
                  ClienteModelo cm = new ClienteModelo();
                  
                  cm.setIdCliente(rs.getInt("id"));
                  cm.setNombre(rs.getString("nombre"));
                  cm.setApellido(rs.getString("apellido"));
                  cm.setTelefono(rs.getString("telefono"));
                  cm.setEmail(rs.getString("email"));
                  cm.setDireccion(rs.getString("direccion"));
                  
                  lista.add(cm);               
              }
        }catch(Exception e){
            System.out.println("no se encontro el cliente" + e.getMessage());
        }
        return lista;
    }
}
