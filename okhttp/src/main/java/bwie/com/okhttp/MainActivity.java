package bwie.com.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclrview;
    private MyAdapter myAdapter;
    private String path = "http://news-at.zhihu.com/api/4/news/latest";
    private List<Bean.TopStoriesBean> mTop_stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOkHttp();
    }

    public void setRecyclerView() {
        recyclrview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclrview.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(MainActivity.this, mTop_stories);
        recyclrview.setAdapter(myAdapter);
        myAdapter.setOnCilckTitle(new MyAdapter.OnCilckTitle() {
            @Override
            public void setOnCilckItem(List<Bean.TopStoriesBean> mTop_stories, int pos) {
                Toast.makeText(MainActivity.this, mTop_stories.get(pos).title, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setOkHttp() {
        OkHttp.getAsync(path, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                Bean bean = gson.fromJson(result, Bean.class);
                mTop_stories = bean.top_stories;
                setRecyclerView();
            }
        });
    }
}
