package team.seventeen.graphcalculator;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class FunctionFragment extends Fragment {
    private static final int CONTEXT_MENU_ITEM_NEW = 1;
    private static final int CONTEXT_MENU_ITEM_DELETE = CONTEXT_MENU_ITEM_NEW+1;
    private static final int CONTEXT_MENU_ITEM_SHOW = CONTEXT_MENU_ITEM_DELETE+1;
    private static final int CONTEXT_MENU_ITEM_CALC = CONTEXT_MENU_ITEM_SHOW+1;
    ArrayList<Bean> list=new ArrayList<Bean>();
    MyAdapters adapter;
    public FunctionFragment() { }
    public static FunctionFragment newInstance(String param) {
        FunctionFragment fragment = new FunctionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_function,container,false);
        initView(view);
        Button button=view.findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cache.getF().clear();
                for(int i=0;i<list.size();++i){
                    String str=list.get(i).getName();
                    if(str!=null)Cache.add(str);
                }
            }
        });
        return view;
    }
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Operations");
        menu.add(1,CONTEXT_MENU_ITEM_NEW,1,"add");
        menu.add(1,CONTEXT_MENU_ITEM_DELETE,1,"del");
        menu.add(1,CONTEXT_MENU_ITEM_SHOW,1,"show");
        menu.add(1,CONTEXT_MENU_ITEM_CALC,1,"calc");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position=menuInfo.position;
        switch (item.getItemId()) {
            case CONTEXT_MENU_ITEM_NEW:
                list.add(new Bean());
                adapter.notifyDataSetChanged();
                break;
            case CONTEXT_MENU_ITEM_DELETE:
                if(list.size()>1)list.remove(list.size()-1);
                adapter.notifyDataSetChanged();
                break;
            case CONTEXT_MENU_ITEM_SHOW:
                Toast.makeText(this.getContext(),""+list.get(position).getName(),Toast.LENGTH_SHORT).show();
                break;
            case CONTEXT_MENU_ITEM_CALC:
                String res=""+Calc.calc(list.get(position).getName());
                if(res.equals("2333333"))res="非法表达式";
                else res="Result:"+res;
                Toast toast=Toast.makeText(this.getContext(),res,Toast.LENGTH_SHORT);
                showMyToast(toast,11*1000);
        }
        return super.onContextItemSelected(item);
    }
    private void initView(View view) {
        list.add(new Bean());
        adapter = new MyAdapters(this.getContext(),list);
        ListView listView=view.findViewById(R.id.lv_main);
        listView.setAdapter(adapter);
        this.registerForContextMenu(listView);
    }
    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() { toast.show(); }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
}