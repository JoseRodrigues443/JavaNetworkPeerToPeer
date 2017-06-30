/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkElements;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class IpProtocolTest {
    
    public IpProtocolTest() {
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
     * Test of equals method, of class IpProtocol.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        IpProtocol o = IpProtocol.IPV4;
        IpProtocol instance = IpProtocol.IPV9;
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class IpProtocol.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        IpProtocol instance = IpProtocol.IPV4;
        String expResult = "ipv4";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
