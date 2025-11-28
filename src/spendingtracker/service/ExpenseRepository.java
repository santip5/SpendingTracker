package spendingtracker.service;

import spendingtracker.model.Expense;       
import spendingtracker.model.PaymentMethod; 
import java.util.*;
import java.time.LocalDate;
import java.nio.file.*;
import java.io.IOException;

/**
 *
 * @author ElFla
 */
public class ExpenseRepository {
    private final List<Expense> expenses;
    
    public ExpenseRepository(){
        this.expenses = new ArrayList<>();
    }
    
    public ExpenseRepository(Collection<Expense> initial){
        this();
        if (initial != null){
            for (Expense e : initial) add(e);
        }
    }
    
    private static boolean inRange(LocalDate d, LocalDate from, LocalDate to) {
        return !d.isBefore(from) && !d.isAfter(to); // inclusive
    }
    
    private static double round2(double x) { return Math.round(x * 100.0) / 100.0; }
    
    public int size(){
        return expenses.size();
    }
    
    public void clear(){
        expenses.clear();
    }
    
    public Expense add(Expense e){
        Objects.requireNonNull(e, "expense");
        for (Expense x : expenses) {
            if (x.getId().equals(e.getId())) {
                throw new IllegalArgumentException("duplicate id: " + e.getId());
            }
        }
        
        expenses.add(e);
        return e;
    }
    
    public boolean removeById(String id){
        Objects.requireNonNull(id, "id");
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId().equals(id)) {
                expenses.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public List<Expense> getAll(){
        return Collections.unmodifiableList(new ArrayList<>(expenses));
    }
    
    public Optional<Expense> findById(String id){
        Objects.requireNonNull(id, "id");
        for (Expense e : expenses) {
            if (e.getId().equals(id)) return Optional.of(e);
        }
        return Optional.empty();
    }
    
    public List<Expense> findByTag(String tag){
       if (tag == null || tag.trim().isEmpty()) return List.of();
        String key = tag.trim().toLowerCase();
        List<Expense> out = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getTags().contains(key)) out.add(e);
        }
        return out;
    }
    
    public List<Expense> findByMethod(PaymentMethod method){
        Objects.requireNonNull(method, "method");
        List<Expense> out = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getMethod() == method) out.add(e);
        }
        return out;
    }
    
    public List<Expense> findByDateRange(LocalDate from, LocalDate to){
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        
        if (to.isBefore(from)) throw new IllegalArgumentException("to < from");

        List<Expense> out = new ArrayList<>();
        for (Expense e : expenses) {
            if (inRange(e.getDate(), from, to)) out.add(e);
        }
        return out;
    }
    
    public boolean updateAmount(String id, double newAmount){
        for (Expense e : expenses) {
            if (e.getId().equals(id)) {
                e.setAmount(newAmount);
                return true;
            }
        }
        return false;
    }
    
    public boolean updateTags(String id, List<String> newTags){
        for (Expense e : expenses) {
            if (e.getId().equals(id)) {
                e.setTags(newTags);   
                return true;
            }
        }
        return false;
    }
    
    public double totalInRange(LocalDate from, LocalDate to){
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (to.isBefore(from)) throw new IllegalArgumentException("to < from");

        double sum = 0.0;
        for (Expense e : expenses) {
            if (inRange(e.getDate(), from, to)) sum += e.getAmount();
        }
        return round2(sum);
    }
    
    public Map<String, Double> totalsByTagInRange(LocalDate from, LocalDate to){
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (to.isBefore(from)) throw new IllegalArgumentException("to < from");

        Map<String, Double> map = new LinkedHashMap<>();
        for (Expense e : expenses) {
            if (!inRange(e.getDate(), from, to)) continue;
            double amt = e.getAmount();
            for (String t : e.getTags()) {
                map.merge(t, amt, Double::sum);
            }
        }
        for (Map.Entry<String, Double> en : map.entrySet()) {
            en.setValue(round2(en.getValue()));
        }
        return map;
    }
    
    public void loadFrom(Path file) throws IOException {
        clear();
        for (Expense e : spendingtracker.storage.ExpenseStorage.load(file)) {
            add(e); 
        }
    }
    
    public void saveTo(Path file) throws IOException {
        spendingtracker.storage.ExpenseStorage.save(file, getAll());
    }
}
