//package com.example.getlocalinfo;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.InetAddress;
//import java.net.MulticastSocket;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
//import android.net.wifi.WifiManager.MulticastLock;
//import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceRequest;
//import android.os.AsyncTask;
//import android.util.Log;
//
//
///// 测试Android多设备之间的组播程序
//class MulticastDemo {
//
//
//    // / 测试Android多设备之间的组播程序之初始化与反初始化。
//    private MulticastLock multicastLock = null;
//
//    public void init(Context c) {
//        // Android的Wifi，默认情况下是不接受组播的，调用MulticastLock对象的acquire方法，获取到组播锁。
//        WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
//        multicastLock = wifiManager.createMulticastLock("multicast.test");
//        multicastLock.acquire();
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        if (wifiInfo != null) {
//            Log.d(TAG, "getBSSID:" + wifiInfo.getBSSID());
//        }
//
//    }
//
//    public void uninit() {
//        if (null != multicastLock) {
//            multicastLock.release();
//            multicastLock = null;
//        }
//    }
//
//    private static final String TAG = "MulticastDemo";
//    private static final int MULTICAST_PORT = 5111;
//    private static final String GROUP_IP = "239.0.0.10";//224.5.0.7
//    private static class WrapAsyncTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            // TODO Auto-generated method stub
//            try {
//                Log.d(TAG, "findAroundStationIpAddress");
//                findAroundStationIpAddress();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
//
//    public static void asyncTaskFindAroundStation() {
//        new WrapAsyncTask().execute();
//    }
//    public static List<String> findAroundStationIpAddress() throws IOException {
//        String ip = null;
//        MulticastSocket multicastSocket = new MulticastSocket(MULTICAST_PORT);
//        multicastSocket.setLoopbackMode(true);
//        //multicastSocket.setSoTimeout(5000);
//        //multicastSocket.setSoTimeout(10 * 1000);//
//        InetAddress group = InetAddress.getByName(GROUP_IP);
//        multicastSocket.joinGroup(group);
//
//        String query =
//                "M-SEARCH * HTTP/1.1\r\n" +
//                        "HOST: 239.255.255.250:1900\r\n"+
//                        "MAN: \"ssdp:discover\"\r\n"+
//                        "MX: 1\r\n"+
//                        "ST: urn:schemas-sony-com:service:ScalarWebAPI:1\r\n"+  // Use for Sonos
//                        //"ST: ssdp:all\r\n"+  // Use this for all UPnP Devices
//                        "\r\n";
//
//
////        final byte[] sendData = query.getLocalIpAddress().getBytes();
////        WifiP2pUpnpServiceRequest
//        Log.d(TAG, "sendData: " + new String(sendData));
//        DatagramPacket packet = new DatagramPacket(sendData, sendData.length,
//                group, MULTICAST_PORT);
//        ArrayList<String> ipAddressList = new ArrayList<String>();
//        for (;;) {
//            multicastSocket.send(packet);
//            Log.d(TAG, ">>>send packet ok");
//            byte[] receiveData = new byte[256];
//            packet = new DatagramPacket(receiveData, receiveData.length);
//            Log.d(TAG, "0 receive packet getLength: " + packet.getLength());
//            multicastSocket.receive(packet);
//            Log.d(TAG, "1 receive packet getLength: " + packet.getLength());
//            String packetIpAddress = packet.getAddress().toString();
//            Log.d(TAG, "packet ip address: " + packetIpAddress);
//            packetIpAddress = packetIpAddress.substring(1, packetIpAddress.length());
//
//            StringBuilder packetContent = new StringBuilder();
//            for (int i = 0; i < receiveData.length; i++) {
//                if (receiveData[i] == 0) {
//                    break;
//                }
//                packetContent.append((char) receiveData[i]);
//            }
//            Log.d(TAG, "packet content is:" + packetContent);
//            ip = packetContent.toString();
//            if (ip.equals(packetIpAddress)) {
//                Log.d(TAG, "find station's ip address: " + ip);
//                ipAddressList.add(ip);
//            } else {
//                Log.d(TAG, "not find station's ip address, continue …");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    Log.e(TAG, "InterruptedException e" + e);
//                    break;
//                }
//            }
//        }
//        return ipAddressList;
//    }
//}