<h1> A Personal Finance Management Application </h1>

<h5>Purpose: </h5>
This desktop application will allow user to manage and record their personal incomes and expenses, hence enables the ability for the application to perform simple analytics and generate financial reports.

<h5> Features: </h5>

<ul>
<li>
Configure separate accounts for compartmentalizing
transactions </li>
<li>
Record and store transaction(s) locally </li>
<li>
Automate recurring bills record entry </li>
<li>
Relate transaction(s) with <i> context based tags  </i>
(e.g Groceries, Medical, Leisure) </li>
<li>
Budget planning for selected accounts </li>
<li>
Generate summaries & financial reports on
 account(s) over a period of time </li>
 </ul>

<h5> User Stories </h5>

<ul>

<li> As a user, I want to be able to create,
 view, update and remove my transaction record(s)
 in a particular account. </li>
<li> As a user, I want to be able to see balance(s)
 on one or more accounts </li>
<li> As a user, when I press checkout, I want to be able
to store my financial records on file. </li>
<li> As a user, I want to be able tag each
of my transaction with one or more tags </li>
<li>
As a user, I want to be able set a budget
 limit on one or more accounts and see
 warnings on my account when my expenditure
 is approaching my imposed limit. </li>
<li> As a user, I want to be able to create charts
 on one or more accounts and view my monthly balance sheets graphically. </li>
  </ul>
  <h3>Instructions for Grader</h3>

  Upon compiling the program and execute it, the application's features/functionality can be evaluate by following performing following actions:

  - You can generate the first required event by create a new checking account by clicking on the menu bar item named Account located at the top menu bar of the application, a dropdown menu should appear and contains item titled Add Account. By clicking on it a pop up dialog will appear on screen and prompts for account information entry.

  - Upon finish entering the required entries, proceed to click on the button OK to create the specified account or Cancel to forfeit. A new item should appear on the list view with label marked as the account name specified in previous step, now you can view the particular account by clicking on that item on the list. This
  will enables you to perform various actions within this required event.

  - First, click on the Add Transaction button located on the right side of the application. A pop-up dialog will appear and prompts for transaction detail entry. Upon finish entering the required details, click the button titled OK to save the transaction record. Now a new row of transaction record should appear and be able to view on the table view of the application.

  - To edit a particular transaction, perform a right click action with your pointer device on the row of transaction you wish to modify, a contextual menu should appear, click on Edit and a pop-up dialog similar to the one you use for adding transaction should appear and allows you to perform changes to the record. You can save your changes by clicking on the OK button or cancel to forfeit.

  - To remove a particular transaction, perform a right click action with your pointer device on the row of transaction you wish to delete, a contextual menu should appear, click on Remove will proceed to remove the row from the table and the data stored in memory.

  - To view account related balance, look for the label text located on the center of the application upon choosing an account. Budget is located on the right of the label for referencing purpose.

  - You can locate my visual component by first creating one or more transaction records with the type expense in a particular account. The visual component can be displayed through clicking on the button titled "View in PieChart". A pop-up dialog will appear and a Pie Chart will show the contributions of tagged transactions group towards all expenses and reflect on the category weight on spendings.

  - You can save the state of my application by locating the menu item named File on the top of the menu bar of the application, by clicking on the menu item, a dropdown menu should appear and contains various actions regarding to  persisting the application state. Both menu items titled "Save" and "Save As" are there to help you save the application state. Both Save and Save As function would only become available when there is accounts data within the application. For the Save option, it is designed to let user save the application state without being ask for file location when they open an existing file. and if the user wish to save the file to somewhere else that they desired, Save As option will prompt user with the file browser dialog and let user chooses the saving location.

  - You can reload the state of my application by locating the menu item named File on the top of the menu bar of the application, by clicking on the menu item, a dropdown menu should appear and contains an item titled "Open".
  By clicking on it, a file browser pop-up dialog should appear and let the user locate their saved records. A handy file extension filter has been implemented to let user know only the compatible file format can be open within the application. Upon finish choosing the file specified by clicking open in the file browser, the application state will reload and refresh the user interface by showing the data being parsed from the file as Accounts and Transactions within the application.

