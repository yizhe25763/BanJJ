package com.shzy.bjj.map;



import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;



public class MapApplication extends Application {
	
    private static MapApplication mInstance = null;
    public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;


	
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		initEngineManager(this);
		
//		//��λ��ʼ��
//	     if (mBMapManager == null) {
//	            mBMapManager = new BMapManager(getApplicationContext());
//	            /**
//	             * ���BMapManagerû�г�ʼ�����ʼ��BMapManager
//	             */
//	           mBMapManager.init(new DemoApplication.MyGeneralListener());
//	        }
//	     //��λ��ʼ��
//	        mLocClient = new LocationClient( this );
//	        locData = new LocationData();
//	        mLocClient.registerLocationListener( myListener );
//	        LocationClientOption option = new LocationClientOption();
//	        option.setOpenGps(true);//��gps
//	        option.setCoorType("bd09ll");     //�����������
//	        option.setScanSpan(1000);
//	        mLocClient.setLocOption(option);
//	        mLocClient.start();
	}
	
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }
   
        if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(MapApplication.getInstance().getApplicationContext(), 
                    "BMapManager  ��ʼ������!", Toast.LENGTH_LONG).show();
        }
	}
	
	public static MapApplication getInstance() {
		return mInstance;
	}
	
	
	// �����¼�������������ͨ�������������Ȩ��֤�����
    public static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(MapApplication.getInstance().getApplicationContext(), "��������������",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(MapApplication.getInstance().getApplicationContext(), "������ȷ�ļ���������",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
        	//����ֵ��ʾkey��֤δͨ��
            if (iError != 0) {
                //��ȨKey����
                Toast.makeText(MapApplication.getInstance().getApplicationContext(), 
                        "���� DemoApplication.java�ļ�������ȷ����ȨKey,�����������������Ƿ���error: "+iError, Toast.LENGTH_LONG).show();
                MapApplication.getInstance().m_bKeyRight = false;
            }
            else{
            	MapApplication.getInstance().m_bKeyRight = true;
            	Toast.makeText(MapApplication.getInstance().getApplicationContext(), 
                        "key��֤�ɹ�", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    
    
}