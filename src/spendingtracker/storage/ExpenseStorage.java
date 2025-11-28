
package spendingtracker.storage;
import spendingtracker.model.Expense;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import spendingtracker.model.PaymentMethod;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author ElFla
 */
public final class ExpenseStorage {
    private ExpenseStorage() {} 

    public static void save(Path file, List<Expense> items) throws IOException { 
        if (file == null) throw new NullPointerException("file");
        if (items == null) throw new NullPointerException("items");
        
        Path dir = file.getParent();
        
        if (dir != null) Files.createDirectories(dir);
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("# id,date,amount,method,tags\n");
        for (Expense e : items){
            String tagsJoined = String.join(";",e.getTags());
            
            sb.append(e.getId()).append(',')
                    .append(e.getDate()).append(',')
                    .append(String.format("%.2f",e.getAmount())).append(',')
                    .append(e.getMethod().name()).append(',')
                    .append(tagsJoined)
                    .append('\n');
        }
        
        String content = sb.toString();
        
        Path tmp = (dir == null ? Paths.get(file.toString() + ".tmp") : dir.resolve(file.getFileName().toString() + ".tmp"));
        
        Files.writeString(tmp,content, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        
        try{
            Files.move(tmp,file,StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch(AtomicMoveNotSupportedException ex){
            Files.move(tmp,file,StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static List<Expense> load(Path file) throws IOException {
        if (file == null) throw new NullPointerException("file");
        if (!Files.exists(file)) return List.of();
        
        List<String> lines = Files.readAllLines(file);
        List<Expense> out = new ArrayList<>();
        int skipped = 0;
        
        for (String line : lines) {
            if (line == null) { skipped++; continue; }
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

            String[] parts = trimmed.split(",", 5); 
            if (parts.length != 5) { skipped++; continue; }

            String id        = parts[0].trim();
            String dateStr   = parts[1].trim();
            String amtStr    = parts[2].trim();
            String methodStr = parts[3].trim();
            String tagsStr   = parts[4]; 

            try {
                ///Date amount and payment method
                LocalDate date = LocalDate.parse(dateStr);
                double amount  = Double.parseDouble(amtStr);
                PaymentMethod method = PaymentMethod.valueOf(methodStr);

                //Tags
                List<String> tags = new ArrayList<>();
                for (String t : tagsStr.split(";")) {
                    String c = t.trim();
                    if (!c.isEmpty()) tags.add(c);
                }
                if (tags.isEmpty()) { skipped++; continue; }

                Expense e = new Expense(id, date, amount, method, tags);
                out.add(e);
            } catch (Exception parseOrValidate) {
                skipped++;
            }
        }
        return out;
        }
}
