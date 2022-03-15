package netty;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    public static void DB_connect(String[] args) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");

    }

    public static void DBcreate_connet(String user1,String user2)throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "insert IGNORE into connected(user1,user2) values('"+user1+"','"+user2+"')";
        String sql2 = "insert IGNORE into connected(user1,user2) values('"+user2+"','"+user1+"')";
        PreparedStatement statement = connection.prepareCall(sql);
        PreparedStatement statement2 = connection.prepareCall(sql2);
        try{
        statement.executeUpdate();
        statement2.executeUpdate();
        }catch(SQLException e){
            System.out.println(e);

        }
        
        statement.close();

        statement2.close();
    }


    public static Boolean DBtell_connect(String user1,String user2)throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "select id  from connected where user1="+user1+" and user2="+user2;
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);
        return rs.next();
    }
    public static void refresh(String user1,String user2)throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "UPDATE member_group.connected set update_time=now()  where user1="+user1+" and user2="+user2;
        String sql2 = "UPDATE member_group.connected set update_time=now()  where user1="+user2+" and user2="+user1;
        PreparedStatement statement = connection.prepareCall(sql);
        PreparedStatement statement2 = connection.prepareCall(sql2);
        try{
        statement.executeUpdate();
        statement2.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
        statement.close();

        statement2.close();
    }
    public static void main(String[] args) throws Exception{
      
        
        
    }


    public static void list_group()throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "select g2.name,g.user from member_group.Grouplist g,member_group.Group g2 where g.groupid=g2.Column_id order by g2.name,g.user asc";
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);

        System.out.println("|Groupid|User\t|");
        System.out.println("=====================");
        while (rs.next()){
   
            String groupid = rs.getString("name");
            String user = rs.getString("user");
            System.out.printf("|"+groupid+"\t|");
            

            System.out.print(user);
            System.out.println("\t|");
        }



    }



    public static void add_user(String member)throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        PreparedStatement statement=null;
        String sql = "insert into user(username) values('"+member+"')";
        
        statement = connection.prepareCall(sql);
        statement.executeUpdate();
        statement.close();



    }

    public static void DB_group_remove_member(String groupid,String member)throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        String[] token=member.split(",");
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        PreparedStatement statement=null;

        String sql2 = "select Column_id from member_group.Group where name='"+groupid+"'";
        Statement statement1=connection.createStatement();
        ResultSet rs=statement1.executeQuery(sql2);
        String groupname="";
        while(rs.next()){
            groupname = rs.getString("Column_id");
        }


        for(String num:token)
    {
        String sql = "DELETE  IGNORE from Grouplist where groupid='"+groupname+"'and user='"+num+"'";
        
        statement = connection.prepareCall(sql);
        
        try{
        statement.executeUpdate();
        
        }catch(Exception e){
            System.out.println(e);
        }


    }
        statement.close();
        System.out.println("finish");
    }


    public static void DB_drop_group(String key)throws Exception,SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        PreparedStatement statement=null;
        String sql2 = "select Column_id from member_group.Group where name='"+key+"'";
        Statement statement1=connection.createStatement();
        ResultSet rs=statement1.executeQuery(sql2);
        String groupname="";
        while(rs.next()){
            groupname = rs.getString("Column_id");
        }



        String sql = "DELETE  IGNORE from Grouplist where groupid='"+groupname+"'";
        statement = connection.prepareCall(sql);
        statement.executeUpdate();
        statement.close();
    }

    
    public static void DB_group_add_member(String groupid,String member)throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        String[] token=member.split(",");
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        PreparedStatement statement=null;
    



        for(String num:token)
    {
        String sql = "insert IGNORE into Grouplist(groupid,user) values("+groupid+","+num+")";
        
        statement = connection.prepareCall(sql);
        
        try{
        statement.executeUpdate();
        
        }catch(Exception e){
            System.out.println(e);
        }


    }
        statement.close();

    }


    public static void list_group_i(String i)throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "SELECT g.id ,g.groupid ,g.`user` ,g2.name  from member_group.Grouplist g ,member_group.`Group` g2 where g2.Column_id =g.groupid and g2.name='"+i+"' order by user asc";
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);

        System.out.println("|User\t|");
        System.out.println("=====================");
        while (rs.next()){
   
          
            String user = rs.getString("user");

            
            System.out.print("|");

            System.out.print(user);
            System.out.println("\t|");
        }



    }

    public static String get_member_id(String i)throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        //System.out.println("I="+i);
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "select ID from member_group.user where username='"+i+"'";
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);

        String user="";
        while (rs.next()){
   
          
            user = rs.getString("ID");

        }

        
        return user;
    }

    public static String get_member_name(String i)throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        //System.out.println("I="+i);
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "select username from member_group.user where ID="+i+"";
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);

        String user="";
        while (rs.next()){
   
          
            user = rs.getString("username");

        }

        
        return user;
    }


    public static ArrayList<String> group_get_member_id(String i)throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        //System.out.println("I="+i);
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "select g.`user` from Grouplist g,member_group.`Group` g2 where g2.Column_id =g.groupid and g2.name='"+i+"'";
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);

        ArrayList<String> list=new ArrayList<String> ();
        
        while (rs.next()){
   
            list.add(rs.getString("user"));
            

        }

        
        return list;
    }

    public static ArrayList<String> group_get()throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        //System.out.println("I="+i);
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "select groupid,user from Grouplist ";
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);

        ArrayList<String> list=new ArrayList<String> ();
        
        while (rs.next()){
            list.add(rs.getString("groupid"));
            list.add(rs.getString("user"));
            

        }

        
        return list;
    }





}
