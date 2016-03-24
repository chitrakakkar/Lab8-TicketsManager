package com.Chitra;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

public class TicketManager
{
    private  static LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();
    private static LinkedList<Ticket> ticketResolved = new LinkedList<Ticket>();

    private static Scanner scan = new Scanner(System.in);
    private static PrintWriter outFile;


    public static void main(String[] args) throws IOException
    {


        //Ask for some ticket info, create tickets, store in ticketQueue
        //ReadingIntoAFile(); -> still need to work on this..

        while(true)
        {

            System.out.println("1.Enter Ticket\n2.Delete by Id\n3.Delete byIssue\n4.SearchByName\n5.Display All Tickets\n6.Quit");
            int task = Integer.parseInt(scan.nextLine());

            if (task == 1) {
                //Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue);

            }
            //added as per the question
            else if (task == 2) {
                //delete a ticket
                deleteTicketByID(ticketQueue);
                System.out.println(ticketResolved);


            }
            //deleting by issue
            else if(task ==3)
            {
                DeleteByIssue(ticketQueue);
                System.out.println(ticketResolved);
            }
            else if(task ==4)
            {
                SearchByName(ticketQueue);
            }
            //prints all the tickets
            else if ( task == 5 )
            {
                printAllTickets(ticketQueue);
                //this will happen for 3 or any other selection that is a valid int
                //TODO Program crashes if you enter anything else - please fix
                //Default will be print all tickets


            }
            //quit program and also write info to the file
            else if(task ==6)

            {
                //Quit. Future prototype may want to save all tickets to a file
                WriteIntoAFile(ticketQueue,ticketResolved);
                System.out.println("Quitting program");
                break;

            }
            //catch all
            else
            {

                System.out.println("Please enter a valid option ");
                System.out.println(" choose from the given menu below !!!");
            }
        }

        scan.close();

    }
    //********************DeleteTicketByID Method*****************************
    //Problem 2-Delete a ticket by Id Number
    protected static void deleteTicketByID(LinkedList<Ticket> ticketQueue) throws IOException
    {
        printAllTickets(ticketQueue);   //display list for user

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }
        //Scanner deleteScanner = new Scanner(System.in);

        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = true;
        //Validation for correct Id info
        while ((found))
        {
            int deleteID = GetDeletedID(); // validation for user input for Id
            for (Ticket ticket : ticketQueue) {
                if (ticket.getTicketID() == deleteID) {
                    found = false;
                    //ticketQueue.remove(ticket);
                    resolvedTicket(ticket,ticketResolved);
                    System.out.println(String.format("Ticket %d deleted", deleteID));
                    break; //don't need loop any more.
                }

            }
            if(found)
            {
                System.out.println("Ticket ID not found, no ticket deleted");
                printAllTickets(ticketQueue);
            }



        }
    }
///****************Validation for Input from user for  Ticket Id to be deleted.*******
    public static Integer GetDeletedID()
    {

        int DeletedId=0;
        while (true)
        {
            try
            {
                Scanner deleteScanner = new Scanner(System.in);
                System.out.println("Enter ID of ticket to delete");
                DeletedId = deleteScanner.nextInt();
            }
            catch (InputMismatchException ime)
            {
                System.out.println("Please enter an Integer");
                continue;
            }
            break;
        }
        return DeletedId;

    }



    //*************************Add tickets***************************


    //Move the adding ticket code to a method
    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);

        boolean moreProblems = true;
        String description;
        String reporter;
        //let's assume all tickets are created today, for testing. We can change this later if needed
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;
        int TicketId;

        while (moreProblems)
        {

            System.out.println("Enter problem");
            description = sc.nextLine();
            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            System.out.println("Enter priority of " + description);
            priority = Integer.parseInt(sc.nextLine());
            TicketId = GetTicketID();
            //creating ticket objects
            Ticket t = new Ticket(description, priority, reporter, dateReported,TicketId);
            ticketQueue.add(t);

            //To test, let's print out all of the currently stored tickets
            printAllTickets(ticketQueue);

            System.out.println("More tickets to add?");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }

    }
    //Validation for ticketId input by the user for deletion
//***************Git TicketId Validation****************
    public static Integer GetTicketID()
    {

        System.out.println("Enter the ticketId");
        Integer Id = 0;
        while (true)
        {
            try
            {
                Scanner sc = new Scanner(System.in);
                Id = sc.nextInt();


            } catch (InputMismatchException ime)
            {
                System.out.println("Input mismatch !!! Please enter an integer");
                continue;
            }
            break;
        }
        //sc.close();
        return Id;
    }
    //********************Add ticketPriority order************************
    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking at

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }
//*************************Search by Issue************************************
    //gets the ticket queue to look for string in every ticket inside the list
    private static LinkedList<Ticket> SearchByName(LinkedList<Ticket>TicketList) {
        LinkedList<Ticket> Results = new LinkedList<Ticket>();
        if (TicketList.isEmpty())
        {
            System.out.println("No list to search in, try again");
        }
        else
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Name/String to search ");
            String description = scanner.nextLine();
            for (Ticket ticket : TicketList
                    )
            {
                if(ticket.getReporter().contains(description))
                {
                    Results.add(ticket);
                }
                else if(ticket.getDescription().contains(description))
                {
                    Results.add(ticket);
                }
            }

                System.out.println(Arrays.asList(Results));


        }
        return Results;
    }


//******************DeleteByIssue*******************************
    // gets the ticket list again to search for string/name to delete by ID
    public static void DeleteByIssue(LinkedList<Ticket>TicketList) throws  IOException
    {
        LinkedList<Ticket> Issues = SearchByName(TicketList);
        deleteTicketByID(Issues);

    }
    // a user-defined method to find the resolved tickets
    public static void resolvedTicket(Ticket T,LinkedList<Ticket> TicketQueue) throws IOException
    {
        // every resolved ticket has a resolution date

        Date date = new Date();
        T.setResolveDate(date);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a resolution for a ticket");
        String resolution = scanner.nextLine();
        T.setResolution(resolution);
        ticketResolved.add(T);
        ticketQueue.remove(T);
        FileWriter out = new FileWriter("Resolution.txt");
        for (Ticket T1:TicketQueue
             ) {
            out.write("The resolution for Ticket \n " + T1.ticketID + " is " + T1.getResolution() + "\n And The date when it got resolved is  " + T1.getResolveDate());
        }

        out.close();
    }

    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");

        for (Ticket t : tickets ) {
            System.out.println(t); //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");


    }

    public static void WriteIntoAFile(LinkedList<Ticket> TicketQueue,LinkedList<Ticket>ticketResolved) throws IOException
    {
        // stack -over flow to get the date and time in file name///
        //http://stackoverflow.com/questions/5242433/create-file-name-using-data-and-time
        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
        File file = new File("Resolved tickets" + dateFormat.format(date) + ".txt") ;
        File file2 = new File("Open Ticket.txt") ;
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2));
        for (Ticket ticket:ticketResolved
             ) {
            out.write(ticket.toString()+ "\n");

        }
        for (Ticket ticket1:TicketQueue
             ) {
            out2.write(ticket1.toString()+ "\n");
        }
        out.close();
        out2.close();
    }

//**************Need to work on formatting date from the open tickets****
    // This reads into a file and extracts data to create ticket objects when program runs
    public static void ReadingIntoAFile() throws IOException
    {
        LinkedList<String> TicketList = new LinkedList<>();
        String description=" ";
        Integer Priority =0;
        String Reporter = "";
        String myTextDate = "01/12/2006";
        Date ReportDate = new Date(myTextDate);
        Integer TicketId = 0;


        FileReader reader = new FileReader("Open Ticket.txt");
        BufferedReader bufReader = new BufferedReader(reader);
        String line = bufReader.readLine();
        while (line != null)
        {
            TicketList.add(line);
            line = bufReader.readLine();
        }
        for (String st:TicketList
             ) {
            try{
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

            description = st.split(":")[0];
            Priority =  Integer.parseInt(st.split(":")[1]);
            Reporter = st.split(":")[2];
                ReportDate =formatter.parse(st.split(":")[3]);
           TicketId = Integer.parseInt(st.split(":")[4]);

        } //for date parsing, it was asking for an exception
            catch (ParseException e)
            {
            e.printStackTrace();
            }
        }
        Ticket t = new Ticket(description,Priority,Reporter,ReportDate,TicketId);
        ticketQueue.add(t);
        reader.close();
        bufReader.close();
    }
    }


