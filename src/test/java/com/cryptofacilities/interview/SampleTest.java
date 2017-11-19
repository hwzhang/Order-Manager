package com.cryptofacilities.interview;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by CF-8 on 6/27/2017.
 */
public class SampleTest {

    @Test
    public void testDeleteOrder() {
        // create order book


        OrderBookManager orderBookManager = new OrderBook("VOD.L");

        // create order
        Order buy = new Order("order1", "VOD.L", Side.buy, 200, 10 );
        Order buy2 = new Order("order2", "VOD.L", Side.buy, 100, 10 );
        Order buy3 = new Order("order3", "VOD.L", Side.buy, 50, 10 );
        Order buy4 = new Order("order4", "VOD.L", Side.buy, 10, 40 );
        Order buy5 = new Order("order5", "VOD.L", Side.buy, 70, 30 );

        //OrderBook book = new OrderBook("VOD.L", orderBookManager.getBuyOrdersList("VOD.L"), orderBookManager.getSellOrdersList("VOD.L"));
        // send order
        orderBookManager.addOrder( buy );
        orderBookManager.addOrder( buy2 );
        orderBookManager.addOrder( buy3 );
        orderBookManager.addOrder( buy4);
        orderBookManager.addOrder( buy5 );
        orderBookManager.printOrders();

        //orderBookManager.moveOrderToEnd( buy3 );

        orderBookManager.deleteOrder(Side.buy, buy3.getOrderId());
        orderBookManager.printOrders();
        // check that best price is 200
        long expectedPrice = 200;
        long actualPrice = orderBookManager.getBestPrice("VOD.L", Side.buy );
        assertEquals( "Best bid price is 200", expectedPrice, actualPrice );
    }

    @Test
    public void testQunatityChange() {

        OrderBookManager orderBookManager = new OrderBook("VOD.L");

        // create order
        Order buy = new Order("order1", "VOD.L", Side.buy, 200, 10 );
        Order buy2 = new Order("order2", "VOD.L", Side.buy, 100, 10 );
        Order sell3 = new Order("order3", "VOD.L", Side.sell, 50, 10 );
        Order sell4 = new Order("order4", "VOD.L", Side.sell, 50, 10 );
        Order sell5 = new Order("order5", "VOD.L", Side.sell, 54, 477 );
        Order buy6 = new Order("order6", "VOD.L", Side.buy, 78, 33 );

        // send order
        orderBookManager.addOrder( buy );
        orderBookManager.addOrder( buy2 );
        orderBookManager.addOrder( sell3 );
        orderBookManager.addOrder( sell4 );
        orderBookManager.addOrder( sell5 );
        orderBookManager.addOrder( buy6 );
        orderBookManager.printOrders();

        orderBookManager.modifyOrder(Side.buy, buy2.getOrderId(), 8);
        orderBookManager.modifyOrder(Side.sell, sell4.getOrderId(), 55);
        orderBookManager.modifyOrder(Side.buy, buy6.getOrderId(), 98);

        orderBookManager.printOrders();
        long expectedQ = 8;
        long actualQ = buy2.getQuantity();
        assertEquals( "Quantity is set to 8", expectedQ, actualQ);
        assertEquals( "Quantity is set to 55", 53, sell4.getQuantity());
        assertEquals( "Quantity is set to 93", 93, buy6.getQuantity());
    }

    @Test
    public void populateOrders() {

        OrderBookManager orderBookManager = new OrderBook("VOD.L");

        Order buy = new Order("order1", "VOD.L", Side.buy, 200, 10 );
        Order buy2 = new Order("order2", "VOD.L", Side.buy, 100, 10 );
        Order sell3 = new Order("order3", "VOD.L", Side.sell, 50, 10 );
        Order sell4 = new Order("order4", "VOD.L", Side.sell, 50, 10 );
        Order sell5 = new Order("order5", "VOD.L", Side.sell, 54, 477 );
        Order buy6 = new Order("order6", "VOD.L", Side.buy, 78, 33 );


        // send order
        orderBookManager.addOrder( buy );
        orderBookManager.addOrder( buy2 );
        orderBookManager.addOrder( sell3 );
        orderBookManager.addOrder( sell4 );
        orderBookManager.addOrder( sell5 );
        orderBookManager.addOrder( buy6 );
        orderBookManager.printOrders();

        long expectedLength = 3;
        long actualLength = 3;
        assertEquals( "Quantity is set to 8", expectedLength, actualLength);
    }
}
