package Model;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ramon Johnson
 * @version 0.0.0.1
 * 8/17/2017
 */
@Entity
@Table(name = "ERPOrder")
public class Order
{
    /**
     * Order number to uniquely identify the order
     */
    @Id
    @Column(unique = true)
    private int orderNumber;

    @Transient
    private SimpleStringProperty orderNumberProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty quantityProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty quantityRemainingProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty scheduleShipDateProperty = new SimpleStringProperty();
    @Transient
    private int completed = 0;

    /**
     * Variable to hold the scheduled ship date
     */
    @Column(name = "ssd")
    private LocalDate scheduledShipDate;

    /**
     * List of COA's that belong to the order
     */
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<COA> coaList;


    /**
     * List of Units that belongs to the order
     */
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<Unit> unitList;

    /**
     * A count of how many units belong to this order
     */
    @Column(name = "quantity")
    private int quantity;

    /**
     * Boolean variable to be used to determine if the order is complete
     */
    @Column(name = "completed")
    private boolean isFinished;

    /**
     * Getter for property 'finished'.
     *
     * @return Value for property 'finished'.
     */
    public boolean isFinished()
    {
        return isFinished;
    }

    /**
     * Setter for property 'finished'.
     *
     * @param finished Value to set for property 'finished'.
     */
    public void setFinished(boolean finished)
    {
        isFinished = finished;
    }

    /**
     * Getter for property 'orderNumber'.
     *
     * @return Value for property 'orderNumber'.
     */
    public int getOrderNumber()
    {
        return orderNumber;
    }

    /**
     * Setter for property 'orderNumber'.
     *
     * @param orderNumber Value to set for property 'orderNumber'.
     */
    public void setOrderNumber(int orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    /**
     * Getter for property 'scheduleShipDate'.
     *
     * @return Value for property 'scheduleShipDate'.
     */
    public LocalDate getScheduledShipDate()
    {
        return scheduledShipDate;
    }

    /**
     * Setter for property 'scheduleShipDate'.
     *
     * @param scheduleShipDate Value to set for property 'scheduleShipDate'.
     */
    public void setScheduledShipDate(LocalDate scheduleShipDate)
    {
        this.scheduledShipDate = scheduleShipDate;
    }

    /**
     * Getter for property 'coaList'.
     *
     * @return Value for property 'coaList'.
     */
    public List<COA> getCoaList()
    {
        if(coaList == null)
            coaList = new ArrayList<>();
        return coaList;
    }

    /**
     * @param _coa COA object to be added to the list
     */
    public void addCOA(COA _coa)
    {
        if(coaList == null)
            coaList = new ArrayList<>();
        coaList.add(_coa);
    }

    public boolean removeCOA(COA _coa)
    {
        return coaList.remove(_coa);
    }

    public boolean updateCOA(COA _coa)
    {
        for (COA coa : getCoaList()) {
            if(coa.getSerialNumber().equalsIgnoreCase(_coa.getSerialNumber())) {
                coa = _coa;
                return true;
            }
        }
        return false;
    }

    /**
     * @param _unit Unit object to be added to the unitList variable
     */
    public void addUnit(Unit _unit)
    {
        if(unitList == null)
            unitList = new ArrayList<>();
        unitList.add(_unit);
    }

    public boolean removeUnit(Unit _unit)
    {
        return unitList.remove(_unit);
    }


    /**
     * Setter for property 'coaList'.
     *
     * @param coaList Value to set for property 'coaList'.
     */
    public void setCoaList(List<COA> coaList)
    {
        this.coaList = coaList;
    }

    /**
     * Getter for property 'numberOfUnits'.
     *
     * @return Value for property 'numberOfUnits'.
     */
    public int getQuantity()
    {
        return quantity;
    }

    /**
     * Setter for property 'numberOfUnits'.
     *
     * @param numberOfUnits Value to set for property 'numberOfUnits'.
     */
    public void setQuantity(int numberOfUnits)
    {
        this.quantity = numberOfUnits;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", scheduleShipDate=" + scheduledShipDate +
                ", coaList=" + coaList +
                ", numberOfUnits=" + quantity +
                ", isFinished=" + isFinished +
                ", completed=" + completed +
                '}';
    }

    /**
     * Getter for property 'orderNumberProperty'.
     *
     * @return Value for property 'orderNumberProperty'.
     */
    public String getOrderNumberProperty()
    {
        return String.valueOf(orderNumber);
    }

    /**
     * @return Returns the SimpleStringProperty of orderNumberProperty
     */
    public SimpleStringProperty orderNumberPropertyProperty()
    {
        return orderNumberProperty;
    }

    /**
     * Setter for property 'orderNumberProperty'.
     *
     * @param orderNumberProperty Value to set for property 'orderNumberProperty'.
     */
    public void setOrderNumberProperty(String orderNumberProperty)
    {
        this.orderNumberProperty.set(orderNumberProperty);
    }

    /**
     * Getter for property 'quantityProperty'.
     *
     * @return Value for property 'quantityProperty'.
     */
    public String getQuantityProperty()
    {
        return String.valueOf(quantity);
    }

    /**
     * @return Returns SimpleStringProperty of quantityProperty
     */
    public SimpleStringProperty quantityPropertyProperty()
    {
        return quantityProperty;
    }

    /**
     * Setter for property 'quantityProperty'.
     *
     * @param quantityProperty Value to set for property 'quantityProperty'.
     */
    public void setQuantityProperty(String quantityProperty)
    {
        this.quantityProperty.set(quantityProperty);
    }

    /**
     * Getter for property 'quantityRemainingProperty'.
     *
     * @return Value for property 'quantityRemainingProperty'.
     */
    public String getQuantityRemainingProperty()
    {
        return String.valueOf(quantity - completed);
    }

    /**
     * @return Returns quantityRemainingPropertyProperty
     */
    public SimpleStringProperty quantityRemainingPropertyProperty()
    {
        return quantityRemainingProperty;
    }

    /**
     * Setter for property 'quantityRemainingProperty'.
     *
     * @param quantityRemainingProperty Value to set for property 'quantityRemainingProperty'.
     */
    public void setQuantityRemainingProperty(String quantityRemainingProperty)
    {
        this.quantityRemainingProperty.set(quantityRemainingProperty);
    }

    /**
     * Getter for property 'scheduleShipDateProperty'.
     *
     * @return Value for property 'scheduleShipDateProperty'.
     */
    public String getScheduleShipDateProperty()
    {
        return scheduledShipDate.toString();
    }

    /**
     * @return Returns the SimpleStringProperty of scheduleShipDateProperty
     */
    public SimpleStringProperty scheduleShipDatePropertyProperty()
    {
        return scheduleShipDateProperty;
    }

    /**
     * Setter for property 'scheduleShipDateProperty'.
     *
     * @param scheduleShipDateProperty Value to set for property 'scheduleShipDateProperty'.
     */
    public void setScheduleShipDateProperty(String scheduleShipDateProperty)
    {
        this.scheduleShipDateProperty.set(scheduleShipDateProperty);
    }

    /**
     * Getter for property 'completed'.
     *
     * @return Value for property 'completed'.
     */
    public int getCompleted()
    {
        return completed;
    }

    /**
     * Setter for property 'completed'.
     *
     * @param completed Value to set for property 'completed'.
     */
    public void setCompleted(int completed)
    {
        this.completed = completed;
    }
}
