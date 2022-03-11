package netty;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class MyClient {
    static public Scanner myObj = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //创建bootstrap对象，配置参数
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(eventExecutors)
                //设置客户端的通道实现类型    
                .channel(NioSocketChannel.class)
                //使用匿名内部类初始化通道
                .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加客户端通道的处理器asd
                            ch.pipeline().addLast(new MyClientHandler());
                            
                        }
                    });
            System.out.println("Command\n(channel,target,message)：send massage\nchannel：\n0：broadcast \n1：groupcast\n2：unicast");
            //连接服务端
            System.out.println("unicast rule");
            
            System.out.println("2,X,connect X is the targetID you want to construct\n");
            
            System.out.println("'list'：lookup group list\n");
            
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();

            Channel  channel =channelFuture.channel();
            
            
            String key;
            
            while(true){
            key=myObj.next();   
            String[] tokens=key.split(",");
            if(tokens[0].equals("2")&&tokens.length>1){
                MyClientHandler.connetmember.add(tokens[1]);
                MyClientHandler.channel="0";
            }




            try
            {
                if(key.equals("Y")){

                    MyClientHandler.addconnet();

                    //System.out.println(MyClientHandler.connetmember);
                    channel.writeAndFlush(Unpooled.copiedBuffer("2,"+MyClientHandler.channel+",agree", CharsetUtil.UTF_8));

                    
                }
                else if(key.equals("N")){
                    channel.writeAndFlush(Unpooled.copiedBuffer("2,"+MyClientHandler.channel+",N", CharsetUtil.UTF_8));


                }
            else{
            
                channel.writeAndFlush(Unpooled.copiedBuffer(key, CharsetUtil.UTF_8));}}
            catch(Exception e){
                System.out.println(e);
            }
            }


        } finally {
            //关闭线程组
            eventExecutors.shutdownGracefully();
        }
    }
}