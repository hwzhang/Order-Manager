package com.cryptofacilities.interview;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.*;

/**
 * Created by CF-8 on 6/27/2017.
 */
public class OrderBook implements OrderBookManager {

    private volatile LinkedList<Order> buyOrderList = new LinkedList<Order>();
    private volatile LinkedList<Order> sellOrderList = new LinkedList<Order>();
    private HashMap<String, LinkedList<Order>> priceLevelBuyOrders = new HashMap<String, LinkedList<Order>>();
    private HashMap<String, LinkedList<Order>> priceLevelSellOrders = new HashMap<String, LinkedList<Order>>();

    public String getInstrument() {
        return instrument;
    }

    private String instrument;
    //private OrderBook orderBook;

    public OrderBook(String instrument) {
        this.instrument = instrument;
        priceLevelBuyOrders.put("0-100", buyOrderList);
        priceLevelBuyOrders.put("101-1000", buyOrderList);
        priceLevelBuyOrders.put("1001-5000", buyOrderList);
        priceLevelBuyOrders.put("5000+", buyOrderList);

        priceLevelSellOrders.put("0-100", buyOrderList);
        priceLevelSellOrders.put("101-1000", buyOrderList);
        priceLevelSellOrders.put("1001-5000", buyOrderList);
        priceLevelSellOrders.put("5000+", buyOrderList);
    }

    public void addOrder(Order order) {

        if(order.getInstrument().equalsIgnoreCase((this.getInstrument()))) {
            if(order.getSide().equals(Side.sell))
                sellOrderList.add(order);
            else
                buyOrderList.add(order);
        }
        else {
            System.out.println("Incorrect instrument - order was not added!");
        }


    }

    public void modifyOrder(Side side, String orderId, long newQuantity) {


        Iterator<Order> orderIterator = null;
        //System.out.println(buyOrderList.contains(orderId));

        if(side.equals(Side.buy))
            orderIterator = buyOrderList.iterator();
        else if (side.equals(Side.sell))
            orderIterator = sellOrderList.iterator();

        while (orderIterator.hasNext()) {
            Order order = orderIterator.next();
            long oldQuantity = order.getQuantity();
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                //System.out.println(orderList.indexOf(order));
                order.setQuantity(newQuantity);
                //orderList.remove(orderList.indexOf(order));
                System.out.println("Order " + order.getOrderId() + "'s quantity changed from " + oldQuantity + " to " + order.getQuantity());

                if (newQuantity > oldQuantity) {
                    this.moveOrderToEnd(order);
                    break;
                }
                else
                    continue;
            }

        }
    }

    public void deleteOrder(Side side, String orderId) {

        Iterator<Order> orderIterator = null;

        if(side.equals(Side.buy))
            orderIterator = buyOrderList.iterator();
        else if (side.equals(Side.sell))
            orderIterator = sellOrderList.iterator();

        while (orderIterator.hasNext()) {
            Order order = orderIterator.next();
                if (order.getOrderId().equalsIgnoreCase(orderId)) {
                    //System.out.println(orderList.indexOf(order));
                    orderIterator.remove();
                    //orderList.remove(orderList.indexOf(order));
                    System.out.println("Order " + order.getOrderId() + " successfully removed!");

                }

        }
    }

    public void printOrders() {
        System.out.println("List of Buy " + this.getInstrument() + " Orders:\n");
        for (Order order: buyOrderList) {
            System.out.println(order.toString());
        }

        System.out.println("List of Sell " + this.getInstrument() + " Orders:\n");
        for (Order order: sellOrderList) {
            System.out.println(order.toString());
        }

    }

    public synchronized void moveOrderToEnd(Order order) {

        if(order.getSide().equals(Side.buy)) {
            buyOrderList.remove(order);
            buyOrderList.addLast(order);
        }
        else {
            sellOrderList.remove(order);
            sellOrderList.addLast(order);
        }

       // order = orderIterator.next();
//        if (order.getOrderId().equalsIgnoreCase(orderId))
//            //System.out.println(orderList.indexOf(order));
//            orderIterator.remove();
        //orderList.remove(orderList.indexOf(order));
        System.out.println("Order " + order.getOrderId() + " moved to the end of the queue!");
    }

    public long getBestPrice(String instrument, Side side) {

        long bestPrice = 0;

        //if(side.equals(Side.buy))


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

    public LinkedList<Order> getBuyOrdersList(String instrument) {
        return buyOrderList;
    }

    public LinkedList<Order> getSellOrdersList(String instrument) {
        return sellOrderList;
    }
}
