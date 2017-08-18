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
    @JoinColumn(name = "Order")
    private COAOrder coaOrder;
    @Column(unique = true, name = "UnitSerial")
    private int unitSerialNumber;
    @Column(name = "OS")
    private String operatingSystem;
    @ManyToOne
    @JoinColumn(name = "operatorID")
    private Operator operatorID;
    @OneToOne(mappedBy = "coa")
    private Unit unit;

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
    public COAOrder getOrder()
    {
        return coaOrder;
    }

    /**
     * Setter for property 'order'.
     *
     * @param coaOrder Value to set for property 'coaOrder'.
     */
    public void setOrder(COAOrder coaOrder)
    {
        this.coaOrder = coaOrder;
    }

    /**
     * Getter for property 'unitSerialNumber'.
     *
     * @return Value for property 'unitSerialNumber'.
     */
    public int getUnitSerialNumber()
    {
        return unitSerialNumber;
    }

    /**
     * Setter for property 'unitSerialNumber'.
     *
     * @param unitSerialNumber Value to set for property 'unitSerialNumber'.
     */
    public void setUnitSerialNumber(int unitSerialNumber)
    {
        this.unitSerialNumber = unitSerialNumber;
    }

    @Override
    public String toString()
    {
        return "COA{" +
                "serialNumber='" + serialNumber + '\'' +
                ", order=" + coaOrder +
                ", unitSerialNumber=" + unitSerialNumber +
                ", operatingSystem='" + operatingSystem + '\'' +
                '}';
    }
}
