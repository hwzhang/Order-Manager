package com.cryptofacilities.interview;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CF-8 on 6/27/2017.
 */
public class OrderBookManagerImpl implements OrderBookManager {

    private static List<Order> orderList = new ArrayList<Order>();

    public void addOrder(Order order) {
        orderList.add(order);
    }

    public void modifyOrder(String orderId, long newQuantity) {

    }

    public void deleteOrder(String orderId) {

        Iterator<Order> orderIterator = orderList.iterator();

        while (orderIterator.hasNext()) {
            Order order = orderIterator.next();
                if (order.getOrderId().equalsIgnoreCase(orderId))
                    //System.out.println(orderList.indexOf(order));
                    orderIterator.remove();
                    //orderList.remove(orderList.indexOf(order));
                System.out.println("Order " + order.getOrderId() + " successfully removed!");
        }
    }

    public void printOrders() {
        System.out.println("List of Orders:\n");
        for (Order order: orderList) {
            System.out.println(order.toString());
        }

    }

    public void moveOrderToEnd(Order order) {

    }

    public long getBestPrice(String instrument, Side side) {
        return 0;
    }

    public long getOrderNumAtLevel(String instrument, Side side, long price) {
        return 0;
    }

    public long getTotalQuantityAtLevel(String instrument, Side side, long price) {
        return 0;
    }

    public long getTotalVolumeAtLevel(String instrument, Side side, long price) {
        return 0;
    }

    public List<Order> getOrdersAtLevel(String instrument, Side side, long price) {
        return null;
    }
}
