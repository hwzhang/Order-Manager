package com.cryptofacilities.interview;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by CF-8 on 6/27/2017.
 */
public class SampleTest {

    @Test
    public void testBestBidPrice() {
        // create order book
        OrderBookManager orderBookManager = new OrderBookManagerImpl();

        // create order
        Order buy = new Order("order1", "VOD.L", Side.buy, 200, 10 );
        Order buy2 = new Order("order2", "VOD.L", Side.buy, 100, 10 );
        Order buy3 = new Order("order3", "VOD.L", Side.buy, 50, 10 );
        Order buy4 = new Order("order4", "VOD.L", Side.buy, 10, 40 );
        Order buy5 = new Order("order5", "VOD.L", Side.buy, 70, 30 );


        // send order
        orderBookManager.addOrder( buy );
        orderBookManager.addOrder( buy2 );
        orderBookManager.addOrder( buy3 );
        orderBookManager.addOrder( buy4);
        orderBookManager.addOrder( buy5 );
        orderBookManager.printOrders();

        orderBookManager.moveOrderToEnd( buy3 );

        orderBookManager.deleteOrder( buy.getOrderId());
        orderBookManager.printOrders();
        // check that best price is 200
        long expectedPrice = 200;
        long actualPrice = orderBookManager.getBestPrice("VOD.L", Side.buy );
        assertEquals( "Best bid price is 200", expectedPrice, actualPrice );
    }

    @Test
    public void testQunatityChange() {

        OrderBookManager orderBookManager = new OrderBookManagerImpl();

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

        orderBookManager.modifyOrder(buy2.getOrderId(), 8);
        orderBookManager.modifyOrder(sell4.getOrderId(), 55);
        orderBookManager.modifyOrder(buy6.getOrderId(), 98);

        orderBookManager.printOrders();
        long expectedQ = 8;
        long actualQ = buy2.getQuantity();
        assertEquals( "Quantity is set to 8", expectedQ, actualQ);
        assertEquals( "Quantity is set to 55", 53, sell4.getQuantity());
        assertEquals( "Quantity is set to 93", 93, buy6.getQuantity());
    }
}
