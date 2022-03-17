package netty; 
import java.sql.SQLException;
import java.util.Timer; 
import java.util.TimerTask; 
public class TimerTest extends TimerTask { 

public TimerTest() throws ClassNotFoundException, SQLException { 
super(); 

} 
@Override 
public void run() { 

    try {
        Database.refresh_connect_list();
    } catch (ClassNotFoundException | SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }; 
} 
} 