package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Ramon Johnson
 * @version 0.0.0.1
 * 8/25/2017
 */
public class OrderProperty
{
    private StringProperty orderNumber;

    private StringProperty quantity;

    private StringProperty quantityRemaining;

    private StringProperty scheduleShipDate;

    public OrderProperty()
    {
        orderNumber = new SimpleStringProperty();
        quantityRemaining = new SimpleStringProperty();
        quantity = new SimpleStringProperty();
        scheduleShipDate = new SimpleStringProperty();
    }

    public OrderProperty(Order _order, int quantityCompleted)
    {
        orderNumber = new SimpleStringProperty(String.valueOf(_order.getOrderNumber()));
        quantity = new SimpleStringProperty(String.valueOf(_order.getQuantity()));
        quantityRemaining = new SimpleStringProperty(String.valueOf(_order.getQuantity() - quantityCompleted));
        scheduleShipDate = new SimpleStringProperty(_order.getScheduledShipDate().toString());
    }


    public String getOrderNumber()
    {
        return orderNumber.get();
    }

    public StringProperty orderNumberProperty()
    {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber)
    {
        this.orderNumber.set(orderNumber);
    }

    public String getQuantity()
    {
        return quantity.get();
    }

    public StringProperty quantityProperty()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity.set(quantity);
    }

    public String getQuantityRemaining()
    {
        return quantityRemaining.get();
    }

    public StringProperty quantityRemainingProperty()
    {
        return quantityRemaining;
    }

    public void setQuantityRemaining(String quantityRemaining)
    {
        this.quantityRemaining.set(quantityRemaining);
    }

    public String getScheduleShipDate()
    {
        return scheduleShipDate.get();
    }

    public StringProperty scheduleShipDateProperty()
    {
        return scheduleShipDate;
    }

    public void setScheduleShipDate(String scheduleShipDate)
    {
        this.scheduleShipDate.set(scheduleShipDate);
    }
}
