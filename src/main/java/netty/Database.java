package netty;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.tree.ExpandVetoException;

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


    public static void list_member()throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "select g2.name,u.username  from Grouplist g,`Group` g2,`user` u where g.groupid =g2.Column_id  and u.ID=g.`user` ";
       // System.out.println(sql);
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);
        System.out.println("member list");
        System.out.println("|Groupname|User\t|");
        System.out.println("=====================");
        while (rs.next()){
   
            String groupid = rs.getString("name");
            String user = rs.getString("username");
            
            String str="";
            if(groupid.length()<7){
                str="|"+groupid+"\t\t";
            }else{
                str= "|"+groupid+"\t";
            }

            System.out.printf(str);

            if(user.length()<7){
                str="|"+user+"\t\t";
            }else{
                str= "|"+user+"\t";
            }
            System.out.print(str);



            System.out.println("|");
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
    {   String sql="DELETE member_group.Grouplist FROM member_group.Grouplist left JOIN member_group.`user`on (member_group.Grouplist.`user` =member_group.`user`.`ID` ) where member_group.`user`.username ='"+num+"' and member_group.Grouplist.groupid ='"+groupid+"'";
        //String sql = "DELETE  IGNORE from Grouplist where groupid='"+groupname+"'and user='"+num+"'";
        
        statement = connection.prepareCall(sql);
        
        try{
        statement.executeUpdate();
        
        }catch(Exception e){
            System.out.println(e);
        }


    }
        statement.close();
        System.out.println("DB_group_remove_member：finish");
    }


    public static void DB_drop_group(String key)throws Exception,SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        PreparedStatement statement=null;
        /*String sql2 = "select Column_id from member_group.Group where name='"+key+"'";
        Statement statement1=connection.createStatement();
        ResultSet rs=statement1.executeQuery(sql2);
        String groupname="";
        while(rs.next()){
            groupname = rs.getString("Column_id");
        }*/



        String sql = "DELETE  from `Group` where `name`='"+key+"'";
        System.out.println(sql);
        statement = connection.prepareCall(sql);
        statement.executeUpdate();
        statement.close();
    }

    
    public static void DB_group_add_member(String groupid,String member)throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        String[] token=member.split(",");
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        PreparedStatement statement=null;
        
        String sql2 = "select Column_id from member_group.Group where name='"+groupid+"'";
        System.out.println(sql2);
        Statement statement1=connection.createStatement();
        ResultSet rs=statement1.executeQuery(sql2);
        String groupname="";
        while(rs.next()){
            groupname = rs.getString("Column_id");
        }

        
        System.out.println(groupname);

        if(!groupname.equals(""))
        {for(String num:token)
    {   

        String sql3 = "select ID from member_group.`user` where username='"+num+"'";
        System.out.println(sql3);

        Statement statement2=connection.createStatement();
        rs=statement2.executeQuery(sql3);
        String userid="";
        while(rs.next()){
            userid = rs.getString("ID");
        }
        if(!userid.equals(""))
        {String sql = "insert IGNORE into Grouplist(groupid,`user`) values("+groupname+","+userid+")";
        System.out.println(sql);
        statement = connection.prepareCall(sql);
        
        try{
        statement.executeUpdate();
        
        }catch(Exception e){
            System.out.println(e);
        }}


    }
        statement.close();
        }
        else {System.out.println("wrong group name");}
    }


    public static void list_group_i(String i)throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
       // System.out.println("String i:"+i);
        String sql = "SELECT g.`user` from member_group.Grouplist g ,member_group.`Group` g2 where  g2.Column_id =g.groupid and g2.name='"+i+"' order by g.`user` asc";
        Statement statement=connection.createStatement();
        System.out.println("=====================");
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
        String sql = "select g.`user` from Grouplist g,member_group.`Group` g2 where  g2.Column_id =g.groupid and g2.name='"+i+"'";
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);
        //System.out.println("sql:"+sql);
        ArrayList<String> list=new ArrayList<String> ();
        
        while (rs.next()){
   
            list.add(rs.getString("user"));
            

        }

        
        return list;
    }

    public static ArrayList<String> group_get()throws Exception,SQLException{
        System.out.println("group_get");

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        //System.out.println("I="+i);
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "select g2.name,u.username  from Grouplist g,`Group` g2,`user` u where g.groupid =g2.Column_id and g2.validation=1 and u.ID=g.`user` and u.Status=1";
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);

        ArrayList<String> list=new ArrayList<String> ();
       
        while (rs.next()){
            list.add(rs.getString("name"));
            list.add(rs.getString("username"));
            

        }
        //System.out.print("rs:"+rs.next());
        for(String num:list){
            System.out.println(num);

        }
        
        return list;
    }

    public static ArrayList<String> group_list_get()throws Exception,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver") ;
        //System.out.println("I="+i);
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        String sql = "select name from member_group.`Group` where `validation`=1";
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);

        ArrayList<String> list=new ArrayList<String> ();
       
        while (rs.next()){
            list.add(rs.getString("name"));
           // list.add(rs.getString("validaton"));
            

        }

        
        return list;
    }




    public static void DB_change_group_name(String group1,String group2)throws Exception,SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver") ;
       
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
        
        String sql = "UPDATE  `Group` set name='"+group2+"'where name='"+group1+"'" ;
       // System.out.println(sql);
        PreparedStatement statement = connection.prepareCall(sql);

        try{
            statement.executeUpdate();
            System.out.println("finish");

            }catch(Exception e){
               // System.out.println(e);
                System.out.println("group name have been existed");
            }
    
            statement.close();
        

    
        
    }

public static void DB_increase_group(String group1)throws Exception,SQLException{
    Class.forName("com.mysql.cj.jdbc.Driver") ;
    //System.out.println("I="+i);
    Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
    String sql = "insert  IGNORE into `Group`(name) values('"+group1+"')";
    PreparedStatement statement = connection.prepareCall(sql);

    try{
        statement.executeUpdate();
        
        }catch(Exception e){
            System.out.println(e);
        }

        statement.close();
    


    
        System.out.println("DB_increase_group：finish");

}

    public static void list_group()throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");
       



        String  sql = "select `name`,validation from `Group` ";
        //System.out.println(sql);
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(sql);
        System.out.println("\nGroup list");
        System.out.println("|Group name\t|validation|");
        System.out.println("=====================");
        while (rs.next()){
   
            String name = rs.getString("name");
            String validation = rs.getString("validation");
            System.out.printf("|"+name+"\t|");
            

            System.out.print(validation);
            System.out.println("\t|");
        }



    } 

    public static void DB_change_group_validation (String i,String j) throws SQLException,ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver") ;
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.104:3306/member_group","root","0000");

        String  sql = "update `Group` set validation='" +i+"' where name='"+j+"'";
        //System.out.println(sql);
        Statement statement=connection.createStatement();
        statement.executeUpdate(sql);

    
        
      



    }



}
