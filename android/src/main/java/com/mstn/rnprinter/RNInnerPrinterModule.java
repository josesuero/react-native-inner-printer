
package com.mstn.rnprinter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.smartdevice.aidl.IZKCService;
import com.sunmi.printerhelper.utils.AidlUtil;

import java.io.UnsupportedEncodingException;

public class RNInnerPrinterModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNInnerPrinterModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
      Intent intent = new Intent("com.zkc.aidl.all");
      intent.setPackage("com.smartdevice.aidl");
      reactContext.bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);

    AidlUtil.getInstance().connectPrinterService(reactContext);
  }

  public static IZKCService mIzkcService;
  private ServiceConnection mServiceConn = new ServiceConnection() {
    @Override
    public void onServiceDisconnected(ComponentName name) {
      Log.e("client", "onServiceDisconnected");
      mIzkcService = null;
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      Log.e("client", "onServiceConnected");
      /**
       服务绑定成功，获取到该服务接口对象，可通过该接口调用相关的接口方法来完成相应的功能
       *success to get the sevice interface object
       */
      mIzkcService = IZKCService.Stub.asInterface(service);
    }
  };

  @Override
  public String getName() {
    return "RNInnerPrinter";
  }

  @ReactMethod
    public void isPrinterAvailable(final Promise promise){
      promise.resolve(true);
  }

  @ReactMethod
    public void print(String content, int size,  boolean isBold, boolean isUnderline){
      String utf8Str = new StringBuilder(content).append("\n\n\n\n").toString();
      byte[] btUTF8 = new byte[0];
      try {

        if (mIzkcService != null) {
          btUTF8 = utf8Str.getBytes("UTF-8");
          //modify printer encoding to utf-8
          mIzkcService.sendRAWData("print", new byte[]{0x1C, 0x43, (byte) 0xFF});
          //must sleep，wait setting and save success
          SystemClock.sleep(100);
          mIzkcService.sendRAWData("print", btUTF8);
        } else {
          AidlUtil.getInstance().initPrinter();
          AidlUtil.getInstance().printText(utf8Str, size, isBold, isUnderline);
        }


      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      } catch (RemoteException e) {
          e.printStackTrace();
      }
  }
}