# client_to_client_netty


## prev 
https://github.com/zhan860127/netty_single_chat

## update

change the server client commucation to client to client.

The server can pass the message to all client.




## log

change the method new handle function to sharable witch can share the handle with all client
add the vector to manage the client message.

## 問題

server 變數沒辦法共享，不同的 channel 各自的server 變數都非共享
解法：透過 netty 的 sharable 來達成，需要在一開始 server new 的時候設定好。

client 端管理，透過 map array方式。


