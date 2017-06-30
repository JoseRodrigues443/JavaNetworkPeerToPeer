/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkElements;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class Address {

    /**
     * future enum true for IPV4, and false for IPV6
     */
    private IpProtocol addressType;

    /**
     *
     * FORMAT
     *
     * e0:db:55:dd:7f:4c OR 192.168.0.69
     */
    private String address;
    /**
     * FORMAT: "/8" OR "/31" etc....
     */
    private String maskAddress;
    /**
     * broadcast address of the network that thr host is located
     */
    private String broadcastAddress;
    private int udpPort;
    private int tcpPort;

    /**
     *
     * @param isServer
     */
    public Address(boolean isServer) {
        address = "empty";
        addressType = IpProtocol.IPV4;
        maskAddress = "empty";
        broadcastAddress = "empty";
        udpPort = (isServer ? ConnectionUDP.PORT_UDP_FOR_SERVER_UPLOAD_DEFAULT
                : ConnectionUDP.PORT_UDP_FOR_CLIENT_CONTACT_SERVER_DEFAULT);
        tcpPort = FileConnectionTCP.MINIMUM_PORT_NUMBER_DEFAULT;

    }

    /**
     *
     * @param addressType
     * @param address
     * @param maskAddress
     * @param broadcastAddress
     * @param udpPort
     * @param tcpPort
     */
    public Address(IpProtocol addressType, String address, String maskAddress, String broadcastAddress, int udpPort, int tcpPort) {
        this.addressType = addressType;
        this.address = address;
        this.maskAddress = maskAddress;
        this.broadcastAddress = broadcastAddress;
        this.udpPort = udpPort;
        this.tcpPort = tcpPort;
    }

    public String getBroadcastAddress() {
        return broadcastAddress;
    }

    public void setBroadcastAddress(String broadcastAddress) {
        this.broadcastAddress = broadcastAddress;
    }

    public IpProtocol getAddressType() {
        return addressType;
    }

    public void setAddressType(IpProtocol addressType) {
        this.addressType = addressType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaskAddress() {
        return maskAddress;
    }

    public void setMaskAddress(String macAddress) {
        this.maskAddress = macAddress;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    @Override
    public String toString() {
        return "Address{" + "addressType=" + addressType + ", address=" + address + ", maskAddress=" + maskAddress + ", broadcastAddress=" + broadcastAddress + ", udpPort=" + udpPort + ", tcpPort=" + tcpPort + '}';
    }

}
