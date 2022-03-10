package netty;


import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import org.json.JSONObject;
import java.util.regex.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class MyServerHandler extends ChannelInboundHandlerAdapter  {
    private static int MAX_CONN=15;
    public int groupNum = 2;
    private Vector <ChannelHandlerContext> contexts=new Vector<>(MAX_CONN);
    private Map<String, ArrayList<Integer>> map = new HashMap<>();
    private Map<String, Boolean> map_valid = new HashMap<>();
    ArrayList<Integer> first;
    ArrayList<Integer> second;
    ArrayList<ArrayList<Integer>> table= new ArrayList<ArrayList<Integer>>();

 

    public MyServerHandler(){
        first = new ArrayList<Integer>(Arrays.asList(1,3));
        second = new ArrayList<Integer>(Arrays.asList(2,3,5));
        table.add(first);
        table.add(second);
        map.put("1", first);
        map_valid.put("1",true);
        map.put("2", second);
        map_valid.put("2",true);
    }//initialize the group
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送消息到服务端
  

        System.out.println("成功連線");
        
        contexts.add(ctx);



        
      
        new Thread(new Runnable() {

            @Override
            public void run() {
                Scanner myObj = new Scanner(System.in);
                String key;
                
                while(true){
                key=myObj.next();  
                manage_member(key,myObj);
                
                }
            
            }
        }).start();       //purpose for let server can communication to client    
        }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端发送过来的消息
       //sendmessage(ctx,msg);
       JSONObject  jsonObjectdata=decodemsg.Decodemsg(msg);
       System.out.println("JSON  in myserver："+jsonObjectdata);
    
       sendmessage_test(ctx,jsonObjectdata);
    }
    private void manage_member(String key,Scanner myObj){
        switch(key){
            case "add_member":
            System.out.println("add_member(group,member1,member2...)");
            key=myObj.next();  
            add_member(key);
            break;

            case "remove_member":
            System.out.println("remove_member(group,member1,member2...)");
            key=myObj.next();  
            remove_member(key);
            break;

            case "list":
            System.out.println("group list ");
            System.out.println(map);
            break;

            case "drop_group":
            System.out.println("group list ");
            System.out.println(map);
            System.out.println("witch group want to drop");
            key=myObj.next();
            drop_group(key);

            break;

            case "change_list_name":
            System.out.println("which group you want to change");
            String group_1=myObj.next();
            System.out.println("change name");
            String group_2=myObj.next();
            ChangeGroupName(group_1,group_2);

            break;


            default :
                if(key.matches("list,\\d")){
                    
                    String[] tokens=key.split(",");
                    System.out.println("group list"+tokens[1]);
                    System.out.println(map.get(tokens[1]));
                }
            break;


        }


    }

    public static String getLastElement(String[] list)
	{
		if((list != null) && (list.length != 0))
		{
			int lastIdx = list.length - 1;
			String lastElement = list[lastIdx];
			return lastElement;
		}
		else
			return null;
	}
    private void sendmessage_test(ChannelHandlerContext ctx, JSONObject  jsonObjectdata) throws Exception {
        
        sendtoserver(jsonObjectdata.get("method").toString(),jsonObjectdata.get("channel").toString(),jsonObjectdata.get("key").toString(),contexts.indexOf(ctx));


    }
    /* not use json to be the 
    private void sendmessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        
     
        String[] tokens = decodemsg.decode(msg);
      

        System.out.println("tokens.length："+tokens.length);
        int currentIndex =contexts.indexOf(ctx);
            //System.out.println("ok");
        sendtoserver(tokens[0],tokens[1],tokens[2],currentIndex);
        System.out.println("client:" + ctx.channel().remoteAddress() + "的消息：" + tokens[2]);
        
    }*/


    private void sendtoserver(String method,String channel,String key,int currentIndex){
        //System.out.println("ok2");
            System.out.println("Key1："+key);
        switch(method){
            default:
            broadcast(key,currentIndex);
            break;
            case "1":
            groupcast(key,channel,currentIndex);
            break;//要 break 不然會自動往下跑

            case "2":
            unicast(channel,key,currentIndex);
            break;

        }

    }


    private void broadcast(String key,int currentIndex){
        
        for (int i=0;i<contexts.size();i++){   
            System.out.println("Usernum："+contexts.size());
            if (i!=currentIndex){
                System.out.println("向："+i);
                contexts.get(i).writeAndFlush(Unpooled.copiedBuffer("User"+currentIndex+"："+key,CharsetUtil.UTF_8));
            }
        }
        }

    private void groupcast(String key,String channel ,int currentIndex){

        System.out.println("Key2："+key);

            ArrayList<Integer> group = map.get(channel);
               System.out.print(group);            
                for (Integer num : group){
                   if(num<contexts.size()&&num!=currentIndex)    
                   {   System.out.println(num);      
                       contexts.get(num).writeAndFlush(Unpooled.copiedBuffer("User"+currentIndex+"："+key,CharsetUtil.UTF_8));                     
                   
                    }         
               }
    }

    private void unicast(String target,String key,int currentIndex){
        System.out.println("unicast work");
                       contexts.get(Integer.parseInt(target)).writeAndFlush(Unpooled.copiedBuffer(currentIndex+"："+key ,CharsetUtil.UTF_8));                     
                          
               }


    public void remove_member(String key){
        String[] tokens = key.split(",");

        Integer[]temp=new Integer[tokens.length];
        for(int i=0;i<tokens.length;i++){
            temp[i]= Integer. parseInt(tokens[i]);
        }
        if(map.get(tokens[0])!=null){

            remove_members(map.get(tokens[0]),temp);
        }
        else{
            System.out.println("群組不存在");
        }
    
    }


    public void drop_group(String key){
        if(map.get(key)!=null){

            table.remove(table.indexOf(map.get(key)));
            map.remove(key);
            map_valid.remove("key");
        }else{System.out.println("group is not exist");}
        }


        public void ChangeGroupName(String group1,String group2){
            if(map.get(group1)!=null){
    
                ArrayList<Integer> temp=map.get(group1);
                map.put(group2,temp);
                map_valid.put(group2,true);
                map.remove(group1);
                map_valid.remove(group1);
            }else{System.out.println("group is not exist");}
            }
    
    public void add_member(String key){
        String[] tokens = key.split(",");

        Integer[]temp=new Integer[tokens.length];
        for(int i=0;i<tokens.length;i++){
            temp[i]= Integer. parseInt(tokens[i]);
        }

        if(map.get(tokens[0])!=null){

        increment_member(map.get(tokens[0]),temp);}
        else 
        {   ArrayList<Integer> a=new ArrayList<Integer>();
            increment_member(a,temp);
            map.put(tokens[0],a);
            map_valid.put(tokens[0],true);
        
        }
    
        }


        private void increment_member(ArrayList<Integer> group,Integer[] member){
            
        
            for (int i=1;i<member.length;i++)
            {   if (group.indexOf(member[i])==-1)
                {group.add(member[i]);}
            }
        }

        private void remove_members(ArrayList<Integer> group,Integer[] member){
            
        
            for (int i=1;i<member.length;i++)
            {   if (group.indexOf(member[i])!=-1)
                {group.remove(group.indexOf(member[i]));}
            }
        }

    
/*

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Scanner myObj = new Scanner(System.in);
        String key;
    
        key=myObj.next();   
        ctx.writeAndFlush(Unpooled.copiedBuffer(key, CharsetUtil.UTF_8));
    }*/
    

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常，关闭通道
        System.out.println(cause);
        ctx.close();
    }

    

}

