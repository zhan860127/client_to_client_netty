package netty;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class MyServerHandler extends ChannelInboundHandlerAdapter  {
    private static int MAX_CONN=15;
    public int connectNum = 0;
    private Vector <ChannelHandlerContext> contexts=new Vector<>(MAX_CONN);
    private Map<String, ArrayList<Integer>> map = new HashMap<>();
    
    public MyServerHandler(){
        ArrayList<Integer> first = new ArrayList<Integer>(Arrays.asList(1,3));
        ArrayList<Integer> second = new ArrayList<Integer>(Arrays.asList(2,3,5));
        map.put("1", first);
        map.put("2", second);
        

    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送消息到服务端
  

        System.out.println("成功連線");
        
        contexts.add(ctx);
        InetSocketAddress socket =(InetSocketAddress)ctx.channel().remoteAddress();
        System.out.println(socket.getAddress().getHostAddress()+":"+socket.getPort());
        System.out.println(connectNum);


        /*
          chc=ctx;
        new Thread(new Runnable() {

            @Override
            public void run() {
                Scanner myObj = new Scanner(System.in);
                String key;
                
                while(true){
                key=myObj.next();   
                chc.writeAndFlush(Unpooled.copiedBuffer(key, CharsetUtil.UTF_8));
                }
            
            }
        }).start();  */     //purpose for let server can communication to client    
        }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端发送过来的消息
        //System.out.println(connectNum);
       sendmessage(ctx,msg);
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

    private void sendmessage(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;
        String a= byteBuf.toString(CharsetUtil.UTF_8);
        String[] tokens = a.split(",");
        String key=getLastElement(tokens);
        int currentIndex =contexts.indexOf(ctx);
        sendtoserver(tokens,key,currentIndex);
        System.out.println("client:" + ctx.channel().remoteAddress() + "的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
    }


    private void sendtoserver(String[] tokens,String key,int currentIndex){
        switch(tokens.length){
            case 1:
            broadcast(key,currentIndex);
            case 2:
            groupcast(key,tokens,currentIndex);
            case 3:
            

        }

    }


    private void broadcast(String key,int currentIndex){

        for (int i=0;i<contexts.size();i++){   
            if (i!=currentIndex){
                contexts.get(i).writeAndFlush(Unpooled.copiedBuffer(currentIndex+"："+key,CharsetUtil.UTF_8));
            }
        }

    }

    private void groupcast(String key,String[] tokens ,int currentIndex){
        ArrayList<Integer> group = map.get(tokens[0]);
               System.out.print(group);            
                for (Integer num : group){
                   if(num<contexts.size()&&num!=currentIndex)    
                   {                 
                       contexts.get(num).writeAndFlush(Unpooled.copiedBuffer(currentIndex+"："+key ,CharsetUtil.UTF_8));                     
                   }         
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
        ctx.close();
    }

}

