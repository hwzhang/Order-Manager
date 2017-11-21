package com.cryptofacilities.interview;

import com.sun.org.apache.xpath.internal.SourceTree;
import sun.awt.image.ImageWatched;

import java.util.*;

/**
 * Created by CF-8 on 6/27/2017.
 */
public class OrderBook implements OrderBookManager {

    /*
     ** Linked lists for orders, so that orders can be appended
     * to the end of the queue when quantity is changed
     */
    private volatile LinkedList<Order> buyOrderListSmall = new LinkedList<Order>();
    private volatile LinkedList<Order> sellOrderListSmall = new LinkedList<Order>();

    private volatile LinkedList<Order> buyOrderListMid = new LinkedList<Order>();
    private volatile LinkedList<Order> sellOrderListMid = new LinkedList<Order>();

    private volatile LinkedList<Order> buyOrderListBig = new LinkedList<Order>();
    private volatile LinkedList<Order> sellOrderListBig = new LinkedList<Order>();

    private volatile LinkedList<Order> buyOrderListLarge = new LinkedList<Order>();
    private volatile LinkedList<Order> sellOrderListLarge = new LinkedList<Order>();

    // HashMaps to store buy/sell orders for an instrument
    private HashMap<String, LinkedList<Order>> priceLevelBuyOrders = new HashMap<String, LinkedList<Order>>();
    private HashMap<String, LinkedList<Order>> priceLevelSellOrders = new HashMap<String, LinkedList<Order>>();

    //Class variable to store the number of books an order manager holds
    private static int numberOfBooks;
    private static List<String> instrumentsList = new ArrayList<String>();

    public String getInstrument() {
        return instrument;
    }

    private String instrument;

    public static int getNumberOfBooks() {
        return numberOfBooks;
    }

    public OrderBook(String instrument) {
        this.instrument = instrument;
        if (instrumentsList.contains(instrument)) {
            System.out.println("'" + instrument + "' already exists.");
            return;
        }
        else
            instrumentsList.add(instrument);

        numberOfBooks++;

        //Populate the books
        priceLevelBuyOrders.put("5000+", buyOrderListLarge);
        priceLevelBuyOrders.put("1001-5000", buyOrderListBig);
        priceLevelBuyOrders.put("101-1000", buyOrderListMid);
        priceLevelBuyOrders.put("0-100", buyOrderListSmall);

        priceLevelSellOrders.put("0-100", sellOrderListSmall);
        priceLevelSellOrders.put("101-1000", sellOrderListMid);
        priceLevelSellOrders.put("1001-5000", sellOrderListBig);
        priceLevelSellOrders.put("5000+", sellOrderListLarge);
        //Collections.sort();

    }

    public void addOrder(Order order) {

        if (order.getPrice() <= 0 || order.getQuantity() <= 0) {
            System.out.println("Price and quantity must be positive. Order " + order.getOrderId() + " has not been added to the list.");
            return;
        }

        //Check if there is already an order with the ID of the order to be added
        for (Map.Entry<String, LinkedList<Order>> iterator : priceLevelBuyOrders.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();
            Iterator it = orderList.iterator();
            while (it.hasNext()) {
                Order o = (Order) it.next();
                if (order.getOrderId().equalsIgnoreCase(o.getOrderId())) {
                    System.out.println("An order with ID " + order.getOrderId() + " already exists.");
                    return;
                }
            }
        }

        for (Map.Entry<String, LinkedList<Order>> iterator : priceLevelSellOrders.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();
            Iterator it = orderList.iterator();
            while (it.hasNext()) {
                Order o = (Order) it.next();
                if (order.getOrderId().equalsIgnoreCase(o.getOrderId())) {
                    System.out.println("An order with ID " + order.getOrderId() + " already exists.");
                    return;
                }
            }
        }

        if (order.getInstrument().equalsIgnoreCase((this.getInstrument()))) {
            if (order.getSide().equals(Side.sell))
                if (order.getPrice() >= 0 && order.getPrice() <= 100)
                    sellOrderListSmall.add(order);
                else if (order.getPrice() > 100 && order.getPrice() <= 1000)
                    sellOrderListMid.add(order);
                else if (order.getPrice() > 1000 && order.getPrice() <= 5000)
                    sellOrderListBig.add(order);
                else
                    sellOrderListLarge.add(order);
            else {
                if (order.getPrice() >= 0 && order.getPrice() <= 100)
                    buyOrderListSmall.add(order);
                else if (order.getPrice() > 100 && order.getPrice() <= 1000)
                    buyOrderListMid.add(order);
                else if (order.getPrice() > 1000 && order.getPrice() <= 5000)
                    buyOrderListBig.add(order);
                else
                    buyOrderListLarge.add(order);
            }
        }
    }

    public void modifyOrder(Side side, String orderId, long newQuantity) {

        if (newQuantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        HashMap<String, LinkedList<Order>> set;

        if (side.equals(Side.buy))
            set = priceLevelBuyOrders;
        else
            set = priceLevelSellOrders;

        for (Map.Entry<String, LinkedList<Order>> iterator : set.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();
            Iterator it = orderList.iterator();
            while (it.hasNext()) {
                Order order = (Order) it.next();
                long oldQuantity = order.getQuantity();
                if (order.getOrderId().equalsIgnoreCase(orderId)) {
                    order.setQuantity(newQuantity);
                    //orderList.remove(orderList.indexOf(order));
                    System.out.println("Order " + order.getOrderId() + "'s quantity changed from " + oldQuantity + " to " + order.getQuantity());

                    if (newQuantity > oldQuantity) {
                        this.moveOrderToEnd(order);
                        break;
                    } else
                        continue;
                }
            }
        }
    }

    public void deleteOrder(Side side, String orderId) {

        HashMap<String, LinkedList<Order>> set;

        if (side.equals(Side.buy))
            set = priceLevelBuyOrders;
        else
            set = priceLevelSellOrders;

        for (Map.Entry<String, LinkedList<Order>> iterator : set.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();
            try {
                Iterator it = orderList.iterator();
                while (it.hasNext()) {
                    Order order = (Order) it.next();
                    if (order.getOrderId().equalsIgnoreCase(orderId)) {
                        orderList.remove(orderList.indexOf(order));
                        System.out.println("Order " + order.getOrderId() + " successfully removed!");
                        break;
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void printOrders() {

        System.out.println("----------------------------------------------------");
        System.out.println("List of Buy " + this.getInstrument() + " Orders:\n");

        Set set1 = priceLevelBuyOrders.entrySet();
        Iterator iterator1 = set1.iterator();
        while(iterator1.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator1.next();
            System.out.print(me2.getKey() + ": ");
            System.out.println(me2.getValue());
        }
        System.out.println("----------------------------------------------------\n");

        System.out.println("----------------------------------------------------");
        System.out.println("List of Sell " + this.getInstrument() + " Orders:\n");

        //Sort sell orders so that they are listed from lowest to highest price level
        Map<String, LinkedList<Order>> map = new TreeMap<String, LinkedList<Order>>(priceLevelSellOrders);
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator2.next();
            System.out.print(me2.getKey() + ": ");
            System.out.println(me2.getValue());
        }
        System.out.println("----------------------------------------------------");
        System.out.println("\n\n");

    }

    public void moveOrderToEnd(Order order) {

        HashMap<String, LinkedList<Order>> set;

        if (order.getSide().equals(Side.buy))
            set = priceLevelBuyOrders;
        else
            set = priceLevelSellOrders;

        for (Map.Entry<String, LinkedList<Order>> iterator : set.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();

            Iterator it = orderList.iterator();
            while (it.hasNext()) {
                Order oo = (Order) it.next();
                if (oo.getOrderId().equalsIgnoreCase(order.getOrderId())) {
                    orderList.remove(order);
                    orderList.add(order);
                    System.out.println("Order " + order.getOrderId() + " successfully removed!");
                    break;
                }
            }
        }
        System.out.println("Order " + order.getOrderId() + " moved to the end of the queue!");
    }

    public long getBestPrice(String instrument, Side side) {

        HashMap<String, LinkedList<Order>> set;

        if (side.equals(Side.buy))
            set = priceLevelBuyOrders;
        else
            set = priceLevelSellOrders;

        //initial best price value
        long bestPrice = Long.MAX_VALUE;
        int ordersLength = 0;

        for (Map.Entry<String, LinkedList<Order>> iterator : set.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();

            ordersLength += orderList.size();

            Iterator it = orderList.iterator();
            while (it.hasNext()) {
                Order order = (Order) it.next();
                if (order.getPrice() < bestPrice) {
                    bestPrice = order.getPrice();
                }
            }
        }

        if(ordersLength == 0) {
            System.out.println("No " + side + " orders for '" + instrument + "'.");
            return -1;
        }

        System.out.println("The best " + side + " price for instrument '" + instrument + "' is " + bestPrice);

        return bestPrice;
    }

    public long getOrderNumAtLevel(String instrument, Side side, long price) {

        if (price <= 0) {
            System.out.println("Price must be positive.");
            return -1;
        }

        HashMap<String, LinkedList<Order>> set;

        if (side.equals(Side.buy))
            set = priceLevelBuyOrders;
        else
            set = priceLevelSellOrders;

        for (Map.Entry<String, LinkedList<Order>> iterator : set.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();
            String[] priceRange = iterator.getKey().split("\\D+");
            if (priceRange.length == 1) {
                if (price > Long.parseLong(priceRange[0])) {
                    System.out.println("The number of " + side + " orders at level '" + iterator.getKey() + "' is " + orderList.size());
                    if (orderList.isEmpty())
                        return -1;
                    return orderList.size();
                }
            }
            else {
                if (price >= Long.parseLong(priceRange[0]) && price <= Long.parseLong(priceRange[1])) {
                    System.out.println("The number of " + side + " orders at level '" + iterator.getKey() + "' is " + orderList.size());
                    if (orderList.isEmpty())
                        return -1;
                    return orderList.size();
                }
            }
        }

        return -1;
    }

    public long getTotalQuantityAtLevel(String instrument, Side side, long price) {

        if (price <= 0) {
            System.out.println("Price must be positive.");
            return -1;
        }

        HashMap<String, LinkedList<Order>> set;

        String priceLevel = null;

        if (side.equals(Side.buy))
            set = priceLevelBuyOrders;
        else
            set = priceLevelSellOrders;

        long quantityAtPrice = 0;

        for (Map.Entry<String, LinkedList<Order>> iterator : set.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();
            String[] priceRange = iterator.getKey().split("\\D+");
            Iterator it = orderList.iterator();
            while (it.hasNext()) {
                Order order = (Order) it.next();
                if (priceRange.length == 1) {
                    if (price > Long.parseLong(priceRange[0])) {
                        quantityAtPrice += order.getQuantity();
                        priceLevel = priceRange[0];
                    }
                }
                else {
                    if (order.getPrice() >= Long.parseLong(priceRange[0]) && price <= Long.parseLong(priceRange[1])) {
                        quantityAtPrice += order.getQuantity();
                        priceLevel = priceRange[0] + "-" + priceRange[1];
                    }
                }
            }
        }

        System.out.println("Cumulative quantity of " + side + " " + instrument + " orders at price range " + priceLevel + ": " + quantityAtPrice);

        if (quantityAtPrice == 0)
            return -1;

        return quantityAtPrice;
    }

    public long getTotalVolumeAtLevel(String instrument, Side side, long price) {

        if (price <= 0) {
            System.out.println("Price must be positive.");
            return -1;
        }


        String priceLevel = null;
        HashMap<String, LinkedList<Order>> set;

        if (side.equals(Side.buy))
            set = priceLevelBuyOrders;
        else
            set = priceLevelSellOrders;

        long quantityAtPrice = 0;
        long volumeAtPrice;

        for (Map.Entry<String, LinkedList<Order>> iterator : set.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();
            String[] priceRange = iterator.getKey().split("\\D+");
            Iterator it = orderList.iterator();
            while (it.hasNext()) {
                Order order = (Order) it.next();
                if (priceRange.length == 1) {
                    if (price > Long.parseLong(priceRange[0])) {
                        quantityAtPrice += order.getQuantity();
                        priceLevel = priceRange[0];
                    }
                }
                else {
                    if (order.getPrice() >= Long.parseLong(priceRange[0]) && price <= Long.parseLong(priceRange[1])) {
                        quantityAtPrice += order.getQuantity();
                        priceLevel = priceRange[0] + "-" + priceRange[1];
                    }
                }
            }
        }

        volumeAtPrice = price * quantityAtPrice;

        System.out.println("Cumulative volume of " + side + " " + instrument + " orders at price range of " + priceLevel + ": " + volumeAtPrice);

        if (volumeAtPrice == 0)
            return -1;

        return volumeAtPrice;
    }

    public LinkedList<Order> getOrdersAtLevel(String instrument, Side side, long price) {

        if (price <= 0) {
            System.out.println("Price must be positive.");
            return null;
        }

        HashMap<String, LinkedList<Order>> set;

        if (side.equals(Side.buy))
            set = priceLevelBuyOrders;
        else
            set = priceLevelSellOrders;

        LinkedList<Order> samePriceOrders = new LinkedList<Order>();

        for (Map.Entry<String, LinkedList<Order>> iterator : set.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();
            Iterator it = orderList.iterator();
            while (it.hasNext()) {
                Order order = (Order) it.next();
                if (order.getPrice() == price) {
                    samePriceOrders.add(order);
                }
            }
        }
        for(Order o: samePriceOrders)
            System.out.println(o);

        return samePriceOrders;
    }

    public LinkedList<Order> getOrdersList(String instrument, Side side) {

        LinkedList<Order> orders = new LinkedList<Order>();
        HashMap<String, LinkedList<Order>> set;

        if (side.equals(Side.buy))
            set = priceLevelBuyOrders;
        else
            set = priceLevelSellOrders;

        for (Map.Entry<String, LinkedList<Order>> iterator : set.entrySet()) {
            LinkedList<Order> orderList = iterator.getValue();
            Iterator it = orderList.iterator();
            while (it.hasNext()) {
                Order order = (Order) it.next();
                orders.add(order);
            }
        }
        for(Order o: orders)
            System.out.println(o);

        return orders;
    }
}