package bwie.com.recyclerviewitemitemdome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<String> list;
    private HomeAdapter homeAdapter;

    private Button addItem;
    private Button removeItem;
    private Button change_listView;
    private Button change_gridView;
    private Button change_waterfall;

    private boolean isFirstView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        initData();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_demo);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        homeAdapter = new HomeAdapter(this, list);
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "点击了Item" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "长按了Item" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    public void findViewById() {
        addItem = (Button) findViewById(R.id.addItem);
        removeItem = (Button) findViewById(R.id.removeItem);
        change_listView = (Button) findViewById(R.id.change_listView);
        change_gridView = (Button) findViewById(R.id.change_gridView);
        change_waterfall = (Button) findViewById(R.id.change_waterfall);

        addItem.setOnClickListener(this);
        removeItem.setOnClickListener(this);
        change_listView.setOnClickListener(this);
        change_gridView.setOnClickListener(this);
        change_waterfall.setOnClickListener(this);
    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 'A'; i <= 'Z'; ++i) {
            list.add("" + (char) i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addItem:
                homeAdapter.addItem(1);
                break;
            case R.id.removeItem:
                homeAdapter.removeItem(1);
                break;
            case R.id.change_listView:
                /**
                 * ListView的效果
                 */
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
                if (isFirstView) {
                    isFirstView = false;
                    onClick(findViewById(R.id.recyclerView_demo));
                }
                break;
            case R.id.change_gridView:
                /**
                 * GridView的效果
                 */
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.addItemDecoration(new RecyclerViewDivider(this, GridLayoutManager.HORIZONTAL));
                break;
            case R.id.change_waterfall:
                /**
                 * 瀑布流的效果
                 */
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                recyclerView.addItemDecoration(new RecyclerViewDivider(this, GridLayoutManager.VERTICAL));
                break;
            default:
                recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
                break;
        }
    }
}
