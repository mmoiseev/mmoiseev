package com.suchorukov.server;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

public final class RequestParser {

    private RequestParser() {
    }

    public static String getRelativePath(String header) throws UnsupportedEncodingException {
        String r = "";
        if (header != null && header.trim().length() > 0) {
            String[] cmd = header.split(" ");
            if (cmd[1].length() > 1) {
                cmd[1] = URLDecoder.decode(cmd[1], "UTF-8");
                cmd[1]  = cmd[1].replaceFirst("/$","");
                r = cmd[1].replace("/", File.separator);
            }
        }
        return r;
    }

    public static String getRequestMethod(String header) {
        String r = "";
        if (header != null && header.trim().length() > 0) {
            String[] cmd = header.split(" ");
            if (cmd[0].length() > 1){
                r = cmd[0];
            }
        }
        return r;
    }

    public HashMap<String, Object> getParams(String header) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String r = "";
        if (header != null && header.trim().length() > 0) {
            String[] cmd = header.split(" ");
            if (cmd[1].length() > 1) {
                String[] reqParams = cmd[1].split("\\?",2);
                if (reqParams.length == 2 && reqParams[1].length() > 0) {
                    String[] paramsStr = reqParams[1].split("&");
                    if (paramsStr.length > 1){
                        for (int i = 0; i < paramsStr.length; i++){
                            String[] kv = paramsStr[i].split("=",2);
                            if (kv.length == 2){
                                map.put(kv[0],kv[1]);
                            } else {
                                map.put(kv[0],null);
                            }
                        }
                    } else {
                        map.put(paramsStr[0],null);
                    }
                }
            }
        }
        return map;
    }

}
