package spendingtracker.model;

import java.time.LocalDate;
import java.util.*;


/**
 *
 * @author ElFla
 */
public class Expense {
    private String id;
    private LocalDate date;
    private double amount;
    private PaymentMethod method;
    private List<String> tags;
    
    // Full Constructor
    public Expense(String id, LocalDate date, double amount, PaymentMethod method, List<String> tags){
        
        Objects.requireNonNull(id,"id missing");
        Objects.requireNonNull(date,"date missing");
        Objects.requireNonNull(method,"payment method missing");
        Objects.requireNonNull(tags,"tags missing");
        
        // Check if negative values are input and round to 2 decimals
        if (amount < 0){
            throw new IllegalArgumentException("amount must be >= 0");
        }
        double normalizedAmount = Math.round(amount * 100.0) / 100.0;
        
        if (tags.isEmpty()){
            throw new IllegalArgumentException("at least one tag required");
        }
        
        List<String> cleanedTags = cleanList(tags);
        
        this.id = id;
        this.date = date;
        this.amount = normalizedAmount;
        this.method = method;
        this.tags = cleanedTags;
    }
    
    //Convenience Constructor
    public Expense (LocalDate date, double amount, PaymentMethod method, List<String> tags){
        this(java.util.UUID.randomUUID().toString(), date, amount, method, tags);
    }
    
    public List<String> cleanList(List<String> list){
        //Lists used in the for loop, LinkedHashSet used to eliminate duplicates
        List<String> listToClean = new ArrayList<>(list);
        Set<String> cleanedListSet = new LinkedHashSet<>();
        
        // Cleans list (trims whitespace and converts to lowercase) and checks for any missing values
        for (int i  = 0; i < listToClean.size(); i++){
            String current = listToClean.get(i);
            if (current == null){
                throw new IllegalArgumentException("null tag included");
            }
            listToClean.set(i,current.trim().toLowerCase());
            if (listToClean.get(i).isEmpty()){
                throw new IllegalArgumentException("empty tag included");
            }
            cleanedListSet.add(listToClean.get(i));
        }
        
        List<String> cleanedTags = new ArrayList<>(cleanedListSet);
        return cleanedTags;
    }
    
    public String getId(){ return id; }
    
    public LocalDate getDate(){ return date; }
    
    public double getAmount() { return amount; }
    
    public PaymentMethod getMethod() { return method; }
    
    public List<String> getTags() { return new ArrayList<>(tags); }
    
    public void setAmount(double amount) {
        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("amount must be a finite number >= 0");
        }
        this.amount = Math.round(amount * 100.0) / 100.0;
    }
    
    public void setTags(List<String> newTags){
         if (newTags == null || newTags.isEmpty()) {
            throw new IllegalArgumentException("at least one tag required");
         }
         this.tags = cleanList(newTags);
    }
    
    public String toString(){
        return "Expense{" +
                "id='" + id + '\''+
                ", date=" + date +
                ", amount=" + String.format("%.2f",amount) +
                ", tags=" + tags +
                '}';
    }
}
