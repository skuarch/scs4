package net;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * wrapper for ConnectPool
 * @author skuarch
 */
public class ConnectAction extends ConnectPool {    
    
    private static DataSource dataSource = null;
    
    static{
        
        InitialContext initialContext = null;
        
        try {
            
            initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("jdbc/ssn_4");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    } // end static
    
    //==========================================================================
    /**
     * create a instance, 
     * for get the <code>DataSource</code> is using <code>ControllerMain</code>
     */
    public ConnectAction(){
        super(dataSource);
    } // end  ConnectAction    
    
}  // end class