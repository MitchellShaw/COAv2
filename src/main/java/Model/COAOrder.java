package Model;

import org.hibernate.annotations.Columns;

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
public class COAOrder
{
    /**
     * Order number to uniquely identify the order
     */
    @Id
    private int orderNumber;
    /**
     * Variable to hold the scheduled ship date
     */
    @Column(name = "ssd")
    private LocalDate scheduleShipDate;
    /**
     * List of COA's that belong to this order
     */
    @OneToMany(mappedBy = "coaOrder")
    private List<COA> coaList;
    /**
     * A count of how many units belong to this order
     */
    @Column(name = "count")
    private int numberOfUnits;

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
    public LocalDate getScheduleShipDate()
    {
        return scheduleShipDate;
    }

    /**
     * Setter for property 'scheduleShipDate'.
     *
     * @param scheduleShipDate Value to set for property 'scheduleShipDate'.
     */
    public void setScheduleShipDate(LocalDate scheduleShipDate)
    {
        this.scheduleShipDate = scheduleShipDate;
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
     * @return Returns true if successful, false if it's not
     */
    public boolean addCOA(COA _coa)
    {
        if(coaList == null)
            coaList = new ArrayList<>();
        coaList.add(_coa);
        return true;
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
    public int getNumberOfUnits()
    {
        return numberOfUnits;
    }

    /**
     * Setter for property 'numberOfUnits'.
     *
     * @param numberOfUnits Value to set for property 'numberOfUnits'.
     */
    public void setNumberOfUnits(int numberOfUnits)
    {
        this.numberOfUnits = numberOfUnits;
    }

    @Override
    public String toString()
    {
        return "COAOrder{" +
                "orderNumber=" + orderNumber +
                ", scheduleShipDate=" + scheduleShipDate +
                ", coaList=" + coaList +
                ", numberOfUnits=" + numberOfUnits +
                ", isFinished=" + isFinished +
                '}';
    }
}
