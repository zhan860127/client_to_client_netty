package netty;

public class connet {
    static private String user1;
    static private String user2;
    static private boolean status=true;

    public void change_status(boolean a){

        this.status=a;
    }

    public void setconnet(String user1,String user2){
            this.user1=user1;
            this.user2=user2;
    }

    public boolean getStatus(){
        return status;
    }
    static public boolean tell_connet(String u1,String u2){
        
        return(user1==u1&&user1==u2);
    }
}
