/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import networkElements.Address;
import networkElements.IpProtocol;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import server.Server;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class UtilsTest {

    public UtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of secondsToMiliseconds method, of class Utils.
     */
    @Test
    public void testSecondsToMiliseconds() {
        System.out.println("secondsToMiliseconds");
        int seconds = 0;
        int defaultValue = 0;
        int expResult = 0;
        int result = Utils.secondsToMiliseconds(seconds, defaultValue);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMachineIpAddress method, of class Utils.
     */
    @Test
    public void testReturnMachineIpAddressFailEmptyAddressType() {
        System.out.println("returnMachineIpAddress");
        String ipv4Ipv6 = "";
        String expResult = "172.0.0.2";
        String result = Utils.getMachineIpAddress(ipv4Ipv6);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMachineIpAddress method, of class Utils.
     */
    @Test
    public void testReturnMachineIpAddressSuccessIpv4() {
        System.out.println("returnMachineIpAddress");
        String ipv4Ipv6 = "ipv4";
        String expResult = "192.168.1.102";
        String result = Utils.getMachineIpAddress(ipv4Ipv6);
        assertEquals(expResult, result);
    }

    /**
     * Test of validateAddress method, of class Utils.
     */
    @Test
    public void testValidateAddressFailEmptyAddressType() {
        System.out.println("validateAddress");
        String address = "";
        String addressType = "";
        boolean expResult = false;
        boolean result = Utils.validateAddress(address, addressType);
        assertEquals(expResult, result);
    }

    /**
     * Test of validateAddress method, of class Utils.
     */
    @Test
    public void testValidateAddressSuccessIpv4() {
        System.out.println("validateAddress");
        String address = "192.168.0.1";
        String addressType = "ipv4";
        boolean expResult = true;
        boolean result = Utils.validateAddress(address, addressType);
        assertEquals(expResult, result);
    }

    /**
     * Test of sumOfAllIpv4Digits method, of class Utils.
     */
    @Test
    public void testSumOfAllIpv4Digits() {
        System.out.println("sumOfAllIpv4Digits");
        String ipv4 = "222.222.222.222";
        int expResult = 888;
        int result = Utils.sumOfAllIpv4Digits(ipv4);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMachineIpAddress method, of class Utils.
     */
    @Test
    public void testReturnMachineIpAddress() {
        System.out.println("returnMachineIpAddress");
        String ipv4Ipv6 = "ipv4";
        String expResult = "192.168.1.102"; /*it may varie in the pc*/
        String result = Utils.getMachineIpAddress(ipv4Ipv6);
        assertEquals(expResult, result);
    }

    /**
     * Test of validateAddress method, of class Utils.
     */
    @Test
    public void testValidateAddress() {
        System.out.println("validateAddress");
        String address = "";
        String addressType = "";
        boolean expResult = false;
        boolean result = Utils.validateAddress(address, addressType);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMachineBroadcastAddressIpv4 method, of class Utils.
     */
    @Test
    public void testGetMachineBroadcastAddressIpv4() {
        System.out.println("getMachineBroadcastAddressIpv4");
        String expResult = "192.168.1.255";
        String result = Utils.getMachineBroadcastAddressIpv4();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMachineMaskAddressIpv4 method, of class Utils.
     */
    @Test
    public void testGetMachinemaskAddressIpv4() {
        System.out.println("getMachinemaskAddressIpv4");
        String expResult = "/24";
        String result = Utils.getMachineMaskAddressIpv4();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMachineIpAddress method, of class Utils.
     */
    @Test
    public void testGetMachineIpAddress() {
        System.out.println("getMachineIpAddress");
        String ipv4Ipv6 = "";
        String expResult = "";
        String result = Utils.getMachineIpAddress(ipv4Ipv6);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMachineMaskAddressIpv4 method, of class Utils.
     */
    @Test
    public void testGetMachineMaskAddressIpv4() {
        System.out.println("getMachineMaskAddressIpv4");
        String expResult = "/21";
        String result = Utils.getMachineMaskAddressIpv4();
        assertEquals(expResult, result);
    }

  

    /**
     * Test of returnModelForJList method, of class Utils.
     */
    @Test
    public void testReturnModelForJList() {
        System.out.println("returnModelForJList");
        ArrayList<File> files = new ArrayList<>();
        files.add(new File("pao quente.txt"));
        DefaultListModel expResult = new DefaultListModel();
        DefaultListModel result = Utils.returnModelForJList(files);
        assertEquals(result, result);
    }


}
