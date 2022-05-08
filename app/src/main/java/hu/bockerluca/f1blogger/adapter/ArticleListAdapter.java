package hu.bockerluca.f1blogger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import hu.bockerluca.f1blogger.DialogActivity;
import hu.bockerluca.f1blogger.R;
import hu.bockerluca.f1blogger.ReloadListener;
import hu.bockerluca.f1blogger.model.Article;

public class ArticleListAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<Article> articles;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ReloadListener reloadArticles;


    public ArticleListAdapter(Context context, List<Article> articles, ReloadListener reloadArticles) {
        this.context = context;
        this.articles = articles;
        this.reloadArticles = reloadArticles;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.article_item, parent, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 0, 5);
        view.setLayoutParams(params);
        viewHolder = new ArticleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Article article = articles.get(position);

            articleViewHolder.itemTitle.setText(article.getTitle());
            articleViewHolder.itemContent.setText(article.getContent());
            articleViewHolder.itemWriter.setText(article.getUserName());
            articleViewHolder.itemDate.setText(article.getDateTime());

            if (article.getUserId().equals(user.getUid())){
                articleViewHolder.edit.setVisibility(View.VISIBLE);
                articleViewHolder.delete.setVisibility(View.VISIBLE);


                articleViewHolder.edit.setOnClickListener(view -> {
                    DialogActivity dialogActivity = new DialogActivity(context, reloadArticles, article);
                    dialogActivity.show();
                });

                articleViewHolder.delete.setOnClickListener(view -> {
                    db.collection("articles").document(article.getDocRef())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Sikeres törlés!",
                                        Toast.LENGTH_SHORT).show();
                                reloadArticles.loadArticles();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Sikertelen törlés!",
                                        Toast.LENGTH_SHORT).show();
                            });
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;
        TextView itemContent;
        TextView itemWriter;
        TextView itemDate;
        ImageButton edit;
        ImageButton delete;

        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemContent = itemView.findViewById(R.id.itemContent);
            itemWriter = itemView.findViewById(R.id.itemWriter);
            itemDate = itemView.findViewById(R.id.itemDate);
            edit = itemView.findViewById(R.id.editButton);
            delete = itemView.findViewById(R.id.deleteButton);
        }
    }
}
