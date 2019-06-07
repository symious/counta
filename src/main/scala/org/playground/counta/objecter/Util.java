package org.playground.counta.objecter;

import java.net.InetAddress;

public class Util {
    public static String getLocalhost(){
        String res = null;
        try {
            res = InetAddress.getLocalHost().toString();
        }catch(Exception e){
           res = "NULL";
        }
        return res;
    }
}
