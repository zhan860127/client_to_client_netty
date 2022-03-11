package netty;

public class connet {
     private String user1;
     private String user2;
     private boolean status=true;

    public void change_status(boolean a){

        this.status=a;
    }

    public String getuser1(){

        return user1;
    }

    public void setconnet(String user1,String user2){
            this.user1=user1;
            this.user2=user2;
    }

    public boolean getStatus(){
        return status;
    }
     public boolean tell_connet(String u1,String u2){

        return(this.user1.equals(u1)&&this.user2.equals(u2));
    }
}
