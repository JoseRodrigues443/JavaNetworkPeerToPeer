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
public enum IpProtocol {
    IPV4("ipv4"),
    IPV6("ipv6"),
    /**
     * the chinese and the japanese are already implementing these protocol ipv9
     */
    IPV9("ipv9"),
    UNKNOWN("ERROR NOT KNOWN");
    
    private final String ipType;

    /**
     *
     * @param s
     */
    private IpProtocol(String s) {
        ipType = s;
    }

    /**
     *
     * @param o
     * @return
     */
    public boolean equals(IpProtocol o) {
        return ipType.equalsIgnoreCase(o.toString());
    }
    
    @Override
    public String toString() {
        return this.ipType;
    }
}
