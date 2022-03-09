package netty;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class MyServerHandler extends ChannelInboundHandlerAdapter  {
    private static int MAX_CONN=15;
    public AtomicInteger connectNum = new AtomicInteger(0);
    private Vector <ChannelHandlerContext> contexts=new Vector<>(MAX_CONN);



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送消息到服务端
        connectNum.incrementAndGet();
        System.out.println(connectNum);

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
        }).start();  */         
        }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端发送过来的消息
        //System.out.println(connectNum);
        ByteBuf byteBuf = (ByteBuf) msg;

       String a= byteBuf.toString(CharsetUtil.UTF_8);
        String[] tokens = a.split(",");
        String key=getLastElement(tokens);

        int currentIndex =contexts.indexOf(ctx);
        int anotherindex = Math.abs(currentIndex-1);

     //   a.writeAndFlush("456");
       // ctx.writeAndFlush(Unpooled.copiedBuffer(byteBuf));
        //contexts.get(anotherindex).writeAndFlush(Unpooled.copiedBuffer(byteBuf));


        for (int i=0;i<contexts.size();i++){

            if (i!=currentIndex){

                contexts.get(i).writeAndFlush(Unpooled.copiedBuffer(key,CharsetUtil.UTF_8));

            }
        }


        System.out.println("client:" + ctx.channel().remoteAddress() + "的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
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

