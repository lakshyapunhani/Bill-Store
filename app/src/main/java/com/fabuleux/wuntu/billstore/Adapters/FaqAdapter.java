package com.fabuleux.wuntu.billstore.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaqAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;
    String[] questions;
    String[] answers;

    public FaqAdapter()
    {}

    public FaqAdapter(String[] questions, String[] answers)
    {
        this.questions = questions;
        this.answers = answers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.question)
        TextView question;

        @BindView(R.id.answer)
        TextView answer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((ViewHolder)holder).answer.setText(answers[position]);
        ((ViewHolder)holder).question.setText(questions[position]);
    }

    @Override
    public int getItemCount() {
        return questions.length;
    }
}
