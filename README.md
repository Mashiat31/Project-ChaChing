# A Personal Finance Management Application

##### Purpose:
This desktop application will allow user to manage and record their personal incomes and expenses, hence enables the ability for the application to perform simple analytics and generate financial reports.

##### Features:

- Configure separate accounts for compartmentalizing
transactions

- Record and store transaction(s) locally
- Automate recurring bills record entry
- Relate transaction(s) with *context based tags*
(e.g Groceries, Medical, Leisure)
Budget planning for selected accounts
- Generate summaries & financial reports on
 account(s) over a period of time

##### User Stories

- As a user, I want to be able to create,
 view, update and remove my transaction record(s)
 in a particular account.
- As a user, I want to be able to see balance(s)
 on one or more accounts.
- As a user, when I press checkout, I want to be able
to store my financial records on file.
- As a user, I want to be able tag each
of my transaction with one or more tags.
- As a user, I want to be able set a budget
 limit on one or more accounts and see
 warnings on my account when my expenditure
 is approaching my imposed limit.
- As a user, I want to be able to create charts
 on one or more accounts and view my monthly balance sheets graphically.

### Instructions for Grader

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


### Phase 4: Task 2

#### Implementation for **type hierarchy**, in `ui/components/ChaChingDialog.java` 
  ```
    public abstract void validateUserInput() throws DialogInputException;
  ```
  ```
    public abstract void createInputControls();
  ```
  ```
    public abstract T parseValuesFromDialogResult();
  ```
  Concrete implementations
  - `ui/components/TransactionDialog.java`
  - `ui/components/AccountDialog.java`

#### Implementation for **use of the Map interface**
  - `ui/ExpenseDistributionView.java`
  `public void populateData()`
  - `model/Account.java`
  `public Map<String, Double> getTaggedTransactionSumPair(String transactionType)`

#### Implementation for **use of a bi-directional association**

  - `ui/components/TransactionTableView.java`
  `private void editTransactionAction(ActionEvent actionEvent)`
  - `ui/Controller.java`
  `public void initialize(URL url, ResourceBundle resourceBundle)`

### Phase 4: Task 3

#### Identified places with too much coupling / poor cohesion
- `Controller` class takes up too much of the responsibility in handling UI actions with tons of adhoc ui objects creation
- `addTransaction()` and `editTransaction` in `Controller.java` presents incohesion for the use of Dialog for similar purpose, repeated declarations of textfields and radio buttons in the controller class
- tightly coupled logics in displaying pie chart for show expenses distribution in `showPieChart()`
- single use of `Dialog` class in `addAccount()` and missing `editAccount()` implementation in comparison to performing `Transaction` related operations
- Scattered listeners and event handlers in the TableView that displays`Transaction` instances in rows in `Controller` class causes poor cohesion

### Description of major changes after refactoring:

- By creating a new abstract class (`ChaChingDialog`) that extends from `Dialog`, this allows for two new subclasses (`TransactionDialog` & `AccountDialog`) to be created for taking off much of the responsiblities in `Controller` for prompting user with dialog to gather inputs

- By taking out the TableView mark up of ui from `ui/ui.fxml` and create its own individual mark up in a separate file `TransactionTable.fxml` hence implementing its class as a custom view control `ui/TransactionTableView.java`, gathering all the listeners and event handler in one single class. Centralizing table view related implementation in its own class hence decouple it from `Controller`

- Added `ExpenseDistributionView` class to handle the display of visual component PieChart, hence decoupling `Controller` from having to instantiate yet another ad hoc dialog and be responsible for populating the pie chart data. 

### UML Design Diagram

![UML Design Diagram](https://github.students.cs.ubc.ca/cpsc210-2019w-t2/project_a1g3b/raw/master/UML_Design_Diagram.png)
