package com.example.android.justjava;

/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        Log.v("MainActivity", "Has whipped cream " + hasWhippedCream);

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();
        Log.v("MainActivity", "Has chocolate " + hasChocolate);

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));   // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order
     *
     * @param addWhippedCream whether or not the user wants whipped cream topping
     * @param addChocolate    wheter or not the user wants chocolate topping
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;

        if (addWhippedCream) {
            basePrice += 1;
        }

        if (addChocolate) {
            basePrice += 2;
        }

        return basePrice * quantity;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, getString(R.string.no_more_than_100), Toast.LENGTH_SHORT).show();

            return;
        }

        quantity++;
        display(quantity);
    }

    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, getString(R.string.no_less_than_1), Toast.LENGTH_SHORT).show();

            return;
        }

        quantity--;
        display(quantity);
    }

    /**
     * Create summary of the order.
     *
     * @param price           of the order
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate    is whether or not the user wants whipped cream topping
     * @param name
     * @return text summary
     */
    public String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        String priceMessage = getString(R.string.order_name, name) + "\n";
        priceMessage += getString(R.string.order_whipped_cream, Boolean.toString(addWhippedCream)) + "\n";
        priceMessage += getString(R.string.order_chocolate, Boolean.toString(addChocolate)) + "\n";
        priceMessage += getString(R.string.order_quantity, Integer.toString(quantity)) + "\n";
        priceMessage += getString(R.string.order_total, Integer.toString(price)) + "\n";
        priceMessage += getString(R.string.order_thank_you);
        return priceMessage;
    }
}