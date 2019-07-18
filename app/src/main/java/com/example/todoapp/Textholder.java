package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Textholder extends RecyclerView.Adapter<Textholder.textholder> {

List<Notes> notelist;
private Context context;
public Textholder( List<Notes> notelist ,Context context)
{    this.context=context;
     this.notelist=notelist;

}
    @NonNull
    @Override
    public textholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item,parent,false);
        return new textholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull textholder holder, int position) {
    Notes tex=notelist.get(position);

        holder.title.setText(tex.getTitle());
        holder.discription.setText(tex.getDesc());

    }

    @Override
    public int getItemCount() {
        return notelist.size();
    }

    public class textholder extends RecyclerView.ViewHolder{
        TextView title,discription;

        public textholder(@NonNull View itemView) {
            super(itemView);
          title=itemView.findViewById(R.id.title);
          discription=itemView.findViewById(R.id.discription);
                   itemView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Notes note=notelist.get(getAdapterPosition());
                           Intent i=new Intent(context, updateAnd.class);
                                   i.putExtra("id", note.id);
                                  i.putExtra("title", note.title);
                                  i.putExtra("desc", note.desc);
                                  context.startActivity(i);
                       }
                   });
        }
    }
}
