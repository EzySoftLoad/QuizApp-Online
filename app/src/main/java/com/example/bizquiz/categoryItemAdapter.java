package com.example.bizquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class categoryItemAdapter  extends RecyclerView.Adapter<categoryItemAdapter.ViewHolder> {


    private List<CategoryItemModel> categoryItemModelList;
    private Context context;

    public categoryItemAdapter(List<CategoryItemModel> categoryItemModelList) {
        this.categoryItemModelList = categoryItemModelList;

    }

    @NonNull
    @Override
    public categoryItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryItemAdapter.ViewHolder holder, int position) {
        holder.setData(categoryItemModelList.get(position).getUrl(),categoryItemModelList.get(position).getName(),position);

    }

    @Override
    public int getItemCount() {
        return categoryItemModelList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView imageView;
        private TextView title;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            title =itemView.findViewById(R.id.item_title);
        }
        private void setData(final String url, final String title, final int position){

            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.title.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setIntent = new Intent(itemView.getContext(),SetsActivity.class);
                    setIntent.putExtra("title",title);
                    setIntent.putExtra("position",position);
                  //  setIntent.putExtra("url",url);
                    //setIntent.putExtra("image", (CharSequence) imageView);
                    itemView.getContext().startActivity(setIntent);
                }
            });
        }

    }
}
