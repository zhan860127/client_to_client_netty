package netty;

public class datatype {
    private String key;
    private String method;
    private String channel;
    private String status;
  

    public void setMethod(String method){

        this.method=method;
    }

    public void setStatus(String status){

        this.status=status;
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
    public String getStatus(){

        return status;
    }


    public datatype(String method,String channel,String status,String key)
    {
        setdatatype(method,channel,status,key);

    }

    public void setdatatype(String method,String channel,String status,String key)
    {   
        setMethod(method);
        setChannel(channel);
        setStatus(status);
        setKey(key);
  
    }
}
