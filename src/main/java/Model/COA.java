package Model;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;

/**
 * @author Ramon Johnson
 * 8/17/2017
 * @version 1.0.0.1
 */
@Entity
public class COA
{
    /**
     * Serial number used as primary key in the database to distinguish
     * each OA
     */
    @Id
    @Column(unique = true)
    private String serialNumber;

    /**
     * Order object that will be used to tell what order the COA was assigned to
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ERPOrder_Order")
    private Order order;

    /**
     * Variable to hold what type of COA is being used
     */
    private String partNumber;

    /**
     * Operator object to be used to determine which operator received the COA
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operatorID")
    private Operator operatorID;

    /**
     * Unit object to be used to hold the information to tell us what specific product the COA was attached to
     */
    @OneToOne(mappedBy = "coa", fetch = FetchType.EAGER)
    private Unit unit;

    @Column(name = "Time_Created")
    private LocalTime createTime;
    @Column(name = "Date_Created")
    private LocalDate createdDate;

    @Column(name = "isAssigned")
    private boolean assigned;

    /**
     * Getter for property 'operatorID'.
     *
     * @return Value for property 'operatorID'.
     */
    public Operator getOperatorID()
    {
        return operatorID;
    }

    /**
     * Setter for property 'operatorID'.
     *
     * @param operatorID Value to set for property 'operatorID'.
     */
    public void setOperatorID(Operator operatorID)
    {
        this.operatorID = operatorID;
    }

    /**
     * Getter for property 'unit'.
     *
     * @return Value for property 'unit'.
     */
    public Unit getUnit()
    {
        return unit;
    }

    /**
     * Setter for property 'unit'.
     *
     * @param unit Value to set for property 'unit'.
     */
    public void setUnit(Unit unit)
    {
        this.unit = unit;
    }

    /**
     * Getter for property 'operatingSystem'.
     *
     * @return Value for property 'operatingSystem'.
     */
    public String getPartNumber()
    {
        return partNumber;
    }

    /**
     * Setter for property 'operatingSystem'.
     *
     * @param operatingSystem Value to set for property 'operatingSystem'.
     */
    public void setPartNumber(String operatingSystem)
    {
        this.partNumber = operatingSystem;
    }

    /**
     * Getter for property 'serialNumber'.
     *
     * @return Value for property 'serialNumber'.
     */

    public String getSerialNumber()
    {
        return serialNumber;
    }

    /**
     * Setter for property 'serialNumber'.
     *
     * @param serialNumber Value to set for property 'serialNumber'.
     */
    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    /**
     * Getter for property 'coaOrder'.
     *
     * @return Value for property 'coaOrder'.
     */
    public Order getOrder()
    {
        return order;
    }

    /**
     * Setter for property 'order'.
     *
     * @param coaOrder Value to set for property 'coaOrder'.
     */
    public void setOrder(Order coaOrder)
    {
        this.order = coaOrder;
    }

    /**
     * Getter for property 'createTime'.
     *
     * @return Value for property 'createTime'.
     */
    public LocalTime getCreateTime()
    {
        return createTime;
    }

    /**
     * Setter for property 'createTime'.
     *
     * @param createTime Value to set for property 'createTime'.
     */
    public void setCreateTime(LocalTime createTime)
    {
        this.createTime = createTime;
    }

    /**
     * Getter for property 'createdDate'.
     *
     * @return Value for property 'createdDate'.
     */
    public LocalDate getCreatedDate()
    {
        return createdDate;
    }

    /**
     * Setter for property 'createdDate'.
     *
     * @param createdDate Value to set for property 'createdDate'.
     */
    public void setCreatedDate(LocalDate createdDate)
    {
        this.createdDate = createdDate;
    }

    public boolean isAssigned()
    {
        return assigned;
    }

    public void setAssigned(boolean assigned)
    {
        this.assigned = assigned;
    }

    public COA getCOAFromOrderList(String _serialNumber)
    {
        COA temp = null;
        for(Iterator<COA> iterator = order.getCoaList().iterator(); iterator.hasNext();)
        {
            COA _temp = iterator.next();
            if (_temp.getSerialNumber().equalsIgnoreCase(_serialNumber)) {
                temp = _temp;
                break;
            }
        }
        return temp;
    }

    public COA getCOAFromOperatorList(String _serialNumber)
    {
        COA temp = null;
        for(Iterator<COA> iterator = operatorID.getCoaList().iterator(); iterator.hasNext();)
        {
            COA _temp = iterator.next();
            if (_temp.getSerialNumber().equalsIgnoreCase(_serialNumber)) {
                temp = _temp;
                break;
            }
        }
        return temp;
    }


    public String getAssignedProperty()
    {
        if(assigned)
            return unit != null ? unit.getSerialNumber() : "Not Assigned";
        else
            return "Not Assigned";
    }
}