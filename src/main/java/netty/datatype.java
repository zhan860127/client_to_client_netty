package netty;

public class datatype {
    private String key;
    private String method;
    private String channel;
  

    public void setMethod(String method){

        this.method=method;
    }

   

    
    public void setChannel(String channel){

        this.channel=channel;
    }

    public void setKey(String key){

        this.key=key;
    }

    
    public String getKey(){

        return key;
    }

    public String getMethod(){

        return method;
    }

    public String getChannel(){

        return channel;
    }


    public datatype(String method,String channel,String key)
    {
        setdatatype(method,channel,key);

    }

    public void setdatatype(String method,String channel,String key)
    {   
        setMethod(method);
        setChannel(channel);
        setKey(key);
    }
}
