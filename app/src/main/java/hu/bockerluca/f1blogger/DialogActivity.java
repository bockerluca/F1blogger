package hu.bockerluca.f1blogger;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hu.bockerluca.f1blogger.model.Article;

public class DialogActivity extends Dialog {

    private EditText articleTitle;
    private EditText articleContent;
    private Button okButton;
    private Context context;
    private ReloadListener reloadArticles;
    private Article article;

    public DialogActivity(@NonNull Context context, ReloadListener reloadArticles, Article article) {
        super(context);
        this.context = context;
        this.reloadArticles = reloadArticles;
        if(article!= null){
            this.article = article;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        articleTitle = findViewById(R.id.articleTitleEditText);
        articleContent = findViewById(R.id.articleContentEditText);
        if(article != null){
            articleTitle.setText(article.getTitle());
            articleContent.setText(article.getContent());
        }
        okButton = findViewById(R.id.okBtn);
        Button cancelButton = findViewById(R.id.cancelBtn);

        cancelButton.setOnClickListener(view -> onBackPressed());
        okButton.setOnClickListener(view -> addNewArticle());
    }

    public void addNewArticle(){
        SharedPreferences sharedPref = context.getSharedPreferences("F1BLOGGER",Context.MODE_PRIVATE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        Article newArticle = new Article();
        newArticle.setTitle(articleTitle.getText().toString());
        newArticle.setContent(articleContent.getText().toString());
        newArticle.setUserName(sharedPref.getString("USERNAME", "noname"));
        assert currentUser != null;
        newArticle.setUserId(currentUser.getUid());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy.MMM.dd");
        newArticle.setDateTime(format.format(Calendar.getInstance().getTime()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(article == null){
            db.collection("articles")
                    .add(newArticle )
                    .addOnSuccessListener(documentReference -> {
                        reloadArticles.loadArticles();
                        Toast.makeText(getContext(), "Sikeres mentés!",
                                Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Sikertelen mentés!",
                                Toast.LENGTH_SHORT).show();
                    });
        }else{
            DocumentReference docRef = db.collection("articles").document(this.article.getDocRef());
            docRef
                    .set(newArticle)
                    .addOnSuccessListener(aVoid -> {
                        reloadArticles.loadArticles();
                        Toast.makeText(getContext(), "Sikeres mentés!",
                                Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Sikertelen mentés!",
                                Toast.LENGTH_SHORT).show();
                    });
        }
        onBackPressed();
    }
}