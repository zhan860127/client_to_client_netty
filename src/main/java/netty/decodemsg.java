package netty;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class decodemsg {

    public  static JSONObject Decodemsg(Object msg){
        String[] tokens=decode(msg);
        Map<String,String> map = new HashMap<String,String>();
        map.put("method", tokens[0]);
        map.put("channel", tokens[1]);
        map.put("key", tokens[2]);

        datatype data=new datatype(tokens[0],tokens[1],tokens[2]);
       // System.out.println("data.method："+data.getmethod());


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
        String[] temp=new String [3];
        if (tokens.length<3){
            
            int lastindex=tokens.length-1;
            for(int i=2;i>=0;i--,lastindex--){
                if (lastindex>=0)
                {temp[i]=tokens[lastindex];}
                else{
                    temp[i]="0";
                }       
            }
        
        }else{String total="";
            for(int i=0;i<=tokens.length-1;i++)
            {   
                if(i<2){
                    temp[i]=tokens[i];
                }else{
                    total=total+tokens[i];
                }
            }
            temp[2]=total;
        }
        return temp;
    }
}
