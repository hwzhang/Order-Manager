package com.cryptofacilities.interview;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by CF-8 on 6/27/2017.
 */
public class SampleTest {

    public OrderBookManager createBookManager(String instrument, int buyOrdNumbers, int sellOrdNumbers) {

        OrderBookManager orderBookManager = new OrderBook(instrument);

        int incr = 0;

        for (int i = 0; i < buyOrdNumbers; i++) {

            Order currentOrder = new Order("order" + i, instrument, Side.buy, 10 + incr, 10 + incr);
            orderBookManager.addOrder( currentOrder );
            incr += 10;
        }

        for (int i = 0; i < sellOrdNumbers; i++) {

            Order currentOrder = new Order("order" + i, instrument, Side.sell, 10 + incr, 10 + incr);
            orderBookManager.addOrder( currentOrder );
            incr += 10;
        }

        orderBookManager.printOrders();

        return orderBookManager;
    }

    @Test
    public void testDeleteOrder() {

        try {
            OrderBookManager oBM = createBookManager("VOD.L", 15, 20);
            oBM.deleteOrder(Side.sell , oBM.getOrdersList("VOD.L", Side.sell).get(0).getOrderId());

            int expectedLength = 4;
            int actualLength = oBM.getOrdersList("VOD.L", Side.sell).size();
            assertEquals(expectedLength, actualLength );
        }
        catch (IndexOutOfBoundsException e) {
            Assert.fail("Error - out of bounds.");
        }
    }

    @Test
    public void testDeleteOrderOOB() {

        try {
            OrderBookManager oBM = createBookManager("VOD.L", 15, 10);
            oBM.deleteOrder(Side.sell , oBM.getOrdersList("VOD.L", Side.sell).get(0).getOrderId());

            int expectedLength = 14;
            int actualLength = oBM.getOrdersList("VOD.L", Side.sell).size();
            assertEquals(expectedLength, actualLength );
        }
        catch (IndexOutOfBoundsException e) {
            Assert.fail("Error - out of bounds.");
        }
    }

    @Test
    public void testMultipleBooks() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 10);
        OrderBookManager oBM1 = createBookManager("VAT.L", 10, 20);
        OrderBookManager oBM2 = createBookManager("VOD.L", 25, 30);
        OrderBookManager oBM3 = createBookManager("VER.L", 100, 40);

        int expectedBooks = 3; //No books of duplicate instruments
        int actualBooks = OrderBook.getNumberOfBooks();
        assertEquals(expectedBooks, actualBooks);
    }

    @Test
    public void testQuantityChange() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 20);

        long expectedQ = 250;
        int orderToModify = 4;

        oBM.modifyOrder(Side.sell, oBM.getOrdersList("VOD.L", Side.sell).get(orderToModify).getOrderId(), expectedQ);

        long actualQ = oBM.getOrdersList("VOD.L", Side.sell).get(orderToModify).getQuantity();
        assertEquals(expectedQ, actualQ);
    }

    @Test
    public void testQuantityChangeOOB() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 10);

        long expectedQ = 250;
        int orderToModify = 4;

        try {
            oBM.modifyOrder(Side.sell, oBM.getOrdersList("VOD.L", Side.sell).get(orderToModify).getOrderId(), expectedQ);
        }
        catch (IndexOutOfBoundsException e) {
            Assert.fail("Error - Array out of bounds.");
        }
    }

    @Test
    public void testQuantityChangeNegative() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 20);

        long quantityNew = -60;
        int orderToModify = 0;

        oBM.modifyOrder(Side.sell, oBM.getOrdersList("VOD.L", Side.sell).get(orderToModify).getOrderId(), quantityNew);

        long actualQ = oBM.getOrdersList("VOD.L", Side.sell).get(orderToModify).getQuantity();
        assertNotEquals(quantityNew, actualQ);
    }

    @Test
    public void testBestPrice() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 15);

        long expectedPrice = 10;
        long actualPrice = oBM.getBestPrice("VOD.L", Side.buy );
        assertEquals(expectedPrice, actualPrice);

    }

    @Test
    public void testBestPriceNoOrders() {

        OrderBookManager oBM = createBookManager("VOD.L", 0, 0);

        long expectedPrice = -1;
        long actualPrice = oBM.getBestPrice("VOD.L", Side.sell );
        assertEquals(expectedPrice, actualPrice);

    }

    @Test
    public void testOrderNumAtLevel() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 24);

        long expectedLength = 5;
        long actualLength = oBM.getOrderNumAtLevel("VOD.L", Side.buy, 700);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testOrderNumAtLevelNegPrice() {

        OrderBookManager oBM = createBookManager("VOD.L", 20, 0);

        long expectedLength = -1;
        long actualLength = oBM.getOrderNumAtLevel("VOD.L", Side.buy, -100);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testOrderNumAtLevelNonExistent() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 14);

        long expectedLength = -1;
        long actualLength = oBM.getOrderNumAtLevel("VOD.L", Side.buy, 4000);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testTotalQuantityAtLevel() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 24);

        long expectedQuantity = 650;
        long actualQuantity = oBM.getTotalQuantityAtLevel("VOD.L", Side.buy, 130);
        assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    public void testTotalQuantityAtLevelNegPrice() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 15);

        long expectedQuantity = -1;
        long actualQuantity = oBM.getTotalQuantityAtLevel("VOD.L", Side.buy, -100);
        assertEquals(expectedQuantity, actualQuantity);
    }


    @Test
    public void testTotalQuantityAtLevelNonExistent() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 8);

        long expectedQuantity = -1;
        long actualQuantity = oBM.getTotalQuantityAtLevel("VOD.L", Side.buy, 45555);
        assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    public void testTotalVolumeAtLevel() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 24);

        long expectedPrice = 130;
        long expectedQuantity = 650;
        long expectedVolume = expectedPrice * expectedQuantity;
        long actualVolume = oBM.getTotalVolumeAtLevel("VOD.L", Side.buy, 130);
        assertEquals(expectedVolume, actualVolume);

    }

    @Test
    public void testTotalVolumeAtLevelNegPrice() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 21);

        long expectedVolume = -1;
        long actualVolume = oBM.getTotalVolumeAtLevel("VOD.L", Side.buy, -20);
        assertEquals(expectedVolume, actualVolume);

    }

    @Test
    public void testTotalVolumeAtLevelNonExistent() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 3);

        long expectedVolume = -1;
        long actualVolume = oBM.getTotalVolumeAtLevel("VOD.L", Side.buy, 7800);
        assertEquals(expectedVolume, actualVolume);

    }

    @Test
    public void testOrderAtLevel() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 14);

        LinkedList<Order> expectedList = oBM.getOrdersList("VOD.L", Side.sell);
        LinkedList<Order> actualList = oBM.getOrdersAtLevel("VOD.L", Side.sell, 100);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testOrderAtLevelAfterQC() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 20);

        long expectedQ = 550;
        int orderToModify = 2;

        oBM.modifyOrder(Side.sell, oBM.getOrdersList("VOD.L", Side.sell).get(orderToModify).getOrderId(), expectedQ);

        int expectedPos = oBM.getOrdersList("VOD.L", Side.sell).size()-1;

        int actualPos = oBM.getOrdersList("VOD.L", Side.sell).indexOf(oBM.getOrdersList("VOD.L", Side.sell).get(expectedPos));
        assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testOrderAtLevelNonExistent() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 20);

        LinkedList<Order> expectedList = new LinkedList<Order>(); //empty list expected
        LinkedList<Order> actualList = oBM.getOrdersAtLevel("VOD.L", Side.sell, 6700);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testOrderAtLevelNegPrice() {

        OrderBookManager oBM = createBookManager("VOD.L", 15, 34);

        LinkedList<Order> expectedList = null;
        LinkedList<Order> actualList = oBM.getOrdersAtLevel("VOD.L", Side.sell, -700);
        assertEquals(expectedList, actualList);
    }
}
