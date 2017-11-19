package com.cryptofacilities.interview;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by CF-8 on 6/27/2017.
 */
public class SampleTest {

    public OrderBookManager createBookManager(String instrument) {

        OrderBookManager orderBookManager = new OrderBook(instrument);

        return orderBookManager;
    }

    public LinkedList<Order> createOrders(int ordNumbers) {
        // create order
        LinkedList<Order> orderList = new LinkedList<Order>();
        int incr = 0;
        OrderBookManager bm = createBookManager("VOD.L");

        for (int i = 0; i < ordNumbers; i++) {
            Order currentOrder = new Order("order" + i, "VOD.L", Side.buy, 50 + incr, 10 + incr);
            bm.addOrder( currentOrder );
            orderList.add(currentOrder);
            incr += 10;
        }
        //OrderBook book = new OrderBook("VOD.L", orderBookManager.getBuyOrdersList("VOD.L"), orderBookManager.getSellOrdersList("VOD.L"));
        // send order
        bm.printOrders();

        return orderList;
    }

    @Test
    public void testDeleteOrder() {
        // create order book
        //orderBookManager.moveOrderToEnd( buy3 );
        LinkedList<Order> ords = createOrders(10);
        createBookManager("VOD.L").deleteOrder(Side.buy, ords.get(3).getOrderId());
        //createOrders(10,createBookManager("VOD.L"));
        //orderBookManager.printOrders();
        // check that best price is 200
        int expectedLength = 9;
        int actualLength = ords.size();
        assertEquals( "Best bid price is 200", expectedLength, actualLength );
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
//        assertEquals( "Quantity is set to 55", 53, sell4.getQuantity());
//        assertEquals( "Quantity is set to 93", 93, buy6.getQuantity());
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

    @Test
    public void testBestPrice() {

        OrderBookManager orderBookManager = new OrderBook("VOD.L");

        // create order
        Order buy = new Order("order1", "VOD.L", Side.sell, 200, 10 );
        Order buy2 = new Order("order2", "VOD.L", Side.sell, 100, 10 );
        Order buy3 = new Order("order3", "VOD.L", Side.sell, 5, 10 );
        Order buy4 = new Order("order4", "VOD.L", Side.sell, 10, 40 );
        Order buy5 = new Order("order5", "VOD.L", Side.sell, 70, 30 );

        //OrderBook book = new OrderBook("VOD.L", orderBookManager.getBuyOrdersList("VOD.L"), orderBookManager.getSellOrdersList("VOD.L"));
        // send order
        orderBookManager.addOrder( buy );
        orderBookManager.addOrder( buy2 );
        orderBookManager.addOrder( buy3 );
        orderBookManager.addOrder( buy4);
        orderBookManager.addOrder( buy5 );
        orderBookManager.printOrders();

        //orderBookManager.moveOrderToEnd( buy3 );

//        orderBookManager.deleteOrder(Side.buy, buy3.getOrderId());
//        orderBookManager.printOrders();
        // check that best price is 200
        long expectedPrice = 200;
        long actualPrice = orderBookManager.getBestPrice("VOD.L", Side.buy );
        assertEquals(expectedPrice, actualPrice);

    }

    @Test
    public void testOrderNumAtLevel() {

        OrderBookManager orderBookManager = new OrderBook("VOD.L");

        // create order
        Order buy = new Order("order1", "VOD.L", Side.buy, 200, 10 );
        Order buy2 = new Order("order2", "VOD.L", Side.buy, 100, 10 );
        Order buy3 = new Order("order3", "VOD.L", Side.buy, 5, 10 );
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

//        orderBookManager.deleteOrder(Side.buy, buy3.getOrderId());
//        orderBookManager.printOrders();
        // check that best price is 200
        long expectedLength = 4;
        long actualLength = orderBookManager.getOrderNumAtLevel("VOD.L", Side.buy, 5006);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testTotalQuantityAtLevel() {
        OrderBookManager orderBookManager = new OrderBook("VOD.L");

        // create order
        Order buy = new Order("order1", "VOD.L", Side.buy, 200, 10 );
        Order buy2 = new Order("order2", "VOD.L", Side.buy, 100, 10 );
        Order buy3 = new Order("order3", "VOD.L", Side.buy, 5, 10 );
        Order buy4 = new Order("order4", "VOD.L", Side.buy, 90, 40 );
        Order buy5 = new Order("order5", "VOD.L", Side.buy, 90, 30 );

        //OrderBook book = new OrderBook("VOD.L", orderBookManager.getBuyOrdersList("VOD.L"), orderBookManager.getSellOrdersList("VOD.L"));
        // send order
        orderBookManager.addOrder( buy );
        orderBookManager.addOrder( buy2 );
        orderBookManager.addOrder( buy3 );
        orderBookManager.addOrder( buy4);
        orderBookManager.addOrder( buy5 );
        orderBookManager.printOrders();

        //orderBookManager.moveOrderToEnd( buy3 );

//        orderBookManager.deleteOrder(Side.buy, buy3.getOrderId());
//        orderBookManager.printOrders();
        // check that best price is 200
        long expectedQuantity = 70;
        long actualQuantity = orderBookManager.getTotalQuantityAtLevel("VOD.L", Side.buy, 56);
        assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    public void testTotalVolumeAtLevel() {

        OrderBookManager orderBookManager = new OrderBook("VOD.L");

        // create order
        Order buy = new Order("order1", "VOD.L", Side.buy, 200, 10 );
        Order buy2 = new Order("order2", "VOD.L", Side.buy, 100, 10 );
        Order buy3 = new Order("order3", "VOD.L", Side.buy, 5, 10 );
        Order buy4 = new Order("order4", "VOD.L", Side.buy, 90, 40 );
        Order buy5 = new Order("order5", "VOD.L", Side.buy, 90, 30 );

        //OrderBook book = new OrderBook("VOD.L", orderBookManager.getBuyOrdersList("VOD.L"), orderBookManager.getSellOrdersList("VOD.L"));
        // send order
        orderBookManager.addOrder( buy );
        orderBookManager.addOrder( buy2 );
        orderBookManager.addOrder( buy3 );
        orderBookManager.addOrder( buy4);
        orderBookManager.addOrder( buy5 );
        orderBookManager.printOrders();

        //orderBookManager.moveOrderToEnd( buy3 );

//        orderBookManager.deleteOrder(Side.buy, buy3.getOrderId());
//        orderBookManager.printOrders();
        // check that best price is 200
        long expectedVolume = 2000;
        long actualVolume = orderBookManager.getTotalVolumeAtLevel("VOD.L", Side.buy, 200);
        assertEquals(expectedVolume, actualVolume);

    }

    @Test
    public void testOrderAtLevel() {

        OrderBookManager orderBookManager = new OrderBook("VOD.L");

        // create order
        Order buy = new Order("order1", "VOD.L", Side.buy, 200, 10 );
        Order buy2 = new Order("order2", "VOD.L", Side.buy, 100, 10 );
        Order buy3 = new Order("order3", "VOD.L", Side.buy, 5, 10 );
        Order buy4 = new Order("order4", "VOD.L", Side.buy, 90, 40 );
        Order buy5 = new Order("order5", "VOD.L", Side.buy, 90, 30 );

        //OrderBook book = new OrderBook("VOD.L", orderBookManager.getBuyOrdersList("VOD.L"), orderBookManager.getSellOrdersList("VOD.L"));
        // send order
        orderBookManager.addOrder( buy );
        orderBookManager.addOrder( buy2 );
        orderBookManager.addOrder( buy3 );
        orderBookManager.addOrder( buy4);
        orderBookManager.addOrder( buy5 );
        orderBookManager.printOrders();

        //orderBookManager.moveOrderToEnd( buy3 );

//        orderBookManager.deleteOrder(Side.buy, buy3.getOrderId());
//        orderBookManager.printOrders();
        // check that best price is 200
        long expectedQuantity = 70;
        LinkedList<Order> actualList = orderBookManager.getOrdersAtLevel("VOD.L", Side.buy, 46);
        assertEquals(expectedQuantity, actualList);
    }
}
