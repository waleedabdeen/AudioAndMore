package com.example.waleed.audioandmore;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Waleed on 06/05/2018.
 */

public class DLNAClass extends AppCompatActivity{

    private Context context;
    public DLNAClass(Context context){
        this.context = context;
    }
    private final static String DISCOVER_MESSAGE = "M-SEARCH * HTTP/1.1\r\n"
            + "HOST: 239.255.255.250:1900\r\n" + "MAN: \"ssdp:discover\"\r\n"
            + "MX: 3\r\n" + " urn:schemas-sony-com:service:ScalarWebAPI:1\r\n";

    public String Discover (){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            MulticastSocket s = new MulticastSocket(1900);
            s.joinGroup(InetAddress.getByName("239.255.255.250") );
            DatagramPacket packet = new DatagramPacket(DISCOVER_MESSAGE.getBytes(), DISCOVER_MESSAGE.length(), getBroadcastAddress(), 1900);
            s.setBroadcast(true);
            s.send(packet);

            while(true) {
                byte[] buf = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

                s.receive(receivePacket);
                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                return msg;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Nothing";
    }

    public InetAddress getBroadcastAddress() throws IOException {

        Context mContext = this.context;
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

}
