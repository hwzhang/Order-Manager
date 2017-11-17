package com.cryptofacilities.interview;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by CF-8 on 6/27/2017.
 */
public interface OrderBookManager {

    /**
     * Add new order
     *
     * Orders for the same instrument, on the same side, with the same price should be kept in the order as they arrive
     *
     * @param order new order to add <br/>
     *
     * @see Order
     */
    void addOrder( Order order );

    /**
     * Modify existing order.
     *
     * If quantity increases, the order should be put at the end of the queue of orders with the same price
     * If quantity decreases, the order should maintain its position in the queue or orders with the same price
     *
     * @param side the type of order (buy/sell)
     * @param orderId unique identifier of existing order to modify
     * @param newQuantity new quantity for the order, NOT a delta from previous quantity, always positive
     */
    void modifyOrder( Side side, String orderId, long newQuantity );

    /**
     * Delete existyng order
     *
     * @param side the order type(buy/sell)
     * @param orderId unique identifier of existing order
     */
    void deleteOrder( Side side, String orderId );

    /**
     * Print all orders
     */
    void printOrders();

    /**
     * Move order to the end of the queue after modification (quantity reduction)
     *
     * @param order the order to be moved at the tail of the queue
     */
    void moveOrderToEnd( Order order );

    /**
     * Get the best price for the instrument and side.
     *
     * For buy orders - the highest price
     * For sell orders - the lowest price
     *
     * @param instrument identifier of an instrument
     * @param side either buy or sell
     * @return the best price, or -1 if there're no orders for the instrument on this side
     */
    long getBestPrice( String instrument, Side side );

    /**
     * Get total number of orders for the instrument on given side with given price
     *
     * @param instrument identifier of an instrument
     * @param side either buy or sell
     * @param price requested price level
     * @return total number of orders, or -1 if there're no orders for the instrument on this side with this price
     */
    long getOrderNumAtLevel( String instrument, Side side, long price );

    /**
     * Get cumulative quantity of all orders for the instrument on given side with given price
     *
     * @param instrument identifier of an instrument
     * @param side either buy or sell
     * @param price requested price level
     * @return total quantity, or -1 if there're no orders for the instrument on this side with this price
     */
    long getTotalQuantityAtLevel( String instrument, Side side, long price );

    /**
     * Get cumulative volume ( sum of price * quantity ) of all orders for the instrument on given side with given price
     *
     * @param instrument identifier of an instrument
     * @param side either buy or sell
     * @param price requested price level
     * @return total volume, or -1 if there're no orders for the instrument on this side with this price
     */
    long getTotalVolumeAtLevel( String instrument, Side side, long price );

    /**
     * Get all orders for the instrument on given side with given price
     *
     * Result should contain orders in the same order as they arrive,
     * but also see {@link #modifyOrder( String , long )} for exception
     *
     * @param instrument identifier of an instrument
     * @param side either buy or sell
     * @param price requested price level
     * @return all orders, or empty list if there're no orders for the instrument on this side with this price
     */
    List<Order> getOrdersAtLevel(String instrument, Side side, long price );

    LinkedList<Order> getBuyOrdersList( String instrument );

    LinkedList<Order> getSellOrdersList(String instrument );


}
