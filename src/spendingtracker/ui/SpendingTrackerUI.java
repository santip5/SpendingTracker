package spendingtracker.ui;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 *
 * @author ElFla
 */
public class SpendingTrackerUI extends javax.swing.JPanel {
    private static final Path DATA_FILE = Paths.get("expenses.csv");
    private final spendingtracker.service.ExpenseRepository repo = new spendingtracker.service.ExpenseRepository();
    
    private void refreshTable(){
        var model = (javax.swing.table.DefaultTableModel) MainTable.getModel();
        
        model.setRowCount(0);
                
        for (spendingtracker.model.Expense e : repo.getAll()){
            String date = e.getDate().toString();
            String amount = String.format("%.2f", e.getAmount());
            String method = e.getMethod().name();
            String tags = String.join(", ", e.getTags());

            model.addRow(new Object[]{date,amount,method,tags});
        }
        
        updateTotalFromTable();
    }
    
    private void clearForm(){
        DateSpinner.setValue(new Date());
        AmountField.setText("");
        TagsField.setText("");
        MethodBox.setSelectedIndex(0);
    }
    
    private void applyFilter(){
        var rs = MainTable.getRowSorter();
        var sorter = (javax.swing.table.TableRowSorter<?>) rs;
        
        java.util.Date fromDate = (java.util.Date) FromSpinner.getValue();
        java.util.Date toDate   = (java.util.Date) ToSpinner.getValue();
        
        java.time.LocalDate from = fromDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        java.time.LocalDate to   = toDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        
        String methodFilter = (String) FMethodBox.getSelectedItem();
        if (methodFilter.equalsIgnoreCase("Any")){
            methodFilter = null;
        }
        
        String tagFilterText = FTagsField.getText().trim().toLowerCase();
        java.util.Set<String> filterTags = null;
        if (!tagFilterText.isEmpty()) {
            filterTags = new java.util.LinkedHashSet<>();
            for (String part : tagFilterText.split(",")) {
                String t = part.trim().toLowerCase();
                if (!t.isEmpty()) {
                    filterTags.add(t);
                }
            }
            if (filterTags.isEmpty()) {
                filterTags = null; 
            }
        }
        
        final java.time.LocalDate fromFinal = from;
        final java.time.LocalDate toFinal = to;
        final String methodFilterFinal = methodFilter;
        final java.util.Set<String> filterTagsFinal = filterTags;
        
        javax.swing.RowFilter<javax.swing.table.TableModel,Integer> rf = 
                new javax.swing.RowFilter<javax.swing.table.TableModel, Integer>(){
                    @Override
                    public boolean include(Entry<? extends javax.swing.table.TableModel, ? extends Integer> entry){
                        String dateStr = entry.getStringValue(0);
                        String methodStr = entry.getStringValue(2);
                        String tagsStr = entry.getStringValue(3).toLowerCase();
                        
                        java.time.LocalDate rowDate = java.time.LocalDate.parse(dateStr);
                        if (rowDate.isBefore(fromFinal) || rowDate.isAfter(toFinal)) {
                            return false;
                        }

                        // method filter
                        if (methodFilterFinal != null &&
                                !methodStr.equalsIgnoreCase(methodFilterFinal)) {
                            return false;
                        }

                        // tag substring filter
                        if (filterTagsFinal != null) {
                        // parse row tags into a set
                        java.util.Set<String> rowTags = new java.util.LinkedHashSet<>();
                        for (String part : tagsStr.split(",")) {
                            String t = part.trim().toLowerCase();
                            if (!t.isEmpty()) {
                                rowTags.add(t);
                            }
                        }

                        boolean anyMatch = false;
                        for (String wanted : filterTagsFinal) {
                            if (rowTags.contains(wanted)) {
                                anyMatch = true;
                                break;
                            }
                        }

                        if (!anyMatch) {
                            return false;
                        }
                    }
                    return true;
                }
        };
        sorter.setRowFilter(rf);
        updateTotalFromTable();
    }
    
    private void updateTotalFromTable(){
        javax.swing.table.TableModel model = MainTable.getModel();
        int rowCountView = MainTable.getRowCount();
        
        double sum = 0.0;
        
        for (int viewRow = 0; viewRow < rowCountView; viewRow++){
            int modelRow = MainTable.convertRowIndexToModel(viewRow);
            
            Object value = model.getValueAt(modelRow,1);
            double amount = Double.parseDouble(value.toString());
            sum += amount;
        }
        sum = Math.round(sum * 100.0) / 100.0;
        int count = rowCountView;
        
        TotalLabel.setText(String.format("Total: $%.2f (%d items)", sum,count));
    }

    /**
     * Creates new form SpendingTrackerUI
     */
    public SpendingTrackerUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TopPanel = new javax.swing.JPanel();
        DateLabel = new javax.swing.JLabel();
        AmountLabel = new javax.swing.JLabel();
        MethodLabel = new javax.swing.JLabel();
        TagsLabel = new javax.swing.JLabel();
        AmountField = new javax.swing.JTextField();
        TagsField = new javax.swing.JTextField();
        AddButton = new javax.swing.JButton();
        MethodBox = new javax.swing.JComboBox<>();
        DateSpinner = new javax.swing.JSpinner();
        FilterPanel = new javax.swing.JPanel();
        FromLabel = new javax.swing.JLabel();
        FromSpinner = new javax.swing.JSpinner();
        ToLabel = new javax.swing.JLabel();
        ToSpinner = new javax.swing.JSpinner();
        FMethodLabel = new javax.swing.JLabel();
        FMethodBox = new javax.swing.JComboBox<>();
        FTagsLabel = new javax.swing.JLabel();
        FTagsField = new javax.swing.JTextField();
        ApplyFilterButton = new javax.swing.JButton();
        ClearFilterButton = new javax.swing.JButton();
        MiddlePanel = new javax.swing.JPanel();
        ScrollPane = new javax.swing.JScrollPane();
        MainTable = new javax.swing.JTable();
        BottomPanel = new javax.swing.JPanel();
        RemoveButton = new javax.swing.JButton();
        SaveButton = new javax.swing.JButton();
        LoadButton = new javax.swing.JButton();
        TotalLabel = new javax.swing.JLabel();

        DateLabel.setText("Date (YYYY-MM-DD)");

        AmountLabel.setText("Amount");

        MethodLabel.setText("Payment Method");

        TagsLabel.setText("Tags");

        AmountField.setColumns(5);

        TagsField.setColumns(10);
        TagsField.setToolTipText("");
        TagsField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TagsFieldActionPerformed(evt);
            }
        });

        AddButton.setText("Add");
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });

        MethodBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Debit", "Credit", "Savings" }));
        MethodBox.setToolTipText("");

        DateSpinner.setModel(new javax.swing.SpinnerDateModel());

        javax.swing.GroupLayout TopPanelLayout = new javax.swing.GroupLayout(TopPanel);
        TopPanel.setLayout(TopPanelLayout);
        TopPanelLayout.setHorizontalGroup(
            TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DateLabel)
                    .addComponent(DateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(81, 81, 81)
                .addGroup(TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AmountLabel)
                    .addComponent(AmountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(100, 100, 100)
                .addGroup(TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MethodLabel)
                    .addComponent(MethodBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(99, 99, 99)
                .addGroup(TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TopPanelLayout.createSequentialGroup()
                        .addComponent(TagsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(AddButton))
                    .addComponent(TagsLabel))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        TopPanelLayout.setVerticalGroup(
            TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopPanelLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DateLabel)
                    .addComponent(AmountLabel)
                    .addComponent(MethodLabel)
                    .addComponent(TagsLabel))
                .addGap(18, 18, 18)
                .addGroup(TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AmountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TagsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddButton)
                    .addComponent(MethodBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        FromLabel.setText("From: ");

        FromSpinner.setModel(new javax.swing.SpinnerDateModel());

        ToLabel.setText("To");

        ToSpinner.setModel(new javax.swing.SpinnerDateModel());

        FMethodLabel.setText("Method");

        FMethodBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Debit", "Credit", "Savings", "Any" }));
        FMethodBox.setToolTipText("");

        FTagsLabel.setText("Including Tags:");

        FTagsField.setColumns(10);
        FTagsField.setToolTipText("");
        FTagsField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FTagsFieldActionPerformed(evt);
            }
        });

        ApplyFilterButton.setText("Apply Filter");
        ApplyFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApplyFilterButtonActionPerformed(evt);
            }
        });

        ClearFilterButton.setText("Clear Filter");
        ClearFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearFilterButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FilterPanelLayout = new javax.swing.GroupLayout(FilterPanel);
        FilterPanel.setLayout(FilterPanelLayout);
        FilterPanelLayout.setHorizontalGroup(
            FilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(FromLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FromSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ToLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ToSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(FMethodLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FMethodBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(FTagsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(FTagsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(FilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ClearFilterButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ApplyFilterButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        FilterPanelLayout.setVerticalGroup(
            FilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FilterPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(FilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FromLabel)
                    .addComponent(FromSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToLabel)
                    .addComponent(ToSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FMethodLabel)
                    .addComponent(FMethodBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FTagsLabel)
                    .addComponent(FTagsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ApplyFilterButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ClearFilterButton)
                .addContainerGap())
        );

        MainTable.setAutoCreateRowSorter(true);
        MainTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "Amount", "Method", "Tags"
            }
        ));
        ScrollPane.setViewportView(MainTable);

        javax.swing.GroupLayout MiddlePanelLayout = new javax.swing.GroupLayout(MiddlePanel);
        MiddlePanel.setLayout(MiddlePanelLayout);
        MiddlePanelLayout.setHorizontalGroup(
            MiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MiddlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPane))
        );
        MiddlePanelLayout.setVerticalGroup(
            MiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MiddlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        RemoveButton.setText("Remove");
        RemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveButtonActionPerformed(evt);
            }
        });

        SaveButton.setText("Save");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        LoadButton.setText("Load");
        LoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadButtonActionPerformed(evt);
            }
        });

        TotalLabel.setText("Total: $0.00 (0 items)");

        javax.swing.GroupLayout BottomPanelLayout = new javax.swing.GroupLayout(BottomPanel);
        BottomPanel.setLayout(BottomPanelLayout);
        BottomPanelLayout.setHorizontalGroup(
            BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TotalLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(RemoveButton)
                .addGap(115, 115, 115)
                .addComponent(SaveButton)
                .addGap(18, 18, 18)
                .addComponent(LoadButton)
                .addGap(27, 27, 27))
        );
        BottomPanelLayout.setVerticalGroup(
            BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RemoveButton)
                    .addComponent(SaveButton)
                    .addComponent(LoadButton)
                    .addComponent(TotalLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TopPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(MiddlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(BottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(FilterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(TopPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FilterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MiddlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BottomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void RemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveButtonActionPerformed
        int row = MainTable.convertRowIndexToModel(MainTable.getSelectedRow());
        if (row == -1){
            JOptionPane.showMessageDialog(this, "Please select an expense to remove.","No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<spendingtracker.model.Expense> all = repo.getAll();
        if (row < 0 || row >= all.size()){
            return;
        }
        spendingtracker.model.Expense e = all.get(row);
        
        int choice = JOptionPane.showConfirmDialog(this,"Remove expense on " + e.getDate() + " for $" + String.format("%.2f", e.getAmount()) + "?", "Confirm Removal",JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION){
            return;
        }
        
        boolean removed = repo.removeById(e.getId());
        
        if (!removed){
            JOptionPane.showMessageDialog(this,"Could not remove the selected expense.","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        refreshTable();
    }//GEN-LAST:event_RemoveButtonActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        try {
            spendingtracker.storage.ExpenseStorage.save(DATA_FILE, repo.getAll());
            
            JOptionPane.showMessageDialog(this,"Expenses saved to " + DATA_FILE.toString(), "Save Successful",JOptionPane.INFORMATION_MESSAGE);
        } catch(IOException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error saving data: " + ex.getMessage(),"Save Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        System.out.println("Add clicked");
        String amountText = AmountField.getText().trim();
        String tagsText = TagsField.getText().trim();
        String methodName = (String) MethodBox.getSelectedItem();
        spendingtracker.model.PaymentMethod method = spendingtracker.model.PaymentMethod.valueOf(methodName.toUpperCase());
        
        if (amountText.isEmpty() || tagsText.isEmpty() || methodName == null){
            JOptionPane.showMessageDialog(this, "Fill in amount, tags, and payment method.","Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Date dateValue = (Date) DateSpinner.getValue();
        LocalDate date = dateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        double amount = Double.parseDouble(amountText);

        List<String> tags = new ArrayList<>();
        for (String part : tagsText.split(",")) {
            String t = part.trim();
            if (!t.isEmpty()) {
                tags.add(t);
            }
        }
        
        spendingtracker.model.Expense e = new spendingtracker.model.Expense(date, amount, method, tags);
        repo.add(e);
        
        refreshTable();
        clearForm();
    }//GEN-LAST:event_AddButtonActionPerformed

    private void TagsFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TagsFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TagsFieldActionPerformed

    private void LoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadButtonActionPerformed
        try {
            List<spendingtracker.model.Expense> loaded = spendingtracker.storage.ExpenseStorage.load(DATA_FILE);
            
            repo.clear();
            for (spendingtracker.model.Expense e : loaded){
                repo.add(e);
            }
            refreshTable();
            
            JOptionPane.showMessageDialog(this, "Loaded "+ loaded.size() + " expenses from " + DATA_FILE.toString(),"Load Successful",JOptionPane.INFORMATION_MESSAGE);
        } catch(NoSuchFileException ex){
            JOptionPane.showMessageDialog(this, "No saved data found","Load",JOptionPane.INFORMATION_MESSAGE);
        } catch(IOException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error loading data: " + ex.getMessage(),"Load Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_LoadButtonActionPerformed

    private void FTagsFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FTagsFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FTagsFieldActionPerformed

    private void ClearFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearFilterButtonActionPerformed
        FromSpinner.setValue(new java.util.Date());
        ToSpinner.setValue(new java.util.Date());
        FMethodBox.setSelectedIndex(0);
        FTagsField.setText("");
        javax.swing.RowSorter<? extends javax.swing.table.TableModel> rs = MainTable.getRowSorter();
        if (rs instanceof javax.swing.table.TableRowSorter) {
            ((javax.swing.table.TableRowSorter<?>) rs).setRowFilter(null);
        }
        updateTotalFromTable();
    }//GEN-LAST:event_ClearFilterButtonActionPerformed

    private void ApplyFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApplyFilterButtonActionPerformed
        applyFilter();
        updateTotalFromTable();
    }//GEN-LAST:event_ApplyFilterButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JTextField AmountField;
    private javax.swing.JLabel AmountLabel;
    private javax.swing.JButton ApplyFilterButton;
    private javax.swing.JPanel BottomPanel;
    private javax.swing.JButton ClearFilterButton;
    private javax.swing.JLabel DateLabel;
    private javax.swing.JSpinner DateSpinner;
    private javax.swing.JComboBox<String> FMethodBox;
    private javax.swing.JLabel FMethodLabel;
    private javax.swing.JTextField FTagsField;
    private javax.swing.JLabel FTagsLabel;
    private javax.swing.JPanel FilterPanel;
    private javax.swing.JLabel FromLabel;
    private javax.swing.JSpinner FromSpinner;
    private javax.swing.JButton LoadButton;
    private javax.swing.JTable MainTable;
    private javax.swing.JComboBox<String> MethodBox;
    private javax.swing.JLabel MethodLabel;
    private javax.swing.JPanel MiddlePanel;
    private javax.swing.JButton RemoveButton;
    private javax.swing.JButton SaveButton;
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JTextField TagsField;
    private javax.swing.JLabel TagsLabel;
    private javax.swing.JLabel ToLabel;
    private javax.swing.JSpinner ToSpinner;
    private javax.swing.JPanel TopPanel;
    private javax.swing.JLabel TotalLabel;
    // End of variables declaration//GEN-END:variables
}
