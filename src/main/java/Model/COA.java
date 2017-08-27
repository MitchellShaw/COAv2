package Model;

import javax.persistence.*;

/**
 * @author Ramon Johnson
 * @version 1.0.0.0
 * 8/17/2017
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ERPOrder_Order")
    private Order order;

    /**
     * Variable to hold what type of COA is being used
     */
    @Column(name = "OS")
    private String operatingSystem;

    /**
     * Operator object to be used to determine which operator received the COA
     */
    @ManyToOne
    @JoinColumn(name = "operatorID")
    private Operator operatorID;

    /**
     * Unit object to be used to hold the information to tell us what specific product the COA was attached to
     */
    @OneToOne(mappedBy = "coa")
    private Unit unit;

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
    public String getOperatingSystem()
    {
        return operatingSystem;
    }

    /**
     * Setter for property 'operatingSystem'.
     *
     * @param operatingSystem Value to set for property 'operatingSystem'.
     */
    public void setOperatingSystem(String operatingSystem)
    {
        this.operatingSystem = operatingSystem;
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

}
