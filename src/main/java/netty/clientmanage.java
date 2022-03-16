package netty;

import java.sql.SQLException;
import java.util.ArrayList;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

public class clientmanage {
    static Boolean create_group=false;
    static String groupname="";
    static ArrayList<String>group_member=new ArrayList<String>(); 





    static public boolean manage_member(String cmd,ChannelHandlerContext ctx) throws SQLException, Exception{
        boolean i=true;
        //System.out.println("cmd："+cmd);
        
        switch(cmd){
            case "cmd:list":
            {//System.out.print("command:list");
            
            list_client(ctx);
            }
            break;

            case "cmd:create_group":
           // System.out.println(cmd);
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("group name",CharsetUtil.UTF_8));                      
            break;

            case "cmd:delete_group":
                
            
            list_client(ctx);
            list_group(ctx);  
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("which group you want to drop",CharsetUtil.UTF_8));                  

            break;


            case "cmd:request_group":
                
            list_group(ctx);  
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("which group you want to join",CharsetUtil.UTF_8));                  

            break;

        default:
            {if(cmd.contains("cmd:createname->")){
                String[] toke=cmd.split("->");
                groupname=toke[1];
                System.out.println("groupname："+groupname);
                ctx.channel().writeAndFlush(Unpooled.copiedBuffer("member：",CharsetUtil.UTF_8));  
            }else if(cmd.contains("cmd:createmember->")){
                String[] toke=cmd.split("->");
                //String[] member=toke[1].split("&");
                System.out.println("groupname："+groupname);
                
                String newString1 = toke[1].replace("&", ",");
                System.out.println("member："+newString1);
                Database.DB_increase_group(groupname);
                Database.DB_group_add_member(groupname, newString1);
            }else if (cmd.contains("cmd:delete_group->")){

                String[] toke=cmd.split("->");
                groupname=toke[1];

                System.out.println("groupname："+groupname);
                Database.DB_drop_group(groupname);
            }
            else{i=false;}
            
        }
            break;
    }
    return i;
}

static public void list_client(ChannelHandlerContext ctx) throws SQLException, Exception{
    ctx.channel().write(Unpooled.copiedBuffer("|Groupname"+"|",CharsetUtil.UTF_8));
    ctx.channel().write(Unpooled.copiedBuffer("membername|"+"\n",CharsetUtil.UTF_8));
    ctx.channel().write(Unpooled.copiedBuffer("======================"+"\n",CharsetUtil.UTF_8));
    ArrayList<String> group = Database.group_get();
                int i=1;    
                
            for (String l : group) {
                
             ctx.channel().write(Unpooled.copiedBuffer("|"+l+"\t",CharsetUtil.UTF_8));
                    if(i%2==0){
                        ctx.channel().write(Unpooled.copiedBuffer("|\n",CharsetUtil.UTF_8));
                    }else{
                   // ctx.channel().write(Unpooled.copiedBuffer("|",CharsetUtil.UTF_8));
                }
                i++;
                
            }
            ctx.channel().flush();

}


static public void list_group(ChannelHandlerContext ctx) throws SQLException, Exception{
    ArrayList<String> group = Database.group_list_get();
                int i=1;    
            ctx.channel().write(Unpooled.copiedBuffer("|Groupname"+"|\n",CharsetUtil.UTF_8));
            ctx.channel().write(Unpooled.copiedBuffer("============"+"\n",CharsetUtil.UTF_8));
            for (String l : group) {
                
             ctx.channel().write(Unpooled.copiedBuffer("|"+l+"\t|\n",CharsetUtil.UTF_8));
                
            }
            ctx.channel().flush();

}



static public void create_group(ChannelHandlerContext ctx,String cmd) throws SQLException, Exception{
   
             ctx.channel().write(Unpooled.copiedBuffer("Group_name:",CharsetUtil.UTF_8));


}

}
