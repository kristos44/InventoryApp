package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract;
import com.example.android.inventoryapp.data.ProductDbHelper;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by krzysztof on 24.08.18.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final Cursor cursorFinal = cursor;

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);

        final int _id = cursor.getInt(cursor.getColumnIndex("_id"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String price = cursor.getString(cursor.getColumnIndex("price"));
        final String quantity = cursor.getString(cursor.getColumnIndex("quantity"));

        nameTextView.setText(name);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);

        Button saleButton = (Button) view.findViewById(R.id.sale_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDbHelper dbHelper = new ProductDbHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                Uri currentProductUri = ContentUris.withAppendedId(
                        ProductContract.ProductEntry.CONTENT_URI,
                        cursorFinal.getColumnIndex("_id"));
                ContentValues values = new ContentValues();

                int quantityInt = Integer.parseInt(quantity);
                int newQuantity = (quantityInt > 0 ? quantityInt - 1 : 0);

                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);

                db.update(ProductEntry.TABLE_NAME, values, "_id = ?",
                        new String[]{Integer.toString(_id)});

                context.getContentResolver().notifyChange(currentProductUri, null);
            }
        });
    }
}
