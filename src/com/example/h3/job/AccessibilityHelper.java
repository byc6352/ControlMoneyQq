/**
 * 
 */
package com.example.h3.job;

import java.util.ArrayList;
import java.util.List;

import com.example.h3.Config;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author byc
 *
 */
public class AccessibilityHelper {
	
	public static List<AccessibilityNodeInfo> classNames= new ArrayList<AccessibilityNodeInfo>();
    /** 通过文本查找*/
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text,int i) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if(list == null || list.isEmpty()) {
            return null;
        }
        if(i==-1)
        	return list.get(list.size()-1);
        else
        	return list.get(i);
    }
    /** 通过id查找*/
    public  static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId,int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if(list != null && !list.isEmpty()) {
            	if(i==-1)return list.get(list.size()-1);
                return list.get(i);
            }
        }
        return null;
    }
    //拆包延时：
    public  static void Sleep(int MilliSecond) {
    	
	    try{
	    	  Thread.sleep(MilliSecond);
	    }catch(Exception e){
	    } 
    }
    /** 通过控件查找*/
    public static AccessibilityNodeInfo findNodeInfosByClassName(AccessibilityNodeInfo rootNode, String className,int i,boolean bClear) {
    	
    	if(bClear){
    		classNames.clear();
    		recycleClassName(rootNode,className);
    	}   	
        if(classNames == null || classNames.isEmpty()) return null;
        if(i<0)
        	return classNames.get(classNames.size()+i);
        else
        	return classNames.get(i);
    }
    public static void recycleClassName(AccessibilityNodeInfo info,String className) {
  		if (info.getChildCount() == 0) {

  			if(className.equals(info.getClassName())){
  				classNames.add(info);
  			}
  			//Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  		} else {
  			for (int i = 0; i < info.getChildCount(); i++) {
  				if(info.getChild(i)!=null){
  					recycleClassName(info.getChild(i),className);
  				}
  			}
  		}
  	}
    /** 得到rootNode*/
    public  static AccessibilityNodeInfo getRootNode(AccessibilityNodeInfo node){
    	if(node==null)return null;
    	AccessibilityNodeInfo parent=node.getParent();
    	AccessibilityNodeInfo tmp=node;
    	while(parent!=null){
    		tmp=parent;
    		parent=parent.getParent();
    	}
    	return tmp;
    }
    /** 点击事件*/
    public static boolean performClick(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        	return false;
        }
        if(nodeInfo.isClickable()) {
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            return performClick(nodeInfo.getParent());
        }
        
    }
    /** 点击事件*/
    public static boolean performLongClick(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        	return false;
        }
        if(nodeInfo.isClickable()) {
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
        } else {
            return performLongClick(nodeInfo.getParent());
        }
        
    }
    public static void recycle(AccessibilityNodeInfo info) {
  		if (info.getChildCount() == 0) {
  			//取信息
  			//mRedInfo[mIntInfo]=info.getText().toString();
  			Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  			//Log.i(TAG, "showDialog:" + info.canOpenPopup());
  			Log.i(Config.TAG, "Text：" + info.getText());
  			Log.i(Config.TAG, "windowId:" + info.getWindowId());
  			Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
  			Log.i(Config.TAG, "isClickable:" + info.isClickable());
  			Log.i(Config.TAG, "desc:" + info.getContentDescription());
  			Rect outBounds=new Rect();
  			info.getBoundsInScreen(outBounds);
  			Log.i(Config.TAG, "outBounds:" + outBounds);
          	//Bundle arguments = new Bundle();
          	//String sText="11";
          	//arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,sText);
          	//info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
  			
  			//if(info.isClickable())info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
  		} else {
  			Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  			//Log.i(TAG, "showDialog:" + info.canOpenPopup());
  			Log.i(Config.TAG, "Text：" + info.getText());
  			Log.i(Config.TAG, "windowId:" + info.getWindowId());
  			Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
  			Log.i(Config.TAG, "isClickable:" + info.isClickable());
  			Log.i(Config.TAG, "desc:" + info.getContentDescription());
  			Rect outBounds=new Rect();
  			info.getBoundsInScreen(outBounds);
  			Log.i(Config.TAG, "outBounds:" + outBounds);
  			for (int i = 0; i < info.getChildCount(); i++) {
  				if(info.getChild(i)!=null){
  					recycle(info.getChild(i));
  				}
  			}
  		}
  	}
}
