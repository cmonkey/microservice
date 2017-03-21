package com.microservice.concurrent;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class DiscardServer{
    private int port;

    public DiscardServer(int port){
        this.port = port;
    }

    public void run() throws Exception{
        EventLoopGroup boseGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(boseGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception{
                        channel.pipeline().addLast(new DiscardServerHandler());
                    }
                })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port);

            f.channel().closeFuture().sync();
        } catch(Exception e) {

            e.printStackTrace();
        }finally{
            workerGroup.shutdownGracefully();
            boseGroup.shutdownGracefully();
        }
    }

    public static void main (String [] args) throws Exception{

        int port ;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }else{
            port = 8080;
        }

        new DiscardServer(port).run();

    }
}
