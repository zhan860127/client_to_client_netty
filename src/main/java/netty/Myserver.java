package netty;

import java.util.Scanner;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class Myserver {
    public static void main(String[] args) throws Exception {
        //创建两个线程组 boosGroup、workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();//bossGroup线程池负责监听端口，获取一个线程作为MainReactor,用于处理端口的Accept事件。
        EventLoopGroup workerGroup = new NioEventLoopGroup();//workerGroup线程池负责处理Channel（通道）的I/O事件，并处理相应的业务。
        try {
            //创建服务端的启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置两个线程组boosGroup和workerGroup
            bootstrap.group(bossGroup, workerGroup)
                //设置服务端通道实现类型    
                .channel(NioServerSocketChannel.class)
                //设置线程队列得到连接个数    
                .option(ChannelOption.SO_BACKLOG, 128)
                //设置保持活动连接状态    
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //使用匿名内部类的形式初始化通道对象    
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    private  final MyServerHandler SHARED =  new MyServerHandler();

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //给pipeline管道设置处理器
                            
                            socketChannel.pipeline().addLast("",SHARED);

                        }
                    });//给workerGroup的EventLoop对应的管道设置处理器
            System.out.println("伺服器準備就緒");
            //绑定端口号，启动服务端

            
            ChannelFuture channelFuture = bootstrap.bind(6666).sync();
            
            
            //对关闭通道进行监听



            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
