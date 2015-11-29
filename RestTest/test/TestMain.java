/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Runi
 */
public class TestMain {
    
    public TestMain() {
    }
        
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void P1(){
        new P1().runTest();
    }
    
    @Test
    public void P2(){
        new P2().runTest();
    }
    
    @Test
    public void B(){
        new B().runTest();
    }
    
    @Test
    public void C1(){
        new C1().runTest();
    }
    
    @Test
    public void C2(){
        new C2().runTest();
    }
}
