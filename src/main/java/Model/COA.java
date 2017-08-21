package Model;

import javax.persistence.*;

/**
 * @author Ramon Johnson
 * @version 0.0.0.1
 * 8/17/2017
 */
@Entity
public class COA
{
    @Id
    @Column(unique = true)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COAOrder_Order")
    private Order order;

    @Column(name = "OS")
    private String operatingSystem;

    @ManyToOne
    @JoinColumn(name = "operatorID")
    private Operator operatorID;

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
