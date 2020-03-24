package ui.components;

import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import model.Account;
import java.util.Map;

public class ExpenseDistributionView extends Dialog<PieChart> {

    private Account account;
    private PieChart chart;

    public ExpenseDistributionView(Account account) {
        this.account = account;
        this.chart = new PieChart();
        this.setTitle("Expense Overview");
        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        this.getDialogPane().setContent(new VBox(8, chart));
        this.chart.setLegendSide(Side.LEFT);
        this.populateData();
    }

    public void populateData() {
        for (Map.Entry<String, Double> entry: account.getTaggedTransactionSumPair("EXPENSE").entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            chart.getData().add(slice);
        }
    }
}
