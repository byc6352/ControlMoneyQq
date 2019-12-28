/**
 * 
 */
package com.example.h3.job;

import java.io.DataOutputStream;
import java.io.OutputStream;

import com.example.h3.Config;

import android.content.Context;

/**
 * @author byc
 *
 */
public class RootShellCmd {
	
	private static RootShellCmd current;
	private  String TAG = "byc001";//调试标识：
	
	private OutputStream os;  
	private Process process;
	OutputStream outputStream ;
	DataOutputStream dataOutputStream;
	
	public RootShellCmd() {
		TAG=Config.TAG;
     
	}
    public static synchronized RootShellCmd getRootShellCmd() {
        if(current == null) {
            current = new RootShellCmd();
        }
        return current;
    }
	   /** 
     * 执行shell指令 
     *  
     * @param cmd 
     *            指令 
     */  
    public final void exec(String cmd) {  
        try {  
            if (os == null) {  
                os = Runtime.getRuntime().exec("su").getOutputStream();  
            }  
            os.write(cmd.getBytes());  
            os.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * 后台模拟全局按键 
     *  
     * @param keyCode 
     *            键值 
     */  
    public final void simulateKey(int keyCode) {  
        exec("input keyevent " + keyCode + "\n");  
    }  
    
    /** 
     * 执行shell命令 
     *  
     * @param cmd 
     */  
    public boolean execShellCmd(String cmd) {  
      
        try {  
            // 申请获取root权限，这一步很重要，不然会没有作用  
            if (process == null) {  
            	Process process = Runtime.getRuntime().exec("su"); 
                // 获取输出流  
                outputStream = process.getOutputStream();  
                dataOutputStream = new DataOutputStream(outputStream);  
            }  

            dataOutputStream.writeBytes(cmd);  
            dataOutputStream.flush();  
            dataOutputStream.close();  
            outputStream.close();  
            return true;
        } catch (Throwable t) {  
            t.printStackTrace();  
            return false;
        }  
    } 
    /** 
     * 初始化shell命令 
     *  
     * @param cmd 
     */  
    public boolean initShellCmd() {  
      
        try {  
            // 申请获取root权限，这一步很重要，不然会没有作用  
            	Process process = Runtime.getRuntime().exec("su"); 
                // 获取输出流  
                outputStream = process.getOutputStream();  
                dataOutputStream = new DataOutputStream(outputStream);  

            return true;
        } catch (Throwable t) {  
            t.printStackTrace();  
            return false;
        }  
    } 
}
