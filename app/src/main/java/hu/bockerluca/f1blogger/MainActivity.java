package hu.bockerluca.f1blogger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import hu.bockerluca.f1blogger.adapter.ArticleListAdapter;
import hu.bockerluca.f1blogger.model.Article;

public class MainActivity extends AppCompatActivity implements ReloadListener {

    private TextView noArticle;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = this.getSharedPreferences("F1BLOGGER", Context.MODE_PRIVATE);
         fab = findViewById(R.id.fab);
        noArticle = findViewById(R.id.noArticleTextView);
        recyclerView = findViewById(R.id.articleList);
        noArticle.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        TextView userName = findViewById(R.id.nameTextView);
        userName.setText(sharedPref.getString("USERNAME", "user"));

        loadArticles();

        fab.setOnClickListener(view -> {
            DialogActivity dialogActivity = new DialogActivity(this, this, null);
            dialogActivity.show();
        });

        startAnimation();
    }

    private void startAnimation() {
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());

        fab.startAnimation(rotate);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Sikeres kijelentkezés!",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    @SuppressLint("NotifyDataSetChanged")
    public void loadArticles(){
        List<Article> articles = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("articles")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Article article = document.toObject(Article.class);
                            article.setDocRef(document.getId());
                            articles.add(article);
                        }
                        if (articles.isEmpty()){
                            noArticle.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else{
                            noArticle.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        ArticleListAdapter articleListAdapter = new ArticleListAdapter(this, articles, this);
                        recyclerView.setAdapter(articleListAdapter);
                        articleListAdapter.notifyDataSetChanged();
                        articleListAdapter.notifyItemInserted(articles.size());
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Nem sikerült betölteni a cikkekekt!",
                                Toast.LENGTH_SHORT).show();                    }
                });
    }
}