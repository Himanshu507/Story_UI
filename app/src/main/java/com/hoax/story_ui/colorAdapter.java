package com.hoax.story_ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class colorAdapter extends RecyclerView.Adapter<colorAdapter.ColorAdapterHolder> {

    private Context context;
    List<Integer> colors = new ArrayList<Integer>();

    public colorAdapter(Context context, List<Integer> colors) {
        this.context = context;
        this.colors = colors;
    }

    @NonNull
    @Override
    public ColorAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.color_items,viewGroup,false);
        return new ColorAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapterHolder colorAdapterHolder, int i) {

        Integer colorcode = colors.get(i);
        //Toast.makeText(context,colorcode.toString(),Toast.LENGTH_LONG).show();
        colorAdapterHolder.imageView.setBackgroundColor(colorcode);

    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class ColorAdapterHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;

        public ColorAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.colors_view);
        }
    }
}
