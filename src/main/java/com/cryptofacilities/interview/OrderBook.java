package com.cryptofacilities.interview;

import com.sun.org.apache.xpath.internal.SourceTree;
import sun.awt.image.ImageWatched;

import java.util.*;

/**
 * Created by CF-8 on 6/27/2017.
 */
public class OrderBook implements OrderBookManager {

    private volatile LinkedList<Order> buyOrderListSmall = new LinkedList<Order>();
    private volatile LinkedList<Order> sellOrderListSmall = new LinkedList<Order>();

    private volatile LinkedList<Order> buyOrderListMid = new LinkedList<Order>();
    private volatile LinkedList<Order> sellOrderListMid = new LinkedList<Order>();

    private volatile LinkedList<Order> buyOrderListBig = new LinkedList<Order>();
    private volatile LinkedList<Order> sellOrderListBig = new LinkedList<Order>();

    private volatile LinkedList<Order> buyOrderListLarge = new LinkedList<Order>();
    private volatile LinkedList<Order> sellOrderListLarge = new LinkedList<Order>();

    private HashMap<String, LinkedList<Order>> priceLevelBuyOrders = new HashMap<String, LinkedList<Order>>();
    private HashMap<String, LinkedList<Order>> priceLevelSellOrders = new HashMap<String, LinkedList<Order>>();

    public String getInstrument() {
        return instrument;
    }

    private String instrument;
    //private OrderBook orderBook;

    public OrderBook(String instrument) {
        this.instrument = instrument;

        priceLevelBuyOrders.put("5000+", buyOrderListLarge);
        priceLevelBuyOrders.put("1001-5000", buyOrderListBig);
        priceLevelBuyOrders.put("101-500", buyOrderListMid);
        priceLevelBuyOrders.put("0-100", buyOrderListSmall);

        priceLevelSellOrders.put("0-100", sellOrderListSmall);
        priceLevelSellOrders.put("101-1000", sellOrderListMid);
        priceLevelSellOrders.put("1001-5000", sellOrderListBig);
        priceLevelSellOrders.put("5000+", sellOrderListLarge);
    }

    public void addOrder(Order order) {

        if(order.getInstrument().equalsIgnoreCase((this.getInstrument()))) {
            if(order.getSide().equals(Side.sell))
                if(order.getPrice() >= 0 && order.getPrice() <= 100)
                    sellOrderListSmall.add(order);
                else if(order.getPrice() > 100 && order.getPrice() <= 1000)
                    sellOrderListMid.add(order);
                else if(order.getPrice() > 1000 && order.getPrice() <= 5000)
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
        else {
            System.out.println("Incorrect instrument - order was not added!");
        }


    }

    public void modifyOrder(Side side, String orderId, long newQuantity) {


        Iterator orderIterator = null;
        //System.out.println(buyOrderList.contains(orderId));
        //for (Order order: priceLevelBuyOrders.get())
        if(side.equals(Side.buy)) {
            for (LinkedList<Order> orderList : priceLevelBuyOrders.values()) {
                for (Order order : orderList) {
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
        }
        else if (side.equals(Side.sell)) {
            for (LinkedList<Order> orderList : priceLevelSellOrders.values()) {
                for (Order order : orderList) {
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
        }



//        while (orderIterator.hasNext()) {
//            Order order =  (Order) orderIterator.next();
//            long oldQuantity = order.getQuantity();
//            if (order.getOrderId().equalsIgnoreCase(orderId)) {
//                //System.out.println(orderList.indexOf(order));
//                order.setQuantity(newQuantity);
//                //orderList.remove(orderList.indexOf(order));
//                System.out.println("Order " + order.getOrderId() + "'s quantity changed from " + oldQuantity + " to " + order.getQuantity());
//
//                if (newQuantity > oldQuantity) {
//                    this.moveOrderToEnd(order);
//                    break;
//                }
//                else
//                    continue;
//            }
//        }
    }

    public void deleteOrder(Side side, String orderId) {

        Iterator orderIterator = null;
        Collection ordersList = null;
        if(side.equals(Side.buy)) {
            for (LinkedList<Order> orderList : priceLevelBuyOrders.values()) {
                for (Order order : orderList) {
                    if (order.getOrderId().equalsIgnoreCase(orderId)) {
                        //System.out.println(orderList.indexOf(order));
                        orderList.remove();
                        //orderList.remove(orderList.indexOf(order));
                        System.out.println("Order " + order.getOrderId() + " successfully removed!");
                    }
                }
            }
        }
            //orderIterator = priceLevelBuyOrders.values().iterator();
        else if (side.equals(Side.sell)) {
            for(LinkedList<Order> orderList: priceLevelSellOrders.values()) {
                for(Order order: orderList) {
                    if (order.getOrderId().equalsIgnoreCase(orderId)) {
                        //System.out.println(orderList.indexOf(order));
                        orderList.remove();
                        //orderList.remove(orderList.indexOf(order));
                        System.out.println("Order " + order.getOrderId() + " successfully removed!");
                    }
                }
            }
        }



//        while (orderIterator.hasNext()) {
//            Order order = (Order) orderIterator.next();
//                if (order.getOrderId().equalsIgnoreCase(orderId)) {
//                    //System.out.println(orderList.indexOf(order));
//                    orderIterator.remove();
//                    //orderList.remove(orderList.indexOf(order));
//                    System.out.println("Order " + order.getOrderId() + " successfully removed!");
//                }
//
//        }
    }

    public void printOrders() {
        System.out.println("List of Buy " + this.getInstrument() + " Orders:\n");

        System.out.println(Arrays.asList(priceLevelBuyOrders));
//        for (String priceRange: priceLevelBuyOrders.keySet()) {
//            System.out.println(pric);
//        }

        System.out.println("List of Sell " + this.getInstrument() + " Orders:\n");
        System.out.println(Arrays.asList(priceLevelSellOrders));
//        for (Order order: sellOrderList) {
//            System.out.println(order.toString());
//        }

    }

    public synchronized void moveOrderToEnd(Order order) {

//        Collection<LinkedList<Order>> orderList = priceLevelBuyOrders.values();
//        System.out.println(priceLevelSellOrders.values());
//        if(priceLevelSellOrders.values().contains(order)){
//            priceLevelSellOrders.values().remove(order);
//
//        }

        if(order.getSide().equals(Side.buy)) {
            for (LinkedList<Order> orderList : priceLevelBuyOrders.values()) {
                for (Order x: orderList) {
                    if (x.getOrderId().equalsIgnoreCase(order.getOrderId())){
                        //System.out.println(orderList.indexOf(order));
                        orderList.remove(order);
                        orderList.addLast(order);
                    }
                }
            }
        }
        else {
            for (LinkedList<Order> orderList : priceLevelSellOrders.values()) {
                for (Order x: orderList) {
                    if (x.getOrderId().equalsIgnoreCase(order.getOrderId())){
                        //System.out.println(orderList.indexOf(order));
                        orderList.remove(order);
                        orderList.addLast(order);
                    }
                }
            }
        }

//        order = orderIterator.next();
//        if (order.getOrderId().equalsIgnoreCase(orderId))
//            //System.out.println(orderList.indexOf(order));
//            orderIterator.remove();
//        orderList.remove(orderList.indexOf(order));
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
        long volume = 0;
    	if(this.getInstrument().equalsIgnoreCase(instrument)) {
    		long orderPrice = 0;
        	if(side.equals(Side.sell)) {
        		if(price >= 0 && price <= 100)
        			for(Order order : sellOrderListSmall) {
        				orderPrice +=order.getPrice();
        			}
        		 volume = orderPrice * sellOrderListSmall.size();
        	} if(side.equals(Side.buy)) {
        		
        	}
        } else {
        	volume = - 1;
        }
    	return volume; 
    }

    public List<Order> getOrdersAtLevel(String instrument, Side side, long price) {
        if(this.getInstrument().equalsIgnoreCase(instrument)){
        	if(side.equals(Side.sell))
                if(price >= 0 && price <= 100)
                    return sellOrderListSmall;
                else if(price > 100 && price <= 1000)
                    return sellOrderListMid;
                else if(price > 1000 && price <= 5000)
                    return sellOrderListBig;
                else
                    return sellOrderListLarge;
            else {
                if (price >= 0 && price <= 100)
                   return buyOrderListSmall;
                 else if (price > 100 && price <= 1000)
                   return buyOrderListMid;
                 else if (price > 1000 && price <= 5000)
                    return buyOrderListBig;
                 else
                   return buyOrderListLarge;
            }
        } else {
        	return null;
        }
    }

    public LinkedList<Order> getBuyOrdersList(String instrument) {
        return null;
    }

    public LinkedList<Order> getSellOrdersList(String instrument) {
    	LinkedList<Order> orders = new LinkedList<Order>();
    	if(this.getInstrument().equals(instrument)) {
        	if (!this.sellOrderListSmall.isEmpty()){
        		for (Order order: sellOrderListSmall) {
        			orders.add(order);
        		}
        	}
        	if(!this.sellOrderListMid.isEmpty()) {
        		for(Order order: sellOrderListMid) {
        			orders.add(order);
        		}
        	} 
        	if(!this.sellOrderListBig.isEmpty()) {
        		for(Order order: sellOrderListBig) {
        			orders.add(order);
        		}
        	}
        	return orders;
        } else {
        	return null;
        }
    }
}
