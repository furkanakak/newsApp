package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends  RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {
    private Context mContext;
    private List<model> modelList;
    private List<model> modelListFull;


    public Adapter(Context mContext, List<model> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
        modelListFull = new ArrayList<>(modelList);
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView pubDate;
        public ImageView image;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_view_title);
            pubDate = itemView.findViewById(R.id.card_view_pubDate);
            image = itemView.findViewById(R.id.card_view_image);
            cardView = itemView.findViewById(R.id.CardView);

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adapter.ViewHolder holder, int position) {
        String mUrl = modelList.get(position).getLink();
        String mtitles = modelList.get(position).getTitle();
        String mpubDate = modelList.get(position).getPubDate();
        String imgUrl = modelList.get(position).getImage();
        Picasso.get().load(imgUrl).into(holder.image);
        holder.title.setText(mtitles);
        holder.pubDate.setText(mpubDate);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,newsContentActivity.class);
                intent.putExtra("url",mUrl);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<model> filteredList = new ArrayList<>();
            if (constraint == null && constraint.length()==0)
            {
                 filteredList.addAll(modelListFull);
            }
            else
            {
                String filterParrent = constraint.toString().toLowerCase().trim();
                for (model mmodel :modelListFull)
                {
                    if (mmodel.getTitle().toLowerCase().contains(filterParrent))
                    {
                        filteredList.add(mmodel);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
          modelList.clear();
          modelList.addAll((List) results.values);
          notifyDataSetChanged();
        }
    };







}
