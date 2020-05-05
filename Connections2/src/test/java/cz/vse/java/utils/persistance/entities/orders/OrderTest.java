package cz.vse.java.utils.persistance.entities.orders;

import cz.vse.java.util.persistance.entities.EUnit;
import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.entities.Product;
import cz.vse.java.util.persistance.entities.orders.Order;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class OrderTest {

    public static Order order;


    @Before
    public void prepare() {

        order = new Order(
                25L,
                "submitter",
                "sub@sub.sub",
                "Prepare the order as soon as possible."
        );

        Product p = new Product();
        p.setId(21L);
        p.setQuantity(234);
        p.setLocation("ASDF2");
        p.setUnit(EUnit.LITER);
        p.setProductName("name");
        p.setBarcode("13413243ASFD1243");
        p.setShortDesc("ASDFASDFADSF");
        p.setLongDesc("ADFADFGADFGSDFSDGADGAFGADFGA");
        p.setPrice(new BigDecimal(342));


        OrderItem oi = new OrderItem(

                31L,
                p,
                4
        );


        order.addOrderItem(oi);
    }


    @Test
    public void adding() {

        Product p2 = new Product();
        p2.setId(25L);
        p2.setQuantity(22);
        p2.setLocation("A2");
        p2.setUnit(EUnit.LITER);
        p2.setProductName("name");
        p2.setBarcode("1FD1243");
        p2.setShortDesc("SD");
        p2.setLongDesc("LD: asdfasdfasdfasddfasdfasdf");
        p2.setPrice(new BigDecimal(1325));

        OrderItem oi2 = new OrderItem(
                35L,
                p2,
                12
        );


        order.addOrderItem(oi2);
        assertEquals(2, order.getOrderItems().size());

        oi2.setId(31L);

        assertEquals(2, order.getOrderItems().size());
    }
}