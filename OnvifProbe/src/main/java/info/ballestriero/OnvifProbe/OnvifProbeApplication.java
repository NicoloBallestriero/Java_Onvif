package info.ballestriero.OnvifProbe;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.udp.UDPConduit;
import org.apache.cxf.ws.discovery.WSDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnvifProbeApplication {

	public static void main(String[] args) throws SocketException {
		SpringApplication.run(OnvifProbeApplication.class, args);
		System.out.println("hello world");
		
		
		
		  Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
	        for (NetworkInterface netint : Collections.list(nets))
	            displayInterfaceInformation(netint);


		
		Bus bus = BusFactory.getThreadDefaultBus();
		try {
			bus.setProperty(UDPConduit.NETWORK_INTERFACE,
			NetworkInterface.getByInetAddress(InetAddress.getByName("192.168.1.101")));
			//NetworkInterface.getByInetAddress(InetAddress.getByName("127.0.0.1")));
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Use WS-Discovery to find references to services that implement the changeName portType
        //WSDiscoveryClient client = new WSDiscoveryClient();
		WSDiscoveryClient client = new WSDiscoveryClient(bus);
        // Setting timeout for WS-Discovery
        client.setDefaultProbeTimeout(10000);
        // Use WS-discovery 1.0 (Standard in use with Onvif ip cameras)
        client.setVersion10();
        System.out.println("Probe:" + client.getAddress());
        List<EndpointReference> references =null;
        try {
        	
        	references = client.probe(new QName("http://www.onvif.org/ver10/network/wsdl", "DiscoveryService"));
        	//references = client.probe();
        }catch (Exception e) {
			// TODO: handle exception
		}finally {
			for (EndpointReference ref : references) {
	        	System.out.println(ref.toString());
			}
		}
        
        try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                 
        
//        for (EndpointReference ref : references) {
//        	System.out.println(ref.toString());
//        } 
		
	}
	
	static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
    	System.out.printf("Display name: %s\n", netint.getDisplayName());
    	System.out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
        	System.out.printf("InetAddress: %s\n", inetAddress);
        }
        System.out.printf("\n");
     }

}
