
package controlador;

import dao.ClienteDao;
import modelo.ClienteModelo;
import java.util.ArrayList;

/**
 *
 * @author Alan
 */
public class ClienteController {
   ClienteDao dao = new ClienteDao();
   
   public boolean insertar(ClienteModelo c){
      return dao.insertar(c);
   }
   
   public boolean modificar(ClienteModelo c){
       return dao.modificar(c);
   }
   
   public boolean eliminar(int Id){
       return dao.eliminar(Id);
   }
   public ArrayList<ClienteModelo> buscarCliente(String dato){
       return dao.buscarCliente(dato);
   }
}
