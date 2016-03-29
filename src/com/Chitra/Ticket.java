package com.Chitra;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by chitrakakkar on 3/21/16.
 */
public class Ticket
{
    private int priority;
    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;
    private static int staticTicketIDCounter =1;
    protected int ticketID;
    private String resolution;
    private Date ResolveDate;

    public String getReporter() {return reporter;}

    public void setReporter(String reporter) {this.reporter = reporter;}



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getResolution()
    {
        return resolution;
    }

    public void setResolution(String resolution)
    {
        this.resolution = resolution;
    }

    public Date getResolveDate() {
        return ResolveDate;
    }

    public void setResolveDate(Date resolveDate) {
        ResolveDate = resolveDate;
    }



    //We can autogenerate get and set methods if and when we need

    //A constructor would be useful

    public Ticket(String desc, int p, String rep, Date date,int TicketID) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = TicketID;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }

    //Called automatically if a Ticket object is an argument to System.out.println
    public String toString(){
        return(this.description +"->" +" Priority: " + this.priority +"->"+ " Reported by: "
                + this.reporter +"->"+ " Reported on: " + this.dateReported +"->" + " : Ticket_ID :"+ticketID);
    }
//    public String AdditionalData(Ticket T)
//    {
//        return ("The resolution for Ticket  " + T + " is " + this.getResolution() + " And The date when it got resolved is  " + T.getResolveDate());
//    }
    public Integer getTicketID()
    {
        return ticketID;
    }
    protected int getPriority()
    {
        return priority;
    }



}
