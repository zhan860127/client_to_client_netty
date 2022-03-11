package netty;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext chc;
    private AtomicInteger integer = new AtomicInteger(0);
    static public ArrayList<String> connetmember=new ArrayList<String>();
    static public String channel;
    static public String method;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端发送过来的消息


        JSONObject a=decodemsg.Decodemsg(msg);
        ByteBuf byteBuf = (ByteBuf) msg;
        String c= byteBuf.toString(CharsetUtil.UTF_8);
        //System.out.println(c);


        String temp=a.get("key").toString();
        
        String method=a.get("method").toString();
        //System.out.println(a);
        if(temp.contains("-"))
        {
        String[] origin=temp.split("-");
        
        channel=origin[1];
        if(connetmember.indexOf(channel)!=-1||!method.equals("2"))
        {System.out.println(origin[0]+origin[1]+origin[2]);}
        else
        {   
           // System.out.println("是否要與 user"+channel+" 建立連線(Y/N)");
           
        }
    }else{

        if(c.contains("[")&&c.contains("]")){temp=c;}
        System.out.println(temp);
    }
        
    }

    static public void addconnet(){
        if(connetmember.indexOf(channel)==-1)
        {connetmember.add(channel);}


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //接收服务端发送过来的消息
    }

}





