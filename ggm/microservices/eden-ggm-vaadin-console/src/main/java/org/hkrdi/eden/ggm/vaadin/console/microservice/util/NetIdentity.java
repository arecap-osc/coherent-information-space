package org.hkrdi.eden.ggm.vaadin.console.microservice.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that allows a device to identify itself on the INTRANET.
 * 
 * @author Decoded4620 2016
 */
public class NetIdentity {

	private static AtomicInteger counter = new AtomicInteger();
	
    private String loopbackHost = "";
    private String host = "";

    private String loopbackIp = "";
    private String ip = "";
    public NetIdentity(){
    }
    
    public String getDescription() {
    	String result = "";
    	
    	try{
            InetAddress loopbackIpAddress = InetAddress.getLocalHost();
            this.loopbackIp = loopbackIpAddress.getHostName();
            result += "\n"+("COUNTER: " + counter.incrementAndGet());
            result += "\n"+("LOCALHOST: " + loopbackIp);
            result += "\n"+loopbackIpAddress.toString();
        }
        catch(UnknownHostException e){
        	result += "\n"+("ERR: " + e.toString());
        }
    	
//        try{
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//
//            while(interfaces.hasMoreElements()){
//                NetworkInterface i = interfaces.nextElement();
//                if(i != null){
//                    Enumeration<InetAddress> addresses = i.getInetAddresses();
//                    result += "\n"+(i.getDisplayName());
//                    result += "\n"+("\t- name:" + i.getName());
//                    result += "\n"+("\t- idx:" + i.getIndex());
//                    result += "\n"+("\t- max trans unit (MTU):" + i.getMTU());
//                    result += "\n"+("\t- is loopback:" + i.isLoopback());
//                    result += "\n"+("\t- is PPP:" + i.isPointToPoint());
//                    result += "\n"+("\t- isUp:" + i.isUp());
//                    result += "\n"+("\t- isVirtual:" + i.isVirtual());
//                    result += "\n"+("\t- supportsMulticast:" + i.supportsMulticast());
//                    while(addresses.hasMoreElements()){
//                        InetAddress address = addresses.nextElement();
//                        String hostAddr = address.getHostAddress();
//
//                        // local loopback
//                        if(hostAddr.indexOf("127.") == 0 ){
//                            this.loopbackIp = address.getHostAddress();
//                            this.loopbackHost = address.getHostName();
//                        }
//
//                        // internal ip addresses (behind this router)
//                        if( hostAddr.indexOf("192.168") == 0 || 
//                                hostAddr.indexOf("10.") == 0 || 
//                                hostAddr.indexOf("172.16") == 0 ){
//                            this.host = address.getHostName();
//                            this.ip = address.getHostAddress();
//                        }
//
//
//                        result += "\n"+("\t\t-" + address.getHostName() + ":" + address.getHostAddress() + " - "+ address.getAddress());
//                    }
//                }
//            }
//        }
//        catch(SocketException e){
//
//        }
        
        return result;
    }

    public String getLoopbackHost(){
        return loopbackHost;
    }

    public String getHost(){
        return host;
    }
    public String getIp(){
        return ip;
    }
    public String getLoopbackIp(){
        return loopbackIp;
    }
}
