package netty;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class decodemsg {

    public  static JSONObject Decodemsg(Object msg){
        String[] tokens=decode(msg);
   
        
        datatype data=new datatype(tokens[0],tokens[1],tokens[2],tokens[3]);
        //System.out.println("data.method："+data.getStatus());


        JSONObject jsonObjectdata=new JSONObject(data);

        return jsonObjectdata;
    }

    public  static String[] decode(Object msg){
        ByteBuf byteBuf = (ByteBuf) msg;
        String a= byteBuf.toString(CharsetUtil.UTF_8);
        String[] tokens = a.split(",");
        String[] temp=handletokens(tokens);
        return temp;
    }

    public  static String[] handletokens(String[] tokens){
        String[] temp=new String [4];
        if (tokens.length<4){
            
            int lastindex=tokens.length-1;
            for(int i=3;i>=0;i--,lastindex--){
                
                if (lastindex>=0)
                {temp[i]=tokens[lastindex];}
                else{
                    temp[i]="0";
                }       

              
            }
        
        }else{String total="";
            for(int i=1;i<=tokens.length-1;i++)
            {   
                if(i<3){
                    temp[i-1]=tokens[i];
                }else{
                    total=total+tokens[i];
                }
           
            }
            temp[3]=total;
            temp[2]=tokens[0];
           
        }
        return temp;
    }
}
