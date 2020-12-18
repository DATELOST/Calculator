package team.seventeen.graphcalculator;

import android.content.Context;
import android.util.AttributeSet;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AxisViewTest {
    AxisView axisView;
    Context context;
    AttributeSet attrs;
    @Before
    public void setUp() throws Exception {
        context= ApplicationProvider.getApplicationContext();
        axisView=new AxisView(context,attrs);
        ArrayList<String>str=new ArrayList<>();
        str.add("y=x^2");
        axisView.setF(str);
        ArrayList<Expression>ex=new ArrayList<>();
        axisView.setFf(ex);
    }
    @After
    public void tearDown() throws Exception { }
    @Test(expected=Exception.class)
    public void getExpressions() {
        assertNotNull(axisView.getF());
        assertEquals(axisView.getF().get(0),"y=x^2");
        axisView.getExpressions();
        assertNotNull(axisView.getFf());
        assertTrue(Math.abs(axisView.getFf().get(0).c)<0.00001f);
        assertTrue(!axisView.getFf().get(0).getP().isEmpty());
        axisView.getF().add("yyyy");
        axisView.getExpressions();
    }

    @Test
    public void f() {
    }
}