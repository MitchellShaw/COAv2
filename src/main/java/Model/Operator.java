package Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramon Johnson
 * 2017-08-18.
 */
@Entity
public class Operator
{
    /**
     * Variable to hold the RQSID of the operator
     */
    @Id
    @Column(unique = true, length = 25)
    private String operator;

    /**
     * First name of the operator
     */
    @Column(length = 35)
    private String firstName;

    /**
     * Last name of the operator
     */
    @Column(length = 35)
    private String lastName;

    /**
     * Will hold the list of COAs this operator has been assigned.
     */
    @OneToMany(mappedBy = "operatorID")
    private List<COA> coaList;

    /**
     * Getter for property 'rqsID'.
     *
     * @return Value for property 'rqsID'.
     */
    public String getOperator()
    {
        return operator;
    }

    /**
     * Setter for property 'rqsID'.
     *
     * @param rqsID Value to set for property 'rqsID'.
     */
    public void setOperator(String rqsID)
    {
        this.operator = rqsID;
    }

    /**
     * Getter for property 'firstName'.
     *
     * @return Value for property 'firstName'.
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Setter for property 'firstName'.
     *
     * @param firstName Value to set for property 'firstName'.
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Getter for property 'lastName'.
     *
     * @return Value for property 'lastName'.
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Setter for property 'lastName'.
     *
     * @param lastName Value to set for property 'lastName'.
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
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
     * @param _coa COA object to be added to Operators list of COA's the operator has staged
     * @return Returns true if successful
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
}
