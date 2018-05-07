package fq.fuqueue;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter{
    ArrayList<Product> items;
    Context context;
    public ProductAdapter(ArrayList<Product> items, Context context){
        this.items = items;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.activity_row_product, parent, false);
        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        String name = (String) items.get(position).name;
        String price = (String) "" + (items.get(position).price);
        String description = items.get(position).description;
        String quantity = (String) "" + (items.get(position).quantity);
        ((ItemHolder)holder).product_name.setText(name);
        ((ItemHolder)holder).product_price.setText(price);
        ((ItemHolder)holder).product_description.setText(description);
        ((ItemHolder)holder).product_quantity.setText(quantity);
        ((ItemHolder)holder).button_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                android.widget.Toast.makeText(context, "Clicked + at position" + position, android.widget.Toast.LENGTH_SHORT).show();
                items.get(position).quantity++;
                storeArrayProducts(items);
                notify_data_changed();
            }
        });
        ((ItemHolder)holder).button_minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                android.widget.Toast.makeText(context, "Clicked - at position" + position, android.widget.Toast.LENGTH_SHORT).show();
                items.get(position).quantity--;
                if(items.get(position).quantity<1){
                    //removeElement(items.get(position).name, position,this); // there need to repair problems with context
                    items.remove(position);
                    storeArrayProducts(items);
                }
                else storeArrayProducts(items);
                notify_data_changed();
            }
        });
        ((ItemHolder)holder).button_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                android.widget.Toast.makeText(context, "Clicked - at position" + position, android.widget.Toast.LENGTH_SHORT).show();
                items.remove(position);
                storeArrayProducts(items);
                notify_data_changed();
            }
        });
        Picasso.with(context).load(R.drawable.a).resize(200,200).into(((ItemHolder) holder).imageViewThumbnail);
    }
    public void removeElement(String selectedItem, final int position,Context context){ //there is some problem with context
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Remove " + selectedItem + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                items.remove(position);
                storeArrayProducts(items);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void notify_data_changed(){
        this.notifyDataSetChanged();
    }
    private class ItemHolder extends RecyclerView.ViewHolder{
        TextView product_name, product_price, product_description,product_quantity;
        android.widget.Button button_plus, button_minus, button_delete;
        ImageView imageViewThumbnail;
        public ItemHolder(View itemView){
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_description = itemView.findViewById(R.id.product_description);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            product_quantity = itemView.findViewById(R.id.product_quantity);
            button_plus = itemView.findViewById(R.id.button_plus);
            button_minus = itemView.findViewById(R.id.button_minus);
            button_delete = itemView.findViewById(R.id.button_delete);
        }
    }
    public void storeArrayProducts( ArrayList<Product> inArrayList)
    {
        android.content.SharedPreferences sharedPreferences =  this.context.getSharedPreferences("shared preferences", context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String json = gson.toJson(inArrayList);
        editor.putString("task list", json);
        editor.apply();
    }
}
