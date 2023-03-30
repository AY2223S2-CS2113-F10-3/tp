package seedu.meal360;

import java.time.LocalDate;

public class Ingredient {
    private static final Parser parser = new Parser();
    public String ingredientName;
    public Integer ingredientCount;
    public LocalDate expiryDate;
    public Boolean expired;

    public Ingredient(String ingredientName, Integer ingredientCount, String expiryDate) {
        this.ingredientName = ingredientName;
        this.ingredientCount = ingredientCount;
        // parse expiry date using parseDate method
        this.expiryDate = parser.parseDate(expiryDate);
        this.updateExpired();
    }

    public void updateExpired() {
        if (expiryDate.isBefore(LocalDate.now())) {
            expired = true;
        } else {
            expired = false;
        }
    }
}
