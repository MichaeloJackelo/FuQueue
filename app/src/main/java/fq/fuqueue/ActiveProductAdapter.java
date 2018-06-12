package fq.fuqueue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.ButterKnife;

//this class make view

public class ActiveProductAdapter extends RecyclerView.Adapter{
    ArrayList<Product> items;
    Context context;
    TextView text_view_summary_price;
    public ActiveProductAdapter(ArrayList<Product> items,TextView text_view_summary_price, Context context){
        this.items = items;
        this.context = context;
        this.text_view_summary_price = text_view_summary_price;
    }
    public double summaryPrice(){
        double summary = 0;
        for (Product p : this.items)
        {
            summary+=(p.price*p.quantity);
        }

        return summary;
    }
    public void refresh_text_view_summary_price()
    {
        text_view_summary_price.setText("Summary price: " + new Double(summaryPrice()).toString()+" USD");
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.activity_row_product, parent, false);
        refresh_text_view_summary_price();
        TypefaceProvider.registerDefaultIconSets();

        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        TypefaceProvider.registerDefaultIconSets();
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
                items.get(position).quantity++;
                ProductListManager.storeActiveListProducts(items,context);
                notify_data_changed();
            }
        });
        ((ItemHolder)holder).button_minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                items.get(position).quantity--;
                if(items.get(position).quantity<1){
                    //removeElement(items.get(position).name, position,this); // there need to repair problems with context
                    items.remove(position);
                    ProductListManager.storeActiveListProducts(items,context);
                }
                else ProductListManager.storeActiveListProducts(items,context);
                notify_data_changed();
            }
        });
        ((ItemHolder)holder).button_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                items.remove(position);
                ProductListManager.storeActiveListProducts(items,context);
                notify_data_changed();

            }
        });
        Picasso.with(context).load(R.drawable.a).resize(88,88).into(((ItemHolder) holder).imageViewThumbnail);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void notify_data_changed(){
        this.notifyDataSetChanged();
        refresh_text_view_summary_price();
    }



    private class ItemHolder extends RecyclerView.ViewHolder{
        TextView product_name, product_price, product_description,product_quantity;
        BootstrapButton  button_plus, button_minus, button_delete;

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
}
